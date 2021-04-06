package com.fy.engineserver.activity.dailyTurnActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.dailyTurnActivity.model.DailyTurnActivity;
import com.fy.engineserver.activity.dailyTurnActivity.model.DiskFileModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.PlayerRewardInfo;
import com.fy.engineserver.activity.dailyTurnActivity.model.RewardArticleModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel4Client;
import com.fy.engineserver.activity.treasure.TreasureActivityManager;
import com.fy.engineserver.activity.treasure.model.BaseArticleModel;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_ConfirmBuyDailyTimes;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class DailyTurnManager {
	public static Logger logger = ActivitySubSystem.logger;
	
	public static DailyTurnManager instance;
	/** excel配表路径 */
	private String fileName;
	/** discache文件目录 (存储玩家当日抽取到的物品) */
	private String diskFileName;
	/**  转盘模板   key=转盘id */
	public Map<Integer, TurnModel> turnMaps = new LinkedHashMap<Integer, TurnModel>();
	/** 转盘模板  key=转盘名 */
	public Map<String, TurnModel> turnNameMaps = new LinkedHashMap<String, TurnModel>();
	/** 奖励物品模板  key=物品id */
	public Map<Integer, RewardArticleModel> articleMaps = new HashMap<Integer, RewardArticleModel>();
	/** 相关提示  */
	public Map<String, String> translate = new HashMap<String, String>();
	
	public int openLevelLimit = 10;
	
	public DiskCache disk = null;
	
	public static final int 未获取 = 0;
	public static final int 获取未用 = 1;
	public static final int 获取已用 = 2;
	public static final int 可购买 = 3;
	public static final int 需要充值 = 4;
	
	public void init() throws Exception {
		instance = this;
		this.initFile();
		this.initDisk();
	}
	/***
	 * 活动是否开启
	 * @return
	 */
	public boolean isDailyTurnOpen () {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.dailyTurnActivity);
		if (cr != null) {
			return cr.getBooleanValue();
		}
		return false;
	}
	/**
	 * 获取当日充值金额
	 * @param player
	 * @return		单位：分
	 */
	public long getCycleChongzhi(Player player) {
		try {
			if (player.getOne_day_rmb() < 0) {
				Calendar cd = Calendar.getInstance();
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				long startTime = cd.getTimeInMillis();
				long endTime = System.currentTimeMillis();
				long rmbs = BossClientService.getInstance().getSavingAmount4Player(player.getId(), startTime, endTime);
				player.setOne_day_rmb(rmbs);
			}
			return player.getOne_day_rmb();
		} catch (Exception e) {
			logger.warn("[getCycleChongzhi] [异常] [" + player.getLogString() + "]", e);
		}
		return 0;
	}
	/**
	 * 购买额外抽奖次数
	 * @param player
	 * @param turnType
	 */
	public void buyExtraTimes(Player player, int turnType, boolean confirm) {
		long cycleChongZhi = this.getCycleChongzhi(player);
		TurnModel tm = turnMaps.get(turnType);
		if (tm != null) {
			if (player.getLevel() < tm.getLevelLimit()) {
				player.sendError(Translate.等级不符);
				return ;
			}
			if (cycleChongZhi < tm.getChongzhiLimit()) {
				player.sendError(String.format(Translate.需要充值XX才可购买, tm.getChongzhiLimit() / 100));
				return ;
			}
			if (tm.getCostSiliverNum() <= 0) {
				player.sendError("不可购买");
				return ;
			}
			long now = System.currentTimeMillis();
			synchronized (player) {
				DiskFileModel model = getDiskFileModel(player, now);
				model.reset(now, player);
				PlayerRewardInfo pri = model.getRewardInfo().get(turnType);
				if (pri == null) {
					pri = new PlayerRewardInfo(tm.getTurnId(), tm.getTurnName());
				}
				if (pri.getPurchaseTimes() >= tm.getCycleMaxBuy()) {
					player.sendError("今天已经购买过，请明天再来吧。");
					return;
				}
				try {
					long cost = tm.getCostSiliverNum();
					if (cost <= 0) {
						player.sendError(Translate.异常);
						return ;
					}
					if (!confirm) {
						this.popConfirmWindow(player, turnType, cost);
						return ;
					}
					if (player.getShopSilver() + player.getSilver() < tm.getCostSiliverNum()) {
						player.sendError(Translate.元宝不足);
						return ;
					}
					BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, ExpenseReasonType.购买每日转盘次数, "购买每日转盘次数");
					player.sendError(String.format(Translate.扣除银子, BillingCenter.得到带单位的银两(cost)));
				} catch (NoEnoughMoneyException e) {
					// TODO Auto-generated catch block
					player.sendError(Translate.元宝不足);
					return ;
				} catch (BillFailedException e) {
					// TODO Auto-generated catch block
					player.sendError(Translate.元宝不足);
					return ;
				}
				pri.setPurchaseTimes(pri.getPurchaseTimes() + 1);
				model.getRewardInfo().put(turnType, pri);
				disk.put(player.getId(), model);
				if (logger.isWarnEnabled()) {
					logger.warn("[购买抽奖次数] [成功] [" + player.getLogString() + "] [turnType:" + turnType + "] [cycleChongZhi:" + cycleChongZhi + "]");
				}
			}
		}
	}
	
	private void popConfirmWindow(Player p, int turnType, long cost) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_ConfirmBuyDailyTimes option1 = new Option_ConfirmBuyDailyTimes();
		option1.setTurnType(turnType);
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.购买次数需要消耗银子;
		msg = String.format(msg, BillingCenter.得到带单位的银两(cost));
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	/**
	 * 获取转盘信息
	 * @param player
	 * @param turnId
	 * @return  客户端显示需要的数据
	 */
	public TurnModel4Client getTurnModel4Client(Player player, int turnId) {
		try {
			long now = System.currentTimeMillis();
			TurnModel tm = turnMaps.get(turnId);
			if (tm == null) {
				logger.warn("[getTurnModel4Client] [异常] [turnId:" + turnId + "] [" + player.getLogString() + "]");
				return null;
			}
			DiskFileModel dfk = this.getDiskFileModel(player, now);
			PlayerRewardInfo pri = dfk.getRewardInfo().get(turnId);
			long[] entityIds = new long[tm.getGoodIds().size()];
			int[] entityNums = new int[tm.getGoodIds().size()];
			int[] entityStatus = new int[tm.getGoodIds().size()];
			for (int i=0; i<tm.getGoodIds().size(); i++) {
				RewardArticleModel rm = articleMaps.get(tm.getGoodIds().get(i));
				entityIds[i] = rm.getEntityId();
				entityNums[i] = rm.getArticleNum();
				if (pri != null && pri.alreadHasGood(tm.getGoodIds().get(i))) {
					entityStatus[i] = 1;
				}
			}
			int[] conditionStatus = dfk.getConditionStatus(player, turnId);
			TurnModel4Client tc = new TurnModel4Client(tm.getTurnName(), tm.getTurnId(), entityIds, entityStatus, entityNums, 
					tm.getConditions(), conditionStatus, tm.getDescription(), dfk.getBtnStr(turnId));
			if (logger.isDebugEnabled()) {
				logger.debug("[getTurnModel4Client] [" + player.getLogString() + "] [" + tc + "]");
			}
			return tc;
		} catch (Exception e) {
			logger.warn("[getTurnModel4Client] [异常] [turnId:" + turnId + "] [" + player.getLogString() + "]", e);
		}
		return null;
	}
	
	public DiskFileModel getDiskFileModel(Player player, long now) {
		DiskFileModel model = (DiskFileModel) disk.get(player.getId());
		if (model == null) {
			model = new DiskFileModel();
			model.setPlayerId(player.getId());
			model.setUpdateTime(now);
		}
		return model;
	}
	
	
	/**
	 * 抽取奖励
	 * @return 获取到的物品的id
	 */
	public int startExtract(Player player, int turnType) {
		synchronized (player) {
			long now = System.currentTimeMillis();
			DiskFileModel model = getDiskFileModel(player, now);
			model.reset(now, player);
			String result = verificate(now, player, model, turnType);
			if (result != null) {
				player.sendError(result);
				return -1;
			}
			TurnModel tm = turnMaps.get(turnType);
			String rr = model.changeUseStatus(player, turnType);
			if (rr != null) {			//修改使用状态
				player.sendError(rr);
				return -1;
			}
			PlayerRewardInfo pri = model.getRewardInfo().get(turnType);
			List<Integer> rewardIds = pri.getRewardIds();
			List<Integer> articleIds = new ArrayList<Integer>();
			for (int i=0; i<tm.getGoodIds().size(); i++) {
				if (articleIds.contains(tm.getGoodIds().get(i)) || (rewardIds != null && rewardIds.contains(tm.getGoodIds().get(i)))) {
					continue;
				}
				articleIds.add(tm.getGoodIds().get(i));
			}
			RewardArticleModel ram = this.getRewardArticleModelByProb(player, articleIds);		
			if (ram != null) {		//得到玩家奖品信息，发奖 (确定直接到背包还是等转盘时间再进入背包)
				if (rewardIds == null) {
					rewardIds = new ArrayList<Integer>();
				}
				rewardIds.add(ram.getId());
				pri.setRewardIds(rewardIds);
				model.getRewardInfo().put(pri.getTurnType(), pri);
				disk.put(player.getId(), model);
				List<BaseArticleModel> rewardList = new ArrayList<BaseArticleModel>();
				rewardList.add(ram);
				TreasureActivityManager.instance.put2Bag(player, rewardList, "每日转盘活动", ArticleEntityManager.每日转盘活动, Translate.转盘发送邮件title, Translate.转盘发送邮件content);
				//真正给奖励。
				if (logger.isWarnEnabled()) {
					logger.warn("[抽取奖励] [成功] [转盘名:" + tm.getTurnName() + "] [奖励:" + ram + "] [" + player.getLogString() + "]");
				}
				return ram.getId();
			}
		}
		return -1;
	}
	/**
	 * 验证玩家是否可以参与抽奖
	 * @param player
	 * @return  null表示可以参与
	 */
	public String verificate(long now, Player player, DiskFileModel model, int turnType) {
		TurnModel tm = turnMaps.get(turnType);
		if (tm == null) {
			return "未找到对应转盘信息";
		}
		if (player.getLevel() < tm.getLevelLimit()) {
			return Translate.等级不符;
		}
		PlayerRewardInfo pri = model.getRewardInfo().get(turnType);
		int cyclePartTime = pri == null ? 0 : pri.getCyclePartTime();
		if (cyclePartTime >= tm.getMaxPlayTimes()) {
			return Translate.抽奖次数用尽;
		}
		
		return null;
	}
	
	/**
	 * 根据概率抽取玩家获得的奖励
	 * @param player
	 * @param articleIds  玩家当前列表中剩余物品
	 * @return		RewardArticleModel or  null
	 */
	public RewardArticleModel getRewardArticleModelByProb(Player player, List<Integer> articleIds) {
		int totalProb = 0;
		for (int i=0; i<articleIds.size(); i++) {
			RewardArticleModel rm = articleMaps.get(articleIds.get(i));
			if (rm != null) {
				totalProb += rm.getProb();
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[未获取到对应奖励物品信息] [id:" + articleIds.get(i) + "] [" + player.getLogString() + "]");
				}
			}
		}
		if (totalProb <= 0) {
			return null;
		}
		int tempProb = 0;
		int ran = player.random.nextInt(totalProb);
		for (int i=0; i<articleIds.size(); i++) {
			RewardArticleModel rm = articleMaps.get(articleIds.get(i));
			if (rm != null) {
				tempProb += rm.getProb();
				if (tempProb >= ran) {
					return rm;
				}
			}
		}
		if (logger.isWarnEnabled()) {
			logger.warn("[异常] [没有随机到奖励] [totalProb:" + totalProb + "] [ran:" + ran + "] ["+ player.getLogString() + "]");
		}
		return null;
	}
	
	private void initDisk() throws IOException {
		File file = new File(diskFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		disk = new DefaultDiskCache(file, null, "dailyTurn", 100L * 365 * 24 * 3600 * 1000L);
	}
	
	private void initFile() throws Exception {
		File f = new File(fileName);
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if (!f.exists()) {
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		List<String> tempList = new ArrayList<String>();
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0); 			//基础配置
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				int index = 0;
				String startTime = ReadFileTool.getString(row, index++, logger);
				String endTime = ReadFileTool.getString(row, index++, logger);
				String platForms = ReadFileTool.getString(row, index++, logger);
				String openServerNames = ReadFileTool.getString(row, index++, logger);
				String notOpenServers = ReadFileTool.getString(row, index++, logger);
				DailyTurnActivity activity = new DailyTurnActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
				AllActivityManager.instance.add2AllActMap(AllActivityManager.dailyTurnActivity, activity);
			} catch (Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}
		
		sheet = workbook.getSheetAt(1); // 基础配置2
		rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			try {
				TurnModel tm = this.getTurnModel(row);
				turnMaps.put(tm.getTurnId(), tm);
				turnNameMaps.put(tm.getTurnName(), tm);
				if (openLevelLimit > tm.getLevelLimit()) {
					openLevelLimit = tm.getLevelLimit();
				}
//				System.out.println(tm);
			} catch(Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}
		
		for (int i=0; i<turnNameMaps.size(); i++) {
			sheet = workbook.getSheetAt(i+2); 
			if (sheet == null) {
				throw new Exception("[转盘名与sheet页不匹配]");
			}
			String sheetName = sheet.getSheetName();
			List<Integer> goods = new ArrayList<Integer>();
			TurnModel tm = turnNameMaps.get(sheetName);
			if (tm == null) {
				throw new Exception("[没有找到对应的转盘配置] [sheetName:" + sheetName + "] [" + turnNameMaps.keySet() + "]");
			} 
			for (int j=1; j<sheet.getPhysicalNumberOfRows(); j++) {
				HSSFRow row = sheet.getRow(j);
				int index = 0;
				RewardArticleModel ram = new RewardArticleModel();
				ram.setId(ReadFileTool.getInt(row, index++, logger));
				ram.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
				ram.setColorType(ReadFileTool.getInt(row, index++, logger));
				ram.setArticleNum(ReadFileTool.getInt(row, index++, logger));
				ram.setProb(ReadFileTool.getInt(row, index++, logger));
				ram.setBind(ReadFileTool.getBoolean(row, index++, logger));
				Article a = ArticleManager.getInstance().getArticleByCNname(ram.getArticleCNNName());
//				Article a =null;
				if (a != null) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, ram.isBind(), ArticleEntityManager.活动奖励临时物品, 
							null, ram.getColorType());
					ram.setEntityId(ae.getId());
				} else  {
					tempList.add(ram.getArticleCNNName());
				}
				goods.add(ram.getId());
				articleMaps.put(ram.getId(), ram);
			}
			tm.setGoodIds(goods);
			turnNameMaps.put(tm.getTurnName(), tm);
			turnMaps.put(tm.getTurnId(), tm);
//			System.out.println(tm);
		}
		if (tempList.size() > 0) {
			throw new Exception("[物品不存在] [" + tempList + "]");
		}
	}
	
	private TurnModel getTurnModel(HSSFRow row) {
		TurnModel tm = new TurnModel();
		int index = 0 ;
		tm.setTurnId(ReadFileTool.getInt(row, index++, logger));
		tm.setTurnName(ReadFileTool.getString(row, index++, logger));
		tm.setLevelLimit(ReadFileTool.getInt(row, index++, logger));
		tm.setMaxPlayTimes(ReadFileTool.getInt(row, index++, logger));
		index++;
		tm.setVipLimit(ReadFileTool.getInt(row, index++, logger));
		tm.setChongzhiLimit(ReadFileTool.getLong(row, index++, logger, 0L) * 100);
		tm.setCostSiliverNum(ReadFileTool.getLong(row, index++, logger, 0L) * 1000);
		tm.setDescription(ReadFileTool.getString(row, index++, logger));
		String str = ReadFileTool.getString(row, index++, logger);
		tm.setConditions(str.split("\\|"));
		return tm;
	}
	public static void main(String[] args) throws Exception {
		DailyTurnManager manager = new DailyTurnManager();
		manager.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//dailyturnActivity.xls");
		manager.initFile();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDiskFileName() {
		return diskFileName;
	}

	public void setDiskFileName(String diskFileName) {
		this.diskFileName = diskFileName;
	}

}
