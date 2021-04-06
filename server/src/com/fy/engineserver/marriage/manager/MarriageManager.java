package com.fy.engineserver.marriage.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.activity.base.SendFlowerActivityConfig;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.activity.newChongZhiActivity.NewXiaoFeiActivity;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.marriage.MarriageBeq;
import com.fy.engineserver.marriage.MarriageDownCity;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.MarriageLevel;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.marriage.Option_LiHunOk;
import com.fy.engineserver.menu.marriage.Option_LihunOther;
import com.fy.engineserver.menu.marriage.Option_MarriageDelayOK;
import com.fy.engineserver.menu.marriage.Option_Marriage_Beq2Other;
import com.fy.engineserver.menu.marriage.Option_Marriage_BeqOK;
import com.fy.engineserver.menu.marriage.Option_Marriage_req;
import com.fy.engineserver.menu.marriage.Option_Marriage_time;
import com.fy.engineserver.menu.marriage.Option_Song;
import com.fy.engineserver.menu.marriage.Option_biYiLing;
import com.fy.engineserver.menu.marriage.Option_biYiLing_cancle;
import com.fy.engineserver.menu.marriage.Option_joinHunLi;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GET_MARRIAGE_FRIEND_REQ;
import com.fy.engineserver.message.GET_MARRIAGE_FRIEND_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.HORSE_RIDE_RES;
import com.fy.engineserver.message.MARRIAGE_ASSIGN_CHOOSE1_RES;
import com.fy.engineserver.message.MARRIAGE_ASSIGN_CHOOSE2_RES;
import com.fy.engineserver.message.MARRIAGE_ASSIGN_RES;
import com.fy.engineserver.message.MARRIAGE_BEQSTART_REQ;
import com.fy.engineserver.message.MARRIAGE_BEQ_FLOWER_RES;
import com.fy.engineserver.message.MARRIAGE_CANCEL_GUEST_RES;
import com.fy.engineserver.message.MARRIAGE_CANCEL_RES;
import com.fy.engineserver.message.MARRIAGE_CHOOSE_GUEST_RES;
import com.fy.engineserver.message.MARRIAGE_DELAYTIME_BEGIN_RES;
import com.fy.engineserver.message.MARRIAGE_FINISH_RES;
import com.fy.engineserver.message.MARRIAGE_GUEST_OVER_RES;
import com.fy.engineserver.message.MARRIAGE_JIAOHUAN2OTHER_RES;
import com.fy.engineserver.message.MARRIAGE_JIAOHUAN_RES;
import com.fy.engineserver.message.MARRIAGE_JOIN_HUNLI_SCREEN_RES;
import com.fy.engineserver.message.MARRIAGE_MENU;
import com.fy.engineserver.message.MARRIAGE_TARGET_MENU_RES;
import com.fy.engineserver.message.MARRIAGE_TIME_RES;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.SET_DEFAULT_HORSE_RES;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newBillboard.BillboardStatDateManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.society.Player_RelatinInfo;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.trade.TradeManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 
 * 
 */
public class MarriageManager implements Runnable {

	public static long ONE_HOUR = 1 * 60 * 60 * 1000L;
	// public static long ONE_HOUR = 5 * 60*1000L;

	private final long MARRIAGE_SPACE_TIME = 60 * 1000; // 请求结婚做1分钟时间间隔

	private final long MARRIAGE_DROP_SPACE_TIME = 20 * 1000; // 掉落物品的时间间隔

	public static long LIHUN_SPACETIME = 24 * 60 * 60 * 1000L;
	// public static long LIHUN_SPACETIME = 60 * 1000L;

	private final String DROP_ICON1 = "budai1"; 
	private final String DROP_ICON2 = "baoxiang1";

	// public static Logger logger = Logger.getLogger(MarriageManager.class);
	public static Logger logger = LoggerFactory.getLogger(MarriageManager.class);

	private static MarriageManager instance;

	public SimpleEntityManager<MarriageInfo> emInfo;
	public SimpleEntityManager<MarriageDownCity> emCity;
	public SimpleEntityManager<MarriageBeq> emBeq;

	private String file;

	private List<String> marriageCityName = new ArrayList<String>();

	private Hashtable<Long, MarriageDownCity> downCityMap = new Hashtable<Long, MarriageDownCity>(); // 结婚副本

	private Hashtable<Long, Long> player2CityMap = new Hashtable<Long, Long>(); // 邀请玩家将要去的结婚场景

	private Hashtable<Long, MarriageBeq> beqMap = new Hashtable<Long, MarriageBeq>(); // 求婚数据

	private Hashtable<Long, MarriageInfo> infoMap = new Hashtable<Long, MarriageInfo>(); // 结婚数据

	private Hashtable<Long, Long> marriageReqTimes = new Hashtable<Long, Long>(); // 结婚请求的数据

	public Hashtable<Long, Game> hunfangGameMap = new Hashtable<Long, Game>(); // 婚房
	private long hunfangCreatTime;

	public List<MarriageActivity> marriageActivitys = new ArrayList<MarriageActivity>();

	public static String beq_map_name = "kunlunshengdian"; // 求婚后愿意传送的场景
	public static int beq_map_x = 350;
	public static int beq_map_y = 2240;

	// 婚礼在举行前各个阶段给宾客广播
	// private static long[] hunli_time = new long[]{3*60*1000, 60*1000, 0};
	public static long[] hunli_time = new long[] { 30 * 60 * 1000, 15 * 60 * 1000, 10 * 60 * 1000, 5 * 60 * 1000, 60 * 1000, 0 };

	public final byte BEQ_TYPE_HUA = 0; // 花
	public final byte BEQ_TYPE_TANG = 1; // 糖

	public static String[] nanBeq_name; // 求婚物品名称男 花
	public static String[] nvBeq_name; // 求婚物品名称女 糖
	public static long[] Beq_price; // 求婚物品价格
	public static int[] Beq_num; // 需要的数目

	public static int[] flowOrSweet_billboardValue = new int[] { 1, 2, 3 };

	public static String[] Song_nan_name;
	public static String[] Song_nv_name;
	public static long[] Song_price;
	public static int[] Song_Num = new int[] { 1, 9, 99 };

	// private final long time_scale = 14*60*1000; //结婚纪念日14天代表1年
	private final long time_scale = 14 * 24 * 60 * 60 * 1000; // 结婚纪念日14天代表1年
	private static int[] marriage_year; // 结婚纪念日
	private static String[] marriage_name; // 结婚纪念日名称
	private static String[][] marriage_propName; // 奖励物品名称
	private static int[][] marriage_propNum; // 奖励物品数目

	public static MarriageLevel[] marriageLevels; // 婚礼规模

	private final int LIHUN_PRICE = 500000; // 强制离婚

	private static int REQ_MARRIAGE = 10000; // 求婚花钱

	public static int GET_RING_MONEY = 10000; // 重另结婚戒指10两

	private Hashtable<String, ArticleEntity> tempArticleEntity = new Hashtable<String, ArticleEntity>();

	public String marriageActivityStartTime = "2017-02-10 00:00:00";
	public String marriageActivityEndTime = "2018-08-19 23:59:59";
	public long marriageActivityStartTimeL = 0L;
	public long marriageActivityEndTimeL = 0L;
	public String[] marriageActivityNames = new String[] { Translate.普通喜酒礼包, Translate.白银喜酒礼包, Translate.黄金喜酒礼包, Translate.白金喜酒礼包, Translate.钻石喜酒礼包 };
	public int[] marriageActivityColor = new int[] { 4, 4, 4, 4, 4 };
	public int[] marriageActivityNum = new int[] { 1, 1, 1, 1, 1 };
	public boolean isOpenMarriageActivity = true;

	public ArticleEntity getTempArticleEntity(String articleName) {
		ArticleEntity entity = tempArticleEntity.get(articleName);
		if (entity == null) {
			Article article = ArticleManager.getInstance().getArticle(articleName);
			if (article == null) {
				if (logger.isWarnEnabled()) logger.warn("创建结婚需要用到得临时Entity出错物品名字：{}", new Object[] { articleName });
				return null;
			}
			try {
				entity = ArticleEntityManager.getInstance().createTempEntity(article, false, ArticleEntityManager.CREATE_REASON_MARRIAGE, null, 0);
				tempArticleEntity.put(articleName, entity);
			} catch (Exception ex) {
				logger.error("结婚取临时Entity出错:", ex);
			}
		}
		return entity;
	}

	public void destroy() {
		emBeq.destroy();
		emInfo.destroy();
		emCity.destroy();
	}

	public void init() throws Exception {
		
		instance = this;
		loadFile();
		emInfo = SimpleEntityManagerFactory.getSimpleEntityManager(MarriageInfo.class);
		emBeq = SimpleEntityManagerFactory.getSimpleEntityManager(MarriageBeq.class);
		emCity = SimpleEntityManagerFactory.getSimpleEntityManager(MarriageDownCity.class);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (marriageActivityStartTimeL == 0) {
				marriageActivityStartTimeL = format.parse(marriageActivityStartTime).getTime();
			}
			if (marriageActivityEndTimeL == 0) {
				marriageActivityEndTimeL = format.parse(marriageActivityEndTime).getTime();
			}
		} catch (Exception e) {
			logger.error("处理结婚送酒活动时间出错", e);
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		List<MarriageInfo> list1 = new ArrayList<MarriageInfo>();
		try {
			long count = emInfo.count();
			int index = 1;
			while (count > 5000) {
				list1.addAll(emInfo.query(MarriageInfo.class, "", new Object[] {}, "", index, index + 5000));
				count -= 5000;
				index += 5000;
			}
			if (count > 0) {
				list1.addAll(emInfo.query(MarriageInfo.class, "", new Object[] {}, "", index, index + count));
			}
		} catch (Exception e) {
			logger.error("载入婚姻数据库出错:严重", e);
			return;
		}
		for (MarriageInfo info : list1) {
			if (info.getState() != MarriageInfo.STATE_JIEHUN && info.getState() != MarriageInfo.STATE_SHIBAI) {
				info.setCacheTime(SystemTime.currentTimeMillis());
				getInfoMap().put(info.getId(), info);
			}
		}
		List<MarriageBeq> list2 = new ArrayList<MarriageBeq>();
		try {
			long count = emBeq.count();
			int index = 1;
			while (count > 5000) {
				list2.addAll(emBeq.query(MarriageBeq.class, "", new Object[] {}, "", index, index + 5000));
				count -= 5000;
				index += 5000;
			}
			if (count > 0) {
				list2.addAll(emBeq.query(MarriageBeq.class, "", new Object[] {}, "", index, index + count));
			}
		} catch (Exception e) {
			logger.error("载入求婚数据库出错:严重", e);
			return;
		}
		for (MarriageBeq info : list2) {
			getBeqMap().put(info.getId(), info);
		}
		List<MarriageDownCity> list3 = new ArrayList<MarriageDownCity>();
		try {
			long count = emCity.count();
			int index = 1;
			while (count > 5000) {
				list3.addAll(emCity.query(MarriageDownCity.class, "", new Object[] {}, "", index, index + 5000));
				count -= 5000;
				index += 5000;
			}
			if (count > 0) {
				list3.addAll(emCity.query(MarriageDownCity.class, "", new Object[] {}, "", index, index + count));
			}
		} catch (Exception e) {
			logger.error("载入结婚场景数据库出错:严重", e);
			return;
		}
		for (MarriageDownCity city : list3) {
			getDownCityMap().put(city.getId(), city);
		}

		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			String serverName = GameConstants.getInstance().getServerName();
			ArrayList<String> unServerNames = new ArrayList<String>();
			if (!unServerNames.contains(serverName)) {
				isOpenMarriageActivity = true;
			}
			if (serverName.equals("国内本地测试")) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					marriageActivityStartTimeL = format.parse("2013-10-08 00:00:00").getTime();
					marriageActivityEndTimeL = format.parse(marriageActivityEndTime).getTime();
				} catch (Exception e) {
					logger.error("国内本地测试", e);
				}
			}
		}

		if (PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
			isOpenMarriageActivity = false;
			String serverName = GameConstants.getInstance().getServerName();
			if (serverName.equals("仙尊降世")) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					marriageActivityStartTimeL = format.parse("2013-10-08 00:00:00").getTime();
					marriageActivityEndTimeL = format.parse(marriageActivityEndTime).getTime();
				} catch (Exception e) {
					logger.error("国内本地测试", e);
				}
			}
		}

		if (PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
			isOpenMarriageActivity = true;
			String serverName = GameConstants.getInstance().getServerName();
			if (serverName.equals("化外之境") || serverName.equals("化外之境测试服") || serverName.equals("化外之境2")) {
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					marriageActivityStartTimeL = format.parse("2013-10-08 00:00:00").getTime();
					marriageActivityEndTimeL = format.parse(marriageActivityEndTime).getTime();
				} catch (Exception e) {
					logger.error("国内本地测试", e);
				}
			}
		}

		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			// 结婚送花活动
			MarriageActivity activity1 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-11-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 0, 0, new String[] { "ALL_SERVER" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束(宗派)" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity1);

			MarriageActivity activity2 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-11-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 1, 0, new String[] { "ALL_SERVER" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束(本国)" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity2);

			MarriageActivity activity3 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-11-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 2, 0, new String[] { "ALL_SERVER" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity3);

			MarriageActivity activity4 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-10-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 0, 0, new String[] { "国内本地测试" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束(宗派)" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity4);

			MarriageActivity activity5 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-10-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 1, 0, new String[] { "国内本地测试" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束(本国)" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity5);

			MarriageActivity activity6 = new MarriageActivity(TimeTool.formatter.varChar19.parse("2013-10-01 00:00:00"), TimeTool.formatter.varChar19.parse("2013-11-01 23:59:59"), 2, 0, new String[] { "国内本地测试" }, new String[] { "清风怡江", "风雨钟山", "龙骨平原" }, "脱光行动奖励", "恭喜您在小光棍节，勇敢的表达您的爱意，对您爱慕的人求婚，勇气可嘉，赠送奖励，请查收附件！", new String[] { "玫瑰花束" }, new int[] { 2 }, new int[] { -1 }, new boolean[] { true });
			marriageActivitys.add(activity6);
		}

		Thread t = new Thread(this, "MarriageInfoManager");
		t.start();
		logger.warn("[初始化MarriageInfoManager] [成功] [infoC=" + getInfoMap().size() + "] [beqC=" + getBeqMap().size() + "] [cityC=" + getDownCityMap().size() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + "ms]");
		ServiceStartRecord.startLog(this);
	}

	public static MarriageManager getInstance() {
		return instance;
	}

	public synchronized MarriageInfo getMarriageInfoById(long id) {
		MarriageInfo info = getInfoMap().get(id);
		if (info == null) {
			try {
				info = emInfo.find(id);
			} catch (Exception e) {
				logger.error("根据id取结婚信息出错：" + id, e);
			}
			if (info == null) {
				if (logger.isInfoEnabled()) logger.info("根据id取结婚信息不存在：" + id);
				return null;
			}
			getInfoMap().put(info.getId(), info);
		}
		info.setCacheTime(SystemTime.currentTimeMillis());
		return info;
	}

	@Override
	public void run() {
		List<MarriageBeq> deleteList = new ArrayList<MarriageBeq>();
		List<MarriageInfo> infoRemoveList = new ArrayList<MarriageInfo>();
		List<MarriageInfo> infoDeleteList = new ArrayList<MarriageInfo>();
		List<MarriageDownCity> cityDeleteList = new ArrayList<MarriageDownCity>();
		List<Long> deletetimeList = new ArrayList<Long>();
		while (true) {
			try {

				try {
					Thread.sleep(1000);
					if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
						continue;
					}
				} catch (InterruptedException e1) {
					logger.error("结婚线程休息出错??????", e1);
				}
				if (PlayerManager.getInstance() == null || ArticleManager.getInstance() == null) {
					continue;
				}
				// 结婚时间限制
				long now = System.currentTimeMillis();
				for (long l : marriageReqTimes.keySet()) {
					long value = marriageReqTimes.get(l);
					if (value + MARRIAGE_SPACE_TIME < now) {
						deletetimeList.add(l);
					}
				}
				for (long l : deletetimeList) {
					marriageReqTimes.remove(l);
				}
				deletetimeList.clear();
				// 结婚
				try {
					MarriageInfo[] marriageInfos = getInfoMap().values().toArray(new MarriageInfo[0]);
					for (MarriageInfo info : marriageInfos) {
						if (info.getState() == MarriageInfo.STATE_CHOOSE_TIME) {
							// 时间确定了，等待婚礼开始
							marriageCountDown(info);
						} else if (info.getState() == MarriageInfo.STATE_DROP) {
							// 交换戒指后，掉落喜糖等
							marriageDropProp(info);
						} else if (info.getState() == MarriageInfo.STATE_JIEHUN) {
							// 结婚周年
							marriageZhouNian(info);
						} else if (info.getState() == MarriageInfo.STATE_HUNLI_START) {
							// 婚礼开始
							if (System.currentTimeMillis() - info.getStartTime() > ONE_HOUR) {
								// 婚礼未能正常开始,通知其他在婚礼场景的玩家
								MarriageDownCity city = getDownCityMap().get(info.getCityId());
								if (city != null) {
									LivingObject[] living = city.getGame().getLivingObjects();
									for (int p = 0; p < living.length; p++) {
										if (living[p] instanceof Player) {
											Player player = (Player) living[p];
											player.send_HINT_REQ(Translate.text_marriage_婚礼场景以关闭婚礼结束, (byte) 5);
										}
									}
								}
								info.setCityId(0);
								if (info.getDelayNum() > 0) {
									// 已经延期过一次了,将不能再举行婚礼
									info.setState(MarriageInfo.STATE_JIEHUN);
									info.setSuccessTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
									logger.warn("[结婚2次延期 婚礼举行1小时了]" + info.getLogString());
								} else {
									info.setState(MarriageInfo.STATE_UNONLINE);
									info.setDelayNum(1);
									try {
										MailManager.getInstance().sendMail(info.getHoldA(), null, null, Translate.text_marriage_003, Translate.text_marriage_004, 0, 0, 0, "");
										MailManager.getInstance().sendMail(info.getHoldB(), null, null, Translate.text_marriage_003, Translate.text_marriage_004, 0, 0, 0, "");
									} catch (Exception e) {
										logger.error("婚礼推迟邮件发送失败:", e);
									}
									logger.warn("[结婚延期  婚礼举行1小时了]" + info.getLogString());
								}
							}
						}
					}
					marriageInfos = getInfoMap().values().toArray(new MarriageInfo[0]);
					for (MarriageInfo info : marriageInfos) {
						if (info.getState() == MarriageInfo.STATE_LIHUN) {
							infoDeleteList.add(info);
						} else if (info.getState() == MarriageInfo.STATE_SHIBAI || info.getState() == MarriageInfo.STATE_JIEHUN) {
							if (info.getCacheTime() + 5 * 60 * 1000 < SystemTime.currentTimeMillis()) {
								if ((!PlayerManager.getInstance().isOnline(info.getHoldA())) && (!PlayerManager.getInstance().isOnline(info.getHoldB()))) {
									infoRemoveList.add(info);
								}
							}
						}
					}
					for (MarriageInfo info : infoDeleteList) {
						getInfoMap().remove(info.getId());
						emInfo.remove(info);
					}
					for (MarriageInfo info : infoRemoveList) {
						getInfoMap().remove(info.getId());
						logger.warn("[移除出内存] [{}]", new Object[] { info.getLogString() });
						emInfo.save(info);
					}
				} catch (Exception e) {
					logger.error("结婚 run 关于婚礼开始和婚礼掉落物品的处理:", e);
				}
				infoRemoveList.clear();
				infoDeleteList.clear();
				try {
					// 求婚提示
					MarriageBeq[] beqs = getBeqMap().values().toArray(new MarriageBeq[0]);
					for (MarriageBeq beq : beqs) {
						if (beq.getState() == MarriageBeq.STATE_START) {
							if (PlayerManager.getInstance().isOnline(beq.getToId())) {
								sendBeqWindow(beq);
							}
						}
						if (beq.getState() == MarriageBeq.STATE_START1) {
							if (!PlayerManager.getInstance().isOnline(beq.getToId())) {
								beq.setState(MarriageBeq.STATE_START);
							}
						}
						if (beq.getState() == MarriageBeq.STATE_AGREE) {
							if (PlayerManager.getInstance().isOnline(beq.getSendId())) {
								Player from = null;
								try {
									from = PlayerManager.getInstance().getPlayer(beq.getSendId());
								} catch (Exception e) {
									beq.setState(MarriageBeq.STATE_OVER);
									logger.error("求婚数据出错FORM:" + beq.getSendId() + "~~TO:" + beq.getToId(), e);
									continue;
								}
								PlayerSimpleInfo to = PlayerSimpleInfoManager.getInstance().getInfoById(beq.getToId());
								if (to == null) {
									beq.setState(MarriageBeq.STATE_OVER);
									logger.error("求婚数据出错TO:" + beq.getSendId() + "~~TO:" + beq.getToId());
									continue;
								}
								MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
								Option_Cancel opt1 = new Option_Cancel();
								opt1.setText(Translate.确定);
								mw.setOptions(new Option[] { opt1 });
								String message = to.getName() + Translate.text_marriage_001;
								MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
								from.addMessageToRightBag(menu);
								beq.setSendTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
								beq.setState(MarriageBeq.STATE_OVER);
							}
						}
						if (beq.getState() == MarriageBeq.STATE_REFUSE) {
							if (PlayerManager.getInstance().isOnline(beq.getSendId())) {
								Player from = null;
								try {
									from = PlayerManager.getInstance().getPlayer(beq.getSendId());
								} catch (Exception e) {
									beq.setState(MarriageBeq.STATE_OVER);
									logger.error("求婚数据出错FORM:" + beq.getSendId() + "~~TO:" + beq.getToId(), e);
									continue;
								}
								PlayerSimpleInfo to = PlayerSimpleInfoManager.getInstance().getInfoById(beq.getToId());
								if (to == null) {
									beq.setState(MarriageBeq.STATE_OVER);
									logger.error("求婚数据出错TO:" + beq.getSendId() + "~~TO:" + beq.getToId());
									continue;
								}
								MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
								Option_Cancel opt1 = new Option_Cancel();
								opt1.setText(Translate.确定);
								mw.setOptions(new Option[] { opt1 });
								MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), to.getName() + Translate.text_marriage_002, mw.getOptions());
								from.addMessageToRightBag(menu);
								beq.setState(MarriageBeq.STATE_OVER);
							}
						}
						if (beq.getState() == MarriageBeq.STATE_OVER) {
							deleteList.add(beq);
							if (logger.isInfoEnabled()) logger.info("[求婚] [删除] [结束] [id={}] [{}] [from={}] [to={}]", new Object[] { beq.getId(), beq.getLevel(), beq.getSendId(), beq.getToId() });
						}
					}
					for (MarriageBeq beq : deleteList) {
						getBeqMap().remove(beq.getId());
						emBeq.remove(beq);
					}

				} catch (Exception e) {
					logger.error("求婚marriageBeq run出错:", e);
				}
				deleteList.clear();

				try {
					MarriageDownCity[] citys = getDownCityMap().values().toArray(new MarriageDownCity[0]);
					for (MarriageDownCity city : citys) {
						city.heartbeat();
					}
					citys = getDownCityMap().values().toArray(new MarriageDownCity[0]);
					for (MarriageDownCity city : citys) {
						if (city.getState() == MarriageDownCity.STATE_OVER) {
							cityDeleteList.add(city);
							if (logger.isInfoEnabled()) logger.info("[结婚场景] [删除] [id={}] [name={}] [startTime={}] [{}]", new Object[] { city.getId(), city.getMapName(), city.getStartTime(), getDownCityMap().size() });
						}
					}

					for (MarriageDownCity city : cityDeleteList) {
						getDownCityMap().remove(city.getId());
						emCity.remove(city);
						if (logger.isInfoEnabled()) logger.info("[结婚场景] [删除] [id={}] [name={}] [startTime={}] [size={}]", new Object[] { city.getId(), city.getMapName(), city.getStartTime(), getDownCityMap().size() });
					}

				} catch (Exception e) {
					logger.error("婚礼场景run出错:", e);
				}
				cityDeleteList.clear();

				try {
					List<Long> removeGame = new ArrayList<Long>();
					for (Long key : hunfangGameMap.keySet()) {
						Game game = hunfangGameMap.get(key);
						game.heartbeat();
						if (game.getNumOfPlayer() == 0 && hunfangCreatTime < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
							removeGame.add(key);
						}
					}
					for (Long key : removeGame) {
						hunfangGameMap.remove(key);
					}
				} catch (Exception e) {
					logger.error("结婚 婚房心跳出错:", e);
				}
				// if (logger.isInfoEnabled())
				// logger.info("[结婚心跳] [infoSize={}] [beqSize={}] [citySize={}] [耗时:{}]", new Object[]{getInfoMap().size(), getBeqMap().size(), getDownCityMap().size(),
				// System.currentTimeMillis() - now});
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("结婚线程出错:", e);
			}
		}
	}

	// 结婚倒计时
	private void marriageCountDown(MarriageInfo info) {
		for (int i = hunli_time.length; --i >= 0;) {
			// 是否到婚礼广播进场时间 以及是否发过这个邀请
			if (info.getStartTime() - hunli_time[i] < SystemTime.currentTimeMillis() && info.getSendMessage2GuestTimeIndex() < i) {
				info.setSendMessage2GuestTimeIndex(i);
				if (info.getCityId() <= 0 || getDownCityMap().get(info.getCityId()) == null) {
					// 创建结婚副本场景
					String cityName = marriageLevels[info.getLevel()].getCityName();
					long id = 0;
					try {
						id = emCity.nextId();
					} catch (Exception e) {
						logger.error("生成结婚场景失败:" + e);
						return;
					}
					MarriageDownCity city = new MarriageDownCity(id, cityName);
					emCity.notifyNewObject(city);
					info.setCityId(city.getId());
					getDownCityMap().put(city.getId(), city);
					if (logger.isWarnEnabled()) {
						logger.warn("[结婚场景] [创建成功] [{}]", new Object[] { info.getLogString() });
					}
				}
				PlayerSimpleInfo marriageA = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldA());
				PlayerSimpleInfo marriageB = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldB());
				if (marriageA == null || marriageB == null) {
					logger.error("[结婚对象不存在] " + info.getLogString());
					return;
				}
				if (i == hunli_time.length - 1) {
					// 最后一次 提示不一样，婚礼开始
					if (PlayerManager.getInstance().isOnline(info.getHoldA()) && PlayerManager.getInstance().isOnline(info.getHoldB())) {
						// 结婚双方都在线
						Player mPlayerA = null;
						Player mPlayerB = null;
						try {
							mPlayerA = PlayerManager.getInstance().getPlayer(info.getHoldA());
							mPlayerB = PlayerManager.getInstance().getPlayer(info.getHoldB());
						} catch (Exception e) {
							logger.error("婚礼开始取结婚双方出错:" + e);
							return;
						}
						// 结婚双方都在婚礼场景
						MarriageDownCity downcity = getDownCityMap().get(info.getCityId());
						if (downcity == null) {
							logger.error("婚礼开始，结婚场景不存在");
							return;
						}
						Game game = downcity.getGame();
						try {
							logger.warn("[结婚场景] [{}] [{}] [{}]", new Object[] { game, mPlayerA.getCurrentGame(), mPlayerB.getCurrentGame() });
						} catch (Exception e) {
							logger.error("~~~~~出错", e);
						}
						if (game.equals(mPlayerA.getCurrentGame()) && game.equals(mPlayerB.getCurrentGame())) {
							// 结婚双方都在结婚场景里
							sendHunLiStart(info, mPlayerA, mPlayerB);
							return;
						}
					}
					// 婚礼未能正常开始,通知其他在婚礼场景的玩家
					MarriageDownCity city = getDownCityMap().get(info.getCityId());
					if (city != null) {
						LivingObject[] living = city.getGame().getLivingObjects();
						for (int p = 0; p < living.length; p++) {
							if (living[p] instanceof Player) {
								Player player = (Player) living[p];
								player.send_HINT_REQ(Translate.text_marriage_婚礼由于结婚双方未在线或不在结婚场景中婚礼结束, (byte) 5);
							}
						}
					}
					info.setCityId(0);
					if (info.getDelayNum() > 0) {
						// 已经延期过一次了,将不能再举行婚礼
						info.setState(MarriageInfo.STATE_JIEHUN);
						info.setSuccessTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
						logger.warn("[结婚2次延期]" + info.getLogString());
					} else {
						info.setState(MarriageInfo.STATE_UNONLINE);
						info.setDelayNum(1);
						try {
							MailManager.getInstance().sendMail(info.getHoldA(), null, null, Translate.text_marriage_003, Translate.text_marriage_004, 0, 0, 0, "");
							MailManager.getInstance().sendMail(info.getHoldB(), null, null, Translate.text_marriage_003, Translate.text_marriage_004, 0, 0, 0, "");
						} catch (Exception e) {
							logger.error("婚礼推迟邮件发送失败:", e);
						}
						logger.warn("[结婚延期]" + info.getLogString());
					}
				} else {
					String msg = Translate.text_marriage_005 + (hunli_time[i] / 1000 / 60) + Translate.text_marriage_006;
					if (PlayerManager.getInstance().isOnline(info.getHoldA())) {
						Player playerA = null;
						try {
							playerA = PlayerManager.getInstance().getPlayer(info.getHoldA());
						} catch (Exception e) {
							logger.error("结婚取对象出错" + info.getLogString() + e);
						}
						if (playerA != null) {
							MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
							Option_joinHunLi opt1 = new Option_joinHunLi(info);
							opt1.setText(Translate.确定);
							Option_Cancel opt2 = new Option_Cancel();
							opt2.setText(Translate.取消);
							mw.setOptions(new Option[] { opt1, opt2 });
							MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
							playerA.addMessageToRightBag(menu);
						}
					}
					if (PlayerManager.getInstance().isOnline(info.getHoldB())) {
						Player playerB = null;
						try {
							playerB = PlayerManager.getInstance().getPlayer(info.getHoldB());
						} catch (Exception e) {
							logger.error("结婚取对象出错" + info.getLogString() + e);
						}
						if (playerB != null) {
							MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
							Option_joinHunLi opt1 = new Option_joinHunLi(info);
							opt1.setText(Translate.确定);
							Option_Cancel opt2 = new Option_Cancel();
							opt2.setText(Translate.取消);
							mw.setOptions(new Option[] { opt1, opt2 });
							MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
							playerB.addMessageToRightBag(menu);
						}
					}

					// 给所有宾客发送邀请
					String message = marriageA.getName() + Translate.text_marriage_008 + marriageB.getName() + Translate.text_marriage_007 + (hunli_time[i] / 1000 / 60) + Translate.text_marriage_006;
					for (int j = 0; j < info.getGuestA().size(); j++) {
						sendHunLiBegin(info, info.getGuestA().get(j), message);
					}
					for (int j = 0; j < info.getGuestB().size(); j++) {
						sendHunLiBegin(info, info.getGuestB().get(j), message);
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[结婚广播] [次数{}] [{}] ", new Object[] { i, info.getLogString() });
					}
				}

			}
		}
	}

	/**
	 * 发送婚礼开始提示
	 * @param playerId
	 * @throws Exception
	 */
	private void sendHunLiBegin(MarriageInfo info, long playerId, String msg) {
		if (PlayerManager.getInstance().isOnline(playerId)) {
			// 如果宾客不在线就不发送
			Player guest = null;
			try {
				guest = PlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				if (logger.isDebugEnabled()) logger.debug("邀请宾客", e);
				return;
			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_joinHunLi opt1 = new Option_joinHunLi(info);
			opt1.setText(Translate.确定);
			Option_Cancel opt2 = new Option_Cancel();
			opt2.setText(Translate.取消);
			mw.setOptions(new Option[] { opt1, opt2 });

			MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
			guest.addMessageToRightBag(menu);
		}
	}

	// 婚礼正式开始
	private void sendHunLiStart(MarriageInfo info, Player from, Player to) {
		MenuWindow mw2 = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Cancel opt23 = new Option_Cancel();
		opt23.setText(Translate.确定);
		mw2.setOptions(new Option[] { opt23 });

		String message2 = Translate.text_marriage_009;
		MARRIAGE_MENU menu1 = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw2.getId(), message2, mw2.getOptions());
		from.addMessageToRightBag(menu1);
		MARRIAGE_MENU menu2 = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw2.getId(), message2, mw2.getOptions());
		to.addMessageToRightBag(menu2);
		for (long guestId : info.getGuestA()) {
			if (PlayerManager.getInstance().isOnline(guestId)) {
				Player guest = null;
				try {
					guest = PlayerManager.getInstance().getPlayer(guestId);
				} catch (Exception e) {
					logger.error("婚礼开始取嘉宾出错", e);
					continue;
				}
				if (!guest.isOnline()) {
					continue;
				}
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel opt2 = new Option_Cancel();
				opt2.setText(Translate.确定);
				mw.setOptions(new Option[] { opt2 });

				String message = from.getName() + Translate.text_marriage_008 + to.getName() + Translate.text_marriage_010;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				guest.addMessageToRightBag(menu);
			}
		}
		for (long guestId : info.getGuestB()) {
			if (PlayerManager.getInstance().isOnline(guestId)) {
				Player guest = null;
				try {
					guest = PlayerManager.getInstance().getPlayer(guestId);
				} catch (Exception e) {
					logger.error("婚礼开始取嘉宾出错", e);
					continue;
				}
				if (!guest.isOnline()) {
					continue;
				}
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel opt2 = new Option_Cancel();
				opt2.setText(Translate.确定);
				mw.setOptions(new Option[] { opt2 });

				String message = from.getName() + Translate.text_marriage_008 + to.getName() + Translate.text_marriage_010;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				guest.addMessageToRightBag(menu);
			}
		}
		info.setState(MarriageInfo.STATE_HUNLI_START);
		if (logger.isWarnEnabled()) logger.warn("[婚礼正式开始] [{}]", new Object[] { info.getLogString() });
	}

	// 结婚掉落
	private void marriageDropProp(MarriageInfo info) {
		Player marriageA = null;
		Player marriageB = null;
		try {
			if (PlayerManager.getInstance().isOnline(info.getHoldA())) {
				marriageA = PlayerManager.getInstance().getPlayer(info.getHoldA());
			}
			if (PlayerManager.getInstance().isOnline(info.getHoldB())) {
				marriageB = PlayerManager.getInstance().getPlayer(info.getHoldB());
			}
		} catch (Exception e) {
			info.setState(MarriageInfo.STATE_SHIBAI);
			logger.warn("结婚掉落取结婚双方:infoID=" + info.getId() + "A=" + info.getHoldA() + "B=" + info.getHoldB(), e);
			return;
		}

		if (info.getDropTime() + info.getDropNum() * MARRIAGE_DROP_SPACE_TIME < SystemTime.currentTimeMillis()) {
			info.setDropNum(info.getDropNum() + 1);
			// 掉落
			MarriageLevel level = marriageLevels[info.getLevel()];
			String[] name = level.getPropName();
			int[] num = level.getPropNum();
			Random ran = new Random();
			for (int i = 0; i < name.length; i++) {
				Article article = ArticleManager.getInstance().getArticle(name[i]);
				if (article == null) {
					logger.error("[结婚掉落物品不存在]" + name[i]);
					continue;
				}
				for (int j = 0; j < num[i]; j++) {
					if (marriageA != null) {
						FlopCaijiNpc fcn = creatMarrigeFlopCaiJiNpc(info, article);
						if (fcn == null) {
							continue;
						}
						fcn.setX(marriageA.getX() - 150 + ran.nextInt(300));
						fcn.setY(marriageA.getY() - 150 + ran.nextInt(300));
						if (marriageA.getCurrentGame() != null) {
							marriageA.getCurrentGame().addSprite(fcn);
						}
					}
					if (marriageB != null) {
						FlopCaijiNpc fcn = creatMarrigeFlopCaiJiNpc(info, article);
						if (fcn == null) {
							continue;
						}
						fcn.setX(marriageB.getX() - 150 + ran.nextInt(300));
						fcn.setY(marriageB.getY() - 150 + ran.nextInt(300));
						if (marriageB.getCurrentGame() != null) {
							marriageB.getCurrentGame().addSprite(fcn);
						}
					}
				}
				if (logger.isWarnEnabled()) logger.warn("[婚礼掉落] [礼物] [infoID={}] [ArtName={}] [Num={}]", new Object[] { info.getId(), name[i], num[i] });
			}
			if (info.getDropNum() >= level.getRewardsNum()) {
				if (logger.isWarnEnabled()) logger.warn("[婚礼结束 ] [{}]", new Object[] { info.getLogString() });
				info.setCityId(0);
				info.setState(MarriageInfo.STATE_JIEHUN);
				info.setSuccessTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				if (level.getHomeName() != null && !"".equals(level.getHomeName())) {
					// 全部结婚喜糖发放完，进入婚房
					GameManager gameManager = GameManager.getInstance();
					Game game = new Game(gameManager, gameManager.getGameInfo(level.getHomeName()));
					try {
						game.init();
					} catch (Exception e) {
						MarriageManager.logger.error("初始化结婚副本出错: mapName =" + level.getHomeName(), e);
					}
					hunfangGameMap.put(info.getId(), game);
					hunfangCreatTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 30000;
					MapArea mapArea = game.gi.getMapAreaByName(Translate.出生点);
					Random random = new Random();
					int x = mapArea.getX() + random.nextInt(mapArea.getWidth());
					int y = mapArea.getY() + random.nextInt(mapArea.getHeight());
					if (marriageA != null) {
						marriageA.getCurrentGame().transferGame(marriageA, new TransportData(0, 0, 0, 0, level.getHomeName(), x, y));
					}
					if (marriageB != null) {
						marriageB.getCurrentGame().transferGame(marriageB, new TransportData(0, 0, 0, 0, level.getHomeName(), x, y));
					}
					if (logger.isWarnEnabled()) logger.warn("[婚礼结束] [传送进婚房] [{}]", new Object[] { level.getHomeName() });
				}
			}
		}
	}

	private FlopCaijiNpc creatMarrigeFlopCaiJiNpc(MarriageInfo info, Article article) {
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, null, article.getColorType(), 1, true);
		} catch (Exception e) {
			logger.error("生成掉落Entity出错" + e);
			return null;
		}
		NPCManager nm = MemoryNPCManager.getNPCManager();
		FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
		fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		fcn.setAllCanPickAfterTime(1000 * 60 * 60);
		fcn.setEndTime(SystemTime.currentTimeMillis() + MemoryNPCManager.掉落NPC的存在时间);
		if (info.getLevel() > 2) {
			fcn.setAvataRace("diaoluo");
			fcn.setAvataSex("baoxiang");
		} else {
			fcn.setAvataRace("diaoluo");
			fcn.setAvataSex("hongbao");
		}
		ResourceManager.getInstance().getAvata(fcn);
		List<Long> guestList = new ArrayList<Long>();
		guestList.addAll(info.getGuestA());
		guestList.addAll(info.getGuestB());
		fcn.setPlayersList(guestList);
		fcn.setAe(ae);
		fcn.setCount(1);
		fcn.setName(ae.getShowName());
		return fcn;
	}

	// 结婚周年
	private void marriageZhouNian(MarriageInfo info) {
		for (int i = marriage_year.length; --i >= 0;) {
			if (info.getTimeLevel() < marriage_year[i] && info.getSuccessTime() + marriage_year[i] * time_scale < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
				ArrayList<ArticleEntity> entitysA = new ArrayList<ArticleEntity>();
				ArrayList<Integer> numA = new ArrayList<Integer>();
				ArrayList<ArticleEntity> entitysB = new ArrayList<ArticleEntity>();
				ArrayList<Integer> numB = new ArrayList<Integer>();
				PlayerSimpleInfo holdA = null;
				PlayerSimpleInfo holdB = null;
				try {
					holdA = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldA());
					holdB = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldB());
				} catch (Exception e) {
					info.setState(MarriageInfo.STATE_SHIBAI);
					logger.warn("结婚周年取 结婚双方出错:infoID=" + info.getId() + "A=" + info.getHoldA() + "B=" + info.getHoldB(), e);
					continue;
				}
				for (int x = 0; x < marriage_propName[i].length; x++) {
					Article article = ArticleManager.getInstance().getArticle(marriage_propName[i][x]);
					if (article == null) {
						logger.warn("[结婚周年发放奖励] [出错] [{}] [{}]", new Object[] { info.getLogString(), "周年" + i + "-" + x + marriage_propName[i][x] });
						continue;
					}
					try {
						entitysA.add(ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, null, article.getColorType(), marriage_propNum[i][x], true));
						entitysB.add(ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, null, article.getColorType(), marriage_propNum[i][x], true));
						numA.add(marriage_propNum[i][x]);
						numB.add(marriage_propNum[i][x]);
						ArticleStatManager.addToArticleStat(null, null, 0, null, holdA.getUsername(), holdA.getLevel(), entitysA.get(entitysA.size() - 1).getArticleName(), article.getArticleLevel(), ArticleManager.getColorString(article, article.getColorType()), true, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, numA.get(numA.size() - 1), "结婚周年奖励", null);
						ArticleStatManager.addToArticleStat(null, null, 0, null, holdB.getUsername(), holdB.getLevel(), entitysB.get(entitysB.size() - 1).getArticleName(), article.getArticleLevel(), ArticleManager.getColorString(article, article.getColorType()), true, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, numB.get(numB.size() - 1), "结婚周年奖励", null);
					} catch (Exception ex) {
						logger.error("结婚创建周年奖励出错:", ex);
					}
				}
				ArticleEntity[] entityA = entitysA.toArray(new ArticleEntity[0]);
				ArticleEntity[] entityB = entitysB.toArray(new ArticleEntity[0]);
				int[] numsA = new int[numA.size()];
				int[] numsB = new int[numB.size()];
				for (int x = 0; x < numA.size(); x++) {
					numsA[x] = numA.get(x);
					numsB[x] = numB.get(x);
				}
				try {
					MailManager.getInstance().sendMail(info.getHoldA(), entityA, numsA, Translate.text_marriage_011 + marriage_name[i], Translate.text_marriage_012 + marriage_name[i] + Translate.text_marriage_013, 0, 0, 0, "结婚周年发送奖励");
				} catch (Exception e) {
					logger.warn("结婚周年发送奖励出错:" + info.getLogString(), e);
				}
				try {
					MailManager.getInstance().sendMail(info.getHoldB(), entityB, numsB, Translate.text_marriage_011 + marriage_name[i], Translate.text_marriage_012 + marriage_name[i] + Translate.text_marriage_013, 0, 0, 0, "结婚周年发送奖励");
				} catch (Exception e) {
					logger.warn("结婚周年发送奖励出错:" + info.getLogString(), e);
				}
				info.setTimeLevel(marriage_year[i]);
				if (logger.isWarnEnabled()) logger.warn("[结婚周年奖励] [成功] [YEAR={}] [{}]", new Object[] { marriage_year[i], info.getLogString() });
			}
		}
	}

	public void setBeqMap(Hashtable<Long, MarriageBeq> beqMap) {
		this.beqMap = beqMap;
	}

	public Hashtable<Long, MarriageBeq> getBeqMap() {
		return beqMap;
	}

	private void setInfoMap(Hashtable<Long, MarriageInfo> infoMap) {
		this.infoMap = infoMap;
	}

	public Hashtable<Long, MarriageInfo> getInfoMap() {
		return infoMap;
	}

	// 载入配置文件
	public void loadFile() throws Exception {
		try {
			File file = new File(getFile());
			if (!file.exists()) {
				logger.error("结婚配置文件不存在");
				return;
			}
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);


			// 求婚配置
			HSSFSheet sheet1 = workbook.getSheetAt(0);
			nanBeq_name = new String[sheet1.getPhysicalNumberOfRows() - 1];
			Beq_price = new long[sheet1.getPhysicalNumberOfRows() - 1];
			Beq_num = new int[sheet1.getPhysicalNumberOfRows() - 1];
			nvBeq_name = new String[sheet1.getPhysicalNumberOfRows() - 1];
			for (int i = 1, n = sheet1.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet1.getRow(i);
				nanBeq_name[i - 1] = (row.getCell(0).getStringCellValue());
				nvBeq_name[i - 1] = (row.getCell(1).getStringCellValue());
				Beq_num[i - 1] = StringTool.getCellValue(row.getCell(2), Integer.class);
				Beq_price[i - 1] = StringTool.getCellValue(row.getCell(3), Integer.class);
			}
			// 结婚纪念日
			HSSFSheet sheet2 = workbook.getSheetAt(1);
			marriage_year = new int[sheet2.getPhysicalNumberOfRows() - 1];
			marriage_name = new String[sheet2.getPhysicalNumberOfRows() - 1];
			marriage_propName = new String[sheet2.getPhysicalNumberOfRows() - 1][];
			marriage_propNum = new int[sheet2.getPhysicalNumberOfRows() - 1][];
			for (int i = 1, n = sheet2.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet2.getRow(i);
				marriage_year[i - 1] = StringTool.getCellValue(row.getCell(0), Integer.class);
				marriage_name[i - 1] = row.getCell(1).getStringCellValue();
				String[] propname = row.getCell(2).getStringCellValue().split(",");
				marriage_propName[i - 1] = propname;
				String[] propNum = StringTool.getCellValue(row.getCell(3), String.class).split(",");
				marriage_propNum[i - 1] = new int[propname.length];
				for (int j = 0; j < propNum.length; j++) {
					marriage_propNum[i - 1][j] = (int)Double.parseDouble(propNum[j]);
				}
			}

			// 婚礼规模
			HSSFSheet sheet3 = workbook.getSheetAt(2);
			marriageLevels = new MarriageLevel[sheet3.getPhysicalNumberOfRows() - 1];
			for (int i = 1, n = sheet3.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet3.getRow(i);
				marriageLevels[i - 1] = new MarriageLevel();
				marriageLevels[i - 1].setName(row.getCell(0).getStringCellValue());
				marriageLevels[i - 1].setPlayerNum(StringTool.getCellValue(row.getCell(1), Integer.class));
				marriageLevels[i - 1].setNeedMoneyType(StringTool.getCellValue(row.getCell(2), Integer.class));
				marriageLevels[i - 1].setNeedMoney(StringTool.getCellValue(row.getCell(3), Integer.class));
				marriageLevels[i - 1].setRewardsNum(StringTool.getCellValue(row.getCell(4), Integer.class));
				String[] propName = row.getCell(5).getStringCellValue().split(",");
				marriageLevels[i - 1].setPropName(propName);
				String[] propNum = row.getCell(6).getStringCellValue().split(",");
				int[] propNums = new int[propNum.length];
				for (int j = 0; j < propNums.length; j++) {
					propNums[j] = Integer.parseInt(propNum[j]);
				}
				marriageLevels[i - 1].setPropNum(propNums);
				marriageLevels[i - 1].setIcon(row.getCell(7).getStringCellValue());
				marriageLevels[i - 1].setCityName(row.getCell(8).getStringCellValue());
				getMarriageCityName().add(row.getCell(8).getStringCellValue());
				if (sheet3.getPhysicalNumberOfRows() < 10 || row.getCell(9).getStringCellValue()== null) {
					marriageLevels[i - 1].setHomeName("");
				} else {
					marriageLevels[i - 1].setHomeName(row.getCell(9).getStringCellValue());
					getMarriageCityName().add(row.getCell(9).getStringCellValue());
				}
				marriageLevels[i - 1].setNanRing(row.getCell(10).getStringCellValue().trim());
				marriageLevels[i - 1].setNvRing(row.getCell(11).getStringCellValue().trim());
				String[] equipmentName = (row.getCell(12).getStringCellValue().trim()).split(",");
				marriageLevels[i - 1].setEqupmentRing(equipmentName);
				marriageLevels[i - 1].setColorType(StringTool.getCellValue(row.getCell(13), Integer.class));
			}

			// 点击头像送东西
			HSSFSheet sheet4 = workbook.getSheetAt(3);
			ArrayList<String> songNanNames = new ArrayList<String>();
			ArrayList<String> songNvNames = new ArrayList<String>();
			ArrayList<String> songPrices = new ArrayList<String>();
			for (int i = 1, n = sheet4.getPhysicalNumberOfRows(); i < n; i++) {
				HSSFRow row = sheet4.getRow(i);
				songNanNames.add(row.getCell(0).getStringCellValue());
				songNvNames.add(row.getCell(1).getStringCellValue());
				songPrices.add(StringTool.getCellValue(row.getCell(2), String.class));
			}
			Song_nan_name = songNanNames.toArray(new String[0]);
			Song_nv_name = songNvNames.toArray(new String[0]);
			Song_price = new long[songPrices.size()];
			for (int i = 0; i < songPrices.size(); i++) {
				Song_price[i] = (long)Double.parseDouble(songPrices.get(i));
			}
		} catch (Exception e) {
			logger.error("loadFile出错:", e);
			throw e;
		}
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	/**
	 * 点击送花或送糖求婚
	 * @param type
	 *            //类型 0是送花，1是送糖
	 * @param player
	 */
	public void option_beqStart(byte type, Player player) {
		try {
			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			if (relation != null && relation.getMarriageId() > 0 && getMarriageInfoById(relation.getMarriageId()) != null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_014);
				player.addMessageToRightBag(req);
				return;
			}
			if (type != player.getSex()) {
				if (player.getSex() == 0) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_015);
					player.addMessageToRightBag(req);
					return;
				} else {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_016);
					player.addMessageToRightBag(req);
					return;
				}
			}
			// logger.debug("点击送花或送糖求婚"+type);
			if (logger.isInfoEnabled()) logger.info("[尝试送花或送糖] [{}] [type={}]", new Object[] { player.getLogString(), type });
			MARRIAGE_BEQSTART_REQ req = null;
			if (type == BEQ_TYPE_HUA) {
				int[] haveFlower = new int[nanBeq_name.length];
				long[] propID = new long[nanBeq_name.length];
				for (int i = 0; i < haveFlower.length; i++) {
					if (logger.isDebugEnabled()) logger.debug("求婚可以送的花[" + nanBeq_name[i] + "]");
					haveFlower[i] = player.getKnapsack_common().countArticle(nanBeq_name[i]);
					ArticleEntity entity = getTempArticleEntity(nanBeq_name[i]);
					if (entity != null) {
						propID[i] = entity.getId();
					} else {
						propID[i] = -1;
					}
				}
				req = new MARRIAGE_BEQSTART_REQ(GameMessageFactory.nextSequnceNum(), type, propID, nanBeq_name, Beq_num, haveFlower, Beq_price);
			} else if (type == BEQ_TYPE_TANG) {
				int[] haveFlower = new int[nvBeq_name.length];
				long[] propID = new long[nvBeq_name.length];
				for (int i = 0; i < haveFlower.length; i++) {
					if (logger.isDebugEnabled()) logger.debug("求婚可以送的糖[" + nvBeq_name[i] + "]");
					haveFlower[i] = player.getKnapsack_common().countArticle(nvBeq_name[i]);
					ArticleEntity entity = getTempArticleEntity(nvBeq_name[i]);
					if (entity != null) {
						propID[i] = entity.getId();
					} else {
						propID[i] = -1;
					}
				}
				req = new MARRIAGE_BEQSTART_REQ(GameMessageFactory.nextSequnceNum(), type, propID, nvBeq_name, Beq_num, haveFlower, Beq_price);
			}

			player.addMessageToRightBag(req);
		} catch (Exception e) {
			logger.error("点击送花或送糖求婚===", e);
		}
	}

	/**
	 * 求婚是否愿意
	 * @param beq
	 * @param type
	 *            0 愿意 1不愿意
	 */
	public synchronized void option_beqOtherReq(MarriageBeq beq, int type) {
		// logger.debug("求婚是否愿意"+type);
		if (logger.isInfoEnabled()) logger.info("[求婚是否愿意] [state={}] [type={}]", new Object[] { beq.getState(), type });
		if (type == 0) {
			beq.setState(MarriageBeq.STATE_AGREE);
			try {
				Player player = PlayerManager.getInstance().getPlayer(beq.getSendId());
				Player player2 = PlayerManager.getInstance().getPlayer(beq.getToId());
				Relation relation = SocialManager.getInstance().getRelationById(player.getId());
				Relation relation2 = SocialManager.getInstance().getRelationById(player2.getId());
				if (relation.getMarriageId() > 0 && getMarriageInfoById(relation.getMarriageId()) != null) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_014);
					player.addMessageToRightBag(req);
					return;
				}
				if (relation2.getMarriageId() > 0 && getMarriageInfoById(relation2.getMarriageId()) != null) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_017);
					player2.addMessageToRightBag(req);
					return;
				}
				
				String mapName = TransportData.getMainCityMap(player2.getCountry());
				int []xy = TransportData.getMarriageXY(player2.getCountry());
				player2.getCurrentGame().transferGame(player2, new TransportData(0, 0, 0, 0, mapName, xy[0], xy[1]));

				AchievementManager.getInstance().record(player, RecordAction.结婚次数);
				AchievementManager.getInstance().record(player2, RecordAction.结婚次数);
				MarriageInfo info = new MarriageInfo();
				info.setId(emInfo.nextId());
				info.setHoldA(player.getId());
				info.setHoldB(player2.getId());
				info.setState(MarriageInfo.STATE_START);
				info.setCacheTime(SystemTime.currentTimeMillis());
				getInfoMap().put(info.getId(), info);
				emInfo.notifyNewObject(info);

				SocialManager.getInstance().getRelationById(player.getId()).setMarriageId(info.getId());
				SocialManager.getInstance().getRelationById(player2.getId()).setMarriageId(info.getId());
				if (logger.isWarnEnabled()) logger.warn("[求婚愿意] [结婚成功] [{}]", new Object[] { info.getLogString() });
				if (player.isOnline()) {
					{
						String mapName2 = TransportData.getMainCityMap(player2.getCountry());
						int []xy2 = TransportData.getMarriageXY(player2.getCountry());
						player.getCurrentGame().transferGame(player2, new TransportData(0, 0, 0, 0, mapName2, xy2[0], xy2[1]));
						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
						Option_Cancel opt1 = new Option_Cancel();
						opt1.setText(Translate.确定);
						mw.setOptions(new Option[] { opt1 });
						String message = player2.getName() + Translate.text_marriage_001;
						MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
						player.addMessageToRightBag(menu);
						beq.setState(MarriageBeq.STATE_OVER);
					}
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					Option_Cancel opt1 = new Option_Cancel();
					opt1.setText(Translate.确定);
					mw.setOptions(new Option[] { opt1 });
					String message = Translate.text_marriage_019;
					MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
					player2.addMessageToRightBag(menu);

					MARRIAGE_ASSIGN_RES res = new MARRIAGE_ASSIGN_RES(GameMessageFactory.nextSequnceNum(), marriageLevels);
					player.addMessageToRightBag(res);
				} else {
					player.send_HINT_REQ(Translate.text_marriage_018, (byte) 5);
				}
			} catch (Exception e) {
				logger.error("求婚是否愿意:", e);
			}
		} else if (type == 1) {
			Player player = null;
			Player player2 = null;
			try {
				player2 = PlayerManager.getInstance().getPlayer(beq.getToId());
				player = PlayerManager.getInstance().getPlayer(beq.getSendId());
			} catch (Exception e) {
				logger.error("求婚不愿意,取求婚双方出错:", e);
				return;
			}
			if (logger.isWarnEnabled()) logger.warn("[求婚] [被拒绝] [A={}] [B={}]", new Object[] { player.getId() + "/" + player.getName(), player2.getId() + "/" + player2.getName() });
			AchievementManager.getInstance().record(player, RecordAction.求婚被拒绝次数);
			if (player.isOnline()) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel opt1 = new Option_Cancel();
				opt1.setText(Translate.确定);
				mw.setOptions(new Option[] { opt1 });
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), player2.getName() + Translate.text_marriage_002, mw.getOptions());
				player.addMessageToRightBag(menu);
				beq.setState(MarriageBeq.STATE_OVER);
			} else {
				beq.setState(MarriageBeq.STATE_REFUSE);
			}
		}
	}

	/**
	 * 结婚请求愿意不愿意
	 * @param from
	 * @param to
	 * @param type
	 *            0愿意 1不愿意
	 */
	public synchronized void option_marriageReq(Player from, Player to, byte type) {
		if (type == 0) {
			MarriageInfo infofrom = getMarriageInfoById(SocialManager.getInstance().getRelationById(from.getId()).getMarriageId());
			if (infofrom != null && infofrom.getState() != MarriageInfo.STATE_LIHUN) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_017);
				from.addMessageToRightBag(req);
				return;
			}
			MarriageInfo infoto = getMarriageInfoById(SocialManager.getInstance().getRelationById(to.getId()).getMarriageId());
			if (infoto != null && infoto.getState() != MarriageInfo.STATE_LIHUN) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_014);
				to.addMessageToRightBag(req);
				return;
			}
			MarriageInfo info = new MarriageInfo();
			try {
				info.setId(emInfo.nextId());
			} catch (Exception e) {
				logger.error("info ID构建出错:", e);
				return;
			}
			AchievementManager.getInstance().record(from, RecordAction.结婚次数);
			AchievementManager.getInstance().record(to, RecordAction.结婚次数);
			info.setHoldA(from.getId());
			info.setHoldB(to.getId());
			info.setState(MarriageInfo.STATE_START);
			info.setCacheTime(SystemTime.currentTimeMillis());
			getInfoMap().put(info.getId(), info);
			emInfo.notifyNewObject(info);
			SocialManager.getInstance().getRelationById(from.getId()).setMarriageId(info.getId());
			SocialManager.getInstance().getRelationById(to.getId()).setMarriageId(info.getId());
			if (logger.isWarnEnabled()) logger.warn("[求婚愿意] [结婚成功] [{}]", new Object[] { info.getLogString() });

			String mapName = TransportData.getMainCityMap(to.getCountry());
			int []xy = TransportData.getMarriageXY(to.getCountry());
			
			to.getCurrentGame().transferGame(to, new TransportData(0, 0, 0, 0, mapName, xy[0], xy[1]));
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_Cancel opt1 = new Option_Cancel();
			opt1.setText(Translate.确定);
			mw.setOptions(new Option[] { opt1 });
			String message = Translate.text_marriage_019;
			MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			to.addMessageToRightBag(menu);

			MARRIAGE_ASSIGN_RES res = new MARRIAGE_ASSIGN_RES(GameMessageFactory.nextSequnceNum(), marriageLevels);
			from.addMessageToRightBag(res);

		} else if (type == 1) {
			if (from != null && to != null) {
				AchievementManager.getInstance().record(from, RecordAction.求婚被拒绝次数);
				if (logger.isWarnEnabled()) logger.warn("[求婚] [被拒绝] [A={}] [B={}]", new Object[] { from.getId() + "/" + from.getName(), to.getId() + "/" + to.getName() });
			}
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_Cancel opt1 = new Option_Cancel();
			opt1.setText(Translate.确定);
			mw.setOptions(new Option[] { opt1 });
			MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), to.getName() + Translate.text_marriage_002, mw.getOptions());
			from.addMessageToRightBag(menu);
		}
	}

	// 选择NPC菜单 布置婚礼
	public void option_marriageAssign(Player player) {
		boolean isOther = false;
		MarriageInfo marriageInfo = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				break;
			}
			if (info.getHoldB() == player.getId()) {
				isOther = true;
				marriageInfo = info;
				break;
			}
		}
		if (marriageInfo == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_020);
			player.addMessageToRightBag(req);
			return;
		}
		if (marriageInfo.getState() == MarriageInfo.STATE_JIEHUN) {
			player.sendError(Translate.text_marriage_014);
			return;
		}

		if (marriageInfo.getState() == MarriageInfo.STATE_START) {
			if (isOther) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_021);
				player.addMessageToRightBag(req);
				return;
			}
			if (!PlayerManager.getInstance().isOnline(marriageInfo.getHoldB())) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_您的另一半不在线请在她在线的时候布置);
				player.addMessageToRightBag(req);
				return;
			}
			try {
				Player to = PlayerManager.getInstance().getPlayer(marriageInfo.getHoldB());
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel opt1 = new Option_Cancel();
				opt1.setText(Translate.确定);
				mw.setOptions(new Option[] { opt1 });
				String message = Translate.text_marriage_019;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				to.addMessageToRightBag(menu);
			} catch (Exception e) {
				logger.error("求婚者出错", e);
			}
			if (logger.isWarnEnabled()) logger.warn("[布置婚礼] [选择婚礼规模] [p={}] [{}] ", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
			MARRIAGE_ASSIGN_RES res = new MARRIAGE_ASSIGN_RES(GameMessageFactory.nextSequnceNum(), marriageLevels);
			player.addMessageToRightBag(res);
		} else if (marriageInfo.getState() == MarriageInfo.STATE_CHOOSE_LEVEL) {
			Player player2 = null;
			try {
				if (isOther) {
					if (PlayerManager.getInstance().isOnline(marriageInfo.getHoldA())) {
						player2 = PlayerManager.getInstance().getPlayer(marriageInfo.getHoldA());
					}
				} else {
					if (PlayerManager.getInstance().isOnline(marriageInfo.getHoldB())) {
						player2 = PlayerManager.getInstance().getPlayer(marriageInfo.getHoldB());
					}
				}
			} catch (Exception e) {
				logger.error("option_marriageAssign==角色不存在", e);
				return;
			}
			if (player2 == null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_018);
				player.addMessageToRightBag(req);
				return;
			}
			if (logger.isWarnEnabled()) logger.warn("[布置婚礼] [选择宾客] [p={}] [{}] ", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
			byte[] a = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			long[] aId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			String[] aName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			byte[] career = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			marriageInfo.getGuest(!isOther, a, aId, aName);
			marriageInfo.getGuestCareer(!isOther, career);
			MARRIAGE_ASSIGN_CHOOSE2_RES req = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), !isOther, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), a, aId, aName, career);
			player.addMessageToRightBag(req);
			byte[] b = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			long[] bId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			String[] bName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			byte[] bcareer = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			marriageInfo.getGuest(isOther, b, bId, bName);
			marriageInfo.getGuestCareer(isOther, bcareer);
			MARRIAGE_ASSIGN_CHOOSE2_RES req2 = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), isOther, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), b, bId, bName, bcareer);
			player2.addMessageToRightBag(req2);
		} else if (marriageInfo.getState() == MarriageInfo.STATE_CHOOSE_GUEST) {
			if (isOther) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_021);
				player.addMessageToRightBag(req);
				return;
			}
			if (!PlayerManager.getInstance().isOnline(marriageInfo.getHoldB())) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_018);
				player.addMessageToRightBag(req);
				return;
			}
			Player player2 = null;
			try {
				player2 = PlayerManager.getInstance().getPlayer(marriageInfo.getHoldB());
			} catch (Exception e) {
				logger.error("option_marriageAssign==角色不存在", e);
				return;
			}

			if (logger.isWarnEnabled()) logger.warn("[布置婚礼] [设定婚礼时间] [p={}] [{}] ", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
			MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
			player.addMessageToRightBag(res);
			MARRIAGE_TIME_RES res2 = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), false);
			player2.addMessageToRightBag(res2);
		} else {
			player.sendError(Translate.text_marriage_022);
		}
	}

	public void msg_DelayTime(Player player, int time) {
		MarriageInfo marriageInfo = null;
		Player other = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				try {
					if (PlayerManager.getInstance().isOnline(info.getHoldB())) {
						other = PlayerManager.getInstance().getPlayer(info.getHoldB());
					}
					marriageInfo = info;
				} catch (Exception e) {
					logger.error("msg_DelayTime结婚重新定婚礼时间，另外那个人不存在了" + info.getLogString(), e);
					return;
				}
				break;
			}
		}
		if (marriageInfo == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}

		if (other == null) {
			player.sendError(Translate.text_marriage_您的另一半不在线请在她在线的时候布置);
			return;
		}

		if (time <= 0) {
			player.sendError(Translate.text_marriage_024);
			return;
		}

		if (logger.isWarnEnabled()) logger.warn("确定设定婚礼延迟时间  [{}]", new Object[] { marriageInfo.getLogString() });
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_MarriageDelayOK delayOK = new Option_MarriageDelayOK(time, 0);
		delayOK.setText(Translate.text_marriage_同意);
		Option_MarriageDelayOK delayNO = new Option_MarriageDelayOK(time, 1);
		delayNO.setText(Translate.text_marriage_不同意);
		mw.setOptions(new Option[] { delayOK, delayNO });
		String message = Translate.text_marriage_025 + time + Translate.text_marriage_026;
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		other.addMessageToRightBag(menu);
	}

	public synchronized void option_DelayTimeOK(Player player, int time, int type) {
		MarriageInfo marriageInfo = null;
		Player other = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldB() == player.getId()) {
				try {
					if (PlayerManager.getInstance().isOnline(info.getHoldA())) {
						other = PlayerManager.getInstance().getPlayer(info.getHoldA());
					}
					marriageInfo = info;
				} catch (Exception e) {
					logger.error("option_DelayTimeOK结婚重新定婚礼时间，另外那个人不存在了" + info.getLogString(), e);
					return;
				}
				break;
			}
		}
		if (marriageInfo == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}

		if (other == null) {
			player.sendError(Translate.text_marriage_您的另一半不在线请在她在线的时候布置);
			return;
		}

		if (time <= 0) {
			player.sendError(Translate.text_marriage_024);
			return;
		}

		if (type == 0) {
			// 同意
			marriageInfo.setBeginTime(time);
			marriageInfo.setSendMessage2GuestTimeIndex(-1);
			marriageInfo.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + marriageInfo.getBeginTime() * ONE_HOUR);
			marriageInfo.setState(MarriageInfo.STATE_CHOOSE_TIME);
			if (logger.isWarnEnabled()) logger.warn("延时确定婚礼时间 [成功] [{}]", new Object[] { marriageInfo.getLogString() });
			{
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.确定);
				mw.setOptions(new Option[] { cancel });
				String message = Translate.text_marriage_027 + time + Translate.text_marriage_028;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				player.addMessageToRightBag(menu);
			}

			{
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel cancel = new Option_Cancel();
				cancel.setText(Translate.确定);
				mw.setOptions(new Option[] { cancel });
				String message = Translate.text_marriage_027 + time + Translate.text_marriage_028;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				other.addMessageToRightBag(menu);
			}

		} else if (type == 1) {
			// 不同意
			option_DelayTime(other);
			other.sendError(Translate.text_marriage_029);
		}
	}

	public void option_DelayTime(Player player) {
		MarriageInfo marriageInfo = null;
		Player other = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				try {
					if (PlayerManager.getInstance().isOnline(info.getHoldB())) {
						other = PlayerManager.getInstance().getPlayer(info.getHoldB());
					}
					marriageInfo = info;
				} catch (Exception e) {
					logger.error("option_DelayTime结婚重新定婚礼时间，另外那个人不存在了" + info.getLogString(), e);
					return;
				}
				break;
			}
			if (info.getHoldB() == player.getId()) {
				player.sendError(Translate.text_marriage_030);
				return;
			}
		}

		if (marriageInfo == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}

		if (marriageInfo.getState() != MarriageInfo.STATE_UNONLINE) {
			player.sendError(Translate.text_marriage_031);
			return;
		}

		if (other == null) {
			player.sendError(Translate.text_marriage_018);
			return;
		}

		MARRIAGE_DELAYTIME_BEGIN_RES res_1 = new MARRIAGE_DELAYTIME_BEGIN_RES(GameMessageFactory.nextSequnceNum());
		player.addMessageToRightBag(res_1);
		if (logger.isWarnEnabled()) logger.warn("尝试重新设定婚礼延迟时间 [{}]", new Object[] { marriageInfo.getLogString() });
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.确定);
		mw.setOptions(new Option[] { cancel });
		String message = Translate.text_marriage_032;
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		other.addMessageToRightBag(menu);
	}

	public void option_time(Player player, int time, boolean isAgree) {
		try {
			MarriageInfo marriageInfo = null;
			Player other = null;
			for (MarriageInfo info : getInfoMap().values()) {
				if (info.getHoldB() == player.getId()) {
					marriageInfo = info;
					try {
						other = PlayerManager.getInstance().getPlayer(info.getHoldA());
					} catch (Exception e) {
						logger.error("婚姻另个玩家查找失败==", e);
						return;
					}
				}
			}
			if (isAgree) {
				String[] gusetName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
				marriageInfo.getGuest(false, null, null, gusetName);
				Date date = new Date((marriageInfo.getBeginTime() * 60 * 60 * 1000) + com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				SimpleDateFormat format = new SimpleDateFormat(Translate.text_年月日时分);
				String timeString = format.format(date);
				MARRIAGE_FINISH_RES req = new MARRIAGE_FINISH_RES(GameMessageFactory.nextSequnceNum(), true, marriageLevels[marriageInfo.getLevel()], gusetName, timeString);
				other.addMessageToRightBag(req);
				MARRIAGE_FINISH_RES req2 = new MARRIAGE_FINISH_RES(GameMessageFactory.nextSequnceNum(), false, marriageLevels[marriageInfo.getLevel()], gusetName, timeString);
				player.addMessageToRightBag(req2);
				if (logger.isInfoEnabled()) logger.info("[结婚] [设定时间确定成功] [{}]", new Object[] { marriageInfo.getLogString() });
			} else {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_029);
				other.addMessageToRightBag(req);
				marriageInfo.setBeginTime(0);
				MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
				other.addMessageToRightBag(res);
				if (logger.isInfoEnabled()) logger.info("[结婚] [设定时间不同意] [{}]", new Object[] { marriageInfo.getLogString() });
			}
		} catch (Exception e) {
			logger.error("option_time===", e);
		}
	}

	public void option_joinHunli(Player player, MarriageInfo info) {
		try {
			MarriageDownCity city = getDownCityMap().get(info.getCityId());
			if (city != null) {
				MapArea mapArea = city.getGame().gi.getMapAreaByName(Translate.出生点);
				Random random = new Random();
				int x = mapArea.getX() + random.nextInt(mapArea.getWidth());
				int y = mapArea.getY() + random.nextInt(mapArea.getHeight());
				player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, city.getGame().gi.name, x, y));
				getPlayer2CityMap().put(player.getId(), city.getId());
				if (logger.isInfoEnabled()) logger.info("[参加婚礼] [成功] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
			}
		} catch (Exception e) {
			logger.error("option_joinHunli参加婚礼出错:", e);
		}
	}

	public void option_lihun(Player player, int type) {
		if (logger.isDebugEnabled()) logger.debug("收到点击离婚");
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_023);
			player.addMessageToRightBag(req);
			return;
		}
		if (info.getSuccessTime() > 0 && SystemTime.currentTimeMillis() - info.getSuccessTime() < LIHUN_SPACETIME) {
			player.sendError(Translate.text_marriage_033);
			return;
		}
		if (info.getState() == MarriageInfo.STATE_DROP) {
			player.sendError(Translate.text_marriage_034);
			return;
		}
		if (type == 0) {

			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_LiHunOk opt1 = new Option_LiHunOk(type);
			opt1.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { opt1, cancel });
			String message = Translate.text_marriage_035;
			CONFIRM_WINDOW_REQ menu = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			player.addMessageToRightBag(menu);
			if (logger.isWarnEnabled()) logger.warn("[离婚] [申请] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
		} else if (type == 1) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_LiHunOk opt1 = new Option_LiHunOk(type);
			opt1.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { opt1, cancel });
			String message = Translate.text_marriage_036 + TradeManager.putMoneyToMyText(LIHUN_PRICE);
			CONFIRM_WINDOW_REQ menu = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			player.addMessageToRightBag(menu);
			if (logger.isWarnEnabled()) logger.warn("[强制离婚] [申请] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
		}
	}

	public void sendLihun2Other(Player player) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_023);
			player.addMessageToRightBag(req);
			return;
		}
		Player player2 = null;
		if (player.getId() == info.getHoldA()) {
			try {
				if (PlayerManager.getInstance().isOnline(info.getHoldB())) {
					player2 = PlayerManager.getInstance().getPlayer(info.getHoldB());
				}
			} catch (Exception e) {
				logger.error("离婚出错：", e);
			}
		} else {
			try {
				if (PlayerManager.getInstance().isOnline(info.getHoldA())) {
					player2 = PlayerManager.getInstance().getPlayer(info.getHoldA());
				}
			} catch (Exception e) {
				logger.error("离婚出错：", e);
			}
		}
		if (player2 != null) {
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_LihunOther opt1 = new Option_LihunOther(0);
			opt1.setText(Translate.确定);
			Option_LihunOther opt2 = new Option_LihunOther(1);
			opt2.setText(Translate.取消);
			mw.setOptions(new Option[] { opt1, opt2 });
			String message = player.getName() + Translate.text_marriage_038;
			CONFIRM_WINDOW_REQ menu = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			player2.addMessageToRightBag(menu);
			if (logger.isWarnEnabled()) logger.warn("[离婚] [给配偶发] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
		} else {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_037);
			player.addMessageToRightBag(req);
		}
	}

	/**
	 * @param player
	 * @param type
	 *            0 不强制 1强制 -1不同意离婚
	 */
	public synchronized void real_lihun(Player player, int type) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}
		Player player2 = null;
		if (player.getId() == info.getHoldA()) {
			try {
				player2 = PlayerManager.getInstance().getPlayer(info.getHoldB());
			} catch (Exception e) {
				logger.error("离婚出错：", e);
				return;
			}
		} else {
			try {
				player2 = PlayerManager.getInstance().getPlayer(info.getHoldA());
			} catch (Exception e) {
				logger.error("离婚出错：", e);
				return;
			}
		}
		if (type == -1) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_039);
			player2.addMessageToRightBag(req);
		} else if (type == 0) {
			SocialManager.getInstance().getRelationById(player.getId()).setMarriageId(-1);
			SocialManager.getInstance().getRelationById(player2.getId()).setMarriageId(-1);
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_040);
			player2.addMessageToRightBag(req);
			info.setState(MarriageInfo.STATE_LIHUN);
			AchievementManager.getInstance().record(player, RecordAction.离婚次数);
			AchievementManager.getInstance().record(player2, RecordAction.离婚次数);
			PlayerTitlesManager.getInstance().removeTitle(player, Translate.text_marriage_结婚);
			PlayerTitlesManager.getInstance().removeTitle(player2, Translate.text_marriage_结婚);
			if (logger.isWarnEnabled()) logger.warn("[离婚] [真正成功] [{}] [{}] [{}]", new Object[] { player.getLogString(), player2.getLogString(), info.getLogString() });
		} else if (type == 1) {
			if (player.getSilver() + player.getShopSilver() < LIHUN_PRICE) {
				player.sendError(Translate.text_marriage_041);
				return;
			}

			try {
				BillingCenter.getInstance().playerExpense(player, LIHUN_PRICE, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "强制离婚");
			} catch (NoEnoughMoneyException e) {
				player.sendError(Translate.text_marriage_041);
				logger.warn("强制离婚扣钱出错:", e);
				return;
			} catch (BillFailedException e) {
				player.sendError(Translate.text_marriage_041);
				logger.warn("强制离婚扣钱出错:", e);
				return;
			}

			SocialManager.getInstance().getRelationById(player.getId()).setMarriageId(-1);
			SocialManager.getInstance().getRelationById(player2.getId()).setMarriageId(-1);
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_042);
			player.addMessageToRightBag(req);
			AchievementManager.getInstance().record(player, RecordAction.离婚次数);
			AchievementManager.getInstance().record(player2, RecordAction.离婚次数);
			if (player2.isOnline()) {
				HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, player.getName() + Translate.text_marriage_043);
				player2.addMessageToRightBag(req2);
			} else {
				try {
					Date date = new Date(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					SimpleDateFormat format = new SimpleDateFormat(Translate.text_年月日时分);
					String timeString = format.format(date);
					MailManager.getInstance().sendMail(player2.getId(), null, null, Translate.text_marriage_044, player.getName() + timeString + Translate.text_marriage_045, 0, 0, 0, "");
				} catch (Exception e) {
					logger.warn("强制离婚给另外一个人发邮件出错:", e);
				}
			}
			PlayerTitlesManager.getInstance().removeTitle(player, Translate.text_marriage_结婚);
			PlayerTitlesManager.getInstance().removeTitle(player2, Translate.text_marriage_结婚);
			if (logger.isWarnEnabled()) logger.warn("[强制离婚] [真正成功] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
			info.setState(MarriageInfo.STATE_LIHUN);
		}
	}

	public void option_jiaohuan(Game game, Player player) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return;
		}
		MarriageDownCity city = getDownCityMap().get(info.getCityId());
		if (city == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_047);
			player.addMessageToRightBag(req);
			return;
		}
		if (!city.getGame().equals(game)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_048);
			player.addMessageToRightBag(req);
			return;
		}

		if (info.getState() == MarriageInfo.STATE_JIEHUN || info.getState() == MarriageInfo.STATE_DROP) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_049);
			player.addMessageToRightBag(req);
			return;
		}

		if (info.getState() != MarriageInfo.STATE_HUNLI_START) {
			player.sendError(Translate.text_marriage_050);
			return;
		}

		Player other = null;
		if (info.getHoldA() == player.getId()) {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldB());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
				return;
			}
		} else {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldA());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
				return;
			}
		}
		int XD = (other.getX() - player.getX()) * (other.getX() - player.getX());
		int YD = (other.getY() - player.getY()) * (other.getY() - player.getY());
		if (Math.abs(XD) > 40000 || Math.abs(YD) > 40000) {
			player.sendError(Translate.text_marriage_051);
			other.sendError(Translate.text_marriage_051);
			return;
		}

		MarriageLevel marriageLevel = marriageLevels[info.getLevel()];

		if (other != null) {
			long entityA = 0;
			long entityB = 0;
			if (player.getSex() == 0) {
				ArticleEntity articleEntity = player.getArticleEntity(marriageLevel.getNvRing());
				if (articleEntity != null) {
					entityA = articleEntity.getId();
				} else {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_052);
					player.addMessageToRightBag(req);
					return;
				}
				articleEntity = other.getArticleEntity(marriageLevel.getNanRing());
				if (articleEntity != null) {
					entityB = articleEntity.getId();
				} else {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_053);
					player.addMessageToRightBag(req);
					HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_052);
					other.addMessageToRightBag(req2);
					return;
				}
			} else {
				ArticleEntity articleEntity = player.getArticleEntity(marriageLevel.getNanRing());
				if (articleEntity != null) {
					entityA = articleEntity.getId();
				} else {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_052);
					player.addMessageToRightBag(req);
					return;
				}
				articleEntity = other.getArticleEntity(marriageLevel.getNvRing());
				if (articleEntity != null) {
					entityB = articleEntity.getId();
				} else {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_053);
					player.addMessageToRightBag(req);
					HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_052);
					other.addMessageToRightBag(req2);
					return;
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[结婚] [申请交换戒指] [{}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info.getLogString() });
			info.setIsjiaohuan(false);
			player.addMessageToRightBag(new MARRIAGE_JIAOHUAN_RES(GameMessageFactory.nextSequnceNum(), other.getId(), entityA, entityB));
			other.addMessageToRightBag(new MARRIAGE_JIAOHUAN_RES(GameMessageFactory.nextSequnceNum(), player.getId(), entityB, entityA));
		}
	}

	public void option_getRing(Game game, Player player) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return;
		}
		MarriageDownCity city = getDownCityMap().get(info.getCityId());
		if (city == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_047);
			player.addMessageToRightBag(req);
			return;
		}
		if (city.getGame() != game) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_048);
			player.addMessageToRightBag(req);
			return;
		}
		MarriageLevel level = marriageLevels[info.getLevel()];
		if (player.getSex() == 0) {
			if (player.getArticleEntity(level.getNvRing()) != null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_054);
				player.addMessageToRightBag(req);
				return;
			}
			if (player.getKnapsack_common().isFull()) {
				player.sendError(Translate.text_marriage_055);
				return;
			}
			if (player.getSilver() + player.getShopSilver() < GET_RING_MONEY) {
				player.sendError(Translate.text_marriage_056);
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, GET_RING_MONEY, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "结婚补领戒指");
			} catch (NoEnoughMoneyException e1) {
				logger.warn("领取戒指扣钱", e1);
				return;
			} catch (BillFailedException e1) {
				logger.warn("领取戒指扣钱", e1);
				return;
			}

			Article nvR = ArticleManager.getInstance().getArticle(level.getNvRing());
			try {
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(nvR, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, player, nvR.getColorType(), 1, true);
				ArticleStatManager.addToArticleStat(null, player, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚领取戒指", null);
				player.getKnapsack_common().put(articleEntity, Translate.text_marriage_结婚);
				if (logger.isWarnEnabled()) logger.warn("[结婚] [重领戒指] [{}] [{}]", new Object[] { player.getId() + "/" + player.getName(), articleEntity.getId() + "/" + articleEntity.getArticleName() });
			} catch (Exception ex) {
				logger.error("[NPC领取结婚戒指] [失败] [" + player.getId() + "]", ex);
			}
		} else {
			if (player.getArticleEntity(level.getNanRing()) != null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_054);
				player.addMessageToRightBag(req);
				return;
			}
			if (player.getKnapsack_common().isFull()) {
				player.sendError(Translate.text_marriage_055);
				return;
			}
			if (player.getSilver() + player.getShopSilver() < GET_RING_MONEY) {
				player.sendError(Translate.text_marriage_056);
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, GET_RING_MONEY, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "结婚补领戒指");
			} catch (NoEnoughMoneyException e1) {
				logger.warn("领取戒指扣钱", e1);
				return;
			} catch (BillFailedException e1) {
				logger.warn("领取戒指扣钱", e1);
				return;
			}

			Article nanR = ArticleManager.getInstance().getArticle(level.getNanRing());
			try {
				ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(nanR, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, player, nanR.getColorType(), 1, true);
				ArticleStatManager.addToArticleStat(null, player, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚领取戒指", null);
				player.getKnapsack_common().put(articleEntity, Translate.text_marriage_结婚);
				if (logger.isWarnEnabled()) logger.warn("[结婚] [重领戒指] [{}] [{}]", new Object[] { player.getId() + "/" + player.getName(), articleEntity.getId() + "/" + articleEntity.getArticleName() });
			} catch (Exception ex) {
				logger.error("重领结婚戒指出错:" + player.getId(), ex);
			}
		}
	}

	public void option_joinHunLiScreen(Player player) {
		// logger.warn("-----------option_joinHunLiScreen---------");
		ArrayList<Long> list = new ArrayList<Long>();
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getCityId() > 0) {
				list.add(info.getId());
			}
		}
		// logger.warn(getInfoMap().size() + "---------AAAAA--------" + list.size());
		Vector<Long> vInfoId = new Vector<Long>();
		Vector<String> vInfoMsg = new Vector<String>();
		for (int i = 0; i < list.size(); i++) {
			long id = list.get(i);
			MarriageInfo info = getMarriageInfoById(id);
			PlayerSimpleInfo one = null;
			PlayerSimpleInfo two = null;
			try {
				one = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldA());
				two = PlayerSimpleInfoManager.getInstance().getInfoById(info.getHoldB());
			} catch (Exception e) {
				logger.error("婚礼信息角色不存在", e);
				continue;
			}
			// logger.warn(info.getId() + "~~~~~~~~~~"+one.getName()+"~~"+two.getName()+"~"+one.getCountry()+"~"+player.getCountry()+"~");
			if (one.getCountry() != player.getCountry()) {
				continue;
			}
			vInfoId.addElement(id);

			String msg = one.getName() + Translate.text_marriage_008 + two.getName() + Translate.text_marriage_057;
			vInfoMsg.addElement(msg);
		}
		// logger.debug("infoId.size:"+infoId.length);
		long[] infoId = new long[vInfoId.size()];
		for (int i = 0; i < vInfoId.size(); i++) {
			infoId[i] = ((Long) vInfoId.elementAt(i)).longValue();
		}
		String[] infoMsg = new String[vInfoMsg.size()];
		vInfoMsg.copyInto(infoMsg);
		MARRIAGE_JOIN_HUNLI_SCREEN_RES res = new MARRIAGE_JOIN_HUNLI_SCREEN_RES(GameMessageFactory.nextSequnceNum(), infoId, infoMsg);
		player.addMessageToRightBag(res);
		if (logger.isInfoEnabled()) logger.info("[查询婚礼] [{}] [{}] [{}]", new Object[] { player.getId() + "/" + player.getName(), vInfoMsg.size() });
	}

	// 确定求婚
	public void real_Beq(Player player, int selectIndex, String playerName, String say) {
		if (logger.isDebugEnabled()) logger.debug("确实求婚");
		Player toId = null;
		if (PlayerManager.getInstance().isOnline(playerName)) {
			try {
				toId = PlayerManager.getInstance().getPlayer(playerName);
			} catch (Exception e) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
				player.addMessageToRightBag(req);
				if (logger.isDebugEnabled()) logger.debug("求婚角色不存在");
				return;
			}
		} else {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
			player.addMessageToRightBag(req);
			return;
		}

		if (player.getSex() == toId.getSex()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_059);
			player.addMessageToRightBag(req);
			return;
		}

		if (selectIndex < 0 || selectIndex >= Beq_num.length) {
			player.sendError(Translate.text_marriage_060);
			return;
		}

		String articleName = "";
		if (player.getSex() == 0) {
			articleName = nanBeq_name[selectIndex];
		} else {
			articleName = nvBeq_name[selectIndex];
		}
		int num = Beq_num[selectIndex];
		int bagNum = player.getKnapsack_common().countArticle(articleName);
		int haveNum = bagNum;
		long needMoney = 0;
		if (bagNum < num) {
			needMoney = (num - bagNum) * Beq_price[selectIndex];
		} else {
			bagNum = num;
		}

		if (player.getSilver() + player.getShopSilver() < needMoney) {
			BillingCenter.银子不足时弹出充值确认框(player, Translate.text_marriage_056);
			// player.sendError("您身上的银子不够");
			return;
		}

		int kouNum = 0; // 扣掉的数目
		synchronized (player.tradeKey) {
			while (bagNum > 0) {
				int index = player.getKnapsack_common().getArticleCellPos(articleName);
				if (index < 0) {
					logger.error("[送花出错] [预计有花但在背包中找不到] [p={}] [NAME={}] [haveNum={}] [kouNum={}] [bagNum={}] index[{}]", new Object[] { player.getLogString(), articleName, haveNum, kouNum, bagNum, index });
					return;
				}
				Cell cell = player.getKnapsack_common().getCell(index);
				if (cell.getCount() > bagNum) {
					kouNum += cell.getCount() - bagNum;
					player.getKnapsack_common().clearCell(index, bagNum, "结婚送花", true);
					// logger.warn("[扣除花数目] [realBeq] [{}] [{}] [{}]", new Object[]{articleName, bagNum, index});
					bagNum = 0;
				} else {
					kouNum += cell.getCount();
					bagNum = bagNum - cell.getCount();
					player.getKnapsack_common().clearCell(index, "结婚", true);
				}
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[送花] [扣花] [p={}] [toID={}] [NAME={}] [haveNum={}] [say={}]", new Object[] { player.getLogString(), toId.getId(), articleName, haveNum, say });
		if (needMoney > 0) {
			try {
				BillingCenter.getInstance().playerExpense(player, needMoney, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "求婚送花或糖" + selectIndex);
				NewChongZhiActivityManager.instance.doXiaoFeiActivity(player, needMoney, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
				if (logger.isWarnEnabled()) logger.warn("[送花] [扣钱] [p={}] [toID={}] [money={}] [haveNum={}]", new Object[] { player.getLogString(), toId.getId(), needMoney, haveNum });
			} catch (NoEnoughMoneyException e) {
				logger.error("求婚送花失败: 需要补回花:" + articleName + "-" + haveNum, e);
				return;
			} catch (BillFailedException e) {
				logger.error("求婚送花失败: 需要补回花:" + articleName + "-" + haveNum, e);
				return;
			}
		}

		for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
			MARRIAGE_BEQ_FLOWER_RES res = new MARRIAGE_BEQ_FLOWER_RES(GameMessageFactory.nextSequnceNum(), player.getSex(), (byte) selectIndex);
			pp.addMessageToRightBag(res);
		}
		String reqMarriageText;
		if (player.getSex() == 0) {
			// 男
			if (say == null || say.equals("")) {
				say = Translate.text_marriage_061;
			} else {
				WordFilter filter = WordFilter.getInstance();
				say = filter.nvalidAndReplace(say, 0, "@#\\$%^\\&\\*");
			}
			reqMarriageText = "<f color='0xffff00' edgeColor='0x72176a'>" + CountryManager.得到国家名(player.getCountry()) + "•" + player.getName() + "</f>" + "<f color='0xffc0e4' edgeColor='0x72176a'>" + Translate.text_marriage_062 + Beq_num[selectIndex] + Translate.text_marriage_063 + nanBeq_name[selectIndex] + Translate.text_marriage_064 + "</f>" + "<f color='0xffff00' edgeColor='0x72176a'>" + CountryManager.得到国家名(toId.getCountry()) + "•" + toId.getName() + "</f>" + "<f color='0xffc0e4' edgeColor='0x72176a'>" + Translate.text_marriage_065 + say + "</f>";
			BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(toId.getId());
			bData.设置送花(toId, 999 * flowOrSweet_billboardValue[selectIndex]);
			AchievementManager.getInstance().record(toId, RecordAction.获得鲜花数量, 999);
		} else {
			// 女
			if (say == null || say.equals("")) {
				say = Translate.text_marriage_066;
			} else {
				WordFilter filter = WordFilter.getInstance();
				say = filter.nvalidAndReplace(say, 1, "@#\\$%^\\&\\*");
			}
			reqMarriageText = "<f color='0xffff00' edgeColor='0x72176a'>" + CountryManager.得到国家名(player.getCountry()) + "•" + player.getName() + "</f>" + "<f color='0xffc0e4' edgeColor='0x72176a'>" + Translate.text_marriage_067 + Beq_num[selectIndex] + Translate.text_marriage_068 + nvBeq_name[selectIndex] + Translate.text_marriage_069 + "</f>" + "<f color='0xffff00' edgeColor='0x72176a'>" + CountryManager.得到国家名(toId.getCountry()) + "•" + toId.getName() + "</f>" + "<f color='0xffc0e4' edgeColor='0x72176a'>" + Translate.text_marriage_065 + say + "</f>";
			BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(toId.getId());
			bData.设置送糖(toId, 999 * flowOrSweet_billboardValue[selectIndex]);
			AchievementManager.getInstance().record(toId, RecordAction.获得糖果数量, 999);
		}

		for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 3, reqMarriageText);
			pp.addMessageToRightBag(req);
		}

		// 求婚送花活动奖励
		for (int i = 0; i < marriageActivitys.size(); i++) {
			if (marriageActivitys.get(i).isStart(selectIndex)) {
				marriageActivitys.get(i).sendReward(player);
			}
		}

		MarriageBeq beq = new MarriageBeq();
		try {
			beq.setId(emBeq.nextId());
		} catch (Exception e) {
			logger.error("beqnextid出错:", e);
			return;
		}
		beq.setLevel(selectIndex);
		beq.setSendId(player.getId());
		beq.setState(MarriageBeq.STATE_START);
		beq.setToId(toId.getId());
		getBeqMap().put(beq.getId(), beq);
		emBeq.notifyNewObject(beq);
		sendBeqWindow(beq);
		if (logger.isWarnEnabled()) logger.warn("[结婚送花] [成功] [p={}] [to={}] [name={}] [haveNum={}] [money={}]", new Object[] { player.getLogString(), toId.getLogString(), articleName, haveNum, needMoney });
	}

	// 求婚
	public void msg_Beq(Player player, int selectIndex, String playerName, String say) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.结婚)) {
			player.sendError(Translate.合服功能关闭提示);
			return;
		}
		Player player2 = null;
		if (PlayerManager.getInstance().isOnline(playerName)) {
			try {
				player2 = PlayerManager.getInstance().getPlayer(playerName);
			} catch (Exception e) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
				player.addMessageToRightBag(req);
				return;
			}
		}
		if (player2 == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
			player.addMessageToRightBag(req);
			return;
		}

		if (player2.getLevel() < 30) {
			player.sendError(Translate.text_marriage_070);
			return;
		}
		if (player.getLevel() < 30) {
			player.sendError(Translate.text_marriage_071);
			return;
		}

		if (player.getCountry() != player2.getCountry()) {
			player.sendError(Translate.text_marriage_072);
			return;
		}

		if (selectIndex < 0) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_073);
			player.addMessageToRightBag(req);
			return;
		}
		if (selectIndex >= nanBeq_name.length) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_060);
			player.addMessageToRightBag(req);
			return;
		}
		if (player.getSex() == player2.getSex()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_059);
			player.addMessageToRightBag(req);
			return;
		}

		try {

			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			Relation relation2 = SocialManager.getInstance().getRelationById(player2.getId());
			if (relation.getMarriageId() > 0 && getMarriageInfoById(relation.getMarriageId()) != null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_014);
				player.addMessageToRightBag(req);
				return;
			}
			if (relation2.getMarriageId() > 0 && getMarriageInfoById(relation2.getMarriageId()) != null) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_017);
				player.addMessageToRightBag(req);
				return;
			}

			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_Marriage_BeqOK marriage_BeqOK = new Option_Marriage_BeqOK(selectIndex, playerName, say);
			marriage_BeqOK.setText(Translate.确定);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			mw.setOptions(new Option[] { marriage_BeqOK, cancel });
			String message = "";
			String attName = "";
			if (player.getSex() == 0) {
				attName = nanBeq_name[selectIndex];
				message = Translate.text_marriage_074 + playerName + Translate.text_marriage_075 + Beq_num[selectIndex] + Translate.text_marriage_063 + nanBeq_name[selectIndex] + Translate.text_marriage_076;
			} else if (player.getSex() == 1) {
				attName = nvBeq_name[selectIndex];
				message = Translate.text_marriage_074 + playerName + Translate.text_marriage_075 + Beq_num[selectIndex] + Translate.text_marriage_068 + nvBeq_name[selectIndex] + Translate.text_marriage_076;
			}
			MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			player.addMessageToRightBag(menu);
			if (logger.isWarnEnabled()) logger.warn("[结婚送花] [尝试] [p={}] [to={}] [index={}]", new Object[] { player.getLogString(), playerName, selectIndex + "--" + attName });
		} catch (Exception e) {
			logger.error("msg_Beq尝试求婚出错：", e);
		}
	}

	/**
	 * 请求结婚
	 * @param from
	 * @param toName
	 * @return
	 */
	public void msg_marriage_req(Player from, String toName) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.结婚)) {
			from.sendError(Translate.合服功能关闭提示);
			return;
		}
		Player to;
		if (PlayerManager.getInstance().isOnline(toName)) {
			try {
				to = PlayerManager.getInstance().getPlayer(toName);
			} catch (Exception e) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
				from.addMessageToRightBag(req);
				return;
			}
		} else {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_058);
			from.addMessageToRightBag(req);
			return;
		}

		if (to.getLevel() < 30) {
			from.sendError(Translate.text_marriage_070);
			return;
		}
		if (from.getLevel() < 30) {
			from.sendError(Translate.text_marriage_071);
			return;
		}

		if (from.getCountry() != to.getCountry()) {
			from.sendError(Translate.text_marriage_072);
			return;
		}

		if (to.getSex() == from.getSex()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_059);
			from.addMessageToRightBag(req);
			return;
		}
		MarriageInfo infofrom = getMarriageInfoById(SocialManager.getInstance().getRelationById(from.getId()).getMarriageId());
		if (infofrom != null && infofrom.getState() != MarriageInfo.STATE_LIHUN) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_014);
			from.addMessageToRightBag(req);
			return;
		}
		MarriageInfo infoto = getMarriageInfoById(SocialManager.getInstance().getRelationById(to.getId()).getMarriageId());
		if (infoto != null && infoto.getState() != MarriageInfo.STATE_LIHUN) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_017);
			from.addMessageToRightBag(req);
			return;
		}

		if (marriageReqTimes.get(from.getId()) != null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_077);
			from.addMessageToRightBag(req);
			return;
		}
		// 合服打折活动
		CompoundReturn cr = ActivityManagers.getInstance().getValue(0, from);
		if (cr != null && cr.getBooleanValue()) {
			REQ_MARRIAGE = cr.getIntValue();
		}
		//

		if (from.getSilver() + from.getShopSilver() < REQ_MARRIAGE) {
			from.sendError(Translate.text_marriage_078 + TradeManager.putMoneyToMyText(REQ_MARRIAGE) + Translate.text_marriage_079);
			return;
		}

		try {
			BillingCenter.getInstance().playerExpense(from, REQ_MARRIAGE, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "请求结婚");
		} catch (NoEnoughMoneyException e) {
			logger.warn("请求结婚扣钱出错:", e);
			return;
		} catch (BillFailedException e) {
			logger.warn("请求结婚扣钱出错:", e);
			return;
		}

		marriageReqTimes.put(from.getId(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Cancel cancel = new Option_Cancel();
		cancel.setText(Translate.确定);
		mw.setOptions(new Option[] { cancel });
		String message = Translate.text_marriage_080;
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		from.addMessageToRightBag(menu);

		MenuWindow mw2 = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Marriage_req marriage_reqOk = new Option_Marriage_req(from, (byte) 0);
		marriage_reqOk.setText(Translate.text_marriage_愿意);
		Option_Marriage_req marriage_reqNo = new Option_Marriage_req(from, (byte) 1);
		marriage_reqNo.setText(Translate.text_marriage_不愿意);
		mw2.setOptions(new Option[] { marriage_reqOk, marriage_reqNo });
		message = from.getName() + Translate.text_marriage_081;
		MARRIAGE_MENU menu2 = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw2.getId(), message, mw2.getOptions());
		to.addMessageToRightBag(menu2);
		if (logger.isWarnEnabled()) logger.warn("[请求结婚] [成功] [p={}] [to={}] [money={}]", new Object[] { from.getLogString(), toName, REQ_MARRIAGE });
	}

	public synchronized CompoundReturn msg_assign_choose1(Player player, int index) {
		CompoundReturn compoundReturn = new CompoundReturn();
		if (index < 0 || index >= marriageLevels.length) {
			player.sendError(Translate.text_marriage_082);
			compoundReturn.setBooleanValue(false);
			return compoundReturn;
		}
		int type = marriageLevels[index].getNeedMoneyType();
		int moneyNum = marriageLevels[index].getNeedMoney();
		if (type == 0) {
			// 绑银
			if (player.getBindSilver() < moneyNum) {
				player.sendError(Translate.text_marriage_083);
				compoundReturn.setBooleanValue(false);
				return compoundReturn;
			}
		} else if (type == 1) {
			// 银子
			if (player.getSilver() + player.getShopSilver() < moneyNum) {
				BillingCenter.银子不足时弹出充值确认框(player, Translate.text_marriage_056);
				// player.sendError("您银子不够支付这个婚礼规模");
				compoundReturn.setBooleanValue(false);
				return compoundReturn;
			}
		}

		MarriageInfo marriageInfo = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				break;
			}
			if (info.getHoldB() == player.getId()) {
				player.sendError(Translate.text_marriage_021);
				compoundReturn.setBooleanValue(false);
				return compoundReturn;
			}
		}
		if (marriageInfo == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			compoundReturn.setBooleanValue(false);
			return compoundReturn;
		}
		Player other = null;
		if (PlayerManager.getInstance().isOnline(marriageInfo.getHoldB())) {
			try {
				other = PlayerManager.getInstance().getPlayer(marriageInfo.getHoldB());
			} catch (Exception e) {
				logger.error("结婚角色不存在", e);
				compoundReturn.setBooleanValue(false);
				return compoundReturn;
			}
		}

		if (other == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_018);
			player.addMessageToRightBag(req);
			compoundReturn.setBooleanValue(false);
			return compoundReturn;
		}

		int playerNum = marriageLevels[index].getPlayerNum();
		if (marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size() > playerNum) {
			marriageInfo.getGuestA().clear();
			marriageInfo.getGuestB().clear();
		}
		marriageInfo.setState(MarriageInfo.STATE_CHOOSE_LEVEL);
		marriageInfo.setLevel(index);
		if (logger.isWarnEnabled()) logger.warn("[选择婚礼规模] [成功] [p={}] [index={}] [{}]", new Object[] { player.getLogString(), index, marriageInfo.getLogString() });
		// 返回用来消失界面1
		other.addMessageToRightBag(new MARRIAGE_ASSIGN_CHOOSE1_RES(GameMessageFactory.nextSequnceNum()));
		compoundReturn.setBooleanValue(true);
		// 显示选择嘉宾1
		byte[] a = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		long[] aId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		String[] aName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		byte[] career = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		marriageInfo.getGuest(true, a, aId, aName);
		marriageInfo.getGuestCareer(true, career);
		MARRIAGE_ASSIGN_CHOOSE2_RES req = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), true, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), a, aId, aName, career);
		player.addMessageToRightBag(req);
		byte[] b = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		long[] bId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		String[] bName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		byte[] bcareer = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
		marriageInfo.getGuest(false, b, bId, bName);
		marriageInfo.getGuestCareer(false, bcareer);
		MARRIAGE_ASSIGN_CHOOSE2_RES req2 = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), false, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), b, bId, bName, bcareer);
		other.addMessageToRightBag(req2);
		return compoundReturn;
	}

	/**
	 * 选择宾客
	 * @param player
	 * @param id
	 * @return
	 */
	public synchronized CompoundReturn msg_chooseGuest(Player player, long id) {
		MarriageInfo marriageInfo = null;
		boolean isAB = false;
		long otherId = -1;
		boolean isMarriage = false;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				isAB = true;
				marriageInfo = info;
				otherId = info.getHoldB();
			}
			if (info.getHoldB() == player.getId()) {
				isAB = false;
				marriageInfo = info;
				otherId = info.getHoldA();
			}
			if (otherId == id) {
				isMarriage = true;
			}
		}
		if (isMarriage) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_084);
			player.addMessageToRightBag(req);
			return null;
		}
		if (marriageInfo == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return null;
		}
		Player other = null;
		PlayerSimpleInfo guest = null;
		try {
			other = PlayerManager.getInstance().getPlayer(otherId);
			guest = PlayerSimpleInfoManager.getInstance().getInfoById(id);
		} catch (Exception e) {
			logger.error("邀请婚礼对象不存在", e);
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_邀请角色不存在);
			player.addMessageToRightBag(req);
			return null;
		}
		if (marriageInfo.getGuestA().contains(id) || marriageInfo.getGuestB().contains(id)) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_085);
			player.addMessageToRightBag(req);
			return null;
		}

		if (guest.getLevel() < 10) {
			player.sendError(Translate.text_marriage_086);
			return null;
		}
		MarriageLevel marriageLevel = marriageLevels[marriageInfo.getLevel()];
		if (marriageLevel.getPlayerNum() <= marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()) {
			player.sendError(Translate.text_marriage_087);
			return null;
		}

		if (isAB) {
			marriageInfo.getGuestA().add(id);
			emInfo.notifyFieldChange(marriageInfo, "guestA");
		} else {
			marriageInfo.getGuestB().add(id);
			emInfo.notifyFieldChange(marriageInfo, "guestB");
		}
		if (logger.isInfoEnabled()) logger.info("[选一个宾客] [p={}] [isAB={}] [id={}]", new Object[] { player.getId() + "/" + player.getName(), isAB, id });
		CompoundReturn compoundReturn = new CompoundReturn();
		compoundReturn.setLongValue(id);
		compoundReturn.setStringValue(guest.getName());
		compoundReturn.setByteValue(guest.getCareer());
		MARRIAGE_CHOOSE_GUEST_RES res = new MARRIAGE_CHOOSE_GUEST_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, (byte) 1, id, guest.getName(), guest.getCareer());
		other.addMessageToRightBag(res);

		return compoundReturn;
	}

	public CompoundReturn msg_cancelGuest(Player player, long id) {
		CompoundReturn compoundReturn = new CompoundReturn();
		MarriageInfo marriageInfo = null;
		boolean isA = false;
		Player other = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				isA = true;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldB());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
				}
			}
			if (info.getHoldB() == player.getId()) {
				marriageInfo = info;
				isA = false;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldA());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
				}
			}
		}
		if (marriageInfo == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return null;
		}
		boolean isRmoveA = false;
		if (marriageInfo.getGuestA().remove(id)) {
			isRmoveA = true;
			emInfo.notifyFieldChange(marriageInfo, "guestA");
		} else if (marriageInfo.getGuestB().remove(id)) {
			emInfo.notifyFieldChange(marriageInfo, "guestB");
		}
		if (other != null) {
			MARRIAGE_CANCEL_GUEST_RES res = new MARRIAGE_CANCEL_GUEST_RES(GameMessageFactory.nextSequnceNum(), (byte) 0, id);
			other.addMessageToRightBag(res);
			PlayerSimpleInfo guest = null;
			try {
				guest = PlayerSimpleInfoManager.getInstance().getInfoById(id);
			} catch (Exception e) {
				logger.warn("取宾客出错:" + id, e);
			}
			long holdAId = marriageInfo.getHoldA();
			long holdBId = marriageInfo.getHoldB();
			Player playerB = null;
			Player playerA = null;
			try {
				playerA = PlayerManager.getInstance().getPlayer(holdAId);
				playerB = PlayerManager.getInstance().getPlayer(holdBId);
			} catch (Exception e) {
				logger.error("取宾客出错:" + holdAId + "===" + holdBId, e);
			}
			if (holdAId == player.getId()) {
				if (guest != null) {
					if (isRmoveA) {
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, guest.getName() + Translate.text_marriage_088);
						playerB.addMessageToRightBag(req);
					} else {
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_090 + guest.getName() + Translate.text_marriage_089);
						playerB.addMessageToRightBag(req);
					}
				}
			} else {
				if (guest != null) {
					if (isRmoveA) {
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_090 + guest.getName() + Translate.text_marriage_089);
						playerA.addMessageToRightBag(req);
					} else {
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, guest.getName() + Translate.text_marriage_088);
						playerA.addMessageToRightBag(req);
					}
				}
			}
		}
		if (logger.isInfoEnabled()) logger.info("[取消一个宾客] [p={}] [id={}]", new Object[] { player.getId() + "/" + player.getName(), id });
		return compoundReturn;
	}

	public synchronized void msg_guest_over(Player player, boolean isOver) {
		MarriageInfo marriageInfo = null;
		Player other = null;
		boolean isA = false;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				isA = true;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldB());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
				}
				break;
			} else if (info.getHoldB() == player.getId()) {
				marriageInfo = info;
				isA = false;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldA());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
				}
				break;
			}
		}
		if (isA) {
			marriageInfo.setGuestOverA(isOver);
		} else {
			marriageInfo.setGuestOverB(isOver);
		}
		if (logger.isWarnEnabled()) logger.warn("[选择宾客完成] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
		if (marriageInfo.isGuestOverA() && marriageInfo.isGuestOverB()) {
			marriageInfo.setState(MarriageInfo.STATE_CHOOSE_GUEST);
			if (isA) {
				MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
				player.addMessageToRightBag(res);
				MARRIAGE_TIME_RES res2 = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), false);
				other.addMessageToRightBag(res2);
			} else {
				MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), false);
				player.addMessageToRightBag(res);
				MARRIAGE_TIME_RES res2 = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
				other.addMessageToRightBag(res2);
			}
			if (logger.isWarnEnabled()) logger.warn("[2个人都完成] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
		}
		MARRIAGE_GUEST_OVER_RES res = new MARRIAGE_GUEST_OVER_RES(GameMessageFactory.nextSequnceNum(), isOver);
		other.addMessageToRightBag(res);
		if (isOver) {
			other.send_HINT_REQ(Translate.text_marriage_091, (byte) 5);
		} else {
			other.send_HINT_REQ(Translate.text_marriage_092, (byte) 5);
		}
	}

	// 布置婚礼
	public synchronized void msg_assign(Player player) {
		Player other = null;
		MarriageInfo info1 = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				info1 = info;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldB());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
					return;
				}
				break;
			}
			if (info.getHoldB() == player.getId()) {
				info1 = info;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldA());
				} catch (Exception e) {
					logger.error("取结婚对象出错", e);
					return;
				}
				break;
			}
		}
		info1.setState(MarriageInfo.STATE_START);
		info1.setGuestOverA(false);
		info1.setGuestOverB(false);
		player.addMessageToRightBag(new MARRIAGE_ASSIGN_RES(GameMessageFactory.nextSequnceNum(), MarriageManager.marriageLevels));
		if (logger.isWarnEnabled()) logger.warn("[重新选择婚礼规模] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), info1.getLogString() });
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Cancel opt1 = new Option_Cancel();
		opt1.setText(Translate.确定);
		mw.setOptions(new Option[] { opt1 });
		String message = Translate.text_marriage_019;
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		other.addMessageToRightBag(menu);
		MARRIAGE_CANCEL_RES clo = new MARRIAGE_CANCEL_RES(GameMessageFactory.nextSequnceNum(), 1, "");
		other.addMessageToRightBag(clo);
		// other.addMessageToRightBag(new MARRIAGE_ASSIGN_RES(GameMessageFactory.nextSequnceNum(), new MarriageLevel[0]));
	}

	public void msg_chooseGuest(Player player) {
		try {
			MarriageInfo marriageInfo = null;
			Player other = null;
			boolean isA = false;
			for (MarriageInfo info : getInfoMap().values()) {
				if (info.getHoldA() == player.getId()) {
					marriageInfo = info;
					isA = true;
					try {
						other = PlayerManager.getInstance().getPlayer(info.getHoldB());
					} catch (Exception e) {
						logger.error("取结婚对象出错", e);
						return;
					}
					break;
				}
				if (info.getHoldB() == player.getId()) {
					try {
						other = PlayerManager.getInstance().getPlayer(info.getHoldA());
					} catch (Exception e) {
						logger.error("取结婚对象出错", e);
						return;
					}
					break;
				}
			}
			marriageInfo.setState(MarriageInfo.STATE_CHOOSE_LEVEL);
			marriageInfo.setGuestOverA(false);
			marriageInfo.setGuestOverB(false);
			if (logger.isWarnEnabled()) logger.warn("[重新选择宾客] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });
			// 显示选择嘉宾
			byte[] a = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			long[] aId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			String[] aName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			byte[] careers = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			marriageInfo.getGuest(isA, a, aId, aName);
			marriageInfo.getGuestCareer(isA, careers);
			MARRIAGE_ASSIGN_CHOOSE2_RES req = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), isA, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), a, aId, aName, careers);
			player.addMessageToRightBag(req);
			byte[] b = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			long[] bId = new long[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			String[] bName = new String[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			byte[] bcareers = new byte[marriageInfo.getGuestA().size() + marriageInfo.getGuestB().size()];
			marriageInfo.getGuest(!isA, b, bId, bName);
			marriageInfo.getGuestCareer(!isA, bcareers);
			MARRIAGE_ASSIGN_CHOOSE2_RES req2 = new MARRIAGE_ASSIGN_CHOOSE2_RES(GameMessageFactory.nextSequnceNum(), !isA, marriageLevels[marriageInfo.getLevel()].getPlayerNum(), b, bId, bName, bcareers);
			other.addMessageToRightBag(req2);
		} catch (Exception e) {
			logger.error("msg_chooseGuest 出错： ", e);
		}
	}

	public void msg_time(Player player) {
		MarriageInfo marriageInfo = null;
		Player other = null;
		boolean isA = false;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldB());
				} catch (Exception e) {
					logger.error("msg_time取结婚对象出错", e);
					return;
				}
				break;
			}
			if (info.getHoldB() == player.getId()) {
				isA = true;
				break;
			}
		}
		if (isA) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_021);
			player.addMessageToRightBag(req);
			return;
		}
		if (marriageInfo == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return;
		}
		MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
		player.addMessageToRightBag(res);
		MARRIAGE_TIME_RES res2 = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), false);
		other.addMessageToRightBag(res2);
		if (logger.isWarnEnabled()) logger.warn("[设定婚礼时间] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), marriageInfo.getLogString() });

		return;
	}

	public void msg_timeOk(Player player, int time) {
		Player other = null;
		MarriageInfo marriageInfo = null;
		for (MarriageInfo info : getInfoMap().values()) {
			if (info.getHoldA() == player.getId()) {
				marriageInfo = info;
				try {
					other = PlayerManager.getInstance().getPlayer(info.getHoldB());
				} catch (Exception e) {
					logger.error("msg_timeOk取结婚对象出错", e);
					return;
				}
				break;
			}
		}

		if (time <= 0 || time > 72) {
			MARRIAGE_TIME_RES res = new MARRIAGE_TIME_RES(GameMessageFactory.nextSequnceNum(), true);
			player.addMessageToRightBag(res);
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_024);
			player.addMessageToRightBag(req);
			return;
		}

		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Marriage_time opt1 = new Option_Marriage_time(time, true);
		opt1.setText(Translate.确定);
		Option_Marriage_time opt2 = new Option_Marriage_time(time, false);
		opt2.setText(Translate.取消);
		mw.setOptions(new Option[] { opt1, opt2 });
		String message = Translate.text_marriage_025 + time + Translate.text_marriage_026;
		MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
		other.addMessageToRightBag(menu);
		marriageInfo.setBeginTime(time);
		if (logger.isWarnEnabled()) logger.warn("[设定婚礼时间,对方确定] [p={}] [time={}]", new Object[] { player.getId() + "/" + player.getName(), time });
	}

	public synchronized void msg_finish(Player player, boolean isOk) {
		try {
			if (UnitServerFunctionManager.needCloseFunctuin(Function.结婚)) {
				player.sendError(Translate.合服功能关闭提示);
				return;
			}
			Player other = null;
			MarriageInfo marriageInfo = null;
			for (MarriageInfo info : getInfoMap().values()) {
				if (info.getHoldA() == player.getId()) {
					marriageInfo = info;
					try {
						other = PlayerManager.getInstance().getPlayer(info.getHoldB());
					} catch (Exception e) {
						logger.error("取结婚对象出错", e);
						return;
					}
					break;
				}
			}

			if (marriageInfo == null) {
				player.sendError(Translate.text_marriage_023);
				return;
			}

			if (marriageInfo.getState() == MarriageInfo.STATE_CHOOSE_TIME) {
				player.sendError(Translate.text_marriage_125);
				return;
			}

			if (isOk) {
				if (marriageInfo.getLevel() < 0 || marriageInfo.getLevel() >= marriageLevels.length) {
					player.sendError(Translate.text_marriage_082);
					return;
				}
				MarriageLevel level = marriageLevels[marriageInfo.getLevel()];
				if (level.getNeedMoneyType() == 0) {
					// 绑银
					if (player.getBindSilver() < level.getNeedMoney()) {
						player.sendError(Translate.text_marriage_083);
						return;
					}
					try {
						BillingCenter.getInstance().playerExpense(player, level.getNeedMoney(), CurrencyType.BIND_YINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "婚礼规模" + level.getNeedMoney());
						if (logger.isWarnEnabled()) logger.warn("[婚礼最后扣钱绑银] [p={}] [mon={}]", new Object[] { player.getLogString(), level.getNeedMoney() });
					} catch (NoEnoughMoneyException e) {
						logger.warn("结婚绑银不够：", e);
						return;
					} catch (BillFailedException e) {
						logger.warn("结婚绑银不够：", e);
						return;
					}
				} else if (level.getNeedMoneyType() == 1) {
					// 银子
					if (player.getSilver() + player.getShopSilver() < level.getNeedMoney()) {
						BillingCenter.银子不足时弹出充值确认框(player, Translate.text_marriage_056);
						// player.sendError("您的绑银不够支付婚礼所需要的钱");
						return;
					}
					try {
						BillingCenter.getInstance().playerExpense(player, level.getNeedMoney(), CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "婚礼规模" + level.getNeedMoney());
						if (logger.isWarnEnabled()) logger.warn("[婚礼最后扣钱银子] [p={}] [mon={}]", new Object[] { player.getLogString(), level.getNeedMoney() });
					} catch (NoEnoughMoneyException e) {
						logger.warn("结婚银子不够：", e);
						return;
					} catch (BillFailedException e) {
						logger.warn("结婚银子不够：", e);
						return;
					}
				}
				try {
					Article article = ArticleManager.getInstance().getArticle(level.getNanRing());
					ArticleEntity nanR = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, other, article.getColorType(), 1, true);
					Article article2 = ArticleManager.getInstance().getArticle(level.getNvRing());
					ArticleEntity nvR = ArticleEntityManager.getInstance().createEntity(article2, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, player, article2.getColorType(), 1, true);

					if (player.getKnapsack_common().isFull()) {
						// 发邮件
						if (player.getSex() == 0) {
							try {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { nvR }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_094, 0, 0, 0, "结婚");
								if (logger.isWarnEnabled()) logger.warn("[婚礼 戒指发邮件] [p={}] [id={}]", new Object[] { player.getLogString(), nvR.getId() + "-" + nvR.getArticleName() });
							} catch (Exception e) {
								logger.warn("结婚因为包满了给发邮件戒指出错", e);
								return;
							}
							ArticleStatManager.addToArticleStat(null, player, nvR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
						} else {
							try {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { nanR }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_094, 0, 0, 0, "结婚");
								if (logger.isWarnEnabled()) logger.warn("[婚礼 戒指发邮件] [p={}] [id={}]", new Object[] { player.getLogString(), nanR.getId() + "-" + nanR.getArticleName() });
							} catch (Exception e) {
								logger.warn("结婚因为包满了给发邮件戒指出错", e);
								return;
							}
							ArticleStatManager.addToArticleStat(null, player, nanR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
						}
						player.sendError(Translate.text_marriage_095);
					} else {
						if (player.getSex() == 0) {
							// 男的给女的戒指，最后交换用
							player.getKnapsack_common().put(nvR, "结婚");
							ArticleStatManager.addToArticleStat(null, player, nvR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
							if (logger.isWarnEnabled()) logger.warn("[婚礼背包加戒指] [p={}] [id={}]", new Object[] { player.getLogString(), nvR.getId() + "-" + nvR.getArticleName() });
						} else {
							// 女的给男的戒指，最后交换用
							player.getKnapsack_common().put(nanR, "结婚");
							ArticleStatManager.addToArticleStat(null, player, nanR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
							if (logger.isWarnEnabled()) logger.warn("[婚礼背包加戒指] [p={}] [id={}]", new Object[] { player.getLogString(), nanR.getId() + "-" + nanR.getArticleName() });
						}
						player.sendError(Translate.text_marriage_096);
					}
					if (other.getKnapsack_common().isFull()) {
						if (other.getSex() == 0) {
							try {
								MailManager.getInstance().sendMail(other.getId(), new ArticleEntity[] { nvR }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_094, 0, 0, 0, "结婚");
								if (logger.isWarnEnabled()) logger.warn("[婚礼戒指发邮件] [other={}] [id={}]", new Object[] { other.getLogString(), nvR.getId() + "-" + nvR.getArticleName() });
								ArticleStatManager.addToArticleStat(null, other, nvR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
							} catch (Exception e) {
								logger.warn("结婚因为包满了给发邮件戒指出错", e);
								return;
							}
						} else {
							try {
								MailManager.getInstance().sendMail(other.getId(), new ArticleEntity[] { nanR }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_094, 0, 0, 0, "结婚");
								if (logger.isWarnEnabled()) logger.warn("[婚礼戒指发邮件] [other={}] [id={}]", new Object[] { other.getLogString(), nanR.getId() + "-" + nanR.getArticleName() });
								ArticleStatManager.addToArticleStat(null, other, nanR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
							} catch (Exception e) {
								logger.warn("结婚因为包满了给发邮件戒指出错", e);
								return;
							}
						}
						other.sendError(Translate.text_marriage_095);
					} else {
						if (other.getSex() == 0) {
							// 男的给女的戒指，最后交换用
							other.getKnapsack_common().put(nvR, "结婚");
							if (logger.isWarnEnabled()) logger.warn("[婚礼背包加戒指] [other={}] [id={}]", new Object[] { other.getLogString(), nvR.getId() + "-" + nvR.getArticleName() });
							ArticleStatManager.addToArticleStat(null, other, nvR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
						} else {
							// 女的给男的戒指，最后交换用
							other.getKnapsack_common().put(nanR, "结婚");
							if (logger.isWarnEnabled()) logger.warn("[婚礼背包加戒指] [other={}] [id={}]", new Object[] { other.getLogString(), nanR.getId() + "-" + nanR.getArticleName() });
							ArticleStatManager.addToArticleStat(null, other, nanR, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚用的戒指", null);
						}
						other.sendError(Translate.text_marriage_096);
					}
				} catch (Exception ex) {
					logger.error("结婚戒指出错:", ex);
				}
				marriageInfo.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + marriageInfo.getBeginTime() * ONE_HOUR);
				marriageInfo.setState(MarriageInfo.STATE_CHOOSE_TIME);
				{
					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
					Option_Cancel opt2 = new Option_Cancel();
					opt2.setText(Translate.确定);
					mw.setOptions(new Option[] { opt2 });
					String message = Translate.text_marriage_097 + marriageInfo.getBeginTime() + Translate.text_marriage_098;
					MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
					player.addMessageToRightBag(menu);
					MARRIAGE_MENU menu2 = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
					other.addMessageToRightBag(menu2);
				}
				AchievementManager.getInstance().record(player, RecordAction.结婚规模, marriageInfo.getLevel());
				AchievementManager.getInstance().record(other, RecordAction.结婚规模, marriageInfo.getLevel());
				if (logger.isWarnEnabled()) logger.warn("[婚礼设定全部完成] [p={}] [规模Money={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), level.getNeedMoney(), marriageInfo.getLogString() });
				// 给宾客发送消息
				for (int i = 0; i < marriageInfo.getGuestA().size(); i++) {
					try {
						if (PlayerManager.getInstance().isOnline(marriageInfo.getGuestA().get(i))) {
							Player p = PlayerManager.getInstance().getPlayer(marriageInfo.getGuestA().get(i));
							MenuWindow mw1 = WindowManager.getInstance().createTempMenuWindow(600);
							Option_Cancel aaa = new Option_Cancel();
							aaa.setText(Translate.确定);
							mw1.setOptions(new Option[] { aaa });
							String mess = Translate.text_marriage_101 + player.getName() + Translate.text_marriage_100 + other.getName() + Translate.text_marriage_099 + marriageInfo.getBeginTime() + Translate.text_marriage_098;
							CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw1.getId(), mess, mw1.getOptions());
							p.addMessageToRightBag(req);
						} else {
							MailManager.getInstance().sendMail(marriageInfo.getGuestA().get(i), null, null, Translate.text_marriage_102, Translate.text_marriage_101 + player.getName() + Translate.text_marriage_100 + other.getName() + Translate.text_marriage_099 + marriageInfo.getBeginTime() + Translate.text_marriage_098, 0, 0, 0, "");
						}
					} catch (Exception e) {
						logger.error("取宾客对象出错,或给宾客发邮件出错(不太重要):", e);
						continue;
					}
				}

				for (int i = 0; i < marriageInfo.getGuestB().size(); i++) {
					try {
						if (PlayerManager.getInstance().isOnline(marriageInfo.getGuestB().get(i))) {
							Player p = PlayerManager.getInstance().getPlayer(marriageInfo.getGuestB().get(i));
							MenuWindow mw1 = WindowManager.getInstance().createTempMenuWindow(600);
							Option_Cancel aaa = new Option_Cancel();
							aaa.setText(Translate.确定);
							mw1.setOptions(new Option[] { aaa });
							String mess = Translate.text_marriage_101 + player.getName() + Translate.text_marriage_100 + other.getName() + Translate.text_marriage_099 + marriageInfo.getBeginTime() + Translate.text_marriage_098;
							CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw1.getId(), mess, mw1.getOptions());
							p.addMessageToRightBag(req);
							// p.send_HINT_REQ("您的好友:"+player.getName() + "和" + other.getName() + "喜结良缘,婚礼将于"+marriageInfo.getBeginTime()+"小时候后开始!邀请您参加!", (byte)5);
						} else {
							MailManager.getInstance().sendMail(marriageInfo.getGuestB().get(i), null, null, Translate.text_marriage_102, Translate.text_marriage_101 + player.getName() + Translate.text_marriage_100 + other.getName() + Translate.text_marriage_099 + marriageInfo.getBeginTime() + Translate.text_marriage_098, 0, 0, 0, "");
						}
					} catch (Exception e) {
						logger.error("取宾客对象出错,或给宾客发邮件出错(不太重要):", e);
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error("msg_finish出错", e);
		}
	}

	public synchronized void msg_jiaohuan(Player player, boolean isOk) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		Player other = null;
		if (info.getState() != MarriageInfo.STATE_HUNLI_START) {
			player.sendError(Translate.text_marriage_050);
			return;
		}
		if (info.getHoldA() == player.getId()) {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldB());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
				return;
			}
		} else {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldA());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
				return;
			}
		}
		if (isOk) {
			if (info.isIsjiaohuan()) {
				MarriageLevel level = marriageLevels[info.getLevel()];
				if (player.getSex() == 0) {
					ArticleEntity articleEntity = player.getArticleEntity(level.getNvRing());
					if (articleEntity != null) {
						player.removeFromKnapsacks(articleEntity, "结婚", false);
						if (logger.isWarnEnabled()) logger.warn("[移除结婚戒指player] [{}] [{}]", new Object[] { player.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() });
					}
					articleEntity = other.getArticleEntity(level.getNanRing());
					if (articleEntity != null) {
						other.removeFromKnapsacks(articleEntity, "结婚", false);
						if (logger.isWarnEnabled()) logger.warn("[移除结婚戒指other] [{}] [{}]", new Object[] { other.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() });
					}
				} else {
					ArticleEntity articleEntity = player.getArticleEntity(level.getNanRing());
					if (articleEntity != null) {
						player.removeFromKnapsacks(articleEntity, "结婚", false);
						if (logger.isWarnEnabled()) logger.warn("[移除结婚戒指player] [{}] [{}]", new Object[] { player.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() });
					}
					articleEntity = other.getArticleEntity(level.getNvRing());
					if (articleEntity != null) {
						other.removeFromKnapsacks(articleEntity, "结婚", false);
						if (logger.isWarnEnabled()) logger.warn("[移除结婚戒指other] [{}] [{}]", new Object[] { other.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() });
					}
				}
				try {
					ArticleEntity activityEntity = null;
					try {
						if (isOpenMarriageActivity) {
							long now = System.currentTimeMillis();
							if (now >= marriageActivityStartTimeL && now <= marriageActivityEndTimeL) {
								Article a = ArticleManager.getInstance().getArticle(marriageActivityNames[info.getLevel()]);
								if (a != null) {
									activityEntity = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_BU_1111, player, marriageActivityColor[info.getLevel()] >= 0 ? marriageActivityColor[info.getLevel()] : a.getColorType(), 1, true);
									if (activityEntity != null) {
										logger.warn("[结婚送礼包活动] [{}] [{}] [{}]", new Object[] { info.getLogString(), marriageActivityColor[info.getLevel()], marriageActivityNum[info.getLevel()] });
									}
								} else {
									logger.warn("活动物品不存在  [" + marriageActivityNames[info.getLevel()] + "]");
								}
							}
						}
					} catch (Exception e) {
						logger.error("v异常" + info.getLogString(), e);
					}
					ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(ArticleManager.getInstance().getArticle(level.getEqupmentRing()[player.getCareer() - 1]), true, ArticleEntityManager.CREATE_REASON_MARRIAGE, player, level.getColorType(), 1, true);
					try {
						if (activityEntity != null) {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { articleEntity, activityEntity }, new int[] { 1, marriageActivityNum[info.getLevel()] }, Translate.text_marriage_093, Translate.text_marriage_103, 0, 0, 0, "给玩家发职业戒指");
						} else {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { articleEntity }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_103, 0, 0, 0, "给玩家发职业戒指");
						}
						if (logger.isWarnEnabled()) logger.warn("[邮件职业结婚戒指player] [{}] [{}]", new Object[] { player.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() + "=" + articleEntity.getColorType() });
					} catch (Exception e) {
						logger.error("给玩家发职业戒指出错:" + player.getId(), e);
						return;
					}
					ArticleStatManager.addToArticleStat(null, player, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚职业戒指", null);
					articleEntity = ArticleEntityManager.getInstance().createEntity(ArticleManager.getInstance().getArticle(level.getEqupmentRing()[other.getCareer() - 1]), true, ArticleEntityManager.CREATE_REASON_MARRIAGE, other, level.getColorType(), 1, true);
					try {
						if (activityEntity != null) {
							MailManager.getInstance().sendMail(other.getId(), new ArticleEntity[] { articleEntity, activityEntity }, new int[] { 1, marriageActivityNum[info.getLevel()] }, Translate.text_marriage_093, Translate.text_marriage_103, 0, 0, 0, "给玩家发职业戒指");
						} else {
							MailManager.getInstance().sendMail(other.getId(), new ArticleEntity[] { articleEntity }, new int[] { 1 }, Translate.text_marriage_093, Translate.text_marriage_103, 0, 0, 0, "给玩家发职业戒指");
						}
						if (logger.isWarnEnabled()) logger.warn("[邮件职业结婚戒指other] [{}] [{}]", new Object[] { other.getLogString(), articleEntity.getId() + "-" + articleEntity.getArticleName() + "=" + articleEntity.getColorType() });
					} catch (Exception e) {
						logger.error("给玩家发职业戒指出错:" + other.getId(), e);
						return;
					}
					ArticleStatManager.addToArticleStat(null, other, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚职业戒指", null);

					if (info.getLevel() > 2) {
						// 判断是否满足生成条件
						boolean isHava = false;
						long horseId = 0;
						String PName = "";
						if (player.getSex() == 0) {
							PName = Translate.text_marriage_爱的炫舞;
						} else {
							PName = Translate.text_marriage_浪漫今生;
						}
						List<Long> list = HorseManager.getInstance().getPlayerHorses(player);
						for (int i = 0; i < list.size(); i++) {
							long id = list.get(i);
							Horse horse = HorseManager.getInstance().getHorseById(id, player);
							if (horse == null) {
								continue;
							}
							if (horse.getHorseName().equals(PName)) {
								isHava = true;
								horseId = horse.getHorseId();
								break;
							}
						}
						if (!isHava) {
							Article article = ArticleManager.getInstance().getArticle(PName);
							ArticleEntity zuoqi = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, player, article.getColorType(), 1, true);
							ArticleStatManager.addToArticleStat(null, player, zuoqi, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚坐骑", null);
							if (article instanceof Props) {
								((Props) article).use(player.getCurrentGame(), player, zuoqi);
							}

							if (player.isIsUpOrDown()) {
								player.downFromHorse();
							}
							horseId = player.getHorseIdList().get(player.getHorseIdList().size() - 1);
							if (logger.isWarnEnabled()) logger.warn("[结婚发坐骑player] [{}] [horseID={}]", new Object[] { player.getLogString(), horseId });
						}
						player.setDefaultHorse(horseId);
						SET_DEFAULT_HORSE_RES dres = new SET_DEFAULT_HORSE_RES(GameMessageFactory.nextSequnceNum(), true, horseId);
						player.addMessageToRightBag(dres);
						player.upToHorse(horseId);
						HORSE_RIDE_RES res = new HORSE_RIDE_RES(GameMessageFactory.nextSequnceNum(), horseId, true);
						player.addMessageToRightBag(res);

						isHava = false;
						horseId = 0;
						list = HorseManager.getInstance().getPlayerHorses(other);
						PName = "";
						if (other.getSex() == 0) {
							PName = Translate.text_marriage_爱的炫舞;
						} else {
							PName = Translate.text_marriage_浪漫今生;
						}
						for (int i = 0; i < list.size(); i++) {
							long id = list.get(i);
							Horse horse = HorseManager.getInstance().getHorseById(id, other);
							if (horse == null) {
								continue;
							}
							if (horse.getHorseName().equals(PName)) {
								isHava = true;
								horseId = horse.getHorseId();
								break;
							}
						}
						if (!isHava) {
							Article article2 = ArticleManager.getInstance().getArticle(PName);
							ArticleEntity zuoqi2 = ArticleEntityManager.getInstance().createEntity(article2, true, ArticleEntityManager.CREATE_REASON_MARRIAGE, other, article2.getColorType(), 1, true);
							ArticleStatManager.addToArticleStat(null, other, zuoqi2, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "结婚坐骑", null);
							if (article2 instanceof Props) {
								((Props) article2).use(other.getCurrentGame(), other, zuoqi2);
							}
							if (other.isIsUpOrDown()) {
								other.downFromHorse();
							}
							horseId = other.getHorseIdList().get(other.getHorseIdList().size() - 1);
							if (logger.isWarnEnabled()) logger.warn("[结婚发坐骑other] [{}] [horseID={}]", new Object[] { other.getLogString(), horseId });
						}
						other.setDefaultHorse(horseId);
						other.upToHorse(horseId);
						SET_DEFAULT_HORSE_RES otherdres = new SET_DEFAULT_HORSE_RES(GameMessageFactory.nextSequnceNum(), true, horseId);
						other.addMessageToRightBag(otherdres);
						HORSE_RIDE_RES res1 = new HORSE_RIDE_RES(GameMessageFactory.nextSequnceNum(), horseId, true);
						other.addMessageToRightBag(res1);
					}

					ParticleData[] particleDatas = new ParticleData[1];
					if (info.getLevel() > 2) {
						particleDatas[0] = new ParticleData(player.getId(), "仙缘光效/百年好合", 2000, 2, 1, 1);
					} else {
						particleDatas[0] = new ParticleData(player.getId(), "仙缘光效/新婚快乐", 2000, 2, 1, 1);
					}

					MarriageDownCity city = getDownCityMap().get(info.getCityId());
					if (city != null && city.getGame() != null) {
						LivingObject[] living = city.getGame().getLivingObjects();
						for (int i = 0; i < living.length; i++) {
							if (living[i] instanceof Player) {
								Player sned = (Player) living[i];
								NOTICE_CLIENT_PLAY_PARTICLE_RES client_PLAY_PARTICLE_RES = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
								sned.addMessageToRightBag(client_PLAY_PARTICLE_RES);
							}
						}
					}
				} catch (Exception e) {
					logger.error("交换戒指后发装备戒指或坐骑出错", e);
					return;
				}

				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				Option_Cancel opt2 = new Option_Cancel();
				opt2.setText(Translate.确定);
				mw.setOptions(new Option[] { opt2 });

				String message = Translate.text_marriage_104;
				MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				player.addMessageToRightBag(menu);
				MARRIAGE_MENU menu2 = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
				other.addMessageToRightBag(menu2);
				String title0 = "";
				String title1 = "";
				if (player.getSex() == 0) {
					title0 = player.getName() + Translate.text_marriage_105;
					title1 = other.getName() + Translate.text_marriage_105;
					PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, title1, true);
					PlayerTitlesManager.getInstance().addSpecialTitle(other, Translate.text_marriage_结婚, title0, true);
				} else {
					title0 = other.getName() + Translate.text_marriage_105;
					title1 = player.getName() + Translate.text_marriage_105;
					PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, title0, true);
					PlayerTitlesManager.getInstance().addSpecialTitle(other, Translate.text_marriage_结婚, title1, true);
				}

				info.setState(MarriageInfo.STATE_DROP);
				info.setDropTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				info.setDropNum(0);
				if (logger.isWarnEnabled()) logger.warn("[结婚交换戒指] [最终完成] [{}] [{}]", new Object[] { player.getLogString(), info.getLogString() });
			} else {
				info.setIsjiaohuan(true);
				if (logger.isWarnEnabled()) logger.warn("[结婚交换戒指] [一个交换了] [{}] [{}]", new Object[] { player.getLogString(), info.getLogString() });
			}
			MARRIAGE_JIAOHUAN2OTHER_RES res = new MARRIAGE_JIAOHUAN2OTHER_RES(GameMessageFactory.nextSequnceNum(), true);
			other.addMessageToRightBag(res);
		} else {
			if (logger.isWarnEnabled()) logger.warn("[取消交换] [{}] [{}]", new Object[] { player.getLogString(), info.getId() });
			info.setIsjiaohuan(false);
			MARRIAGE_JIAOHUAN2OTHER_RES res = new MARRIAGE_JIAOHUAN2OTHER_RES(GameMessageFactory.nextSequnceNum(), false);
			other.addMessageToRightBag(res);
		}
	}

	public void msg_cancel(Player player, int cancelType) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			logger.error("msg_cancel角色结婚信息不存在");
			return;
		}
		Player other = null;
		if (info.getHoldA() == player.getId()) {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldB());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
			}
		} else {
			try {
				other = PlayerManager.getInstance().getPlayer(info.getHoldA());
			} catch (Exception e) {
				logger.error("交换戒指取另个角色出错:", e);
			}
		}
		String message = "";
		if (cancelType == 0) {
			message = Translate.text_marriage_106;
		} else if (cancelType == 1) {
			message = Translate.text_marriage_107;
		} else if (cancelType == 2) {
			message = Translate.text_marriage_108;
		}
		if (logger.isInfoEnabled()) logger.info("[取消设置婚礼] [p={}] [{}]", new Object[] { player.getId() + "/" + player.getName(), message });
		MARRIAGE_CANCEL_RES res = new MARRIAGE_CANCEL_RES(GameMessageFactory.nextSequnceNum(), cancelType, message);
		other.addMessageToRightBag(res);
	}

	/**
	 * 参加某个婚礼
	 * @param player
	 * @param infoId
	 */
	public void msg_joinHunLiScreen(Player player, long infoId) {
		MarriageInfo info = getMarriageInfoById(infoId);
		if (info == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_046);
			player.addMessageToRightBag(req);
			return;
		}
		MarriageDownCity city = getDownCityMap().get(info.getCityId());
		if (city == null) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_marriage_047);
			player.addMessageToRightBag(req);
			return;
		}
		MapArea mapArea = city.getGame().gi.getMapAreaByName(Translate.出生点);
		Random random = new Random();
		int x = mapArea.getX() + random.nextInt(mapArea.getWidth());
		int y = mapArea.getY() + random.nextInt(mapArea.getHeight());
		player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, city.getGame().gi.name, x, y));
		getPlayer2CityMap().put(player.getId(), city.getId());
		if (logger.isInfoEnabled()) logger.info("[参加他人婚礼] [p={}] [info={}]", new Object[] { player.getId() + "/" + player.getName(), infoId });
	}

	public GET_MARRIAGE_FRIEND_RES msg_getFriendList(RequestMessage message, Player player) {
		GET_MARRIAGE_FRIEND_REQ req = (GET_MARRIAGE_FRIEND_REQ) message;
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		relation.reSortFriendList();
		List<Long> online = new ArrayList<Long>(relation.getOnline());
		List<Long> marryList = new ArrayList<Long>();
		long id;
		for (int i = 0; i < online.size(); i++) {
			id = online.get(i);
			try {
				PlayerSimpleInfo p = PlayerSimpleInfoManager.getInstance().getInfoById(id);
				if (p != null) {
					if (p.getSex() != player.getSex()) {
						marryList.add(p.getId());
					}

				}

			} catch (Exception e) {
				logger.error("[获取可以结婚的好友列表,去除同性] [" + id + "] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] []", e);
			}

		}
		online = marryList;
		if (online.size() == 0) {
			GET_MARRIAGE_FRIEND_RES res = new GET_MARRIAGE_FRIEND_RES(req.getSequnceNum(), new Player_RelatinInfo[0]);
			return res;
		}

		List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
		byte relationShip;

		String icon;
		String name;
		boolean isonline;
		String mood;
		for (int i = 0; i < online.size(); i++) {

			id = online.get(i);
			try {
				// Player p = PlayerManager.getInstance().getPlayer(id);
				PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
				icon = "";
				name = info.getName();
				isonline = info.getSex() == 0 ? true : false;//
				mood = info.getMood();
				relationShip = (byte) SocialManager.getInstance().getRelationA2B(player.getId(), id);
				list.add(new Player_RelatinInfo(relationShip, id, info.getCareer(), icon, name, isonline, mood));
			} catch (Exception e) {
				logger.error("[获取可以结婚的好友列表] [" + id + "] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "] []", e);
			}

		}
		GET_MARRIAGE_FRIEND_RES res = new GET_MARRIAGE_FRIEND_RES(req.getSequnceNum(), list.toArray(new Player_RelatinInfo[0]));
		return res;
	}

	// 点击送花
	public MARRIAGE_TARGET_MENU_RES msg_targetMenuBeqStart(Player from, long toID) {
		Player to = null;
		if (PlayerManager.getInstance().isOnline(toID)) {
			try {
				to = PlayerManager.getInstance().getPlayer(toID);
			} catch (Exception e) {
				logger.error("结婚送花对象", e);
				return null;
			}

		} else {
			from.sendError(Translate.text_marriage_037);
			return null;
		}

		// if (from.getSex() == to.getSex()) {
		// from.sendError(Translate.text_marriage_109);
		// return null;
		// }

		long[] entityIDs = new long[Song_nan_name.length];
		int[] haveNum = new int[Song_nan_name.length];
		String[] nameS;
		if (from.getSex() == 0) {
			// 男送女
			nameS = Song_nan_name;
			for (int i = 0; i < haveNum.length; i++) {
				entityIDs[i] = getTempArticleEntity(Song_nan_name[i]).getId();
				haveNum[i] = from.getKnapsack_common().countArticle(Song_nan_name[i]);
			}
		} else {
			// 女送男
			nameS = Song_nv_name;
			for (int i = 0; i < haveNum.length; i++) {
				entityIDs[i] = getTempArticleEntity(Song_nv_name[i]).getId();
				haveNum[i] = from.getKnapsack_common().countArticle(Song_nv_name[i]);
			}
		}

		MARRIAGE_TARGET_MENU_RES res = new MARRIAGE_TARGET_MENU_RES(GameMessageFactory.nextSequnceNum(), toID, from.getSex(), entityIDs, nameS, Song_Num, haveNum, Song_price);
		return res;
	}

	public void msg_targetSend(Player from, long pID, int flowType, int flowNum) {

		if (flowType < 0) {
			String msg = "";
			if (from.getSex() == 0) {
				msg = Translate.text_marriage_110;
			} else {
				msg = Translate.text_marriage_111;
			}
			from.sendError(msg);
			return;
		}

		if (flowNum < 0) {
			String msg = Translate.text_marriage_112;
			from.sendError(msg);
			return;
		}

		Player to = null;
		if (PlayerManager.getInstance().isOnline(pID)) {
			try {
				to = PlayerManager.getInstance().getPlayer(pID);
			} catch (Exception e) {
				logger.error("结婚送花对象", e);
				return;
			}
		} else {
			from.sendError(Translate.text_marriage_037);
			return;
		}

		if (from.getSex() == to.getSex() && flowType < 3) {
			from.sendError(Translate.text_marriage_109);
			return;
		}
		String name = null;
		if (from.getSex() == 0) {
			// 男的
			name = Song_nan_name[flowType];
		} else {
			name = Song_nv_name[flowType];
		}
		long price = Song_price[flowType];
		int num = Song_Num[flowNum];

		int bagNum = from.getKnapsack_common().countArticle(name);
		long needMoney = 0;
		if (bagNum > num) {
			bagNum = num;
		} else {
			needMoney = (num - bagNum) * price;
		}

		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		Option_Song opt1 = new Option_Song(pID, flowType, flowNum);
		opt1.setText(Translate.确定);
		Option_Cancel opt2 = new Option_Cancel();
		opt2.setText(Translate.取消);
		mw.setOptions(new Option[] { opt1, opt2 });

		String msg = Translate.text_marriage_113 + (from.getSex() == 0 ? Translate.text_marriage_114 : Translate.text_marriage_115) + Translate.text_marriage_116 + TradeManager.putMoneyToMyText(needMoney);
		CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), msg, mw.getOptions());
		from.addMessageToRightBag(req);
	}

	public void opt_targetSend(Player from, long pID, int flowType, int flowNum) {
		Player to = null;
		if (PlayerManager.getInstance().isOnline(pID)) {
			try {
				to = PlayerManager.getInstance().getPlayer(pID);
			} catch (Exception e) {
				logger.error("结婚送花对象", e);
				return;
			}
		} else {
			from.sendError(Translate.text_marriage_037);
			return;
		}

		if (from.getSex() == to.getSex() && flowType < 3) {
			from.sendError(Translate.text_marriage_109);
			return;
		}
		String name = null;
		if (from.getSex() == 0) {
			// 男的
			name = Song_nan_name[flowType];
		} else {
			name = Song_nv_name[flowType];
		}
		long price = Song_price[flowType];
		int num = Song_Num[flowNum];

		int bagNum = from.getKnapsack_common().countArticle(name);
		int haveNum = bagNum;
		long needMoney = 0;
		if (bagNum > num) {
			bagNum = num;
		} else {
			needMoney = (num - bagNum) * price;
		}

		if (needMoney > from.getSilver() + from.getShopSilver()) {
			BillingCenter.银子不足时弹出充值确认框(from, Translate.text_marriage_056);
			return;
		}

		int kouNum = 0; // 扣掉的数目
		synchronized (from.tradeKey) {
			while (bagNum > 0) {
				int index = from.getKnapsack_common().getArticleCellPos(name);
				if (index < 0) {
					logger.error("[msg_targetSend出错] [预计有花但在背包中找不到] [p={}] [NAME={}] [haveNum={}] [kouNum={}] [index={}]", new Object[] { from.getLogString(), name, haveNum, kouNum, index });
					return;
				}
				Cell cell = from.getKnapsack_common().getCell(index);
				if (cell.getCount() > bagNum) {
					kouNum += cell.getCount() - bagNum;
					from.getKnapsack_common().clearCell(index, bagNum, "结婚送花", true);
					bagNum = 0;
				} else {
					kouNum += cell.getCount();
					bagNum = bagNum - cell.getCount();
					from.getKnapsack_common().clearCell(index, "结婚", true);
				}
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[msg_targetSend] [扣花] [p={}] [toID={}] [NAME={}] [haveNum={}]", new Object[] { from.getLogString(), to.getId(), name, haveNum });
		if (needMoney > 0) {
			try {
				BillingCenter.getInstance().playerExpense(from, needMoney, CurrencyType.SHOPYINZI, ExpenseReasonType.MARRIAGE_JIEHUN, "送花或糖" + flowType);
				NewChongZhiActivityManager.instance.doXiaoFeiActivity(from, needMoney, NewXiaoFeiActivity.XIAOFEI_TYPE_SHOP);
				if (logger.isWarnEnabled()) logger.warn("[msg_targetSend] [扣钱] [p={}] [toID={}] [money={}] [haveNum={}]", new Object[] { from.getLogString(), to.getId(), needMoney, haveNum });
			} catch (NoEnoughMoneyException e) {
				logger.error("求婚送花失败: 需要补回花:" + name + "-" + haveNum, e);
				return;
			} catch (BillFailedException e) {
				logger.error("求婚送花失败: 需要补回花:" + name + "-" + haveNum, e);
				return;
			}
		}
		if (flowType < 3) {
			String msg = Translate.translateString(Translate.谁送给了谁多少朵什么花, new String[][] { { Translate.STRING_1, from.getName() }, { Translate.STRING_2, to.getName() }, { Translate.COUNT_1, "" + num }, { Translate.STRING_3, from.getSex() == 0 ? Translate.text_marriage_063 : Translate.text_marriage_068 }, { Translate.STRING_4, name } });
			for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
				pp.send_HINT_REQ(msg, (byte) 2);
			}
			if (from.getSex() == 0) {
				// 男
				BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(to.getId());
				bData.设置送花(to, num * flowOrSweet_billboardValue[flowType]);
				AchievementManager.getInstance().record(to, RecordAction.获得鲜花数量, num);
			} else {
				BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(to.getId());
				bData.设置送糖(to, num * flowOrSweet_billboardValue[flowType]);
				AchievementManager.getInstance().record(to, RecordAction.获得糖果数量, num);
			}
			if (logger.isWarnEnabled()) logger.warn("[结婚送花] [成功] [p={}] [type={}] [to={}] [name={}] [haveNum={}] [money={}]", new Object[] { from.getLogString(), flowType, to.getLogString(), name, haveNum, needMoney });
		} else {
			if(name != null){
				if(name.equals(Translate.棉花糖)){
					String msg = "<f color='0xFFCC00'>          " + from.getName() + "送给了你" + num + "个" + name + "</f>";
					String fromMessage = "<f color='0xFFCC00'>你送给了" + to.getName() + " " + num + "个" + name + "</f>";
					from.sendError(fromMessage);
					to.send_HINT_REQ(msg, (byte) 2);
					// TODO:如果加排行 这里加
					BillboardStatDate bData = BillboardStatDateManager.getInstance().getBillboardStatDate(to.getId());
					bData.设置送棉花糖(to, num);
					AchievementManager.getInstance().record(to, RecordAction.获得棉花糖果数量, num);
					if (logger.isWarnEnabled()) logger.warn("[送棉花糖] [成功] [p={}] [type={}] [to={}] [name={}] [haveNum={}] [money={}] [kouNum={}] [num={}]", new Object[] { from.getLogString(), flowType, to.getLogString(), name, haveNum, needMoney, kouNum, num });
				}else if(name.equals(Translate.情人糖)){
					String msg = Translate.translateString(Translate.情人节送花提示, new String[][] { { Translate.STRING_1, to.getName() }, { Translate.STRING_2, from.getName() },  { Translate.STRING_3, name}, { Translate.COUNT_1, "" + num } });
					for (Player pp : PlayerManager.getInstance().getOnlinePlayers()) {
						pp.send_HINT_REQ(msg, (byte) 2);
					}
					if (logger.isWarnEnabled()) logger.warn("[送棉花糖] [成功] [p={}] [type={}] [to={}] [name={}] [haveNum={}] [money={}] [kouNum={}] [num={}]", new Object[] { from.getLogString(), flowType, to.getLogString(), name, haveNum, needMoney, kouNum, num });
				}
				List<SendFlowerActivityConfig> sendFlowerActivity = ActivityManager.getInstance().sendFlowerActivity;
				for(SendFlowerActivityConfig activityConfig : sendFlowerActivity){
					if(activityConfig != null && activityConfig.isEffectActivity(name, num)){
						if(activityConfig.getSenderRewardArticleName() != null && activityConfig.getSenderRewardArticleNum() > 0){
							try{
								Article a = ArticleManager.getInstance().getArticle(activityConfig.getSenderRewardArticleName());
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.送花奖励, from, a.getColorType(), activityConfig.getSenderRewardArticleNum(), true);
								String mess = Translate.translateString(Translate.恭喜获得物品, new String[][] { { Translate.STRING_1, activityConfig.getSenderRewardArticleName() }, { Translate.COUNT_1, String.valueOf(activityConfig.getSenderRewardArticleNum()) } });
								MailManager.getInstance().sendMail(from.getId(), new ArticleEntity[]{ae}, new int[]{activityConfig.getSenderRewardArticleNum()}, mess, mess, 0, 0, 0, "送花活动(赠送者)");
							}catch(Exception e){
								logger.warn("[给赠送者发奖励] [{}] [异常:{}]",from.getLogString(), e);
							}
						}
						if(activityConfig.getReceiveRewardArticleName() != null && activityConfig.getReceiveRewardArtilceNum() > 0){
							try{
								Article a = ArticleManager.getInstance().getArticle(activityConfig.getReceiveRewardArticleName());
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.送花奖励, to, a.getColorType(), activityConfig.getReceiveRewardArtilceNum(), true);
								String mess = Translate.translateString(Translate.恭喜获得物品, new String[][] { { Translate.STRING_1, activityConfig.getReceiveRewardArticleName() }, { Translate.COUNT_1, String.valueOf(activityConfig.getReceiveRewardArtilceNum()) } });
								MailManager.getInstance().sendMail(to.getId(), new ArticleEntity[]{ae}, new int[]{activityConfig.getReceiveRewardArtilceNum()}, mess, mess, 0, 0, 0, "送花活动(接受者)");
							}catch(Exception e){
								logger.warn("[给接受者发奖励] [{}] [异常:{}]",from.getLogString(), e);
							}
						}
						if(activityConfig.isHasNotice()){
							String msg = Translate.translateString(Translate.情人节送花提示, new String[][] { { Translate.STRING_1, to.getName() }, { Translate.STRING_2, from.getName() },  { Translate.STRING_3, name}, { Translate.COUNT_1, "" + num } });
							if(name.equals(Translate.洋气红包)){
								msg = Translate.translateString(Translate.情人节送洋气红包提示, new String[][] { { Translate.STRING_1, to.getName() }, { Translate.STRING_2, from.getName() },  { Translate.STRING_3, name}, { Translate.COUNT_1, "" + num } });
							}
							ChatMessage chatMessage = new ChatMessage();
							chatMessage.setMessageText(msg);
							try {
								ChatMessageService.getInstance().sendMessageToSystem(chatMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(activityConfig.getLiZiName() != null && activityConfig.getLiZiName().length() > 0){
							//放粒子规则
							Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
							for (Player p : onlinePlayers) {
								ParticleData[] particleDatas = new ParticleData[1];
								particleDatas[0] = new ParticleData(p.getId(), activityConfig.getLiZiName(), 2000, 2, 1, 1);
								NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
								p.addMessageToRightBag(resp);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 发愿意不愿意窗口
	 * @param beq
	 */
	private void sendBeqWindow(MarriageBeq beq) {
		try {
			if (logger.isInfoEnabled()) logger.info("[发送结婚beq] [开始] [fromID={}] [state={}] [level={}] [toID={}]", new Object[] { beq.getId(), beq.getState(), beq.getLevel(), beq.getToId() });
			beq.setState(MarriageBeq.STATE_START1);
			PlayerSimpleInfo from = PlayerSimpleInfoManager.getInstance().getInfoById(beq.getSendId());
			Player to = PlayerManager.getInstance().getPlayer(beq.getToId());
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_Marriage_Beq2Other opt1 = new Option_Marriage_Beq2Other(beq, 0);
			opt1.setText(Translate.text_marriage_愿意);
			Option_Marriage_Beq2Other opt2 = new Option_Marriage_Beq2Other(beq, 1);
			opt2.setText(Translate.text_marriage_不愿意);
			mw.setOptions(new Option[] { opt1, opt2 });
			String message = "";
			if (from.getSex() == 0) {
				message = "<f color='0xffff00'>" + from.getName() + "</f>" + Translate.text_marriage_以 + Beq_num[beq.getLevel()] + Translate.text_marriage_063 + nanBeq_name[beq.getLevel()] + Translate.text_marriage_081;
			} else {
				message = "<f color='0xffff00'>" + from.getName() + "</f>" + Translate.text_marriage_以 + Beq_num[beq.getLevel()] + Translate.text_marriage_068 + nvBeq_name[beq.getLevel()] + Translate.text_marriage_081;
			}
			MARRIAGE_MENU menu = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), message, mw.getOptions());
			to.addMessageToRightBag(menu);
			beq.setSendTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			if (logger.isInfoEnabled()) logger.info("[发送结婚beq] [结束] [fromID={}] [state={}] [level={}] [toID={}]", new Object[] { beq.getId(), beq.getState(), beq.getLevel(), beq.getToId() });
		} catch (Exception e) {
			logger.error("求婚发给另外一个人出错：", e);
		}
	}

	public void setDownCityMap(Hashtable<Long, MarriageDownCity> downCityMap) {
		this.downCityMap = downCityMap;
	}

	public Hashtable<Long, MarriageDownCity> getDownCityMap() {
		return downCityMap;
	}

	public void setPlayer2CityMap(Hashtable<Long, Long> player2CityMap) {
		this.player2CityMap = player2CityMap;
	}

	public Hashtable<Long, Long> getPlayer2CityMap() {
		return player2CityMap;
	}

	public void setMarriageCityName(List<String> marriageCityName) {
		this.marriageCityName = marriageCityName;
	}

	public List<String> getMarriageCityName() {
		return marriageCityName;
	}

	public boolean 使用比翼令(Player player) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			player.sendError(Translate.text_marriage_023);
			return false;
		}
		Player other = null;
		long otherID = 0;
		if (info.getHoldA() == player.getId()) {
			otherID = info.getHoldB();
		} else if (info.getHoldB() == player.getId()) {
			otherID = info.getHoldA();
		}
		boolean isXianjie = false;
		try {
			String mapname = player.getCurrentGame().gi.name;
			isXianjie = RobberyConstant.没飞升玩家不可进入的地图.contains(mapname);
		} catch (Exception e) {
			
		}
		try {
			String result = GlobalTool.verifyMapTrans(otherID);
			if (result != null) {
				if (Translate.挑战仙尊限制功能.equals(result)) {
					player.sendError(Translate.对方正在挑战仙尊);
				} else {
					player.sendError(result);
				}
				return false;
			}
			String result2 = GlobalTool.verifyTransByOther(player.getId());
			if (result2 != null) {
				player.sendError(result2);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (otherID <= 0) {
			player.sendError(Translate.text_marriage_118);
			return false;
		}
		if (PlayerManager.getInstance().isOnline(otherID)) {
			try {
				other = PlayerManager.getInstance().getPlayer(otherID);
//				if(JJCManager.isJJCBattle(other.getCurrentGame().gi.name)){
//					return false;
//				}
				if(DiceManager.getInstance().isDiceGame(other)){
					return false;
				}
				if(WolfManager.getInstance().isWolfGame(other)){
					return false;
				}
			} catch (Exception e) {
				logger.error("使用比翼令出错：" + player.getId(), e);
				player.sendError(Translate.text_marriage_119);
				return false;
			}
		} else {
			player.sendError(Translate.text_marriage_037);
			return false;
		}
		if (isXianjie && other.getLevel() <= 220) {
			player.sendError(Translate.仙界不能传送220以下玩家);
			return false;
		}

		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		mw.setTitle(Translate.text_marriage_120);
		Option_biYiLing option = new Option_biYiLing(player.getCurrentGame().gi.getName(), player.getTransferGameCountry(), player.getX(), player.getY());
		option.setText(Translate.确定);
		option.setUsePlayer(player);
		Option_biYiLing_cancle cancel = new Option_biYiLing_cancle();
		cancel.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancel });
		MARRIAGE_MENU res = new MARRIAGE_MENU(GameMessageFactory.nextSequnceNum(), mw.getId(), Translate.text_marriage_121 + player.getName() + Translate.text_marriage_122, mw.getOptions());
		;
		other.addMessageToRightBag(res);
		return true;
	}

	public void 确定召唤(Player player, int gamecountry, String name, int x, int y) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}
		Player other = null;
		long otherID = 0;
		if (info.getHoldA() == player.getId()) {
			otherID = info.getHoldB();
		} else if (info.getHoldB() == player.getId()) {
			otherID = info.getHoldA();
		}
		if (otherID <= 0) {
			player.sendError(Translate.text_marriage_118);
			return;
		}
		try {
			other = PlayerManager.getInstance().getPlayer(otherID);
		} catch (Exception e) {
			logger.error("确定召唤：" + player.getId(), e);
			player.sendError(Translate.text_marriage_119);
			return;
		}
		if (player.getCurrentGame() != null) {
			player.setTransferGameCountry(gamecountry);
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, name, x, y));
		}
	}

	public void 取消召唤(Player player) {
		MarriageInfo info = getMarriageInfoById(SocialManager.getInstance().getRelationById(player.getId()).getMarriageId());
		if (info == null) {
			player.sendError(Translate.text_marriage_023);
			return;
		}
		Player other = null;
		long otherID = 0;
		if (info.getHoldA() == player.getId()) {
			otherID = info.getHoldB();
		} else if (info.getHoldB() == player.getId()) {
			otherID = info.getHoldA();
		}
		if (otherID <= 0) {
			player.sendError(Translate.text_marriage_118);
			return;
		}
		try {
			other = PlayerManager.getInstance().getPlayer(otherID);
		} catch (Exception e) {
			logger.error("取消召唤：" + player.getId(), e);
			player.sendError(Translate.text_marriage_119);
			return;
		}
		other.send_HINT_REQ(Translate.text_marriage_123, (byte) 5);
	}

}
