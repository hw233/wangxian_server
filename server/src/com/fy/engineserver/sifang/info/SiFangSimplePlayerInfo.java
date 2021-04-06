package com.fy.engineserver.sifang.info;

public class SiFangSimplePlayerInfo {

	private long playerId;
	
	private String playerName;
	
	private int career;
	
	private int playerLevel;

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getCareer() {
		return career;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

}
