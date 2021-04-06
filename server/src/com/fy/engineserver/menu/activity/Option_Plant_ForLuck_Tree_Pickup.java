package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;

/**
 * 摘取祝福果
 * 
 * 
 */
public class Option_Plant_ForLuck_Tree_Pickup extends Option_ForLuck implements NeedCheckPurview,
		Cloneable {

	@Override
	public void doSelect(Game game, Player player) {
		try {

			if (ForLuckFruitManager.logger.isInfoEnabled()) {
				ForLuckFruitManager.logger.info(player.getLogString() + "[摘取祝福果]");
			}
			if (getNpc() == null || !getNpc().isInService()) {
				player.sendError(Translate.text_forluck_019);
				return;
			}
			ForLuckFruitNPC npc = getNpc();
			npc.onBeCollection(player);
		} catch (Exception e) {
			ForLuckFruitManager.logger.error("摘取祝福果异常", e);
		}
	}

	@Override
	public boolean canSee(Player player) {
		if (getNpc() == null || !getNpc().isInService()) {
			return false;
		}
		return player.inSelfSeptStation() && getNpc().canCollection(player);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
