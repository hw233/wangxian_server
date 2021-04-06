package com.fy.engineserver.datasource.article.concrete;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.SuitEquipment;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.ServiceStartRecord;

public class ReadEquipmentExcelManager {

	private static ReadEquipmentExcelManager self;

	public static ReadEquipmentExcelManager getInstance() {
		return self;
	}

	/**
	 * 装备数据的文件
	 */
	protected File configFile;

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public void init() throws Exception {
		
		if (configFile != null && configFile.isFile()) {
			loadEquipmentDataExcel(configFile);
		}
		self = this;
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 武器,头,肩,胸,护腕,腰带,鞋
	 * 可以镶嵌宝石的装备
	 * 数组下标为装备的类型
	 * 在生成装备时需要按照装备的颜色产生孔数，此数组为装备产生的最大孔数
	 */
	public int[] equipmentInlayMaxNumber = new int[] { 5, 3, 3, 4, 3, 3, 3 };

	/**
	 * 三维数组
	 * 第一维为职业 0为无门无派 1修罗 2影魅 3仙心 4九黎，5兽魁，蓬莱岛
	 * 第二维为装备类型 武器,头,肩,胸,护腕,腰带,鞋,戒指,项链,饰品,面甲,颈甲,体铠,鞍铠,蹄甲
	 * 第三维为该装备类型下允许各个颜色允许镶嵌的数量，第三维的下标为宝石颜色编号"绿","橙","黑","蓝","红","白","黄","紫"
	 */
	public int[][][] equipmentInlayColorLimit = new int[][][] { { { 0, 2, 0, 2, 1, 0, 0, 0 }, { 1, 0, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 1 }, { 2, 0, 0, 0, 1, 0, 0, 1 }, { 0, 1, 0, 0, 0, 1, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 0, 0, 1, 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },
			//修罗
			{ { 0, 3, 0, 1, 1, 0, 0, 0 }, { 1, 0, 0, 0, 0, 1, 1, 0 }, { 0, 1, 1, 0, 1, 0, 0, 0 }, { 2, 0, 1, 0, 1, 0, 0, 0 }, { 0, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 0, 1, 1, 0, 0, 1 }, { 0, 1, 0, 0, 0, 0, 1, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } }, 
			//影魅
			{ { 0, 2, 0, 2, 0, 0, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 0, 0, 1, 1, 0, 0, 1, 0 }, { 1, 0, 2, 0, 0, 0, 1, 0 }, { 1, 1, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 1, 0, 1, 1, 0 }, { 0, 0, 0, 1, 1, 1, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } }, 
			//仙心
			{ { 0, 3, 0, 1, 0, 0, 0, 1 }, { 1, 0, 0, 0, 1, 1, 0, 0 }, { 0, 1, 0, 1, 0, 0, 0, 1 }, { 1, 0, 2, 0, 0, 0, 0, 1 }, { 0, 1, 0, 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1, 1 }, { 0, 1, 0, 0, 0, 1, 1, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } }, 
			//九黎
			{ { 0, 2, 0, 2, 1, 0, 0, 0 }, { 1, 0, 1, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 1, 0, 1, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 1, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } }, 
			//兽魁
			{ { 0, 2, 0, 1, 1, 0, 1, 0 }, { 1, 0, 0, 0, 0, 0, 1, 1 }, { 0, 1, 1, 0, 0, 0, 1, 0 }, { 1, 0, 1, 1, 0, 0, 0, 1 }, { 0, 1, 1, 0, 0, 1, 0, 0 }, { 0, 0, 1, 0, 0, 0, 1, 1 }, { 1, 0, 0, 1, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } }, 
			{ { 0, 2, 0, 2, 0, 0, 1, 0 }, { 1, 0, 1, 0, 0, 0, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 1, 0, 1, 0, 0, 1, 1, 0 }, { 0, 1, 0, 0, 0, 1, 0, 1 }, { 1, 0, 0, 0, 0, 1, 1, 0 }, { 1, 0, 0, 0, 1, 0, 0, 1 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } } };

	/**
	 * 二维数组
	 * 第一维为装备类型 武器 头 肩 胸 护腕 腰带 鞋 戒指 项链 饰品 面甲 颈甲 体铠 鞍铠 蹄甲
	 * 第二维为该装备颜色对应的镶嵌数量 白装,绿装,蓝装,紫装,完美紫装,橙装,完美橙装
	 */
	public int[][] equipmentColorLimitInlayNumber = new int[][] { { 0, 2, 3, 3, 4, 4, 5 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 1, 2, 2, 3, 3, 4 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 1, 2, 2, 3, 3, 4 }, { 0, 0, 1, 1, 2, 2, 3 }, { 0, 0, 1, 1, 2, 2, 3 } };

	/**
	 * 二维数组
	 * 第一维为装备颜色 白装,绿装,蓝装,紫装,完美紫装,橙装,完美橙装
	 * 第二维为该颜色装备应该具有的属性数，0表示属性数为0
	 */
	public int[][] equipmentColorAdditionPropertyNumbers = new int[][] { { 0 }, { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10, 11 }, { 12, 13, 14, 15 } };
	
	/**
	 * 二维数组
	 * 第一维为装备颜色 白装,绿装,蓝装,紫装,完美紫装,橙装,完美橙装
	 * 第二维为该颜色装备应该具有的属性数对应的概率，加和为概率基数
	 */
	public int[][] equipmentColorAdditionPropertyNumbersProp = new int[][] { { 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 }, { 30, 60, 10 }, { 15, 50, 30, 5 } };

	public static String[] es = new String[] { "装备部位权重", "通用职业装备取值", "修罗装备取值", "影魅装备取值", "仙心装备取值", "九黎装备取值", "万毒谷装备取值", "蓬莱岛装备取值", "计算方式", "职业装备部位宝石孔数量" };

	/**
	 * 各个职业对应Excel的sheet的下标
	 * 数组下标表示职业，对应的值为Excel的sheet下标
	 */
	public static int[] careerSheet = new int[] { 2, 3, 4, 5, 6, 7 };

	/**
	 * 三维数组
	 * 第一维 为职业，分别为"无门无派装备取值","修罗装备取值","影魅装备取值","仙心装备取值","九黎装备取值","万毒谷装备取值"
	 * 第二维 为级别
	 * 第三维 为各个属性的数值
	 */
	public int[][][] careerExcelDatas = new int[0][][];

	/**
	 * 宝石的各个属性数值
	 * 二维数组
	 * 第一维为宝石级别分别代表1-9级
	 * 第二维为各个属性值
	 * 顺序为下面所示
	 * MHP血量 AP-外功 AP-内法 AC-外防 AC-内防 DGP闪躲 DCHP会心防御 HITP命中 CHP会心一击 ACTP精准 DAC破甲 FAP火属性攻击 FRT火抗性 DFRT火减抗 IAP冰属性攻击 IRT冰抗性 DIRT冰减抗 WAP风属性攻击 WRT风抗性 DWRT风减抗 TAP雷属性攻击 TRT雷抗性 DTRT雷减抗
	 */
	public int[][] inlayArticleExcelDatas = new int[0][];

	/**
	 * 因为通用职业、修罗、影魅、装备属性权重一样，因此放在一起
	 * 三维数组第一维代表职业分类
	 * 0代表通用职业、修罗、影魅、灵隐阁
	 * 1代表仙心、九黎、蓬莱岛
	 * 第二维表示部件位置 顺序为 武器，头，肩，胸，护腕，腰带，鞋，戒指，项链，饰品
	 */
	public double[][][] equipmentQuanZhong = new double[0][][];

	/**
	 * 星级权重
	 */
	public static final double[] starQuanZhong = new double[] { 0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.40, 0.45, 0.5, 0.61, 0.72, 0.85, 0.98, 1.13, 1.28, 1.45, 1.62, 1.81, 2 ,2.1,2.2,2.3,2.4,2.5,2.6,2.7,2.8,2.9,3,3.1};

	/**
	 * 装备强化提升比例
	 */
	public static final double[] strongValues = new double[] { 0, 0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.40, 0.45, 0.5, 0.61, 0.72, 0.85, 0.98, 1.13, 1.28, 1.45, 1.62, 1.81, 2,2.45,2.69,2.94,3.2,3.47,3.76,4.29,4.9,5.61,6.43,7.39 };

	/**
	 * 装备颜色提升比例
	 */
	public static final double[] colorValues = new double[] { 0.1, 0.15, 0.25, 0.4, 0.6, 0.8, 1 };

	/**
	 * 装备铭刻提升比例
	 */
	public static final double inscriptionValue = 0.1;

	/**
	 * 装备资质提升比例
	 */
	public static final double[] endowmentsValues = new double[] { 0, 0.04, 0.08, 0.12, 0.16, 0.20 };

	public static double[] endowmentsNewValues = new double[] { 0.20, 0.24, 0.32, 0.44, 0.60 };
	public static int[] endowmentsNewValues_int = new int[] { 20, 24, 32, 44, 60 };
	public static String[] endowmentsNewNames = new String[] { Translate.传奇, Translate.传说, Translate.神话, Translate.永恒 };
	public static String[] endowmentsNewPropNames = new String[] { Translate.传奇鉴定符, Translate.传说鉴定符, Translate.神话鉴定符, Translate.永恒鉴定符 };

	/**
	 * 读取Excel的装备数值开始坐标数值，数值从零开始
	 */
	public int equipmentValuesInExcelStartX = 1;// 代表从第二列开始
	public int equipmentValuesInExcelStartY = 1;// 代表从第二行开始

	/**
	 * 读取Excel的装备数值开始坐标数值，数值从零开始
	 */
	public int equipmentQuanZhongValueExcelStartX = 1;// 代表从第二列开始
	/**
	 * 代表通用职业、修罗、影魅、灵隐阁装备属性权重的开始行数
	 */
	public int equipmentTongyongFangQuanZhongValueExcelStartY = 5;// 代表从第六行开始
	/**
	 * 代表仙心、九黎、蓬莱岛装备属性权重的开始行数
	 */
	public int equipmentKunlunFangQuanZhongValueExcelStartY = 25;// 代表从第二十一行开始

	/**
	 * 表示一共读取多少行
	 */
	public int equipmentTongyongFangQuanZhongValueExcelRowLength = 15;// 表示一共读取15行

	/**
	 * 代表镶嵌宝石的从excel中取值的开始列数，数值从零开始
	 */
	public int inlayArticle_in_Excel_start_Cindex = 1;// 代表从第二列开始

	/**
	 * 代表镶嵌宝石的从excel中取值的开始行数，数值从零开始
	 */
	public int inlayArticle_in_Excel_start_Rindex = 1;// 代表从第二行开始

	/**
	 * 代表镶嵌宝石的到excel中取值的结束行数
	 */
	public int inlayArticle_in_Excel_end_Rindex = 9;// 代表到第十行结束

	public String[] propertys = new String[] { "MHP", "AP", "AP2", "AC", "AC2", "MMP", "DAC", "HITP", "DGP", "ACTP", "CHP", "DCHP", "FAP", "IAP", "WAP", "TAP", "FRT", "IRT", "WRT", "TRT", "DFRT", "DIRT", "DWRT", "DTRT" };

	public void loadEquipmentDataExcel(File file) throws Exception {
		if (file != null && file.isFile() && file.exists()) {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			double[][][] equipmentQuanZhong = new double[2][][];
			int[][][] careerExcelDataTemps = new int[8][][];
			int[][] inlayArticleExcelDataTemps = null;
			for (int i = 0; i < 10; i++) {
				HSSFSheet sheet = workbook.getSheetAt(i);
				int rows = sheet.getPhysicalNumberOfRows();
				// i==0表示读取的是装备部位权重sheet，剩余的7个sheet为各职业装备对应属性数值表
				if (i == 0) {
					if (rows <= equipmentKunlunFangQuanZhongValueExcelStartY) {
						throw new Exception("第" + i + "个sheet格式不正确，行数不大于开始行数");
					}
					// 通用职业、修罗、影魅、灵隐阁装备属性权重
					double[][] quanzhongData = new double[equipmentTongyongFangQuanZhongValueExcelRowLength][];
					for (int r = equipmentTongyongFangQuanZhongValueExcelStartY; r < equipmentTongyongFangQuanZhongValueExcelStartY + equipmentTongyongFangQuanZhongValueExcelRowLength; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int cellNumber = row.getPhysicalNumberOfCells();
							if (cellNumber <= equipmentQuanZhongValueExcelStartX) {
								throw new Exception("第" + i + "个sheet格式不正确，列数不大于开始列数" + cellNumber + " " + equipmentQuanZhongValueExcelStartX);
							}
							double[] cellValues = new double[cellNumber - equipmentQuanZhongValueExcelStartX];
							for (int j = equipmentQuanZhongValueExcelStartX; j < cellNumber; j++) {
								HSSFCell cell = row.getCell(j);
								cellValues[j - equipmentQuanZhongValueExcelStartX] = cell.getNumericCellValue();
							}
							quanzhongData[r - equipmentTongyongFangQuanZhongValueExcelStartY] = cellValues;
						}
					}
					equipmentQuanZhong[0] = quanzhongData;

					// 仙心、九黎、蓬莱岛装备属性权重
					quanzhongData = new double[equipmentTongyongFangQuanZhongValueExcelRowLength][];
					for (int r = equipmentKunlunFangQuanZhongValueExcelStartY; r < equipmentKunlunFangQuanZhongValueExcelStartY + equipmentTongyongFangQuanZhongValueExcelRowLength; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int cellNumber = row.getPhysicalNumberOfCells();
							if (cellNumber <= equipmentQuanZhongValueExcelStartX) {
								throw new Exception("第" + i + "个sheet格式不正确，列数不大于开始列数" + cellNumber + " " + equipmentQuanZhongValueExcelStartX);
							}
							double[] cellValues = new double[cellNumber - equipmentQuanZhongValueExcelStartX];
							for (int j = equipmentQuanZhongValueExcelStartX; j < cellNumber; j++) {
								HSSFCell cell = row.getCell(j);
								cellValues[j - equipmentQuanZhongValueExcelStartX] = cell.getNumericCellValue();
							}
							quanzhongData[r - equipmentKunlunFangQuanZhongValueExcelStartY] = cellValues;
						}
					}
					equipmentQuanZhong[1] = quanzhongData;
				} else if (i == 9) {// 宝石
					if (rows <= inlayArticle_in_Excel_end_Rindex) {
						throw new Exception("第" + i + "个sheet格式不正确，行数不大于inlayArticle_in_Excel_end_Rindex");
					}
					int[][] inlayArticleData = new int[inlayArticle_in_Excel_end_Rindex - inlayArticle_in_Excel_start_Rindex + 1][];
					for (int r = inlayArticle_in_Excel_start_Rindex; r <= inlayArticle_in_Excel_end_Rindex; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int cellNumber = row.getPhysicalNumberOfCells();
							if (cellNumber <= inlayArticle_in_Excel_start_Cindex) {
								throw new Exception("第" + i + "个sheet格式不正确，列数不大于开始列数" + cellNumber + " " + inlayArticle_in_Excel_start_Cindex);
							}
							int[] cellValues = new int[cellNumber - inlayArticle_in_Excel_start_Cindex];
							for (int j = inlayArticle_in_Excel_start_Cindex; j < cellNumber; j++) {
								HSSFCell cell = row.getCell(j);
								int value = 0;
								try {
									value = (int) cell.getNumericCellValue();
								} catch (Exception ex) {
									try {
										value = Integer.parseInt(cell.getStringCellValue());
									} catch (Exception e) {
										throw e;
									}
								}
								ArticleManager.logger.warn("宝石属性:(" + r + "," + j + ")value:" + value);
								cellValues[j - inlayArticle_in_Excel_start_Cindex] = value;
							}
							inlayArticleData[r - inlayArticle_in_Excel_start_Rindex] = cellValues;
						}
					}
					// 宝石属性数值
					inlayArticleExcelDataTemps = inlayArticleData;
					for (int[] aa : inlayArticleExcelDataTemps) {
						ArticleManager.logger.warn("宝石属性:" + Arrays.toString(aa));
					}
				} else {
					if (rows <= equipmentValuesInExcelStartY) {
						throw new Exception("第" + i + "个sheet格式不正确，行数不大于开始行数");
					}
					int[][] careerData = new int[rows - equipmentValuesInExcelStartY][];
					for (int r = equipmentValuesInExcelStartY; r < rows; r++) {
						HSSFRow row = sheet.getRow(r);
						if (row != null) {
							int cellNumber = row.getPhysicalNumberOfCells();
							if (cellNumber <= equipmentValuesInExcelStartX) {
								throw new Exception("第" + i + "个sheet格式不正确，列数不大于开始列数" + cellNumber + " " + equipmentValuesInExcelStartX);
							}
							int[] cellValues = new int[cellNumber - equipmentValuesInExcelStartX];
							for (int j = equipmentValuesInExcelStartX; j < cellNumber; j++) {
								HSSFCell cell = row.getCell(j);
								int value = 0;
								try {
									value = Integer.parseInt(cell.getStringCellValue());
								} catch (Exception ex) {
									try {
										value = (int) cell.getNumericCellValue();
									} catch (Exception e) {
										throw e;
									}
								}
								cellValues[j - equipmentValuesInExcelStartX] = value;
							}
							careerData[r - equipmentValuesInExcelStartY] = cellValues;
						}
					}
					// "通用职业装备取值","修罗装备取值","影魅装备取值","仙心装备取值","九黎装备取值","万毒谷装备取值"
					careerExcelDataTemps[i - 1] = careerData;
				}
			}
			this.equipmentQuanZhong = equipmentQuanZhong;
			this.careerExcelDatas = careerExcelDataTemps;
			this.inlayArticleExcelDatas = inlayArticleExcelDataTemps;
			if (is != null) {
				is.close();
			}
		}
	}

	public static Random random = new Random();

	/**
	 * 通过颜色星级以及装备物种，生成装备实体
	 * 物种中包含职业，装备等级限制，部件位置 根据这些从数据里取值，组成装备实体
	 * 先通过职业取得该职业的装备属性数据及属性对应权重("通用职业装备取值","蜀山派装备取值","炼狱宗装备取值","昆仑山装备取值","灵隐阁装备取值","万毒谷装备取值")
	 * 在根据部位取得该部位应该具有的属性
	 * 在根据装备等级限制得到相应级别下的属性值
	 * @param ee
	 * @param e
	 * @param star
	 * @param levelLimit
	 */
	public static void setEquipmentEntity(EquipmentEntity ee, Equipment e, int star, int color) throws Exception {
	}

	public static void main(String[] args) throws Exception {
		File file = new File("D:\\mywork\\工作用的资料\\equipmentExcel.xls");
		ReadEquipmentExcelManager reem = new ReadEquipmentExcelManager();
		ReadEquipmentExcelManager.self = reem;
		reem.loadEquipmentDataExcel(file);
		int[][] inlayArticleExcelDatas = reem.inlayArticleExcelDatas;
		Equipment e = new Equipment();
		e.setPlayerLevelLimit(20);
		e.setCareerLimit(1);
		e.setEquipmentType(0);
		e.setName("测试装备生成");
		EquipmentEntity ee = new EquipmentEntity(100);
		ArticleManager am = new ArticleManager();
		am.initFile(new File("D:\\mywork\\工作用的资料\\article.xls"));
		for (String a : am.getSuitEquipmentMap().keySet()) {
			SuitEquipment se = am.getSuitEquipmentMap().get(a);
			StringBuffer sb = new StringBuffer();
			sb.append(se.getSuitEquipmentName() + " ");
			sb.append(se.getSuitLevel() + " ");
			sb.append("属性位置:");
			for (int b : se.getOpenPropertyIndexs()) {
				sb.append(b + ",");
			}
			sb.append("件数:");
			for (int b : se.getOpenPropertySuitCounts()) {
				sb.append(b + ",");
			}
			sb.append("属性值:");
			for (int b : se.getPropertyValue()) {
				sb.append(b + ",");
			}
		}
	}
}
