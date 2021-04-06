package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class LogOutGameFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(LogOutGameFlow.class);
	/**
	 * 名称
	 */
	private String userName="";
	/**
	 * 日期（精确到秒）
	 */
	private Long date=System.currentTimeMillis();
	/**
	 * 游戏
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
	 * 在线时长
	 */
	private Long onLineTime;
	
	private Long yuanBaoCount;  //剩余银子数
	
	private Long youXiBi;       //vip
	
	/**
	 * 渠道
	 */
	private String quDao="";
	
	private String jixing="";//机型

	@Override
	public String toString() {
		return "LogOutGameFlow [date:" + date + "] [fenQu:" + fenQu + "] [game:" + game + "] [jixing:" + jixing + "] [level:" + level
				+ "] [onLineTime:" + onLineTime + "] [quDao:" + quDao + "] [userName:" + userName + "] [youXiBi:" + youXiBi + "] [yuanBaoCount:"
				+ yuanBaoCount + "]";
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

	public Long getOnLineTime() {
		return onLineTime;
	}

	public void setOnLineTime(Long onLineTime) {
		this.onLineTime = onLineTime;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public Long getYuanBaoCount() {
		return yuanBaoCount;
	}

	public void setYuanBaoCount(Long yuanBaoCount) {
		this.yuanBaoCount = yuanBaoCount;
	}

	public Long getYouXiBi() {
		return youXiBi;
	}

	public void setYouXiBi(Long youXiBi) {
		this.youXiBi = youXiBi;
	}


	public String getQuDao() {
		return quDao;
	}


	public void setQuDao(String quDao) {
		this.quDao = quDao;
	}
	
}
