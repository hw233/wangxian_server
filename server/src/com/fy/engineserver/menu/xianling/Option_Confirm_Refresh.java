package com.fy.engineserver.menu.xianling;

import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_Confirm_Refresh extends Option {
	
	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		XianLingManager xlm = XianLingManager.instance;
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData.getNextRefreshTime() <= System.currentTimeMillis()) {
			xlm.refreshTimedTask(player, false);
		} else {
			xlm.refreshTimedTask(player, true);
		}
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
