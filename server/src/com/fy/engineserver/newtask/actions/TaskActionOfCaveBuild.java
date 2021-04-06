package com.fy.engineserver.newtask.actions;

import com.fy.engineserver.newtask.service.TaskConfig.TargetType;

/**
 * 0 逍遥居,1 万宝库,2 驭兽斋,3 门牌,4 法门,5 田地1,6 田地2,7 田地3,8 田地4,9 田地5,10 田地6
 * 
 * 
 */
public class TaskActionOfCaveBuild extends TaskAction {

	/** 建筑类型 */
	private int buildType;
	/** 建筑等级 */
	private int level;

	private TaskActionOfCaveBuild() {
		setTargetType(TargetType.CAVE_BUILD);
	}

	public static TaskAction createTaskAction(int buildType, int level) {
		TaskActionOfCaveBuild action = new TaskActionOfCaveBuild();
		action.setNum(1);
		action.buildType = buildType;
		action.level = level;
		return action;
	}

	public int getBuildType() {
		return buildType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
