package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel4Client;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.BUY_EXTRA_TIMES4TURN_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
/**
 * 购买转盘次数二次确认
 * @author Administrator
 *
 */
public class Option_ConfirmBuyDailyTimes extends Option{
	private int turnType;
	
	@Override
	public void doSelect(Game game, Player player) {
		DailyTurnManager.instance.buyExtraTimes(player, turnType, true);
		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, turnType);
		BUY_EXTRA_TIMES4TURN_RES resp = new BUY_EXTRA_TIMES4TURN_RES(GameMessageFactory.nextSequnceNum(), info);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public int getTurnType() {
		return turnType;
	}

	public void setTurnType(int turnType) {
		this.turnType = turnType;
	}

	
}
