package com.fy.engineserver.tournament.data;

public class TournamentRankDataClient {

	/**
	 * 国家
	 */
	public byte country;
	
	public long id;
	
	/**
	 * 玩家名字
	 */
	public String name;
	
	/**
	 * 职业
	 */
	public byte career;
	
	/**
	 * 级别
	 */
	public short level;
	
	/**
	 * 积分
	 */
	public int point;
	
	/**
	 * 胜利场数
	 */
	public int win;
	
	/**
	 * 失败场数
	 */
	public int lost;

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

}
