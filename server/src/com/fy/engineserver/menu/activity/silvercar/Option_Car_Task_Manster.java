package com.fy.engineserver.menu.activity.silvercar;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 家族族长接受的运镖任务菜单
 * 
 * 
 */
public class Option_Car_Task_Manster extends Option {

	private String taskName;

	@Override
	public void doSelect(Game game, Player player) {
		super.doSelect(game, player);
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
}
