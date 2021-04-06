package com.fy.engineserver.event.cate;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * create on 2013年7月29日
 */
public class EventPlayerLogin extends EventOfPlayer{

	public EventPlayerLogin(Player p) {
		super(p);
	}

	@Override
	public void initId() {
		id = Event.PLAYER_LOGIN;
	}
	
}
