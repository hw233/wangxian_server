package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;

public class TaskActionOfTaskDeliver extends TaskAction implements TaskConfig {
	private TaskActionOfTaskDeliver() {
		setTargetType(TargetType.TASK_DELIVER);
	}

	public static TaskActionOfTaskDeliver createAction(String taskName) {
		TaskActionOfTaskDeliver action = new TaskActionOfTaskDeliver();
		action.setName(taskName);
		action.setNum(1);
		return action;
	}
}
