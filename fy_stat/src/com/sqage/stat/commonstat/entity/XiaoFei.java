/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 
 *
 */
public class XiaoFei {
	
	private Long id;
	private String userName;
	private Date time;
	private String game;
	private String fenQu;
    private String gameLevel;
    private Long money;
    
    private String jixing;
    
	@Override
	public String toString() {
		return "XiaoFei [fenQu:" + fenQu + "] [game:" + game + "] [gameLevel:" + gameLevel + "] [id:" + id + "] [jixing:" + jixing + "] [money:"
				+ money + "] [time:" + time + "] [userName:" + userName + "]";
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
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

}
