package com.fy.engineserver.activity.newChongZhiActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeRatio;
import com.fy.engineserver.message.GET_RMBREWARD_REQ;
import com.fy.engineserver.message.GET_RMBREWARD_RES;
import com.fy.engineserver.message.GET_RMB_REWARDMSG_REQ;
import com.fy.engineserver.message.GET_RMB_REWARDMSG_RES;
import com.fy.engineserver.message.GET_WEEKACTIVITY_REQ;
import com.fy.engineserver.message.GET_WEEKACTIVITY_RES;
import com.fy.engineserver.message.GET_WEEKANDMONTH_INFO_REQ;
import com.fy.engineserver.message.GET_WEEKANDMONTH_INFO_RES;
import com.fy.engineserver.message.GET_WEEKMONTH_REWARD_REQ;
import com.fy.engineserver.message.GET_WEEKMONTH_REWARD_RES;
import com.fy.engineserver.message.GET_WEEK_REWARD_REQ;
import com.fy.engineserver.message.GET_WEEK_REWARD_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.WEEKACTIVITY_STATE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.config.ConfigServiceManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class NewChongZhiActivityManager implements Runnable {
	
	public static Logger logger = LoggerFactory.getLogger(NewChongZhiActivityManager.class);
	
	public static NewChongZhiActivityManager instance;
	
	public static boolean runable = true;
	public static int runSleepTime = 1000 * 5;
	
	private String excelPath;						//配置文件excel的路径
	
	private String diskCatchPath;					//diskCatch路径
	private static DiskCache diskCache;					//diskCatch
	
	public ArrayList<NewMoneyActivity> chongZhiActivitys = new ArrayList<NewMoneyActivity>();	//充值活动集合

	public ArrayList<NewXiaoFeiActivity> xiaoFeiAcitivitys = new ArrayList<NewXiaoFeiActivity>();	//消费活动集合
	
	public static int RMB_PLAYER_LEVEL = 10;
	public ArrayList<RmbRewardData> allRmbDatas = new ArrayList<RmbRewardData>();					//全部的长期活动
	public RmbRewardData shouchongRmbData;															//首充
	public ArrayList<RmbRewardData> rmbDatas = new ArrayList<RmbRewardData>();				//累计充值活动的有序队列，按needmoney排序
	
	public static ArrayList<WeekAndMonthChongZhiActivity> weekMonthDatas = new ArrayList<WeekAndMonthChongZhiActivity>();
	
	public List<XiaoFeiGiveJiFenActivity> jiFenActivitys = new ArrayList<XiaoFeiGiveJiFenActivity>();
	
	public ArrayList<WeekChongZhiActivity> weekCZs = new ArrayList<WeekChongZhiActivity>();
	
	public void init() throws SameIDException, Exception {
		
		instance = this;
		
		diskCache = new DefaultDiskCache(new File(getDiskCatchPath()), "", 1000L * 60 * 60 * 24 * 365);
		NewMoneyActivity.diskcache = diskCache;
		NewMoneyActivity.logger = logger;
		RmbRewardData.diskcache = diskCache;
		RmbRewardData.logger = logger;
		WeekChongZhiActivity.diskcache = diskCache;
		WeekChongZhiActivity.logger = logger;
		
		WeekAndMonthChongZhiActivity.diskcache = diskCache;
		WeekAndMonthChongZhiActivity.logger = logger;
		
		loadFile();
		
		loadChangQiFile();
		
		loadXiaoFeiFile();
		
		loadWeekMonthFile();
		
		LoadWeekChongZhiActivity();
		
		
		findSelfChangQiActivity();
		
		new Thread(this).start();
		ServiceStartRecord.startLog(this);
	}
	
	public void loadFile() throws SameIDException {
		int i = 1;
		try{
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("新充值活动配置不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//充值活动
			Sheet sheet = workbook.getSheet(0);
			int maxRow = sheet.getRows();
			for (; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int activityID = -1;
				String activityName = "";
				int activityType = -1;
				int platform = -1;
				String[] serverName = null;
				String[] unServerName = null;
				String startTime = "";
				String endTime = "";
				String mailTile = "";
				String mailMsg = "";
				String[] parameters = new String[]{"","","","",""};
				for (int j = 0; j < cells.length; j++) {
					switch(j) {
					case 0:
						activityID = Integer.parseInt(cells[j].getContents());
						break;
					case 1:
						activityName = cells[j].getContents();
						break;
					case 2:
						activityType = Integer.parseInt(cells[j].getContents());
						break;
					case 3:
						platform = Integer.parseInt(cells[j].getContents());
						break;
					case 4:
						serverName = cells[j].getContents().split(",");
						break;
					case 5:
						unServerName = cells[j].getContents().split(",");
						break;
					case 6:
						startTime = cells[j].getContents();
						break;
					case 7:
						endTime = cells[j].getContents();
						break;
					case 8:
						mailTile = cells[j].getContents();
						break;
					case 9:
						mailMsg = cells[j].getContents();
						break;
					case 10:
						parameters[0] = cells[j].getContents();
						break;
					case 11:
						parameters[1] = cells[j].getContents();
						break;
					case 12:
						parameters[2] = cells[j].getContents();
						break;
					case 13:
						parameters[3] = cells[j].getContents();
						break;
					case 14:
						parameters[4] = cells[j].getContents();
						break;
					}
				}
				NewMoneyActivity ac = null;
				if (activityType == 0) {
					//首充，邮件发送奖励
					ac = new FirstChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 1) {
					//累计，邮件发送奖励
					ac = new TotalChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 4) {
					//单笔充值
					ac = new SingleChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 5) {
					//充值后，长时间返利
					ac = new FanLi4LongTimeChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 6) {
					//充值排行榜
					ac = new BillBoardActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 7) {
					//充值马上返利的活动
					ac = new FanLiTimelyActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 8) {
					//首充 马上返利
					ac = new FirstChongZhiFanLiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 9) {
					//累计充值多次奖励
					ac = new TotalTimesChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 10) {
					//单笔充值 余额要奖励其他
					ac = new SingleBalanceChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 11) {
					ac = new ShowShopChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 12) {
					ac = new SingleChongZhiSingleActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 13) {
					ac = new FanArticle4LongTimeChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 14) {
					ac = new FirstFanArticle4LongTimeChongZhiActivity(activityID, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}
				
				if (ac != null) {
					for (NewMoneyActivity x : chongZhiActivitys) {
						if (x.getConfigID() == ac.getConfigID()) {
							throw new SameIDException("充值活动ID重复" + ac.getConfigID());
						}
					}
					ac.creatParameter(parameters);
					ac.loadDiskCache();
					chongZhiActivitys.add(ac);
				}
			}
		}catch(Exception e) {
			logger.error("新充值活动配置读取出错["+i+"]", e);
		}
	}
	
	public void loadChangQiFile() {
		try {
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("新充值活动配置不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//充值活动
			Sheet sheet = workbook.getSheet(1);
			int maxRow = sheet.getRows();
			for (int i = 1; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int id = -1;
				int type = -1;
				long needMoney = 0;
				int platform = -1;					//平台			0是官方    1是台湾    2是腾讯  3是马来
				String[] serverNames = null;			//服务器名字
				String[] unServerNames = null;			//不参加服务器名字
				String[] rewardPropNames = null;
				String mailTitle = "";
				String mailMsg = "";
				String notifyMsg = "";
				int[] rewardPropNums = null;
				int[] rewardColors = null;
				boolean[] rewardBinds = null;
				boolean[] rewardRare = null;
				int showType = 0;
				long showMoney = 0;
				for (int j = 0; j < cells.length; j++) {
					switch(j) {
					case 0:
						id = Integer.parseInt(cells[j].getContents());
						break;
					case 1:
						String m = cells[j].getContents();
						m = m.replace(",", "");
						m = m.trim();
						needMoney = Long.parseLong(m);
						break;
					case 2:
						type = Integer.parseInt(cells[j].getContents());
					case 3:
						platform = Integer.parseInt(cells[j].getContents());
						break;
					case 4:
						String servers = cells[j].getContents();
						serverNames = servers.split(",");
						break;
					case 5:
						String unServers = cells[j].getContents();
						if (unServers == null) {
							unServers = "";
						}
						unServerNames = unServers.split(",");
						break;
					case 6:
						mailTitle = cells[j].getContents();
						break;
					case 7:
						mailMsg = cells[j].getContents();
						break;
					case 8:
						notifyMsg = cells[j].getContents();
						break;
					case 9:
						String propNames = cells[j].getContents();
						rewardPropNames = propNames.split(",");
						break;
					case 10:
						String propNum_str = cells[j].getContents();
						String[] propNums = propNum_str.split(",");
						rewardPropNums = new int[propNums.length];
						for (int k = 0; k < propNums.length; k++) {
							rewardPropNums[k] = Integer.parseInt(propNums[k]);
						}
						break;
					case 11:
						String rewardColor_str = cells[j].getContents();
						String[] rewardColorss = rewardColor_str.split(",");
						rewardColors = new int[rewardColorss.length];
						for (int k = 0; k < rewardColorss.length; k++) {
							rewardColors[k] = Integer.parseInt(rewardColorss[k]);
						}
						break;
					case 12:
						String rare_str = cells[j].getContents();
						String[] rares = rare_str.split(",");
						rewardRare = new boolean[rares.length];
						for (int k = 0; k < rares.length; k++) {
							rewardRare[k] = Boolean.parseBoolean(rares[k]);
						}
						break;
					case 13:
						String bind_str = cells[j].getContents();
						String[] binds = bind_str.split(",");
						rewardBinds = new boolean[binds.length];
						for (int k = 0; k < binds.length; k++) {
							rewardBinds[k] = Boolean.parseBoolean(binds[k]);
						}
						break;
					case 14:
						showType = Integer.parseInt(cells[j].getContents());
						break;
					case 15:
						showMoney = Long.parseLong(cells[j].getContents());
						break;
					}
				}
				RmbRewardData data = new RmbRewardData(id, needMoney, type, platform, serverNames, unServerNames, mailTitle, mailMsg, notifyMsg, rewardPropNames, rewardPropNums, rewardColors, rewardBinds, rewardRare, showType, showMoney);
				allRmbDatas.add(data);
			}
		}catch(Exception e) {
			logger.error("长期充值活动出错", e);
		}
	}
	
	public void loadXiaoFeiFile () throws SameIDException {
		int i = 1;
		try {
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("新消费活动配置不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//消费活动
			Sheet sheet = workbook.getSheet(2);
			int maxRow = sheet.getRows();
			for (; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int activityID = -1;
				String activityName = "";
				int activityType = -1;
				int[] xiaofeiType = null;
				int platform = -1;
				String[] serverName = null;
				String[] unServerName = null;
				String startTime = "";
				String endTime = "";
				String mailTile = "";
				String mailMsg = "";
				String[] parameters = new String[]{"","","","","",""};
				for (int j = 0; j < cells.length; j++) {
					switch(j) {
					case 0:
						activityID = Integer.parseInt(cells[j].getContents());
						break;
					case 1:
						activityName = cells[j].getContents();
						break;
					case 2:
						activityType = Integer.parseInt(cells[j].getContents());
						break;
					case 3:
						String[] ss = cells[j].getContents().split(",");
						xiaofeiType = new int[ss.length];
						for (int k = 0; k < ss.length; k++) {
							xiaofeiType[k] = Integer.parseInt(ss[k]);
						}
						break;
					case 4:
						platform = Integer.parseInt(cells[j].getContents());
						break;
					case 5:
						serverName = cells[j].getContents().split(",");
						break;
					case 6:
						unServerName = cells[j].getContents().split(",");
						break;
					case 7:
						startTime = cells[j].getContents();
						break;
					case 8:
						endTime = cells[j].getContents();
						break;
					case 9:
						mailTile = cells[j].getContents();
						break;
					case 10:
						mailMsg = cells[j].getContents();
						break;
					case 11:
						parameters[0] = cells[j].getContents();
						break;
					case 12:
						parameters[1] = cells[j].getContents();
						break;
					case 13:
						parameters[2] = cells[j].getContents();
						break;
					case 14:
						parameters[3] = cells[j].getContents();
						break;
					case 15:
						parameters[4] = cells[j].getContents();
						break;
					case 16:
						parameters[5] = cells[j].getContents();
						break;
					}
				}
				
				NewXiaoFeiActivity ac = null;
				if (activityType == 0) {
					ac = new FirstXiaoFeiActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 1) {
					ac = new SingleXiaoFeiActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 2) {
					ac = new TotalXiaoFeiActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 3) {
					ac = new TotalTimesXiaoFeiActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 4) {
					ac = new BillXiaoFeiBoardActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}else if (activityType == 5) {
					ac = new FanArticle4LongTimeXiaoFeiActivity(activityID, xiaofeiType, activityName, platform, startTime, endTime, serverName, unServerName, mailTile, mailMsg, parameters);
				}
				if (ac != null) {
					for (NewXiaoFeiActivity x : xiaoFeiAcitivitys) {
						if (x.getConfigID() == ac.getConfigID()) {
							throw new SameIDException("消费活动ID重复" + ac.getConfigID());
						}
					}
					ac.creatParameter(parameters);
					ac.loadDiskCache();
					xiaoFeiAcitivitys.add(ac);
				}
			}
		}catch(Exception e) {
			logger.error("消费活动出错["+i+"]", e);
		}
	}
	
	public void loadWeekMonthFile() throws SameIDException {
		int i = 1;
		try {
			weekMonthDatas.clear();
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("新消费活动配置不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//消费活动
			Sheet sheet = workbook.getSheet(3);
			int maxRow = sheet.getRows();
			for (; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int dataID = 0;
				int type = 0;
				int platform = -1;
				String[] serverNames = null;
				String[] unServerNames = null;
				long showRMBMoney = -1;
				long needMoney = 0;
				String startTime = "";
				String endTime = "";
				String mailTitle = "";
				String mailMsg = "";
				String[] rewardPropNames = null;
				int[] rewardPropNums = null;
				int[] rewardColors = null;
				boolean[] rewardBinds = null;
				boolean[] rewardRare = null;
				String[] buyPropNames = null;
				int[] buyPropNums = null;
				int[] buyColors = null;
				boolean[] buyBinds = null;
				boolean[] buyRare = null;
				long buyPrice = 0;
				for (int j = 0; j < cells.length; j++) {
					String s = cells[j].getContents();
					switch (j) {
					case 0:
						dataID = Integer.parseInt(s);
						break;
					case 1:
						showRMBMoney = Long.parseLong(s);
						break;
					case 2:
						needMoney = Long.parseLong(s);
						break;
					case 3:
						type = Integer.parseInt(s);
						break;
					case 4:
						platform = Integer.parseInt(s);
						break;
					case 5:
						serverNames = s.split(",");
						break;
					case 6:
						unServerNames = s.split(",");
						break;
					case 7:
						startTime = s;
						break;
					case 8:
						endTime = s;
						break;
					case 9:
						mailTitle = s;
						break;
					case 10:
						mailMsg = s;
						break;
					case 11:
						rewardPropNames = s.split(",");
						break;
					case 12:{
						String[] ss = s.split(",");
						rewardPropNums = new int[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							rewardPropNums[kk] = Integer.parseInt(ss[kk]);
						}
					}break;
					case 13:{
						String[] ss = s.split(",");
						rewardColors = new int[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							rewardColors[kk] = Integer.parseInt(ss[kk]);
						}
					}break;
					case 14:{
						String[] ss = s.split(",");
						rewardRare = new boolean[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							rewardRare[kk] = Boolean.parseBoolean(ss[kk]);
						}
					}break;
					case 15:{
						String[] ss = s.split(",");
						rewardBinds = new boolean[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							rewardBinds[kk] = Boolean.parseBoolean(ss[kk]);
						}
					}break;
					case 16:
						buyPropNames = s.split(",");
						break;
					case 17:{
						String[] ss = s.split(",");
						buyPropNums = new int[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							buyPropNums[kk] = Integer.parseInt(ss[kk]);
						}
					}break;
					case 18:{
						String[] ss = s.split(",");
						buyColors = new int[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							buyColors[kk] = Integer.parseInt(ss[kk]);
						}
					}break;
					case 19:{
						String[] ss = s.split(",");
						buyRare = new boolean[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							buyRare[kk] = Boolean.parseBoolean(ss[kk]);
						}
					}break;
					case 20:{
						String[] ss = s.split(",");
						buyBinds = new boolean[ss.length];
						for (int kk = 0; kk < ss.length; kk++) {
							buyBinds[kk] = Boolean.parseBoolean(ss[kk]);
						}
					}break;
					case 21:
						buyPrice = Long.parseLong(s);
						break;
					}
				}
				WeekAndMonthChongZhiActivity ac = new WeekAndMonthChongZhiActivity(dataID, type, platform, serverNames, unServerNames, showRMBMoney, needMoney, startTime, endTime, mailTitle, mailMsg, rewardPropNames, rewardPropNums, rewardColors, rewardBinds, rewardRare, buyPropNames, buyPropNums, buyColors, buyBinds, buyRare, buyPrice);
				logger.warn("["+i+"] ["+ac.getLogString()+"]");
				for (WeekAndMonthChongZhiActivity w : weekMonthDatas) {
					if (w.getDataID() == ac.getDataID()) {
						throw new SameIDException("周月活动ID相同:" + w.getDataID());
					}
				}
				weekMonthDatas.add(ac);
			}
		} catch (Exception e) {
			logger.error("周月活动出错["+i+"]", e);
		}
	}
	
	public void LoadWeekChongZhiActivity() throws Exception {
		weekCZs.clear();
		int maxRow = 0;
		int i = 1;
		int j = 0;
		try {
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("配置文件不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//周累计充值活动
			Sheet sheet = workbook.getSheet("周累充活动");
//			logger.warn("----   ["+Arrays.toString(workbook.getSheetNames())+"]");
			maxRow = sheet.getRows();
			for (; i < maxRow; i++) {
				Cell[][] cells = new Cell[10][];
				j = 0;
				for (; j < 10; j++) {
					cells[j] = sheet.getRow(i);
//					logger.warn("j=["+j+"]  ["+cells[j].length+"]");
					i++;
				}
				i--;
				WeekChongZhiActivity ac = new WeekChongZhiActivity(cells);
				weekCZs.add(ac);
			}
		}catch(Exception e) {
			logger.error("LoadWeekChongZhiActivity出错 i["+i+"] i["+j+"]  maxRow["+maxRow+"]:", e);
			throw new Exception("周累计充值活动出错"+e);
		}
	}
	
	public void loadXiaoFeiJiFenFile () throws Exception {
		int i = 1;
		try {
			File f = new File(getExcelPath());
			f = new File(ConfigServiceManager.getInstance().getFilePath(f));
			if (!f.exists()) {
				logger.error("新消费积分活动配置不存在");
				return;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			//消费活动
			Sheet sheet = workbook.getSheet(4);
			int maxRow = sheet.getRows();
			for (; i < maxRow; i++) {
				Cell[] cells = sheet.getRow(i);
				int xiaofeiType = -1;
				int platform = -1;
				String[] serverName = null;
				String[] unServerName = null;
				String startTime = "";
				String endTime = "";
				long xiaofeinums = -1;
				long givefeinums = -1;
				double jifennums = -1;
				
				for (int j = 0; j < cells.length; j++) {
					if(cells[j]!=null){
						switch(j) {
							case 4:
								platform = Integer.parseInt(cells[j].getContents());
								break;
							case 2:
								serverName = cells[j].getContents().split(",");
								break;
							case 3:
								unServerName = cells[j].getContents().split(",");
								break;
							case 0:
								startTime = cells[j].getContents();
								break;
							case 1:
								endTime = cells[j].getContents();
								break;
							case 5:
								xiaofeiType = Integer.parseInt(cells[j].getContents());
								break;	
							case 6:
								xiaofeinums = Long.parseLong(cells[j].getContents());
								break;	
							case 7:
								givefeinums = Long.parseLong(cells[j].getContents());
								break;	
							case 8:
								jifennums = Double.parseDouble(cells[j].getContents());
								break;	
						}
					}
				}
				XiaoFeiGiveJiFenActivity xfactivity = new XiaoFeiGiveJiFenActivity(xiaofeiType, platform, serverName, unServerName, startTime, endTime, xiaofeinums, jifennums,givefeinums);
				jiFenActivitys.add(xfactivity);
				logger.warn("[消费积分活动加载] [加载第"+i+"个] ["+xfactivity+"]");
			}
			logger.warn("[消费积分活动加载] [完成] [maxRow:"+maxRow+"] [jiFenActivitys:"+jiFenActivitys.size()+"]");
		}catch(Exception e) {
			logger.error("消费积分活动出错["+i+"]", e);
			throw new Exception("消费积分活动出错"+e);
		}
	}
	
	/**
	 * 找自己的长期累积充值活动
	 * 
	 */
	public void findSelfChangQiActivity () {
		shouchongRmbData = null;
		ArrayList<RmbRewardData> leijis = new ArrayList<RmbRewardData>();
		for (int i = 0 ; i < allRmbDatas.size(); i++) {
			RmbRewardData data = allRmbDatas.get(i);
			if (data.getType() == 0) {
				if (data.isCanServer()) {
					shouchongRmbData = data;
				}
			}else if (data.getType() == 1) {
				if (data.isCanServer()) {
					int index = -1;
					for (int j = 0; j < leijis.size(); j++) {
						RmbRewardData o = leijis.get(j);
						if (o.getNeedMoney() > data.getNeedMoney()) {
							index = j;
							break;
						}
					}
					if (index >= 0) {
						leijis.add(index, data);
					}else {
						leijis.add(data);
					}
				}
			}
		}
		rmbDatas = leijis;
		if (shouchongRmbData != null) {
			logger.warn("[首充---] [{}]", new Object[]{shouchongRmbData.getLogString()});
		}
		for (int i = 0 ; i < rmbDatas.size(); i++) {
			logger.warn("[累计---] [{}]", new Object[]{rmbDatas.get(i).getLogString()});
		}
	}
	public HashSet<String> firstChargeMess = new HashSet<String>();
	public boolean canFirstCharge(Player p,String chargeId){
		initFirstChargeData();
		if (firstChargeMess.contains(p.getId() + chargeId)) {
			return false;
		}
		return true;
	}
	
	public void doFirstCharge(Player p,String chargeId){
		initFirstChargeData();
		firstChargeMess.add(p.getId() + chargeId);
		diskCache.put("firstChargeOfChargeId", firstChargeMess);
		logger.warn("[首充活动] [增加首充数据] [chargeId:{}] [{}] [{}]", new Object[]{chargeId,p.getLogString(), firstChargeMess.size()});
	}
	
	public void cleanFirstChargeData() {
		initFirstChargeData();
		if (firstChargeMess.size() > 0) {
			logger.warn("[首充活动] [清空首充数据] [{}]", new Object[]{firstChargeMess.size()});
			firstChargeMess.clear();
			diskCache.put("firstChargeOfChargeId", firstChargeMess);
		}
	}
	
	public void initFirstChargeData() {
		try{
			if(firstChargeMess == null || firstChargeMess.size() == 0){
				Object obj = diskCache.get("firstChargeOfChargeId");
				if (obj == null) {
					HashSet<String> firstChargeMess2 = new HashSet<String>();
					diskCache.put("firstChargeOfChargeId", firstChargeMess2);
				}else {
					firstChargeMess = (HashSet<String>)obj;
				}
				logger.warn("[首充活动] [加载首充数据] [成功] [{}]", new Object[]{firstChargeMess.size()});
			}
		}catch(Exception e) {
			logger.warn("[首充活动] [加载首充数据] [异常] [{}]", new Object[]{firstChargeMess.size()});
			logger.error("首充 loadDiskCache2", e);
		}
	}
	
	public void doChongZhiActivity (Player player, long yinzi) {
		for (NewMoneyActivity activity : chongZhiActivitys) {
			if (activity.isCanServer()) {
				activity.doActivity(player, yinzi);
			}
		}
		if (shouchongRmbData != null) {
			shouchongRmbData.doChongZhi(player, yinzi);
		}
		for (int i = 0; i < rmbDatas.size(); i++) {
			rmbDatas.get(i).doChongZhi(player, yinzi);
		}
		
		for (int i = 0; i < weekMonthDatas.size(); i++) {
			WeekAndMonthChongZhiActivity activity = weekMonthDatas.get(i);
			if (activity.isCanServer()) {
				weekMonthDatas.get(i).doChongZhi(player, yinzi);
			}
		}
		
		for (int i = 0; i < weekCZs.size(); i++) {
			WeekChongZhiActivity activity = weekCZs.get(i);
			if (activity.isStart()) {
				activity.DoChongZhi(player, yinzi);
			}
		}
	}
	
	public void doXiaoFeiActivity (Player player, long yinzi, int type) {
		for (NewXiaoFeiActivity activity : xiaoFeiAcitivitys) {
//			System.out.println("[name:"+activity.getName()+"] ["+activity.isCanServer()+"] ["+activity.isXiaoFeiType(type)+"] [yinzi:"+yinzi+"] [type:"+type+"] ["+player.getName()+"]");
			if (activity.isCanServer() && activity.isXiaoFeiType(type)) {
				activity.doActivity(player, yinzi);
			}
		}
	}
	
	public void doXiaoFeiJiFenActivity(Player player, long yinzi){
		for(XiaoFeiGiveJiFenActivity jf : jiFenActivitys){
			if(jf.isEffective()){
				jf.doPrice(player, yinzi);
			}
		}
	}
	
	public NewMoneyActivity getBillBoardActivity () {
		for (NewMoneyActivity activity : chongZhiActivitys) {
			if (activity instanceof BillBoardActivity) {
				if (activity.isCanServer()) {
					long now = System.currentTimeMillis();
					if (now < activity.getStartTimeLong() || now > activity.getEndTimeLong()) {
						continue;
					}
					return activity;
				}
			}
		}
		return null;
	}
	
	public NewXiaoFeiActivity getBillBoardXiaoFeiActivity () {
		for (NewXiaoFeiActivity activity : xiaoFeiAcitivitys) {
			if (activity instanceof BillXiaoFeiBoardActivity) {
				if (activity.isCanServer()) {
					long now = System.currentTimeMillis();
					if (now < activity.getStartTimeLong() || now > activity.getEndTimeLong()) {
						continue;
					}
					return activity;
				}
			}
		}
		return null;
	}
	
	public GET_RMB_REWARDMSG_RES handle_GET_RMB_REWARDMSG_REQ (GET_RMB_REWARDMSG_REQ req, Player player) {
		try{
			if (shouchongRmbData == null) {
				return null;
			}
			RmbRewardClientData shouchong = shouchongRmbData.getClientData(player);
			RmbRewardClientData[] leijis = new RmbRewardClientData[rmbDatas.size()];
			String leiji_str = "";
			long leijiMoney = 0;
			for (int i = 0; i < leijis.length; i++) {
				leijis[i] = rmbDatas.get(i).getClientData(player);
				leiji_str += leijis[i].getDataID() + "" + leijis[i].isIsGetBefore();
				if ( i == leijis.length - 1) {
					Long m = rmbDatas.get(i).playerLeiJiMoneys.get(player.getId());
					if (m != null) {
						leijiMoney = m;
					}
				}
			}
			GET_RMB_REWARDMSG_RES res = new GET_RMB_REWARDMSG_RES(req.getSequnceNum(), RMB_PLAYER_LEVEL, ChargeRatio.DEFAULT_CHARGE_RATIO, leijiMoney, shouchong, leijis);
			logger.warn("[玩家查询累计活动] [{}] [{}] [{}] [{}]", new Object[]{player.getLogString(), shouchong.getDataID(), shouchong.isIsGetBefore(), leiji_str});
			return res;
		}catch(Exception e) {
			logger.error("handle_GET_RMB_REWARDMSG_REQ出错", e);
		}
		return null;
	}
	
	public GET_RMBREWARD_RES handle_GET_RMBREWARD_REQ(GET_RMBREWARD_REQ req, Player player) {
		int dataID = req.getDataID();
		try{
			StringBuffer sb = new StringBuffer("");
			sb.append("恭喜玩家" + player.getName());
			if (shouchongRmbData.getDataID() == dataID) {
				String re = shouchongRmbData.sendReward(player);
				if (re.length() > 0) {
					GET_RMBREWARD_RES res = new GET_RMBREWARD_RES(req.getSequnceNum(), false, re);
					return res;
				}else {
					sb.append(shouchongRmbData.getNotifyMsg());
					Player players[] = PlayerManager.getInstance().getOnlinePlayers();
					for (Player pp : players) {
						if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
							continue;
						}
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, sb.toString());
						pp.addMessageToRightBag(hreq);
					}
					GET_RMBREWARD_RES res = new GET_RMBREWARD_RES(req.getSequnceNum(), true, "");
					return res;
				}
			}else {
				for (int i = 0; i < rmbDatas.size(); i++) {
					if (rmbDatas.get(i).getDataID() == dataID) {
						String re = rmbDatas.get(i).sendReward(player);
						if (re.length() > 0) {
							GET_RMBREWARD_RES res = new GET_RMBREWARD_RES(req.getSequnceNum(), false, re);
							return res;
						}else {
							sb.append(rmbDatas.get(i).getNotifyMsg());
							Player players[] = PlayerManager.getInstance().getOnlinePlayers();
							for (Player pp : players) {
								if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
									continue;
								}
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, sb.toString());
								pp.addMessageToRightBag(hreq);
							}
							GET_RMBREWARD_RES res = new GET_RMBREWARD_RES(req.getSequnceNum(), true, "");
							return res;
						}
					}
				}
			}
		}catch(Exception e) {
			logger.error("handle_GET_RMB_REWARDMSG_REQ出错", e);
		}
		logger.error("[领取不存在的活动] [{}] [{}]", new Object[]{player.getLogString(), dataID});
		return null;
	}
	
	public GET_WEEKANDMONTH_INFO_RES handle_GET_WEEKANDMONTH_INFO_REQ (GET_WEEKANDMONTH_INFO_REQ req, Player player) {
		try {
			ArrayList<WeekAndMonthClientData> weeks = new ArrayList<WeekAndMonthClientData>();
			ArrayList<WeekAndMonthClientData> months = new ArrayList<WeekAndMonthClientData>();
			for (int i = 0; i < weekMonthDatas.size(); i++) {
				WeekAndMonthChongZhiActivity ac = weekMonthDatas.get(i);
				if (ac.isInTime() && ac.isCanServer()) {
					if (ac.getType() == 0) {
						weeks.add(ac.getClientData(player));
					}else if (ac.getType() == 1) {
						months.add(ac.getClientData(player));
					}
				}
			}
			
			Comparator<WeekAndMonthClientData> com = new Comparator<WeekAndMonthClientData>() {
				@Override
				public int compare(WeekAndMonthClientData o1,WeekAndMonthClientData o2) {
					if (o1 != null && o2 != null && o1.getNeedMoney() > o2.getNeedMoney()) {
						return 1;
					}
					return 0;
				}
			};
			Collections.sort(weeks, com);
			Collections.sort(months, com);
			if (weeks.size() == 0 && months.size() == 0) {
				return null;
			}
			GET_WEEKANDMONTH_INFO_RES res = new GET_WEEKANDMONTH_INFO_RES(req.getSequnceNum(), RMB_PLAYER_LEVEL, weeks.toArray(new WeekAndMonthClientData[0]), months.toArray(new WeekAndMonthClientData[0]));
			logger.warn("[获取周月反馈详细] [{}] [周={}] [月个数={}]", new Object[]{player.getLogString(), weeks.size(), months.size()});
			return res;
		} catch (Exception e) {
			logger.error("handle_GET_WEEKANDMONTH_INFO_REQ出错", e);
		}
		return null;
	}
	
	public void sendWeekActivityState(Player player) {
		WeekChongZhiActivity ac = null;
		for (int i = 0; i < weekCZs.size(); i++) {
			if (weekCZs.get(i).isStart()) {
				ac = weekCZs.get(i);
				break;
			}
		}
		
		boolean isStart = false;
		boolean isShow = false;
		if (ac != null) {
			isStart = true;
			int index = ac.getActivityIndex();
			long[] totals = ac.playerTotal4Week.get(player.getId());
			if (totals == null) {
				isShow = true;
			}else {
				int num = 0;
				for (int i = 0; i < totals.length; i++) {
					if (totals[i] >= ac.getNeedMonty()) {
						num ++;
					}
				}
				boolean[] gets = ac.playerGetReward.get(player.getId());
				if (gets == null || !gets[index]) {
					isShow = true;
				}else if (num >= ac.getSmall().getTotalNum() && !gets[gets.length - 2]) {
					isShow = true;
				}else if (num >= ac.getBig().getTotalNum() && !gets[gets.length - 1]) {
					isShow = true;
				}
			}
		}
		isShow = true;
		WEEKACTIVITY_STATE_RES res = new WEEKACTIVITY_STATE_RES(GameMessageFactory.nextSequnceNum(), isStart, isShow);
		player.addMessageToRightBag(res);
	}
	
	public GET_WEEKACTIVITY_RES handle_GET_WEEKACTIVITY_REQ	(GET_WEEKACTIVITY_REQ req, Player player) {
		WeekChongZhiActivity ac = null;
		for (int i = 0; i < weekCZs.size() ; i++) {
			if (weekCZs.get(i).isStart()) {
				ac = weekCZs.get(i);
				break;
			}
		}
		if (ac == null) {
			player.send_HINT_REQ(Translate.没有此类活动);
			return null;
		}
		int index = ac.getActivityIndex();
		WeekClientActivity[] clents = new WeekClientActivity[3];
		clents[0] = ac.getClients()[index];
		clents[1] = ac.getClients()[ac.getClients().length-2];
		clents[2] = ac.getClients()[ac.getClients().length-1];
		GET_WEEKACTIVITY_RES res = new GET_WEEKACTIVITY_RES(req.getSequnceNum(), ac.getId(), ac.getMsg(), ac.getTimeMsg(), clents, ac.GetPlayerTotalMoney(player.getId())/ChargeRatio.DEFAULT_CHARGE_RATIO, ac.GetPlayerTotalDay(player.getId()), ac.GetIsGetRewards(player.getId()));
		return res;
	}
	
	public GET_WEEK_REWARD_RES handle_GET_WEEK_REWARD_REQ (GET_WEEK_REWARD_REQ req, Player player) {
		WeekChongZhiActivity ac = null;
		for (int i = 0; i < weekCZs.size() ; i++) {
			if (weekCZs.get(i).isStart()) {
				ac = weekCZs.get(i);
				break;
			}
		}
		if (ac == null || ac.getId() != req.getDataID()) {
			return new GET_WEEK_REWARD_RES(req.getSequnceNum(), false, req.getDataID(), req.getDataType(), Translate.周充值活动结束);
		}
		int re = ac.GetReward(player, req.getDataType());
		switch(re) {
		case 0:
			sendWeekActivityState(player);
			return new GET_WEEK_REWARD_RES(req.getSequnceNum(), true, req.getDataID(), req.getDataType(), "");
		case 2:
			return new GET_WEEK_REWARD_RES(req.getSequnceNum(), false, req.getDataID(), req.getDataType(), Translate.周充值不能领取活动奖励);
		case 3:
			return new GET_WEEK_REWARD_RES(req.getSequnceNum(), false, req.getDataID(), req.getDataType(), Translate.周充值已经领取活动奖励);
		}
		return new GET_WEEK_REWARD_RES(req.getSequnceNum(), false, req.getDataID(), req.getDataType(), Translate.周充值未知错误);
	}
	
	
	public static String[] articleColors = new String[]{"0xffffff","0x00ff00","0x0020ff","0xff00ff","0xffe000"};
	public static String[] equColors = new String[]{"0xffffff","0x00ff00","0x0020ff","0xff00ff","0xff00ff","0xffe000","0xffe000"};
	
	public GET_WEEKMONTH_REWARD_RES handle_GET_WEEKMONTH_REWARD_REQ (GET_WEEKMONTH_REWARD_REQ req, Player player) {
		try {
			int dataId = req.getDataID();
			int type = req.getHandleType();
			for (int i = 0; i < weekMonthDatas.size(); i++) {
				WeekAndMonthChongZhiActivity ac = weekMonthDatas.get(i);
				if (ac.getDataID() == dataId) {
					if (type == 0) {
						String re = ac.getRewards(player);
						boolean result = false;
						if (re == null) {
							result = true;
							re = "";
						}
						GET_WEEKMONTH_REWARD_RES res = new GET_WEEKMONTH_REWARD_RES(req.getSequnceNum(), result, dataId, type, re);
						if (result) {
							Player[] oPs = PlayerManager.getInstance().getOnlinePlayers();
							String reP =  "";
							for (int j = 0; j < ac.getRewardTempEntitys().length; j++) {
								ArticleEntity entity = ac.getRewardTempEntitys()[j];
								String color="";
								if (entity instanceof EquipmentEntity) {
									color = equColors[entity.getColorType()];
								}else {
									color = articleColors[entity.getColorType()];
								}
								reP = reP + "<f color='"+color+"'>"+entity.getArticleName()+"</f>,";
							}
							String sendM = "";
							if(ac.getType() == 0) {
								sendM = Translate.translateString(Translate.恭喜玩家领取周月活动奖励, new String[][]{{Translate.STRING_1, "<f color='0xffff00'>"+player.getName()+"</f>"},{Translate.STRING_2, Translate.周},{Translate.STRING_3, reP}});
								player.send_HINT_REQ(Translate.恭喜您参与周累计充值活动领取奖励, (byte)7);
							}else {
								sendM = Translate.translateString(Translate.恭喜玩家领取周月活动奖励, new String[][]{{Translate.STRING_1, "<f color='0xffff00'>"+player.getName()+"</f>"},{Translate.STRING_2, Translate.月},{Translate.STRING_3, reP}});
								player.send_HINT_REQ(Translate.恭喜您参与月累计充值活动领取奖励, (byte)7);
							}
							for (int j = 0; j < oPs.length; j++) {
								oPs[j].send_HINT_REQ(sendM, (byte)7);
							}
						}
						logger.warn("[领取活动奖励] [{}] [{}] [{}]", new Object[]{player.getLogString(), ac.getLogString(), re});
						return res;
					}else if (type == 1) {
						String re = ac.buyProps(player);
						boolean result = false;
						if (re == null) {
							result = true;
							re = "";
						}
						GET_WEEKMONTH_REWARD_RES res = new GET_WEEKMONTH_REWARD_RES(req.getSequnceNum(), result, dataId, type, re);
						if (result) {
							Player[] oPs = PlayerManager.getInstance().getOnlinePlayers();
							String reP =  "";
							for (int j = 0; j < ac.getBuyTempEntitys().length; j++) {
								ArticleEntity entity = ac.getBuyTempEntitys()[j];
								String color="";
								if (entity instanceof EquipmentEntity) {
									color = equColors[entity.getColorType()];
								}else {
									color = articleColors[entity.getColorType()];
								}
								reP = reP + "<f color='"+color+"'>"+entity.getArticleName()+"</f>,";
							}
							String sendM = "";
							if(ac.getType() == 0) {
								player.send_HINT_REQ(Translate.恭喜您参与周累计充值活动购买奖励, (byte)7);
								sendM = Translate.translateString(Translate.恭喜玩家购买周月活动奖励, new String[][]{{Translate.STRING_1, "<f color='0xffff00'>"+player.getName()+"</f>"},{Translate.STRING_2, Translate.周},{Translate.STRING_3, reP}});
							}else {
								sendM = Translate.translateString(Translate.恭喜玩家购买周月活动奖励, new String[][]{{Translate.STRING_1, "<f color='0xffff00'>"+player.getName()+"</f>"},{Translate.STRING_2, Translate.月},{Translate.STRING_3, reP}});
								player.send_HINT_REQ(Translate.恭喜您参与月累计充值活动购买奖励, (byte)7);
							}
							for (int j = 0; j < oPs.length; j++) {
								oPs[j].send_HINT_REQ(sendM, (byte)7);
							}
						}
						logger.warn("[购买活动奖励] [{}] [{}] [{}]", new Object[]{player.getLogString(), ac.getLogString(), re});
						return res;
					}
					return null;
				}
			}
			logger.warn("[活动不存在?] [{}] [{}] [{}]", new Object[]{player.getLogString(), dataId, type});
		} catch (Exception e) {
			logger.error("GET_WEEKMONTH_REWARD_RES出错", e);
		}
		return null;
	}
	
	public void WeekCZ_GetReward(Player p, int type) {
		for (int i = 0; i < weekCZs.size(); i++) {
			if (weekCZs.get(i).isStart()) {
				weekCZs.get(i).GetReward(p, type);
				break;
			}
		}
	}
	
	@Override
	public void run() {
		while(runable) {
			try{
				for (NewMoneyActivity activity : chongZhiActivitys) {
					if (activity.isCanServer()) {
						activity.heatbeat();
					}
				}
			}catch(Exception e) {
				logger.error("心跳出错", e);
			}
			try{
				for (NewXiaoFeiActivity activity : xiaoFeiAcitivitys) {
					if (activity.isCanServer()) {
						activity.heatbeat();
					}
				}
			}catch(Exception e) {
				logger.error("心跳出错", e);
			}
			try {
				ChargeManager.getInstance().clearData();
			} catch (Exception e) {
				logger.error("心跳出错2", e);
			}
			try{
				Thread.sleep(runSleepTime);
			}catch(Exception e) {
				logger.error("心跳sleep失败", e);
			}
		}
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	public String getExcelPath() {
		return excelPath;
	}

	public void setDiskCatchPath(String diskCatchPath) {
		this.diskCatchPath = diskCatchPath;
	}

	public String getDiskCatchPath() {
		return diskCatchPath;
	}

	public static void setDiskCache(DiskCache diskCache) {
		NewChongZhiActivityManager.diskCache = diskCache;
	}

	public static DiskCache getDiskCache() {
		return diskCache;
	}

	//是否可以看见某个商店
	public boolean isShowShop (long pID, String shopName) {
		for (int i = 0; i < chongZhiActivitys.size(); i++) {
			NewMoneyActivity a = chongZhiActivitys.get(i);
			long now = System.currentTimeMillis();
			if (a instanceof ShowShopChongZhiActivity && a.isCanServer() && now >= a.getStartTimeLong()
					&& now <= a.getEndTimeLong()) {
				ShowShopChongZhiActivity sa = (ShowShopChongZhiActivity)a;
				if (sa.isShow(pID, shopName)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
