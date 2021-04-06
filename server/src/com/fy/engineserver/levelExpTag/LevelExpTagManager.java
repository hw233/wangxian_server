package com.fy.engineserver.levelExpTag;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.fy.engineserver.util.ServiceStartRecord;


public class LevelExpTagManager {

	public static int level = 300;
	public static int activityNum = 15;
	
	private static LevelExpTagManager self = null;
	private long[][] mapping = new long[level][activityNum];

	private String configFile = "f:\\levelExp.xlsx";

	public static synchronized LevelExpTagManager getInstance() {
		
		if(self == null){
			throw new RuntimeException("解析体力数值表错误");
		}
		return self;
	}

	public long getValueByLevelAndActivity(int level,ActivityType activityType)throws Exception{
		return this.mapping[level-1][activityType.type];
	}
	
	public void init() throws Exception{
		
		
		self = loadExcel();
		ServiceStartRecord.startLog(this);
		
	}
	public LevelExpTagManager loadExcel() throws Exception {

		int 家族建设任务 = 1;
		int 神农 = 2;
		int 平定四方 = 3;
		int 国内寻宝 = 4;
		int 国内仙缘 = 5;
		int 国内论道 = 6;
		int 国外寻宝 = 7;
		int 国外仙缘 = 8;
		int 国外论道 = 9;
		int 国外刺探 = 10;
		int 国外宝藏 = 11;
		int 经验挂机副本 = 12;
		int 家族经验副本 = 13;
		int 装备挂机副本 = 14;
		int 宠物捕捉副本 = 15;

//		File file = new File(configFile);
//		HSSFWorkbook workbook = null;
		HSSFWorkbook workbook = null;
		try {
//			InputStream is = new FileInputStream(file);
//			POIFSFileSystem pss = new POIFSFileSystem(is);
			
			workbook = new HSSFWorkbook(new FileInputStream( new File(configFile)));
//			HSSFSheet sheet = null;
			HSSFSheet sheet = null;
			sheet = workbook.getSheetAt(0);
			if (sheet == null)
				return null;
			int rows = sheet.getPhysicalNumberOfRows();
			
			if(rows != level+1){
				throw new RuntimeException("等级经验对应表错误");
			}
			
			LevelExpTagManager ee = new LevelExpTagManager();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					for(int i= 1;i<activityNum+1;i++){
						long value = (long)row.getCell(i).getNumericCellValue();
						ee.mapping[r-1][i-1] = value;
					}
				}
			}
			return ee;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	
	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public static void main(String[] args) throws Exception {
		
		LevelExpTagManager let1 = new LevelExpTagManager();
		let1.init();
		LevelExpTagManager let = LevelExpTagManager.getInstance();
		long value = let.getValueByLevelAndActivity(220, ActivityType.国内寻宝);
	}
}
