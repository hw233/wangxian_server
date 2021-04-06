package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;

/**
 * 宠物技能书道具，使用时通知客户端打开传承界面。
 */
public class PetSkillProp extends Props{
	public int skillId;
	public int skillLv;
	/**
	 * TF / JC
	 */
	public String cateKey;
	public String xingGe;
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(!super.use(game,player,ae)){
			return false;
		}
		Pet2SkillCalc.getInst().useBook(player, ae);
		return false;
	}

	public int getSkillId() {
		return skillId;
	}

	public int getSkillLv() {
		return skillLv;
	}

	public String getCateKey() {
		return cateKey;
	}

	public String getXingGe() {
		return xingGe;
	}
}
