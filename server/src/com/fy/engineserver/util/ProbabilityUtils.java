package com.fy.engineserver.util;

import java.util.Random;

import com.fy.engineserver.sprite.Player;

public class ProbabilityUtils {

	static java.util.Random random = new java.util.Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	
	/**
	 * 随机概率，以给定的概率返回true.
	 * 
	 * @param probability 必须是0~1之间的一个数，标识百分比
	 * @return
	 */
	public static boolean randomProbability(Random random, double probability){
		if(probability == 0) return false;
		if(probability == 1) return true;
		double d = random.nextDouble();
		if(d < probability) return true;
		return false;
	}
	
	/**
	 * 随机概率，以给定的概率分布，随机落在那个分布中
	 * 返回的为概率分布的下标，要求概率分布之和必须为1
	 * @param probabilitys
	 * @return
	 */
	public static int randomProbability(Random random, double probabilitys[]){
		double d = random.nextDouble();
		
		double s = 0;
		for(int i = 0 ; i < probabilitys.length ; i++){
			if(s <= d && d < s + probabilitys[i]){
				return i;
			}
			s += probabilitys[i];
		}
		return probabilitys.length-1;
	}

	/**
	 * 随机概率，以给定的概率返回true.
	 * 
	 * @param probability 必须是0~1之间的一个数，标识百分比
	 * @return
	 */
	public static boolean randomProbability(Player player, double probability){
		if(probability == 0) return false;
		if(probability == 1) return true;
		double d = player.random.nextDouble();
		if(d < probability) return true;
		return false;
	}
	
	/**
	 * 随机概率，以给定的概率分布，随机落在那个分布中
	 * 返回的为概率分布的下标，要求概率分布之和必须为1
	 * @param probabilitys
	 * @return
	 */
	public static int randomProbability(Player player, double probabilitys[]){
		double d = player.random.nextDouble();
		
		double s = 0;
		for(int i = 0 ; i < probabilitys.length ; i++){
			if(s <= d && d < s + probabilitys[i]){
				return i;
			}
			s += probabilitys[i];
		}
		return probabilitys.length-1;
	}

	/**
	 * 取两个数之间的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randomGaussian(Random random, int min,int max){
		return (int)(min + random.nextDouble() * (max - min));
	}

	/**
	 * 取两个数之间的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(Random random, int min,int max){
		return (int)(min + random.nextDouble() * (max - min));
	}
	
}
