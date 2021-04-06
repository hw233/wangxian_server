package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class TaskPrizeOfGongxun extends TaskPrize {

	private TaskPrizeOfGongxun() {
		setPrizeType(PrizeType.GONGXUN);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int num) {
		TaskPrizeOfGongxun gongxun = new TaskPrizeOfGongxun();
		long[] nums = new long[1];
		nums[0] = num;
		gongxun.setPrizeNum(nums);
		return gongxun;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		player.setGongxun(player.getGongxun() + getPrizeNum()[0]);
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得功勋:{}] [增加后功勋值:{}]", new Object[] { task.getName(), getPrizeNum()[0], player.getGongxun() });
		}
	}
}
