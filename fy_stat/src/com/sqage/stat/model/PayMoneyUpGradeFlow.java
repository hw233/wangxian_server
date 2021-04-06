package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class PayMoneyUpGradeFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(PayMoneyUpGradeFlow.class);
	/**
	 * 名称
	 */
	private String userName="";
	/**
	 * 充值日期（精确到秒）
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
	
	
    /**
     * 设备型号
     */
	private String modelType="";
	/**
	 * vip
	 */
	private String vip="";
	/**
	 * 用户注册时间
	 */
	private Long registDate=0L;// 用户注册时间
	/** 
	 * 角色名称
	 */
	private String playName="";
	
	/**
	 * 备用字段
	 */
	private String colum1="";
	

	@Override
	public String toString() {
		return "PayMoneyUpGradeFlow [cardType:" + cardType + "] [colum1:" + colum1 + "] [cost:" + cost + "] [date:" + date + "] [fenQu:" + fenQu
				+ "] [game:" + game + "] [jixing:" + jixing + "] [level:" + level + "] [modelType:" + modelType
				+ "] [payMoney:" + payMoney + "] [playName:" + playName + "] [quDao:" + quDao + "] [registDate:" + registDate + "] [type:" + type
				+ "] [userName:" + userName + "] [vip:" + vip + "]";
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


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
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


	public String getJixing() {
		return jixing;
	}


	public void setJixing(String jixing) {
		this.jixing = jixing;
	}


	public String getModelType() {
		return modelType;
	}


	public void setModelType(String modelType) {
		this.modelType = modelType;
	}


	public String getVip() {
		return vip;
	}


	public void setVip(String vip) {
		this.vip = vip;
	}


	public String getPlayName() {
		return playName;
	}


	public void setPlayName(String playName) {
		this.playName = playName;
	}


	public String getColum1() {
		return colum1;
	}


	public void setColum1(String colum1) {
		this.colum1 = colum1;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Long getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Long registDate) {
		this.registDate = registDate;
	}
	
}
