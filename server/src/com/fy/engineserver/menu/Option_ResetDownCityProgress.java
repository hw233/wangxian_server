package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_ResetDownCityProgress extends Option {
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_重置副本进度;
	}

	public void doSelect(Game game, Player player) {}
	
	
}
