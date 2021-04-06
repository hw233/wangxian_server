package com.fy.engineserver.billboard;


public class BillboardData {
	/**
	 * 名字
	 */
	String rankObject;
	
	/**
	 * 描述
	 */
	String description="";
	
	/**
	 * 排行依据
	 */
	long value;
	
	long id;
	
	int rank;
	
	int additionalInfo;
	
	/**
	 * 玩家ID，装备排行榜专用
	 */
	long playerId;
	
	public BillboardData(int rank){
		this.setRank(rank);
	}

	public String getRankingObject() {
		return rankObject;
	}

	public void setRankingObject(String name) {
		this.rankObject = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(int politicalCamp) {
		this.additionalInfo = politicalCamp;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	
}
