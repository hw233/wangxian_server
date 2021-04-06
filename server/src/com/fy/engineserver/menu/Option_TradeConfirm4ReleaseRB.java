package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.trade.requestbuy.RequestBuyRule;
import com.fy.engineserver.trade.requestbuy.service.RequestBuyManager;

public class Option_TradeConfirm4ReleaseRB extends Option {

	private long perPrice;
	private int num;
	private RequestBuyRule buyRule;

	public Option_TradeConfirm4ReleaseRB(RequestBuyRule buyRule, int num, long perPrice) {
		this.perPrice = perPrice;
		this.num = num;
		this.buyRule = buyRule;
	}

	@Override
	public void doSelect(Game game, Player player) {
		try {
			RequestBuyManager manager = RequestBuyManager.getInstance();
			manager.relReleaseRequestBuy(player, buyRule, num, perPrice);
		} catch (Exception e) {
			if(RequestBuyManager.logger.isWarnEnabled())
				RequestBuyManager.logger.warn("点了按钮",e);
		}
	}
	
	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
