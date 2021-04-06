package com.fy.engineserver.activity.newChongZhiActivity;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;

import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.DiskCache;

/**
 *	充值消费活动的抽象接口
 */
public abstract class NewMoneyActivity {
	
	public static String ALL_SERVER = "ALL_SERVER";
	
	public static DiskCache diskcache;
	public static Logger logger;
	
	private int configID;					//配置ID
	private String name;					//配置名字
	private String startTime;				//开始时间
	private String endTime;					//结束时间
	private long startTimeLong;				//开始时间
	private long endTimeLong;				//开始时间
	private int platform;					//平台			0是官方    1是台湾    2是腾讯  3是马来
	private String[] serverNames;			//服务器名字
	private String[] unServerNames;			//不参加服务器名字
	private String mailTitle;				//邮件标题
	private String mailMsg;					//邮件内容
	private String[] parameters;			//参数
	
	public abstract void loadDiskCache();							//读取diskcache
	public abstract void doActivity(Player player, long yinzi);		//参加活动
	public abstract void heatbeat();								//心跳
	public abstract void creatParameter(String[] parameters);		//每个活动有自己独特的参数
	public abstract String getParameter();							//返回参数字符串
	public abstract void cleanActivityData();						//清楚活动数据
	public abstract String getLogString();							//得到日志
	public abstract void sendReward(Player player);								//发奖励
	public abstract String[] getActivityPartake();					//参与情况
	
	public NewMoneyActivity(int configID, String name, int platform, String startTime, String endTime, 
			String[] serverNames, String[] unServerNames, String mailTitle, String mailMsg, String[] parameters) throws Exception {
		this.configID = configID;
		this.name = name;
		this.platform = platform;
		this.startTime = startTime;
		this.endTime = endTime;
		this.serverNames = serverNames;
		this.unServerNames = unServerNames;
		this.mailTitle = mailTitle;
		this.mailMsg = mailMsg;
		this.parameters = parameters;
		createLongTime();
	}
	
	public void createLongTime() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		startTimeLong = format.parse(getStartTime()).getTime();
		endTimeLong = format.parse(getEndTime()).getTime();
	}
	
	public boolean isEffect(){
		long now = System.currentTimeMillis();
		return now >= getStartTimeLong() && now < getEndTimeLong();
	}
	
	public boolean isCanServer() {
		if (getPlatform() == 0) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return false;
			}
		}else if (getPlatform() == 1) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
				return false;
			}
		}else if (getPlatform() == 2) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
				return false;
			}
		}else if (getPlatform() == 3) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.马来)) {
				return false;
			}
		}else if (getPlatform() == 4) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				return false;
			}
		}
		String serverName = GameConstants.getInstance().getServerName();
		//如果服务器名字在 unserver名字中，就不参加活动
		for (String s : getUnServerNames()) {
			if (s.equals(serverName)) {
				return false;
			}
		}
		//如果是allserver 的  或者是这个活动的就参加
		for (String s : getServerNames()) {
			if (s.equals(ALL_SERVER)) {
				return true;
			}else if (s.equals(serverName)) {
				return true;
			}
		}
		return false;
	}
	
	public void setConfigID(int configID) {
		this.configID = configID;
	}
	public int getConfigID() {
		return configID;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setServerNames(String[] serverNames) {
		this.serverNames = serverNames;
	}
	public String[] getServerNames() {
		return serverNames;
	}
	public void setUnServerNames(String[] unServerNames) {
		this.unServerNames = unServerNames;
	}
	public String[] getUnServerNames() {
		return unServerNames;
	}
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}
	public String getMailTitle() {
		return mailTitle;
	}
	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}
	public String getMailMsg() {
		return mailMsg;
	}
	public void setStartTimeLong(long startTimeLong) {
		this.startTimeLong = startTimeLong;
	}
	public long getStartTimeLong() {
		return startTimeLong;
	}
	public void setEndTimeLong(long endTimeLong) {
		this.endTimeLong = endTimeLong;
	}
	public long getEndTimeLong() {
		return endTimeLong;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public int getPlatform() {
		return platform;
	}
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	public String[] getParameters() {
		return parameters;
	}
	
}
