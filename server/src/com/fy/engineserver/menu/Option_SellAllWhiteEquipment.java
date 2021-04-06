package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_SellAllWhiteEquipment extends Option {
	String shopName;
	
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void doSelect(Game game, Player player) {}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_变卖所有白装;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
}
