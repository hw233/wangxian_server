package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfMonsterLvNearPlayer;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfMonsterLvNearPlayer extends TaskTarget implements TaskConfig {

	private int minLimit;
	private int maxLimit;

	/**
	 * @param minLimit
	 * @param maxLimit
	 */
	public TaskTargetOfMonsterLvNearPlayer(int minLimit, int maxLimit, int num) {
		setTargetType(TargetType.MONSTER_LV_PLAYER);
		setTargetByteType(getTargetType().getIndex());
		setMinLimit(minLimit);
		setMaxLimit(maxLimit);
		setTargetNum(num);

		initNotic();
	}

	@Override
	public void initNotic() {
		setNoticeName(Translate.text_task_031);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			if (colorFit(action)) {
				TaskActionOfMonsterLvNearPlayer lvNearPlayer = (TaskActionOfMonsterLvNearPlayer) action;
				Player self = lvNearPlayer.getSelf();
				if (gradeFit(minLimit, maxLimit, self.getSoulLevel(), lvNearPlayer.getGrade())) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
				}
			}

		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	// 仅此处用
	private boolean gradeFit(int minLimit, int maxLimit, int soulLevel, int monsterGrade) {
		int gradeDistance = monsterGrade - soulLevel;
		return gradeDistance >= minLimit && gradeDistance <= maxLimit;
	}

	public int getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(int minLimit) {
		this.minLimit = minLimit;
	}

	public int getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		sbf.append("杀怪数量[").append(getTargetNum()).append("]杀怪等级段与角色相比[").append(getMinLimit()).append("~").append(getMaxLimit()).append("]");
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfMonsterLvNearPlayer lvNearPlayer = new TaskTargetOfMonsterLvNearPlayer(-2, 11, 11);
//		System.out.println(lvNearPlayer.toHtmlString("CSS"));
	}
}
