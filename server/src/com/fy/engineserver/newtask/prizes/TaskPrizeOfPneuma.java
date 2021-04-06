package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励-修法值
 * 
 * 
 */
public class TaskPrizeOfPneuma extends TaskPrize implements TaskConfig {

	private TaskPrizeOfPneuma() {
		setPrizeType(PrizeType.PNEUMA);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int pneumaNum) {
		TaskPrizeOfPneuma prize = new TaskPrizeOfPneuma();
		prize.setPrizeNum(new long[] { pneumaNum });
		prize.setPrizeName(new String[] { prize.getPrizeType().getName() });
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		// TODO 上限判断
		player.setEnergy(player.getEnergy() + (int) getPrizeNum()[0]);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得修法值:{}] [增加后修法值:{}]", new Object[] { task.getName(), getPrizeNum()[0], player.getEnergy() });
		}
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
		sbf.append("修法值数量[").append(getPrizeNum()[0]).append("]");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize prize = createTaskPrize(333);
		// System.out.println(prize.toHtmlString("GG"));
	}
}
