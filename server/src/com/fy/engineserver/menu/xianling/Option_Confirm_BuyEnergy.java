package com.fy.engineserver.menu.xianling;

import java.util.Date;

import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NEW_USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.XL_BUTENERGY_SURE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.NpcinfoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;

public class Option_Confirm_BuyEnergy extends Option {
	private PlayerXianLingData xianlingData;
	private int needSilver;

	public Option_Confirm_BuyEnergy() {
		// TODO Auto-generated constructor stub
	}

	public Option_Confirm_BuyEnergy(PlayerXianLingData xianlingData, int needSilver) {
		super();
		this.xianlingData = xianlingData;
		this.needSilver = needSilver;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, needSilver, CurrencyType.YINZI, ExpenseReasonType.购买真气, "仙灵购买真气");
			try {
				//统计
				StatClientService statClientService = StatClientService.getInstance();
				NpcinfoFlow npcinfoFlow = new NpcinfoFlow();
				npcinfoFlow.setAward("0");
				CareerManager cm = CareerManager.getInstance();
				npcinfoFlow.setCareer(cm.getCareer(player.getCareer()).getName());
				npcinfoFlow.setColumn1(player.getName());
				npcinfoFlow.setColumn2("");
				npcinfoFlow.setCreateDate(new Date().getTime());
				npcinfoFlow.setFenQu(GameConstants.getInstance().getServerName());
				npcinfoFlow.setGameLevel(player.getLevel());
				npcinfoFlow.setGetDaoJu(0);
				npcinfoFlow.setGetWuPin(0);
				npcinfoFlow.setGetYOuXiBi(0);
				Connection conn = player.getConn();
				if (conn != null) {
					NEW_USER_ENTER_SERVER_REQ mess = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
					if (mess != null) {
						npcinfoFlow.setJixing(mess.getPhoneType());
					}
				}
				npcinfoFlow.setNpcName("仙灵大会");
				npcinfoFlow.setTaskType("购买真气");
				npcinfoFlow.setUserName(player.getUsername());
				statClientService.sendNpcinfoFlow("", npcinfoFlow);
			} catch (Exception e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.warn("[仙灵] [购买真气统计]", e);
			}
			xianlingData.setEnergy(xianlingData.getEnergy() + XianLingManager.单次购买真气点数);
			xianlingData.setBugEntityTimes(xianlingData.getBugEntityTimes() + 1);
			XL_BUTENERGY_SURE_RES res = new XL_BUTENERGY_SURE_RES(GameMessageFactory.nextSequnceNum(), xianlingData.getEnergy(), needSilver + XianLingManager.购买真气增长银两);
			if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵] [确定购买真气] [handle_BUTENERGY_SURE_REQ] [" + player.getLogString() + "] [购买后真气:" + xianlingData.getEnergy() + "] [本次活动已购买次数:" + xianlingData.getBugEntityTimes() + "]");
			player.addMessageToRightBag(res);
			player.sendError(Translate.text_jiazu_150);
		} catch (NoEnoughMoneyException e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [确定购买真气] [异常] [Option_Confirm_SilverRefresh]" + player.getLogString(), e);
			e.printStackTrace();
		} catch (BillFailedException e) {
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [确定购买真气] [异常] [Option_Confirm_SilverRefresh]" + player.getLogString(), e);
			e.printStackTrace();
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public PlayerXianLingData getXianlingData() {
		return xianlingData;
	}

	public void setXianlingData(PlayerXianLingData xianlingData) {
		this.xianlingData = xianlingData;
	}

	public int getNeedSilver() {
		return needSilver;
	}

	public void setNeedSilver(int needSilver) {
		this.needSilver = needSilver;
	}

}
