package com.fy.engineserver.datasource.article.data.equipextra.module;

import java.util.Arrays;

import com.fy.engineserver.util.SimpleKey;

public class EquipStarModule {
	/** 索引 */
	@SimpleKey
	private int index;
	/** 物品统计名 */
	private String articleCNNName;
	/** 物品颜色 */
	private int colorType;
	/** 装备星级 */
	private int[] articleStars;
	/** 增加成功率 */
	private int[] successRate; 
	/** 增加的幸运值 */
	private int[] luckyNum;
	
	@Override
	public String toString() {
		return "EquipStarModule [index=" + index + ", articleCNNName=" + articleCNNName + ", colorType=" + colorType + ", articleStars=" + Arrays.toString(articleStars) + ", successRate=" + Arrays.toString(successRate) + ", luckyNum=" + Arrays.toString(luckyNum) + "]";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getArticleCNNName() {
		return articleCNNName;
	}

	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int[] getArticleStars() {
		return articleStars;
	}

	public void setArticleStars(int[] articleStars) {
		this.articleStars = articleStars;
	}

	public int[] getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(int[] successRate) {
		this.successRate = successRate;
	}

	public int[] getLuckyNum() {
		return luckyNum;
	}

	public void setLuckyNum(int[] luckyNum) {
		this.luckyNum = luckyNum;
	}
	
}
