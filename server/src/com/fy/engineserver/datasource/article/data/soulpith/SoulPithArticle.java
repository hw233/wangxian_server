package com.fy.engineserver.datasource.article.data.soulpith;

import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.soulpith.SoulPithManager;

/**
 * 灵髓道具
 */
public class SoulPithArticle extends Article{
	
	/** key=灵髓等级 value=对应等级灵髓附加的属性等 */
	public Map<Integer, SoulPithArticleLevelData> getLevelDatas() {
		return SoulPithManager.getInst().soulArticleDatas.get(this.getName_stat()).getLevelData();
	}
	
}
