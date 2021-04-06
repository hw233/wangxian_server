package com.fy.engineserver.menu.activity.wolf;

import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.WOLF_SIGN_UP_SURE_RES;
import com.fy.engineserver.message.WOLF_STATE_NOTICE_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Wolf_SignUp_Sure extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		String result = WolfManager.getInstance().signUp(player,true);
		if(result != null && result.equals(Translate.报名成功)){
			WOLF_SIGN_UP_SURE_RES res = new WOLF_SIGN_UP_SURE_RES(GameMessageFactory.nextSequnceNum(), true);
			player.addMessageToRightBag(res);
			WOLF_STATE_NOTICE_RES res2 = new WOLF_STATE_NOTICE_RES(GameMessageFactory.nextSequnceNum(), 1);
			player.addMessageToRightBag(res2);
			player.sendError(Translate.报名成功);
		}else{
			player.sendError(result);
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
