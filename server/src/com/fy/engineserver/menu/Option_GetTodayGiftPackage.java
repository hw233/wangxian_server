package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_GetTodayGiftPackage extends Option {
	String packageName;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void doSelect(Game game, Player player) {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取今日礼包;
	}
}
