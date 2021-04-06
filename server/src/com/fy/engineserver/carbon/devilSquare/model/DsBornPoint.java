package com.fy.engineserver.carbon.devilSquare.model;


public class DsBornPoint {
	int x, y;

	public boolean isAlive = false;
	public long lastDisappearTime = 0;
	public long flushFrequency = 30000;
	public long monsterRandId;		//不是配表中的id
	public int spriteCategoryId;		//怪物配表中id

	public DsBornPoint(int x, int y) {
		this.x = x;
		this.y = y;
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
}
