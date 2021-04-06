package com.fy.engineserver.activity.disaster.module;
/**
 * 经验桃奖励配置
 */
public class DisasterExpRewardModule {
	/** 等级下限 */
	private int minLevel;
	/** 等级上限 */
	private int maxLevel;
	/** 奖励经验比例(万分比) */
	private long rewardExpRate;
	
	public int getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	public int getMaxLevel() {
		return maxLevel;
	}
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	public long getRewardExpRate() {
		return rewardExpRate;
	}
	public void setRewardExpRate(long rewardExpRate) {
		this.rewardExpRate = rewardExpRate;
	}
}
