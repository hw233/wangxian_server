package com.fy.engineserver.menu.charge;

import java.util.Arrays;
import java.util.List;

import com.fy.boss.client.BossClientService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GET_CHARGE_ORDER_MULTIIO_REQ;
import com.fy.engineserver.message.GET_CHARGE_ORDER_MULTIIO_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 移动短信充值提示框
 * 
 * 
 */
public class Option_CmccSMSCharge extends Option {

	private GET_CHARGE_ORDER_MULTIIO_REQ req;

	public Option_CmccSMSCharge() {

	}

	public Option_CmccSMSCharge(GET_CHARGE_ORDER_MULTIIO_REQ req) {
		super();
		this.req = req;
	}

	@Override
	public void doSelect(Game game, Player player) {
		String userChannel = req.getChannel();
		String os = req.getOs();
		String modeName = req.getModeName();
		String confId = req.getId();

		String[] muitiParms = req.getMuitiParms();
		long start = SystemTime.currentTimeMillis();

		int result = 0;
		String orderId = "";

		ChargeManager chargeManager = ChargeManager.getInstance();
		List<ChargeMode> modeList = chargeManager.getChannelChargeModes(userChannel);
		if (modeList == null || modeList.size() == 0) {
			ChargeManager.logger.error(player.getLogString() + "[Option_CmccSMSCharge] [多参数,请求订单号] [失败] [无匹配的渠道充值] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
			player.sendNotice("无匹配的渠道充值");
			return;
		}
		ChargeMoneyConfigure chargeMoneyConfigure = chargeManager.getChargeMoneyConfigures().get(confId);
		if (chargeMoneyConfigure == null) {
			ChargeManager.logger.error(player.getLogString() + "[Option_CmccSMSCharge] [多参数,请求订单号] [失败] [无效的充值面额] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
			player.sendNotice("无效的充值面额");
			return;
		}

		ChargeMode chargeMode = chargeMoneyConfigure.getChargeMode(modeName);
		if (chargeMode == null) {
			ChargeManager.logger.error(player.getLogString() + "[Option_CmccSMSCharge] [多参数,请求订单号] [失败] [无效的充值方式] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]");
			player.sendNotice(Translate.无效的充值方式);
			return;
		}
		try {
			GameConstants gameConstants = GameConstants.getInstance();
			if (ChargeManager.useNewChargeInterface) {
				orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), chargeMoneyConfigure.getSpecialConf(), "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os,new String[]{String.valueOf(player.getId())});
			} else {
				orderId = BossClientService.getInstance().savingForChannelUser(player.getUsername(), 1, chargeMode.getModeName(), chargeMoneyConfigure.getSpecialConf(), "", (int) chargeMoneyConfigure.getDenomination(), gameConstants.getServerName(), userChannel, os);
			}
		} catch (Exception e) {
			result = 1;
			orderId = Translate.通信异常请稍后再试;
			ChargeManager.logger.error(player.getLogString() + "[Option_CmccSMSCharge] [多参数,请求订单号] [异常] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [muitiParms:" + Arrays.toString(muitiParms) + "]", e);
		}
		if (ChargeManager.logger.isWarnEnabled()) {
			ChargeManager.logger.warn(player.getLogString() + "[Option_CmccSMSCharge] [多参数,请求订单号] [成功] [userName:" + player.getUsername() + "] [confId:" + confId + "] [userChannel:" + userChannel + "] [modeName:" + modeName + "] [os:" + os + "] [订单号:" + orderId + "] [muitiParms:" + Arrays.toString(muitiParms) + "] [耗时:" + (SystemTime.currentTimeMillis() - start) + "ms]");
		}
		player.addMessageToRightBag(new GET_CHARGE_ORDER_MULTIIO_RES(GameMessageFactory.nextSequnceNum(), result,chargeMoneyConfigure.getDescription(), chargeMoneyConfigure.getDenomination(), orderId, muitiParms));
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
