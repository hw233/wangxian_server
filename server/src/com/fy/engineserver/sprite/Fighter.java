package com.fy.engineserver.sprite;

import com.fy.engineserver.combat.CombatElement;
import com.fy.engineserver.datasource.buff.Buff;

public interface Fighter extends CombatElement {
	
	//物理伤害
	public static final int DAMAGETYPE_PHYSICAL = 0;
	//法术伤害
	public static final int DAMAGETYPE_SPELL = 1;
	//物理暴击
	public static final int DAMAGETYPE_PHYSICAL_CRITICAL = 2;
	//法术暴击
	public static final int DAMAGETYPE_SPELL_CRITICAL = 3;
	//闪避
	public static final int DAMAGETYPE_DODGE = 4;
	//中毒
	public static final int DAMAGETYPE_ZHAONGDU = 5;
	//返伤
	public static final int DAMAGETYPE_FANSHANG = 6;
	//免疫，无敌状态下
	public static final int DAMAGETYPE_MIANYI = 7;
	//吸收，护盾状态下
	public static final int DAMAGETYPE_XISHOU = 8;
	
	public static final int DAMAGETYPE_MISS = 9;
	
	public static final String DAMAGETYPE_NAMES[] = new String[]{
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		"",
		""
	};
	
	public String getName();
	
	/**
	 * 战斗方的类型，分敌方，中立方，友方
	 * 
	 * @return
	 */
	public static final int FIGHTING_TYPE_ENEMY = 0;
	public static final int FIGHTING_TYPE_NEUTRAL = 1;
	public static final int FIGHTING_TYPE_FRIEND = 2;

	int getLevel();
	
	int getX();

	int getY();

	boolean isStun();
	
	void setStun(boolean b);
	
	boolean isIceState();
	
	boolean isSilence();
	
	boolean isHold();
	
	/**
	 * 是否隐身
	 * @return
	 */
	boolean isObjectOpacity();
	/**
	 * 通知此对象，我准备攻击a
	 * @param a
	 */
	void notifyPrepareToFighting(Fighter a);
	
	/**
	 * 通知此对象，a准备攻击我
	 * 当a为怪的时候，怪将我作为敌人的时候就通知
	 * 当a为玩家的时候，服务器收到有目标攻击指令时通知
	 * @param a
	 */
	void notifyPrepareToBeFighted(Fighter a);
	
	
	/**
	 * 通知此对象，a不再攻击我。
	 * 当a为怪的时候，a不在把我作为攻击目标时通知
	 * 当a为玩家的时候，我从玩家的广播区域里消失，通知
	 * @param a
	 */
	void notifyEndToBeFighted(Fighter a);
	
	/**
	 * 通知此对象，不再准备攻击a.
	 * 
	 * @param a
	 */
	void notifyEndToFighting(Fighter a);
	

	/**
	 * 加生命值，由caster对自己加生命值
	 */
	void enrichHP(Fighter caster, int hp , boolean baoji);

	/**
	 * 给目标加生命值，有自己对别人加生命值
	 */
	void enrichHPFeedback(Fighter target, int hp, boolean baoji);
	
	/**
	 * 造成伤害，由caster造成对自己的伤害
	 * damage为伤害值
	 * damageType为伤害的类型，包括 物理伤害，法术伤害，物理暴击，法术暴击，闪避
	 * @param caster
	 * @param damage
	 * @param damageType
	 */
	void causeDamage(Fighter caster, int damage, int hateParam,int damageType);

	/**
	 * 造成别人伤害，由自己造成对target的伤害
	 * damage为伤害值
	 * damageType为伤害的类型，包括 物理伤害，法术伤害，物理暴击，法术暴击，闪避
	 * @param caster
	 * @param damage
	 * @param damageType
	 */
	void damageFeedback(Fighter target, int damage,int hateParam,int damageType);

	void setState(byte state);

	byte getClassType();

	long getId();

	/**
	 * 判断战斗者是否属于同一个队伍
	 * 
	 * @param fighter
	 * @return
	 */
	boolean isSameTeam(Fighter fighter);
	
	/**
	 * 种植一个buff到玩家或者怪的身上，
	 * 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff);
	
	/**
	 * 通过buff的templateId获得buff
	 * @return
	 */
	public Buff getBuff(int templateId);
	/**
	 * 给定一个fighter，返回是敌方，友方，还是中立方。
	 * 
	 * 0 表示敌方
	 * 1 表示中立方
	 * 2 表示友方
	 * 
	 * @param fighter
	 * @return
	 */
	public int getFightingType(Fighter fighter);
	
	/**
	 * 判断是否死亡，此标记只是标记是否死亡，比如HP = 0
	 * 不同于LivingObject的alive标记。
	 * alive标记用于是否要将生物从游戏中清除。死亡不代表要清除。
	 * @return
	 */
	public boolean isDeath();
	

	/**
	 * 护盾能吸收伤害的值
	 */
	public int getHuDunDamage();
	
	/**
	 * 设置护盾能吸收伤害的值
	 */
	public void setHuDunDamage(int hudun);
	
	
	/**
	 * 判断是否在战场中
	 */
	public boolean isInBattleField();
	
	/**
	 * 获得其在战场中的哪一方，A方还是B方
	 * @return
	 */
	public byte getBattleFieldSide();
	
	/**
	 * 是否能免于收到技能伤害，主要用于安全区
	 * @return
	 */
	public boolean canFreeFromBeDamaged(Fighter fighter);
	
	public byte getSpriteType();
	
	/**
	 * 获取暴击系数，默认200
	 * @return
	 */
	public int getCritFactor();
	/** 获取生物所处状态，根据此状态可以有相应处理，以后根据需求修改此方法  1为中了兽魁噬魂效果 */
	public byte getSpriteStatus(Fighter caster);
}
