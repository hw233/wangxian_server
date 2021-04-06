package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCaveBuild;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 任务目标:建造庄园建筑
 * 
 * 
 */
public class TaskTargetOfCaveBuild extends TaskTarget implements FaeryConfig {

	private int buildType;

	private int level;

	public TaskTargetOfCaveBuild(int buildType, int level) {
		setTargetType(TargetType.CAVE_BUILD);
		setTargetByteType(getTargetType().getIndex());
		setTargetNum(1);
		setLevel(level);
		setBuildType(buildType);
		setTargetName(new String[] { CAVE_BUILDING_NAMES[buildType] });
		setMapName(new String[] { "" });
		setResMapName(new String[] { "" });
		setX(new int[1]);
		setY(new int[1]);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfCaveBuild taskActionOfCaveBuild = (TaskActionOfCaveBuild) action;
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug("[action:{}] [建筑类型:{}] [建筑等级:{}] [目标类型:{}] [目标等级:{}]", new Object[] { taskActionOfCaveBuild.getClass(), taskActionOfCaveBuild.getBuildType(), taskActionOfCaveBuild.getLevel(), getBuildType(), getLevel() });
			}
			if (getBuildType() == taskActionOfCaveBuild.getBuildType()) {
				if (taskActionOfCaveBuild.getLevel() >= level) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	@Override
	public CompoundReturn dealOnGet(Player player, Task task) {
		if (player.getCaveId() <= 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		Cave cave = FaeryManager.getInstance().getCave(player);
		if (cave != null) {
			if (cave.getCaveBuilding(buildType).getGrade() >= level) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getTargetName()[0]).append(",").append(level).append(Translate.级);
		return sbf.toString();
	}
}
