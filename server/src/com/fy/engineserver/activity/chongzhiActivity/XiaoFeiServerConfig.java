package com.fy.engineserver.activity.chongzhiActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class XiaoFeiServerConfig {
	public static String ALL_SERVER_NAME = "ALL_SERVER";
	
	public static int FIRST_XIAOFEI_TYPE = 1;					//首充
	public static int ONETIME_XIAOFEI_TYPE = 2;					//单次
	public static int TOTAL_XIAOFEI_TYPE = 3;					//累计
	public static int TOTAL_TIMES_XIAOFEI_TYPE = 4;				//累计多次
	
	public static String[] XIAOFEI_TYPE_STR = new String[]{"", "首次消费", "单次消费", "累计消费", "累计消费多次"};
	
	public static int XIAOFEI_TONGDAO_SHANGCHENG = 0;			//商城
	public static int XIAOFEI_TONGDAO_JIAOYISHUI = 1;			//交易税
	
	public static int XIAOFEI_TONGDAO_SIZE = 2;					//消费通道数目
	
	public static String[] XIAOFEI_TONGDAO_STR = new String[]{"商城","交易税"};
	
	private long id = 0;						//活动ID
	private String serverName = "";				//服务器名字
	private int type;							//活动类型
	private int[] xiaoFeiTongDao;				//消费通道
	private long money;							//金额
	private String propName = "";				//奖励
	private int propNum;						//物品个数
	private int colorType;						//物品颜色
	private boolean isBind;						//是否绑定
	private String mailTitle = "";				//邮件标题
	private String mailMsg = "";				//邮件内容
	private String startTime = "";				//开启时间
	private long start_time;					//开启时间
	private String endTime = "";				//结束时间
	private long end_time;						//结束时间
	
	public String toLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[").append(serverName).append("] ");
		sb.append("[").append(Arrays.toString(xiaoFeiTongDao)).append("] ");
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
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int[] getXiaoFeiTongDao() {
		return xiaoFeiTongDao;
	}
	public void setXiaoFeiTongDao(int[] xiaoFeiTongDao) {
		this.xiaoFeiTongDao = xiaoFeiTongDao;
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

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	
}
