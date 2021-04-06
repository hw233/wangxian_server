package com.fy.engineserver.menu.arborday;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;

/**
 * 经验兑换历练
 * 
 * 
 */
public class Option_Exp_Exchange_Lilian extends Option implements NeedCheckPurview{

	public static Map<Integer, Integer> costExpMap = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> prizeLilianMap = new HashMap<Integer, Integer>();
	/** 角色使用记录 */
	public static Map<String, Map<Long, Integer>> playerUseRecord = new HashMap<String, Map<Long, Integer>>();
	public static int MAX_TIMES = 3;

	static {
		costExpMap.put(70, 600000);
		costExpMap.put(110, 4000000);
		costExpMap.put(150, 11000000);
		costExpMap.put(190, 27000000);

		prizeLilianMap.put(70, 12628);
		prizeLilianMap.put(110, 19794);
		prizeLilianMap.put(150, 26960);
		prizeLilianMap.put(190, 34126);
	}

	@Override
	public void doSelect(Game game, Player player) {
		String today = TimeTool.formatter.varChar10.format(new Date());
		if (playerUseRecord.containsKey(today) && playerUseRecord.get(today).containsKey(player.getId()) && playerUseRecord.get(today).get(player.getId()) >= MAX_TIMES) {
			player.sendError("你今天已经用经验浇水达到了" + MAX_TIMES + "次, 明天再来吧");
			return;
		}
		SealManager sealManager = SealManager.getInstance();
		int currentSeal = sealManager.seal.sealLevel;
		if (costExpMap.containsKey(currentSeal)) {
			int subExp = costExpMap.get(currentSeal);
			if (player.getExp() < subExp) {
				player.sendError("你的经验不足.不能浇水!");
				return;
			}
			player.subExp(subExp, "植树节活动");
			{
				// 发奖励
				int prizeLilian = prizeLilianMap.get(currentSeal);
				if (prizeLilian > 0) {
					try {
						BillingCenter.getInstance().playerSaving(player, prizeLilian, CurrencyType.LILIAN, SavingReasonType.活动奖励, "植树节");
						if (ActivitySubSystem.logger.isWarnEnabled()) {
							ActivitySubSystem.logger.warn("[植树节] [经验浇水] [成功] [" + player.getLogString() + "] [消耗经验:" + subExp + "] [获得历练:" + prizeLilian + "]");
						}
						player.sendError("恭喜你消耗:" + subExp + "经验,获得历练值:" + prizeLilian);
						// 记录次数
						record(player.getId(), today);
					} catch (SavingFailedException e) {
						ActivitySubSystem.logger.warn("[植树节] [经验浇水] [异常] [" + player.getLogString() + "] [消耗经验:" + subExp + "] [获得历练:" + prizeLilian + "]", e);
					}
				}
			}
		}
	}

	public synchronized void record(long playerId, String day) {
		if (!playerUseRecord.containsKey(day)) {
			playerUseRecord.put(day, new HashMap<Long, Integer>());
		}
		if (!playerUseRecord.get(day).containsKey(playerId)) {
			playerUseRecord.get(day).put(playerId, 0);
		}
		playerUseRecord.get(day).put(playerId, playerUseRecord.get(day).get(playerId) + 1);
	}

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public boolean canSee(Player player) {
		long now = SystemTime.currentTimeMillis();
		return ArborDayManager.inCycle(now);
	}
}
