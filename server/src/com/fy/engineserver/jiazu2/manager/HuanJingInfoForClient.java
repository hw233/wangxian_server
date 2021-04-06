package com.fy.engineserver.jiazu2.manager;

public class HuanJingInfoForClient {

	long bossId;
	String bossName = "";
	String bossIcon = "";
	String dropRate = "";
	int playerLevel;
	long[] ids;
	int[] nums;
	String cityName = "";
	String openTime = "";
	int jiazuLevel;
	int enterTimes;
	int totleTimes;
	
	public int getEnterTimes() {
		return enterTimes;
	}
	public void setEnterTimes(int enterTimes) {
		this.enterTimes = enterTimes;
	}
	public int getTotleTimes() {
		return totleTimes;
	}
	public void setTotleTimes(int totleTimes) {
		this.totleTimes = totleTimes;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public int getJiazuLevel() {
		return jiazuLevel;
	}
	public void setJiazuLevel(int jiazuLevel) {
		this.jiazuLevel = jiazuLevel;
	}
	public long getBossId() {
		return bossId;
	}
	public void setBossId(long bossId) {
		this.bossId = bossId;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
	}
	public String getBossIcon() {
		return bossIcon;
	}
	public void setBossIcon(String bossIcon) {
		this.bossIcon = bossIcon;
	}
	public String getDropRate() {
		return dropRate;
	}
	public void setDropRate(String dropRate) {
		this.dropRate = dropRate;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public long[] getIds() {
		return ids;
	}
	public void setIds(long[] ids) {
		this.ids = ids;
	}
	public int[] getNums() {
		return nums;
	}
	public void setNums(int[] nums) {
		this.nums = nums;
	}
	
}
