package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class Option_Cave_Cancle_Plant extends CaveOption implements NeedCheckPurview {

	public Option_Cave_Cancle_Plant() {

	}

	@Override
	public void doSelect(Game game, Player player) {

		boolean isSelf = FaeryManager.isSelfCave(player, getNpc().getId());
		if (!isSelf) {
			player.sendError(Translate.text_cave_016);
			return;
		}
		Cave cave = getNpc().getCave();
		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return;
		}
		CompoundReturn cr = cave.cancelPlanting(getNpc().getId(),player);
		String failreason = Translate.text_cave_001;
		if (!cr.getBooleanValue()) {
			switch (cr.getIntValue()) {
			case 1:
				failreason = Translate.text_cave_022;
				break;
			case 2:
				failreason = Translate.text_cave_015;
				break;
			case 3:
				failreason = Translate.text_cave_021;
				break;
			case 4:
				failreason = Translate.text_cave_055;
				break;
			default:
				break;
			}
		}
		player.sendNotice(failreason);
		return;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
