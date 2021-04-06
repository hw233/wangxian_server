package com.fy.engineserver.articleEnchant;

import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.ENCHANT_EQUIPMENT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_confirmEnchant extends Option{

	private long equiptIndex;
	
	private int knapeIndex;
	
	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	@Override
	public void doSelect(Game game, Player player) {
		boolean result = EnchantEntityManager.instance.enchant(player, equiptIndex, knapeIndex, true);
		if (result) {
			ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(equiptIndex);
			String articleDes = "";
			if (ae != null && ae instanceof EquipmentEntity) {
				articleDes = ((EquipmentEntity)ae).getEnchantInfoShow(player, false);
			}
			ENCHANT_EQUIPMENT_RES resp = new ENCHANT_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), articleDes);
			player.addMessageToRightBag(resp);
		}
		
	}
	public long getEquiptIndex() {
		return equiptIndex;
	}
	public void setEquiptIndex(long equiptIndex) {
		this.equiptIndex = equiptIndex;
	}
	public int getKnapeIndex() {
		return knapeIndex;
	}
	public void setKnapeIndex(int knapeIndex) {
		this.knapeIndex = knapeIndex;
	}
	
	
}
