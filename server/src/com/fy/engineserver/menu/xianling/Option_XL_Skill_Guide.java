package com.fy.engineserver.menu.xianling;

import com.fy.engineserver.activity.xianling.XianLing;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HELP_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;

public class Option_XL_Skill_Guide extends Option implements NeedCheckPurview {
	@Override
	public void doSelect(Game game, Player player) {
		// WindowManager windowManager = WindowManager.getInstance();
		// MenuWindow mw = windowManager.createTempMenuWindow(600);
		// mw.setDescriptionInUUB(Translate.仙灵技能介绍);
		// if (XianLingManager.logger.isDebugEnabled()) XianLingManager.logger.debug("[仙灵] [技能介绍] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString());
		// Option_UnConfirm_Close o1 = new Option_UnConfirm_Close();
		// o1.setText(Translate.确定);
		// mw.setOptions(new Option[] { o1 });
		// player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
		player.addMessageToRightBag(new HELP_RES(GameMessageFactory.nextSequnceNum(), Translate.仙灵技能介绍));

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
		return true;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
