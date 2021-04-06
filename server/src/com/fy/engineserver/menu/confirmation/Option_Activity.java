package com.fy.engineserver.menu.confirmation;

import com.fy.confirm.bean.GameActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PUSH_CONFIRMACTION_ACTIVITY_RES;
import com.fy.engineserver.sprite.Player;

public class Option_Activity extends Option {

	private GameActivity activity;

	public Option_Activity(GameActivity activity) {
		this.activity = activity;
	}

	@Override
	public void doSelect(Game game, Player player) {
		int prizeSize = 0;
		if (activity.getPrizes() != null) {
			prizeSize = activity.getPrizes().length;
		}
		// player.check
		PUSH_CONFIRMACTION_ACTIVITY_RES res = new PUSH_CONFIRMACTION_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), activity);
		player.addMessageToRightBag(res);
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
