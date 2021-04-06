package com.fy.engineserver.activity;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 活动数据,包含服务器和时间信息
 * 
 */
public class ActivityDataForServerAndTime {
	public static GameConstants constants = GameConstants.getInstance();

	private long startTime;// 活动开始时间
	private long endTime;// 活动结束时间

	private boolean allServer;// 是否是全服活动
	private String[] servers;// 开放的服务器,allServer=false的时候才有效

	private List<ActivityPrize> activityPrizeList = new ArrayList<ActivityPrize>();// 活动奖励

	/**
	 * 是否在活动中
	 * @param nowTime
	 * @return
	 */
	public boolean inActivity(long nowTime) {
		if (startTime > nowTime || endTime < nowTime) {// 时间不符合,直接返回false
			return false;
		}
		if (allServer) {// 所有服务器都开放
			return true;
		}
		if (servers == null) {
			return false;
		}
		for (String openServer : servers) {
			if (openServer.equals(constants.getServerName())) {
				return true;
			}
		}
		return false;
	}

	public void doPrize(Player player) {
		if (activityPrizeList != null) {
			for (ActivityPrize activityPrize : activityPrizeList) {
				boolean succ = activityPrize.doPrize(player);
				// LOG
			}
		}
	}
}