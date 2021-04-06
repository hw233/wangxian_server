package com.fy.engineserver.guozhan;

import java.io.Serializable;

/**
 *
 * 
 * @version 创建时间：May 7, 2012 9:40:16 PM
 * 
 */
public class GuozhanStat implements Serializable {
	
	public static final long serialVersionUID = 679348293282948L;
	
	private byte countryId;
	
	private int winNum;
	
	private int loseNum;
	
	private int attackWinNum;
	
	private int defendLoseNum;

	public byte getCountryId() {
		return countryId;
	}

	public void setCountryId(byte countryId) {
		this.countryId = countryId;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public int getLoseNum() {
		return loseNum;
	}

	public void setLoseNum(int loseNum) {
		this.loseNum = loseNum;
	}

	public int getAttackWinNum() {
		return attackWinNum;
	}

	public void setAttackWinNum(int attackWinNum) {
		this.attackWinNum = attackWinNum;
	}

	public int getDefendLoseNum() {
		return defendLoseNum;
	}

	public void setDefendLoseNum(int defendLoseNum) {
		this.defendLoseNum = defendLoseNum;
	}
	
	
}
