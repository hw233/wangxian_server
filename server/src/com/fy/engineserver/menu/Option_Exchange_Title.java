package com.fy.engineserver.menu;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.playerTitles.PlayerTitlesActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_Exchange_Title extends Option{
	private int exchangeType ;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		try {
			PlayerTitlesActivityManager.instance.exchangeTitle(player, exchangeType);
		} catch (Exception e) {
			TransitRobberyManager.logger.error("[兑换称号活动] [错误]["+ player.getLogString() + "]", e);
			player.sendError(Translate.服务器出现错误);
		}
	}

	public int getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(int exchangeType) {
		this.exchangeType = exchangeType;
	}
	
}
