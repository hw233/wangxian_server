package com.fy.engineserver.battlefield.dota;

import com.fy.engineserver.datasource.language.Translate;



public class DotaConstants {

	public static final String 战场地图 = Translate.text_2006;
	
	public static final String 战场徽章 = Translate.text_2007;
	
	public static final int 英雄的最大级别 = 25;
	
	public static final String 对阵双方的名称[] = new String[]{Translate.text_1846,Translate.text_2008,Translate.text_2009};
	
	public static int 经验值和基本对应表[] = new int[] {
		0   , 200,500,900,1400,2000,2700,3500,4400,5400,
		6500, 7700,9000,10400,11900,13500,15200,17000,18900,20900,23000,
		25200,27500,29900,32400	};
	
	public static int 杀死英雄经验掉落[] = new int[]{
		0,100,120,160, 220, 300,
		400,500,600,700, 800,900, 1000, 1100,
		1200,1300,1400,1500,1600,1700,1800,
		1900,2000, 2100,2200,2300
	};
	// 5v5  10v10  15v15  20v20
	//public static int 近程兵的血量[] = new int[]{1380,1380*2,1380*3,1380*4};
	public static int 近程兵的血量[] = new int[]{18000,18000,18000,18000};
	public static int 近程兵的武器伤害[] = new int[]{95,95,95,95};
	public static int 近程兵的攻击强度[] = new int[]{375,375,375,375};
	public static int 近程兵的法术强度[] = new int[]{0,0,0,0};
	public static int 近程兵的命中等级[] = new int[]{100,100,100,100};
	public static int 近程兵的闪避等级[] = new int[]{3,3,3,3};
	public static int 近程兵的暴击等级[] = new int[]{5,5,5,5};
	public static int 近程兵的物理防御[] = new int[]{188,188*3/2,188*2,188*5/2};
	public static int 近程兵的法术防御[] = new int[]{0,0,0,0};
	
	//public static int 远程兵的血量[] = new int[]{904,904*2,904*3,904*4};
	public static int 远程兵的血量[] = new int[]{10800,10800,10800,10800};
	public static int 远程兵的武器伤害[] = new int[]{110,110,110,110};
	public static int 远程兵的攻击强度[] = new int[]{0,0,0,0};
	public static int 远程兵的法术强度[] = new int[]{240,240,240,240};
	public static int 远程兵的命中等级[] = new int[]{95,95,95,95};
	public static int 远程兵的闪避等级[] = new int[]{0,0,0,0};
	public static int 远程兵的暴击等级[] = new int[]{100,100,100,100};
	public static int 远程兵的物理防御[] = new int[]{208,208,208,208};
	public static int 远程兵的法术防御[] = new int[]{0,0,0,0};
	
	//public static int 攻城兵的血量[] = new int[]{1892,1892*2,1892*3,1892*4};
	public static int 攻城兵的血量[] = new int[]{24000,24000,24000,24000};
	public static int 攻城兵的武器伤害[] = new int[]{160,160,160,160};
	public static int 攻城兵的攻击强度[] = new int[]{450,450,450,450};
	public static int 攻城兵的法术强度[] = new int[]{590,590,590,590};
	public static int 攻城兵的命中等级[] = new int[]{100,100,100,100};
	public static int 攻城兵的闪避等级[] = new int[]{0,0,0,0};
	public static int 攻城兵的暴击等级[] = new int[]{200,200,200,200};
	public static int 攻城兵的物理防御[] = new int[]{1200,1600,2000,2400};
	public static int 攻城兵的法术防御[] = new int[]{500,800,1000,1200};
	
	//public static int 前塔的血量[] = new int[]{21234,21234*2,21234*3,21234*4};
	public static int 前塔的血量[] = new int[]{360000,360000,360000,360000};
	public static int 前塔的武器伤害[] = new int[]{100,100,100,100};
	public static int 前塔的攻击强度[] = new int[]{550,550,550,550};
	public static int 前塔的法术强度[] = new int[]{880,880,880,880};
	public static int 前塔的命中等级[] = new int[]{200,200,200,200};
	public static int 前塔的闪避等级[] = new int[]{0,0,0,0};
	public static int 前塔的暴击等级[] = new int[]{200,200,200,200};
	public static int 前塔的物理防御[] = new int[]{500,500,500,500};
	public static int 前塔的法术防御[] = new int[]{2500,2500,2500,2500};
	
	//public static int 中塔的血量[] = new int[]{23320,23320*2,23320*3,23320*4};
	public static int 中塔的血量[] = new int[]{480000,480000,480000,480000};
	public static int 中塔的武器伤害[] = new int[]{100,100,100,100};
	public static int 中塔的攻击强度[] = new int[]{600,600,600,600};
	public static int 中塔的法术强度[] = new int[]{1030,1030,1030,1030};
	public static int 中塔的命中等级[] = new int[]{200,200,200,200};
	public static int 中塔的闪避等级[] = new int[]{0,0,0,0};
	public static int 中塔的暴击等级[] = new int[]{200,200,200,200};
	public static int 中塔的物理防御[] = new int[]{500,500,500,500};
	public static int 中塔的法术防御[] = new int[]{2500,2500,2500,2500};

	//public static int 后塔的血量[] = new int[]{25320,25320*2,25320*3,25320*4};
	public static int 后塔的血量[] = new int[]{600000,600000,600000,600000};
	public static int 后塔的武器伤害[] = new int[]{100,100,100,100};
	public static int 后塔的攻击强度[] = new int[]{600,600,600,600};
	public static int 后塔的法术强度[] = new int[]{1630,1630,1630,1630};
	public static int 后塔的命中等级[] = new int[]{200,200,200,200};
	public static int 后塔的闪避等级[] = new int[]{0,0,0,0};
	public static int 后塔的暴击等级[] = new int[]{200,200,200,200};
	public static int 后塔的物理防御[] = new int[]{500,500,500,500};
	public static int 后塔的法术防御[] = new int[]{2500,2500,2500,2500};
	
	//public static int 基地塔的血量[] = new int[]{28880,28880*2,28880*3,28880*4};
	public static int 基地塔的血量[] = new int[]{1000000,1000000,1000000,1000000};
	public static int 基地塔的武器伤害[] = new int[]{100,100,100,100};
	public static int 基地塔的攻击强度[] = new int[]{900,900,900,900};
	public static int 基地塔的法术强度[] = new int[]{1930,1930,1930,1930};
	public static int 基地塔的命中等级[] = new int[]{200,200,200,200};
	public static int 基地塔的闪避等级[] = new int[]{0,0,0,0};
	public static int 基地塔的暴击等级[] = new int[]{200,200,200,200};
	public static int 基地塔的物理防御[] = new int[]{500,500,500,500};
	public static int 基地塔的法术防御[] = new int[]{2500,2500,2500,2500};
	
	//public static int 温泉塔的血量[] = new int[]{31234,31234*2,31234*3,31234*4};
	public static int 温泉塔的血量[] = new int[]{1500000,1500000,1500000,1500000};
	public static int 温泉塔的武器伤害[] = new int[]{1300,1300,1300,1300};
	public static int 温泉塔的攻击强度[] = new int[]{3450,3450,3450,3450};
	public static int 温泉塔的法术强度[] = new int[]{4380,4380,4380,4380};
	public static int 温泉塔的命中等级[] = new int[]{200,200,200,200};
	public static int 温泉塔的闪避等级[] = new int[]{0,0,0,0};
	public static int 温泉塔的暴击等级[] = new int[]{200,200,200,200};
	public static int 温泉塔的物理防御[] = new int[]{5000,5000,5000,5000};
	public static int 温泉塔的法术防御[] = new int[]{2000,2000,2000,2000};
	
	public static int 兵营的血量[] = new int[]{360000,360000,360000,360000};
	public static int 兵营的物理防御[] =  new int[]{500,500,500,500};
	public static int 兵营的法术防御[] = new int[]{2000,2000,2000,2000};
	
	public static int 主将的血量[] = new int[]{2000000,2000000,2000000,2000000};
	public static int 主将的物理防御[] = new int[]{500,500,500,500};
	public static int 主将的法术防御[] = new int[]{2000,2000,2000,2000};
	
	public static String BattleItem[] = new String[]{	Translate.text_2007,
														Translate.text_2010,
														Translate.text_2011,
														Translate.text_2012,
														Translate.text_2013,
														Translate.text_2014,
														Translate.text_2015,
														Translate.text_2016,
														Translate.text_2017,
														Translate.text_2018,
														Translate.text_2019,
														Translate.text_2020,
														Translate.text_2021,
														Translate.text_2022,
														Translate.text_2023,
														Translate.text_2024,
														Translate.text_2025,
														Translate.text_2026,
														Translate.text_2027,
														Translate.text_2028,
														Translate.text_2029,
														Translate.text_2030,
														Translate.text_2031,
														Translate.text_2032,
														Translate.text_2033,
														Translate.text_2034,
														Translate.text_2035,
														Translate.text_2036,
														Translate.text_2037,
														Translate.text_2038,
														Translate.text_2039,
														Translate.text_2040,
														Translate.text_2041,
														Translate.text_2042,
														Translate.text_2043,
														Translate.text_2044,
														Translate.text_2045,
														Translate.text_2046,
														Translate.text_2047,
														Translate.text_2048,
														Translate.text_2049,
														Translate.text_2050,
														Translate.text_2051,
														Translate.text_2052,
														Translate.text_2053,
														Translate.text_2054,
														Translate.text_2055,
														Translate.text_2056,
														Translate.text_2057,
														Translate.text_2058,
														Translate.text_2059,
														Translate.text_2060,
														Translate.text_2061,
														Translate.text_2062,
														Translate.text_2063,
														Translate.text_2064,
														Translate.text_2065,
														Translate.text_2066,
														Translate.text_2067,
														Translate.text_2068,
														Translate.text_2069,
														Translate.text_2070,
														Translate.text_2071,
														Translate.text_2072,
														Translate.text_2073,
														Translate.text_2074,
														Translate.text_2075,
														Translate.text_2076,
														Translate.text_2077,
														Translate.text_2078};
}
