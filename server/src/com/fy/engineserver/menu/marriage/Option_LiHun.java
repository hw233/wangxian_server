package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_LiHun extends Option{

	private int liHuntype;					//是否强制   0是不强制   1是强制
	
	public Option_LiHun(){
		super();
	}

	public Option_LiHun(int type){
		super();
		this.liHuntype = type;
	}
	
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().option_lihun(player, liHuntype);
	}

	public void setLiHuntype(int liHuntype) {
		this.liHuntype = liHuntype;
	}

	public int getLiHuntype() {
		return liHuntype;
	}
}
