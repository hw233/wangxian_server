package com.fy.engineserver.menu.activity.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.ExtraAwardActivity;
import com.fy.engineserver.activity.RefreshSpriteManager;
import com.fy.engineserver.activity.cave.MaintanceActivity;
import com.fy.engineserver.activity.cave.OutputActivity;
import com.fy.engineserver.activity.expactivity.DayilyTimeDistance;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ConfigServiceManager;

public class ExchangeActivityManager {
	private static ExchangeActivityManager instance;
	// private Map<Integer, List<String>> activityMap = new HashMap<Integer, List<String>>();
	private Map<String, List<ExchangeActivity>> menuMap = new HashMap<String, List<ExchangeActivity>>();// <活动名，菜单相关内容>

	private List<ExchangeActivityLimit> eaLimits = new ArrayList<ExchangeActivityLimit>();
	private List<ExtraAwardActivity> extraAwaList = new ArrayList<ExtraAwardActivity>();
	public List<DuJieActivity> dujieActivitys = new ArrayList<DuJieActivity>();
	public List<ExchangeActivity> activities = new ArrayList<ExchangeActivity>();
	
	/**仙府活动*/
	public List<OutputActivity> outputActivities = new ArrayList<OutputActivity>();
	public List<MaintanceActivity> maintanceActivities = new ArrayList<MaintanceActivity>();

	private String filePath;

	public static ExchangeActivityManager getInstance() {
		return instance;
	}

	/**
	 * 通过兑换活动名称获得活动限制ExchangeActivityLimit
	 * @param activityName
	 * @return
	 */
	public ExchangeActivityLimit getExchangeActivityLimit(String activityName) {
		for (ExchangeActivityLimit eaLimit : eaLimits) {
			if (eaLimit.getActivityName().equals(activityName)) {
				return eaLimit;
			}
		}
		return null;
	}

	/**
	 * 通过兑换菜单名获得兑换信息ExchangeActivity
	 * @param menuName
	 * @return
	 */
	public ExchangeActivity getExchangeActivity(String menuName) {
		for (Iterator<String> it = menuMap.keySet().iterator(); it.hasNext();) {
			String activityName = it.next();
			List<ExchangeActivity> exchangeActivities = menuMap.get(activityName);
			for (ExchangeActivity ea : exchangeActivities) {
				if (ea.getMenuName().equals(menuName)) {
					return ea;
				}
			}
		}
		ActivitySubSystem.logger.error("[通过菜单名获取ExchangeActivity错误] [活动名:" + menuName + "]");
		return null;
	}

	// 渡劫活动
	public void loadDuJieConfig(HSSFSheet sheet) throws Exception {
		long now = System.currentTimeMillis();
		int maxrow = sheet.getPhysicalNumberOfRows();
		List<BaseActivityInstance> tempList = new ArrayList<BaseActivityInstance>();
		for (int i = 1; i < maxrow; i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				int index = 0;
				String activityName = "";
				int minlevel[] = new int[0];
				String priceNames[] = new String[0];
				int priceColors[] = new int[0];
				int priceNums[] = new int[0];
				String platformName = "";
				// List<String> openServerNames = new ArrayList<String>();
				// List<String> limitServerNames = new ArrayList<String>();
				String mailTitle = "";
				String mailContent = "";
				long starttime = 0;
				long endtime = 0;
				int type = 0;

				String priceNamestr = "";
				String priceColorstr = "";
				String priceNumstr = "";
				String starttimestr = "";
				String endtimestr = "";

				try {
					activityName = StringTool.getCellValue(row.getCell(index++), String.class);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载activityName] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					String minlevelstr = StringTool.getCellValue(row.getCell(index++), String.class);
					minlevel = StringToInt(minlevelstr.split(","));
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载minlevel] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					priceNamestr = StringTool.getCellValue(row.getCell(index++), String.class);
					priceNames = priceNamestr.split(",");
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载priceNames] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					priceColorstr = StringTool.getCellValue(row.getCell(index++), String.class);
					priceColors = StringToInt(priceColorstr.split(","));
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载priceColors] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					priceNumstr = StringTool.getCellValue(row.getCell(index++), String.class);
					priceNums = StringToInt(priceNumstr.split(","));
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载priceNums] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					platformName = StringTool.getCellValue(row.getCell(index++), String.class);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载platformName] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					starttimestr = StringTool.getCellValue(row.getCell(index++), String.class);
					starttime = TimeTool.formatter.varChar19.parse(starttimestr);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载starttime] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					endtimestr = StringTool.getCellValue(row.getCell(index++), String.class);
					endtime = TimeTool.formatter.varChar19.parse(endtimestr);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载endtime] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				String openservers = "";
				try {
					openservers = StringTool.getCellValue(row.getCell(index++), String.class);
					// openServerNames = Arrays.asList(openservers.split(","));
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载openServerNames] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				String limitservers = "";
				try {
					limitservers = StringTool.getCellValue(row.getCell(index++), String.class);
					// limitServerNames = Arrays.asList(limitservers);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载limitServerNames] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					mailTitle = StringTool.getCellValue(row.getCell(index++), String.class);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载mailTitle] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					mailContent = StringTool.getCellValue(row.getCell(index++), String.class);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载mailContent] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}
				try {
					type = StringTool.getCellValue(row.getCell(index++), Integer.class);
				} catch (Exception e) {
					throw new Exception("[渡劫活动加载type] [页：" + sheet.getSheetName() + "] [行：" + i + "]  [单元格从0开始：" + (index - 1) + "] [出错信息：" + e + "]");
				}

				if (priceNames == null || priceNames.length != priceColors.length || priceNames.length != priceNums.length) {
					throw new Exception("奖励物品的颜色，数量长度不一致。");
				}

				DuJieActivity activity = new DuJieActivity(starttimestr, endtimestr, platformName, openservers, limitservers);
				activity.setOtherVar(activityName, minlevel, priceNames, priceColors, priceNums, mailTitle, mailContent, type);
				dujieActivitys.add(activity);
				boolean iseffect = now > starttime && now < endtime;
				ActivitySubSystem.logger.warn("[数据：" + dujieActivitys.size() + "] [iseffect:" + iseffect + "] [" + (now > starttime) + "] [" + (now < endtime) + "]" + activity.toString());
				tempList.add(activity);
			}
		}
		AllActivityManager.instance.add2AllActMap(AllActivityManager.dujieAct, tempList);
	}

	private int[] StringToInt(String[] a) {
		int[] fs = new int[a.length];
		for (int k = 0; k < a.length; k++) {
			fs[k] = Integer.parseInt(a[k]);
		}
		return fs;
	}

	public void loadXml() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			long now = System.currentTimeMillis();
			HSSFWorkbook hssfWorkbook = null;
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			hssfWorkbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			sheet = hssfWorkbook.getSheetAt(0);
			if (sheet == null) return;
			int rows0 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows0; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					String activityName = cell.getStringCellValue();
					String startTimeStr = row.getCell(index++).getStringCellValue();
					String endTimeStr = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServerNames = "";
					if (cell != null) {
						openServerNames = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServerNames = "";
					if (cell != null) {
						limitServerNames = cell.getStringCellValue();
					}
					ExchangeActivityLimit eal = new ExchangeActivityLimit(startTimeStr, endTimeStr, "sqage,tencent,malai,taiwan,korea", openServerNames, limitServerNames);
					eal.setOtherVar(activityName);
					eaLimits.add(eal);
					AllActivityManager.instance.add2AllActMap(AllActivityManager.exchangeAct, eal);
				}
			}
			sheet = hssfWorkbook.getSheetAt(1);
			if (sheet == null) return;
			int rows1 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows1; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					String activityName = row.getCell(index++).getStringCellValue();
					String menuName = row.getCell(index++).getStringCellValue();
					boolean tipYN = row.getCell(index++).getBooleanCellValue();
					String tip="";
					if(tipYN){
						tip = row.getCell(index++).getStringCellValue();
					}else{
						index++;
					}
					String costArticleNames = row.getCell(index++).getStringCellValue();
					String costArticleCNNames = row.getCell(index++).getStringCellValue();
					String costArticleColors = row.getCell(index++).getStringCellValue();
					String costArticleNums = row.getCell(index++).getStringCellValue();
					String gainArticleNames = row.getCell(index++).getStringCellValue();
					String gainArticleCNNames = row.getCell(index++).getStringCellValue();
					String gainArticleColors = row.getCell(index++).getStringCellValue();
					String gainArticleNums = row.getCell(index++).getStringCellValue();
					String gainArticleBinds = row.getCell(index++).getStringCellValue();
					String mailTitle = row.getCell(index++).getStringCellValue();
					String mailContent = row.getCell(index++).getStringCellValue();
					
					int rewardType = 0;
					int rewardNum = 0;
					int nextindex = index++;
					try{
						rewardType = (int)row.getCell(nextindex).getNumericCellValue();
					}catch(Exception e){
						try{
							rewardType = Integer.parseInt(row.getCell(nextindex).getStringCellValue());
						}catch(Exception e2){
							e2.printStackTrace();
						}
					}
					nextindex = index++;
					try{
						rewardNum = (int)row.getCell(nextindex).getNumericCellValue();
					}catch(Exception e){
						try{
							rewardNum = Integer.parseInt(row.getCell(nextindex).getStringCellValue());
						}catch(Exception e2){
							e2.printStackTrace();
						}
					}
					
					String[] costArticleNameArr = null;
					String[] costArticleCNNameArr = null;
					Integer[] costArticleColorArr = null;
					Integer[] costArticleNumArr = null;
					if (costArticleCNNames != null && !"".equals(costArticleCNNames)) {
						costArticleNameArr = StringTool.string2Array(costArticleNames, ",", String.class);
						costArticleCNNameArr = StringTool.string2Array(costArticleCNNames, ",", String.class);
						costArticleColorArr = StringTool.string2Array(costArticleColors, ",", Integer.class);
						costArticleNumArr = StringTool.string2Array(costArticleNums, ",", Integer.class);
						if (costArticleNameArr.length != costArticleCNNameArr.length || costArticleNumArr.length != costArticleCNNameArr.length || costArticleCNNameArr.length != costArticleColorArr.length) {
							System.out.println("[exchangeActivity.xls配置错误]");
							throw new IllegalStateException("[exchangeActivity.xls] [配置错误] [消耗物品名:" + costArticleNames + "] [消耗物品统计名:" + costArticleCNNames + "] [消耗物品颜色:" + costArticleColors + "] [消耗物品数量:" + costArticleNums + "]");
						}

					} else {
						throw new IllegalStateException("[exchangeActivity.xls] [未配置] [消耗物品名:" + costArticleNames + "] [消耗物品统计名:" + costArticleCNNames + "] [消耗物品颜色:" + costArticleColors + "] [消耗物品数量:" + costArticleNums + "]");
					}
					String[] gainArticleNameArr = null;
					String[] gainArticleCNNameArr = null;
					Integer[] gainArticleColorArr = null;
					Integer[] gainArticleNumArr = null;
					Boolean[] gainArticleBindArr = null;
					if (gainArticleCNNames != null && !"".equals(gainArticleCNNames)) {
						gainArticleNameArr = StringTool.string2Array(gainArticleNames, ",", String.class);
						gainArticleCNNameArr = StringTool.string2Array(gainArticleCNNames, ",", String.class);
						gainArticleColorArr = StringTool.string2Array(gainArticleColors, ",", Integer.class);
						gainArticleNumArr = StringTool.string2Array(gainArticleNums, ",", Integer.class);
						if (gainArticleBinds != null && gainArticleBinds.trim().length() > 0) {
							String strs[] = gainArticleBinds.split(",");
							Boolean bbs[] = new Boolean[strs.length];
							for (int j = 0, len = strs.length; j < len; j++) {
								if (strs[j].equals("true")) {
									bbs[j] = true;
								} else {
									bbs[j] = false;
								}
							}
							gainArticleBindArr = bbs;
						}
						if (gainArticleNameArr.length != gainArticleCNNameArr.length || gainArticleCNNameArr.length != gainArticleNumArr.length || gainArticleCNNameArr.length != gainArticleColorArr.length || gainArticleCNNameArr.length != gainArticleBindArr.length) {
							System.out.println("[exchangeActivity.xls配置错误]");
							throw new IllegalStateException("[exchangeActivity.xls] [配置错误] [获得物品名:" + gainArticleNames + "] [获得物品统计名:" + gainArticleCNNames + "] [获得物品颜色:" + gainArticleColors + "] [获得物品数量:" + gainArticleNums + "] [获得物品是否绑定:" + gainArticleBinds + "]");
						}
					} else {
						throw new IllegalStateException("[exchangeActivity.xls] [未配置] [获得物品名:" + gainArticleNames + "] [获得物品统计名:" + gainArticleCNNames + "] [获得物品颜色:" + gainArticleColors + "] [获得物品数量:" + gainArticleNums + "] [获得物品是否绑定:" + gainArticleBinds + "]");
					}
					ExchangeActivity eal = new ExchangeActivity(menuName, tipYN, tip, costArticleNameArr, costArticleCNNameArr, costArticleColorArr, costArticleNumArr, gainArticleNameArr, gainArticleCNNameArr, gainArticleColorArr, gainArticleNumArr, gainArticleBindArr, mailTitle, mailContent);
					eal.setRewardType(rewardType);
					eal.setRewardNum(rewardNum);
					if (!menuMap.containsKey(activityName)) {
						menuMap.put(activityName, new ArrayList<ExchangeActivity>());
					}
					menuMap.get(activityName).add(eal);
					ActivitySubSystem.logger.warn("[加载兑换活动] [已经加载的] [总数据：" + menuMap.size() + "] [行：" + i + "] [activityName:" + activityName == null ? "" : activityName + "] [menuName:" + menuName == null ? "" : menuName + "]");
				}
			}
			sheet = hssfWorkbook.getSheetAt(2);
			if (sheet == null) return;
			int rows2 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows2; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					String type = row.getCell(index++).getStringCellValue();
					String[] name = row.getCell(index++).getStringCellValue().split(",");
					String[] name_stat = row.getCell(index++).getStringCellValue().split(",");
					boolean needScore = (boolean) row.getCell(index++).getBooleanCellValue();
					HSSFCell cell = row.getCell(index++);
					int score = 0;
					if (cell != null) {
						score = (int) cell.getNumericCellValue();
					}
					cell = row.getCell(index++);
					boolean exact = false;
					if (cell != null) {
						exact = (boolean) cell.getBooleanCellValue();
					}
					String platForms = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServers = "";
					if (cell != null) {
						openServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServers = "";
					if (cell != null) {
						limitServers = cell.getStringCellValue();
					}
					int levelLimit = (int) row.getCell(index++).getNumericCellValue();
					cell = row.getCell(index++);
					String startTime = "";
					if (cell != null) {
						startTime = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String endTime = "";
					if (cell != null) {
						endTime = cell.getStringCellValue();
					}
					String[] awardNames = row.getCell(index++).getStringCellValue().split(",");
					String[] awardCNNames = row.getCell(index++).getStringCellValue().split(",");
					String[] awardColors = row.getCell(index++).getStringCellValue().split(",");
					String[] awardNums = row.getCell(index++).getStringCellValue().split(",");
					String[] awardBinds = row.getCell(index++).getStringCellValue().split(",");
					ActivityProp[] props = new ActivityProp[awardCNNames.length];
					if (awardCNNames.length != awardColors.length || awardCNNames.length != awardNums.length || awardCNNames.length != awardBinds.length) {
						new IllegalStateException("[exchangeActivity.xls] [sheet2] [奖励物品名字、颜色、数量、是否绑定数组长度不一致]");
					} else {
						for (int j = 0; j < awardNames.length; j++) {
							props[j] = new ActivityProp(awardCNNames[j], Integer.parseInt(awardColors[j]), Integer.parseInt(awardNums[j]), Boolean.parseBoolean(awardBinds[j]));
						}
					}
					String mailTitle = row.getCell(index++).getStringCellValue();
					String mailContent = row.getCell(index++).getStringCellValue();

					ExtraAwardActivity eaa = new ExtraAwardActivity(startTime, endTime, platForms, openServers, limitServers);
					eaa.setOtherVar(type, name, name_stat, needScore, score, exact, levelLimit, props, mailTitle, mailContent);
					extraAwaList.add(eaa);
					AllActivityManager.instance.add2AllActMap(AllActivityManager.extraAwardAct, eaa);
					// new ExtraAwardTaskEventTransact(eaa);
					ActivitySubSystem.logger.warn("[加载日常任务额外奖励活动完毕] [" + (System.currentTimeMillis() - now) + "ms]");
				}
			}
			sheet = hssfWorkbook.getSheetAt(3);
			loadDuJieConfig(sheet);
			
			//配方兑换活动
			sheet = hssfWorkbook.getSheetAt(4);
			if (sheet == null) return;
			rows1 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows1; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					String activityName = row.getCell(index++).getStringCellValue();
					String menuName = row.getCell(index++).getStringCellValue();
					String costArticleNames = row.getCell(index++).getStringCellValue();
					String costArticleCNNames = row.getCell(index++).getStringCellValue();
					String costArticleColors = row.getCell(index++).getStringCellValue();
					String costArticleNums = row.getCell(index++).getStringCellValue();
					String gainArticleNames = row.getCell(index++).getStringCellValue();
					String gainArticleCNNames = row.getCell(index++).getStringCellValue();
					String gainArticleColors = "";
					int nextindex = index++;
					try{
						gainArticleColors = row.getCell(nextindex).getStringCellValue();
					}catch(Exception e){
						gainArticleColors = row.getCell(nextindex).getNumericCellValue()+"";
					}
					String gainArticleNums = "";
					nextindex = index++;
					try{
						gainArticleNums = row.getCell(nextindex).getStringCellValue();
					}catch(Exception e){
						gainArticleNums = row.getCell(nextindex).getNumericCellValue()+"";
					}
					String gainArticleBinds = row.getCell(index++).getStringCellValue();
					int rewardType = 0;
					int rewardNum = 0;
					nextindex = index++;
					try{
						rewardType = (int)row.getCell(nextindex).getNumericCellValue();
					}catch(Exception e){
						try{
							rewardType = Integer.parseInt(row.getCell(nextindex).getStringCellValue());
						}catch(Exception e2){
							e2.printStackTrace();
						}
					}
					nextindex = index++;
					try{
						rewardNum = (int)row.getCell(nextindex).getNumericCellValue();
					}catch(Exception e){
						try{
							rewardNum = Integer.parseInt(row.getCell(nextindex).getStringCellValue());
						}catch(Exception e2){
							e2.printStackTrace();
						}
					}
					String starttime = row.getCell(index++).getStringCellValue();
					String endtime = row.getCell(index++).getStringCellValue();
					
					long startTime = TimeTool.formatter.varChar19.parse(starttime);
					long endTime = TimeTool.formatter.varChar19.parse(endtime);
					
					String[] costArticleNameArr = null;
					String[] costArticleCNNameArr = null;
					Integer[] costArticleColorArr = null;
					Integer[] costArticleNumArr = null;
					if (costArticleCNNames != null && !"".equals(costArticleCNNames)) {
						costArticleNameArr = StringTool.string2Array(costArticleNames, ",", String.class);
						costArticleCNNameArr = StringTool.string2Array(costArticleCNNames, ",", String.class);
						costArticleColorArr = StringTool.string2Array(costArticleColors, ",", Integer.class);
						costArticleNumArr = StringTool.string2Array(costArticleNums, ",", Integer.class);
						if (costArticleNameArr.length != costArticleCNNameArr.length || costArticleNumArr.length != costArticleCNNameArr.length || costArticleCNNameArr.length != costArticleColorArr.length) {
							System.out.println("[exchangeActivity.xls4配置错误]");
							throw new IllegalStateException("[exchangeActivity.xls4] [配置错误] [消耗物品名:" + costArticleNames + "] [消耗物品统计名:" + costArticleCNNames + "] [消耗物品颜色:" + costArticleColors + "] [消耗物品数量:" + costArticleNums + "]");
						}

					} else {
						throw new IllegalStateException("[exchangeActivity.xls4] [未配置] [消耗物品名:" + costArticleNames + "] [消耗物品统计名:" + costArticleCNNames + "] [消耗物品颜色:" + costArticleColors + "] [消耗物品数量:" + costArticleNums + "]");
					}
					String[] gainArticleNameArr = null;
					String[] gainArticleCNNameArr = null;
					Integer[] gainArticleColorArr = null;
					Integer[] gainArticleNumArr = null;
					Boolean[] gainArticleBindArr = null;
					if (gainArticleCNNames != null && !"".equals(gainArticleCNNames)) {
						gainArticleNameArr = StringTool.string2Array(gainArticleNames, ",", String.class);
						gainArticleCNNameArr = StringTool.string2Array(gainArticleCNNames, ",", String.class);
						gainArticleColorArr = StringTool.string2Array(gainArticleColors, ",", Integer.class);
						gainArticleNumArr = StringTool.string2Array(gainArticleNums, ",", Integer.class);
						if (gainArticleBinds != null && gainArticleBinds.trim().length() > 0) {
							String strs[] = gainArticleBinds.split(",");
							Boolean bbs[] = new Boolean[strs.length];
							for (int j = 0, len = strs.length; j < len; j++) {
								if (strs[j].equals("true")) {
									bbs[j] = true;
								} else {
									bbs[j] = false;
								}
							}
							gainArticleBindArr = bbs;
						}
						if (gainArticleNameArr.length != gainArticleCNNameArr.length || gainArticleCNNameArr.length != gainArticleNumArr.length || gainArticleCNNameArr.length != gainArticleColorArr.length || gainArticleCNNameArr.length != gainArticleBindArr.length) {
							System.out.println("[exchangeActivity.xls配置错误]");
							throw new IllegalStateException("[exchangeActivity.xls4] [配置错误] [获得物品名:" + gainArticleNames + "] [获得物品统计名:" + gainArticleCNNames + "] [获得物品颜色:" + gainArticleColors + "] [获得物品数量:" + gainArticleNums + "] [获得物品是否绑定:" + gainArticleBinds + "]");
						}
					} else {
						throw new IllegalStateException("[exchangeActivity.xls4] [未配置] [获得物品名:" + gainArticleNames + "] [获得物品统计名:" + gainArticleCNNames + "] [获得物品颜色:" + gainArticleColors + "] [获得物品数量:" + gainArticleNums + "] [获得物品是否绑定:" + gainArticleBinds + "]");
					}
					if(gainArticleNameArr==null || gainArticleColorArr==null ||gainArticleNumArr==null || gainArticleBindArr==null){
						throw new Exception("exchangeActivity.xls 奖励物品配置有空"+(gainArticleNameArr==null)+","+(gainArticleColorArr==null)+","+(gainArticleNumArr==null)+","+(gainArticleBindArr==null));
					}
					if(gainArticleNameArr.length!=gainArticleColorArr.length && gainArticleNameArr.length!=gainArticleColorArr.length && gainArticleNameArr.length!=gainArticleBindArr.length){
						throw new Exception("exchangeActivity.xls 奖励物品数量不匹配"+gainArticleNameArr.length+","+gainArticleColorArr.length+","+gainArticleNameArr.length+","+gainArticleBindArr.length);
					}
					
					ExchangeActivity eal = new ExchangeActivity(activityName,menuName, costArticleNameArr, costArticleCNNameArr, costArticleColorArr, costArticleNumArr, gainArticleNameArr, gainArticleCNNameArr, gainArticleColorArr, gainArticleNumArr, gainArticleBindArr, "", "");
					eal.setRewardType(rewardType);
					eal.setRewardNum(rewardNum);
					eal.setStartTime(startTime);
					eal.setEndTime(endTime);
					activities.add(eal);
					ActivitySubSystem.logger.warn("[加载配方兑换活动] [已经加载的] [总数据：" + menuMap.size() + "] [行：" + i + "] [activityName:" + activityName == null ? "" : activityName + "] [menuName:" + menuName == null ? "" : menuName + "]");
				}
			}
			
			sheet = hssfWorkbook.getSheetAt(5);
			if (sheet == null) return;
			int rows5 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows5; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platForms = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServers = "";
					if (cell != null) {
						openServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServers = "";
					if (cell != null) {
						limitServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String timeHours = cell.getStringCellValue().trim();
					cell = row.getCell(index++);
					String timeMinits = cell.getStringCellValue().trim();
					Double rate=row.getCell(index++).getNumericCellValue();
					
					List<DayilyTimeDistance> times = new ArrayList<DayilyTimeDistance>();
					String[] tempP1 = timeHours.split("\\|");
					String[] tempP2 = timeMinits.split("\\|");
					if (tempP1.length != tempP2.length) {
						throw new Exception("小时与分钟配表不匹配！");
					}
					for (int j = 0; j < tempP1.length; j++) {
						int[] point1 = RefreshSpriteManager.parse2Int(tempP1[j].split(","));
						int[] point2 = RefreshSpriteManager.parse2Int(tempP2[j].split(","));
						if ((point1.length != 2) || (point2.length != 2)) {
							throw new Exception("小时或者分钟开启结束时间不匹配！");
						}
						times.add(new DayilyTimeDistance(point1[0], point2[0], point1[1], point2[1]));
					}
					
					OutputActivity oa = new OutputActivity(startTime, endTime, platForms, openServers, limitServers);
					oa.setOtherVar(rate, times);
					outputActivities.add(oa);
					AllActivityManager.instance.add2AllActMap(AllActivityManager.caveActivity, oa);
				}
			}
			
			sheet = hssfWorkbook.getSheetAt(6);
			if (sheet == null) return;
			int rows6 = sheet.getPhysicalNumberOfRows();
			for (int i = 1; i < rows6; i++) {
				HSSFRow row = sheet.getRow(i);
				if (row != null) {
					int index = 0;
					HSSFCell cell = row.getCell(index++);
					String startTime = cell.getStringCellValue();
					String endTime = row.getCell(index++).getStringCellValue();
					String platForms = row.getCell(index++).getStringCellValue();
					cell = row.getCell(index++);
					String openServers = "";
					if (cell != null) {
						openServers = cell.getStringCellValue();
					}
					cell = row.getCell(index++);
					String limitServers = "";
					if (cell != null) {
						limitServers = cell.getStringCellValue();
					}
					Double rate=row.getCell(index++).getNumericCellValue();
					MaintanceActivity ma = new MaintanceActivity(startTime, endTime, platForms, openServers, limitServers);
					ma.setRate(rate);
					maintanceActivities.add(ma);
					AllActivityManager.instance.add2AllActMap(AllActivityManager.caveActivity, ma);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) {
		ExchangeActivityManager eam=new ExchangeActivityManager();
		eam.setFilePath("E:\\code\\game_mieshi_server\\conf\\game_init_config\\activity\\exchangeActivity.xls");
		try {
			eam.loadXml();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getAllExtraAwardTaskNames() {
		String[] taskNames = null;
		List<String> taskNameList = new LinkedList<String>();
		for (ExtraAwardActivity eaa : extraAwaList) {
			if (eaa.getType().equals("group")) {
				String[] groupName = eaa.getName();
				if (groupName != null) {
					for (String group : groupName) {
						List<Task> tasks = TaskManager.getInstance().getTaskCollectionsByName(group);
						if (tasks != null) {
							for (Task task : tasks) {
								ActivitySubSystem.logger.warn("任务额外奖励活动] [获取任务名:" + task.getName() + "]");
								taskNameList.add(task.getName());
							}
						}
					}
				}
			} else if (eaa.getType().equals("task")) {
				for (String name : eaa.getName()) {
					taskNameList.add(name);
				}
			}
			ActivitySubSystem.logger.warn("任务额外奖励活动] [任务数量:" + taskNameList.size() + "]");
			taskNames = new String[taskNameList.size()];
			for (int i = 0; i < taskNameList.size(); i++) {
				taskNames[i] = taskNameList.get(i);
			}
			ActivitySubSystem.logger.warn("任务额外奖励活动] [任务名:" + Arrays.toString(taskNames) + "]");
		}
		return taskNames;
	}

	public ExtraAwardActivity getRightExtraAwardActivity(String taskName) {
		for (ExtraAwardActivity eaa : extraAwaList) {
			if (eaa.getType().equals("group")) {
				String[] groupName = eaa.getName();
				if (groupName != null) {
					for (String group : groupName) {
						List<Task> tasks = TaskManager.getInstance().getTaskCollectionsByName(group);
						if (tasks != null) {
							for (Task task : tasks) {
								if (task.getName().equals(taskName)) {
									if (eaa.isThisServerFit() == null) {
										ActivitySubSystem.logger.warn("任务额外奖励活动] [" + taskName + "] [找到对应的额外奖励]");
										return eaa;
									}
								}
							}
						}
					}
				}
			} else if (eaa.getType().equals("task")) {
				for (String name : eaa.getName()) {
					if (name.equals(taskName)) {
						if (eaa.isThisServerFit() == null) {
							ActivitySubSystem.logger.warn("任务额外奖励活动] [" + taskName + "] [找到对应的额外奖励]");
							return eaa;
						}
					}
				}
			}
		}
		ActivitySubSystem.logger.warn("任务额外奖励活动] [" + taskName + "] [未找到对应的额外奖励]");
		return null;
	}
	
	public OutputActivity getCurrOutputActivity(Calendar ca){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS"); 
		for(OutputActivity oa:outputActivities){
			if(oa.getOutputActivity(ca).getBooleanValue()){
				if(ActivitySubSystem.logger.isWarnEnabled()){
					ActivitySubSystem.logger.warn("固定时间庄园种植收益加倍] [种植时间:" + sdf.format(ca.getTimeInMillis()) + "] [倍数:"+oa.getOutputActivity(ca).getDoubleValue()+"]");
				}
				return oa;
			}
		}
		if(ActivitySubSystem.logger.isWarnEnabled()){
			ActivitySubSystem.logger.warn("固定时间庄园种植收益加倍] [种植时间:" + sdf.format(ca.getTimeInMillis()) + "] [无加倍收益]");
		}
		return null;
	}

	public void init() throws Exception {
		
		long now = System.currentTimeMillis();
		instance = this;
		loadXml();
		ServiceStartRecord.startLog(this);
	}

	public Map<String, List<ExchangeActivity>> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<String, List<ExchangeActivity>> menuMap) {
		this.menuMap = menuMap;
	}

	public List<ExchangeActivityLimit> getEaLimits() {
		return eaLimits;
	}

	public void setEaLimits(List<ExchangeActivityLimit> eaLimits) {
		this.eaLimits = eaLimits;
	}

	public List<ExtraAwardActivity> getExtraAwaList() {
		return extraAwaList;
	}

	public void setExtraAwaList(List<ExtraAwardActivity> extraAwaList) {
		this.extraAwaList = extraAwaList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<OutputActivity> getOutputActivities() {
		return outputActivities;
	}

	public void setOutputActivities(List<OutputActivity> outputActivities) {
		this.outputActivities = outputActivities;
	}

	public List<MaintanceActivity> getMaintanceActivities() {
		return maintanceActivities;
	}

	public void setMaintanceActivities(List<MaintanceActivity> maintanceActivities) {
		this.maintanceActivities = maintanceActivities;
	}
	
}