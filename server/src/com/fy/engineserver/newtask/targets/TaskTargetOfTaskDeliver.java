package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfTaskDeliver extends TaskTarget implements TaskConfig {

	/**
	 * 
	 * @param targetName目标名
	 */
	public TaskTargetOfTaskDeliver(String[] targetName) {
		setTargetType(TargetType.TASK_DELIVER);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		boolean isFit = false;
		if (isSameType(action)) {
			for (int i = 0; i < getTargetName().length; i++) {
				isFit = getTargetName()[i].equals(action.getName());
				if (isFit) {
					break;
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(isFit).setIntValue(isFit ? 1 : 0);
	}


	@Override
	public CompoundReturn dealOnGet(Player player,Task task) {
		for (int i = 0; i < getTargetName().length; i++) {
			int status = player.getCurrTaskStatus(TaskManager.getInstance().getTask(getTargetName()[i],player.getCountry()));
			if (status == TASK_STATUS_DEILVER) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(1);
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(0);
	}
	
	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");

		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append("[").append(getTargetName()[i]).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}

		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfTaskDeliver deliver = new TaskTargetOfTaskDeliver(new String[] { "T1", "T2" });
//		System.out.println(deliver.toHtmlString("AA"));
	}
}
