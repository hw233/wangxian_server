package com.fy.engineserver.menu.activity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.props.DiscountCard;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;

/**
 * 打折提示
 * 
 * 
 */
public class Option_discount extends Option {

	private Shop shop;
	private Goods goods;
	private Player player;
	private int buyNum;
	private DiscountCard discountCard;
	private int moneyType;

	public Option_discount(Shop shop, Goods goods, Player player, int buyNum, DiscountCard discountCard, int moneyType) {
		this.shop = shop;
		this.goods = goods;
		this.player = player;
		this.buyNum = buyNum;
		this.discountCard = discountCard;
		this.moneyType = moneyType;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player != this.player) {
			return;
		}
		shop.buyGoods(player, goods.getId(), buyNum, moneyType, discountCard.getDiscountRate());
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
