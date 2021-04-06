package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

public class TaskActionOfDiscovery extends TaskAction implements TaskConfig {

	private Player self;
	
	private TaskActionOfDiscovery() {
		setTargetType(TargetType.DISCOVERY);
	}

	public static TaskAction createTaskAction(Player self) {
		TaskActionOfDiscovery action = new TaskActionOfDiscovery();
		action.setSelf(self);
		return action;
	}

	public Player getSelf() {
		return self;
	}

	public void setSelf(Player self) {
		this.self = self;
	}
	
	
}
