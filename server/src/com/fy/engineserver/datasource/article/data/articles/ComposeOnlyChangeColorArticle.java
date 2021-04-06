package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;

/**
 * 仅仅是颜色变化的合成，此合成物品有5种颜色
 *
 */
public class ComposeOnlyChangeColorArticle extends Article implements ComposeInterface{

	
	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount) {
		// TODO Auto-generated method stub
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(am == null || aem == null){
			return null;
		}
		if(ae.getColorType() < ArticleManager.notEquipmentColorMaxValue){
			Article a = am.getArticle(ae.getArticleName());
			if(a != null){
				try{
					ArticleEntity aee = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, ae.getColorType()+1, createCount, true);
					return aee;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			return null;
		}
		
		return null;
	}

	public byte getComposeArticleType() {
		return 1;
	}
@Override
public long getTempComposeEntityId(Player player, ArticleEntity ae,
		boolean binded) {
	long tempId = -1;
	if(ae != null){
		if(ae.getColorType() < ArticleManager.notEquipmentColorMaxValue){
			return 0;
		}
	}
	return tempId;
}
@Override
public String getTempComposeEntityDescription(Player player,
		ArticleEntity ae, boolean binded) {
	// TODO Auto-generated method stub
	String description = "";
	return description;
}
}
