package com.fy.engineserver.menu.marriage;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 求婚2次确认
 * 
 *
 */
public class Option_Marriage_BeqOK extends Option{

	int selectIndex;
	String playerName;
	String say;
	
	public Option_Marriage_BeqOK(int selectIndex, String playerName, String say){
		super();
		this.selectIndex = selectIndex;
		this.playerName = playerName;
		this.say = say;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		MarriageManager.getInstance().real_Beq(player, selectIndex, playerName, say);
	}
	
}
