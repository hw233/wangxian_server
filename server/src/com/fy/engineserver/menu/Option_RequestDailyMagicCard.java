package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_RequestDailyMagicCard extends Option {

	public void doSelect(Game game, Player player) {}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_领取每日神奇卡;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
}
