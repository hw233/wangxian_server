package com.fy.engineserver.activity.minigame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import com.fy.engineserver.util.ServiceStartRecord;

public class MinigameActivityManager {
	private static MinigameActivityManager instance;
	
	private String activityFile;
	
	public static int act1LevelLimit = 10;
	
	private Map<Integer, MinigameActivityModel> activityMap;
	
	private MinigameActivityManager(){
		instance = this;
	}
	
	public static MinigameActivityManager getInstance(){
		return instance;
	}
	/**
	 * 增加勾玉活动增加勾玉数量
	 * @return
	 */
	public int getAct1AddAmount(){
		if(activityMap == null || activityMap.get(1) == null){
			return 0;
		}
		MinigameActivityModel model = activityMap.get(1);
		return model.getAddAmount();
	}
	
	public int getAct1LevelLimit(){
		if(activityMap == null || activityMap.get(1) == null){
			return 3000;
		}
		MinigameActivityModel model = activityMap.get(1);
		return model.getLevelLimit();
	}
	
	/**
	 * 使用活动id检测活动是否开启
	 * @param activityId
	 * @return
	 */
	public boolean checkActivityAct(int activityId){
		if(activityMap == null || activityMap.get(activityId) == null){
			return false;
		}
		MinigameActivityModel model = activityMap.get(activityId);
		Timestamp cut = new Timestamp(System.currentTimeMillis());
		if(model.getStartTime().compareTo(cut) <= 0 && model.getEndTime().compareTo(cut) >= 0){
			return true;
		}
		return false;
	}
	
	private void loadActivityFile() throws Exception{
		File f = new File(activityFile);
		if(!f.exists()){
			throw new Exception("activityminigame.xlsx配表不存在! " + f.getAbsolutePath());
		}
		activityMap = new HashMap<Integer, MinigameActivityModel>();
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);
		int rows = sheet.getPhysicalNumberOfRows();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			MinigameActivityModel model = new MinigameActivityModel();
			int rowNum = 0;
			HSSFCell cell = row.getCell(rowNum++);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			int id = (int) cell.getNumericCellValue();
			String tips;
			int levelLimit;
			String startTime;
			String endTime;
			int addAmount;
			cell = row.getCell(rowNum++);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			tips = cell.getStringCellValue();
			cell = row.getCell(rowNum++);
			levelLimit = (int) cell.getNumericCellValue();
			cell = row.getCell(rowNum++);
			startTime = cell.getStringCellValue();
			cell = row.getCell(rowNum++);
			endTime = cell.getStringCellValue();
			cell = row.getCell(rowNum++);
			addAmount = (int) cell.getNumericCellValue();
			model.setId(id);
			model.setTips(tips);
			model.setStartTime(Timestamp.valueOf(startTime));
			model.setEndTime(Timestamp.valueOf(endTime));
			model.setLevelLimit(levelLimit);
			model.setAddAmount(addAmount);
			activityMap.put(model.getId(), model);
		}
		act1LevelLimit = activityMap.get(1).getLevelLimit();
	}
	
	public void init(){
		
		try{
			loadActivityFile();
		}catch(Exception e){
			
		}
		ServiceStartRecord.startLog(this);
	}

	public String getActivityFile() {
		return activityFile;
	}

	public void setActivityFile(String activityFile) {
		this.activityFile = activityFile;
	}
}
