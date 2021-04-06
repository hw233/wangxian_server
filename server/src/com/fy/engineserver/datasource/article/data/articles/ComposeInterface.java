package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 可使用瓶子合成的接口
 *
 */
public interface ComposeInterface {

	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount);
	
	public long getTempComposeEntityId(Player player, ArticleEntity ae, boolean binded);
	
	public String getTempComposeEntityDescription(Player player, ArticleEntity ae, boolean binded);
}
