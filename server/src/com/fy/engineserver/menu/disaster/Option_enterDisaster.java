package com.fy.engineserver.menu.disaster;

import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
/**
 * 进入金猴天灾活动
 * 
 * @date on create 2016年3月8日 下午2:29:28
 */
public class Option_enterDisaster extends Option{
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		DisasterManager.getInst().optionEnterDisaster(player);
	}
}
