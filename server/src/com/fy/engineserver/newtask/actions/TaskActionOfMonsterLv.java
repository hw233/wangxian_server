package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Sprite;

public class TaskActionOfMonsterLv extends TaskAction {
	private TaskActionOfMonsterLv() {
		setTargetType(TargetType.MONSTER_LV);
	}

	public static TaskActionOfMonsterLv createAction(Sprite s) {
		return createAction(s, 1);
	}

	public static TaskActionOfMonsterLv createAction(Sprite s, int num) {
		TaskActionOfMonsterLv action = new TaskActionOfMonsterLv();
		action.setName(s.getName());
		action.setNum(num);
		action.setGrade(s.getLevel());
		return action;
	}
	
	@Override
	public int getScore() {
		//TODO 等公式  颜色
		return getName().length();
	}
}
