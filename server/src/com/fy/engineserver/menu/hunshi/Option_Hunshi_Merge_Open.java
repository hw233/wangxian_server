package com.fy.engineserver.menu.hunshi;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HUNSHI_OPEN_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Hunshi_Merge_Open extends Option implements NeedCheckPurview {
	@Override
	public void doSelect(Game game, Player player) {
		HunshiManager hm = HunshiManager.getInstance();
		if (hm == null) return;
		player.addMessageToRightBag(new HUNSHI_OPEN_RES(GameMessageFactory.nextSequnceNum(), Translate.魂石合成描述));
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if(player.getLevel()<151){
			return false;
		}
		return true;
	}
}
