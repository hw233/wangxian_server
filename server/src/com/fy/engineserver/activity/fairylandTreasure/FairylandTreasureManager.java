package com.fy.engineserver.activity.fairylandTreasure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.RefreshSpriteManager;
import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.activity.pickFlower.FlushPoint;
import com.fy.engineserver.battlefield.concrete.BattleFieldLineupService.WaittingItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.START_DRAW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.TimerTask;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class FairylandTreasureManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(FairylandTreasureManager.class.getName());
	private static FairylandTreasureManager instance;

	public static FairylandTreasureManager getInstance() {
		return instance;
	}

	private List<FairylandTreasureActivity> activityList = new ArrayList<FairylandTreasureActivity>();

	private List<FairylandBox> fairylandBoxList = new ArrayList<FairylandBox>();
	public Map<Integer, String> prayTypeMap = new HashMap<Integer, String>(); // 祈福类型Map<类型id,祈福类型>;
	public List<FlushPoint> pointList = new ArrayList<FlushPoint>(); // 坐标点
	public List<AddExpBean> addExpList = new ArrayList<AddExpBean>(); // 经验
	long[] addGongZi = new long[5];
	int[] addYuanQi = new int[5];

	private int maxOpenNum; // 每人每天开宝箱上限(所有类型宝箱总数)
	private long prayCost; // 祈福扣费(单位:文)
	private long prayLastingTime; // 祈福持续时间(单位:毫秒)

	private Map<Integer, List<ArticleForDraw>> drawMap = new HashMap<Integer, List<ArticleForDraw>>(); // 道具库Map<祈福类型, List<ArticleForDraw>>
	private Map<Integer, List<ArticleForDraw>> drawSureShowMap = new HashMap<Integer, List<ArticleForDraw>>(); // 必现道具库
	public Map<Long, ArticleForDraw> tempDrawMap = new HashMap<Long, ArticleForDraw>(); // Map<临时物品id,ArticleForDraw>

	public static Random random = new Random();

	public static final int CELLNUM = 24; // 箱子中格子个数
	public static int 自动发送等待时间 = 60 * 1000;

	private List<WaitTimeSend> waitTimeSendList = new LinkedList<WaitTimeSend>();// 待发送的抽奖结果

	// public List<WaitForSend> waitList = new ArrayList<WaitForSend>();// 待发送的抽奖结果
	public DiskCache disk = null;// <年月日+playerid,开宝箱数量>
	private String diskFile;

	private String filePath;

	public void init() throws Exception {
		
		long now = System.currentTimeMillis();
		instance = this;
		loadXml();
		File file = new File(diskFile);
		disk = new DefaultDiskCache(file, null, "fairylandTreasure", 100L * 365 * 24 * 3600 * 1000L);
		Thread t = new Thread(this, "仙界宝箱心跳");
		t.start();
		logger.error("[仙界宝箱初始化完成]");
		ServiceStartRecord.startLog(this);
	}

	public static final int 仙界宝箱 = 0;
	public static final int 宝箱信息 = 1;
	public static final int 祈福类型 = 2;
	public static final int 仙界宝箱刷新点 = 3;
	public static final int 仙界宝箱上限 = 4;
	public static final int 经验数值 = 5;
	public static final int 人物经验 = 6;
	public static final int 人物经验必现 = 7;
	public static final int 工资数值 = 8;
	public static final int 工资 = 9;
	public static final int 工资必现 = 10;
	public static final int 元气数值 = 11;
	public static final int 元气 = 12;
	public static final int 元气必现 = 13;
	public static final int 神兽碎片 = 14;
	public static final int 神兽碎片必现 = 15;
	public static final int 技能碎片 = 16;
	public static final int 技能碎片必现 = 17;
	public static final int 宠物经验 = 18;
	public static final int 宠物经验必现 = 19;

	// public static void main(String[] args) {
	// FairylandTreasureManager ftm=new FairylandTreasureManager();
	// try {
	// ftm.testloadXml();
	// System.out.println("完成");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	public void testloadXml() throws Exception {

		File file = new File("E:\\code\\game_mieshi_server\\conf\\game_init_config\\fairylandTreasure.xls");
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;

			sheet = hssfWorkbook.getSheetAt(仙界宝箱);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platForms = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServerNames = "";
					if (cell != null) {
						openServerNames = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String notOpenServers = "";
					if (cell != null) {
						notOpenServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String timeHours = cell.getStringCellValue().trim();
					cell = row.getCell(index++);
					String timeMinits = cell.getStringCellValue().trim();

					List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
					String[] tempP1 = timeHours.split("\\|");
					String[] tempP2 = timeMinits.split("\\|");
					if (tempP1.length != tempP2.length) {
						throw new Exception("小时与分钟配表不匹配！");
					}
					for (int j = 0; j < tempP1.length; j++) {
						int[] point1 = RefreshSpriteManager.parse2Int(tempP1[j].split(","));
						int[] point2 = RefreshSpriteManager.parse2Int(tempP2[j].split(","));
						if ((point1.length != 2) || (point2.length != 2)) {
							throw new Exception("小时或者分钟开启结束时间不匹配！");
						}
						times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
					}
					int lastingTime = (int) row.getCell(index++).getNumericCellValue();
					int refreshSpace = (int) row.getCell(index++).getNumericCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(宝箱信息);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int npcId = (int) cell.getNumericCellValue();
					String boxName = row.getCell(index++).getStringCellValue();
					String boxNameStat = row.getCell(index++).getStringCellValue();
					int boxType = (int) row.getCell(index++).getNumericCellValue();
					long needSilver = (long) row.getCell(index++).getNumericCellValue();
					int num = (int) row.getCell(index++).getNumericCellValue();
					String rateString = row.getCell(index++).getStringCellValue();
					String prayRateString = row.getCell(index++).getStringCellValue();
					Integer[] rateInteger = StringTool.string2Array(rateString, ",", Integer.class);
					int[] rates = new int[rateInteger.length];
					for (int j = 0; j < rateInteger.length; j++) {
						rates[j] = rateInteger[j].intValue();
					}
					Integer[] prayRateInteger = StringTool.string2Array(prayRateString, ",", Integer.class);
					int[] prayRates = new int[prayRateInteger.length];
					for (int j = 0; j < prayRateInteger.length; j++) {
						prayRates[j] = prayRateInteger[j].intValue();
					}
					String boxKeyName = row.getCell(index++).getStringCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(祈福类型);
			prayTypeMap.put(0, Translate.无);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int typeId = (int) cell.getNumericCellValue();
					String type = row.getCell(index++).getStringCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(经验数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int level = (int) cell.getNumericCellValue();
					long[] addExp = new long[5];
					addExp[0] = (long) row.getCell(index++).getNumericCellValue();
					addExp[1] = (long) row.getCell(index++).getNumericCellValue();
					addExp[2] = (long) row.getCell(index++).getNumericCellValue();
					addExp[3] = (long) row.getCell(index++).getNumericCellValue();
					addExp[4] = (long) row.getCell(index++).getNumericCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(仙界宝箱刷新点);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String map = cell.getStringCellValue();
					byte country = (byte) row.getCell(index++).getNumericCellValue();
					int x = (int) row.getCell(index++).getNumericCellValue();
					int y = (int) row.getCell(index++).getNumericCellValue();

				}
			}

			sheet = hssfWorkbook.getSheetAt(仙界宝箱上限);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					maxOpenNum = (int) cell.getNumericCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(工资数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			HSSFRow row = sheet.getRow(1);
			if (row != null) {
				int index = 0;
				addGongZi[0] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[1] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[2] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[3] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[4] = (long) row.getCell(index++).getNumericCellValue();
			}

			sheet = hssfWorkbook.getSheetAt(元气数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			row = sheet.getRow(1);
			if (row != null) {
				int index = 0;
				addYuanQi[0] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[1] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[2] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[3] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[4] = (int) row.getCell(index++).getNumericCellValue();
			}

			loadArticleForDraw(hssfWorkbook, 人物经验, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 人物经验必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 工资, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 工资必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 元气, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 元气必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 神兽碎片, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 神兽碎片必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 技能碎片, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 技能碎片必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 宠物经验, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 宠物经验必现, drawSureShowMap, false);

		} catch (Exception e) {
			throw e;
		}

	}

	public void loadXml() throws Exception {
		File file = new File(filePath);
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

			sheet = hssfWorkbook.getSheetAt(仙界宝箱);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platForms = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServerNames = "";
					if (cell != null) {
						openServerNames = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String notOpenServers = "";
					if (cell != null) {
						notOpenServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String timeHours = cell.getStringCellValue().trim();
					cell = row.getCell(index++);
					String timeMinits = cell.getStringCellValue().trim();

					List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
					String[] tempP1 = timeHours.split("\\|");
					String[] tempP2 = timeMinits.split("\\|");
					if (tempP1.length != tempP2.length) {
						throw new Exception("小时与分钟配表不匹配！");
					}
					for (int j = 0; j < tempP1.length; j++) {
						int[] point1 = RefreshSpriteManager.parse2Int(tempP1[j].split(","));
						int[] point2 = RefreshSpriteManager.parse2Int(tempP2[j].split(","));
						if ((point1.length != 2) || (point2.length != 2)) {
							throw new Exception("小时或者分钟开启结束时间不匹配！");
						}
						times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
					}
					int lastingTime = (int) row.getCell(index++).getNumericCellValue();
					int refreshSpace = (int) row.getCell(index++).getNumericCellValue();
					FairylandTreasureActivity fta = new FairylandTreasureActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
					fta.setOtherVar(times, lastingTime, refreshSpace);
					activityList.add(fta);
					logger.warn("[仙界宝箱加载] [sheet:" + sheet.getSheetName() + "] [第" + i + "行]"); // i从0开始算
				}
			}

			sheet = hssfWorkbook.getSheetAt(宝箱信息);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int npcId = (int) cell.getNumericCellValue();
					String boxName = row.getCell(index++).getStringCellValue();
					String boxNameStat = row.getCell(index++).getStringCellValue();
					int boxType = (int) row.getCell(index++).getNumericCellValue();
					long needSilver = (long) row.getCell(index++).getNumericCellValue();
					int num = (int) row.getCell(index++).getNumericCellValue();
					String rateString = row.getCell(index++).getStringCellValue();
					String prayRateString = row.getCell(index++).getStringCellValue();
					Integer[] rateInteger = StringTool.string2Array(rateString, ",", Integer.class);
					int[] rates = new int[rateInteger.length];
					for (int j = 0; j < rateInteger.length; j++) {
						rates[j] = rateInteger[j].intValue();
					}
					Integer[] prayRateInteger = StringTool.string2Array(prayRateString, ",", Integer.class);
					int[] prayRates = new int[prayRateInteger.length];
					for (int j = 0; j < prayRateInteger.length; j++) {
						prayRates[j] = prayRateInteger[j].intValue();
					}
					String boxKeyName = row.getCell(index++).getStringCellValue();
					FairylandBox box = new FairylandBox(npcId, boxName, boxNameStat, boxType, needSilver, num, rates, prayRates, boxKeyName);
					fairylandBoxList.add(box);
				}
			}

			sheet = hssfWorkbook.getSheetAt(祈福类型);
			prayTypeMap.put(0, Translate.无);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int typeId = (int) cell.getNumericCellValue();
					String type = row.getCell(index++).getStringCellValue();
					prayTypeMap.put(typeId, type);
				}
			}

			sheet = hssfWorkbook.getSheetAt(经验数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					int level = (int) cell.getNumericCellValue();
					long[] addExp = new long[5];
					addExp[0] = (long) row.getCell(index++).getNumericCellValue();
					addExp[1] = (long) row.getCell(index++).getNumericCellValue();
					addExp[2] = (long) row.getCell(index++).getNumericCellValue();
					addExp[3] = (long) row.getCell(index++).getNumericCellValue();
					addExp[4] = (long) row.getCell(index++).getNumericCellValue();
					AddExpBean addExpBean = new AddExpBean(level, addExp);
					addExpList.add(addExpBean);
				}
			}

			sheet = hssfWorkbook.getSheetAt(仙界宝箱刷新点);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					String map = cell.getStringCellValue();
					byte country = (byte) row.getCell(index++).getNumericCellValue();
					int x = (int) row.getCell(index++).getNumericCellValue();
					int y = (int) row.getCell(index++).getNumericCellValue();

					FlushPoint fp = new FlushPoint(map, country, x, y);

					if (GameManager.getInstance().getGameByName(map, country) == null) {
						throw new RuntimeException("仙界宝箱位置配置表错误，不存在的地图，第几行" + i);
					}

					for (FlushPoint p : pointList) {
						if (p.equals(fp)) {
							throw new RuntimeException("仙界宝箱位置配置表错误，同样的坐标存在，第几行" + i);
						}
					}
					pointList.add(fp);
				}
			}

			sheet = hssfWorkbook.getSheetAt(仙界宝箱上限);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						System.out.println("读到了空白");
						break;
					}
					maxOpenNum = (int) cell.getNumericCellValue();
					prayCost = (long) row.getCell(index++).getNumericCellValue();
					prayLastingTime = (long) row.getCell(index++).getNumericCellValue();
				}
			}

			sheet = hssfWorkbook.getSheetAt(工资数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			HSSFRow row = sheet.getRow(1);
			if (row != null) {
				int index = 0;
				addGongZi[0] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[1] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[2] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[3] = (long) row.getCell(index++).getNumericCellValue();
				addGongZi[4] = (long) row.getCell(index++).getNumericCellValue();
			}

			sheet = hssfWorkbook.getSheetAt(元气数值);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			row = sheet.getRow(1);
			if (row != null) {
				int index = 0;
				addYuanQi[0] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[1] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[2] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[3] = (int) row.getCell(index++).getNumericCellValue();
				addYuanQi[4] = (int) row.getCell(index++).getNumericCellValue();
			}

			loadArticleForDraw(hssfWorkbook, 人物经验, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 人物经验必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 工资, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 工资必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 元气, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 元气必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 神兽碎片, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 神兽碎片必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 技能碎片, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 技能碎片必现, drawSureShowMap, false);
			loadArticleForDraw(hssfWorkbook, 宠物经验, drawMap, true);
			loadArticleForDraw(hssfWorkbook, 宠物经验必现, drawSureShowMap, false);

		} catch (Exception e) {
			throw e;
		}
	}

	public int getPrayTypeId(String type) {
		for (int prayType : prayTypeMap.keySet()) {
			if (type.matches(prayTypeMap.get(prayType) + ".*")) {
				return prayType;
			}
		}
		if (logger.isErrorEnabled()) {
			logger.error("[仙界宝箱] [未获取到祈福类型id] [祈福类型:" + type + "]");
		}
		return 1;
	}

	public void loadArticleForDraw(HSSFWorkbook hssfWorkbook, int sheetIndex, Map<Integer, List<ArticleForDraw>> map, boolean creatTempArticle) throws Exception {
		HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetIndex);
		List<ArticleForDraw> afdList = new ArrayList<ArticleForDraw>();
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rows; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int index = 0;
				HSSFCell cell = row.getCell(index++);
				if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					System.out.println("读到了空白");
					break;
				}
				int id = (int) cell.getNumericCellValue();
				String name = row.getCell(index++).getStringCellValue();
				String nameStat = row.getCell(index++).getStringCellValue();
				int color = (int) row.getCell(index++).getNumericCellValue();
				int num = (int) row.getCell(index++).getNumericCellValue();
				boolean bind = row.getCell(index++).getBooleanCellValue();
				boolean worth = row.getCell(index++).getBooleanCellValue();
				boolean broadcast = row.getCell(index++).getBooleanCellValue();
				int[] rates = new int[3];
				rates[0] = (int) row.getCell(index++).getNumericCellValue();
				rates[1] = (int) row.getCell(index++).getNumericCellValue();
				rates[2] = (int) row.getCell(index++).getNumericCellValue();
				ArticleForDraw afd = new ArticleForDraw(id, name, nameStat, color, num, bind, worth, broadcast, rates);
				Article a = ArticleManager.getInstance().getArticleByCNname(nameStat);
				if (a != null && creatTempArticle) {
					ArticleEntity ae = ArticleEntityManager.getInstance().createTempEntity(a, bind, ArticleEntityManager.仙界宝箱, null, color);
					long tempArticleId = ae.getId();
					afd.setTempArticleId(tempArticleId);
					tempDrawMap.put(tempArticleId, afd);
				}
				afdList.add(afd);
			}
			int prayTypeId = getPrayTypeId(sheet.getSheetName());
			map.put(prayTypeId, afdList);
		}
	}

	/**
	 * 获取当前日期串,格式20150630
	 * @return
	 */
	public String getCurrentDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		return sdf.format(date);
	}

	public static int 开宝箱距离 = 100 * 100;

	public FairylandTreasureEntity fairylandTreasureEntity = null;
	public static boolean open = true;
	private long startTimeRecord;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		while (open) {
			try {
				Thread.sleep(200);
				if (waitTimeSendList.size() > 0) {
					for (WaitTimeSend wts : waitTimeSendList) {
						if ((wts.getSendTime() - System.currentTimeMillis()) <= 0) {
							sendToPlayerByMail(wts.getPlayer(), wts.getBoxName(), wts.getAfd());
						}
					}
				}
				if (fairylandTreasureEntity == null) {
					for (FairylandTreasureActivity fta : activityList) {
						Calendar currentCalender = Calendar.getInstance();
						if (fta.isThisServerFit() == null) {
							// 根据时间段判断活动是否开启
							DayilyTimeDistance dtd = fta.getNowDayilyTimeDistance();
							if (dtd != null && dtd.inthisTimeDistance(currentCalender)) {
								Calendar todayStart = Calendar.getInstance();
								todayStart.set(Calendar.HOUR_OF_DAY, 0);
								todayStart.set(Calendar.MINUTE, 0);
								todayStart.set(Calendar.SECOND, 0);
								todayStart.set(Calendar.MILLISECOND, 0);
								long now = System.currentTimeMillis();
								startTimeRecord = todayStart.getTimeInMillis() + dtd.getStartHour() * 60 * 60 * 1000 + dtd.getStartMinute() * 60 * 1000;
								// logger.error("开始时间 " + sdf.format(new Date(startTimeRecord)));
								int times = (int) ((now - startTimeRecord) / (fta.getRefreshSpace() * 60 * 1000));
								if (times > 1) {
									startTimeRecord = startTimeRecord + times * (fta.getRefreshSpace() * 60 * 1000);
									// logger.error("[间隔" + times + "次] " + sdf.format(new Date(startTimeRecord)));
								}
								if ((now / (60 * 1000) - startTimeRecord / (60 * 1000)) >= 0 && (now / (60 * 1000) - startTimeRecord / (60 * 1000)) <= 1) {
									// 开启
									fairylandTreasureEntity = new FairylandTreasureEntity(fta.getLastingTime());
									logger.error("[仙界宝箱刷新]");
									// TODO 发送活动开始提示?
									// // List<Player> pickFlowerBegin = pickFlowerBegin();
								}
							}
						}
					}

				} else {
					fairylandTreasureEntity.heartBeat();
					if (!fairylandTreasureEntity.isEffect()) {
						// 活动结束 删除npc
						// TODO 发活动结束提示吗?
						// caihuaEndNotice();
						fairylandTreasureEntity.fairylandTreasureEnd();
						fairylandTreasureEntity = null;
						// 发送滚屏信息(false);
						// {
						// ActivityNoticeManager.getInstance().activityEnd(Activity.采花大盗, GamePlayerManager.getInstance().getOnlinePlayers());
						// }
						logger.error("[仙界宝箱结束]");
					}
				}
			} catch (Throwable t) {
				logger.error("[fairylandTreasure心跳异常]", t);
			}

		}
	}

	// 根据刷新点创建宝箱实体
	public FairylandTreasure createFairylandTreasure(FlushPoint fp) {
		FairylandTreasure fairylandTreasure = new FairylandTreasure();
		fairylandTreasure.fairylandTreasureEntity = this.fairylandTreasureEntity;
		fairylandTreasure.point = fp;
		Game game = GameManager.getInstance().getGameByName(fp.gameMap, fp.country);
		if (game != null) {
			fairylandTreasure.game = game;
		} else {
			logger.error("[仙界宝箱] [刷新点地图未找到]");
			return null;
		}
		return fairylandTreasure;
	}

	/**
	 * 随机出类型
	 */
	public int getRandomType(int prayType, FairylandBox box) {
		int[] rates = new int[box.getRates().length];
		for (int j = 0; j < rates.length; j++) {
			rates[j] = box.getRates()[j];
		}
		// logger.error("[仙界宝箱] [祈福前概率:" + Arrays.toString(rates)+ "]");
		int[] prayRates = box.getPrayRates();
		if (prayType > 0) {
			// logger.error("[仙界宝箱] [祈福加成:" + Arrays.toString(prayRates)+ "]");
			// 玩家有祈福状态,对应类型的概率增加
			rates[prayType - 1] = rates[prayType - 1] + prayRates[prayType - 1];
			// logger.error("[仙界宝箱] [祈福后概率:" + Arrays.toString(rates)+ "]");
		}
		int allValue = 0;
		for (int i = 0; i < rates.length; i++) {
			allValue = allValue + rates[i];
		}
		int randomNum = random.nextInt(allValue) + 1;
		// logger.error("[仙界宝箱] [随机数值:" + randomNum + "]");
		int nowValue = 0;
		for (int i = 0; i < rates.length; i++) {
			if (nowValue < randomNum && randomNum <= nowValue + rates[i]) {

				// logger.error("[仙界宝箱] [随机结果:" + (i + 1) + "]");
				return i + 1;
			}
			nowValue = nowValue + rates[i];
		}
		logger.error("[仙界宝箱] [随机类型失败]");
		return 0;
	}

	/**
	 * 随机出最终物品
	 * @param afdList
	 * @param box
	 * @return
	 */
	public int getRandomArticle(List<ArticleForDraw> afdList, FairylandBox box) {
		int allValue = 0;
		for (int i = 0; i < afdList.size(); i++) {
			allValue = allValue + afdList.get(i).getRates()[box.getBoxType()];
		}
		int randomNum = random.nextInt(allValue) + 1;
		int nowValue = 0;
		for (int i = 0; i < afdList.size(); i++) {
			if (nowValue < randomNum && randomNum <= nowValue + afdList.get(i).getRates()[box.getBoxType()]) {
				return i;
			}
			nowValue = nowValue + afdList.get(i).getRates()[box.getBoxType()];
		}
		logger.error("[仙界宝箱] [随机物品失败] [宝箱名字:" + box.getBoxNameStat() + "]");
		return 0;
	}

	/**
	 * @param player
	 * @param boxType
	 *            宝箱类型:0-仙气宝箱,1-精致仙气宝箱,2-稀有仙气宝箱
	 */
	public void send_START_DRAW_RES(Player player, FairylandBox box) {
		long[] tempArticleIds = new long[CELLNUM];
		String[] tempArticleNames = new String[CELLNUM];// 打log用的
		int target = random.nextInt(CELLNUM);
		// 通过buff获得玩家祈福类型
		int prayType = player.getPrayType();
		if (logger.isWarnEnabled()) {
			logger.warn(player.getLogString() + "[宝箱名:" + box.getBoxNameStat() + "] [祈福状态:" + prayType + "] [祈福类型:" + prayTypeMap.get(prayType) + "]");
		}
		boolean putWorth = false;
		List<Integer> resultList = new ArrayList<Integer>();

		// 获取玩家最终得到的物品
		int randomType = getRandomType(prayType, box);
		if (randomType > 0) {
			List<ArticleForDraw> articleForDraws = drawMap.get(randomType);
			ArticleForDraw afd = articleForDraws.get(getRandomArticle(articleForDraws, box));
			WaitForSend wfs = new WaitForSend(player, afd);
			// waitList.add(wfs);
			tempArticleIds[target] = afd.getTempArticleId();
			tempArticleNames[target] = afd.getNameStat();
			resultList.add(afd.getId());

			WaitTimeSend wts = new WaitTimeSend(player, box.getBoxName(), afd, System.currentTimeMillis() + 自动发送等待时间);
			waitTimeSendList.add(wts);

			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[仙界宝箱] [目标宝箱类型:" + prayTypeMap.get(randomType) + "] [抽中物品:" + afd.getNameStat() + "] [下标:" + target + "]");
			}
			if (!afd.isWorth()) {
				putWorth = true;
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[仙界宝箱] [摆放目标] [随机类型为0]");
			}
		}

		// 摆放值钱物品
		if (putWorth) {

			int before = random.nextInt(2) + 1;
			if ((target - before) >= 0) {
				boolean find = true;
				while (find) {
					randomType = getRandomType(prayType, box);
					if (randomType > 0) {
						List<ArticleForDraw> articleForDraws = drawMap.get(randomType);
						ArticleForDraw afd = articleForDraws.get(getRandomArticle(articleForDraws, box));
						if (afd.isWorth()) {
							tempArticleIds[target - before] = afd.getTempArticleId();
							tempArticleNames[target - before] = afd.getNameStat();
							resultList.add(afd.getId());
							find = false;
							if (logger.isWarnEnabled()) {
								logger.warn(player.getLogString() + "[仙界宝箱] [摆放目标前" + before + "放值钱物品:" + afd.getNameStat() + "] [下标:" + (target - before) + "]");
							}
						}
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[仙界宝箱] [摆放目标前] [随机类型为0]");
						}
					}
				}
			}
			int after = random.nextInt(2) + 1;
			if ((target + after) < CELLNUM) {
				boolean find = true;
				while (find) {
					randomType = getRandomType(prayType, box);
					if (randomType > 0) {
						List<ArticleForDraw> articleForDraws = drawMap.get(randomType);
						ArticleForDraw afd = articleForDraws.get(getRandomArticle(articleForDraws, box));
						if (afd.isWorth()) {
							tempArticleIds[target + after] = afd.getTempArticleId();
							tempArticleNames[target + after] = afd.getNameStat();
							resultList.add(afd.getId());
							find = false;
							if (logger.isWarnEnabled()) {
								logger.warn(player.getLogString() + "[仙界宝箱] [摆放目标后" + after + "放值钱物品:" + afd.getNameStat() + "] [下标:" + (target + after) + "]");
							}
						}
					} else {
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[仙界宝箱] [摆放目标后] [随机类型为0]");
						}
					}
				}
			}
		}

		// 摆放必现物品
		int sureShow = random.nextInt(2) + 1;
		for (int i = 0; i < sureShow; i++) {
			boolean find = true;
			while (find) {
				List<ArticleForDraw> articleForDraws = null;
				if (prayType > 0) {
					articleForDraws = drawMap.get(prayType);
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[仙界宝箱] [无祈福,不摆放必现]");
					}
					find = false;
				}
				if (articleForDraws != null) {
					ArticleForDraw afd = articleForDraws.get(getRandomArticle(articleForDraws, box));
					int index = random.nextInt(CELLNUM);
					if (resultList.contains(afd.getId())) {
						find = false;
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[仙界宝箱] [摆放必现:" + afd.getNameStat() + "] [数组中已有,无需再找]");
						}
					} else if (tempArticleIds[index] <= 0 && !resultList.contains(afd.getId())) {
						tempArticleIds[index] = afd.getTempArticleId();
						tempArticleNames[index] = afd.getNameStat();
						resultList.add(afd.getId());
						find = false;
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[仙界宝箱] [摆放必现:" + afd.getNameStat() + "] [下标:" + index + "]");
						}
					}
				}
			}
		}

		// 摆放剩余物品
		for (int i = 0; i < CELLNUM; i++) {
			randomType = getRandomType(prayType, box);
			if (randomType > 0) {
				List<ArticleForDraw> articleForDraws = drawMap.get(randomType);
				ArticleForDraw afd = articleForDraws.get(getRandomArticle(articleForDraws, box));
				// int index = random.nextInt(CELLNUM);
				if (tempArticleIds[i] <= 0) {
					tempArticleIds[i] = afd.getTempArticleId();
					tempArticleNames[i] = afd.getNameStat();
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + "[仙界宝箱] [摆放剩余:" + afd.getNameStat() + "] [下标:" + i + "]");
					}
				}
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn(player.getLogString() + "[仙界宝箱] [摆放剩余] [随机类型为0]");
				}
			}
		}

		// 计数
		String key = getCurrentDateStr() + player.getId();
		if (disk.get(key) == null) {
			disk.put(key, 1);
		} else {
			int num = (Integer) disk.get(key);
			num++;
			disk.put(key, num);
		}
		logger.info(player.getLogString() + "[仙界宝箱] [展示物品] " + Arrays.toString(tempArticleNames));
		START_DRAW_RES res = new START_DRAW_RES(GameMessageFactory.nextSequnceNum(), box.getBoxName(), prayTypeMap.get(prayType), target, tempArticleIds);
		player.addMessageToRightBag(res);
	}

	// 如果抽到加经验,获取对应的经验值
	public long getAddExp(Player player, int color) {
		int level = player.getLevel();
		for (int i = 0; i < addExpList.size(); i++) {
			if (level <= addExpList.get(i).getLevel()) {
				return addExpList.get(i).getAddExp()[color];
			}
		}
		return 0;
	}

	/**
	 * 获得要删除或者要发送的抽奖结果
	 * @param player
	 * @param afd
	 * @return
	 */
	public WaitTimeSend getWaitTimeSend(Player player, ArticleForDraw afd) {
		for (WaitTimeSend wts : waitTimeSendList) {
			if (wts.getPlayer().getId() == player.getId()) {
				if (wts.getAfd().getId() == afd.getId()) {
					return wts;
				}
			}
		}
		return null;
	}

	/**
	 * 最终发送
	 */
	public void sendToPlayer(Player player, ArticleForDraw afd) {
		WaitTimeSend waittimesend = getWaitTimeSend(player, afd);
		if (waittimesend != null) {
			Article a = ArticleManager.getInstance().getArticleByCNname(afd.getNameStat());
			if (a != null) {
				int colorValue = ArticleManager.getColorValue(a, afd.getColor());
				String content = "";
				if (afd.getNameStat().equals("人物经验")) {
					long addExp = getAddExp(player, afd.getColor());
					player.addExp(addExp, ExperienceManager.仙界宝箱);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, addExp + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送物品成功:" + afd.getNameStat() + "] [增加经验:" + addExp + "]");
				} else if (afd.getNameStat().equals("工资")) {
					// 加工资
					player.setWage(player.getWage() + addGongZi[afd.getColor()]);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, BillingCenter.得到带单位的银两(addGongZi[afd.getColor()]) + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送物品成功:" + afd.getNameStat() + "] [增加工资:" + addGongZi[afd.getColor()] + "]");
				} else if (afd.getNameStat().equals("元气点")) {
					// 加元气
					player.setEnergy(player.getEnergy() + addYuanQi[afd.getColor()]);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, addYuanQi[afd.getColor()] + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送物品成功:" + afd.getNameStat() + "] [增加元气:" + addYuanQi[afd.getColor()] + "]");
				} else {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, afd.isBind(), ArticleEntityManager.仙界宝箱, player, afd.getColor(), afd.getNum(), true);
						player.putToKnapsacks(ae, "仙界宝箱");
						content = Translate.translateString(Translate.获得物品, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送物品成功:" + afd.getNameStat() + "]");

					} catch (Exception e) {
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送物品异常]" + e);
						e.printStackTrace();
					}
				}
				logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送提示:" + content + "]");
				player.send_HINT_REQ(content, (byte) 5);
				if (afd.isBroadcast()) {
					try {
						String msg = "";
						if (afd.getNameStat().equals("人物经验")) {
							if (afd.getColor() == 3) {
								msg = Translate.translateString(Translate.大量人物经验广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
							} else if (afd.getColor() == 4) {
								msg = Translate.translateString(Translate.海量人物经验广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
							}
						} else if (afd.getNameStat().equals("工资")) {
							msg = Translate.translateString(Translate.获得工资或元气点广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, BillingCenter.得到带单位的银两(addGongZi[afd.getColor()]) + "" }, { Translate.STRING_4, afd.getName() } });
						} else if (afd.getNameStat().equals("元气点")) {
							msg = Translate.translateString(Translate.获得工资或元气点广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, addYuanQi[afd.getColor()] + "" }, { Translate.STRING_4, afd.getName() } });
						} else {
							msg = Translate.translateString(Translate.获得物品广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
						}
						ChatMessageService.getInstance().sendMessageToSystem(msg);
						if (logger.isWarnEnabled()) {
							logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送世界公告成功]" + msg);
						}
					} catch (Exception e) {
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送世界公告异常]" + e);
						e.printStackTrace();
					}
				}
			}
			waitTimeSendList.remove(waittimesend);
		}
	}

	/**
	 * 延迟通过邮件发送
	 */
	public void sendToPlayerByMail(Player player, String boxName, ArticleForDraw afd) {
		WaitTimeSend waittimesend = getWaitTimeSend(player, afd);
		if (waittimesend != null) {
			Article a = ArticleManager.getInstance().getArticleByCNname(afd.getNameStat());
			if (a != null) {
				int colorValue = ArticleManager.getColorValue(a, a.getColorType());
				String content = "";
				if (afd.getNameStat().equals("人物经验")) {
					long addExp = getAddExp(player, afd.getColor());
					player.addExp(addExp, ExperienceManager.仙界宝箱);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, addExp + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [延迟发送物品成功:" + afd.getNameStat() + "] [增加经验:" + addExp + "]");
				} else if (afd.getNameStat().equals("工资")) {
					// 加工资
					player.setWage(player.getEnergy() + addGongZi[afd.getColor()]);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, BillingCenter.得到带单位的银两(addGongZi[afd.getColor()]) + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [延迟发送物品成功:" + afd.getNameStat() + "] [增加工资:" + addGongZi[afd.getColor()] + "]");
				} else if (afd.getNameStat().equals("元气点")) {
					// 加元气
					player.setEnergy(player.getEnergy() + addYuanQi[afd.getColor()]);
					content = Translate.translateString(Translate.获得经验工资或元气点, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, addYuanQi[afd.getColor()] + "" }, { Translate.STRING_4, afd.getName() } });
					logger.warn("[" + player.getLogString() + "] [仙界宝箱] [延迟发送物品成功:" + afd.getNameStat() + "] [增加元气:" + addYuanQi[afd.getColor()] + "]");

				} else {
					try {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, afd.isBind(), ArticleEntityManager.仙界宝箱, player, afd.getColor(), afd.getNum(), true);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { afd.getNum() }, Translate.延迟邮件标题, Translate.translateString(Translate.延迟邮件内容, new String[][] { { Translate.STRING_1, boxName } }), 0, 0, 0, "仙界宝箱");
						content = Translate.translateString(Translate.获得物品, new String[][] { { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [延迟发送物品成功:" + afd.getNameStat() + "]");
					} catch (Exception e) {
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [延迟发送物品异常]" + e);
						e.printStackTrace();
					}
				}
				logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送提示:" + content + "]");
				player.send_HINT_REQ(content, (byte) 5);
				if (afd.isBroadcast()) {
					try {
						String msg = Translate.translateString(Translate.获得物品广播, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, afd.getName() } });
						ChatMessageService.getInstance().sendMessageToSystem(msg);
						if (logger.isWarnEnabled()) {
							logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送世界公告成功]" + msg);
						}
					} catch (Exception e) {
						logger.warn("[" + player.getLogString() + "] [仙界宝箱] [发送世界公告异常]" + e);
						e.printStackTrace();
					}
				}
			}
			waitTimeSendList.remove(waittimesend);
		}
	}

	public static void fireBuff(Player player, String buffName, int buffLevel, long time) {
		try {
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			Buff buff = bt.createBuff(buffLevel);
			buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + time);
			buff.setCauser(player);
			player.placeBuff(buff);
		} catch (Exception e) {
			logger.error("[fireBuff] [异常] [" + player.getLogString() + "] [" + buffName + "] [" + buffLevel + "]", e);
		}
	}

	public List<FlushPoint> getPointList() {
		return pointList;
	}

	public void setPointList(List<FlushPoint> pointList) {
		this.pointList = pointList;
	}

	public List<FairylandBox> getFairylandBoxList() {
		return fairylandBoxList;
	}

	public void setFairylandBoxList(List<FairylandBox> fairylandBoxList) {
		this.fairylandBoxList = fairylandBoxList;
	}

	public Map<Integer, List<ArticleForDraw>> getDrawMap() {
		return drawMap;
	}

	public void setDrawMap(Map<Integer, List<ArticleForDraw>> drawMap) {
		this.drawMap = drawMap;
	}

	public Map<Integer, List<ArticleForDraw>> getDrawSureShowMap() {
		return drawSureShowMap;
	}

	public void setDrawSureShowMap(Map<Integer, List<ArticleForDraw>> drawSureShowMap) {
		this.drawSureShowMap = drawSureShowMap;
	}

	public int getMaxOpenNum() {
		return maxOpenNum;
	}

	public void setMaxOpenNum(int maxOpenNum) {
		this.maxOpenNum = maxOpenNum;
	}

	public List<FairylandTreasureActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<FairylandTreasureActivity> activityList) {
		this.activityList = activityList;
	}

	public List<AddExpBean> getAddExpList() {
		return addExpList;
	}

	public void setAddExpList(List<AddExpBean> addExpList) {
		this.addExpList = addExpList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public FairylandTreasureEntity getFairylandTreasureEntity() {
		return fairylandTreasureEntity;
	}

	public void setFairylandTreasureEntity(FairylandTreasureEntity fairylandTreasureEntity) {
		this.fairylandTreasureEntity = fairylandTreasureEntity;
	}

	public static boolean isOpen() {
		return open;
	}

	public static void setOpen(boolean open) {
		FairylandTreasureManager.open = open;
	}

	public DiskCache getDisk() {
		return disk;
	}

	public void setDisk(DiskCache disk) {
		this.disk = disk;
	}

	public String getDiskFile() {
		return diskFile;
	}

	public void setDiskFile(String diskFile) {
		this.diskFile = diskFile;
	}

	public long getPrayCost() {
		return prayCost;
	}

	public void setPrayCost(long prayCost) {
		this.prayCost = prayCost;
	}

	public long getPrayLastingTime() {
		return prayLastingTime;
	}

	public void setPrayLastingTime(long prayLastingTime) {
		this.prayLastingTime = prayLastingTime;
	}

	public long getStartTimeRecord() {
		return startTimeRecord;
	}

	public void setStartTimeRecord(long startTimeRecord) {
		this.startTimeRecord = startTimeRecord;
	}

}
