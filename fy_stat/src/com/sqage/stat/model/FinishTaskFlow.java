package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class FinishTaskFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;
	public static Logger logger = Logger.getLogger(FinishTaskFlow.class);

	private Long id=0L;
	private String fenQu="";
	private String userName="";
	private String taskName="";
	private String status="";
	private int getYOuXiBi;
	private int getWuPin;
	private int getDaoJu;
	/**
	 * 任务奖励
	 */
	private String award="";
	
	private String taskGroup="";  //任务组
	private String preTaskGroup="";  //前置任务组
	private String career="";     //职业
	
	/**
	 * 日期（精确到秒）
	 */
	private Long date=System.currentTimeMillis();
	private String jixing="";//机型

	@Override
	public String toString() {
		return "FinishTaskFlow [award:" + award + "] [career:" + career + "] [date:" + date + "] [fenQu:" + fenQu + "] [getDaoJu:" + getDaoJu
				+ "] [getWuPin:" + getWuPin + "] [getYOuXiBi:" + getYOuXiBi + "] [id:" + id + "] [jixing:" + jixing + "] [preTaskGroup:"
				+ preTaskGroup + "] [status:" + status + "] [taskGroup:" + taskGroup + "] [taskName:" + taskName + "] [userName:" + userName + "]";
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


	public void setGetDaoJu(int getDaoJu) {
		this.getDaoJu = getDaoJu;
	}


	public String getAward() {
		return award;
	}


	public void setAward(String award) {
		this.award = award;
	}


	public String getTaskGroup() {
		return taskGroup;
	}


	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}


	public String getPreTaskGroup() {
		return preTaskGroup;
	}


	public void setPreTaskGroup(String preTaskGroup) {
		this.preTaskGroup = preTaskGroup;
	}


	public String getCareer() {
		return career;
	}


	public void setCareer(String career) {
		this.career = career;
	}


	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
	
}
