package com.fy.engineserver.sprite.pet.suit.effect;

import java.util.Arrays;

import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;

public class EffectAddDamageByHp2 extends AbstractSuitEffect{

	@Override
	public void doEffect(Pet pet, PetSuitArticleEntity pae, int index) {
		// TODO Auto-generated method stub
		PetSuitArticle a = (PetSuitArticle) ArticleManager.getInstance().getArticle(pae.getArticleName());
		int[] tempA = Arrays.copyOf(pet.getHpPercent4Hit2(), pet.getHpPercent4Hit2().length + 1);
		tempA[tempA.length - 1] = a.getTriggerCondNum()[index];
		int[] tempB = Arrays.copyOf(pet.getHitIncreaseRate2(), pet.getHitIncreaseRate2().length + 1);
		tempB[tempB.length - 1] = a.getPropertyNum()[index];
		pet.setHpPercent4Hit2(tempA);
		pet.setHitIncreaseRate2(tempB);
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("["+this.getClass().getSimpleName()+"] [index:"+index+"] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "] ["+Arrays.toString(pet.getHpPercent4Hit2())+"] ["+Arrays.toString(pet.getHitIncreaseRate2())+"]") ;
		}
	}

}
