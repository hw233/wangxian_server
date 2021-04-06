package com.fy.engineserver.activity.furnace.model;
/**
 * 丹炉
 * @author Administrator
 *
 */
public class FurnaceModel {
	private int id;
	
	private long minTime;
	
	private long maxTime;
	
	private int npcId;
	
	@Override
	public String toString() {
		return "FurnaceModel [id=" + id + ", minTime=" + minTime + ", maxTime=" + maxTime + ", npcId=" + npcId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	
	
}
