package com.fy.engineserver.menu;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class Option_ConfirmLockEnchant extends Option{
	
	private int index;
	
	private long articleId;
	
	private int enchantType;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		player.sendError(Translate.正在锁定);
		EnchantEntityManager.instance.openLockTimerTask(player, index, articleId, enchantType,true);
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
