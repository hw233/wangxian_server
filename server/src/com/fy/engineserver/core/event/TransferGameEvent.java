package com.fy.engineserver.core.event;

import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.sprite.Player;

public class TransferGameEvent implements Event {
	private final Player player;
	TransportData td;
	public TransferGameEvent(Player player,TransportData td) {
		this.player = player;
		this.td = td;
	}

	public Player getPlayer() {
		return player;
	}
	
	public TransportData getTransportData(){
		return td;
	}

	public void handle() {
		// TODO Auto-generated method stub
		
	}
	
	
}
