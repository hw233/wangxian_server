package com.fy.engineserver.economic.charge.card;

import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

public class KaoPuChargeMode extends ChargeMode {

	public KaoPuChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		// TODO Auto-generated method stub
		return super.doCharge(player, passport, clientChannel, os, chargeMoneyConfigure, parms);
	}

}
