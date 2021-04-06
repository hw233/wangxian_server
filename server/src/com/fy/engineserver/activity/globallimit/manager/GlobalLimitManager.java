package com.fy.engineserver.activity.globallimit.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.activity.globallimit.model.PlayerLimitModel;
import com.fy.engineserver.sprite.Player;

public class GlobalLimitManager {
	
	private static GlobalLimitManager instance;
	
	private static Object lock = new Object();
	
	public Map<Long, PlayerLimitModel> pLimitMap = new Hashtable<Long, PlayerLimitModel>();
	
	public List<Integer> bossIds = new ArrayList<Integer>();
	
	public static int 每天通过地灵天书boss获得经验次数限制 = 6;
	
	public int lastResetDay = 0;
	
	private GlobalLimitManager(){}
	
	public static GlobalLimitManager getInst () {
		if (instance != null) {
			return instance;
		}
		synchronized (lock) {
			if (instance != null) {
				return instance;
			}
			instance = new GlobalLimitManager();
			Calendar cal = Calendar.getInstance();
			instance.lastResetDay = cal.get(Calendar.DAY_OF_YEAR);
			return instance;
		}
	}
	
	public boolean canAddExp(Player player, int monsterId) {
		if (!bossIds.contains(monsterId)) {
			return true;
		}
		synchronized (lock) {
			Calendar cal = Calendar.getInstance();
			int dd = cal.get(Calendar.DAY_OF_YEAR);
			if (dd != lastResetDay) {
				if (AchievementManager.logger.isDebugEnabled()) {
					AchievementManager.logger.debug("[使用地灵天书限制] [跨天重置次数] [" + player.getLogString() + "] [bossId : " + monsterId + "]");
				}
				lastResetDay = dd;
				pLimitMap.clear();
			}
			int addTimes = notifyPlayerGainExp(player);
			if (addTimes > 每天通过地灵天书boss获得经验次数限制) {
				if (AchievementManager.logger.isDebugEnabled()) {
					AchievementManager.logger.debug("[使用地灵天书限制] [" + player.getLogString() + "] [今天已使用次数:" + addTimes + "] [bossId : " + monsterId + "]");
				}
				return false;
			}
			return true;
		}
	}
	
	public int notifyPlayerGainExp(Player player) {
		PlayerLimitModel pm = pLimitMap.get(player.getId());
		if (pm == null) {
			pm = new PlayerLimitModel();
			pm.setPlayerId(player.getId());
			pm.setDilingBossTimes(1);
			pLimitMap.put(player.getId(), pm);
			return 1;
		}
		pm.setDilingBossTimes(pm.getDilingBossTimes() + 1);
		pLimitMap.put(player.getId(), pm);
		return pm.getDilingBossTimes();
	}
	
}
