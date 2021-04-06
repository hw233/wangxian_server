package com.fy.engineserver.compose.model;
/**
 * 合成所需物品
 * @author Administrator
 *
 */
public class TempArtilce {
	/** 物品名 */
	private String articleName;
	/** 物品颜色 */
	private int articleColorNum;
	
	public TempArtilce(String articleName, int articleColorNum) {
		this.articleName = articleName;
		this.articleColorNum = articleColorNum;
	}
	
	@Override
	public String toString() {
		return "TempArtilce [articleName=" + articleName + ", articleColorNum=" + articleColorNum + "]";
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public int getArticleColorNum() {
		return articleColorNum;
	}
	public void setArticleColorNum(int articleColorNum) {
		this.articleColorNum = articleColorNum;
	}
}
