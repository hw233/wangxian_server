package com.fy.engineserver.vip.data;

public class VIPData {

	private int vipLevel;
	private String vipIcon = "";
	private int needCost;
	private String articleName;
	private String description;
	/**
	 * 临时id
	 */
	private long articleId;
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public String getVipIcon() {
		return vipIcon;
	}
	public void setVipIcon(String vipIcon) {
		this.vipIcon = vipIcon;
	}
	public int getNeedCost() {
		return needCost;
	}
	public void setNeedCost(int needCost) {
		this.needCost = needCost;
	}
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
}
