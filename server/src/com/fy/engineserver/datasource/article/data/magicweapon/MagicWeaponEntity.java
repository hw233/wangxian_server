package com.fy.engineserver.datasource.article.data.magicweapon;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;

public class MagicWeaponEntity extends ArticleEntity{

	protected long owner;						//拥有者
	
	protected byte colortype;					//法宝颜色
	
	protected String basicPropertyName;			//基础属性名，基础属性不变，可以获得基础属性类型
	
	protected String title;						//法宝称号
	
	protected int star;							//法宝加持级别
	
	
	
}
