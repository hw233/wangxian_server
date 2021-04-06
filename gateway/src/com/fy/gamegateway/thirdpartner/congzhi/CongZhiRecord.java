package com.fy.gamegateway.thirdpartner.congzhi;

//账号	金额	级别	分区	渠道	充值类型	充值卡类型	充值时间	平台	手续费
public class CongZhiRecord {

	protected String username;
	protected int money;
	protected int level;
	protected String fenqu;
	protected String channel;
	protected String congzhiType;
	protected String congzhiCardType;
	protected String congzhiTime; //"yyyy-MM-dd HH:mm:ss.S"
	protected String platform;
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getFenqu() {
		return fenqu;
	}
	public void setFenqu(String fenqu) {
		this.fenqu = fenqu;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCongzhiType() {
		return congzhiType;
	}
	public void setCongzhiType(String congzhiType) {
		this.congzhiType = congzhiType;
	}
	public String getCongzhiCardType() {
		return congzhiCardType;
	}
	public void setCongzhiCardType(String congzhiCardType) {
		this.congzhiCardType = congzhiCardType;
	}
	public String getCongzhiTime() {
		return congzhiTime;
	}
	public void setCongzhiTime(String congzhiTime) {
		this.congzhiTime = congzhiTime;
	}
	
	
}
