package com.fy.engineserver.menu.cave.activity;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;

import java.util.*;

/**
 * 仙府收获活动管理
 * 
 * 
 */
public class CaveHarvestActivityManager {

	public static long startTime = TimeTool.formatter.varChar19.parse("2014-04-25 00:00:00");
	public static long startTime_tw = TimeTool.formatter.varChar19.parse("2014-04-30 00:00:00");
	public static long endTime = TimeTool.formatter.varChar19.parse("2014-05-08 23:59:59");
	public static long endTime_tw = TimeTool.formatter.varChar19.parse("2014-05-13 23:59:59");

	public static int maxNum = 20;// 最大摘取次数
	public static Map<String, Map<Long, Integer>> pickHistory = new HashMap<String, Map<Long, Integer>>();// 摘取记录

	/**
	 * 活动是否开启
	 * @return
	 */
	public static boolean activityOpening() {
		long now = SystemTime.currentTimeMillis();
		if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
			return startTime_tw <= now && now <= endTime_tw;
		}
		return startTime <= now && now <= endTime;
	}

	/**
	 * 仙府收获的活动数据
	 * 
	 * 
	 */
	public static abstract class CaveHarvestActivityData {

		/**
		 * 活动额外奖励系数
		 * @return
		 */
		public abstract int getTimes();
		
		/**
		 * 活动的消耗,扣除玩家身上的XXX
		 * @param player
		 * @return
		 */
		public abstract CompoundReturn doDeduct(Player player);

		/**
		 * 活动的提示
		 * @return
		 */
		public abstract String getNotice();

		/**
		 * 活动的奖励
		 * @param player
		 */
		public abstract void doPrize(Player player);

	}

	public static void noticePickup(Player player, String time) {
		Map<Long, Integer> dayMap = getDayMap(time);
		if (!dayMap.containsKey(player.getId())) {
			dayMap.put(player.getId(), 0);
		}
		dayMap.put(player.getId(), dayMap.get(player.getId()) + 1);
	}

	private static Map<Long, Integer> getDayMap(String time) {
		if (!pickHistory.containsKey(time)) {
			synchronized (CaveHarvestActivityManager.class) {
				if (!pickHistory.containsKey(time)) {
					pickHistory.put(time, new HashMap<Long, Integer>());
				}
			}
		}
		return pickHistory.get(time);
	}

	public static boolean canPickup(Player player, String time) {
		Map<Long, Integer> dayMap = getDayMap(time);
		if (dayMap == null || dayMap.get(player.getId()) == null || dayMap.get(player.getId()) == null) {
			return true;
		}
		return dayMap.get(player.getId()) < maxNum;
	}
}
