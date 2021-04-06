package com.fy.engineserver.sprite.pet.suit.effect;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;

/**
 * 宠物套装属性处理
 * 
 * @date on create 2016年8月26日 上午9:55:26
 */
public abstract class AbstractSuitEffect {
	/**
	 * 宠物穿套装检查
	 * @param pet
	 * @param ae
	 * @return
	 *//*
	public String check4Puton(Pet pet, ArticleEntity ae) {
		try {
			if (ae instanceof PetSuitArticleEntity) {
				return null;
			} else {
				return "must_pet_suit";
			}
		} catch (Exception e) {
			PetManager.logger.warn("[检查宠物传套装] [异常] [" + pet.getId() + "] [" + ae.getId() + "," + ae.getArticleName() + "]", e);
		}
		return Translate.破封任务配置错误;
	}
	public String check4Puton(Pet pet, Article a) {
		try {
			if (a instanceof PetSuitArticle) {
				return null;
			} else {
				return "must_pet_suit";
			}
		} catch (Exception e) {
			PetManager.logger.warn("[检查宠物传套装] [异常] [" + pet.getId() + "] [" + a.getName() + "]", e);
		}
		return Translate.破封任务配置错误;
	}*/
	/**
	 * 宠物套装生效检查
	 * @param pet
	 * @param ae
	 * @return
	 */
	public String check4Effect(Pet pet, ArticleEntity ae) {
		try {
			if (ae instanceof PetSuitArticleEntity) {
				PetSuitArticle a = (PetSuitArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
				if (a.getCharacter() >= 0 && a.getCharacter() != pet.getCharacter()) {
					return "性格不符";
				}
				ArticleEntity pae = ArticleEntityManager.getInstance().getEntity(pet.getPetPropsId());
				Article pa = ArticleManager.getInstance().getArticle(pae.getArticleName());
				if (a.getPetEggName() != null && a.getPetEggName().length > 0 ) {
					for (String st : a.getPetEggName()) {
						if (st.equals(pa.getName_stat())) {
							return null;
						}
					}
				} else {
					return null;
				}
				return "宠物名不符";
			} else {
				return "不是宠物饰品";
			}
		} catch (Exception e) {
			PetManager.logger.warn("[检查宠物传套装生效] [异常] [" + pet.getId() + "] [" + ae.getId() + "," + ae.getArticleName() + "]", e);
		}
		return Translate.破封任务配置错误;
	}
	
	public final void effect(Pet pet, PetSuitArticleEntity pae, int index) {
		try {
			String str = this.check4Effect(pet, pae);
			if (str == null) {
				this.doEffect(pet, pae, index);
			}
			if (PetManager.logger.isInfoEnabled()) {
				PetManager.logger.info("[宠物套装属性] [检查结果:" + str + "] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "]");
			}
		} catch (Exception e) {
			PetManager.logger.warn("[宠物套装属性] [增加异常] [petId:" + pet.getId() + "] [aeId:" + pae.getId() + "]", e );
		}
	}
	
	/**
	 * 套装效果
	 * @param pet
	 */
	public abstract void doEffect(Pet pet, PetSuitArticleEntity pae, int index);
	
}
