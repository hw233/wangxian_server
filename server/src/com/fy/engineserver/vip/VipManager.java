package com.fy.engineserver.vip;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.shop.ActivityPropHasRate;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.NPCFlushAgent;
import com.fy.engineserver.core.NPCFlushAgent.BornPoint;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.VIP_LOTTERY_OPEN_REQ;
import com.fy.engineserver.message.VIP_PULL_FRIEND_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.fy.engineserver.vip.data.VIPData;
import com.fy.engineserver.vip.data.VIPData2;
import com.fy.engineserver.vip.data.VipConf;
import com.fy.engineserver.vip.vipinfo.VipInfoRecordManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.ResponseMessage;

public class VipManager implements MConsole {
	public static String title4pull = "pull";
	public static String body4pull = "body";
	public static String timeLimit = "timeLimit";
	public static Map<Long, OptionVipPullAgree> requestMap = new HashMap<Long, OptionVipPullAgree>();

	private static VipManager self;
	@ChangeAble("是否是测试授权")
	public static boolean isTest = false;

	private static String recordNpcName = "VIP特派使";
	private static String recordGame = "kunhuagucheng";
	public static int recordVipLevel = 6;
	public static int recordVipLevel2 = 2;
	
	public static String mailTitle = "VIP每月奖励";
	public static String mailContent = "恭喜您，成功领取本月VIP奖励，记得下月1号后继续到VIP特派使处，领取每月VIP奖励。";
	

	public static String shopName = "全部道具";

	private Map<Integer, int[]> recordNPCLocation = new HashMap<Integer, int[]>();

	/** 转盘游戏 */
	private LotteryGame lotteryGame;

	public static VipManager getInstance() {
		return self;
	}

	public VipManager() {

	}

	public static Logger logger = LoggerFactory.getLogger(VipManager.class.getName());
	ArticleEntityManager aem;
	ArticleManager am;

	public ArticleEntityManager getAem() {
		return aem;
	}

	public void setAem(ArticleEntityManager aem) {
		this.aem = aem;
	}

	public ArticleManager getAm() {
		return am;
	}

	public void setAm(ArticleManager am) {
		this.am = am;
	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		self = this;
		load();
		initRecordInfo();
		MConsoleManager.register(getInstance());
		if(GameConstants.getInstance().getServerName().equals("客户端测试") || GameConstants.getInstance().getServerName().equals("飘渺寻仙") ){
			VipInfoRecordManager.isOpenRecord = true;
			
		}
		ServiceStartRecord.startLog(this);
	}

	private void initRecordInfo() {
		for (int i = 1; i <= 3; i++) {
			String displayName = GameManager.getInstance().getDisplayName(recordGame, i);
			Game game = GameManager.getInstance().getGameByDisplayName(displayName, i);
			NPCFlushAgent flushAgent = game.getNpcFlushAgent();
			if (flushAgent != null) {
				NPCTempalte tempalte = ((MemoryNPCManager) MemoryNPCManager.getNPCManager()).getNPCTempalteByCategoryName(recordNpcName);
				if (tempalte != null) {
					BornPoint[] bornPoints = flushAgent.getBornPoints4NpcCategoryId(tempalte.NPCCategoryId);
					if (bornPoints != null && bornPoints.length > 0) {
						int[] location = new int[2];
						location[0] = bornPoints[0].getX();
						location[1] = bornPoints[0].getY();
						recordNPCLocation.put(i, location);
						logger.warn("[国家:" + i + "] [NPC位置] [(" + location[0] + "," + location[1] + ")]");
					} else {
						logger.error("[NPC出生点错误] [" + recordNpcName + "]");
					}
				}
			}
		}
	}

	File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void load() throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			vipDatas = new VIPData[rows - 1];
			vipDatas2 = new VIPData2[rows - 1];
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				VIPData data = new VIPData();
				VIPData2 data2 = new VIPData2();
				try {
					String icon = row.getCell(0).getStringCellValue().trim();
					if (icon != null) {
						data.setVipIcon(icon);
						data2.setVipIcon(icon);
					}
				} catch (Exception ex) {
					Log.error(" load exception ", ex);
				}
				data.setVipLevel(i);
				data2.setVipLevel(i);
				data.setNeedCost((int) row.getCell(1).getNumericCellValue());
				data2.setNeedCost((long) row.getCell(1).getNumericCellValue());
				data.setArticleName((row.getCell(2).getStringCellValue().trim()));
				data2.setArticleName((row.getCell(2).getStringCellValue().trim()));
				data.setDescription((row.getCell(3).getStringCellValue()));
				data2.setDescription((row.getCell(3).getStringCellValue()));

				// 创建临时物品
				if (am != null) {// 支持测试载入文件。
					Article a = am.getArticle(data.getArticleName());
					if (a == null) {
						logger.error("没有找到道具 {}", data.getArticleName());
					} else {
						ArticleEntity ae = aem.createTempEntity(a, true, ArticleEntityManager.CREATE_REASON_VIP, null, a.getColorType());
						data.setArticleId(ae.getId());
						data2.setArticleId(ae.getId());
					}
				}

				vipDatas[i - 1] = data;
				vipDatas2[i - 1] = data2;
			}
			loadVipConf(workbook.getSheetAt(3));
			loadTranslation(workbook.getSheetAt(4));
			loadLottery(workbook.getSheetAt(5));
			fixByPlatForm();
			is.close();
		}
	}

	/**
	 * 加载抽奖数据
	 * @param sheet
	 */
	private void loadLottery(HSSFSheet sheet) {
		lotteryGame = new LotteryGame();
		int rows = sheet.getPhysicalNumberOfRows();
		int rowNum = 1;
		lotteryGame.setFixedGiven(getArticleProperyAtrow(sheet.getRow(rowNum++)));
		if (logger.isInfoEnabled()) {
			logger.info("[系统启动] [vip转盘奖励] [固定奖励] " + lotteryGame.getFixedGiven().toString());
		}

		List<ActivityPropHasRate> randomGiven = new ArrayList<ActivityPropHasRate>();
		for (; rowNum < rows; rowNum++) {
			ActivityPropHasRate articleProperyAtrow = getArticleProperyAtrow(sheet.getRow(rowNum));
			randomGiven.add(articleProperyAtrow);
			if (logger.isInfoEnabled()) {
				logger.info("[系统启动] [vip转盘奖励] [随机奖励] [" + rowNum + "] " + articleProperyAtrow.toString());
			}
		}
		lotteryGame.setRandomGiven(randomGiven);
		lotteryGame.init();
	}

	/**
	 * 加载抽奖数据,生成lotteryGame对象
	 * @param row
	 * @return
	 */
	private ActivityPropHasRate getArticleProperyAtrow(HSSFRow row) {
		ActivityPropHasRate property = new ActivityPropHasRate();
		int index = 1;
		property.setArticleCNName(StringTool.getCellValue(row.getCell(index++), String.class));
		property.setArticleNum(StringTool.getCellValue(row.getCell(index++), Integer.class));
		property.setArticleColor(StringTool.getCellValue(row.getCell(index++), Integer.class));
		property.setBind(StringTool.getCellValue(row.getCell(index++), Boolean.class));
		property.setWight(StringTool.getCellValue(row.getCell(index++), Integer.class));
		return property;
	}

	private void loadTranslation(HSSFSheet sheet) {
		int rows = sheet.getPhysicalNumberOfRows();
		for (int r = 0; r < rows; r++) {
			HSSFRow row = sheet.getRow(r);
			String key = PetGrade.getString(row, 0, logger);
			String v = PetGrade.getString(row, 1, logger);
			if (v == null) {
			} else if ("title".equals(key)) {
				title4pull = v;
			} else if ("body".equals(key)) {
				body4pull = v;
			} else if ("timeLimit".equals(key)) {
				timeLimit = v;
			}
		}
	}

	public void loadVipConf(HSSFSheet sheet) {
		int rows = sheet.getPhysicalNumberOfRows();
		VipConf[] arr = new VipConf[rows - 1];
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			VipConf c = new VipConf();
			c.level = i - 1;
			c.vipMiniGameTimes = PetGrade.getInt(row, 1, logger);
			c.vipJinJieRenWuTimes = PetGrade.getInt(row, 2, logger);
			c.vip使用酒的次数 = PetGrade.getInt(row, 3, logger);
			c.vip使用封魔录的次数 = PetGrade.getInt(row, 4, logger);
			c.vip使用祈福的次数 = PetGrade.getInt(row, 5, logger);
			c.vip爬千层塔的百分比double = row.getCell(6).getNumericCellValue();
			c.vip进入交费地图时间提升百分比double = row.getCell(7).getNumericCellValue();
			c.vip每天可以多使用的绑银数 = PetGrade.getInt(row, 8, logger);
			arr[c.level] = c;
		}
		VipConf.conf = arr;
	}

	public static final int 每分钱对应的银子 = 500;
	public static final int TYPE_VIP_1 = 1;
	public static final int TYPE_VIP_2 = 2;
	public static final int TYPE_VIP_3 = 3;
	public static final int TYPE_VIP_4 = 4;
	public static final int TYPE_VIP_5 = 5;
	public static final int TYPE_VIP_6 = 6;
	public static final int TYPE_VIP_7 = 7;
	public static final int TYPE_VIP_8 = 8;
	public static final int TYPE_VIP_9 = 9;
	public static final int TYPE_VIP_10 = 10;
	public static final int TYPE_绿_11 = 11;
	public static final int TYPE_蓝_12 = 12;
	public static final int TYPE_紫_13 = 13;
	public static final int TYPE_橙_14 = 14;
	public static final int TYPE_皇冠_15 = 15;
	public static final String 酒的分类名 = Translate.酒;
	public static final String 封魔录的分类名 = Translate.封魔录;
	public static int openPullOtherLevel = TYPE_绿_11;
	// 下面这些都包括不是vip的情况
	// ////////////////////////////////////////////// 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
	// public static int[] vip使用酒的次数 = new int[] { 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 5 };
	// public static int[] vip使用封魔录的次数 = new int[] { 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 5 };
	// public static int[] vip使用祈福的次数 = new int[] { 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 5, 5 };
	// public static double[] vip爬千层塔的百分比 = new double[] { 1, 1, 1, 1, 1, 1.5, 2, 2.5, 2.5, 2.5, 2.5, 2.5, 2.5 };
	// public static double[] vip进入交费地图时间提升百分比 = new double[] { 1, 1, 1, 1, 1, 1.1, 1.1, 1.5, 1.5, 1.5, 1.5, 1.5, 1.5 };
	// public static int[] vip每天可以多使用的绑银数 = new int[] { 0, 0, 200000, 200000, 500000, 500000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000 };
	// public static boolean[] vip是否开启幽冥幻域刷新 = new boolean[] { false, false, false, false, false, true, true, true, true, true, true, true, true };
	public static int vipOpenTongTianTaShuaXin = 5;
	// public static boolean[] vip是否开启中级炼妖 = new boolean[] { false, false, false, true, true, true, true, true, true, true, true, true, true };
	public static int vipOpenZhongJiLianYao = 3;
	// public static boolean[] vip是否开启高级炼妖 = new boolean[] { false, false, false, false, false, true, true, true, true, true, true, true, true };
	public static int vipOpenGaoJiLianYao = 5;
	// public static boolean[] vip是否开启庄园加速 = new boolean[] { false, false, false, false, true, true, true, true, true, true, true, true, true };
	public static int vipOpenZhuangYuanJiaSu = 4;
	// public static boolean[] vip是否开启境界任务刷新 = new boolean[] { false, false, false, false, true, true, true, true, true, true, true, true, true };
	public static int vipOpenJinJieRenWuShuaXin = 4;
	// public static boolean[] vip是否开启用元宝购买贡献度 = new boolean[] { false, false, false, false, false, true, true, true, true, true, true, true, true };
	public static int vipOpenYuanBaoMaiGongXiang = 5;
	// public static boolean[] vip是否开启随身修理 = new boolean[] { false, true, true, true, true, true, true, true, true, true, true, true, true };
	public static int vipOpenXiuLi = 1;
	// public static boolean[] vip是否开启随身邮件 = new boolean[] { false, false, true, true, true, true, true, true, true, true, true, true, true };
	public static int vipOpenMail = 2;
	// public static boolean[] vip是否开启随身仓库 = new boolean[] { false, false, false, true, true, true, true, true, true, true, true, true, true };
	public static int vip开启随身仓库 = 3;
	// public static boolean[] vip是否开启随身求购 = new boolean[] { false, false, false, false, true, true, true, true, true, true, true, true, true };
	public static int vip开启随身求购 = 4;
	public static int vipCallFriendAtLv = TYPE_绿_11;
	public static VIPData[] vipDatas;
	public static VIPData2[] vipDatas2;

	public void fixByPlatForm() {
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			int[] vip每天可以多使用的绑银数 = new int[] { 0, 0, 0, 0, 200000, 500000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000 };
			for (int i = 0; i < vip每天可以多使用的绑银数.length; i++) {
				VipConf.conf[i].vip每天可以多使用的绑银数 = vip每天可以多使用的绑银数[i];
			}
			// vip是否开启随身修理 = new boolean[] { false, false, false, true, true, true, true, true, true, true, true, true, true };
			vipOpenXiuLi = 3;
			// vip是否开启随身仓库 = new boolean[] { false, false, false, false, false, true, true, true, true, true, true, true, true };
			vip开启随身仓库 = 5;
		}
	}

	public int vip每日增加的道具使用次数(Player player, String 二级分类) {
		if (酒的分类名.equals(二级分类)) {
			return VipConf.conf[getPlayerVipLevel(player, false)].vip使用酒的次数;
		}
		if ("封魔录".equals(二级分类)) {
			return VipConf.conf[getPlayerVipLevel(player, false)].vip使用封魔录的次数;
		}
		return 0;
	}

	public int vip每日增加的祈福使用次数(Player player) {
		return VipConf.conf[getPlayerVipLevel(player, false)].vip使用祈福的次数;
	}

	public double vip爬千层塔的百分比(Player player) {
		return VipConf.conf[getPlayerVipLevel(player, false)].vip爬千层塔的百分比double;
	}
	
	public double vip付费地图时间的百分比(Player player) {
		return VipConf.conf[getPlayerVipLevel(player, false)].vip进入交费地图时间提升百分比double;
	}

	public boolean vip是否开启幽冥幻域刷新(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenTongTianTaShuaXin;
	}

	public int vip每天可以多使用的绑银数(Player player) {
		return VipConf.conf[getPlayerVipLevel(player, false)].vip每天可以多使用的绑银数;
	}

	public boolean vip是否开启中级炼妖(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenZhongJiLianYao;
	}

	public boolean vip是否开启高级炼妖(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenGaoJiLianYao;
	}

	public int vip开启中级炼妖级别() {
		return vipOpenZhongJiLianYao;
	}

	public int vip开启高级炼妖级别() {
		return vipOpenGaoJiLianYao;
	}

	public boolean vip是否开启庄园加速(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenZhuangYuanJiaSu;
	}

	public boolean vip是否开启境界任务刷新(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenJinJieRenWuShuaXin;
	}

	public boolean vip是否开启用元宝购买贡献度(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenYuanBaoMaiGongXiang;
	}

	public boolean vip是否开启随身修理(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenXiuLi;
	}

	public boolean vip是否开启随身邮件(Player player) {
		return getPlayerVipLevel(player, false) >= vipOpenMail;
	}

	public boolean vip是否开启随身仓库(Player player) {
		return getPlayerVipLevel(player, false) >= vip开启随身仓库;
	}

	public boolean vip是否开启随身求购(Player player) {
		return getPlayerVipLevel(player, false) >= vip开启随身求购;
	}

	// public int getPlayerVipLevel(Player player){
	// return getPlayerVipLevel(player, false);
	// }
	/**
	 * 0级代表不是vip
	 * @param player
	 * @param realVip
	 *            是否是真实vip
	 * @return
	 */
	public int getPlayerVipLevel(Player player, boolean realVip) {
		if (!player.vipDisplay) {
			return 0;
		}
		int realVipLevel = 0;
		long rmb = player.getRMB();
//		if(player.getViprmb() > rmb){
//			rmb = player.getViprmb();
//		}
		for (int i = vipDatas2.length - 1; i >= 0; i--) {
			if (rmb >= vipDatas2[i].getNeedCost()) {
				realVipLevel = vipDatas2[i].getVipLevel();
				break;
			}
		}
		
		if (realVip) {
			return realVipLevel;
		} else {
			int cardVipLevel = player.getCardVip();
			return Math.max(realVipLevel, cardVipLevel);
		}
	}

	public void 设置玩家的vip属性(Player player) {
		player.setVipLevel(player.getVipLevel());
		player.setRepairCarry(vip是否开启随身修理(player));
		player.setMailCarry(vip是否开启随身邮件(player));
		player.setWareHouseCarry(vip是否开启随身仓库(player));
		player.setQuickBuyCarry(vip是否开启随身求购(player));
		player.setVipZhongJiLianYaoFlag(vip是否开启中级炼妖(player));
		player.setVipJingJieZaNianFlushFlag(vip是否开启境界任务刷新(player));
		player.setVipXianFuLingFlag(vip是否开启庄园加速(player));
		player.setVipJuanXianZiJinFlag(vip是否开启用元宝购买贡献度(player));
		player.setVipTowerFlushFlag(vip是否开启幽冥幻域刷新(player));
		player.setVipGaoJiLianYaoFlag(vip是否开启高级炼妖(player));
	}

	Calendar calendar = Calendar.getInstance();

	public void 获取vip奖励(Player player) {
		int vipLevel = getPlayerVipLevel(player, true);
		if (vipLevel <= 0) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.不是vip);
			player.addMessageToRightBag(hreq);
			return;
		}

		if (player.getVipPickedRewardLevel() >= vipLevel) {
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.您已经领取了VIP奖励了, new String[][] { { Translate.COUNT_1, vipLevel + "" } }));
			player.addMessageToRightBag(hreq);
			return;
		}

		// 给奖励
		Article a = am.getArticle(vipDatas2[player.getVipPickedRewardLevel()].getArticleName());
		if (a != null) {
			try {
				ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_VIP, player, a.getColorType(), 1, true);
				if (ae != null) {
					if (player.putToKnapsacks(ae, "领取VIP奖励")) {
						player.setVipPickedRewardLevel((byte) (player.getVipPickedRewardLevel() + 1));
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.translateString(Translate.成功领取vip奖励, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
						player.addMessageToRightBag(hreq);
						logger.info("[领取VIP奖励] [成功] [{}] [articleName:{}] [articleId:{}] [articleColor:{}]", new Object[] { player.getLogString(), ae.getArticleName(), ae.getId(), ae.getColorType() });
					} else {
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.背包空间不足);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
			} catch (Exception ex) {
				logger.error("[领取VIP奖励] [异常] [" + player.getLogString() + "]", ex);
			}
		}

	}

	public boolean 是否领取了vip奖励(Player player) {
		int vipLevel = getPlayerVipLevel(player, true);
		if (vipLevel > 0) {
			if (player.getVipPickedRewardLevel() < vipLevel) {
				return false;
			}
		}
		return true;
	}

	public static Map<String, Integer> vipBuffMap = new HashMap<String, Integer>();
	static {
		vipBuffMap.put(Translate.VIP1体验卡, 1);
		vipBuffMap.put(Translate.VIP2体验卡, 2);
		vipBuffMap.put(Translate.VIP3体验卡, 3);
		vipBuffMap.put(Translate.VIP4体验卡, 4);
		vipBuffMap.put(Translate.VIP5体验卡, 5);
		vipBuffMap.put(Translate.VIP6体验卡, 6);
		vipBuffMap.put(Translate.VIP7体验卡, 7);
	}

	public static long hourMs = 3600000;

	public Map<Long, Long> vipPullTime = new Hashtable<Long, Long>();

	public static long VIP拉人操作CD = 10 * 1000;

	/**
	 * 点击传送按钮后判断
	 * @param vip
	 * @return
	 */
	public boolean vipPullCheck(Player vip, Player invited) {
		try {
			if (Arrays.asList(RobberyConstant.限制使用拉令的仙界地图集).contains(vip.getCurrentGame().gi.name)) {
				return false;
			}
			boolean b = QianCengTaManager.getInstance().isInQianCengTa(vip);
			if (b) {
				return false;
			}
			if (Arrays.asList(RobberyConstant.vip限制拉人地图).contains(vip.getCurrentGame().gi.name)) {
				return false;
			}
			if (SealManager.getInstance().isSealDownCity(vip.getCurrentGame().gi.name)) {
				return false;
			}
			String result = GlobalTool.isLimitAllTrans(vip);
			if (result != null) {
				return false;
			}
			if (vip.isInBattleField() && vip.getDuelFieldState() > 0) {
				return false;
			}
			DownCityManager dcm = DownCityManager.getInstance();
			if (dcm != null && dcm.isDownCityByName(vip.getCurrentGame().getGameInfo().name)) {// 副本状态
				return false;
			}
			String rr = GlobalTool.verifyTransByOther(vip.getId(), vip.getCurrentGame());
			if (rr != null) {
				return false;
			}
			if ("jiazuditu".contains(vip.getCurrentGame().gi.name) && vip.getJiazuId() != invited.getJiazuId()) {
				return false;
			}
			String mapname = vip.getCurrentGame().gi.name;
			if (PetDaoManager.getInstance().isPetDao(mapname)) {
				return false;
			}
		} catch (Exception e) {

		}
		return true;
	}

	public ResponseMessage vipCallOther(Player vip, VIP_PULL_FRIEND_REQ message) {
		long now = System.currentTimeMillis();
		try {
			if (vipPullTime.containsKey(vip.getId())) {
				long ct = vipPullTime.get(vip.getId());
				if (now <= (ct + VIP拉人操作CD)) {
					vip.sendError(Translate.整理背包太过频繁);
					return null;
				}
			}
			vipPullTime.put(vip.getId(), now);
		} catch (Exception e) {

		}
		try {
			if (SealManager.getInstance().isSealDownCity(vip.getCurrentGame().gi.name)) {
				vip.sendError(com.fy.engineserver.datasource.language.Translate.玩家正在进行封印副本挑战);
				return null;
			}
			if (Arrays.asList(RobberyConstant.限制使用拉令的仙界地图集).contains(vip.getCurrentGame().gi.name)) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.此地图不可使用特殊传送);
				vip.addMessageToRightBag(hreq);
				return null;
			}
			String result = GlobalTool.isLimitAllTrans(vip);
			if (result != null) {
				vip.sendError(result);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (vip.isInBattleField() && vip.getDuelFieldState() > 0) {
				vip.sendError(Translate.text_jiazu_060);
				return null;
			}
		} catch (Exception e) {

		}
		if (vip.getVipLevel() < openPullOtherLevel) {
			vip.sendError(com.fy.engineserver.datasource.language.Translate.text_requestbuy_026);
			return null;
		}
		try {
			DownCityManager dcm = DownCityManager.getInstance();
			if (dcm != null && dcm.isDownCityByName(vip.getCurrentGame().getGameInfo().name)) {// 副本状态
				vip.sendError(Translate.正在副本中提示);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String rr = GlobalTool.verifyTransByOther(vip.getId(), vip.getCurrentGame());
			if (rr != null) {
				vip.sendError(rr);
				return null;
			}
			String rr2 = GlobalTool.verifyTransByOther(message.getTargetId());
			if (rr2 != null) {
				if (Translate.正在副本中提示.equals(rr2)) {
					vip.sendError(Translate.对方正在副本中);
				} else if (Translate.挑战仙尊限制功能.equals(rr2)) {
					vip.sendError(Translate.对方正在挑战仙尊);
				} else if (Translate.鲜血试炼中不能操作.equals(rr2)) {
					vip.sendError(Translate.对方正在献血试炼);
				} else if (Translate.骰子副本不能拉人.equals(rr2)) {
					vip.sendError(Translate.对方正在副本);
				} else {
					vip.sendError(Translate.召唤您的伙伴在限制地图);
				}
				return null;
			}
		} catch (Exception e) {

		}
		OptionVipPullAgree pre = requestMap.get(vip.getId());
		if (pre != null && pre.tpOk && SystemTime.currentTimeMillis() - pre.invitTime < hourMs) {
			String msg = timeLimit;
			vip.sendError(msg);
			logger.info("VipManager.vipCallOther: time limit {}", vip.getName());
			return null;
		}
		PlayerManager pm = PlayerManager.getInstance();
		Player invited = null;
		long targetId = message.getTargetId();
		try {
			invited = pm.getPlayer(targetId);
		} catch (Exception e) {
			logger.error("find player error {}", message);
			logger.error("exception", e);
			return null;
		}
		if (invited.isOnline() == false) {
			logger.info("vipCallOther 目标不在线 {}", invited.getName());
			return null;
		}
		if (invited.getCountry() != vip.getCountry()) {
			vip.sendError(com.fy.engineserver.datasource.language.Translate.text_jiazu_063);
			return null;
		}
		if (invited.isInPrison()) {
			vip.sendError(Translate.仙友在监狱);
			return null;
		}
		try {
			if (invited.getCurrentGame() != null && Arrays.asList(RobberyConstant.限制使用拉令的仙界地图集).contains(invited.getCurrentGame().gi.name)) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.此地图不可使用特殊传送2);
				vip.addMessageToRightBag(hreq);
				return null;
			}
		} catch (Exception e) {

		}
		try {
			if (invited.isInBattleField() && invited.getDuelFieldState() > 0) {
				vip.sendError(Translate.仙友在比武);
				return null;
			}
			if ("jiazuditu".contains(vip.getCurrentGame().gi.name) && vip.getJiazuId() != invited.getJiazuId()) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.不可传送非本家族成员);
				vip.addMessageToRightBag(hreq);
				return null;
			}
		} catch (Exception e) {

		}
		boolean isXianjie = false;
		try {
			String mapname = vip.getCurrentGame().gi.name;
			if (PetDaoManager.getInstance().isPetDao(mapname)) {
				vip.sendError(Translate.仙友在迷城);
				return null;
			}
			if (mapname != null) {
				isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
			}
			if (invited.getLevel() <= 220 && isXianjie) {
				vip.sendError(Translate.仙界不能传送220以下玩家);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String title = title4pull;
		String body = body4pull;
		body = String.format(body, vip.getName());
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(title);
		mw.setDescriptionInUUB(body);

		OptionVipPullAgree agree = new OptionVipPullAgree();
		agree.invitTime = SystemTime.currentTimeMillis();
		agree.setInviteId(vip.getId());
		agree.setText(Translate.确定);

		OptionVipPullReject disAgree = new OptionVipPullReject();
		disAgree.setInviteId(vip.getId());
		disAgree.setText(Translate.取消);
		mw.setOptions(new Option[] { agree, disAgree });

		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());

		invited.addMessageToRightBag(res);
		requestMap.put(vip.getId(), agree);
		if (logger.isInfoEnabled()) {
			logger.info("send vip call from {} to {}", vip.getName(), invited.getName());
		}
		return null;
	}

	public LotteryGame getLotteryGame() {
		return lotteryGame;
	}

	public void setLotteryGame(LotteryGame lotteryGame) {
		this.lotteryGame = lotteryGame;
	}

	public void noticePlayerRecordVipInfo(Player player) {
		if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return;
		}
		if (player.getVipAgent().needRecord() && player.modifyVipInfo(player.getClientId()) ) {
			VipInfoRecordManager.getInstance().gatherVipInfo(player);
//			int[] pos = recordNPCLocation.get((int) player.getCountry());
//			if (pos == null) {
//				if (logger.isWarnEnabled()) {
//					logger.warn("[VIP] [提醒记录信息] [NPC位置不存在]");
//				}
//				return;
//			}
//			Option_Find_Way findNpc_Option = new Option_Find_Way(pos[0], pos[1], recordGame);
//			// TODO 发送窗口
//			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
//			mw.setDescriptionInUUB("恭喜你获得VIP通道资格,请去国家主城.寻找" + recordNpcName + ",填写资料,成功填写后，可享受每月一次VIP专属福利！~（橙酒、橙帖在向你招手呢）.");
//			findNpc_Option.setText("立即填写");
//			mw.setOptions(new Option[] { findNpc_Option });
//			CONFIRM_WINDOW_REQ res = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
//			player.addMessageToRightBag(res);
			if (logger.isWarnEnabled()) {
				logger.warn("[VIP] [提醒记录信息] [成功] [发送消息] [" + player.getName() + "] [" + recordGame + "] [" + recordNpcName + "]");
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[VIP] [提醒记录信息] [--] [不需要提醒] [" + player.getName() + "]");
			}
		}
	}

	/**
	 * 为某个玩家弹出VIP活动转盘
	 * @param player
	 */
	public void openLottery(Player player) {
		long fixedGivenId = lotteryGame.getFixedGiven().getTempArticleEntity().getId();
		long[] randomGivenId = new long[lotteryGame.getRandomGiven().size()];
		for (int i = 0; i < lotteryGame.getRandomGiven().size(); i++) {
			ActivityPropHasRate activityPropHasRate = lotteryGame.getRandomGiven().get(i);
			randomGivenId[i] = activityPropHasRate.getTempArticleEntity().getId();
		}
		VIP_LOTTERY_OPEN_REQ req = new VIP_LOTTERY_OPEN_REQ(GameMessageFactory.nextSequnceNum(), lotteryGame.getFixedGiven(), fixedGivenId, lotteryGame.getRandomGiven().toArray(new ActivityPropHasRate[0]), randomGivenId);
		player.addMessageToRightBag(req);
		if (logger.isInfoEnabled()) {
			logger.info("[VIP转盘] [通知玩家打开转盘] [" + player.getLogString() + "] [" + fixedGivenId + "] [" + Arrays.toString(randomGivenId) + "]");
		}
	}

	@Override
	public String getMConsoleName() {
		return "VIPmanager";
	}

	@Override
	public String getMConsoleDescription() {
		return "VIPmanager";
	}
}
