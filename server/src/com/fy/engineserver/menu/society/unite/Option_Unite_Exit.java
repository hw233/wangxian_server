package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 退出结义
 * @author Administrator
 *
 */
public class Option_Unite_Exit extends Option {

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	
	@Override
	public void doSelect(Game game, Player player) {
		
		UniteManager.getInstance().exit_unite(player);
		
	}
}
