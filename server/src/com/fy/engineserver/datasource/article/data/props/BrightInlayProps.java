package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class BrightInlayProps extends Props {

	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	public byte getArticleType() {
		return ARTICLE_TYPE_BRIGHTINLAY;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		// TODO Auto-generated method stub
//		return super.use(game, player, ae);
		return false;
	}

	@Override
	public String get物品一级分类() {
		// TODO Auto-generated method stub
		return Translate.宝石类;
	}

	@Override
	public String get物品二级分类() {
		// TODO Auto-generated method stub
		return Translate.光效宝石;
	}
	
	

}
