package com.fy.engineserver.activity.dice;

import java.util.Arrays;

public class DicePointData {

	private long pid;
	private String playerName = "";
	private int point;
	private int[] pointList = new int[3];
	
	public DicePointData(long pid,String playerName, int point, int[] pointList) {
		super();
		this.pid = pid;
		this.playerName = playerName;
		this.point = point;
		this.pointList = pointList;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int[] getPointList() {
		return pointList;
	}
	public void setPointList(int[] pointList) {
		this.pointList = pointList;
	}
	
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	@Override
	public String toString() {
		return "[pid=" + pid + ", playerName=" + playerName
				+ ", point=" + point + ", pointList="
				+ Arrays.toString(pointList) + "]";
	}
	
}
