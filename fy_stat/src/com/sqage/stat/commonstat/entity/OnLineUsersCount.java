/**
 * 
 */
package com.sqage.stat.commonstat.entity;

import java.util.Date;

/**
 * 
 *
 */
public class OnLineUsersCount {

	  private Long id;
	  private Date onlineTime;
	  private String game;
	  private String fenQu;
	  private String quDao;
	  private Long userCount;
	  
	  private String jixing;
	
	@Override
	public String toString() {
		return "OnLineUsersCount [fenQu:" + fenQu + "] [game:" + game + "] [id:" + id + "] [jixing:" + jixing + "] [onlineTime:" + onlineTime
				+ "] [quDao:" + quDao + "] [userCount:" + userCount + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
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
	public Long getUserCount() {
		return userCount;
	}
	public void setUserCount(Long userCount) {
		this.userCount = userCount;
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
