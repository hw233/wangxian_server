package com.fy.engineserver.datasource.article.manager;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.DiscountCard;
import com.fy.engineserver.datasource.article.manager.ArticleManager.BindType;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;

/**
 * 打折卡代理
 * 
 */
public class DiscountCardAgent {
	/** 打折道具列表 */
	private List<DiscountCard> list = new ArrayList<DiscountCard>();

	private static DiscountCardAgent instance;

	public static DiscountCardAgent getInstance() {
		if (instance == null) {
			synchronized (DiscountCardAgent.class) {
				if (instance == null) {
					instance = new DiscountCardAgent();
				}
			}
		}
		return instance;
	}

	public List<DiscountCard> getList() {
		return list;
	}

	public void setList(List<DiscountCard> list) {
		this.list = list;
	}

	/**
	 * 获取打折道具
	 * @param buyArticleName
	 * @param buyNum
	 * @param shopName
	 * @return
	 */
	public DiscountCard getDiscountCard(String buyArticleName, int buyNum, String shopName, int buycolor) {
		for (DiscountCard card : list) {
			if (card.discount(buyArticleName, buyNum, shopName, buycolor)) {
				return card;
			}
		}
		return null;
	}

	public void noticeBuySuccess(Player player, String buyArticleName, int buyNum, String shopName, int buycolor) {
		try {
			DiscountCard discountCard = getDiscountCard(buyArticleName, buyNum, shopName, buycolor);
			if (discountCard == null) {
				return;
			}
			ArticleEntity removeAe = player.removeArticleByNameColorAndBind(discountCard.getName(), discountCard.getColorType(), BindType.BOTH, "打折卡扣除", true);
			if (removeAe == null) {
				if (ShopManager.logger.isWarnEnabled()) {
					ShopManager.logger.warn(player.getLogString() + " [在商店" + shopName + "] [购买:" + buyArticleName + "] [数量:" + buyNum + "] [享受折扣:" + discountCard.getDiscountRate() + "] [消耗打折卡:" + discountCard.getName() + "] [扣除道具:" + removeAe + "]");
				}
				return;
			}
			if (ShopManager.logger.isWarnEnabled()) {
				ShopManager.logger.warn(player.getLogString() + " [在商店" + shopName + "] [购买:" + buyArticleName + "] [数量:" + buyNum + "] [享受折扣:" + discountCard.getDiscountRate() + "] [消耗打折卡:" + discountCard.getName() + "] [扣除道具:" + removeAe + "]");
			}
		} catch (Exception e) {
			ShopManager.logger.error(player.getLogString() + " [在商店" + shopName + "] [购买:" + buyArticleName + "] [数量:" + buyNum + "] [异常]", e);
		}
	}

	public boolean checkHaveDiscountCard(Player player, String buyArticleName, int buyNum, String shopName, int buycolor) {
		DiscountCard discountCard = getDiscountCard(buyArticleName, buyNum, shopName, buycolor);
		if (discountCard == null) {
			return false;
		}
		int count = player.countArticleInKnapsacksByName(discountCard.getName());
		if (count <= 0) {
			player.sendError(Translate.translateString(Translate.您没有此物品1, new String[][] { { Translate.STRING_1, discountCard.getName()+"~~" } }));
			return false;
		}
		return true;
	}
}
