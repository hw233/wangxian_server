package com.fy.engineserver.menu.quizActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家取消关闭答题活动
 * @author Administrator
 *
 */
public class Option_UnConfirm_Close extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		
	}

}
