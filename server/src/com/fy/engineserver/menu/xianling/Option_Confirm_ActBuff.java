package com.fy.engineserver.menu.xianling;

import java.util.Date;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.xianling.MeetMonsterRate;
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
import com.fy.engineserver.message.XL_ACT_MEETMONSTER_BUFF_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.NpcinfoFlow;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.transport.Connection;

public class Option_Confirm_ActBuff extends Option {
	
	@Override
	public void doSelect(Game game, Player player) {
		if(UnitServerFunctionManager.needCloseFunctuin(Function.仙灵大会)) {
			player.sendError(Translate.合服功能关闭提示);
			return ;
		}
		XianLingManager xlm = XianLingManager.instance;
		if (player.getSilver() >= XianLingManager.REFRESH_BUFF_COST) {
			try {
				BillingCenter.getInstance().playerExpense(player, XianLingManager.REFRESH_BUFF_COST, CurrencyType.YINZI, ExpenseReasonType.刷新激活buff, "仙灵刷新激活buff");
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
					npcinfoFlow.setTaskType("刷新遇怪buff");
					npcinfoFlow.setUserName(player.getUsername());
					statClientService.sendNpcinfoFlow("", npcinfoFlow);
				} catch (Exception e) {
					if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.warn("[仙灵] [刷新遇怪buff统计]", e);
				}
				int[] rates = new int[xlm.meetMonsterRateMap.size()];
				for (int i = 0; i < rates.length; i++) {
					rates[i] = xlm.meetMonsterRateMap.get(i + 1).getRate();
				}
				int index = xlm.getIndexByRate(rates);
				MeetMonsterRate mm = xlm.meetMonsterRateMap.get(index + 1);
				// 加buff
				FairylandTreasureManager.fireBuff(player, "遇怪加成"+mm.getBuffId()+"_@@", mm.getBuffId()-1, XianLingManager.REFRESH_BUFF_LASTING);
				// 这里传buff名字改为传描述
				XL_ACT_MEETMONSTER_BUFF_RES res = new XL_ACT_MEETMONSTER_BUFF_RES(GameMessageFactory.nextSequnceNum(), mm.getIcon(), mm.getDes(), XianLingManager.REFRESH_BUFF_LASTING);
				if (XianLingManager.logger.isWarnEnabled()) XianLingManager.logger.warn("[仙灵] [刷新激活buff] [" + player.getLogString() + "] [bufflevel:" + mm.getBuffId() + "] [buffName:" + mm.getName() + "]");
				player.addMessageToRightBag(res);
			} catch (NoEnoughMoneyException e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [刷新激活buff] [银子不足] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString(), e);
			} catch (BillFailedException e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [刷新激活buff] [异常] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString(), e);
			} catch (Exception e) {
				if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [刷新激活buff] [异常] [XianLingManager.handle_ACT_MEETMONSTER_BUFF_REQ]" + player.getLogString(), e);
			}
		} else {
			player.sendError(Translate.银子不足);
		}
	}
	
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
