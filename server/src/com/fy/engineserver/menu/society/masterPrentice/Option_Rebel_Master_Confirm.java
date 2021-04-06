package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 确认背叛师傅
 * @author Administrator
 *
 */
public class Option_Rebel_Master_Confirm extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private Player master;
	
	public Player getMaster() {
		return master;
	}
	public void setMaster(Player master) {
		this.master = master;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		MasterPrenticeManager.getInstance().rebelMasterConfirm(player, master);
	}
	
}
