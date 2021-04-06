package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_Vote extends Option{
	
	@Override
	public void doSelect(Game game, Player player) {
//		CountryManager.getInstance().发送官员名称(player);
		CountryManager.getInstance().得到投票结果(player);
		player.countryVoteFlag = true;
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
