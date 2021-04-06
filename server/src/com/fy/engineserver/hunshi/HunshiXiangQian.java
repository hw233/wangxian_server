package com.fy.engineserver.hunshi;

import com.fy.engineserver.util.SimpleKey;

public class HunshiXiangQian {
	@SimpleKey
	private int holeIndex;
	private int playerLevel;
	private long needSilver;

	public HunshiXiangQian() {
	}

	public HunshiXiangQian(int holeIndex, int playerLevel, long needSilver) {
		this.holeIndex = holeIndex;
		this.playerLevel = playerLevel;
		this.needSilver = needSilver;
	}

	public int getHoleIndex() {
		return holeIndex;
	}

	public void setHoleIndex(int holeIndex) {
		this.holeIndex = holeIndex;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public long getNeedSilver() {
		return needSilver;
	}

	public void setNeedSilver(long needSilver) {
		this.needSilver = needSilver;
	}

}
