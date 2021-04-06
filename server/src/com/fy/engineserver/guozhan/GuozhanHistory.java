package com.fy.engineserver.guozhan;

import java.io.Serializable;

import com.fy.engineserver.datasource.language.Translate;

/**
 *
 * 
 * @version 创建时间：May 7, 2012 9:02:20 PM
 * 
 */
public class GuozhanHistory implements Serializable {
	
	public static final long serialVersionUID = 973046589656905L;
	
	private long id;
	
	/**
	 * 攻击国
	 */
	private String attackCountryName;
	
	/**
	 * 攻击国国王
	 */
	private String attackKing;
	
	/**
	 * 进攻方击杀榜
	 */
	private String attackTopPlayers[];
	
	private int attackTopKillNums[];
	
	/**
	 * 防守国
	 */
	private String defendCountryName;
	
	/**
	 * 防守国国王
	 */
	private String defendKing;
	
	/**
	 * 防守方击杀榜
	 */
	private String defendTopPlayers[];
	
	private int defendTopKillNums[];
	
	/**
	 * 时间
	 */
	private long openTime;
	
	/**
	 * 结果, 0-进攻方获胜， 1-防御方获胜
	 */
	private int result;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAttackCountryName() {
		return attackCountryName;
	}

	public void setAttackCountryName(String attackCountryName) {
		this.attackCountryName = attackCountryName;
	}

	public String getDefendCountryName() {
		return defendCountryName;
	}

	public void setDefendCountryName(String defendCountryName) {
		this.defendCountryName = defendCountryName;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
	
	
	public String getAttackKing() {
		return attackKing;
	}

	public void setAttackKing(String attackKing) {
		this.attackKing = attackKing;
	}

	public String getDefendKing() {
		return defendKing;
	}

	public void setDefendKing(String defendKing) {
		this.defendKing = defendKing;
	}

	public String[] getAttackTopPlayers() {
		return attackTopPlayers;
	}

	public void setAttackTopPlayers(String[] attackTopPlayers) {
		this.attackTopPlayers = attackTopPlayers;
	}

	public int[] getAttackTopKillNums() {
		return attackTopKillNums;
	}

	public void setAttackTopKillNums(int[] attackTopKillNums) {
		this.attackTopKillNums = attackTopKillNums;
	}

	public String[] getDefendTopPlayers() {
		return defendTopPlayers;
	}

	public void setDefendTopPlayers(String[] defendTopPlayers) {
		this.defendTopPlayers = defendTopPlayers;
	}

	public int[] getDefendTopKillNums() {
		return defendTopKillNums;
	}

	public void setDefendTopKillNums(int[] defendTopKillNums) {
		this.defendTopKillNums = defendTopKillNums;
	}

	public String getResultDesp() {
		return result==0?(attackCountryName+Translate.攻城成功):(defendCountryName+Translate.防御成功);
	}
}
