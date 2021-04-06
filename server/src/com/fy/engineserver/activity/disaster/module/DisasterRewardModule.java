package com.fy.engineserver.activity.disaster.module;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.treasure.model.BaseArticleModel;
import com.fy.engineserver.util.NeedBuildExtraData;

/**
 * 活动名次奖励
 */
public class DisasterRewardModule implements NeedBuildExtraData{
	/** 等级下限 */
	private int minLevel;
	/** 等级上限 */
	private int maxLevel;
	/** 名次 */
	private int rankLevel;
	/** 奖励经验比例(万分比) */
	private long rewardExpRate;
	private String[] tempArticles;
	/** 物品奖励，空代表没有物品奖励 */
	private List<BaseArticleModel> rewardArticles;
	
	@Override
	public void buildExtraData() throws Exception {
		// TODO Auto-generated method stub
		if (tempArticles != null && tempArticles.length > 0) {
			rewardArticles = new ArrayList<BaseArticleModel>();
			for (int i=0; i<tempArticles.length; i++) {
				rewardArticles.add(new BaseArticleModel(tempArticles[i]));
			}
		}
	}

	public int getRankLevel() {
		return rankLevel;
	}

	public void setRankLevel(int rankLevel) {
		this.rankLevel = rankLevel;
	}

	public long getRewardExpRate() {
		return rewardExpRate;
	}
	
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

	public void setRewardExpRate(long rewardExpRate) {
		this.rewardExpRate = rewardExpRate;
	}

	public List<BaseArticleModel> getRewardArticles() {
		return rewardArticles;
	}

	public void setRewardArticles(List<BaseArticleModel> rewardArticles) {
		this.rewardArticles = rewardArticles;
	}

	public void setTempArticles(String[] tempArticles) {
		this.tempArticles = tempArticles;
	}

}
