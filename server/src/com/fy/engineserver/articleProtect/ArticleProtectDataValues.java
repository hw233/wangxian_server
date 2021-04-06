package com.fy.engineserver.articleProtect;

public interface ArticleProtectDataValues {

	//state 的类型
	public static final int ArticleProtect_State_Common = 1;		//普通锁魂
	
	public static final int ArticleProtect_State_High = 2;			//高级锁魂
	
	public static final int ArticleProtect_State_Remove = 1000;		//解除锁魂
	
	public static final int ArticleProtect_State_Error = -1;		//异常
	
	//articleType 的类型
	public static final int ArticleProtect_ArticleType_Pet = 100;			//宠物
	
	public static final int ArticleProtect_ArticleType_Equpment = 101;		//装备
	
	public static final int ArticleProtect_ArticleType_Gem = 102;			//宝石
	
	public static final int ArticleProtect_Common = 0;					//普通
	public static final int ArticleProtect_High = 1;					//高级

	
}
