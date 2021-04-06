package com.fy.engineserver.economic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.fy.engineserver.sprite.Player;

/**
 * 
 * 
 * @version 创建时间：Aug 8, 2012 12:59:14 PM
 * 
 */
public class AppStoreSavingLimit {

	public static LinkedHashMap<Integer, Long> levelMap = new LinkedHashMap<Integer, Long>();

	public static HashMap<Long, Long> playerSavingMap = new HashMap<Long, Long>();

	public static HashMap<String, List<String>> uuidSavingMap = new HashMap<String, List<String>>();

	public static long lastRefreshDate = 0;

	static {
		levelMap.put(10, 5000L);
		levelMap.put(20, 10000L);
		levelMap.put(30, 20000L);
		levelMap.put(40, 30000L);
		levelMap.put(220, 588000000L);
	}

	public static boolean levelCanSaving(Player player) {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		if (day != lastRefreshDate) {
			playerSavingMap.clear();
			uuidSavingMap.clear();
			lastRefreshDate = day;
		}
		Long amount = playerSavingMap.get(player.getId());
		if (amount == null) {
			amount = 0L;
		}
		long limit = 99999999999999999L;
		int level = player.getLevel();
		Integer levels[] = levelMap.keySet().toArray(new Integer[0]);
		for (Integer ll : levels) {
			if (level <= ll) {
				limit = levelMap.get(ll);
				break;
			}
		}
		if (amount >= limit) {
			return false;
		}
		return true;
	}

	// uuid
	public static boolean uuidCanSaving(Player player, String dcode) {
		int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		if (day != lastRefreshDate) {
			playerSavingMap.clear();
			uuidSavingMap.clear();
			lastRefreshDate = day;
		}
		String uuid = null;
		if (dcode.indexOf("MAC") != -1) {
			uuid = dcode.split("MAC")[0];
		}
		if (uuid == null) {
			return true;
		}
		if ("UUID=".equalsIgnoreCase(uuid.trim()) || "".equals(uuid.trim())) {
			return true;
		}
		List<String> ulist = uuidSavingMap.get(uuid);// ""
		if (ulist != null && ulist.size() >= 2 && !ulist.contains(player.getUsername())) {
			return false;
		}
		return true;
	}

	public static void notifyPlayerSaving(Player player, long amount) {
		Long a = playerSavingMap.get(player.getId());
		if (a == null) {
			a = 0L;
		}
		playerSavingMap.put(player.getId(), a + amount);
	}

	public static void notifyUUIDSaving(Player player, String dcode) {
		String uuid = null;
		if (dcode.indexOf("MAC") != -1) {
			uuid = dcode.split("MAC")[0];
		}
		if (uuid == null) {
			return;
		}
		List<String> ulist = uuidSavingMap.get(uuid);
		if (ulist == null) {
			ulist = new ArrayList<String>();
		}
		if (!ulist.contains(player.getUsername())) {
			ulist.add(player.getUsername());
		}
		uuidSavingMap.put(uuid, ulist);
	}
}
