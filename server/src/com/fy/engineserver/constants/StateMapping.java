package com.fy.engineserver.constants;

/**
 * Mapping常量，用于获得不同状态下的动画
 * 
 */
public interface StateMapping {
	/** 中毒 **/
	public final static int STATE_POSIONED = 0;
	
	/** 晕 **/
	public final static int STATE_FAINT = 1;
	
	/** 衰弱 **/
	public final static int STATE_WEAK = 2;
	
	/** 定身 **/
	public final static int STATE_HOLD = 3;
	
	/** 无敌 **/
	public final static int STATE_INVINCIBLE = 4;
	
	/** 盾 **/
	public final static int STATE_SHIELD = 5;
	
	/** 免疫 **/
	public final static int STATE_IMMUNE = 6;
	
	/** 有物品可以拣 **/
	public final static int STATE_PICKUP = 7;
}
