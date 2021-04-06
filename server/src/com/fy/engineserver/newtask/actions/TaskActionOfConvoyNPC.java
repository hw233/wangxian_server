package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;

public class TaskActionOfConvoyNPC extends TaskAction implements TaskConfig {

	private String mapName;
	private String areaName;

	private TaskActionOfConvoyNPC() {
		setTargetType(TargetType.CONVOY);
	}

	public static TaskAction createTaskAction(String NPCName,int npcColor) {
		TaskActionOfConvoyNPC action = new TaskActionOfConvoyNPC();
		action.setName(NPCName);
		action.setColor(npcColor);
		action.setNum(1);
		action.setScore(npcColor);
		return action;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
