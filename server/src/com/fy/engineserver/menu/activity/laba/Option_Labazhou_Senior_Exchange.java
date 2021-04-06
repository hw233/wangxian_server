package com.fy.engineserver.menu.activity.laba;

import java.util.*;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Player;

/**
 * 上品腊八粥
 * 
 * 
 */
public class Option_Labazhou_Senior_Exchange extends Option_Labazhou_Exchange {

	public static int dailyNum = 3;
	public static String articleName = "上品腊八粥";

	public static int minLilian = 10800;
	public static int maxLilian = 42600;
	public static HashMap<Integer, Integer> maxYuanqiMap = new HashMap<Integer, Integer>();// 最大修法值
	public static HashMap<Integer, Integer> minYuanqiMap = new HashMap<Integer, Integer>();// 最小修法值
	static {
		minYuanqiMap.put(70, 160);
		maxYuanqiMap.put(70, 320);

		minYuanqiMap.put(110, 1600);
		maxYuanqiMap.put(110, 3200);

		minYuanqiMap.put(150, 5600);
		maxYuanqiMap.put(150, 11200);

		minYuanqiMap.put(190, 13600);
		maxYuanqiMap.put(190, 27200);
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
		if (random.nextBoolean()) {// true 给历练 fasle给元气
			int lilianAdd = random.nextInt(maxLilian - minLilian) + minLilian;
			player.setLilian(player.getLilian() + lilianAdd);
			return "获得历练:" + lilianAdd + ",今天你还可以获得:" + getLeftExchangeNum(player, date) + "次";
		} else {
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
	}

	@Override
	public void doSelect(Game game, Player player) {
		super.doSelect(game, player);
	}

}
