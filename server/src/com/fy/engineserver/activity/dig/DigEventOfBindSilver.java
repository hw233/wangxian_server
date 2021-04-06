package com.fy.engineserver.activity.dig;

import com.fy.engineserver.activity.dig.DigEvent;
import com.fy.engineserver.activity.dig.DigManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingFailedException;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class DigEventOfBindSilver extends DigEvent {
	/**角色等级*/
	private int playerLevel;
	/**绑银*/
	private long bindSilver;
	
	public DigEventOfBindSilver(String useArticleName, String useArticleNameStat, int eventType) {
		super(useArticleName, useArticleNameStat, eventType);
	}

	public DigEventOfBindSilver(String useArticleName, String useArticleNameStat, int playerLevel, long bindSilver) {
		this(useArticleName, useArticleNameStat, DigManager.EVENT_BINDSILVER);
		this.playerLevel = playerLevel;
		this.bindSilver = bindSilver;
	}

	@Override
	public void execute(Player player, Game game) {
		// TODO Auto-generated method stub
		try {
			BillingCenter.getInstance().playerSaving(player, bindSilver, CurrencyType.BIND_YINZI, SavingReasonType.挖宝, "挖宝");
			player.sendError(Translate.translateString(Translate.挖宝获得银子提示, new String[][]{{Translate.STRING_1,BillingCenter.得到带单位的银两(bindSilver)}}));
		} catch (SavingFailedException e) {
			TaskSubSystem.logger.warn(player.getLogString()+e);
			e.printStackTrace();
		}
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public long getBindSilver() {
		return bindSilver;
	}

	public void setBindSilver(long bindSilver) {
		this.bindSilver = bindSilver;
	}

	@Override
	public String toString() {
		return "DigEventOfBindSilver [bindSilver=" + bindSilver + ", playerLevel=" + playerLevel + "]";
	}

}
