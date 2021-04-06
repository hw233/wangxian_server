package com.fy.engineserver.menu.activity.doomsday;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_DuiHuan extends Option {

	public Option_DuiHuan() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		DoomsdayManager.getInstance().duihuanTONGXINGZHENG(player);
	}
	
}
