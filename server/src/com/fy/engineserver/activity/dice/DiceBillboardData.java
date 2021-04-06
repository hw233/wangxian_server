package com.fy.engineserver.activity.dice;

public class DiceBillboardData {

	private int rank;
	private String playerName = "";
	private int point;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
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
	@Override
	public String toString() {
		return "DiceBillboardData [playerName=" + playerName + ", point="
				+ point + ", rank=" + rank + "]";
	}
	
}
