package com.fy.engineserver.datasource.article.data.props;

import org.slf4j.Logger;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_PARTICLE_REQ;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.sprite.Player;

/**
 * 使用后获得称号的物品
 *
 */
public class TitleItemProp  extends Props {
	public String titleName;
	
	public String titleName_stat;
	
	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(!super.use(game,player,ae)){
			return false;
		}
		PlayerTitlesManager ttMgr = PlayerTitlesManager.getInstance();
		int key = ttMgr.getKeyByType(titleName);
		Logger log = ArticleManager.logger;
		if(key<=0){
			log.error("title not found [{}]", titleName);
			return false;
		}else{
			ttMgr.addTitle(player, titleName, false);
			if(log.isDebugEnabled()){
				log.debug("[{}] 使用物品获得称号 [{}]",
						player.getName(), titleName);
			}
			try {
				NOTICE_PARTICLE_REQ req = new NOTICE_PARTICLE_REQ(GameMessageFactory.nextSequnceNum(), 1, 3);
				player.addMessageToRightBag(req);
			} catch (Exception e) {
				
			}
		}
		return true;
	}
}
