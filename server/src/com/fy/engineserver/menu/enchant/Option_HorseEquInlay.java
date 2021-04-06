package com.fy.engineserver.menu.enchant;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INLAYTAKE_WINDOW_RES;
import com.fy.engineserver.sprite.Player;

public class Option_HorseEquInlay extends Option implements NeedCheckPurview{
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		INLAYTAKE_WINDOW_RES resp = new INLAYTAKE_WINDOW_RES(GameMessageFactory.nextSequnceNum(), 2, new String[0], new int[0]);
		player.addMessageToRightBag(resp);
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel() < WindowManager.LMLV){
			return false;
		}
		return true;
	}
	
}
