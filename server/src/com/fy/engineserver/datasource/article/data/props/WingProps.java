package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class WingProps extends Props {
	private int kindId;// 翅膀所属种id

	@Override
	public String canUse(Player p) {
		return super.canUse(p);
	}

	public byte getArticleType() {
		return ARTICLE_TYPE_WING;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if (!super.use(game, player, ae)) {
			return false;
		}
		return false;
	}
	
	

	@Override
	public String get物品一级分类() {
		// TODO Auto-generated method stub
		return Translate.翅膀类;
	}
	
	@Override
	public String get物品二级分类() {
		return Translate.翅膀类;
	}


	public int getKindId() {
		return kindId;
	}

	public void setKindId(int kindId) {
		this.kindId = kindId;
	}

}
