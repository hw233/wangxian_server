package com.fy.engineserver.menu.disaster;

import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 报名金猴天灾活动
 * 
 * @date on create 2016年3月10日 下午1:52:49
 */
public class Option_signUp extends Option{

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		DisasterManager.getInst().signUp(player);
	}
}
