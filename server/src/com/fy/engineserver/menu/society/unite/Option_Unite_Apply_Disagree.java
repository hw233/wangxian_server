package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 其他人不同意
 * @author Administrator
 *
 */
public class Option_Unite_Apply_Disagree extends Option {
	
	
//	private Unite u;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		UniteManager.getInstance().member_disAgree(player);
	}

//	public Unite getU() {
//		return u;
//	}
//
//
//	public void setU(Unite u) {
//		this.u = u;
//	}
	

	
	

}
