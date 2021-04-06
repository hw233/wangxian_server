package com.fy.engineserver.activity.fireActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.cache.Cacheable;


public class FireActivityNpc extends NPC implements Cacheable {
	
	private static final long serialVersionUID = 5863409755242303104L;
	private long septId;
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	@Override
	public Object clone() {
		
		FireActivityNpc npc = new FireActivityNpc();
		npc.cloneAllInitNumericalProperty(this);
		npc.setWindowId(this.getWindowId());
		return npc;
	}

	public long getSeptId() {
		return septId;
	}

	public void setSeptId(long septId) {
		this.septId = septId;
	}

}
