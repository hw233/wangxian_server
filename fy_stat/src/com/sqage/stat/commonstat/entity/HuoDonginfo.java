package com.sqage.stat.commonstat.entity;

import java.util.Date;

public class HuoDonginfo {

	private Long id;
	private Date createDate;
	private String fenQu;
	private String userName;
	private String gameLevel;
	private String taskName;
	private String taskType;
	private String status;
	private int getYOuXiBi;
	private int getWuPin;
	private int getDaoJu;
	
	/**
	 * 获取奖励
	 */
	private String award;
	
	private String jixing;
	
	
	@Override
	public String toString() {
		return "HuoDonginfo [award:" + award + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "] [gameLevel:" + gameLevel + "] [getDaoJu:"
				+ getDaoJu + "] [getWuPin:" + getWuPin + "] [getYOuXiBi:" + getYOuXiBi + "] [id:" + id + "] [jixing:" + jixing + "] [status:"
				+ status + "] [taskName:" + taskName + "] [taskType:" + taskType + "] [userName:" + userName + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
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
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
	
}
