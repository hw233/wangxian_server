package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

public class HunshiProps extends Props {
	private int type;// 0-魂石；1-套装魂石
	private int index;// 带窍魂石的窍位，其它为-1

	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	@Override
	public byte getArticleType() {
		return type == 0 ? ARTICLE_TYPE_HUNSHI : ARTICLE_TYPE_TAOZHUANGHUNSHI;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		return false;
	}

	@Override
	public String get物品一级分类() {
		// TODO Auto-generated method stub
		return super.get物品一级分类();
	}

	@Override
	public String get物品二级分类() {
		// TODO Auto-generated method stub
		return super.get物品二级分类();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
