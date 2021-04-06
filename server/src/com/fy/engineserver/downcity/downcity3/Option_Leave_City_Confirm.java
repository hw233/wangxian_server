package com.fy.engineserver.downcity.downcity3;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Leave_City_Confirm extends Option {

	public void doSelect(Game game, Player p) {
		p.getCurrentGame().transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
		BossCityManager.logger.warn("[通知玩家副本结束] [副本结束玩家传送] [{}]",new Object[]{p.getLogString()});
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
