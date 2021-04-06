package com.fy.engineserver.septstation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;

import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.activity.datamanager.AbstractActivity;
import com.fy.engineserver.activity.datamanager.ActivityConstant;
import com.fy.engineserver.activity.datamanager.ActivityDataManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;

/**
 * 家族驻地地图模板数据
 * 
 * 
 */
@CheckAttribute("家族驻地地图模板数据")
public class SeptStationMapTemplet implements AbstractActivity{

	private static SeptStationMapTemplet instance;

	@CheckAttribute("RES_PASH")
	private String RES_PASH = "";

	public static Logger logger = JiazuSubSystem.logger;

	/** 家族内的建筑位置 */
	@CheckAttribute(value = "家族内的建筑位置", des = "<Index,Station>")
	private HashMap<Integer, NpcStation> septNpcStations = new HashMap<Integer, NpcStation>();// <NPCINDEX,STATION>

	private HashMap<Integer, List<ActivityInfo>> activityInfos = new HashMap<Integer, List<ActivityInfo>>();

	/** 家族徽章 */
	@CheckAttribute("家族徽章")
	private Map<Integer, ArrayList<JiazuBedge>> bedges = new HashMap<Integer, ArrayList<JiazuBedge>>();

	private List<JiazuBedge> bedgeList = new ArrayList<JiazuBedge>();

	// @CheckAttribute("buildingTemplet")
	// private SeptBuildingTemplet buildingTemplet;
	/** 家族建设任务NPC */
	@CheckAttribute("家族建设任务NPC")
	private String buildingTaskNpc;
	/** 家族建设任务组名 */
	@CheckAttribute("家族建设任务组名")
	private String buildingTaskCollection;

	@CheckAttribute("家族召唤BOSS名字")
	private Integer[] jiazuBossIds;

	@CheckAttribute("家族召唤BOSS花费")
	private Long[] jiazuBossCost;

	@CheckAttribute("家族召唤BOSS位置")
	private Point bossPoint;
	@CheckAttribute("家族boss活动复活点")
	private String reliveName;
	@CheckAttribute("家族boss活动开始时间[只有部分数据]")
	private Calendar[] startTimes;
	@CheckAttribute("家族boss活动结束时间[只有部分数据]")
	private Calendar[] endTimes;
	@CheckAttribute("家族boss活动持续时间")
	private int continuance;
	@CheckAttribute("家族boss活动奖励")
	private String[] prizes;

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private SeptStationMapTemplet() {

	}

	public static SeptStationMapTemplet getInstance() {
		return instance;
	}

	/**
	 * 通过索引得到NPC的位置
	 * 2011-4-28
	 * 
	 * @param index
	 * @return NpcStation
	 * 
	 */
	public NpcStation getNpcStation(int index) {
		for (NpcStation npcPoint : septNpcStations.values()) {
			if (npcPoint.getIndex() == index) {
				return npcPoint;
			}
		}
		return null;
	}

	public NpcStation getNpcStationByCategoryId(int categoryId) {
		for (NpcStation npcPoint : septNpcStations.values()) {
			if (npcPoint.getNpcTempletId() == categoryId) {
				return npcPoint;
			}
		}
		return null;
	}

	/**
	 * 通过建筑类型获得NPC位置
	 * @param buildingType
	 * @return
	 */
	public NpcStation getNpcStationByType(int buildingType) {
		for (NpcStation npcPoint : septNpcStations.values()) {
			if (npcPoint.getBuilingType() == buildingType) {
				return npcPoint;
			}
		}
		return null;
	}

	public HashMap<Integer, NpcStation> getSeptNpcStations() {
		return septNpcStations;
	}

	public void setMap(HashMap<Integer, NpcStation> map) {
		this.septNpcStations = map;
	}

	public String getBuildingTaskNpc() {
		return buildingTaskNpc;
	}

	public String getBuildingTaskCollection() {
		return buildingTaskCollection;
	}

	public void load() throws Throwable {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		File file = new File(RES_PASH);
		if (file.exists() && file.isFile()) {
			try {
				InputStream is = new FileInputStream(file);
				POIFSFileSystem pss = new POIFSFileSystem(is);
				HSSFWorkbook workbook = new HSSFWorkbook(pss);
				HSSFSheet sheet = workbook.getSheetAt(1);// 驻地内建筑位置配置
				int maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 1; i < maxRow; i++) {
					HSSFRow row = sheet.getRow(i);
					int index = 0;
					int id = (int) row.getCell(index++).getNumericCellValue();
					HSSFCell cell = row.getCell(index++);
					if (cell == null) {
						System.out.println("cell is null");
					}
					int x = StringTool.getCellValue(cell, Integer.class);
					int y = StringTool.getCellValue(row.getCell(index++), Integer.class);
					String buildingName = row.getCell(index++).getStringCellValue();// 建筑名字
					int npcTempletId = (int) row.getCell(index++).getNumericCellValue();
					String des = row.getCell(index++).getStringCellValue();// 描述
					int buildingType = -1;
					for (BuildingType type : BuildingType.values()) {
						if (Game.logger.isDebugEnabled()) {
							logger.debug("[trace_sept] [name:" + type.getName() + "] [buildingName:" + buildingName + "] [match:" + (type.getName().startsWith(buildingName)) + "]");
						}
						if (type.getName().startsWith(buildingName)) {// 箭楼的原因
							buildingType = type.getIndex();
							break;
						}
					}
					if (buildingType == -1) {
						throw new IllegalStateException("名字配置错误[" + buildingName + "]");
					}
					NpcStation npcStation = new NpcStation(x, y, id, buildingType, npcTempletId, buildingName, des);
					logger.info("[加载驻地内建筑信息]{}", new Object[] { npcStation.toString() });

					septNpcStations.put(npcStation.getIndex(), npcStation);
					if (logger.isInfoEnabled()) {
						logger.info("[服务器启动] [加载XML配置文件]" + npcStation.toString());
					}
				}

				sheet = workbook.getSheetAt(2);// 家族徽章配置
				maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 1; i < maxRow; i++) {
					HSSFRow row = sheet.getRow(i);
					int index = 0;
					int id = (int) row.getCell(index++).getNumericCellValue();
					int color = (int) row.getCell(index++).getNumericCellValue();
					int levelLimit = (int) row.getCell(index++).getNumericCellValue();
					String name = row.getCell(index++).getStringCellValue();
					String resName = row.getCell(index++).getStringCellValue();
					int price = (int) row.getCell(index++).getNumericCellValue();
					String des = row.getCell(index++).getStringCellValue();
					int type = (int) row.getCell(index++).getNumericCellValue();

					JiazuBedge bedge = new JiazuBedge(id, name, resName, color, price, levelLimit, des, type);
					if (!getBedges().containsKey(bedge.getLevelLimit())) {
						getBedges().put(bedge.getLevelLimit(), new ArrayList<JiazuBedge>());
					}
					getBedges().get(bedge.getLevelLimit()).add(bedge);
					bedgeList.add(bedge);
					logger.info("[加载了家族徽章] [{}]", new Object[] { bedge.toString() });
				}
				sheet = workbook.getSheetAt(3);// 家族任务怪物配置
				maxRow = sheet.getPhysicalNumberOfRows();
				HSSFRow row = sheet.getRow(1);
				int index = 0;
				buildingTaskNpc = row.getCell(index++).getStringCellValue();
				buildingTaskCollection = row.getCell(index++).getStringCellValue();
				if (logger.isInfoEnabled()) {
					logger.info("[加载家族驻地信息][buildingTaskNpc:{}][buildingTaskCollection:{}]", new Object[] { buildingTaskNpc, buildingTaskCollection });
				}
				jiazuBossIds = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Integer.class);
				jiazuBossCost = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Long.class);
				bossPoint = new Point((int) row.getCell(index++).getNumericCellValue(), (int) row.getCell(index++).getNumericCellValue());
				reliveName = row.getCell(index++).getStringCellValue();
				startTimes = new Calendar[2];
				startTimes[0] = Calendar.getInstance();
				startTimes[1] = Calendar.getInstance();
				String temp = StringTool.getCellValue(row.getCell(index++), String.class);
				Integer[] time = StringTool.string2Array(temp, ":", Integer.class);// [周,小时,分钟]

				startTimes[0].set(Calendar.DAY_OF_WEEK, time[0]);
				startTimes[0].set(Calendar.HOUR_OF_DAY, time[1]);
				startTimes[0].set(Calendar.MINUTE, time[2]);
				time = StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ":", Integer.class);// [周,小时,分钟]

				startTimes[1].set(Calendar.DAY_OF_WEEK, time[0]);
				startTimes[1].set(Calendar.HOUR_OF_DAY, time[1]);
				startTimes[1].set(Calendar.MINUTE, time[2]);
				int minute = (int) row.getCell(index++).getNumericCellValue();
				continuance = minute;
				prizes = StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", String.class);

				endTimes = new Calendar[startTimes.length];
				endTimes[0] = Calendar.getInstance();
				endTimes[1] = Calendar.getInstance();
				for (int i = 0; i < endTimes.length; i++) {
					Calendar c = endTimes[i];
					c.setTimeInMillis(startTimes[i].getTimeInMillis());
					c.add(Calendar.MINUTE, minute);
				}
				for (int k = 0; k < startTimes.length; k++) {
					logger.error("[家族BOSS] [开始时间" + sdf.format(startTimes[k].getTime()) + ",星期值:" + startTimes[k].get(Calendar.DAY_OF_WEEK) + "] [结束时间" + sdf.format(endTimes[k].getTime()) + ",星期值:" + endTimes[k].get(Calendar.DAY_OF_WEEK) + "]");
				}

				sheet = workbook.getSheetAt(4);// 依赖建筑变化的地表NPC
				maxRow = sheet.getPhysicalNumberOfRows();
				for (int i = 1; i < maxRow; i++) {
					row = sheet.getRow(i);
					int dependIndex = (int) row.getCell(0).getNumericCellValue();// Integer.valueOf(cells[0].getContents());
					long npcId = (long) row.getCell(1).getNumericCellValue();
					int x = StringTool.getCellValue(row.getCell(2), Integer.class);
					int y = StringTool.getCellValue(row.getCell(3), int.class);
					GroundNpcStation groundNpcStation = new GroundNpcStation(dependIndex, npcId, x, y);
					NpcStation ns = septNpcStations.get(groundNpcStation.getDependIndex());
					if (ns != null) {
						ns.getGroundNpcStation().add(groundNpcStation);
						if (logger.isInfoEnabled()) {
							logger.info("[加载家族驻地信息] [NpcStation:{}]", new Object[] { ns });
						}
					} else {
						logger.error("[依赖地表录入异常] [依赖建筑索引不存在:{}]", new Object[] { groundNpcStation.getDependIndex() });
					}
				}
				/****************************** 活动数据 *******************/
				sheet = workbook.getSheetAt(5);// 依赖建筑变化的地表NPC
				maxRow = sheet.getPhysicalNumberOfRows();
				activityInfos = new HashMap<Integer, List<ActivityInfo>>();
				for (int i = 1; i < maxRow; i++) {
					row = sheet.getRow(i);
					Integer id = StringTool.getCellValue(row.getCell(0), int.class);
					String title = StringTool.getCellValue(row.getCell(1), String.class);
					String icon = StringTool.getCellValue(row.getCell(2), String.class);
					String name = StringTool.getCellValue(row.getCell(3), String.class);
					String content = StringTool.getCellValue(row.getCell(4), String.class);
					ActivityInfo activityInfo = new ActivityInfo(id, name, title, icon, content);
					if (!activityInfos.containsKey(id)) {
						activityInfos.put(id, new ArrayList<ActivityInfo>());
					}
					activityInfos.get(id).add(activityInfo);
				}
				logger.info("[家族活动数据] [加载完毕]");
			} catch (Throwable e) {
				logger.error("[加载家族驻地信息异常]", e);
				throw e;
			} finally {
			}
		} else {
			logger.error("[加载资源] [加载家族驻地] [地图模板 不存在] [{}]", new Object[] { RES_PASH });
			throw new Exception();
		}
		logger.info("[加载资源] [加载家族驻地模板][耗时]{}", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
	}

	public HashMap<Integer, List<ActivityInfo>> getActivityInfos() {
		return activityInfos;
	}

	public void setActivityInfos(HashMap<Integer, List<ActivityInfo>> activityInfos) {
		this.activityInfos = activityInfos;
	}

	public void init() throws Throwable {
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		instance = this;
		instance.load();
		logger.error("[加载家族驻地地图模板][耗时：{}毫秒]", new Object[] { (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start) });
		ServiceStartRecord.startLog(this);
	}

	/***************************************** getters and setters *****************************************/
	public String getRES_PASH() {
		return RES_PASH;
	}

	public void setRES_PASH(String rES_PASH) {
		RES_PASH = rES_PASH;
	}

	public Map<Integer, ArrayList<JiazuBedge>> getBedges() {
		return bedges;
	}

	public void setBedges(Map<Integer, ArrayList<JiazuBedge>> bedges) {
		this.bedges = bedges;
	}

	/**
	 * 是否是家族召唤BOSS
	 * @param bossName
	 * @return
	 */
	public boolean isJiazuBoss(Monster monster) {
		for (int id : jiazuBossIds) {
			if (monster.getSpriteCategoryId() == id) {
				return true;
			}
		}
		return false;
	}

	public JiazuBedge getBedge(int bedgeId) {
		for (Iterator<Integer> it = getBedges().keySet().iterator(); it.hasNext();) {
			int grade = it.next();
			if (getBedges().get(grade) != null) {
				for (JiazuBedge bedge : getBedges().get(grade)) {
					if (bedge.getId() == bedgeId) {
						return bedge;
					}
				}
			} else {
			}
		}
		return null;
	}

	/**
	 * 是否在BOSS活动期间
	 * @param calendar
	 * @return
	 */
	public boolean isInbossActivityTime(Calendar calendar) {
		for (int i = 0; i < startTimes.length; i++) {
			Calendar startTime = startTimes[i];
			Calendar endTime = endTimes[i];
			if (startTime != null && endTime != null) {
				if (calendar.get(Calendar.DAY_OF_WEEK) == startTime.get(Calendar.DAY_OF_WEEK)) { // 星期符合
					int startTimeMinute = startTime.get(Calendar.HOUR_OF_DAY) * 60 + startTime.get(Calendar.MINUTE);
					int endTimeMinute = endTime.get(Calendar.HOUR_OF_DAY) * 60 + endTime.get(Calendar.MINUTE);
					int calenderTimeMinute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
					if (calenderTimeMinute >= startTimeMinute && calenderTimeMinute <= endTimeMinute) {// 分钟符合
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<JiazuBedge> getBedgeList() {
		return bedgeList;
	}

	public void setBedgeList(List<JiazuBedge> bedgeList) {
		this.bedgeList = bedgeList;
	}

	public Integer[] getJiazuBossIds() {
		return jiazuBossIds;
	}

	public void setJiazuBossIds(Integer[] jiazuBossIds) {
		this.jiazuBossIds = jiazuBossIds;
	}

	public Long[] getJiazuBossCost() {
		return jiazuBossCost;
	}

	public void setJiazuBossCost(Long[] jiazuBossCost) {
		this.jiazuBossCost = jiazuBossCost;
	}

	public Point getBossPoint() {
		return bossPoint;
	}

	public void setBossPoint(Point bossPoint) {
		this.bossPoint = bossPoint;
	}

	public String getReliveName() {
		return reliveName;
	}

	public void setReliveName(String reliveName) {
		this.reliveName = reliveName;
	}

	public Calendar[] getStartTimes() {
		return startTimes;
	}

	public void setStartTimes(Calendar[] startTimes) {
		this.startTimes = startTimes;
	}

	public Calendar[] getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(Calendar[] endTimes) {
		this.endTimes = endTimes;
	}

	public int getContinuance() {
		return continuance;
	}

	public void setContinuance(int continuance) {
		this.continuance = continuance;
	}

	public String[] getPrizes() {
		return prizes;
	}

	public void setPrizes(String[] prizes) {
		this.prizes = prizes;
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.HOUR));
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
	}

	@Override
	public String getActivityName() {
		// TODO Auto-generated method stub
		return ActivityConstant.异界入侵;
	}

	@Override
	public List<String> getActivityOpenTime(long now) {
		// TODO Auto-generated method stub
		List<String> result = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		for (int i = 0; i < startTimes.length; i++) {
			if (startTimes[i].get(Calendar.DAY_OF_WEEK) == cal.get(Calendar.DAY_OF_WEEK)) {
				result.add(startTimes[i].get(Calendar.HOUR_OF_DAY) + ":" + ActivityDataManager.getMintisStr(startTimes[i].get(Calendar.MINUTE)));
			}
		}
		return result;
	}

	@Override
	public boolean isActivityTime(long now) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		return this.isInbossActivityTime(cal);
	}
}
