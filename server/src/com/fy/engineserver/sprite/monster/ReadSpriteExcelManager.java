package com.fy.engineserver.sprite.monster;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.util.ServiceStartRecord;

public class ReadSpriteExcelManager {
	
	private static ReadSpriteExcelManager self;
	
	public static ReadSpriteExcelManager getInstance(){
		return self;
	}
	/**
	 * 装备数据的文件
	 */
	protected File configFile = new File("D:\\mywork\\工作用的资料\\spriteData.xls");
	public File getConfigFile() {
		return configFile;
	}
	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}
	public void init() throws Exception{
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(configFile != null && configFile.isFile()){
			loadSpriteDataExcel(configFile);
		}
		self = this;
		ServiceStartRecord.startLog(this);
	}

	/**
	 * 三维数组
	 * 第一维 为职业，分别为"通用职业装备取值","蜀山派装备取值","炼狱宗装备取值","昆仑山装备取值","灵隐阁装备取值","万毒谷装备取值"
	 * 第二维 为级别
	 * 第三维 为各个属性的数值
	 */
	public int[][][] careerExcelDatas = new int[0][][];

	/**
	 * 读取Excel的装备数值开始坐标数值，数值从零开始
	 */
	public int equipmentValuesInExcelStartX = 1;//代表从第二列开始
	public int equipmentValuesInExcelStartY = 1;//代表从第二行开始
	
	public String[] propertys = new String[]{"MHP","AP","AP2","AC","AC2","MMP","DAC",
											"HITP","DGP","ACTP","CHP","DCHP","FAP",
											"IAP","WAP","TAP","FRT","IRT","WRT",
											"TRT","DFRT","DIRT","DWRT","DTRT"};
	public void loadSpriteDataExcel(File file) throws Exception{
		if(file != null && file.isFile() && file.exists()){
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			HSSFWorkbook workbook = new HSSFWorkbook(pss);

			int[][][] careerExcelDataTemps = new int[4][][];

			for(int i = 0; i < 4; i++){
				HSSFSheet sheet = workbook.getSheetAt(i);
	            int rows = sheet.getPhysicalNumberOfRows();
	            //i==0表示读取的是装备部位权重sheet，剩余的7个sheet为各职业装备对应属性数值表
	            
				if(rows <= equipmentValuesInExcelStartY){
					throw new Exception("第"+i+"个sheet格式不正确，行数不大于开始行数");
				}
				int[][] careerData = new int[rows - equipmentValuesInExcelStartY][];
	            for (int r = equipmentValuesInExcelStartY; r < rows; r++) {
	                HSSFRow row = sheet.getRow(r);
	                if (row != null) {
	                	int cellNumber = row.getPhysicalNumberOfCells();
	                	if(cellNumber <= equipmentValuesInExcelStartX){
	    					throw new Exception("第"+i+"个sheet格式不正确，列数不大于开始列数"+cellNumber+" "+equipmentValuesInExcelStartX);
	    				}
	                	int[] cellValues = new int[cellNumber - equipmentValuesInExcelStartX];
	                	for(int j = equipmentValuesInExcelStartX; j < cellNumber; j++){
	                		HSSFCell cell = row.getCell((short)j);
	                		int value = 0;
	                		try{
	                			value = Integer.parseInt(cell.getStringCellValue());
	                		}catch(Exception ex){
	                			try{
	                				value = (int)cell.getNumericCellValue();
	                			}catch(Exception e){
	                				throw e;
	                			}
	                		}
	                		cellValues[j - equipmentValuesInExcelStartX] = value;
	                	}
	                	careerData[r - equipmentValuesInExcelStartY] = cellValues;
	                }
	            }
	            //"通用职业装备取值","蜀山派装备取值","炼狱宗装备取值","昆仑山装备取值","灵隐阁装备取值","万毒谷装备取值"
	            careerExcelDataTemps[i] = careerData;
            }
			this.careerExcelDatas = careerExcelDataTemps;

			if(is != null){
				is.close();
			}
		}
	}
	public static Random random = new Random();

	public static void main(String[] args) throws Exception{
		ReadSpriteExcelManager rm = new ReadSpriteExcelManager();
		rm.loadSpriteDataExcel(rm.configFile);
//		System.out.println();
	}
}
