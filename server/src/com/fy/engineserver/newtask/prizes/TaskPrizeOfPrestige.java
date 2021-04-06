package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励-声望
 * 
 * 
 */
public class TaskPrizeOfPrestige extends TaskPrize implements TaskConfig {

	private int prestigeType;

	private TaskPrizeOfPrestige() {
		setPrizeType(PrizeType.PRESTIGE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int prestigeType, int prestigeNum) {
		TaskPrizeOfPrestige prize = new TaskPrizeOfPrestige();
		prize.setPrizeNum(new long[] { prestigeNum });
		prize.setPrestigeType(prestigeType);
		prize.setPrizeName(new String[] { prize.getPrizeType().getName() });
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		super.doPrize(player, index, task);
	}

	public int getPrestigeType() {
		return prestigeType;
	}

	public void setPrestigeType(int prestigeType) {
		this.prestigeType = prestigeType;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr>");
		sbf.append("<td>");
		sbf.append(getPrizeType().getName());
		sbf.append("</td>");
		sbf.append("<td>");
		;
		sbf.append("声望类型[").append(getPrestigeType()).append("]声望值[").append(getPrizeNum()[0]).append("]");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}
}