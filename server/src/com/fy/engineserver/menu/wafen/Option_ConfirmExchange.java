package com.fy.engineserver.menu.wafen;

import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.EXCHANGE_CHANZI_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmExchange extends Option {
	
	private byte exchangeType;
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		try {
			int leftNum = WaFenManager.instance.exchangeChanZi(player, getExchangeType(), true);
			EXCHANGE_CHANZI_RES resp = new EXCHANGE_CHANZI_RES(GameMessageFactory.nextSequnceNum(), exchangeType, leftNum);
			player.addMessageToRightBag(resp);
		} catch (Exception e) {
			WaFenManager.logger.error("[挖坟活动] [兑换铲子] [异常] [" + player.getLogString() + "] [exchangeType:" + exchangeType + "]", e);
		}
	}

	public byte getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(byte exchangeType) {
		this.exchangeType = exchangeType;
	}
}
