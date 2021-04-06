package com.fy.engineserver.activity.peekandbrick;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.CheckAttribute;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

/**
 * 刺探和偷砖 管理器<BR/>
 * 数据如此相像，可以考虑做成2个对象<BR/>
 * 
 */
@CheckAttribute("刺探和偷砖管理器")
public class PeekAndBrickManager {

	public static Logger logger = LoggerFactory.getLogger(PeekAndBrickManager.class);

	private static PeekAndBrickManager instance;

	private CountryManager countryManager;

	private String filePath;

	/** 刺探任务配置<国家,任务列表> */
	@CheckAttribute(value = "刺探任务配置", des = "<国家,任务列表>")
	private HashMap<Byte, String[]> countryPeekTasks = new HashMap<Byte, String[]>();
	/** 国家被刺探的任务 */
	@CheckAttribute(value = "国家被刺探的任务")
	private String[][] countryBepeektask = new String[3][];

	/** 偷砖任务配置<国家,任务列表> */
	@CheckAttribute(value = "偷砖任务配置", des = "<国家,任务列表>")
	private HashMap<Byte, String[]> countryBrickTasks = new HashMap<Byte, String[]>();
	/** 国家被偷砖的任务 */
	@CheckAttribute(value = "国家被偷砖的任务")
	private String[][] countryBebricktask = new String[3][];

	/** 刺探buff名 */
	@CheckAttribute(value = "刺探buff名")
	private String[] peekBuffName;
	/** 偷砖buff名 */
	@CheckAttribute(value = "偷砖buff名")
	private String[] brickBuffName;
	/** 刺探buff个颜色的比例 */
	@CheckAttribute(value = "刺探buff个颜色的比例")
	private double[] peekBuffRate;
	/** 偷砖buff个颜色的比例 */
	@CheckAttribute(value = "偷砖buff个颜色的比例")
	private double[] brickBuffRate;
	/** 刺探读条时间 */
	@CheckAttribute(value = "刺探读条时间 ")
	private long peekTimeBarDelay;
	/** 偷砖读条时间 */
	@CheckAttribute(value = "偷砖读条时间")
	private long brickTimeBarDelay;
	/** 普通刺探的CD */
	@CheckAttribute(value = "普通刺探的CD ")
	private long simplePeekCD;
	/** 国刺的CD */
	@CheckAttribute(value = "国刺的CD")
	private long countryPeekCD;
	/** 刺探NPC名字 */
	@CheckAttribute(value = "刺探NPC名字")
	private String peekTaskNpcName;
	/** 刺探NPC所在地图 */
	@CheckAttribute(value = "刺探NPC所在地图")
	private String peekTaskGameName;
	/** 偷砖NPC名字 */
	@CheckAttribute(value = "偷砖NPC名字")
	private String brickTaskNpcName;
	/** 偷砖NPC所在地图 */
	@CheckAttribute(value = "偷砖NPC所在地图")
	private String brickTaskGameName;
	/** 各颜色经验倍数 基于任务经验 */
	@CheckAttribute(value = "各颜色经验倍数 基于任务经验")
	private Double[] expRate;

	private PeekAndBrickManager() {

	}

	public static PeekAndBrickManager getInstance() {
		return instance;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public HashMap<Byte, String[]> getCountryPeekTasks() {
		return countryPeekTasks;
	}

	public HashMap<Byte, String[]> getCountryBrickTasks() {
		return countryBrickTasks;
	}

	public String[] getPeekBuffName() {
		return peekBuffName;
	}

	public String[] getBrickBuffName() {
		return brickBuffName;
	}

	public double[] getPeekBuffRate() {
		return peekBuffRate;
	}

	public double[] getBrickBuffRate() {
		return brickBuffRate;
	}

	public long getPeekTimeBarDelay() {
		return peekTimeBarDelay;
	}

	public long getBrickTimeBarDelay() {
		return brickTimeBarDelay;
	}

	public long getSimplePeekCD() {
		return simplePeekCD;
	}

	public long getCountryPeekCD() {
		return countryPeekCD;
	}

	private void load() throws Exception {
		long start = SystemTime.currentTimeMillis();
		File file = new File(getFilePath());
		if (!file.exists()) {
			logger.error("[刺探和偷砖活动][加载异常][配置文件不存在]");
			throw new Exception();
		}
		try {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			HSSFSheet sheet = workbook.getSheetAt(0);

			int maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				Byte country = StringTool.getCellValue(row.getCell(0), Byte.class);
				String[] peekTasks = StringTool.string2Array((row.getCell(1).getStringCellValue()), ",", String.class);
				String[] brickTasks = StringTool.string2Array((row.getCell(2).getStringCellValue()), ",", String.class);
				countryBepeektask[i - 1] = StringTool.string2Array((row.getCell(3).getStringCellValue()), ",", String.class);
				countryBebricktask[i - 1] = StringTool.string2Array((row.getCell(4).getStringCellValue()), ",", String.class);
				countryPeekTasks.put(country, peekTasks);
				countryBrickTasks.put(country, brickTasks);
			}
			sheet = workbook.getSheetAt(1);
			int index = 0;
			HSSFRow row = sheet.getRow(1);
			peekBuffName = StringTool.string2Array((row.getCell(index++).getStringCellValue()), ",", String.class);
			peekBuffRate = Double2doubleArray(StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Double.class));
			brickBuffName = StringTool.string2Array((row.getCell(index++).getStringCellValue()), ",", String.class);
			brickBuffRate = Double2doubleArray(StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Double.class));
			peekTimeBarDelay = StringTool.getCellValue(row.getCell(index++), Integer.class) * TimeTool.TimeDistance.SECOND.getTimeMillis();
			brickTimeBarDelay = StringTool.getCellValue(row.getCell(index++), Integer.class) * TimeTool.TimeDistance.SECOND.getTimeMillis();
			simplePeekCD = StringTool.getCellValue(row.getCell(index++), Integer.class) * TimeTool.TimeDistance.SECOND.getTimeMillis();
			countryPeekCD = StringTool.getCellValue(row.getCell(index++), Integer.class) * TimeTool.TimeDistance.SECOND.getTimeMillis();
			peekTaskGameName = row.getCell(index++).getStringCellValue();
			peekTaskNpcName = row.getCell(index++).getStringCellValue();
			brickTaskGameName = row.getCell(index++).getStringCellValue();
			brickTaskNpcName = row.getCell(index++).getStringCellValue();
			expRate = StringTool.string2Array(row.getCell(index++).getStringCellValue(), ",", Double.class);
//			System.out.println("----------------------[刺探和偷砖加载完毕]----------------------[耗时" + (SystemTime.currentTimeMillis() - start) + "ms]");
		} catch (Exception e) {
			logger.error("[记载文件异常]", e);
			throw e;
		}
	}

	public String getPeekTaskNpcName() {
		return peekTaskNpcName;
	}

	public String getPeekTaskGameName() {
		return peekTaskGameName;
	}

	public String getBrickTaskNpcName() {
		return brickTaskNpcName;
	}

	public String getBrickTaskGameName() {
		return brickTaskGameName;
	}

	public Double[] getExpRate() {
		return expRate;
	}

	private double[] Double2doubleArray(Double[] values) {
		double[] res = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			res[i] = values[i];
		}
		return res;
	}

	public String[][] getCountryBepeektask() {
		return countryBepeektask;
	}

	public String[][] getCountryBebricktask() {
		return countryBebricktask;
	}

	public CountryManager getCountryManager() {
		return countryManager;
	}

	public void setCountryManager(CountryManager countryManager) {
		this.countryManager = countryManager;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.load();
		instance.releaseTasks();
		ServiceStartRecord.startLog(this);
	}

	private void releaseTasks() {
		try {
			countryManager.getData().reReleaseBricktask();
			countryManager.getData().reReleasePeektask();
		} catch (Exception e) {
			logger.error("[系统启动发布任务异常]", e);
		}
	}
}
