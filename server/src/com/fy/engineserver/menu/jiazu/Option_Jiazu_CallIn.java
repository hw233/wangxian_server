package com.fy.engineserver.menu.jiazu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Jiazu_CallIn extends Option {

	private Game targetGame;
	private int targetX;
	private int targetY;
	private Player master;

	private int masterTransferGameCountry;

	public Option_Jiazu_CallIn(Game targetGame, Player master) {
		super();
		this.targetGame = targetGame;
		this.master = master;
		this.targetX = master.getX();
		this.targetY = master.getY();
		this.masterTransferGameCountry = master.getTransferGameCountry();

	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (!master.isOnline()) {

			player.sendError(master.getName() + Translate.text_jiazu_001);
			return;
		}
		if (master.getJiazuId() != player.getJiazuId()) {
			return;
		}
		String oldTranGame = player.getLastTransferGame();
		if (player.getLastTransferGame() == null || "".equals(player.getLastTransferGame())) {
			player.setLastTransferGame(player.getResurrectionMapName());
			player.setLastX(player.getResurrectionX());
			player.setLastY(player.getResurrectionY());
		}

		player.setTransferGameCountry(this.masterTransferGameCountry);

		if (player.getCurrentGame() != null) {
			player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, targetGame.gi.name, targetX, targetY));
			JiazuSubSystem.logger.warn("[目标地图:"+targetGame+"] [目标国家:"+this.masterTransferGameCountry+"] [目标xy:("+targetX+","+targetY+")] [oldTranGame:"+oldTranGame+"] [族长:" + master.getLogString() + "] [召唤族员:" + player.getLogString() + "] [弹框成功]");
		} else {
			JiazuSubSystem.logger.warn("[族长:" + master.getLogString() + "] [召唤族员:" + player.getLogString() + "] [族员当前地图不存在]");
		}
	}
}
