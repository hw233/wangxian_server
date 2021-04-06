package com.fy.engineserver.battlefield.concrete;

/**
 * 进入战场窗口所需的数据结构
 */
public class BattleItem{	
	//战场的名称
	private String name = "";
	
	private String displayName = "";
	
	//战场的说明　UUB格式
	private String description = "";
	
	//玩家的状态　0:在战场外　1:在排队中　2：在该战场中
	private int playerType = 0;
	
	//玩家排队的状态(剩余时间)
	private String waitTime = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPlayerType() {
		return playerType;
	}

	public void setPlayerType(int playerType) {
		this.playerType = playerType;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
