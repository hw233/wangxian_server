package com.fy.engineserver.menu.activity.explore;

import java.util.List;

import com.fy.engineserver.activity.explore.ExploreManager;
import com.fy.engineserver.articleExchange.ArticleExchangeManager;
import com.fy.engineserver.articleExchange.ExchangeDeal;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SPECIAL_DEAL_AGREE_RES;
import com.fy.engineserver.sprite.Player;

/**
 * 同意交换
 * @author Administrator
 *
 */
public class Option_Agree_Exchange extends Option {
	
	private Player playerA;
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();
		boolean bln = aem.playerCanExchange(playerA, player);
		if(bln){
			ExchangeDeal ed = aem.createNewExchangeDeal(playerA, player);
			if(ed != null){
				ExploreManager em = ExploreManager.getInstance();
				List<Long> listA = em.queryCanExchangeArticle(playerA);
				int maxa = listA.size();
				if(maxa > 0){
					long[] la = new long[maxa];
					int i=0;
					for(long id : listA){
						la[i] = id;
						i++;
					}
					SPECIAL_DEAL_AGREE_RES res = new SPECIAL_DEAL_AGREE_RES(GameMessageFactory.nextSequnceNum(), ed.getId(),la);
					playerA.addMessageToRightBag(res);
				}else{
					SPECIAL_DEAL_AGREE_RES res = new SPECIAL_DEAL_AGREE_RES(GameMessageFactory.nextSequnceNum(), ed.getId(),new long[0]);
					playerA.addMessageToRightBag(res);
				}
				
				List<Long> listB = em.queryCanExchangeArticle(player);
				int maxb = listB.size();
				if(maxb > 0){
					long[] lb = new long[maxb];
					int i=0;
					for(long id : listB){
						lb[i] = id;
						i++;
					}
					SPECIAL_DEAL_AGREE_RES res = new SPECIAL_DEAL_AGREE_RES(GameMessageFactory.nextSequnceNum(), ed.getId(),lb);
					player.addMessageToRightBag(res);
				}else{
					SPECIAL_DEAL_AGREE_RES res = new SPECIAL_DEAL_AGREE_RES(GameMessageFactory.nextSequnceNum(), ed.getId(),new long[0]);
					player.addMessageToRightBag(res);
				}

				if(ArticleExchangeManager.logger.isWarnEnabled()){
					ArticleExchangeManager.logger.warn("[交换对方同意] ["+player.getLogString()+"]");
				}
			}else{
				if(ArticleExchangeManager.logger.isWarnEnabled()){
					ArticleExchangeManager.logger.warn("[交换对方同意失败] ["+player.getLogString()+"]");
				}
			}
			
		}
		
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}
	
}


