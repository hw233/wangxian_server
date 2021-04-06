package com.fy.engineserver.menu.tengxun;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;

public class Option_TX_getWeekLiBao extends Option implements NeedCheckPurview {

	public Option_TX_getWeekLiBao() {}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		TengXunDataManager.instance.opt_getWeekLiBao(player);
	}
	
	
	@Override
	public boolean canSee(Player player) {
		if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0) {
			return true;
		}
		return false;
	}

}
