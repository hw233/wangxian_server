package com.fy.engineserver.compose.model;

import java.util.List;

public class Compose4Other {
	/** 合成后的物品名 */
	private String articleName;
	/** 合成后物品颜色 */
	private int articleColor;
	/** 合成后物品数量 */
	private int articleNum;
	/** 合成后物品是否绑定 */
	private boolean isBind;
	/** 成功率（千分比） */
	private int successRate;
	/** 需要物品个数 */
	private int needArticleNum;
	/** 合成所需物品 */
	private List<TempArtilce> needArticles;
	/** 合成消耗类型；0：优先绑银，1：只银子 */
	private int costType;
	/** 消耗银子数量，单位：文 */
	private int costNum;
	
	@Override
	public String toString() {
		return "Compose4Other [articleName=" + articleName + ", articleColor=" + articleColor + ", articleNum=" + articleNum + ", isBind=" + isBind + ", successRate=" + successRate + ", needArticleNum=" + needArticleNum + ", needArticles=" + needArticles + "]";
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getArticleColor() {
		return articleColor;
	}
	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}
	public int getArticleNum() {
		return articleNum;
	}
	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
	public boolean isBind() {
		return isBind;
	}
	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}
	public int getSuccessRate() {
		return successRate;
	}
	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}
	public int getNeedArticleNum() {
		return needArticleNum;
	}
	public void setNeedArticleNum(int needArticleNum) {
		this.needArticleNum = needArticleNum;
	}
	public List<TempArtilce> getNeedArticles() {
		return needArticles;
	}
	public void setNeedArticles(List<TempArtilce> needArticles) {
		this.needArticles = needArticles;
	}
	public int getCostType() {
		return costType;
	}
	public void setCostType(int costType) {
		this.costType = costType;
	}
	public int getCostNum() {
		return costNum;
	}
	public void setCostNum(int costNum) {
		this.costNum = costNum;
	}
}
