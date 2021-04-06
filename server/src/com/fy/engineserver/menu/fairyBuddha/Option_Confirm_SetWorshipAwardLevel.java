package com.fy.engineserver.menu.fairyBuddha;

import com.fy.engineserver.activity.fairyBuddha.DefaultFairyNpcData;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.StatueForFairyBuddha;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.FAIRY_SET_AWARDLEVEL_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager;
import com.fy.engineserver.uniteserver.UnitServerFunctionManager.Function;

public class Option_Confirm_SetWorshipAwardLevel extends Option {
	private byte level;
	private long cost;
	private String awardName;
	private long messageSequnceNum;

	public Option_Confirm_SetWorshipAwardLevel(byte level, long cost, String awardName, long messageSequnceNum) {
		this.level = level;
		this.cost = cost;
		this.awardName = awardName;
		this.messageSequnceNum = messageSequnceNum;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (UnitServerFunctionManager.needCloseFunctuin(Function.仙尊)) {
			player.sendError(Translate.合服功能关闭提示);
			return;
		}
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		String result = "";
		int succ=0;
		try {
			String key = fbm.getKey(0) + fbm.KEY_膜拜奖励等级 + "_" + player.getId();
			if (level != 0) {
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.设置膜拜等级, "设置膜拜等级");
			}

			fbm.disk.put(key, level);
			result = Translate.translateString(Translate.设置膜拜奖励成功, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) }, { Translate.STRING_2, awardName } });
			succ=1;
			
		} catch (Exception e) {
			result = Translate.出现异常;
			e.printStackTrace();
		}
		try{
			FairyBuddhaInfo fbi=fbm.getFariyBuddhaById(player.getCurrSoul().getCareer(),  player.getId());
			if(fbi!=null){
				fbm.setWorshipOption(player.getCurrSoul().getCareer(), level);
			}
		}catch(Exception e){
			FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [设置膜拜按钮报错]"+e);
			e.printStackTrace();
		}
		FAIRY_SET_AWARDLEVEL_RES res = new FAIRY_SET_AWARDLEVEL_RES(messageSequnceNum, result,succ);
		FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [发送协议FAIRY_SET_AWARDLEVEL_RES]");
		player.addMessageToRightBag(res);
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
