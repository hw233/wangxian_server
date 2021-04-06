package com.fy.engineserver.menu.arborday;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

/**
 * 家族工资兑换历练和元气
 * 
 * 
 */
public class Option_JiazuGongzi_Exchange extends Option implements NeedCheckPurview{

	public static Map<Integer, Integer> costGongziMap = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> prizeLilianMap = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> prizeYuanqiMap = new HashMap<Integer, Integer>();

	/** 角色使用记录 */
	public static Map<String, Map<Long, Integer>> playerUseRecord = new HashMap<String, Map<Long, Integer>>();
	public static int MAX_TIMES = 3;

	static {
		costGongziMap.put(70, 800000);
		costGongziMap.put(110, 800000);
		costGongziMap.put(150, 800000);
		costGongziMap.put(190, 800000);

		prizeLilianMap.put(70, 18944);
		prizeLilianMap.put(110, 29693);
		prizeLilianMap.put(150, 40442);
		prizeLilianMap.put(190, 51190);

		prizeYuanqiMap.put(70, 504);
		prizeYuanqiMap.put(110, 2320);
		prizeYuanqiMap.put(150, 3855);
		prizeYuanqiMap.put(190, 6574);
	}

	public static Random random = new Random();

	@Override
	public void doSelect(Game game, Player player) {
		String today = TimeTool.formatter.varChar10.format(new Date());
		if (playerUseRecord.containsKey(today) && playerUseRecord.get(today).containsKey(player.getId()) && playerUseRecord.get(today).get(player.getId()) >= MAX_TIMES) {
			player.sendError("你今天已经用工资浇水达到了" + MAX_TIMES + "次, 明天再来吧");
			return;
		}
		SealManager sealManager = SealManager.getInstance();
		int currentSeal = sealManager.seal.sealLevel;
		if (costGongziMap.containsKey(currentSeal)) {
			int subGongzi = costGongziMap.get(currentSeal);
			if (player.getWage() < subGongzi) {
				player.sendError("你的家族工资不足.不能除虫!");
				return;
			}
			try {
				BillingCenter.getInstance().playerExpense(player, subGongzi, CurrencyType.GONGZI, ExpenseReasonType.活动, "植树节",-1);
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
			{
				// 发奖励
				if (random.nextBoolean()) {// true发历练
					int prizeLilian = prizeLilianMap.get(currentSeal);
					if (prizeLilian > 0) {
						try {
							BillingCenter.getInstance().playerSaving(player, prizeLilian, CurrencyType.LILIAN, SavingReasonType.活动奖励, "植树节");
							if (ActivitySubSystem.logger.isWarnEnabled()) {
								ActivitySubSystem.logger.warn("[植树节] [工资浇水] [成功] [" + player.getLogString() + "] [消耗工资:" + subGongzi + "] [获得历练:" + prizeLilian + "]");
							}
							player.sendError("除虫成功.恭喜你使用家族工资:" + BillingCenter.得到带单位的银两(subGongzi) + ",获得历练值:" + prizeLilian);
							// 记录次数
							record(player.getId(), today);
						} catch (SavingFailedException e) {
							ActivitySubSystem.logger.warn("[植树节] [工资浇水] [异常] [" + player.getLogString() + "] [消耗工资:" + subGongzi + "] [获得历练:" + prizeLilian + "]", e);
						}
					}
				} else { // 发元气
					int yuanqi = prizeYuanqiMap.get(currentSeal);
					if (yuanqi > 0) {
						player.setEnergy(player.getEnergy() + yuanqi);
						player.sendError("除虫成功.恭喜你使用家族工资:" + BillingCenter.得到带单位的银两(subGongzi) + ",获得修法值:" + yuanqi);
						record(player.getId(), today);
						if (ActivitySubSystem.logger.isWarnEnabled()) {
							ActivitySubSystem.logger.warn("[植树节] [工资浇水] [成功] [" + player.getLogString() + "] [消耗工资:" + subGongzi + "] [获得元气:" + yuanqi + "] [总元气:"+player.getEnergy()+"]");
						}
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
