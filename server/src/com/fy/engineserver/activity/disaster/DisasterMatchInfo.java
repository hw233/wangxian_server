package com.fy.engineserver.activity.disaster;

import java.util.Arrays;

import com.fy.engineserver.activity.disaster.instance.Disaster;

/**
 * 匹配信息
 */
public class DisasterMatchInfo {
	private int id;
	/** 匹配成功时间 */
	private long matchedTime;
	/** 匹配成功的玩家id */
	private long[] playerIds;
	/** 对应玩家是否准备 */
	private boolean[] ready;
	
	private Disaster disaster;
	
	public String getLogString() {
		return "[playerIds:" + Arrays.toString(playerIds) + "]";
	}
	
	public DisasterMatchInfo(int id, Disaster disaster) {
		this.id = id;
		this.disaster = disaster;
	}
	public long getMatchedTime() {
		return matchedTime;
	}
	public void setMatchedTime(long matchedTime) {
		this.matchedTime = matchedTime;
	}
	public long[] getPlayerIds() {
		return playerIds;
	}
	public void setPlayerIds(long[] playerIds) {
		this.playerIds = playerIds;
	}
	public boolean[] getReady() {
		return ready;
	}
	public void setReady(boolean[] ready) {
		this.ready = ready;
	}

	public Disaster getDisaster() {
		return disaster;
	}

	public void setDisaster(Disaster disaster) {
		this.disaster = disaster;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
