package com.fy.engineserver.activity.chestFight.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChestBaseModel {
	/** 进入等级限制 */
	private int levelLimit;
	/** 活动地图 */
	private String mapName;
	/** 最大玩家数限制 */
	private int maxAllowPlayer;
	/** 离开时经验百分比 */
	private double basicExpRate;
	/** 玩家进入地图出生点坐标 */
	private List<Integer[]> bornPoints = new ArrayList<Integer[]>();
	/** 活动开启后准备时间（在此期间不允许pk） */
	private long waitTime;
	/** 活动开启时间(周) */
	private int[] openDayOfWeek;
	/** 活动开启时间（小时） */
	private int[] openHourOfDay;
	/** 活动开启时间（分） */
	private int[] openMinitOfHour;
	/** 活动开启后可进入时间(毫秒) */
	private long lastTime;
	/** 宝箱刷新个数 */
	private int chestNum;
	/** 宝箱类型 */
	private int[] chestTypes;
	/** 对应刷新个数 */
	private int[] refreshNums;
	/** 宝箱刷新坐标 */
	private List<Integer[]> chestBornPoints = new ArrayList<Integer[]>();
	
	@Override
	public String toString() {
		return "ChestBaseModel [levelLimit=" + levelLimit + ", mapName=" + mapName + ", maxAllowPlayer=" + maxAllowPlayer + ", basicExpRate=" + basicExpRate + ", bornPoints=" + bornPoints + ", waitTime=" + waitTime + ", openDayOfWeek=" + Arrays.toString(openDayOfWeek) + ", openHourOfDay=" + Arrays.toString(openHourOfDay) + ", openMinitOfHour=" + Arrays.toString(openMinitOfHour) + ", lastTime=" + lastTime + ", chestNum=" + chestNum + ", chestTypes=" + Arrays.toString(chestTypes) + ", refreshNums=" + Arrays.toString(refreshNums) + ", chestBornPoints=" + chestBornPoints + "]";
	}
	public int getChestNum() {
		return chestNum;
	}
	public void setChestNum(int chestNum) {
		this.chestNum = chestNum;
	}
	public int[] getChestTypes() {
		return chestTypes;
	}
	public void setChestTypes(int[] chestTypes) {
		this.chestTypes = chestTypes;
	}
	public int[] getRefreshNums() {
		return refreshNums;
	}
	public void setRefreshNums(int[] refreshNums) {
		this.refreshNums = refreshNums;
	}
	public List<Integer[]> getChestBornPoints() {
		return chestBornPoints;
	}
	public void setChestBornPoints(List<Integer[]> chestBornPoints) {
		this.chestBornPoints = chestBornPoints;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int[] getOpenDayOfWeek() {
		return openDayOfWeek;
	}
	public void setOpenDayOfWeek(int[] openDayOfWeek) {
		this.openDayOfWeek = openDayOfWeek;
	}
	public int[] getOpenHourOfDay() {
		return openHourOfDay;
	}
	public void setOpenHourOfDay(int[] openHourOfDay) {
		this.openHourOfDay = openHourOfDay;
	}
	public int[] getOpenMinitOfHour() {
		return openMinitOfHour;
	}
	public void setOpenMinitOfHour(int[] openMinitOfHour) {
		this.openMinitOfHour = openMinitOfHour;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public int getMaxAllowPlayer() {
		return maxAllowPlayer;
	}
	public void setMaxAllowPlayer(int maxAllowPlayer) {
		this.maxAllowPlayer = maxAllowPlayer;
	}
	public double getBasicExpRate() {
		return basicExpRate;
	}
	public void setBasicExpRate(double basicExpRate) {
		this.basicExpRate = basicExpRate;
	}
	public List<Integer[]> getBornPoints() {
		return bornPoints;
	}
	public void setBornPoints(List<Integer[]> bornPoints) {
		this.bornPoints = bornPoints;
	}
	public long getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	
	
}
