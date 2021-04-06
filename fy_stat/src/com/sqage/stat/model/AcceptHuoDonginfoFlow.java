package com.sqage.stat.model;

import java.io.Serializable;

import org.apache.log4j.Logger;

public class AcceptHuoDonginfoFlow implements Serializable {
	
	private static final long serialVersionUID = -9203088531016193193L;

	public static Logger logger = Logger.getLogger(AcceptHuoDonginfoFlow.class);

	private Long id=0L;
	private Long createDate=System.currentTimeMillis();
	private String fenQu="";
	private String userName="";
	private String gameLevel="";
	private String taskName="";
	private String taskType="";
	
	private String jixing="";//机型
	
	@Override
	public String toString() {
		return "AcceptHuoDonginfoFlow [createDate:" + createDate + "] [fenQu:" + fenQu + "] [gameLevel:" + gameLevel + "] [id:" + id + "] [jixing:"
				+ jixing + "] [taskName:" + taskName + "] [taskType:" + taskType + "] [userName:" + userName + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
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

	public String getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(String gameLevel) {
		this.gameLevel = gameLevel;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getJixing() {
		return jixing;
	}

	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	
	
}
