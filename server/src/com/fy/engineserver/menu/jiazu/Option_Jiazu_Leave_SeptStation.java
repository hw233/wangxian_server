package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 离开驻地
 * 
 * 
 */
public class Option_Jiazu_Leave_SeptStation extends Option {

	public Option_Jiazu_Leave_SeptStation() {

	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		if (player.getLastTransferGame() == null || "".equals(player.getLastTransferGame())) {
			String mapname = TransportData.getXinShouCityMap(player.getCountry());
			Game defaultTransferGame = GameManager.getInstance().getGameByName(mapname, player.getCountry());
			
			if (defaultTransferGame == null) {
				defaultTransferGame = GameManager.getInstance().getGameByName(player.getResurrectionMapName(), player.getCountry());
			} else {
				MapArea mapArea = defaultTransferGame.getGameInfo().getMapAreaByName(Translate.出生点);
				if (mapArea == null) {
					player.sendError(Translate.text_jiazu_005);
					if (JiazuSubSystem.logger.isWarnEnabled()) {
						JiazuSubSystem.logger.warn(player.getLogString() + "[离开家族驻地][传送到默认点][地图区域不存在]");
					}
					return;
				}
				game.transferGame(player, new TransportData(0, 0, 0, 0, "", defaultTransferGame.gi.name, mapArea.getY(), mapArea.getY()));
				if (JiazuSubSystem.logger.isDebugEnabled()) {
					JiazuSubSystem.logger.debug(player.getLogString() + "[离开家族驻地][player.getLastTransferGame()空][传送到默认点]");
				}
			}
		} else {
			game.transferGame(player, new TransportData(0, 0, 0, 0, player.getLastTransferGame(), player.getLastX(), player.getLastY()));
			if (JiazuSubSystem.logger.isDebugEnabled()) {
				JiazuSubSystem.logger.debug(player.getLogString() + "[离开家族驻地][player.getLastTransferGame(){}][{},{}]", new Object[] { player.getLastTransferGame(), player.getLastX(), player.getLastY() });
			}
		}
	}
}
