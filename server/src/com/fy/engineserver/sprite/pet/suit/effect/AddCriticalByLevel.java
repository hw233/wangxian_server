package com.fy.engineserver.sprite.pet.suit.effect;

import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;
/**
 * 等级相关增加宠物暴击值   (（公式：等级索引值+等级*10）*等级*0.25)
 * 
 * @date on create 2016年11月25日 下午3:15:51
 */
public class AddCriticalByLevel extends AbstractSuitEffect{

	@Override
	public void doEffect(Pet pet, PetSuitArticleEntity pae, int index) {
		// TODO Auto-generated method stub
		int lv = pet.getLevel();
//		PetSuitArticle a = (PetSuitArticle) ArticleManager.getInstance().getArticle(pae.getArticleName());
		int addNum = (int) (CombatCaculator.F(lv) * CombatCaculator.A_会心一击 * 0.25) ;
		int oldValue = pet.getCriticalHitB();
		pet.setCriticalHitB(pet.getCriticalHitB() + addNum);
		int newValue = pet.getCriticalHitB();
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("["+this.getClass().getSimpleName()+"] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "] [值:" + oldValue + "->" + newValue + "]");
		}
	}

}
