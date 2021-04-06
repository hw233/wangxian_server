package com.fy.engineserver.downcity.downcity3;

public class CityPlayer {

	private long playerId;
	private String playerName;
	private long playerDamage;
	private double playerPercent = 0;
	private double jiaZuPercent = 0;
	
	public void upPlayerDamage(long damage){
		playerDamage += damage * (1 + playerPercent / 100);
	}
	
	public long getPlayerDamage() {
		return playerDamage;
	}

	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public void setPlayerDamage(long playerDamage) {
		this.playerDamage = playerDamage;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public double getPlayerPercent() {
		return playerPercent;
	}

	public void setPlayerPercent(double playerPercent) {
		this.playerPercent = playerPercent;
	}

	public double getJiaZuPercent() {
		return jiaZuPercent;
	}

	public void setJiaZuPercent(double jiaZuPercent) {
		this.jiaZuPercent = jiaZuPercent;
	}

	@Override
	public String toString() {
		return "玩家id:" + playerId + "] [家族鼓舞:"+ jiaZuPercent + "] [玩家伤害:" + playerDamage+ "] [玩家鼓舞:" + playerPercent;
	}
	
	
}
