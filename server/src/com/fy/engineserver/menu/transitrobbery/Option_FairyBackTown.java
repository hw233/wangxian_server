package com.fy.engineserver.menu.transitrobbery;

import com.fy.engineserver.activity.fairyRobbery.FairyRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_FairyBackTown extends Option{
	
	private Game game;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		FairyRobberyManager.回城(player, game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	

}
