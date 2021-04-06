package com.fy.engineserver.articleExchange;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

public interface ExchangeInterface {

	public boolean canExchange(ArticleEntity ae);
	
	public ArticleEntity exchange(ArticleEntity ae,Player player);
		
}
