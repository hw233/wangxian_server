package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfMonsterLv extends TaskTarget implements TaskConfig {

	/** 最低等级 -1表示没限制 */
	private int[] minLv;
	/** 最高等级 -1表示没限制 */
	private int[] maxLv;

	/**
	 * 
	 * @param targetColor
	 * @param targetNum
	 * @param minLv
	 * @param maxLv
	 */
	public TaskTargetOfMonsterLv(int targetColor, int targetNum, int[] minLv, int[] maxLv) {
		setTargetType(TargetType.MONSTER_LV);
		setTargetByteType(getTargetType().getIndex());
		setTargetColor(targetColor);
		setTargetNum(targetNum);

		setMinLv(minLv);
		setMaxLv(maxLv);
		initNotic();
	}

	@Override
	public void initNotic() {
		setNoticeName(Translate.text_task_031);
	}

	public int[] getMinLv() {
		return minLv;
	}

	public void setMinLv(int[] minLv) {
		this.minLv = minLv;
	}

	public int[] getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(int[] maxLv) {
		this.maxLv = maxLv;
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			// if (colorFit(action)) {
			if (gradeFit(action)) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
			}
			// }
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	/**
	 * 等级是否匹配
	 * @param action
	 * @return
	 */
	private boolean gradeFit(TaskAction action) {
		int actionLv = action.getGrade();
		for (int i = 0; i < minLv.length; i++) {
			if (TaskManager.gradeFit(minLv[i], maxLv[i], actionLv)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n--------\n");
		sbf.append("杀怪颜色[").append(getTargetColor()).append("]杀怪数量[").append(getTargetNum()).append("]杀怪等级段");
		for (int i = 0; i < getMinLv().length; i++) {
			sbf.append("[").append(getMinLv()[i]).append("~").append(getMaxLv()[i]).append("]").append(i < getMinLv().length - 1 ? "【或者】" : "");
		}
		return sbf.toString();
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		sbf.append("杀怪颜色[").append(getTargetColor()).append("]杀怪数量[").append(getTargetNum()).append("]杀怪等级段");
		for (int i = 0; i < getMinLv().length; i++) {
			sbf.append("[").append(getMinLv()[i]).append("~").append(getMaxLv()[i]).append("]").append(i < getMinLv().length - 1 ? "【或者】" : "");
		}
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfMonsterLv lv = new TaskTargetOfMonsterLv(1, 22, new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
		// System.out.println(lv.toHtmlString(""));
	}
}
