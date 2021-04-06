package com.fy.engineserver.menu.downcity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAY_DOWNCITY_TUN_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_StartTun extends Option {

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		long[] result = DownCityManager2.instance.startTun(player, true);
		long resultId = result[0];
		int num = (int) result[1];
		int left = (int) result[2];
		if (resultId < 0) {
			return ;
		}
		PLAY_DOWNCITY_TUN_RES resp = new PLAY_DOWNCITY_TUN_RES(GameMessageFactory.nextSequnceNum(), resultId, num, left);
		player.addMessageToRightBag(resp);
	}
}
