package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.CaveNPC;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Cave_CloseDoor extends CaveOption implements NeedCheckPurview {
	public Option_Cave_CloseDoor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		CaveNPC caveNPC = getNpc();
		Cave cave = caveNPC.getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_109);
			return;
		}
		CompoundReturn cr = cave.closeDoor(player);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		CaveNPC caveNPC = getNpc();
		Cave cave = caveNPC.getCave();
		if (cave.getFence().getOpenStatus() == FaeryConfig.FENCE_STATUS_CLOSE) {
			return false;
		}
		return true;
	}
}
