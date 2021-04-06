package com.fy.engineserver.menu.cave;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 离开庄园
 * 
 * 
 */
public class Option_Cave_Leave extends CaveOption implements FaeryConfig, NeedCheckPurview {

	public Option_Cave_Leave() {

	}

	@Override
	public void doSelect(Game game, Player player) {
		player.transferGameCountry = player.getCountry();

		if (player.getLastTransferGame() == null || "".equals(player.getLastTransferGame())) {
			if (FaeryManager.logger.isInfoEnabled()) {
				FaeryManager.logger.info(player.getLogString() + "[option] [离开庄园] [上次传送地图为空] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
			}
			player.setLastTransferGame(player.getResurrectionMapName());
			player.setLastX(player.getResurrectionX());
			player.setLastY(player.getResurrectionY());
		}
		if (FaeryManager.logger.isInfoEnabled()) {
			FaeryManager.logger.info(player.getLogString() + "[option] [离开庄园] [LastTransferGame:{}] [X:{}] [Y:{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
		}
		player.transferGameCountry = player.getCountry();
		game.transferGame(player, new TransportData(0, 0, 0, 0, player.getLastTransferGame(), player.getLastX(), player.getLastY()));
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}
}
