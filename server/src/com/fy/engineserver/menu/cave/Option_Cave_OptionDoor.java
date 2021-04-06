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

public class Option_Cave_OptionDoor extends CaveOption implements NeedCheckPurview {

	@Override
	public void doSelect(Game game, Player player) {

		CaveNPC caveNPC = getNpc();
		Cave cave = caveNPC.getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_109);
			return;
		}
		CompoundReturn cr = cave.openDoor(player);
		if (!cr.getBooleanValue()) {
			switch (cr.getIntValue()) {
			case 1:// 钱不够
				player.sendError(Translate.您今天可使用的绑银不足);
				break;

			default:
				break;
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		CaveNPC caveNPC = getNpc();
		Cave cave = caveNPC.getCave();
		if (cave.getFence().getOpenStatus() == FaeryConfig.FENCE_STATUS_OPEN) {
			return false;
		}
		return true;
	}
}
