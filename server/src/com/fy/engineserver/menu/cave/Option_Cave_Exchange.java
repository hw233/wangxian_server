package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CAVE_NOTICE_CONVERTARTICLE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

/**
 * 兑换资源
 * 
 * 
 */
public class Option_Cave_Exchange extends CaveOption implements FaeryConfig, NeedCheckPurview {

	public Option_Cave_Exchange() {

	}

	@Override
	public void doSelect(Game game, Player player) {

		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave == null) {
			player.sendError(Translate.text_cave_007);
			return;
		}
		CAVE_NOTICE_CONVERTARTICLE_RES res = new CAVE_NOTICE_CONVERTARTICLE_RES(GameMessageFactory.nextSequnceNum());
		player.addMessageToRightBag(res);

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return FaeryManager.isSelfCave(player, getNpc().getId());
	}
}
