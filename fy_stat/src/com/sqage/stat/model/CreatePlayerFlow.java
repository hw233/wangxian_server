package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class CreatePlayerFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(CreatePlayerFlow.class);
	
	private String game="";
	
	private String userName="";
	 
	private String playerName="";
	
    private String qudao="";
    
    private String sex="";
    
    private String nation="";//国家
     
    private String fenQu="";//分区（即服务器）
	
	/**
	 * 创建角色时间
	 */
	private Long creatPlayerTime=System.currentTimeMillis();

	@Override
	public String toString() {
		return "CreatePlayerFlow [creatPlayerTime:" + creatPlayerTime + "] [fenQu:" + fenQu + "] [game:" + game + "] [nation:" + nation
				+ "] [playerName:" + playerName + "] [qudao:" + qudao + "] [sex:" + sex + "] [userName:" + userName + "]";
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

	public Long getCreatPlayerTime() {
		return creatPlayerTime;
	}

	public void setCreatPlayerTime(Long creatPlayerTime) {
		this.creatPlayerTime = creatPlayerTime;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getFenQu() {
		return fenQu;
	}

	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}

}
