package com.fy.engineserver.articleEnchant;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;

public class AddBuffTimerTask implements Callbackable {
	private Player player;
	
	public AddBuffTimerTask(Player player) {
		this.player = player;
	}

	@Override
	public void callback() {
		// TODO Auto-generated method stub
		EnchantManager.fireBuff(player, Translate.免疫控制, 0, Translate.免疫控制描述);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
