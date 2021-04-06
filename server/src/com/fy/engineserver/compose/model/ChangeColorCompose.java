package com.fy.engineserver.compose.model;

public class ChangeColorCompose {
	/** 物品名 */
	private String articleName;
	/** 合成后物品数量 */
	private int targetNum;
	/** 合成后物品是否绑定 */
	private boolean isBind;
	/** 合成后物品颜色最大值 */
	private int maxValueNum;
	/** 成功率（千分比） */
	private int successRate;
	/** 合成所需物品颜色值 */
	private int needArticleColor;
	/** 合成需要物品数量 */
	private int needArticleNum;
	/** 合成消耗类型；0：优先绑银，1：只银子 */
	private int costType;
	/** 消耗银子数量，单位：文 */
	private int costNum;
	
	@Override
	public String toString() {
		return "ChangeColorCompose [articleName=" + articleName + ", targetNum=" + targetNum + ", isBind=" + isBind + ", maxValueNum=" + maxValueNum + ", successRate=" + successRate + ", needArticleColor=" + needArticleColor + ", needArticleNum=" + needArticleNum + "]";
	}
	
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getTargetNum() {
		return targetNum;
	}
	public void setTargetNum(int targetNum) {
		this.targetNum = targetNum;
	}
	public boolean isBind() {
		return isBind;
	}
	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}
	public int getMaxValueNum() {
		return maxValueNum;
	}
	public void setMaxValueNum(int maxValueNum) {
		this.maxValueNum = maxValueNum;
	}
	public int getSuccessRate() {
		return successRate;
	}
	public void setSuccessRate(int successRate) {
		this.successRate = successRate;
	}
	public int getNeedArticleColor() {
		return needArticleColor;
	}
	public void setNeedArticleColor(int needArticleColor) {
		this.needArticleColor = needArticleColor;
	}
	public int getNeedArticleNum() {
		return needArticleNum;
	}
	public void setNeedArticleNum(int needArticleNum) {
		this.needArticleNum = needArticleNum;
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
