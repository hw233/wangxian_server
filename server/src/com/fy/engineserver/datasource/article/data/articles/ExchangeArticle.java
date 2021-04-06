package com.fy.engineserver.datasource.article.data.articles;

public class ExchangeArticle extends Article {

	/**
	 * 配对交换物
	 */
	private String partnerArticle;
	
	private String partnerArticle_stat;
	/**
	 * 配对后新生成的物品
	 */
	private String createArticle;
	
	private String createArticle_stat;
	
	/**
	 * 左右 上下 全
	 */
	private int ExchangearticleType;
	
	// false 不用，就不会在产生
	private boolean isUse = true;
	
	public String getCreateArticle_stat() {
		return createArticle_stat;
	}
	public void setCreateArticle_stat(String createArticle_stat) {
		this.createArticle_stat = createArticle_stat;
	}
	public String getPartnerArticle_stat() {
		return partnerArticle_stat;
	}
	public void setPartnerArticle_stat(String partnerArticle_stat) {
		this.partnerArticle_stat = partnerArticle_stat;
	}
	public String getPartnerArticle() {
		return partnerArticle;
	}
	public void setPartnerArticle(String partnerArticle) {
		this.partnerArticle = partnerArticle;
	}
	public String getCreateArticle() {
		return createArticle;
	}
	public void setCreateArticle(String createArticle) {
		this.createArticle = createArticle;
	}
	public int getExchangearticleType() {
		return ExchangearticleType;
	}
	public void setExchangearticleType(int exchangearticleType) {
		ExchangearticleType = exchangearticleType;
	}
	public boolean isUse() {
		return isUse;
	}
	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
	
}
