package com.fy.engineserver.menu.activity.chunjie;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_Addbuff extends Option {

	private String buffName;
	private int minLevel;
	private int maxLevel;
	private long delayTime;
	private int buffLevel;

	@Override
	public void doSelect(Game game, Player player) {
		if (player.getLevel() < minLevel || player.getLevel() > maxLevel) {
			player.sendError(Translate.你的等级不符合活动要求);
			return;
		}

		Buff oldBuff = player.getBuffByName(buffName);

		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate buffTemplate = btm.getBuffTemplateByName(buffName);
		if (buffTemplate == null) {
			if (ActivitySubSystem.logger.isWarnEnabled()) {
				ActivitySubSystem.logger.warn("[领取buff活动] [" + player.getLogString() + "] [buff不存在:" + buffName + "]");
			}
			return;
		}
		Buff buff = buffTemplate.createBuff(buffLevel);
		buff.setStartTime(SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + delayTime);
		// buff.setInvalidTime(buff.getStartTime() + 1000L* 60);
		buff.setCauser(player);
		player.placeBuff(buff);
		if (ActivitySubSystem.logger.isWarnEnabled()) {
			ActivitySubSystem.logger.warn("[领取buff活动] [" + player.getLogString() + "] [buff:" + buffName + "] [是否有老buff:" + (oldBuff == null) + "]");
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public int getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int buffLevel) {
		this.buffLevel = buffLevel;
	}
}