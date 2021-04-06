package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;

public class Option_TradeConfirm4CancleRB extends Option {

	private long rbId;

	public Option_TradeConfirm4CancleRB(long rbId) {
		this.rbId = rbId;
	}
	
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		RequestBuyManager.getInstance().relCancel(player, rbId);
	}
}
