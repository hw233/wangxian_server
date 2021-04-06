package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfCaveSteal;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfCaveSteal extends TaskTarget {

	private int fruitType;

	/**
	 * @param fruitType
	 * @param num
	 */
	public TaskTargetOfCaveSteal(int fruitType, int num) {
		setTargetType(TargetType.CAVE_STEAL);
		setTargetByteType(getTargetType().getIndex());
		String name = Translate.text_cave_075;
		if (fruitType >= 0 && fruitType < Translate.text_cave_071.length) {
			name = Translate.text_cave_071[fruitType];
		}
		setTargetName(new String[] { name });
		setTargetColor(-1);
		setTargetNum(num);
		setFruitType(fruitType);
		setMapName(new String[] { "" });
		setResMapName(new String[] { "" });
		setX(new int[1]);
		setY(new int[1]);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfCaveSteal actionOfCavePlant = (TaskActionOfCaveSteal) action;
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug("[检查目标-果实偷取] [actionFruitType:{}] [targetFruitType:{}]", new Object[] { actionOfCavePlant.getFruitType(), getFruitType() });
			}
			boolean plantTypeFit = fruitType == -1 ? true : actionOfCavePlant.getFruitType() == fruitType;
			if (plantTypeFit) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public int getFruitType() {
		return fruitType;
	}

	public void setFruitType(int fruitType) {
		this.fruitType = fruitType;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer();
		sbf.append(getTargetName()[0]).append(",").append(getTargetNum()).append("次");
		return sbf.toString();
	}
}
