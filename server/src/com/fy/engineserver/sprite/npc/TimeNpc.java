package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;

/**
 * 采集NPC
 * 
 * 
 * 
 *
 */
public class TimeNpc extends NPC implements Cloneable{
	
	private static final long serialVersionUID = -4393238389156456469L;

	/**
	 * 秒为单位
	 */
	int lastingTime;
	
	private long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	
	public int getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(int lastingTime) {
		this.lastingTime = lastingTime;
	}
	
	
	
	public void heartbeat(long heartBeatStartTime, long interval, Game game){
		super.heartbeat(heartBeatStartTime, interval, game);
		
		if(this.isAlive() == false) return;
		
		if(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime > lastingTime * 1000){
			this.setAlive(false);
		}
	}
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		TimeNpc p = new TimeNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
