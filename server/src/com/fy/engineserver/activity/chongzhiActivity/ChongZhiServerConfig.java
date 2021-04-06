package com.fy.engineserver.activity.chongzhiActivity;

import java.text.SimpleDateFormat;

public class ChongZhiServerConfig {
	public static String ALL_SERVER_NAME = "ALL_SERVER";
	
	public static int FIRST_CHONGZHI_TYPE = 1;					//首充
	public static int ONETIME_CHONGZHI_TYPE = 2;				//单次
	public static int TOTAL_CHONGZHI_TYPE = 3;					//累计
	public static int EVERYDAY_CHONGZHI_BACK_TYPE = 4;			//每天充值返利
	public static int TOTAL_CHONGZHI_BACK_TYPE = 5;				//累计充值返利
	public static int CHONGZHI_SPECIAL_TYPE = 6;				//特殊充值活动
	public static int CHONGZHI_FANLI_TODAY_TYPE = 7;			//长时间返利， 多次
	public static int CHONGZHI_FANLI_TIMELY = 8;				//充值返利，当时就返
	public static int CHONGZHI_REWARD_ARTICLE_ONLYONE = 9;      //充值奖励物品，只奖励一次
	
	public static String[] CHONGZHI_TYPE_STR = new String[]{"", "首充充值", "单次充值", "累计充值", "每天返利", "充值返利", "特殊充值", "长时间返利", "当时返利"};
	
	private int activityID;					//活动ID
	private String serverName = "";			//服务器名字
	private int type;						//活动类型
	private long money;						//金额
	private String propName = "";			//奖励
	private int propNum;					//物品个数
	private int colorType;					//物品颜色
	private boolean isBind;					//是否绑定
	private String mailTitle = "";			//邮件标题
	private String mailMsg = "";			//邮件内容
	private String startTime = "";			//开启时间
	private long start_time;				//开启时间
	private String endTime = "";			//结束时间
	private long end_time;					//结束时间
	
	public String toLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(serverName).append("] ");
		sb.append("[").append(type).append("] ");
		sb.append("[").append(money).append("] ");
		sb.append("[").append(propName).append("] ");
		sb.append("[").append(propNum).append("] ");
		sb.append("[").append(colorType).append("] ");
		sb.append("[").append(isBind).append("] ");
		sb.append("[").append(startTime).append("] ");
		sb.append("[").append(endTime).append("]");
		return sb.toString();
	}
	
	public boolean isStart() {
		long now = System.currentTimeMillis();
//		logger.warn("[是否开始] [{}] [{}] [{}]", new Object[]{now, start_time, end_time});
		if (start_time <= now && end_time >= now) {
			return true;
		}
		return false;
	}
	
	public void creatLongTime() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		start_time = format.parse(getStartTime()).getTime();
		end_time = format.parse(getEndTime()).getTime();
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerName() {
		return serverName;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public int getPropNum() {
		return propNum;
	}

	public void setPropNum(int propNum) {
		this.propNum = propNum;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public boolean isBind() {
		return isBind;
	}

	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailMsg() {
		return mailMsg;
	}

	public void setMailMsg(String mailMsg) {
		this.mailMsg = mailMsg;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	public void setActivityID(int activityID) {
		this.activityID = activityID;
	}

	public int getActivityID() {
		return activityID;
	}
}
