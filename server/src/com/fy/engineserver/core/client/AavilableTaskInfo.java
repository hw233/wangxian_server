package com.fy.engineserver.core.client;

import com.fy.engineserver.newtask.Task;

/**
 * 可接任务信息
 * 
 */
public class AavilableTaskInfo {

	private long taskId;
	private byte showType;
	private String taskName;
	private int grade;
	private String mapName;
	private String npcName;

	private int x;
	private int y;

	public AavilableTaskInfo() {

	}

	public AavilableTaskInfo(Task task) {
		this.taskId = task.getId();
		this.showType = task.getShowType();
		this.taskName = task.getName();
		this.grade = task.getGrade();
		this.mapName = task.getStartMap();
		this.npcName = task.getStartNpc();
		this.x = task.getStartX();
		this.y = task.getStartY();
		if (mapName == null) {
			mapName = "";
		}
		
		if (npcName == null) {
			npcName = "";
		}
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public byte getShowType() {
		return showType;
	}

	public void setShowType(byte showType) {
		this.showType = showType;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getNpcName() {
		return npcName;
	}

	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}
}
