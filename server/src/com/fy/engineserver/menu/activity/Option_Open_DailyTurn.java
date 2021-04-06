package com.fy.engineserver.menu.activity;

import java.util.Iterator;

import com.fy.engineserver.activity.dailyTurnActivity.DailyTurnManager;
import com.fy.engineserver.activity.dailyTurnActivity.model.DiskFileModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel;
import com.fy.engineserver.activity.dailyTurnActivity.model.TurnModel4Client;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.OPEN_DAILY_TURN_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
/**
 * 打开每日转盘
 * @author Administrator
 *
 */
public class Option_Open_DailyTurn extends Option implements NeedCheckPurview{
	
	@Override
	public void doSelect(Game game, Player player) {
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			player.sendError(Translate.活动未开启);
			return;
		}
		if (player.getLevel() < DailyTurnManager.instance.openLevelLimit) {
			player.sendError(String.format(Translate.等级不够无法打开界面, DailyTurnManager.instance.openLevelLimit));
			return ;
		}
		long now = System.currentTimeMillis();
		DiskFileModel model = DailyTurnManager.instance.getDiskFileModel(player, now);
		model.reset(now, player);
		Iterator<Integer> ite = DailyTurnManager.instance.turnMaps.keySet().iterator();
		String[] turnNames = new String[DailyTurnManager.instance.turnMaps.size()];
		int[] turnIds = new int[DailyTurnManager.instance.turnMaps.size()];
		int index = 0;
		int tempKey = -1;
		while (ite.hasNext()) {
			int key = ite.next();
			if (index == 0) {
				tempKey = key;
			}
			TurnModel tm = DailyTurnManager.instance.turnMaps.get(key);
			turnNames[index] = tm.getTurnName();
			turnIds[index] = tm.getTurnId();
			index++;
		}
		TurnModel4Client info = DailyTurnManager.instance.getTurnModel4Client(player, tempKey);
		OPEN_DAILY_TURN_WINDOW_RES resp = new OPEN_DAILY_TURN_WINDOW_RES(GameMessageFactory.nextSequnceNum(), turnNames, turnIds, info);
		player.addMessageToRightBag(resp);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		if (!DailyTurnManager.instance.isDailyTurnOpen()) {
			return false;
		}
		return true;
	}

}
