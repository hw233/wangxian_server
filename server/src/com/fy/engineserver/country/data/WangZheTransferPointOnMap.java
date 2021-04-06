package com.fy.engineserver.country.data;

public class WangZheTransferPointOnMap {

	public String mapName = "";

	public String displayMapName = "";
	
	public short[] pointsX = new short[0];
	
	public short[] pointsY = new short[0];
	
	/**
	 * 半径
	 */
	public short[] ranges = new short[0];
	
	public String[] pointNames = new String[0];

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public short[] getPointsX() {
		return pointsX;
	}

	public void setPointsX(short[] pointsX) {
		this.pointsX = pointsX;
	}

	public short[] getPointsY() {
		return pointsY;
	}

	public void setPointsY(short[] pointsY) {
		this.pointsY = pointsY;
	}

	public String[] getPointNames() {
		return pointNames;
	}

	public void setPointNames(String[] pointNames) {
		this.pointNames = pointNames;
	}

	public short[] getRanges() {
		return ranges;
	}

	public void setRanges(short[] ranges) {
		this.ranges = ranges;
	}

	public String getDisplayMapName() {
		return displayMapName;
	}

	public void setDisplayMapName(String displayMapName) {
		this.displayMapName = displayMapName;
	}
	
}
