package com.fy.engineserver.menu.fairyBuddha;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_show_last_electors extends Option {
	private byte career;

	@Override
	public void doSelect(Game game, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.仙尊)) {
			player.sendError(Translate.合服功能关闭提示);
			return;
		}

		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		fbm.send_FAIRY_SHOW_ELECTORS_RES(player, career, -1);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

}
