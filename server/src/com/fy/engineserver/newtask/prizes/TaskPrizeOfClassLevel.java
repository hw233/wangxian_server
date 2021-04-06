package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励-境界
 * 
 * 
 */
public class TaskPrizeOfClassLevel extends TaskPrize {

	public TaskPrizeOfClassLevel() {
		setPrizeType(PrizeType.CLASSLEVEL);
		setPrizeByteType(getPrizeType().getIndex());
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		player.addBournExp((int) getPrizeNum()[0]);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得境界经验:{}] [增加后:{}]", new Object[] { task.getName(), getPrizeNum()[0], player.getBournExp() });
		}
	}

	public static TaskPrize createTaskPrize(int prizeNum) {
		TaskPrize prize = new TaskPrizeOfClassLevel();
		prize.setPrizeNum(new long[] { prizeNum });
		prize.setPrizeName(new String[] { prize.getPrizeType().getName() });
		return prize;
	}
}
