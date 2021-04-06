package com.fy.engineserver.activity.dig;

import com.fy.engineserver.activity.dig.DigEvent;
import com.fy.engineserver.activity.dig.DigManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.sprite.Player;

public class DigEventOfExp extends DigEvent {

	/**角色等级*/
	private int playerLevel;
	/**经验*/
	private long exp;
	
	public DigEventOfExp(String useArticleName, String useArticleNameStat, int eventType) {
		super(useArticleName, useArticleNameStat, eventType);
	}

	public DigEventOfExp(String useArticleName, String useArticleNameStat, int playerLevel, long exp) {
		this(useArticleName, useArticleNameStat, DigManager.EVENT_EXP);
		this.playerLevel = playerLevel;
		this.exp = exp;
	}

	@Override
	public void execute(Player player, Game game) {
		player.addExp(exp, ExperienceManager.挖宝);
		player.sendError(Translate.translateString(Translate.挖宝获得经验提示, new String[][]{{Translate.STRING_1,String.valueOf(exp)}}));
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}


	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "DigEventOfExp [exp=" + exp + ", playerLevel=" + playerLevel + "]";
	}

}
