package com.fy.engineserver.datasource.article.data.equipments;

import com.fy.engineserver.datasource.language.Translate;

public class Weapon extends Equipment{

//	0  斧
//	1  杖
//
//	2  刺
//	3  轮
//	
//	4  笔
//	5  剑
//	
//	6  杖
//	7  幡
//  8 镰刀
	public static String[] WEAPONTYPE_NAME = new String[]{Translate.空手, Translate.斧, Translate.禅杖, Translate.刺, Translate.轮,Translate.笔, Translate.剑, Translate.杖, Translate.幡, Translate.镰刀, Translate.权杖};
	/**
	 * 武器种类
	 */
	protected byte weaponType;

	public byte getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(byte weaponType) {
		this.weaponType = weaponType;
	}
	@Override
	public byte getArticleType() {
		// TODO Auto-generated method stub
		return ARTICLE_TYPE_WEAPON;
	}
}
