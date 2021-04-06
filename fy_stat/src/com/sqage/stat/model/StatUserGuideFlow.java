package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class StatUserGuideFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(StatUserGuideFlow.class);

	private Long createDate=System.currentTimeMillis();
	private Long registDate=System.currentTimeMillis();
	private String fenQu="";
	private String userName="";
	private String playerName="";
	private String quDao="";
	private String step="";//操作步骤
	private String action="1";//操作   1继续引导 2 跳过完成引导
	
	private String jixing="";//机型
	
	@Override
	public String toString() {
		return "StatUserGuideFlow [action:" + action + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "] [jixing:" + jixing + "] [playerName:"
				+ playerName + "] [quDao:" + quDao + "] [registDate:" + registDate + "] [step:" + step + "] [userName:" + userName + "]";
	}

	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public Long getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Long registDate) {
		this.registDate = registDate;
	}
	public String getFenQu() {
		return fenQu;
	}
	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getQuDao() {
		return quDao;
	}

	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}

	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
