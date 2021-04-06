package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

/**
 * 任务奖励--历练
 * 
 * 
 */
public class TaskPrizeOfCountryRes extends TaskPrize {

	private TaskPrizeOfCountryRes() {
		setPrizeType(PrizeType.COUNTRY_RES);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int num) {
		TaskPrizeOfCountryRes res = new TaskPrizeOfCountryRes();
		long[] nums = new long[1];
		nums[0] = num;
		res.setPrizeNum(nums);
		res.setPrizeName(new String[] { "国家资源" });
		return res;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		// TODO 奖励
		Country country = CountryManager.getInstance().getCountryByCountryId(player.getCountry());
		if (country != null) {
			synchronized (country) {
				country.setGuozhanResource(country.getGuozhanResource() + getPrizeNum()[0]);
				if (TaskSubSystem.logger.isWarnEnabled()) {
					TaskSubSystem.logger.warn(player.getLogString() + "[完成任务] [增加国家资源:" + getPrizeNum()[0] + "] [增加后国家资源:" + country.getGuozhanResource() + "]");
				}
			}
		}
	}
}
