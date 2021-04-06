package com.fy.engineserver.economic.charge.card;

import java.util.Arrays;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.economic.charge.UrlArgsReturnChargeMode;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_OPEN_URL_ARGS_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;
import com.fy.boss.client.BossClientService;
import com.xuanzhi.boss.game.GameConstants;

/**
 * web支付宝
 * 
 * 
 */
public class WebZhifubaoChargeMode extends UrlArgsReturnChargeMode {

	public WebZhifubaoChargeMode(String modeName) {
		super(modeName);
	}

	@Override
	public synchronized String doCharge(Player player, Passport passport, String clientChannel, String os, ChargeMoneyConfigure chargeMoneyConfigure, String... parms) {
		/*
		 * ChargeManager.logger.error(player.getLogString() + "[发送充值协议] [疑似客户端错误或者老包] [充值方式" + getModeName() + "] [clientChannel:" + clientChannel + "] [os:" + os + "] [parms:" +
		 * (parms == null ? "null" : Arrays.toString(parms)) + "]");
		 * return "无效充值方式";
		 */

		BossClientService bossClientService = BossClientService.getInstance();
		GameConstants gameConstants = GameConstants.getInstance();
		String notice = Translate.充值提交成功请稍后在充值记录中查询;
		String res = "";
		try {
			if (ChargeManager.useNewChargeInterface) {
				res = bossClientService.savingForChannelUser(player.getUsername(), 1, getModeName(), parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName() + "@" + player.getConn().getRemoteAddress(), clientChannel, os, new String[] { String.valueOf(player.getId()) });
			} else {
				res = bossClientService.savingForChannelUser(player.getUsername(), 1, getModeName(), parms[0], parms[1], (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName() + "@" + player.getConn().getRemoteAddress(), clientChannel, os);
			}
		} catch (Exception e) {
			notice = Translate.提交异常请联系GM;
			ChargeManager.logger.error(player.getLogString() + "[充值异常] [doCharge] [卡类型:" + getModeName() + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "]", e);
		}
		if (ChargeManager.logger.isWarnEnabled()) {
			ChargeManager.logger.warn(player.getLogString() + "[充值提交] [doCharge] [卡类型:" + getModeName() + "] [充值面额ID:" + chargeMoneyConfigure.getId() + "] [渠道:" + clientChannel + "] [平台:" + os + "] [parms:" + Arrays.toString(parms) + "] [结果:" + notice + "] [网关返回:" + res + "]");
		}

		if (res != null && !res.equals("") && res.toLowerCase().startsWith("http")) {
			// 认为返回的是充值URL返回
			NOTICE_OPEN_URL_ARGS_REQ req = new NOTICE_OPEN_URL_ARGS_REQ(GameMessageFactory.nextSequnceNum(), res, getModeName());
			player.addMessageToRightBag(req);
			if (ChargeManager.logger.isWarnEnabled()) {
				ChargeManager.logger.warn(player.getLogString() + " [充值方式:" + getModeName() + "] [金额:" + chargeMoneyConfigure.getDescription() + "] [充值返回地址] [" + res + "]");
			}
		}
		return notice;
	}
}
