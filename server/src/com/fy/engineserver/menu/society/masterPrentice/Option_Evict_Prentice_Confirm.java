package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 确认逐出徒弟
 * @author Administrator
 *
 */
public class Option_Evict_Prentice_Confirm extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player prentice;
	
	public Player getPrentice() {
		return prentice;
	}
	public void setPrentice(Player prentice) {
		this.prentice = prentice;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		MasterPrenticeManager.getInstance().evictPrenticeConfirm(player, prentice);
	}
	
}
