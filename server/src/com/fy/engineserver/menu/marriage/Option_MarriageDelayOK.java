package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_MarriageDelayOK extends Option{
	int time;			//时间
	int type;			//0为同意，1为不同意
	public Option_MarriageDelayOK(int time, int type){
		super();
		this.time = time;
		this.type = type;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game,Player player){
		MarriageManager.getInstance().option_DelayTimeOK(player, time, type);
	}
}
