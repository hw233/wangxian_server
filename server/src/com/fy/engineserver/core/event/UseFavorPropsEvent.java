package com.fy.engineserver.core.event;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

public class UseFavorPropsEvent implements Event {

	Player owner;
	
	Game game;
	
	int indexOfKnapsack;

	long favorPlayerId;
	
	public void handle() {
		// TODO Auto-generated method stub

	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getIndexOfKnapsack() {
		return indexOfKnapsack;
	}

	public void setIndexOfKnapsack(int indexOfKnapsack) {
		this.indexOfKnapsack = indexOfKnapsack;
	}

	public long getFavorPlayerId() {
		return favorPlayerId;
	}

	public void setFavorPlayerId(long favorPlayerId) {
		this.favorPlayerId = favorPlayerId;
	}

}
