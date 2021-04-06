package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class GameChongZhiFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(GameChongZhiFlow.class);
	
	private String userName="";
	private Long time;
	private String game="";
	private String fenQu="";
    private String gameLevel="";
    
    private Long money;
    //0 充值 ，1 消耗
    private int action; //充值还是消耗
    
    private String currencyType="";//货币类型
    
    private String reasonType=""; //原因
    
    private String quDao="";
	
    private String jixing="";//机型
    
	@Override
	public String toString() {
		return "GameChongZhiFlow [action:" + action + "] [currencyType:" + currencyType + "] [fenQu:" + fenQu + "] [game:" + game + "] [gameLevel:"
				+ gameLevel + "] [jixing:" + jixing + "] [money:" + money + "] [quDao:" + quDao + "] [reasonType:" + reasonType + "] [time:" + time
				+ "] [userName:" + userName + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	public Long getTime() {
		return time;
	}


	public void setTime(Long time) {
		this.time = time;
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

	public String getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(String gameLevel) {
		this.gameLevel = gameLevel;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getQuDao() {
		return quDao;
	}

	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	
}
