package com.fy.engineserver.newtask.prizes;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.newtask.ActivityTaskExp;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 任务奖励-经验
 * 
 * 
 */
public class TaskPrizeOfExp extends TaskPrize {

	/** 奖励经验的公式 */
	private int expPrizeType;

	private int prizeExpId;

	private TaskPrizeOfExp() {
		setPrizeType(PrizeType.EXP);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int expPrizeType, long num) {
		TaskPrizeOfExp prizeOfExp = new TaskPrizeOfExp();
		if (expPrizeType == 1) {
			prizeOfExp.setPrizeExpId((int) num);
			prizeOfExp.setPrizeNum(new long[] { -1 });
		} else {
			prizeOfExp.setPrizeNum(new long[] { num });
		}
		prizeOfExp.setExpPrizeType(expPrizeType);
		prizeOfExp.setPrizeName(new String[] { prizeOfExp.getPrizeType().getName() });
		return prizeOfExp;
	}

	public int getExpPrizeType() {
		return expPrizeType;
	}

	public void setExpPrizeType(int expPrizeType) {
		this.expPrizeType = expPrizeType;
	}

	public int getPrizeExpId() {
		return prizeExpId;
	}

	public void setPrizeExpId(int prizeExpId) {
		this.prizeExpId = prizeExpId;
	}

	@Override
	public void doPrize(Player player, int[] index, Task task) {
		// TODO 根据经验奖励
		long exp = getExp(player);
		// if (expPrizeType == 0) {
		// exp = getPrizeNum()[0];
		// } else if (expPrizeType == 1) {// 活动,根据配置表给经验
		// ActivityTaskExp activityTaskExp = TaskManager.getInstance().activityPrizeMap.get(prizeExpId);
		// if (activityTaskExp == null) {
		// TaskSubSystem.logger.error(player.getLogString() + "[完成任务] [经验奖励异常] [活动奖励不存在] [prizeId:" + prizeExpId + "]");
		// return;
		// }
		// if (player.getLevel() > activityTaskExp.getExpPrize().length) {
		// TaskSubSystem.logger.error(player.getLogString() + "[完成任务] [角色等级异常] [playerLevel:{}] [配置的等级上限:{}]", new Object[] { player.getLevel(),
		// activityTaskExp.getExpPrize().length });
		// return;
		// }
		// exp = activityTaskExp.getExpPrize()[player.getLevel() - 1];
		// }
		if (exp > 0) {
			player.addExp(exp, ExperienceManager.ADDEXP_REASON_TASK);
			if (task.getName_stat().matches("平定四方.*") || task.getName_stat().matches(".*级.*元神.*") || task.getName_stat().matches(".*级.*") || task.getName_stat().matches("神农.*") || task.getName_stat().matches(".*仙缘.*体.*") || task.getName_stat().matches(".*论道.*体.*")) {
				CountryManager.getInstance().addExtraExp(player, exp);
			}
			if (player.getActivePetId() > 0) {
				Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
				int petExp = 0;
				if (pet != null) {
					petExp = (int) (exp * 0.3);
					if (petExp > 0) {
						pet.addExp(petExp, PetExperienceManager.ADDEXP_REASON_TASK);
					}
				}
			}
			if (TaskSubSystem.logger.isWarnEnabled()) {
				TaskSubSystem.logger.warn(player.getLogString() + "[完成任务:{}] [获得经验:{}]", new Object[] { task.getName(), exp });
			}
		}
	}

	public long getExp(Player player) {
		long exp = 0;
		if (expPrizeType == 0) {
			exp = getPrizeNum()[0];
		} else if (expPrizeType == 1) {// 活动,根据配置表给经验
			ActivityTaskExp activityTaskExp = TaskManager.getInstance().activityPrizeMap.get(prizeExpId);
			if (activityTaskExp == null) {
				TaskSubSystem.logger.error(player.getLogString() + "[完成任务] [经验奖励异常] [活动奖励不存在] [prizeId:" + prizeExpId + "]");
				return 0;
			}
			if (player.getLevel() > activityTaskExp.getExpPrize().length) {
				TaskSubSystem.logger.error(player.getLogString() + "[完成任务] [角色等级异常] [playerLevel:{}] [配置的等级上限:{}]", new Object[] { player.getLevel(), activityTaskExp.getExpPrize().length });
				return 0;
			}
			exp = activityTaskExp.getExpPrize()[player.getLevel() - 1];
		}
		return exp;
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
		sbf.append("经验公式[").append(getExpPrizeType()).append("]经验值[").append(getPrizeNum()[0]).append("]");
		sbf.append("</tr>");
		sbf.append("</table>");
		return sbf.toString();
	}
}
