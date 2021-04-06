package com.fy.engineserver.activity.chongzhiActivity;

public class ChongZhiSpecialActivity extends ChongZhiServerConfig {

	private long yuMoney;				//钱的余数
	private String yuPropName = "";		//余数奖励物品名字
	
	public ChongZhiSpecialActivity(String serverName, String startTime, String endTime, String mailTitle,
			String mailMag, long money, String propName, long yuMoney, String yuPropName) throws Exception {
		setType(CHONGZHI_SPECIAL_TYPE);
		setServerName(serverName);
		setStartTime(startTime);
		setEndTime(endTime);
		creatLongTime();
		setMailTitle(mailTitle);
		setMailMsg(mailMag);
		setMoney(money);
		setPropName(propName);
		setPropNum(1);
		setColorType(-1);
		setBind(true);
		setYuMoney(yuMoney);
		setYuPropName(yuPropName);
	}
	
	@Override
	public String toLogString() {
		StringBuffer sb = new StringBuffer();
		sb.append(super.toLogString());
		sb.append(" [").append(yuMoney).append("] ");
		sb.append("[").append(yuPropName).append("]");
		return sb.toString();
	}

	public void setYuMoney(long yuMoney) {
		this.yuMoney = yuMoney;
	}

	public long getYuMoney() {
		return yuMoney;
	}

	public void setYuPropName(String yuPropName) {
		this.yuPropName = yuPropName;
	}

	public String getYuPropName() {
		return yuPropName;
	}
	
	
}
