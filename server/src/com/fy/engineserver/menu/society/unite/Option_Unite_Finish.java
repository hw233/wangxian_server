package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.Unite;

/**
 * 最后确认结义 完成
 * @author Administrator
 *
 */
public class Option_Unite_Finish extends Option {

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	private Unite u;
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	
	@Override
	public void doSelect(Game game, Player player) {
		
//		try {
//			UniteManager.getInstance().unite_finish_confirm(player, u);
//		} catch (Exception e) {
//			UniteManager.logger.warn("[最后确认结义异常] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
//		}
	}

	public Unite getU() {
		return u;
	}



	public void setU(Unite u) {
		this.u = u;
	}
	
	
}
