package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

/**
 * 种植
 * 
 * 
 */
public class TaskActionOfCavePlant extends TaskAction {

	private int plantType;

	private TaskActionOfCavePlant() {
		setTargetType(TargetType.CAVE_PLANT);
	}

	public static TaskAction createTaskAction(int plantType) {
		TaskActionOfCavePlant actionOfCavePlant = new TaskActionOfCavePlant();
		actionOfCavePlant.setColor(-1);
		actionOfCavePlant.setNum(1);
		actionOfCavePlant.setPlantType(plantType);
		return actionOfCavePlant;
	}

	public int getPlantType() {
		return plantType;
	}

	public void setPlantType(int plantType) {
		this.plantType = plantType;
	}

}
