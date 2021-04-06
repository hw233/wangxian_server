package com.fy.engineserver.event.cate;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.sprite.Player;

/**
 * 玩家进入游戏场景，不同于进入游戏，因为场景可以切换（过地图），所以会出现很多次。
 * 
 * create on 2013年7月29日
 */
public class EventPlayerEnterScene extends EventOfPlayer{
	
	public Game game;

	public EventPlayerEnterScene(Player p, Game g) {
		this(p);
		game = g;
	}
	public EventPlayerEnterScene(Player p) {
		super(p);
	}

	@Override
	public void initId() {
		id = Event.PLAYER_ENTER_SCENE;
	}

}
