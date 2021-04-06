package com.fy.engineserver.activity.chongzhiActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class MoneyBillBoardActivity {
	
	public static long ToLongTime = 1000L * 60 * 60 * 24 * 7;
	
	public static String ALL_SERVER_NAME = "ALL_SERVER";
	
	public static int MONEYBILLBOARDACTIVITY_CHONGZHI = 0;			//充值
	public static int MONEYBILLBOARDACTIVITY_XIAOFEI = 1;			//消费
	
	public static int XIAOFEI_TONGDAO_SHANGCHENG = 0;			//商城
	public static int XIAOFEI_TONGDAO_JIAOYISHUI = 1;			//交易税

	private String serverName = "";				//服务器名字
	private int platfrom = -1;					//平台名字
	private int type;							//活动类型
	private String[] parameters;				//参数
	private String startTime = "";				//开启时间
	private long start_time;					//开启时间
	private String endTime = "";				//结束时间
	private long end_time;						//结束时间
	private String[] propNames;					//奖励物品名字
	private String mailTitle = "";				//邮件标题
	private String mailMsg = "";				//邮件内容
	private String billboardMsg = "";			//在排行榜显示的信息
	
	public String toLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(serverName).append("] ");
		sb.append("[").append(type).append("] ");
		sb.append("[").append(Arrays.toString(parameters)).append("] ");
		sb.append("[").append(Arrays.toString(propNames)).append("] ");
		sb.append("[").append(startTime).append("] ");
		sb.append("[").append(isStart()).append("] ");
		sb.append("[").append(isEnd()).append("] ");
		sb.append("[").append(isEndToLong()).append("] ");
		sb.append("[").append(startTime).append("] ");
		sb.append("[").append(endTime).append("]");
		return sb.toString();
	}
	
	//是否开始
	public boolean isStart() {
		long now = System.currentTimeMillis();
		if (now > start_time) {
			return true;
		}
		return false;
	}
	
	//是否结束
	public boolean isEnd() {
		long now = System.currentTimeMillis();
		if (now <= end_time) {
			return false;
		}
		return true;
	}
	
	public boolean isEndToLong() {
		long now = System.currentTimeMillis();
		if (now - end_time > ToLongTime) {
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
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setParameters(String[] parameters) {
		this.parameters = parameters;
	}
	public String[] getParameters() {
		return parameters;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	public long getEnd_time() {
		return end_time;
	}
	public void setPropNames(String[] propNames) {
		this.propNames = propNames;
	}
	public String[] getPropNames() {
		return propNames;
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
	public void setBillboardMsg(String billboardMsg) {
		this.billboardMsg = billboardMsg;
	}
	public String getBillboardMsg() {
		return billboardMsg;
	}

	public void setPlatfrom(int platfrom) {
		this.platfrom = platfrom;
	}

	public int getPlatfrom() {
		return platfrom;
	}
	
}
