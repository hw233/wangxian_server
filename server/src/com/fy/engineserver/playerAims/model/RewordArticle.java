package com.fy.engineserver.playerAims.model;

public class RewordArticle {
	/** 物品统计名 */
	private String articleCNName;
	/** 颜色 */
	private int colorType;
	/** 物品数量 */
	private int num;
	/** 是否显示粒子 */
	private boolean hasParticle;
	
	public RewordArticle(String articleCNName, int colorType, int num, boolean hasParticle) {
		super();
		this.articleCNName = articleCNName;
		this.colorType = colorType;
		this.num = num;
		this.hasParticle = hasParticle;
	}
	public String getArticleCNName() {
		return articleCNName;
	}
	public void setArticleCNName(String articleCNName) {
		this.articleCNName = articleCNName;
	}
	public int getColorType() {
		return colorType;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public boolean isHasParticle() {
		return hasParticle;
	}
	public void setHasParticle(boolean hasParticle) {
		this.hasParticle = hasParticle;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
