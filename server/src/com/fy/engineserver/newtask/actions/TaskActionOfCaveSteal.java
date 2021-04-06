package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

public class TaskActionOfCaveSteal extends TaskAction {

	private int fruitType;

	private TaskActionOfCaveSteal() {
		setTargetType(TargetType.CAVE_STEAL);
	}

	public static TaskAction createTaskAction(int fruitType) {
		TaskActionOfCaveSteal taskAction = new TaskActionOfCaveSteal();
		taskAction.setFruitType(fruitType);
		taskAction.setColor(-1);
		taskAction.setNum(1);
		return taskAction;
	}

	public int getFruitType() {
		return fruitType;
	}

	public void setFruitType(int fruitType) {
		this.fruitType = fruitType;
	}

}
