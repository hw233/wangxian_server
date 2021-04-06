package com.fy.engineserver.menu;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.ResourceCollection;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.SHOP_GET_RES;
import com.fy.engineserver.message.SHOP_OTHER_INFO_RES;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.model.Passport;

/**
 * 进入商店
 * 
 * 
 * 
 */
public class Option_Shop_Get_Display extends Option implements NeedCheckPurview,
		NeedInitialiseOption {

	String shopName;

	/** 参与活动的渠道串，不配的话表示所有渠道都有 */
	String channel;

	/** 开始和结束时间,非必选项.但是如果有要求两个成对出现 */
	private String startTimeStr;// "2013-06-08 00:00:00"
	private String endTimeStr;// "2013-06-30 23:59:59"

	/* 解析后的数据 */
	private long startTime;
	private long endTime;
	private String[] channels;// public static String[] channels = {
								// "KOREAKT_MIESHI" };

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * 
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game, Player player) {
		try {
			ShopManager shopManager = ShopManager.getInstance();
			Shop shop = shopManager.getShop(shopName.trim(), player);

			if (shop == null) {

				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2776 + shopName + Translate.text_2777);
				player.addMessageToRightBag(hreq);
				ActivitySubSystem.logger.warn("商店[" + shopName + "]不存在");

			} else {
				ActivitySubSystem.logger.warn("商店[" + shopName + "]存在");
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
				com.fy.engineserver.shop.client.Goods[] cGoods = ShopManager.translate(player, shop, gs);
				SHOP_GET_RES res = new SHOP_GET_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.getVersion(), shop.shopType, coins, cGoods);
				player.addMessageToRightBag(res);

				SHOP_OTHER_INFO_RES shopOtherRes = new SHOP_OTHER_INFO_RES(GameMessageFactory.nextSequnceNum(), marketType, shopName, shop.shopType, cGoods);
				player.addMessageToRightBag(shopOtherRes);
				ActivitySubSystem.logger.warn("商店[" + shopName + "]存在 [OK]");
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error(player.getLogString() + "[打折商店]", e);
		}
	}

	@Override
	public void init() {
		if (startTimeStr == null || "".equals(startTimeStr)) {
			System.out.println(getText() + "无时间配置");
			return;
		}
		startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		endTime = TimeTool.formatter.varChar19.parse(endTimeStr);

		if (channel == null || "".equals(channel.trim())) {
			Game.logger.warn(getText() + "无渠道串配置");
			return;
		}
		channels = channel.split(",");
	}

	@Override
	public boolean canSee(Player player) {
		boolean rightChannel = false;
		boolean rightTime = false;

		if (channel == null || "".equals(channel.trim())) {// 无渠道限制
			rightChannel = true;
		} else {
			for (String channel : channels) {
				Passport passport = player.getPassport();
				if (passport != null) {
					String channelName = passport.getLastLoginChannel(); // 获取当前登录渠道
					if (channelName.equals(channel)) {
						rightChannel = true;
						ActivitySubSystem.logger.warn(player.getLogString() + "[渠道正确商店可见]");
					}
				} else {
					ActivitySubSystem.logger.warn(player.getLogString() + "[玩家渠道为空]");
				}
			}
		}
		if (startTimeStr == null || "".equals(startTimeStr.trim())) {// 无时间限制
			rightTime = true;
		} else {
			long now = SystemTime.currentTimeMillis();
			if (startTime <= now && now <= endTime) {
				rightTime = true;
			}
		}

		return rightChannel && rightTime;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_进入商店;
	}

	public void setOId(int oid) {
	}

	public String toString() {
		return Translate.text_4825 + ":" + this.shopName;
	}
}
