package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.MarriageBeq;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 被求婚愿意
 * 
 *
 */
public class Option_Marriage_Beq2Other extends Option {
	
	MarriageBeq beq;
	int type;				//0愿意 还是 1不愿意
	public Option_Marriage_Beq2Other(MarriageBeq beq, int type){
		super();
		this.beq = beq;
		this.type = type;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public void doSelect(Game game,Player player){
		MarriageManager.getInstance().option_beqOtherReq(beq, type);
	}
}
