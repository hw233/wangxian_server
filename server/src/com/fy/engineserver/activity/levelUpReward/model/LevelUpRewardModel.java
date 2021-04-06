package com.fy.engineserver.activity.levelUpReward.model;

import java.util.ArrayList;
import java.util.List;

public class LevelUpRewardModel {
	/** 冲级奖励id--对应配表 (数据库记录此id对应查找相应奖励发放给玩家)*/
	private int type;
	/** 冲级红利名 */
	private String name;
	/** 购买最低等级 */
	private int lowLevelLimit;
	/** 购买最高等级 */
	private int highLevelLimit;
	/** 返利次数 */
	private int maxRewardTimes;
	/** 返利等级 */
	private List<Integer> rewardLevels = new ArrayList<Integer>();
	/** 对应返还银子数量 */
	private List<Long> rewardSilvers = new ArrayList<Long>();
	/** 描述 */
	private String description;
	/** 需要充值的金额   单位：元*/
	private int needRmb;
	
	@Override
	public String toString() {
		return "LevelUpRewardModel [type=" + type + ", name=" + name + ", lowLevelLimit=" + lowLevelLimit + ", highLevelLimit=" + highLevelLimit + ", maxRewardTimes=" + maxRewardTimes + ", rewardLevels=" + rewardLevels + ", rewardSilvers=" + rewardSilvers + ", description=" + description + ", needRmb=" + needRmb + "]";
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLowLevelLimit() {
		return lowLevelLimit;
	}
	public void setLowLevelLimit(int lowLevelLimit) {
		this.lowLevelLimit = lowLevelLimit;
	}
	public int getHighLevelLimit() {
		return highLevelLimit;
	}
	public void setHighLevelLimit(int highLevelLimit) {
		this.highLevelLimit = highLevelLimit;
	}
	public int getMaxRewardTimes() {
		return maxRewardTimes;
	}
	public void setMaxRewardTimes(int maxRewardTimes) {
		this.maxRewardTimes = maxRewardTimes;
	}
	public List<Integer> getRewardLevels() {
		return rewardLevels;
	}
	public void setRewardLevels(List<Integer> rewardLevels) {
		this.rewardLevels = rewardLevels;
	}
	public List<Long> getRewardSilvers() {
		return rewardSilvers;
	}
	public void setRewardSilvers(List<Long> rewardSilvers) {
		this.rewardSilvers = rewardSilvers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNeedRmb() {
		return needRmb;
	}

	public void setNeedRmb(int needRmb) {
		this.needRmb = needRmb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
