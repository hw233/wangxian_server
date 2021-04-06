package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励-称号
 * 
 * 
 */
public class TaskPrizeOfTitle extends TaskPrize implements TaskConfig {

	private String titleName;

	private TaskPrizeOfTitle() {
		setPrizeType(PrizeType.TITLE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(String titleName) {
		TaskPrizeOfTitle prize = new TaskPrizeOfTitle();
		prize.setTitleName(titleName);
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		PlayerTitlesManager.getInstance().addTitle(player, titleName, false);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得称号:{}]", new Object[] { task.getName(), titleName });
		}
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
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
		sbf.append("称号[").append(getTitleName()).append("]");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize prize = createTaskPrize("OP");
		// System.out.println(prize.toHtmlString("GG"));
	}

}
