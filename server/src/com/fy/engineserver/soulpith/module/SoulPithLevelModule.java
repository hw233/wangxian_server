package com.fy.engineserver.soulpith.module;

import com.fy.engineserver.util.SimpleKey;


/**
 * 灵髓等级基础数值
 * 
 * @date on create 2016年3月16日 下午3:10:10
 */
public class SoulPithLevelModule {
	/** 等级 */
	@SimpleKey
	private int level;
	/** 等级对应的灵髓点上限 */
	private int[] maxSoulNums;
	private int[] douluoBase;
	private int[] guishaBase;
	private int[] lingzunBase;
	private int[] wuhuangBase;
	private int[] shoukuiBase;
	/**
	 * 职业对应的基础灵髓点数
	 * @param career
	 * @return
	 */
	public int[] getCareerBaseSoulNum(byte career) {
		if (career == 1) {
			return douluoBase;
		} else if (career == 2) {
			return guishaBase;
		} else if (career == 3) {
			return lingzunBase;
		} else if (career == 4) {
			return wuhuangBase;
		} else if (career == 5) {
			return shoukuiBase;
		}
		return douluoBase;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int[] getMaxSoulNums() {
		return maxSoulNums;
	}

	public void setMaxSoulNums(int[] maxSoulNums) {
		this.maxSoulNums = maxSoulNums;
	}

	public int[] getDouluoBase() {
		return douluoBase;
	}

	public void setDouluoBase(int[] douluoBase) {
		this.douluoBase = douluoBase;
	}

	public int[] getGuishaBase() {
		return guishaBase;
	}

	public void setGuishaBase(int[] guishaBase) {
		this.guishaBase = guishaBase;
	}

	public int[] getLingzunBase() {
		return lingzunBase;
	}

	public void setLingzunBase(int[] lingzunBase) {
		this.lingzunBase = lingzunBase;
	}

	public int[] getWuhuangBase() {
		return wuhuangBase;
	}

	public void setWuhuangBase(int[] wuhuangBase) {
		this.wuhuangBase = wuhuangBase;
	}

	public int[] getShoukuiBase() {
		return shoukuiBase;
	}

	public void setShoukuiBase(int[] shoukuiBase) {
		this.shoukuiBase = shoukuiBase;
	}
	
}
