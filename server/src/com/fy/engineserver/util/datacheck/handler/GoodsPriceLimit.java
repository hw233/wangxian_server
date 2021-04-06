package com.fy.engineserver.util.datacheck.handler;

public class GoodsPriceLimit {
	private String articleCNName;
	private int color;
	private int limitPrice;
	private int limitNum;

	public GoodsPriceLimit(String articleCNName, int color, int limitPrice, int limitNum) {
		this.articleCNName = articleCNName;
		this.color = color;
		this.limitPrice = limitPrice;
		this.limitNum = limitNum;
	}

	public String getArticleCNName() {
		return articleCNName;
	}

	public void setArticleCNName(String articleCNName) {
		this.articleCNName = articleCNName;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(int limitPrice) {
		this.limitPrice = limitPrice;
	}

	public int getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}

	@Override
	public String toString() {
		return "GoodsPriceLimit [articleCNName=" + articleCNName + ", color=" + color + ", limitNum=" + limitNum + ", limitPrice=" + limitPrice + "]";
	}

}
