package com.fy.engineserver.activity.fireActivity;

public class FireActivityTemplate {

	// 家族篝火实体	100	家族篝火	1000	0	20	350001;350002	1	1	1310	1140
	private String fireName;
	private int distance ;
	private String bufferName;
	private int lastTime;
	private int extraLastTime;
	private double expInterval;
	private int[] npcIds;
	private int fireNum;
	private int addWoodNum;
	private int x;
	private int y;
	
	// 家族篝火1    普通篝火0
	private int fireType;
	private String  mapName;
	
	public String getFireName() {
		return fireName;
	}
	public void setFireName(String fireName) {
		this.fireName = fireName;
	}
	public int getDistance() {
		return distance;
	}
	public String getBufferName() {
		return bufferName;
	}
	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getLastTime() {
		return lastTime;
	}
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	public int getExtraLastTime() {
		return extraLastTime;
	}
	public void setExtraLastTime(int extraLastTime) {
		this.extraLastTime = extraLastTime;
	}
	public double getExpInterval() {
		return expInterval;
	}
	public void setExpInterval(double expInterval) {
		this.expInterval = expInterval;
	}
	public int[] getNpcIds() {
		return npcIds;
	}
	public void setNpcIds(int[] npcIds) {
		this.npcIds = npcIds;
	}
	public int getFireNum() {
		return fireNum;
	}
	public void setFireNum(int fireNum) {
		this.fireNum = fireNum;
	}
	public int getAddWoodNum() {
		return addWoodNum;
	}
	public void setAddWoodNum(int addWoodNum) {
		this.addWoodNum = addWoodNum;
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
	public int getFireType() {
		return fireType;
	}
	public void setFireType(int fireType) {
		this.fireType = fireType;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
}
