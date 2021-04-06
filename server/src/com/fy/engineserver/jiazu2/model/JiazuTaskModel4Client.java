package com.fy.engineserver.jiazu2.model;

public class JiazuTaskModel4Client {
	private long taskId;
	
	private String taskName;
	
	private String des;
	
	private int star;
	
	private String rewardDes;
	
	private String targetDes;
	
	private int targetLevel;

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getRewardDes() {
		return rewardDes;
	}

	public void setRewardDes(String rewardDes) {
		this.rewardDes = rewardDes;
	}

	public String getTargetDes() {
		return targetDes;
	}

	public void setTargetDes(String targetDes) {
		this.targetDes = targetDes;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

	public void setTargetLevel(int targetLevel) {
		this.targetLevel = targetLevel;
	}
	
	
}
