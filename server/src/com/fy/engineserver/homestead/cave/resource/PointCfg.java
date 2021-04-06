package com.fy.engineserver.homestead.cave.resource;

/**
 * 建筑在地图上的点配置
 * 
 * 
 */
public class PointCfg {

	/** 在地图中的位置 */
	private int index;
	private Point point;
	private int type;

	public PointCfg(int index, Point point, int type) {
		this.index = index;
		this.point = point;
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
