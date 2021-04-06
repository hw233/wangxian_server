package com.fy.engineserver.activity.oldPlayerComeBack;

import java.io.Serializable;

/**
 * 记录玩家的一些信息
 * 
 *
 */
public class OldPlayerInfo implements Serializable{

	
	//召回id
	private long id;
		
	/**
	 * 玩家类型
	 * 1:A类(15天内有登陆记录的)
	 * 2:B类(15天内没有登陆记录的)
	 * 
	 */
	private int OLD_PLAYER_TYPE = 0;
	
	//已经召回XX位 老朋友
	private int playerBackNums;
	
	//已获得XX次奖励
	private int rewardNums;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getOLD_PLAYER_TYPE() {
		return OLD_PLAYER_TYPE;
	}

	public void setOLD_PLAYER_TYPE(int oLD_PLAYER_TYPE) {
		OLD_PLAYER_TYPE = oLD_PLAYER_TYPE;
	}

	public int getPlayerBackNums() {
		return playerBackNums;
	}

	public void setPlayerBackNums(int playerBackNums) {
		this.playerBackNums = playerBackNums;
	}

	public int getRewardNums() {
		return rewardNums;
	}

	public void setRewardNums(int rewardNums) {
		this.rewardNums = rewardNums;
	}
	
	
}
