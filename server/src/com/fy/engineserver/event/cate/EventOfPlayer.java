package com.fy.engineserver.event.cate;

import com.fy.engineserver.event.Event;
import com.fy.engineserver.sprite.Player;

/**
 * 
 * create on 2013年7月29日
 */
public abstract class EventOfPlayer extends Event{
	public Player player;
	public EventOfPlayer(Player p){
		player = p;
	}
}
