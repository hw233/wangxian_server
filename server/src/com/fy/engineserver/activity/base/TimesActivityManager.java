package com.fy.engineserver.activity.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;

public class TimesActivityManager {
	
	public static int QIFU_ACTIVITY = 1;				//祈福
	public static int HEJIU_ACTIVITY = 2;				//喝酒
	public static int TUMOTIE_ACTIVITY = 3;				//封魔录
	
	public static TimesActivityManager instacen = null;
	
	private String fileName;

	public ArrayList<TimesActivity> activitys = new ArrayList<TimesActivity>();
	
	public void init() throws Exception {
		
		ActivitySubSystem.logger.warn("TimesActivityManager初始化开始");
		instacen = this;
		initTimesActFile();
		ActivitySubSystem.logger.warn("TimesActivityManager初始化结束");
		ServiceStartRecord.startLog(this);
	}
	
	public static void main(String[] args) throws Exception {
		TimesActivityManager t = new TimesActivityManager();
		t.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//addActivityTimes.xls");
		t.initTimesActFile();
	}
	
	private void initTimesActFile() throws Exception {
		File f = new File(fileName);
		f = new File(ConfigServiceManager.getInstance().getFilePath(f));
		if(!f.exists()){
			throw new Exception("addActivityTimes.xls配表不存在! " + f.getAbsolutePath());
		}
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = workbook.getSheetAt(0);			
		int rows = sheet.getPhysicalNumberOfRows();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int blessActivityID = 1000;
		List<BaseActivityInstance> tempList = new ArrayList<BaseActivityInstance>();
		for(int i=1; i<rows; i++){
			HSSFRow row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			try{
				TimesActivity temp = getTimesActivity(row, format, blessActivityID++);
				addActivity(temp);
				tempList.add(temp);
			}catch(Exception e) {
				throw new Exception("addActivityTimes.xls第" + (i+1) + "行异常！！", e);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.addTimesAct, tempList);
	}
	
	private TimesActivity getTimesActivity(HSSFRow row, SimpleDateFormat format, int blessId) throws Exception {
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String serverNames = "";
		String unopenServerNames = "";
		int levelLimit = 0;
		int addNum = 0;
		int activeId = 0;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		
		cell = row.getCell(rowNum++);
		if(cell != null){
			serverNames = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if(cell != null){
			unopenServerNames = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		activeId = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		levelLimit = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		addNum = (int) cell.getNumericCellValue();
		
		TimesActivity activ = new TimesActivity(startTime, endTime, platForm, serverNames, unopenServerNames);
		activ.setOtherVar(blessId, addNum, activeId, levelLimit);
		return activ;
	}
	
	public TimesActivityManager() {
//		ActivitySubSystem.logger.warn("TimesActivityManager初始化开始");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		int blessActivityID = 1000;
//		if (PlatformManager.getInstance().platformOf(Platform.官方)) {
//			try{
//				String startTime = "2013-09-30 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, HEJIU_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-09-30 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TUMOTIE_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-09-30 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 1, QIFU_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			
//			
//			try{
//				String startTime = "2013-09-10 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, HEJIU_ACTIVITY);
//				activity.setServerNames(new String[]{"pan3"});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-09-10 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TUMOTIE_ACTIVITY);
//				activity.setServerNames(new String[]{"pan3"});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-09-10 00:00:00";
//				String endTime = "2013-10-07 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 1, QIFU_ACTIVITY);
//				activity.setServerNames(new String[]{"pan3"});
//				addActivity(activity);
//				blessActivityID++;
//			} catch (Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			
//		}else if (PlatformManager.getInstance().platformOf(Platform.台湾)) {
//			try{
//				String startTime = "2013-08-19 10:00:00";
//				String endTime = "2013-09-02 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 1, QIFU_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("祈福活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-08-19 10:00:00";
//				String endTime = "2013-09-19 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, HEJIU_ACTIVITY);
//				activity.setServerNames(new String[]{"雪域冰城"});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("喝酒活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-08-19 10:00:00";
//				String endTime = "2013-09-19 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 5, HEJIU_ACTIVITY);
//				activity.setServerNames(new String[]{"皇圖霸業","仙尊降世"});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("喝酒活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-08-19 10:00:00";
//				String endTime = "2013-09-19 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TUMOTIE_ACTIVITY);
//				activity.setServerNames(new String[]{"雪域冰城"});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("封魔录活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-08-19 10:00:00";
//				String endTime = "2013-09-19 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 5, TUMOTIE_ACTIVITY);
//				activity.setServerNames(new String[]{"皇圖霸業","仙尊降世"});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("封魔录活动创建出错",e );
//			}
//		}else if (PlatformManager.getInstance().platformOf(Platform.韩国)) {
//			try{
//				String startTime = "2013-09-10 10:00:00";
//				String endTime = "2013-10-15 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, QIFU_ACTIVITY,20);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("祈福活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-07-10 10:00:00";
//				String endTime = "2013-10-15 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, HEJIU_ACTIVITY,20);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("喝酒活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-07-10 10:00:00";
//				String endTime = "2013-10-15 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TUMOTIE_ACTIVITY,20);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("封魔录活动创建出错",e );
//			}
//		}else if(PlatformManager.getInstance().platformOf(Platform.腾讯)){
//			try{
//				String startTime = "2013-06-24 00:00:00";
//				String endTime = "2013-06-29 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, HEJIU_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//			try{
//				String startTime = "2013-06-24 00:00:00";
//				String endTime = "2013-06-29 23:59:59";
//				TimesActivity activity = new TimesActivity(blessActivityID, format.parse(startTime).getTime(), format.parse(endTime).getTime(), 2, TUMOTIE_ACTIVITY);
//				activity.setServerNames(new String[]{TimesActivity.ALL_SERVER_ACTIVITY});
//				addActivity(activity);
//				blessActivityID++;
//			}catch(Exception e) {
//				ActivitySubSystem.logger.error("活动创建出错",e );
//			}
//		}
//		ActivitySubSystem.logger.warn("TimesActivityManager初始化结束");
	}
	
	public void addActivity(TimesActivity activity) {
		for (int i = 0; i < activitys.size(); i++) {
			if (activitys.get(i).activityID == activity.activityID) {
				activitys.remove(i);
				break;
			}
		}
		activitys.add(activity);
	}
	
	public void removeActivity(int activityID) {
		for (int i = 0; i < activitys.size(); i++) {
			if (activitys.get(i).activityID == activityID) {
				activitys.remove(i);
				break;
			}
		}
	}
	
	public int getAddNum(Player player, int type) {
		int num = 0;
		for(int i = 0; i < activitys.size(); i++) {
			TimesActivity acti = activitys.get(i);
			if (acti.activityType == type) {
				num += acti.getAddNum(player);
				//CoreSubSystem.logger.warn("[查询喝酒次数test] [activityType:"+acti.activityType+"] [num:"+num+"] ["+player.getName()+"]");
			}
		}
		return num;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
