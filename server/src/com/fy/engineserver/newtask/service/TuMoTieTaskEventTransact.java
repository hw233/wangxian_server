package com.fy.engineserver.newtask.service;

import java.util.Arrays;
import java.util.Calendar;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.base.ExpAddManager;
import com.fy.engineserver.activity.expactivity.ExpActivity;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfExp;
import com.fy.engineserver.newtask.service.TaskConfig.PrizeType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 封魔录 任务
 * 
 * 
 */
public class TuMoTieTaskEventTransact extends AbsTaskEventTransact {

	public static String[] tumotieName = Translate.tumotieName;

	public void init() {
		ActivitySubSystem.logger.warn(Arrays.toString(tumotieName));
	}

	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch (action) {
		case deliver:
		case done:
			return tumotieName;
		default:
			break;
		}
		return null;
	}

	@Override
	public void handleAccept(Player player, Task task, Game game) {
	}

	@Override
	public void handleDone(Player player, Task task, Game game) {
		ActivitySubSystem.logger.warn(player.getLogString() + " [完成任务:" + task.getName() + "/" + task.getName_stat() + "]");
	}

	@Override
	public void handleDeliver(Player player, Task task, Game game) {
		ActivitySubSystem.logger.warn(player.getLogString() + " [交付任务:" + task.getName() + "/" + task.getName_stat() + "]");
		long exp = ((TaskPrizeOfExp) task.getPrizeByType(PrizeType.EXP).get(0)).getExp(player);
		int colorType = -1;
		if (task.getName().indexOf(Translate.text_3714) >= 0) {
			colorType = 0;
		} else if (task.getName().indexOf(Translate.text_3715) >= 0) {
			colorType = 1;
		} else if (task.getName().indexOf(Translate.text_37151) >= 0) {
			colorType = 2;
		} else if (task.getName().indexOf(Translate.text_3716) >= 0) {
			colorType = 3;
		} else if (task.getName().indexOf(Translate.text_3717) >= 0) {
			colorType = 4;
		}
		long newExp = ExpAddManager.instance.doAddExp(player, exp, 100000, colorType);
		if (newExp > exp) {
			player.addExp(newExp - exp, ExperienceManager.ADDEXP_REASON_TASK);
		}

		try {
			CompoundReturn cr = ActivityManager.getInstance().getExpRateFromTumotieActivity(Calendar.getInstance());
			if (cr.getBooleanValue()) {
				double expRateFromActivity = cr.getDoubleValue();
				long tumoActivityexp = ((long) (exp * (expRateFromActivity - 1)));// 封魔录经验跳2下，要扣除已经加了的经验
				ActivitySubSystem.logger.warn(player.getLogString() + " [完成任务:" + task.getName() + "/" + task.getName_stat() + "] [参与双倍活动] [额外增加经验:" + tumoActivityexp + "]");
				if (tumoActivityexp > 0) {
					player.addExp(tumoActivityexp, ExperienceManager.活动);
					ExpActivity activity = (ExpActivity) cr.getObjValue();
					if ("韩国0807".equals(activity.getName())) {
						player.send_HINT_REQ("도마첩 사용계절 이벤트에서 " + tumoActivityexp + "경험 보상을 획득한것을 축하합니다! ");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleDrop(Player player, Task task, Game game) {
	}

}
