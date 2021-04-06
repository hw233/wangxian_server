package com.fy.engineserver.economic.charge.card;

import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

/**
 * 中国联通卡充值
 * 
 * 
 */
public class CuccChargeMode extends ChargeMode {

	public CuccChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		// TODO Auto-generated method stub
		//
		// BossClientService.getInstance().savingForChannelUser(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return super.doCharge(player, passport, clientChannel, os, chargeMoneyConfigure, parms);
	}
}
