package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.datasource.article.data.articles.Article;

/**
 * 打折卡道具
 * 
 */
public class DiscountCard extends Article {
	/** 要打折的道具名称 */
	private String discountArticleName;
	
	private String discountArticleName_stat;
	/** 折扣 */
	private double discountRate;
	/** 需要购买道具数量触发折扣 */
	private int buyArticleNum;
	/** 指定商店的名字 */
	private String shopName;
	
	private int buyArticlecolor;
	
	private String shopName_stat;
	public String getDiscountArticleName_stat() {
		return discountArticleName_stat;
	}
	public void setDiscountArticleName_stat(String discountArticleName_stat) {
		this.discountArticleName_stat = discountArticleName_stat;
	}
	public String getDiscountArticleName() {
		return discountArticleName;
	}
	public void setDiscountArticleName(String discountArticleName) {
		this.discountArticleName = discountArticleName;
	}
	public double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	public int getBuyArticleNum() {
		return buyArticleNum;
	}
	public void setBuyArticleNum(int buyArticleNum) {
		this.buyArticleNum = buyArticleNum;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getShopName_stat() {
		return shopName_stat;
	}
	public void setShopName_stat(String shopName_stat) {
		this.shopName_stat = shopName_stat;
	}
	
	public int getBuyArticlecolor() {
		return buyArticlecolor;
	}
	public void setBuyArticlecolor(int buyArticlecolor) {
		this.buyArticlecolor = buyArticlecolor;
	}
	public boolean discount(String buyArticleName,int buyNum,String shopName,int buycolor) {
		return this.shopName.equals(shopName) && this.discountArticleName.equals(buyArticleName) && this.buyArticleNum == buyNum && this.buyArticlecolor == buycolor;
	}
}
