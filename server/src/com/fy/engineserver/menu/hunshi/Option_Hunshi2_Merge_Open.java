package com.fy.engineserver.menu.hunshi;

import java.util.Set;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HUNSHI2_OPEN_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Hunshi2_Merge_Open extends Option implements NeedCheckPurview {
	@Override
	public void doSelect(Game game, Player player) {
		HunshiManager hm = HunshiManager.getInstance();
		if (hm == null) return;
		Set<String> kindSet = HunshiManager.getInstance().hunshi2KindMap.keySet();
		player.addMessageToRightBag(new HUNSHI2_OPEN_RES(GameMessageFactory.nextSequnceNum(), kindSet.toArray(new String[0])));
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public boolean canSee(Player player) {
		if (player.getLevel() < 151) {
			return false;
		}
		return true;
	}
}
