package com.fy.engineserver.sprite.pet2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class OptionJinJieReject extends Option  {
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		Pet2Manager.log.info("reject jin jie {}", player.getName());
	}
}
