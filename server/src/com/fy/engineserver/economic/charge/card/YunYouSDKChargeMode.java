package com.fy.engineserver.economic.charge.card;

import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

/**
 * 云游SDK充值
 * 
 * 
 */
public class YunYouSDKChargeMode extends ChargeMode {

	public YunYouSDKChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public synchronized String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		return super.doCharge(player, passport, clientChannel, os, chargeMoneyConfigure, parms);
	}
}
