package com.fy.engineserver.menu.activity.laba;

import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Player;

/**
 * 普通腊八粥
 * 
 * 
 */
public class Option_Labazhou_Common_Exchange extends Option_Labazhou_Exchange {

	public static int dailyNum = 3;
	public static String articleName = "腊八粥";

	public static int minLilian = 3600;
	public static int maxLilian = 14200;

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
		int lilianAdd = random.nextInt(maxLilian - minLilian) + minLilian;
		player.setLilian(player.getLilian() + lilianAdd);
		return "恭喜你!获得历练:" + lilianAdd + ",今天你还可以获得:" + getLeftExchangeNum(player, date) + "次";
	}

	@Override
	public void doSelect(Game game, Player player) {
		super.doSelect(game, player);
	}

}
