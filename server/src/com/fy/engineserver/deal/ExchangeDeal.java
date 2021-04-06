package com.fy.engineserver.deal;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;

public class ExchangeDeal extends Deal {

	
	public ExchangeDeal(Player playerA, Player playerB) {
		super(playerA, playerB);
	}
	
	public boolean deal(Deal deal, Player player){

		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntity ae = null;
		if (deal.getPlayerA().getId() == player.getId()) {
			deal.setAgreedA(true);
		} else {
			deal.setAgreedB(true);
		}
		if (deal.isAgreedA() && deal.isAgreedB()) {
			
			long entityIdAs[] = deal.getEntityIdsA();
			long entityIdBs[] = deal.getEntityIdsB();
			boolean 物品数目是否相等 = true;
			if(entityIdAs.length != entityIdBs.length){
				物品数目是否相等 = false;
			}
			if(!物品数目是否相等){
				return false;
			}
			
			Article a = null;
			boolean 是否都是可交换物品 = true;
			List<ExchangeArticle> listA = new ArrayList<ExchangeArticle>();
			List<ExchangeArticle> listB = new ArrayList<ExchangeArticle>();
			for(long id : entityIdAs){
				ae = aem.getEntity(id);
				if(ae != null){
					a = am.getArticle(ae.getArticleName());
					if( a !=null && a instanceof ExchangeArticle){
						listA.add((ExchangeArticle)a);
					}else{
						是否都是可交换物品 = false;
						break;
					}
				}else{
					是否都是可交换物品 = false;
					break;
				}
			}
			for(long id : entityIdBs){
				ae = aem.getEntity(id);
				if(ae != null){
					a = am.getArticle(ae.getArticleName());
					if( a !=null && a instanceof ExchangeArticle){
						listB.add((ExchangeArticle)a);
					}else{
						是否都是可交换物品 = false;
						break;
					}
				}else{
					是否都是可交换物品 = false;
					break;
				}
			}
			boolean 是否都能匹配 = true;
			for(ExchangeArticle ea : listA){
				boolean 指定这个能不能匹配 = false;
				ExchangeArticle e = null ;
				String parner = ea.getPartnerArticle();
				
				for(ExchangeArticle eaB : listB){
					if(parner.equals(eaB.getName())){
						e = eaB;
						指定这个能不能匹配 = true;
					}
				}
				if(指定这个能不能匹配){
					listB.remove(e);
				}else{
					是否都能匹配 = false;
					break;
				}
			}
			
			boolean allFinish = true;
			List<ArticleEntity> createListA = new ArrayList<ArticleEntity>();
			List<ArticleEntity> createListB = new ArrayList<ArticleEntity>();
			for(ExchangeArticle ea : listA){
				Article article = am.getArticle(ea.getCreateArticle());
				if(article == null){
					allFinish = false;
					if(ExploreManager.logger.isWarnEnabled())
						ExploreManager.logger.warn("[交换物品错误] [不存在生成的article] ["+ea.getCreateArticle()+"]");
					break;
				}else{
					try{
					ArticleEntity ae1 = ArticleEntityManager.getInstance().createEntity(article, true, 0, player, 1, 1, true);
					ArticleEntity ae2 = ArticleEntityManager.getInstance().createEntity(article, true, 0, player, 1, 1, true);
					createListA.add(ae1);
					createListB.add(ae2);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
			
			if(allFinish){
				for(long ida : entityIdAs){
					deal.getPlayerA().getKnapsack_common().removeByArticleId(ida,"交换", false);
				}
				for(long idb : entityIdBs){
					deal.getPlayerB().getKnapsack_common().removeByArticleId(idb,"交换", false);
				}
				for(ArticleEntity ae1 : createListA){
					deal.getPlayerA().getKnapsack_common().put(ae1,"交换");
				}
				for(ArticleEntity ae2 : createListB){
					deal.getPlayerB().getKnapsack_common().put(ae2,"交换");
				}
				if(ExploreManager.logger.isWarnEnabled()){
					ExploreManager.logger.warn("[交换物品成功] ["+player.getLogString()+"]");
				}
				return true;
			}
			
			if(ExploreManager.logger.isWarnEnabled()){
				ExploreManager.logger.warn("[交换物品失败] [物品数目是否相等:"+物品数目是否相等+"] [是否都是可交换物品:"+是否都是可交换物品+"] [是否都能匹配:"+是否都能匹配+"] []");
			}
		}
		return false;
	}
}
