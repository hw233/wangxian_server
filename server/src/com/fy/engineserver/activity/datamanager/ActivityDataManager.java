package com.fy.engineserver.activity.datamanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.datamanager.module.ActivityModule;
import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.activity.peoplesearch.PeopleSearchManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ExcelDataLoadUtil;

/**
 * 新活动界面管理
 */
public class ActivityDataManager {
	
	public static Logger logger = ActivitySubSystem.logger;
	
	private static ActivityDataManager inst;
	private String fileName;
	/** 所有归此类管理的活动列表 */
	public List<AbstractActivity> allActivitys = new ArrayList<AbstractActivity>();
	/** 活动静态配制信息 */
	public Map<String, ActivityModule> activityModules = new HashMap<String, ActivityModule>();
	
	 public List<String> errorArticle = new ArrayList<String>();
	
	public static ActivityDataManager getInst() {
		return inst;
	}
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		inst = this;
		allActivitys.add(PeopleSearchManager.getInstance());
		allActivitys.add(DevilSquareManager.instance);
//		allActivitys.add(DisasterManager.getInst());
//		allActivitys.add(SiFangManager.getInstance());
		allActivitys.add(ChestFightManager.inst);
		allActivitys.add(DiceManager.getInstance());
		allActivitys.add(SeptStationMapTemplet.getInstance());
		File file = new File(fileName);
		activityModules = (Map<String, ActivityModule>) ExcelDataLoadUtil.loadExcelData(file, 0, ActivityModule.class, logger);
		if (errorArticle.size() > 0) {
			throw new Exception("[配置物品不存在] [" + errorArticle + "] [filename:" + file.getName() + "]");
		}
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		ActivityDataManager m = new ActivityDataManager();
		File file = new File("E://javacode2//hg1-clone//game_mieshi_server//conf//game_init_config//activity//activityShowInfo.xls");
		m.activityModules = (Map<String, ActivityModule>) ExcelDataLoadUtil.loadExcelData(file, 0, ActivityModule.class, logger);
		for (ActivityModule mm : m.activityModules.values()) {
			System.out.println(mm);
		}
	}
	/**
	 * 获取所有当天开启的活动
	 * @param player
	 * @return
	 */
	public List<ActivityModule> getAllOpenActivitys(Player player) {
		long now = System.currentTimeMillis();
		List<ActivityModule> list = new ArrayList<ActivityModule>();
		for (AbstractActivity a : allActivitys) {
			List<String> openDes = a.getActivityOpenTime(now);
			if (openDes != null && openDes.size() > 0) {
				StringBuffer str = new StringBuffer();
				for (int i=0; i<openDes.size(); i++) {
					str.append(openDes.get(i));
					if (i % 3 == 2) {
						str.append("\n");
					}
					
					if (i < (openDes.size()-1)) {
						str.append("，");
					}
				}
				ActivityModule module = activityModules.get(a.getActivityName());
				if (player.getLevel() < module.getLevellimit()) {
					continue;
				}
				module.setInActivityTime(a.isActivityTime(now));
				module.buildData(str.toString());
				list.add(module);
			}
		}
		return list;
	}
	
	public static String getMintisStr(int c) {
		if (c < 10) {
			return "0" + c;
		}
		return c+"";
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
