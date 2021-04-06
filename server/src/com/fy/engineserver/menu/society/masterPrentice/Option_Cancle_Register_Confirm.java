package com.fy.engineserver.menu.society.masterPrentice;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.OptionConstants;
import com.fy.engineserver.sprite.Player;

/**
 * 确认取消收徒拜师登记
 * @author Administrator
 *
 */
public class Option_Cancle_Register_Confirm extends Option {
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_师徒;
	}
	
	private boolean masterOrPrentice;
	
	public boolean isMasterOrPrentice() {
		return masterOrPrentice;
	}
	public void setMasterOrPrentice(boolean masterOrPrentice) {
		this.masterOrPrentice = masterOrPrentice;
	}
	@Override
	public void doSelect(Game game, Player player) {
		
		MasterPrenticeManager.getInstance().cancleRegisterConfirm(player, masterOrPrentice);
	}
	
}
