package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Song extends Option{

	long pID;
	int flowType;
	int flowNum;
	public Option_Song(long pID, int flowType, int flowNum){
		super();
		this.pID = pID;
		this.flowType = flowType;
		this.flowNum = flowNum;
	}
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().opt_targetSend(player, pID, flowType, flowNum);
	}
}
