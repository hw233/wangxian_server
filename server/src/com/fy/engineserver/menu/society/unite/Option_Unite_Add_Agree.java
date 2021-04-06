package com.fy.engineserver.menu.society.unite;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.unite.UniteManager;

/**
 * 成员同意
 * @author Administrator
 *
 */
public class Option_Unite_Add_Agree extends Option {
	
	private List<Boolean> stateList;
	private Player p1;

	public Player getP1() {
		return p1;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public List<Boolean> getStateList() {
		return stateList;
	}

	public void setStateList(List<Boolean> stateList) {
		this.stateList = stateList;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_结义;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		UniteManager.getInstance().member_agree_add_unite(player,p1,stateList);
	}

}
