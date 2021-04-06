package com.fy.engineserver.economic.thirdpart.migu.entity.model;

public class TempPlayerInfo {
	private long playerId;
	
	private String name;
	
	private String serverName;
	
	private int career;
	
	private int playerLevel;
	
	private int vipLevel;
	
	private int country;
	
	public TempPlayerInfo(long playerId, String name, String serverName, int career, int playerLevel, int vipLevel, int country) {
		super();
		this.playerId = playerId;
		this.name = name;
		this.serverName = serverName;
		this.career = career;
		this.playerLevel = playerLevel;
		this.vipLevel = vipLevel;
		this.country = country;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
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

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}
	
	
}
