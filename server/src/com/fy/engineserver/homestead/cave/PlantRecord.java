package com.fy.engineserver.homestead.cave;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class PlantRecord {
	/** 种植的组名 */
	private String groupName;
	/** 种植的次数 */
	private int plantCount;
	/** 最后一次种植时间 */
	private long lastPlantTime;

	public PlantRecord() {

	}

	public PlantRecord(String groupName, int plantCount, long lastPlantTime) {
		this.groupName = groupName;
		this.plantCount = plantCount;
		this.lastPlantTime = lastPlantTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getPlantCount() {
		return plantCount;
	}

	public void setPlantCount(int plantCount) {
		this.plantCount = plantCount;
	}

	public long getLastPlantTime() {
		return lastPlantTime;
	}

	public void setLastPlantTime(long lastPlantTime) {
		this.lastPlantTime = lastPlantTime;
	}

	@Override
	public String toString() {
		return "PlantRecord [groupName=" + groupName + ", plantCount=" + plantCount + ", lastPlantTime=" + lastPlantTime + "]";
	}
}
