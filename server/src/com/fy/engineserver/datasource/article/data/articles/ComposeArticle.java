package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;


/**
 * 
 * 合成类物品
 */
public class ComposeArticle extends Article implements ComposeInterface{

	/**
	 * 合成后物品名
	 */
	protected String comparedArticleName = null;
	
	protected String comparedArticleName_stat = null;

	public String getComparedArticleName_stat() {
		return comparedArticleName_stat;
	}

	public void setComparedArticleName_stat(String comparedArticleName_stat) {
		this.comparedArticleName_stat = comparedArticleName_stat;
	}

	public String getComparedArticleName() {
		return comparedArticleName;
	}

	public void setComparedArticleName(String comparedArticleName) {
		this.comparedArticleName = comparedArticleName;
	}
	/**
	 * 客户端用的，0为不能合成 1为能合成
	 */
	public byte getComposeArticleType() {
		return 1;
	}
	@Override
	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount) {
		// TODO Auto-generated method stub
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(am != null && aem != null && player != null && ae != null && this.name.equals(ae.getArticleName())){
			Article a = am.getArticle(comparedArticleName);
			if(a != null){
				try{
					ArticleEntity aee = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, a.colorType, createCount, true);
					return aee;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public long getTempComposeEntityId(Player player, ArticleEntity ae, boolean binded) {
		// TODO Auto-generated method stub
		long tempId = -1;
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(am != null && aem != null && player != null && ae != null && this.name.equals(ae.getArticleName())){
			if(comparedArticleName == null || comparedArticleName.trim().equals("")){
				return -1;
			}
			Article a = am.getArticle(comparedArticleName);
			if(a != null){
				try{
					ArticleEntity aee = aem.createTempEntity(a, binded, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, a.colorType);
					if(aee != null){
						return aee.getId();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
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
