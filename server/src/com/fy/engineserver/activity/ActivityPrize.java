package com.fy.engineserver.activity;

import com.fy.engineserver.sprite.Player;

/**
 * 活动奖励,
 * 
 */
public class ActivityPrize {

	/** 奖励物品名 */
	private String prizeName;

	/** 奖励物品颜色 */
	private int prizeColor;

	/** 奖励物品数量 */
	private int prizeNum;

	private boolean prizeBind; 

	/**
	 * 给某个人发送奖励
	 * @param player
	 * @return
	 */
	public boolean doPrize(Player player) {
		return false;
	}

}
