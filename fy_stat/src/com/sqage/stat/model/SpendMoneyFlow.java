package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class SpendMoneyFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(SpendMoneyFlow.class);
	/**
	 * 名称
	 */
	private String userName="";
	/**
	 * 日期（精确到秒）
	 */
	private Long date=System.currentTimeMillis();
	/**
	 * 游戏
	 */
	private String game="";
	/**
	 * 分区
	 */
	private String fenQu="";
	/**
	 * 级别
	 */
	private String level="";
	
	/**
	 * 消费金额
	 */
	private Long spendMoney=0L;
	
	private String jixing="";//机型

	@Override
	public String toString() {
		return "SpendMoneyFlow [date:" + date + "] [fenQu:" + fenQu + "] [game:" + game + "] [jixing:" + jixing + "] [level:" + level
				+ "] [spendMoney:" + spendMoney + "] [userName:" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getFenQu() {
		return fenQu;
	}

	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getSpendMoney() {
		return spendMoney;
	}

	public void setSpendMoney(Long spendMoney) {
		this.spendMoney = spendMoney;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

}
