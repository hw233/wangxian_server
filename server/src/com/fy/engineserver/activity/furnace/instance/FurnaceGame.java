package com.fy.engineserver.activity.furnace.instance;

import java.io.Serializable;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

/**
 * 炼丹活动
 * @author Administrator
 *
 */
public class FurnaceGame implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 参与者id */
	private long playerId;
	/** 最后一次重置时间 */
	private long lastResetTime;
	/** 周期内参与活动次数 */
	private int enterTimes;
	/**
	 * 重置次数
	 */
	public void reset() {
		long now = System.currentTimeMillis();
		boolean result = TimeTool.instance.isSame(now, lastResetTime, TimeDistance.DAY);
		if (!result) {
			this.lastResetTime = now;
			this.enterTimes = 0;
		}
	}
	
	
	@Override
	public String toString() {
		return "FurnaceGame [playerId=" + playerId + ", lastResetTime=" + lastResetTime + ", enterTimes=" + enterTimes + "]";
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getLastResetTime() {
		return lastResetTime;
	}
	public void setLastResetTime(long lastResetTime) {
		this.lastResetTime = lastResetTime;
	}
	public int getEnterTimes() {
		return enterTimes;
	}
	public void setEnterTimes(int enterTimes) {
		this.enterTimes = enterTimes;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
