package com.fy.engineserver.uniteserver;

import static com.fy.engineserver.datasource.language.Translate.的徒弟;
import static com.fy.engineserver.util.TimeTool.TimeDistance.DAY;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.activity.chongzhiActivity.ChargeRecord;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardRecord;
import com.fy.engineserver.activity.clifford.CliffordData;
import com.fy.engineserver.activity.fairyRobbery.instance.FairyRobberyEntity;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.activity.levelUpReward.instance.LevelUpRewardEntity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.activity.unitedserver.DailyLoginActivity;
import com.fy.engineserver.activity.village.data.VillageData;
import com.fy.engineserver.articleEnchant.EnchantData;
import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.auction.Auction;
import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.buffsave.BuffSave;
import com.fy.engineserver.cityfight.citydata.CityData;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.data.CountryManagerData;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntArticleExtraData;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gm.feedback.Feedback;
import com.fy.engineserver.gm.feedback.GMRecord;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.hotspot.HotspotInfo;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.BiaoCheEntity;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.marriage.MarriageBeq;
import com.fy.engineserver.marriage.MarriageDownCity;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeInfo;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.newBillboard.BillboardInfo;
import com.fy.engineserver.newBillboard.BillboardStatDate;
import com.fy.engineserver.newtask.DeliverTask;
import com.fy.engineserver.newtask.NewDeliverTask;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.notice.Notice;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.qiancengta.QianCengTa_Dao;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.seal.data.SealTaskInfo;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.sifang.info.SiFangInfo;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.fy.engineserver.talent.TalentData;
import com.fy.engineserver.tournament.data.OneTournamentData;
import com.fy.engineserver.tournament.data.TournamentData;
import com.fy.engineserver.trade.requestbuy.RequestBuy;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl;

/**
 * 合服管理器 <BR/>
 * 启动的服务器是主服务器,将其他服务器的数据合并到当前的服务器
 * 
 * 合服过程 : <li>1)配置要合并的服务器 config(UnitedServer[] servers)</li> <li>2)清除一些diskcache(非程序执行)</li> <li>3)修改部分重名的数据(家族,宗派)</li> <li>4)合并数据</li>
 * 
 * 
 */
public class UnitedServerManager implements MConsole {

	public static String GaiMing_BiaoJi = "@"; // 改名标记
	public static int singleClassUnsaveMax = 3000;// 单个类未保存上限.超过这个上限,不再通知底层存储

	public static Logger logger = LoggerFactory.getLogger(UnitedServerManager.class);
	public static Logger ExcepTionlogger = LoggerFactory.getLogger("Exception");

	private Map<String, UnitedServer> allserverMap = new HashMap<String, UnitedServer>();// 所有配置的服务器
	private Map<String, UnitedServer> unitedServerMap = new HashMap<String, UnitedServer>();// 所有被合并的服务器,不包括主服务器
	private UnitedServer mainServer;// 主服务器

	/** 同名角色存储,在角色要进入的地方去做一些事 */
	private DiskCache sameNamePlayerCache;// <id,name> <changeName,List<Ids>
	public static String changeNameListKey = "changeNameList";
	private String diskFile;

	private static UnitedServerManager instance;

	public boolean releaseReference = true;

	public static int ONCE_QUERY = 9000;

	public static int sameTimeRunUnitedClassNum = 5;// 同时合并表的上限

	public static List<Class<?>> uniteClasses = new ArrayList<Class<?>>();// 要合并的所有表
	public static List<Class<?>> clearClasses = new ArrayList<Class<?>>();// 要清除的表

	public static Map<Class<?>, DataCollect<?>> dataCollectMap = new Hashtable<Class<?>, DataCollect<?>>();
	public static Map<Class<?>, DataUnit<?>> dataUnitMap = new Hashtable<Class<?>, DataUnit<?>>();

	private List<UnitedServerThread> currentUnitedThreads = new ArrayList<UnitedServerThread>();// 当前正在执行合并的Class

	private static long unitedServerStartTime;// 合服开始时间
	/** 合并完毕的类数量 */
	static int uniteClassesNum = 0;
	private long startTime;

	@ChangeAble(value = "是否合并数据")
	public static boolean unitedData = true;
	@ChangeAble(value = "是否改名字")
	public static boolean unitedChanagerName = true;
	// public static boolean unitedChanagerName = false;
	@ChangeAble(value = "是否只合并物品")
	public static boolean unitedOnlyArticleEntity = false;
	//public static boolean unitedOnlyArticleEntity = true;
	public static boolean removeData = true;
//	public static boolean removeData = false;

	/** 合服(或者其他)后登录活动 */
	public List<DailyLoginActivity> dailyLoginActivities = new ArrayList<DailyLoginActivity>();

	// 登录活动KEY,数据格式<DAILYLOGINACTIVITY_KEY,Set<playerId>>and playerID作为单独key 值为;Set<活动ID>.这样存储玩家只影响自己的数据,存储的时候速度快,但是会使得键值对很多
	public static final String DAILYLOGINACTIVITY_KEY = "DAILYLOGINACTIVITY_KEY";

	static {
		uniteClasses.add(ArticleEntity.class);
		uniteClasses.add(DeliverTask.class);
		uniteClasses.add(TaskEntity.class);
		uniteClasses.add(Player.class);
		uniteClasses.add(JiazuMember.class);
		uniteClasses.add(Jiazu.class);
		uniteClasses.add(SeptStation.class);
		uniteClasses.add(Cave.class);
		uniteClasses.add(GameDataRecord.class);
		//2014年5月12日9:46:02  删除  mazhencai
//		uniteClasses.add(AchievementEntity.class);
		uniteClasses.add(QianCengTa_Ta.class);
		uniteClasses.add(HotspotInfo.class);
		uniteClasses.add(MarriageBeq.class);
		uniteClasses.add(MarriageInfo.class);
		uniteClasses.add(MarriageDownCity.class);
		uniteClasses.add(Unite.class);
		uniteClasses.add(MasterPrenticeInfo.class);
		uniteClasses.add(Relation.class);
		uniteClasses.add(ZongPai.class);
		uniteClasses.add(Horse.class);
		uniteClasses.add(Pet.class);
		uniteClasses.add(BillboardStatDate.class);
		uniteClasses.add(FateActivity.class);
		uniteClasses.add(BillboardInfo.class);
		// 2013-10-14 17:13:22新增
		uniteClasses.add(PetsOfPlayer.class);
		uniteClasses.add(PlayerActivenessInfo.class);
		uniteClasses.add(ArticleProtectData.class);
		uniteClasses.add(SkBean.class);
		uniteClasses.add(TransitRobberyEntity.class);
		// 2013-10-28 18:06新增 liuyang
		uniteClasses.add(BuffSave.class);
		// 2013-12-27 10:22:25新增 madcat
		uniteClasses.add(NewDeliverTask.class);
		// 2014年5月12日9:44:20 新增  mazhencai
		uniteClasses.add(Horse2RelevantEntity.class);
		uniteClasses.add(PlayerAimsEntity.class);
		uniteClasses.add(SealTaskInfo.class);
		//2014-12-16 11:09 新增九个类 liuyang
			uniteClasses.add(JiazuMember2.class);
			uniteClasses.add(BiaoCheEntity.class);
//			uniteClasses.add(JJCResultRecord.class);
//			uniteClasses.add(JJCBillboardTeamInfo.class);//20150629 wtx 过往赛季排行记录不需要合了
			uniteClasses.add(Faery.class);		//2015年1月16日17:35:05     调整仙境数据为需要合的数据
			uniteClasses.add(HuntLifeEntity.class);
			uniteClasses.add(MonthCardRecord.class);
			uniteClasses.add(LevelUpRewardEntity.class);
			uniteClasses.add(ChargeRecord.class);
			uniteClasses.add(EnchantData.class);
			uniteClasses.add(BaoShiXiaZiData.class);
			uniteClasses.add(HuntArticleExtraData.class);
			//2015年9月21日14:10:20   新增
			uniteClasses.add(PetFlyState.class);
			uniteClasses.add(TalentData.class);
			uniteClasses.add(FairyRobberyEntity.class);
			

	}

	static {
		clearClasses.add(CliffordData.class);
		clearClasses.add(OneTournamentData.class);
		clearClasses.add(RequestBuy.class);
		clearClasses.add(Auction.class);
		clearClasses.add(Feedback.class);
		clearClasses.add(Notice.class);
		clearClasses.add(GMRecord.class);
		clearClasses.add(Mail.class);
		clearClasses.add(CityData.class);
		clearClasses.add(Country.class);
		clearClasses.add(TournamentData.class);
		clearClasses.add(CountryManagerData.class);
		clearClasses.add(VillageData.class);
		clearClasses.add(SiFangInfo.class);
		clearClasses.add(Faery.class);
	}
	private boolean initialized = false;

	public UnitedServer getUnitedServerByServerName(String serverName) {
		return allserverMap.get(serverName);
	}

	public static UnitedServerManager getInstance() {
		return instance;
	}

	public List<UnitedServerThread> getCurrentUnitedThreads() {
		return currentUnitedThreads;
	}

	public void setCurrentUnitedThreads(List<UnitedServerThread> currentUnitedThreads) {
		this.currentUnitedThreads = currentUnitedThreads;
	}

	private boolean config(UnitedServer[] servers) {
		if (initialized) {
			throw new IllegalStateException("[系统已经初始化过] [不能再配置]");
		}
		startTime = System.currentTimeMillis();
		for (UnitedServer server : servers) {
			if (server.isMainserver()) {
				if (mainServer != null) {
					throw new IllegalStateException("已经存在了主服务器:" + mainServer.toString() + " [又出现另一个主服务器:" + server.toString() + "] [请重新配置]");
				}
				if (GameConstants.getInstance().getServerName().equals(server.getServerName())) {
					mainServer = server;
				} else {
					throw new IllegalStateException("[要增加的主服务器:" + server.toString() + "] [不是当前服务器:" + GameConstants.getInstance().getServerName() + "] [请重新配置]");
				}
			} else {
				unitedServerMap.put(server.getServerName(), server);
			}
			allserverMap.put(server.getServerName(), server);
		}
		return validate();
	}

	/**
	 * 获得主服务器,即当前服务器
	 * @return
	 */
	public UnitedServer getMainServer() {
		return mainServer;
	}

	private boolean validate() {
		if (allserverMap.size() < 2) {
			logger.error("合区数据异常,map.size()=" + allserverMap.size(), new Exception());
			ExcepTionlogger.error("合区数据异常,map.size()=" + allserverMap.size(), new Exception());
			return false;
		}
		int mailServerNum = 0;
		int otherNum = 0;
		Iterator<UnitedServer> itor = allserverMap.values().iterator();
		while (itor.hasNext()) {
			UnitedServer next = itor.next();
			if (next.isMainserver()) {
				mailServerNum++;
			} else {
				otherNum++;
			}
		}
		if (mailServerNum != 1) {
			logger.error("合区数据异常,主服务器数量:" + mailServerNum, new Exception());
			ExcepTionlogger.error("合区数据异常,主服务器数量:" + mailServerNum, new Exception());
			return false;
		}
		if (otherNum <= 0) {
			logger.error("合区数据异常,被合并的服数量:" + otherNum, new Exception());
			ExcepTionlogger.error("合区数据异常,被合并的服数量:" + otherNum, new Exception());
			return false;
		}
		boolean succUnite = validateClasses(uniteClasses);
		boolean succClean = validateClasses(clearClasses);
		if (!succUnite || !succClean) {
			logger.error("合区数据异常,class配置错误:" + "[succUnite:" + succUnite + "] [succClean:" + succClean + "]", new Exception());
			ExcepTionlogger.error("合区数据异常,class配置错误:" + "[succUnite:" + succUnite + "] [succClean:" + succClean + "]", new Exception());
			return false;
		}
		return true;
	}

	private boolean validateClasses(List<Class<?>> classes) {
		boolean hasWrongClass = false;
		StringBuffer sbf = new StringBuffer("[class配置错误,含有非SimpleEntity] ");
		for (Class<?> clazz : classes) {
			SimpleEntity annotation = clazz.getAnnotation(SimpleEntity.class);
			if (annotation == null) {
				hasWrongClass = true;
				sbf.append("[" + clazz.getSimpleName() + "] ");
			}
		}
		if (hasWrongClass) {
			throw new IllegalStateException(sbf.toString());
		}
		return true;
	}

	/**
	 * 修改家族名字
	 */
	private void changeJiazuName() {
		try {
			HashMap<String, HashSet<Jiazu>> allJiaZus = new HashMap<String, HashSet<Jiazu>>();
			{
				// 取主服务器上的所有家族名字
				SimpleEntityManager<Jiazu> jiazuEntityMananger = mainServer.getFactory().getSimpleEntityManager(Jiazu.class);
				long count = jiazuEntityMananger.count();
				long start = 1;
				long end = 1;
				long pagSize = 500;
				allJiaZus.put(mainServer.getServerName(), new HashSet<Jiazu>());
				HashSet<Jiazu> jiazuSet = allJiaZus.get(mainServer.getServerName());
				while (count > 0) {
					long now = System.currentTimeMillis();
					end = start + pagSize;
					List<Jiazu> jiazus = jiazuEntityMananger.query(Jiazu.class, "", "", start, end);
					if (jiazus != null) {
						for (int i = 0; i < jiazus.size(); i++) {
							jiazuSet.add(jiazus.get(i));
						}
					}
					start += pagSize;
					count -= pagSize;
					logger.warn("[家族改名->加载家族] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { mainServer.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
				}
			}
			for (UnitedServer unitedServer : unitedServerMap.values()) {
				SimpleEntityManager<Jiazu> entityManager = unitedServer.getFactory().getSimpleEntityManager(Jiazu.class);
				long count = entityManager.count();
				long start = 1;
				long end = 1;
				long pagSize = 500;
				allJiaZus.put(unitedServer.getServerName(), new HashSet<Jiazu>());
				HashSet<Jiazu> jiazuSet = allJiaZus.get(unitedServer.getServerName());
				while (count > 0) {
					long now = System.currentTimeMillis();
					end = start + pagSize;
					List<Jiazu> jiazus = entityManager.query(Jiazu.class, "", "", start, end);
					if (jiazus != null) {
						for (int i = 0; i < jiazus.size(); i++) {
							Jiazu jiazu = jiazus.get(i);
							jiazuSet.add(jiazu);
						}
					}
					start += pagSize;
					count -= pagSize;
					logger.warn("[家族改名->加载家族] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { unitedServer.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
				}
			}
			HashMap<String, HashSet<Jiazu>> chongMingJiaZus = new HashMap<String, HashSet<Jiazu>>();
			for (String serverName : allJiaZus.keySet()) {
				HashSet<Jiazu> jiazuSet = allJiaZus.get(serverName);
				for (Jiazu jizu : jiazuSet) {
					for (String otherServerName : allJiaZus.keySet()) {
						if (otherServerName == serverName) {
							continue;
						}
						HashSet<Jiazu> otherJiazuSet = allJiaZus.get(otherServerName);
						for (Jiazu otherJiazu : otherJiazuSet) {
							if (otherJiazu.getName().equals(jizu.getName())) {
								// 重名
								HashSet<Jiazu> chongming = chongMingJiaZus.get(otherServerName);
								if (chongming == null) {
									chongming = new HashSet<Jiazu>();
									chongMingJiaZus.put(otherServerName, chongming);
								}
								chongming.add(otherJiazu);
								logger.warn("[家族重名] [{}] [{}] [{}] [{}]", new Object[] { otherServerName, otherJiazu.getLogString(), serverName, jizu.getLogString() });
							}
						}
					}
				}
			}
			for (String serverName : chongMingJiaZus.keySet()) {
				UnitedServer s = unitedServerMap.get(serverName);
				HashSet<Jiazu> jiazus = chongMingJiaZus.get(serverName);
				if (s == null) {
					SimpleEntityManager<Jiazu> jiazuEntityMananger = mainServer.getFactory().getSimpleEntityManager(Jiazu.class);
					for (Jiazu j : jiazus) {
						j.setName(j.getName() + GaiMing_BiaoJi + serverName);
						logger.warn("[家族改名] [{}] [{}]", new Object[] { serverName, j.getLogString() });
						jiazuEntityMananger.flush(j);
					}
				} else {
					SimpleEntityManager<Jiazu> entityManager = s.getFactory().getSimpleEntityManager(Jiazu.class);
					for (Jiazu j : jiazus) {
						j.setName(j.getName() + GaiMing_BiaoJi + serverName);
						logger.warn("[家族改名] [{}] [{}]", new Object[] { serverName, j.getLogString() });
						entityManager.flush(j);
					}
				}
			}
		} catch (Exception e) {
			logger.error("修改家族名字出错", e);
			ExcepTionlogger.error("修改家族名字出错", e);
		}
	}
	
	/**
	 * 修改宗派名字
	 */
	private void changeZongpaiName() {
		try {
			HashMap<String, HashSet<ZongPai>> allZongPais = new HashMap<String, HashSet<ZongPai>>();
			{
				// 取主服务器上的所有家族名字
				SimpleEntityManager<ZongPai> zongPaiEntityMananger = mainServer.getFactory().getSimpleEntityManager(ZongPai.class);
				allZongPais.put(mainServer.getServerName(), new HashSet<ZongPai>());
				HashSet<ZongPai> zongpais = allZongPais.get(mainServer.getServerName());
				long count = zongPaiEntityMananger.count();
				long start = 1;
				long end = 1;
				long pagSize = 500;

				while (count > 0) {
					long now = System.currentTimeMillis();
					end = start + pagSize;
					List<ZongPai> zongPais = zongPaiEntityMananger.query(ZongPai.class, "", "", start, end);
					if (zongPais != null) {
						for (int i = 0; i < zongPais.size(); i++) {
							zongpais.add(zongPais.get(i));
						}
					}
					start += pagSize;
					count -= pagSize;
					logger.warn("[宗派改名->加载宗派] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { mainServer.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
				}
			}
			for (UnitedServer unitedServer : unitedServerMap.values()) {
				try {
					SimpleEntityManager<ZongPai> entityManager = unitedServer.getFactory().getSimpleEntityManager(ZongPai.class);
					allZongPais.put(unitedServer.getServerName(), new HashSet<ZongPai>());
					HashSet<ZongPai> zongpais = allZongPais.get(unitedServer.getServerName());
					long count = entityManager.count();
					long start = 1;
					long end = 1;
					long pagSize = 500;

					while (count > 0) {
						long now = System.currentTimeMillis();
						end = start + pagSize;
						List<ZongPai> zongPais = entityManager.query(ZongPai.class, "", "", start, end);
						if (zongPais != null) {
							for (int i = 0; i < zongPais.size(); i++) {
								ZongPai zongPai = zongPais.get(i);
								zongpais.add(zongPai);
							}
						}
						start += pagSize;
						count -= pagSize;
						logger.warn("[宗派改名->加载宗派] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { unitedServer.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
					}
				} catch (Exception e) {
					logger.error("[宗派异常] " + unitedServer.toString(), e);
					ExcepTionlogger.error("[宗派异常] " + unitedServer.toString(), e);
				}
			}
			HashMap<String, HashSet<ZongPai>> chongMingZongPais = new HashMap<String, HashSet<ZongPai>>();
			for (String serverName : allZongPais.keySet()) {
				HashSet<ZongPai> zongpaiSet = allZongPais.get(serverName);
				for (ZongPai zongpai : zongpaiSet) {
					for (String otherServerName : allZongPais.keySet()) {
						if (otherServerName.equals(serverName)) {
							continue;
						}
						HashSet<ZongPai> otherzongpaiSet = allZongPais.get(otherServerName);
						for (ZongPai otherZongPai : otherzongpaiSet) {
							if (otherZongPai.getZpname().equals(zongpai.getZpname())) {
								// 重名
								HashSet<ZongPai> chongming = chongMingZongPais.get(otherServerName);
								if (chongming == null) {
									chongming = new HashSet<ZongPai>();
									chongMingZongPais.put(otherServerName, chongming);
								}
								chongming.add(otherZongPai);
								logger.warn("[宗派重名] [{}] [{}] [{}] [{}]", new Object[] { otherServerName, otherZongPai.getLogString(), serverName, zongpai.getLogString() });
							}
						}
					}
				}
			}
			for (String serverName : chongMingZongPais.keySet()) {
				UnitedServer s = unitedServerMap.get(serverName);
				HashSet<ZongPai> zongpais = chongMingZongPais.get(serverName);
				if (s == null) {
					SimpleEntityManager<ZongPai> jiazuEntityMananger = mainServer.getFactory().getSimpleEntityManager(ZongPai.class);
					for (ZongPai j : zongpais) {
						j.setZpname(j.getZpname() + GaiMing_BiaoJi + serverName);
						logger.warn("[宗派改名] [{}] [{}]", new Object[] { serverName, j.getLogString() });
						jiazuEntityMananger.flush(j);
					}
				} else {
					SimpleEntityManager<ZongPai> entityManager = s.getFactory().getSimpleEntityManager(ZongPai.class);
					for (ZongPai j : zongpais) {
						j.setZpname(j.getZpname() + GaiMing_BiaoJi + serverName);
						logger.warn("[宗派改名] [{}] [{}]", new Object[] { serverName, j.getLogString() });
						entityManager.flush(j);
					}
				}
			}
		} catch (Exception e) {
			logger.error("修改宗派名字出错", e);
			ExcepTionlogger.error("修改宗派名字出错", e);
		}
	}

	/**
	 * 修改角色名字,并记录在dc中
	 * @throws Exception
	 */
	private void changePlayerName() throws Exception {
		long startTime = SystemTime.currentTimeMillis();
		Map<String, Map<String, LocalPlayer>> serverMap = new HashMap<String, Map<String, LocalPlayer>>();// <服务器,<角色名,数据>>
		for (UnitedServer server : allserverMap.values()) {
			long start = SystemTime.currentTimeMillis();
			logger.warn("[查询角色] [开始] [" + server.getServerName() + "]");
			Map<String, LocalPlayer> mainServerPlayers = getLocalPlayers(server);
			serverMap.put(server.getServerName(), mainServerPlayers);
			logger.warn("[查询角色] [结束] [" + server.getServerName() + "] [cost:" + (System.currentTimeMillis() - start) + "ms]");
		}
		// 得到了所有服务器的角色,找到想同名字的,修改
		Set<String> sameNames = new HashSet<String>();// 先找到重复的名字
		Set<String> notsameNames = new HashSet<String>();// 不同名的

		for (Iterator<String> serverNameItor = serverMap.keySet().iterator(); serverNameItor.hasNext();) {
			String serverName = serverNameItor.next();
			for (String playername : serverMap.get(serverName).keySet()) {
				if (notsameNames.contains(playername)) {
					sameNames.add(playername);
				} else {
					notsameNames.add(playername);
				}
			}
		}
		// 找到了所有重复的名字.现在要做的是查询出所有服务器的符合这个条件的角色,改名,存储,放入dc

		for (Iterator<String> serverItor = serverMap.keySet().iterator(); serverItor.hasNext();) {
			String serverName = serverItor.next();
			Set<Long> needChangeNameSet = new HashSet<Long>();
			Map<String, LocalPlayer> map = serverMap.get(serverName);
			if (map == null) {
				continue;
			}
			for (String sameName : sameNames) {// 找到在同名列表里的,修改一下,并且要存库
				LocalPlayer localPlayer = map.get(sameName);
				if (localPlayer != null) {
					needChangeNameSet.add(localPlayer.getId());
				}
			}
			logger.warn("[查询角色] [重名个数:" + sameNames.size() + "]");
			// 查询出了所有要改名的ID.load出player,然后改名,再放回去
			UnitedServer server = getUnitedServerByServerName(serverName);
			if (server == null) {
				throw new IllegalStateException("[异常] [不应该出现的错误] [服务器配置不存在:" + serverName + "]");
			}
			SimpleEntityManager<Player> manager = server.getFactory().getSimpleEntityManager(Player.class);
			for (Long playerId : needChangeNameSet) {
				try {
					Player player = manager.find(playerId);
					player.setName(player.getName() + GaiMing_BiaoJi + serverName);
					manager.flush(player);

					sameNamePlayerCache.put(player.getId(), player.getName());
					List<Long> changeList = null;
					if (sameNamePlayerCache.get(changeNameListKey) == null) {
						changeList = new ArrayList<Long>();
					} else {
						changeList = (List<Long>) sameNamePlayerCache.get(changeNameListKey);
					}
					changeList.add(playerId);
					sameNamePlayerCache.put(changeNameListKey, (Serializable) changeList);

					logger.error("[服务器角色修改名字] [成功] [服务器:" + serverName + "] [角色ID:" + playerId + "] [修改后:" + player.getName() + "]");
				} catch (Exception e) {
					logger.error("[服务器角色修改名字] [异常] [服务器:" + serverName + "] [角色ID:" + playerId + "]", e);
					ExcepTionlogger.error("[服务器角色修改名字] [异常] [服务器:" + serverName + "] [角色ID:" + playerId + "]", e);
				}
			}
		}
		logger.warn("[修改角色名] [完成] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
	}

	private Map<String, LocalPlayer> getLocalPlayers(UnitedServer unitedServer) throws Exception {
		logger.warn("[获取角色信息:" + unitedServer.toString() + "]");
		long startTime = System.currentTimeMillis();
		SimpleEntityManager<Player> manager = unitedServer.getFactory().getSimpleEntityManager(Player.class);
		Map<String, LocalPlayer> localPlayerMap = new HashMap<String, LocalPlayer>();
		long[] queryIds = manager.queryIds(Player.class, null);
		logger.warn(unitedServer.toString() + " [获取角色信息] [" + queryIds.length + "]");
		int onceQuery = 3000;

		for (int i = 0; i < queryIds.length; i += onceQuery) {
			logger.warn(unitedServer.toString() + " [获取角色信息] [" + queryIds.length + "] [index:" + i + "]");
			long now = System.currentTimeMillis();
			Set<Long> tempIds = new HashSet<Long>();
			for (int k = i; k < ((i + onceQuery > queryIds.length) ? queryIds.length : (i + onceQuery)); k++) {// TODO 越界
				tempIds.add(queryIds[k]);
			}
			logger.warn(unitedServer.toString() + " [获取角色信息] [" + queryIds.length + "] [index:" + i + "] [tempIds:" + tempIds.size() + "]");
			Long[] ids = tempIds.toArray(new Long[0]);
			long[] realIds = new long[ids.length];
			for (int m = 0; m < realIds.length; m++) {// 将Long 转成 long
				realIds[m] = ids[m];
			}
			List<LocalPlayer> localPlayerList = manager.queryFields(LocalPlayer.class, realIds);
			for (LocalPlayer lp : localPlayerList) {
				localPlayerMap.put(lp.getName(), lp);
			}
			logger.warn("[角色改名->加载LocalPlayer] [server:{}] [num:{}] [total:{}] [elap:{}ms] [totalCost:{}ms]", new Object[] { unitedServer.getServerName(), realIds.length, localPlayerMap.size(), (System.currentTimeMillis() - now), (System.currentTimeMillis() - startTime) });
		}
		return localPlayerMap;
	}

	public HashMap<String, HashSet<Long>> articleAllEntitys = new HashMap<String, HashSet<Long>>();
	public LinkedList<Long> mainAllArticleEntityIDs;

	private void getAllArticleEntitys() {
		try {
			articleAllEntitys.put(mainServer.getServerName(), new HashSet<Long>());
			for (String serverName : unitedServerMap.keySet()) {
				articleAllEntitys.put(serverName, new HashSet<Long>());
			}
			logger.warn("开始坐骑中articleEntity数据获取");
			getHorseArticleEntitys();
			logger.warn("结束坐骑中articleEntity数据获取");
			logger.warn("开始宠物articleEntity数据获取");
			getPetArticleEntitys();
			logger.warn("结束宠物articleEntity数据获取");
			logger.warn("开始家族仓库articleEntity数据获取");
			getJiaZuCangKuArticleEntitys();
			logger.warn("结束家族仓库articleEntity数据获取");
			logger.warn("开始千层塔 articleEntity数据获取");
			getQianCengTaArticleEntitys();
			logger.warn("结束千层塔 articleEntity数据获取");
			logger.warn("兽魂面板articleentity数据获取");
			getShouhunArticleEntity();
			logger.warn("兽魂面板articleentity数据获取结束");
			logger.warn("开始宝石匣子 articleEntity数据获取");
			getXiaZiArticleEntitys();
			logger.warn("结束宝石匣子 articleEntity数据获取");
			logger.warn("开始Player articleEntity数据获取");
			getPlayerArticleEntitys();
			logger.warn("结束Player articleEntity数据获取");
			logger.warn("开始装备中宝石器灵 articleEntity数据获取");
			mainServer.getArticleManager().releaseReference();
			int num = 0;
			for (String serverName : articleAllEntitys.keySet()) {
				int baoshi = 0;
				int qiling = 0;
				int jianchaNum = 0;
				HashSet<Long> ids = articleAllEntitys.get(serverName);
				ArrayList<Long> entityArrays = new ArrayList<Long>();
				entityArrays.addAll(ids);
				Collections.sort(entityArrays);
				logger.warn("server:" + serverName + "  ids num" + ids.size());
				SimpleEntityManager<ArticleEntity> manager = null;
				if (serverName.equals(mainServer.getServerName())) {
					manager = mainServer.getArticleManager();
				} else {
					manager = unitedServerMap.get(serverName).getArticleManager();
				}
				HashSet<Long> add = new HashSet<Long>();
				Iterator<Long> iIds = entityArrays.iterator();
				while (true) {
					int errnum = 0;
					try {
						ArrayList<Long> id50 = new ArrayList<Long>();
						int findNum = 2000;
						boolean isOver = false;
						for (;;) {
							try {
								//这里因为添加了合服的服务器 而合服的服务器中是没有数据的 所以需要预先加判断
								if(iIds.hasNext())
								{
									Long id = iIds.next();
									id50.add(id);
									findNum--;
									jianchaNum++;
									if (!iIds.hasNext()) {
										isOver = true;
										break;
									}
									if (findNum <= 0) {
										break;
									}
								}
								else
								{
									logger.warn("[获取物品] [集合数量为0] ["+serverName+"] ["+iIds.hasNext()+"]");
									isOver = true;
									break;
								}
							} catch (Exception e) {
								errnum++;
								if(errnum > 1000)
								{
									if(errnum > 50000)
									{
										isOver = true;
									}
									break;
								}
								logger.error("[获取物品] [不应该出现的错误]", e);
								ExcepTionlogger.error("[获取物品] [不应该出现的错误]", e);
							}
						}
						String sq = "";
						for (int i = 0; i < id50.size(); i++) {
							if (i == id50.size() - 1) {
								sq += "id=" + id50.get(i);
							} else {
								sq += "id=" + id50.get(i) + " or ";
							}
						}
						List<ArticleEntity> entitys = manager.query(ArticleEntity.class, sq, "", 1, id50.size() + 1);
						logger.warn("[id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "/" + entityArrays.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
						for (ArticleEntity entity : entitys) {
							if (entity instanceof EquipmentEntity) {
								EquipmentEntity eq = (EquipmentEntity) entity;
								for (int i = 0; i < eq.getInlayArticleIds().length; i++) {
									if (eq.getInlayArticleIds()[i] > 0) {
										add.add(eq.getInlayArticleIds()[i]);
										num++;
										baoshi++;
									}
								}
								for (int i = 0; i < eq.getInlayQiLingArticleIds().length; i++) {
									if (eq.getInlayQiLingArticleIds()[i] > 0) {
										add.add(eq.getInlayQiLingArticleIds()[i]);
										num++;
										qiling++;
									}
								}
							}
						}
						logger.warn("检查物品ID " + serverName + "  ids " + ids.size() + " 已检查" + jianchaNum + "  宝石" + baoshi + "  器灵" + qiling + ",isOver:" + isOver);
						if (isOver) {
							break;
						}
					} catch (Exception e) {
						logger.error("getAllArticleEntitys出错A:", e);
						ExcepTionlogger.error("getAllArticleEntitys出错A:", e);
						break;
					}
				}
				ids.addAll(add);
				manager.releaseReference();
				logger.warn(serverName + "装备中宝石器灵" + " 宝石=" + baoshi + " 器灵=" + qiling);
			}
			logger.warn("结束装备中宝石器灵 articleEntity数据获取" + " 总数:" + num);
		} catch (Exception e) {
			logger.error("getAllArticleEntitys出错:", e);
			ExcepTionlogger.error("getAllArticleEntitys出错:", e);
		}
	}

	private void getArticleEntitys() {
		try {
			{
				SimpleEntityManager<ArticleEntity> manager = mainServer.getArticleManager();
				if (manager instanceof SimpleEntityManagerMysqlImpl) {
					((SimpleEntityManagerMysqlImpl) manager).dropEntityIndex(ArticleEntity.class);
				}
				HashSet<Long> mainIds = articleAllEntitys.get(mainServer.getServerName());
				ArrayList<Long> mainListIds = new ArrayList<Long>(mainIds);
				Collections.sort(mainListIds);
				long now = System.currentTimeMillis();
				ArrayList<Long> fistIds = new ArrayList<Long>();
				long lastID = 0;
				for (int i = 0; i < mainListIds.size(); i++) {
					long iid = mainListIds.get(i);
					if (iid - lastID > 10000000000L) {
						fistIds.add(iid);
					}
					lastID = iid;
				}
				logger.warn("[查出的头ID是:] [" + fistIds + "] [花费:" + (System.currentTimeMillis() - now) + "ms]");
				long mainCount = manager.count();
				mainAllArticleEntityIDs = new LinkedList<Long>();
				// 先查出来所有的ID
				now = System.currentTimeMillis();
				if (false) {
					if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
						for (int i = 0; i < fistIds.size(); i++) {
							int pagNum = 0;
							long firstID = fistIds.get(i) / 10000000000L * 10000000000L;
							while (true) {
								try {
									int findNum = 10000;
									if (manager instanceof SimpleEntityManagerOracleImpl) {
										findNum = 10000;
									} else if (manager instanceof SimpleEntityManagerMysqlImpl) {
										findNum = 80000;
									}
									long beginID = firstID + pagNum * findNum;
									long endID = firstID + (pagNum + 1) * findNum;
									long[] ids = manager.queryIds(ArticleEntity.class, " id >= ? and id < ?", new Object[] { beginID, endID });
									for (long ii : ids) {
										mainAllArticleEntityIDs.add(ii);
									}
									logger.warn("主服查询出物品ID: 已查出:" + ids.length + " 总数:" + mainCount + "开始ID[" + beginID + "] 结束ID[" + endID + "]");
									pagNum++;
									if (pagNum >= 25000) {
										break;
									}
								} catch (Exception e) {
									logger.error("main查物品ID出错:", e);
									ExcepTionlogger.error("main查物品ID出错:", e);
								}
							}
						}
						logger.warn("主服查出来ArticleEntityID数目:" + mainAllArticleEntityIDs.size() + " 有用数目:" + mainIds.size() + " 花费:" + (System.currentTimeMillis() - now) + "ms");
						// 得到没用articleEntityID数目0
						try {
							now = System.currentTimeMillis();
							Iterator<Long> it = mainAllArticleEntityIDs.iterator();
							int removeNum = 0;
							int passNum = 0;
							for (; it.hasNext();) {
								long id = it.next();
								passNum++;
								if (mainIds.contains(id)) {
									it.remove();
									if (removeNum % 100 == 0) {
										logger.warn("移除不需要删除ID: 已遍历:" + passNum + " 移除:" + removeNum);
									}
								} else {
									removeNum++;
								}
							}
							logger.warn("从所有ID中删除有用的-需要删除的:" + mainAllArticleEntityIDs.size() + " 花费:" + (System.currentTimeMillis() - now) + "ms");
						} catch (Exception e) {
							logger.error("", e);
							ExcepTionlogger.error("", e);
						}
					}
				}
			}

			// 先去删除主服中的无用数据
			UnitedServerThread deleteArticleEntity = new UnitedServerThread("deleteArticleEntity", null, null);
			Thread thread_deleteArticleEntity = new Thread(deleteArticleEntity, "合服-deleteArticleEntity");
			thread_deleteArticleEntity.start();

			// 合并数据
			{
				// 合并物品
				{
					startTime = SystemTime.currentTimeMillis();
					List<ArticleEntity> noticeSimpleJpa = new ArrayList<ArticleEntity>();// 通知底层的数据
					// 合并 articleEntity
					Field versionField = getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
					Field idField = getFieldByAnnotation(ArticleEntity.class, SimpleId.class);
					if (versionField == null) {
						throw new IllegalStateException("ArticleEntity's versionField not found");
					}
					idField.setAccessible(true);
					versionField.setAccessible(true);
					SimpleEntityManager<ArticleEntity> main = mainServer.getArticleManager();
					for (String serverName : articleAllEntitys.keySet()) {
						if (serverName.equals(mainServer.getServerName())) {
							continue;
						}
						HashSet<Long> entityIds = articleAllEntitys.get(serverName);
						ArrayList<Long> entityArrays = new ArrayList<Long>();
						entityArrays.addAll(entityIds);
						int num = entityIds.size();

						Collections.sort(entityArrays);
						SimpleEntityManager<ArticleEntity> manager = unitedServerMap.get(serverName).getArticleManager();
						manager.setReadOnly(true);
						Iterator<Long> eIt = entityArrays.iterator();
						int daoruNum = 0;

						while (true) {
							{
								// 检测底层存储的
								Iterator<ArticleEntity> itor = noticeSimpleJpa.iterator();
								while (itor.hasNext()) {
									ArticleEntity articleEntity = itor.next();
									int version = versionField.getInt(articleEntity);
									if (version != 0) {
										long id = idField.getLong(articleEntity);
										itor.remove();
										if (UnitedServerManager.logger.isInfoEnabled()) {
											UnitedServerManager.logger.info("[移除已经存储的物品] [id:" + id + "]");
										}
									}
								}
								if (noticeSimpleJpa.size() > 20000) {
									Thread.sleep(10000);
									logger.warn("[存储物品] [底层未存储超过20000] [" + noticeSimpleJpa.size() + "] [暂停10秒]");
									continue;
								}
							}
							boolean isOver = false;
							ArrayList<Long> id50 = new ArrayList<Long>();
							int findNum = 2000;
							for (;;) {
								Long id = eIt.next();
								id50.add(id);
								findNum--;
								daoruNum++;
								if (!eIt.hasNext()) {
									isOver = true;
									break;
								}
								if (findNum <= 0) {
									break;
								}
							}

							String sq = "";
							for (int i = 0; i < id50.size(); i++) {
								if (i == id50.size() - 1) {
									sq += "id=" + id50.get(i);
								} else {
									sq += "id=" + id50.get(i) + " or ";
								}
							}
							List<ArticleEntity> entitys = manager.query(ArticleEntity.class, sq, "", 1, id50.size() + 1);// //TODO
							logger.warn("[id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
							for (ArticleEntity entity : entitys) {
								versionField.set(entity, 0);
								main.notifyNewObject(entity);
								long id = idField.getLong(entity);
								if (UnitedServerManager.logger.isInfoEnabled()) {
									UnitedServerManager.logger.info("[通知底层存储物品] [notifyNewObject] [id:" + id + "]");
								}
								noticeSimpleJpa.add(entity);
							}
							logger.warn("[导入articleEntity] [" + serverName + "] [总数:" + num + "] [已导入:" + daoruNum + "]");
							if (isOver) {
								break;
							}
						}
					}
					while (noticeSimpleJpa.size() > 0) {// 通知底层完成，但是还没存储完毕
						Thread.sleep(10000);
						Iterator<ArticleEntity> itor = noticeSimpleJpa.iterator();
						while (itor.hasNext()) {
							ArticleEntity articleEntity = itor.next();
							int version = versionField.getInt(articleEntity);
							if (version != 0) {
								long id = idField.getLong(articleEntity);
								itor.remove();
								if (UnitedServerManager.logger.isInfoEnabled()) {
									UnitedServerManager.logger.info("[全部通知底层完成] [移除已经存储的物品] [id:" + id + "]");
								}
							}
						}
					}
					logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [合并articleEntity] [结束] [total耗时:" + (SystemTime.currentTimeMillis() - startTime) + "]");
				}
			}

			while (true) {
				if (deleteArticleEntity.isRunning()) {
					Thread.sleep(10000);
				} else {
					break;
				}
			}
			String sNum = "";
			for (String skey : articleAllEntitys.keySet()) {
				sNum = sNum + "[skey:" + articleAllEntitys.get(skey).size() + "] ";
			}
			logger.warn("--------[物品完全合并] [物品总数:" + mainServer.getArticleManager().count() + "] sNum");
			articleAllEntitys.clear();
		} catch (Exception e) {
			logger.error("取entity对象", e);
			ExcepTionlogger.error("取entity对象", e);
		}
	}

	private void deleteArticleEntity() {
		try {
			if (true) {
				return;
			}
			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return;
			}
			startTime = SystemTime.currentTimeMillis();
			SimpleEntityManager<ArticleEntity> manager = mainServer.getArticleManager();

			// mainAllArticleEntityIDs.removeAll(mainIds);
			// Collections.sort(mainAllArticleEntityIDs);
			int deletePagNum = 0;
			int rDeleteNum = 0;
			long now = System.currentTimeMillis();
			ArrayList<Long> arg1 = new ArrayList<Long>();
			int mSize = mainAllArticleEntityIDs.size();
			Iterator<Long> it = mainAllArticleEntityIDs.iterator();
			logger.warn("开始删除ArticleEntity: 总数:" + manager.count() + " 需删除:" + mainAllArticleEntityIDs.size() + " 不需删除:" + articleAllEntitys.get(mainServer.getServerName()).size());
			while (true) {
				try {
					long sta1 = System.currentTimeMillis();
					int deleteNumn = 10000;
					int relNum = deleteNumn;
					if (deleteNumn * 1L * (deletePagNum + 1) > mSize) {
						relNum = (int) (mSize - (deleteNumn * 1L * (deletePagNum)));
					}
					for (int i = 0; i < relNum; i++) {
						if (it.hasNext()) {
							Long curId = it.next();
							arg1.add(curId);
						}
					}
					long pass1 = System.currentTimeMillis() - sta1;
					sta1 = System.currentTimeMillis();
					rDeleteNum += relNum;
					deletePagNum++;
					long beginId = -1l;
					long endId = -1l;
					if (arg1.size() > 0) {
						beginId = arg1.get(0);
						endId = arg1.get(arg1.size() - 1);
					}
					manager.batch_delete_by_Ids(ArticleEntity.class, arg1);
					arg1.clear();
					long pass2 = System.currentTimeMillis() - sta1;

					logger.warn("删除1111ArticleEntity 总数:" + mSize + " 开始ID：" + beginId + " 结束ID:" + endId + " 以删除:" + rDeleteNum + "- pass1:" + pass1 + "ms  pass2:" + pass2);
					if (deletePagNum * 1L * deleteNumn > mSize) {
						break;
					}
				} catch (Exception e) {
					logger.error("删除出错:", e);
					ExcepTionlogger.error("删除出错:", e);
				}
			}
			mainAllArticleEntityIDs.clear();
			logger.warn("删除完成， 花费:" + (System.currentTimeMillis() - now) + "ms");
		} catch (Exception e) {
			logger.error("", e);
			ExcepTionlogger.error("", e);
		}
	}

	/**
	 * 取所有 坐骑装备的 entityID；
	 * @throws Exception
	 */
	private void getHorseArticleEntitys() throws Exception {
		int num = 0;
		num += getHorseArticleEntitysTOServer(mainServer);
		logger.warn("[主服从坐骑中取出entityID] [total:{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getHorseArticleEntitysTOServer(server);
		}
		logger.warn("[从坐骑中取出entityID] [total:{}]", new Object[] { num });
	}

	private int getHorseArticleEntitysTOServer(UnitedServer server) throws Exception {
		SimpleEntityManager<Horse> manager = server.getFactory().getSimpleEntityManager(Horse.class);
		int num = 0;
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 5000;

		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Horse> horses = manager.query(Horse.class, "", "", start, end);
			if (horses != null) {
				for (Horse h : horses) {
					long[] horseEquIDs = h.getEquipmentColumn().getEquipmentIds();
					for (long i : horseEquIDs) {
						if (i > 0) {
							ids.add(i);
							num++;
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从坐骑中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		return num;
	}

	private void getPetArticleEntitys() throws Exception {
		int num = 0;
		num += getPetArticleEntitysToServer(mainServer);
		logger.warn("[主服从宠物中取出entityID] [total:{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getPetArticleEntitysToServer(server);
		}
		logger.warn("[从宠物中取出entityID] [total:{}]", new Object[] { num });
	}

	private int getPetArticleEntitysToServer(UnitedServer server) throws Exception {
		SimpleEntityManager<Pet> manager = server.getFactory().getSimpleEntityManager(Pet.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		int num = 0;
		// long count = manager.count();
		long count = manager.count(Pet.class, " delete='F' or (delete='T' and rarity = 2)");
		long start = 1;
		long end = 1;
		long pagSize = 10000;

		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<PetProp> queryFields = manager.queryFields(PetProp.class, manager.queryIds(Pet.class, " delete='F' or (delete='T' and rarity = 2)", "id", start, end));
			for (PetProp pp : queryFields) {
				if (pp.getPetPropsId() > 0) {
					ids.add(pp.getPetPropsId());
					num++;
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从宠物中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		return num;
	}

	public void getJiaZuCangKuArticleEntitys() throws Exception {
		int num = 0;
		num += getJiaZuCangKuArticleEntitysToServer(mainServer);
		logger.warn("[从主服家族仓库中取出entityID] [{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getJiaZuCangKuArticleEntitysToServer(server);
		}
		logger.warn("[从家族仓库中取出entityID] [{}]", new Object[] { num });
	}

	public int getJiaZuCangKuArticleEntitysToServer(UnitedServer server) throws Exception {
		SimpleEntityManager<Jiazu> manager = server.getFactory().getSimpleEntityManager(Jiazu.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		int num = 0;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Jiazu> jiazus = manager.query(Jiazu.class, "", "", start, end);
			if (jiazus != null) {
				for (Jiazu j : jiazus) {
					for (int i = 0; i < j.getWareHouse().size(); i++) {
						if (j.getWareHouse().get(i).getEntityId() > 0) {
							ids.add(j.getWareHouse().get(i).getEntityId());
							num++;
							entityNum++;
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从家族仓库中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从" + server.getServerName() + "中取出:" + entityNum);
		return num;
	}
	
	

	public void getQianCengTaArticleEntitys() throws Exception {
		int num = 0;
		num += getQianCengTaArticleEntitysToServer(mainServer);
		logger.warn("[主服从千层塔奖励中取出entityID] [{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getQianCengTaArticleEntitysToServer(server);
		}
		logger.warn("[从千层塔奖励中取出entityID] [{}]", new Object[] { num });
	}

	public int getQianCengTaArticleEntitysToServer(UnitedServer server) throws Exception {
		SimpleEntityManager<QianCengTa_Ta> manager = server.getFactory().getSimpleEntityManager(QianCengTa_Ta.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		int num = 0;
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<QianCengTa_Ta> tas = manager.query(QianCengTa_Ta.class, "", "", start, end);
			if (tas != null) {
				for (QianCengTa_Ta q : tas) {
					for (int i = 0; i < q.getDaoList().size(); i++) {
						QianCengTa_Dao dao = q.getDaoList().get(i);
						for (int j = 0; j < dao.getCengList().size(); j++) {
							QianCengTa_Ceng ceng = dao.getCengList().get(j);
							for (int k = 0; k < ceng.getRewards().size(); k++) {
								if (ceng.getRewards().get(k).getEntityId() > 0) {
									ids.add(ceng.getRewards().get(k).getEntityId());
									num++;
									entityNum++;
								}
							}
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从千层塔奖励中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从" + server.getServerName() + "中取出:" + entityNum);
		return num;
	}

	public void getPlayerArticleEntitys() throws Exception {
		int num = 0;
		num += getPlayerArticleEntitysToServer(mainServer);
		logger.warn("[从player中取出entityID] [{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getPlayerArticleEntitysToServer(server);
		}
		logger.warn("[从player中取出entityID] [{}]", new Object[] { num });
	}

	public int getPlayerArticleEntitysToServer(UnitedServer server) throws Exception {
		int num = 0;
		SimpleEntityManager<Player> manager = server.getFactory().getSimpleEntityManager(Player.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 2000;
		int beibao = 0; // 背包物品数
		int cangku = 0; // 仓库物品数
		int fangbaobao = 0; // 防暴包物品数+防暴包本身
		int zhuangbei = 0; // 装备数目，包括元神
		int qiling = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<Player> players = manager.query(Player.class, "", "", start, end);
			if (players != null) {
				for (Player p : players) {
					try
					{
						if (needRemovePlayer(p, now)) {
							continue;
						}
						int pFbb = 0;
						int pBb = 0;
						int pCk = 0;
						int pZb = 0;
						int pQl = 0;
						// 防暴包
						if (p.getKnapsack_fangBao_Id() > 0) {
							ids.add(p.getKnapsack_fangBao_Id());
							num++;
							fangbaobao++;
						}
						if (p.getKnapsack_fangbao() != null) {
							for (int i = 0; i < p.getKnapsack_fangbao().size(); i++) {
								try {
									if (p.getKnapsack_fangbao().getCell(i) != null) {
										if (p.getKnapsack_fangbao().getCell(i).getEntityId() > 0) {
											ids.add(p.getKnapsack_fangbao().getCell(i).getEntityId());
											num++;
											fangbaobao++;
											pFbb++;
										}
									} else {
										logger.warn("[从玩家身上取出物品] [错误] [cell为null] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]");
									}
								} catch (Exception e) {
									logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
									ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
								}
							}
						}
						if(p.getKnapsack_common() != null)
						{
							// 背包
							for (int i = 0; i < p.getKnapsack_common().size(); i++) {
								try {
									if (p.getKnapsack_common().getCell(i) != null) {
										if (p.getKnapsack_common().getCell(i).getEntityId() > 0) {
											ids.add(p.getKnapsack_common().getCell(i).getEntityId());
											num++;
											beibao++;
											pBb++;
										}
									} else {
										logger.warn("[从玩家身上取出物品] [错误] [cell为null] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]");
									}
								} catch (Exception e) {
									logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
									ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
								}
							}
						}
						if(p.getKnapsacks_cangku() != null)
						{
							// 仓库
							for (int i = 0; i < p.getKnapsacks_cangku().size(); i++) {
								try {
									if (p.getKnapsacks_cangku().getCell(i) != null) {
										if (p.getKnapsacks_cangku().getCell(i).getEntityId() > 0) {
											ids.add(p.getKnapsacks_cangku().getCell(i).getEntityId());
											num++;
											cangku++;
											pCk++;
										}
									} else {
										logger.warn("[从玩家身上取出物品] [错误] [cell为null] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]");
									}
								} catch (Exception e) {
									logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
									ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
								}
							}
						}
						if(p.getSouls() != null)
						{
							// 装备
							for (int i = 0; i < p.getSouls().length; i++) {
								try {
									Soul soul = p.getSouls()[i];
									if (soul == null || soul.getEc() == null) {
										continue;
									}
									for (int j = 0; j < soul.getEc().getEquipmentIds().length; j++) {
										if (soul.getEc().getEquipmentIds()[j] > 0) {
											ids.add(soul.getEc().getEquipmentIds()[j]);
											num++;
											zhuangbei++;
											pZb++;
										}
									}
								} catch (Exception e) {
									logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
									ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
								}
							}
						}
						if(p.getKnapsacks_QiLing() != null)
						{
							// 器灵
							for (int i = 0; i < p.getKnapsacks_QiLing().size(); i++) {
								try {
									if (p.getKnapsacks_QiLing().getCell(i) != null) {
										if (p.getKnapsacks_QiLing().getCell(i).getEntityId() > 0) {
											ids.add(p.getKnapsacks_QiLing().getCell(i).getEntityId());
											num++;
											qiling++;
											pQl++;
										}
									} else {
										logger.warn("[从玩家身上取出物品] [错误] [cell为null] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]");
									}
								} catch (Exception e) {
									logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
									ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
								}
							}
						}
						{
							if (p.getShouhunKnap() != null) {
								//兽魂仓库
								for (int i=0; i<p.getShouhunKnap().length; i++) {
									if (p.getShouhunKnap()[i] > 0) {
										ids.add(p.getShouhunKnap()[i]);
									}
								}
							}
						}
						logger.warn("从玩家身上取出物品: [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "]");
					}
					catch(Exception e)
					{
						logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "]", e);
						ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "]", e);
					}
					
				}
				
				
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从player中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, (System.currentTimeMillis() - now) });
		}
		logger.warn("从" + server.getServerName() + "中取出: 背包=" + beibao + " 仓库=" + cangku + " 防暴=" + fangbaobao + " 装备=" + zhuangbei + " 器灵=" + qiling);
		return num;
	}

	/**
	 * 清除数据
	 * @param <T>
	 * @param clazz
	 * @throws Exception
	 */
	private <T> void clear(Class<T> clazz) throws Exception {
		long startTime = SystemTime.currentTimeMillis();
		logger.warn("[移除数据] [开始] [" + clazz.getSimpleName() + "]");
		SimpleEntityManager<T> manager = mainServer.getFactory().getSimpleEntityManager(clazz);
		int onceQuery = 10000;
		long totalNum = manager.count();
		// 这里应该不会有特别大的数据量10000g个应该够了
		for (int i = 0; i < totalNum; i += onceQuery) {
			List<T> list = manager.query(clazz, "", "", i + 1, i + 1 + onceQuery);
			for (T t : list) {
				manager.remove(t);
			}
		}
		logger.warn("[移除数据] [结束] [" + clazz.getSimpleName() + "] [totalNum:" + totalNum + "] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
	}

	/**
	 * 合并数据
	 * @param clazz
	 */
	private <T> void merge(Class<T> clazz) {
		Queue<T> queue = new LinkedList<T>();
		long start = System.currentTimeMillis();
		logger.warn("[$$$$$$$$$$$$$$$$$$$$] [合并数据] [开始] [" + clazz.getSimpleName() + "]");

		DataCollect<T> dataCollect = new DataCollect<T>(queue, unitedServerMap, clazz);
		DataUnit<T> dataUnit = new DataUnit<T>(queue, getMainServer(), clazz, dataCollect);

		Thread collectThread = new Thread(dataCollect, "collectThread-" + clazz.getSimpleName());
		Thread unitThread = new Thread(dataUnit, "unitThread-" + clazz.getSimpleName());
		collectThread.start();
		unitThread.start();
		int minute = 0;

		dataCollectMap.put(clazz, dataCollect);
		dataUnitMap.put(clazz, dataUnit);

		while (dataCollect.isRunning() || dataUnit.isRunning()) {
			try {
				Thread.sleep(60000L);
				minute++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.warn("[合并数据] [" + clazz.getSimpleName() + "] [合并中] [已耗时:" + minute + "分钟]");
		}
		logger.warn("[合并数据] [结束] [" + clazz.getSimpleName() + "] [" + (++uniteClassesNum) + "/" + uniteClasses.size() + "] [耗时:" + TimeTool.instance.getShowTime(System.currentTimeMillis() - start) + "]");
		if (uniteClassesNum >= uniteClasses.size()) {
			logger.error("###########################################################################################################");
			logger.error("############################################合服结束(" + (System.currentTimeMillis() - startTime) + ")ms)###########################################");
			logger.error("###########################################################################################################");
		}
	}

	/**
	 * 合服,调用此方法 <li>配置服务器</li> <li>更改家族名字</li> <li>更改宗派名字</li> <li>合并DB数据</li> <li>移除DB数据</li>
	 * @param servers
	 */
	public void uniteServerBegin(UnitedServer[] servers) throws Exception {
		logger.error("###########################################################################################################");
		logger.error("##################################################合服开始#################################################");
		logger.error("###########################################################################################################");
		uniteClassesNum = 0;
		unitedServerStartTime = System.currentTimeMillis();

		boolean configSucc = config(servers);
		if (configSucc) {
			for (UnitedServer us : servers) {
				logger.warn("[服务器配置:" + us.toString() + "]");
			}
			
			
			long startTime = SystemTime.currentTimeMillis();
			{
				// 干掉所有主服务器要合并表的索引
				for (Class<?> clazz : uniteClasses) {
					long dropIndexStartTime = System.currentTimeMillis();
					SimpleEntityManager<?> simpleEntityManager = mainServer.getFactory().getSimpleEntityManager(clazz);
					simpleEntityManager.dropEntityIndex(clazz);
					// 设置一次自动存储的数量
					simpleEntityManager.setBatchSaveOrUpdateSize(1000);
					logger.warn("[移除表的索引:" + clazz.getSimpleName() + "] [耗时:" + (System.currentTimeMillis() - dropIndexStartTime) + "ms]");
				}
			}

			{
				// 设置非主服务器entityreadonly
				for (Class<?> clazz : uniteClasses) {
					for (UnitedServer unitedServer : unitedServerMap.values()) {
						SimpleEntityManager<?> entityManager = unitedServer.getFactory().getSimpleEntityManager(clazz);
						entityManager.setReadOnly(true);
					}
				}
			}
			{
				// 启动释放弱引用
				UnitedServerThread releaseReference = new UnitedServerThread("releaseReference", null, null);
				Thread thread_releaseReference = new Thread(releaseReference, "合服-releaseReference");
				thread_releaseReference.start();
			}
			if (unitedChanagerName) {
				UnitedServerThread changeJiazuName = new UnitedServerThread("changeJiazuName", null, null);
				Thread thread_changeJiazuName = new Thread(changeJiazuName, "合服-changeJiazuName");
				thread_changeJiazuName.start();

				UnitedServerThread changeZongpaiName = new UnitedServerThread("changeZongpaiName", null, null);
				Thread thread_changeZongpaiName = new Thread(changeZongpaiName, "合服-changeZongpaiName");
				thread_changeZongpaiName.start();

				UnitedServerThread changePlayerName = new UnitedServerThread("changePlayerName", null, null);
				Thread thread_changePlayerName = new Thread(changePlayerName, "合服-changePlayerName");
				thread_changePlayerName.start();

				while (changeJiazuName.isRunning() || changeZongpaiName.isRunning() || changePlayerName.isRunning()) {
					Thread.sleep(20000L);
					logger.error("[合服] [修改家族名字进行中:" + changeJiazuName.isRunning() + "] [宗派改名进行中:" + changeZongpaiName.isRunning() + "] [角色改名进行中:" + changePlayerName.isRunning() + "] [cost:" + (System.currentTimeMillis() - startTime) + "] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
				}
			}

			UnitedServerThread getAllArticleEntitys = new UnitedServerThread("getAllArticleEntitys", null, null);
			Thread thread_getAllArticleEntitys = new Thread(getAllArticleEntitys, "合服-getAllArticleEntitys");
			thread_getAllArticleEntitys.start();
			while (getAllArticleEntitys.isRunning()) {
				Thread.sleep(10000L);
				logger.error("合服 查ArticleEntityID，检查有用ArticleEntitys中:" + getAllArticleEntitys.isRunning() + "");
			}

			logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [删除数据] [开始]");
			{
				if (removeData) {
					for (Class<?> clazz : clearClasses) {
						UnitedServerThread serverThread = new UnitedServerThread("clear", new Class<?>[] { Class.class }, new Object[] { clazz });
						Thread thread = new Thread(serverThread, "删除数据-" + clazz.getSimpleName());
						thread.start();
					}
				}
			}
			logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [删除数据] [结束] [总耗时:" + UnitedServerManager.getTotolCost() + "]");

			logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [合并数据] [开始]");
			{
				if (unitedData) {
					// 合并数据
					List<Class<?>> leftUniteClasses = new ArrayList<Class<?>>();// 剩余没有执行过合并的classes
					leftUniteClasses.addAll(uniteClasses);

					// 当有剩余的未合并的class,并且当前同时执行合并的线程小于sameTimeRunUnitedClassNum,则分配一个新的线程工作
					while (leftUniteClasses.size() > 0) {
						if (currentUnitedThreads.size() < sameTimeRunUnitedClassNum) {
							Class<?> clazz = leftUniteClasses.remove(0);
							try {
								if (clazz.equals(ArticleEntity.class)) {
									UnitedServerThread getArticleEntitys = new UnitedServerThread("getArticleEntitys", null, null);
									Thread thread_getArticleEntitys = new Thread(getArticleEntitys, "合服-getArticleEntitys");
									thread_getArticleEntitys.start();
									currentUnitedThreads.add(getArticleEntitys);
								} else if (!unitedOnlyArticleEntity) {
									UnitedServerThread serverThread = new UnitedServerThread("merge", new Class<?>[] { Class.class }, new Object[] { clazz });
									Thread thread = new Thread(serverThread, "合并数据-" + clazz.getSimpleName());
									thread.start();
									currentUnitedThreads.add(serverThread);
								}
							} catch (Exception e) {
								logger.error("合并数据出错了:", e);
								ExcepTionlogger.error("合并数据出错了:", e);
							}
						}
						logger.warn("[数据合并中] [暂停20秒] [剩余class" + leftUniteClasses.size() + "] [正在执行线程数:" + currentUnitedThreads.size() + "] [正在执行的class] [" + getCurrentRunThreadInfo() + "] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
						Thread.sleep(20000L);
						removeDoneThread();
					}
					while (!allThreadOver(currentUnitedThreads)) {
						removeDoneThread(); // 在此线程中清理应该删除 但是未删除的已经运行完毕的线程
						logger.warn("[最后的表合并中] [每分钟检查一次] [" + getCurrentRunThreadInfo() + "] [总耗时:" + UnitedServerManager.getTotolCost() + "]");
						Thread.sleep(60000);
					}

					logger.warn("===========================================================================================");
					logger.warn("=========================================数据合并完毕=======================================");
					logger.warn("===========================================================================================");
				}
			}
			logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [合并数据] [结束] [total耗时:" + (SystemTime.currentTimeMillis() - startTime) + "]");

			logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [结束] [total耗时:" + (SystemTime.currentTimeMillis() - startTime) + "]");
		} else {
			throw new IllegalStateException("合服配置数据失败");
		}
	}
	


	public void releaseReference() {
		while (releaseReference) {
			long loopStart = System.currentTimeMillis();
			for (UnitedServer us : allserverMap.values()) {
				if (us != null) {
					for (Class<?> clazz : uniteClasses) {
						us.getFactory().getSimpleEntityManager(clazz).releaseReference();
					}
				}
			}
			try {
				logger.error("[调用底层释放弱引用] [2秒一次] [耗时:" + TimeTool.instance.getShowTime(System.currentTimeMillis() - loopStart) + "]");
				Thread.sleep(20000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void removeDoneThread() {
		Iterator<UnitedServerThread> itor = currentUnitedThreads.iterator();
		while (itor.hasNext()) {
			UnitedServerThread ut = itor.next();
			if (!ut.isRunning()) {
				logger.warn("[移除进程] [成功] [" + ut.getMethodName() + "]");
				itor.remove();
			}
		}
	}

	public String getCurrentRunThreadInfo() {
		StringBuffer sbf = new StringBuffer();
		try {
			for (UnitedServerThread ut : currentUnitedThreads) {
				if (ut == null) {
					continue;
				}
				String name = "";
				if (ut.getArgs() == null) {
					name = "ArticleEntity";
				} else {
					name = ut.getArgs()[0].toString();
				}
				sbf.append("[");
				sbf.append(name);
				sbf.append(",已执行:").append(TimeTool.instance.getShowTime(System.currentTimeMillis() - ut.getStartTime()));
				sbf.append("] ");

			}
		} catch (Exception e) {
			logger.error("getCurrentRunThreadInfo出错:", e);
			ExcepTionlogger.error("getCurrentRunThreadInfo出错:", e);
		}
		return sbf.toString();
	}

	public boolean allThreadOver(List<UnitedServerThread> list) {
		for (UnitedServerThread serverThread : list) {
			if (serverThread.isRunning()) {
				return false;
			}
		}
		return true;
	}

	public static boolean needRemovePlayer(Player player, long now) {
		return false;
	}

	/**
	 * 由于玩家改名,需要做的一些操作
	 * 1.仙府名字修改
	 * 2.称号修改
	 * 3.修改家族中的成员名字
	 * 
	 * @param player
	 * @param oldName
	 */
	public void notifyPlayerChanageName(Player player, String oldName) {
		noticePlayerChangeName(player.getId());
		((GamePlayerManager) GamePlayerManager.getInstance()).updateCacheForChangeName(player, oldName);
		{
			long startTime = SystemTime.currentTimeMillis();
			// 仙府.从数据库中查出来,修改.再flush
			if (player.getCaveId() > 0) {
				try {
					Cave cave = FaeryManager.caveEm.find(player.getCaveId());
					if (cave != null) {
						cave.setOwnerName(player.getName());
						FaeryManager.caveEm.flush(cave);
						logger.warn(player.getLogString() + " [修改仙府名字] [完毕] [caveId:" + player.getCaveId() + "] [cost:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
					}
					cave.onLoadInitNpc();

				} catch (Exception e) {
					logger.error(player.getLogString() + " [修改仙府名字] [异常] [caveId:" + player.getCaveId() + "]", e);
					ExcepTionlogger.error(player.getLogString() + " [修改仙府名字] [异常] [caveId:" + player.getCaveId() + "]", e);
				}
			} else {
				logger.warn(player.getLogString() + " [修改仙府名字] [没有仙府] [caveId:" + player.getCaveId() + "]");
			}
		}
		{
			// 家族，重新init成员信息
			long jiazuId = player.getJiazuId();
			if (jiazuId > 0) {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
				if (jiazu != null) {
					jiazu.initMember4Client();
				}
			}
		}
		{
			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			// 称号
			// 1 师徒 - 改徒弟的
			// 2 婚姻 - 改对方的
			if (relation != null) {
				MasterPrentice masterPrentice = relation.getMasterPrentice();
				if (masterPrentice != null) {
					// 师傅不会有关于徒弟的称号，徒弟会有关于师傅的
					List<Long> ids = masterPrentice.getPrentices();
					for (int i = 0; i < ids.size(); i++) {
						Player pp = null;
						try {
							pp = PlayerManager.getInstance().getPlayer(ids.get(i));
						} catch (Exception e) {
							logger.error("徒弟角色不存在", e);
							ExcepTionlogger.error("徒弟角色不存在", e);
						}
						if (pp != null) {
							PlayerTitlesManager.getInstance().addSpecialTitle(pp, MasterPrenticeManager.师徒称号, player.getName() + 的徒弟, true);
						}
					}
				}
			}

			if (relation != null && relation.getMarriageId() > 0) {
				// 有配偶
				MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(relation.getMarriageId());
				if (info != null) {
					long pID = 0;
					if (info.getHoldA() == player.getId()) {
						pID = info.getHoldB();
					} else if (info.getHoldB() == player.getId()) {
						pID = info.getHoldA();
					}
					if (pID > 0) {
						Player p = null;
						try {
							p = PlayerManager.getInstance().getPlayer(pID);
						} catch (Exception e) {
							logger.error("查找结婚另外一个出错", e);
							ExcepTionlogger.error("查找结婚另外一个出错", e);
						}
						if (p != null) {
							p.setSpouse(player.getName());
							PlayerTitlesManager.getInstance().addSpecialTitle(p, Translate.text_marriage_结婚, player.getName() + Translate.text_marriage_105, true);
							PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, p.getName() + Translate.text_marriage_105, true);
						}
					}
				}
			}
		}

		Hashtable<Long, Auction> auctionMap = AuctionManager.getInstance().getAuctionMap();
		for (Long key : auctionMap.keySet()) {
			Auction auction = auctionMap.get(key);
			if (auction.getSellerId() == player.getId()) {
				auction.setSellName(player.getName());
				auction.postLoad();
			}
		}
		List<RequestBuy> requestBuyList = RequestBuyManager.getInstance().getPlayerRuleMap().get(player.getId());
		if (requestBuyList != null && requestBuyList.size() > 0) {
			for (RequestBuy buy : requestBuyList) {
				buy.postLoad();
			}
		}
	}

	public DiskCache getSameNamePlayerCache() {
		return sameNamePlayerCache;
	}

	public void setSameNamePlayerCache(DiskCache sameNamePlayerCache) {
		this.sameNamePlayerCache = sameNamePlayerCache;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public boolean needChangeName(long playerId) {
		String playerName = (String) sameNamePlayerCache.get(playerId);
		if (playerName == null) {
			return false;
		}
		return true;
	}

	public void noticePlayerChangeName(long playerId) {
		sameNamePlayerCache.remove(playerId);
	}

	public static Field getFieldByAnnotation(Class<?> clazz, Class annotation) {
		Field found = null;
		for (Field f : clazz.getDeclaredFields()) {
			Annotation ann = f.getAnnotation(annotation);
			if (ann == null) {
				continue;
			} else {
				found = f;
				break;
			}
		}
		if (found == null) {
			if (clazz.getSuperclass().equals(Object.class)) {
				return null;
			}
			return getFieldByAnnotation(clazz.getSuperclass(), annotation);
		}
		return found;
	}

	/** 合服关闭功能、合服活动 */
	private String filePath;
	private List<UnitServerActivity> unitServerActivities = new ArrayList<UnitServerActivity>();
	private List<String> server4Rename = new LinkedList<String>();// 显示家族宗派改名的服务器

	// 老玩家召回
	private String playerComebackKey;
	private long playerBackId;

	// 崛起奖励
	private List<UnitServerRiseActivity> riseActivities = new ArrayList<UnitServerRiseActivity>();

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<UnitServerActivity> getUnitServerActivities() {
		return unitServerActivities;
	}

	public void setUnitServerActivities(List<UnitServerActivity> unitServerActivities) {
		this.unitServerActivities = unitServerActivities;
	}

	public String getPlayerComebackKey() {
		return playerComebackKey;
	}

	public void setPlayerComebackKey(String playerComebackKey) {
		this.playerComebackKey = playerComebackKey;
	}

	public long getPlayerBackId() {
		return playerBackId;
	}

	public void setPlayerBackId(long playerBackId) {
		this.playerBackId = playerBackId;
	}

	public List<String> getServer4Rename() {
		return server4Rename;
	}

	public void setServer4Rename(List<String> server4Rename) {
		this.server4Rename = server4Rename;
	}

	public List<UnitServerRiseActivity> getRiseActivities() {
		return riseActivities;
	}

	public void setRiseActivities(List<UnitServerRiseActivity> riseActivities) {
		this.riseActivities = riseActivities;
	}

	public void loadFile() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			sheet = hssfWorkbook.getSheetAt(0);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			int rows0 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows0; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					ActivitySubSystem.logger.warn("[unitServer.xls] [sheet0] [第" + i + "行] [共" + rows0 + "行]");
					String platformName = row.getCell(index++).getStringCellValue();
					String serverNames = row.getCell(index++).getStringCellValue();
					String unitedTime = row.getCell(index++).getStringCellValue();
					String activityStartTime = row.getCell(index++).getStringCellValue();
					String activityEndTime = row.getCell(index++).getStringCellValue();
					int closeDay = (int) row.getCell(index++).getNumericCellValue();
					String[] serverNameArr = serverNames.split(",");
					Set<String> serverNameSet = new HashSet<String>();
					for (String name : serverNameArr) {
						serverNameSet.add(name.trim());
					}
					Platform platform = PlatformManager.getInstance().getPlatformByENName(platformName);
					ServerCfg sc1 = new ServerCfg(TimeTool.formatter.varChar19.parse(unitedTime), serverNameSet, platform, activityStartTime, activityEndTime, new boolean[] { true, true });
					if (PlatformManager.getInstance().isPlatformOf(platform) && serverNameSet.contains(GameConstants.getInstance().getServerName())) {
						// 如果服务器是对应的，关功能的配置用表里的天数
						Function.拍卖.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.求购.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.邮件.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.武圣.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.城战.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.矿战.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.国战.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.仙府.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.五方.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.镖局.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.结婚.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.仙尊.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.仙灵大会.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.创建角色.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
						Function.武神之巅.setCloseBeforeUnited(DAY.getTimeMillis() * closeDay);
					}
					UnitServerFunctionManager.cfgs.add(sc1);
				}
			}

			sheet = hssfWorkbook.getSheetAt(1);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			int rows1 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows1; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					ActivitySubSystem.logger.warn("[unitServer.xls] [sheet1] [第" + i + "行] [共" + rows1 + "行]");
					String serverName = row.getCell(0).getStringCellValue();
					server4Rename.add(serverName);
				}
			}

			// 合服活动
			sheet = hssfWorkbook.getSheetAt(2);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			int rows2 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows2; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					ActivitySubSystem.logger.warn("[unitServer.xls] [sheet2] [第" + i + "行] [共" + rows2 + "行]");
					String activityName = row.getCell(index++).getStringCellValue();
					String platforms = row.getCell(index++).getStringCellValue();
					HSSFCell cell = row.getCell(index++);
					String openServers = "";
					if (cell != null) {
						openServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServers = "";
					if (cell != null) {
						limitServers = cell.getStringCellValue();
					}
					String startTimeStr = row.getCell(index++).getStringCellValue();
					String endTimeStr = row.getCell(index++).getStringCellValue();
					UnitServerActivity usa = new UnitServerActivity(startTimeStr, endTimeStr, platforms, openServers, limitServers);
					usa.setOtherVar(activityName);
					unitServerActivities.add(usa);
					AllActivityManager.instance.add2AllActMap(AllActivityManager.unitServerAct, usa);
				}
			}

			// 老玩家召回
			sheet = hssfWorkbook.getSheetAt(3);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			HSSFRow row3 = sheet.getRow(1);
			playerBackId = (long) row3.getCell(0).getNumericCellValue();
			playerComebackKey = row3.getCell(1).getStringCellValue();

			// 崛起奖励
			sheet = hssfWorkbook.getSheetAt(4);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			int rows4 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows4; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					ActivitySubSystem.logger.warn("[unitServer.xls] [sheet4] [第" + i + "行] [共" + rows4 + "行]");
					String id = row.getCell(index++).getStringCellValue();
					String platform = row.getCell(index++).getStringCellValue();
					String serverName = row.getCell(index++).getStringCellValue();
					byte countryId = (byte) row.getCell(index++).getNumericCellValue();
					String startTime = row.getCell(index++).getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					int levelLimit = (int) row.getCell(index++).getNumericCellValue();
					String[] awardCNNames = row.getCell(index++).getStringCellValue().split(",");
					String[] awardColors = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					String[] awardNums = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					String[] awardBinds = StringTool.getCellValue(row.getCell(index++), String.class).split(",");
					ActivityProp[] props = new ActivityProp[awardCNNames.length];
					if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
						new IllegalStateException("[exchangeActivity.xls] [sheet4] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
					} else {
						for (int j = 0; j < awardCNNames.length; j++) {
							props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
						}
					}
					String mailTitle = row.getCell(index++).getStringCellValue();
					String mailContent = row.getCell(index++).getStringCellValue();

					UnitServerRiseActivity usra = new UnitServerRiseActivity(id, platform, serverName, countryId, startTime, endTime, levelLimit, props, mailTitle, mailContent);
					riseActivities.add(usra);
				}
			}

			// 指定日期的登录活动
			sheet = hssfWorkbook.getSheetAt(5);
			if (sheet == null) {
				throw new Exception("sheet不存在:" + sheet.getSheetName());
			}
			int rows5 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows5; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					long id = (long) row.getCell(index++).getNumericCellValue();
					String platformName = row.getCell(index++).getStringCellValue();
					String[] serverNames = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", String.class);
					String day = row.getCell(index++).getStringCellValue();
					String articleName = row.getCell(index++).getStringCellValue();
					int articleColor = (int) row.getCell(index++).getNumericCellValue();
					int articleNum = (int) row.getCell(index++).getNumericCellValue();
					boolean articleBind = row.getCell(index++).getBooleanCellValue();
					String mailTitle = row.getCell(index++).getStringCellValue();
					String mailContent = row.getCell(index++).getStringCellValue();
					ActivityProp activityProp = new ActivityProp(articleName, articleColor, articleNum, articleBind);
					Set<String> servers = new HashSet<String>();
					for (String serverName : serverNames) {
						servers.add(serverName);
					}
					DailyLoginActivity dailyLoginActivity = new DailyLoginActivity(id, PlatformManager.getInstance().getPlatformByCNName(platformName), servers, day, activityProp, mailTitle, mailContent);
					dailyLoginActivities.add(dailyLoginActivity);
					logger.warn("[系统启动] [加载登录活动]" + dailyLoginActivity.toString());
				}
			}
			Game.logger.warn("[系统初始化] [合服相关]");
		} catch (Exception e) {
			throw e;
		}
	}

	public List<UnitServerActivity> getUnitServerActivityByName(String activityName) {
		List<UnitServerActivity> usActivities = new ArrayList<UnitServerActivity>();
		for (UnitServerActivity usa : unitServerActivities) {
			if (usa.getActivityName().equals(activityName)) {
				usActivities.add(usa);
			}
		}
		ActivitySubSystem.logger.warn("[总活动条数：" + unitServerActivities.size() + "]");
		ActivitySubSystem.logger.warn("[获得活动条数：" + usActivities.size() + "]");
		return usActivities;
	}

	/**
	 * 获取当前服务器对应的合服活动
	 * @param usaList
	 * @return
	 */
	public UnitServerActivity getRightUnitServerActivity(List<UnitServerActivity> usaList) {
		for (UnitServerActivity usa : usaList) {
			try {
				if (usa.isThisServerFit() == null) {
					return usa;
				}
			} catch (Exception e) {
				ActivitySubSystem.logger.error("[获取正确的合服活动出错] [" + usaList.get(0).getActivityName() + "]" + e);
				e.printStackTrace();
			}
		}
		ActivitySubSystem.logger.error("[没有获取到当前服务器对应的合服活动] [" + usaList.get(0).getActivityName() + "]");
		return null;
	}

	public UnitServerRiseActivity getRiseActivity(Player p) {
		if (p.getLevel() > 70) {
			byte countryId = p.getCountry();
			String serverName = GameConstants.getInstance().getServerName();
			for (UnitServerRiseActivity ra : riseActivities) {
				if (ra.getCountryId() == countryId && ra.getServerName().equals(serverName)) {
					if (ra.isOpen()) {
						return ra;
					} else {
						if (ActivitySubSystem.logger.isDebugEnabled()) {
							ActivitySubSystem.logger.debug("[合服活动] [崛起奖励] [当前服务器无对应活动] [" + serverName + "]");
						}
					}
				} else {
					if (ActivitySubSystem.logger.isDebugEnabled()) {
						ActivitySubSystem.logger.debug("[合服活动] [崛起奖励] [当前服务器无对应活动] [" + serverName + "]");
					}
				}
			}
		} else {
			ActivitySubSystem.logger.warn("[合服活动] [崛起奖励] [玩家等级不符] [" + p.getLogString() + "]");
		}
		return null;
	}

	/**
	 * 发送合服活动崛起奖励
	 */
	public void sendRiseAward(Player p) {
		UnitServerRiseActivity ra = getRiseActivity(p);
		if (ra != null) {
			String serverName = GameConstants.getInstance().getServerName();
			if (ActivityManagers.getInstance().getDdc().get(p.getId() + serverName + ra.getKeyId()) == null) {
				List<Player> players = new ArrayList<Player>();
				players.add(p);
				ActivityManager.sendMailForActivity(players, ra.getProps(), ra.getMailTitle(), ra.getMailContent(), "合服活动");
				ActivityManagers.getInstance().getDdc().put(p.getId() + serverName + ra.getKeyId(), 0l);
			}
		}
	}

	public void init() throws Exception {
		
		File file = new File(diskFile);
		sameNamePlayerCache = new DefaultDiskCache(file, null, "unitedserver", 100L * 365 * 24 * 3600 * 1000L);
		if (sameNamePlayerCache.get(DAILYLOGINACTIVITY_KEY) == null) {
			sameNamePlayerCache.put(DAILYLOGINACTIVITY_KEY, new HashSet<Long>());
		}
		instance = this;
		loadFile();
		MConsoleManager.register(this);
		logger.warn("[合区diskcache] [完成] [" + Arrays.toString(clearClasses.toArray()) + "]2013-08-01");
		ServiceStartRecord.startLog(this);
	}

	/** 处理登录活动 */
	public void dealwithDailyLoginActivity(Player player) {
		String day = TimeTool.formatter.varChar10.format(new Date());
		String serverName = GameConstants.getInstance().getServerName();
		Set<Long> playerReceiveRecord = getPlayerReceiveRecord(player);
		if (playerReceiveRecord == null) {
			playerReceiveRecord = new HashSet<Long>();
			recordPlayer(player.getId());
		}
		for (DailyLoginActivity activity : dailyLoginActivities) {
			if (activity != null) {
				if (activity.timeAndSeverFit(day, serverName)) {
					// 是否领取过了
					if (!playerReceiveRecord.contains(activity.getId())) {
						// 没领取过.可以领取,并且记录
						activity.doPrize(player);
						playerReceiveRecord.add(activity.getId());
						sameNamePlayerCache.put(getLoginActivityKey(player.getId()), (Serializable) playerReceiveRecord);
						logger.warn("[登录奖励] [领取成功] [" + player.getLogString() + "] [奖励数据:" + activity.getLogString() + "]");
					} else {
						logger.warn("[登录奖励] [已经领取过了] [" + player.getLogString() + "] [奖励数据:" + activity.getLogString() + "]");
					}
				}
			}
		}
	}
	
	public void getShouhunArticleEntity() throws Exception {
		getShouhunArticleEntity2(mainServer);
		for (UnitedServer server : unitedServerMap.values()) {
			getShouhunArticleEntity2(server);
		}
	}
	
	public void getShouhunArticleEntity2(UnitedServer server) throws Exception {
		SimpleEntityManager<HuntLifeEntity> manager = server.getFactory().getSimpleEntityManager(HuntLifeEntity.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		while (count > 0) {
			end = start + pagSize;
			List<HuntLifeEntity> list = manager.query(HuntLifeEntity.class, "", "", start, end);
			if(list != null){
				for(HuntLifeEntity wp : list){
					if(wp == null){
						continue;
					}
					//兽魂面板
					long[] inlayArticleIds = wp.getHuntDatas();
					if(inlayArticleIds != null){
						for(long id : inlayArticleIds){
							if(id > 0){
								ids.add(id);
							}
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
		}
	}
	
	public void getXiaZiArticleEntitys() throws Exception {
		int num = 0;
		num += getXiaZiArticleEntitysToServer(mainServer);
		logger.warn("[从主服宝石匣子中取出entityID] [{}]", new Object[] { num });
		for (UnitedServer server : unitedServerMap.values()) {
			num += getXiaZiArticleEntitysToServer(server);
		}
		logger.warn("[从宝石匣子中取出entityID] [{}]", new Object[] { num });
	}

	public int getXiaZiArticleEntitysToServer(UnitedServer server) throws Exception {
		SimpleEntityManager<BaoShiXiaZiData> manager = server.getFactory().getSimpleEntityManager(BaoShiXiaZiData.class);
		HashSet<Long> ids = articleAllEntitys.get(server.getServerName());
		long count = manager.count();
		long start = 1;
		long end = 1;
		long pagSize = 500;
		int entityNum = 0;
		while (count > 0) {
			long now = System.currentTimeMillis();
			end = start + pagSize;
			List<BaoShiXiaZiData> datas = manager.query(BaoShiXiaZiData.class, "", "", start, end);
			if (datas != null) {
				for (BaoShiXiaZiData j : datas) {
					if(j != null && j.getIds() != null){
						for(long id : j.getIds()){
							if(id > 0){
								ids.add(id);
								entityNum++;
							}
						}
					}
				}
			}
			start += pagSize;
			count -= pagSize;
			logger.warn("[getArticleEntitys->从宝石匣子中取出entityID] [server:{}] [start:{}] [end:{}] [left:{}] [entityNum:{}] [elap:{}ms]", new Object[] { server.getServerName(), start - pagSize, end, count, entityNum,(System.currentTimeMillis() - now) });
		}
		logger.warn("从" + server.getServerName() + "宝石匣子中取出:" + entityNum);
		return entityNum;
	}

	public synchronized void recordPlayer(long playerId) {
		Set<Long> activityPlayerIds = (Set<Long>) sameNamePlayerCache.get((Serializable) DAILYLOGINACTIVITY_KEY);
		if (activityPlayerIds.contains(playerId)) {
			return;
		}
		activityPlayerIds.add(playerId);
		sameNamePlayerCache.put(DAILYLOGINACTIVITY_KEY, (Serializable) activityPlayerIds);
	}

	public String getLoginActivityKey(long playerId) {
		return "login_activity_" + playerId;
	}

	/**
	 * 获得玩家领取登录活动的记录
	 * @param player
	 * @return
	 */
	public Set<Long> getPlayerReceiveRecord(Player player) {
		Object obj = sameNamePlayerCache.get(getLoginActivityKey(player.getId()));
		if (obj != null) {
			return (Set<Long>) obj;
		}
		return null;
	}

	public static String getTotolCost() {
		long cost = System.currentTimeMillis() - unitedServerStartTime;
		return TimeTool.instance.getShowTime(cost);
	}

	@Override
	public String getMConsoleName() {
		return "合区";
	}

	@Override
	public String getMConsoleDescription() {
		return "合区数据管理";
	}
}

interface PetProp {
	long getPetPropsId();
}