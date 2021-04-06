package com.fy.engineserver.movedata;

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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.GameDataRecord;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.activeness.PlayerActivenessInfo;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.instance.HuntLifeEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.PetHookInfo;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.movedata.moveArticle.MoveArticleManager;
import com.fy.engineserver.newtask.NewDeliverTask;
import com.fy.engineserver.playerAims.instance.PlayerAimsEntity;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.qiancengta.QianCengTa_Ceng;
import com.fy.engineserver.qiancengta.QianCengTa_Dao;
import com.fy.engineserver.qiancengta.QianCengTa_Ta;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.instance.Horse2RelevantEntity;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet2.PetsOfPlayer;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.MConsole;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
import com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;

public class DataMoveManager implements MConsole {

	private DefaultSimpleEntityManagerFactory factory;

	// public static Logger logger = LoggerFactory.getLogger(UnitedServerManager.class);
	public static Logger logger = TransitRobberyManager.logger;
	public static Logger ExcepTionlogger = LoggerFactory.getLogger("Exception");

	private String configFilePath;
	/** 不需要导入新库的playerId */
	private Set<Long> playerIds = new HashSet<Long>(); // 考虑是否要改为不需要转移的废弃数据，因为sql方便写

	/** 需要保留player和背包物品的playerIds */
	private Set<Long> needKeepPlayer = new HashSet<Long>();

	public long playerTotalCount = 0;

	public static long now = System.currentTimeMillis();

	/** 物品IDS */
	public Queue<Long> articleIds = new LinkedList<Long>();

	public static int maxArticleIdNum = 2000000;

	public static final long MONTH = 30L * 24 * 60 * 60 * 1000;

	public static DataMoveManager instance;

	public boolean articleCollectThreadIsOver = false;

	public static final int ONCE_QUERY = 6000;

	public Map<Long, HashSet<Long>> playerArticleIds = new LinkedHashMap<Long, HashSet<Long>>();

	private List<DataMoveThread> currentRunThreads = new ArrayList<DataMoveThread>();

	public static List<Class<?>> moveClasses = new ArrayList<Class<?>>();// 需要导入新库的所有表

	public static int sameTimeRunUnitedClassNum = 5;// 同时转移数据的线程最大数
	/** key=class value[0]=原库数据总数 value[1]=导入新库的数据总数 **/
	public Map<Class<?>, Long[]> dataMoveMaps = new HashMap<Class<?>, Long[]>();
	
	public static StringBuffer errorLog = new StringBuffer();
	public static StringBuffer checkLog = new StringBuffer();
	
	// 不需要导入新库的数据规则：
	// 1、未充值
	// 2、连续1个月未登陆服务器,40级以下（不含40级）角色及相关可删除
	// 3、连续3个月未登陆服务器，70级以下(不含70级)角色及相关可删除
	// 4、连续6个月未登陆服务器，110级以下(不含110级)角色及相关可删除
	// 5、连续12个月未登陆服务器，150级以下(不含150级)角色及相关可删除
	public static final String[] wheres = new String[] { "RMB = 0 and quitGameTime <=" + (now - 3 * MONTH) + " and level < 40", "RMB = 0 and quitGameTime <=" + (now - 6 * MONTH) + " and level < 70" };
	public static final String[] wheres2 = new String[] { "RMB = 0 and quitGameTime <=" + (now - 6 * MONTH) + " and level < 110", "RMB = 0 and quitGameTime <=" + (now - 12 * MONTH) + " and level < 150" };
	// public static final String[] wheres = new String[]{
	// "RMB = 0 and quitGameTime <=" + (now-MONTH) + " and level < 40",
	// "RMB = 0 and quitGameTime <=" + (now-3*MONTH) + " and level < 70",
	// "RMB = 0 and quitGameTime <=" + (now-6*MONTH) + " and level < 110",
	// "RMB = 0 and quitGameTime <=" + (now-12*MONTH) + " and level < 150"};

	public static final byte checkTypeSimpleId = 1;
	public static final byte checkTypeVar = 2;
	public static final byte checkTypeOther = 3;

	public static final boolean isdebug = true;
	public static boolean tiaoshi = false;
	
	public List<Long> allPlayerIds = new ArrayList<Long>();
	public List<Long> hasSave = new ArrayList<Long>();

	public static boolean hasStart = false;

	public static String[] mailAddress = new String[] {"3472335707@qq.com","116004910@qq.com"};
	/** 存储所有导入新库的playerId，倒物品用 */
	public List<Long> needKeepPlayerIds = new ArrayList<Long>();

	// 当前时间 - 一个月 < 下线时间

	public static List<Class<?>> nomalClass = new ArrayList<Class<?>>();// simpleId就是playerId的entity类

	public static Map<Class<?>, String> checkVarClass = new HashMap<Class<?>, String>(); // 根据配置的变量名匹配playerId决定是否为有效数据

	public static List<Class<?>> otherClass = new ArrayList<Class<?>>();

	static {
		otherClass.add(ArticleEntity.class);
		otherClass.add(JiazuMember.class);
		otherClass.add(Relation.class);
		otherClass.add(Jiazu.class);
		otherClass.add(Unite.class);
		otherClass.add(MarriageInfo.class);
	}

	static {
		nomalClass.add(Player.class);
		nomalClass.add(TransitRobberyEntity.class);
		nomalClass.add(JiazuMember2.class);
		nomalClass.add(Horse2RelevantEntity.class);
		nomalClass.add(PetsOfPlayer.class);
		nomalClass.add(PlayerActivenessInfo.class);
		nomalClass.add(SkBean.class);
		nomalClass.add(PlayerAimsEntity.class);
	}

	static {
		checkVarClass.put(JiazuMember.class, "playerID");
		checkVarClass.put(Cave.class, "ownerId");
		// checkVarClass.put(DeliverTask.class, "playerId");
		checkVarClass.put(GameDataRecord.class, "playerId");
		checkVarClass.put(QianCengTa_Ta.class, "playerId");
		checkVarClass.put(Horse.class, "ownerId");
		checkVarClass.put(Pet.class, "ownerId");
		// checkVarClass.put(Mail.class, "receiver");
//		checkVarClass.put(TaskEntity.class, "playerId");
		checkVarClass.put(NewDeliverTask.class, "playerId");
	}

	public DataMoveManager(String configFilePath) {
		{
			for (Class<?> clazz : nomalClass) {
				SimpleEntityManager<?> simpleEntityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
				simpleEntityManager.setReadOnly(true);
			}
			for (Class<?> clazz : checkVarClass.keySet()) {
				SimpleEntityManager<?> simpleEntityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
				simpleEntityManager.setReadOnly(true);
			}
			for (Class<?> clazz : otherClass) {
				SimpleEntityManager<?> simpleEntityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
				simpleEntityManager.setReadOnly(true);
			}
		}
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.configFilePath = configFilePath;
		factory = new DefaultSimpleEntityManagerFactory(configFilePath);
		factory.getSimpleEntityManager(Player.class);
		instance = this;

	}
	/** 超过此数据量的等待其他线程跑完再跑 */
	public static long 阈值 = 50000000;
	
	public static boolean 是否使用旧的删物品方法 = false;

	/**
	 * 开始导数据 流程：1、查找出所有需要导的playerId 2、根据查找出来的playerId查找其他库中需要导入新库的数据并导入到新库中
	 * @throws InterruptedException
	 */
	public void dataMoveBegin() throws InterruptedException {

		if (hasStart) {
			logger.warn("**************************************************************");
			logger.warn("********************此方法已经被调用，不可重复调用**********************");
			logger.warn("**************************************************************");
			return;
		}
		logger.warn("**************************************************************");
		logger.warn("********************       和服倒数据开始              *********************");
		logger.warn("**************************************************************");
		hasStart = true;
		totalStartTime = System.currentTimeMillis();

		{
			// 启动释放弱引用
			DataMoveThread releaseReference = new DataMoveThread("releaseReference", null, null);
			Thread thread_releaseReference = new Thread(releaseReference, "转移数据-releaseReference");
			thread_releaseReference.start();
		}

		{
			// 设置批量保存数量
			for (Class<?> clazz : nomalClass) {
				long dropIndexStartTime = System.currentTimeMillis();
				SimpleEntityManager<?> simpleEntityManager = getFactory().getSimpleEntityManager(clazz);
				// simpleEntityManager.dropEntityIndex(clazz);
				// 设置一次自动存储的数量
				simpleEntityManager.setBatchSaveOrUpdateSize(1000);
				logger.warn("[修改批量保存数据量:" + clazz.getSimpleName() + "] [耗时:" + (System.currentTimeMillis() - dropIndexStartTime) + "ms]");
			}
			for (Class<?> clazz : checkVarClass.keySet()) {
				long dropIndexStartTime = System.currentTimeMillis();
				SimpleEntityManager<?> simpleEntityManager = getFactory().getSimpleEntityManager(clazz);
				// simpleEntityManager.dropEntityIndex(clazz);
				// 设置一次自动存储的数量
				simpleEntityManager.setBatchSaveOrUpdateSize(1000);
				logger.warn("[修改批量保存数据量:" + clazz.getSimpleName() + "] [耗时:" + (System.currentTimeMillis() - dropIndexStartTime) + "ms]");
			}
		}

		this.collectPlayerIds(); // 查找需要转移的playerId
		this.collectPlayerIds2(); // 查找需要转移的playerId
//		if (playerIds.size() > 0) {
			// 根据之前收集好的playerId收集对应库中数据导入新表中
			List<Class<?>> leftmoveClasses = new ArrayList<Class<?>>();// 剩余没有执行过合并的classes
			leftmoveClasses.addAll(nomalClass);
			while (leftmoveClasses.size() > 0) {
				if (currentRunThreads.size() < sameTimeRunUnitedClassNum) {
					Class<?> clazz = leftmoveClasses.remove(0);
					try {
						DataMoveThread dt = new DataMoveThread("move", new Class<?>[] { Class.class, byte.class }, new Object[] { clazz, checkTypeSimpleId });
						Thread thread = new Thread(dt, "转移数据-" + clazz.getSimpleName());
						thread.start();
						currentRunThreads.add(dt);
					} catch (Exception e) {
						logger.error("转移数据出错了:", e);
						ExcepTionlogger.error("转移数据出错了:", e);
					}
				}
				logger.warn("[数据合并中] [暂停20秒] [剩余class" + leftmoveClasses.size() + "] [正在执行线程数:" + currentRunThreads.size() + "] [正在执行的class] [" + getCurrentRunThreadInfo() + "] [总耗时:" + 1 + "]");
				Thread.sleep(20000L);
				removeDoneThread();
			}

			while (!allThreadOver(currentRunThreads)) {
				removeDoneThread(); // 在此线程中清理应该删除 但是未删除的已经运行完毕的线程
				logger.warn("[最后的表合并中1] [每分钟检查一次] [" + getCurrentRunThreadInfo() + "] [总耗时:" + (System.currentTimeMillis() - startTime) + "]");
				Thread.sleep(60000);
			}
			leftmoveClasses.clear();
			leftmoveClasses.addAll(checkVarClass.keySet());
			while (leftmoveClasses.size() > 0) {
				if (currentRunThreads.size() < sameTimeRunUnitedClassNum) {
					Class<?> clazz = leftmoveClasses.remove(0);
					try {
						DataMoveThread dt = new DataMoveThread("move", new Class<?>[] { Class.class, byte.class }, new Object[] { clazz, checkTypeVar });
						Thread thread = new Thread(dt, "转移数据-" + clazz.getSimpleName());
						thread.start();
						currentRunThreads.add(dt);
					} catch (Exception e) {
						logger.error("转移数据出错了:", e);
						ExcepTionlogger.error("转移数据出错了:", e);
					}
				}
				logger.warn("[数据合并中] [暂停20秒] [剩余class" + leftmoveClasses.size() + "] [正在执行线程数:" + currentRunThreads.size() + "] [正在执行的class] [" + getCurrentRunThreadInfo() + "] [总耗时:" + 1 + "]");
				Thread.sleep(20000L);
				removeDoneThread();
			}
			boolean lastMoveArticle = false;
			{ // 在将所有的数据导完以后执行
				DataMoveThread dt4 = new DataMoveThread("move", new Class<?>[] { Class.class, byte.class }, new Object[] { MarriageInfo.class, checkTypeOther });
				Thread thread4 = new Thread(dt4, "结婚数据转移");
				thread4.start();
				currentRunThreads.add(dt4);

				DataMoveThread dt = new DataMoveThread("moveJiazuData", null, null);
				Thread thread = new Thread(dt, "家族数据转移");
				thread.start();
				currentRunThreads.add(dt);

				DataMoveThread dt2 = new DataMoveThread("moveJJCTeamData", null, null);
				Thread thread2 = new Thread(dt2, "竞技场战队数据转移");
				thread2.start();
				currentRunThreads.add(dt2);

				DataMoveThread dt3 = new DataMoveThread("moveJJCMemberDat", null, null);
				Thread thread3 = new Thread(dt3, "竞技场成员数据转移");
				thread3.start();
				currentRunThreads.add(dt3);

				DataMoveThread dt6 = new DataMoveThread("changeUnitData", null, null);
				Thread thread6 = new Thread(dt6, "结义数据转移");
				thread6.start();
				currentRunThreads.add(dt6);

				while (dt4.isRunning()) { // relation数据转移必须得在结婚数据转移完成后才能做
					Thread.sleep(5000);
				}
				DataMoveThread dt5 = new DataMoveThread("move", new Class<?>[] { Class.class, byte.class }, new Object[] { Relation.class, checkTypeSimpleId });
				Thread thread5 = new Thread(dt5, "玩家人物关系数据转移");
				thread5.start();
				currentRunThreads.add(dt5);

				DataMoveThread dt7 = new DataMoveThread("moveJJCBattleData", null, null);
				Thread thread7 = new Thread(dt7, "竞技场战斗数据转移");
				thread7.start();
				currentRunThreads.add(dt7);
				if (是否使用旧的删物品方法) {
					try {
						long aeCt = ArticleEntityManager.getInstance().em.count();
						lastMoveArticle = aeCt > 阈值;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!lastMoveArticle) {				//是否在最后单线程跑物品
						DataMoveThread dt8 = new DataMoveThread("collectArticleEntityData", new Class<?>[] { Queue.class }, new Object[] { articleIds });
						Thread thread8 = new Thread(dt8, "物品收集");
						thread8.start();
						currentRunThreads.add(dt8);
		
						DataMoveThread dt9 = new DataMoveThread("moveArticleEntityData", new Class<?>[] { Queue.class }, new Object[] { articleIds });
						Thread thread9 = new Thread(dt9, "物品转移");
						thread9.start();
						currentRunThreads.add(dt9);
					}
				}

				DataMoveThread dt10 = new DataMoveThread("movePetData", null, null);
				Thread thread10 = new Thread(dt10, "宠物道具转移");
				thread10.start();
				currentRunThreads.add(dt10);
			}

			while (!allThreadOver(currentRunThreads)) {
				removeDoneThread(); // 在此线程中清理应该删除 但是未删除的已经运行完毕的线程
				logger.warn("[最后的表合并中2] [每分钟检查一次] [" + getCurrentRunThreadInfo() + "] [总耗时:" + (System.currentTimeMillis() - startTime) + "]");
				Thread.sleep(60000);
			}
			
			if (是否使用旧的删物品方法 && lastMoveArticle) {				//是否在最后单线程跑物品
				DataMoveThread dt8 = new DataMoveThread("collectArticleEntityData", new Class<?>[] { Queue.class }, new Object[] { articleIds });
				Thread thread8 = new Thread(dt8, "物品收集");
				thread8.start();
				currentRunThreads.add(dt8);

				DataMoveThread dt9 = new DataMoveThread("moveArticleEntityData", new Class<?>[] { Queue.class }, new Object[] { articleIds });
				Thread thread9 = new Thread(dt9, "物品转移");
				thread9.start();
				currentRunThreads.add(dt9);
			}
			while (!allThreadOver(currentRunThreads)) {
				removeDoneThread(); // 在此线程中清理应该删除 但是未删除的已经运行完毕的线程
				logger.warn("[最后的表合并中3] [物品] [每分钟检查一次] [" + getCurrentRunThreadInfo() + "] [总耗时:" + (System.currentTimeMillis() - startTime) + "]");
				Thread.sleep(60000);
			}
			String rr = "";
			if (!是否使用旧的删物品方法) {
				MoveArticleManager inst = new MoveArticleManager(this.configFilePath);
				try {
					inst.playerIds = needKeepPlayerIds;
					inst.moveBegin();
				} catch (Exception e) {
					logger.warn("[!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!]!!!!!!!!!!!!!!!!!!!!!!!!!!");
					logger.warn("[!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!] [删物品] [异常] [!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!] ", e);
					logger.warn("[!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!]");
					rr = "删物品异常，需要查看！!!!!!!!!!!!!!!!!!!!!!!!！";
				}
			}
			
			long cost = System.currentTimeMillis() - totalStartTime;
			logger.warn("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			logger.warn("$$$$$$$ [导数据执行结束] [总耗时:" + (TimeTool.instance.getShowTime(cost)) + "] $$$$$$");
			logger.warn("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			String des = GameConstants.getInstance().getServerName() + "_导数据结束";
			String content = rr + des + ",总耗时:" + (TimeTool.instance.getShowTime(cost));
			if (errorLog.length() > 0) {
				content = content + "<br>" + errorLog.toString();
			}
			check4AllDatabase();
			content = content + "<br>" + checkLog.toString();
			DataMoveManager.sendMail(des, content);

//		} else {
//			logger.warn("[*****************************************************************]");
//			logger.warn("[************************有效的player数据为0**************************]");
//			logger.warn("[*****************************************************************]");
//		}
	}
	
	public void check4AllDatabase() {
		for (Class<?> clazz : nomalClass) {
			try {
				SimpleEntityManager<?> entityManager = DataMoveManager.instance.getFactory().getSimpleEntityManager(clazz);
				long count = entityManager.count();
				checkLog.append("[").append(clazz.toString()).append("<font color='red'><pre>").append("总数据量:").append(count).append("</pre></font>]<br>");
			} catch (Exception e) {
				errorLog.append("[收集合服后数据库数据] [异常] [" + e.toString() + "]");
				logger.warn("[收集合服后数据库数据] [异常]", e);
			}
		}
		for (Class<?> clazz : DataMoveManager.otherClass) {
			try {
				SimpleEntityManager<?> entityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
				long count = entityManager.count();
				checkLog.append("[").append(clazz.toString()).append("<font color='red'><pre>").append("总数据量:").append(count).append("</pre></font>]<br>");
			} catch (Exception e) {
				errorLog.append("[收集合服后数据库数据] [异常] [" + e.toString() + "]");
				logger.warn("[收集合服后数据库数据] [异常]", e);
			}
		}
		for (Class<?> clazz : DataMoveManager.checkVarClass.keySet()) {
			try {
				SimpleEntityManager<?> entityManager = SimpleEntityManagerFactory.getSimpleEntityManager(clazz);
				long count = entityManager.count();
				checkLog.append("[").append(clazz.toString()).append("<font color='red'><pre>").append("总数据量:").append(count).append("</pre></font>]<br>");
			} catch (Exception e) {
				errorLog.append("[收集合服后数据库数据] [异常] [" + e.toString() + "]");
				logger.warn("[收集合服后数据库数据] [异常]", e);
			}
		}
		try {
			SimpleEntityManager<?> entityManager = SimpleEntityManagerFactory.getSimpleEntityManager(ArticleEntity.class);
			long count = entityManager.count();
			checkLog.append("[").append(ArticleEntity.class.toString()).append("<font color='red'><pre>").append("总数据量:").append(count).append("</pre></font>]<br>");
		} catch (Exception e) {
			errorLog.append("[收集合服后数据库数据] [异常] [" + e.toString() + "]");
			logger.warn("[收集合服后数据库数据] [异常]", e);
		}
	}

	public static void sendMail(String title, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append(content);
		sb.append("<br>" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		ArrayList<String> args = new ArrayList<String>();
		args.add("-username");
		args.add("wtx062@126.com");
		args.add("-password");
		args.add("wangtianxin1986");

		args.add("-smtp");
		args.add("smtp.126.com");
		args.add("-from");
		args.add("wtx062@126.com");
		args.add("-to");
		// TODO mailAddress 修改邮件
		String address_to = "";

		for (String address : mailAddress) {
			address_to += address + ",";
		}

		if (!"".equals(address_to)) {
			args.add(address_to);
			args.add("-subject");
			args.add(title);
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=utf-8");
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean allThreadOver(List<DataMoveThread> list) {
		for (DataMoveThread serverThread : list) {
			if (serverThread.isRunning()) {
				return false;
			}
		}
		return true;
	}

	public String getCurrentRunThreadInfo() {
		StringBuffer sbf = new StringBuffer();
		try {
			for (DataMoveThread ut : currentRunThreads) {
				if (ut == null) {
					continue;
				}
				String name = "";
				if (ut.getArgs() != null && ut.getArgs().length > 0) {
					name = ut.getArgs()[0].toString();
				}
				if (name != null) {
					name = ut.getMethodName();
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

	private void removeDoneThread() {
		Iterator<DataMoveThread> itor = currentRunThreads.iterator();
		while (itor.hasNext()) {
			DataMoveThread ut = itor.next();
			if (!ut.isRunning()) {
				logger.warn("[移除进程] [成功] [" + ut.getMethodName() + "]");
				itor.remove();
			}
		}
	}

	public static int tempNum = 3000;

	/**
	 * 收集需要导入新库的playerId(在导数据开始前需要全部查出来) 规则：
	 */
	public void collectPlayerIds() {
		long sT = System.currentTimeMillis();
		SimpleEntityManager<Player> manager = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		Field idField = UnitedServerManager.getFieldByAnnotation(Player.class, SimpleId.class);
		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + Player.class.toString() + "]");
		}
		try {
			this.playerTotalCount = manager.count();
			if (playerTotalCount > 0) {
				if (playerTotalCount <= 30000 ) {
					checkVersionTime = 1000L;
					DataMove2.checkVersionTime = 1000L;
				}
				// long[] idss = new long[1];
				// List<Long> tempIdList = new ArrayList<Long>();
				long lastQuitTime = now - 3 * MONTH;
				long left = playerTotalCount + 1; // 剩余未查出的数量
				for (int i = 0; i < wheres.length; i++) {
					long[] tempId = manager.queryIds(Player.class, wheres[i], "", 1, left);// 这里改成prepared
					int quertCount = tempId.length;
					int resultCount = 0;
					List<Long> tempList = new ArrayList<Long>();
					for (long id : tempId) {
						if (!playerIds.contains(id)) {
							tempList.add(id);
						}

						if (tempList.size() >= tempNum) {
							long[] tempPlayerIds = new long[tempList.size()];
							for (int kk = 0; kk < tempPlayerIds.length; kk++) {
								tempPlayerIds[kk] = tempList.get(kk);
							}
							// idss[0] = id;
							List<SimplePlayerInfo> list = manager.queryFields(SimplePlayerInfo.class, tempPlayerIds);
							for (SimplePlayerInfo spi : list) {
								if (tempList.contains(spi.getId()) && spi.getLevel() < 70 && spi.getQuitGameTime() < lastQuitTime) {
									playerIds.add(spi.getId());
									resultCount++;
								}
							}
							tempList.clear();
						}
					}
					if (tempList.size() > 0) {
						long[] tempPlayerIds = new long[tempList.size()];
						for (int kk = 0; kk < tempPlayerIds.length; kk++) {
							tempPlayerIds[kk] = tempList.get(kk);
						}
						// idss[0] = id;
						List<SimplePlayerInfo> list = manager.queryFields(SimplePlayerInfo.class, tempPlayerIds);
						for (SimplePlayerInfo spi : list) {
							if (tempList.contains(spi.getId()) && spi.getLevel() < 70 && spi.getQuitGameTime() < lastQuitTime) {
								playerIds.add(spi.getId());
								resultCount++;
							}
						}
					}
					tempList = null;
					logger.warn("[Player收集数据] [sql:" + wheres[i] + "] [sql查出id数量:" + quertCount + "] [符合规则数据:" + resultCount + "]");
				}
				logger.warn("[Player收集数据] [总数:" + playerTotalCount + "] [无效数据:" + playerIds.size() + "]");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[Player收集数据]", e);
			e.printStackTrace();
		}
		logger.warn("[player数据收集完成] [耗时:" + (System.currentTimeMillis() - sT) + "毫秒]");
	}

	public void collectPlayerIds2() {
		long sT = System.currentTimeMillis();
		SimpleEntityManager<Player> manager = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		Field idField = UnitedServerManager.getFieldByAnnotation(Player.class, SimpleId.class);
		if (idField == null) {
			throw new IllegalStateException("[未找到ID字段] [" + Player.class.toString() + "]");
		}
		try {
			this.playerTotalCount = manager.count();
			if (playerTotalCount > 0) {
				long left = playerTotalCount + 1; // 剩余未查出的数量
				for (int i = 0; i < wheres2.length; i++) {
					long[] tempId = manager.queryIds(Player.class, wheres2[i], "", 1, left);// 这里改成prepared
					for (long id : tempId) {
						needKeepPlayer.add(id);
					}
				}
				logger.warn("[Player收集数据] [总数:" + playerTotalCount + "] [需要保留部分数据的player数:" + needKeepPlayer.size() + "]");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[Player收集数据]", e);
			e.printStackTrace();
		}
		logger.warn("[needKeepPlayer数据收集完成] [耗时:" + (System.currentTimeMillis() - sT) + "毫秒]");
	}

	public <T> void move(Class<T> clazz, byte checkType) {
		Queue<T> queue = new LinkedList<T>();
		long start = System.currentTimeMillis();
		logger.warn("[$$$$$$$$$$$$$$$$$$$$] [转移数据] [开始] [" + clazz.getSimpleName() + "]");

		DataCollect2<T> dataCollect = new DataCollect2<T>(queue, clazz);
		DataMove2<T> dataMove = new DataMove2<T>(queue, clazz, dataCollect, checkType);
		Thread collectThread = new Thread(dataCollect, "collect2Thread-" + clazz.getSimpleName());
		Thread unitThread = new Thread(dataMove, "move2Thread-" + clazz.getSimpleName());
		collectThread.start();
		unitThread.start();

		int minute = 0;
		while (dataCollect.isRunning() || dataMove.isRunning()) {
			try {
				Thread.sleep(60000L);
				minute++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.warn("[转移数据] [" + clazz.getSimpleName() + "] [转移中] [已耗时:" + minute + "分钟]");
		}

		logger.warn("[转移数据] [结束] [" + clazz.getSimpleName() + "] [" + (++uniteClassesNum) + "/" + (nomalClass.size() + checkVarClass.size()) + "] [耗时:" + TimeTool.instance.getShowTime(System.currentTimeMillis() - start) + "]");
		if (uniteClassesNum >= (nomalClass.size() + checkVarClass.size())) {
			logger.error("###########################################################################################################");
			logger.error("############################################转移结束(" + (System.currentTimeMillis() - startTime) + ")ms)###########################################");
			logger.error("###########################################################################################################");
		}
	}

	private long startTime;
	/** 只在开始转移数据时!!! */
	private long totalStartTime;

	static int uniteClassesNum = 0;

	public boolean releaseReference = true;

	public void releaseReference() {
		while (releaseReference) {
			long loopStart = System.currentTimeMillis();
			for (Class<?> clazz : nomalClass) {
				SimpleEntityManagerFactory.getSimpleEntityManager(clazz).releaseReference();
			}
			for (Class<?> clazz : checkVarClass.keySet()) {
				SimpleEntityManagerFactory.getSimpleEntityManager(clazz).releaseReference();
			}
			try {
				logger.error("[调用底层释放弱引用] [2秒一次] [耗时:" + TimeTool.instance.getShowTime(System.currentTimeMillis() - loopStart) + "]");
				Thread.sleep(20000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*** 无效的结婚数据，在转移relation的时候需要将有效的relation中这些无效的结婚id清除 */
	public Set<Long> needRemoveMarriageId = new HashSet<Long>();

	/** key=jiazuId value=需要从家族中清除掉的无效的memberIds */
	public Map<Long, Set<Long>> needRemoveJiazuMembers = new Hashtable<Long, Set<Long>>();

	public void notifyNewNeedRemoveMembers(long jiazuId, long memberId) {
		Set<Long> set = needRemoveJiazuMembers.get(jiazuId);
		if (set == null) {
			set = new HashSet<Long>();
		}
		set.add(memberId);
		needRemoveJiazuMembers.put(jiazuId, set);
	}

	/**
	 * 根据之前统计出来的有效的playerId，更新结义表中的数据，结义玩家列表为空的就不需要转移到新库了
	 */
	public void changeUnitData() {
		try {
			SimpleEntityManager<Unite> unitEntityManager = SimpleEntityManagerFactory.getSimpleEntityManager(Unite.class);
			unitEntityManager.setReadOnly(true);
			Field versionField = DataMoveManager.getFieldByAnnotation(Unite.class, SimpleVersion.class);
			versionField.setAccessible(true);
			List<Unite> allUnite = new ArrayList<Unite>();
			long count = unitEntityManager.count();
			long start = 1;
			long end = 1;
			long pagSize = 500;
			while (count > 0) {
				long now = System.currentTimeMillis();
				end = start + pagSize;
				List<Unite> unites = unitEntityManager.query(Unite.class, "", "", start, end);
				if (unites != null) {
					for (int i = 0; i < unites.size(); i++) {
						allUnite.add(unites.get(i));
					}
				}
				start += pagSize;
				count -= pagSize;
				logger.warn("[结义数据更新->加载结义数据] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { start - pagSize, end, count, (System.currentTimeMillis() - now) });
			}
			Unite[] unites = allUnite.toArray(new Unite[0]);
			allUnite = null;
			SimpleEntityManager<Unite> unitEntityManager2 = this.factory.getSimpleEntityManager(Unite.class);
			List<Unite> notifiedList = new ArrayList<Unite>();
			for (int i = 0; i < unites.length; i++) { // 将查出来的jiazu数据存入新库
				while (unitEntityManager2.countUnSavedNewObjects() >= 3000) { // 需要存储的数据量过大。暂停两秒
					Thread.sleep(2000);
				}
				List<Long> list = unites[i].getMemberIds();
				if (list != null && list.size() > 0) {
					List<Long> needRemove = new ArrayList<Long>();
					for (long pId : list) {
						if (this.playerIds.contains(pId)) {
							needRemove.add(pId);
						}
					}
					if (needRemove.size() == list.size()) { // 需要删除的数据量与总数据量相同就直接跳过
						continue;
					} else {
						list.removeAll(needRemove);
					}
				}
				if (list != null && list.size() > 0) { // 结义列表中有数据才需要导入新库
					versionField.setInt(unites[i], 0);
					unitEntityManager2.notifyNewObject(unites[i]);
					notifiedList.add(unites[i]);
				}
			}
		} catch (Exception e) {
			DataMoveManager.logger.error("[更新结义数据异常]", e);
		}
	}

	/**
	 * 根据之前统计出来的不用导入新库的jiazumemberId更新jiazu中的成员列表
	 */
	public void moveJiazuData() {
		try {
			SimpleEntityManager<Jiazu> jiazuManager = SimpleEntityManagerFactory.getSimpleEntityManager(Jiazu.class);
			jiazuManager.setReadOnly(true);
			Field versionField = DataMoveManager.getFieldByAnnotation(Jiazu.class, SimpleVersion.class);
			versionField.setAccessible(true);
			List<Jiazu> allJiaZus = new ArrayList<Jiazu>();
			long count = jiazuManager.count();
			long start = 1;
			long end = 1;
			long pagSize = 500;
			while (count > 0) {
				long now = System.currentTimeMillis();
				end = start + pagSize;
				List<Jiazu> jiazus = jiazuManager.query(Jiazu.class, "", "", start, end);
				if (jiazus != null) {
					for (int i = 0; i < jiazus.size(); i++) {
						allJiaZus.add(jiazus.get(i));
					}
				}
				start += pagSize;
				count -= pagSize;
				logger.warn("[家族成员列表更新->加载家族] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { start - pagSize, end, count, (System.currentTimeMillis() - now) });
			}
			Jiazu[] jiazuS = allJiaZus.toArray(new Jiazu[0]);
			allJiaZus = null;
			SimpleEntityManager<Jiazu> jiazuManager2 = this.factory.getSimpleEntityManager(Jiazu.class);
			List<Jiazu> notifiedList = new ArrayList<Jiazu>();
			for (int i = 0; i < jiazuS.length; i++) { // 将查出来的jiazu数据存入新库
				while (jiazuManager2.countUnSavedNewObjects() >= 3000) { // 需要存储的数据量过大。暂停两秒
					Thread.sleep(2000);
				}
				Set<Long> set = needRemoveJiazuMembers.get(jiazuS[i].getJiazuID());
				if (set != null) {
					for (long memberId : set) {
						jiazuS[i].getMemberSet().remove(memberId);
					}
				}
				versionField.setInt(jiazuS[i], 0);
				jiazuManager2.notifyNewObject(jiazuS[i]);
				notifiedList.add(jiazuS[i]);
			}
			needRemoveJiazuMembers = null;
			jiazuS = null;
			while (notifiedList.size() > 0) {
				try {
					Thread.sleep(5000);
					{ // 剔除version不等于0的数据
						Iterator<Jiazu> itor = notifiedList.iterator();
						while (itor.hasNext()) {
							Jiazu t = itor.next();
							try {
								int version = versionField.getInt(t);
								if (version != 0) {
									itor.remove();
								}
							} catch (Exception e) {
								DataMoveManager.logger.error("DataMove出错:", e);
							}
						}
					}
				} catch (Exception e2) {
					DataMoveManager.logger.error("DataMove2出错:", e2);
				}
			}

		} catch (Exception e) {
			DataMoveManager.logger.error("DataMove3出错:", e);
		}
	}


	public List<Long> lists = new ArrayList<Long>();

	/**
	 * 物品相关收集
	 */
	public void collectArticleEntityData(Queue<Long> queue) {
		long now = System.currentTimeMillis();
		long starttime = System.currentTimeMillis();
		try {
			int articleCollectSuccNum = 0; // 物品收集总数
			int effectPlayerNums = 0; // 有效玩家数
			int beibao = 0; // 背包物品数
			int cangku = 0; // 仓库物品数
			int fangbaobao = 0; // 防暴包物品数+防暴包本身
			int zhuangbei = 0; // 装备数目，包括元神
			int qiling = 0;
			int eq_baoshi = 0; // 装备宝石
			int eq_qiling = 0; // 装备器灵
			int gxbaoshi = 0;
			int xqbaoshi = 0;
			int xiazi_nums = 0;	//匣子数
			int xiazi_baoshi_nums = 0;	//匣子里宝石数
			SimpleEntityManager<ArticleEntity> am = SimpleEntityManagerFactory.getSimpleEntityManager(ArticleEntity.class);
			long totalCount = am.count();
			{
				SimpleEntityManager<Player> manager = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
				long count = manager.count();
				long start = 1;
				long end = 1;
				long pagSize = 2000;
				while (count > 0) {
					end = start + pagSize;
					List<Player> players = manager.query(Player.class, "", "", start, end);
					Map<Long, List<Long>> allEquiptIds = new HashMap<Long, List<Long>>();
					// List<Long> allEquiptIds = new ArrayList<Long>();
					if (players != null) {
						// 装备收集会出问题，放在单独收集
						for (Player p : players) {
							try {
								if (playerIds.contains(p.getId())) {
									continue;
								}
								if (p.getSouls() != null) {
									List<Long> allArticleEntityIds = new ArrayList<Long>();
									int tempZb = 0;
									for (int i = 0; i < p.getSouls().length; i++) {
										try {
											Soul soul = p.getSouls()[i];
											if (soul == null || soul.getEc() == null) {
												continue;
											}
											if (logger.isWarnEnabled()) {
												logger.warn("[收集前] [" + p.getName() + "] [soul:" + soul.getSoulType() + "] [ECS:" + Arrays.toString(soul.getEc().getEquipmentIds()) + "] [收集到装备数:" + tempZb + "]");
											}
											for (int j = 0; j < soul.getEc().getEquipmentIds().length; j++) {
												if (soul.getEc().getEquipmentIds()[j] > 0) {
													allArticleEntityIds.add(soul.getEc().getEquipmentIds()[j]);
													// allEquiptIds.add(soul.getEc().getEquipmentIds()[j]);
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [装备] [玩家:" + p.getName() + "] [物品id:" + soul.getEc().getEquipmentIds()[j] + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
													zhuangbei++;
													tempZb++;
												}
											}
											if (logger.isWarnEnabled()) {
												logger.warn("[收集后] [" + p.getName() + "] [soul:" + soul.getSoulType() + "] [ECS:" + Arrays.toString(soul.getEc().getEquipmentIds()) + "] [收集到装备数:" + tempZb + "]");
											}
										} catch (Exception e) {
											logger.warn("[异常2]", e);
										}
									}
									allEquiptIds.put(p.getId(), allArticleEntityIds);
									synchronized (queue) {
										articleCollectSuccNum += allArticleEntityIds.size();
										queue.addAll(allArticleEntityIds);
									}
								}
							} catch (Exception e) {
								logger.warn("[异常]", e);
							}
						}

						for (Player p : players) {
							try {
								if (playerIds.contains(p.getId())) {
									continue;
								}
								effectPlayerNums++;
								List<Long> allArticleEntityIds = new ArrayList<Long>();
								int pFbb = 0;
								int pBb = 0;
								int pCk = 0;
								int pZb = 0;
								int pQl = 0;
								int pXiaZi = 0;
								int pXiaZiBaoShi = 0;
								// 千层塔
								{
									now = System.currentTimeMillis();
									QianCengTa_Ta q = QianCengTaManager.getInstance().getTaForUnitedServer(p.getId());
									int entityNum = 0;
									if (q != null) {
										for (int i = 0; i < q.getDaoList().size(); i++) {
											QianCengTa_Dao dao = q.getDaoList().get(i);
											for (int j = 0; j < dao.getCengList().size(); j++) {
												QianCengTa_Ceng ceng = dao.getCengList().get(j);
												for (int k = 0; k < ceng.getRewards().size(); k++) {
													if (ceng.getRewards().get(k).getEntityId() > 0) {
														allArticleEntityIds.add(ceng.getRewards().get(k).getEntityId());
														if (logger.isDebugEnabled()) {
															logger.debug("[收集物品] [千层塔] [玩家:" + p.getName() + "] [物品id:" + ceng.getRewards().get(k).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
														}
														entityNum++;
													}
												}
											}
										}
										logger.warn("[合服收集千层塔奖励集] [结束] [***********************************] [账号:{}] [玩家角色:{}] [id:{}] [千层塔物品数:{}] [物品总数:{}] [cost:{}]", new Object[] { p.getUsername(), p.getName(), p.getId(), entityNum, allArticleEntityIds.size(), (System.currentTimeMillis() - now) });
									}
								}

								// 坐骑装备
								{
									int entityNum = 0;
									now = System.currentTimeMillis();
									for (Soul soul : p.getSouls()) {
										if (soul == null) {
											continue;
										}
										for (long horseId : soul.getHorseArr()) {
											Horse horse = HorseManager.em.find(horseId);
											if (horse != null && !horse.isFly()) {
												long[] horseEquIDs = horse.getEquipmentColumn().getEquipmentIds();
												for (long i : horseEquIDs) {
													if (i > 0) {
														allArticleEntityIds.add(i);
														if (logger.isDebugEnabled()) {
															logger.debug("[收集物品] [坐骑] [玩家:" + p.getName() + "] [物品id:" + i + "] [收集总数:" + allArticleEntityIds.size() + "]");
														}
														entityNum++;
													}
												}
											}
										}
									}
									if (logger.isInfoEnabled()) {
										logger.info("[合服收集坐骑装备id集] [结束] [***********************************] [账号:{}] [玩家角色:{}] [id:{}] [坐骑装备数量:{}] [物品总数:{}] [cost:{}ms]", new Object[] { p.getUsername(), p.getName(), p.getId(), entityNum, allArticleEntityIds.size(), (System.currentTimeMillis() - now) });
									}
								}
								
								// 家族
								{
									int entityNum = 0;
									now = System.currentTimeMillis();
									Jiazu jiazu = JiazuManager.getInstance().getJiazu(p.getJiazuId());
									if (jiazu != null && jiazu.getWareHouse() != null) {
										for (int i = 0; i < jiazu.getWareHouse().size(); i++) {
											if (jiazu.getWareHouse().get(i).getEntityId() > 0) {
												allArticleEntityIds.add(jiazu.getWareHouse().get(i).getEntityId());
												if (logger.isDebugEnabled()) {
													logger.debug("[收集物品] [家族] [玩家:" + p.getName() + "] [物品id:" + jiazu.getWareHouse().get(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
												}
												entityNum++;
											}
										}
									}
									logger.warn("[合服收集家族仓库物品id集] [结束] [***********************************] [账号:{}] [玩家角色:{}] [id:{}] [家族仓库物品数量:{}] [物品总数:{}] [cost:{}ms]", new Object[] { p.getUsername(), p.getName(), p.getId(), entityNum, allArticleEntityIds.size(), (System.currentTimeMillis() - now) });
								}

								// 防暴包
								if (p.getKnapsack_fangbao() != null) {
									int entityNum = 0;
									now = System.currentTimeMillis();
									for (int i = 0; i < p.getKnapsack_fangbao().size(); i++) {
										try {
											if (p.getKnapsack_fangbao().getCell(i) != null) {
												if (p.getKnapsack_fangbao().getCell(i).getEntityId() > 0) {
													allArticleEntityIds.add(p.getKnapsack_fangbao().getCell(i).getEntityId());
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [防爆包] [玩家:" + p.getName() + "] [物品id:" + p.getKnapsack_fangbao().getCell(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
													fangbaobao++;
													pFbb++;
												}
											} else {
												logger.warn("[从玩家身上取出物品] [错误] [cell为null] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]");
											}
											if (logger.isInfoEnabled()) {
												logger.info("[合服收集防爆包物品id集] [结束] [***********************************] [账号:{}] [玩家角色:{}] [id:{}] [家族仓库物品数量:{}] [物品总数:{}] [cost:{}ms]", new Object[] { p.getUsername(), p.getName(), p.getId(), entityNum, allArticleEntityIds.size(), (System.currentTimeMillis() - now) });
											}
										} catch (Exception e) {
											logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
											ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [索引:" + i + "]", e);
										}
									}
								}

								// 背包
								if (p.getKnapsack_common() != null) {
									for (int i = 0; i < p.getKnapsack_common().size(); i++) {
										try {
											if (p.getKnapsack_common().getCell(i) != null) {
												if (p.getKnapsack_common().getCell(i).getEntityId() > 0) {
													allArticleEntityIds.add(p.getKnapsack_common().getCell(i).getEntityId());
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [背包] [玩家:" + p.getName() + "] [物品id:" + p.getKnapsack_common().getCell(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
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

								// 宠物背包
								if (p.getPetKnapsack() != null) {
									for (int i = 0; i < p.getPetKnapsack().size(); i++) {
										try {
											if (p.getPetKnapsack().getCell(i) != null) {
												if (p.getPetKnapsack().getCell(i).getEntityId() > 0) {
													allArticleEntityIds.add(p.getPetKnapsack().getCell(i).getEntityId());
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [宠物背包] [玩家:" + p.getName() + "] [物品id:" + p.getPetKnapsack().getCell(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
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
								// 仓库
								if (p.getKnapsacks_cangku() != null) {
									for (int i = 0; i < p.getKnapsacks_cangku().size(); i++) {
										try {
											if (p.getKnapsacks_cangku().getCell(i) != null) {
												if (p.getKnapsacks_cangku().getCell(i).getEntityId() > 0) {
													allArticleEntityIds.add(p.getKnapsacks_cangku().getCell(i).getEntityId());
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [仓库] [玩家:" + p.getName() + "] [物品id:" + p.getKnapsacks_cangku().getCell(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
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

								// 器灵
								if (p.getKnapsacks_QiLing() != null) {
									for (int i = 0; i < p.getKnapsacks_QiLing().size(); i++) {
										try {
											if (p.getKnapsacks_QiLing().getCell(i) != null) {
												if (p.getKnapsacks_QiLing().getCell(i).getEntityId() > 0) {
													allArticleEntityIds.add(p.getKnapsacks_QiLing().getCell(i).getEntityId());
													if (logger.isDebugEnabled()) {
														logger.debug("[收集物品] [器灵] [玩家:" + p.getName() + "] [物品id:" + p.getKnapsacks_QiLing().getCell(i).getEntityId() + "] [收集总数:" + allArticleEntityIds.size() + "]");
													}
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

								{// 装备器灵，宝石
									int jianchaNum = 0;
									ArrayList<Long> entityArrays = new ArrayList<Long>();
									entityArrays.addAll(allArticleEntityIds);
									List<Long> tempelist = allEquiptIds.get(p.getId());
									if (tempelist != null && tempelist.size() > 0) {
										entityArrays.addAll(tempelist);
									}
									Collections.sort(entityArrays);
									Iterator<Long> iIds = entityArrays.iterator();
									while (true) {
										int errnum = 0;
										try {
											ArrayList<Long> id50 = new ArrayList<Long>();
											int findNum = 2000;
											boolean isOver = false;
											for (;;) {
												try {
													// 这里因为添加了合服的服务器 而合服的服务器中是没有数据的 所以需要预先加判断
													if (!iIds.hasNext()) {
														isOver = true;
														break;
													}
													if (iIds.hasNext()) {
														Long id = iIds.next();
														id50.add(id);
														findNum--;
														jianchaNum++;
														if (findNum <= 0) {
															break;
														}
													} else {
														logger.warn("[获取物品] [集合数量为0] [" + entityArrays.size() + "] [" + iIds.hasNext() + "]");
														isOver = true;
														break;
													}
												} catch (Exception e) {
													errnum++;
													if (errnum > 1000) {
														if (errnum > 50000) {
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
											List<ArticleEntity> entitys = am.query(ArticleEntity.class, sq, "", 1, id50.size() + 1);
											if (logger.isInfoEnabled()) {
												logger.info("[id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "/" + entityArrays.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
											}
											for (ArticleEntity entity : entitys) {
												if (entity instanceof EquipmentEntity) {
													EquipmentEntity eq = (EquipmentEntity) entity;
													for (int i = 0; i < eq.getInlayArticleIds().length; i++) {
														if (eq.getInlayArticleIds()[i] > 0) {
															allArticleEntityIds.add(eq.getInlayArticleIds()[i]);
															eq_baoshi++;
															
															//宝石匣子,翅膀中不能放入匣子
															BaoShiXiaZiData xiaZiEntity = ArticleEntityManager.baoShiXiLianEM.find(eq.getInlayArticleIds()[i]);
															if(xiaZiEntity != null && xiaZiEntity.getIds() != null){
																xiazi_nums ++;
																pXiaZi++;
																for(long id : xiaZiEntity.getIds()){
																	if(id > 0){
																		pXiaZiBaoShi++;
																		xiazi_baoshi_nums++;
																		allArticleEntityIds.add(id);
																	}
																}
															}
														}
													}
													for (int i = 0; i < eq.getInlayQiLingArticleIds().length; i++) {
														if (eq.getInlayQiLingArticleIds()[i] > 0) {
															allArticleEntityIds.add(eq.getInlayQiLingArticleIds()[i]);
															eq_qiling++;
														}
													}
												}else {
													if(entity != null){
														Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
														if(a != null && a instanceof InlayArticle){
															InlayArticle ia = (InlayArticle)a;
															if(ia.getInlayType() > 1){
																BaoShiXiaZiData xiaZiEntity = ArticleEntityManager.baoShiXiLianEM.find(entity.getId());
																if(xiaZiEntity != null && xiaZiEntity.getIds() != null){
																	xiazi_nums ++;
																	pXiaZi++;
																	for(long id : xiaZiEntity.getIds()){
																		if(id > 0){
																			pXiaZiBaoShi++;
																			xiazi_baoshi_nums++;
																			allArticleEntityIds.add(id);
																		}
																	}
																}
															}
														}
													}
												}
											}

											if (isOver) {
												break;
											}
										} catch (Exception e) {
											logger.error("collectArticleEntityData出错A:", e);
											ExcepTionlogger.error("collectArticleEntityData出错A:", e);
											break;
										}
									}
									if (logger.isWarnEnabled()) {
										logger.warn("[检查玩家装备宝石器灵] [结束] [物品id总数量：" + allArticleEntityIds.size() + "] [已检查:" + jianchaNum + "] [宝石:" + eq_baoshi + "] [器灵:" + eq_qiling + "] [匣子数:"+pXiaZi+"] [匣子宝石数："+pXiaZiBaoShi+"]");
									}
								}
								
								{	//兽魂仓库和兽魂面板
									if (p.getShouhunKnap() != null ) {				//兽魂仓库
										for (int i=0; i<p.getShouhunKnap().length; i++) {
											if (p.getShouhunKnap()[i] > 0) {
												allArticleEntityIds.add(p.getShouhunKnap()[i]);
											}
										}
									}
									SimpleEntityManager<HuntLifeEntity> heManager = SimpleEntityManagerFactory.getSimpleEntityManager(HuntLifeEntity.class);
									HuntLifeEntity he = heManager.find(p.getId());
									if (he != null && he.getHuntDatas() != null) {
										for (long hdId : he.getHuntDatas()) {
											if (hdId > 0) {
												allArticleEntityIds.add(hdId);
											}
										}
									}
								}
								try {		//仙府
									Cave cave = FaeryManager.caveEm.find(p.getId());
									if (cave != null) {
										long[] pps = cave.getPethouse().getStorePets();
										for (int i=0; i<pps.length; i++) {
											if (pps[i] > 0) {
												allArticleEntityIds.add(pps[i]);
											}
										}
										PetHookInfo[] his = cave.getPethouse().getHookInfos();
										if (his != null) {
											for (int i=0; i<his.length; i++) {
												if (his[i].getArticleId() > 0) {
													allArticleEntityIds.add(his[i].getArticleId());
												}
											}
										}
									}
								} catch (Exception e) {
									logger.warn("[数据转移] [收集玩家仙府宠物物品id] [异常] [" + p.getLogString() + "]", e);
								}
								
								am.releaseReference();

								synchronized (queue) {
									articleCollectSuccNum += allArticleEntityIds.size();
									queue.addAll(allArticleEntityIds);
								}
								if (queue.size() >= maxArticleIdNum) {
									Thread.sleep(1000 * 60);
								}
								logger.warn("[从玩家身上取出物品结束] [服务器收集总数:" + articleCollectSuccNum + "] [玩家总物品数:" + allArticleEntityIds.size() + "] [" + p.getId() + "] [" + p.getName() + "] [防暴包:" + pFbb + "] [背包:" + pBb + "] [仓库:" + pCk + "] [装备:" + pZb + "] [器灵:" + pQl + "] [匣子数:"+pXiaZi+"] [匣子宝石数："+pXiaZiBaoShi+"]");
							} catch (Exception e) {
								logger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "]", e);
								ExcepTionlogger.error("[从玩家身上取出物品] [错误] [出现未知异常] [" + p.getId() + "] [" + p.getName() + "]", e);
							}

						}
					}
					start += pagSize;
					count -= pagSize;
					logger.warn("[collectArticleEntityData->从player中取出entityID] [start:{}] [end:{}] [left:{}] [elap:{}ms]", new Object[] { start - pagSize, end, count, (System.currentTimeMillis() - now) });
				}

			}

			articleCollectThreadIsOver = true;
			if (logger.isWarnEnabled()) {
				logger.warn("[玩家背包物品收集] [结束] [无效玩家总数:" + playerIds.size() + "] [有效玩家总数:" + effectPlayerNums + "] [背包=" + beibao + "] [仓库=" + cangku + "] [防暴=" + fangbaobao + "] [装备=" + zhuangbei + "] [器灵=" + qiling + "] [装备宝石=" + eq_baoshi + "] [装备器灵=" + eq_qiling + "] [物品收集总数:" + articleCollectSuccNum + "] [原库总数:" + totalCount + "] [翅膀光效宝石:" + gxbaoshi + "] [翅膀镶嵌宝石:" + xqbaoshi + "] [匣子数:"+xiazi_nums+"] [匣子宝石数："+xiazi_baoshi_nums+"] [物品收集总耗时:" + (System.currentTimeMillis() - starttime) + "ms]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 物品移动
	 */
	public void moveArticleEntityData(Queue<Long> queue) {
		try {
			long starttime = System.currentTimeMillis();
			Field versionField = getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
			Field idField = getFieldByAnnotation(ArticleEntity.class, SimpleId.class);
			if (versionField == null) {
				throw new IllegalStateException("ArticleEntity's versionField not found");
			}
			SimpleEntityManager<ArticleEntity> aeEntityManager = factory.getSimpleEntityManager(ArticleEntity.class);
			int articleCollectNums = 0;
			int articleMoveSuccNum = 0; // 物品移动成功总数
			idField.setAccessible(true);
			versionField.setAccessible(true);
			while (!articleCollectThreadIsOver || queue.size() > 0) {
				HashSet<Long> allArticleEntityIds = new HashSet<Long>();
				synchronized (queue) {
					while (queue.size() > 0) {
						long id = queue.poll();
						articleCollectNums++;
						if (!allArticleEntityIds.contains(id)) {
							allArticleEntityIds.add(id);
						}
					}
				}
				if (logger.isInfoEnabled()) {
					logger.info("[allArticleEntityIds:" + allArticleEntityIds.size() + "]");
				}
				startTime = SystemTime.currentTimeMillis();
				List<ArticleEntity> noticeSimpleJpa = new ArrayList<ArticleEntity>();// 通知底层的数据
				if (allArticleEntityIds != null && allArticleEntityIds.size() > 0) {
					ArrayList<Long> entityArrays = new ArrayList<Long>();
					entityArrays.addAll(allArticleEntityIds);
					int num = allArticleEntityIds.size();

					Collections.sort(entityArrays);
					SimpleEntityManager<ArticleEntity> manager = ArticleEntityManager.getInstance().em;
					manager.setReadOnly(true);
					Iterator<Long> eIt = entityArrays.iterator();
					int daoruNum = 0;
					if (entityArrays.size() <= 0) {
						continue;
					}

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
									if (DataMoveManager.logger.isInfoEnabled()) {
										DataMoveManager.logger.info("[移除已经存储的物品] [id:" + id + "]");
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
						int findNum = tempAeNum;
						for (;;) {
							if (!eIt.hasNext()) {
								isOver = true;
								break;
							}
							Long id = eIt.next();
							id50.add(id);
							findNum--;
							daoruNum++;
							if (findNum <= 0) {
								break;
							}
						}
						if (logger.isInfoEnabled()) {
							logger.info("[find id50:" + id50.size() + "]");
						}
						if  (id50.size() == 0) {
							break;
						}
						String sq = "";
						for (int i = 0; i < id50.size(); i++) {
							if (i == id50.size() - 1) {
								sq += "id=" + id50.get(i);
							} else {
								sq += "id=" + id50.get(i) + " or ";
							}
						}
						if (logger.isInfoEnabled()) {
							logger.info("[查询物品SQL] " + sq);
						}
						List<ArticleEntity> entitys = manager.query(ArticleEntity.class, sq, "", 1, id50.size() + 1);// //TODO]
						List<Long> saveids = new ArrayList<Long>();
						if (logger.isInfoEnabled()) {
							logger.info(" [isOver:" + isOver + "] [findNum:" + findNum + "] [id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
						}
						for (ArticleEntity entity : entitys) {
							if (saveids.contains(entity.getId())) {
								DataMoveManager.logger.warn("[通知底层存储物品] [相同id] [id:" + entity.getId() + "] [name:" + entity.getArticleName() + "]");
								continue;
							}
							if (!lists.contains(entity.getId())) {
								versionField.set(entity, 0);
								aeEntityManager.notifyNewObject(entity);
								articleMoveSuccNum++;
								lists.add(entity.getId());
								saveids.add(entity.getId());
								if (DataMoveManager.logger.isDebugEnabled()) {
									DataMoveManager.logger.debug("[通知底层存储物品] [notifyNewObject] [id:" + entity.getId() + "] [name:" + entity.getArticleName() + "]");
								}
								noticeSimpleJpa.add(entity);
							} 
						}
						if (logger.isInfoEnabled()) {
							logger.info("[导入articleEntity] [articleCollectThreadIsOver:" + articleCollectThreadIsOver + "] [isOver:" + isOver + "] [总数:" + num + "] [已导入:" + daoruNum + "]");
						}
						if (isOver) {
							break;
						}
					}
				}
				int loopCount = 0;
				while (noticeSimpleJpa.size() > 0) {// 通知底层完成，但是还没存储完毕
					try {
						loopCount++;
						long currentTime = System.currentTimeMillis();
						boolean isCheck = false;
						Thread.sleep(10000);
						Iterator<ArticleEntity> itor = noticeSimpleJpa.iterator();
						while (itor.hasNext()) {
							ArticleEntity articleEntity = itor.next();
							int version = versionField.getInt(articleEntity);
							long id = idField.getLong(articleEntity);
							if (version == 0) {
								if (logger.isInfoEnabled()) {
									logger.info("[物品移动测试] [noticeSimpleJpa:" + noticeSimpleJpa.size() + "] [version:" + version + "] [aename:" + (articleEntity == null ? "" : articleEntity.getArticleName()) + "] [id:" + id + "]");
								}
							}
							if (version != 0) {
								itor.remove();
								if (DataMoveManager.logger.isInfoEnabled()) {
									DataMoveManager.logger.info("[全部通知底层完成] [移除已经存储的物品] [aename:" + (articleEntity == null ? "null" : articleEntity.getArticleName()) + "] [id:" + id + "]");
								}
							} else if (currentTime > lastCheckArticleTime + checkVersionTime) {
								if (!isCheck) {
									isCheck = true;
								}
//								long id = idField.getLong(t);
								long ct = aeEntityManager.count(ArticleEntity.class, "id="+id);
								if (ct > 0) {
									itor.remove();
								} 
							}
							if (version == 0 && loopCount > 重新存储) {
								aeEntityManager.flush(articleEntity);
								itor.remove();
							}
						}
						if (loopCount > 重新存储) {
							loopCount = 0;
							
						}
						if (isCheck) {
							lastCheckArticleTime = currentTime;
							isCheck = false;
						}
					} catch (Exception e) {
						logger.warn("[物品移动通知底层] [异常] [{}] [noticeSimpleJpa:{}]", e, noticeSimpleJpa.size());
					}
				}
				if (allArticleEntityIds != null && allArticleEntityIds.size() > 0) {
					logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [合并articleEntity] [结束] [数量:" + (allArticleEntityIds == null ? "null" : allArticleEntityIds.size()) + "] [total耗时:" + (SystemTime.currentTimeMillis() - startTime) + "]");
				}
			}

			logger.warn("[移动玩家物品数据] [成功] [队列总数:" + articleCollectNums + "] [存储成功总数:{}] [物品移动总耗时:{}]", new Object[] { articleMoveSuccNum, (System.currentTimeMillis() - starttime) });
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[移动玩家物品数据] [异常:{}]", new Object[] { e });
		}
	}
	
	public static int 重新存储 = 5;
	
	public long lastCheckArticleTime = 0;
	public long checkVersionTime = 3 * 60 * 1000L;
	
	public static int tempAeNum = 1000;

	/**
	 * 宠物相关
	 */
	public void movePetData() {
		long starttime = System.currentTimeMillis();
		HashSet<Long> allIds = new HashSet<Long>();
		try {
			{// 收集
				logger.warn("[合服移动宠物entityid集] [开始] [***********************************]");
				SimpleEntityManager<Pet> em_pet = PetManager.em;
				int num = 0;
				try {
					long count = em_pet.count(Pet.class, " delete='F' or (delete='T' and rarity >= 2)");
					long start = 1;
					long end = 1;
					long pagSize = 10000;
					while (count > 0) {
						now = System.currentTimeMillis();
						end = start + pagSize;
						List<PetProp> queryFields = em_pet.queryFields(PetProp.class, em_pet.queryIds(Pet.class, " delete='F' or (delete='T' and rarity >= 2)", "id", start, end));
						for (PetProp pp : queryFields) {
							if (pp.getPetPropsId() > 0) {
								allIds.add(pp.getPetPropsId());
								num++;
							}
						}
						start += pagSize;
						count -= pagSize;
						if (logger.isInfoEnabled()) {
							logger.info("[collectArticleEntityData->从宠物中取出entityID] [start:{}] [end:{}] [left:{}] [num:{}] [cost:{}ms]", new Object[] { start - pagSize, end, count, num, (System.currentTimeMillis() - now) });
						}
					}
					logger.warn("[合服移动宠物entityid集] [结束] [***********************************] [宠物entity数:{}] [物品总数:{}] [宠物收集总耗时:{}]", new Object[] { num, allIds.size(), (System.currentTimeMillis() - starttime) });
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("[合服移动宠物entityid集] [异常] [{}] [***********************************]", e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		{// 移动
			try {
				HashSet<Long> allArticleEntityIds = new HashSet<Long>();
				allArticleEntityIds.addAll(allIds);
				starttime = SystemTime.currentTimeMillis();
				List<ArticleEntity> noticeSimpleJpa = new ArrayList<ArticleEntity>();// 通知底层的数据
				Field versionField = getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
				Field idField = getFieldByAnnotation(ArticleEntity.class, SimpleId.class);
				if (versionField == null) {
					throw new IllegalStateException("ArticleEntity's versionField not found");
				}
				idField.setAccessible(true);
				int moveSuccNums = 0;
				versionField.setAccessible(true);
				if (allArticleEntityIds != null && allArticleEntityIds.size() > 0) {
					ArrayList<Long> entityArrays = new ArrayList<Long>();
					entityArrays.addAll(allArticleEntityIds);
					int num = allArticleEntityIds.size();

					Collections.sort(entityArrays);
					SimpleEntityManager<ArticleEntity> manager = ArticleEntityManager.getInstance().em;
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
									if (DataMoveManager.logger.isInfoEnabled()) {
										DataMoveManager.logger.info("[移除已经存储的物品] [id:" + id + "]");
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
							if (!eIt.hasNext()) {
								isOver = true;
								break;
							}
							Long id = eIt.next();
							id50.add(id);
							findNum--;
							daoruNum++;
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
						List<ArticleEntity> entitys = manager.query(ArticleEntity.class, sq, " LastUpdateTime >= 0 ", 1, id50.size() + 1);// //TODO
						if (logger.isInfoEnabled()) {
							logger.info("[id个数:" + id50.size() + "] [对象个数:" + entitys.size() + "] [" + (id50.size() == entitys.size() ? "一样" : "不一样") + "]");
						}
						for (ArticleEntity entity : entitys) {
							versionField.set(entity, 0);
							factory.getSimpleEntityManager(ArticleEntity.class).notifyNewObject(entity);
							moveSuccNums++;
							long id = idField.getLong(entity);
							if (DataMoveManager.logger.isInfoEnabled()) {
								DataMoveManager.logger.info("[通知底层存储物品] [notifyNewObject] [id:" + id + "] [name:" + entity.getArticleName() + "]");
							}
							noticeSimpleJpa.add(entity);
						}
						if (logger.isInfoEnabled()) {
							logger.info("[导入articleEntity] [总数:" + num + "] [已导入:" + daoruNum + "]");
						}
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
							if (DataMoveManager.logger.isInfoEnabled()) {
								DataMoveManager.logger.info("[全部通知底层完成] [移除已经存储的物品] [id:" + id + "]");
							}
						}
					}
				}
				logger.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@[合服流程] [合并宠物articleEntity] [结束] [收集总数:" + (allArticleEntityIds == null ? "0" : allArticleEntityIds.size()) + "] [成功合并数:" + moveSuccNums + "] [合并宠物总耗时:" + (SystemTime.currentTimeMillis() - starttime) + "]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Long> jiazuEmptyCellIds = new ArrayList<Long>();

	public void checkJiazuCell() {
		try {
			SimpleEntityManager<Jiazu> jiazuEntityManager = this.getFactory().getSimpleEntityManager(Jiazu.class);
			SimpleEntityManager<ArticleEntity> articleEntityManager = this.getFactory().getSimpleEntityManager(ArticleEntity.class);
			long[] jiazuIds = jiazuEntityManager.queryIds(Jiazu.class, "");
			for (long jiazuId : jiazuIds) {
				Jiazu jiazu = jiazuEntityManager.find(jiazuId);
				if (jiazu.getWareHouse() != null) {
					for (Cell cl : jiazu.getWareHouse()) {
						if (cl.getEntityId() > 0) {
							ArticleEntity ae = articleEntityManager.find(cl.getEntityId());
							if (ae == null) {
								jiazuEmptyCellIds.add(cl.getEntityId());
								logger.warn("[查找空格子] [家族仓库] [" + jiazu.getLogString() + "] [aeId:" + cl.getEntityId() + "]");
							} else if (ae instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ae;
								this.checkEquiptMent(articleEntityManager, ee, null);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[执行异常] [checkJiazuCell]", e);
		}
	}

	public List<Long> wingEmptyCellIds = new ArrayList<Long>();


	public List<Long> playerCellEmptyCellIds = new ArrayList<Long>();

	public volatile List<Long> emptyIrIds = new ArrayList<Long>();

	/**
	 * 检测玩家是否有空格子，扫数据库中所有数据
	 */
	public void checkPlayerCells() {
		try {
			SimpleEntityManager<Player> playerEntityManager = this.getFactory().getSimpleEntityManager(Player.class);
			SimpleEntityManager<ArticleEntity> articleEntityManager = this.getFactory().getSimpleEntityManager(ArticleEntity.class);
			SimpleEntityManager<Horse> horseEntityManager = this.getFactory().getSimpleEntityManager(Horse.class);
			long[] pIds = playerEntityManager.queryIds(Player.class, "");
			logger.warn("[新数据库中player总数:" + pIds.length + "]");
			for (int i = 0; i < pIds.length; i++) {
				Player player = playerEntityManager.find(pIds[i]);
				Knapsack[] knaps = player.getKnapsacks_common(); // 普通背包
				for (Knapsack kp : knaps) {
					for (Cell cl : kp.getCells()) {
						if (cl.getEntityId() > 0) {
							ArticleEntity ae = articleEntityManager.find(cl.getEntityId());
							if (ae == null) {
								if (!playerCellEmptyCellIds.contains(cl.getEntityId())) {
									playerCellEmptyCellIds.add(cl.getEntityId());
									logger.warn("[查找玩家空格子] [getKnapsacks_common] [" + player.getLogString() + "] [aeId:" + cl.getEntityId() + "]");
								}
							} else if (ae instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ae;
								this.checkEquiptMent(articleEntityManager, ee, player);
							}
						}
					}
				}
				Knapsack kpF = player.getKnapsack_fangbao(); // 防爆包
				if (kpF != null) {
					for (Cell cl : kpF.getCells()) {
						if (cl.getEntityId() > 0) {
							ArticleEntity ae = articleEntityManager.find(cl.getEntityId());
							if (ae == null) {
								if (!playerCellEmptyCellIds.contains(cl.getEntityId())) {
									playerCellEmptyCellIds.add(cl.getEntityId());
									logger.warn("[查找玩家空格子] [getKnapsack_fangbao] [" + player.getLogString() + "] [aeId:" + cl.getEntityId() + "]");
								}
							} else if (ae instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ae;
								this.checkEquiptMent(articleEntityManager, ee, player);
							}
						}
					}
				}
				Knapsack ck = player.getKnapsacks_cangku(); // 仓库
				if (ck != null) {
					for (Cell cl : ck.getCells()) {
						if (cl.getEntityId() > 0) {
							ArticleEntity ae = articleEntityManager.find(cl.getEntityId());
							if (ae == null) {
								if (!playerCellEmptyCellIds.contains(cl.getEntityId())) {
									playerCellEmptyCellIds.add(cl.getEntityId());
									logger.warn("[查找玩家空格子] [getKnapsacks_cangku] [" + player.getLogString() + "] [aeId:" + cl.getEntityId() + "]");
								}
							} else if (ae instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ae;
								this.checkEquiptMent(articleEntityManager, ee, player);
							}
						}
					}
				}

				for (Soul soul : player.getSouls()) {
					for (long id : soul.getEc().getEquipmentIds()) {
						if (id > 0) {
							ArticleEntity ae = articleEntityManager.find(id);
							if (ae == null) {
								if (!playerCellEmptyCellIds.contains(id)) {
									playerCellEmptyCellIds.add(id);
									logger.warn("[查找玩家空格子] [玩家装备] [" + player.getLogString() + "] [aeId:" + id + "]");
								}
							} else if (ae instanceof EquipmentEntity) {
								EquipmentEntity ee = (EquipmentEntity) ae;
								this.checkEquiptMent(articleEntityManager, ee, player);
							}
						}
					}
					for (long horseId : soul.getHorseArr()) {
						Horse h = horseEntityManager.find(horseId);
						if (h == null) {
							logger.warn("[查找玩家空格子] [坐骑为空] [" + player.getLogString() + "] [aeId:" + horseId + "]");
						} else {
							for (long id : h.getEquipmentIds()) {
								if (id <= 0) {
									continue;
								}
								ArticleEntity ae = articleEntityManager.find(id);
								if (ae == null) {
									if (!playerCellEmptyCellIds.contains(id)) {
										playerCellEmptyCellIds.add(id);
										logger.warn("[查找玩家空格子] [玩家装备] [" + player.getLogString() + "] [aeId:" + id + "]");
									}
								} else if (ae instanceof EquipmentEntity) {
									EquipmentEntity ee = (EquipmentEntity) ae;
									this.checkEquiptMent(articleEntityManager, ee, player);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[执行异常] [checkPlayerCells]", e);
		}
	}

	/**
	 * 检测装备
	 * @param am
	 * @param ee
	 * @param player
	 * @throws Exception
	 */
	private void checkEquiptMent(SimpleEntityManager<ArticleEntity> am, EquipmentEntity ee, Player player) {
		try {
			if (ee.getInlayArticleIds() != null && ee.getInlayArticleIds().length > 0) {
				for (long id : ee.getInlayArticleIds()) {
					if (id > 0) {
						ArticleEntity ae = am.find(id);
						if (ae == null && !emptyIrIds.contains(id)) {
							emptyIrIds.add(id);
							if (player != null) {
								logger.warn("[查找玩家空格子] [装备宝石] [" + player.getLogString() + "] [aeId:" + id + "]");
							}
							logger.warn("[查找玩家空格子] [装备宝石] [aeId:" + id + "]");
						}
					}
				}
				for (long id : ee.getInlayQiLingArticleIds()) {
					if (id > 0) {
						ArticleEntity ae = am.find(id);
						if (ae == null && !emptyIrIds.contains(id)) {
							emptyIrIds.add(id);
							if (player != null) {
								logger.warn("[查找玩家空格子] [装备器灵] [" + player.getLogString() + "] [aeId:" + id + "]");
							}
							logger.warn("[查找玩家空格子] [装备器灵] [aeId:" + id + "]");
						}
					}
				}

			}
		} catch (Exception e) {
			logger.warn("[checkEquiptMent]", e);
		}
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

	@Override
	public String getMConsoleName() {
		// TODO Auto-generated method stub
		return "合服前数据转移";
	}

	@Override
	public String getMConsoleDescription() {
		// TODO Auto-generated method stub
		return "合服前数据转移管理";
	}

	public DefaultSimpleEntityManagerFactory getFactory() {
		return factory;
	}

	public void setFactory(DefaultSimpleEntityManagerFactory factory) {
		this.factory = factory;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public Set<Long> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(Set<Long> playerIds) {
		this.playerIds = playerIds;
	}

	public List<DataMoveThread> getCurrentRunThreads() {
		return currentRunThreads;
	}

	public void setCurrentRunThreads(List<DataMoveThread> currentRunThreads) {
		this.currentRunThreads = currentRunThreads;
	}

	public Set<Long> getNeedKeepPlayer() {
		return needKeepPlayer;
	}

	public void setNeedKeepPlayer(Set<Long> needKeepPlayer) {
		this.needKeepPlayer = needKeepPlayer;
	}

	interface PetProp {
		long getPetPropsId();
	}
}
