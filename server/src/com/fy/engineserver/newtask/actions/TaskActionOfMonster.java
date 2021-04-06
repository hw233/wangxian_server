package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Sprite;

public class TaskActionOfMonster extends TaskAction implements TaskConfig {

	private TaskActionOfMonster() {
		setTargetType(TargetType.MONSTER);
	}

	public static TaskActionOfMonster createAction(Sprite s) {
		TaskActionOfMonster action = new TaskActionOfMonster();
		action.setName(s.getName());
		action.setNum(1);
		return action;
	}
	
	@Override
	public int getScore() {
		//TODO 等公式  颜色
		return getName().length();
	}
}
