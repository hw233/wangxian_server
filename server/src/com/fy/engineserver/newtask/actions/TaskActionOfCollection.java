package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

public class TaskActionOfCollection extends TaskAction {

	private TaskActionOfCollection() {
		setTargetType(TargetType.COLLECTION);
	}

	public static TaskActionOfCollection createTaskAction(int color, String name, int num) {
		TaskActionOfCollection collection = new TaskActionOfCollection();
		collection.setColor(color);
		collection.setName(name);
		collection.setNum(num);
		return collection;
	}
}
