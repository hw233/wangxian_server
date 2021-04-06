package com.fy.engineserver.menu.fairylandTreasure;

import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasure;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureNpc;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class Option_FairylandTreasure extends Option {

	private FairylandTreasure fairylandTreasure;

	public Option_FairylandTreasure() {
	}

	public Option_FairylandTreasure(FairylandTreasure fairylandTreasure) {
		this.fairylandTreasure = fairylandTreasure;
	}

	@Override
	public void doSelect(Game game, Player player) {
		FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
		try {
			if(fairylandTreasure.isDisappear()){
				player.sendError(Translate.宝箱消失);
				return;
			}
			long cost = ftm.getFairylandBoxList().get(fairylandTreasure.getLevel()).getNeedSilver();
			if (cost < player.getSilver()) {
				fairylandTreasure.setEffect(false);
				fairylandTreasure.setPickingPlayerId(player.getId());
				if (fairylandTreasure.npc != null) {
					fairylandTreasure.game.removeSprite(fairylandTreasure.npc);
					fairylandTreasure.setDisappear(true);
				} else {
					FairylandTreasureManager.logger.warn("[打开仙界宝箱删除npc错误] [npc null]");
				}
				BillingCenter.getInstance().playerExpense(player, cost, CurrencyType.YINZI, ExpenseReasonType.仙界宝箱, "仙界宝箱");
				ftm.send_START_DRAW_RES(player, ftm.getFairylandBoxList().get(fairylandTreasure.getLevel()));
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
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
