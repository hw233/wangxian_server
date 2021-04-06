package com.sqage.stat.commonstat.entity;

import java.util.Date;

public class Taskinfo {

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
	
	private String taskGroup;  //任务组
	private String preTaskGroup;  //前置任务组
	private String career;        //职业

	private String jixing;
	
	@Override
	public String toString() {
		return "Taskinfo [award:" + award + "] [career:" + career + "] [createDate:" + createDate + "] [fenQu:" + fenQu + "] [gameLevel:" + gameLevel
				+ "] [getDaoJu:" + getDaoJu + "] [getWuPin:" + getWuPin + "] [getYOuXiBi:" + getYOuXiBi + "] [id:" + id + "] [jixing:" + jixing
				+ "] [preTaskGroup:" + preTaskGroup + "] [status:" + status + "] [taskGroup:" + taskGroup + "] [taskName:" + taskName
				+ "] [taskType:" + taskType + "] [userName:" + userName + "]";
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
	
}
