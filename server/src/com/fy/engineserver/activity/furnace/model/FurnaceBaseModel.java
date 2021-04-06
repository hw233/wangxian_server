package com.fy.engineserver.activity.furnace.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FurnaceBaseModel {
	/** 进入等级限制 */
	private int levelLimit;
	/** 周期内进入次数 */
	private int enterTimes;
	/** 进入地图名 */
	private String mapName;
	/** 玩家出生点坐标 */
	private int[] bornPoint;
	/** 丹炉个数 */
	private int furnaceNum;
	/** 对应丹炉id */
	private List<Integer> furnaceIds;
	/** 对应刷出npc坐标 */
	private List<Integer[]> furnacePoints = new ArrayList<Integer[]>();
	
	public FurnaceBaseModel(int levelLimit, int enterTimes, String mapName, int[] bornPoint, int furnaceNum, List<Integer> furnaceIds, List<Integer[]> furnacePoints) {
		super();
		this.levelLimit = levelLimit;
		this.enterTimes = enterTimes;
		this.mapName = mapName;
		this.bornPoint = bornPoint;
		this.furnaceNum = furnaceNum;
		this.furnaceIds = furnaceIds;
		this.furnacePoints = furnacePoints;
	}
	@Override
	public String toString() {
		return "FurnaceBaseModel [levelLimit=" + levelLimit + ", enterTimes=" + enterTimes + ", mapName=" + mapName + ", bornPoint=" + Arrays.toString(bornPoint) + ", furnaceNum=" + furnaceNum + ", furnaceIds=" + furnaceIds + ", furnacePoints=" + furnacePoints + "]";
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public int getEnterTimes() {
		return enterTimes;
	}
	public void setEnterTimes(int enterTimes) {
		this.enterTimes = enterTimes;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int[] getBornPoint() {
		return bornPoint;
	}
	public void setBornPoint(int[] bornPoint) {
		this.bornPoint = bornPoint;
	}
	public int getFurnaceNum() {
		return furnaceNum;
	}
	public void setFurnaceNum(int furnaceNum) {
		this.furnaceNum = furnaceNum;
	}
	public List<Integer> getFurnaceIds() {
		return furnaceIds;
	}
	public void setFurnaceIds(List<Integer> furnaceIds) {
		this.furnaceIds = furnaceIds;
	}
	public List<Integer[]> getFurnacePoints() {
		return furnacePoints;
	}
	public void setFurnacePoints(List<Integer[]> furnacePoints) {
		this.furnacePoints = furnacePoints;
	}
	
	
}
