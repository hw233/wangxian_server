package com.fy.engineserver.economic.charge.card;

import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.economic.charge.UrlArgsReturnChargeMode;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;

/**
 * PPCMCC
 * 
 * 
 */
public class PPCmccChargeMode extends UrlArgsReturnChargeMode {

	public PPCmccChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public synchronized String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		return super.doCharge(player, passport, clientChannel, os, chargeMoneyConfigure, parms);
//		BossClientService bossClientService = BossClientService.getInstance();
//		GameConstants gameConstants = GameConstants.getInstance();
//		String res = "";
//		try {
//			res = bossClientService.savingForChannelUser(player.getUsername(), 1, getModeName(), parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), clientChannel, os);
//		} catch (Exception e) {
//			notice = "提交异常,请联系GM!";
//			ChargeManager.logger.error(player.getLogString() + "[充值异常] [doCharge] [卡类型:" + getModeName() + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "]", e);
//		}
//		if (ChargeManager.logger.isWarnEnabled()) {
//			ChargeManager.logger.warn(player.getLogString() + "[充值提交] [doCharge] [卡类型:" + getModeName() + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "] [结果:" + notice + "]");
//		}
//
//		if (res != null && !res.equals("") && res.toLowerCase().startsWith("http")) {
//			// 认为返回的是充值URL返回
//			NOTICE_OPEN_URL_REQ req = new NOTICE_OPEN_URL_REQ(GameMessageFactory.nextSequnceNum(), res);
//			player.addMessageToRightBag(req);
//			if (ChargeManager.logger.isWarnEnabled()) {
//				ChargeManager.logger.warn(player.getLogString() + " [充值方式:" + getModeName() + "] [金额:" + chargeMoneyConfigure.getDescription() + "] [充值返回地址] [" + res + "]");
//			}
//		}
//		return notice;
	}
}
