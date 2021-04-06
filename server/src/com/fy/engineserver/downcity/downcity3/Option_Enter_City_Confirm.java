package com.fy.engineserver.downcity.downcity3;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Enter_City_Confirm extends Option {

	public void doSelect(Game game, Player player) {
		BossCityManager.getInstance().entenRoom(player);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
