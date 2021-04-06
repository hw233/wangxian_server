package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 注册师傅
 * @author Administrator
 *
 */
public class Option_Register_Master extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		try {
			boolean opration = true;//师傅
			// 发布(true)
			String result = MasterPrenticeManager.getInstance().registerMasterOrPrentice(player, opration);
			if(result.equals("")){
				player.sendError(Translate.text_发布成功);
//				MasterPrenticeManager.logger.error("[发布成功] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
				MasterPrenticeManager.logger.error("[发布成功] [{}] [{}] [{}] []", new Object[]{player.getId(),player.getName(),player.getUsername()});
			}else{
				player.sendError(result);
			}
		} catch (Exception e) {
			if(MasterPrenticeManager.logger.isWarnEnabled())
				MasterPrenticeManager.logger.warn("[注册师傅异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
	}
	
}
