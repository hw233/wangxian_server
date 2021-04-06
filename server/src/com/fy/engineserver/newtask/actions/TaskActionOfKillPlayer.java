package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

public class TaskActionOfKillPlayer extends TaskAction implements TaskConfig {

	private Player self;
	private Player target;

	private TaskActionOfKillPlayer() {
		setTargetType(TargetType.KILL_PLAYER);
	}

	public static TaskAction createTaskAction(Player self, Player target) {
		TaskActionOfKillPlayer action = new TaskActionOfKillPlayer();
		action.setSelf(self);
		action.setTarget(target);
		return action;
	}

	public Player getSelf() {
		return self;
	}

	public void setSelf(Player self) {
		this.self = self;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}

	@Override
	public int getScore() {
		// TODO 等待公式 官阶 等级
		return (target.getLevel() + 1) * 2;
	}
}
