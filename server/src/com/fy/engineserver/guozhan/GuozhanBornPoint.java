package com.fy.engineserver.guozhan;

/**
 *
 * 
 * @version 创建时间：May 5, 2012 12:22:14 PM
 * 
 */
public class GuozhanBornPoint {
	
	private String name;
	
	private byte belongCountryId;
	
	private String mapName;
	
	private byte mapCountry;
	
	private int x;
	
	private int y;

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

	public byte getBelongCountryId() {
		return belongCountryId;
	}

	public void setBelongCountryId(byte belongCountryId) {
		this.belongCountryId = belongCountryId;
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

	public byte getMapCountry() {
		return mapCountry;
	}

	public void setMapCountry(byte mapCountry) {
		this.mapCountry = mapCountry;
	}
	
	public String getLogStr() {
		return "{map:"+mapName+"}{country:"+mapCountry+"}{x:"+x+"}{y:"+y+"}";
	}
}
