package com.fy.engineserver.activity.explore;

import com.fy.engineserver.core.g2d.Point;

public class ExploreTemplate {

	// 0	fengxuezhidian xxx  100   200	
	private int index;
	private String mapName;
	private Point points;
	private String mapSegmentNames;
	private int range;
	//0(国内国外),1(国内),2(国外)
	private int inOut;
	
	private String showMap;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Point getPoints() {
		return points;
	}
	public void setPoints(Point points) {
		this.points = points;
	}
	public String getMapSegmentNames() {
		return mapSegmentNames;
	}
	public void setMapSegmentNames(String mapSegmentNames) {
		this.mapSegmentNames = mapSegmentNames;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public String getShowMap() {
		return showMap;
	}
	public void setShowMap(String showMap) {
		this.showMap = showMap;
	}
	public int getInOut() {
		return inOut;
	}
	public void setInOut(int inOut) {
		this.inOut = inOut;
	}
}
