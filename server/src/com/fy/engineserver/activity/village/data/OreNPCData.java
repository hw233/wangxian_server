package com.fy.engineserver.activity.village.data;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


@SimpleEmbeddable
public class OreNPCData {

	/**
	 * 数组下标，即在矿数组的位置，这个位置固定，一旦生成就不更改了
	 */
	int arrayIndex;
	
	/**
	 * 占领家族id
	 */
	long jiazuId;
	
	/**
	 * 占领后族长修改的矿名
	 */
	String newName = "";
	
	/**
	 * 所在地图名
	 */
	String mapName = "";
	
	/**
	 * 所在地图国家
	 */
	byte country;
	
	/**
	 * 刷新位置x
	 */
	int x;
	
	/**
	 * 刷新位置y
	 */
	int y;
	
	/**
	 * 这个矿附近的复活点
	 */
	int[] relivePoints = new int[4];
	
	public OreNPCData() {
		// TODO Auto-generated constructor stub
	}

	public long getJiazuId() {
		return jiazuId;
	}

	public void setJiazuId(long jiazuId) {
		this.jiazuId = jiazuId;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public int getArrayIndex() {
		return arrayIndex;
	}

	public void setArrayIndex(int arrayIndex) {
		this.arrayIndex = arrayIndex;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
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

	public int[] getRelivePoints() {
		return relivePoints;
	}

	public void setRelivePoints(int[] relivePoints) {
		this.relivePoints = relivePoints;
	}
}
