package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;

/**
 * 任务目标 - 社会关系
 * 
 * 
 */
public class TaskActionOfRelation extends TaskAction implements TaskConfig {
	
	private int type ;
	
	private TaskActionOfRelation() {
		setTargetType(TargetType.RELATION);
	}
	
	public static TaskAction createTaskAction (int type) {
		TaskActionOfRelation action = new TaskActionOfRelation();
		
		return action;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
