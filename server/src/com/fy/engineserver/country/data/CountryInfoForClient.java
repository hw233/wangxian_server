package com.fy.engineserver.country.data;

public class CountryInfoForClient {

	long playerId;
	int countryPosition = -1;
	String playerName = "";
	int level;
	String zongPaiName = "";
	boolean onLine;
	boolean biaozhang;
	boolean shouxun;
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getCountryPosition() {
		return countryPosition;
	}
	public void setCountryPosition(int countryPosition) {
		this.countryPosition = countryPosition;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getZongPaiName() {
		return zongPaiName;
	}
	public void setZongPaiName(String zongPaiName) {
		this.zongPaiName = zongPaiName;
	}
	public boolean isOnLine() {
		return onLine;
	}
	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}
	public boolean isBiaozhang() {
		return biaozhang;
	}
	public void setBiaozhang(boolean biaozhang) {
		this.biaozhang = biaozhang;
	}
	public boolean isShouxun() {
		return shouxun;
	}
	public void setShouxun(boolean shouxun) {
		this.shouxun = shouxun;
	}
	
}
