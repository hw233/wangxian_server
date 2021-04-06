package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_LiHunOk extends Option{

	int type;					//是否强制   0是不强制   1是强制 
	public Option_LiHunOk(int type){
		super();
		this.type = type;
	}
	
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		if(type==0){
			MarriageManager.getInstance().sendLihun2Other(player);
		}else{
			MarriageManager.getInstance().real_lihun(player, 1);
		}
	}
}
