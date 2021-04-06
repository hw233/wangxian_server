package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class FinishHuoDonginfoFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(FinishHuoDonginfoFlow.class);

	private Long id=0L;
	private String fenQu="";
	private String userName="";
	private String taskName="";
	private String status="";
	private int getYOuXiBi=0;
	private int getWuPin=0;
	private int getDaoJu=0;
	
	
	/**
	 * 日期（精确到秒）
	 */
	private Long date=System.currentTimeMillis();
	private String jixing="";//机型
	
	/**
	 * 任务奖励
	 */
	private String award="";
	
	@Override
	public String toString() {
		return "FinishHuoDonginfoFlow [award:" + award + "] [date:" + date + "] [fenQu:" + fenQu + "] [getDaoJu:" + getDaoJu + "] [getWuPin:"
				+ getWuPin + "] [getYOuXiBi:" + getYOuXiBi + "] [id:" + id + "] [jixing:" + jixing + "] [status:" + status + "] [taskName:"
				+ taskName + "] [userName:" + userName + "]";
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public int getGetYOuXiBi() {
		return getYOuXiBi;
	}


	public void setGetYOuXiBi(int getYOuXiBi) {
		this.getYOuXiBi = getYOuXiBi;
	}


	public int getGetWuPin() {
		return getWuPin;
	}


	public void setGetWuPin(int getWuPin) {
		this.getWuPin = getWuPin;
	}


	public int getGetDaoJu() {
		return getDaoJu;
	}


	public String getAward() {
		return award;
	}


	public void setAward(String award) {
		this.award = award;
	}


	public void setGetDaoJu(int getDaoJu) {
		this.getDaoJu = getDaoJu;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
	
}
