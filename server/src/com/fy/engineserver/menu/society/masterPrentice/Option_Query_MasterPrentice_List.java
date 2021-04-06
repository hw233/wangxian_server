package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 查询收徒拜师登记
 * @author Administrator
 *
 */
public class Option_Query_MasterPrentice_List extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {		
	
		int beginNum = 1;
		boolean ptype = true;
		try{
			MasterPrenticeManager.getInstance().queryMessageAll(player, beginNum, ptype);
		}catch(Exception e){
			MasterPrenticeManager.logger.error("[查询拜师收徒登记异常] ["+player.getLogString()+"]",e);
		}
	}
}
