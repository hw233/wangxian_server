package com.fy.engineserver.datasource.skill2;

import com.fy.engineserver.datasource.buff.Buff;

/**
 * 影响某些属性的buff，通过属性指定影响哪个精灵属性，避免每种属性都需要一个buff类。
 * 
 * create on 2013年8月30日
 */
public class GenericBuff extends Buff{
	public static final int ATT_ERROR		= -1;
	public static final int ATT_SubA_AddB = 5;
	public static final int ATT_ZHUAN_HUAN = 6;
	public static final int ATT_KanRenXiaCai = 7;
	public static final int ATT_APPEND_DAMAGE = 8;
	public static final int ATT_ADD_SUB_BUFF = 9;
	public static final int ATT_LiLiang = 11;
	public static final int ATT_ShenFa = 12;
	public static final int ATT_LinLi = 13;
	public static final int ATT_NaiLi = 14;
	public static final int ATT_DingLi = 15;
	
	/**
	 * 人物经验获得增加的百分比
	 */
	public static final int ATT_PLAYER_EXP_PERCENT = 16;
	/**
	 * 暴击率，千分
	 */
	public static final int ATT_BaoJiLv = 17;
	/**
	 * 暴击点数（就是暴击最基础的那个值）
	 */
	public static final int ATT_BaoJiPoint = 18;
	public static final int ATT_GongJiLi = 19;
	public static final int ATT_phy_Defence = 20;
	public static final int ATT_HP_MAX = 21;
	public static final int ATT_SPEED_POINT = 22;
	public static final int ATT_phy_ATK = 23;
	/**
	 * 暴击系数，默认200
	 */
	public static final int ATT_BaoJiXiShu = 24;
	public static final int ATT_magic_ATK = 25;
	//
	public static final int ATT_Feng_Gong = 31;
	public static final int ATT_Huo_Gong = 32;
	public static final int ATT_Lei_Gong = 33;
	public static final int ATT_Bing_Gong = 34;
	//
	public static final int ATT_Feng_Kang = 41;
	public static final int ATT_Huo_Kang = 42;
	public static final int ATT_Lei_Kang = 43;
	public static final int ATT_Bing_Kang = 44;
	//
	/**
	 * 对幽冥幻域内所有怪物造成1.5倍伤害
	 */
	public static final int ATT_Tower = 45;
	/**
	 * 几率性产生加倍伤害。
	 */
	public static final int ATT_DMG_SCALE_RATE = 46;
	/** 增加百分比血上限及反伤 */
	public static final int ATT_HPANDANTI_SCALE_RATE = 47;
	
	public static final int ATT_AURA_SKILL = 48;
	/** 受到致命一击回满血并无敌5s */
	public static final int ATT_INVINCIBLE = 49;
	
	public static final int ATT_REMOVE_DEBUFF = 50;
	
	public static final int ATT_QUANNENGFAJIE = 51;
	
	public static final int ATT_LEI = 100;
	
	//=====================================================
	public int attId;
	public String attName;
	public int[] values;
	public boolean percent;
	/**
	 * 是 ATT_ADD_SUB_BUFF 表示触发buff的级别。
	 */
	public int v;
	/**
	 * 是ATT_APPEND_DAMAGE 表示附加什么类型的伤害。
	 * 是ATT_KanRenXiaCai 表示目标怪物的getSpriteCategoryId
	 * 属性转换（A->B）时表示A
	 * ATT_DMG_SCALE_RATE 表示几率
	 */
	public int paramInt;
	/**
	 * 属性转换（A->B）时表示B
	 * ATT_DMG_SCALE_RATE 表示放到倍数
	 */
	public int paramIntB;
	public GenericBuff next;
	public String srcSkName;
	//
	public BuffCondition triggerCondtion;
	public long lastMS;
}
