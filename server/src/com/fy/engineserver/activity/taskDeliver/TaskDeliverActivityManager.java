package com.fy.engineserver.activity.taskDeliver;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.bloodrate.BloodRateAct;
import com.fy.engineserver.activity.extraquiz.ExtraQuizActivity;
import com.fy.engineserver.activity.floprateActivity.FlopRateActivity;
import com.fy.engineserver.activity.jiazu.LuckTreeActivity;
import com.fy.engineserver.activity.jiazu.RefreshTaskActivity;
import com.fy.engineserver.activity.jiazu.ShangXiangActivity;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.carbon.devilSquare.activity.ExtraDevilOpenTimesActivity;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
/**
 * 活动
 * @author Administrator
 *
 */
public class TaskDeliverActivityManager {
	public static TaskDeliverActivityManager instance;
	
	private String fileName;
	
	public static final int 完成特定次数任务活动sheet = 0;
	public static final int 恶魔城堡加次数活动sheet = 1;
	public static final int 地图怪物掉率翻倍sheet = 2;
	public static final int 血脉获取增加sheet = 3;
	public static final int 家族炼丹sheet = 4;
	public static final int 家族上香sheet = 5;
	public static final int 刷新家族任务sheet = 6;
	public static final int 额外答题奖励活动sheet = 7;
	
	private TaskDeliverActivityManager(){
		instance = this;
	}
	
	public static void main(String[] args) throws Exception {
		TaskDeliverActivityManager m = new TaskDeliverActivityManager();
		m.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//delivertaskAct.xls");
		m.initFile();
	}
	
	public void init() throws Exception {
		
		initFile();
		ServiceStartRecord.startLog(this);
	}

	private void initFile() throws Exception {
		File ff = new File(fileName);
		File f = new File(ConfigServiceManager.getInstance().getFilePath(ff));
		if(!f.exists()){
			throw new Exception(fileName + " 配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(完成特定次数任务活动sheet);			
		int rows = sheet.getPhysicalNumberOfRows();
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				TaskDeliverAct tda = getTaskDeliverAct(row);
//				System.out.println(tda);
				if(!list.contains(tda)) {
					list.add(tda);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.taskDeliverAct, list);;
		
		sheet = workbook.getSheetAt(恶魔城堡加次数活动sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list2 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ExtraDevilOpenTimesActivity tda = getExtraDevilOpenTimesActivity(row);
				if(!list2.contains(tda)) {
					list2.add(tda);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.addDevilOpenTimes, list2);
		
		sheet = workbook.getSheetAt(地图怪物掉率翻倍sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list3 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				FlopRateActivity tda = getFlopRateActivity(row);
				if(!list3.contains(tda)) {
					list3.add(tda);
//					System.out.println(tda);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.addFlopRate, list3);
		

		sheet = workbook.getSheetAt(血脉获取增加sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list4 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				BloodRateAct tda = getBloodRateAct(row);
				if(!list4.contains(tda)) {
					list4.add(tda);
//					System.out.println(tda);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.bloodActivity, list4);
		
		
		sheet = workbook.getSheetAt(家族炼丹sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list5 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				LuckTreeActivity lta = getLuckTreeActivity(row);
				if(!list5.contains(lta)) {
					list5.add(lta);
//					System.out.println(lta);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.platLuckTree, list5);
		
		sheet = workbook.getSheetAt(家族上香sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list6 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ShangXiangActivity lta = getShangXiangActivity(row);
				if(!list6.contains(lta)) {
					list6.add(lta);
//					System.out.println(lta);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.shangxiangActivity, list6);
		
		sheet = workbook.getSheetAt(刷新家族任务sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> list7 = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				RefreshTaskActivity lta = getRefreshTaskActivity(row);
				if(!list7.contains(lta)) {
					list7.add(lta);
//					System.out.println(lta);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.refreshTaskActivity, list7);
		
		
		sheet = workbook.getSheetAt(额外答题奖励活动sheet);
		rows = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> datiList = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				ExtraQuizActivity lta = getExtraQuizActivity(row);
				if(!datiList.contains(lta)) {
					datiList.add(lta);
//					System.out.println(lta);
				}
			}catch(Exception e) {
				throw new Exception(fileName + "第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.datiActivity, datiList);
		
	}
	
	private ExtraQuizActivity getExtraQuizActivity(HSSFRow row) throws Exception{
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		ExtraQuizActivity lta = new ExtraQuizActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		String openTimes = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		String quizNums = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		String rightNums = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		String rewardArticles = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		lta.initOtherVar(openTimes, quizNums, rightNums, rewardArticles);
		return lta;
	}
	
	private RefreshTaskActivity getRefreshTaskActivity(HSSFRow row) throws Exception{
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		RefreshTaskActivity lta = new RefreshTaskActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		String cost = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		lta.initOtherVar(cost);
		return lta;
	}
	
	private ShangXiangActivity getShangXiangActivity(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		ShangXiangActivity lta = new ShangXiangActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		String colorType = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String levels = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String costs = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		lta.initOtherStr(colorType, levels, costs);
		return lta;
	}
	
	private LuckTreeActivity getLuckTreeActivity(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		LuckTreeActivity lta = new LuckTreeActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		String colorType = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String levels = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String costs = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		lta.initOtherStr(colorType, levels, costs);
		return lta;
	}
	
	
	private BloodRateAct getBloodRateAct(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String raritys = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		String muls = ReadFileTool.getString(row, rowNum++, ActivitySubSystem.logger);
		String[] temp = raritys.split(",");
		String[] temp2 = muls.split(",");
		if (temp.length != temp2.length) {
			throw new Exception("[稀有度与倍数不匹配]");
		}
		byte[] rarit = new byte[temp.length];
		double[] mul = new double[temp2.length];
		for (int i=0; i<rarit.length; i++) {
			rarit[i] = Byte.parseByte(temp[i]);
			mul[i] = Double.parseDouble(temp2[i]);
		}
		BloodRateAct bra = new BloodRateAct(startTime, endTime, platForms, openServerNames, notOpenServers);
		bra.setOtherVar(rarit, mul);
		return bra;
	}
	
	private FlopRateActivity getFlopRateActivity(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		double extraTimes = TaskDeliverActivityManager.getDouble(row, rowNum++, ActivitySubSystem.logger);
		String maps = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		List<String> list = new ArrayList<String>();
		String[] tempT = maps.split(",");
		for(int i=0; i<tempT.length; i++) {
			list.add(tempT[i]);
		}
		FlopRateActivity tda = new FlopRateActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		tda.setOtherVar(extraTimes, list);
		return tda;
	}
	
	public static double getDouble(HSSFRow row, int i, Logger logger){
		HSSFCell cell = row.getCell(i);
		if(cell == null){
			logger.error("单元格是null，页签{} 行{} 列{} ",
					new Object[]{row.getSheet().getSheetName(), row.getRowNum(), i});
			return 0;
		}
		int type = cell.getCellType();
		if(type == Cell.CELL_TYPE_STRING){
			return Double.parseDouble(cell.getStringCellValue().trim());
		}else if(type == Cell.CELL_TYPE_FORMULA){
			int typeF = cell.getCachedFormulaResultType();
			if(typeF == Cell.CELL_TYPE_STRING){
				return Double.parseDouble(cell.getStringCellValue().trim());
			}
		}
		double ret = cell.getNumericCellValue();
		return ret;
	}
	
	private ExtraDevilOpenTimesActivity getExtraDevilOpenTimesActivity(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String extraTimes = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String[] tempT = extraTimes.split(",");
		int[] ii = new int[tempT.length];
		for(int i=0; i<ii.length; i++) {
		 		ii[i] = Integer.parseInt(tempT[i].replace(".0", ""));
			}
		ExtraDevilOpenTimesActivity tda = new ExtraDevilOpenTimesActivity(startTime, endTime, platForms, openServerNames, notOpenServers);
		tda.setOtherVar(ii);
		return tda;
	}
	
	private TaskDeliverAct getTaskDeliverAct(HSSFRow row) throws Exception {
		int rowNum = 0;
		String startTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String endTime = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String platForms = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String openServerNames = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String notOpenServers = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String taskType = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String taskName = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		int needDeliverTimes = getInt(row,rowNum++);
		@SuppressWarnings("unused")
		String givePropName = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String givePropName_stat = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		int givePropColor = getInt(row,rowNum++);
		int givePropNum = getInt(row,rowNum++);
		HSSFCell cell = row.getCell(rowNum++);
		boolean givePropBind = cell.getBooleanCellValue();
		String mailTitle = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		String mailContent = PetGrade.getString(row, rowNum++, ActivitySubSystem.logger);
		TaskDeliverAct tda = new TaskDeliverAct(startTime, endTime, platForms, openServerNames, notOpenServers);
		ActivityProp giveProp = new ActivityProp(givePropName_stat, givePropColor, givePropNum, givePropBind);
		tda.setOtherVar(needDeliverTimes, taskType, taskName, mailTitle, mailContent, giveProp);
		return tda;
	}
	int getInt(HSSFRow row, int cellIdx) {
		HSSFCell cell = row.getCell(cellIdx);
		if (cell == null) {
			ActivitySubSystem.logger.error("单元格是null " + cellIdx + " 行 " + row.getRowNum() + " at " + row.getSheet().getSheetName());
			return 0;
		}
		int type = cell.getCellType();
		if (type == HSSFCell.CELL_TYPE_NUMERIC) {
			return (int) cell.getNumericCellValue();
		}
		if(cell.getStringCellValue().trim().length()>0){
			return Integer.parseInt(cell.getStringCellValue());
		}
		return 0;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
