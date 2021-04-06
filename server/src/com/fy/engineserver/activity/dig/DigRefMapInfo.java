package com.fy.engineserver.activity.dig;

import com.fy.engineserver.core.g2d.Point;


public class DigRefMapInfo {
	private String mapName;
	private Point point;

	public DigRefMapInfo(String mapName, Point point) {
		this.mapName = mapName;
		this.point = point;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return "DigRefMapInfo [mapName=" + mapName + ", point=" + point + "]";
	}

}
