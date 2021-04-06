package com.fy.engineserver.activity.dig;

import java.util.Random;

import com.fy.engineserver.core.g2d.Point;

public class DigEntity {
	private static int secondDistance = 500;
	private static int thirdDistance = 200;
	private static Random random = new Random();
	
	private String mapName;
	private Point point;
	private String mapSegmentName;
	private int range;
	private byte country;
	private String showMap;
	//国家
	private long propsId;
	
	private long resourceMapId;
	
	private long taskId;
	
	public DigEntity(){
	}

	public static int getSecondDistance() {
		return secondDistance;
	}

	public static void setSecondDistance(int secondDistance) {
		DigEntity.secondDistance = secondDistance;
	}

	public static int getThirdDistance() {
		return thirdDistance;
	}

	public static void setThirdDistance(int thirdDistance) {
		DigEntity.thirdDistance = thirdDistance;
	}

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random random) {
		DigEntity.random = random;
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

	public String getMapSegmentName() {
		return mapSegmentName;
	}

	public void setMapSegmentName(String mapSegmentName) {
		this.mapSegmentName = mapSegmentName;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public String getShowMap() {
		return showMap;
	}

	public void setShowMap(String showMap) {
		this.showMap = showMap;
	}

	public long getPropsId() {
		return propsId;
	}

	public void setPropsId(long propsId) {
		this.propsId = propsId;
	}

	public long getResourceMapId() {
		return resourceMapId;
	}

	public void setResourceMapId(long resourceMapId) {
		this.resourceMapId = resourceMapId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}


	
}
