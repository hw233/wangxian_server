package com.fy.engineserver.jiazu2.model;

public class BiaocheRewardModel {
	/** 家族等级 */
	private int jiazuLevel;
	/** 奖励家族资金数量 */
	private long rewardMoney;
	
	@Override
	public String toString() {
		return "BiaocheRewardModel [jiazuLevel=" + jiazuLevel + ", rewardMoney=" + rewardMoney + "]";
	}
	public int getJiazuLevel() {
		return jiazuLevel;
	}
	public void setJiazuLevel(int jiazuLevel) {
		this.jiazuLevel = jiazuLevel;
	}
	public long getRewardMoney() {
		return rewardMoney;
	}
	public void setRewardMoney(long rewardMoney) {
		this.rewardMoney = rewardMoney;
	}
	
	
}
