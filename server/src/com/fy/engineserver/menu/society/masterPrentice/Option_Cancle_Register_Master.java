package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 取消收徒登记
 * @author Administrator
 *
 */
public class Option_Cancle_Register_Master extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		
		// 师傅取消(true)
		boolean opration = true;
		String result = MasterPrenticeManager.getInstance().cancleRegister(player, opration);
		if(result.equals("")){
			MasterPrenticeManager.logger.error("[取消成功] [{}] [{}] [{}] []", new Object[]{player.getId(),player.getName(),player.getUsername()});
		}else{
			player.sendError(result);
			MasterPrenticeManager.logger.error("[取消失败] [{}] [{}] [{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),result});
		}
	}
	
}
