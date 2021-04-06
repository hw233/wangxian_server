package com.fy.engineserver.activity.shop;

import java.util.ArrayList;

import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;

/**
 * 商店沟买活动
 * 
 */
public abstract class ShopActivity extends BaseActivityInstance{

	private String mailTitle;
	private String mailContent;

	public ShopActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}

	/**
	 * 当购买物品之后调用
	 * @param shop
	 * @param goods
	 * @param buyNum
	 */
	public abstract void afterBuyGoods(Player player, Shop shop, Goods goods, int buyNum);
	
	/**
	 * 使用物品成功后调用 
	 * @param player
	 * @param articlename
	 * @param useNum
	 */
	public abstract void afterUseArticle(Player player, ArticleEntity article, int useNum);
	/**
	 * 使用物品后调用
	 * @param player
	 * @param article
	 * @param useNum
	 */
	public void afterUseArticle(Player player, ArrayList<ArticleEntity> articles ,byte useType){}

	public abstract boolean platFormFit();

	/**
	 * 当前服务器是否开放
	 * @return
	 */
	public boolean thisServerOpen(long now) {
		return (this.isThisServerFit() == null);
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	
}
