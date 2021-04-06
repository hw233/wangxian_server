package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.client;

public class TempLuckModel {
	private long articleId;
	
	private String articleName;
	
	private int colorType;
	
	private int num;
	
	public TempLuckModel(long articleId, String articleName, int colorType, int num) {
		super();
		this.articleId = articleId;
		this.articleName = articleName;
		this.colorType = colorType;
		this.num = num;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getColorType() {
		return colorType;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
}
