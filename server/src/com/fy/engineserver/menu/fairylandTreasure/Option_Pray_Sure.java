package com.fy.engineserver.menu.fairylandTreasure;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class Option_Pray_Sure extends Option implements NeedCheckPurview {
	private byte prayType;
	private String buffName;

	public Option_Pray_Sure() {
	}

	public Option_Pray_Sure(byte prayType, String buffName) {
		this.prayType = prayType;
		this.buffName = buffName;
	}

	@Override
	public void doSelect(Game game, Player player) {
		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		try {
			long cost = ftm.getPrayCost();
			if (cost < player.getSilver()) {
				ftm.fireBuff(player, buffName, prayType, ftm.getPrayLastingTime());
				ftm.logger.warn(player.getLogString() + "[" + buffName + ":" + prayType + "] [类型:" + ftm.prayTypeMap.get((int) prayType) + "]");
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.仙界宝箱祈福, "仙界宝箱");
				player.sendError(Translate.translateString(Translate.祈福成功, new String[][] { { Translate.STRING_1, ftm.prayTypeMap.get((int) prayType) } }));
			} else {
				player.sendError(Translate.余额不足);
				return;
			}
		} catch (NoEnoughMoneyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BillFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public byte getPrayType() {
		return prayType;
	}

	public void setPrayType(byte prayType) {
		this.prayType = prayType;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

}
