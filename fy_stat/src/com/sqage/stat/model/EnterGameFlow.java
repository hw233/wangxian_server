package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class EnterGameFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(EnterGameFlow.class);
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
	 * 渠道
	 */
	private String quDao="";
	private String jixing="";//机型
	
	private String zhiye="";// 职业
	private String column1="";//备用扩展项
	private String column2="";//备用扩展项
	
	@Override
	public String toString() {
		return "EnterGameFlow [column1:" + column1 + "] [column2:" + column2 + "] [date:" + date + "] [fenQu:" + fenQu + "] [game:" + game
				+ "] [jixing:" + jixing + "] [level:" + level + "] [quDao:" + quDao + "] [userName:" + userName + "] [zhiye:" + zhiye + "]";
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

	public String getZhiye() {
		return zhiye;
	}

	public void setZhiye(String zhiye) {
		this.zhiye = zhiye;
	}

	public String getColumn1() {
		return column1;
	}

	public void setColumn1(String column1) {
		this.column1 = column1;
	}

	public String getColumn2() {
		return column2;
	}

	public void setColumn2(String column2) {
		this.column2 = column2;
	}

	
}
