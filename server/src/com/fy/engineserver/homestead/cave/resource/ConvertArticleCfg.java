package com.fy.engineserver.homestead.cave.resource;

import com.fy.engineserver.homestead.cave.ResourceCollection;

/**
 * 兑换道具配置
 * 
 * 
 */
public class ConvertArticleCfg {
	private int id;
	/** 仓库等级 */
	private int storeGrade;
	/** 组别 */
	private int groupIndex;
	/** 物品颜色 */
	private int articleColor;
	/** 物品名 */
	private String articleName;
	/** 组内每日兑换上限 */
	private int daiylMaxNum;
	/** 兑换花费 */
	private ResourceCollection convertCost;

	public ConvertArticleCfg() {
		// TODO Auto-generated constructor stub
	}

	public ConvertArticleCfg(int id, int storeGrade, int groupIndex, int articleColor, String articleName, int daiylMaxNum, ResourceCollection convertCost) {
		this.id = id;
		this.storeGrade = storeGrade;
		this.groupIndex = groupIndex;
		this.articleColor = articleColor;
		this.articleName = articleName;
		this.daiylMaxNum = daiylMaxNum;
		this.convertCost = convertCost;
	}

	public int getStoreGrade() {
		return storeGrade;
	}

	public void setStoreGrade(int storeGrade) {
		this.storeGrade = storeGrade;
	}

	public int getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getDaiylMaxNum() {
		return daiylMaxNum;
	}

	public void setDaiylMaxNum(int daiylMaxNum) {
		this.daiylMaxNum = daiylMaxNum;
	}

	public ResourceCollection getConvertCost() {
		return convertCost;
	}

	public void setConvertCost(ResourceCollection convertCost) {
		this.convertCost = convertCost;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ConvertArticleCfg [id=" + id + ", storeGrade=" + storeGrade + ", groupIndex=" + groupIndex + ", articleColor=" + articleColor + ", articleName=" + articleName + ", daiylMaxNum=" + daiylMaxNum + ", convertCost=" + convertCost + "]";
	}
}
