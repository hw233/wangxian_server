package com.fy.engineserver.guozhan.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 
 *
 */
public class Option_Guozhan_OK extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		GuozhanOrganizer.getInstance().playerAttendGuozhan(player);
	}

}
