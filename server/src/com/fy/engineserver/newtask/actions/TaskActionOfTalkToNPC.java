package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig;

public class TaskActionOfTalkToNPC extends TaskAction implements TaskConfig {
	private TaskActionOfTalkToNPC() {
		setTargetType(TargetType.TALK_NPC);
	}

	public static TaskActionOfTalkToNPC createAction(String NPCName) {
		TaskActionOfTalkToNPC action = new TaskActionOfTalkToNPC();
		action.setName(NPCName);
		action.setNum(1);
		return action;
	}
}
