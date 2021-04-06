package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class TaskPrizeOfBindSilver extends TaskPrize {

	private TaskPrizeOfBindSilver() {
		setPrizeType(PrizeType.BIND_SILVER);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int money) {
		TaskPrizeOfBindSilver bindSilver = new TaskPrizeOfBindSilver();
		bindSilver.setPrizeColor(new int[] { 0 });
		bindSilver.setPrizeNum(new long[] { money });
		bindSilver.setTotalNum(1);
		bindSilver.setPrizeId(new long[] { 0 });
		bindSilver.setPrizeName(new String[] { BillingCenter.得到带单位的银两(money) });
		return bindSilver;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		try {
			BillingCenter.getInstance().playerSaving(player, getPrizeNum()[0], CurrencyType.BIND_YINZI, SavingReasonType.TASK_PRIZE, "");
			if (TaskSubSystem.logger.isDebugEnabled()) {
				TaskSubSystem.logger.debug(player.getLogString() + "[得到任务奖励] [绑银] [成功] [{}]", new Object[] { getPrizeName()[0] });
			}
		} catch (SavingFailedException e) {
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[得到任务奖励] [绑银] [异常] [{}]", new Object[] { getPrizeName()[0], e });
			}
		}
	}
}
