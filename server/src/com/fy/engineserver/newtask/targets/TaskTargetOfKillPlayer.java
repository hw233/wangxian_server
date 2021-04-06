package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.actions.TaskActionOfKillPlayer;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskTargetOfKillPlayer extends TaskTarget implements TaskConfig {

	// 比当前角色低X级别
	private int minLv;
	// 比当前角色高级别
	private int maxLv;
	private int limit;

	public TaskTargetOfKillPlayer(int minLv, int maxLv, int limit ,int num) {
		setTargetType(TargetType.KILL_PLAYER);
		setTargetByteType(getTargetType().getIndex());
		setMinLv(minLv);
		setMaxLv(maxLv);
		setLimit(limit);
		setTargetNum(num);
	}

	@Override
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			TaskActionOfKillPlayer killPlayer = (TaskActionOfKillPlayer) action;
			Player self = killPlayer.getSelf();
			Player target = killPlayer.getTarget();
			int gradeDistance = target.getLevel() - self.getLevel();

			if (gradeDistance >= getMinLv() && gradeDistance <= getMaxLv()) {
				if (limit == KILL_PLAYER_COUNTRY_LIMIT_NON) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
				} else if (limit == KILL_PLAYER_COUNTRY_LIMIT_OTHER) {
					return CompoundReturn.createCompoundReturn().setBooleanValue(self.getCountry() != target.getCountry()).setIntValue(action.getNum());
				}
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public int getMinLv() {
		return minLv;
	}

	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}

	public int getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr><td>");
		sbf.append(getTargetType().getName()).append("</td></tr><tr>");
		sbf.append("<td>");
		sbf.append(limit == KILL_PLAYER_COUNTRY_LIMIT_NON ? "不限国家" : "其他国家").append("</td><td> 比角色等级低[").append(getMinLv()).append("]以上,比角色等级高[").append(getMaxLv()).append("]以下</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskTargetOfKillPlayer killPlayer = new TaskTargetOfKillPlayer(-4, 22, -1,11);
//		System.out.println(killPlayer.toHtmlString("d"));
	}
}
