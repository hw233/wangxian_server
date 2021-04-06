package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 查询帮会积分排行榜
 * 
 * @author Administrator
 *
 */
public class Option_QueryGangRank extends Option {
	public void doSelect(Game game,Player player){
		
	}
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_查询帮会积分排行;
	}
}
