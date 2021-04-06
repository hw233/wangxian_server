package com.fy.engineserver.articleEnchant.model;

import com.fy.engineserver.datasource.article.data.articles.Article;

public class EnchantArticle extends Article{
	/** 物品对应的附魔id */
	private int enchantId;
	/** 附魔对应装备类型 */
	private int equipmentType;

	public int getEnchantId() {
		return enchantId;
	}

	public void setEnchantId(int enchantId) {
		this.enchantId = enchantId;
	}

	public int getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(int equipmentType) {
		this.equipmentType = equipmentType;
	}

}
