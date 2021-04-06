package com.fy.engineserver.sprite.horse2.model;

public class HorseColorModel {
	/** 颜色  */
	private int colorType;
	/** 成长率 */
	private double growUpRate;
	/** 升级至下一颜色所需道具名 (统计名不翻译)*/
	private String articleCNNName;
	/** 所需道具数量 */
	private int needNum;
	/** 当前颜色对应血脉可以提升的最大星级 */
	private int maxStar;
	/** 升级所需临时物品id */
	private long tempArticleId;
	
	@Override
	public String toString() {
		return "HorseColorModel [colorType=" + colorType + ", growUpRate=" + growUpRate + ", articleCNNName=" + articleCNNName + ", needNum=" + needNum + ", maxStar=" + maxStar + "]";
	}
	public int getColorType() {
		return colorType;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public String getArticleCNNName() {
		return articleCNNName;
	}
	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}
	public int getNeedNum() {
		return needNum;
	}
	public void setNeedNum(int needNum) {
		this.needNum = needNum;
	}
	public int getMaxStar() {
		return maxStar;
	}
	public void setMaxStar(int maxStar) {
		this.maxStar = maxStar;
	}
	public double getGrowUpRate() {
		return growUpRate;
	}
	public void setGrowUpRate(double growUpRate) {
		this.growUpRate = growUpRate;
	}
	public long getTempArticleId() {
		return tempArticleId;
	}
	public void setTempArticleId(long tempArticleId) {
		this.tempArticleId = tempArticleId;
	}
	
	
}
