package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class UserRegistFlow implements Serializable {

	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(UserRegistFlow.class);
	/**
	 * 注册时间
	 */
	private Long registtime=System.currentTimeMillis();
    /**
      * 名称
      */
	private String userName="";
	/**
	 * 角色名
	 */
   private String playerName="";
	/**
	 * 渠道
	 */
   private String qudao="";
   /**
    * 游戏
    */
	private String game="";
	/**
	 * 机型
	 */
	private String jixing="";
	/**
	 * 地点
	 */
	private String didian="";
	
	/**
	 * 号码
	 */
	private String haoma="";
	
	/**
	 * EMEI
	 */
	private String emei="";
	/**
	 * 国家
	 */
	private String nations="";
	
	/**
	 * 创建角色时间
	 */
	private Long creatPlayerTime=System.currentTimeMillis();
	/**
	 * 游戏分区
	 */
	private String fenQu="";
	
	
	@Override
	public String toString() {
		return "UserRegistFlow [creatPlayerTime:" + creatPlayerTime + "] [didian:" + didian + "] [emei:" + emei + "] [fenQu:" + fenQu + "] [game:"
				+ game + "] [haoma:" + haoma + "] [jixing:" + jixing + "] [nations:" + nations + "] [playerName:" + playerName + "] [qudao:" + qudao
				+ "] [registtime:" + registtime + "] [userName:" + userName + "]";
	}

	public Long getRegisttime() {
		return registtime;
	}

	public void setRegisttime(Long registtime) {
		this.registtime = registtime;
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

	public String getQudao() {
		return qudao;
	}

	public void setQudao(String qudao) {
		this.qudao = qudao;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public String getDidian() {
		return didian;
	}

	public void setDidian(String didian) {
		this.didian = didian;
	}

	public String getHaoma() {
		return haoma;
	}

	public void setHaoma(String haoma) {
		this.haoma = haoma;
	}

	public String getEmei() {
		return emei;
	}

	public void setEmei(String emei) {
		this.emei = emei;
	}

	public String getNations() {
		return nations;
	}

	public void setNations(String nations) {
		this.nations = nations;
	}

	public Long getCreatPlayerTime() {
		return creatPlayerTime;
	}

	public void setCreatPlayerTime(Long creatPlayerTime) {
		this.creatPlayerTime = creatPlayerTime;
	}

	public String getFenQu() {
		return fenQu;
	}

	public void setFenQu(String fenQu) {
		this.fenQu = fenQu;
	}

}
