package com.fy.engineserver.activity.vipExpActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ServerFit4Activity;


public class VipExpActivityManager {
	public static VipExpActivityManager instance;
	/** key消费类型  value为对应的活动列表 */
	public Map<Integer, List<VipExpActivity>> vipExpActivityMap = new HashMap<Integer, List<VipExpActivity>>();
	
	private String fileName;
	
	public static final int all_expends_activity = 1;	//所有，非韩服默认此项
	public static final int shop_expends_activity = 2;	//商城..
	public static final int petdao_expends_activity = 3;	//宠物迷城 ..
	public static final int duobao_expends_activity = 4;	//福地洞天  ..
	public static final int shengshou_expends_activity = 5;		//圣兽岛 ..
	public static final int qifu_expends_activity = 6;			//祈福 ..
	public static final int lianyao_expends_activity = 7;		//炼妖..
	public static final int default_add_rmb_expense = 99;		//默认需要加vip经验但是没活动的消费类型
	
	private VipExpActivityManager() {
		instance = this;
	}
	/**
	 * 判断vip经验活动
	 * @param player
	 * @param type
	 * @return
	 */
	public double getVipExpAddMul(Player player, int type) {
		double mul = 0;
		boolean isAllActivity = false;
		if(vipExpActivityMap.get(all_expends_activity) != null) {			//优先判断是否有所有获取方式都生效的活动
			List<VipExpActivity> al = vipExpActivityMap.get(all_expends_activity);
			if(al != null && al.size() > 0) {
				for(VipExpActivity ve : al) {
					if(ve.isServerFit(player)) {
						isAllActivity = true;
						mul = ve.getMultiple();
						ActivitySubSystem.logger.warn("[vip经验活动] [所有经验获取途径均有效，额外增加倍数:" + mul + "] [" + player.getLogString() + "]");
						break;
					}
				}
			}
		}
		if(!isAllActivity && type != all_expends_activity) {
			if(vipExpActivityMap.get(type) != null) {
				List<VipExpActivity> al = vipExpActivityMap.get(type);
				if(al != null && al.size() > 0) {
					for(VipExpActivity ve : al) {
						if(ve.isServerFit(player)) {
							mul = ve.getMultiple();
							ActivitySubSystem.logger.warn("[vip经验活动] [单个类型vip经验翻倍活动生效，额外增加倍数:" + mul + " && 类型: " + type + "] [" + player.getLogString() + "]");
							break;
						}
					}
				}
			}
		}
		return mul;
	}
	
	public static void main(String[] args) throws Exception {
		VipExpActivityManager vm = new VipExpActivityManager();
		vm.setFileName("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//vipExpActivity.xls");
		vm.init();
	}
	
	public void init() throws Exception {
		
		loadFile();
		ServiceStartRecord.startLog(this);
	}
	
	private void loadFile() throws Exception {
		File f = new File(fileName);
		if(!f.exists()){
			throw new Exception("vipExpActivity.xlsx配表不存在! " + f.getAbsolutePath());
		}
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
			VipExpActivity value = getVipExpActivity(row);
			add2Map(value);
//			System.out.println(value);
		}
	}
	
	private void add2Map(VipExpActivity value) {
		if(vipExpActivityMap.get(value.getExpenseType()) == null) {
			List<VipExpActivity> list = new ArrayList<VipExpActivity>(); 
			vipExpActivityMap.put(value.getExpenseType(), list);
		}
		List<VipExpActivity> ll = vipExpActivityMap.get(value.getExpenseType());
		if(!ll.contains(value)) {
			ll.add(value);
			vipExpActivityMap.put(value.getExpenseType(), ll);
		}
	}
	
	private VipExpActivity getVipExpActivity(HSSFRow row) throws Exception {
		VipExpActivity ve = new VipExpActivity();
		String startTime = "";
		String endTime = "";
		String platForm = "";
		String openServerName = "";
		String notOpenServerName = "";
		int vipLevelLimit = 0;
		int expenseType = -1;
		double mults = 0;
		
		int rowNum = 0;
		HSSFCell cell = row.getCell(rowNum++);
		startTime = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		endTime = cell.getStringCellValue().trim();

		cell = row.getCell(rowNum++);
		platForm = cell.getStringCellValue().trim();
		cell = row.getCell(rowNum++);
		if (cell != null) {
			openServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		if (cell != null) {
			notOpenServerName = cell.getStringCellValue().trim();
		}
		cell = row.getCell(rowNum++);
		vipLevelLimit = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		expenseType = (int) cell.getNumericCellValue();
		cell = row.getCell(rowNum++);
		mults = cell.getNumericCellValue();
		
		ServerFit4Activity serverfit = new ServerFit4Activity(platForm, openServerName, notOpenServerName);
		long st = TimeTool.formatter.varChar19.parse(startTime);
		long et = TimeTool.formatter.varChar19.parse(endTime);
		ve.setStartTime(st);
		ve.setEndTime(et);
		ve.setServerfit(serverfit);
		ve.setMultiple(mults);
		ve.setVipLevelLimit(vipLevelLimit);
		ve.setExpenseType(expenseType);
		return ve;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
