package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.articleExchange.ArticleExchangeManager;
import com.fy.engineserver.articleExchange.ExchangeInterface;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

@SimpleEntity
public class ExchangeArticleEntity extends ArticleEntity implements
		ExchangeInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExchangeArticleEntity(){}
	public ExchangeArticleEntity(long id){
		super(id);
	}
	
	
	private long taskId;
	
	@Override
	public boolean canExchange(ArticleEntity ae) {
		
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(this.getArticleName());
		if( a != null && a instanceof ExchangeArticle){
			ExchangeArticle ea = (ExchangeArticle)a;
			String partner = ea.getPartnerArticle();
			if(partner.equals(ae.getArticleName())){
				
				if(ArticleExchangeManager.logger.isInfoEnabled()){
					ArticleExchangeManager.logger.info("[判断是否能交换] [能交换]");
				}
				return true;
			}else{
				if(ArticleExchangeManager.logger.isInfoEnabled()){
					ArticleExchangeManager.logger.info("[判断是否能交换] [不能交换] ["+partner+"] ["+ae.getArticleName()+"]");
				}
			}
		}else{
			if(ArticleExchangeManager.logger.isInfoEnabled()){
				ArticleExchangeManager.logger.info("[判断是否能交换] [不是交换物品]");
			}
		}
		return false;
	}

	@Override
	public ArticleEntity exchange(ArticleEntity ae,Player player) {
		
		if(this.canExchange(ae)){
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(this.getArticleName());
			ExchangeArticle ea = (ExchangeArticle)a;
			String name = ea.getCreateArticle();
			Article create = am.getArticle(name);
			if(create != null){
				try{
					ArticleEntity ae1= ArticleEntityManager.getInstance().createEntity(create, true, ArticleEntityManager.CREATE_REASON_explore, null, 1, 1, true);
					
					if(ae1 instanceof ExchangeArticleEntity){
						ExchangeArticleEntity eae1 = (ExchangeArticleEntity)ae1;
						eae1.setTaskId(taskId);
						ArticleEntityManager.getInstance().em.notifyFieldChange(this, "taskId");
						if(ExploreManager.logger.isWarnEnabled())
							ExploreManager.logger.warn("[交换成功] [得到物品] ["+player.getLogString()+"]");
						return eae1;
					}else{
						if(ExploreManager.logger.isWarnEnabled())
							ExploreManager.logger.warn("[交换失败] [不是寻宝物品] ["+player.getLogString()+"]");
						return null;
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}else{
				if (ArticleExchangeManager.logger.isWarnEnabled()) {
					ArticleExchangeManager.logger.warn("[创建交换后物品失败] []");
				}
			}
		}
		return null;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
		ArticleEntityManager.getInstance().em.notifyFieldChange(this, "taskId");
	}
	
}
