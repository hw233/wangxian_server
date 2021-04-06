package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.Position;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.worldmap.WorldMapManager;

public class TaskTargetOfTalkToNPC extends TaskTarget implements TaskConfig {
	/**
	 * 
	 * @param targetName
	 */
	public TaskTargetOfTalkToNPC(String[] targetMapName, String[] targetName,int country) {
		setTargetType(TargetType.TALK_NPC);
		setTargetByteType(getTargetType().getIndex());
		setTargetName(targetName);
		setMapName(targetMapName);
		setTargetNum(1);
		String[] resMapName = new String[targetMapName.length];
		for (int i = 0; i < resMapName.length; i++) {
			resMapName[i] = "";
		}
		int[] x = new int[targetMapName.length];
		int[] y = new int[targetMapName.length];
		if (targetName != null) {
			for (int i = 0; i < getTargetName().length; i++) {
				CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndName(getMapName()[i], getTargetName()[i]);
				if (cr.getBooleanValue()) {
					x[i] = cr.getIntValues()[0];
					y[i] = cr.getIntValues()[1];
					resMapName[i] = GameManager.getInstance().getResName(getMapName()[i], CountryManager.国家A);
					if (resMapName[i] == null) {
						resMapName[i] = GameManager.getInstance().getResName(getMapName()[i], CountryManager.中立);
					}
				} else {
					TaskManager.notices.append("[与NPC对话][通过地图名和npc名字未找到记录][ID:").append(TaskManager.currentLoadId).append("][地图:").append(getMapName()[i]).append("][NPC:").append(getTargetName()[i]).append("]<BR/>");
				}
			}
		}
		setX(x);
		setY(y);
		setResMapName(resMapName);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			if (inTargetNames(action.getName())) {
				return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}
	@Override
	public String gettarDes() {
		StringBuffer sb = new StringBuffer();
		if (getTargetName() != null && getTargetName().length > 0) {
			sb.append(getTargetName()[0]);
		}
		sb.append("(0/1)");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuffer sbf = new StringBuffer(getTargetType().getName());
		sbf.append("\n--------\n");
		sbf.append("NPC对话目标\n");
		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append("NPC[").append(getTargetName()[i]).append("]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}
		return sbf.toString();
	}

	@Override
	public CompoundReturn dealOnGet(Player player, Task task) {
		boolean deliverNpcIsTarget = false;
		for (int i = 0; i < getTargetName().length; i++) {
			String name = getTargetName()[i];
			if (name.equals(task.getEndNpc())) {
				if (task.getEndMap().equals(getMapName()[i])) {
					deliverNpcIsTarget = true;
					break;
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(deliverNpcIsTarget).setIntValue(1);
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("/").append(getTargetByteType()).append("</td></tr><tr>");
		sbf.append("<td>");

		for (int i = 0; i < getTargetName().length; i++) {
			sbf.append("与NPC聊天[").append(getTargetName()[i]).append("[").append(getX()[i]).append(",").append(getY()[i]).append("]]").append(i < getTargetName().length - 1 ? "【或者】" : "");
		}

		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}
}
