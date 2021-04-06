package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class TaskPrizeOfSkillPoint extends TaskPrize implements TaskConfig {

	private TaskPrizeOfSkillPoint() {
		setPrizeType(PrizeType.SKILL_POINT);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int pointNum) {
		TaskPrizeOfSkillPoint prize = new TaskPrizeOfSkillPoint();
		prize.setPrizeNum(new long[] { pointNum });
		prize.setPrizeName(new String[] { prize.getPrizeType().getName() });
		return prize;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		player.setUnallocatedSkillPoint(player.getUnallocatedSkillPoint() + (int) getPrizeNum()[0]);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得技能点:{}] [增加后:{}]", new Object[] { task.getName(), getPrizeNum()[0], player.getUnallocatedSkillPoint() });
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
		sbf.append("技能点[").append(getPrizeNum()[0]).append("]");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		TaskPrize prize = createTaskPrize(222);
		// System.out.println(prize.toHtmlString("GG"));
	}

}
