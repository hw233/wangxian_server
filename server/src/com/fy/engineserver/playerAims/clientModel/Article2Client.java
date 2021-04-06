package com.fy.engineserver.playerAims.clientModel;

public class Article2Client {
	/** 临时物品Id */
	private long articleId;
	/** 奖励数量 */
	private long rewardNum;
	
	public Article2Client(long articleId, long rewardNum) {
		super();
		this.articleId = articleId;
		this.rewardNum = rewardNum;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public long getRewardNum() {
		return rewardNum;
	}
	public void setRewardNum(long rewardNum) {
		this.rewardNum = rewardNum;
	}
	
	
}
