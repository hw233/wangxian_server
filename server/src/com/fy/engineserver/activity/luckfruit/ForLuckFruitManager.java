package com.fy.engineserver.activity.luckfruit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.homestead.cave.resource.Point;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;

/**
 * 祝福果树活动mamager
 * 
 */
public class ForLuckFruitManager {

	public static Logger logger = LoggerFactory.getLogger(ForLuckFruitManager.class);

	/** 最大丹炉的数量 */
	public static final int maxTreeNum = 5;

	private String filePath;
	private static ForLuckFruitManager instance;

	public static ForLuckFruitManager getInstance() {
		return instance;
	}

	/** 果树成熟时间 */
	private long growupTime;
	/** 果树消失时间 */
	private long deadTime;
	/** 产出果子个数 */
	private int maxFruitNum;
	/** 产出果子名称 */
	private String fruitName;
	/** 树最大数量 */
	private Point[] treePoints = new Point[maxTreeNum];
	/** 各种品质树的加钱 */
	private long[] plantCost = new long[ArticleManager.color_article_Strings.length];
	/** 各种品质果子兑换的经验值 */
	private long[] fruitExp = new long[ArticleManager.color_article_Strings.length];
	/** 丹炉的NPCID */
	private int treeNpcId;
	/** 各种颜色的树产出的物品颜色概率 */
	private double[][] treeOutputArticleColor = new double[5][ArticleManager.color_article_Strings.length];
	/** 每天兑换上限 */
	private int dailyMaxExchangeNum;

	public ForLuckFruitManager() {

	}

	public long getGrowupTime() {
		return growupTime;
	}

	public void setGrowupTime(long growupTime) {
		this.growupTime = growupTime;
	}

	public long getDeadTime() {
		return deadTime;
	}

	public void setDeadTime(long deadTime) {
		this.deadTime = deadTime;
	}

	public Point[] getTreePoints() {
		return treePoints;
	}

	public void setTreePoints(Point[] treePoints) {
		this.treePoints = treePoints;
	}

	public int getTreeNpcId() {
		return treeNpcId;
	}

	public void setTreeNpcId(int treeNpcId) {
		this.treeNpcId = treeNpcId;
	}

	public static int getMaxtreenum() {
		return maxTreeNum;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getMaxFruitNum() {
		return maxFruitNum;
	}

	public void setMaxFruitNum(int maxFruitNum) {
		this.maxFruitNum = maxFruitNum;
	}

	public String getFruitName() {
		return fruitName;
	}

	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}

	public long[] getPlantCost() {
		return plantCost;
	}

	public long[] getFruitExp() {
		return fruitExp;
	}

	public double[][] getTreeOutputArticleColor() {
		return treeOutputArticleColor;
	}

	public int getDailyMaxExchangeNum() {
		return dailyMaxExchangeNum;
	}

	public void load() throws Exception {
		try {
			File file = new File(getFilePath());

			if (!file.exists()) {
				logger.error("[加载祝福果树失败][配置文件不存在]");
				System.out.println("[ERROR][加载祝福果树失败][配置文件不存在]");
				return;
			}
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);
			/************ 丹炉的位置配置 ************/
			HSSFSheet sheet = workbook.getSheetAt(0);
			int maxRow = sheet.getPhysicalNumberOfRows();
			List<Point> points = new ArrayList<Point>();
			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				int x = (int) row.getCell(0).getNumericCellValue();
				int y = (int) row.getCell(1).getNumericCellValue();
				Point point = new Point(x, y);
				points.add(point);
			}
			treePoints = points.toArray(new Point[0]);

			/************ 仙丹兑换经验配置 ************/
			sheet = workbook.getSheetAt(1);
			maxRow = sheet.getPhysicalNumberOfRows();

			for (int i = 1; i < maxRow; i++) {
				HSSFRow row = sheet.getRow(i);
				plantCost[i - 1] = (long) row.getCell(1).getNumericCellValue();
				fruitExp[i - 1] = (long) row.getCell(2).getNumericCellValue();
				;
			}
			/************ 杂项配置 ************/
			sheet = workbook.getSheetAt(2);
			HSSFRow row = sheet.getRow(1);
			int index = 0;
			growupTime = (long) row.getCell(index++).getNumericCellValue() * TimeTool.TimeDistance.MINUTE.getTimeMillis();
			deadTime = (long) row.getCell(index++).getNumericCellValue() * TimeTool.TimeDistance.MINUTE.getTimeMillis();

			treeOutputArticleColor[0] = Double2double(StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", Double.class));
			treeOutputArticleColor[1] = Double2double(StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", Double.class));
			treeOutputArticleColor[2] = Double2double(StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", Double.class));
			treeOutputArticleColor[3] = Double2double(StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", Double.class));
			treeOutputArticleColor[4] = Double2double(StringTool.string2Array(row.getCell(index++).getStringCellValue().trim(), ",", Double.class));

			dailyMaxExchangeNum = (int) row.getCell(index++).getNumericCellValue();

			treeNpcId = (int) row.getCell(index++).getNumericCellValue();

			maxFruitNum = (int) row.getCell(index++).getNumericCellValue();

			fruitName = row.getCell(index++).getStringCellValue();
		} catch (Exception e) {
			logger.error("[加载异常]", e);
			throw e;
		}
	}

	private double[] Double2double(Double[] values) {
		double[] array = new double[values.length];

		for (int i = 0; i < values.length; i++) {
			array[i] = values[i].doubleValue();
		}
		return array;

	}

	public void init() throws Exception {
		
		instance = this;
		instance.load();
		ServiceStartRecord.startLog(this);
	}
}
