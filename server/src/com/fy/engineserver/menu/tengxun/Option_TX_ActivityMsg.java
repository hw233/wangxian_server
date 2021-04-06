package com.fy.engineserver.menu.tengxun;

import com.fy.engineserver.activity.tengXun.TengXunActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_TX_ActivityMsg extends Option {

	public Option_TX_ActivityMsg() {}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		TengXunActivityManager.instance.option_activityMsg(player);
	}
	
}
