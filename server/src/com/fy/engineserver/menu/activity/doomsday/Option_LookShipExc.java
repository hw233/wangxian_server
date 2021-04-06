package com.fy.engineserver.menu.activity.doomsday;

import com.fy.engineserver.activity.doomsday.DoomsdayManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 查看造船进度
 * 
 *
 */
public class Option_LookShipExc extends Option {

	public Option_LookShipExc() {
		
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		DoomsdayManager.getInstance().lookSipExc(player);
	}
	
}
