package com.fy.engineserver.menu.fairyBuddha;

import java.util.List;

import com.fy.engineserver.activity.fairyBuddha.DefaultFairyNpcData;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.FIND_WAY_RES;
import com.fy.engineserver.sprite.Player;

public class Option_SetDeclare extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		// TODO 寻路
		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		List<DefaultFairyNpcData> defaultNpcList = fbm.getDefaultNpcList();
		for (DefaultFairyNpcData dfnd : defaultNpcList) {
			if (dfnd.getCareer() == player.getCurrSoul().getCareer()) {
				FIND_WAY_RES res = new FIND_WAY_RES();
				res.setIntx(dfnd.getPointX());
				res.setInty(dfnd.getPointY());
				res.setMapName(dfnd.getMapName());
				FairyBuddhaManager.logger.error("[仙尊] [" + player.getLogString() + "] [要寻路到" + dfnd.getMapName() + "] [x:"+dfnd.getPointX()+",Y:"+dfnd.getPointY()+"]");
				player.addMessageToRightBag(res);
			}
		}
	}

	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
