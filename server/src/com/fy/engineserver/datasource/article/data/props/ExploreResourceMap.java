package com.fy.engineserver.datasource.article.data.props;


import com.fy.engineserver.activity.explore.ExploreEntity;
import com.fy.engineserver.activity.explore.ExploreResourceMapEntity;
import com.fy.engineserver.articleExchange.ArticleExchangeManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.USE_EXPLORE_RESOURCEMAP_RES;
import com.fy.engineserver.sprite.Player;

public class ExploreResourceMap extends Props {

	@Override
	public String canUse(Player p) {
		
		return null;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		if(ae instanceof ExploreResourceMapEntity){
			ExploreEntity ee = player.getExploreEntity();
			if(ee != null){
				String ss = ee.getMapSegmentName();
				if(ss != null){
					//显示地图 :实际地图
					String show = ee.getShowMap()+":"+ee.getMapName();
					USE_EXPLORE_RESOURCEMAP_RES res = new USE_EXPLORE_RESOURCEMAP_RES(GameMessageFactory.nextSequnceNum(), ee.getPoint().x,ee.getPoint().y,show,ee.getCountry());
					player.addMessageToRightBag(res);
					if(ArticleExchangeManager.logger.isWarnEnabled()){
						ArticleExchangeManager.logger.warn("[使用寻宝藏宝图] ["+player.getLogString()+"]");
					}
					ArticleExchangeManager.logger.error("[成功] [ae:"+ae+"] ["+player.getLogString()+"]");
					return true;
				}else{
					ArticleExchangeManager.logger.error("[使用寻宝藏宝图失败3] [ae:"+ae+"] ["+player.getLogString()+"]");
				}
			}else{
				ArticleExchangeManager.logger.error("[使用寻宝藏宝图失败2] [ae:"+ae+"] ["+player.getLogString()+"]");
			}
		}else{
			ArticleExchangeManager.logger.error("[使用寻宝藏宝图失败1] [ae:"+ae+"] ["+player.getLogString()+"]");
		}
	
		ArticleExchangeManager.logger.error("[使用寻宝藏宝图失败] ["+player.getLogString()+"]");

		return false;
	}

	
}
