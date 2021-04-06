package com.fy.engineserver.guozhan;

/**
 * 国战小地图上的点
 * 
 * @version 创建时间：May 16, 2012 11:47:28 AM
 * 
 */
public class GuozhanMapPoint {
	
	/**
	 * 点的编号
	 */
	private int id;
	
	/**
	 * 所在游戏地图的名称
	 */
	private String mapName;
	
	/**
	 * 点的名字
	 */
	private String name;
	
	/**
	 * 点在国战小地图上的坐标x
	 */
	private int x;
	
	/**
	 * 点在国战小地图上的坐标y
	 */
	private int y;
	
	/**
	 * 所属方：0-进攻方， 1-防守方
	 */
	private byte ownerType;;
	
	/**
	 * 是否是大boss
	 */
	private boolean bigBoss;
	
	/**
	 * 是否是小boss
	 */
	private boolean littleBoss;
	
	/**
	 * 拥有者国家id
	 */
	private byte ownerCountryId;
	
	/**
	 * 是否是复活点 
	 */
	private boolean bornPoint;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public byte getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(byte ownerType) {
		this.ownerType = ownerType;
	}

	public boolean isBigBoss() {
		return bigBoss;
	}

	public void setBigBoss(boolean bigBoss) {
		this.bigBoss = bigBoss;
	}

	public boolean isLittleBoss() {
		return littleBoss;
	}

	public void setLittleBoss(boolean littleBoss) {
		this.littleBoss = littleBoss;
	}

	public byte getOwnerCountryId() {
		return ownerCountryId;
	}

	public void setOwnerCountryId(byte ownerCountryId) {
		this.ownerCountryId = ownerCountryId;
	}

	public boolean isBornPoint() {
		return bornPoint;
	}

	public void setBornPoint(boolean bornPoint) {
		this.bornPoint = bornPoint;
	}

}
