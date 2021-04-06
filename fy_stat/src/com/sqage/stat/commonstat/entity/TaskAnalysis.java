package com.sqage.stat.commonstat.entity;

import java.io.Serializable;
import java.util.Date;

public class TaskAnalysis implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Date createDate;
	private String fenQu;
	private String taskName;
	private String taskType;
	
	private String taskGroup;
	private String preTaskGroup;
	private String career;
	
	private long acceptCount;
    private long finishCount;
    private long nextAcceptCount;
    
    private String jixing;
	
	@Override
	public String toString() {
		return "TaskAnalysis [acceptCount:" + acceptCount + "] [career:" + career + "] [createDate:" + createDate + "] [fenQu:" + fenQu
				+ "] [finishCount:" + finishCount + "] [id:" + id + "] [jixing:" + jixing + "] [nextAcceptCount:" + nextAcceptCount
				+ "] [preTaskGroup:" + preTaskGroup + "] [taskGroup:" + taskGroup + "] [taskName:" + taskName + "] [taskType:" + taskType + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	public long getAcceptCount() {
		return acceptCount;
	}
	public void setAcceptCount(long acceptCount) {
		this.acceptCount = acceptCount;
	}
	public long getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(long finishCount) {
		this.finishCount = finishCount;
	}
	public long getNextAcceptCount() {
		return nextAcceptCount;
	}
	public void setNextAcceptCount(long nextAcceptCount) {
		this.nextAcceptCount = nextAcceptCount;
	}
	public String getJixing() {
		return jixing;
	}
	public void setJixing(String jixing) {
		this.jixing = jixing;
	}
    
}
