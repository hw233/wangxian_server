package com.fy.engineserver.menu.trade;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.props.DiscountCard;
import com.fy.engineserver.datasource.article.manager.DiscountCardAgent;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_discount;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;

public class Option_ShopSilverBuy extends Option {

	public Option_ShopSilverBuy() {
	}

	private Shop shop;
	private int goodsId;
	private int amount;

	private int chooseType;

	// 0 代表选的使用银子 1代表的是选择的商城银子
	public Option_ShopSilverBuy(int type) {
		chooseType = type;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// if (chooseType == 0) {
		// shop.buyGoods(player, goodsId, amount, 0);
		// }else if (chooseType == 1) {
		// shop.buyGoods(player, goodsId, amount, 1);
		// }
		//

		Goods g = shop.getGoodsById(goodsId);
		if (g == null) {
			return;
		}
		DiscountCard discountCard = DiscountCardAgent.getInstance().getDiscountCard(g.getArticleName(), g.getBundleNum() * amount, shop.getName(),g.getColor());
		boolean hasDiscount = false;
		if (discountCard != null) {
			int hasNum = player.getArticleEntityNum(discountCard.getName());
			if (hasNum > 0) {
				hasDiscount = true;
			}
		}
		if (!hasDiscount) {// 不参与折扣
			shop.buyGoods(player, goodsId, amount, chooseType);
		} else {
			// 参与折扣
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			Option_Cancel cancel = new Option_Cancel();
			cancel.setText(Translate.取消);
			mw.setTitle(Translate.恭喜你享受折扣);
			mw.setDescriptionInUUB(Translate.translateString(Translate.折扣提示, new String[][] { { Translate.STRING_1, discountCard.getName() }, { Translate.STRING_2, BillingCenter.得到带单位的银两(g.getPrice() * amount) }, { Translate.STRING_3, BillingCenter.得到带单位的银两((long) (g.getPrice() * amount * discountCard.getDiscountRate() * TengXunDataManager.instance.getShopAgio2Buy(player))) } }));
			Option_discount option_discount = new Option_discount(shop, g, player, amount, discountCard, chooseType);
			option_discount.setText(Translate.确定);
			mw.setOptions(new Option[] { option_discount, cancel });
			QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
			player.addMessageToRightBag(res);
			// shop.buyGoods(player, req.getGoodsId(), req.getBuynum(), 0,discountCard.getDiscountRate());
		}

	}

	public void setChooseType(int chooseType) {
		this.chooseType = chooseType;
	}

	public int getChooseType() {
		return chooseType;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Shop getShop() {
		return shop;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}
}
