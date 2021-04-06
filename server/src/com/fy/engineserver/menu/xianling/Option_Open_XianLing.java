package com.fy.engineserver.menu.xianling;

import com.fy.engineserver.activity.xianling.XianLing;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_Open_XianLing extends Option implements NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		// 打开主面板
		XianLingManager.instance.handle_OPEN_MAIN_REQ(player);
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getLevel() <= XianLingManager.NEEDLEVEL) {
			return false;
		}
		XianLing activity = XianLingManager.instance.getCurrentActivity();
		if (activity != null && activity.isThisServerFit() != null) {
			return false;
		}
		if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
			return true;
		}
		return false;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
