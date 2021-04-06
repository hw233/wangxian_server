package com.fy.engineserver.core.event;

import com.fy.engineserver.sprite.Player;

public class LeaveGameEvent implements Event {
	private final Player player;

	public LeaveGameEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void handle() {
		// TODO Auto-generated method stub
		
	}
	
	
}
