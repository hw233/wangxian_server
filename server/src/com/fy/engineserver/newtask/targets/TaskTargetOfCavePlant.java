package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCavePlant;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfCavePlant extends TaskTarget {

	private int targetType;

	public TaskTargetOfCavePlant(int plantType, int plantNum) {
		targetType = plantType;
		setTargetType(TargetType.CAVE_PLANT);
		setTargetByteType(getTargetType().getIndex());
		String name = Translate.text_cave_075;
		if (targetType >= 0 && targetType < Translate.text_cave_071.length) {
			name = Translate.text_cave_071[targetType];
		}
		setTargetName(new String[] { name });
		setTargetColor(-1);
		setTargetNum(plantNum);
		setMapName(new String[] { "" });
		setResMapName(new String[] { "" });
		setX(new int[1]);
		setY(new int[1]);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfCavePlant actionOfCavePlant = (TaskActionOfCavePlant) action;
			boolean plantTypeFit = targetType == -1 ? true : actionOfCavePlant.getPlantType() == targetType;
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug("[处理种植action][actionOfCavePlant.getPlantType():{}] [targetType:{}][{}] [数量:{}]", new Object[] { actionOfCavePlant.getPlantType(), targetType, plantTypeFit, action.getNum() });
			}
			if (plantTypeFit) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getTargetName()[0]).append(",").append(getTargetNum()).append("次");
		return sbf.toString();
	}
}
