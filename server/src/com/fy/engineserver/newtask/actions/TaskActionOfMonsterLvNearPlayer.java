package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;

public class TaskActionOfMonsterLvNearPlayer extends TaskAction {

	private Player self;

	private TaskActionOfMonsterLvNearPlayer() {
		setTargetType(TargetType.MONSTER_LV_PLAYER);
	}

	public static TaskAction createTaskAction(Sprite s, Player self) {
		return createTaskAction(s, self, 1);
	}

	public static TaskAction createTaskAction(Sprite s, Player self, int num) {
		TaskActionOfMonsterLvNearPlayer action = new TaskActionOfMonsterLvNearPlayer();
		action.setGrade(s.getLevel());
		action.setNum(num);
		// TODO 怪颜色
		action.setColor(1);
		action.setSelf(self);
		return action;
	}

	public Player getSelf() {
		return self;
	}

	public void setSelf(Player self) {
		this.self = self;
	}
	@Override
	public int getScore() {
		//TODO 等公式  颜色
		return getName().length();
	}
}
