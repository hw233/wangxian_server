package com.fy.engineserver.activity.dailyTurnActivity.model;

import com.fy.engineserver.activity.treasure.model.BaseArticleModel;

public class RewardArticleModel extends BaseArticleModel{
	/** 物品id */
	private int id;
	/** 获得概率 */
	private int prob;
	/** 临时物品id */
	private long entityId;
	
	@Override
	public String toString() {
		return "RewardArticleModel [id=" + id + ", prob=" + prob + ", entityId=" + entityId + ", getArticleCNNName()=" + getArticleCNNName() + ", getColorType()=" + getColorType() + ", isBind()=" + isBind() + ", getArticleNum()=" + getArticleNum() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProb() {
		return prob;
	}

	public void setProb(int prob) {
		this.prob = prob;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}
}
