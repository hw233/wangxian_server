package com.fy.engineserver.country.data;

public class CountryQiujinAndJinyanForClient {

	long playerId;
	String playerName = "";
	//剩余囚禁时间
	long lastQiujinTime;
	//剩余禁言时间
	long lastJinyanTime;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public long getLastQiujinTime() {
		return lastQiujinTime;
	}
	public void setLastQiujinTime(long lastQiujinTime) {
		this.lastQiujinTime = lastQiujinTime;
	}
	public long getLastJinyanTime() {
		return lastJinyanTime;
	}
	public void setLastJinyanTime(long lastJinyanTime) {
		this.lastJinyanTime = lastJinyanTime;
	}
	
}
