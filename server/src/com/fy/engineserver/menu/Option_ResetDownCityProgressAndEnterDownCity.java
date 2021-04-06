package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_ResetDownCityProgressAndEnterDownCity extends Option {
	String downCityName;

	public String getDownCityName() {
		return downCityName;
	}

	public void setDownCityName(String downCityName) {
		this.downCityName = downCityName;
	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_重置副本进度并且进入副本;
	}
	
	public void doSelect(Game game, Player player) {}
}
