package com.fy.engineserver.menu.activity.jiazu2;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_TASK_REFRESH_RES;
import com.fy.engineserver.sprite.Player;

public class Open_refreshTaskConfirm extends Option{
	@Override
	public void doSelect(Game game, Player player) {
		boolean r = JiazuManager2.instance.refreshTaskList(player, false, true);
		JIAZU_TASK_REFRESH_RES resp = new JIAZU_TASK_REFRESH_RES(GameMessageFactory.nextSequnceNum(), (byte) (r?1:0));
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
