package com.fy.engineserver.activity.fireActivity;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.Utils;

public class CommonFireExpTemplate {

	private int level;
	private long exp1;
	private long exp2;
	private long exp3;
	private long exp4;

	public CommonFireExpTemplate(int level, long exp1, long exp2, long exp3, long exp4) {
		super();
		this.level = level;
		this.exp1 = exp1;
		this.exp2 = exp2;
		this.exp3 = exp3;
		this.exp4 = exp4;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExp1() {
		return exp1;
	}

	public void setExp1(long exp1) {
		this.exp1 = exp1;
	}

	public long getExp2() {
		return exp2;
	}

	public void setExp2(long exp2) {
		this.exp2 = exp2;
	}

	public long getExp3() {
		return exp3;
	}

	public void setExp3(long exp3) {
		this.exp3 = exp3;
	}

	public long getExp4() {
		return exp4;
	}

	public void setExp4(long exp4) {
		this.exp4 = exp4;
	}

	public void addExp(Buff buff, Player player, int n) {

		// 酒buff level 从0开始

		int level = buff.getLevel() + 1;
		int type = (int) Math.ceil(1.0 * level / 5);
		int beerLevel = level % 5;
		int multiple = CommonFireEntity.beerQualityAffect[beerLevel];

		long sourceExp = -1;
		if (type == 1) {
			sourceExp = exp1;
		} else if (type == 2) {
			sourceExp = exp2;
		} else if (type == 3) {
			sourceExp = exp3;
		} else {
			sourceExp = exp4;
		} 

		if (sourceExp < 0) {
			FireManager.logger.error("[普通篝火活动加经验错误] [buff级别错误] [" + player.getLogString() + "] [级别:" + level + "]");
			return;
		}
		
		long exp = (long) ((1l * sourceExp * (100 + CommonFireEntity.add[n]) * multiple) / 100);
		if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
			exp /= 2;
		}
		if (exp > 0) {
			player.addExp(exp, ExperienceManager.ADDEXP_REASON_FIREACTIVITY);
		}
		if (FireManager.logger.isWarnEnabled()) {
			FireManager.logger.warn("[普通篝火活动加经验成功] [" + player.getLogString() + "] [经验:" + exp + "] [级别:" + level + "] [酒的类型:" + type + "] [酒的级别:" + beerLevel + "] [multiple:" + multiple + "] [人数:" + n + "] [sourceExp:" + sourceExp + "] [CommonFireEntity.add[n]:" + CommonFireEntity.add[n] + "]");
		}
	}

	public void addExpJiazu(int addWoodlevel, Buff buff, Player player, int n, boolean bln) {
		try {
			int level = buff.getLevel() + 1;
			int type = (int) Math.ceil(1.0 * level / 5);
			int beerLevel = level % 5;
			int multiple = CommonFireEntity.beerQualityAffect[beerLevel];

			long sourceExp = -1;
			if (type == 1) {
				sourceExp = exp1;
			} else if (type == 2) {
				sourceExp = exp2;
			} else if (type == 3) {
				sourceExp = exp3;
			} else {
				sourceExp = exp4;
			}

			if (sourceExp < 0) {
				FireManager.logger.error("[家族篝火活动加经验错误] [buff级别错误] [" + player.getLogString() + "] [级别:" + level + "] [addwood:" + addWoodlevel + "]");
				return;
			}
			int playerNumAdd = 0;
			if (n >= 10) {
				playerNumAdd = 20;
			} else {
				playerNumAdd = 2 * n;
			}

			long exp = (long) ((1l * sourceExp * (100 + CommonFireEntity.addWood[addWoodlevel] + playerNumAdd) * multiple) / 100);
			boolean specialDay = false;
			try {
				for (int[] ints : FireManager.指定日期) {
					specialDay = Utils.isSpecialSameDay(ints[0], ints[1], ints[2]);
					if (specialDay) {
						exp = exp + (long) (0.5 * sourceExp * multiple);
						break;
					}
				}
			} catch (Exception e) {
				FireManager.logger.error("[家族篝火活动加双倍经验异常] [" + player.getLogString() + "]", e);
			}

			if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				exp /= 2;
			}
			if (player.getExtraHejiu() > 0) {
				exp = (long) ((1+player.getExtraHejiu()/1000d) * exp);
			}

			if(exp > 0){
				player.addExp(exp, ExperienceManager.ADDEXP_REASON_FIREACTIVITY);
			}
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.家族喝酒次数, 1L});
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计购买周月礼包次数异常] [" + player.getLogString() + "]", eex);
			}
			if (FireManager.logger.isWarnEnabled()) {
				FireManager.logger.warn("[家族篝火活动加经验成功] [指定一天:" + specialDay + "] [" + player.getLogString() + "] [经验:" + exp + "] [级别:" + level + "] [酒的类型:" + type + "] [酒的级别:" + beerLevel + "] [加柴级别:" + addWoodlevel + "] [source:" + sourceExp + "] [multiple:" + multiple + "]");
			}
		} catch (Exception e) {
			FireManager.logger.error("[家族篝火活动加经验异常] [" + player.getLogString() + "]", e);
		}
	}

}
