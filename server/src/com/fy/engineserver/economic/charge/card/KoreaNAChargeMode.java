package com.fy.engineserver.economic.charge.card;

import java.util.Arrays;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

/**
 * 乐逗支付
 * 
 * 
 */
public class KoreaNAChargeMode extends ChargeMode {

	public KoreaNAChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public synchronized String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		ChargeManager.logger.error(player.getLogString() + "[发送充值协议] [疑似客户端错误或者老包] [充值方式" + getModeName() + "] [clientChannel:" + clientChannel + "] [os:" + os + "] [parms:" + (parms == null ? "null" : Arrays.toString(parms)) + "]");
		return Translate.无效的充值方式;
	}
}
