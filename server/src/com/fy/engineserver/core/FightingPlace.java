package com.fy.engineserver.core;

import java.util.ArrayList;

import com.fy.engineserver.sprite.Player;

/**
 * 战场
 */
public class FightingPlace {

	protected String name;
	//地图名字
	protected Game game;
	
	//各个阵营的复活点
	protected String resurrectionRegion_紫薇宫;
	
	protected String resurrectionRegion_日月盟;
	
	protected String general_紫薇宫;
	
	protected String general_日月盟;
	
	protected long startTime = 0;
	
	protected long endTime = 0;
	
	//玩家最小等级需求
	protected int minPlayerLevel = 10;
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getResurrectionRegion_紫薇宫() {
		return resurrectionRegion_紫薇宫;
	}

	public void setResurrectionRegion_紫薇宫(String resurrectionRegion_紫薇宫) {
		this.resurrectionRegion_紫薇宫 = resurrectionRegion_紫薇宫;
	}

	public String getResurrectionRegion_日月盟() {
		return resurrectionRegion_日月盟;
	}

	public void setResurrectionRegion_日月盟(String resurrectionRegion_日月盟) {
		this.resurrectionRegion_日月盟 = resurrectionRegion_日月盟;
	}

	public String getGeneral_紫薇宫() {
		return general_紫薇宫;
	}

	public void setGeneral_紫薇宫(String general_紫薇宫) {
		this.general_紫薇宫 = general_紫薇宫;
	}

	public String getGeneral_日月盟() {
		return general_日月盟;
	}

	public void setGeneral_日月盟(String general_日月盟) {
		this.general_日月盟 = general_日月盟;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public int getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(int minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}

	public FightingPlace(String name,Game game,String resurrectionRegion_紫薇宫,String resurrectionRegion_日月盟,
			String general_紫薇宫,String general_日月盟,long startTime,long endTime){
		this.name = name;
		this.game = game;
		this.resurrectionRegion_紫薇宫 = resurrectionRegion_紫薇宫;
		this.resurrectionRegion_日月盟 = resurrectionRegion_日月盟;
		this.general_紫薇宫 = general_紫薇宫;
		this.general_日月盟 = general_日月盟;
		
		this.startTime = startTime;
		this.endTime = endTime;
		 
	}
	
	public void reset(long startTime,long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		flag_120000 = false;
		flag_60000 = false;
		flag_30000 = false;
		flag_10000 =false;
	}
	
	public Player[] getPlayers(){
		LivingObject ll[] = game.getLivingObjects();
		ArrayList<Player> al = new ArrayList<Player>();
		for(LivingObject o : ll){
			if(o instanceof Player){
				al.add((Player)o);
			}
		}
		return al.toArray(new Player[0]);
	}
	
	boolean flag_120000 = false;
	boolean flag_60000 = false;
	boolean flag_30000 = false;
	boolean flag_10000 = false;
	
	public void heartbeat(){}
	
	/**
	 * 战场是否在开放
	 * @return
	 */
	
	public boolean isOpen(){
		long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(l > startTime && (l < endTime || endTime ==0)){
			return true;
		}
		return false;
	}
}
