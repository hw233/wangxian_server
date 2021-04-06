package com.fy.engineserver.newtask;

import java.util.Arrays;

/**
 * 活动类经验奖励
 * 
 * 
 */
public class ActivityTaskExp {

	public static int maxLevel = 300;
	private int prizeId;
	private String name;
	private int thew;
	private Long[] expPrize = new Long[maxLevel];

	public ActivityTaskExp(int prizeId, String name, int thew, Long[] expPrize) {
		this.prizeId = prizeId;
		this.name = name;
		this.thew = thew;
		this.expPrize = expPrize;
	}

	public static int getMaxLevel() {
		return maxLevel;
	}

	public static void setMaxLevel(int maxLevel) {
		ActivityTaskExp.maxLevel = maxLevel;
	}

	public int getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(int prizeId) {
		this.prizeId = prizeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getThew() {
		return thew;
	}

	public void setThew(int thew) {
		this.thew = thew;
	}

	public Long[] getExpPrize() {
		return expPrize;
	}

	public void setExpPrize(Long[] expPrize) {
		this.expPrize = expPrize;
	}

	@Override
	public String toString() {
		return "ActivityTaskExp [prizeId=" + prizeId + ", name=" + name + ", thew=" + thew + ", expPrize=" + Arrays.toString(expPrize) + "]";
	}
}
