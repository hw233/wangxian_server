package com.fy.engineserver.downcity;

import com.fy.engineserver.util.TimeSlice;

/**
 * 记录副本的配置信息
 * 
 * 比如：人数限制
 *      CD限制
 *      副本对应的地图
 *      副本进入的等级限制
 *      
 * 
 *
 */
public class DownCityInfo {

	/**
	 * 玩家可以重置
	 */
	public static final int RESET_TYPE_BY_PLAYER = 0;
	
	/**
	 * 系统重置
	 */
	public static final int RESET_TYPE_BY_SYSTEM = 1;
	
	/**
	 * 按天重置
	 */
	public static final int RESET_TYPE_BY_DAY = 2;

	/**
	 * 普通副本类型
	 */
	public static final int COMMON_DOWNCITY = 0;
	
	/**
	 * 勇士副本类型
	 */
	public static final int HERO_DOWNCITY = 1;
	
	int next_id = 1;
	
	public synchronized int nextId(){
		return next_id++;
	}
	
	/**
	 * 副本的编号，比如 01
	 */
	public String seqNum;
	
	/**
	 * 副本的名字，必须唯一
	 */
	protected String name;
	
	/**
	 * 副本最大人数限制
	 */
	protected int playerNumLimit;
	
	/**
	 * 副本对应的地图名
	 */
	protected String mapName = "";
	
	/**
	 * 副本对应的地图名
	 */
	public String mapDisplayName = "";
	
	/**
	 * 副本限制的最小玩家级别，小于此级别是不能进入副本的
	 */
	protected int minPlayerLevel;
	
	/**
	 * 重置类型
	 */
	protected int resetType;
	
	/**
	 * 副本类型
	 */
	protected int downCityType;
	
	/**
	 * 副本开启后，持续多少时间后，被系统重置
	 * 单位毫秒 
	 */
	protected long lastingTime = 1 * 3600 * 1000;

	/**
	 * 玩家进入副本的位置
	 */
	int x, y;
	
	/**
	 * 副本入口地图名称
	 */
	String enterPointMapName;
	
	/**
	 * 副本的入口坐标
	 */
	int enterPointX , enterPointY;
	
	/**
	 * 副本开启的时间
	 */
	private TimeSlice timeSlice; 
	
	
	public String getEnterPointMapName() {
		return enterPointMapName;
	}

	public void setEnterPointMapName(String enterPointMapName) {
		this.enterPointMapName = enterPointMapName;
	}

	public int getEnterPointX() {
		return enterPointX;
	}

	public void setEnterPointX(int enterPointX) {
		this.enterPointX = enterPointX;
	}

	public int getEnterPointY() {
		return enterPointY;
	}

	public void setEnterPointY(int enterPointY) {
		this.enterPointY = enterPointY;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getResetType() {
		return resetType;
	}

	public void setResetType(int resetType) {
		this.resetType = resetType;
	}

	public int getDownCityType() {
		return downCityType;
	}

	public void setDownCityType(int downCityType) {
		this.downCityType = downCityType;
	}

	public long getLastingTime() {
		return lastingTime;
	}

	public void setLastingTime(long lastingTime) {
		this.lastingTime = lastingTime;
	}

	public int getPlayerNumLimit() {
		return playerNumLimit;
	}

	public void setPlayerNumLimit(int playerNumLimit) {
		this.playerNumLimit = playerNumLimit;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(int minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}

	public String getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}
	
	
}
