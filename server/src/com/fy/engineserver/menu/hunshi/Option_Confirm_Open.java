package com.fy.engineserver.menu.hunshi;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HUNSHI2_CELL_BUY_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class Option_Confirm_Open extends Option {
	private Horse h;
	private int index;
	private long needSilver;

	public Option_Confirm_Open(Horse h, int index, long needSilver) {
		this.h = h;
		this.index = index;
		this.needSilver = needSilver;
	}

	public Option_Confirm_Open() {
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getSilver() < needSilver) {
			player.sendError(Translate.余额不足);
			player.addMessageToRightBag(new HUNSHI2_CELL_BUY_RES(GameMessageFactory.nextSequnceNum(), index, false));
			return;
		}
		try {
			BillingCenter.getInstance().playerExpense(player, needSilver, CurrencyType.YINZI, ExpenseReasonType.魂石格子开启, "魂石格子开启");
			h.getHunshi2Open()[index] = true;
			h.setHunshi2Open(h.getHunshi2Open());
			if (HunshiManager.getInstance().logger.isWarnEnabled()) HunshiManager.getInstance().logger.warn(player.getLogString() + "[套装魂石格子开启] [horseId:" + h.getHorseId() + "] [index:" + index + "]");
			player.addMessageToRightBag(new HUNSHI2_CELL_BUY_RES(GameMessageFactory.nextSequnceNum(), index, true));
		} catch (NoEnoughMoneyException e) {
			player.sendError(Translate.余额不足);
			player.addMessageToRightBag(new HUNSHI2_CELL_BUY_RES(GameMessageFactory.nextSequnceNum(), index, false));
			e.printStackTrace();
		} catch (BillFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public Horse getH() {
		return h;
	}

	public void setH(Horse h) {
		this.h = h;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getNeedSilver() {
		return needSilver;
	}

	public void setNeedSilver(long needSilver) {
		this.needSilver = needSilver;
	}

}
