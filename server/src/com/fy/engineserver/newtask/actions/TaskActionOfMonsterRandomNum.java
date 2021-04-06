package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Sprite;

public class TaskActionOfMonsterRandomNum extends TaskAction {

	public TaskActionOfMonsterRandomNum() {
		setTargetType(TargetType.MONSTER_RANDOM_NUM);
	}

	public static TaskAction createTaskAction(Sprite s) {
		TaskActionOfMonsterRandomNum action = new TaskActionOfMonsterRandomNum();
		action.setColor(1);
		action.setName(s.getName());
		action.setNum(1);
		return action;
	}
}
