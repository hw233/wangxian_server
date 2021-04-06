package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

public class TaskActionOfCaveHarvest extends TaskAction {

	private int fruitType;

	private TaskActionOfCaveHarvest() {
		setTargetType(TargetType.CAVE_HARVEST);
	}

	public static TaskAction createTaskAction(int fruitType) {
		TaskActionOfCaveHarvest taskAction = new TaskActionOfCaveHarvest();
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
