package com.fy.engineserver.activity.chestFight.model;

import java.util.Arrays;

public class ChestArticleModel {
	private int id;
	/** 物品统计名 */
	private String articleCNNName;
	/** 个数 */
	private int num;
	/** 颜色 */
	private int colorType;
	/** 绑定 */
	private boolean bind;
	/** 概率 */
	private int[] prob;
	
	@Override
	public String toString() {
		return "ChestArticleModel [id=" + id + ", articleCNNName=" + articleCNNName + ", num=" + num + ", colorType=" + colorType + ", bind=" + bind + ", prob=" + Arrays.toString(prob) + "]";
	}

	public String getArticleCNNName() {
		return articleCNNName;
	}

	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	public int[] getProb() {
		return prob;
	}

	public void setProb(int[] prob) {
		this.prob = prob;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
