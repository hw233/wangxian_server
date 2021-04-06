package com.fy.engineserver.activity.newserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.fy.engineserver.activity.ServerActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.boss.game.GameConstants;

public class NewServerActivityManager {

	private static NewServerActivityManager instance;

	private GameConstants constants;// 需要注入

	private LinkedHashMap<String, ServerActivity> newServerActivityMap = new LinkedHashMap<String, ServerActivity>();
	private LinkedHashMap<String, ServerActivity> ServerActivityMap = new LinkedHashMap<String, ServerActivity>();

	private List<NewServer> newServers = new ArrayList<NewServer>();
	private String filePath;

	public void putIntoMap(ServerActivity serverActivity) {
		newServerActivityMap.put(serverActivity.getServerName(), serverActivity);
	}

	public void putIntoActivityMap(ServerActivity serverActivity) {
		ServerActivityMap.put(serverActivity.getServerName(), serverActivity);
	}

	public static NewServerActivityManager getInstance() {
		return instance;
	}

	public GameConstants getConstants() {
		return constants;
	}

	public void setConstants(GameConstants constants) {
		this.constants = constants;
	}

	public LinkedHashMap<String, ServerActivity> getNewServerActivityMap() {
		return newServerActivityMap;
	}

	public void setNewServerActivityMap(LinkedHashMap<String, ServerActivity> newServerActivityMap) {
		this.newServerActivityMap = newServerActivityMap;
	}

	public LinkedHashMap<String, ServerActivity> getServerActivityMap() {
		return ServerActivityMap;
	}

	public void setServerActivityMap(LinkedHashMap<String, ServerActivity> serverActivityMap) {
		ServerActivityMap = serverActivityMap;
	}

	public List<NewServer> getNewServers() {
		return newServers;
	}

	public void setNewServers(List<NewServer> newServers) {
		this.newServers = newServers;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private void load() throws Exception {
		File file = new File(getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		if (!file.exists()) {
			throw new Exception("配置文件不存在");
		}
		try {
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet0 = workbook.getSheet(0);
			for (int i = 1; i < sheet0.getRows(); i++) {
				Cell[] cells = sheet0.getRow(i);
				int index = 0;
				String platformName = (StringTool.modifyContent(cells[index++]));
				String serverName = (StringTool.modifyContent(cells[index++]));
				String startTime = (StringTool.modifyContent(cells[index++]));
				String endTime = (StringTool.modifyContent(cells[index++]));
				NewServer ns = new NewServer(platformName, serverName, startTime, endTime);
				newServers.add(ns);
			}
			Game.logger.warn("[系统初始化] [新服等级礼包]");
		} catch (Exception e) {
			throw e;
		}
	}

	// 倒着加.新服在最上面
	public void initActivity() {
		HashMap<Integer, String> spPrize = new HashMap<Integer, String>();
		spPrize.put(20, "飘渺仙匣");
		spPrize.put(30, "飘渺宝匣");
		
		HashMap<Integer, String> spPrizeTencent = new HashMap<Integer, String>();
		spPrizeTencent.put(20, "飘渺神匣");
		spPrizeTencent.put(30, "飘渺宝匣");

		HashMap<Integer, String> spPrizeTW = new HashMap<Integer, String>();
		spPrizeTW.put(10, Translate.炼狱神匣);
		spPrizeTW.put(20, Translate.炼狱宝匣);

		HashMap<Integer, String> spPrizeKorea = new HashMap<Integer, String>();
		spPrizeKorea.put(10, Translate.炼狱神匣);
		spPrizeKorea.put(20, Translate.炼狱宝匣);

		{
			// // 韩国 内测
			// HashMap<Integer, String> koreaPrize = new HashMap<Integer, String>();
			// koreaPrize.put(20, "행화촌(FGT)");
			// koreaPrize.put(30, "행화촌(FGT)");
			// koreaPrize.put(40, "행화촌(FGT)");
			// koreaPrize.put(50, "행화촌(FGT)");
			// koreaPrize.put(25, "25레벨 도마첩(FGT)");
			// koreaPrize.put(45, "45레벨 도마첩(FGT)");
			// koreaPrize.put(65, "65레벨 도마첩(FGT)");
			// putIntoMap(new ServerActivity(Platform.韩国, "ST", "2013-02-17 00:00:00", "2013-05-27 00:00:00", koreaPrize));

		}
		{
			/* 礼包 */
			HashMap<Integer, List<String>> spPrizeNoticeKorea = new HashMap<Integer, List<String>>();
			List<String> libao1 = new ArrayList<String>();
			libao1.add("고급장비 주머니");// 靓装宝囊一
			List<String> libao2 = new ArrayList<String>();
			libao2.add("탈 것 주머니");// 迅捷宝囊
			List<String> libao3 = new ArrayList<String>();
			libao3.add("최고급장비 주머니");// 靓装礼包二
			List<String> libao4 = new ArrayList<String>();
			libao4.add("도마첩 주머니");// 飞升宝囊-帖
			List<String> libao5 = new ArrayList<String>();
			libao5.add("술표 주머니");// 飞升宝囊-酒
			List<String> libao6 = new ArrayList<String>();
			libao6.add("애완동물 주머니");// 梦宠宝囊
			spPrizeNoticeKorea.put(5, libao1);
			spPrizeNoticeKorea.put(15, libao2);
			spPrizeNoticeKorea.put(20, libao3);
			spPrizeNoticeKorea.put(25, libao4);
			spPrizeNoticeKorea.put(30, libao5);
			spPrizeNoticeKorea.put(35, libao6);

			/* 公告 */
			HashMap<Integer, String> spNoticeKorea = new HashMap<Integer, String>();
			spNoticeKorea.put(5, "“고급장비 주머니”획득! 다음 선물은 15레벨에 지급!");
			spNoticeKorea.put(15, "“탈 것 주머니”획득! 다음 선물은 20레벨에 지급!");
			spNoticeKorea.put(20, "“최고급장비 주머니”획득! 다음 선물은 25레벨에 지급!");
			spNoticeKorea.put(25, "“도마첩 주머니”획득! 다음 선물은 30레벨에 지급!");
			spNoticeKorea.put(30, "“술표 주머니”획득! 다음 선물은 35레벨에 지급!");
			spNoticeKorea.put(35, "“애완동물 주머니”획득! 더욱 즐거운 수행길 되세요!");

			putIntoActivityMap(new ServerActivity(Platform.韩国, "ST", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S1-원시천존", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S2-태상노군", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S3-영보천존", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S4-옥황상제", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S5-서왕모", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S6-현천상제", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S7-삼관대제", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S8-북두성군", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
			putIntoActivityMap(new ServerActivity(Platform.韩国, "S9-천선낭랑", "2013-02-17 00:00:00", "2013-07-10 23:59:59", spPrizeNoticeKorea, spNoticeKorea));
		}
		for (NewServer ns : newServers) {
			if (Platform.官方.getPlatformName().equals(ns.getPlatformName())) {
				putIntoMap(new ServerActivity(Platform.官方, ns.getServerName(), ns.getStartTime(), ns.getEndTime(), spPrize));
			}
			if (Platform.腾讯.getPlatformName().equals(ns.getPlatformName())) {
				putIntoMap(new ServerActivity(Platform.腾讯, ns.getServerName(), ns.getStartTime(), ns.getEndTime(), spPrizeTencent));
			}
			if (Platform.台湾.getPlatformName().equals(ns.getPlatformName())) {
				putIntoMap(new ServerActivity(Platform.台湾, ns.getServerName(), ns.getStartTime(), ns.getEndTime(), spPrizeTW));
			}
			if (Platform.韩国.getPlatformName().equals(ns.getPlatformName())) {
				putIntoMap(new ServerActivity(Platform.韩国, ns.getServerName(), ns.getStartTime(), ns.getEndTime(), spPrizeKorea));
			}
			if (Platform.马来.getPlatformName().equals(ns.getPlatformName())) {
				putIntoMap(new ServerActivity(Platform.马来, ns.getServerName(), ns.getStartTime(), ns.getEndTime(), spPrize));
			}
		}
	}

	/**
	 * 返回该服务器在该时间段是否参与新服活动
	 * @return
	 */
	public CompoundReturn inNewserverActivity() {
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		if (constants == null) {
			return cr.setBooleanValue(false);
		}
		if (!newServerActivityMap.containsKey(constants.getServerName())) {
			return cr.setBooleanValue(false);
		}
		ServerActivity serverActivity = newServerActivityMap.get(constants.getServerName());
		if (!serverActivity.getPlatform().getPlatformName().equalsIgnoreCase(constants.getPlatForm())) {
			return cr.setBooleanValue(false);
		}
		long now = SystemTime.currentTimeMillis();
		return cr.setBooleanValue(serverActivity.getStartTime() <= now && now <= serverActivity.getEndTime()).setObjValue(serverActivity.getLevelPrizeMap());
	}

	/**
	 * 返回该服务器在该时间段是否参与活动
	 * @return
	 */
	public CompoundReturn inServerActivity() {
		CompoundReturn cr = CompoundReturn.createCompoundReturn();
		if (constants == null) {
			return cr.setBooleanValue(false);
		}
		if (!ServerActivityMap.containsKey(constants.getServerName())) {
			return cr.setBooleanValue(false);
		}
		ServerActivity serverActivity = ServerActivityMap.get(constants.getServerName());
		if (!serverActivity.getPlatform().getPlatformName().equalsIgnoreCase(constants.getPlatForm())) {
			return cr.setBooleanValue(false);
		}
		long now = SystemTime.currentTimeMillis();
		return cr.setBooleanValue(serverActivity.getStartTime() <= now && now <= serverActivity.getEndTime()).setObjVlues(new Object[] { serverActivity.getLevelNoticeMap(), serverActivity.getNoticeMap() });
	}

	public void init() throws Exception {
		
		long start = System.currentTimeMillis();
		load();
		initActivity();
		instance = this;
		ServiceStartRecord.startLog(this);
	}

}

class NewServer {
	private String platformName;
	private String serverName;
	private String startTime;
	private String endTime;

	public NewServer() {
	}

	public NewServer(String platformName, String serverName, String startTime, String endTime) {
		this.platformName = platformName;
		this.serverName = serverName;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}