package com.fy.engineserver.chuangong;

import java.util.List;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.masterAndPrentice.MasterPrenticeManager;
import com.fy.engineserver.sprite.Player;

public class Chuangong {

	private long id;
	private Player active;
	private Player passive;

	public Chuangong(long id, Player active, Player passive) {
		this.id = id;
		this.active = active;
		this.passive = passive;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Player getActive() {
		return active;
	}

	public void setActive(Player active) {
		this.active = active;
	}

	public Player getPassive() {
		return passive;
	}

	public void setPassive(Player passive) {
		this.passive = passive;
	}

	public void reward(Player player) {
		String is = MasterPrenticeManager.getInstance().checkMasterPrentice(active, passive);

		if (is.equals("")) {

			if (player == active) {

				List<Integer> list = ChuanGongManager.getInstance().getEnergyList();
				if (list != null && list.size() + 79 >= player.getLevel()) {

					int energy = (int) (list.get(player.getLevel() - 80) * ChuanGongManager.非师徒关系);
					{
						//元气活动
						double rate = ActivityManager.getInstance().getYuanqiRateFormActivity();
						energy *= rate;
					}
					active.setEnergy(active.getEnergy() + energy);
					// active.send_HINT_REQ("增加元气"+energy);
					active.send_HINT_REQ(Translate.translateString(Translate.增加元气xx, new String[][] { { Translate.STRING_1, energy + "" } }));
					if (ChuanGongManager.logger.isWarnEnabled()) {
						ChuanGongManager.logger.warn("[师徒传功奖励元气成功] [" + player.getLogString() + "] [增加元气:" + energy + "]");
					}
				} else {
					ChuanGongManager.logger.error("[师徒传功奖励元气错误] [人物级别错误] [" + player.getLogString() + "] [" + player.getLevel() + "] [list长度,要是等于0为null" + (list != null ? list.size() : 0) + "]");
				}

			} else {

				List<Integer> list = ChuanGongManager.getInstance().getExperienceList();
				if (list != null && list.size() <= 79) {

					int experience = (int) (list.get(player.getLevel() - 1) * ChuanGongManager.非师徒关系);
					passive.addExp(experience, ExperienceManager.ADDEXP_REASON_CHUANGONG);
					if (ChuanGongManager.logger.isWarnEnabled()) {
						ChuanGongManager.logger.warn("[师徒传功奖励经验成功] [" + player.getLogString() + "] [" + experience + "]");
					}
				} else {
					ChuanGongManager.logger.error("[师徒传功奖励经验错误] [人物级别错误] [" + passive.getLogString() + "] [" + player.getLevel() + "] [list长度,要是等于0为null" + (list != null ? list.size() : 0) + "]");
				}
			}

		} else {

			if (player == active) {

				List<Integer> list = ChuanGongManager.getInstance().getEnergyList();
				if (list != null && list.size() + 79 >= player.getLevel()) {

					int energy = list.get(player.getLevel() - 80);
					double rate = ActivityManager.getInstance().getYuanqiRateFormActivity();
					energy *= rate;
					active.setEnergy(active.getEnergy() + energy);
					// active.send_HINT_REQ("增加元气"+energy);
					active.send_HINT_REQ(Translate.translateString(Translate.增加元气xx, new String[][] { { Translate.STRING_1, energy + "" } }));
					if (ChuanGongManager.logger.isWarnEnabled()) {
						ChuanGongManager.logger.warn("[普通传功奖励元气成功] [" + player.getLogString() + "] [" + energy + "]");
					}
				} else {
					ChuanGongManager.logger.error("[普通传功奖励元气错误] [人物级别错误] [" + player.getLogString() + "] [" + player.getLevel() + "] [list长度,要是等于0为null" + (list != null ? list.size() : 0) + "]");
				}

			} else {

				List<Integer> list = ChuanGongManager.getInstance().getExperienceList();
				if (list != null && list.size() <= 79) {
					int experience = list.get(player.getLevel() - 1);
					passive.addExp(experience, ExperienceManager.ADDEXP_REASON_CHUANGONG);
					if (ChuanGongManager.logger.isWarnEnabled()) {
						ChuanGongManager.logger.warn("[普通传功奖励经验成功] [" + player.getLogString() + "] [" + experience + "]");
					}
				} else {
					ChuanGongManager.logger.error("[普通传功奖励经验错误] [人物级别错误] [" + passive.getLogString() + "] [" + player.getLevel() + "] [list长度,要是等于0为null" + (list != null ? list.size() : 0) + "]");
				}
			}
		}
	}

}
