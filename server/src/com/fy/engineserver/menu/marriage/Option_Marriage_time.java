package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Marriage_time extends Option{

	int time;
	boolean isAgree;
	public Option_Marriage_time(int time, boolean isAgree){
		super();
		this.time = time;
		this.isAgree = isAgree;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().option_time(player, time, isAgree);
	}
}
