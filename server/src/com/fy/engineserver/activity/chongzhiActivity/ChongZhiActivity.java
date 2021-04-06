package com.fy.engineserver.activity.chongzhiActivity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class ChongZhiActivity implements Runnable { 
	public static Logger logger = LoggerFactory.getLogger(ChongZhiActivity.class);
	
	private static ChongZhiActivity instance;
	
	public boolean isRunning = true;
	public int runStep = 1000;

	public static String FIRST_PLAYERS = "FIRST_PLAYERS";
	private ArrayList<Long> firstplayers = new ArrayList<Long>();					//首充的保存数据
	public static String TOTAL_PLAYERS = "TOTAL_PLAYERS_NEW";
	private HashMap<Integer, HashMap<Long, Long>> chongZhiMoney2Id = new HashMap<Integer, HashMap<Long, Long>>();		//累计充值的保存数据
	public static String EVERYDAY_MONEYS = "EVERYDAY_MONEYS";
	private HashMap<Long, Long> todayChongZhiMoney2Id = new HashMap<Long, Long>();		//当天的充值金额
	private int today_data;
	private ChongZhiServerConfig todayChongZhiActivity;								//当前运行的这个活动
	public static String TOTAL_FANLI_MONEYS = "TOTAL_FANLI_MONEYS";
	private HashMap<Long, Long> totalChongZhiMoney2Id = new HashMap<Long, Long>();		//累计的充值金额返利
	public ChongZhiFanLiAcivity fanliActivity;											//返利
	
	public ChongZhiSpecialActivity specialActivity;										//特殊充值活动
	
	public static String FIRST_XIAOFEI_PLAYERS = "FIRST_XIAOFEI_PLAYERS";
	private ArrayList<Long> firstXiaoFeiPlayers = new ArrayList<Long>();
	public static String TOTAL_XIAOFEI_PLAYERS = "TOTAL_XIAOFEI_PLAYERS_NEW";
	private HashMap<Long, HashMap<Long, Long[]>> xiaoFeiMoney2Id = new HashMap<Long, HashMap<Long, Long[]>>();
	public static String TOTAL_XIAOFEI_MONEYS = "TOTAL_XIAOFEI_MONEYS";
	private HashMap<Long, Long> xiaoFeiMoneys = new HashMap<Long, Long>();

	//充值活动配置
	public ArrayList<ChongZhiServerConfig> chongZhiServerConfigs = new ArrayList<ChongZhiServerConfig>();
	//消费活动配置
	public ArrayList<XiaoFeiServerConfig> xiaoFeiServerConfigs = new ArrayList<XiaoFeiServerConfig>();
	//充值消费活动排行榜配置
	public ArrayList<MoneyBillBoardActivity> moneyBillboardConfigs = new ArrayList<MoneyBillBoardActivity>();
	
	private boolean isChongZhiActivityStart = true;
	private boolean isXiaoFeiActivityStart = true;
	private boolean isMoneyBillboardStart = true;
	
	public MoneyBillBoardActivity chongZhiBillBoard = null;
	public static String CHONGZHI_BILLBOARD = "CHONGZHI_BILLBOARD";
	public static String CHONGZHI_ENDOVER = "CHONGZHI_ENDOVER";
	public boolean isChongZhiEndOver = false;
	private HashMap<Long, Long> chongZhiBillBoardMsg = new HashMap<Long, Long>();
	public MoneyBillBoardActivity xiaoFeiBillBoard = null;
	public static String XIAOFEI_BILLBOARD = "XIAOFEI_BILLBOARD";
	public static String XIAOFEI_ENDOVER = "XIAOFEI_ENDOVER";
	public boolean isXiaoFeiEndOver = false;
	private HashMap<Long, Long> xiaoFeiBillBoardMsg = new HashMap<Long, Long>();
	
	public static String CHONGHZI_YIDONG = "CHONGHZI_YIDONG";
	public HashMap<Integer, HashMap<Long, Long>> chongzhiFanLiMoneys = new HashMap<Integer, HashMap<Long, Long>>();
	public static String CHONGZHI_YIDONG_SEND = "CHONGZHI_YIDONG_SEND";
	public HashMap<Integer, Long> chongzhiFanLiTimes = new HashMap<Integer, Long>();
	
	public DiskCache diskCache;
	private String filePath;							//catchPath;
	private String configPath;							//配置路径

	public void init() throws Exception {
		
		instance = this;
		setToday_data(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
		diskCache = new DefaultDiskCache(new File(getFilePath()), "充值消费活动", 1000L * 60 * 60 * 24 * 365);
		{
			//首次充值活动
			Object o = diskCache.get(FIRST_PLAYERS);
			if (o == null) {
				diskCache.put(FIRST_PLAYERS, (Serializable) firstplayers);
			}
			firstplayers = (ArrayList<Long>)diskCache.get(FIRST_PLAYERS);
		}
		{
			//累计充值活动
			Object o = diskCache.get(TOTAL_PLAYERS);
			if (o == null) {
				diskCache.put(TOTAL_PLAYERS, (Serializable) chongZhiMoney2Id);
			}
			chongZhiMoney2Id = (HashMap<Integer, HashMap<Long, Long>>)diskCache.get(TOTAL_PLAYERS);
		}
		{
			//每天返充值金额比例活动
			Object o = diskCache.get(EVERYDAY_MONEYS);
			if (o == null) {
				diskCache.put(EVERYDAY_MONEYS, (Serializable) todayChongZhiMoney2Id);
			}
			todayChongZhiMoney2Id = (HashMap<Long, Long>)diskCache.get(EVERYDAY_MONEYS);
		}
		{
			//累计返充值金额比例活动
			Object o = diskCache.get(TOTAL_FANLI_MONEYS);
			if (o == null) {
				diskCache.put(TOTAL_FANLI_MONEYS, (Serializable) totalChongZhiMoney2Id);
			}
			totalChongZhiMoney2Id = (HashMap<Long, Long>)diskCache.get(TOTAL_FANLI_MONEYS);
		}
		{
			//首次消费活动
			Object o = diskCache.get(FIRST_XIAOFEI_PLAYERS);
			if (o == null) {
				diskCache.put(FIRST_XIAOFEI_PLAYERS, (Serializable) firstXiaoFeiPlayers);
			}
			firstXiaoFeiPlayers = (ArrayList<Long>)diskCache.get(FIRST_XIAOFEI_PLAYERS);
		}
		{
			//累计消费活动
			Object o = diskCache.get(TOTAL_XIAOFEI_PLAYERS);
			if (o == null) {
				diskCache.put(TOTAL_XIAOFEI_PLAYERS, (Serializable) xiaoFeiMoney2Id);
			}
			xiaoFeiMoney2Id = (HashMap<Long, HashMap<Long, Long[]>>)diskCache.get(TOTAL_XIAOFEI_PLAYERS);
		}
		{
			//累计消费活动  多次
			Object o = diskCache.get(TOTAL_XIAOFEI_MONEYS);
			if (o == null) {
				diskCache.put(TOTAL_XIAOFEI_MONEYS, (Serializable) xiaoFeiMoneys);
			}
			xiaoFeiMoneys = (HashMap<Long, Long>)diskCache.get(TOTAL_XIAOFEI_MONEYS);
		}
		{
			//充值活动排行榜数据
			Object o = diskCache.get(CHONGZHI_BILLBOARD);
			if (o == null) {
				diskCache.put(CHONGZHI_BILLBOARD, (Serializable) chongZhiBillBoardMsg);
			}
			chongZhiBillBoardMsg = (HashMap<Long, Long>)diskCache.get(CHONGZHI_BILLBOARD);
		}
		{
			//充值活动是否结束
			Object o = diskCache.get(CHONGZHI_ENDOVER);
			if (o == null) {
				diskCache.put(CHONGZHI_ENDOVER, isChongZhiEndOver);
			}
			isChongZhiEndOver = (Boolean)diskCache.get(CHONGZHI_ENDOVER);
		}
		{
			//消费活动排行榜数据
			Object o = diskCache.get(XIAOFEI_BILLBOARD);
			if (o == null) {
				diskCache.put(XIAOFEI_BILLBOARD, (Serializable) xiaoFeiBillBoardMsg);
			}
			xiaoFeiBillBoardMsg = (HashMap<Long, Long>)diskCache.get(XIAOFEI_BILLBOARD);
		}
		{
			//充值活动是否结束
			Object o = diskCache.get(XIAOFEI_ENDOVER);
			if (o == null) {
				diskCache.put(XIAOFEI_ENDOVER, isXiaoFeiEndOver);
			}
			isXiaoFeiEndOver = (Boolean)diskCache.get(XIAOFEI_ENDOVER);
		}
		{
			//充值返利，充值后 接下来每天返一定的钱
			Object o = diskCache.get(CHONGHZI_YIDONG);
			if (o == null) {
				diskCache.put(CHONGHZI_YIDONG, chongzhiFanLiMoneys);
			}
			chongzhiFanLiMoneys = (HashMap<Integer, HashMap<Long, Long>>)diskCache.get(CHONGHZI_YIDONG);
			o = null;
			o = diskCache.get(CHONGZHI_YIDONG_SEND);
			if (o == null) {
				diskCache.put(CHONGZHI_YIDONG_SEND, chongzhiFanLiTimes);
			}
			chongzhiFanLiTimes = (HashMap<Integer, Long>)diskCache.get(CHONGZHI_YIDONG_SEND);
		}
		
//		loadFile();
		
		findTodayChongZhiFanLiActivity();
		
		findMoneyActivity();
		
		createTotalChongZhiFanLiActivity();
		
		createSpecialChongZhiActivity();
		
		createFanLiToDayActivity();
		
		Thread t = new Thread(instance, "ChongXiaoActivity");
		t.start();
		System.out.println("感恩充值活动启动");
		logger.warn("初始化完成");
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 创建累计充值返利活动
	 */
	public void createTotalChongZhiFanLiActivity () {
		String serverName = GameConstants.getInstance().getServerName();
		ChongZhiFanLiAcivity activity = null;
		try{
			activity = new ChongZhiFanLiAcivity();
			activity.setActivityID(200000);
			activity.setServerName(ChongZhiServerConfig.ALL_SERVER_NAME);
			activity.setStartTime("2013-02-04 00:00:00");
			activity.setEndTime("2013-02-08 23:59:59");
			activity.setLingquTime("2013-02-10 23:59:59");
			activity.setPropName("1000");
			activity.creatLongTime();
			activity.setMailTitle("恭喜获得飘渺寻仙曲新年红包");
			activity.setMailMsg("恭喜您成功领取飘渺寻仙曲新年红包，内含银子@COUNT_1@文，已直接打到您的账户上。感谢您的支持！");
			chongZhiServerConfigs.add(activity);
		}catch(Exception e) {
			logger.error("createTotalChongZhiFanLiActivity", e);
		}
		
		
		if (activity != null) {
			if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
				fanliActivity = activity;
			}
		}
	}
	
	public void createSpecialChongZhiActivity() {
		String serverName = GameConstants.getInstance().getServerName();
		ChongZhiSpecialActivity activity = null;
		try{
			if (PlatformManager.getInstance().platformOf(Platform.腾讯)) {
				activity = new ChongZhiSpecialActivity(ChongZhiServerConfig.ALL_SERVER_NAME, 
						"2013-05-07 00:00:00", "2013-05-15 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300000);
				chongZhiServerConfigs.add(activity);
			}else if (PlatformManager.getInstance().platformOf(Platform.官方)) {
				activity = new ChongZhiSpecialActivity(ChongZhiServerConfig.ALL_SERVER_NAME, 
						"2013-05-09 00:00:00", "2013-05-13 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300010);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("国内本地测试", 
						"2013-05-05 00:00:00", "2013-05-07 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300011);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("幽恋蝶谷", 
						"2013-05-05 00:00:00", "2013-05-06 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300012);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("天上人间", 
						"2013-05-05 00:00:00", "2013-05-06 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300013);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("一战倾城", 
						"2013-05-05 00:00:00", "2013-05-06 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300014);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("梦倾天下", 
						"2013-05-09 00:00:00", "2013-05-15 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300015);
				chongZhiServerConfigs.add(activity);
				if (activity != null) {
					if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
						specialActivity = activity;
					}
				}
				activity = new ChongZhiSpecialActivity("菩提众生", 
						"2013-05-05 00:00:00", "2013-05-06 23:59:59",
						"春季盛宴之神宠现世", "恭喜您在神宠现世活动中获得奖励，请查收附件。感谢您对飘渺寻仙曲的支持！", 
						4900000, "四君子特惠锦囊", 2500000, "梅兰竹菊锦囊");
				activity.setActivityID(300016);
				chongZhiServerConfigs.add(activity);
			}
		}catch(Exception e) {
			logger.error("createSpecialChongZhiActivity", e);
		}
		if (activity != null) {
			if (activity.getServerName().equals(serverName) || activity.getServerName().equals(ChongZhiServerConfig.ALL_SERVER_NAME)){
				specialActivity = activity;
			}
		}
	}
	
	public void createFanLiToDayActivity() {
		try{
			if (PlatformManager.getInstance().platformOf(Platform.台湾)) {
//				String[] serverNames = new String[]{"昆侖仙境","飄渺王城","昆华古城","華山之巔","雪域冰城",
//						"天降神兵","斬龍屠魔","皇圖霸業",	"人間仙境","伏虎沖天","無相如來",	"仙侶奇緣",
//						"仙人指路","瓊漿玉露","道骨仙風","仙姿玉貌","步雲登仙"};
//				for (int i = 0; i < serverNames.length; i++) {
//					ChongZhiActivityFanLiToDay activity = new ChongZhiActivityFanLiToDay(400000 + i,serverNames[i], 
//							"2013-03-18 10:30:00", "2013-03-21 23:59:59",
//							"恭喜您获得充值返现", "恭喜您获得充值返现百分百活动奖励，请查收附件。感谢您对飘渺寻仙曲的支持！",
//							"", new long[]{24*60*60*1000L,24*60*60*1000L,24*60*60*1000L,24*60*60*1000L},
//							new String[]{"2013-03-22 00:00:00", "2013-03-28 00:00:00", "2013-04-03 00:00:00","2013-04-11 00:00:00", "2013-04-21 00:00:00"}, new int[]{100,200,400,500}
//							);
//					chongZhiServerConfigs.add(activity);
//				}
			}else if (PlatformManager.getInstance().platformOf(Platform.官方)) {
				String[] serverNames = new String[]{"国内本地测试"};
				for (int i = 0; i < serverNames.length; i++) {
					ChongZhiActivityFanLiToDay activity = new ChongZhiActivityFanLiToDay(400000 + i,serverNames[i], 
							"2013-03-18 10:30:00", "2013-03-24 23:59:59",
							"恭喜您获得充值返现", "恭喜您获得充值返现活动奖励，请查收附件。感谢您对飘渺寻仙曲的支持！",
							"", new long[]{24*60*60*1000L},
							new String[]{"2013-03-25 00:00:00", "2013-03-25 01:00:00"}, new int[]{5000}
							);
					chongZhiServerConfigs.add(activity);
				}
			}
		}catch(Exception e) {
			logger.error("createFanLiToDayActivity", e);
		}
	}
	
	/**
	 * 确定参加的充值返利活动
	 */
	public void findTodayChongZhiFanLiActivity() {
		String serverName = GameConstants.getInstance().getServerName();
		for (int i = 0; i < chongZhiServerConfigs.size(); i++) {
			ChongZhiServerConfig con = chongZhiServerConfigs.get(i);
			if (con.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE) {
				if (serverName.equals(con.getServerName())) {
					todayChongZhiActivity = con;
					return;
				}
			}
		}
		for (int i = 0; i < chongZhiServerConfigs.size(); i++) {
			ChongZhiServerConfig con = chongZhiServerConfigs.get(i);
			if (con.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE) {
				if (con.getServerName().equals(MoneyBillBoardActivity.ALL_SERVER_NAME)) {
					todayChongZhiActivity = con;
					return;
				}
			}
		}
	}
	
	//确定参加的活动
	public void findMoneyActivity() {
		boolean isSelfChongZhi = false;
		boolean isSelfXiaoFei = false;
		String serverName = GameConstants.getInstance().getServerName();
		for (int i = 0; i < moneyBillboardConfigs.size(); i++) {
			MoneyBillBoardActivity config = moneyBillboardConfigs.get(i);
			if (config.getServerName().equals(serverName)) {
				if (config.getType() == MoneyBillBoardActivity.MONEYBILLBOARDACTIVITY_CHONGZHI) {
					if ((config.getPlatfrom() == 0 && PlatformManager.getInstance().platformOf(Platform.官方))
					||(config.getPlatfrom() == 1 && PlatformManager.getInstance().platformOf(Platform.台湾))
					||(config.getPlatfrom() == 2 && PlatformManager.getInstance().platformOf(Platform.腾讯))
					|| (config.getPlatfrom() == 3 && PlatformManager.getInstance().platformOf(Platform.韩国))) {
						isSelfChongZhi = true;
						chongZhiBillBoard = config;
					}
				}else if (config.getType() == MoneyBillBoardActivity.MONEYBILLBOARDACTIVITY_XIAOFEI) {
					if ((config.getPlatfrom() == 0 && PlatformManager.getInstance().platformOf(Platform.官方))
							||(config.getPlatfrom() == 1 && PlatformManager.getInstance().platformOf(Platform.台湾))
							||(config.getPlatfrom() == 2 && PlatformManager.getInstance().platformOf(Platform.腾讯))
							|| (config.getPlatfrom() == 3 && PlatformManager.getInstance().platformOf(Platform.韩国))) {
						isSelfXiaoFei = true;
						xiaoFeiBillBoard = config;
					}
				}
			}
		}
		
		for (int i = 0; i < moneyBillboardConfigs.size(); i++) {
			MoneyBillBoardActivity config = moneyBillboardConfigs.get(i);
			if (config.getServerName().equals(MoneyBillBoardActivity.ALL_SERVER_NAME)) {
				if (config.getType() == MoneyBillBoardActivity.MONEYBILLBOARDACTIVITY_CHONGZHI && !isSelfChongZhi) {
					if ((config.getPlatfrom() == 0 && PlatformManager.getInstance().platformOf(Platform.官方))
							||(config.getPlatfrom() == 1 && PlatformManager.getInstance().platformOf(Platform.台湾))
							||(config.getPlatfrom() == 2 && PlatformManager.getInstance().platformOf(Platform.腾讯))
							|| (config.getPlatfrom() == 3 && PlatformManager.getInstance().platformOf(Platform.韩国))) {
						chongZhiBillBoard = config;
					}
				}else if (config.getType() == MoneyBillBoardActivity.MONEYBILLBOARDACTIVITY_XIAOFEI && !isSelfXiaoFei) {
					if ((config.getPlatfrom() == 0 && PlatformManager.getInstance().platformOf(Platform.官方))
							||(config.getPlatfrom() == 1 && PlatformManager.getInstance().platformOf(Platform.台湾))
							||(config.getPlatfrom() == 2 && PlatformManager.getInstance().platformOf(Platform.腾讯))
							|| (config.getPlatfrom() == 3 && PlatformManager.getInstance().platformOf(Platform.韩国))) {
						xiaoFeiBillBoard = config;
					}
				}
			}
		}
	}
	
	public void loadFile() {
		try{
			File file = new File(getConfigPath());
			file = new File(ConfigServiceManager.getInstance().getFilePath(file));
			if (!file.exists()) {
				isChongZhiActivityStart = false;
				isXiaoFeiActivityStart = false;
				logger.error("充值活动配置文件不存在");
			}else { 
				Workbook workbook = Workbook.getWorkbook(file);
				//充值活动
				Sheet sheet = workbook.getSheet(0);
				int maxRow = sheet.getRows();
				for (int i = 1; i < maxRow; i++) {
					Cell[] cells = sheet.getRow(i);
					ChongZhiServerConfig chongZhiServerConfig = new ChongZhiServerConfig();
					for (int j = 0; j < cells.length; j++) {
						Cell cell = cells[j];
						switch(j) {
						case 0:
							chongZhiServerConfig.setActivityID(Integer.parseInt(cell.getContents()));
							break;
						case 1:
							chongZhiServerConfig.setServerName(cell.getContents());
							break;
						case 2:
							chongZhiServerConfig.setType(Integer.parseInt(cell.getContents()));
							break;
						case 3:
							chongZhiServerConfig.setStartTime(cell.getContents());
							break;
						case 4:
							chongZhiServerConfig.setEndTime(cell.getContents());
							break;
						case 5:
							chongZhiServerConfig.setMoney(Long.parseLong(cell.getContents()));
							break;
						case 6:
							chongZhiServerConfig.setPropName(cell.getContents());
							break;
						case 7:
							chongZhiServerConfig.setPropNum(Integer.parseInt(cell.getContents()));
							break;
						case 8:
							chongZhiServerConfig.setColorType(Integer.parseInt(cell.getContents()));
							break;
						case 9:
							chongZhiServerConfig.setBind(Integer.parseInt(cell.getContents()) == 0);
							break;
						case 10:
							chongZhiServerConfig.setMailTitle(cell.getContents());
							break;
						case 11:
							chongZhiServerConfig.setMailMsg(cell.getContents());
							break;
						}
					}
					chongZhiServerConfig.creatLongTime();
					chongZhiServerConfigs.add(chongZhiServerConfig);
				}
				
				//消费活动
				sheet = workbook.getSheet(1);
				maxRow = sheet.getRows();
				for (int i = 1; i < maxRow; i++) {
					Cell[] cells = sheet.getRow(i);
					XiaoFeiServerConfig xiaoFeiServerConfig = new XiaoFeiServerConfig();
					for (int j = 0; j < cells.length; j++) {
						Cell cell = cells[j];
						switch(j) {
						case 0:
							xiaoFeiServerConfig.setId(Long.parseLong(cell.getContents()));
							break;
						case 1:
							xiaoFeiServerConfig.setServerName(cell.getContents());
							break;
						case 2:
							xiaoFeiServerConfig.setType(Integer.parseInt(cell.getContents()));
							break;
						case 3:
							String[] ss = cell.getContents().split(",");
							int[] tongdao = new int[ss.length];
							for (int k = 0; k < tongdao.length; k++) {
								tongdao[k] = Integer.parseInt(ss[k]);
							}
							xiaoFeiServerConfig.setXiaoFeiTongDao(tongdao);
							break;
						case 4:
							xiaoFeiServerConfig.setStartTime(cell.getContents());
							break;
						case 5:
							xiaoFeiServerConfig.setEndTime(cell.getContents());
							break;
						case 6:
							xiaoFeiServerConfig.setMoney(Long.parseLong(cell.getContents()));
							break;
						case 7:
							xiaoFeiServerConfig.setPropName(cell.getContents());
							break;
						case 8:
							xiaoFeiServerConfig.setPropNum(Integer.parseInt(cell.getContents()));
							break;
						case 9:
							xiaoFeiServerConfig.setColorType(Integer.parseInt(cell.getContents()));
							break;
						case 10:
							xiaoFeiServerConfig.setBind(Integer.parseInt(cell.getContents()) == 0);
							break;
						case 11:
							xiaoFeiServerConfig.setMailTitle(cell.getContents());
							break;
						case 12:
							xiaoFeiServerConfig.setMailMsg(cell.getContents());
							break;
						}
					}
					xiaoFeiServerConfig.creatLongTime();
					this.xiaoFeiServerConfigs.add(xiaoFeiServerConfig);
				}
				
				//充值消费排行榜
				sheet = workbook.getSheet(2);
				maxRow = sheet.getRows();
				for (int i = 1; i < maxRow; i++) {
					Cell[] cells = sheet.getRow(i);
					MoneyBillBoardActivity moneyConfig = new MoneyBillBoardActivity();
					for (int j = 0; j < cells.length; j++) {
						Cell cell = cells[j];
						switch(j) {
						case 0:
							moneyConfig.setServerName(cell.getContents());
							break;
						case 1:
							moneyConfig.setPlatfrom(Integer.parseInt(cell.getContents()));
							break;
						case 2:
							moneyConfig.setType(Integer.parseInt(cell.getContents()));
							break;
						case 3:
							if (cell.getContents() != null) {
								String[] ss = cell.getContents().split(",");
								moneyConfig.setParameters(ss);
							}
							break;
						case 4:
							moneyConfig.setStartTime(cell.getContents());
							break;
						case 5:
							moneyConfig.setEndTime(cell.getContents());
							break;
						case 6:
							if (cell.getContents() != null) {
								String[] ss = cell.getContents().split(",");
								moneyConfig.setPropNames(ss);
							}
							break;
						case 7:
							moneyConfig.setMailTitle(cell.getContents());
							break;
						case 8:
							moneyConfig.setMailMsg(cell.getContents());
							break;
						case 9:
							moneyConfig.setBillboardMsg(cell.getContents());
							break;
						}
					}
					moneyConfig.creatLongTime();
					moneyBillboardConfigs.add(moneyConfig);
				}
			}
		}catch(Exception e) {
			logger.error("loadFileConfig出错", e);
			chongZhiServerConfigs.clear();
			xiaoFeiServerConfigs.clear();
			moneyBillboardConfigs.clear();
		}
	}
	
	public void doChongZhiActivity(Player player, long yinzi) {
		try{
			if (!isChongZhiActivityStart) {
				return;
			}
			
			String serverName = GameConstants.getInstance().getServerName();
			boolean isConfigSelf = false;				//是否有自己服务器的活动
			boolean isHaveFirst = false;				//是否有首充活动
			boolean isHaveTotal = false;				//是否有累计活动
			boolean isHaveToDayBackType = false;		//是否有每天返利充值活动
			boolean isHaveBackType = false;				//是否有累计返利充值活动
			boolean isHaveSpecial = false;				//是否有余额返利活动
			boolean isHaveFanLiToDay = false;			//是否有移动返利模式的充值活动
			for (int i = 0 ; i < chongZhiServerConfigs.size(); i++) {
				ChongZhiServerConfig chongZhiConfig = chongZhiServerConfigs.get(i);
				if (ChongZhiServerConfig.ALL_SERVER_NAME.equals(chongZhiConfig.getServerName()) || chongZhiConfig.getServerName().equals(serverName)) {
					if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_BACK_TYPE) {
						if (chongZhiConfig.isStart()) {
							isHaveBackType = true;
							doRealChongZhiActivity(chongZhiConfig, player, yinzi);
						}
					}else if (chongZhiConfig.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE) {
						if (chongZhiConfig.isStart()) {
							isHaveToDayBackType = true;
							doRealChongZhiActivity(chongZhiConfig, player, yinzi);
						}
					}else if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_SPECIAL_TYPE) {
						if (chongZhiConfig.isStart()) {
							isHaveSpecial = true;
							doRealChongZhiActivity(chongZhiConfig, player, yinzi);
						}
					}else if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE) {
						if (chongZhiConfig.isStart()) {
							isHaveFanLiToDay = true;
							doRealChongZhiActivity(chongZhiConfig, player, yinzi);
						}
					}
				}
			}
			
			for (int i = 0 ; i < chongZhiServerConfigs.size(); i++) {
				ChongZhiServerConfig chongZhiConfig = chongZhiServerConfigs.get(i);
				if (chongZhiConfig.getServerName().equals(serverName)) {
					isConfigSelf = true;
					if (chongZhiConfig.isStart()) {
						if (chongZhiConfig.getType() == ChongZhiServerConfig.FIRST_CHONGZHI_TYPE) {
							isHaveFirst = true;
						}else if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_TYPE) {
							isHaveTotal = true;
						}
						if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_BACK_TYPE && isHaveBackType) {
							continue;
						}
						if (chongZhiConfig.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE && isHaveToDayBackType) {
							continue;
						}
						if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_SPECIAL_TYPE && isHaveSpecial) {
							continue;
						}
						if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE && isHaveFanLiToDay) {
							continue;
						}
						doRealChongZhiActivity(chongZhiConfig, player, yinzi);
					}
				}
			}
			if (!isConfigSelf) {
				for (int i = 0 ; i < chongZhiServerConfigs.size(); i++) {
					ChongZhiServerConfig chongZhiConfig = chongZhiServerConfigs.get(i);
					if (ChongZhiServerConfig.ALL_SERVER_NAME.equals(chongZhiConfig.getServerName())) {
						if (chongZhiConfig.isStart()) {
							if (chongZhiConfig.getType() == ChongZhiServerConfig.FIRST_CHONGZHI_TYPE) {
								isHaveFirst = true;
							}else if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_TYPE) {
								isHaveTotal = true;
							}
							if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_BACK_TYPE && isHaveBackType) {
								continue;
							}
							if (chongZhiConfig.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE && isHaveToDayBackType) {
								continue;
							}
							if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_SPECIAL_TYPE && isHaveSpecial) {
								continue;
							}
							if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE && isHaveFanLiToDay) {
								continue;
							}
							doRealChongZhiActivity(chongZhiConfig, player, yinzi);
						}
					}
				}
			}
			
			if (!isHaveFirst) {
				if (firstplayers.size() > 0) {
					firstplayers.clear();
					diskCache.put(FIRST_PLAYERS, firstplayers);
				}
			}
			if (!isHaveTotal) {
				if (chongZhiMoney2Id.size() > 0) {
					chongZhiMoney2Id.clear();
					diskCache.put(TOTAL_PLAYERS, chongZhiMoney2Id);
				}
			}
		}catch(Exception e) {
			logger.error("doActivity出错", e);
		}
	}
	
	public void doRealChongZhiActivity(ChongZhiServerConfig chongZhiConfig, Player player, long yinzi) throws Exception {
		if (chongZhiConfig.getType() == ChongZhiServerConfig.FIRST_CHONGZHI_TYPE) {
			//首次充值
			if (yinzi >= chongZhiConfig.getMoney()) {
				if (!firstplayers.contains(player.getId())) {
					Article a = ArticleManager.getInstance().getArticle(chongZhiConfig.getPropName());
					if (a != null) {
						int colorType = chongZhiConfig.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, chongZhiConfig.isBind(), ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{chongZhiConfig.getPropNum()}, chongZhiConfig.getMailTitle(), chongZhiConfig.getMailMsg(), 0, 0, 0, "首充送好礼");
						firstplayers.add(player.getId());
						diskCache.put(FIRST_PLAYERS, firstplayers);
						logger.warn("[参与首充活动] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
					}else {
						logger.error("[首充奖励物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
					}
				}
			}
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.ONETIME_CHONGZHI_TYPE) {
			//单次充值
			for (int j = 0; j < yinzi / chongZhiConfig.getMoney(); j++) {
				//发奖励
				Article a = ArticleManager.getInstance().getArticle(chongZhiConfig.getPropName());
				if (a != null) {
					int colorType = chongZhiConfig.getColorType();
					if (colorType < 0) {
						colorType = a.getColorType();
					}
					ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, chongZhiConfig.isBind(), ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{chongZhiConfig.getPropNum()}, chongZhiConfig.getMailTitle(), chongZhiConfig.getMailMsg(), 0, 0, 0, "单次充值金额达到奖励活动");
					logger.warn("[多次充值达到奖励] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
				}else {
					logger.error("[多次奖励物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
				}
			}
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_TYPE) {
			//累计充值
			HashMap<Long, Long> moneys = chongZhiMoney2Id.get(chongZhiConfig.getActivityID());
			if (moneys == null) {
				moneys = new HashMap<Long, Long>();
				chongZhiMoney2Id.put(chongZhiConfig.getActivityID(), moneys);
			}
			Long oldMoney = moneys.get(player.getId());
			boolean isGive = false;
			if (oldMoney != null && oldMoney >= chongZhiConfig.getMoney()) {
				isGive = true;
			}
			if (!isGive) {
				if (oldMoney == null && yinzi >= chongZhiConfig.getMoney()) {
					Article a = ArticleManager.getInstance().getArticle(chongZhiConfig.getPropName());
					ArticleEntity entity2 = null;
					if (a != null) {
						int colorType = chongZhiConfig.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						entity2 = ArticleEntityManager.getInstance().createEntity(a, chongZhiConfig.isBind(), ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity2}, new int[]{chongZhiConfig.getPropNum()}, chongZhiConfig.getMailTitle(), chongZhiConfig.getMailMsg(), 0, 0, 0, "累计充值活动奖励");
						logger.warn("[累计充值达到奖励] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
					}else {
						logger.error("[累计奖励物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
					}
				}else if (oldMoney != null && yinzi + oldMoney >= chongZhiConfig.getMoney()) {
					Article a = ArticleManager.getInstance().getArticle(chongZhiConfig.getPropName());
					ArticleEntity entity2 = null;
					if (a != null) {
						int colorType = chongZhiConfig.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						entity2 = ArticleEntityManager.getInstance().createEntity(a, chongZhiConfig.isBind(), ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity2}, new int[]{chongZhiConfig.getPropNum()}, chongZhiConfig.getMailTitle(), chongZhiConfig.getMailMsg(), 0, 0, 0, "累计充值活动奖励");
						logger.warn("[累计充值达到奖励] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), oldMoney.longValue(), yinzi, chongZhiConfig.toLogString()});
					}else {
						logger.error("[累计奖励物品不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
					}
				}
			}
			if (oldMoney == null) {
				moneys.put(player.getId(), yinzi);
			}else {
				moneys.put(player.getId(), oldMoney + yinzi);
			}
			chongZhiMoney2Id.put(chongZhiConfig.getActivityID(), moneys);
			diskCache.put(TOTAL_PLAYERS, chongZhiMoney2Id);
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.EVERYDAY_CHONGZHI_BACK_TYPE) {
			//每日充值返利活动
			synchronized (todayChongZhiMoney2Id) {
				Long oldMoney = todayChongZhiMoney2Id.get(player.getId());
				if (oldMoney == null) {
					oldMoney = 0L;
				}
				oldMoney += yinzi;
				todayChongZhiMoney2Id.put(player.getId(), oldMoney);
				diskCache.put(EVERYDAY_MONEYS, todayChongZhiMoney2Id);
			}
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.TOTAL_CHONGZHI_BACK_TYPE) {
			//累计充值返利
			Long oldMoney = totalChongZhiMoney2Id.get(player.getId());
			if (oldMoney == null) {
				oldMoney = 0L;
			}
			oldMoney += yinzi;
			totalChongZhiMoney2Id.put(player.getId(), oldMoney);
			diskCache.put(TOTAL_FANLI_MONEYS, totalChongZhiMoney2Id);
			
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_SPECIAL_TYPE) {
			//单笔充值余额也给道具
			if (specialActivity != null && specialActivity == chongZhiConfig) {
				long yuMoney = yinzi%specialActivity.getMoney();
				for (int j = 0; j < yinzi / specialActivity.getMoney(); j++) {
					//发奖励
					Article a = ArticleManager.getInstance().getArticle(specialActivity.getPropName());
					if (a != null) {
						int colorType = specialActivity.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, specialActivity.isBind(), ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{specialActivity.getPropNum()}, specialActivity.getMailTitle(), specialActivity.getMailMsg(), 0, 0, 0, "有余额充值活动");
						logger.warn("[有余额奖励] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, yuMoney, specialActivity.toLogString()});
					}else {
						logger.error("[有余额奖励不存在] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, yuMoney, specialActivity.toLogString()});
					}
				}
				for (int j = 0; j < yuMoney / specialActivity.getYuMoney(); j++) {
					//发奖励
					Article a = ArticleManager.getInstance().getArticle(specialActivity.getYuPropName());
					if (a != null) {
						int colorType = a.getColorType();
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_CHONGZHI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{1}, specialActivity.getMailTitle(), specialActivity.getMailMsg(), 0, 0, 0, "有余额充值活动(余额)");
						logger.warn("[有余额奖励(余额)] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, yuMoney, specialActivity.toLogString()});
					}else {
						logger.error("[有余额奖励不存在(余额)] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, yuMoney, specialActivity.toLogString()});
					}
				}
				
			}else {
				logger.error("[special活动不存在] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
			}
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE) {
			//按移动的返话费模式，充值后  在接下来一段时间  返利
			ChongZhiActivityFanLiToDay ac = (ChongZhiActivityFanLiToDay)chongZhiConfig;
			HashMap<Long, Long> moneys = chongzhiFanLiMoneys.get(ac.getActivityID());
			if (moneys == null) {
				moneys = new HashMap<Long, Long>();
				chongzhiFanLiMoneys.put(ac.getActivityID(), moneys);
			}
			Long money = moneys.get(player.getId());
			if (money == null) {
				money = new Long(0);
			}
			money += yinzi;
			moneys.put(player.getId(), money);
			chongzhiFanLiMoneys.put(ac.getActivityID(), moneys);
			diskCache.put(CHONGHZI_YIDONG, chongzhiFanLiMoneys);
			logger.warn("[充值，进入返] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), ac.getActivityID(), ac.getServerName(), money});
		}else if (chongZhiConfig.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TIMELY) {
			//当时返利
			int bili = Integer.parseInt(chongZhiConfig.getPropName());
			if (yinzi >= chongZhiConfig.getMoney()) {
				long backMoney = yinzi * bili / 10000;
				if (backMoney <= 0) {
					backMoney = 1;
				}
				MailManager.getInstance().sendMail(player.getId(), null, chongZhiConfig.getMailTitle(), chongZhiConfig.getMailMsg(), backMoney, 0, 0, "充值活动");
				logger.warn("[充值当时返利] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), chongZhiConfig.toLogString(), yinzi, backMoney});
			}
		}else {
			logger.error("[活动类型不存在] [{}] [rmb={}] [config={}]", new Object[]{player.getLogString(), yinzi, chongZhiConfig.toLogString()});
		}
	}
	
	//取当前服务器的消费累计活动
	public XiaoFeiServerConfig getXiaoFeiConfig4 (Player player) {
		try{
			if (!isXiaoFeiActivityStart()){
				return null;
			}
			String serverName = GameConstants.getInstance().getServerName();
			for (int i = 0; i < xiaoFeiServerConfigs.size(); i++) {
				XiaoFeiServerConfig xiaofeiConfig = xiaoFeiServerConfigs.get(i);
				if (xiaofeiConfig.getServerName().equals(serverName)) {
					if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_TIMES_XIAOFEI_TYPE) {
						return xiaofeiConfig;
					}
				}
			}
			for (int i = 0; i < xiaoFeiServerConfigs.size(); i++) {
				XiaoFeiServerConfig xiaofeiConfig = xiaoFeiServerConfigs.get(i);
				if (XiaoFeiServerConfig.ALL_SERVER_NAME.equals(xiaofeiConfig.getServerName())) {
					if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_TIMES_XIAOFEI_TYPE) {
						return xiaofeiConfig;
					}
				}
			}
			
		}catch(Exception e){
			logger.error("getXiaoFeiConfig出错 ", e);
		}
		return null;
	}
	
	public void doXiaoFeiActivity(Player player, long yinzi, int xiaofeiType) {
		try{
			if (!isXiaoFeiActivityStart()){
				return;
			}
			String serverName = GameConstants.getInstance().getServerName();
			boolean isConfigSelf = false;				//是否有自己服务器的活动
			boolean isHaveFirst = false;				//是否有首充活动
			boolean isHaveTotal = false;				//是否有累计活动
			boolean isHaveTotal2 = false;				//是否有累计多次活动
			
			for (int i = 0; i < xiaoFeiServerConfigs.size(); i++) {
				XiaoFeiServerConfig xiaofeiConfig = xiaoFeiServerConfigs.get(i);
				if (xiaofeiConfig.getServerName().equals(serverName)) {
					isConfigSelf = true;
					if (xiaofeiConfig.isStart()) {
						if (xiaofeiConfig.getType() == XiaoFeiServerConfig.FIRST_XIAOFEI_TYPE) {
							isHaveFirst = true;
						}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_XIAOFEI_TYPE) {
							isHaveTotal  =true;
						}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_TIMES_XIAOFEI_TYPE) {
							isHaveTotal2 = true;
						}
						doRealXiaoFeiActivity(xiaofeiConfig, player, yinzi, xiaofeiType);
					}
				}
			}
			
			if (!isConfigSelf) {
				for (int i = 0; i < xiaoFeiServerConfigs.size(); i++) {
					XiaoFeiServerConfig xiaofeiConfig = xiaoFeiServerConfigs.get(i);
					if (XiaoFeiServerConfig.ALL_SERVER_NAME.equals(xiaofeiConfig.getServerName())) {
						if (xiaofeiConfig.isStart()) {
							if (xiaofeiConfig.getType() == XiaoFeiServerConfig.FIRST_XIAOFEI_TYPE) {
								isHaveFirst = true;
							}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_XIAOFEI_TYPE) {
								isHaveTotal  =true;
							}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_TIMES_XIAOFEI_TYPE) {
								isHaveTotal2 = true;
							}
							doRealXiaoFeiActivity(xiaofeiConfig, player, yinzi, xiaofeiType);
						}
					}
				}
			}
			
			if (!isHaveFirst) {
				if (firstXiaoFeiPlayers.size() > 0) {
					firstXiaoFeiPlayers.clear();
					diskCache.put(FIRST_XIAOFEI_PLAYERS, firstXiaoFeiPlayers);
				}
			}
			if (!isHaveTotal) {
				if (xiaoFeiMoney2Id.size() > 0) {
					xiaoFeiMoney2Id.clear();
					diskCache.put(TOTAL_XIAOFEI_PLAYERS, xiaoFeiMoney2Id);
				}
			}
			if (!isHaveTotal2) {
				if (xiaoFeiMoneys.size() > 0) {
					xiaoFeiMoneys.clear();
					diskCache.put(TOTAL_XIAOFEI_MONEYS, xiaoFeiMoneys);
				}
			}
		}catch(Exception e) {
			logger.error("[参与消费活动失败] [{}] [YINZI={}] [TYPE={}]", new Object[]{player.getLogString(), yinzi, xiaofeiType});
		}
	}
	
	public void doRealXiaoFeiActivity(XiaoFeiServerConfig xiaofeiConfig, Player player, long yinzi, int xiaofeiType) throws Exception {
		boolean isXiaoFeiType = false;
		for (int i = 0; i < xiaofeiConfig.getXiaoFeiTongDao().length; i++) {
			if (xiaofeiConfig.getXiaoFeiTongDao()[i] == xiaofeiType) {
				isXiaoFeiType = true;
				break;
			}
		}
		if (isXiaoFeiType) {
			if (xiaofeiConfig.getType() == XiaoFeiServerConfig.FIRST_XIAOFEI_TYPE) {
				//首次消费到一定金额
				if (yinzi >= xiaofeiConfig.getMoney()) {
					if (!firstXiaoFeiPlayers.contains(player.getId())) {
						Article a = ArticleManager.getInstance().getArticle(xiaofeiConfig.getPropName());
						if (a != null) {
							int colorType = xiaofeiConfig.getColorType();
							if (colorType < 0) {
								colorType = a.getColorType();
							}
							ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, xiaofeiConfig.isBind(), ArticleEntityManager.CREATE_REASON_XIAOFEI_ACTIVITY, player, colorType, 1, true);
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{xiaofeiConfig.getPropNum()}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "首次消费送好礼");
							firstXiaoFeiPlayers.add(player.getId());
							diskCache.put(FIRST_XIAOFEI_PLAYERS, firstXiaoFeiPlayers);
							logger.warn("[首次消费] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}else {
							logger.error("[首次消费奖励物品不存在] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}
					}
				}
			}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.ONETIME_XIAOFEI_TYPE) {
				//单次消费一定金额
				for (int i = 0; i < yinzi/xiaofeiConfig.getMoney(); i++) {
					Article a = ArticleManager.getInstance().getArticle(xiaofeiConfig.getPropName());
					if (a != null) {
						int colorType = xiaofeiConfig.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, xiaofeiConfig.isBind(), ArticleEntityManager.CREATE_REASON_XIAOFEI_ACTIVITY, player, colorType, 1, true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{xiaofeiConfig.getPropNum()}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "单次消费金额达到奖励活动");
						logger.warn("[单次消费达到奖励] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
					}else {
						logger.error("[单次消费物品不存在] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
					}
				}
			}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_XIAOFEI_TYPE) {
				//累计消费已经金额
				HashMap<Long, Long[]> pMoneys = xiaoFeiMoney2Id.get(xiaofeiConfig.getId());
				if (pMoneys == null) {
					pMoneys = new HashMap<Long, Long[]>();
					xiaoFeiMoney2Id.put(xiaofeiConfig.getId(), pMoneys);
				}
				Long[] moneys = pMoneys.get(player.getId());
				if (moneys == null) {
					if (yinzi >= xiaofeiConfig.getMoney()) {
						Article a = ArticleManager.getInstance().getArticle(xiaofeiConfig.getPropName());
						if (a != null) {
							int colorType = xiaofeiConfig.getColorType();
							if (colorType < 0) {
								colorType = a.getColorType();
							}
							ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, xiaofeiConfig.isBind(), ArticleEntityManager.CREATE_REASON_XIAOFEI_ACTIVITY, player, colorType, 1, true);
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{xiaofeiConfig.getPropNum()}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费奖励活动");
							logger.warn("[累计消费达到奖励] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}else {
							logger.error("[累计消费物品不存在] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}
					}
				}else {
					long oldMoney = 0;
					for (int i = 0; i < moneys.length; i++) {
						oldMoney += moneys[i];
					}
					if (oldMoney < xiaofeiConfig.getMoney() && oldMoney + yinzi >= xiaofeiConfig.getMoney()) {
						Article a = ArticleManager.getInstance().getArticle(xiaofeiConfig.getPropName());
						if (a != null) {
							int colorType = xiaofeiConfig.getColorType();
							if (colorType < 0) {
								colorType = a.getColorType();
							}
							ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, xiaofeiConfig.isBind(), ArticleEntityManager.CREATE_REASON_XIAOFEI_ACTIVITY, player, colorType, 1, true);
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{xiaofeiConfig.getPropNum()}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费奖励活动");
							logger.warn("[累计消费达到奖励] [{}] [old={}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), oldMoney, yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}else {
							logger.error("[累计消费物品不存在] [{}] [old={}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), oldMoney, yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
						}
					}
				}
				
				if (moneys == null) {
					moneys = new Long[XiaoFeiServerConfig.XIAOFEI_TONGDAO_SIZE];
					for (int i = 0; i < moneys.length; i++) {
						moneys[i] = 0L;
					}
					moneys[xiaofeiType] = yinzi;
					pMoneys.put(player.getId(), moneys);
				}else {
					moneys[xiaofeiType] = moneys[xiaofeiType] + yinzi;
					pMoneys.put(player.getId(), moneys);
				}
				diskCache.put(TOTAL_XIAOFEI_PLAYERS, xiaoFeiMoney2Id);
			}else if (xiaofeiConfig.getType() == XiaoFeiServerConfig.TOTAL_TIMES_XIAOFEI_TYPE) {
				//累计消费金额，多次
				Long money = xiaoFeiMoneys.get(player.getId());
				if (money == null) {
					money = yinzi;
				}else {
					money += yinzi;
				}
				
				int num = (int)(money/xiaofeiConfig.getMoney());
				money -= xiaofeiConfig.getMoney() * num;
				num = num * xiaofeiConfig.getPropNum();
				Article a = ArticleManager.getInstance().getArticle(xiaofeiConfig.getPropName());
				if (num > 0) {
					if (a != null) {
						int colorType = xiaofeiConfig.getColorType();
						if (colorType < 0) {
							colorType = a.getColorType();
						}
						ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, xiaofeiConfig.isBind(), ArticleEntityManager.CREATE_REASON_XIAOFEI_ACTIVITY, player, colorType, num, true);
						if (a.isOverlap()) {
							while (num > 0) {
								if (num > a.getOverLapNum()) {
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{a.getOverLapNum()}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费多次金额达到奖励活动");
								}else {
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{num}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费多次金额达到奖励活动");
								}
								num = num - a.getOverLapNum();
							}
						}else {
							while (num > 0) {
								if (num > 5) {
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity,entity,entity,entity,entity}, new int[]{1,1,1,1,1}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费多次金额达到奖励活动");
								}else {
									for (int i = 0; i < num; i++) {
										MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{entity}, new int[]{1}, xiaofeiConfig.getMailTitle(), xiaofeiConfig.getMailMsg(), 0, 0, 0, "累计消费多次金额达到奖励活动");
									}
								}
								num = num - 5;
							}
						}
						logger.warn("[累计消费多次达到奖励] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
					}else {
						logger.error("[累计消费多次物品不存在] [{}] [yz={}] [xt={}] [{}]", new Object[]{player.getLogString(), yinzi, xiaofeiType, xiaofeiConfig.toLogString()});
					}
				}
				xiaoFeiMoneys.put(player.getId(), money);
				diskCache.put(TOTAL_XIAOFEI_MONEYS, xiaoFeiMoneys);
			}
		}
	}
	
	public void doChongZhiMoneyActivity(Player player, long yinzi) {
		try{
			if (!isMoneyBillboardStart) {
				return;
			}
			if (chongZhiBillBoard != null) {
				if (!chongZhiBillBoard.isStart()) {
					if (chongZhiBillBoardMsg.size() > 0) {
						chongZhiBillBoardMsg.clear();
						diskCache.put(CHONGZHI_BILLBOARD, chongZhiBillBoardMsg);
						isChongZhiEndOver = false;
						diskCache.put(CHONGZHI_ENDOVER, isChongZhiEndOver);
					}
				}else {
					if (chongZhiBillBoard.isEndToLong()) {
						if (chongZhiBillBoardMsg.size() > 0) {
							chongZhiBillBoardMsg.clear();
							diskCache.put(CHONGZHI_BILLBOARD, chongZhiBillBoardMsg);
							isChongZhiEndOver = false;
							diskCache.put(CHONGZHI_ENDOVER, isChongZhiEndOver);
						}
					}else if (!chongZhiBillBoard.isEnd()) {
						Long oldMoney = chongZhiBillBoardMsg.get(player.getId());
						long a = 0;
						if (oldMoney == null) {
							chongZhiBillBoardMsg.put(player.getId(), yinzi);
						}else {
							a = oldMoney;
							chongZhiBillBoardMsg.put(player.getId(), oldMoney + yinzi);
						}
						diskCache.put(CHONGZHI_BILLBOARD, chongZhiBillBoardMsg);
						logger.warn("[参加充值排行活动] [{}] [{}] [{}]", new Object[]{player.getLogString(), a +"~~" + yinzi, chongZhiBillBoard.toLogString()});
					}
				}
			}
		}catch(Exception e) {
			logger.error("[参与充值排行出错] [{}] [{}]", new Object[]{player.getLogString(), yinzi});
		}
	}
	
	public void doXiaoFeiMoneyActivity(Player player, long yinzi, int type) {
		try{
			if (!isMoneyBillboardStart) {
				return;
			}
			if (xiaoFeiBillBoard != null) {
				boolean isType = false;
				for (int i = 0 ; i < xiaoFeiBillBoard.getParameters().length; i++) {
					int xiaofeiType = Integer.parseInt(xiaoFeiBillBoard.getParameters()[i]);
					if (type == xiaofeiType) {
						isType = true;
					}
				}
				if (isType) {
					if (!xiaoFeiBillBoard.isStart()) {
						if (xiaoFeiBillBoardMsg.size() > 0) {
							xiaoFeiBillBoardMsg.clear();
							diskCache.put(XIAOFEI_BILLBOARD, xiaoFeiBillBoardMsg);
							isXiaoFeiEndOver = false;
							diskCache.put(XIAOFEI_ENDOVER, isXiaoFeiEndOver);
						}
					}else {
						if (xiaoFeiBillBoard.isEndToLong()) {
							if (xiaoFeiBillBoardMsg.size() > 0) {
								xiaoFeiBillBoardMsg.clear();
								diskCache.put(XIAOFEI_BILLBOARD, xiaoFeiBillBoardMsg);
								isXiaoFeiEndOver = false;
								diskCache.put(XIAOFEI_ENDOVER, isXiaoFeiEndOver);
							}
						}else if (!xiaoFeiBillBoard.isEnd()) {
							Long oldMoney = xiaoFeiBillBoardMsg.get(player.getId());
							long a = 0;
							if (oldMoney == null) {
								xiaoFeiBillBoardMsg.put(player.getId(), yinzi);
							}else {
								a = oldMoney;
								xiaoFeiBillBoardMsg.put(player.getId(), oldMoney + yinzi);
							}
							diskCache.put(XIAOFEI_BILLBOARD, xiaoFeiBillBoardMsg);
							logger.warn("[参加消费排行活动] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), a +"~~" + yinzi, type, xiaoFeiBillBoard.toLogString()});
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error("[参与消费排行出错] [{}] [{}] [{}]", new Object[]{player.getLogString(), yinzi, type});
		}
	}
	
	public List<MoneyBillBoardData> getChongZhiSort(int num) {
		ArrayList<MoneyBillBoardData> re = new ArrayList<ChongZhiActivity.MoneyBillBoardData>(chongZhiBillBoardMsg.size());
		for (Long key : chongZhiBillBoardMsg.keySet()) {
			Long money = chongZhiBillBoardMsg.get(key);
			MoneyBillBoardData data = new MoneyBillBoardData();
			data.setMoney(money);
			data.setPlayerID(key);
			re.add(data);
		}
		Collections.sort(re);
		if (re.size() < num) {
			num = re.size();
		}
		List<MoneyBillBoardData> aaa = re.subList(0, num);
		
		logger.warn("[查询充值排行] [{}] [{}]", new Object[]{num, aaa.toString()});
		
		return aaa;
	}
	
	public List<MoneyBillBoardData> getXiaoFeiSort(int num) {
		ArrayList<MoneyBillBoardData> re = new ArrayList<ChongZhiActivity.MoneyBillBoardData>(xiaoFeiBillBoardMsg.size());
		for (Long key : xiaoFeiBillBoardMsg.keySet()) {
			Long money = xiaoFeiBillBoardMsg.get(key);
			MoneyBillBoardData data = new MoneyBillBoardData();
			data.setMoney(money);
			data.setPlayerID(key);
			re.add(data);
		}
		Collections.sort(re);
		if (re.size() < num) {
			num = re.size();
		}
		List<MoneyBillBoardData> aaa = re.subList(0, num);
		
		logger.warn("[查询消费排行] [{}] [{}]", new Object[]{num, aaa.toString()});
		
		return aaa;
	}
	
	public static void setInstance(ChongZhiActivity instance) {
		ChongZhiActivity.instance = instance;
	}

	public static ChongZhiActivity getInstance() {
		return instance;
	}

	public void setChongZhiActivityStart(boolean isActivityStart) {
		this.isChongZhiActivityStart = isActivityStart;
	}

	public boolean isChongZhiActivityStart() {
		return isChongZhiActivityStart;
	}
	
	public void chanageOne(int index, int activityID, String serverName, int type, String startTime, String endTime, long money, String propName, int propNum, int propColor, boolean isBind, String mailTitle, String mailMsg) {
		if (index < 0 || index > chongZhiServerConfigs.size()) {
			return;
		}
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ChongZhiServerConfig config = chongZhiServerConfigs.get(index);
			String oldLog = config.toLogString();
			long start_time = format.parse(startTime).getTime();
			long end_time = format.parse(endTime).getTime();
			config.setActivityID(activityID);
			config.setServerName(serverName);
			config.setType(type);
			config.setStartTime(startTime);
			config.setStart_time(start_time);
			config.setEndTime(endTime);
			config.setEnd_time(end_time);
			config.setMoney(money);
			config.setPropName(propName);
			config.setPropNum(propNum);
			config.setColorType(propColor);
			config.setBind(isBind);
			config.setMailTitle(mailTitle);
			config.setMailMsg(mailMsg);
			logger.warn("[修改充值活动] [老的{}] [新的{}]", new Object[]{oldLog, config.toLogString()});
		}catch(Exception e) {
			logger.error("chanageOne出错:", e);
		}
	}
	
	public void createOne(int activityID, String serverName, int type, String startTime, String endTime, long money, String propName, int propNum, int propColor, boolean isBind, String mailTitle, String mailMsg) {
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ChongZhiServerConfig config = new ChongZhiServerConfig();
			long start_time = format.parse(startTime).getTime();
			long end_time = format.parse(endTime).getTime();
			config.setActivityID(activityID);
			config.setServerName(serverName);
			config.setType(type);
			config.setStartTime(startTime);
			config.setStart_time(start_time);
			config.setEndTime(endTime);
			config.setEnd_time(end_time);
			config.setMoney(money);
			config.setPropName(propName);
			config.setPropNum(propNum);
			config.setColorType(propColor);
			config.setBind(isBind);
			config.setMailTitle(mailTitle);
			config.setMailMsg(mailMsg);
			chongZhiServerConfigs.add(config);
			logger.warn("[页面创建一个新充值活动] [{}]", new Object[]{config.toLogString()});
		}catch(Exception e) {
			logger.error("createOne出错:", e);
		}
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public void setFirstplayers(ArrayList<Long> firstplayers) {
		this.firstplayers = firstplayers;
	}

	public ArrayList<Long> getFirstplayers() {
		return firstplayers;
	}

	public void setChongZhiMoney2Id(HashMap<Integer, HashMap<Long, Long>> chongZhiMoney2Id) {
		this.chongZhiMoney2Id = chongZhiMoney2Id;
	}

	public HashMap<Integer, HashMap<Long, Long>> getChongZhiMoney2Id() {
		return chongZhiMoney2Id;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public void setFirstXiaoFeiPlayers(ArrayList<Long> firstXiaoFeiPlayers) {
		this.firstXiaoFeiPlayers = firstXiaoFeiPlayers;
	}

	public ArrayList<Long> getFirstXiaoFeiPlayers() {
		return firstXiaoFeiPlayers;
	}

	public void setXiaoFeiMoney2Id(HashMap<Long, HashMap<Long, Long[]>> xiaoFeiMoney2Id) {
		this.xiaoFeiMoney2Id = xiaoFeiMoney2Id;
	}

	public HashMap<Long, HashMap<Long, Long[]>> getXiaoFeiMoney2Id() {
		return xiaoFeiMoney2Id;
	}

	public HashMap<Long, Long> getXiaoFeiMoneys() {
		return xiaoFeiMoneys;
	}

	public void setXiaoFeiMoneys(HashMap<Long, Long> xiaoFeiMoneys) {
		this.xiaoFeiMoneys = xiaoFeiMoneys;
	}
	
	public void setXiaoFeiActivityStart(boolean isXiaoFeiActivityStart) {
		this.isXiaoFeiActivityStart = isXiaoFeiActivityStart;
	}

	public boolean isXiaoFeiActivityStart() {
		return isXiaoFeiActivityStart;
	}

	@Override
	public void run() {
		while(isRunning) {
			try{
				long startTime = System.currentTimeMillis();
				try{
					if (fanliActivity != null) {
						if (fanliActivity.getLingqu_Time() < startTime) {
							//活动结束
							if (totalChongZhiMoney2Id.size() > 0){
								for (Long key : totalChongZhiMoney2Id.keySet()) {
									if (totalChongZhiMoney2Id.get(key) != 0 ){
										logger.warn("[玩家未领取返利] [{}] [{}]", new Object[]{key, totalChongZhiMoney2Id.get(key)});
									}
								}
								totalChongZhiMoney2Id.clear();
								diskCache.put(TOTAL_FANLI_MONEYS, (Serializable) totalChongZhiMoney2Id);
							}
						}
					}
				}catch(Exception e) {
					logger.error("累计返利出错", e);
				}
				
				try{
					int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
					if (nowDay != today_data && todayChongZhiMoney2Id.size() > 0) {
						if (todayChongZhiActivity == null) {
							findTodayChongZhiFanLiActivity();
						}
						if (todayChongZhiActivity != null) {
							HashMap<Long, Long> money2Id = new HashMap<Long, Long>();
							synchronized (todayChongZhiMoney2Id) {
								money2Id.putAll(todayChongZhiMoney2Id);
								todayChongZhiMoney2Id.clear();
								diskCache.put(EVERYDAY_MONEYS, todayChongZhiMoney2Id);
							}
							for (Long keyid : money2Id.keySet()) {
								try{
									Long money = money2Id.get(keyid);
									money = money * (Integer.parseInt(todayChongZhiActivity.getPropName())) / 10000;
									MailManager.getInstance().sendMail(keyid, null, todayChongZhiActivity.getMailTitle(), todayChongZhiActivity.getMailMsg(), money, 0, 0, "");
									logger.warn("[充值返利活动奖励] [id={}] [充={}] [奖={}]", new Object[]{keyid, money2Id.get(keyid), money});
								}catch(Exception e) {
									logger.error("发送每天返利邮件出错:" + keyid,e );
								}
							}
						}
					}
					today_data = nowDay;
				}catch(Exception e) {
					logger.error("处理每天返利活动出错:",e );
				}
				try{
					long now = System.currentTimeMillis();
					for (int i =0 ; i < chongZhiServerConfigs.size(); i++) {
						ChongZhiServerConfig config = chongZhiServerConfigs.get(i);
						if (config.getType() == ChongZhiServerConfig.CHONGZHI_FANLI_TODAY_TYPE) {
							ChongZhiActivityFanLiToDay activity = (ChongZhiActivityFanLiToDay)config;
							int timeIndex = activity.findSendTimeIndex(now);
							if (timeIndex >= 0) {
								//需要发放奖励
								Long lastSendMail = chongzhiFanLiTimes.get(config.getActivityID());
								if (lastSendMail == null) {
									lastSendMail = new Long(0);
								}
								
								if (now - lastSendMail > activity.getFanliSpaces()[timeIndex]) {
									//发奖励
									lastSendMail = now;
									chongzhiFanLiTimes.put(config.getActivityID(), lastSendMail);
									diskCache.put(CHONGZHI_YIDONG_SEND, chongzhiFanLiTimes);
									HashMap<Long, Long> playerMoneys = chongzhiFanLiMoneys.get(activity.getActivityID());
									if (playerMoneys != null) {
										logger.warn("[充值返利活动开始返利] [{}] [{}]", new Object[]{activity.toLogString(), playerMoneys.size()});
										for (Long playerid : playerMoneys.keySet()) {
											Long m = playerMoneys.get(playerid);
											try{
												long toMoney = m * activity.getFanliBiLi()[timeIndex] / 10000;
												if (toMoney == 0) {
													toMoney = 1;
												}
												MailManager.getInstance().sendMail(playerid, new ArticleEntity[]{}, activity.getMailTitle(), activity.getMailMsg(), toMoney, 0, 0, "充值返利");
												logger.warn("[给玩家发充值返利活动邮件] [{}] [{}] [{}] [{}] [{}]", new Object[]{playerid, activity.getActivityID(), activity.getServerName(), toMoney, m});
											}catch(Exception e) {
												logger.error("", e);
											}
										}
									}else {
										logger.warn("[返利充值活动没有人参加?] [{}] [{}]", new Object[]{activity.toLogString(), timeIndex});
									}
								}
							}else if (timeIndex == -2) {
								if (!activity.isStart()) {
									if (chongzhiFanLiTimes.get(config.getActivityID()) != null) {
										chongzhiFanLiTimes.remove(config.getActivityID());
										diskCache.put(CHONGZHI_YIDONG_SEND, chongzhiFanLiTimes);
									}
									if (chongzhiFanLiMoneys.get(config.getActivityID()) != null) {
										chongzhiFanLiMoneys.remove(config.getActivityID());
										diskCache.put(CHONGHZI_YIDONG, chongzhiFanLiMoneys);
									}
								}
							}
						}
					}
				}catch(Exception e) {
					logger.error("处理移动模式返利活动出错:",e );
				}
				try{
					if (chongZhiBillBoard != null) {
						if (!isChongZhiEndOver) {
							if (chongZhiBillBoard.isEnd()) {
								isChongZhiEndOver = true;
								diskCache.put(CHONGZHI_ENDOVER, isChongZhiEndOver);
								List<MoneyBillBoardData> lists = getChongZhiSort(chongZhiBillBoard.getPropNames().length);
								for (int i = 0; i < lists.size(); i++) {
									try{
										MoneyBillBoardData data = lists.get(i);
										Article a = ArticleManager.getInstance().getArticle(chongZhiBillBoard.getPropNames()[i]);
										if (a != null) {
											ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_MONEYBILLBOARD_ACTIVITY, null, a.getColorType(), 1, true);
											MailManager.getInstance().sendMail(data.getPlayerID(), new ArticleEntity[]{entity}, chongZhiBillBoard.getMailTitle(), chongZhiBillBoard.getMailMsg(), 0, 0, 0, "充值排行活动奖励");
											logger.warn("[充值排行奖励] [{}] [{}] [{}] [{}]", new Object[]{data.getPlayerID(), data.getMoney(), entity.getId(), chongZhiBillBoard.getPropNames()[i]});
										}
									}catch(Exception e) {
										logger.error("充值排行奖励发放出错:", e);
									}
								}
							}
						}
					}
				}catch(Exception e) {
					logger.error("充值排行run出错", e);
				}
				try{
					if (xiaoFeiBillBoard != null) {
						if (!isXiaoFeiEndOver) {
							if (xiaoFeiBillBoard.isEnd()) {
								isXiaoFeiEndOver = true;
								diskCache.put(XIAOFEI_ENDOVER, isXiaoFeiEndOver);
								List<MoneyBillBoardData> lists = getXiaoFeiSort(xiaoFeiBillBoard.getPropNames().length);
								for (int i = 0; i < lists.size(); i++) {
									try{
										MoneyBillBoardData data = lists.get(i);
										Article a = ArticleManager.getInstance().getArticle(xiaoFeiBillBoard.getPropNames()[i]);
										if (a != null) {
											ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_MONEYBILLBOARD_ACTIVITY, null, a.getColorType(), 1, true);
											MailManager.getInstance().sendMail(data.getPlayerID(), new ArticleEntity[]{entity}, xiaoFeiBillBoard.getMailTitle(), xiaoFeiBillBoard.getMailMsg(), 0, 0, 0, "消费排行活动奖励");
											logger.warn("[消费排行奖励] [{}] [{}] [{}] [{}]", new Object[]{data.getPlayerID(), data.getMoney(), entity.getId(), xiaoFeiBillBoard.getPropNames()[i]});
										}
									}catch(Exception e) {
										logger.error("消费排行奖励发放出错:", e);
									}
								}
							}
						}
					}
				}catch(Exception e) {
					logger.error("消费排行run出错", e);
				}
				long useTime = System.currentTimeMillis() - startTime;
				logger.warn("[心跳] [ms="+useTime+"] [{}] [{}]", new Object[]{isChongZhiEndOver, isXiaoFeiEndOver});
				if (useTime < runStep) {
					try{
						Thread.sleep(runStep - useTime);
					}catch(Exception e) {
						logger.error("[sleep出错:]", e);
					}
				}
			}catch(Exception e) {
				logger.error("[run出错:]", e);
			}
		}
	}
	
	//查询返利
	public void option_FanLi_ChaXun(Player player) {
		try{
			if (fanliActivity == null) {
				player.sendError("此服务器未配置此活动。");
				return ;
			}
			long now = System.currentTimeMillis();
			if (fanliActivity.getStart_time() >= now || now >= fanliActivity.getLingqu_Time()) {
				player.sendError("此服务器活动还未开始或已经结束。");
				return;
			}
			Long money = totalChongZhiMoney2Id.get(player.getId());
			long chongzhi = 0;
			if (money != null) {
				chongzhi = money;
			}
			long hongbao = chongzhi * Integer.parseInt(fanliActivity.getPropName()) / 10000;
			String msg = "活动期间，您已充值" + TradeManager.putMoneyToMyText(chongzhi) + "\n"
			+"您的红包金额已有" + TradeManager.putMoneyToMyText(hongbao) + "\n"
			+"注:红包奖励可在\n<f color='0xffff00'>" + fanliActivity.getEndTime() + "</f>-<f color='0xffff00'>" + fanliActivity.getLingquTime() + "</f>在<f color='0xffff00'>财神</f>处领取。\n<f color='0xff0000'>红包逾期清零，请一定按时领取哦~</f>";
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(20);
			CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, new Option[]{});
			player.addMessageToRightBag(req);
		}catch(Exception e) {
			logger.error("option_FanLi_ChaXun出错", e);
		}
	}
	
	//领取返利
	public void option_FanLi_LinQu(Player player) {
		try{
			if (fanliActivity == null) {
				player.sendError("此服务器未配置此活动。");
				return;
			}
			if (fanliActivity.getLingqu_Time() < System.currentTimeMillis()) {
				player.sendError("以过了领取时间了，不能领取红包了。");
				return;
			}
			if (!fanliActivity.canLingQu()) {
				long spaceTime = fanliActivity.getEnd_time() - System.currentTimeMillis();
				long day = spaceTime/1000/60/60/24;
				long hore = spaceTime/1000/60/60%24;
				String msg = "还有<f color='0xffff00'>"+day+"天"+hore+"小时"+"</f>就可以领取红包了哦^_^\n注:红包奖励可在\n<f color='0xffff00'>"+fanliActivity.getEndTime()+"</f>—<f color='0xffff00'>"+fanliActivity.getLingquTime()+"</f>领取。\n<f color='0xff0000'>红包逾期清零，请一定按时领取哦~</f>";
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(20);
				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, new Option[]{});
				player.addMessageToRightBag(req);
				return;
			}
			
			Long money = totalChongZhiMoney2Id.get(player.getId());
			if (money == null) {
				player.sendError("您没有在活动期间内充值过。");
				return;
			}
			if (money == 0) {
				player.sendError("您已经领取过红包了");
				return;
			}
			long hongbao = money * Integer.parseInt(fanliActivity.getPropName()) / 10000;
			totalChongZhiMoney2Id.put(player.getId(), 0L);
			try{
				BillingCenter.getInstance().playerSaving(player, hongbao, CurrencyType.YINZI, SavingReasonType.充值活动奖励, "累计充值奖励返利");
				player.sendError("红包领取成功，共获得" + TradeManager.putMoneyToMyText(hongbao));
				logger.warn("[红包领取成功] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), hongbao, money, fanliActivity.toLogString()});
			}catch(Exception e){
				logger.error("充值返利红包出错" + player.getLogString() + "~~~" + hongbao, e);
			}
			MailManager.getInstance().sendMail(player.getId(), null, fanliActivity.getMailTitle(), Translate.translateString(fanliActivity.getMailMsg(), new String[][]{{Translate.COUNT_1, hongbao + ""}}), 0, 0, 0, "累计充值返利");
		}catch(Exception e) {
			logger.error("option_FanLi_LinQu出错", e);
		}
	}

	public void setMoneyBillboardStart(boolean isMoneyBillboardStart) {
		this.isMoneyBillboardStart = isMoneyBillboardStart;
	}

	public boolean isMoneyBillboardStart() {
		return isMoneyBillboardStart;
	}

	public void setChongZhiBillBoardMsg(HashMap<Long, Long> chongZhiBillBoardMsg) {
		this.chongZhiBillBoardMsg = chongZhiBillBoardMsg;
	}

	public HashMap<Long, Long> getChongZhiBillBoardMsg() {
		return chongZhiBillBoardMsg;
	}

	public void setXiaoFeiBillBoardMsg(HashMap<Long, Long> xiaoFeiBillBoardMsg) {
		this.xiaoFeiBillBoardMsg = xiaoFeiBillBoardMsg;
	}

	public HashMap<Long, Long> getXiaoFeiBillBoardMsg() {
		return xiaoFeiBillBoardMsg;
	}
	
	public void setTodayChongZhiMoney2Id(HashMap<Long, Long> todayChongZhiMoney2Id) {
		this.todayChongZhiMoney2Id = todayChongZhiMoney2Id;
	}

	public HashMap<Long, Long> getTodayChongZhiMoney2Id() {
		return todayChongZhiMoney2Id;
	}

	public void setToday_data(int today_data) {
		this.today_data = today_data;
	}

	public int getToday_data() {
		return today_data;
	}

	public void setTotalChongZhiMoney2Id(HashMap<Long, Long> totalChongZhiMoney2Id) {
		this.totalChongZhiMoney2Id = totalChongZhiMoney2Id;
	}

	public HashMap<Long, Long> getTotalChongZhiMoney2Id() {
		return totalChongZhiMoney2Id;
	}

	public static class MoneyBillBoardData implements Comparable<MoneyBillBoardData> {
		private long playerID;
		private long money;
		
		public int compareTo(MoneyBillBoardData o) {
			if (o.getMoney() > this.getMoney()) {
				return 1;
			}
			return -1;
		}
		
		public void setPlayerID(long playerID) {
			this.playerID = playerID;
		}
		public long getPlayerID() {
			return playerID;
		}
		public void setMoney(long money) {
			this.money = money;
		}
		public long getMoney() {
			return money;
		}
		
		public String toString() {
			return playerID + "~" + money;
		}
	}
}
