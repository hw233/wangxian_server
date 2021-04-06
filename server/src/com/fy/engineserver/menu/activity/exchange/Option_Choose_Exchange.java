package com.fy.engineserver.menu.activity.exchange;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 选择那种兑换
 * @author Administrator
 *
 */
public class Option_Choose_Exchange extends Option {
	
	//0酒，1帖
	private int chooseType;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getChooseType() {
		return chooseType;
	}
	public void setChooseType(int chooseType) {
		this.chooseType = chooseType;
	}


	@Override
	public void doSelect(Game game, Player player) {
		try{
			ActivityManager.getInstance().showTrueExchange(player, chooseType);
		}catch (Exception e) {
			ActivitySubSystem.logger.error("[选择兑换异常] ["+player.getLogString()+"]",e);
		}
	}
	
}


