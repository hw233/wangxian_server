package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

public class ExploreProps extends Props{


	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return Article.KNAP_奇珍;
	}
	
	@Override
	public boolean isUsedUndisappear() {
		return true;
	}
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		
		if(!super.use(game,player,ae)){
			return false;
		}
		if(ExploreManager.logger.isWarnEnabled()){
			ExploreManager.logger.warn("[使用铲子寻宝] ["+player.getLogString()+"]");
		}
		if(player.getExploreEntity() != null){
			return player.getExploreEntity().execute(player);
		}
		return false;
	}
}
