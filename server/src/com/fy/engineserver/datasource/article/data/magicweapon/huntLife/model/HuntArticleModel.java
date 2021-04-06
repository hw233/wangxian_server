package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model;
/**
 * 猎命抽奖物品
 * @author Administrator
 *
 */
public class HuntArticleModel {
	/** 配表中对应物品id */
	private int id;
	/** 物品统计名 */
	private String articleCNNName;
	/** 颜色 */
	private int colorType;
	/** 是否绑定 */
	private boolean bind;
	/** 物品数量 */
	private int articleNums;
	/** 单抽概率 */
	private long prob4once;
	/** 十连抽概率 */
	private long prob4te;
	/** 兽魂等级 */
	private int level;
	/** 1为发世界公告 */
	private int temp;
	
	public String getLogString() {
		return "id:" + id + "，articleCNNName:" + articleCNNName + "，colorType:" + colorType + "，articleNums:" + articleNums + "，level:" + level + "，bind:" + bind;
	}
	
	@Override
	public String toString() {
		return "HuntArticleModel [id=" + id + ", articleCNNName=" + articleCNNName + ", colorType=" + colorType + ", bind=" + bind + ", articleNums=" + articleNums + ", prob4once=" + prob4once + ", prob4te=" + prob4te + ", level=" + level + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getArticleNums() {
		return articleNums;
	}

	public void setArticleNums(int articleNums) {
		this.articleNums = articleNums;
	}

	public long getProb4once() {
		return prob4once;
	}

	public void setProb4once(long prob4once) {
		this.prob4once = prob4once;
	}

	public long getProb4te() {
		return prob4te;
	}

	public void setProb4te(long prob4te) {
		this.prob4te = prob4te;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}
	
	
}
