package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励 - 贡献度
 * 
 * 
 */
public class TaskPrizeOfContribute extends TaskPrize implements TaskConfig {

	private int contributeType;

	private TaskPrizeOfContribute() {
		setPrizeType(PrizeType.CONTRIBUTE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int contributeType, int contributeNum) {
		TaskPrizeOfContribute prize = new TaskPrizeOfContribute();
		prize.setPrizeNum(new long[] { contributeNum });
		prize.setContributeType(contributeType);
		prize.setPrizeName(new String[] { prize.getPrizeType().getName() });
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		super.doPrize(player, index, task);
	}

	public int getContributeType() {
		return contributeType;
	}

	public void setContributeType(int contributeType) {
		this.contributeType = contributeType;
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
		sbf.append("奖励贡献度类型");
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(getContributeType());
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append("奖励贡献度数量");
		sbf.append("</td>");
		sbf.append("<td>");
		sbf.append(getPrizeNum()[0]);
		sbf.append("</td>");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize prize = createTaskPrize(1, 2);
		// System.out.println(prize.toHtmlString("GG"));
	}
}
