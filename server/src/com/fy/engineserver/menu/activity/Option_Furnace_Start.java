package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Furnace_Start extends Option implements NeedCheckPurview{
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		FurnaceManager.inst.enterFrunaceGame(player);
	}
	
	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		return player.getLevel() >= 221;
	}
}
