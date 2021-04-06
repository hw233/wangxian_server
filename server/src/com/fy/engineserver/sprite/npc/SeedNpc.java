package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;

/**
 * 种子NPC
 * 
 * 某某人种下的种子
 * 到时间后开始两个小时种植的那个人可以采集
 * 两小时后其他人可以采集(打开对话页面，上面有采集选项)
 * 
 * 
 * 
 *
 */
public class SeedNpc extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7875170914561677684L;

	/////////////////////////////////////////////////////////////////////
	// 以下为种子NPC内部数据
	/**
	 * 没有成熟状态
	 */
	public static final int UN_RIPE_STATE = 0;
	/**
	 * 成熟种植者可以采摘状态
	 */
	public static final int RIPE_STATE_AND_PLANTPLAYER_ONLY = 1;
	/**
	 * 成熟所有人可以采摘状态
	 */
	public static final int RIPE_STATE_AND_ALLPLAYER_CAN = 2;
	/**
	 * 种植者id
	 */
	private long plantPlayerId;
	
	/**
	 * 种植时的等级
	 */
	private int plantPlayerLevel;
	
	/**
	 * 种植的时间
	 */
	private long plantTime;
	
	/**
	 * 成熟的时间点
	 */
	private long ripeTime;
	
	/**
	 * 种植者采摘的时长
	 */
	private long personalPickupLastingTime;

	/**
	 * 成熟后的形象
	 */
	private int afterRipeImage;
	
	private String fruitName;

	public int getAfterRipeImage() {
		return afterRipeImage;
	}
	public void setAfterRipeImage(int afterRipeImage) {
		this.afterRipeImage = afterRipeImage;
	}

	public String getFruitName() {
		return fruitName;
	}
	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}

	/**
	 * 现在的状态，通过此状态来改变相应的显示内容，0代表还是种子状态，1代表成熟但只有种植者可以采摘状态，2代表所有人都可以采摘状态
	 */
	private int currentState = 0;

	public long getPlantPlayerId() {
		return plantPlayerId;
	}
	public void setPlantPlayerId(long plantPlayerId) {
		this.plantPlayerId = plantPlayerId;
	}
	public int getPlantPlayerLevel() {
		return plantPlayerLevel;
	}
	public void setPlantPlayerLevel(int plantPlayerLevel) {
		this.plantPlayerLevel = plantPlayerLevel;
	}
	public long getPlantTime() {
		return plantTime;
	}
	public void setPlantTime(long plantTime) {
		this.plantTime = plantTime;
	}
	public long getRipeTime() {
		return ripeTime;
	}
	public void setRipeTime(long ripeTime) {
		this.ripeTime = ripeTime;
	}
	public long getPersonalPickupLastingTime() {
		return personalPickupLastingTime;
	}
	public void setPersonalPickupLastingTime(long personalPickupLastingTime) {
		this.personalPickupLastingTime = personalPickupLastingTime;
	}

	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public void heartbeat(long heartBeatStartTime, long interval, Game game){}
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		SeedNpc p = new SeedNpc();
		p.cloneAllInitNumericalProperty(this);
		
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}

}
