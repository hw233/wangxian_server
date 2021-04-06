package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励--历练
 * 
 * 
 */
public class TaskPrizeOfLilian extends TaskPrize {

	private TaskPrizeOfLilian() {
		setPrizeType(PrizeType.LILIAN);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int num) {
		TaskPrizeOfLilian lilian = new TaskPrizeOfLilian();
		lilian.setPrizeNum(new long[] { num });
		lilian.setPrizeName(new String[] { PrizeType.LILIAN.getName() });
		return lilian;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		synchronized (player) {
			try {
				BillingCenter.getInstance().playerSaving(player, getPrizeNum()[0], CurrencyType.LILIAN, SavingReasonType.TASK_PRIZE_LILIAN, task.getName());
				if (TaskSubSystem.logger.isWarnEnabled()) {
					TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [增加历练:{}] [增加后历练:{}]", new Object[] { task.getName(), getPrizeNum()[0], player.getLilian() });
				}
			} catch (SavingFailedException e) {
				TaskSubSystem.logger.error(player.getLogString() + "[完成任务:{" + task.getName() + "}] [增加历练:{" + getPrizeNum()[0] + "}] [异常]", e);
			}
		}
	}
}
