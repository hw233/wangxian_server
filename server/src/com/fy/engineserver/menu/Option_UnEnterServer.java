package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * @author madCat
 *         2014-3-19
 * 
 */
public class Option_UnEnterServer extends Option {

	public Option_UnEnterServer() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		player.getConn().close();
		Game.logger.warn("[用户不同意PK] [关闭连接] [{}]", new Object[]{player.getLogString()});
//		PLAYER_ENTER_RES handler_playerEnter = CoreSubSystem.getInstance().handler_playerEnter(req, passport, conn, username, this.player);
//		this.player.addMessageToRightBag(handler_playerEnter);
//		System.out.println(Option_EnterServer.class.getSimpleName() + ", [ok]");
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
