package com.fy.engineserver.activity.farming;



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

import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

/**
 * 神农活动管理器
 * 
 */
public class FarmingManager {

	/** 圆盘转的时间 */
	public static long runTime = TimeTool.TimeDistance.SECOND.getTimeMillis() * 3;
	public static Logger logger = LoggerFactory.getLogger(TaskSubSystem.class);

	private static FarmingManager instance;
	public static int maxFruitNum = 3;

	private String taskCollectionName;

	/** 各种果子 */
	private HashMap<Integer, FarmingFruit> fruits = new HashMap<Integer, FarmingFruit>();
	/** 需要弹出随机盘子的任务(任务完成) */
	private String[] needSendRadomplateTasks;
	/** 果实出现方案 */
	private HashMap<Integer, PlateProject> projects = new HashMap<Integer, PlateProject>();

	private String filePath;

	private FarmingManager() {

	}

	private void loadFile() throws Exception {
		File file = new File(getFilePath());
		if (!file.exists()) {
			logger.error("[加载神农活动][异常][配置文件不存在 ]");
			throw new Exception();
		}
		try {
			
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			/** 一般项配置 */
			HSSFSheet sheet = workbook.getSheetAt(0);
			needSendRadomplateTasks = StringTool.string2Array(sheet.getRow(1).getCell(0).getStringCellValue(), ",", String.class);
			taskCollectionName = sheet.getRow(1).getCell(1).getStringCellValue().trim();

			/** 水果配置 */

			sheet = workbook.getSheetAt(1);
			int maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int id = (int)row.getCell(0).getNumericCellValue();
				String name = row.getCell(1).getStringCellValue();
				String[] taskName = StringTool.string2Array(row.getCell(2).getStringCellValue().trim(), ",", String.class);
				FarmingFruit farmingFruit = new FarmingFruit(id, name, taskName);
				fruits.put(farmingFruit.getId(), farmingFruit);
			}
			/** 盘子方案 */

			sheet = workbook.getSheetAt(2);
			maxRow = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int id = (int)row.getCell(0).getNumericCellValue();
				Integer[] _fruitIds = StringTool.string2Array(row.getCell(1).getStringCellValue(), ",", Integer.class);
				Double[] _fruitRate = StringTool.string2Array(row.getCell(2).getStringCellValue(), ",", Double.class);
				int[] fruitIds = new int[_fruitIds.length];
				double[] fruitRate = new double[_fruitRate.length];
				for (int j = 0; j < fruitIds.length; j++) {
					fruitIds[j] = _fruitIds[j];
					fruitRate[j] = _fruitRate[j];
				}
				PlateProject plateProject = new PlateProject(id, fruitIds, fruitRate);
				plateProject.initNames();
				projects.put(plateProject.getId(), plateProject);
			}

		} catch (Exception e) {
			logger.error("[加载神农活动][异常][配置文件解析错误]", e);
			throw e;
		}
	}

	public static FarmingManager getInstance() {
		return instance;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public HashMap<Integer, FarmingFruit> getFruits() {
		return fruits;
	}

	public String[] getNeedSendRadomplateTasks() {
		return needSendRadomplateTasks;
	}

	public HashMap<Integer, PlateProject> getProjects() {
		return projects;
	}

	public String getTaskCollectionName() {
		return taskCollectionName;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.loadFile();
		ServiceStartRecord.startLog(this);
	}
}
