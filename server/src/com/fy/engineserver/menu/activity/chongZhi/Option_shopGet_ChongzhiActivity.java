package com.fy.engineserver.menu.activity.chongZhi;

import com.fy.engineserver.activity.newChongZhiActivity.NewChongZhiActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.SHOP_GET_RES;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;

public class Option_shopGet_ChongzhiActivity extends Option implements NeedCheckPurview {

	String shopName;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game, Player player) {
		ShopManager shopManager = ShopManager.getInstance();
		Shop shop = shopManager.getShop(shopName.trim(), player);

		if (shop == null) {

			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
			player.addMessageToRightBag(hreq);

		} else {
			byte marketType = 0;
			if (ShopManager.元宝商城左边标签显示的商店的名字 != null) {
				for (int i = 0; i < ShopManager.元宝商城左边标签显示的商店的名字.length; i++) {
					String name = ShopManager.元宝商城左边标签显示的商店的名字[i];
					if (shopName.trim().equals(name)) {
						marketType = ShopManager.元宝商城类型;
					}
				}
			}
			String[] 元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字;
			if (player.getVipLevel() > 0) {
				元宝商城右边隐藏的商店的名字 = ShopManager.元宝商城右边隐藏的商店的名字包含vip;
			}
			if (元宝商城右边隐藏的商店的名字 != null) {
				for (int i = 0; i < 元宝商城右边隐藏的商店的名字.length; i++) {
					String name = 元宝商城右边隐藏的商店的名字[i];
					if (shopName.trim().equals(name)) {
						marketType = ShopManager.元宝商城类型;
					}
				}
			}
			Goods gs[] = shop.getGoods(player);
			long[] coins = null;
			if (shop.shopType == ShopManager.商店_绑银商店) {
				coins = new long[2];
				coins[0] = player.getBindSilver();
				coins[1] = player.getSilver();
			} else if (shop.shopType == ShopManager.商店_资源商店) {
				coins = new long[3];
				Cave cave = FaeryManager.getInstance().getCave(player);
				if (cave != null) {
					ResourceCollection rc = cave.getCurrRes();
					if (rc != null) {
						coins[0] = rc.getFood();
						coins[1] = rc.getWood();
						coins[2] = rc.getStone();
					}
				}

			} else if (shop.shopType == ShopManager.商店_工资商店) {
				coins = new long[1];
				coins[0] = player.getWage();
			} else if (shop.shopType == ShopManager.商店_银子商店) {
				coins = new long[1];
				coins[0] = player.getSilver();
			} else {
				coins = new long[0];
			}
			SHOP_GET_RES res = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, coins, ShopManager.translate(player, shop, gs));
			player.addMessageToRightBag(res);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		// oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_进入商店;
	}

	public void setOId(int oid) {
	}

	public String toString() {
		return Translate.text_4825 + ":" + this.shopName;
	}

	@Override
	public boolean canSee(Player player) {
		return NewChongZhiActivityManager.instance.isShowShop(player.getId(), shopName);
	}
}
