package com.fy.engineserver.activity.treasure.model;

public class BaseArticleModel {
	/**  物品统计名 */
	private String articleCNNName;
	/** 物品颜色 */
	private int colorType;
	/** 是否绑定 */
	private boolean bind;
	/** 物品数量 */
	private int articleNum;
	
	public BaseArticleModel() {
		super();
	}
	public BaseArticleModel(String str) {
		String[] s = str.split(",");
		articleCNNName = s[0];
		colorType = Integer.parseInt(s[1]);
		bind = "false".equalsIgnoreCase(s[2]) ? false : true;
		articleNum = Integer.parseInt(s[3]);
	}
	
	@Override
	public String toString() {
		return "BaseArticleModel [articleCNNName=" + articleCNNName + ", colorType=" + colorType + ", bind=" + bind + ", articleNum=" + articleNum + "]";
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
	public boolean isBind() {
		return bind;
	}
	public void setBind(boolean bind) {
		this.bind = bind;
	}
	public int getArticleNum() {
		return articleNum;
	}
	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}
	
	
}
