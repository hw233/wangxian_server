package com.fy.engineserver.shop;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;

public class SellItem {
	
	//卖出的物品
	ArticleEntity ae;
	
	//卖出的数量
	int count = 0;
	
	//卖出的钱
	int sellMoney = 0;
	
	//卖出的时间
	long sellTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
}
