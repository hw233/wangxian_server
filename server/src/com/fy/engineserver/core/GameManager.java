package com.fy.engineserver.core;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.activity.village.manager.VillageFightManager;
import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.MultiLanguageTranslateManager;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.gateway.GameNetworkFramework;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 管理所有的GameWorld和GameInfo
 * 
 */
public class GameManager {

	public static Logger logger = LoggerFactory.getLogger(Game.class);

	public static final String[][] 同一复活点的多个地图名字集合 = new String[][] 
	                                                             {{"jiaoshacun"},{"wanfayiji"},{"wanfazhiyuan","wanfamidao","fayuandadao","wanfayaosai","wanfayuan","bianjing"},
																{"linshuangcun"},{"kunhuagucheng"},{"kunlunshengdian","kunlunmidao","shengtianzhidao","kunlunyaosai","tianquedian","bianjing"},
																{"taoyuancun"}, {"shangzhouxianjing"},{ "jiuzhoutiancheng","xiangong","jiuzhoumidao","jiuzhoutianlu","jiuzhouyaosai","bianjing" },
																{"miemoshenyu"}};
	public static final String[] 复活地图名字集合 = new String[] {"jiaoshacun","wanfayiji","wanfazhiyuan",
																"linshuangcun","kunhuagucheng","kunlunshengdian",
																"taoyuancun", "shangzhouxianjing","jiuzhoutiancheng",
																"miemoshenyu"};
	protected File configFile;
	/** 达成成就所需探索的地图 */
	public static final String[] achievementMapnames = { "linshuangcun", "kunhuagucheng", "xiaoyaojin", "xiaoyinsi", "baicaojing", "mizonglin", "tongboshan", "motianya", "kunlunshengdian", "huanggong", "wangchengmidao", "kunlunyaosai","wanfayaosai","jiuzhouyaosai", "shengtianzhidao","fayuandadao","jiuzhoutianlu", "bianjing", "miemoshenyu", "juechenling", "fengshentai", "feishengya", "yulongpu", "bolangsha", "huishenggu", "canglangshui", "fenglingdu", "yunmengze", "lushenmudi", "diyumoyuan", "guidou", "ranshaoshengdian", "lianyujuedi", "yunboguiling", "huawaizhijing", "donghaizhibin", "taihuashenshan", "fengxuezhidian", "taihuazhiding", "wentianlingtai", "donghailonggong", "youhaimijing", "shilianzhitayiceng", };
	String webappDir;

	protected File monsterBornPointFile;
	protected File npcBornPointFile;
	protected File worldMapInfoFile;

	protected GameNetworkFramework framework;

	protected boolean checkPath = true;
	protected boolean usingBucket = true;
	protected int bucketWidth = 256;
	protected int bucketHeight = 128;

	protected boolean enableHeartBeatStat = false;
	protected boolean enableCollectStat = false;

	WorldMapInfo worldMapInfo;

	/**
	 * 为什么要使用LinkedHashMap？
	 */
	// protected LinkedHashMap<String, GameInfo> giMap = new LinkedHashMap<String, GameInfo>();

	protected LinkedHashMap<String, Game> gameMap = new LinkedHashMap<String, Game>();

	protected LinkedHashMap<String, Game> displayGameMap = new LinkedHashMap<String, Game>();

	protected LinkedHashMap<String, Handset> handsetMap = new LinkedHashMap<String, Handset>();

	protected LinkedHashMap<String, Integer> topoDistanceMap = new LinkedHashMap<String, Integer>();

	/**
	 * 显示名字
	 */
	public ArrayList<String> 非中立的国家地图 = new ArrayList<String>();
	public PlayerManager playerManager;
	public MonsterManager monsterManager;
	public CareerManager careerManager;
	public NPCManager npcManager;

	static GameManager instance;

	int defaultWorldMapX = 10, defaultWorldMapY = 10;

	public Point getLocationInWorldMap(String mapName) {
		if (worldMapInfo != null) {
			WorldMapLocation wml = worldMapInfo.getWorldMapLocation(mapName);
			if (wml != null) {
				return new Point(wml.getX(), wml.getY());
			}
		}

		return new Point(defaultWorldMapX, defaultWorldMapY);
	}

	public WorldMapInfo getWorldMapInfo() {
		return worldMapInfo;
	}

	public void setWorldMapInfo(WorldMapInfo wi) {
		this.worldMapInfo = wi;
	}

	public static GameManager getInstance() {
		return instance;
	}

	public File getWorldMapInfoFile() {
		return worldMapInfoFile;
	}

	public void setWorldMapInfoFile(File worldMapInfoFile) {
		this.worldMapInfoFile = worldMapInfoFile;
	}

	public NPCManager getNpcManager() {
		return npcManager;
	}

	public void setNpcManager(NPCManager npcManager) {
		this.npcManager = npcManager;
	}

	public MonsterManager getMonsterManager() {
		return monsterManager;
	}

	public void setMonsterManager(MonsterManager monsterManager) {
		this.monsterManager = monsterManager;
	}

	public GameInfo[] getGameInfos() {
		return ResourceManager.getInstance().getGameInfos();
	}

	// public void addGameInfo(GameInfo gi) {
	// if (giMap.containsKey(gi.name) == false) {
	// giMap.put(gi.name, gi);
	// }
	// }

	// public void removeGameInfo(GameInfo gi) {
	// giMap.remove(gi.name);
	// }

	public GameInfo getGameInfo(String name) {
		return ResourceManager.getInstance().getGameInfo(name, "");
	}

	public Handset getHandset(String name) {
		return handsetMap.get(name);
	}

	public void addHandset(Handset set) {
		if (!handsetMap.containsKey(set.getName())) {
			handsetMap.put(set.getName(), set);
		}
	}

	public void removeHandset(String name) {
		handsetMap.remove(name);
	}

	public Handset[] getHandsets() {
		return handsetMap.values().toArray(new Handset[0]);
	}

	/**
	 * 只有确认不是副本地图时，此方法才可以用
	 * country默认为0中立地图
	 * 游戏中有3个国家，country编号为1 2 3
	 * 
	 * @param name
	 * @return
	 */
	public Game getGameByName(String name, int country) {
		String names = name + country;
		Game g = gameMap.get(names);
		if (g != null && g.country == country) {
			return g;
		}
		return null;
	}

	//
	// public LinkedHashMap<String, Game> getDisplayGameMap() {
	// return displayGameMap;
	// }

	public Game[] getGames() {
		return gameMap.values().toArray(new Game[0]);
	}

	// public Game getGameByIndex(int index) {
	// GameInfo gis[] = getGameInfos();
	// if (index < 0 || index >= gis.length) return null;
	// return this.getGameByName(gis[index].name);
	// }

	public Game getRandomGame() {
		Game games[] = gameMap.values().toArray(new Game[0]);
		if (games.length > 0) {
			return games[new java.util.Random().nextInt(games.length)];
		}
		return null;
	}

	private ResourceManager resourceManager;

	String gamemapInCountryFileName;

	public String getGamemapInCountryFileName() {
		return gamemapInCountryFileName;
	}

	public void setGamemapInCountryFileName(String gamemapInCountryFileName) {
		this.gamemapInCountryFileName = gamemapInCountryFileName;
	}

	/**
	 * 解析XML，装置所有的游戏以及游戏的资源
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		load();
		File file = new File(gamemapInCountryFileName);
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		加载国家地图标记(file);

		GameInfo[] gameInfos = getGameInfos();

		for (int i = 0; i < gameInfos.length; i++) {
			GameInfo gi = gameInfos[i];

			if (非中立的国家地图 != null && 非中立的国家地图.contains(gi.displayName)) {
				for (int j = 1; j <= 3; j++) {
					Game game = new Game(this, gameInfos[i]);
					game.country = (byte) j;
					gameMap.put(game.gi.name + j, game);
					displayGameMap.put(game.gi.displayName + j, game);
					if (this.playerManager instanceof PlayerInOutGameListener) {
						game.addPlayerInOutGameListener((PlayerInOutGameListener) this.playerManager);
					}
					if (logger.isInfoEnabled()) logger.info("[addGame] [{}] [{}]", new Object[] { game.gi.name, (game.gi.displayName + j) });
					game.init();
				}
			} else {
				Game game = new Game(this, gameInfos[i]);
				gameMap.put(game.gi.name + CountryManager.中立, game);
				displayGameMap.put(game.gi.displayName + CountryManager.中立, game);
				if (this.playerManager instanceof PlayerInOutGameListener) {
					game.addPlayerInOutGameListener((PlayerInOutGameListener) this.playerManager);
				}
				if (logger.isInfoEnabled()) logger.info("[addGame] [{}] [{}]", new Object[] { game.gi.name, (game.gi.displayName + CountryManager.中立) });
				game.init();
			}

		}
		// for (int i = 0; i < gameInfos.length; i++) {
		// Game game = gameMap.get(gameInfos[i].name);
		// game.init();
		// }

		int careerNum = InitialPlayerConstant.各门派的名称.length;
		// for (int i = 0; i < careerNum; i++) {
		// String gameName = InitialPlayerConstant.日月盟各门派的出生地图[i];
		// String regionName = InitialPlayerConstant.日月盟各门派的出生地图的区域[i];
		// GameInfo gi = this.getGameInfo(gameName);
		// if (gi == null) {
		// throw new Exception("人物出生数据配置出错，" + InitialPlayerConstant.各门派的名称[i] + "门派配置的出生地图[" + gameName + "]不存在！");
		// }
		// MapArea m = gi.getMapAreaByName(regionName);
		// if (m == null) {
		// throw new Exception("人物出生数据配置出错，" + InitialPlayerConstant.各门派的名称[i] + "门派配置的出生地图[" + gameName + "]中的出生区域[" + regionName + "]不存在！");
		// }
		//
		// gameName = InitialPlayerConstant.紫薇宫各门派的出生地图[i];
		// regionName = InitialPlayerConstant.紫薇宫各门派的出生地图的区域[i];
		// gi = this.getGameInfo(gameName);
		// if (gi == null) {
		// throw new Exception("人物出生数据配置出错，" + InitialPlayerConstant.各门派的名称[i] + "门派配置的出生地图[" + gameName + "]不存在！");
		// }
		// m = gi.getMapAreaByName(regionName);
		// if (m == null) {
		// throw new Exception("人物出生数据配置出错，" + InitialPlayerConstant.各门派的名称[i] + "门派配置的出生地图[" + gameName + "]中的出生区域[" + regionName + "]不存在！");
		// }
		// }

		constructTopo();

		instance = this;

		ServiceStartRecord.startLog(this);
	}

	// public static String[] countryMapNames = new
	public void 加载国家地图标记(File file) throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = workbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				VillageFightManager.logger.warn("name:" + sheet.getRow(i).getCell(0).getStringCellValue().trim());
				非中立的国家地图.add((sheet.getRow(i).getCell(0).getStringCellValue().trim()));
			}
		}
	}

	private void constructTopo() {
		GameInfo gis[] = getGameInfos();
		String mapNames[] = new String[gis.length];
		byte neighberNums[] = new byte[gis.length];
		int count = 0;
		for (int i = 0; i < gis.length; i++) {
			mapNames[i] = gis[i].getName();
			neighberNums[i] = (byte) gis[i].getTransports().length;
			count += neighberNums[i];
		}
		short neighbers[] = new short[count];
		int index = 0;
		for (int i = 0; i < gis.length; i++) {
			TransportData tds[] = gis[i].getTransports();
			for (int j = 0; j < tds.length; j++) {
				String n = tds[j].getTargetMapName();
				for (int l = 0; l < mapNames.length; l++) {
					if (mapNames[l].equals(n)) {
						neighbers[index] = (short) l;
						break;
					}
				}
				index++;
			}
		}
		TopologicalDiagram td = new TopologicalDiagram(mapNames, neighberNums, neighbers);

		for (int i = 0; i < mapNames.length; i++) {
			for (int j = 0; j < mapNames.length; j++) {
				if (i == j) {
					topoDistanceMap.put(mapNames[i] + "->" + mapNames[j], 0);
				} else {
					String path[] = td.findPath(mapNames[i], mapNames[j]);
					if (path.length == 0) {// 不通
						topoDistanceMap.put(mapNames[i] + "->" + mapNames[j], -1);
					} else {
						topoDistanceMap.put(mapNames[i] + "->" + mapNames[j], path.length);
					}
				}
			}
		}
	}

	/**
	 * 获得两个地图之间的距离，所谓的距离就是跳地图几次 -1标识不通。
	 * @param mapA
	 * @param mapB
	 * @return
	 */
	public int getDinstanceFromA2B(String mapA, String mapB) {
		Integer i = topoDistanceMap.get(mapA + "->" + mapB);
		if (i == null) {
			return -1;
		}
		return i.intValue();
	}

	/**
	 * 配置文件的格式： <game-manager> <game-worlds> <game-world name=''/> <game-world
	 * name=''/> </game-worlds> <game-infos> <game-info name='' max-player=''
	 * datafile=''/> <game-info name='' max-player='' datafile=''/>
	 * </game-infos> <handsets> <handset id='' view-width='' view-height=''/>
	 * </handsets> </game-manager>
	 * 
	 * @throws Exception
	 */
	protected void load() throws Exception {
		if (configFile == null || webappDir == null) throw new Exception("configFile is null");
		///
		
		File configNewFile = new File(ConfigServiceManager.getInstance().getFilePath(configFile));
		if(configNewFile!=null&&configNewFile.exists()){
			configFile = configNewFile;
		}
		Document dom = XmlUtil.load(configFile.getAbsolutePath() + File.separator + "games.xml", "utf-8");
		Element root = dom.getDocumentElement();

		Element eles[] = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root, "handsets"), "handset");
		for (int i = 0; i < eles.length; i++) {
			Element e = eles[i];
			String name = XmlUtil.getAttributeAsString(e, "name", null, TransferLanguage.getMap());
			int viewWidth = XmlUtil.getAttributeAsInteger(e, "view-width", 320);
			int viewHeight = XmlUtil.getAttributeAsInteger(e, "view-height", 480);
			if (name != null) {
				Handset hs = new Handset();
				hs.name = name;
				hs.viewWidth = viewWidth;
				hs.viewHeight = viewHeight;
				this.addHandset(hs);
			}
		}

		eles = XmlUtil.getChildrenByName(XmlUtil.getChildByName(root, "game-infos"), "game-info");
		for (int i = 0; i < eles.length; i++) {
		}
		monsterBornPointFile = new File(ConfigServiceManager.getInstance().getFilePath(monsterBornPointFile) + File.separator + "bornpoints.xml");
		loadMonsterBornPointFile(monsterBornPointFile);

		if (npcBornPointFile != null && npcBornPointFile.exists()) {
			npcBornPointFile = new File(ConfigServiceManager.getInstance().getFilePath(npcBornPointFile) + File.separator + "mapnpcs.xml");
			loadNPCBornPointFile(npcBornPointFile);
		} else {
			System.out.println("提示：GameManager没有加载NPC出生点数据，NPC出生点数据文件[" + npcBornPointFile + "]不存在！");
		}
		worldMapInfoFile = new File(ConfigServiceManager.getInstance().getFilePath(worldMapInfoFile));
		loadWorldMapInfoFile(worldMapInfoFile);
	}

	void loadWorldMapInfoFile(File dataFile) throws Exception {
		if (dataFile.exists() && dataFile.isFile()) {
			Document document = XmlUtil.load(dataFile.getCanonicalPath());
			Element de = document.getDocumentElement();
			Element e = XmlUtil.getStrictChildByName(de, "worldmap");

			String name = XmlUtil.getAttributeAsString(e, "name", TransferLanguage.getMap());

			worldMapInfo = new WorldMapInfo();
			worldMapInfo.setMapName(name);

			Element[] es = XmlUtil.getChildrenByName(e, "map");
			for (int j = 0; j < es.length; j++) {
				Element e2 = es[j];
				String mn = XmlUtil.getAttributeAsString(e2, "name", TransferLanguage.getMap());
				int x = XmlUtil.getAttributeAsInteger(e2, "x");
				int y = XmlUtil.getAttributeAsInteger(e2, "y");

				WorldMapLocation wl = new WorldMapLocation();
				wl.setMapName(mn);
				wl.setX(x);
				wl.setY(y);

				worldMapInfo.ls.add(wl);
			}

		}
	}

	/**
	 * 重新加载怪物出生点数据文件
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void reloadMonsterBornPointFile(File file) throws Exception {
		monsterBornPointFile = file;
		InputStream is = new FileInputStream(file);
		reloadMonsterBornPointFileFromInputStream(is);
		is.close();
	}

	public void reloadMonsterBornPointFileFromInputStream(InputStream is) throws Exception {
		loadMonsterBornPointFileFromInputStream(is);
		Game games[] = gameMap.values().toArray(new Game[0]);
		for (int i = 0; i < games.length; i++) {
			Game game = games[i];
			if (game.gi.monsterBornEle != null) {
				MonsterFlushAgent m = new MonsterFlushAgent(game, game.gi.monsterBornEle);
				m.load();
				if (game.spriteFlushAgent != null) game.spriteFlushAgent.clear();
				game.spriteFlushAgent = m;
			}
		}
		XinShouChunFuBenManager xscfbm = XinShouChunFuBenManager.getInstance();
		if (xscfbm != null) {
			Game[][] gamess = xscfbm.games;
			if (gamess != null) {
				for (int j = 0; j < gamess.length; j++) {
					Game[] gams = gamess[j];
					if (gams != null) {
						for (int i = 0; i < gams.length; i++) {
							Game game = gams[i];
							if (game.gi.monsterBornEle != null) {
								MonsterFlushAgent m = new MonsterFlushAgent(game, game.gi.monsterBornEle);
								m.load();
								if (game.spriteFlushAgent != null) game.spriteFlushAgent.clear();
								game.spriteFlushAgent = m;
							}
						}
					}
				}
			}
		}
		DownCityManager dcm = DownCityManager.getInstance();
		if (dcm != null) {
			DownCity[] dcs = dcm.getAllDownCity();
			if (dcs != null) {
				for (int j = 0; j < dcs.length; j++) {
					DownCity dc = dcs[j];
					if (dc != null) {
						Game game = dc.getGame();
						if (game != null) {
							if (game.gi.monsterBornEle != null) {
								MonsterFlushAgent m = new MonsterFlushAgent(game, game.gi.monsterBornEle);
								m.load();
								if (game.spriteFlushAgent != null) game.spriteFlushAgent.clear();
								game.spriteFlushAgent = m;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 重新加载NPC出生点数据文件
	 * @param file
	 * @throws Exception
	 */
	public void reloadNPCBornPointFile(File file) throws Exception {
		npcBornPointFile = file;
		InputStream is = new FileInputStream(file);
		reloadNPCBornPointFileFromInputStream(is);
		is.close();
	}

	public void reloadNPCBornPointFileFromInputStream(InputStream is) throws Exception {

		loadNPCBornPointFileFromInputStream(is);
		Game games[] = gameMap.values().toArray(new Game[0]);
		for (int i = 0; i < games.length; i++) {
			Game game = games[i];
			if (game.gi.npcBornEle != null) {
				NPCFlushAgent m = new NPCFlushAgent(game, game.gi.npcBornEle);
				m.load();
				if (game.npcFlushAgent != null) game.npcFlushAgent.clear();
				game.npcFlushAgent = m;
			}

		}
		XinShouChunFuBenManager xscfbm = XinShouChunFuBenManager.getInstance();
		if (xscfbm != null) {
			Game[][] gamess = xscfbm.games;
			if (gamess != null) {
				for (int j = 0; j < gamess.length; j++) {
					Game[] gams = gamess[j];
					if (gams != null) {
						for (int i = 0; i < gams.length; i++) {
							Game game = gams[i];
							if (game.gi.npcBornEle != null) {
								NPCFlushAgent m = new NPCFlushAgent(game, game.gi.npcBornEle);
								m.load();
								if (game.npcFlushAgent != null) game.npcFlushAgent.clear();
								game.npcFlushAgent = m;
							}
						}
					}
				}
			}
		}
		DownCityManager dcm = DownCityManager.getInstance();
		if (dcm != null) {
			DownCity[] dcs = dcm.getAllDownCity();
			if (dcs != null) {
				for (int j = 0; j < dcs.length; j++) {
					DownCity dc = dcs[j];
					if (dc != null) {
						Game game = dc.getGame();
						if (game != null) {
							if (game.gi.npcBornEle != null) {
								NPCFlushAgent m = new NPCFlushAgent(game, game.gi.npcBornEle);
								m.load();
								if (game.npcFlushAgent != null) game.npcFlushAgent.clear();
								game.npcFlushAgent = m;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * <sprite_born_points>
	 * <game name=''>
	 * <sprite categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </sprite>
	 * <sprite categoryId=''>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * <bornpoint x='' y=''/>
	 * </sprite>
	 * </game>
	 * </sprite_born_points>
	 * @param file
	 * @throws Exception
	 */
	protected void loadMonsterBornPointFile(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		loadMonsterBornPointFileFromInputStream(is);
		is.close();
	}

	protected void loadMonsterBornPointFileFromInputStream(InputStream is) throws Exception {
		Document dom = XmlUtil.load(is);

		Element gameEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(), "game");
		for (int i = 0; i < gameEles.length; i++) {
			Element e = gameEles[i];
			String name = XmlUtil.getAttributeAsString(e, "name", TransferLanguage.getMap());
			name = MultiLanguageTranslateManager.languageTranslate(name);
			GameInfo gi = getGameInfo(name);
			if (gi != null) {
				gi.monsterBornEle = e;
			} else {
				throw new Exception("Game for Name [" + name + "] not exists!");
			}
		}
	}

	protected void loadNPCBornPointFile(File file) throws Exception {
		InputStream is = new FileInputStream(file);
		loadNPCBornPointFileFromInputStream(is);
		is.close();
	}

	protected void loadNPCBornPointFileFromInputStream(InputStream is) throws Exception {
		Document dom = XmlUtil.load(is);

		Element gameEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(), "game");
		for (int i = 0; i < gameEles.length; i++) {
			Element e = gameEles[i];
			String name = XmlUtil.getAttributeAsString(e, "name", TransferLanguage.getMap());
			name = MultiLanguageTranslateManager.languageTranslate(name);
			GameInfo gi = getGameInfo(name);
			if (gi != null) {
				gi.npcBornEle = e;
			} else {
				throw new Exception("Game for Name [" + name + "] not exists!");
			}
		}
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public GameNetworkFramework getGameNetworkFramework() {
		return framework;
	}

	public void setGameNetworkFramework(GameNetworkFramework framework) {
		this.framework = framework;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public boolean isCheckPath() {
		return checkPath;
	}

	public void setCheckPath(boolean checkPath) {
		this.checkPath = checkPath;
	}

	public int getBucketWidth() {
		return bucketWidth;
	}

	public void setBucketWidth(int bucketWidth) {
		this.bucketWidth = bucketWidth;
	}

	public int getBucketHeight() {
		return bucketHeight;
	}

	public void setBucketHeight(int bucketHeight) {
		this.bucketHeight = bucketHeight;
	}

	public boolean isUsingBucket() {
		return usingBucket;
	}

	public void setUsingBucket(boolean usingBucket) {
		this.usingBucket = usingBucket;
	}

	public boolean isEnableHeartBeatStat() {
		return enableHeartBeatStat;
	}

	public void setEnableHeartBeatStat(boolean enableHeartBeatStat) {
		this.enableHeartBeatStat = enableHeartBeatStat;
	}

	public boolean isEnableCollectStat() {
		return enableCollectStat;
	}

	public void setEnableCollectStat(boolean enableCollectStat) {
		this.enableCollectStat = enableCollectStat;
	}

	public File getMonsterBornPointFile() {
		return monsterBornPointFile;
	}

	public void setMonsterBornPointFile(File monsterBornPointFile) {
		this.monsterBornPointFile = monsterBornPointFile;
	}

	public File getNpcBornPointFile() {
		return npcBornPointFile;
	}

	public void setNpcBornPointFile(File npcBornPointFile) {
		this.npcBornPointFile = npcBornPointFile;
	}

	public CareerManager getCareerManager() {
		return careerManager;
	}

	public void setCareerManager(CareerManager careerManager) {
		this.careerManager = careerManager;
	}

	/**
	 * 获得某张地图上所有可能的怪物和NPC
	 * 
	 * @param mapName
	 * @return
	 */
	public short[] getAllSpriteTypeOnGame(String mapName) {
		return new short[0];
	}

	public String getWebappDir() {
		return webappDir;
	}

	public void setWebappDir(String webappDir) {
		this.webappDir = webappDir;
	}

	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * 通过资源名获得显示名字
	 * @param resName
	 * @return
	 */
	public String getDisplayName(String resName, int country) {
		Game game = getGameByName(resName, country);
		if (game != null) {
			return game.getGameInfo().displayName;
		}
		return null;
	}

	/**
	 * 通过显示名字获得资源名
	 * @param displayName
	 * @return
	 */
	public String getResName(String displayName, int country) {
		String dis = displayName + country;
		Game game = displayGameMap.get(dis);
		if (game != null) {
			return game.getGameInfo().name;
		}
		return null;
	}

	/**
	 * 通过显示名字获得game
	 * @param displayName
	 * @return
	 */
	public Game getGameByDisplayName(String displayName, int country) {
		String dis = displayName + country;
		Game game = displayGameMap.get(dis);
		if (game != null && game.country == country) {
			return game;
		}
		return null;
	}
}
