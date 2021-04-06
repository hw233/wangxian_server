package com.fy.engineserver.activity.xianling;

public class MonsterData {
	private int catorageId;
	private int scale;// 大小比例，原值是1000
	private int x;// 在面板上的位置坐标x
	private int y;// 在面板上的位置坐标y

	public MonsterData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonsterData(int catorageId, int scale, int x, int y) {
		super();
		this.catorageId = catorageId;
		this.scale = scale;
		this.x = x;
		this.y = y;
	}

	public int getCatorageId() {
		return catorageId;
	}

	public void setCatorageId(int catorageId) {
		this.catorageId = catorageId;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
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
