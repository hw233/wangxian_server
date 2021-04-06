package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class PayMoneyFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(PayMoneyFlow.class);
	/**
	 * 名称
	 */
	private String userName="";
	/**
	 * 日期（精确到秒）
	 */
	private Long date=System.currentTimeMillis();
	/**
	 * 国家
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
	 * 充值金额
	 */
	private Long payMoney=0L;
	
	/**
	 * 充值类型 ，例如神州付，易宝等
	 */
	private String type="";
	/**
	 * 充值卡类型，例如盛大卡，神州行卡，联通卡
	 */
	private String cardType="";
	/**
	 * 渠道
	 */
	private String quDao="";
	/**
	 * 渠道手续费
	 */
	private String cost="";
	
	private String jixing="";//机型
	

	@Override
	public String toString() {
		return "PayMoneyFlow [cardType:" + cardType + "] [cost:" + cost + "] [date:" + date + "] [fenQu:" + fenQu + "] [game:" + game + "] [jixing:"
				+ jixing + "] [level:" + level + "] [payMoney:" + payMoney + "] [quDao:" + quDao + "] [type:" + type + "] [userName:" + userName
				+ "]";
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

	public Long getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Long payMoney) {
		this.payMoney = payMoney;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuDao() {
		return quDao;
	}

	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

}
