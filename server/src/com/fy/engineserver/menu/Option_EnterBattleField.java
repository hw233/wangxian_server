package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_EnterBattleField extends Option {
	String battleFieldName;

	public String getBattleFieldName() {
		return battleFieldName;
	}

	public void setBattleFieldName(String battleFieldName) {
		this.battleFieldName = battleFieldName;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_传送到战场;
	}

	public void doSelect(Game game, Player player) {}
	
	
}
