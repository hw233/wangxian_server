package com.fy.engineserver.jiazu2.manager;

import java.util.Arrays;

public class HuanJingBoss {
	
	private int level;
	private int bossId;
	private String mapName;
	private int x;
	private int y;
	private String rewards [] = new String[]{};
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String[] getRewards() {
		return rewards;
	}
	public void setRewards(String[] rewards) {
		this.rewards = rewards;
	}
	@Override
	public String toString() {
		return "[bossId=" + bossId + ", level=" + level
				+ ", mapName=" + mapName + ", rewards="
				+ Arrays.toString(rewards) + ", x=" + x + ", y=" + y + "]";
	}
	
}
