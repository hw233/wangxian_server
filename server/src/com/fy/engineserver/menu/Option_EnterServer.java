package com.fy.engineserver.menu;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.PLAYER_ENTER_REQ;
import com.fy.engineserver.message.PLAYER_ENTER_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;

public class Option_EnterServer extends Option {

	public Option_EnterServer() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		CoreSubSystem.beCarePlayer.add(player.getId());
		Game.logger.warn("[用户同意PK] [{}]", new Object[]{player.getLogString()});
//		PLAYER_ENTER_RES handler_playerEnter = CoreSubSystem.getInstance().handler_playerEnter(req, passport, conn, username, this.player);
//		this.player.addMessageToRightBag(handler_playerEnter);
//		System.out.println(Option_EnterServer.class.getSimpleName() + ", [ok]");
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
