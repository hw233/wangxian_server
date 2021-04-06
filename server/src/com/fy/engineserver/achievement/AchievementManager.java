package com.fy.engineserver.achievement;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.faery.service.QuerySelect;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

/**
 * 成就管理类
 * 
 */
public class AchievementManager implements AchievementConf {

	public final static int STATUS_ERROR = -1;
	public final static int STATUS_DOING = 0;
	public final static int STATUS_DONE = 1;

	private static AchievementManager instance;

	public static Logger logger = LoggerFactory.getLogger(AchievementManager.class);

	public static SimpleEntityManager<GameDataRecord> gameDREm;
	public static SimpleEntityManager<AchievementEntity> aeEm;

	private List<Achievement> achievementList = new ArrayList<Achievement>();
	// 系统中的所有成就列表.<ActionType,Achievement>
	private HashMap<Integer, List<Achievement>> systemAchievementMap = new HashMap<Integer, List<Achievement>>();
	// 成就列表<主分类,<子分类,子分类中的列表>>,
	private HashMap<String, HashMap<String, List<Achievement>>> mainSubMap = new HashMap<String, HashMap<String, List<Achievement>>>();

	// 左边栏,此数据在服务器加载后就不变了
	private List<LeftMenu> leftMenus = new ArrayList<LeftMenu>();

	// 成就完成的依赖<被依赖的成就ID,依赖的成就ID>
	private HashMap<Achievement, List<Achievement>> achievementDependMap = new HashMap<Achievement, List<Achievement>>();

	// 统计数据缓存 <PlayerId,gdrList>
	// private HashMap<Long, HashMap<Integer,GameDataRecord>> gdrMap = new HashMap<Long, HashMap<Integer,GameDataRecord>>();

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 配置文件路径 */
	private String filePath = "";

	public static AchievementManager getInstance() {
		return instance;
	}

	/**
	 * 记录一个可能影响玩家成就的事件,默认数量增加1
	 * @param player
	 * @param achievementType
	 * @throws Exception
	 */
	public void record(Player player, RecordAction recordAction) {
		record(player, recordAction, 1L, true);
	}
	public void record(Player player, RecordAction recordAction, long addNum) {
		record(player, recordAction, addNum, true);
	}

	/**
	 * 记录一个可能影响玩家成就的事件
	 * 某个角色做了一件什么事情,当前改变的值
	 * @param player
	 * @param action
	 * @param addNum
	 * @throws Exception
	 */
	public void record(Player player, RecordAction recordAction, long addNum, boolean isNotify) {
		try {

//			List<Achievement> achievements = getSystemAchievement(recordAction);
//			{
//				// 此段代码只着眼于成就系统本身,如有其他统计需求,可修改下面的逻辑
//				if (achievements == null) {
//					if (logger.isWarnEnabled()) {
//						logger.warn(player.getLogString() + "[不存在任何成就的统计项:{}]", new Object[] { recordAction });
//					}
//					return;
//				}
//			}

			GameDataRecord gdr = getPlayerDataRecord(player, recordAction);
			boolean isNew = false;
			if (gdr == null) {// 还未开启的统计项
				synchronized (player.Lock_for_Ach) {
					gdr = getPlayerDataRecord(player, recordAction);
					if (gdr == null) {
						GameDataRecord dataRecord = new GameDataRecord();
						dataRecord.setId(gameDREm.nextId());
						dataRecord.setPlayerId(player.getId());
						dataRecord.setDataType(recordAction.getType());
						dataRecord.setNum(addNum);// 认为正常取值不能是负数,-1做标记表示没有被记录数值的数据,只记录一次,以此作为依据
						gdr = dataRecord;
						isNew = true;
						if (player.gdrMap != null) player.gdrMap.put(recordAction.getType(), gdr);
					}
				}
			}
			boolean killAllBoss = true;
			for(RecordAction rac : PlayerAimManager.对应统计action) {
				if(getPlayerDataRecord(player, rac) == null) {
					killAllBoss = false;
					break;
				}
			}
			boolean allShengShou = true;
			for (RecordAction rac : PetManager.RecordActionNames) {
				GameDataRecord tgdr = getPlayerDataRecord(player, rac);
				if (tgdr == null) {
					allShengShou = false;
					break;
				}
			}
			long tempNum = 50;
			for(RecordAction rac : PlayerAimManager.防心法action) {
				GameDataRecord tgdr = getPlayerDataRecord(player, rac);
				if(tgdr == null) {
					tempNum = 0;
					break;
				}
				if(tgdr.getNum() < tempNum) {
					tempNum = tgdr.getNum();
				}
			}
			boolean allShenYuan = true;
			for (RecordAction ra : PlayerAimManager.幽冥幻域深渊action) {
				GameDataRecord tgdr = getPlayerDataRecord(player, ra);
				if (tgdr == null) {
					allShenYuan = false;
					break;
				}
			}

			if (isNew) {	
				gameDREm.notifyNewObject(gdr);
				if (logger.isWarnEnabled()) {
					logger.warn(player.getLogString() + "[增加了一个新的统计项:{}] [数量:{}]", new Object[] { recordAction, addNum });
				}
			} else {
				switch (recordAction.getRecordType()) {
				case 累加:
					gdr.addNum(addNum);
					gameDREm.notifyFieldChange(gdr, "num");
					if (logger.isDebugEnabled()) {
						logger.debug(player.getLogString() + "[修改了记录数据] [更改量:" + addNum + "] [更改后:" + gdr.getNum() + "] [统计类型:" + recordAction + "] [记录类型:" + recordAction.getRecordType() + "]");
					}
					break;
				case 设置:
					gdr.setNum(addNum);
					gameDREm.notifyFieldChange(gdr, "num");
					if (logger.isInfoEnabled()) {
						logger.info(player.getLogString() + "[修改了记录数据] [更改量:" + addNum + "] [更改后:" + gdr.getNum() + "] [统计类型:" + recordAction + "] [记录类型:" + recordAction.getRecordType() + "]");
					}
					break;
				case 记录最大值:
					if (addNum > gdr.getNum()) {
						gdr.setNum(addNum);
						gameDREm.notifyFieldChange(gdr, "num");
						if (logger.isInfoEnabled()) {
							logger.info(player.getLogString() + "[修改了记录数据] [更改量:" + addNum + "] [更改后:" + gdr.getNum() + "] [统计类型:" + recordAction + "] [统计类型:" + recordAction + "] [记录类型:" + recordAction.getRecordType() + "]");
						}
					} else {
						if (logger.isInfoEnabled()) {
							logger.info(player.getLogString() + "[修改了记录数据] [更改量:" + addNum + "] [更改后:" + gdr.getNum() + "] [统计类型:" + recordAction + "] [记录类型:" + recordAction.getRecordType() + "] [未做更改,原有值:" + gdr.getNum() + "]");
						}
					}
					break;
				case 只记录第一次:
					if (gdr.getNum() == -1) {// 没记录过
						gdr.setNum(addNum);
						gameDREm.notifyFieldChange(gdr, "num");
						if (logger.isInfoEnabled()) {
							logger.info(player.getLogString() + "[修改了记录数据] [更改量:" + addNum + "] [更改后:" + gdr.getNum() + "] [统计类型:" + recordAction + "]  [记录类型:" + recordAction.getRecordType() + "] [第一次记录:" + gdr.getNum() + "]");
						}
					}
					break;
				default:
					logger.error(player.getLogString() + "[记录数据] [异常] [未标记的记录类型:{}]", new Object[] { recordAction.getRecordType() });
					break;
				}
			}
			// check目前未达成的同类型的成就
			if (gdr != null) {
//				checkPlayerAchievement(player, gdr);
				PlayerAimeEntityManager.instance.checkPlayerAims(player, gdr, isNotify);
			}
			if(killAllBoss) {
				GameDataRecord dataRecord = new GameDataRecord();
				dataRecord.setId(gameDREm.nextId());
				dataRecord.setPlayerId(player.getId());
				dataRecord.setDataType(RecordAction.击杀所有修仙界世界boss.getType());
				dataRecord.setNum(1);
				PlayerAimeEntityManager.instance.checkPlayerAims(player, dataRecord, isNotify);
			}
			if(tempNum >= 50) {
				GameDataRecord dataRecord = new GameDataRecord();
				dataRecord.setId(gameDREm.nextId());
				dataRecord.setPlayerId(player.getId());
				dataRecord.setDataType(RecordAction.内防和外防心法都达到50重.getType());
				dataRecord.setNum(1);
				PlayerAimeEntityManager.instance.checkPlayerAims(player, dataRecord, isNotify);
			}
			if (allShengShou) {
				GameDataRecord dataRecord = new GameDataRecord();
				dataRecord.setId(gameDREm.nextId());
				dataRecord.setPlayerId(player.getId());
				dataRecord.setDataType(RecordAction.获得所有圣兽.getType());
				dataRecord.setNum(1);
				PlayerAimeEntityManager.instance.checkPlayerAims(player, dataRecord, isNotify);
			}
			if (allShenYuan) {
				GameDataRecord data = new GameDataRecord();
				data.setId(gameDREm.nextId());
				data.setPlayerId(player.getId());
				data.setDataType(RecordAction.深渊幽冥幻域.getType());
				data.setNum(1);
				PlayerAimeEntityManager.instance.checkPlayerAims(player, data, isNotify);
			}

		} catch (Exception e) {
			logger.error(player.getLogString() + "[记录数据异常] [recordAction:" + recordAction + "] [num:" + addNum + "]", e);
		}
	}

	/**
	 * 检查角色的成就,当前值是否满足达成成就
	 * 过滤之前已经完成了的
	 * @param player
	 * @param gdr
	 * @throws Exception
	 */
	private void checkPlayerAchievement(Player player, GameDataRecord gdr) throws Exception {
		long now = SystemTime.currentTimeMillis();
		if (player == null || gdr == null) {
			logger.error("[checkPlayerAchievement] [参数为NULL] [player:{}] [GameDataRecord:{}]", new Object[] { player, gdr });
			throw new IllegalArgumentException();
		}
		List<Achievement> achievements = getAchievementByDataType(gdr.getDataType());
		if (achievements == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(player.getLogString() + "[对于数据类型:{} 无配置的成就]", new Object[] { gdr.getDataType() });
			}
			return;
		}
		for (Achievement achievement : achievements) {
			if (achievement != null) {
				AchievementEntity ae = getPlayerAchievementEntity(player, achievement);
				if (ae != null) { // 已经达成{有记录则表示已达成}
					// if (logger.isDebugEnabled()) {
					// logger.debug(player.getLogString() + "[已达成成就:{}] [当前记录数据类型:{}]", new Object[] { achievement.getName(), gdr.toString() });
					// }
					continue;
				} else {
					synchronized (player) {
						ae = getPlayerAchievementEntity(player, achievement);
						if (ae == null) {
							if (achievement.getNum() <= gdr.getNum()) { // 玩家达成了这个成就.直接NEW出来
								AchievementEntity newAe = new AchievementEntity();
								newAe.setAchievementId(achievement.getId());
								newAe.setDeliverTime(now);
								newAe.setId(aeEm.nextId());
								newAe.setPlayerId(player.getId());
								aeEm.notifyNewObject(newAe);
								newAe.doOnDeliver(player);

								if (player.achievementEntityMap != null) {
									player.achievementEntityMap.put(newAe.getAchievementId(), newAe);
								}

								if (logger.isWarnEnabled()) {
									logger.warn(player.getLogString() + "[当前数据:{}] [成就达成] [成就:{}]", new Object[] { gdr.toString(), achievement.getName() });
								}
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug(player.getLogString() + "[当前数据类型:{}] [成就未达成] [成就:{}]", new Object[] { gdr.getDataType(), achievement.getName() });
								}
							}
						}// end if
					}// end sync

				}
			}
		}
	}

	private List<Achievement> getAchievementByDataType(int dataType) {
		return systemAchievementMap.get(dataType);
	}

	/**
	 * 得到玩家某个记录类型的已完成成就
	 * @param player
	 * @param achievement
	 * @return
	 */
	public AchievementEntity getPlayerAchievementEntity(Player player, Achievement achievement) {
		HashMap<Integer, AchievementEntity> map = getPlayerAchievementEntitys(player);
		if (map == null) {
			if (logger.isDebugEnabled()) {
				logger.debug(player.getLogString() + "[获得某个成就] [列表没取到] [成就:{" + achievement.getName() + "}]");
			}
			return null;
		}

		return map.get(achievement.getId());

	}

	/**
	 * 获得某玩家的所有成就列表
	 * 优先在缓存中获得,如果缓存中不存在该玩家记录,则去数据库中LOAD完成的所有成就
	 * @return
	 */
	public HashMap<Integer, AchievementEntity> getPlayerAchievementEntitys(Player player) {

		long now = SystemTime.currentTimeMillis();
		if (player.achievementEntityMap != null) return player.achievementEntityMap;
		synchronized (player) {
			if (player.achievementEntityMap != null) return player.achievementEntityMap;
			try {

				QuerySelect<AchievementEntity> querySelect = new QuerySelect<AchievementEntity>();
				List<AchievementEntity> queryList = querySelect.selectAll(AchievementEntity.class, " playerId = ?", new Object[] { player.getId() }, null, aeEm);
				if (queryList == null) {
					queryList = new ArrayList<AchievementEntity>();
				}
				HashMap<Integer, AchievementEntity> map = new HashMap<Integer, AchievementEntity>();
				for (AchievementEntity ae : queryList) {
					if (ae != null) {
						map.put(ae.getAchievementId(), ae);
					}
				}

				player.achievementEntityMap = map;

				if (logger.isInfoEnabled()) {
					logger.info(player.getLogString() + "[获取成就列表] [成功] [result:" + queryList.size() + "] [耗时" + (SystemTime.currentTimeMillis() - now) + "ms]");
				}
				return player.achievementEntityMap;

			} catch (Exception e) {
				logger.error(player.getLogString() + "[获取成就列表] [异常] [result:--] [耗时" + (SystemTime.currentTimeMillis() - now) + "ms]", e);
			}
		}
		return null;
	}

	/**
	 * 通过成就获得一个玩家的记录数据
	 * 这里不是一次都load出来,
	 * @param player
	 * @param action
	 * @return
	 */
	public GameDataRecord getPlayerDataRecord(Player player, RecordAction action) {
		HashMap<Integer, GameDataRecord> gdrMap = getPlayerDataRecords(player);
		if (gdrMap == null) {
			return null;
		}
		return gdrMap.get(action.getType());
	}

	/**
	 * 加载玩家的统计数据.?是否做成惰性?
	 * @param player
	 * @return
	 */
	public HashMap<Integer, GameDataRecord> getPlayerDataRecords(Player player) {
		long now = SystemTime.currentTimeMillis();
		if (player.gdrMap != null) return player.gdrMap;
		synchronized (player) {
			if (player.gdrMap != null) return player.gdrMap;
			try {
				QuerySelect<GameDataRecord> querySelect = new QuerySelect<GameDataRecord>();
				List<GameDataRecord> queryList = querySelect.selectAll(GameDataRecord.class, "playerId = ?", new Object[] { player.getId() }, null, gameDREm);
				if (queryList == null) {
					queryList = new ArrayList<GameDataRecord>();
				}
				HashMap<Integer, GameDataRecord> gdrMap = new HashMap<Integer, GameDataRecord>();
				for (GameDataRecord dataRecord : queryList) {
					gdrMap.put(dataRecord.getDataType(), dataRecord);
				}
				player.gdrMap = gdrMap;
				logger.error(player.getLogString() + "[加载统计数据] [成功] [result:" + queryList.size() + "] [耗时:" + (SystemTime.currentTimeMillis() - now) + "ms]");
				return player.gdrMap;
			} catch (Exception e) {
				logger.error(player.getLogString() + "[加载统计数据] [异常] [result:--] [耗时:" + (SystemTime.currentTimeMillis() - now) + "ms]", e);
			}
		}
		return null;

	}

	/**
	 * 得到系统中所有关注某一action的成就列表
	 * @param action
	 * @return
	 */
	public List<Achievement> getSystemAchievement(RecordAction action) {
		return getAchievementByDataType(action.getType());
	}

	/**
	 * 通过Id获得成就模板数据
	 * @param achievementId
	 * @return
	 */
	public Achievement getSystemAchievement(long achievementId) {
		for (Achievement achievement : achievementList) {
			if (achievementId == achievement.getId()) {
				return achievement;
			}
		}
		return null;
	}

	public void playerRemoveFromMemory(Player player) throws Exception {
		long now = SystemTime.currentTimeMillis();
		int count1 = 0;
		int count2 = 0;

		if (player.gdrMap != null) {
			Iterator<Integer> it = player.gdrMap.keySet().iterator();
			while (it.hasNext()) {
				Integer t = it.next();
				GameDataRecord r = player.gdrMap.get(t);
				if (r != null) {
					gameDREm.save(r);
					count1++;
				}
			}

		}

		if (player.achievementEntityMap != null) {
			Iterator<Integer> it = player.achievementEntityMap.keySet().iterator();
			while (it.hasNext()) {
				Integer t = it.next();
				AchievementEntity r = player.achievementEntityMap.get(t);
				if (r != null) {
					aeEm.save(r);
					count2++;
				}
			}
		}

		logger.error(player.getLogString() + "[下线保存计数据] [完成] [保存对象:" + count1 + "," + count2 + "个] [耗时" + (SystemTime.currentTimeMillis() - now) + "ms]");

	}

	private void loadFile() throws Exception {
		long startTime = SystemTime.currentTimeMillis();
		File file = new File(getFilePath());
		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				POIFSFileSystem pss = new POIFSFileSystem(is);
				HSSFWorkbook workbook = new HSSFWorkbook(pss);
				HSSFSheet sheet = workbook.getSheetAt(0);
				int maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 2; i < maxRow; i++) {
					HSSFRow row = sheet.getRow(i);
					HSSFCell cell = null;
					int index = 0;
					int id = (int) (row.getCell(index++).getNumericCellValue());
					int level = (int) (row.getCell(index++).getNumericCellValue());
					int actionType = (int) (row.getCell(index++).getNumericCellValue());
					RecordAction action = getAction(actionType);
					String maimType = row.getCell(index++).getStringCellValue();
					String subType = row.getCell(index++).getStringCellValue();
					long num = (long) (row.getCell(index++).getNumericCellValue());
					cell = row.getCell(index++);
					String name = cell == null ? "" : (cell.getStringCellValue());
					cell = row.getCell(index++);
					String des = cell == null ? "" : (cell.getStringCellValue());
					cell = row.getCell(index++);
					String fullDes = cell == null ? "" : (cell.getStringCellValue());
					if (fullDes == null || "".equals(fullDes)) {
						fullDes = des;
					}
					cell = row.getCell(index++);
					String prizeArticle = cell == null ? "" : cell.getStringCellValue();
					cell = row.getCell(index++);
					String prizeTitle = cell == null ? "" : cell.getStringCellValue();
					/***************10*************/
					cell = row.getCell(index++);
					long prizeAchievementNum = cell == null ? 0L : (long)cell.getNumericCellValue();
					prizeArticle = prizeArticle == null ? "" : prizeArticle;
					prizeTitle = prizeTitle == null ? "" : prizeTitle;
					cell = row.getCell(index++);
					String icon = cell == null ? "" : cell.getStringCellValue();
					icon = icon == null ? "" : icon;
					boolean showSchedule = (0 == (row.getCell(index++).getNumericCellValue()));

					Achievement achievement = new Achievement(id, level, action, maimType, subType, num, name, des, fullDes, prizeArticle, prizeTitle, prizeAchievementNum, icon, showSchedule);
					achievementList.add(achievement);
				}
				// 依赖其他成就配置
				sheet = workbook.getSheetAt(1);
				maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 1; i < maxRow; i++) {
					// Cell[] cells = sheet.getRow(i);
					HSSFRow row = sheet.getRow(i);
					int achievementId = (int) (row.getCell(0).getNumericCellValue());
					Achievement achievement = getSystemAchievement(achievementId);
					if (achievement == null) {
						throw new Exception("依赖配置错误,无效的成就ID:" + achievementId + ",行数:" + i);
					}
					Integer[] depends = StringTool.string2Array(row.getCell(1).getStringCellValue(), ",", Integer.class);
					if (depends != null) {
						for (int dependId : depends) {
							Achievement depAchievement = getSystemAchievement(dependId);
							if (depAchievement == null) {
								throw new Exception("依赖配置错误,无效的成就ID:" + dependId + ",行数:" + i);
							}
							if (!achievementDependMap.containsKey(depAchievement)) {
								achievementDependMap.put(depAchievement, new ArrayList<Achievement>());
							}
							achievementDependMap.get(depAchievement).add(achievement);
						}
					} else {
						throw new Exception("录入错误,依赖的ID录入,行数:" + i);
					}
				}
				// 左边栏配置
				sheet = workbook.getSheetAt(2);
				maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 1; i < maxRow; i++) {
					HSSFRow row = sheet.getRow(i);
					String mainName = (row.getCell(0).getStringCellValue());
					String[] subNames = StringTool.string2Array((row.getCell(1).getStringCellValue()), ",", String.class);
					LeftMenu leftMenu = new LeftMenu(mainName, subNames);
					leftMenus.add(leftMenu);
					logger.info("[系统初始化] [左边栏配置] [" + leftMenu.toString() + "]");

				}
				logger.error("[系统初始化] [加载成就配置完毕] [耗时:" + (SystemTime.currentTimeMillis() - startTime) + "ms]");
			} catch (Exception e) {
				throw e;
			}

		} else {
			throw new Exception("[配置文件不存在:" + getFilePath() + "]");
		}
	}

	public RecordType getRecordType(int recordTypeId) {
		for (RecordType recordType : RecordType.values()) {
			if (recordType.getId() == recordTypeId) {
				return recordType;
			}
		}
		throw new IllegalStateException("[recordTypeId:" + recordTypeId + "]");
	}

	public RecordAction getAction(int actionType) {
		for (RecordAction action : RecordAction.values()) {
			if (action.getType() == actionType) {
				return action;
			}
		}
		throw new IllegalStateException("[无效的actionType:" + actionType + "]");
	}

	public void init() throws Exception {
		
		long start = SystemTime.currentTimeMillis();
		try {
			gameDREm = SimpleEntityManagerFactory.getSimpleEntityManager(GameDataRecord.class);
			aeEm = SimpleEntityManagerFactory.getSimpleEntityManager(AchievementEntity.class);
			loadFile();
			for (Achievement achievement : achievementList) {
				{
					// 记录类型列表
					if (!systemAchievementMap.containsKey(achievement.getAction().getType())) {
						systemAchievementMap.put(achievement.getAction().getType(), new ArrayList<Achievement>());
					}
					systemAchievementMap.get(achievement.getAction().getType()).add(achievement);
				}
				{
					// 列表分类列表
					if (!mainSubMap.containsKey(achievement.getMaimType())) {
						mainSubMap.put(achievement.getMaimType(), new HashMap<String, List<Achievement>>());
					}
					if (!mainSubMap.get(achievement.getMaimType()).containsKey(achievement.getSubType())) {
						mainSubMap.get(achievement.getMaimType()).put(achievement.getSubType(), new ArrayList<Achievement>());
					}
					mainSubMap.get(achievement.getMaimType()).get(achievement.getSubType()).add(achievement);
				}
			}
			instance = this;
		} catch (Exception e) {
			logger.error("[系统初始化] [异常]", e);
			throw e;
		}
		ServiceStartRecord.startLog(this);
	}

	public HashMap<Integer, List<Achievement>> getSystemAchievementMap() {
		return systemAchievementMap;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setSystemAchievementMap(HashMap<Integer, List<Achievement>> systemAchievementMap) {
		this.systemAchievementMap = systemAchievementMap;
	}

	public HashMap<String, HashMap<String, List<Achievement>>> getMainSubMap() {
		return mainSubMap;
	}

	public void setMainSubMap(HashMap<String, HashMap<String, List<Achievement>>> mainSubMap) {
		this.mainSubMap = mainSubMap;
	}

	public List<LeftMenu> getLeftMenus() {
		return leftMenus;
	}

	public void setLeftMenus(List<LeftMenu> leftMenus) {
		this.leftMenus = leftMenus;
	}

	public List<Achievement> getAchievementList() {
		return achievementList;
	}

	public void setAchievementList(List<Achievement> achievementList) {
		this.achievementList = achievementList;
	}

	public HashMap<Achievement, List<Achievement>> getAchievementDependMap() {
		return achievementDependMap;
	}

	public void destroy() {
		if (gameDREm != null) {
			gameDREm.destroy();
		}
		if (aeEm != null) {
			aeEm.destroy();
		}
	}
}