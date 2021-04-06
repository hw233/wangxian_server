package com.fy.engineserver.activity.treasure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.treasure.instance.TreasureActivity;
import com.fy.engineserver.activity.treasure.model.BaseArticleModel;
import com.fy.engineserver.activity.treasure.model.TreasureArticleModel;
import com.fy.engineserver.activity.treasure.model.TreasureModel;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
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
import com.fy.engineserver.menu.activity.Option_Open_ConfirmDigTreasure;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.minigame.MinigameConstant;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 极地寻宝活动
 */
public class TreasureActivityManager {
	public static TreasureActivityManager instance;
	
	public static Logger logger = ActivitySubSystem.logger;
	/** 配表路径 */
	private String fileName;
	/** discache文件路径 */
	private String diskFileName;
	/** 寻宝物品map   key=物品id */
	public Map<Integer, TreasureArticleModel> articleModels = new HashMap<Integer, TreasureArticleModel>();
	/** 寻宝map  key=宝藏类型 */
	public Map<Integer, TreasureModel> treasureModels = new LinkedHashMap<Integer, TreasureModel>();
	/** 转盘模板  key=宝藏名 */
	public Map<String, TreasureModel> treasureNameMaps = new LinkedHashMap<String, TreasureModel>();
	/** disk   key=playerId */
	public DiskCache disk = null;
	
	public int openLevelLimit = 10;
	
	public void init() throws Exception {
		instance = this;
		this.initFile();
		this.initDisk();
	}
	
	public void initFile () throws Exception {
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
				TreasureActivity activity = new TreasureActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
				AllActivityManager.instance.add2AllActMap(AllActivityManager.treasureActivity, activity);
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
				TreasureModel tm = this.getTreasureModel(row);
				treasureModels.put(tm.getType(), tm);
				treasureNameMaps.put(tm.getName(), tm);
				if (openLevelLimit > tm.getLevelLimit()) {
					openLevelLimit = tm.getLevelLimit();
				}
			} catch(Exception e) {
				throw new Exception("[" + fileName + "] [" + sheet.getSheetName() + "] [" + "第" + (i + 1) + "行异常！！]", e);
			}
		}
		
		for (int i=0; i<treasureNameMaps.size(); i++) {
			sheet = workbook.getSheetAt(i+2); 
			if (sheet == null) {
				throw new Exception("[转盘名与sheet页不匹配]");
			}
			String sheetName = sheet.getSheetName();
			List<Integer> goods = new ArrayList<Integer>();
			TreasureModel tm = treasureNameMaps.get(sheetName);
			if (tm == null) {
				throw new Exception("[没有找到对应的配置] [sheetName:" + sheetName + "] [" + treasureNameMaps.keySet() + "]");
			} 
			for (int j=1; j<sheet.getPhysicalNumberOfRows(); j++) {
				HSSFRow row = sheet.getRow(j);
				int index = 0;
				TreasureArticleModel tam = new TreasureArticleModel();
				tam.setId(ReadFileTool.getInt(row, index++, logger));
				tam.setArticleCNNName(ReadFileTool.getString(row, index++, logger));
				tam.setColorType(ReadFileTool.getInt(row, index++, logger));
				tam.setArticleNum(ReadFileTool.getInt(row, index++, logger));
				tam.setBind(ReadFileTool.getBoolean(row, index++, logger));
				tam.setProblem(ReadFileTool.getInt(row, index++, logger));
				Article a = ArticleManager.getInstance().getArticleByCNname(tam.getArticleCNNName());
				if (a != null) {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createTempEntity(a, tam.isBind(), ArticleEntityManager.活动奖励临时物品, 
							null, tam.getColorType());
					tam.setTempEntityId(ae.getId());
				} else  {
					tempList.add(tam.getArticleCNNName());
				}
				goods.add(tam.getId());
				articleModels.put(tam.getId(), tam);
			}
			tm.setGoodIds(goods);
			treasureNameMaps.put(tm.getName(), tm);
			treasureModels.put(tm.getType(), tm);
		}
		
		if (tempList.size() > 0) {
			throw new Exception("[物品不存在] [" + tempList + "]");
		}
	}
	
	private TreasureModel getTreasureModel(HSSFRow row) throws Exception {
		int index = 0 ;
		TreasureModel tm = new TreasureModel();
		tm.setType(ReadFileTool.getInt(row, index++, logger));
		tm.setIsopen(ReadFileTool.getBoolean(row, index++, logger));
		tm.setName(ReadFileTool.getString(row, index++, logger));
		tm.setLevelLimit(ReadFileTool.getInt(row, index++, logger));
		int[] digTimes = ReadFileTool.getIntArrByString(row, index++, logger, ",");
		for (int i=0; i<digTimes.length; i++) {
			tm.getDigTimes().add(digTimes[i]);
		}
		long[] costs = ReadFileTool.getLongArrByString(row, index++, logger, ",", 1000);
		for (int i=0; i<costs.length; i++) {
			tm.getCostS().add(costs[i]);
		}
		String articles = ReadFileTool.getString(row, index++, logger);
		if (articles != null && !articles.isEmpty()) {
			String[] ats = articles.split(",");
			for (int i=0; i<ats.length; i++) {
				tm.getCostArticles().add(ats[i]);
			}
		}
		int[] showIds = ReadFileTool.getIntArrByString(row, index++, logger, ",");
		for (int i=0; i<showIds.length; i++) {
			tm.getNeedShowGoods().add(showIds[i]);
		}
		tm.setDescription(ReadFileTool.getString(row, index++, logger));
		if (digTimes.length != costs.length) {
			throw new Exception("[配置错误] [" + tm.getCostS() + "] [" + tm.getDigTimes() + "]");
		}
		return tm;
	}
	
	public void initDisk () throws IOException {
		File file = new File(diskFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		disk = new DefaultDiskCache(file, null, "TreasureActivityManager", 100L * 365 * 24 * 3600 * 1000L);
	}
	/**
	 * 获取离寻宝或动结束还有多久
	 * @return  毫秒数
	 */
	public long getActivityLeftTime () {
		long result = 0;			//返回给客户端的剩余时间
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.treasureActivity);
		if (cr != null && cr.getBooleanValue()) {
			long now = System.currentTimeMillis();
			result = cr.getLongValue() - now;
			if (result > 0) {
				return result;
			}
		}
		return 0L;
	}
	
	public boolean isActivityOpen() {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.treasureActivity);
		if (cr != null) {
			return cr.getBooleanValue();
		}
		return false;
	}
	/**
	 * 寻宝
	 * @param player
	 * @param treasureId
	 * @param digTimes
	 * @return
	 */
	public long[] digTreasure(Player player, int treasureId, int digTimes, boolean confirm) {
		synchronized (player) {
			String result = this.cost4Treasure(player, treasureId, digTimes, confirm);		//扣除道具或者银子
			if (result != null) {
				if (result.startsWith("确认_")) {
					this.popConfirmWindow(player, treasureId, Long.parseLong(result.split("_")[1]), digTimes);
					return null;
				}
				player.sendError(result);
				return null;
			}
			long[] resultIds = new long[digTimes];
			List<BaseArticleModel> props = new ArrayList<BaseArticleModel>();
			for (int i=0; i<digTimes; i++) {
				TreasureArticleModel tm = this.getResultTreasure(player, treasureId);
				resultIds[i] = tm.getTempEntityId();
				props.add(tm);
			}
			this.put2Bag(player, props, "极地寻宝获得", ArticleEntityManager.极地寻宝活动获得, Translate.极地寻宝活动, Translate.极地寻宝活动邮件内容);
			if (logger.isWarnEnabled()) {
				logger.warn("[类型:极地寻宝活动发送奖励] [结果:成功] [" + player.getLogString() + "] [treasureId:" + treasureId + "] [digTimes:" + digTimes + "]"
						+ "[奖励物品:" + this.getRewardInfo(props) + "]");
			}
			return resultIds;
		}
	}
	
	private void popConfirmWindow(Player p, int treasureId, long cost, int digTime) {
		WindowManager wm = WindowManager.getInstance();
		MenuWindow mw = wm.createTempMenuWindow(600);
		Option_Open_ConfirmDigTreasure option1 = new Option_Open_ConfirmDigTreasure();
		option1.setTreasureId(treasureId);
		option1.setDigTime(digTime);
		option1.setText(MinigameConstant.CONFIRM);
		Option_Cancel option2 = new Option_Cancel();
		option2.setText(MinigameConstant.CANCLE);
		Option[] options = new Option[] {option1, option2};
		mw.setOptions(options);
		String msg = Translate.需要消耗银子是否确定;
		msg = String.format(msg, BillingCenter.得到带单位的银两(cost));
		mw.setDescriptionInUUB(msg);
		CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), options);
		p.addMessageToRightBag(creq);
	}
	
	private String getRewardInfo (List<BaseArticleModel> props) {
		try {
			StringBuffer sb = new StringBuffer();
			for (BaseArticleModel am : props) {
				sb.append(am.getArticleCNNName()).append(",").append(am.getColorType()).append(",").append(am.getArticleNum());
				sb.append(";");
			}
			return sb.toString();
		} catch (Exception e) {
			logger.warn("[异常]", e);
		}
		return "";
	}
	
	public void put2Bag(Player p, List<BaseArticleModel> props, String putReason, int createReason, String mailTitle, String mailContant) {
		Knapsack bag = p.getKnapsack_common();
		ActivityProp[] mailProps = new ActivityProp[0];
		for (int i=0; i<props.size(); i++) {
			BaseArticleModel tm = props.get(i);
			if (bag.getEmptyNum() > 0) {
				Article a = ArticleManager.getInstance().getArticleByCNname(tm.getArticleCNNName());
				try {
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, tm.isBind(), createReason, p, tm.getColorType(),
							tm.getArticleNum(), true);
					for (int j=0; j<tm.getArticleNum(); j++) {
						if (!bag.put(ae, putReason)) {
							ActivityProp ap = new ActivityProp(tm.getArticleCNNName(), tm.getColorType(), 1, tm.isBind());
							mailProps = Arrays.copyOf(mailProps, mailProps.length+1);
							mailProps[mailProps.length-1] = ap;
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.warn("["+this.getClass().getSimpleName()+"] [put2Bag] [异常] [" + p.getLogString() + "]", e);
				}
			} else {			//没有空格子了，发邮件
				ActivityProp ap = new ActivityProp(tm.getArticleCNNName(), tm.getColorType(), tm.getArticleNum(), tm.isBind());
				mailProps = Arrays.copyOf(mailProps, mailProps.length+1);
				mailProps[mailProps.length-1] = ap;
			}
		}
		if (mailProps.length > 0) {
			List<Player> players = new ArrayList<Player>();
			players.add(p);
			ActivityManager.sendMailForActivity(players, mailProps, mailTitle, mailContant, putReason);
		}
	}
	
	/**
	 * 
	 * @param player
	 * @param treasureType
	 * @param costType  抽奖几次
	 */
	public String cost4Treasure(Player player, int treasureType, int costType, boolean confirm) {
		try {
			TreasureModel tm = treasureModels.get(treasureType);
			if (tm == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("[cost4Treasure] [异常] [tm == null] [" + player.getLogString() + "] [treasureType:" + treasureType + "] [costType:" + costType + "]");
					return Translate.异常;
				}
			}
			if (!tm.isIsopen()) {
				return Translate.暂未开放敬请期待;
			}
			if (player.getLevel() < tm.getLevelLimit()) {
				return Translate.等级不符;
			}
			String costArticle = tm.getCostArticles(costType);
			boolean removeResult = false;
			if (costArticle != null && !costArticle.isEmpty()) {
				Article a = ArticleManager.getInstance().getArticleByCNname(costArticle);
				Knapsack bag = player.getKnapsack_common();
				if (a != null && bag.countArticle(a.getName()) > 0) {
					removeResult = player.removeArticle(a.getName(),"极地寻宝扣除");
				}
			}
			if (!removeResult) {			//需要扣钱
				long cost = tm.getCostSiliver(costType);
				if (cost <= 0 || (player.getShopSilver() + player.getSilver()) < cost) {
					return "您的银子不足,需要消耗:"+BillingCenter.得到带单位的银两(cost);
				}
				if (!confirm) {
					return "确认_" + cost;
				}
				try {
					BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.SHOPYINZI, 
							ExpenseReasonType.极地寻宝抽奖, "极地寻宝抽奖");
					player.sendError(String.format(Translate.扣除银子, BillingCenter.得到带单位的银两(cost)));
				} catch (NoEnoughMoneyException e1) {
					return Translate.元宝不足;
				} catch (BillFailedException e2) {
					return Translate.元宝不足;
				}
			}
			return null;
		} catch (Exception e) {
			logger.warn("[cost4Treasure] [异常] [" + player.getLogString() + "] [treasureType:" + treasureType + "] [costType:" + costType + "]", e);
			return Translate.异常;
		}
	}
	
	/**
	 * 随机获取到玩家此次抽奖的物品信息
	 * @param player
	 * @param treasureType
	 * @return
	 */
	public TreasureArticleModel getResultTreasure(Player player, int treasureType) {
		TreasureModel tm = treasureModels.get(treasureType);
		if (tm == null) {
			if (logger.isWarnEnabled()) {
				logger.warn("[没有获取到宝藏信息] [type:" + treasureType + "] [" + player.getLogString() + "]");
			}
		} else  {
			int totalProb = 0;
			for (int id : tm.getGoodIds()) {
				TreasureArticleModel tam = articleModels.get(id);
				if (tam == null) {
					if (logger.isWarnEnabled()) {
						logger.warn("[没有获取到对应物品信息] [id:" + id + "] [treasureType:"+treasureType+"] [" + player.getLogString() + "]");
					}
				} else {
					totalProb += tam.getProblem();
				}
			}
			if (totalProb > 0) {
				int ran = player.random.nextInt(totalProb);
				int tempProb = 0;
				for (int id : tm.getGoodIds()) {
					TreasureArticleModel tam = articleModels.get(id);
					if (tam != null) {
						tempProb += tam.getProblem();
						if (ran <= tempProb) {
							return tam;
						}
					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[没有随机到物品] [getResultTreasure] [treasureType:" + treasureType + "] [" + player.getLogString() + "]");
			}
		}
		return null;
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
