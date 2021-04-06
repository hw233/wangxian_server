package com.fy.engineserver.menu.activity.laba;

import java.util.*;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;

/**
 * 贡品腊八粥
 * 
 * 
 */
public class Option_Labazhou_Super_Exchange extends Option_Labazhou_Exchange {

	public static int dailyNum = 10;
	public static String articleName = "贡品腊八粥";

	public static HashMap<Integer, Integer> maxYuanqiMap = new HashMap<Integer, Integer>();// 最大修法值
	public static HashMap<Integer, Integer> minYuanqiMap = new HashMap<Integer, Integer>();// 最小修法值
	static {
		minYuanqiMap.put(70, 200);
		maxYuanqiMap.put(70, 400);

		minYuanqiMap.put(110, 2000);
		maxYuanqiMap.put(110, 4000);

		minYuanqiMap.put(150, 7000);
		maxYuanqiMap.put(150, 14000);

		minYuanqiMap.put(190, 17000);
		maxYuanqiMap.put(190, 34000);
	}
	public static Random random = new Random();

	@Override
	int getDailyNum() {
		return dailyNum;
	}

	@Override
	String getArticleName() {
		return articleName;
	}

	@Override
	String exchange(Player player, String date) {
		int sealLevel = SealManager.getInstance().seal.sealLevel;
		if (sealLevel == 0) {
			sealLevel = 70;
		}
		Integer maxYuanqi = maxYuanqiMap.get(sealLevel);
		Integer minYuanqi = minYuanqiMap.get(sealLevel);
		if (maxYuanqi == null || minYuanqi == null) {
			maxYuanqi = maxYuanqiMap.get(70);
			minYuanqi = minYuanqiMap.get(70);
		}
		int yuanqiAdd = random.nextInt(Math.abs(maxYuanqi - minYuanqi)) + minYuanqi;
		player.setEnergy(player.getEnergy() + yuanqiAdd);
		ActivitySubSystem.logger.warn(player.getLogString() + " [兑换] [增加元气:" + yuanqiAdd + "] [总元气:" + player.getEnergy() + "]");
		return "获得元气:" + yuanqiAdd + ",今天你还可以获得:" + getLeftExchangeNum(player, date) + "次";
	}

	@Override
	public void doSelect(Game game, Player player) {
		super.doSelect(game, player);
	}

}
