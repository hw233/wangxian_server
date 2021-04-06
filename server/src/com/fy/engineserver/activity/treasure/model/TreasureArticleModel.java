package com.fy.engineserver.activity.treasure.model;

public class TreasureArticleModel extends BaseArticleModel{
	/** 物品id */
	private int id;
	/** 抽取到物品的概率 */
	private int problem;
	/** 临时物品id */
	private long tempEntityId;
	
	@Override
	public String toString() {
		return "TreasureArticleModel [id=" + id + ", problem=" + problem + ", tempEntityId=" + tempEntityId + ", getArticleCNNName()=" + getArticleCNNName() + ", getColorType()=" + getColorType() + ", isBind()=" + isBind() + ", getArticleNum()=" + getArticleNum() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProblem() {
		return problem;
	}
	public void setProblem(int problem) {
		this.problem = problem;
	}
	public long getTempEntityId() {
		return tempEntityId;
	}
	public void setTempEntityId(long tempEntityId) {
		this.tempEntityId = tempEntityId;
	}
	
	
}
