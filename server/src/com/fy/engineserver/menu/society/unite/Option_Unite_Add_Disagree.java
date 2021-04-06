package com.fy.engineserver.menu.society.unite;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 不同意
 * @author Administrator
 *
 */
public class Option_Unite_Add_Disagree extends Option {
	
	private Player p1;
	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		UniteManager.getInstance().member_disAgree_add_unite(player,p1);
	}
	

}
