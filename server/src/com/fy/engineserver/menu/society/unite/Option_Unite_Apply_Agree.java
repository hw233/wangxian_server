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
public class Option_Unite_Apply_Agree extends Option {
	
	private List<Boolean> stateList;
	private int num;
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
		
		UniteManager.getInstance().member_agree_unite(player,stateList,num);
	}

}
