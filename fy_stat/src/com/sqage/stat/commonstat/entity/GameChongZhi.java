/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 游戏中的货币充值，消耗
 * 
 *
 */
public class GameChongZhi {
	
	private Long id;
	private String userName;
	private Date time;
	private String game;
	private String fenQu;
    private String gameLevel;
    
    private Long money;
    //0 充值 ，1 消耗
    private int action; //充值还是消耗
    
    private String currencyType;//货币类型
    
    private String reasonType; //原因
    
    private String quDao;
	
    private String jixing;
    
   
	@Override
	public String toString() {
		return "GameChongZhi [action:" + action + "] [currencyType:" + currencyType + "] [fenQu:" + fenQu + "] [game:" + game + "] [gameLevel:"
				+ gameLevel + "] [id:" + id + "] [jixing:" + jixing + "] [money:" + money + "] [quDao:" + quDao + "] [reasonType:" + reasonType
				+ "] [time:" + time + "] [userName:" + userName + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
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
