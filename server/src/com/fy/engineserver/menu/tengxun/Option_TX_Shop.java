package com.fy.engineserver.menu.tengxun;

import com.fy.engineserver.activity.tengXun.TengXunActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.tengxun.TengXunDataManager;

public class Option_TX_Shop extends Option implements NeedCheckPurview {

	public Option_TX_Shop(){}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		TengXunActivityManager.instance.option_mozhuanShop(player);
	}

	@Override
	public boolean canSee(Player player) {
		if (TengXunDataManager.instance.getGameLevel(player.getId()) > 0) {
			if (TengXunActivityManager.instance.isShopActivityOpen()) {
				return true;
			}
		}
		return false;
	}
}
