package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.JIAZU_QUERY_STATION_RES;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.service.SeptStationManager;
import com.fy.engineserver.sprite.Player;

public class Option_Jiazu_SelfStation extends Option implements NeedCheckPurview {

	// private Boolean selected = false;

	public Option_Jiazu_SelfStation() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			if (player.getCurrentGame() == null || SeptStationManager.jiazuMapName.contains(player.getCurrentGame().gi.getName())) {
				FaeryManager.logger.error(player.getLogString() + "[要进入家族,其实已经在家族内了,直接返回不处理]");
				return;
			}
			if (player.getJiazuId() <= 0) {
				player.sendError(Translate.你没有家族);
				return;
			}
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				player.sendError(Translate.你没有家族);
				return;
			}

			if (jiazu.getBaseID() <= 0) {
				player.sendError(Translate.text_6357);
				return;
			}

			SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
			if (septStation == null) {
				player.sendError(Translate.text_6357);
				return;
			}
			
			if(player.getCountry() != game.country){
				player.sendError(Translate.请回本国操作);
				return;
			}

			player.markLastGameInfo();
			
			game.playerLeaveGame(player);
			
			JIAZU_QUERY_STATION_RES res = new JIAZU_QUERY_STATION_RES(GameMessageFactory.nextSequnceNum(), septStation.getGame().gi.name, septStation.getGame().gi.name + "_jz_" + jiazu.getJiazuID(), septStation.getGame().gi.getVersion());
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			JiazuSubSystem.logger.error(player.getLogString() + "[点击进入家族地图][异常]", e);
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		if (player.getJiazuId() <= 0) {
			return false;
		}

		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		if (jiazu == null) {
			return false;
		}
		if (jiazu.getBaseID() <= 0) {
			return false;
		}
		return true;
	}
}
