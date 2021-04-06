package com.fy.engineserver.menu.activity.exchange;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 开始兑换
 * @author Administrator
 *
 */
public class Option_Query_Exchange extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		try{
			ActivityManager.getInstance().beginExchange(player);
		}catch (Exception e) {
			ActivitySubSystem.logger.error("[查询兑换异常] ["+player.getLogString()+"]",e);
		}
	}
	
}


