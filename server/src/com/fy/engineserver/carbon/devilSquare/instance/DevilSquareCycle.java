package com.fy.engineserver.carbon.devilSquare.instance;

import java.io.Serializable;

/**
 * 存储玩家进入副本的次数，周期内进入次数限制
 * @author Administrator
 *
 */
public class DevilSquareCycle implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long playerId;
	/** 最后一次进入副本时间 */
	private long lastEnterTime;
	/** 周期内进入副本次数 */
	private int cycleCount;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getLastEnterTime() {
		return lastEnterTime;
	}
	public void setLastEnterTime(long lastEnterTime) {
		this.lastEnterTime = lastEnterTime;
	}
	public int getCycleCount() {
		return cycleCount;
	}
	public void setCycleCount(int cycleCount) {
		this.cycleCount = cycleCount;
	}
	
	
}
