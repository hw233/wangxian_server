package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 篝火NPC
 * 
 * 
 * 
 *
 */
public class FireNpc extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370993L;

//	/**
//	 * 分钟为单位
//	 */
//	int lastingTime = 30;
	
	/**
	 * 间隔时间
	 */
	private long intervalTime;
	
	private long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//	
//	public int getLastingTime() {
//		return lastingTime;
//	}
//
//	public void setLastingTime(int lastingTime) {
//		this.lastingTime = lastingTime;
//	}
	
	
	
	public void heartbeat(long heartBeatStartTime, long interval, Game game){}
	
	/**
	 * 得到烤火经验
	 * @param player
	 * @return
	 */
	private int getExpValue(Player player){
		int expValue = 1;
		expValue = (3375+20*player.getLevel()*player.getLevel())/600+1;
		return expValue;
	}
	
	/**
	 * 判断合适距离
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean rangeValid(int x,int y){
		boolean valid = false;
		double resultValue = (this.x - x)*(this.x - x)+(this.y - y)*(this.y - y);
		double result = Math.sqrt(resultValue);
		if(result <= 100){
			valid = true;
		}
		return valid;
	}
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		FireNpc p = new FireNpc();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
