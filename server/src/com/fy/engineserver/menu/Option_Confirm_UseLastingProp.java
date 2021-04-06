package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.LastingProps;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;

public class Option_Confirm_UseLastingProp extends Option {
	
	private ArticleEntity ae;
	
	private Game game;
	
	private LastingProps props;

	@Override
	public byte getOType() {
		return OPTION_TYPE_SERVER_FUNCTION;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		ArticleEntity aee = player.getArticleEntity(ae.getId());
		if (aee != null) {
			boolean result = props.use(game, player, aee, true);
			if (result) {
				ArticleEntity ae = player.removeArticleEntityFromKnapsackByArticleId(aee.getId(), "使用道具删除", true);
				if(ae==null){
					String description = Translate.删除物品不成功;
					HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, description);
					player.addMessageToRightBag(hreq);
					if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具] ["+description+"] [id:"+aee.getId()+"]");
					return;
				}
				player.send_HINT_REQ(Translate.translateString(Translate.您使用了物品, new String[][] { { Translate.STRING_1, ae.getArticleName() } }));
			}
		}
	}

	public ArticleEntity getAe() {
		return ae;
	}

	public void setAe(ArticleEntity ae) {
		this.ae = ae;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public LastingProps getProps() {
		return props;
	}

	public void setProps(LastingProps props) {
		this.props = props;
	}
}
