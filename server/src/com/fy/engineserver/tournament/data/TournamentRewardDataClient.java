package com.fy.engineserver.tournament.data;

public class TournamentRewardDataClient {

	/**
	 * 排名区间(闭区间)
	 */
	public int[] rankRange = new int[0];
	
	/**
	 * 奖励钱
	 */
	public String rewardDes = "";

	/**
	 * 奖励物品
	 */
	public long[] articleEntityIds = new long[0];
	
	/**
	 * 奖励物品个数
	 */
	public int[] articleEntityCounts = new int[0];

	public int[] getRankRange() {
		return rankRange;
	}

	public void setRankRange(int[] rankRange) {
		this.rankRange = rankRange;
	}

	public String getRewardDes() {
		return rewardDes;
	}

	public void setRewardDes(String rewardDes) {
		this.rewardDes = rewardDes;
	}

	public long[] getArticleEntityIds() {
		return articleEntityIds;
	}

	public void setArticleEntityIds(long[] articleEntityIds) {
		this.articleEntityIds = articleEntityIds;
	}

	public int[] getArticleEntityCounts() {
		return articleEntityCounts;
	}

	public void setArticleEntityCounts(int[] articleEntityCounts) {
		this.articleEntityCounts = articleEntityCounts;
	}
}
