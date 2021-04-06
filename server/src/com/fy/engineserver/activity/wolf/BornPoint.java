package com.fy.engineserver.activity.wolf;

public class BornPoint {
	
	private long playerId;
	//0:羊，1:狼
	private int role;
	private int x;
	private int y;
	public BornPoint(long playerId, int role, int x, int y) {
		this.playerId = playerId;
		this.role = role;
		this.x = x;
		this.y = y;
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
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
	
	@Override
	public String toString() {
		return "[playerId=" + playerId + ", role=" + role + ", x=" + x + ", y="
				+ y + "]";
	}
	
}
