package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gaiming.GaiMingManager;
import com.fy.engineserver.message.WILL_CHANGE_NAME_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

public class Option_GaiMing extends Option{
	@Override
	public void doSelect(Game game, Player player) {
		int count=player.getKnapsack_common().countArticle(Translate.改名卡);
		if(count>0){
			if(System.currentTimeMillis()-player.getLastGaiMingTime()>GaiMingManager.getInstance().gaimingCD){
				player.addMessageToRightBag(new WILL_CHANGE_NAME_RES());
			}else{
				long leftCD=player.getLastGaiMingTime()+GaiMingManager.getInstance().gaimingCD-System.currentTimeMillis();
				player.sendError(Translate.translateString(Translate.改名太频繁, new String[][]{{Translate.STRING_1,TimeTool.instance.getShowTime(leftCD)}}));
			}
		}else{
			player.sendError(Translate.没有改名道具);
		}
	}
	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
