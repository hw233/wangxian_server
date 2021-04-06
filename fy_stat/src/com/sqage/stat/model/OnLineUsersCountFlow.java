package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class OnLineUsersCountFlow implements Serializable{

	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(OnLineUsersCountFlow.class);
	
	  private Long onlineTime=System.currentTimeMillis();
	  private String game="";
	  private String fenQu="";
	  private String quDao="";
	  private Long userCount;
	  private String jixing="";//机型

	@Override
	public String toString() {
		return "OnLineUsersCountFlow [fenQu:" + fenQu + "] [game:" + game + "] [jixing:" + jixing + "] [onlineTime:" + onlineTime + "] [quDao:"
				+ quDao + "] [userCount:" + userCount + "]";
	}
	public Long getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Long onlineTime) {
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
