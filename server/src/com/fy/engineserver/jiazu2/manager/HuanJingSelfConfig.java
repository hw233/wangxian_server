package com.fy.engineserver.jiazu2.manager;

import java.util.Arrays;

public class HuanJingSelfConfig {

	private int bossId;
	private String bossName;
	private String bossIcon;
	private String mapName;
	private int x;
	private int y;
	private int playerLevel;
	private int jiazuLevel;
	private String [] rewardNames;
	private int [] rewardNums;
	private String cityName;
	
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
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
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public int getJiazuLevel() {
		return jiazuLevel;
	}
	public void setJiazuLevel(int jiazuLevel) {
		this.jiazuLevel = jiazuLevel;
	}
	public String[] getRewardNames() {
		return rewardNames;
	}
	public void setRewardNames(String[] rewardNames) {
		this.rewardNames = rewardNames;
	}
	public int[] getRewardNums() {
		return rewardNums;
	}
	public void setRewardNums(int[] rewardNums) {
		this.rewardNums = rewardNums;
	}
	@Override
	public String toString() {
		return "[bossId=" + bossId + ", jiazuLevel="
				+ jiazuLevel + ", mapName=" + mapName + ", playerLevel="
				+ playerLevel + ", rewardNames=" + Arrays.toString(rewardNames)
				+ ", rewardNums=" + Arrays.toString(rewardNums) + ", x=" + x
				+ ", y=" + y + "]";
	}
	
}
