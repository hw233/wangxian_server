package com.fy.engineserver.menu.xianling;

import com.fy.engineserver.activity.xianling.XianLingChallenge;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_ExitBuZhuo extends Option {
	private XianLingChallenge xc;

	public Option_Confirm_ExitBuZhuo(XianLingChallenge xc) {
		super();
		this.xc = xc;
	}

	public Option_Confirm_ExitBuZhuo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSelect(Game game, Player player) {
		xc.setResult(xc.USETRANSPROP);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public XianLingChallenge getXc() {
		return xc;
	}

	public void setXc(XianLingChallenge xc) {
		this.xc = xc;
	}

}
