package com.fy.engineserver.menu;

import com.fy.engineserver.auction.service.AuctionManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class Option_AuctionCancel extends Option {

	long id;
	public Option_AuctionCancel(long id){
		this.id = id;
	}
	
	public void doSelect(Game game, Player player) {
		AuctionManager.getInstance().realCancel(player, id);
	}
	
	@Override
	public byte getOType() {
		// TODO Auto-generated method stub
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
