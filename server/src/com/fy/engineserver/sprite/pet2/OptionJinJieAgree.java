package com.fy.engineserver.sprite.pet2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.PET_JIN_JIE_REQ;
import com.fy.engineserver.sprite.Player;

public class OptionJinJieAgree extends Option  {
	public PET_JIN_JIE_REQ req;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		Pet2Manager.log.info("{} agree jin jie", player.getName());
		Pet2Manager.getInst().jinJie(player, req, false);
	}
}
