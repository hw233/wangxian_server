package com.fy.engineserver.menu.activity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CHARGE_AGRS_RES;
import com.fy.engineserver.sprite.Player;

public class Option_MonthCard_Charge_Sure extends Option {


	private CHARGE_AGRS_RES res;
	
	public CHARGE_AGRS_RES getRes() {
		return this.res;
	}

	public void setRes(CHARGE_AGRS_RES res) {
		this.res = res;
	}

	public Option_MonthCard_Charge_Sure() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		player.addMessageToRightBag(res);
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
