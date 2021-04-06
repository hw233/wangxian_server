package com.fy.engineserver.menu.activity.dice;

import com.fy.engineserver.activity.dice.DiceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.DICE_SIGN_UP_QUERY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;

public class Option_Join_Activity extends Option {

	@Override
	public void doSelect(Game game, Player player) {
		boolean hasSignUp = DiceManager.getInstance().signList.contains(player.getId());
		long aeIds [] = new long[DiceManager.getInstance().winArticles.length];
		int index = 0;
		for(String name : DiceManager.getInstance().winArticles){
			Article article = ArticleManager.getInstance().getArticleByCNname(name);
			if(article == null){
				DiceManager.logger.warn("[骰子仙居] [报名] [获取奖励数据出错] [物品不存在:"+name+"] [player:"+player.getLogString()+"]");
				continue;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, article.getColorType());
			if(ae == null){
				try {
					int colorType = article.getColorType();
					if(name.equals("古董")){
						colorType = 3;
					}else if(name.equals("人物经验")){
						colorType = 4;
					}
					ae = ArticleEntityManager.getInstance().createTempEntity(article, true, ArticleEntityManager.骰子活动临时物品, null, colorType);
					ae.setBinded(true);
				} catch (Exception e) {
					e.printStackTrace();
					DiceManager.logger.warn("[骰子仙居] [报名] [创建临时物品出错] [物品不存在:"+name+"] [player:"+player.getLogString()+"]");
				}
			}
			aeIds[index] = ae.getId();
			index++;
		}
		DICE_SIGN_UP_QUERY_RES res = new DICE_SIGN_UP_QUERY_RES(GameMessageFactory.nextSequnceNum(), Translate.参与时间, 
				Translate.参与方式, Translate.进入仙居, Translate.活动介绍, Translate.经验, (DiceManager.getInstance().signList.size()),hasSignUp,aeIds,DiceManager.getInstance().winNums);
		player.addMessageToRightBag(res);
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
}
