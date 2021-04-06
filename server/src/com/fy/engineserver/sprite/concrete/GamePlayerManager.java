package com.fy.engineserver.sprite.concrete;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.client.BossClientService;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.event.LeaveGameEvent;
import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.dajing.DajingGroup;
import com.fy.engineserver.dajing.DajingStudio;
import com.fy.engineserver.dajing.DajingStudioManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NEW_MIESHI_UPDATE_PLAYER_INFO;
import com.fy.engineserver.message.REPORT_ONLINENUM_REQ;
import com.fy.engineserver.message.USER_CLIENT_INFO_REQ;
import com.fy.engineserver.newtask.NewDeliverTaskManager;
import com.fy.engineserver.newtask.service.DeliverTaskManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.playerBack.OldPlayerBackManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.SimplePlayer4Load;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.SoulData;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.util.GameStatTool;
import com.fy.engineserver.util.RowData;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.fy.gamegateway.mieshi.resource.manager.PackageManager.Package;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.client.StatClientService;
import com.xuanzhi.stat.model.PlayerUpdateFlow;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.gamestat.AnalyseTool;
import com.xuanzhi.tools.gamestat.GameStatService;
import com.xuanzhi.tools.mail.JavaMailUtils;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;

public class GamePlayerManager extends PlayerManager implements PlayerInOutGameListener, Runnable {

	// static Logger logger = Logger.getLogger(PlayerManager.class.getName());
	public static Logger logger = LoggerFactory.getLogger(PlayerManager.class.getName());

	// 更新周期,s
	protected long updatePeriod;

	protected boolean running = true;
	protected Thread thread;

	protected BossClientService bossClientService;
	protected GameConstants gameConstants;

	public SimpleEntityManager<Player> em;

	public static boolean LimitCreateNewPlayer = false;
	public static List<String> limitCreateServers = new ArrayList<String>();

	public static boolean sendLevelPackage = true;

	{
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			sendLevelPackage = false;
		}
	}
	// 本地缓存，在线玩家
	public Hashtable<Long, Player> idPlayerMap = new Hashtable<Long, Player>();
	public Hashtable<String, Player> namePlayerMap = new Hashtable<String, Player>();

	public Object create_lock = new Object() {
	};
	public HashSet<String> creatingPlayerNameSet = new HashSet<String>();

	private Object load_lock = new Object() {
	};
	private HashMap<Long, Object> loadLockIdMap = new HashMap<Long, Object>();
	private HashMap<String, Object> loadLockNameMap = new HashMap<String, Object>();

	public static boolean isAllowActionStat = false;

	private File playerBasicPropertyValuesFile;

	private File playerPropertyMaxValuesFile;
	/** 战斗公式系数配表 */
	private File combatFValuesFile;

	public File getPlayerBasicPropertyValuesFile() {
		return playerBasicPropertyValuesFile;
	}

	public File getPlayerPropertyMaxValuesFile() {
		return playerPropertyMaxValuesFile;
	}

	public void setPlayerPropertyMaxValuesFile(File playerPropertyMaxValuesFile) {
		this.playerPropertyMaxValuesFile = playerPropertyMaxValuesFile;
	}

	public void setPlayerBasicPropertyValuesFile(File playerBasicPropertyValuesFile) {
		this.playerBasicPropertyValuesFile = playerBasicPropertyValuesFile;
	}

	public void init() throws Exception {
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);

		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		// this.loadOldPlayers(server1);
		// this.loadOldPlayers(server2);

		Thread t = new Thread(new SendOnlinePlayerStatusTask(), "SendOnlinePlayerStatusLogTask");
		t.start();

		initUpdateOnlineNUm();

		self = this;

		各个职业各个级别的基础属性值 = new int[6][][];
		各个职业各个级别的所有战斗属性理论最大数值 = new int[6][][];

		loadFile(playerBasicPropertyValuesFile);

		loadMaxValueFile(playerPropertyMaxValuesFile);

		loadCombatFFile(combatFValuesFile);
		Thread thread = new Thread(this, "playerManager");
		thread.start();

		// 查看服务器中是否有玩家级别大于150
		{
			long count = em.count(Player.class, "level > 149");
			if (count > 0) {
				PlayerManager.开启赐福标记 = true;
			}
		}
		//limitCreateServers.add("飘渺寻仙");

		ServiceStartRecord.startLog(this);
	}

	public void loadCombatFFile(File f) throws Exception {
		try {
			InputStream is = new FileInputStream(f);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(0); // 基础配置
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				try {
					combatFNums.put(ReadFileTool.getInt(row, 0, logger), ReadFileTool.getInt(row, 1, logger));
				} catch (Exception e) {
					throw new Exception(f.getAbsolutePath() + "第" + (i + 1) + "行异常！！", e);
				}
			}
		} catch (Exception e) {
			logger.error("战斗公式等级系数初始化异常", e);
			throw e;
		}
	}

	/**
	 * 读取人物各个职业每个级别的基础属性值
	 * @param file
	 * @throws Exception
	 */
	public void loadFile(File file) throws Exception {
		try {
			if (file != null && file.isFile() && file.exists()) {
				InputStream is = new FileInputStream(file);
				POIFSFileSystem pss = new POIFSFileSystem(is);
				HSSFWorkbook workbook = new HSSFWorkbook(pss);
				for (int i = 0; i <= 4; i++) {
					HSSFSheet sheet = workbook.getSheetAt(i);
					int rows = sheet.getPhysicalNumberOfRows();
					if (rows <= 220) {
						throw new Exception("人物属性级别不足220");
					}
					各个职业各个级别的基础属性值[i + 1] = new int[rows - 1][7];
					for (int j = 1; j < rows; j++) {
						HSSFCell hc = sheet.getRow(j).getCell(1);
						各个职业各个级别的基础属性值[i + 1][j - 1][0] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(2);
						各个职业各个级别的基础属性值[i + 1][j - 1][1] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(3);
						各个职业各个级别的基础属性值[i + 1][j - 1][2] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(4);
						各个职业各个级别的基础属性值[i + 1][j - 1][3] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(5);
						各个职业各个级别的基础属性值[i + 1][j - 1][4] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(6);
						各个职业各个级别的基础属性值[i + 1][j - 1][5] = (int) hc.getNumericCellValue();
						hc = sheet.getRow(j).getCell(7);
						各个职业各个级别的基础属性值[i + 1][j - 1][6] = (int) hc.getNumericCellValue();
					}
				}
			}
		} catch (Exception e) {
			logger.error("各个职业各个级别的基础属性值出错:", e);
			throw e;
		}
	}

	/**
	 * 读取人物各个职业每个级别的理论最大属性值
	 * @param file
	 * @throws Exception
	 */
	public void loadMaxValueFile(File file) throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			for (int i = 0; i <= 4; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				int[][] careerData = new int[rows - 1][];
				for (int r = 1; r < rows; r++) {
					HSSFRow row = sheet.getRow(r);
					if (row != null) {
						int cellNumber = row.getPhysicalNumberOfCells();
						int[] cellValues = new int[cellNumber - 1];
						for (int j = 1; j < cellNumber; j++) {
							HSSFCell cell = row.getCell(j);
							int value = 0;
							try {
								value = Integer.parseInt(cell.getStringCellValue());
							} catch (Exception ex) {
								try {
									value = (int) cell.getNumericCellValue();
								} catch (Exception e) {
									System.out.println("~~~~~~~~~[" + r + "]~~[" + j + "]");
									throw e;
								}
							}
							cellValues[j - 1] = value;
						}
						careerData[r - 1] = cellValues;
					}
				}
				各个职业各个级别的所有战斗属性理论最大数值[i + 1] = careerData;
			}
		}
	}

	public Object getCreate_lock() {
		return create_lock;
	}

	public void setCreate_lock(Object create_lock) {
		this.create_lock = create_lock;
	}

	public void setUpdatePeriod(long updatePeriod) {
		this.updatePeriod = updatePeriod;
	}

	public void setBossClientService(BossClientService bossClientService) {
		this.bossClientService = bossClientService;
	}

	public BossClientService getBossClientService() {
		return bossClientService;
	}

	public void destroy() {

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		em.destroy();

		System.out.println("[Destroy] 调用destroy方法保存所有角色, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) + " ms");
		// logger.warn("[Destroy] 调用destroy方法保存所有角色, 数量(新："+newPlayerList.size()+"，旧："+updatePlayerList.size()+")", start);
		if (logger.isWarnEnabled()) logger.warn("[Destroy] 调用destroy方法保存所有角色, [{}ms]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
	}

	Random random = new Random();

	public static long ANZHI_SDK_PRIZE_END = TimeTool.formatter.varChar19.parse("2018-09-08 00:00:00");

	/*
	 * (non-Javadoc)
	 * @see com.fy.engineserver.sprite.PlayerManager#createPlayer(java.lang.String, java.lang.String, byte, byte, byte, byte, int)
	 */
	public Player createPlayer(String username, String name, byte race, byte sex, byte country, byte politicalCamp, int career) throws Exception {

		name = name.replaceAll("'", "");

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (career <= 0 || career >= CareerManager.careerNames.length) {
			throw new Exception("career is not exist");
		}
		if (country <= 0 || country > CountryManager.国家名称.length) {
			throw new Exception("country is not exist");
		}
		if (username == null || username.length() == 0 || name == null || name.length() == 0) {
			throw new Exception("Username or name can not be null or empty");
		}
		synchronized (create_lock) {
			if (creatingPlayerNameSet.contains(name)) {
				throw new Exception("角色名正在被其他玩家创建");
			}
			creatingPlayerNameSet.add(name);
		}
		if (logger.isDebugEnabled()) logger.debug("[trace_create0] [{}] [{}]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
		try {
			boolean exists = isExists(name);
			if (logger.isDebugEnabled()) logger.debug("[trace_create1] [{}] [{}]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
			if (!exists) {
				Player player = new Player();

				player.setId(em.nextId());

				player.setUsername(username);
				player.setName(name);

				player.setSex(sex);
				player.setCountry(country);
				player.setAvataRace(Constants.races[race]);
				player.setAvataSex(Constants.sexs[sex]);

				player.setSpeed(175);
				player.setLevel(1);
				player.setClassLevel((short) 0);
				player.setBournExp(0);
				CountryManager cm = CountryManager.getInstance();
				String gameName = Constants.bornName[country]; //Constants.mapNameBorn;
//				if (Translate.兽魁.equals(CareerManager.careerNames[career])) {
//					gameName = Constants.mapNameBorn4Shoukui;
//				}
				player.setLastGame(gameName);
				GameManager gm = GameManager.getInstance();
				GameInfo gi = gm.getGameInfo(gameName);
				MapArea[] mas = gi.getMapAreasByName(Translate.出生点);
				MapArea ma = null;
				if (mas != null && mas.length > 0) {
					ma = mas[random.nextInt(mas.length)];
				}
				if (ma != null) {
					player.setX(ma.x + (int) (ma.width * Math.random()));
					player.setY(ma.y + (int) (ma.height * Math.random()));
					if (Game.logger.isInfoEnabled()) Game.logger.info(name + " " + gameName + " " + ma.getName() + " " + player.getX() + "," + player.getY());
				} else {
					player.setX(2288);
					player.setY(1666);
					if (Game.logger.isInfoEnabled()) Game.logger.info(name + " " + gameName + " " + player.getX() + "," + player.getY());
				}
				player.setLastX(player.getX());
				player.setLastY(player.getY());
				player.setHomeMapName(gameName);
				player.setHomeX((int) player.getX());
				player.setHomeY((int) player.getY());
				player.setResurrectionMapName(gameName);
				player.setResurrectionX((int) player.getX());
				player.setResurrectionY((int) player.getY());
				player.setCareer((byte) career);
				player.setCountry((byte) country);
				cm.getCountryMap().get(player.getCountry()).setCount(cm.getCountryMap().get(player.getCountry()).getCount() + 1);

				Knapsack knaps[] = new Knapsack[2];
				for (int i = 0; i < knaps.length; i++) {
					knaps[i] = new Knapsack(player, i == 1 ? 5 : 40);
				}
				player.setKnapsacks_common(knaps);

				Soul soul = new Soul(0);
				soul.setCareerBasicSkillsLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				soul.setBianShenLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				soul.setSkillOneLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				soul.setCareer(player.getCareer());
				player.setSkillTwoLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				player.setNuqiSkillsLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				player.setXinfaLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				// player.setBianShenLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
				player.setCurrSoul(soul);
				player.setSoulLevel(1);

				if (logger.isDebugEnabled()) logger.debug("[trace_create2] [{}] [{}]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });

				// 加入测试数据
				addDefaultArticle2NewPlayer(player);

				if (logger.isDebugEnabled()) logger.debug("[trace_create3] [{}] [{}]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });

				try {
					player.init("createPlayer");
					player.setCreateTime(start);
					em.save(player);

				} catch (Exception e) {
					logger.warn("[创建新玩家] [初始化出现异常] [username:" + username + "] [name:" + name + "]", e);
					throw e;
				}
				if (logger.isDebugEnabled()) logger.debug("[trace_create4] [{}] [{}]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });

				player.setChatChannelStatus(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
				player.setBindSilver(50000);

				addDefaultTask2CreatePlayer(player);

				player.setHp(player.getMaxHP());
				player.setMp(player.getMaxMP());

				player.setGameMsgs(new String[Player.gggNum]);
				GamePlayerManager.createPlayerMsg(player, 0, false);
				GamePlayerManager.createPlayerMsg(player, 1, false);
				GamePlayerManager.createPlayerMsg(player, 2, false);
				GamePlayerManager.createPlayerMsg(player, 3, true);

				namePlayerMap.put(name, player);
				idPlayerMap.put(new Long(player.getId()), player);

				老玩家回馈发放(player);

				/**
				 * 发送礼包
				 */
				ActivityManager.getInstance().sendNewPlayerPrize(player);
				/**
				 * BT操作
				 */
				player.setBindSilver(999000);
//				player.setSilver(20000000);
				player.setChargePoints(1000);
				player.setChargePointsSendTime(System.currentTimeMillis());
				player.setRMB(119000);
				player.setViprmb(119000);
				
				OldPlayerBackManager.getInstance().registerOldPlayerBack(player);

				NEW_MIESHI_UPDATE_PLAYER_INFO info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), username, player.getId(), name, player.getLevel(), player.getCareer(), (int) player.getRMB(), player.getVipLevel(), 0);
				MieshiGatewayClientService.getInstance().sendMessageToGateway(info);

				if (logger.isInfoEnabled()) logger.info("[创建角色] [username:{}] [{}] [speed:{}] [gameName:{}] [country:{}] [{}ms]", new Object[] { username,player.getLogString(),player.getSpeed(), gameName,country,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				try {
					String names [] = {"开局随机绑银箱","开局随机材料箱","首充之后秒天秒地秒空气"};
//					if (System.currentTimeMillis() < ANZHI_SDK_PRIZE_END) {
//						if (username.startsWith("HAODONGUSER")) {
					for(String n : names){
						ArticleManager am = ArticleManager.getInstance();
						Article a = am.getArticle(n);
						if (a != null) {
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, a.getColorType(), 1, true);
							if (ae != null) {
								player.putToKnapsacks(ae, "渠道专属特权礼包");
							}
						}
					}
							
//						}
//					}
				} catch (Exception e) {
					logger.warn("HAODONGSDK_XUNXIAN", e);
				}
				ActivityManagers.getInstance().sendPlayerInfoToUC(player,1);
//				try {
//					if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
//						if (UcVipActivityManager.getInstance().isUC(username)) UcVipActivityManager.getInstance().sendUcVipMail4EnterServer("贵宾仙囊", player, username);
//					}
//				} catch (Exception e) {
//					ActivitySubSystem.logger.error("[ucvip礼包发放] [错误] [出现异常] [" + player.getId() + "] [" + player.getName() + "] [" + player.getUsername() + "]", e);
//				}

				return player;
			} else {
				throw new Exception("角色名已经被使用");
			}
		} finally {
			synchronized (create_lock) {
				creatingPlayerNameSet.remove(name);
			}
		}
	}

	// public static String[] korea_st_prizes = { "비행 탈것 패키지(FGT)", "펫 패키지(FGT)", "20레벨 완미 오렌지색 장비세트(FGT)" };
	// public static String[] korea_st_prizes_2 = { "비행 탈것 패키지(CBT)", "펫 패키지(CBT)", "20레벨 완미 오렌지색 장비세트(CBT)" };
	// 飞行坐骑礼包（FGT）,宠物礼包（FGT）,完美橙色套装（FGT）

	public static final List<String> 满40的老玩家账号 = new ArrayList<String>();
	public static final List<String> 没有满40的老玩家账号 = new ArrayList<String>();

	public void 老玩家回馈发放(Player player) {
		// try{
		// ArrayList<Article> aeList = new ArrayList<Article>();
		// ArticleManager am = ArticleManager.getInstance();
		// if(满40的老玩家账号.contains(player.getUsername())){
		// Article a = am.getArticle("一测老友锦囊");
		// if(a != null){
		// aeList.add(a);
		// }
		//
		// a = am.getArticle("一测高手锦囊");
		// if(a != null){
		// aeList.add(a);
		// }
		// }else if(没有满40的老玩家账号.contains(player.getUsername())){
		// Article a = am.getArticle("一测老友锦囊");
		// if(a != null){
		// aeList.add(a);
		// }
		// }else{
		// Article a = am.getArticle("二测新手锦囊");
		// if(a != null){
		// aeList.add(a);
		// }
		// }
		//
		// if (aeList.size() > 0) {
		// ArticleEntityManager aem = ArticleEntityManager.getInstance();
		// try {
		// for(Article a : aeList){
		// ArticleEntity ae = aem.createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, a.getColorType(), true);
		// if (ae != null) {
		// player.putToKnapsacks(ae, "老玩家活动礼包获得");
		//
		// if(ArticleManager.logger.isInfoEnabled()){
		// ArticleManager.logger.info("[老玩家活动礼包] [成功] [{}] [{}] [{}]", new Object[] { player.getLogString(), ae.getArticleName(), ae.getId() });
		// }
		//
		// //统计
		// ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "老玩家活动礼包获得", null);
		//
		// } else {
		// if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[老玩家活动礼包] [失败，没有生成新物品] [{}] [{}]", new Object[] { player.getLogString(), a.getName() });
		// }
		// }
		// } catch (Exception ex) {
		// if (ArticleManager.logger.isErrorEnabled()) ArticleManager.logger.error("[老玩家活动礼包] [异常] ["+player.getLogString()+"]",ex);
		// }
		// }
		// }catch(Exception e){
		// if (ArticleManager.logger.isErrorEnabled()) ArticleManager.logger.error("[老玩家活动礼包] [异常] ["+player.getLogString()+"]",e);
		// }

	}

	public Player createPurePlayeForHefu(String username, String name, byte sex, byte politicalCamp, int career) throws Exception {
		return null;
	}

	public int getAmountOfPlayers() throws Exception {
		return (int) em.count();
	}

	@Override
	public int getAmountOfPlayers(String username) throws Exception {
		// TODO Auto-generated method stub
		return (int) em.count(Player.class, "username=?", new Object[] { username });
	}

	public boolean isOnline(long id) {
		Player p = idPlayerMap.get(id);
		return (p != null && p.isOnline());
	}

	public boolean isOnline(String name) {
		Player p = namePlayerMap.get(name);
		return (p != null && p.isOnline());
	}

	public boolean isUserOnline(String username) {
		Player ps[] = getOnlinePlayers();
		for (Player p : ps) {
			if (p.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过角色的Id获得角色
	 */
	public Player getPlayerInCache(long id) {
		Player p2 = idPlayerMap.get(id);
		return p2;
	}

	public long getPlayerId(String username) {
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		long[] list = null;
		try {
			list = em.queryIds(Player.class, " name = '" + username + "'");
			if (list != null && list.length > 0) {
				return list[0];
			}
		} catch (Exception e) {
			logger.error("[查询player简单对象] [异常] [" + username + "]", e);
		}
		return -1;
	}
	
	public List<SimplePlayer4Load> getSimplePlayer4Load(String name) {
		try {
			long[] ids = em.queryIds(Player.class, "name=?", new Object[]{name});
			if (ids != null && ids.length > 0) {
				return getSimplePlayer4Load(ids[0]);
			}
		} catch (Exception e) {
			logger.error("[查询player简单对象] [异常] [name:" + name + "]", e);
		}
		return new ArrayList<SimplePlayer4Load>();
	}
	
	public List<SimplePlayer4Load> getSimplePlayer4Load(long ids) {
		long[] idss = new long[1];
		idss[0] = ids;

		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		if (idss != null && idss.length > 0) {
			List<SimplePlayer4Load> list = null;
			try {
				list = em.queryFields(SimplePlayer4Load.class, idss);
				List<SimplePlayer4Load> sortList = new ArrayList<SimplePlayer4Load>();
				for (long id : idss) {
					for (SimplePlayer4Load ib : list) {
						if (id == ib.getId()) {
							sortList.add(ib);
							break;
						}
					}
				}
				return sortList;
			} catch (Exception e) {
				logger.error("[查询player简单对象] [异常] [" + idss[0] + "]", e);
			}
		} else {
			logger.error("[查询player简单对象] [没有记录] [" + ids + "]");
		}
		return new ArrayList<SimplePlayer4Load>();
	}

	public Player getPlayer(String name) throws Exception {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player player = namePlayerMap.get(name);
		if (player != null) return player;

		Object lock = null;
		synchronized (load_lock) {
			lock = loadLockNameMap.get(name);
			long ids = this.getPlayerId(name);
			if (lock == null) {
				lock = new Object() {
				};
				loadLockNameMap.put(name, lock);
			}
			if (HorseManager.logger.isDebugEnabled()) {
				HorseManager.logger.debug("[加载简单player锁对象] [通过id加载] [ids:" + ids + "] [lock:" + lock + "]");
			}
			if (ids > 0 && lock != null) {
				loadLockIdMap.put(ids, lock);
				if (HorseManager.logger.isDebugEnabled()) {
					HorseManager.logger.debug("[加载简单player锁对象] [通过名字加载] [playerName:" + name + "] [id:" + ids + "]");
				}
			}
		}

		synchronized (lock) {
			try {
				player = namePlayerMap.get(name);
				if (player != null) return player;

				player = load(name);

				if (player != null) {
					Player p2 = idPlayerMap.get(player.getId());
					if (p2 != null) {
						namePlayerMap.put(player.getName(), p2);
					} else {
						synchronized (load_lock) {
							p2 = idPlayerMap.get(player.getId());
							if (p2 != null) {
								namePlayerMap.put(player.getName(), p2);
							} else {
								idPlayerMap.put(player.getId(), player);
								namePlayerMap.put(player.getName(), player);
							}
						}
					}
					if (p2 != null) {
						return p2;
					}
					return player;
				} else {
					if (logger.isWarnEnabled()) logger.warn("[无法加载对应的角色] [可能是正常的情况，对应角色本来就不存在] [name={}] ", new Object[] { name });
				}
				if (logger.isWarnEnabled()) logger.warn("[获得玩家] [玩家不存在] [玩家名:{}] [{}ms]", new Object[] { name, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
				throw new Exception("Player with name[" + name + "] not found.");

			} finally {
				synchronized (load_lock) {
					lock = loadLockNameMap.remove(name);
				}
			}
		}
	}

	public boolean checkPlayerNameExist(String name) throws Exception {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player player = namePlayerMap.get(name);
		if (player != null) return true;

		Object lock = null;
		synchronized (load_lock) {
			lock = loadLockNameMap.get(name);
			if (lock == null) {
				lock = new Object() {
				};
				loadLockNameMap.put(name, lock);
			}
		}

		synchronized (lock) {
			try {
				player = namePlayerMap.get(name);
				if (player != null) return true;
				return isNameExist(name);
			} finally {
				synchronized (load_lock) {
					lock = loadLockNameMap.remove(name);
				}
			}
		}
	}

	public Player getPlayer(long id) throws Exception {
		if (id < 0) {
			throw new Exception("player's id can not be < 0 id:" + id);
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player player = idPlayerMap.get(id);// asd
		if (player != null) return player;
		Object lock = null;
		synchronized (load_lock) {
			lock = loadLockIdMap.get(id);
			long startTime = System.currentTimeMillis();
			List<SimplePlayer4Load> tempList = this.getSimplePlayer4Load(id);
			HorseManager.logger.debug("[消耗时间] [" + (System.currentTimeMillis() - startTime) + "]");
			if (lock == null) {
				lock = new Object() {
				};
				loadLockIdMap.put(id, lock);
			}
			if (HorseManager.logger.isDebugEnabled()) {
				HorseManager.logger.debug("[加载简单player锁对象] [通过id加载] [tempList.size():" + tempList.size() + "] [lock:" + lock + "]");
			}
			if (tempList != null && tempList.size() > 0 && lock != null && tempList.get(0).getName() != null) {
				loadLockNameMap.put(tempList.get(0).getName(), lock);
				if (HorseManager.logger.isDebugEnabled()) {
					HorseManager.logger.debug("[加载简单player锁对象] [通过id加载] [playerName:" + tempList.get(0).getName() + "] [id:" + id + "]");
				}
			}
		}

		synchronized (lock) {// asd
			try {
				player = idPlayerMap.get(id);
				if (player != null) return player;

				player = load(id);

				if (player != null) {
					Player p2 = namePlayerMap.get(player.getName());
					if (p2 != null) {
						idPlayerMap.put(player.getId(), p2);
					} else {
						synchronized (load_lock) {
							p2 = namePlayerMap.get(player.getName());
							if (p2 != null) {
								idPlayerMap.put(player.getId(), p2);
							} else {
								idPlayerMap.put(player.getId(), player);
								namePlayerMap.put(player.getName(), player);
							}
						}
					}
					if (p2 != null) {
						return p2;
					}
					return player;
				} else {
					// logger.warn("[获得玩家] [玩家不存在或者初始化失败] [id:"+id+"]", start);
					if (logger.isWarnEnabled()) logger.warn("[获得玩家] [玩家不存在或者初始化失败] [id:{}] [{}ms]", new Object[] { id, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
					throw new Exception("玩家不存在或者初始化失败 id[" + id + "]");
				}
			} finally {
				synchronized (load_lock) {
					loadLockIdMap.remove(id);
				}
			}
		}

	}

	public Player[] getOnlinePlayerByUser(String username) throws Exception {
		ArrayList<Player> al = new ArrayList<Player>();
		Player ps[] = this.idPlayerMap.values().toArray(new Player[0]);
		for (int i = 0; i < ps.length; i++) {
			if (ps[i].getUsername().equals(username) && ps[i].isOnline()) {
				al.add(ps[i]);
			}
		}
		return al.toArray(new Player[0]);
	}
	
	public static int 最大可查询的角色数量 = 30;

	public Player[] getPlayerByUser(String username) throws Exception {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			ArrayList<Player> playerList = new ArrayList<Player>();
			StringBuffer sb = new StringBuffer();

			long[] ids = em.queryIds(Player.class, "username=?", new Object[] { username }, "enterGameTime desc", 1, 最大可查询的角色数量);

			for (int i = 0; i < ids.length; i++) {
				try {
					Player p = getPlayer(ids[i]);
					sb.append(ids[i] + ",");
					if (p != null) {
						sb.append(p.getName() + ";");
					} else {
						sb.append("null;");
					}
					playerList.add(p);
				} catch (Exception e) {
					logger.warn("[获取指定帐号角色列表--加载用户] [失败] [" + username + "] [id：" + ids[i] + "] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
					throw e;
				}
			}

			if (logger.isInfoEnabled()) {
				logger.info("[获取指定帐号角色列表] [成功] [{}] [ids:{}] [{}ms]", new Object[] { username, sb, (System.currentTimeMillis() - startTime) });
			}

			Player players[] = playerList.toArray(new Player[0]);
			return players;

		} catch (Exception e) {
			logger.warn("[获取指定帐号角色列表] [出现未知异常] [" + (System.currentTimeMillis() - startTime) + "ms]", e);
			throw e;
		}
	}

	/**
	 * 此方法仅供页面显示玩家使用
	 * 返回的Player不在内存cache中，也没有调用init方法
	 * 直接用数据库查询的记录
	 */
	public Player[] getPlayersByPage(int start, int size) throws Exception {
		SimpleEntityManagerOracleImpl<Player> emImpl = (SimpleEntityManagerOracleImpl<Player>) em;

		long ids[] = emImpl.queryIds(Player.class, "", new Object[] {}, "", start + 1, start + 1 + size);
		List<Player> playerList = new ArrayList<Player>();
		for (long id : ids) {
			Player p = emImpl.select_object(id);
			playerList.add(p);
		}
		return playerList.toArray(new Player[0]);
	}

	@Override
	public int getUpperLevelPlayerCount(int level) {
		// TODO Auto-generated method stub
		try {
			return (int) em.count(Player.class, "level>=?", new Object[] { level });
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 此方法仅供页面显示玩家使用
	 * 返回的Player不在内存cache中，也没有调用init方法
	 * 直接用数据库查询的记录
	 */
	public Player[] getUpperLevelPlayer(int start, int length, int level) throws Exception {
		SimpleEntityManagerOracleImpl<Player> emImpl = (SimpleEntityManagerOracleImpl<Player>) em;

		long ids[] = emImpl.queryIds(Player.class, "level>=?", new Object[] { level }, "", start + 1, start + 1 + length);
		List<Player> playerList = new ArrayList<Player>();
		for (long id : ids) {
			Player p = emImpl.select_object(id);
			playerList.add(p);
		}
		return playerList.toArray(new Player[0]);
	}

	public boolean isExists(String name) {
		try {
			return checkPlayerNameExist(name);
		} catch (Exception e) {
		}
		return false;
	}

	public boolean isExists(long id) {
		try {
			return getPlayer(id) != null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return false;
	}

	public Player[] getOnlinePlayers() {
		ArrayList<Player> al = new ArrayList<Player>();
		Player players[] = idPlayerMap.values().toArray(new Player[0]);
		for (Player p : players) {
			if (p.getConn() != null && p.getConn().getState() != Connection.CONN_STATE_CLOSE) al.add(p);
		}
		return al.toArray(new Player[0]);
	}

	public Player[] getOnlinePlayersNotInGame() {
		ArrayList<Player> al = new ArrayList<Player>();
		Player players[] = idPlayerMap.values().toArray(new Player[0]);
		for (Player p : players) {
			if (p.getConn() != null && p.getConn().getState() != Connection.CONN_STATE_CLOSE && p.getCurrentGame() == null) {
				al.add(p);
			}
			if (logger.isDebugEnabled()) logger.debug("[得到在线但不在游戏中的玩家] [" + p.getName() + "] [" + p.getConn() + "] [" + (p.getCurrentGame() != null ? p.getCurrentGame().getGameInfo().getName() : "NULL") + "]");
		}
		if (logger.isDebugEnabled()) logger.debug("[得到在线但不在游戏中的玩家] [总量:" + players.length + "] [在线但未进入游戏:" + al.size() + "]");
		return al.toArray(new Player[0]);
	}

	@Override
	public Player[] getOnlinePlayerByJiazu(Jiazu jiazu) {
		ArrayList<Player> al = new ArrayList<Player>();
		Set<JiazuMember> set = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
		for (JiazuMember jm : set) {
			Player p = idPlayerMap.get(jm.getPlayerID());
			if (p != null && p.isOnline()) {
				al.add(p);
			}
		}
		return al.toArray(new Player[0]);
	}

	public Player[] getOnlinePlayerByCountry(byte countryId) {
		ArrayList<Player> al = new ArrayList<Player>();
		Player players[] = null;
		int count = 0;
		while (players == null && ++count < 100) {
			try {
				players = idPlayerMap.values().toArray(new Player[0]);
			} catch (Exception e) {
			}
		}
		for (Player p : players) {
			if (p.getCountry() == countryId) {
				al.add(p);
			}
		}
		return al.toArray(new Player[0]);
	}

	@Override
	public Player[] getCachedPlayers() {
		// TODO Auto-generated method stub
		Player players[] = idPlayerMap.values().toArray(new Player[0]);
		return players;
	}

	private Player load(long id) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			Player p = em.find(id);
			if (p == null) {
				if (logger.isInfoEnabled()) {
					// logger.info("[加载角色] [失败] [id:"+id+"] [角色已删除] [帮会:--] [头衔:--] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"ms]");
					if (logger.isInfoEnabled()) logger.info("[加载角色] [失败] [id:{}] [角色已删除] [帮会:--] [头衔:--] [{}ms]", new Object[] { id, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
				return null;
			}
			// 获取此玩家账户身上的元宝
			// long yuanbao = bossClientService.getUserYuanbao(p.getUsername());
			// p.setTotalRmbyuanbao(p.getRmbyuanbao() + yuanbao);
			try {
				p.init("load by id");
				if (logger.isInfoEnabled()) {
					// logger.info("[加载角色] [成功] [id:"+id+"] ["+p.getName()+"] [帮会:"+p.getGangName()+"] [头衔:"+p.getGangTitle()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()
					// - startTime)+"ms]");
					if (logger.isInfoEnabled()) logger.info("[加载角色] [成功] [id:{}] [{}] [帮会:{}] [头衔:{}] [{}ms]", new Object[] { id, p.getName(), p.getGangName(), p.getGangTitle(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
				return p;
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[加载角色] [失败] [id:" + id + "] [" + p.getName() + "] [帮会:" + p.getGangName() + "] [头衔:" + p.getGangTitle() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
			}

		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[加载角色] [失败] [id:" + id + "] [--] [帮会:--] [头衔:--] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
		}
		return null;
	}

	private Player load(String name) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			List<Player> pList = em.query(Player.class, "name=?", new Object[] { name }, "", 1, 10);
			Player p = null;
			if (pList.size() > 0) p = pList.get(0);
			if (p == null) {
				if (logger.isInfoEnabled()) {
					// logger.info("[加载角色] [失败] [name:"+name+"] [角色已删除] [帮会:--] [头衔:--] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"ms]");
					if (logger.isInfoEnabled()) logger.info("[加载角色1] [失败] [name:{}] [角色已删除] [帮会:--] [头衔:--] [{}ms]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
				return null;
			}
			// // 获取此玩家账户身上的元宝
			// long yuanbao = bossClientService.getUserYuanbao(p.getUsername());
			// p.setTotalRmbyuanbao(p.getRmbyuanbao() + yuanbao);

			try {
				p.init("load by name");

				if (logger.isInfoEnabled()) {
					// logger.info("[加载角色1] [成功] [id:"+p.getId()+"] ["+p.getName()+"] [帮会:"+p.getGangName()+"] [头衔:"+p.getGangTitle()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()
					// -
					// startTime)+"ms]");
					if (logger.isInfoEnabled()) logger.info("[加载角色1] [成功] [id:{}] [{}] [帮会:{}] [头衔:{}] [{}ms]", new Object[] { p.getId(), p.getName(), p.getGangName(), p.getGangTitle(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
				return p;
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[加载角色1] [失败] [id:" + p.getId() + "] [" + p.getName() + "] [帮会:" + p.getGangName() + "] [头衔:" + p.getGangTitle() + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[加载角色1] [失败] [id:-] [" + name + "] [帮会:--] [头衔:--] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
		}
		return null;
	}

	/**
	 * 只用于检查角色名存不存在，不能用于得到玩家
	 * @param name
	 * @return
	 */
	private boolean isNameExist(String name) {
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try {
			List<Player> pList = em.query(Player.class, "name=?", new Object[] { name }, "", 1, 10);

			if (pList.size() == 0) {
				if (logger.isInfoEnabled()) {
					logger.info("[检查角色名] [不存在] [name:{}] [{}ms]", new Object[] { name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) });
				}
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("[检查角色名] [失败] [id:-] [" + name + "] [" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "ms]", e);
		}
		return true;
	}

	public void enterGame(Game game, Player player) {

		// SocialManager.getInstance().upOrDownNotice(player, true);

	}

	public void leaveGame(Game game, Player player) {

		// SocialManager.getInstance().upOrDownNotice(player, false);
	}

	@Override
	public void enterServerNotify(Player player) {
	}

	@Override
	public void leaveServerNotify(Player player) {
	}

	@Override
	public void playerAddFriend(Player player, Player friend) {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (friend != null && friend.isOnline()) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_2585 + player.getName() + Translate.text_5700);
			friend.addMessageToRightBag(req);
		}

		if (logger.isInfoEnabled()) {
			// logger.info("[玩家加好友] ["+player.getName()+"] [好友方:"+friend.getName()+"] [发送加好友通知] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"ms]");
			if (logger.isInfoEnabled()) logger.info("[玩家加好友] [{}] [好友方:{}] [发送加好友通知] [{}ms]", new Object[] { player.getName(), friend.getName(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
		}
	}

	@Override
	public void kickPlayer(long playerId) {
		// TODO Auto-generated method stub
		Player p = idPlayerMap.get(playerId);
		if (p != null && p.isOnline()) {
			if (p.getCurrentGame() != null) {
				p.getCurrentGame().getQueue().push(new LeaveGameEvent(p));
			}
			p.leaveServer();
			p.getConn().close();
			if (logger.isInfoEnabled()) {
				// logger.info("[踢玩家] ["+p.getName()+"] [id:"+p.getId()+"]");
				if (logger.isInfoEnabled()) logger.info("[踢玩家] [{}] [id:{}]", new Object[] { p.getName(), p.getId() });
			}
		}
	}

	public Player savePlayer(Player player) throws Exception {
		// TODO Auto-generated method stub
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		player.setLastUpdateTime(start);
		em.save(player);

		// logger.info("[savePlayer] ["+player.getId()+"] ["+player.getName()+"]", start);
		if (logger.isInfoEnabled()) logger.info("[savePlayer] [{}] [{}] [{}ms]", new Object[] { player.getId(), player.getName(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });

		return player;
	}

	public void removePlayer(String name) throws Exception {
		// TODO Auto-generated method stub
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player p = getPlayer(name);
		if (p != null) {
			{
				// 这个块儿里的东需需要设计,暂时先这么写
				JiazuManager.getInstance().removePlayer(p);
			}

			em.remove(p);

			idPlayerMap.remove(p.getId());
			namePlayerMap.remove(name);

			NEW_MIESHI_UPDATE_PLAYER_INFO info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), p.getUsername(), p.getId(), name, p.getLevel(), p.getCareer(), (int) p.getRMB(), p.getVipLevel(), 4);
			MieshiGatewayClientService.getInstance().sendMessageToGateway(info);

			if (logger.isInfoEnabled()) {
				logger.info("[删除角色] [user:{}] [id:{}] [name:{}] [{}]", new Object[] { p.getUsername(), p.getId(), name, (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
			}
		}
	}

	/**
	 * 角色改名更新缓存
	 * @param player
	 * @param oldName
	 */
	public void updateCacheForChangeName(Player player, String oldName) {
		namePlayerMap.remove(oldName);
		namePlayerMap.put(player.getName(), player);
	}

	public void userModifyPlayer(Player player, String name, byte sex, byte career) throws Exception {
		// TODO Auto-generated method stub
		if (player.getLevel() > 5) {
			throw new Exception("只允许5级以下玩家修改角色信息");
		}
		String oldname = player.getName();
		if (!oldname.equals(name)) {
			// 检查角色名称是否合法
			WordFilter filter = WordFilter.getInstance();
			boolean valid = filter.rvalid(name);
			if (!valid) {
				throw new Exception("角色名称不合法");
			}
			if (this.isExists(name)) {
				throw new Exception("此角色名称已存在");
			}
			player.setName(name);
			// 其他处理
		}
		if (sex != player.getSex()) {
			player.setSex(sex);
			// 其他处理
		}
		if (career != player.getCareer()) {
			player.setCareer(career);
			// 其他处理
		}
		this.updatePlayer(player);

		// 修改账号的注册类型为转注册
		PlayerUpdateFlow flow = new PlayerUpdateFlow();
		flow.setUsername(player.getUsername());
		flow.setOldname(oldname);
		flow.setNewname(name);
		flow.setServer(gameConstants.getServerName());
		flow.setSex(player.getSex());
		flow.setCareer(CareerManager.getInstance().getCareer(player.getCareer()).getName());
		flow.setTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		StatClientService.getInstance().sendPlayerUpdateFlow(flow);
	}

	public Player getPlayer(String name, String username) throws Exception {
		// TODO Auto-generated method stub
		Player player = getPlayer(name);
		if (player == null || !player.getUsername().equals(username)) {
			throw new Exception("Player with name[" + name + "] and username[" + username + "] not found.");
		}
		return player;
	}

	public Hashtable<Long, Player> getIdPlayerMap() {
		return idPlayerMap;
	}

	public Hashtable<String, Player> getNamePlayerMap() {
		return namePlayerMap;
	}

	public void setGameConstants(GameConstants gameConstants) {
		this.gameConstants = gameConstants;
	}

	public class SendOnlinePlayerStatusTask implements Runnable {
		public void run() {
		}
	}

	public static boolean isAllowActionStat() {
		return isAllowActionStat;
	}

	public static void setAllowActionStat(boolean isAllowActionStat) {
		GamePlayerManager.isAllowActionStat = isAllowActionStat;
	}

	public void updatePlayerName(Player player, String newName) {
		// TODO Auto-generated method stub
		long playerid = player.getId();
		String oldName = player.getName();
		Player p = null;
		try {
			p = this.getPlayer(playerid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (p != null) {
			Player idPlayer = idPlayerMap.get(playerid);
			if (idPlayer != null) {
				idPlayer.setName(newName);
				namePlayerMap.remove(oldName);
				namePlayerMap.put(newName, idPlayer);
			}
		}
	}

	public void updatePlayerUsername(Player player, String newUsername) {
		// TODO Auto-generated method stub
		Player p = null;
		try {
			p = this.getPlayer(player.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (p != null) {
			p.setUsername(newUsername);
		}
	}

	/**
	 * 得到在cache中所有的用户
	 * @return
	 */
	public Object[] getAllPlayerInCache() {

		return this.idPlayerMap.values().toArray(new Object[0]);

	}

	@Override
	public Player[] getOnlineInZongpai(long zongpaiId) {

		ArrayList<Player> all = new ArrayList<Player>();
		ZongPaiManager zpm = ZongPaiManager.getInstance();
		JiazuManager jm = JiazuManager.getInstance();
		if (zpm != null && jm != null) {
			ZongPai zp = zpm.getZongPaiById(zongpaiId);
			if (zp != null) {
				List<Long> list = zp.getJiazuIds();
				for (long jiazuId : list) {
					Set<JiazuMember> jz = jm.getJiazuMember(jiazuId);
					for (JiazuMember member : jz) {
						Player p = idPlayerMap.get(member.getPlayerID());
						if (p != null && p.isOnline()) {
							all.add(p);
						}
					}
				}
				return all.toArray(new Player[0]);
			}
		}
		return null;
	}

	private void addDefaultArticle2NewPlayer(Player player) {

	}

	/**
	 * 给新建的角色增加默认的任务
	 * @param player
	 */
	private void addDefaultTask2CreatePlayer(Player player) {
		// String taskName = NewPlayerTaskEventTransact.newPlayer_task;
		// if (taskName != null && !taskName.isEmpty()) {
		// Task defaultTask = TaskManager.getInstance().getTask(taskName);
		// if (defaultTask != null) {
		// player.addTaskByServer(defaultTask);
		// }
		// }
	}

	/**
	 * 更新在线玩家数据
	 */
	static DiskCache onlineDC = null;
	private String onlinenumDDCFile;
	MieshiGatewayClientService mgs;
	String serverName = "";

	private void initUpdateOnlineNUm() {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);

						检测多开();
						检测篡改客户端版本();

						检测打金成员();

						updateOnlineNum();
						向网关发送心跳超时数据();
					} catch (Throwable e) {
						logger.error("[统计在线玩家数量异常]", e);
					}
				}
			}

		}, "Update-OnlineNum-Thread");
		t.start();
	}

	private void updateOnlineNum() {
		try {
			int num = this.getOnlinePlayers().length;
			String day = DateUtil.formatDate(new Date(), "yyyy-MM-dd");

			if (onlineDC == null) {
				onlineDC = new DefaultDiskCache(new File(onlinenumDDCFile), "onlinenum", 10L * 365L * 24L * 3600L * 100L);
			}

			HashMap<String, Integer> al = (HashMap<String, Integer>) onlineDC.get(day);
			if (al == null) {
				al = new HashMap<String, Integer>();
			}
			String key = DateUtil.formatDate(new Date(), "HH:mm");
			al.put(key, num);
			onlineDC.put(day, al);

			String dayMem = "mem-" + DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			HashMap<String, String> almem = (HashMap<String, String>) onlineDC.get(dayMem);
			if (almem == null) {
				almem = new HashMap<String, String>();
			}
			String keyMem = DateUtil.formatDate(new Date(), "HH:mm:ss");
			keyMem = keyMem.substring(0, keyMem.length() - 1);
			Runtime r = Runtime.getRuntime();
			long total = r.totalMemory();
			long free = r.freeMemory();
			almem.put(keyMem, total + "," + free);
			onlineDC.put(dayMem, almem);
			向网关发送在线数据(num);
			if (logger.isDebugEnabled()) logger.debug("[执行在线玩家数量统计] [keyMem:" + keyMem + "] [day:" + day + "] [time:" + key + "] [num:" + num + "] [memTotal:" + total + "] [memFree:" + free + "]");
		} catch (Throwable e) {
			// System.out.println("================== 统计在线玩家代码出现错误 ================== ");
			e.printStackTrace(System.out);
		}
	}

	public void 向网关发送心跳超时数据() {
	}

	public void 向网关发送在线数据(int num) {
		if (mgs == null) {
			mgs = MieshiGatewayClientService.getInstance();
		}
		if (serverName == null || serverName.equals("")) {
			serverName = GameConstants.getInstance().getServerName();
		}
		if (mgs != null) {
			mgs.sendMessageToGateway(new REPORT_ONLINENUM_REQ(GameMessageFactory.nextSequnceNum(), serverName, num));
		}
	}

	public void 检测多开() {
		Player[] players = getOnlinePlayers();

		// 同一IP+同一设备号+同一机型 --》多个玩家，如果有一个绑定成功，其他都绑定失败
		HashMap<String, ArrayList<Player>> duokaiMap = new HashMap<String, ArrayList<Player>>();
		for (int i = 0; i < players.length; i++) {
			Connection conn = players[i].getConn();
			USER_CLIENT_INFO_REQ o = null;
			if (conn != null) o = (USER_CLIENT_INFO_REQ) conn.getAttachmentData("USER_CLIENT_INFO_REQ");

			String deviceId = "";
			String phoneType = "";
			String ip = "";
			if (conn != null) {
				String s = conn.getIdentity();
				ip = s.split(":")[0];
			}
			if (o != null) {
				phoneType = o.getPhoneType();
				deviceId = o.getUUID() + o.getDEVICEID();
			}

			if (ip.length() > 0 && deviceId.length() > 0 && phoneType.length() > 0) {
				String key = ip + "#" + deviceId + "#" + phoneType;
				ArrayList<Player> al = duokaiMap.get(key);
				if (al == null) {
					al = new ArrayList<Player>();
					duokaiMap.put(key, al);
				}
				al.add(players[i]);
			}
		}

		Iterator<String> it = duokaiMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			ArrayList<Player> al = duokaiMap.get(key);
			if (al.size() > 1) {
				int bindFail = 0;
				int bindSucc = 0;
				for (int i = 0; i < al.size(); i++) {
					Player p = al.get(i);
					Connection conn = p.getConn();
					USER_CLIENT_INFO_REQ o = null;
					if (conn != null) o = (USER_CLIENT_INFO_REQ) conn.getAttachmentData("USER_CLIENT_INFO_REQ");
					if (o.getIsStartServerBindFail()) {
						bindFail++;
					} else {
						bindSucc++;
					}
				}
				if (bindSucc == 1 && bindFail >= 1) {
					// 多开
					for (int i = 0; i < al.size(); i++) {
						Player p = al.get(i);
						Connection conn = p.getConn();
						USER_CLIENT_INFO_REQ o = null;
						if (conn != null) o = (USER_CLIENT_INFO_REQ) conn.getAttachmentData("USER_CLIENT_INFO_REQ");
						String clientId = "";
						String deviceId = "";
						String phoneType = "";
						boolean isExistOtherServer = false;
						boolean isStartServerBindFail = false;

						if (o != null) {
							isExistOtherServer = o.getIsExistOtherServer();
							isStartServerBindFail = o.getIsStartServerBindFail();
							phoneType = o.getPhoneType();
							deviceId = o.getUUID() + o.getDEVICEID();
							clientId = o.getClientId();
						}
						GameNetworkFramework.logger.warn("[涉嫌多开] [强制断线] [" + conn.getIdentity() + "] [clientId:" + clientId + "] [deviceId:" + deviceId + "] [phoneType:" + phoneType + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerBindFail:" + isStartServerBindFail + "] " + p.getLogString());
						logger.warn("[涉嫌多开] [强制断线] [" + conn.getIdentity() + "] [clientId:" + clientId + "] [deviceId:" + deviceId + "] [phoneType:" + phoneType + "] [isExistOtherServer:" + isExistOtherServer + "] [isStartServerBindFail:" + isStartServerBindFail + "] " + p.getLogString());

						if (p.getConn() != null) p.getConn().close();

					}

				}
			}
		}
	}

	public void 检测篡改客户端版本() {
		// Player[] players = getOnlinePlayers();
		// for (int i = 0; i < players.length; i++) {
		// Connection conn = players[i].getConn();
		// USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ) conn.getAttachmentData("USER_CLIENT_INFO_REQ");
		// if (o != null) continue;
		// QUERY_CLIENT_INFO_RES res = (QUERY_CLIENT_INFO_RES) conn.getAttachmentData("QUERY_CLIENT_INFO_RES");
		// if (res == null) {
		// MieshiGatewayClientService service = MieshiGatewayClientService.getInstance();
		// service.sendMessageToGateway(new QUERY_CLIENT_INFO_REQ(GameMessageFactory.nextSequnceNum(), "", players[i].getUsername()));
		// } else {
		// if (res.getClientId().length() > 0 && res.getProgramVersion().length() > 0) {
		// Version version = new Version(res.getProgramVersion());
		// Version xxxxxxx = new Version("1.7.19");
		// if (version.compareTo(xxxxxxx) >= 0) {
		// // 篡改客户端版本
		//
		// GameNetworkFramework.logger.warn("[涉嫌篡改客户端版本] [强制断线] [" + conn.getIdentity() + "] [clientId:" + res.getClientId() + "] [deviceId:" + res.getUuid() + res.getDeviceId() +
		// "] [phoneType:" + res.getPhoneType() + "] [isExistOtherServer:false] [isStartServerBindFail:false] " + players[i].getLogString());
		// logger.warn("[涉嫌篡改客户端版本] [强制断线] [" + conn.getIdentity() + "] [clientId:" + res.getClientId() + "] [deviceId:" + res.getUuid() + res.getDeviceId() + "] [phoneType:" +
		// res.getPhoneType() + "] [isExistOtherServer:false] [isStartServerBindFail:false] " + players[i].getLogString());
		//
		// if (players[i].getConn() != null) players[i].getConn().close();
		// }
		// }
		// }
		// }

	}

	public void 检测打金成员() {
		Player[] players = getOnlinePlayers();
		DajingStudioManager dsManager = DajingStudioManager.getInstance();
		if (dsManager == null) return;
		for (int i = 0; i < players.length; i++) {
			Connection conn = players[i].getConn();
			USER_CLIENT_INFO_REQ o = (USER_CLIENT_INFO_REQ) conn.getAttachmentData("USER_CLIENT_INFO_REQ");
			if (o == null) continue;
			DajingStudio ds = dsManager.getDajingStudioByPlayer(players[i]);
			if (ds != null) continue;
			if (players[i].getLevel() < 10) continue;

			String phoneType = o.getPhoneType();
			String iden = conn.getIdentity();
			String ip = "";
			if (iden != null) {
				ip = iden.split(":")[0];
			}
			if (ip.length() == 0) continue;

			Iterator<String> it = dsManager.dajingMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (key.contains(ip)) {
					ds = dsManager.dajingMap.get(key);
					break;
				}
			}
			if (ds != null) {
				DajingGroup dg = ds.getDajingGroup(phoneType);
				if (dg != null) {
					if (dg.usernameList.contains(players[i].getUsername()) == false) {
						dg.usernameList.add(players[i].getUsername());
						dsManager.saveForManual();
					}
				}
			}
		}
	}

	public static class Version implements java.lang.Comparable<Version> {
		public ArrayList<Integer> versionNumList = new ArrayList<Integer>();
		public boolean forceUpdate;
		public String versionString;

		public ArrayList<Package> packageList = new ArrayList<Package>();

		public Version(String version) {

			versionString = version;

			if (version == null) return;
			int k = version.indexOf("_");
			if (k >= 0) {
				String s = version.substring(k + 1);
				// 这个force是加在版本文件夹上
				if (s.contains("force")) {
					forceUpdate = true;
				} else {
					forceUpdate = false;
				}

				version = version.substring(0, k);
			}

			String ss[] = version.split("\\.");
			for (int i = 0; i < ss.length; i++) {
				try {
					Integer v = Integer.parseInt(ss[i].trim());
					versionNumList.add(v);
				} catch (Exception e) {
					throw new java.lang.IllegalArgumentException("错误的版本串:" + versionString);
				}

			}
		}

		public int compareTo(Version o) {
			if (this == o) return 0;

			for (int i = 0; i < versionNumList.size() && i < o.versionNumList.size(); i++) {
				Integer x = versionNumList.get(i);
				Integer ox = o.versionNumList.get(i);

				if (x.intValue() < ox.intValue()) return -1;
				if (x.intValue() > ox.intValue()) return 1;
			}

			if (versionNumList.size() < o.versionNumList.size()) {
				return -1;
			}

			if (versionNumList.size() > o.versionNumList.size()) {
				return 1;
			}

			return 0;
		}

		public String toString() {
			return this.versionString;
		}

	}

	/*
	 * 获取在线人数
	 */
	public Map<String, Integer> getOnlineNum(String day) {
		if (onlineDC != null) {
			return (HashMap<String, Integer>) onlineDC.get(day);
		} else {
			return new HashMap<String, Integer>();
		}
	}

	public int[] getOnlineNumAsEveryMinute(String day) {
		ArrayList<Integer> nums = new ArrayList<Integer>();
		if (onlineDC != null) {
			HashMap<String, Integer> al = (HashMap<String, Integer>) onlineDC.get(day);

			String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			int maxH = 23;
			int maxM = 59;
			if (today.equals(day)) {
				maxH = Integer.parseInt(DateUtil.formatDate(new Date(), "HH"));
				maxM = Integer.parseInt(DateUtil.formatDate(new Date(), "mm"));
			}

			StringBuffer key = new StringBuffer();
			if (al != null) {
				for (int h = 0; h < 24; h++) {
					for (int m = 0; m < 60; m++) {
						key.setLength(0);
						if (h < 10) {
							key.append("0");
						}
						key.append(h);
						key.append(":");

						if (m < 10) {
							key.append("0");
						}
						key.append(m);

						Integer num = al.get(key.toString());
						if (num == null) {
							num = 0;
						}
						nums.add(num);

						if (h == maxH && m == maxM) {
							h = 24;
							m = 60;
							break;
						}
					}
				}

			}

		}
		int r[] = new int[nums.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = nums.get(i);
		}
		return r;
	}

	/**
	 * 
	 * @param day
	 * @return
	 */
	public String[] getMemAsEveryFiveSec(String day) {
		ArrayList<String> nums = new ArrayList<String>();
		if (onlineDC != null) {
			HashMap<String, String> al = (HashMap<String, String>) onlineDC.get("mem-" + day);
			String today = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
			int maxH = 23;
			int maxM = 59;
			if (today.equals(day)) {
				maxH = Integer.parseInt(DateUtil.formatDate(new Date(), "HH"));
				maxM = Integer.parseInt(DateUtil.formatDate(new Date(), "mm"));
			}

			StringBuffer key = new StringBuffer();
			if (al != null) {
				for (int h = 0; h < 24; h++) {
					for (int m = 0; m < 60; m++) {
						for (int k = 0; k < 6; k++) {
							key.setLength(0);
							if (h < 10) {
								key.append("0");
							}
							key.append(h);
							key.append(":");

							if (m < 10) {
								key.append("0");
							}
							key.append(m);

							key.append(":");
							key.append(k);

							String mem = al.get(key.toString());
							if (mem == null) {
								mem = "0,0";
							}
							nums.add(mem);

							if (h == maxH && m == maxM) {
								h = 24;
								m = 60;
								break;
							}
						}
					}
				}

			}

		}
		String r[] = new String[nums.size()];
		for (int i = 0; i < r.length; i++) {
			r[i] = nums.get(i);
		}
		return r;
	}

	public String getOnlinenumDDCFile() {
		return onlinenumDDCFile;
	}

	public void setOnlinenumDDCFile(String onlinenumDDCFile) {
		this.onlinenumDDCFile = onlinenumDDCFile;
	}

	/**
	 * 更新这个角色
	 * @param player
	 */
	public void updatePlayer(Player player) throws Exception {
		em.save(player);
	}

	public void run() {
		long lastRecordPlayerStat = SystemTime.currentTimeMillis();
		while (true) {
			try {
				Thread.sleep(60 * 1000);
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				Player players[] = idPlayerMap.values().toArray(new Player[0]);
				List<Player> clearPlayerList = new ArrayList<Player>();
				for (Player p : players) {
					if (!p.isOnline() && now - p.getLastRequestTime() > 1L * 60 * 60 * 1000) {
						clearPlayerList.add(p);
					}
				}
				if (clearPlayerList.size() > 0) {
					for (Player p : clearPlayerList) {
						p.getAllTask().clear();
						p.gdrMap = null;
						p.deliverTaskMap = null;
						p.newdeliverTaskMap = null;
						p.hasLoadAllDeliverTask = false;
						p.achievementEntityMap = null;
						p.setActivenessInfo(null);
						idPlayerMap.remove(p.getId());
						namePlayerMap.remove(p.getName());

						em.flush(p);

						AchievementManager aeManager = AchievementManager.getInstance();
						if (aeManager != null) {
							aeManager.playerRemoveFromMemory(p);
						}

						NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
						if (ndtm != null && ndtm.isNewDeliverTaskAct) {
							ndtm.notifyRemoveFromServerMemory(p);
						} else {
							DeliverTaskManager dtm = DeliverTaskManager.getInstance();
							if (dtm != null) {
								dtm.notifyRemoveFromServerMemory(p);
							}
						}
					}
				}

				if (SystemTime.currentTimeMillis() - lastRecordPlayerStat > 1000 * 60 * 10) {

					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(lastRecordPlayerStat);
					int oldDay = calendar.get(Calendar.DAY_OF_YEAR);
					calendar.setTimeInMillis(SystemTime.currentTimeMillis());
					int nowDay = calendar.get(Calendar.DAY_OF_YEAR);

//					for (Player p : players) {
//						if (p.isOnline()) {
//							GameStatTool.update(p);
//						}
//					}

					if (oldDay != nowDay) {
						// 存盘,清除内存数据

						AnalyseTool analyseTool = new AnalyseTool(RowData.class);
						analyseTool.analyse();

						RowData[] allDatas = GameStatTool.map.values().toArray(new RowData[0]);

						long[] 充值金额 = { -1, 10, 599, 5999, 29999, Long.MAX_VALUE };

						for (int k = 0; k < GameStatService.userTypeNames.length - 1; k++) {
							ArrayList<RowData> al = new ArrayList<RowData>();
							for (int j = 0; j < allDatas.length; j++) {
								if (allDatas[j].充值金额 > 充值金额[k] && allDatas[j].充值金额 <= 充值金额[k + 1]) {
									al.add(allDatas[j]);
								}
							}
							RowData[] data = al.toArray(new RowData[0]);

							RowData[] result = new RowData[260];
							for (int i = 0; i < result.length; i++) {
								result[i] = (RowData) analyseTool.mergeByIndex(data, i + 1);
							}
							GameStatService.getInstance().saveData(RowData.class.getName(), DateUtil.formatDate(new Date(lastRecordPlayerStat), "yyyy-MM-dd"), k, result);

						}

						RowData[] result = new RowData[260];
						for (int i = 0; i < result.length; i++) {
							result[i] = (RowData) analyseTool.mergeByIndex(allDatas, i + 1);
						}
						GameStatService.getInstance().saveData(RowData.class.getName(), DateUtil.formatDate(new Date(lastRecordPlayerStat), "yyyy-MM-dd"), GameStatService.userTypeNames.length - 1, result);

						if (logger.isInfoEnabled()) logger.info("[玩家状态统计数据] [内存中数量：" + GameStatTool.map.size() + "] [{}ms]");

						GameStatTool.map.clear();
					}
					lastRecordPlayerStat = SystemTime.currentTimeMillis();
				}

				if (logger.isInfoEnabled()) logger.info("[清除缓存中的玩家，数据保存心跳] [内存中数量：{}] [clear:{}] [{}ms]", new Object[] { idPlayerMap.size(), clearPlayerList.size(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now });

			} catch (Throwable e) {
				logger.error("[玩家数据心跳时发生异常]", e);
			}
		}
	}

	private String server1 = "";
	private String server2 = "";
	public static int oneTestMaxLevel = 40;

	public void loadOldPlayers(String server) throws Exception {

		File file = new File(server);
		HSSFWorkbook workbook = null;

		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		sheet = workbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		HSSFRow row = null;
		HSSFCell cell = null;

		for (int i = 1; i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {

				cell = row.getCell(1);
				String userName = "";
				try {
					userName = cell.getStringCellValue();
				} catch (Exception e) {
					userName = String.valueOf((long) cell.getNumericCellValue());
				}
				cell = row.getCell(3);
				int level = (int) cell.getNumericCellValue();
				if (level >= oneTestMaxLevel) {
					满40的老玩家账号.add(userName);
				} else {
					没有满40的老玩家账号.add(userName);
				}
			}
		}
		logger.warn("[老玩家加载完毕] [" + server + "] [满40玩家 " + 满40的老玩家账号.size() + "] [没有满40玩家 " + 没有满40的老玩家账号.size() + "]");
	}

	public void setServer1(String server1) {
		this.server1 = server1;
	}

	public void setServer2(String server2) {
		this.server2 = server2;
	}

	@Override
	public int getPlayerCountByCountry(int countryType) {
		try {
			return (int) em.count(Player.class, "country = ?", new Object[] { countryType });
		} catch (Exception e) {
			logger.error("[根据国家得到玩家数量] [异常]", e);
		}
		return 0;
	}

	@Override
	public long[] getPlayerIdsByLastingRequestTime(long lastRequestTime) {

		try {
			return em.queryIds(Player.class, "lastRequestTime >= ?", new Object[] { lastRequestTime });
		} catch (Exception e) {
			logger.error("[根据最后一次更新时间得到玩家ids] [异常]", e);
		}
		return new long[0];
	}

	@Override
	public long getPlayerNumByLastingRequestTime(int country, long fromLastRequestTime, long toLastRequestTime) {
		try {
			return em.count(Player.class, "country = ? and lastRequestTime >= ? and lastRequestTime <= ?", new Object[] { country, fromLastRequestTime, toLastRequestTime });
		} catch (Exception e) {
			logger.error("[根据国家和最后一次更新时间得到玩家数量] [异常]", e);
		}
		return 0;
	}

	public static SoulData souldatas[] = new SoulData[10];

	static {
		int[] levels = { 60, 110, 150, 190, 220, 221, 222, 223, 224, 225, 226 };
		for (int i = 0; i < levels.length; i++) {
			if (levels[i] >= 60 && levels[i] < 110) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0x78f4ff");
				souldata.setTitle(Translate.初识元神);
				souldata.setPercent(new int[] { 20, 21, 22, 23, 24 });
				souldatas[i] = souldata;
			} else if (levels[i] >= 110 && levels[i] < 150) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0x73f3ff");
				souldata.setTitle(Translate.心领神会);
				souldata.setPercent(new int[] { 25, 26, 27, 28, 29 });
				souldatas[i] = souldata;
			} else if (levels[i] >= 150 && levels[i] < 190) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0x71f1ff");
				souldata.setTitle(Translate.天衣无缝);
				souldata.setPercent(new int[] { 30, 31, 32, 33, 34 });
				souldatas[i] = souldata;
			} else if (levels[i] >= 190 && levels[i] < 220) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffE706EA");
				souldata.setTitle(Translate.登峰造极);
				souldata.setPercent(new int[] { 35, 36, 37, 38, 39 });
				souldatas[i] = souldata;
			} else if (levels[i] == 220) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.出神入化);
				souldata.setPercent(new int[] { 40, 41, 42, 43, 44 });
				souldatas[i] = souldata;
			} else if (levels[i] == 221) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.别有天地);
				souldata.setPercent(new int[] { 40, 41, 42, 43, 44 });
				souldatas[i] = souldata;
			} else if (levels[i] == 222) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.金丹换骨);
				souldata.setPercent(new int[] { 45, 46, 47, 48, 49 });
				souldatas[i] = souldata;
			} else if (levels[i] == 223) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.超凡入圣);
				souldata.setPercent(new int[] { 50, 51, 52, 53, 54 });
				souldatas[i] = souldata;
			} else if (levels[i] == 224) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.玄妙修神);
				souldata.setPercent(new int[] { 55, 56, 57, 58, 59 });
				souldatas[i] = souldata;
			} else if (levels[i] == 225) {
				SoulData souldata = new SoulData();
				souldata.setLevel(levels[i]);
				souldata.setColor("0xffFDA700");
				souldata.setTitle(Translate.一念通天);
				souldata.setPercent(new int[] { 60, 61, 62, 63, 64 });
				souldatas[i] = souldata;
			}
		}
	}

	


	
	public static void sendErrorMail(Player p, String msg) {
		String serverName = GameConstants.getInstance().getServerName();
		long si = p.getSilver();
		long ji = p.getChargePoints();
		long exp = p.getExp();
		long wage = p.getWage();
		if (EnterLimitManager.isCheckSilver) {
			String content = msg;
			GameConstants gc = GameConstants.getInstance();
			content += "[飘渺寻仙曲:游戏服]";
			StringBuffer sb = new StringBuffer();
			sb.append(content);
			sb.append("<br>");
			sb.append("玩家信息:").append(p.getLogString()).append(" [银子]").append(si).append(" [积分]").append(ji).append(" [经验]").append(exp).append(" [工资]").append(wage).append(" 加密信息:").append(Arrays.toString(p.getGameMsgs()));
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
			args.add("3472335707@qq.com,116004910@qq.com");
			args.add("-subject");
			args.add("[飘渺寻仙曲] [玩家关键数据异常] [平台 " + PlatformManager.getInstance().getPlatform().getPlatformName() + "] [" + gc.getServerName() + "]");
			args.add("-message");
			args.add(sb.toString());
			args.add("-contenttype");
			args.add("text/html;charset=gbk");
			try {
				JavaMailUtils.sendMail(args.toArray(new String[0]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.warn("数据验证不正确: [{}] 原因[{}] [{}] 银子[{}] 积分[{}] 经验[{}] 工资[{}]", new Object[] { p.getLogString(), msg, serverName, si, ji, exp, wage });
	}

	public static void createPlayerMsg(Player p, int type, boolean isSave) {
		try {
			long vv = 0L;
			if (type == 0) {
				vv = p.getSilver();
			} else if (type == 1) {
				vv = p.getChargePoints();
			} else if (type == 2) {
				vv = p.getExp();
			} else if (type == 3) {
				vv = p.getWage();
			}

			String old = p.getGameMsgs()[type];
			p.getGameMsgs()[type] = createMsg(p.getId(), vv, type);
			p.setDirty(true, "gameMsgs");
			if (isSave) {
				try {
					PlayerManager.getInstance().savePlayer(p);
				} catch (Exception e1) {
//					logger.error("保存出错: [" + p.getLogString() + "]", e1);
				}
			}
			logger.warn("[createPlayerMsg] [{}] [类型{}] [值{}] [{}-{}]", new Object[] { p.getLogString(), type, vv, old, p.getGameMsgs()[type] });
		} catch (Exception e) {
			logger.error("ggg出错了--type:"+type, e);
		}
	}

	public static String createMsg(long one, long two, int type) {
		String md5 = StringUtil.hash(two + "");
		int rv = (int) one % 1000;
		for (int i = 0; i < md5.length(); i++) {
			if (type == 0) {
				rv += ((int) md5.charAt(i)) * i - i;
			} else if (type == 1) {
				rv += ((int) md5.charAt(i)) * i / 2 + i * 3;
			} else if (type == 2) {
				rv += ((int) md5.charAt(i)) * i / 3 + i * 2;
			} else if (type == 3) {
				rv += ((int) md5.charAt(i)) * i + i;
			}
		}
		return rv + "";
	}

	public static void checkPlayerMsgs(Player p) {
		// try {
		// long si = p.getSilver();
		// String siMsg = createMsg(p.getId(), si, 0);
		// long ji = p.getChargePoints();
		// String jiMsg = createMsg(p.getId(), ji, 1);
		// long ex = p.getExp();
		// String exMsg = createMsg(p.getId(), ex, 2);
		// long gz = p.getWage();
		// String gzMsg = createMsg(p.getId(), gz, 3);
		// if (!siMsg.equals(p.getGameMsgs()[0])) {
		// sendErrorMail(p, "用户银子比较不正确。。。。。。");
		// }
		// if (!jiMsg.equals(p.getGameMsgs()[1])) {
		// sendErrorMail(p, "用户积分比较不正确。。。。。。");
		// }
		// if (!exMsg.equals(p.getGameMsgs()[2])) {
		// sendErrorMail(p, "用户经验比较不正确。。。。。。");
		// }
		// if (!gzMsg.equals(p.getGameMsgs()[3])) {
		// sendErrorMail(p, "用户工资工资比较不正确。。。。。。");
		// }
		// logger.warn("checkPlayerMsgs [{}] 银子[{},{}-{}] 积分[{},{}-{}] 经验[{},{}-{}] 工资[{},{}-{}]", new Object[] { p.getLogString(), si, siMsg, p.getGameMsgs()[0], ji, jiMsg,
		// p.getGameMsgs()[1], ex, exMsg, p.getGameMsgs()[2], gz, gzMsg, p.getGameMsgs()[3] });
		// } catch (Exception e) {
		// logger.error("checkPlayerMsgs出错:", e);
		// }
	}

	public File getCombatFValuesFile() {
		return combatFValuesFile;
	}

	public void setCombatFValuesFile(File combatFValuesFile) {
		this.combatFValuesFile = combatFValuesFile;
	}

}
