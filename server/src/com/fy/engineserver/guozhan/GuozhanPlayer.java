package com.fy.engineserver.guozhan;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.sprite.Player;

/**
 * 国战玩家包装，记录了一些和玩家相关的国战数据
 * 
 * @version 创建时间：May 7, 2012 3:41:34 PM
 * 
 */
public class GuozhanPlayer {
	
	public static final int TYPE_进攻 = 0;
	public static final int TYPE_防御 = 1;
	
	public Player player;
	
	public int fightType;
	
	public long lastKillTime;
	
	public int lianshengNum;
	
	public int gongxun;
	
	public long exp;
	
	public long damage;
	
	/**
	 * 击杀列表
	 */
	public List<Long> killList = new ArrayList<Long>();
	
	/**
	 * 我被击杀列表
	 */
	public List<Long> beKilledList = new ArrayList<Long>();
	
	public GuozhanPlayer(Player player, int fightType) {
		this.player = player;
		this.fightType = fightType;
	}
	
}
