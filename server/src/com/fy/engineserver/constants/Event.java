package com.fy.engineserver.constants;

/**
 * 事件常量，用于标记通知的事件类型
 * 
 */
public interface Event {
	/** 升级 **/
	public final static int LEVEL_UPGRADE = 0;
	
	/** 一次性治疗 **/
	public final static int HEAL_ONE_SHOT = 1;
	
	/** 缓疗 **/
	public final static int HEAL_CONTINUOUS = 2;
	
	/** 接受任务 **/
	public final static int TASK_RECEIVED = 3;
	
	/** 完成任务 **/
	public final static int TASK_ACCOMPLISHED = 4;
	
	/** 加血 **/
	public final static int HP_INCREASED = 5;
	
	/** 暴击加血 **/
	public final static int HP_INCREASED_BAOJI = 6;
	
	/** 物理伤害 **/
	public final static int HP_DECREASED_PHYSICAL = 7;
	
	/** 物理暴击伤害 **/
	public final static int HP_DECREASED_PHYSICAL_BAOJI = 8;
	
	/** 法术伤害 **/
	public final static int HP_DECREASED_SPELL = 9;
	
	/** 法术暴击伤害 **/
	public final static int HP_DECREASED_SPELL_BAOJI = 10;
	
	/** 中毒减血 **/
	public final static int HP_DECREASED_ZHONGDU = 11;
	
	/** 中毒减血 **/
	public final static int HP_DECREASED_FANSHANG = 12;

	/** 免疫伤害 **/
	public final static int HP_MIANYI = 13;

	/** 盾吸收伤害 **/
	public final static int HP_XISHOU = 14;
	
	/** 闪避 **/
	public final static int DODGE = 15;
	
	/** 加蓝 **/
	public final static int MP_INCREASED = 16;
	
	/** 暴击加蓝 **/
	public final static int MP_INCREASED_BAOJI = 17;
	
	/** 减蓝 **/
	public final static int MP_DECREASED = 18;
	
	/** 暴击减蓝 **/
	public final static int MP_DECREASED_BAOJI = 19;
	
	/** 获得经验值 **/
	public final static int GAIN_EXPERIENCE = 20;	
	
	/**
	 * 超出切磋范围，多少秒钟后系统会判断输
	 */
	public final static int OUTOF_QIECUO_REGION = 21;
	
	/**
	 * 为命中
	 */
	public final static int MISS = 22;
	
	/**
	 * 技能不能释放
	 */
	public final static int SKILL_ERROR = 23;
	
	
	/** DRAW ICON **/
	public final static int DRAW_ICON = 24;

	/**法宝 升级 **/
	public final static int MW_LEVEL_UPGRADE = 25;

	/**宠物 升级 **/
	public final static int PET_LEVEL_UPGRADE = 26;
	/** 获得道具**/
	public final static int GET_ARTICLE = 27;
	
	/** 打怪获得经验值 **/
	public final static int GAIN_EXPERIENCE_MONSTER = 28;
	
	/** 物理伤害 宠物 **/
	public final static int HP_DECREASED_PHYSICAL_4_PET = 29;
	
	/** 物理暴击伤害 宠物 **/
	public final static int HP_DECREASED_PHYSICAL_BAOJI_4_PET = 30;
	
	/** 法术伤害 宠物 **/
	public final static int HP_DECREASED_SPELL_4_PET = 31;
	
	/** 法术暴击伤害 宠物 **/
	public final static int HP_DECREASED_SPELL_BAOJI_4_PET = 32;
	/** 系统伤害 */
	public final static int RAY_DAMAGE = 33;

}
