package com.fy.engineserver.datasource.article.data.magicweapon;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.sprite.Player;

public class MagicWeaponEatProps extends Article implements Prop4MagicWeaponEat{
	/** 吞噬获得经验 */
	private int eatExp;

	public int getEatExp() {
		return eatExp;
	}

	public void setEatExp(int eatExp) {
		this.eatExp = eatExp;
	}

	@Override
	public int getAddExp(Player p) {
		// TODO Auto-generated method stub
		int exp = eatExp;
		if(p != null) {
			exp = exp*(p.getMagicWeaponDevourPercent()+100)/100;
		}
		return eatExp;
	}
}
