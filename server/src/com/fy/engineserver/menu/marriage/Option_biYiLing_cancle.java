package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_biYiLing_cancle extends Option{
	public Option_biYiLing_cancle(){
		super();
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().取消召唤(player);
	}
}
