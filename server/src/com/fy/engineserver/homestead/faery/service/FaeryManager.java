package com.fy.engineserver.homestead.faery.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_chant_slow;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.FaeryBeatHeartThread;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveBuilding;
import com.fy.engineserver.homestead.cave.CaveDoorplate;
import com.fy.engineserver.homestead.cave.CaveFence;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.cave.CaveFieldBombConfig;
import com.fy.engineserver.homestead.cave.CaveMainBuilding;
import com.fy.engineserver.homestead.cave.CavePethouse;
import com.fy.engineserver.homestead.cave.CavePosition;
import com.fy.engineserver.homestead.cave.CaveStorehouse;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.cave.resource.BuildingOutShowCfg;
import com.fy.engineserver.homestead.cave.resource.ConvertArticleCfg;
import com.fy.engineserver.homestead.cave.resource.DropsArticle;
import com.fy.engineserver.homestead.cave.resource.FenceCfg;
import com.fy.engineserver.homestead.cave.resource.FieldAssartCfg;
import com.fy.engineserver.homestead.cave.resource.FieldLvUpCfg;
import com.fy.engineserver.homestead.cave.resource.HitAreaCfg;
import com.fy.engineserver.homestead.cave.resource.IncreaseScheduleCfg;
import com.fy.engineserver.homestead.cave.resource.MainBuildingCfg;
import com.fy.engineserver.homestead.cave.resource.PetHouseCfg;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.homestead.cave.resource.PointCfg;
import com.fy.engineserver.homestead.cave.resource.ResourceLevelCfg;
import com.fy.engineserver.homestead.cave.resource.StorehouseCfg;
import com.fy.engineserver.homestead.exceptions.AlreadyHasCaveException;
import com.fy.engineserver.homestead.exceptions.BuildingCanNotLvUpException;
import com.fy.engineserver.homestead.exceptions.CountyNotSameException;
import com.fy.engineserver.homestead.exceptions.FertyNotFountException;
import com.fy.engineserver.homestead.exceptions.IndexHasBeUsedException;
import com.fy.engineserver.homestead.exceptions.LevelToolowException;
import com.fy.engineserver.homestead.exceptions.OutOfMaxLevelException;
import com.fy.engineserver.homestead.exceptions.PointNotFoundException;
import com.fy.engineserver.homestead.faery.Faery;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.message.CAVE_SIMPLE_REQ;
import com.fy.engineserver.message.CAVE_SIMPLE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class FaeryManager implements FaeryConfig {

	public static SimpleEntityManager<Cave> caveEm;
	public static SimpleEntityManager<Faery> faeryEm;

	static Random random = new Random();
	/** 开始有维护费的时间 */
	public final static int maintenanceGrade = 5;
	private static FaeryManager instance;
	private String filePath = "";

	private boolean running = true;

	public static Logger logger = LoggerFactory.getLogger(CaveSubSystem.class);

	private ShopManager shopManager;
	// private FaeryDao faeryDao;
	// private CaveDao caveDao;

	public static final String driveArticleName = Translate.玄兽丹;
	// 维护费用
	private MainBuildingCfg[] mainCfgs;
	private StorehouseCfg[] storeCfgs;
	private ResourceLevelCfg[] resLvCfg;
	private PetHouseCfg[] pethouseCfg;
	private FenceCfg[] fenceCfg;
	private FieldLvUpCfg[] fieldLvUpCfg;
	private FieldAssartCfg[] fieldAssartCfgs;
	private PointCfg[][] pointCfgs;// 第一维是 位置 第二维是每个建筑
	private HitAreaCfg[] hitAreaCfgs;//
	private BuildingOutShowCfg[] buildingOutShowCfgs;// index是建筑类型
	private double[] exchangeRate;// 各种颜色果实兑换概率
	private HashMap<Integer, Double> petHookExp;// /**各等级宠物挂机基础经验 */

	private List<CaveFieldBombConfig> bombConfigs = new ArrayList<CaveFieldBombConfig>();/* 仙府炸弹配置列表 */

	public final static String accelerateArticleName = Translate.鬼斧神工令;
	public final static long accelerateTime = 1000L * 60 * 60 * 6;

	public static long assartFieldArticlePrize;

	private MemoryNPCManager npcManager;
	/** 兑换物品规则 */
	private HashMap<Integer, List<ConvertArticleCfg>> convertMap = new HashMap<Integer, List<ConvertArticleCfg>>();

	/** 种植列表<种植类型,种植列表> 按所需土地等级排序 */
	private HashMap<Integer, List<PlantCfg>> plantMap = new HashMap<Integer, List<PlantCfg>>();

	private List<IncreaseScheduleCfg> increaseScheduleCfgs = new ArrayList<IncreaseScheduleCfg>();
	/** 种植列表<等级,种植列表> */
	private HashMap<Integer, List<PlantCfg>> plantGradeMap = new HashMap<Integer, List<PlantCfg>>();

	/** 每个国家的仙境列表 */
	private Hashtable<Integer, List<Faery>> countryMap = new Hashtable<Integer, List<Faery>>();

	private Hashtable<Long, Faery> faeryMap = new Hashtable<Long, Faery>();

	/** 角色仙府<PlayerId,Cave> */
	private Hashtable<Long, Cave> playerCave = new Hashtable<Long, Cave>();
	/** 角色仙府<CaveID,Cave> */
	private Hashtable<Long, Cave> caveIdmap = new Hashtable<Long, Cave>();

	/** 封印状态的列表 <角色ID,洞府ID> */
	private Hashtable<Long, Long> khatamMap = new Hashtable<Long, Long>();

	/** 负责心跳的线程 */
	private List<FaeryBeatHeartThread> threadPool = new ArrayList<FaeryBeatHeartThread>();

	static DecimalFormat df = new DecimalFormat("##.00%");

	/** 每个等级宠物仓库能存放宠物的最大数 */
	public static int[] petStoreNums;

	private GamePlayerManager playerManager;
	private JiazuManager jiazuManager;

	/**
	 * 创建一个空的仙境
	 * @param county
	 * @return
	 */
	public Faery createEmptyFaery(int county) {
		List<Faery> faerys = getCountryMap().get(Integer.valueOf(county));
		int nextIndex = faerys.get(faerys.size() - 1).getIndex() + 1;
		Faery faery = new Faery(county, nextIndex);
		return faery;
	}

	/**
	 * 为玩家创建一个新的洞府
	 * @param player角色
	 * @param faeryId仙境ID
	 * @param index仙境中的位置
	 * @return
	 * @throws PointNotFoundException
	 * @throws FertyNotFountException
	 * @throws CountyNotSameException
	 * @throws AlreadyHasCaveException
	 * @throws IndexHasBeUsedException
	 * @throws Exception
	 */
	public synchronized Cave createDefaultCave(Player player) throws PointNotFoundException, FertyNotFountException, CountyNotSameException, AlreadyHasCaveException, IndexHasBeUsedException, Exception {
		Cave checkCave = getCave(player);
		if (logger.isWarnEnabled()) {
			logger.warn(player.getLogString() + "[申请洞府] [原来是否有洞府:{}]", new Object[] { checkCave != null });
		}
		if (checkCave != null) {
			throw new AlreadyHasCaveException(checkCave.toString());
		}
		if (player.getLevel() < 20) {
			throw new LevelToolowException();
		}
		Cave cave = new Cave();
		try {
			cave.setId(FaeryManager.caveEm.nextId());
		} catch (Exception e1) {
			logger.error(player.getLogString() + "[申请洞府] [获取洞府ID失败]", e1);
			throw e1;
		}

		CompoundReturn cr = getRandomCavePosition(player.getCountry(), cave.getId());
		long faeryId = cr.getLongValue();
		int index = cr.getIntValue();
		if (faeryId == -1 || index == -1) {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[申请洞府] [返回的仙境ID:{}] [位置:{}]", new Object[] { faeryId, index });
			}
			return null;
		}

		Faery faery = getFaery(faeryId);
		if (faery == null) {
			throw new FertyNotFountException("[Player:" + player.getName() + "][faery:" + faeryId + "]");
		}
		if (faery.getCountry() != player.getCountry()) {
			throw new CountyNotSameException("[player.county:" + player.getCountry() + "][faery.county:" + faery.getCountry());
		}
		checkCave = faery.getCave(index);

		if (checkCave != null) {
			throw new IndexHasBeUsedException("[player.name:]" + player.getName() + "][index:" + index + "][checkCave:" + checkCave + "]");
		}

		player.markLastGameInfo();

		try {
			Point point = getPoint(index, CAVE_BUILDING_TYPE_MAIN);
			cave.setCurrRes(new ResourceCollection(DEFAULT_FOOD, DEFAULT_WOOD, DEFAULT_STONE));
			cave.setOwnerId(player.getId());
			cave.setOwnerName(player.getName());
			cave.setIndex(index);
			cave.setStatus(CAVE_STATUS_OPEN);
			cave.setFaery(faery);

			CaveMainBuilding main = new CaveMainBuilding();
			main.setGrade(1);
			main.setPoint(point);
			cave.setMainBuilding(main);

			point = getPoint(index, CAVE_BUILDING_TYPE_FENCE);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FENCE);
			}
			CaveFence fence = new CaveFence();
			fence.setGrade(1);
			fence.setStatus(FENCE_STATUS_CLOSE);
			fence.setPoint(point);
			cave.setFence(fence);

			point = getPoint(index, CAVE_BUILDING_TYPE_PETHOUSE);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_PETHOUSE);
			}
			CavePethouse pethouse = new CavePethouse();
			pethouse.setGrade(1);
			pethouse.setPoint(point);
			cave.setPethouse(pethouse);

			point = getPoint(index, CAVE_BUILDING_TYPE_DOORPLATE);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_DOORPLATE);
			}
			CaveDoorplate doorplate = new CaveDoorplate();
			doorplate.setGrade(1);
			doorplate.setPoint(point);
			cave.setDoorplate(doorplate);

			point = getPoint(index, CAVE_BUILDING_TYPE_STORE);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_STORE);
			}
			CaveStorehouse storehouse = new CaveStorehouse();
			storehouse.setGrade(1);
			storehouse.setPoint(point);
			storehouse.setFoodLevel(1);
			storehouse.setWoodLevel(1);
			storehouse.setStoneLevel(1);
			cave.setStorehouse(storehouse);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD1);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD1);
			}

			cave.setFields(new ArrayList<CaveField>());

			CaveField caveField1 = new CaveField(CAVE_BUILDING_TYPE_FIELD1);
			caveField1.setGrade(1);
			caveField1.setPoint(point);
			caveField1.setAssartStatus(FIELD_STATUS_UNPLANTING);
			cave.getFields().add(caveField1);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD2);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD2);
			}

			CaveField caveField2 = new CaveField(CAVE_BUILDING_TYPE_FIELD2);
			caveField2.setGrade(1);
			caveField2.setPoint(point);
			caveField2.setAssartStatus(FIELD_STATUS_UNPLANTING);

			cave.getFields().add(caveField2);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD3);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD3);
			}

			CaveField caveField3 = new CaveField(CAVE_BUILDING_TYPE_FIELD3);
			caveField3.setGrade(0);
			caveField3.setPoint(point);
			caveField3.setAssartStatus(FIELD_STATUS_DESOLATION);

			cave.getFields().add(caveField3);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD4);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD4);
			}

			CaveField caveField4 = new CaveField(CAVE_BUILDING_TYPE_FIELD4);
			caveField4.setGrade(0);
			caveField4.setPoint(point);
			caveField4.setAssartStatus(FIELD_STATUS_DESOLATION);
			cave.getFields().add(caveField4);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD5);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD5);
			}

			CaveField caveField5 = new CaveField(CAVE_BUILDING_TYPE_FIELD5);
			caveField5.setGrade(0);
			caveField5.setPoint(point);
			caveField5.setAssartStatus(FIELD_STATUS_DESOLATION);
			cave.getFields().add(caveField5);

			point = getPoint(index, CAVE_BUILDING_TYPE_FIELD6);
			if (point == null) {
				throw new PointNotFoundException("index = " + index + ",type = " + CAVE_BUILDING_TYPE_FIELD6);
			}

			CaveField caveField6 = new CaveField(CAVE_BUILDING_TYPE_FIELD6);
			caveField6.setGrade(0);
			caveField6.setAssartStatus(FIELD_STATUS_DESOLATION);
			cave.getFields().add(caveField6);

			// ----------------------------------------------------------------
			cave.onLoadInitNpc();

			cave.setOwnerId(player.getId());
			player.setFaeryId(faery.getId());
			player.setCaveId(cave.getId());

			faery.getCaveIds()[index] = cave.getId();
			faery.getCaves()[index] = cave;

			faeryEm.notifyFieldChange(faery, "caveIds");

			getPlayerCave().put(player.getId(), cave);
			getCaveIdmap().put(cave.getId(), cave);
			// getKhatamMap().remove(player.getId());
			caveEm.notifyNewObject(cave);

			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[申请仙府成功] [faeryId:{}] [index:{}] [caveId:{}]", new Object[] { faery.getId(), index, cave.getId() });
			}
		} catch (Exception e) {
			faery.clearCaveid(cave.getId());
			logger.error(player.getLogString() + "[创建仙府异常] [ID恢复" + cave.getId() + "]", e);

		}

		return cave;
	}

	/**
	 * 得到坐标信息
	 * @param index
	 * @param type
	 * @return
	 */
	public Point getPoint(int index, int type) {
		if (index >= getPointCfgs().length || index < 0) {
			return null;
		}
		PointCfg[] cfgs = getPointCfgs()[index];
		for (int i = 0; i < cfgs.length; i++) {
			PointCfg pc = cfgs[i];
			if (pc.getType() == type) {
				return pc.getPoint();
			}
		}
		return null;
	}

	/**
	 * 得到升级信息
	 * @param buildingType
	 * @param currLv
	 * @return
	 * @throws OutOfMaxLevelException
	 * @throws BuildingCanNotLvUpException
	 */
	public CompoundReturn getLvUpInfo(int buildingType, int currLv) throws OutOfMaxLevelException {
		if (currLv > BUILDING_MAX_LEVEL) {
			throw new OutOfMaxLevelException("Level:" + currLv);
		}
		ResourceCollection result = new ResourceCollection(0, 0, 0);
		int levelUpTime = 0;
		String levelUpDes = "";
		String currentLevelDes = "";

		switch (buildingType) {
		case CAVE_BUILDING_TYPE_MAIN:
			if (currLv < FaeryConfig.BUILDING_MAX_LEVEL) {
				result = getMainCfgs()[currLv].getLvUpCost();
				levelUpTime = getMainCfgs()[currLv].getLvUpTime();
				levelUpDes = getMainCfgs()[currLv].getLevelUpDes();
			}
			currentLevelDes = getMainCfgs()[currLv - 1].getCurrentLevelDes();
			break;
		case CAVE_BUILDING_TYPE_STORE:
			if (currLv < FaeryConfig.BUILDING_MAX_LEVEL) {
				result = getStoreCfgs()[currLv].getLvUpCost();
				levelUpTime = getStoreCfgs()[currLv].getLvUpTime();
				levelUpDes = getStoreCfgs()[currLv].getLevelUpDes();
			}
			if (currLv != 0) {
				currentLevelDes = getStoreCfgs()[currLv - 1].getCurrentLevelDes();
			}
			break;
		case CAVE_BUILDING_TYPE_PETHOUSE:
			if (currLv < FaeryConfig.BUILDING_MAX_LEVEL) {
				result = getPethouseCfg()[currLv].getLvUpCost();
				levelUpTime = getPethouseCfg()[currLv].getLvUpTime();
				levelUpDes = getPethouseCfg()[currLv].getLevelUpDes();
			}
			if (currLv != 0) {
				currentLevelDes = getPethouseCfg()[currLv - 1].getCurrentLevelDes();
			}
			break;
		case CAVE_BUILDING_TYPE_FENCE:
			if (currLv < FaeryConfig.BUILDING_MAX_LEVEL) {
				result = getFenceCfg()[currLv].getLvUpCost();
				levelUpTime = getFenceCfg()[currLv].getLvUpTime();
				levelUpDes = getFenceCfg()[currLv].getLevelUpDes();
			}
			if (currLv != 0) {
				currentLevelDes = getFenceCfg()[currLv - 1].getCurrentLevelDes();
			}
			break;
		case CAVE_BUILDING_TYPE_DOORPLATE:
			result = new ResourceCollection(0, 0, 0);
			levelUpTime = 0;
			levelUpDes = Translate.不能升级;
			break;
		case CAVE_BUILDING_TYPE_FIELD1:
		case CAVE_BUILDING_TYPE_FIELD2:
		case CAVE_BUILDING_TYPE_FIELD3:
		case CAVE_BUILDING_TYPE_FIELD4:
		case CAVE_BUILDING_TYPE_FIELD5:
		case CAVE_BUILDING_TYPE_FIELD6:
			if (currLv < FaeryConfig.BUILDING_MAX_LEVEL) {
				result = getFieldLvUpCfg()[currLv].getLvUpCost();
				levelUpTime = getFieldLvUpCfg()[currLv].getLvUpTime();
				levelUpDes = getFieldLvUpCfg()[currLv].getLevelUpDes();
			}
			if (currLv != 0) {
				currentLevelDes = getFieldLvUpCfg()[currLv - 1].getCurrentLevelDes();
			}
			break;
		default:
			break;
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setObjValue(result).setIntValue(levelUpTime).setStringValue(levelUpDes).setStringValues(new String[] { currentLevelDes });
	}

	/**
	 * 通过ID得到仙境
	 * @param county
	 * @param id
	 * @return
	 */
	public Faery getFaery(long id) {
		return getFaeryMap().get(id);
	}

	public Faery getFaery(Game game) {
		for (Iterator<Long> itor = getFaeryMap().keySet().iterator(); itor.hasNext();) {
			Long id = itor.next();
			Faery faery = getFaeryMap().get(id);
			if (faery.getGame().equals(game)) {
				return faery;
			}
		}
		return null;
	}

	/**
	 * 获得某人仙府
	 * @param player
	 * @return
	 */
	public Cave getCave(Player player) {
		return getPlayerCave().get(player.getId());
	}

	/**
	 * 得到仙府
	 * @param county
	 * @param position
	 * @return
	 */
	public Cave getCave(int county, CavePosition position) {
		List<Faery> tempList = getCountryMap().get(Integer.valueOf(county));
		if (tempList != null) {
			for (Faery faery : tempList) {
				if (faery.getId() == position.getFaeryId()) {
					return faery.getCaves()[position.getIndex()];
				}
			}
		}
		return null;
	}

	/**
	 * 通过name得到仙境
	 * @param country
	 * @param name
	 * @return
	 */
	public Faery getFaety(int country, String name) {
		if (countryMap.containsKey(country)) {
			for (int i = 0; i < countryMap.get(country).size(); i++) {
				Faery faery = countryMap.get(country).get(i);
				if (faery.getName().equals(name)) {
					return faery;
				}
			}
		}
		return null;
	}

	/**
	 * 获得当前维护费用
	 * @param faery
	 * @return
	 */
	public ResourceCollection getMaintenanceCost(Cave cave) {
		if (cave.getMainBuilding().getGrade() < 0 || cave.getMainBuilding().getGrade() > FaeryConfig.BUILDING_MAX_LEVEL) {
			return new ResourceCollection(0, 0, 0);
		}
		return getMaintenanceCost(cave.getMainBuilding().getGrade());
	}

	/**
	 * 获得当前维护费用
	 * @param level
	 * @return
	 */
	public ResourceCollection getMaintenanceCost(int level) {
		if (level < 0 || level > getMainCfgs().length) {
			return null;
		}
		return getMainCfgs()[level - 1].getMaintenanceCost();
	}

	private void loadFile() throws Exception {
		long start = SystemTime.currentTimeMillis();
		File file = new File(getFilePath());
		if (!file.exists()) {
			System.out.println("文件不存在:" + getFilePath());
			throw new Exception();
		}
		FileInputStream fin = new FileInputStream(file);
		try {
			// Workbook workbook = Workbook.getWorkbook(fin);

			// Sheet sheet = workbook.getSheet(FILE_CFG_MAIN);// 主建筑
			/************************************************************* 主建筑 *************************************************************/

			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(FILE_CFG_MAIN);// 主建筑
			int maxRow = sheet.getPhysicalNumberOfRows();
			List<MainBuildingCfg> cfgs = new ArrayList<MainBuildingCfg>();
			for (int i = 1; i < maxRow; i++) {
				int index = 0;
				// Cell[] cell = sheet.getRow(i);
				HSSFRow row = sheet.getRow(i);
				int grade = (int) row.getCell(index++).getNumericCellValue();

				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection maintenanceCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				ResourceCollection maintenanceReback = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int storehouseLvLimit = (int) row.getCell(index++).getNumericCellValue();
				int pethouseLvLimit = (int) row.getCell(index++).getNumericCellValue();
				int fenceLvLimit = (int) row.getCell(index++).getNumericCellValue();
				int fieldLvLimit = (int) row.getCell(index++).getNumericCellValue();
				int fieldNumLimit = (int) row.getCell(index++).getNumericCellValue();
				int khatamDay = (int) row.getCell(index++).getNumericCellValue();
				int deleteDay = (int) row.getCell(index++).getNumericCellValue();

				String levelUpDes = (row.getCell(index++).getStringCellValue());
				String currentLevelDes = (row.getCell(index++).getStringCellValue());
				MainBuildingCfg cfg = new MainBuildingCfg(grade, lvUpCost, maintenanceCost, maintenanceReback, lvUpTime, storehouseLvLimit, pethouseLvLimit, fenceLvLimit, fieldLvLimit, fieldNumLimit, khatamDay, deleteDay, levelUpDes, currentLevelDes);
				cfgs.add(cfg);
			}
			Collections.sort(cfgs);
			setMainCfgs(cfgs.toArray(new MainBuildingCfg[0]));

			if (logger.isInfoEnabled()) logger.info("[主建筑耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 仓库 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_STOREHOUSE);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<StorehouseCfg> store = new ArrayList<StorehouseCfg>();
			for (int i = 1; i < maxRow; i++) {
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				int grade = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				int foodLv = (int) row.getCell(index++).getNumericCellValue();
				int woodLv = (int) row.getCell(index++).getNumericCellValue();
				int stoneLv = (int) row.getCell(index++).getNumericCellValue();
				String levelUpDes = (row.getCell(index++).getStringCellValue());
				String currentLevelDes = (row.getCell(index++).getStringCellValue());
				ResourceCollection maintenanceCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				ResourceCollection maintenanceReback = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				StorehouseCfg cfg = new StorehouseCfg(grade, lvUpCost, lvUpTime, foodLv, woodLv, stoneLv, levelUpDes, currentLevelDes, maintenanceCost, maintenanceReback);
				store.add(cfg);
			}
			Collections.sort(store);
			setStoreCfgs(store.toArray(new StorehouseCfg[0]));
			if (logger.isInfoEnabled()) logger.info("[仓库耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 资源等级 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_STOREHOUSE_RESLV);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<ResourceLevelCfg> resourceLv = new ArrayList<ResourceLevelCfg>();
			for (int i = 1; i < maxRow; i++) {
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				int grade = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				int lvUpResourceNum = (int) row.getCell(index++).getNumericCellValue();
				ResourceLevelCfg cfg = new ResourceLevelCfg(grade, lvUpCost, lvUpTime, lvUpResourceNum);
				resourceLv.add(cfg);
			}
			setResLvCfg(resourceLv.toArray(new ResourceLevelCfg[0]));

			if (logger.isInfoEnabled()) logger.info("[资源等级耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 宠物仓库 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_PETHOUSE);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<PetHouseCfg> petHouse = new ArrayList<PetHouseCfg>();
			for (int i = 1; i < maxRow; i++) {
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				int grade = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				int hookNum = (int) row.getCell(index++).getNumericCellValue();
				double expParm = row.getCell(index++).getNumericCellValue();
				int storeNum = (int) row.getCell(index++).getNumericCellValue();
				String levelUpDes = row.getCell(index++).getStringCellValue();
				String currentLevelDes = row.getCell(index++).getStringCellValue();
				ResourceCollection maintenanceCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				ResourceCollection maintenanceReback = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				PetHouseCfg cfg = new PetHouseCfg(grade, lvUpCost, lvUpTime, hookNum, expParm, storeNum, levelUpDes, currentLevelDes, maintenanceCost, maintenanceReback);
				petHouse.add(cfg);
			}
			Collections.sort(petHouse);
			setPethouseCfg(petHouse.toArray(new PetHouseCfg[0]));
			initPetStore();
			if (logger.isInfoEnabled()) logger.info("[宠物仓库耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 仓库开放兑换道具 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_CONVERT_ARTICLE);
			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int id = (int) row.getCell(index++).getNumericCellValue();
				int storeGrade = (int) row.getCell(index++).getNumericCellValue();
				int groupIndex = (int) row.getCell(index++).getNumericCellValue();
				String articleCfg = (row.getCell(index++).getStringCellValue());
				String[] values = articleCfg.split(",");
				if (values.length != 2) {
					logger.error("[加载兑换物品错误][{}]", new Object[] { articleCfg });
					continue;
				}

				int articleColor = Integer.valueOf(values[0]);
				String articleName = modifyContent(values[1]);
				int daiylMaxNum = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection convertCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());

				ConvertArticleCfg cfg = new ConvertArticleCfg(id, storeGrade, groupIndex, articleColor, articleName, daiylMaxNum, convertCost);
				if (!convertMap.containsKey(cfg.getGroupIndex())) {
					convertMap.put(cfg.getGroupIndex(), new ArrayList<ConvertArticleCfg>());
				}
				convertMap.get(cfg.getGroupIndex()).add(cfg);
			}

			for (Iterator<Integer> iterator = convertMap.keySet().iterator(); iterator.hasNext();) {
				int gi = iterator.next();
				int num = convertMap.get(gi).get(0).getDaiylMaxNum();
				for (int i = 0; i < convertMap.get(gi).size(); i++) {
					ConvertArticleCfg articleCfg = convertMap.get(gi).get(i);
					if (articleCfg.getDaiylMaxNum() != num) {
						logger.error("[兑换物品配置兑换数量错误]组别[{}]的[{}]", new Object[] { gi, articleCfg.toString() });
						continue;
					}
				}
			}
			if (logger.isInfoEnabled()) logger.info("[兑换道具:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 栅栏配置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_FENCE);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<FenceCfg> fences = new ArrayList<FenceCfg>();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int grade = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				int enterCost = (int) row.getCell(index++).getNumericCellValue();
				String levelUpDes = (row.getCell(index++).getStringCellValue());
				String currentLevelDes = (row.getCell(index++).getStringCellValue());
				ResourceCollection maintenanceCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				ResourceCollection maintenanceReback = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				FenceCfg cfg = new FenceCfg(grade, lvUpCost, lvUpTime, enterCost, levelUpDes, currentLevelDes, maintenanceCost, maintenanceReback);
				fences.add(cfg);
			}
			Collections.sort(fences);
			setFenceCfg(fences.toArray(new FenceCfg[0]));
			if (logger.isInfoEnabled()) logger.info("[栅栏耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 田地升级配置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_FIELD_LVUP);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<FieldLvUpCfg> fieldLvUp = new ArrayList<FieldLvUpCfg>();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int grade = (int) row.getCell(index++).getNumericCellValue();
				ResourceCollection lvUpCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				int lvUpTime = (int) row.getCell(index++).getNumericCellValue();
				String levelUpDes = (row.getCell(index++).getStringCellValue());
				String currentLevelDes = (row.getCell(index++).getStringCellValue());
				ResourceCollection maintenanceCost = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				ResourceCollection maintenanceReback = new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				FieldLvUpCfg cfg = new FieldLvUpCfg(grade, lvUpCost, lvUpTime, levelUpDes, currentLevelDes, maintenanceCost, maintenanceReback);
				fieldLvUp.add(cfg);
			}
			Collections.sort(fieldLvUp);
			setFieldLvUpCfg(fieldLvUp.toArray(new FieldLvUpCfg[0]));

			if (logger.isInfoEnabled()) logger.info("[田地升级耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 田地开垦配置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_FIELD_ASSART);
			maxRow = sheet.getPhysicalNumberOfRows();
			List<FieldAssartCfg> fieldAssart = new ArrayList<FieldAssartCfg>();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				int grade = (int) row.getCell(index++).getNumericCellValue();
				int costNum = (int) row.getCell(index++).getNumericCellValue();
				String articleName = (row.getCell(index++).getStringCellValue());
				FieldAssartCfg cfg = new FieldAssartCfg(grade, costNum, articleName);
				fieldAssart.add(cfg);
			}
			Collections.sort(fieldAssart);
			setFieldAssartCfgs(fieldAssart.toArray(new FieldAssartCfg[0]));

			if (logger.isInfoEnabled()) logger.info("[田地开垦耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 种植列表配置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_PLANT_LIST);
			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				PlantCfg cfg = new PlantCfg();
				cfg.setId((int) row.getCell(index++).getNumericCellValue());
				cfg.setName((row.getCell(index++).getStringCellValue()));
				cfg.setPlantGroup((row.getCell(index++).getStringCellValue()));
				cfg.setFieldLvNeed((int) row.getCell(index++).getNumericCellValue());
				cfg.setTempletNpc((int) row.getCell(index++).getNumericCellValue());
				cfg.setAvataRaces(StringTool.string2StringArr(row.getCell(index++).getStringCellValue(), ","));
				cfg.setAvataSex(StringTool.string2StringArr(row.getCell(index++).getStringCellValue(), ","));
				cfg.setType((int) row.getCell(index++).getNumericCellValue());
				cfg.setCost(new ResourceCollection((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue()));
				cfg.setMoneyCost((int) row.getCell(index++).getNumericCellValue());
				/******** 10 *******/
				cfg.setOutputType((int) row.getCell(index++).getNumericCellValue());
				cfg.setOutputName((row.getCell(index++).getStringCellValue()));
				cfg.setOutputNumArr(StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Integer.class));
				cfg.setOutputNum(cfg.getOutputNumArr()[0]);
				cfg.setOutputArticleColor(StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Integer.class));
				cfg.setColorRate(StringTool.stringArr2DoubleArr(StringTool.string2StringArr(row.getCell(index++).getStringCellValue(), ","), df));
				cfg.setGrownUpTime(1000 * (int) row.getCell(index++).getNumericCellValue());
				cfg.setChangeOutShowRate(row.getCell(index++).getNumericCellValue());
				cfg.init();
				Cell cell = row.getCell(index++);
				if (cell != null) {
					List<DropsArticle> excessArticle = getExcess(cell.getStringCellValue(), cfg.getId());
					if (excessArticle != null) {
						cfg.setExcessArticle(excessArticle);
					}
				}
				cfg.setDailyMaxTimes((int) row.getCell(index++).getNumericCellValue());
				cfg.setAtOneTimeMaxTimes((int) row.getCell(index++).getNumericCellValue());
				cfg.setDes((row.getCell(index++).getStringCellValue()));
				/******* 20 *******/
				cfg.setBaseExchangeNum((int) row.getCell(index++).getNumericCellValue());
				if (!plantMap.containsKey(cfg.getType())) {
					plantMap.put(cfg.getType(), new ArrayList<PlantCfg>());
				}
				plantMap.get(cfg.getType()).add(cfg);

				if (!plantGradeMap.containsKey(cfg.getFieldLvNeed())) {
					plantGradeMap.put(cfg.getFieldLvNeed(), new ArrayList<PlantCfg>());
				}
				plantGradeMap.get(cfg.getFieldLvNeed()).add(cfg);
				if (logger.isInfoEnabled()) logger.info("[种植配置]" + cfg.toString());
			}
			if (logger.isInfoEnabled()) logger.info("[种植列表耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 坐标配置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_POINT);
			maxRow = sheet.getPhysicalNumberOfRows();
			PointCfg[][] pointCfgs = new PointCfg[maxRow - 1][11];
			HitAreaCfg[] hitAreaCfgs = new HitAreaCfg[maxRow - 1];
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int index = 0;
				index++;
				String value = row.getCell(index++).getStringCellValue();
				String[] subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg mainPoint = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_MAIN);
				pointCfgs[i - 1][0] = mainPoint;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg stonePoint = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_STORE);
				pointCfgs[i - 1][1] = stonePoint;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg petPoint = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_PETHOUSE);
				pointCfgs[i - 1][2] = petPoint;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg doorplatePoint = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_DOORPLATE);
				pointCfgs[i - 1][3] = doorplatePoint;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg fencePoint = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FENCE);
				pointCfgs[i - 1][4] = fencePoint;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field1Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD1);
				pointCfgs[i - 1][5] = field1Point;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field2Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD2);
				pointCfgs[i - 1][6] = field2Point;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field3Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD3);
				pointCfgs[i - 1][7] = field3Point;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field4Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD4);
				pointCfgs[i - 1][8] = field4Point;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field5Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD5);
				pointCfgs[i - 1][9] = field5Point;

				value = row.getCell(index++).getStringCellValue();
				subs = value.split(",");
				if (subs.length != 2) {
					logger.error("[坐标点设置错误][{}]", new Object[] { value });
					return;
				}
				PointCfg field6Point = new PointCfg(index, new Point(Integer.valueOf(subs[0]), Integer.valueOf(subs[1])), CAVE_BUILDING_TYPE_FIELD6);
				pointCfgs[i - 1][10] = field6Point;

				try {
					HitAreaCfg hitAreaCfg = new HitAreaCfg();
					Short xs[] = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Short.class);
					Short ys[] = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Short.class);
					short[] x = new short[xs.length];
					short[] y = new short[ys.length];
					for (int m = 0; m < x.length; m++) {
						x[m] = xs[m];
					}
					for (int m = 0; m < y.length; m++) {
						y[m] = ys[m];
					}
					hitAreaCfg.setX(x);
					hitAreaCfg.setY(y);
					hitAreaCfgs[i - 1] = hitAreaCfg;
				} catch (Exception e) {
					if (logger.isInfoEnabled()) logger.info("[加载碰撞异常]", e);
				}

			}
			setPointCfgs(pointCfgs);
			setHitAreaCfgs(hitAreaCfgs);
			if (logger.isInfoEnabled()) logger.info("[坐标耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 建筑外形设置 *************************************************************/
			sheet = workbook.getSheetAt(FILE_CFG_OUTSHOW);
			buildingOutShowCfgs = new BuildingOutShowCfg[11];
			HSSFRow row = sheet.getRow(1);
			for (int i = 1; i < row.getPhysicalNumberOfCells(); i++) {
				int defaultNpcId = (int) row.getCell(i).getNumericCellValue();
				BuildingOutShowCfg cfg = new BuildingOutShowCfg(defaultNpcId);
				buildingOutShowCfgs[i - 1] = cfg;
			}
			if (logger.isInfoEnabled()) logger.info("[外形耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 增加建筑队列 *************************************************************/
			sheet = workbook.getSheetAt(FILE_ADD_QUEUE);
			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				row = sheet.getRow(i);
				increaseScheduleCfgs.add(new IncreaseScheduleCfg((row.getCell(0).getStringCellValue()), (int) row.getCell(1).getNumericCellValue(), 1000 * (long) row.getCell(2).getNumericCellValue()));
			}
			if (logger.isInfoEnabled()) logger.info("[增加队列耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			/************************************************************* 各种颜色果实兑换倍率 *************************************************************/
			sheet = workbook.getSheetAt(FILE_EXCHANGE_RATE);
			row = sheet.getRow(1);
			exchangeRate = new double[ArticleManager.color_article.length];
			for (int i = 0; i < exchangeRate.length; i++) {
				exchangeRate[i] = row.getCell(i).getNumericCellValue();
			}
			if (logger.isInfoEnabled()) {
				logger.info("[果实兑换倍率耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
			}
			/** 各等级宠物挂机基础经验 */
			sheet = workbook.getSheetAt(FILE_PET_HOOKEXP);
			maxRow = sheet.getPhysicalNumberOfRows();
			petHookExp = new HashMap<Integer, Double>();
			for (int i = 1; i < maxRow; i++) {
				row = sheet.getRow(i);
				petHookExp.put((int) row.getCell(0).getNumericCellValue(), row.getCell(1).getNumericCellValue());
			}
			/************************ 杂项配置 ****************************/
			sheet = workbook.getSheetAt(FILE_OTHERS);
			row = sheet.getRow(1);
			String shopName = (row.getCell(0).getStringCellValue());
			String articleName = (row.getCell(1).getStringCellValue());
			Shop shop = shopManager.getShop(shopName, null);
			if (shop == null) {
				throw new Exception("[商店不存在:" + shopName + "]");
			}
			Goods goods = shop.getGoods(articleName, -1);
			if (goods == null) {
				throw new Exception("[商店内物品不存在,商店:" + shopName + ",物品 :" + articleName + "]");
			}

			/************************ 炸弹配置 ****************************/
			sheet = workbook.getSheetAt(FILE_BOMB);

			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				int index = 0;
				row = sheet.getRow(i);
				String bombName = StringTool.getCellValue(row.getCell(index++), String.class);
				String bombName_stat = StringTool.getCellValue(row.getCell(index++), String.class);
				int articleColor = StringTool.getCellValue(row.getCell(index++), Integer.class);
				int totalBombTimes = StringTool.getCellValue(row.getCell(index++), Integer.class);
				int rate = StringTool.getCellValue(row.getCell(index++), Integer.class);
				String showIcon = StringTool.getCellValue(row.getCell(index++), String.class);
				String buffName = StringTool.getCellValue(row.getCell(index++), String.class);
				int buffLast = StringTool.getCellValue(row.getCell(index++), Integer.class);
				int buffLevel = StringTool.getCellValue(row.getCell(index++), Integer.class);
				CaveFieldBombConfig cfg = new CaveFieldBombConfig(bombName, bombName_stat, articleColor, rate, totalBombTimes, showIcon, buffName, buffLast, buffLevel);
				bombConfigs.add(cfg);
				logger.warn("[系统载入炸弹信息] " + cfg.toString());
			}

			assartFieldArticlePrize = goods.getPrice();
			logger.info("[系统初始化] [记载完毕] [田地令价格:" + assartFieldArticlePrize + "]");
		} catch (Exception e) {
			logger.error("[解析额外物品错误]", e);
			throw e;
		} finally {
			fin.close();
		}
	}

	private void initPetStore() {
		if (pethouseCfg != null) {
			petStoreNums = new int[pethouseCfg.length];
			for (int i = 0; i < pethouseCfg.length; i++) {
				petStoreNums[i] = pethouseCfg[i].getStoreNum();
			}
		}
	}

	/**
	 * 解析额外奖励
	 * @param value
	 * @return
	 */
	private List<DropsArticle> getExcess(String value, int id) {
		List<DropsArticle> list = null;
		try {
			if (value != null && !value.isEmpty()) {
				list = new ArrayList<DropsArticle>();
				String[] subs = value.split("\\|");
				for (int i = 0; i < subs.length; i++) {
					String[] values = subs[i].split(",");
					if (values.length != 5) {
						logger.error("[加载额外物品失败][{}]", new Object[] { value });
						return null;
					}
					DropsArticle article = new DropsArticle(Integer.valueOf(values[0]), values[1], Integer.valueOf(values[2]), Integer.valueOf(values[3]), df.parse(values[4]).doubleValue());
					list.add(article);
				}
			}
		} catch (Exception e) {
			logger.error("[加载额外物品解析错误][ID=" + id + "][" + value + "]", e);
			return null;
		}
		return list;
	}

	public static FaeryManager getInstance() {
		return instance;
	}

	public GamePlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(GamePlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public JiazuManager getJiazuManager() {
		return jiazuManager;
	}

	public void setJiazuManager(JiazuManager jiazuManager) {
		this.jiazuManager = jiazuManager;
	}

	/**
	 * 对excel表中输入的内容做修正<BR/>
	 * 1.封印所有的半角空格[ ]<BR/>
	 * 2.封印所有的全角空格[　]<BR/>
	 * @param input
	 * @return
	 */
	private static String modifyContent(String input) {

		String result = input;
		if (result.indexOf(" ") != -1) {
			result = result.replaceAll("[ ]*", "");
		}
		if (result.indexOf("　") != -1) {
			result = result.replaceAll("[　]*", "");
		}
		return result;
	}

	public void init() throws Exception {
		
		caveEm = SimpleEntityManagerFactory.getSimpleEntityManager(Cave.class);
		faeryEm = SimpleEntityManagerFactory.getSimpleEntityManager(Faery.class);

		long start = SystemTime.currentTimeMillis();
		instance = this;
		instance.loadFile();
		instance.loadKhatam();
		instance.initBeatHeartThread();
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 加载所有被封印的仙府
	 * @throws Exception
	 */
	private void loadKhatam() throws Exception {
		QuerySelect<Cave> querySelect = new QuerySelect<Cave>();
		List<Cave> khatamed = querySelect.selectAll(Cave.class, " status = ?", new Object[] { CAVE_STATUS_KHATAM }, null, caveEm);
		if (khatamed != null) {
			for (Cave cave : khatamed) {
				getKhatamMap().put(cave.getOwnerId(), cave.getId());
			}
		}
	}

	// /**
	// * 保存所有仙境
	// * @param reason
	// */
	// public void saveFaery(String reason) {
	// long start = SystemTime.currentTimeMillis();
	// int newNum = 0;
	// int dirtyNum = 0;
	// for (Iterator<Integer> iterator = getCountryMap().keySet().iterator(); iterator.hasNext();) {
	// int county = iterator.next();
	// for (Faery faery : getCountryMap().get(county)) {
	// if (faery.isNew()) {
	// faeryDao.insert(faery);
	// faery.setNew(false);
	// faery.setDirty(false);
	// newNum++;
	// } else if (faery.isDirty()) {
	// faeryDao.update(faery);
	// faery.setDirty(false);
	// dirtyNum++;
	// }
	// }
	// }
	// if (logger.isWarnEnabled()) {
	// logger.warn("[{}] [保存所有仙府] [新仙府:{}个] [更新仙府:{}个] [耗时:{}]", new Object[] { reason, newNum, dirtyNum, (SystemTime.currentTimeMillis() - start) });
	// }
	// }

	public void destroy() {
		caveEm.destroy();
		faeryEm.destroy();
		// for (Iterator<Integer> iterator = getCountryMap().keySet().iterator(); iterator.hasNext();) {
		// int country = iterator.next();
		// for (Faery faery : getCountryMap().get(country)) {
		// for (int i = 0; i < faery.getCaves().length; i++) {
		// Cave cave = faery.getCaves()[i];
		// if (cave != null) {
		// if (cave.isNew()) {
		// caveDao.insert(cave);
		// cave.setNew(false);
		// cave.setDirty(false);
		// } else if (cave.isDirty()) {
		// caveDao.update(cave);
		// cave.setDirty(false);
		// }
		// }
		// }
		// }
		// }
		// saveFaery("系统关闭");
	}

	/**
	 * 初始化线程池
	 */
	private void initBeatHeartThread() {
		try {
			long start = SystemTime.currentTimeMillis();
			int total = loadFaery();
			int threadNum = BEATHEART_THREAD_NUM;
			if (total > BEATHEART_THREAD_NUM * BEATHEART_MAX_NUM) {
				threadNum = (total / BEATHEART_MAX_NUM) + 1;
			}
			for (int i = 0; i < threadNum; i++) {
				threadPool.add(new FaeryBeatHeartThread("仙境线程-" + i));
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[系统初始化] [实例化线程数:{}]", new Object[] { threadPool.size() });
			}
			// 平均个数
			int avg = (total / threadPool.size()) + 1;
			if (logger.isWarnEnabled()) {
				logger.warn("[系统初始化] [仙境总数量:{}] [线程数:{}个] [每个线程平均处理仙境:{}个]", new Object[] { total, threadNum, avg });
			}
			int count = 0;
			for (Iterator<Integer> iterator = getCountryMap().keySet().iterator(); iterator.hasNext();) {
				int county = iterator.next();
				for (int i = 0; i < getCountryMap().get(county).size(); i++) {
					threadPool.get(threadPool.size() - 1 - (i / avg)).addFaery(getCountryMap().get(county).get(i));
					count++;
				}
			}

			for (int i = 0; i < threadPool.size(); i++) {
				Thread t = new Thread(threadPool.get(i), threadPool.get(i).getName());
				t.start();
			}
		} catch (Exception e) {
			logger.error("初始化线程错误initBeatHeartThread", e);
		}

	}

	private int loadFaery() {
		try {
			// 查询出所有的仙府
			QuerySelect<Faery> querySelect = new QuerySelect<Faery>();
			List<Faery> tempList = querySelect.selectAll(Faery.class, null, new Object[] {}, null, faeryEm);
			QuerySelect<Cave> queryCave = new QuerySelect<Cave>();
			List<Cave> allCaves = queryCave.selectAll(Cave.class, " status = ? or status = ?", new Object[] { CAVE_STATUS_OPEN, CAVE_STATUS_LOCKED }, null, caveEm);
			if (logger.isWarnEnabled()) {
				logger.warn("[系统初始化] [loadFaery] [系统中数量allCaves:{}]", new Object[] { allCaves.size() });
			}
			caveIdmap = new Hashtable<Long, Cave>();

			for (int i = 0; i < allCaves.size(); i++) {
				Cave cave = allCaves.get(i);
				caveIdmap.put(cave.getId(), cave);
			}
			if (tempList == null || tempList.size() == 0) {
				// 第一次加载 创建默认的一些仙境
				tempList = new ArrayList<Faery>();
				for (int i = 0; i < CountryManager.国家名称.length; i++) {
					for (int j = 0; j < DEFAULT_FAERY_NUM; j++) {
						tempList.add(new Faery(i + 1, j + 1));
					}
				}
			}
			if (logger.isWarnEnabled()) {
				logger.warn("[系统初始化] [loadFaery] [系统中数量:{}]", new Object[] { tempList.size() });
			}
			
			List<String> list = new ArrayList<String>();
			boolean hasSame = false;
			for (int i=0; i<tempList.size(); i++) {
				String temp = tempList.get(i).getCountry() + "_" + tempList.get(i).getIndex();
				if (list.contains(temp)) {
					hasSame = true;
					break;
				}
				list.add(temp);
			}
			if (hasSame) {
				int[] countryIndes = new int[CountryManager.国家名称.length];
				for (int i=0; i<tempList.size(); i++) {
					int oldIndex = tempList.get(i).getIndex();
					tempList.get(i).setIndex(countryIndes[tempList.get(i).getCountry() - 1]+1);
					countryIndes[tempList.get(i).getCountry() - 1] ++ ;
					logger.warn("[修改仙境index] [仙境id:"+tempList.get(i).getId()+"] [国家:"+tempList.get(i).getCountry()+"] [old:" + oldIndex + "] [new:" + tempList.get(i).getIndex() + "]");
				}
			}
			
			
			for (int i = 0; i < tempList.size(); i++) {
				Faery temp = tempList.get(i);
				temp.initGame();
				temp.setCaves(new Cave[temp.getCaveIds().length]);
				for (int k = 0; k < temp.getCaveIds().length; k++) {
					Long caveId = temp.getCaveIds()[k];
					if (caveId != null && caveId > 0) {
						if (caveIdmap.get(caveId) != null) {
							temp.getCaves()[k] = caveIdmap.get(caveId);
						}
					}
				}
				Cave[] caves = temp.getCaves();
				int count = 0;
				for (int j = 0; j < caves.length; j++) {
					if (caves[j] != null) {
						getPlayerCave().put(caves[j].getOwnerId(), caves[j]);
						caves[j].setFaery(temp);
						count++;
					}
				}
				if (logger.isWarnEnabled()) {
					logger.warn("[系统初始化] [loadFaery] [Faery:{}] [caves:{}] [数量:{}]", new Object[] { temp.getName(), Arrays.toString(temp.getCaveIds()), count });
				}
				try {
					for (int m = 0; m < temp.getCaves().length; m++) {
						Cave cave = temp.getCaves()[m];
						if (cave != null) {
							cave.setIndex(m);
							cave.onLoadInitNpc();
						}
					}
				} catch (Exception e) {
					logger.error("[仙境加载异常] [ID:{}]", new Object[] { temp.getId() }, e);
					continue;
				}
				temp.init();
				int county = Integer.valueOf(temp.getCountry());
				if (getCountryMap().get(county) == null) {
					getCountryMap().put(county, new ArrayList<Faery>());
				}
				getCountryMap().get(county).add(temp);
				getFaeryMap().put(temp.getId(), temp);
				if (logger.isWarnEnabled()) {
					logger.warn("[系统初始化] [loadFaery] [初始化完成:{}]", new Object[] { temp.getName() });
				}
			}
			int total = 0;
			for (Iterator<Integer> iter = getCountryMap().keySet().iterator(); iter.hasNext();) {
				int county = iter.next();
				Collections.sort(getCountryMap().get(county));
				total += getCountryMap().get(county).size();
			}
			return total;
		} catch (Exception e) {
			logger.error("[仙境初始化异常]", e);
		}
		return 0;
	}

	/**
	 * 是否是种植的产物
	 * [考虑优化,单独存储]
	 * @param name
	 * @return
	 */
	public PlantCfg getPlantCfg(String name) {
		for (Iterator<Integer> iter = getPlantMap().keySet().iterator(); iter.hasNext();) {
			int type = iter.next();
			for (PlantCfg cfg : getPlantMap().get(type)) {
				if (cfg.getOutputName().equals(name)) {
					return cfg;
				}
			}
		}
		return null;
	}

	public PlantCfg getPlantCfg(int id) {
		for (Iterator<Integer> iter = getPlantMap().keySet().iterator(); iter.hasNext();) {
			int type = iter.next();
			for (PlantCfg cfg : getPlantMap().get(type)) {
				if (cfg.getId() == id) {
					return cfg;
				}
			}
		}
		return null;
	}

	/**
	 * 获得最靠前的一个仙府位置【唤醒的默认规则】<BR/>
	 * 1.非封印状态的洞府不能唤醒<BR/>
	 * 2.主人不存在<BR/>
	 * @param county
	 * @return
	 */
	public synchronized CompoundReturn notifyCave(Cave cave) {
		if (CaveSubSystem.logger.isInfoEnabled()) {
			CaveSubSystem.logger.info("[开始解封仙府] [Id:{}]", new Object[] { cave.getId() });
		}
		if (cave.getStatus() != CAVE_STATUS_KHATAM) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(1);
		}
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(cave.getOwnerId());
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info("[角色没拿到]" + cave.getOwnerId(), e);
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(2);
		}
		CompoundReturn cr = getRandomCavePosition(player.getCountry(), cave.getId());
		long faeryId = cr.getLongValue();
		int index = cr.getIntValue();

		if (faeryId == -1 || index == -1) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		getKhatamMap().remove(player.getId());
		// 放回去
		Faery faery = FaeryManager.getInstance().getFaery(faeryId);

		player.setFaeryId(faery.getId());
		cave.setStatus(CAVE_STATUS_OPEN);
		cave.setOwnerLastVisitTime(SystemTime.currentTimeMillis());
		faery.getCaveIds()[index] = cave.getId();
		faery.initSize();
		FaeryManager.faeryEm.notifyFieldChange(faery, "caveIds");

		FaeryManager.getInstance().getPlayerCave().put(player.getId(), cave);
		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setObjValue(new CavePosition(faeryId, index));
	}

	/**
	 * 得到一个空的仙府位置
	 * @param county
	 * @return
	 */
	public synchronized CompoundReturn getRandomCavePosition(byte county, long caveId) {
		List<Faery> countyList = getCountryMap().get(Integer.valueOf(county));
		long faeryId = -1;
		int index = -1;
		boolean ok = false;
		for (int i = 0; i < countyList.size(); i++) {
			Faery faery = countyList.get(i);
			if (faery.isFull()) {
				continue;
			}
			index = faery.getOneEmptyIndexAndHold(caveId);
			if (index != -1) {
				faeryId = faery.getId();
				ok = true;
				break;
			}
		}
		if (!ok) {
			try {
				if (logger.isWarnEnabled()) {
					logger.warn("[新增一个仙境] [country:{}]", new Object[] { county });
				}
				Faery newFaery = createEmptyFaery(county);
				if (newFaery != null) {
					newFaery.initGame();
					newFaery.init();
					newFaery.initSize();
					addFaeryToBeatHeart(newFaery);
					getCountryMap().get(Integer.valueOf(county)).add(newFaery);
					getFaeryMap().put(newFaery.getId(), newFaery);
					index = newFaery.getOneEmptyIndexAndHold(caveId);
					if (logger.isWarnEnabled()) {
						logger.warn("[新增一个仙境] [新位置:{}]", new Object[] { index, newFaery.getId() });
					}
					return CompoundReturn.createCompoundReturn().setIntValue(index).setLongValue(newFaery.getId());
				}
			} catch (Exception e) {
				if (logger.isWarnEnabled()) logger.warn("[新增一个家园] [异常] [country:{}]", new Object[] { county }, e);
			}
		}
		return CompoundReturn.createCompoundReturn().setIntValue(index).setLongValue(faeryId);
	}

	public void addFaeryToBeatHeart(Faery faery) {
		try {
			FaeryBeatHeartThread fhThread = null;
			for (FaeryBeatHeartThread beatHeartThread : threadPool) {
				if (beatHeartThread.getFaeries().size() < BEATHEART_MAX_NUM) {
					fhThread = beatHeartThread;
					if (logger.isWarnEnabled()) {
						logger.warn("[新增仙境:{}] [加入到旧的线程中] [线程名字:{}]", new Object[] { faery.getName(), fhThread.getName() });
					}
					break;
				}
			}
			if (fhThread == null) {// 需要新增一个线程
				fhThread = new FaeryBeatHeartThread("仙境线程-" + (threadPool.size() + 1));
				Thread t = new Thread(fhThread, fhThread.getName());
				t.start();
				threadPool.add(fhThread);
				if (logger.isWarnEnabled()) {
					logger.warn("[新增仙境:{}] [加入到新的线程中] [线程名字:{}]", new Object[] { faery.getName(), fhThread.getName() });
				}
			}
			if (fhThread != null) {
				fhThread.addFaery(faery);
			}
		} catch (Throwable e) {
			if (logger.isWarnEnabled()) logger.warn("[新增仙境:{}] [异常]", new Object[] { faery.getName() }, e);
		}
	}

	public static int randomColor(double[] colorRate) {
		double ran = random.nextDouble();
		double sum = 0d;
		for (int i = 0; i < colorRate.length; i++) {
			sum += colorRate[i];
			if (ran < sum) {
				return i;
			}
		}
		return 0;
	}

	/************************************************************* getters and setters *************************************************************/

	public MainBuildingCfg[] getMainCfgs() {
		return mainCfgs;
	}

	public void setMainCfgs(MainBuildingCfg[] mainCfgs) {
		this.mainCfgs = mainCfgs;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Hashtable<Integer, List<Faery>> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Hashtable<Integer, List<Faery>> countyMap) {
		this.countryMap = countyMap;
	}

	public StorehouseCfg[] getStoreCfgs() {
		return storeCfgs;
	}

	public ResourceLevelCfg[] getResLvCfg() {
		return resLvCfg;
	}

	public void setResLvCfg(ResourceLevelCfg[] resLvCfg) {
		this.resLvCfg = resLvCfg;
	}

	public FenceCfg[] getFenceCfg() {
		return fenceCfg;
	}

	public void setFenceCfg(FenceCfg[] fenceCfg) {
		this.fenceCfg = fenceCfg;
	}

	public void setStoreCfgs(StorehouseCfg[] storeCfgs) {
		this.storeCfgs = storeCfgs;
	}

	public PetHouseCfg[] getPethouseCfg() {
		return pethouseCfg;
	}

	public void setPethouseCfg(PetHouseCfg[] pethouseCfg) {
		this.pethouseCfg = pethouseCfg;
	}

	public FieldLvUpCfg[] getFieldLvUpCfg() {
		return fieldLvUpCfg;
	}

	public void setFieldLvUpCfg(FieldLvUpCfg[] fieldLvUpCfg) {
		this.fieldLvUpCfg = fieldLvUpCfg;
	}

	public FieldAssartCfg[] getFieldAssartCfgs() {
		return fieldAssartCfgs;
	}

	public void setFieldAssartCfgs(FieldAssartCfg[] fieldAssartCfgs) {
		this.fieldAssartCfgs = fieldAssartCfgs;
	}

	public HashMap<Integer, List<PlantCfg>> getPlantMap() {
		return plantMap;
	}

	public void setPlantMap(HashMap<Integer, List<PlantCfg>> plantMap) {
		this.plantMap = plantMap;
	}

	public HashMap<Integer, List<ConvertArticleCfg>> getConvertMap() {
		return convertMap;
	}

	public void setConvertMap(HashMap<Integer, List<ConvertArticleCfg>> convertMap) {
		this.convertMap = convertMap;
	}

	public PointCfg[][] getPointCfgs() {
		return pointCfgs;
	}

	public void setPointCfgs(PointCfg[][] pointCfgs) {
		this.pointCfgs = pointCfgs;
	}

	public Hashtable<Long, Long> getKhatamMap() {
		return khatamMap;
	}

	public void setKhatamMap(Hashtable<Long, Long> khatamMap) {
		this.khatamMap = khatamMap;
	}

	public BuildingOutShowCfg[] getBuildingOutShowCfgs() {
		return buildingOutShowCfgs;
	}

	public void setBuildingOutShowCfgs(BuildingOutShowCfg[] buildingOutShowCfgs) {
		this.buildingOutShowCfgs = buildingOutShowCfgs;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public Hashtable<Long, Cave> getPlayerCave() {
		return playerCave;
	}

	public void setPlayerCave(Hashtable<Long, Cave> playerCave) {
		this.playerCave = playerCave;
	}

	public HashMap<Integer, List<PlantCfg>> getPlantGradeMap() {
		return plantGradeMap;
	}

	public void setPlantGradeMap(HashMap<Integer, List<PlantCfg>> plantGradeMap) {
		this.plantGradeMap = plantGradeMap;
	}

	public HitAreaCfg[] getHitAreaCfgs() {
		return hitAreaCfgs;
	}

	public void setHitAreaCfgs(HitAreaCfg[] hitAreaCfgs) {
		this.hitAreaCfgs = hitAreaCfgs;
	}

	/**
	 * 获得内存内的地图名字<BR/>
	 * 规则：realMapName_df_faeryId
	 * @param faery
	 * @return
	 */
	public static void setMemoryMapName(Faery faery) {
		StringBuffer buffer = new StringBuffer(faery.getGameName());
		buffer.append("_jy_").append(faery.getId());
		faery.setMemoryMapName(buffer.toString());
	}

	/**
	 * 是否是本人洞府的建筑
	 * @param NPCId
	 * @return
	 */
	public static boolean isSelfCave(Player player, long NPCId) {
		Cave selfCave = getInstance().getCave(player);
		if (selfCave == null) {
			return false;
		} else {
			return selfCave.getBuildings().containsKey(NPCId);
		}
	}

	public Hashtable<Long, Faery> getFaeryMap() {
		return faeryMap;
	}

	public void setFaeryMap(Hashtable<Long, Faery> faeryMap) {
		this.faeryMap = faeryMap;
	}

	public List<IncreaseScheduleCfg> getIncreaseScheduleCfgs() {
		return increaseScheduleCfgs;
	}

	public void setIncreaseScheduleCfgs(List<IncreaseScheduleCfg> increaseScheduleCfgs) {
		this.increaseScheduleCfgs = increaseScheduleCfgs;
	}

	public double[] getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double[] exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public HashMap<Integer, Double> getPetHookExp() {
		return petHookExp;
	}

	public void setPetHookExp(HashMap<Integer, Double> petHookExp) {
		this.petHookExp = petHookExp;
	}

	public void removePlayerCave(Player player, String reson) {
		long start = SystemTime.currentTimeMillis();
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn("[" + reson + "的时候封印仙府]" + player.getLogString());
		}
		try {
			Cave cave = getCave(player);
			if (cave != null) {
				Faery faery = cave.getFaery();
				if (faery != null) {
					faery.getGame().removeSprite(cave.getMainBuilding().getNpc());
					faery.getGame().removeSprite(cave.getDoorplate().getNpc());

					faery.getGame().removeSprite(cave.getStorehouse().getNpc());
					try {
						cave.getPethouse().rebackHookPetBykhatam();
					} catch (Exception e) {
						logger.error("[移除仙府,退还宠物异常] [caveID:" + cave.getId() + "]", e);
					}
					faery.getGame().removeSprite(cave.getPethouse().getNpc());// 要处理宠物//.....
					faery.getGame().removeSprite(cave.getFence().getNpc());
					for (int i = 0; i < cave.getFields().size(); i++) {
						CaveField caveField = cave.getFields().get(i);
						faery.getGame().removeSprite(caveField.getNpc());
					}
					faery.getCaves()[cave.getIndex()] = null;
					faery.getCaveIds()[cave.getIndex()] = 0L;

					faeryEm.notifyFieldChange(faery, "caveIds");
					faeryEm.save(faery);

					if (CaveSubSystem.logger.isInfoEnabled()) {
						CaveSubSystem.logger.info(player.getLogString() + "[" + reson + "] [封印洞府][有洞府] [封印完毕] [耗时:{}ms]", new Object[] { (SystemTime.currentTimeMillis() - start) });
					}
				} else {
					if (CaveSubSystem.logger.isInfoEnabled()) {
						CaveSubSystem.logger.info(player.getLogString() + "[" + reson + "] [封印洞府][仙境不存在]");
					}
				}
			} else {
				if (CaveSubSystem.logger.isInfoEnabled()) {
					CaveSubSystem.logger.info(player.getLogString() + "[" + reson + "] [封印洞府][洞府不存在]");
				}
			}
		} catch (Exception e) {
			CaveSubSystem.logger.error(player.getLogString() + "[" + reson + "] [封印庄园异常]", e);
		}
	}

	public MemoryNPCManager getNpcManager() {
		return npcManager;
	}

	public void setNpcManager(MemoryNPCManager npcManager) {
		this.npcManager = npcManager;
	}

	/**
	 * 当前资源等级对应的资源上限
	 * @param grade
	 * @return
	 */
	public int getResource(int grade) {
		for (ResourceLevelCfg cfg : getResLvCfg()) {
			if (cfg.getGrade() == grade) {
				return cfg.getLvUpResourceNum();
			}
		}
		return 0;
	}

	/**
	 * 得到仙府内NPC的基础名字,防止客户端显示超屏
	 * @param name
	 * @return
	 */
	public static String getBasename(String name) {
		int index = name.lastIndexOf(CaveBuilding.nameseparator);
		if (index != -1) {
			name = name.substring(index + 1);
		}
		return name;
	}

	/**
	 * 得到某一建筑在某等级下的维护费用
	 * @param buildingType
	 * @param grade
	 * @return
	 */
	public static ResourceCollection getMaintenanceCost(int buildingType, int grade) {
		if (grade == 0) return new ResourceCollection(0, 0, 0);
		switch (buildingType) {
		case CAVE_BUILDING_TYPE_MAIN:
			return getInstance().getMainCfgs()[grade - 1].getMaintenanceCost();
		case CAVE_BUILDING_TYPE_STORE:
			return getInstance().getStoreCfgs()[grade - 1].getMaintenanceCost();
		case CAVE_BUILDING_TYPE_PETHOUSE:
			return getInstance().getPethouseCfg()[grade - 1].getMaintenanceCost();
		case CAVE_BUILDING_TYPE_FENCE:
			return getInstance().getFenceCfg()[grade - 1].getMaintenanceCost();
		case CAVE_BUILDING_TYPE_DOORPLATE:
			return new ResourceCollection(0, 0, 0);
		case CAVE_BUILDING_TYPE_FIELD1:
		case CAVE_BUILDING_TYPE_FIELD2:
		case CAVE_BUILDING_TYPE_FIELD3:
		case CAVE_BUILDING_TYPE_FIELD4:
		case CAVE_BUILDING_TYPE_FIELD5:
		case CAVE_BUILDING_TYPE_FIELD6:
			return getInstance().getFieldLvUpCfg()[grade - 1].getMaintenanceCost();
		default:
			return new ResourceCollection(0, 0, 0);
		}
	}

	/**
	 * 得到某一建筑在某等级下退回的资源
	 * @param buildingType
	 * @param grade
	 * @return
	 */
	public static ResourceCollection getRebackResourse(int buildingType, int grade) {
		switch (buildingType) {
		case CAVE_BUILDING_TYPE_MAIN:
			return getInstance().getMainCfgs()[grade - 1].getMaintenanceReback();
		case CAVE_BUILDING_TYPE_STORE:
			return getInstance().getStoreCfgs()[grade - 1].getMaintenanceReback();
		case CAVE_BUILDING_TYPE_PETHOUSE:
			return getInstance().getPethouseCfg()[grade - 1].getMaintenanceReback();
		case CAVE_BUILDING_TYPE_FENCE:
			return getInstance().getFenceCfg()[grade - 1].getMaintenanceReback();
		case CAVE_BUILDING_TYPE_DOORPLATE:
			return new ResourceCollection(0, 0, 0);
		case CAVE_BUILDING_TYPE_FIELD1:
		case CAVE_BUILDING_TYPE_FIELD2:
		case CAVE_BUILDING_TYPE_FIELD3:
		case CAVE_BUILDING_TYPE_FIELD4:
		case CAVE_BUILDING_TYPE_FIELD5:
		case CAVE_BUILDING_TYPE_FIELD6:
			return getInstance().getFieldLvUpCfg()[grade - 1].getMaintenanceReback();
		default:
			return new ResourceCollection(0, 0, 0);
		}
	}

	public Hashtable<Long, Cave> getCaveIdmap() {
		return caveIdmap;
	}

	public void setCaveIdmap(Hashtable<Long, Cave> caveIdmap) {
		this.caveIdmap = caveIdmap;
	}

	public static int[] fruitScoresRate = { 1, 2, 3, 5, 8 };

	/**
	 * 得到偷取果子的分数
	 * @param color
	 * @param isSelfCountry
	 * @return
	 */
	public static int getFruitScore(int color, boolean isSelfCountry) {
		return fruitScoresRate[color] * (isSelfCountry ? 1 : 2);
	}

	public ShopManager getShopManager() {
		return shopManager;
	}

	public void setShopManager(ShopManager shopManager) {
		this.shopManager = shopManager;
	}

	public void setBombConfigs(List<CaveFieldBombConfig> bombConfigs) {
		this.bombConfigs = bombConfigs;
	}

	public List<CaveFieldBombConfig> getBombConfigs() {
		return bombConfigs;
	}

	public CaveFieldBombConfig getCaveFieldBombConfig(String articleName, int articleColor) {
		for (CaveFieldBombConfig cc : bombConfigs) {
			if (cc.getArticleColor() == articleColor && articleName.equals(cc.getArticleName())) {
				return cc;
			}
		}
		return null;
	}

	public void noticeDynamic(Player player) {
		try {
			if (player.getCaveId() <= 0) {
				return;
			}
			Cave cave = getCave(player);
			if (cave == null) {
				return;
			}
			cave.noticeOwnerDynamic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CAVE_SIMPLE_RES caveSimple(Player player, CAVE_SIMPLE_REQ req) {
		int caveStatus = 0;
		String caveStatusDes = "'";
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (player.getCaveId() <= 0) {
			caveStatus = -1;
			caveStatusDes = Translate.你还没有仙府;
		} else if (cave != null && cave.getStatus() == FaeryConfig.CAVE_STATUS_LOCKED) {
			caveStatus = cave.getStatus();
			caveStatusDes = Translate.仙府被锁定;
		} else if (FaeryManager.getInstance().getKhatamMap().containsKey(player.getId())) {
			caveStatus =FaeryManager.CAVE_STATUS_KHATAM;
			caveStatusDes = Translate.仙府被封印;
		} else {
			caveStatus = 0;
			caveStatusDes = "";
		}
		return new CAVE_SIMPLE_RES(req == null ? GameMessageFactory.nextSequnceNum() : req.getSequnceNum(), caveStatus, caveStatusDes);
	}

	/**
	 * 得到玩家偷菜时间
	 * @param player
	 * @return
	 */
	public long getStealTime(Player player) {
		int rate = 0;
		Buff buff = player.getBuff(Buff_chant_slow.class);
		if (buff != null) {
			rate = ((Buff_chant_slow) buff).getChant_slow_rate();
		}
		return (long) (TimeTool.TimeDistance.SECOND.getTimeMillis() * 5 * ((double) (100 + rate)) / 100);// 偷菜时间改为5秒 2014-5-14 10:06:05
	}
}
