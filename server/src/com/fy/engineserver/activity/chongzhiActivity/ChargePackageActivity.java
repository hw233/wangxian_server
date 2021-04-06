package com.fy.engineserver.activity.chongzhiActivity;


import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.sprite.Player;

public class ChargePackageActivity extends BaseActivityInstance{

	private int days;
	private long money = 0;
	private String packageName = "";
	private String titleMess = "";	
	private String contentMess = "";
	private int levelLimit;
	
	public ChargePackageActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	
	public void setArgs(int days,long money,String packageName,String titleMess,String contentMess,int levelLimit){
		this.days = days;
		this.money = money;
		this.packageName = packageName;
		this.titleMess = titleMess;
		this.contentMess = contentMess;
		this.levelLimit = levelLimit;
	}
	
	public boolean isEffectActivity(Player player){
		if(this.isThisServerFit() == null && player.getLevel() >= levelLimit){
			return true;
		}
		return false;
	}

	@Override
	public String getInfoShow() {
		return this.toString();
	}

	public int getDays() {
		return this.days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public long getMoney() {
		return this.money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTitleMess() {
		return this.titleMess;
	}

	public void setTitleMess(String titleMess) {
		this.titleMess = titleMess;
	}

	public String getContentMess() {
		return this.contentMess;
	}

	public void setContentMess(String contentMess) {
		this.contentMess = contentMess;
	}

	public int getLevelLimit() {
		return this.levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	@Override
	public String toString() {
		return "ChargePackageActivity [days=" + this.days + ", money=" + this.money + ", packageName=" + this.packageName + ", titleMess=" + this.titleMess + ", contentMess=" + this.contentMess + ", levelLimit=" + this.levelLimit + "]";
	}


}
