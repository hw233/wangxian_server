package com.fy.engineserver.qiancengta;

/**
 * 千层塔里的排行榜的玩家
 * 
 *
 */
public class QianCengTa_Player {

	long playerId;
	String playerName;
	int country;
	int career;
	int playerLevel;
	
	int maxDaoIndex;
	int maxCengIndex;
	long reachMaxTime;
	
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
	public int getCountry() {
		return country;
	}
	public void setCountry(int country) {
		this.country = country;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}
	public int getMaxDaoIndex() {
		return maxDaoIndex;
	}
	public void setMaxDaoIndex(int maxDaoIndex) {
		this.maxDaoIndex = maxDaoIndex;
	}
	public int getMaxCengIndex() {
		return maxCengIndex;
	}
	public void setMaxCengIndex(int maxCengIndex) {
		this.maxCengIndex = maxCengIndex;
	}
	public long getReachMaxTime() {
		return reachMaxTime;
	}
	public void setReachMaxTime(long reachMaxTime) {
		this.reachMaxTime = reachMaxTime;
	}
}
