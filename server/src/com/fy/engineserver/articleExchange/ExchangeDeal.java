package com.fy.engineserver.articleExchange;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;


public class ExchangeDeal {

	public  static int idd =0 ;
	//删除状态  为true 删除
//	private boolean state;
	
	private long id;
		
	private long playerAId;
	private long entityAId;
	private boolean playerAConfirmed;
	
	private long playerBId;
	private long entityBId;
	private boolean playerBConfirmed;
	
	private long createTime;
	
	/**
	 * 双方都同意后  完成交换
	 * @return
	 */
	public boolean finishExchange(){
		PlayerManager pm = PlayerManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(playerAConfirmed && playerBConfirmed){
			if(entityAId != -1 && entityAId != -1){
				try {
					Player playerA = pm.getPlayer(playerAId);
					Player playerB = pm.getPlayer(playerBId);
					ArticleEntity ae1 = playerA.getArticleEntity(entityAId);
					ArticleEntity ae2 = playerB.getArticleEntity(entityBId);
					if(ae1 != null && ae2 != null){
						if(ae1 instanceof ExchangeInterface && ae2 instanceof ExchangeInterface && ae1 instanceof ExchangeArticleEntity && ae2 instanceof ExchangeArticleEntity){
							ExchangeInterface eif1 = (ExchangeInterface)ae1;
							ExchangeInterface eif2 = (ExchangeInterface)ae2;
							boolean can = eif1.canExchange(ae2);
							if(can){
								ArticleEntity ee1 = eif1.exchange(ae2,playerA);
								ArticleEntity ee2 = eif2.exchange(ae1,playerB);
							
								if(ee1 != null && ee2 != null){
									playerA.getKnapsack_common().removeByArticleId(entityAId,"交换", true);
									playerB.getKnapsack_common().removeByArticleId(entityBId,"交换", true);
									
									playerA.getKnapsack_common().put(ee1,"交换");
									playerB.getKnapsack_common().put(ee2,"交换");
									ArticleStatManager.addToArticleStat(playerA, null, ee1, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "寻宝交换获得", null);
									ArticleStatManager.addToArticleStat(playerB, null, ee2, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "寻宝交换获得", null);
									if(ExploreManager.logger.isWarnEnabled()){
										ExploreManager.logger.warn("[交换物品成功] ["+playerA.getLogString()+"] ["+playerB.getLogString()+"]");
									}
//									this.state = true;
									playerAConfirmed = false;
									playerBConfirmed = false;
									return true;
								}else{
									if(ArticleExchangeManager.logger.isWarnEnabled())
										ArticleExchangeManager.logger.warn("[完成交换失败] [生成物品为null]");
								}
							}else{
								playerAConfirmed = false;
								playerBConfirmed = false;
								playerA.sendError(Translate.可交换的俩种物品不匹配不可交换);
							}
						}else{
							if(ArticleExchangeManager.logger.isWarnEnabled())
								ArticleExchangeManager.logger.warn("[完成交换失败] [提交物品不是可交换物品]");
						}
					}else{
						if(ArticleExchangeManager.logger.isWarnEnabled())
							ArticleExchangeManager.logger.warn("[完成交换失败] [提交物品为null] []");
					}
				} catch (Exception e) {
					ArticleExchangeManager.logger.warn("[完成交换失败] ["+playerAId+"] ["+playerBId+"]",e);
				}
			}else{
				if(ArticleExchangeManager.logger.isWarnEnabled())
					ArticleExchangeManager.logger.warn("[完成交换失败] [有未提交物品]");
			}
		}else{
			if(ArticleExchangeManager.logger.isWarnEnabled())
				ArticleExchangeManager.logger.warn("[完成交换失败] [有人没同意]");
		}
		return false;
	}
	
	
	public boolean isCanExchange(){
		return playerAConfirmed&&playerBConfirmed;
	}
	
	public ExchangeDeal(){
		this.id = getIdd();
	}
	
	
	public synchronized long getIdd(){
		return ++idd;
	}
	
	
//	public boolean isState() {
//		return state;
//	}
//
//	public void setState(boolean state) {
//		this.state = state;
//	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlayerAId() {
		return playerAId;
	}

	public void setPlayerAId(long playerAId) {
		this.playerAId = playerAId;
	}

	public long getEntityAId() {
		return entityAId;
	}

	public void setEntityAId(long entityAId) {
		this.entityAId = entityAId;
	}

	public boolean isPlayerAConfirmed() {
		return playerAConfirmed;
	}

	public void setPlayerAConfirmed(boolean playerAConfirmed) {
		this.playerAConfirmed = playerAConfirmed;
	}

	public long getPlayerBId() {
		return playerBId;
	}

	public void setPlayerBId(long playerBId) {
		this.playerBId = playerBId;
	}

	public long getEntityBId() {
		return entityBId;
	}

	public void setEntityBId(long entityBId) {
		this.entityBId = entityBId;
	}

	public boolean isPlayerBConfirmed() {
		return playerBConfirmed;
	}

	public void setPlayerBConfirmed(boolean playerBConfirmed) {
		this.playerBConfirmed = playerBConfirmed;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
