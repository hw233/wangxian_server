package com.fy.engineserver.articleEnchant;

import com.fy.engineserver.message.ENCHANT_ALL_EQUIPT_RES;
import com.fy.engineserver.message.ENCHANT_LOCK_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Callbackable;
import com.fy.engineserver.sprite.Player;

public class EnchantTimerTask implements Callbackable{
	
	private Player player;
	
	private int index;

	private long articleId;
	
	private int enchantType;
	
	public EnchantTimerTask(Player player, int index, long articleId, int enchantType) {
		this.player = player;
		this.index = index;
		this.articleId = articleId;
		this.enchantType = enchantType;
	}
	
	@Override
	public void callback() {
		// TODO Auto-generated method stub
		if (enchantType == 1) {
			boolean rr = EnchantEntityManager.instance.lockEnchant(player, index);
			int result = rr ? 1 : -1;
			ENCHANT_LOCK_RES resp = new ENCHANT_LOCK_RES(GameMessageFactory.nextSequnceNum(), result, articleId);
			player.addMessageToRightBag(resp);
		} else {
			boolean rr = EnchantEntityManager.instance.lockAllEnchant(player, 1);
			int result = rr ? 1 : -1;
			ENCHANT_ALL_EQUIPT_RES resp = new ENCHANT_ALL_EQUIPT_RES(GameMessageFactory.nextSequnceNum(), result, 1);
			player.addMessageToRightBag(resp);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getEnchantType() {
		return enchantType;
	}

	public void setEnchantType(int enchantType) {
		this.enchantType = enchantType;
	}
	
	

}
