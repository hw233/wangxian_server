package com.fy.engineserver.menu.carbon;

import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmEnterCarbon extends Option{
	private int carbonLevel;

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		DevilSquareManager.instance.notifyEnterCarbon(player, getCarbonLevel(), true);
	}
	
	public int getCarbonLevel() {
		return carbonLevel;
	}

	public void setCarbonLevel(int carbonLevel) {
		this.carbonLevel = carbonLevel;
	}

}
