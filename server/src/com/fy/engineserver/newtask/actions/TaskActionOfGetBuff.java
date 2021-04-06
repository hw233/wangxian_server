package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

public class TaskActionOfGetBuff extends TaskAction {

	private TaskActionOfGetBuff() {
		setTargetType(TargetType.BUFF);
	}

	public static TaskAction createTaskAction(Buff buff) {
		TaskActionOfGetBuff actionOfGetBuff = new TaskActionOfGetBuff();
		actionOfGetBuff.setName(buff.getTemplateName());
		actionOfGetBuff.setColor(buff.getLevel());
		actionOfGetBuff.setGrade(0);
		actionOfGetBuff.setNum(1);
		return actionOfGetBuff;
	}
}
