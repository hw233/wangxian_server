package com.fy.engineserver.datasource.skill;

import java.util.Arrays;
import java.util.Random;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.articleEnchant.ControlBuff;
import com.fy.engineserver.articleEnchant.EnchantEntityManager;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_FanShang;
import com.fy.engineserver.datasource.buff.BuffTemplate_JiaXue;
import com.fy.engineserver.datasource.buff.BuffTemplate_JianBaoJi;
import com.fy.engineserver.datasource.buff.BuffTemplate_addDamage;
import com.fy.engineserver.datasource.buff.Buff_BaoJi;
import com.fy.engineserver.datasource.buff.Buff_ChenMo;
import com.fy.engineserver.datasource.buff.Buff_CouHenDiYi;
import com.fy.engineserver.datasource.buff.Buff_DingSheng;
import com.fy.engineserver.datasource.buff.Buff_GongQiangFangYu;
import com.fy.engineserver.datasource.buff.Buff_JiaXue;
import com.fy.engineserver.datasource.buff.Buff_JiaXueShangXianBiLi;
import com.fy.engineserver.datasource.buff.Buff_JianBaoJi;
import com.fy.engineserver.datasource.buff.Buff_JianShu;
import com.fy.engineserver.datasource.buff.Buff_LiLiangZhuFu;
import com.fy.engineserver.datasource.buff.Buff_RecoverShield;
import com.fy.engineserver.datasource.buff.Buff_Silence;
import com.fy.engineserver.datasource.buff.Buff_WangZheZhuFu;
import com.fy.engineserver.datasource.buff.Buff_XuanYun;
import com.fy.engineserver.datasource.buff.Buff_XuanYunAndWeak;
import com.fy.engineserver.datasource.buff.Buff_YinShenAndJianShang;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithMatrix;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithSummonNPC;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnConf;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill2.GenericBuffCalc;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.FighterTool;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;
import com.fy.engineserver.talent.FlyTalentManager;

/**
 * 主动技能的抽象类<br>
 * 主动技能的生命周期有三个阶段：僵直、发招、冷却。<br>
 * 其中，前两个阶段有动画，也就有动画风格。<br>
 * 发招阶段有一个或多个出招时间点，出招时间点以发招阶段的起始时间为零时刻！
 * 
 * 0号技能为普通攻击，所有的人默认都有普通攻击
 * 
 * @author Administrator
 * 
 */
public abstract class ActiveSkill extends Skill {

	public static String getDamageTypeName(int damageType) {
		if (damageType < 0 || damageType >= Fighter.DAMAGETYPE_NAMES.length) {
			return "";
		}
		return Fighter.DAMAGETYPE_NAMES[damageType];
	}

	protected static final int HEART_BEATS_PER_SECOND = 5;

	public static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

	/**
	 * 僵直时间，在这段时间内，播放僵直动画，时间为毫秒，
	 * 此时间为0时，不会播放僵直动画
	 */
	private int duration1;

	/**
	 * 攻击时间，这段时间内，播放攻击动画，时间为毫秒
	 * 
	 */
	private int duration2;

	/**
	 * 冷却时间，这段时间内，人物状态会回到站立的状态
	 */
	private int duration3;

	/**
	 * 计算伤害的时间
	 */
	protected int calDamageTime;

	/**
	 * 僵直动画的风格
	 */
	private String style1 = "";

	/**
	 * 攻击动画的风格
	 */
	private String style2 = "";

	/**
	 * 一个或多个出招时间，跟等级无关
	 */
	protected int[] effectiveTimes;

	/**
	 * 是否有武器限制
	 */
	protected byte enableWeaponType = 0;

	/**
	 * 武器具体的限制是什么
	 */
	protected byte weaponTypeLimit = 0;

	/**
	 * 攻击类型，表示物理攻击还是法术攻击
	 * 0 表示物理攻击
	 * 1 表示法术攻击
	 */
	protected int attackType = 0;

	/**
	 * 定义该技能用完之后的后续操作：
	 * 0-无操作
	 * 1-后接普通攻击
	 * 2-继续释放该技能
	 */
	private byte followByCommonAttack;

	/**
	 * 技能释放时忽视眩晕和禁言状态
	 */
	private boolean ignoreStun;

	/**
	 * 人物使用。
	 * 技能伤害或者加血计算公式：
	 * 
	 * 武器伤害 *（param1 + param2 * 技能等级）
	 * + 攻击强度 *（param3 + param4 * 技能等级）
	 * + 法术强度 *（param5 + param6 * 技能等级）
	 * + param7 + param8 * 技能等级
	 */
	protected float param1 = 0;
	protected float param2 = 0;
	protected float param3 = 0;
	protected float param4 = 0;
	protected float param5 = 0;
	protected float param6 = 0;
	protected float param7 = 0;
	protected float param8 = 0;

	/**
	 * 仇恨系数
	 */
	int hateParam = 10;

	/**
	 * 是否激活武器上的buff
	 */
	boolean weaponBuffEnabled;

	/**
	 * 飞行音效
	 */
	protected String flySound = "";

	/**
	 * 爆炸音效
	 */
	protected String explodeSound = "";

	/**
	 * 飞行粒子名数组，数组长度为技能级别，如果只填写一个值的话，所有级别都是这个粒子效果
	 */
	protected String[] flyParticle = new String[0];

	/**
	 * 爆炸粒子名数组，数组长度为技能级别，如果只填写一个值的话，所有级别都是这个粒子效果
	 */
	protected String[] explodeParticle = new String[0];

	/**
	 * 爆炸后效占目标身高比例，基数为100，即如果值为90的话，代表90%
	 */
	public short explodePercent;

	/**
	 * 身上光效粒子名数组，数组长度为技能级别，如果只填写一个值的话，所有级别都是这个粒子效果
	 */
	public String[] bodyParticle = new String[0];

	/**
	 * 身上光效粒子占目标身高比例，基数为100，即如果值为90的话，代表90%
	 */
	public short bodyPercent;

	/**
	 * 身上光效粒子上、下、左、右偏移量
	 */
	public short[] bodyParticleOffset = new short[0];

	/**
	 * 身上粒子播放开始时间，在出招多长时间后开始
	 */
	public long bodyParticlePlayStart;

	/**
	 * 身上粒子播放开始时间，在出招多长时间后结束
	 */
	public long bodyParticlePlayEnd;

	/**
	 * 身上光效部件路径名
	 */
	public String bodyPartPath = "";

	/**
	 * 身上光效动画名
	 */
	public String bodyPartAnimation = "";

	/**
	 * 身上光效动画占目标身高比例，基数为100，即如果值为90的话，代表90%
	 */
	public short bodyPartAnimationPercent;

	/**
	 * 身上光效动画上、下、左、右偏移量
	 */
	public short[] bodyPartAnimationOffset = new short[0];

	/**
	 * 身上动画播放开始时间，在出招多长时间后开始
	 */
	public long bodyPartAnimationPlayStart;

	/**
	 * 身上动画播放开始时间，在出招多长时间后结束
	 */
	public long bodyPartAnimationPlayEnd;

	/**
	 * 脚下光效粒子名数组，数组长度为技能级别，如果只填写一个值的话，所有级别都是这个粒子效果
	 */
	public String[] footParticle = new String[0];

	/**
	 * 脚下光效粒子上、下、左、右偏移量
	 */
	public short[] footParticleOffset = new short[0];

	/**
	 * 脚下粒子播放开始时间，在出招多长时间后开始
	 */
	public long footParticlePlayStart;

	/**
	 * 脚下粒子播放开始时间，在出招多长时间后结束
	 */
	public long footParticlePlayEnd;

	/**
	 * 脚下光效部件路径名
	 */
	public String footPartPath = "";

	/**
	 * 脚下光效动画名
	 */
	public String footPartAnimation = "";

	/**
	 * 脚下光效动画上、下、左、右偏移量
	 */
	public short[] footPartAnimationOffset = new short[0];

	/**
	 * 脚下动画播放开始时间，在出招多长时间后开始
	 */
	public long footPartAnimationPlayStart;

	/**
	 * 脚下动画播放开始时间，在出招多长时间后结束
	 */
	public long footPartAnimationPlayEnd;

	/**
	 * 目标点脚下光效粒子名数组，数组长度为技能级别，如果只填写一个值的话，所有级别都是这个粒子效果
	 */
	public String[] targetFootParticle = new String[0];

	/**
	 * 是否挂机技能
	 */
	public boolean guajiFlag;

	/**
	 * 角度（落雷技能从上往下的角度，直接落下为0度，水平为90度，有轨迹技能如火球类这个值不起作用）
	 */
	public short angle;

	public short getAngle() {
		return angle;
	}

	public void setAngle(short angle) {
		this.angle = angle;
	}

	public String getFlySound() {
		return flySound;
	}

	public void setFlySound(String flySound) {
		this.flySound = flySound;
	}

	public String getExplodeSound() {
		return explodeSound;
	}

	public void setExplodeSound(String explodeSound) {
		this.explodeSound = explodeSound;
	}

	public String[] getFlyParticle() {
		return flyParticle;
	}

	public void setFlyParticle(String[] flyParticle) {
		this.flyParticle = flyParticle;
	}

	public String[] getExplodeParticle() {
		return explodeParticle;
	}

	public void setExplodeParticle(String[] explodeParticle) {
		this.explodeParticle = explodeParticle;
	}

	public short getExplodePercent() {
		return explodePercent;
	}

	public void setExplodePercent(short explodePercent) {
		this.explodePercent = explodePercent;
	}

	public String[] getBodyParticle() {
		return bodyParticle;
	}

	public void setBodyParticle(String[] bodyParticle) {
		this.bodyParticle = bodyParticle;
	}

	public short getBodyPercent() {
		return bodyPercent;
	}

	public void setBodyPercent(short bodyPercent) {
		this.bodyPercent = bodyPercent;
	}

	public short[] getBodyParticleOffset() {
		return bodyParticleOffset;
	}

	public void setBodyParticleOffset(short[] bodyParticleOffset) {
		this.bodyParticleOffset = bodyParticleOffset;
	}

	public long getBodyParticlePlayStart() {
		return bodyParticlePlayStart;
	}

	public void setBodyParticlePlayStart(long bodyParticlePlayStart) {
		this.bodyParticlePlayStart = bodyParticlePlayStart;
	}

	public long getBodyParticlePlayEnd() {
		return bodyParticlePlayEnd;
	}

	public void setBodyParticlePlayEnd(long bodyParticlePlayEnd) {
		this.bodyParticlePlayEnd = bodyParticlePlayEnd;
	}

	public String getBodyPartPath() {
		return bodyPartPath;
	}

	public void setBodyPartPath(String bodyPartPath) {
		this.bodyPartPath = bodyPartPath;
	}

	public String getBodyPartAnimation() {
		return bodyPartAnimation;
	}

	public void setBodyPartAnimation(String bodyPartAnimation) {
		this.bodyPartAnimation = bodyPartAnimation;
	}

	public short getBodyPartAnimationPercent() {
		return bodyPartAnimationPercent;
	}

	public void setBodyPartAnimationPercent(short bodyPartAnimationPercent) {
		this.bodyPartAnimationPercent = bodyPartAnimationPercent;
	}

	public short[] getBodyPartAnimationOffset() {
		return bodyPartAnimationOffset;
	}

	public void setBodyPartAnimationOffset(short[] bodyPartAnimationOffset) {
		this.bodyPartAnimationOffset = bodyPartAnimationOffset;
	}

	public long getBodyPartAnimationPlayStart() {
		return bodyPartAnimationPlayStart;
	}

	public void setBodyPartAnimationPlayStart(long bodyPartAnimationPlayStart) {
		this.bodyPartAnimationPlayStart = bodyPartAnimationPlayStart;
	}

	public long getBodyPartAnimationPlayEnd() {
		return bodyPartAnimationPlayEnd;
	}

	public void setBodyPartAnimationPlayEnd(long bodyPartAnimationPlayEnd) {
		this.bodyPartAnimationPlayEnd = bodyPartAnimationPlayEnd;
	}

	public String[] getFootParticle() {
		return footParticle;
	}

	public void setFootParticle(String[] footParticle) {
		this.footParticle = footParticle;
	}

	public short[] getFootParticleOffset() {
		return footParticleOffset;
	}

	public void setFootParticleOffset(short[] footParticleOffset) {
		this.footParticleOffset = footParticleOffset;
	}

	public long getFootParticlePlayStart() {
		return footParticlePlayStart;
	}

	public void setFootParticlePlayStart(long footParticlePlayStart) {
		this.footParticlePlayStart = footParticlePlayStart;
	}

	public long getFootParticlePlayEnd() {
		return footParticlePlayEnd;
	}

	public void setFootParticlePlayEnd(long footParticlePlayEnd) {
		this.footParticlePlayEnd = footParticlePlayEnd;
	}

	public String getFootPartPath() {
		return footPartPath;
	}

	public void setFootPartPath(String footPartPath) {
		this.footPartPath = footPartPath;
	}

	public String getFootPartAnimation() {
		return footPartAnimation;
	}

	public void setFootPartAnimation(String footPartAnimation) {
		this.footPartAnimation = footPartAnimation;
	}

	public short[] getFootPartAnimationOffset() {
		return footPartAnimationOffset;
	}

	public void setFootPartAnimationOffset(short[] footPartAnimationOffset) {
		this.footPartAnimationOffset = footPartAnimationOffset;
	}

	public long getFootPartAnimationPlayStart() {
		return footPartAnimationPlayStart;
	}

	public void setFootPartAnimationPlayStart(long footPartAnimationPlayStart) {
		this.footPartAnimationPlayStart = footPartAnimationPlayStart;
	}

	public long getFootPartAnimationPlayEnd() {
		return footPartAnimationPlayEnd;
	}

	public void setFootPartAnimationPlayEnd(long footPartAnimationPlayEnd) {
		this.footPartAnimationPlayEnd = footPartAnimationPlayEnd;
	}

	public boolean isWeaponBuffEnabled() {
		return weaponBuffEnabled;
	}

	public void setWeaponBuffEnabled(boolean weaponBuffEnabled) {
		this.weaponBuffEnabled = weaponBuffEnabled;
	}

	public int getHateParam() {
		return hateParam;
	}

	public void setHateParam(int hateParam) {
		this.hateParam = hateParam;
	}

	public float getParam1() {
		return param1;
	}

	public void setParam1(float param1) {
		this.param1 = param1;
	}

	public float getParam2() {
		return param2;
	}

	public void setParam2(float param2) {
		this.param2 = param2;
	}

	public float getParam3() {
		return param3;
	}

	public void setParam3(float param3) {
		this.param3 = param3;
	}

	public float getParam4() {
		return param4;
	}

	public void setParam4(float param4) {
		this.param4 = param4;
	}

	public float getParam5() {
		return param5;
	}

	public void setParam5(float param5) {
		this.param5 = param5;
	}

	public float getParam6() {
		return param6;
	}

	public void setParam6(float param6) {
		this.param6 = param6;
	}

	public float getParam7() {
		return param7;
	}

	public void setParam7(float param7) {
		this.param7 = param7;
	}

	public float getParam8() {
		return param8;
	}

	public void setParam8(float param8) {
		this.param8 = param8;
	}

	public byte getFollowByCommonAttack() {
		return followByCommonAttack;
	}

	public void setFollowByCommonAttack(byte followByCommonAttack) {
		this.followByCommonAttack = followByCommonAttack;
	}

	public boolean isIgnoreStun() {
		return ignoreStun;
	}

	/**
	 * 服务器判断，是否忽略眩晕
	 * @return
	 */
	public boolean isLostStun(Fighter f) {
		if (f instanceof Player == false) {
			return false;
		}
		Player p = (Player) f;
		return p.isStunServer;
	}

	public void setIgnoreStun(boolean ignoreStun) {
		this.ignoreStun = ignoreStun;
	}

	// ////////////////////////////////////////////////////////////////////////////////
	// 服务器端的Buff
	// ////////////////////////////////////////////////////////////////////////////////
	/**
	 * buff作用的对象：
	 * 
	 * 0 表示作用在目标身上
	 * 1 表示作用在施法者身上，
	 * 
	 * 此属性对各个级别的技能有效。
	 */
	protected int buffTargetType = 0;

	/**
	 * 每一个级别的技能，使用的Buff的名称，
	 * 此名称必须索引到一个存在的Buff
	 */
	protected String buffName = "";

	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff的级别，用于表示buff的威力
	 */
	protected int buffLevel[];

	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff持续的时间
	 */
	protected long buffLastingTime[];

	/**
	 * 每一个级别的技能，通过buffName确定要使用的Buff后，
	 * 指定这个buff产生的概率
	 */
	protected int buffProbability[];

	/**
	 * 技能的伤害，如果这个技能为怪物使用的技能，
	 * 就采用这个伤害
	 */
	protected int[] attackDamages;

	public static final int 没有状态加成 = 0;
	public static final int 定身2倍暴击 = 1;
	public static final int 定身3倍暴击 = 2;
	public static final int 眩晕2倍暴击 = 3;
	public static final int 眩晕3倍暴击 = 4;

	/**
	 * 状态伤害加成类型
	 */
	protected int stateIncreaseType = 0;

	/**
	 * 是否为怒气技能，一般情况下技能消耗为mp，但如果是怒气技能的话，对玩家而言直接消耗完怒气释放技能
	 */
	protected boolean nuqiFlag;
	/** 针对兽魁某些技能设定，有此标志位需要判断增加或减少豆 */
	private boolean douFlag;
	/** 消耗或增加豆数量，正数为加，负数为减 */
	private int pointNum;
	/** 是否根据玩家所处状态触发特殊效果技能 */
	private boolean specialSkillFlag;
	/** 触发特殊效果需要玩家状态 */
	private byte needStatus;
	/** 额外增加伤害比例 */
	private long extraDmgRate = 0;
	/** 吸血比例 */
	private long recoverHpRate;
	/** 传播buff名 */
	private String spreadBuffName = null;
	/** 是否为变身按钮(兽魁专用，需要判断玩家当前形态，人变兽时需要记录时间判断cd，兽变人不记录时间且无cd) */
	private boolean bianshenBtn;
	/** 是否为为变身技能，是变身技能则人形态无法使用，不是则兽形态无法使用 */
	private boolean isBianshenSkill = false;

	/**
	 * 传播buff
	 * @param caster
	 * @param target
	 * @param mainId
	 * @param buffName
	 * @return
	 */
	public String spreadBuff(Fighter caster, Fighter target, Fighter mainId, String buffName, int buffLevel) {
		try {
			if (target == null || caster == null) {
				return "被攻击者或攻击者为null";
			}
			if (target == mainId) {
				return "被攻击者为主传播者";
			}
			if (target != null && target.getHp() <= 0) {
				return "被攻击者已死";
			}
			if (target instanceof Player && caster instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
				Player p = (Player) target;
				if (p.getCountry() == p.getCurrentGameCountry()) {
					return "被攻击者级别太低";
				}
			}
			if (target instanceof Player && caster instanceof Pet && target.getLevel() <= PlayerManager.保护最大级别) {
				Player p = (Player) target;
				if (p.getCountry() == p.getCurrentGameCountry()) {
					return "被攻击者级别太低";
				}
			}
			if (target.canFreeFromBeDamaged(caster)) {
				return "被攻击者在安全区";
			}
			if (caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
				return "被攻击者不是敌人";
			}
			BuffTemplateManager btm = BuffTemplateManager.getInstance();
			BuffTemplate bt = btm.getBuffTemplateByName(buffName);
			if (bt != null) {
				Buff buff = bt.createBuff(buffLevel - 1);
				buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 15 * 1000);
				buff.setCauser(caster);
				target.placeBuff(buff);
				//HunshiManager.getInstance().dealWithInfectSkill(caster, target, buff);
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[传播buff] [buff为空] [buffName:" + buffName + "]");
				}
			}
			return null;
		} catch (Exception e) {
			logger.warn("[异常]", e);
			return "异常";
		}
	}

	public int getStateIncreaseType() {
		return stateIncreaseType;
	}

	public void setStateIncreaseType(int stateIncreaseType) {
		this.stateIncreaseType = stateIncreaseType;
	}

	public String getBuffName() {
		return buffName;
	}

	public void setBuffName(String buffName) {
		this.buffName = buffName;
	}

	public int[] getAttackDamages() {
		return attackDamages;
	}

	public void setAttackDamages(int[] attackDamages) {
		this.attackDamages = attackDamages;
	}

	/**
	 * 返回技能的类型
	 * @return
	 */
	public byte getSkillType() {
		return Skill.SKILL_ACTIVE;
	}

	public int getBuffTargetType() {
		return buffTargetType;
	}

	public void setBuffTargetType(int buffTargetType) {
		this.buffTargetType = buffTargetType;
	}

	public int[] getBuffLevel() {
		return buffLevel;
	}

	public void setBuffLevel(int[] buffLevel) {
		this.buffLevel = buffLevel;
	}

	public long[] getBuffLastingTime() {
		return buffLastingTime;
	}

	public void setBuffLastingTime(long[] buffLastingTime) {
		this.buffLastingTime = buffLastingTime;
	}

	public int getAttackType() {
		return attackType;
	}

	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	public byte getEnableWeaponType() {
		return enableWeaponType;
	}

	public void setEnableWeaponType(byte enableWeaponType) {
		this.enableWeaponType = enableWeaponType;
	}

	public byte getWeaponTypeLimit() {
		return weaponTypeLimit;
	}

	public void setWeaponTypeLimit(byte weaponTypeLimit) {
		this.weaponTypeLimit = weaponTypeLimit;
	}

	public boolean isNuqiFlag() {
		return nuqiFlag;
	}

	public void setNuqiFlag(boolean nuqiFlag) {
		this.nuqiFlag = nuqiFlag;
	}

	public String[] getTargetFootParticle() {
		return targetFootParticle;
	}

	public void setTargetFootParticle(String[] targetFootParticle) {
		this.targetFootParticle = targetFootParticle;
	}

	/**
	 * 
	 * @param p
	 * @param level
	 * @return 增加或者消耗都的数量，正数为增加负数为消耗
	 */
	public int calculateDou(Player p, int level) {
		if (!douFlag || p.getCareer() != 5) { // 只有兽魁需要考虑豆的消耗增加问题
			return 0;
		}
		return pointNum;
	}

	// public CompoundReturn getSpecial

	/**
	 * 计算玩家使用此技能需要消耗的内力值
	 * @param p
	 * @param level
	 * @return
	 */
	public int calculateMp(Player p, int level) {
		short mp[] = getMp();
		if (mp != null && mp.length >= level) {
			ActiveSkillParam param = p.getActiveSkillParam(this);
			int aa = 0;
			if (param != null) {
				aa = param.getMp();
			}
			int x = 0;
			if (level > 0) x = mp[level - 1];
			else x = mp[level];

			if (aa < 0) aa = 0;
			if (aa > 100) aa = 100;

			x = x * (100 - aa) / 100;

			return x;
		} else {
			return 0;
		}
	}

	/**
	 * 僵直阶段开始，回调函数
	 * 当僵直阶段开始的时候，系统会调用此方法
	 */
	public void start(ActiveSkillEntity ase) {

	}

	/**
	 * 僵直阶段结束，攻击阶段开始
	 * 当僵直阶段结束，攻击阶段开始的时候，系统会调用此方法
	 */
	public void duration2Start(ActiveSkillEntity ase, Game game) {
	}

	/**
	 * 攻击阶段结束，冷却时间开始，会调用此方法
	 */
	public void duration3Start(ActiveSkillEntity ase, Game game) {

	}

	/**
	 * 技能结束，会调用此方法
	 * 包括在僵直阶段被取消的情况
	 */
	public void end(ActiveSkillEntity ase) {

	}

	/**
	 * 获得这个技能的伤害或者补血量
	 * @param f
	 *            攻击者
	 * @return
	 */
	/**
	 * @param f
	 * @param level
	 * @return
	 */
	public int calDamageOrHpForFighter(Fighter f, int level) {
		int damage = 0;

		// if(this instanceof CommonAttackSkill){
		// //普通攻击计算外功或法术攻击，不计算技能伤害
		// return 0;
		// }
		if (f instanceof Pet) {
			Pet p = (Pet) f;
			if (logger.isDebugEnabled()) {
				logger.debug("宠物 {}", p.getName());
				logger.debug("p.getWeaponDamage() {}", p.getWeaponDamage());
				logger.debug("param1 {}", param1);
				logger.debug("param2 {}", param2);
				logger.debug("level {}", level);
				logger.debug("p.getPhyAttack() {}", p.getPhyAttack());
				logger.debug("param3 {}", param3);
				logger.debug("param4 {}", param4);
				logger.debug("p.getMagicAttack() {}", p.getMagicAttack());
				logger.debug("param7 {}", param7);
				logger.debug("param8 {}", param8);
				// logger.debug(" {}",);
				// logger.debug(" {}",);
			}
			int ud = (int) (p.getWeaponDamage() * (param1 + param2 * level) + p.getPhyAttack() * (param3 + param4 * level) + p.getMagicAttack() * (param5 + param6 * level) + param7 + param8 * level);
			logger.debug("pet udA {}", ud);

			if (effectiveTimes != null && effectiveTimes.length > 1) {
				ud = ud / effectiveTimes.length;
			}
			logger.debug("pet udB {}", ud);

			int d1 = (int) (ud * (1 + 0.05));
			int d2 = (int) (ud * (1 - 0.05));

			if (d1 == d2) {
				damage = d1;
			} else {
				damage = d2 + random.nextInt(d1 - d2);
			}
			float tempf = (100f + p.getSkillDamageIncreaseRate()) / 100f;
			damage = (int) (damage * tempf);
			
			logger.debug("pet damage {}", damage);
			return damage;
		} else {
			if (f instanceof Player) {
				Player p = (Player) f;

				int aa = 0;
				ActiveSkillParam param = p.getActiveSkillParam(this);
				if (param != null) {
					aa = param.getAttackPercent();
				}

				int ud = 1;
				if (attackDamages != null && attackDamages.length > 0) {
					ud = this.attackDamages[level - 1];
				}
				int addDiv = 0;
				if (ud > 0 && pageIndex >= 0 && p.getLevel() >= TransitRobberyManager.openLevel && SkEnhanceManager.open) {
					SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
					if (bean != null && bean.useLevels != null && bean.useLevels[pageIndex] > 0) {
						int lv = bean.useLevels[pageIndex];
						int point = SkEnConf.conf[p.getCareer() - 1][pageIndex].levelAddPoint[lv - 1];
						ud += point;
						if (id == 702 && lv >= 10) {
							addDiv = lv / 10;
						}
						if (logger.isDebugEnabled()) {
							logger.debug("ActiveSkill.calDamageOrHpForFighter: 触发大师技能 {} lv {}", getName(), lv);
							logger.debug("ActiveSkill.calDamageOrHpForFighter: 点数 {}", point);
						}
					}
				}
				switch (id) {
				case 912:// 战血 造成$mCnt倍外攻伤害 DL
				case 913:// 万鬼窟 造成$mCnt倍外攻伤害 GS
					ud = (level == 1 ? 2 : 3) * p.getPhyAttack();
					logger.debug("ActiveSkill.calDamageOrHpForFighter: 触发禁忌技能 外攻 {} lv {}", name, level);
					break;
				case 914:// 冰川时代 造成$mCnt倍内法伤害 LZ
				case 915:// 九黎之怒 造成$mCnt倍内攻伤害 WH
					ud = (level == 1 ? 2 : 3) * p.getMagicAttack();
					logger.debug("ActiveSkill.calDamageOrHpForFighter: 触发禁忌技能 内攻 {} lv {}", name, level);
					break;
				case 803:// 仙心 803
					ud += SkEnhanceManager.getInst().fixLingZun803(ud, p, this);
					break;
				case 21007:
					ud += SkEnhanceManager.getInst().fixLingZun21007(ud, p, this);
					break;
				case 916:// 兽魁 兽神狂怒
					ud = (level == 1 ? 2 : 3) * p.getMagicAttack();
					break;
				}

				if (effectiveTimes != null && effectiveTimes.length > 1) {
					ud = ud / (effectiveTimes.length + addDiv);
				}

				int d1 = (int) (ud * (1 + 0.05));
				int d2 = (int) (ud * (1 - 0.05));

				if (d1 == d2) {
					damage = d1;
				} else {
					damage = d2 + random.nextInt(d1 - d2);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[ud:" + ud + "] [damage:" + damage + "] [d1:" + d1 + "] [d2:" + d2 + "] [aa:" + aa + "]");
				}
				float tempF = (100 + aa) / 100f;
				damage = (int) (damage * tempF);
				// damage = damage * (100 + aa) / 100;

				// damage = damage * (100 + p.getSkillDamageIncreaseRate())/100;

				return damage;

			} else if (f instanceof Sprite) {
				Sprite p = (Sprite) f;

				int ud = 1;
				if (attackDamages != null && attackDamages.length > 0) {
					ud = this.attackDamages[level - 1];
				}

				if (effectiveTimes != null && effectiveTimes.length > 1) {
					ud = ud / effectiveTimes.length;
				}

				int d1 = (int) (ud * (1 + 0.05));
				int d2 = (int) (ud * (1 - 0.05));

				if (d1 == d2) {
					damage = d1;
				} else {
					damage = d2 + random.nextInt(d1 - d2);
				}
				logger.debug("[技能检查] [" + this.getName() + "_" + this.getId() + "] [" + (attackDamages == null ? null : Arrays.toString(attackDamages)) + "] [damage:" + damage + "]");

				float tempF = (100 + p.getSkillDamageIncreaseRate()) / 100f;
				damage = (int) (damage * tempF);
				// damage = damage * (100 + p.getSkillDamageIncreaseRate()) / 100;
				return damage;
			}
		}
		return damage;
	}

	/**
	 * 主动技能发出的后效击中目标时调用此方法
	 * 
	 * @param caster
	 *            施法者
	 * @param target
	 *            被击中的目标
	 * @param level
	 *            技能等级，从1开始
	 * @param effectIndex
	 *            第几次出招，针对有多个出招时间的情况
	 */
	public void hit(Fighter caster, Fighter target, int level, int effectIndex) {
		if (target != null && target.getHp() <= 0) {
			return;
		}
		// if(this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix) {
		if(caster instanceof Player){
			HunshiManager.getInstance().getDamage((Player)caster, target,!(this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix));
		}

		if (target instanceof Player && caster instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
			Player p = (Player) target;
			if (p.getCountry() == p.getCurrentGameCountry()) {
				return;
			}
		}

		if (target instanceof Player && caster instanceof Pet && target.getLevel() <= PlayerManager.保护最大级别) {
			Player p = (Player) target;
			if (p.getCountry() == p.getCurrentGameCountry()) {
				return;
			}
		}
		logger.debug("ActiveSkill hit : caster {}", caster.getName());
		if (target.canFreeFromBeDamaged(caster)) {
			return;
		}

		if (level <= 0) {
			logger.debug("ActiveSkill hit : skill level <=0 skName{}", name);
			return;
		}

		if (caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
			if (logger.isInfoEnabled()) logger.info("[技能命中计算] [错误，非敌对两个玩家不能攻击] [{}] [{}] [{}]", new Object[] { this.getName(), caster.getName(), target.getName() });
			return;
		}

		boolean isHit = CombatCaculator.isHit(caster);
		boolean isDodge = CombatCaculator.isDodge(caster, 0, target, 0);
		logger.debug("ActiveSkill[hit:" + isHit + "], {isDodge:" + isDodge + "] [攻击者:" + caster.getId() + "] [目标:" + target.getName() + "]");
		logger.debug("caster {} target {}", caster.getClass().getSimpleName(), target.getClass().getSimpleName());
		if (caster instanceof Pet) {
			if (((Pet) caster).talent1Skill == 110119 || ((Pet) caster).talent2Skill == 110119) { // 宠物攻击必中
				isHit = true;
				isDodge = false;
				if (logger.isDebugEnabled()) {
					logger.debug("[宠物攻击] [触发必中效果] [" + caster.getId() + "]");
				}
			}
		}
		if (isHit && !isDodge) {
			if (caster.getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY) {
				if (target instanceof Player && ((Player) target).isIceState()) {
					if (logger.isInfoEnabled()) logger.info("[技能命中计算] [错误，目标处于冰冻状态] [{}] [{}] [{}]", new Object[] { this.getName(), caster.getName(), target.getName() });
					return;
				}
				fireBuff(caster, target, level, effectIndex);
				
			}

			try {
				if (caster instanceof Player) {
					SkEnhanceManager.getInst().fixSkEnStep((Player) caster, target, this, -1);
				}
			} catch (Exception e) {
				logger.error("ActiveSkill.hit: fixSkEnStep error.", e);
			}
		}
		if (caster != null) {
			if (caster instanceof Player) {
				Player p = (Player) caster;
				if (this.getId() == 714) {
					if (target != null) {
						if (target instanceof BossMonster == false) {
							SkEnhanceManager.getInst().checkWuHuang714(p, target, this);// 714降头 人阶10重:获得所有技能1%使目标流血，每秒减少1%生命上限血量伤害持续5秒
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[降头技能是否有效判断] [对boss无效] [boss:" + target.getName() + "] [skill:" + this.getId() + "] [" + p.getLogString() + "]");
							}
						}
					}
				}
			}
		}
		if (this.getId() == 712 && caster != null && caster instanceof Player) {
			SkEnhanceManager.getInst().checkLingZun712((Player) caster, target);
		}
		if (this.getId() == 9034 && caster != null && caster instanceof Player) {
			SkEnhanceManager.getInst().checkShoukui9034((Player) caster, target);
		}

		int damage = calDamageOrHpForFighter(caster, level);

		
		if (this.getId() == 710 && caster != null && caster instanceof Player) {
			damage += SkEnhanceManager.getInst().checkWuHuang710((Player) caster, target, this);
		}

		if (this.getId() == 9044 && caster != null && caster instanceof Player) {
			damage += SkEnhanceManager.getInst().checkWuHuang9044((Player) caster, target, this, damage);
		}
		int adddamage = 0;
		if (this.getId() == 18012 && caster != null && caster instanceof Player) {
			adddamage = SkEnhanceManager.getInst().checkShouKuiSkill18012((Player) caster, target, damage, this);
			damage += adddamage;
		}
		if (this.getId() == 9113 && caster != null) {
			adddamage = SkEnhanceManager.getInst().checkShouKuiSkill9113((Player) caster, target, damage, this);
			damage += adddamage;
		}

		logger.debug("ActiveSkill hit : skill name , [name:" + name + "] [damage:" + damage + "] [adddamage:" + adddamage + "]");

		if (caster instanceof Player) {
			((Player) caster).checkPassiveEnchant(EnchantEntityManager.主动攻击);
		}
		
		// 如果技能没有伤害，那么就认为这个技能不造成伤害，如只加buff的技能，无需进入人物属性伤害计算
		if (this instanceof CommonAttackSkill || damage != 0) {
			// by 康建虎 2013年9月4日11:14:22 ，第二版宠物功能中增强了技能，天生技能的buff会《每次攻击附带1000点雷属性伤害》
			// 将散落在各处的causeDamage、damageFeedback统一的一处去调用，使用dmgType来控制是否调用。
			// logger.debug("ActiveSkill hit : 3");
			final int noFeedBack = -1000;
			int dmgType = noFeedBack;
			if (!isHit) {
				// 闪避
				dmgType = Fighter.DAMAGETYPE_MISS;
				damage = 0;
				// target.causeDamage(caster, 0, hateParam,Fighter.DAMAGETYPE_MISS);
				// caster.damageFeedback(target, 0, hateParam,Fighter.DAMAGETYPE_MISS);

				if (logger.isInfoEnabled()) {
					logger.info("ActiveSkill hit 3-1: {}  skill miss  {}", caster.getName(), name);
				}
			} else if (isDodge) {
				// 闪避
				dmgType = Fighter.DAMAGETYPE_DODGE;
				damage = 0;
				// target.causeDamage(caster, 0, hateParam,Fighter.DAMAGETYPE_DODGE);
				// caster.damageFeedback(target, 0, hateParam,Fighter.DAMAGETYPE_DODGE);

				if (logger.isInfoEnabled()) {
					logger.info("ActiveSkill hit 3-2: dodge");
				}
			} else {
				// logger.debug("ActiveSkill hit : 4");
				// 调用caculateDamage方法计算出了技能伤害外的其他伤害值
				int attackerCareer = 0;
				Player casterP = null;
				Player targetP = null;
				if (caster instanceof Player) {
					casterP = (Player) caster;
					attackerCareer = casterP.getCareer();
				} else if (caster instanceof Sprite) {
					attackerCareer = ((Sprite) caster).getCareer();
				}
				int defenderCareer = 0;
				if (target instanceof Player) {
					targetP = ((Player) target);
					defenderCareer = targetP.getCareer();
				} else if (target instanceof Sprite) {
					defenderCareer = ((Sprite) target).getCareer();
				}

				damage = CombatCaculator.caculateDamage(caster, attackerCareer, target, defenderCareer, calDamageTime, this.attackType == 0, damage);
				
				// if (id == 710 && casterP != null) {// 影魅 710 断喉 人阶10重:获得断喉技能有2%触发双倍伤害
				// int step = SkEnhanceManager.getInst().getSlotStep(casterP, pageIndex);
				// step = step >= 3 ? 5 : step + 1;
				// int r = random.nextInt(100);
				// if (r < step) {
				// damage += damage;
				// logger.debug("ActiveSkill.hit:影魅710断喉 大阶段触发双倍伤害");
				// }
				// } else

				damage += BossCityManager.getInstance().addGuLiDamageOfYuanZheng(caster, damage);
				
				if (id == 21005 && casterP != null) {// 影魅 21005 碎影 人阶10重:获得碎影技能伤害提升5% 10,15
					int step = SkEnhanceManager.getInst().getSlotStep(casterP, pageIndex);
					step = step * 5;
					if (step > 0) {
						float f = step / 100f;
						damage += (int) damage * f;
						logger.debug("ActiveSkill.hit: 影魅21005碎影 scale {}%", step);
					}
				} else if (id == 702 && casterP != null) {
					int step = SkEnhanceManager.getInst().getSlotStep(casterP, pageIndex);
					if (step > 0) {
						damage = (int) ((damage) * Math.pow(0.9F, step));
						logger.debug("修罗 双舞 修正 {}", damage);
					}
				} else if (id == 709 && casterP != null) {
					/** 血毒技能伤害提升10%，15%，20% **/
					int step = SkEnhanceManager.getInst().getSlotStep(casterP, pageIndex);
					int moreDamage = 0;
					if (step > 0) {
						if (step == 1) {
							moreDamage = (int) (damage * 0.1);
						} else if (step == 2) {
							moreDamage = (int) (damage * 0.15);
						} else if (step == 3) {
							moreDamage = (int) (damage * 0.2);
						}
						damage += moreDamage;
						logger.debug("影魅 《血毒》 改动 {} 额外伤害{} 施法者{} 目标{}", new Object[] { damage, moreDamage, (casterP == null ? "null" : casterP.getName()), (targetP == null ? "null" : targetP.getName()) });
					}
				}

				// 计算暴击

				int fightType = Fighter.DAMAGETYPE_PHYSICAL;
				if (CombatCaculator.isCriticalHit(caster, target)) {
					int critFactor = caster.getCritFactor();
					int olddamage = damage;
					if (critFactor == 200) {
						damage += damage;
					} else {
						logger.debug("special crit factor {} of {}", critFactor, caster.getName());
						float f = critFactor / 100f;
						damage = (int) (damage * f);
					}
					fightType = Fighter.DAMAGETYPE_PHYSICAL_CRITICAL;
					try {
						if (caster instanceof Player) {
							SkEnhanceManager.getInst().fixSkEnStep((Player) caster, target, this, fightType);
							logger.error("[计算物理暴击] [成功] [caster：" + caster.getName() + "] [target:" + target.getName() + "] [fightType:" + fightType + "] [伤害:" + olddamage + "-->" + damage + "]");
						}
					} catch (Exception e) {
						logger.error("[计算物理暴击] [异常] [caster：" + caster.getName() + "] [target:" + target.getName() + "] [fightType:" + fightType + "] [伤害:" + olddamage + "-->" + damage + "]", e);
					}
					logger.debug("ActiveSkill hit : 5 CRITICAL");
				}
				dmgType = fightType;

				logger.debug("ActiveSkill hit : 6 damage {}", damage);

				{// 影魅 轮刺 获得狼印状态下吸取伤害值的1%转为法力值
					if (casterP != null && casterP.getCareer() == 2) {
						Buff b = casterP.getBuffByName(Translate.狼印);
						if (b != null) {
							logger.debug("影魅 《轮刺》 [" + casterP.getName() + "] [2] [" + b.getDescription() + "]");
							int step = SkEnhanceManager.getInst().getSlotStep(casterP, 2);
							int moreMp = 0;
							if (step > 0) {
								if (step == 1) {
									moreMp = (int) (damage * 0.01);
								} else if (step == 2) {
									moreMp = (int) (damage * 0.02);
								} else if (step == 3) {
									moreMp = (int) (damage * 0.03);
								}
							}
							if (moreMp > 0) {
								casterP.setMp(casterP.getMp() + moreMp);
								// 加MP，通知客户端
								NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, casterP.getId(), (byte) Event.MP_INCREASED, moreMp);
								casterP.addMessageToRightBag(req);

								logger.debug("影魅 《轮刺》 伤害{} 新法力值 {} 增加额外法力值{} 施法者{} 目标{}", new Object[] { damage, (casterP == null ? "" : casterP.getMp()), moreMp, (casterP == null ? "" : casterP.getName()), (target == null ? "" : target.getName()) });
							}
						}
					}
				}

				{// 仙心 坠天 获得灵印状态下使伤害值的1%转换成法力值
					if (casterP != null && casterP.getCareer() == 3) {
						Buff b = casterP.getBuffByName(Translate.灵印);
						if (b != null) {
							int step = SkEnhanceManager.getInst().getSlotStep(casterP, 5);
							int moreMp = 0;
							if (step > 0) {
								if (step == 1) {
									moreMp = (int) (damage * 0.01);
								} else if (step == 2) {
									moreMp = (int) (damage * 0.02);
								} else if (step == 3) {
									moreMp = (int) (damage * 0.03);
								}
							}
							logger.debug("仙心 《坠天》 伤害{} 新法力值 {} 增加额外法力值{} 施法者{} 目标{} 技能{} step{}", new Object[] { damage, (casterP == null ? "" : casterP.getMp()), moreMp, (casterP == null ? "" : casterP.getName()), (target == null ? "" : target.getName()), this.getName(), step });
							if (moreMp > 0) {
								casterP.setMp(casterP.getMp() + moreMp);
								NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, casterP.getId(), (byte) Event.MP_INCREASED, moreMp);
								casterP.addMessageToRightBag(req);
							}
						}
					}
				}

				{// 兽魁 吞噬 激活永久提升吞噬回血10%
					if (casterP != null && casterP.getCareer() == 5 && this.getId() == 9033) {
						int moreHP = SkEnhanceManager.getInst().checkShouKuiSkill9033(casterP, damage);
						logger.debug("兽魁 《吞噬》 伤害{} 新血量值 {} 增加额外血量值{} 施法者{} 目标{} 技能{} ", new Object[] { damage, (casterP == null ? "" : casterP.getMp()), moreHP, (casterP == null ? "" : casterP.getName()), (target == null ? "" : target.getName()), this.getName() });
						if (moreHP > 0) {
							if (!casterP.isCanNotIncHp()) {
								casterP.setHp(casterP.getHp() + moreHP);
								NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, casterP.getId(), (byte) Event.HP_INCREASED, moreHP);
								casterP.addMessageToRightBag(req);
							} else {
								if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[无法回血状态] [屏蔽兽魁吞噬] [" + casterP.getLogString() + "] [血]");
							}
						}
					}
				}

				try {
					if (casterP != null) {
						damage = FlyTalentManager.getInstance().handle_仙婴悟攻(casterP, damage);
						damage = FlyTalentManager.getInstance().handle_暴燃(casterP, damage);
						damage = FlyTalentManager.getInstance().handle_仙婴固化(targetP, damage);
					}

					if (caster instanceof Pet) {
						damage = FlyTalentManager.getInstance().handle_猛兽(((Pet) caster), damage);
						damage = FlyTalentManager.getInstance().handle_仙婴兽化(((Pet) caster), damage);
						damage = FlyTalentManager.getInstance().handle_强化猛兽(((Pet) caster), damage);
						damage = FlyTalentManager.getInstance().handle_会心(((Pet) caster), damage);
					}
				} catch (Exception e) {
					logger.warn("[计算天赋技能] [异常] [" + e + "]");
				}
					
				{// 新反噬技能魂，只要用该技能，就给玩家自身套一个反伤buff
					if (casterP != null && casterP.getCareer() == 4) {
						if (id == 9044) {
							int step = SkEnhanceManager.getInst().getSlotStep(casterP, 6);
							if (step > 0) {
								BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1859);
								if (bt != null && bt instanceof BuffTemplate_FanShang) {
									Buff b = bt.createBuff(step);
									if (b != null) {
										b.setStartTime(SystemTime.currentTimeMillis());
										b.setInvalidTime(b.getStartTime() + 6000);
										b.setCauser(casterP);
										casterP.placeBuff(b);
										logger.debug("[吾皇反噬] [种植buff成功] [反噬伤害百分比:"+casterP.getIronMaidenPercent()+"] [bufflevel:" + b.getLevel() + "] [" + bt.getName() + "] [施法者：" + casterP.getName() + "]");
									}
								}
							}
						}
					}
				}

				{// 吾皇，惊雷，定身，伤害加成%，持续5秒
					if (casterP != null && casterP.getCareer() == 4 && target != null) {
						if (target.isHold()) {
							int step = SkEnhanceManager.getInst().getSlotStep(casterP, 9);
							if (step > 0) {
								int bufflevel = 0;
								if (step == 1) {
									bufflevel = 1;
								} else if (step == 2) {
									bufflevel = 2;
								} else if (step == 3) {
									bufflevel = 3;
								}
								Long value = casterP.skillTouchSpace.get(new Integer(this.getId()));
								if (value == null) {
									casterP.skillTouchSpace.put(new Integer(this.getId()), new Long(1111l));
								}
								if (value != null) {
									if (System.currentTimeMillis() - value.longValue() > 10000) {
										BuffTemplate bt = BuffTemplateManager.getInstance().getBuffTemplateById(1759);
										if (bt != null && bt instanceof BuffTemplate_addDamage) {
											Buff b = bt.createBuff(bufflevel);
											if (b != null) {
												b.setStartTime(SystemTime.currentTimeMillis());
												b.setInvalidTime(b.getStartTime() + 5000);
												b.setCauser(casterP);
												target.placeBuff(b);
												casterP.skillTouchSpace.put(new Integer(this.getId()), new Long(System.currentTimeMillis()));
												logger.debug("[吾皇惊雷定身伤害加成] [种植buff成功] [bufflevel:" + bufflevel + "] [" + bt.getName() + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
											}
										}
									}
								}
								double percent = 0.0;
								if (target instanceof Player) {
									targetP = ((Player) target);
									if (targetP.getBuffByName(Translate.伤害提升) != null) {
										logger.debug("[吾皇惊雷定身伤害加成] [check惊魂] [buff不为空] [id:" + (targetP.getBuffByName("伤害提升").getTemplateId()) + "]");
										percent = SkEnhanceManager.getInst().check惊魂(casterP, (Fighter) target);
										damage += (int) (damage * percent);
									}
								} else if (target instanceof Sprite) {

								}
								logger.debug("[吾皇惊雷定身伤害加成] [check惊魂] [damage:" + damage + "] [percent:" + percent + "] [施法者：" + casterP.getName() + "] [目标：" + target.getName() + "]");
							}
						}
					}
				}

				try {
					if (this.isSpecialSkillFlag() && this.getExtraDmgRate() > 0 && target.getSpriteStatus(caster) == Constants.shihunFlag) {
						damage = (int) (damage * (1 + this.getExtraDmgRate() / 100));
					}
					if (logger.isDebugEnabled()) {
						logger.debug("[特殊技能测试] [" + this.getId() + "] [isSpecialSkillFlag:" + isSpecialSkillFlag() + "] [getExtraDmgRate:" + getExtraDmgRate() + "] [targetSt:" + target.getSpriteStatus(caster) + "]  [类id:" + caster.getClassType() + "] [" + caster.getId() + "] [" + caster.getName() + "]");
					}
				} catch (Exception e) {
					logger.warn("[兽魁技能] [根据玩家状态触发增加伤害] [异常] [id:" + id + "] [类id:" + caster.getClassType() + "] [" + caster.getId() + "] [" + caster.getName() + "]", e);
				}

				if (target != null && target instanceof Player) {
					if (SkEnhanceManager.inst.checkdikangnum((Player) target, this) > 0) {
						damage = 1;
					}
				}

				if (target instanceof Player) {
					Player p = (Player) target;
					if (p.isInvulnerable()) { // 无敌
						dmgType = Fighter.DAMAGETYPE_MIANYI;
						// target.causeDamage(caster, damage, hateParam,Fighter.DAMAGETYPE_MIANYI);
						// caster.damageFeedback(target, damage, hateParam,Fighter.DAMAGETYPE_MIANYI);

						if (logger.isInfoEnabled()) {
							logger.info("ActiveSkill hit 7 mian yi");
						}
					} else {
						// target.causeDamage(caster, damage, hateParam,fightType);
						//
						// caster.damageFeedback(target, damage, hateParam,fightType);

						if (caster instanceof Player && target instanceof Player) {
							Player pp = (Player) target;
							pp.notifyAttack((Player) caster, getName(), level, fightType, damage);
						} else if (caster instanceof Pet && target instanceof Player) {
							Player pp = (Player) target;
							pp.notifyAttack((Pet) caster, getName(), level, fightType, damage);
						}

						if (logger.isDebugEnabled()) {
							logger.debug("[技能命中计算] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [targetHP:{}] [{}] [造成实际伤害:{}]", new Object[] { (getClass().getSimpleName()), this.getName(), level, effectIndex, caster.getName(), target.getName(), target.getHp(), getDamageTypeName(fightType), (damage) });
							logger.debug("ActiveSkill hit 8");
						}
					}
				} else if (target instanceof Sprite) {
					// logger.debug("ActiveSkill hit 9");
					Sprite p = (Sprite) target;
					if (p.isInvulnerable()) { // 无敌
						dmgType = Fighter.DAMAGETYPE_MIANYI;
						// target.causeDamage(caster, damage, hateParam,Fighter.DAMAGETYPE_MIANYI);
						// caster.damageFeedback(target, damage, hateParam,Fighter.DAMAGETYPE_MIANYI);

						if (logger.isInfoEnabled()) {
							logger.info("[技能命中计算] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [targetHP:{}] [{}] [目标处于无敌状态，减免所有伤害:{}]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), level, effectIndex, caster.getName(), target.getName(), target.getHp(), getDamageTypeName(Fighter.DAMAGETYPE_MIANYI), damage });
						}

					} else {

						// target.causeDamage(caster, damage , hateParam,fightType);
						//
						// caster.damageFeedback(target, damage, hateParam,fightType);

						if (MonsterManager.logger.isDebugEnabled()) {
							MonsterManager.logger.debug("[怪物坐标:{}]", new Object[] { caster.getX() + "," + caster.getY() });
						}
						if (caster instanceof Player && target instanceof Monster) {
							Monster m = (Monster) target;
							m.notifyAttack((Player) caster, getName(), level, fightType, damage);
						} else if (caster instanceof Pet && target instanceof Monster) {
							Pet pet = (Pet) caster;
							Monster m = (Monster) target;
							if (pet.specialTargetFactor != null) {
								Integer v = pet.specialTargetFactor.get(m.getSpriteCategoryId());
								if (v != null && v != 0) {
									float f = v / 100f;
									damage = (int) (damage * f);
									logger.debug("special damage factor {} to {}", v, m.getName());
								} else {
									logger.debug("special damage not contails {}", m.getName());
								}
							} else {
								logger.debug("ActiveSkill hit caster {} has no specialTargetFactor", caster.getName());
							}
							//
							Monster pp = (Monster) target;
							pp.notifyAttack((Pet) caster, getName(), level, fightType, damage);
						} else {
							logger.debug("ActiveSkill.hit: 9-2 {}", target.getClass().getSimpleName());
						}

						if (logger.isDebugEnabled()) {
							logger.debug("[技能命中计算] 9-3 [{}] [Lv:{},In:{}] [{}] hit [{}] [造成实际伤害:{}]", new Object[] { this.getName(), level, effectIndex, caster.getName(), target.getName(), damage });
						}
					}
				}

			}
			// logger.debug("ActiveSkill hit 10");
			if (dmgType != noFeedBack) {
				boolean isPetCaster = caster.getSpriteType() == Sprite.SPRITE_TYPE_PET;
				if (isPetCaster) {
					damage = checkPetTianShengSk(caster, target, damage);
					damage = checkPetSuit(caster, target, damage);	
				} else {
					logger.debug("ActiveSkill.hit: not SPRITE_TYPE_PET {} {}", caster.getClass().getSimpleName(), caster.getSpriteType());
				}
				// if(this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix) {
				if(caster instanceof Player){
					damage = HunshiManager.getInstance().getDamage((Player)caster, target, damage,!(this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix));
				}
				target.causeDamage(caster, damage, hateParam, dmgType);
				caster.damageFeedback(target, damage, hateParam, dmgType);
				if (this.isSpecialSkillFlag() && this.getRecoverHpRate() > 0) {
					int recoverHp = (int) ((long) damage * this.getRecoverHpRate() / 100L);
					if (recoverHp > 0) {
						if (((LivingObject) caster).isCanNotIncHp()) {
							if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[无法回血状态] [屏蔽--] [Sprite] [血]");
						} else {
							caster.setHp(caster.getHp() + recoverHp);
						}
					}
				}
				if (isPetCaster) {
					Pet pet = (Pet) caster;
					logger.debug("ActiveSkill.hit: pet proces {}", pet.getName());
					GenericBuffCalc.getInst().damageFeedback(caster, target, damage, dmgType, pet.pet2buff);
				} else {
					logger.debug("ActiveSkill.hit: caster is not pet");
				}
			} else {
				logger.debug("ActiveSkill.hit: 10-1, dmtType is noFeedBack.");
			}
		} else {
			logger.debug("ActiveSkill hit 11");
			if (logger.isDebugEnabled()) {
				logger.debug("[技能命中计算]  [{}] [Lv:{},In:{}] [{}] [{}]  [造成实际伤害:{}]", new Object[] { this.getName(), level, effectIndex, caster.getName(), target.getName(), damage });
			}
		}
		logger.debug("ActiveSkill.hit: end\n");
	}

	public static final double[] mulDamage1 = new double[] { 1, 1.5, 2 };
	public static final int[] probabblyt1 = new int[] { 20, 60, 20 };
	public static final double[] mulDamage2 = new double[] { 1, 1.5, 2, 2.5, 3 };
	public static final int[] probabblyt2 = new int[] { 10, 15, 50, 15, 10 };

	public static final int 善报概率 = 30;
	public static final int 恶报概率 = 70;
	/**
	 * 检查宠物饰品
	 * @param caster
	 * @param target
	 * @param damage
	 * @return
	 */
	public int checkPetSuit(Fighter caster, Fighter target, int damage) {
		try {
			if (target == null) {
				return damage;
			}
			int result = damage;
			Pet pet = (Pet) caster;
			try {
				int addRate = 0;
				int hpRate = (int) (((float)target.getHp() / (float)target.getMaxHP()) * 100);
				for (int i=0; i<pet.getHpPercent4Hit().length; i++) {
					if (hpRate >= pet.getHpPercent4Hit()[i] && addRate < pet.getHitIncreaseRate()[i]) {
						addRate = pet.getHitIncreaseRate()[i];
					}
				}
				for (int i=0; i<pet.getHpPercent4Hit2().length; i++) {
					if (hpRate <= pet.getHpPercent4Hit2()[i] && addRate < pet.getHitIncreaseRate2()[i]) {
						addRate = pet.getHitIncreaseRate2()[i];
					}
				}
				if (addRate > 0) {
					float f = 1 + addRate / 100f;
					result = (int) (damage * f);
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[目标血量高于比例加伤] [" + pet.getId() + "] [addRate:" + addRate + "] [damage:" + damage + "] [result:" + result + "]");
				}
				int extraDamage = pet.check4SingDamage(target);
				if (extraDamage > 0) {
					return result + extraDamage;
				}
				return result;
			} catch (Exception e) {
				logger.warn("[宠物血量低于一定比例增加伤害] [异常] [" + pet.getId() + "]", e);
			}
		} catch (Exception e) {
			logger.warn("[检查宠物饰品] [异常]", e);
		}
		return damage;
	}

	/**
	 * 检查宠物天生技能。
	 * @param caster
	 * @param target
	 * @param damage
	 * @return
	 */
	public int checkPetTianShengSk(Fighter caster, Fighter target, int damage) {
		if (target == null) {
			logger.debug("ActiveSkill.checkPetTianShengSk: target is null");
			return damage;
		}
		Pet pet = (Pet) caster;
		if (pet.dmgScaleRate > 0 && pet.dmgScale > 0) {
			logger.debug("ActiveSkill.hit: 计算几率性加倍伤害1 {} {}", pet.dmgScaleRate, pet.dmgScale);
			int r = random.nextInt(100);
			if (r < pet.dmgScaleRate + Pet2Manager.debugRate) {
				float f = pet.dmgScale / 100f;
				damage = (int) (damage * f);
				logger.debug("ActiveSkill.hit1: 几率放大命中 {}", r);
			} else {
				logger.debug("ActiveSkill.hit1: 几率放大miss{}", r);
			}
		}
		if (pet.dmgScaleRate2 > 0 && pet.dmgScale2 > 0) {
			logger.debug("ActiveSkill.hit: 计算几率性加倍伤害2 {} {}", pet.dmgScaleRate2, pet.dmgScale2);
			int r = random.nextInt(100);
			if (r < pet.dmgScaleRate2 + Pet2Manager.debugRate) {
				float f = pet.dmgScale2 / 100f;
				damage = (int) (damage * f);
				// damage = damage * pet.dmgScale2 / 100;
				logger.debug("ActiveSkill.hit2: 几率放大命中 {}", r);
			} else {
				logger.debug("ActiveSkill.hit2: 几率放大miss{}", r);
			}
		}
		Player p = pet.getMaster();
		if (pet.towerRate <= 0) {
			logger.debug("ActiveSkill.hit: has no tower rate");
		} else if (p == null) {
			logger.debug("Pet2SkillCalc.addSkill: mast is null {}", pet.getName());
		} else if (QianCengTaManager.getInstance().isInQianCengTa(p)) {
			float f = pet.towerRate / 100f;
			damage += damage * f;
			// damage += damage * pet.towerRate / 100;
			logger.debug("ActiveSkill.hit: tower rate {}", pet.towerRate);
		} else {
			logger.debug("ActiveSkill.hit: not in tower {}", p.getName());
		}
		if ((pet.talent1Skill == 110075 || pet.talent2Skill == 110075)) {
			int r = Pet2SkillCalc.getInst().rnd.nextInt(100);
			boolean isWorldBoss = false;
			if (target instanceof Monster) {
				Monster boss = (Monster) target;
			}
			if (isWorldBoss) {
				logger.debug("世界boss不触发勾魂 {}", target.getName());
			} else if (Pet2SkillCalc.getInst().isProtectedNPC(target)) {
			} else if (target.getClass() != BossMonster.class && r < 30 && target.getHp() < target.getMaxHP() / 5) {
				// 当目标血量少于20%时有30%几率将目标直接斩杀（对BOSS无效）
				damage = target.getHp() + 1000;
				logger.debug("ActiveSkill.hit: 触发勾魂");
			} else if (logger.isDebugEnabled()) {
				logger.debug("勾魂条件不满足 {} {} {}", new Object[] { target.getClass(), r, target.getHp(), target.getMaxHP() / 5 });
			}
		}
		if (pet.talent1Skill == 110128 || pet.talent2Skill == 110128) { // 对女性角色造成三倍伤害
			if (target instanceof Player) {
				Player pl = (Player) target;
				if (pl.getSex() == 1) {
					damage *= 3;
					if (logger.isDebugEnabled()) {
						logger.debug("[辣手摧花] [三倍伤害] [" + pl.getLogString() + "] [petId:" + pet.getId() + "] [petName:" + pet.getName() + "]");
					}
				}
			}
		}
		if (pet.talent1Skill == 110131 || pet.talent2Skill == 110131) { // 对男性角色造成三倍伤害
			if (target instanceof Player) {
				Player pl = (Player) target;
				if (pl.getSex() == 0) {
					damage *= 3;
					if (logger.isDebugEnabled()) {
						logger.debug("[火眼金睛] [三倍伤害] [" + pl.getLogString() + "] [petId:" + pet.getId() + "] [petName:" + pet.getName() + "]");
					}
				}
			}
		}

		if (pet.talent1Skill == 110120 || pet.talent2Skill == 110120) { // 善恶有报 百分之70造成三倍伤害 百分之30给对方加血
			if (!(target instanceof BossMonster)) {
				int ran = random.nextInt(100);
				if (ran <= 善报概率) { // 触发善报给对方加血
					if (!FighterTool.isCanNotIncHp(target)) {
						int addHp = damage / 2;
						target.setHp(target.getHp() + addHp);
						if (logger.isDebugEnabled()) {
							logger.debug("[善恶有报] [触发回血:" + target.getName() + "] [ran:" + ran + "] [恢复血量:" + (damage / 2) + "] [" + (p == null ? p : p.getLogString()));
						}
						try {
							if (p != null) {
								NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, target.getId(), (byte) Event.HP_INCREASED, addHp);
								p.addMessageToRightBag(req);
								if (target instanceof Player) {
									NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, target.getId(), (byte) Event.HP_INCREASED, addHp);
									((Player) target).addMessageToRightBag(req2);
								}
							}
						} catch (Exception e) {
						}

					} else {
						if (Skill.logger.isDebugEnabled()) Skill.logger.debug("[无法回血状态] [屏蔽善报] [" + p.getLogString() + "] [血]");
					}
					damage = 0;
				} else {
					damage *= 3;
					if (logger.isDebugEnabled()) {
						logger.debug("[善恶有报] [触发恶报:" + target.getName() + "] [ran:" + ran + "] [伤害:" + damage + "]");
					}
				}
			}
		}

		if (pet.talent1Skill == 110011 || pet.talent2Skill == 110011) {
			// 嗜杀 TS_shisha 所有群体伤害技能必定为2倍暴击伤害
			logger.debug("ActiveSkill.checkPetTianShengSk: 全体暴击变暴击检查 {} {}", getClass().getSimpleName(), name);
			if (this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix || this instanceof SkillWithoutTraceAndWithSummonNPC || this instanceof SkillWithTraceAndDirectionOrTarget) {
				// int r = random.nextInt(100);
				// int index = 0;
				// int tempInt = 0;
				// for(int i=0; i<probabblyt1.length; i++) {
				// tempInt += probabblyt1[i];
				// if(r <= tempInt) {
				// index = i;
				// break;
				// }
				// }
				// if(logger.isDebugEnabled()) {
				// logger.debug("[ActiveSkill.hit: 触发嗜杀 触发暴击] [原始伤害值:" + damage + "]");
				// }
				damage *= 2;
				logger.debug("[ActiveSkill.hit: 触发嗜杀 触发暴击] [最终伤害值:" + damage + "]");
			}
		}
		if (pet.talent1Skill == 110035 || pet.talent2Skill == 110035) {
			// 杀神 TS_shashen 所有群体伤害技能必定为3倍暴击伤害
			if (this instanceof SkillWithoutTraceAndWithRange || this instanceof SkillWithoutTraceAndWithMatrix || this instanceof SkillWithoutTraceAndWithSummonNPC || this instanceof SkillWithTraceAndDirectionOrTarget) {
				int r = random.nextInt(100);
				int index = 0;
				int tempInt = 0;
				for (int i = 0; i < probabblyt2.length; i++) {
					tempInt += probabblyt2[i];
					if (r <= tempInt) {
						index = i;
						break;
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("[ActiveSkill.hit: 触发 杀神 触发暴击] [原始伤害值:" + damage + "]");
				}
				damage = (int) (mulDamage2[index] * damage);
				logger.debug("[ActiveSkill.hit: 触发 杀神 触发暴击] [随机数:" + r + "] [暴击比率:" + mulDamage2[index] + "] [最终伤害值:" + damage + "]");
			}
		}
		if (pet.talent1Skill == 110038 || pet.talent2Skill == 110038) {
			if (target instanceof Player) {
				if (damage > 0) {
					// 当对人物造成伤害时对敌人任意装备降低耐久度5点
					Player targetP = (Player) target;
					targetP.getEquipmentColumns().decreaseRandomDur(5);
					logger.debug("ActiveSkill.checkPetTianShengSk: 减少目标耐久 {}", target.getName());
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("ActiveSkill.checkPetTianShengSk: 伤害为0 不掉耐久 {}", target.getName());
					}
				}
			}
		}
		if (pet.talent1Skill == 110085 || pet.talent2Skill == 110085) {
			if (target instanceof Player) {
				if (damage > 0) {
					// 当对人物造成伤害时对敌人任意装备降低耐久度5点
					Player targetP = (Player) target;
					targetP.getEquipmentColumns().decreaseRandomDur(50);
					logger.debug("ActiveSkill.checkPetTianShengSk: 减少目标耐久 {}", target.getName());
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("ActiveSkill.checkPetTianShengSk: 伤害为0 不掉耐久 {}", target.getName());
					}
				}
			}
		}
		if (pet.talent1Skill == 110072 || pet.talent2Skill == 110072 && target.getHp() < target.getMaxHP() / 2) {
			// 当目标血量低于50%，普通攻击附带自身血量上限8%的伤害（若目标处于定身状态，附带自身血量上限为10%）
			float f = 8f / 100f;
			int add = (int) (pet.getMaxHP() * f);
			if (target.isHold()) {
				f = 10f / 100f;
				add = (int) (pet.getMaxHP() * f);
			}
			damage += add;
			logger.debug("ActiveSkill.checkPetTianShengSk: 触发追命 add {}", add);
		}
		if (pet.talent1Skill == 110094 || pet.talent2Skill == 110094) {
			// 对眩晕目标造成伤害为正常伤害的3倍
			if (target.isStun()) {
				damage *= 3;
				logger.debug("ActiveSkill.checkPetTianShengSk: 触发目标眩晕则3倍伤害 {}", target.getName());
			}
		}
		if (pet.talent1Skill == 110081 || pet.talent2Skill == 110081) {
			// 当对宠物造成伤害时减少目标宠物当前快乐值，每只宠物对多减少50点
			if (target instanceof Pet) {
				Pet targetPet = (Pet) target;
				logger.debug("ActiveSkill.checkPetTianShengSk: 触发减少目标宠物快乐值 {}", target.getName());
				targetPet.setHappyness(targetPet.getHappyness() - 50);
			} else {
				logger.debug("ActiveSkill.checkPetTianShengSk: 减少目标宠物当前快乐值，不是宠物{}", target.getClass().getSimpleName());
			}
		}
		if ((pet.talent1Skill == 110058 || pet.talent2Skill == 110058)) {
			// 110058 荆棘 TS_kaiwu 自身物理防御上限值按百分比转换为反伤伤害
			float f = 0.5f;
			damage += pet.getPhyDefence() * f;
			// damage += pet.getPhyDefence() * 50 / 100;
		}
		if ((pet.talent1Skill == 110062 || pet.talent2Skill == 110062) && random.nextInt(100) < 5) {
			// //每次攻击有5%几率对目标造成10W伤害 110062 瞬击
			damage += 100000;
			logger.debug("ActiveSkill.checkPetTianShengSk: 110062	瞬击");
		}
		if ((pet.talent1Skill == 110096 || pet.talent2Skill == 110096)) {
			// 免爆 TS_mianbao02 50%几率降低目标10%的暴击抗性，持续3秒
			if (random.nextInt(100) < 50) {
				BuffTemplate tt = BuffTemplateManager.getInstance().getBuffTemplateById(1151);
				if (tt instanceof BuffTemplate_JianBaoJi == false) {
					logger.debug("checkPetTianShengSk: miss match {}", tt);
					return damage;
				}
				Buff_JianBaoJi buff = (Buff_JianBaoJi) tt.createBuff(1);
				buff.setStartTime(SystemTime.currentTimeMillis());
				buff.setInvalidTime(buff.getStartTime() + 3000);
				buff.setCauser(pet);
				buff.criticalHit = 100;
				buff.setDescription(Translate.text_3142 + 10 + "%");
				target.placeBuff(buff);
				logger.debug("免爆 50%几率降低目标10%的暴击抗性，持续3秒");
			}
		}
		// if ((pet.talent1Skill == 110085 || pet.talent2Skill == 110085)) { //原虎扑技能效果没用，删除  2014年6月3日17:53:09
		// // 每7次攻击后下次攻击目标将会对目标造成定身，并造成2倍伤害，如果目标处于定身状态将造成4倍伤害
		// if (pet.getSkillAgent().atkTimes % 7 != 0) {
		// } else if (target.isStun()) {
		// damage *= 4;
		// logger.debug("ActiveSkill.checkPetTianShengSk: 虎扑4倍 {}", target.getName());
		// } else {
		// target.setStun(true);
		// logger.debug("ActiveSkill.checkPetTianShengSk: 虎扑定身 {}", target.getName());
		// }
		// }
		logger.debug("ActiveSkill.checkPetTianShengSk: end-----");
		return damage;
	}

	/**
	 * 释放Buff，调用此方法表示技能已经命中目标，
	 * 准备释放Buff
	 * 
	 * @param caster
	 * @param target
	 * @param level
	 *            从1 开始
	 * @param effectIndex
	 */
	protected void fireBuff(Player caster, Fighter target, Weapon w, EquipmentEntity ee) {
	}

	protected void notifyBuffIcon(Fighter target, Buff buff) {
	}

	/**
	 * 释放Buff，调用此方法表示技能已经命中目标，
	 * 准备释放Buff
	 * 
	 * @param caster
	 * @param target
	 * @param level
	 *            从1 开始
	 * @param effectIndex
	 */
	protected void fireBuff(Fighter caster, Fighter target, int level, int effectIndex) {
		if (buffName == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[技能释放BUFF] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [没有配置]", new Object[] { (getClass().getSimpleName()), this.getName(), (level), effectIndex, caster.getName(), target.getName(), buffName });
			}
			return;
		}
		if (buffName.isEmpty()) return;
		if (buffProbability == null || buffProbability.length < level) {
			if (logger.isInfoEnabled()) {
				logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [概率设置错误]", new Object[] { getClass().getSimpleName(), this.getName(), (level), effectIndex, caster.getName(), target.getName(), buffName });
			}
			return;
		}

		if (level == 0) {
			if (logger.isInfoEnabled()) {
				logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}]  [技能等级为0]", new Object[] { (getClass().getSimpleName()), this.getName(), (level), effectIndex, caster.getName(), target.getName(), buffName });
			}
			return;
		}

		int probability = buffProbability[level - 1];
		int aa = 0;
		Player casterP = null;
		if (caster instanceof Player) {
			Player p = (Player) caster;
			ActiveSkillParam param = p.getActiveSkillParam(this);
			if (param != null) {
				aa = param.getBuffProbability();
			}
			casterP = p;
		}

		probability += aa;
		if (id == 705 && casterP != null) {
			// //修罗 705 夺命 人阶10重:获得夺命触发暴击几率增加5%
			int vv = SkEnhanceManager.getInst().getBuffAddBp(casterP, this);
			logger.debug("ActiveSkill.fireBuff: //修罗	705	夺命	人阶10重:获得夺命触发暴击几率增加{}%", vv);
			probability += vv + SkEnhanceManager.debugRateAdd;
		} else if (id == 715 && casterP != null) {
			// 715 噬灵 人阶10重:获得噬灵技能中毒效果出发几率增加1%
			int vv = SkEnhanceManager.getInst().getBuffAddBp(casterP, this);
			logger.debug("ActiveSkill.fireBuff: //715	噬灵	人阶10重:获得噬灵技能中毒效果出发几率增加{}%", vv);
			probability += vv + SkEnhanceManager.debugRateAdd;
		}

		if (random.nextInt(100) + 1 > probability) {

			if (logger.isInfoEnabled()) {
				// logger.info("[技能释放BUFF] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [概率不执行]", new
				// Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),(level),effectIndex,caster.getName(),target.getName(),caster.getName(),target.getName(),buffName,probability});
			}

			return;
		}

		BuffTemplateManager bm = BuffTemplateManager.getInstance();
		BuffTemplate bt = bm.getBuffTemplateByName(buffName);
		if (bt == null) {
			if (logger.isInfoEnabled()) {
				logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [buff模板不存在]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buffName, probability });
			}

			return;
		}
		int bl = 0;
		if (buffLevel != null && buffLevel.length >= level) {
			bl = buffLevel[level - 1];
			if (bl > 0) bl = bl - 1; // 因为配置的时候bl从1开始，但是buff的数据数组下标从0开始
		}
		/**
		 * 获得增加杀戮技能附带减速效果增加5%
		 */
		if (pageIndex >= 0 && casterP != null && casterP.getLevel() >= TransitRobberyManager.openLevel && SkEnhanceManager.open) {
			if (id == 602) {
				try {
					SkBean bean = SkEnhanceManager.getInst().findSkBean(casterP);
					if (bean != null && bean.useLevels != null) {
						int lv = bean.useLevels[pageIndex];
						if (lv > 0) {
							int step = lv / 10;
							if (step == 1) { // 55%
								bl = 28 - 1;
							} else if (step == 2) { // 65%
								bl = 33 - 1;
							} else if (step == 3) { // 75%
								bl = 38 - 1;
							}
							logger.warn("[技能触发buff] [成功] [技能：杀戮] [技能id：602] [pageIndex:" + pageIndex + "] [lv:" + lv + "] [step:" + step + "] [bl:" + bl + "] [" + casterP.getLogString() + "]");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("[技能触发buff] [异常] [技能：杀戮] [技能id：602] [pageIndex:" + pageIndex + "] [" + casterP.getLogString() + "]", e);
				}
			}
		}

		Buff buff = bt.createBuff(bl);
		// 大师技能中，有四个增加攻击属性的
		if (buff == null) {
			logger.warn("[释放buff] [错误] [buff==null] [level:" + level + "] [caster:" + (caster == null ? "null" : caster.getName()) + "] [target:" + (target == null ? "null" : target.getName()) + "]");
		}
		int skEnBuffAdd = 0;
		logger.debug("ActiveSkill.fireBuff: page index {} template {} level{} buffname{} templatename{}", new Object[] { pageIndex, bt.getClass().getSimpleName(), buff.getLevel(), buff.getDescription(), buff.getTemplateName() });
		if (pageIndex >= 0 && casterP != null && casterP.getLevel() >= TransitRobberyManager.openLevel && SkEnhanceManager.open) {
			if (buff instanceof Buff_GongQiangFangYu) {
				checkSlot1buff(casterP, buff);
			} else if (id == 401) {// //血印 额外增加生命上限
				Buff_JiaXueShangXianBiLi b = (Buff_JiaXueShangXianBiLi) buff;
				b.calc();
				SkBean bean = SkEnhanceManager.getInst().findSkBean(casterP);
				if (bean != null && bean.useLevels != null) {
					int lv = bean.useLevels[pageIndex];
					if (lv > 0) {
						int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[lv - 1];
						b.totalHPC += point;
						buff.setDescription(Translate.text_3290 + b.totalHPC + "%");
						logger.debug("ActiveSkill.fireBuff: 血印 额外增加生命上限 {}%", point);
					}
					SkEnhanceManager.getInst().checkSlot3DouLuo(casterP, target, bean, logger);
				}
			} else {
				int layerValue = SkEnhanceManager.getInst().getLayerValue(casterP, pageIndex);
				if (id == 402) {// 402普度 气血回复百分比增强
					int lv = layerValue;
					do {
						if (lv <= 0) break;
						int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[lv - 1];
						if (point <= 0) break;
						Buff_JiaXue bb = (Buff_JiaXue) buff;
						bb.hpFix += point * 10;// 基数1000
						logger.debug("ActiveSkill.fireBuff: 普度 +{}", point);
						BuffTemplate_JiaXue tt = (BuffTemplate_JiaXue) bb.getTemplate();
						tt.fixDesc(bb.getLevel(), bb, point);
					} while (false);
				} else if (id == 403) {// 仙心 403 焱印 人阶10重:激活焱印技能暴击率增加2%
					int skLv = layerValue;
					do {
						if (skLv <= 0) break;
						Buff_BaoJi bj = (Buff_BaoJi) buff;
						bj.calcValue();
						int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[skLv - 1];
						bj.criticalHit += point * 10;
						logger.debug("ActiveSkill.fireBuff:仙心403焱印技能暴击率增加{}每层!", point);
						int step = skLv / 10;
						if (step > 0) {
							step += 1;
							bj.criticalHit += step * 10;
							logger.debug("ActiveSkill.fireBuff:仙心403焱印技能暴击率增加{}", step);
						}
						buff.setDescription(Translate.text_3141 + bj.criticalHit * 1f / 10 + "%");
					} while (false);
				} else if (id == 703) {// 修罗 703 震魂
					SkBean bean = SkEnhanceManager.getInst().findSkBean(casterP);
					if (bean != null && bean.useLevels != null && bean.useLevels[pageIndex] >= 10) {
						int lv = bean.useLevels[pageIndex] / 10;
						int vv[] = { 0, 3, 5, 8 };
						lv = vv[lv] * 1000;
						skEnBuffAdd = lv;
						logger.debug("ActiveSkill.fireBuff: 修罗	703	震魂 增加持续时间 {}", lv);
					}
				}
				// else if (id == 9044) {// 9044 反噬 九黎 获得反噬技能持续时间增加2秒 //造成额外%d伤害转换为自身生命值
				// if (layerValue > 0) {
				// int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[layerValue - 1];
				// Buff_CouXie cx = (Buff_CouXie) buff;
				// cx.calc();
				// int pre = cx.hpStealPercent;
				// cx.hpStealPercent += point;
				// cx.setDescription(Translate.text_3162 + pre + "(+" + point + ")" + Translate.text_3163);
				// skEnBuffAdd = SkEnhanceManager.getInst().getBuffAddTime(casterP, this) * 1000;
				// logger.debug("ActiveSkill.fireBuff: 812 反噬 九黎 抽血百分比增加 {} {} {}",new Object[]{cx.hpStealPercent ,cx.getDescription(), point});
				// }
				// }
				else if (id == 8213) {// 8213 遁形 隐身且受到伤害降低%d%%
				// // 获得遁形技能持续时间增加2秒且冷却时间减少20秒
					if (layerValue > 0) {
						int point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].levelAddPoint[layerValue - 1];
						Buff_YinShenAndJianShang ys = (Buff_YinShenAndJianShang) buff;
						ys.calc();
						ys.decreaseDamage += point * 10;
						buff.setDescription(Translate.translateString(Translate.隐身且减少受到伤害详细, new String[][] { { Translate.COUNT_1, ys.decreaseDamage / 10.0 + "" } }));
						skEnBuffAdd = SkEnhanceManager.getInst().getBuffAddTime(casterP, this) * 1000;
						logger.debug("ActiveSkill.fireBuff: 8213 隐身且受到伤害降低 {}{}", point, skEnBuffAdd);
					}
				} else if (id == 8258) { // 8258 嗜血 增加buff吸收伤害上限
					if (layerValue > 0) {
						double point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].specilAddPoint[layerValue - 1];
						Buff_RecoverShield brs = (Buff_RecoverShield) buff;
						brs.setExtraPer(point);
						if (logger.isDebugEnabled()) {
							logger.debug("[ActiveSkill.fireBuff] [8258增加护盾吸收伤害百分比] [" + point + "] [" + casterP.getLogString() + "]");
						}
					}
				} else if (id == 8257) { // 8257 刚胆
					if (layerValue > 0) {
						double point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].specilAddPoint[layerValue - 1];
						Buff_WangZheZhuFu brs = (Buff_WangZheZhuFu) buff;
						brs.setExtraRate(point);
						if (logger.isDebugEnabled()) {
							logger.debug("[ActiveSkill.fireBuff] [8257增加钢胆效果] [" + point + "] [" + casterP.getLogString() + "]");
						}
					}
				} else if (id == 8256) { // 8256 兽印
					if (layerValue > 0) {
						double point = SkEnConf.conf[casterP.getCareer() - 1][pageIndex].specilAddPoint[layerValue - 1];
						Buff_LiLiangZhuFu brs = (Buff_LiLiangZhuFu) buff;
						brs.setExtraRate(point);
						if (logger.isDebugEnabled()) {
							logger.debug("[ActiveSkill.fireBuff] [8256增加兽印效果] [" + point + "] [" + casterP.getLogString() + "]");
						}
					}
				} else {
					skEnBuffAdd = SkEnhanceManager.getInst().getBuffAddTime(casterP, this) * 1000;
				}
			}
		}
		buff.setTemplate(bt);
		buff.setGroupId(bt.getGroupId());

		long bi = -1;
		if (this.buffLastingTime != null && this.buffLastingTime.length >= level) {
			bi = buffLastingTime[level - 1] + skEnBuffAdd;
		}

		if (bi == -1) {

			if (logger.isInfoEnabled()) {
				logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [没有设置buff持续时间]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), bt.getName(), probability });
			}

			return;
		}

		aa = 0;
		if (caster instanceof Player) {
			Player p = (Player) caster;
			ActiveSkillParam param = p.getActiveSkillParam(this);
			if (param != null) {
				aa = param.getBuffLastingTime();
			}
		}

		bi += aa;
		buff.setLevel(bl);
		if (buff instanceof ControlBuff) {
			if (caster != null && target != null) {
				if (caster instanceof Player && (target instanceof Player)) {
					bi += FlyTalentManager.getInstance().handle_专注(((Player) target), ((Player) caster), bi);
				}
			}
		}
		buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + bi);
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setCauser(caster);
		if (this.buffTargetType == 1) {
			caster.placeBuff(buff);
			notifyBuffIcon(caster, buff);

			if (logger.isInfoEnabled()) {
				logger.info("[技能释放BUFF成功] [持续时间：{}] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}]  [概率：{}] [施加给施法者] [开始时间:{}] [结束时间:{}]", 
						new Object[] { bi, (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex,(caster!=null?caster.getName():"null") , (target!=null?target.getName():"null"), (caster!=null?caster.getName():"null"),(target!=null?target.getName():"null") , buff.getDescription(), probability, buff.getStartTime(), buff.getInvalidTime() });
			}

		} else if (this.buffTargetType == 0) {

			if (target instanceof BossMonster) {
				BossMonster boss = (BossMonster) target;
				if (boss.isImmuneHoldFlag() && buff instanceof Buff_DingSheng) {

					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else if (boss.isImmuneSilenceFlag() && (buff instanceof Buff_Silence || buff instanceof Buff_ChenMo)) {
					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else if (boss.isImmuneSneerFlag() && buff instanceof Buff_CouHenDiYi) {
					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else if (boss.isImmuneStunFlag() && buff instanceof Buff_XuanYun) {
					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else if (boss.isImmuneStunFlag() && buff instanceof Buff_XuanYunAndWeak) {
					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else if (boss.isImmuneSpeedDownFlag() && buff instanceof Buff_JianShu) {
					caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				} else {
					target.placeBuff(buff);

					if (logger.isInfoEnabled()) {
						logger.info("[技能释放BUFF成功] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
					}
				}
			} else {
				target.placeBuff(buff);
				notifyBuffIcon(target, buff);
				if (logger.isInfoEnabled()) {
					logger.info("[技能释放BUFF成功] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标] [开始时间:{}] [结束时间:{}] [buff级别：{}]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability, buff.getStartTime(), buff.getInvalidTime(), buff.getLevel() });
				}
				//HunshiManager.instance.dealWithInfectSkill(caster, target, buff);
			}
			
		} else {
			if (logger.isInfoEnabled()) logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}]  [概率：{}] [buffTargetType值不对]", new Object[] { (getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1)), this.getName(), (level), effectIndex, caster.getName(), target.getName(), caster.getName(), target.getName(), buff.getDescription(), probability });
		}
	}

	public void checkSlot1buff(Player casterP, Buff buff) {
		SkBean bean = SkEnhanceManager.getInst().findSkBean(casterP);
		if (bean != null && bean.useLevels != null && bean.useLevels[pageIndex] > 0) {
			int lv = bean.useLevels[pageIndex];
			byte career = casterP.getCareer();
			SkEnConf[] skEnConfs = SkEnConf.conf[career - 1];
			int point = skEnConfs[pageIndex].levelAddPoint[lv - 1];// 每一重的加成。
			int vv = bean.useLevels[0];
			logger.debug("ActiveSkill.checkSlot1buff: Point [" + point + "] [vv:" + vv + "]");
			Buff_GongQiangFangYu preOne = (Buff_GongQiangFangYu) casterP.getBuff(buff.getTemplateId());
			if (vv < 10) {
				logger.debug("ActiveSkill.fireBuff: first slot < 10，无大段加成");
			} else if (career == 2) {
				// 影魅 熊印 提升2%外防加成
				// vv /= 10;
				// switch (vv) {
				// case 1:
				// vv = 2;
				// break;
				// case 2:
				// vv = 3;
				// break;
				// case 3:
				// vv = 5;
				// break;
				// }
				// int base = (casterP.getPhyDefenceA() + casterP.getPhyDefenceB());
				// if (preOne != null) {
				// base -= preOne.phyDefence;
				// logger.debug("ActiveSkill.checkSlot1buff: pre {}", preOne.phyDefence);
				// }
				// vv = base * vv / 100;
				// point += vv;
				// logger.debug("ActiveSkill.fireBuff: 熊印大段加成 {}", vv);
			} else if (career == 3) {
				// 仙心 404 灵印 人阶10重激活永久提升内功+2%
				// vv = (vv / 10) + 1;
				// int base = (casterP.getMagicAttackA() + casterP.getMagicAttackB());
				// if (preOne != null) {
				// base -= preOne.magicDamage;
				// logger.debug("ActiveSkill.checkSlot1buff: pre {}", preOne.magicDamage);
				// }
				// vv = base * vv / 100;
				// point += vv;
				// logger.debug("ActiveSkill.fireBuff: 灵印大段加成 {}", vv);
			} else if (career == 4) {
				// // 九黎 408 骨印 人阶10重获得永久内防加成3%
				// vv = (vv / 10) + 2;
				// int base = (casterP.getMagicAttackA() + casterP.getMagicAttackB());
				// if (preOne != null) {
				// base -= preOne.magicDamage;
				// logger.debug(casterP.getName() + "ActiveSkill.checkSlot1buff:[pre:" + preOne.magicDamage + "]");
				// }
				// vv = base + vv / 100;
				// point += vv;
				// logger.debug(casterP.getName() + "ActiveSkill.fireBuff: [骨印大段加成:" + vv + "] [point:" + point + "]");
			}
			Buff_GongQiangFangYu bb = (Buff_GongQiangFangYu) buff;
			bb.fixEnhance(point);
			if (logger.isDebugEnabled()) {
				logger.debug(casterP.getName() + "ActiveSkill.fireBuff: 触发大师技能 {} point {}", getName(), point);
				logger.debug(casterP.getName() + "ActiveSkill.fireBuff: buff name {}", bb.getTemplateName());
			}
		}
	}

	/**
	 * 当主动技能发招时调用此方法
	 * 
	 * @param skillEntity
	 *            主动技能的技能体
	 * @param target
	 *            释放目标
	 * @param game
	 *            游戏场景
	 * @param level
	 *            技能等级，从1开始
	 * @param effectIndex
	 *            第几次出招，针对有多个出招时间的情况
	 * @param direction
	 *            出招朝向，无目标有方向的技能使用，其余技能不用
	 */
	public abstract void run(ActiveSkillEntity skillEntity, Fighter target, Game game, int level, int effectIndex, int targetX, int targetY, byte direction);

	/**
	 * 检查技能释放条件是否满足
	 * 
	 * @param caster
	 *            技能使用者
	 * @param target
	 *            释放目标
	 * @param level
	 *            技能等级，从1开始
	 * @return
	 */
	public abstract int check(Fighter caster, Fighter target, int level);

	/**
	 * 获得各个技能需要的蓝，如果技能不需要蓝，返回值中，对应级别的数应该是0
	 * 否则，就表示不能使用
	 * @return
	 */
	public abstract short[] getMp();

	/**
	 * @return 技能释放周期
	 */
	public int getInterval() {
		return duration1 + duration2 + duration3;
	}

	public int getEffectiveTime(int i) {
		return effectiveTimes[i];
	}

	public int effectTimes() {
		return effectiveTimes.length;
	}

	public int getDuration1() {
		return duration1;
	}

	public int getDuration2() {
		return duration2;
	}

	public String getStyle1() {
		return style1;
	}

	public String getStyle2() {
		return style2;
	}

	public int getDuration3() {
		return duration3;
	}

	public void setDuration3(int duration3) {
		this.duration3 = duration3;
	}

	public int[] getEffectiveTimes() {
		return effectiveTimes;
	}

	public void setEffectiveTimes(int[] effectiveTimes) {
		this.effectiveTimes = effectiveTimes;
	}

	public void setDuration1(int duration1) {
		this.duration1 = duration1;
	}

	public void setDuration2(int duration2) {
		this.duration2 = duration2;
	}

	public void setStyle1(String style1) {
		this.style1 = style1;
	}

	public void setStyle2(String style2) {
		this.style2 = style2;
	}

	public int[] getBuffProbability() {
		return buffProbability;
	}

	public void setBuffProbability(int[] buffProbability) {
		this.buffProbability = buffProbability;
	}

	public int getRange() {
		return 0;
	}

	public boolean isGuajiFlag() {
		return guajiFlag;
	}

	public void setGuajiFlag(boolean guajiFlag) {
		this.guajiFlag = guajiFlag;
	}

	public int getCalDamageTime() {
		return calDamageTime;
	}

	public void setCalDamageTime(int calDamageTime) {
		this.calDamageTime = calDamageTime;
	}

	public boolean isDouFlag() {
		return douFlag;
	}

	public void setDouFlag(boolean douFlag) {
		this.douFlag = douFlag;
	}

	public int getPointNum() {
		return pointNum;
	}

	public void setPointNum(int pointNum) {
		this.pointNum = pointNum;
	}

	public boolean isSpecialSkillFlag() {
		return specialSkillFlag;
	}

	public void setSpecialSkillFlag(boolean specialSkillFlag) {
		this.specialSkillFlag = specialSkillFlag;
	}

	public byte getNeedStatus() {
		return needStatus;
	}

	public void setNeedStatus(byte needStatus) {
		this.needStatus = needStatus;
	}

	public long getExtraDmgRate() {
		return extraDmgRate;
	}

	public void setExtraDmgRate(long extraDmgRate) {
		this.extraDmgRate = extraDmgRate;
	}

	public String getSpreadBuffName() {
		return spreadBuffName;
	}

	public void setSpreadBuffName(String spreadBuffName) {
		this.spreadBuffName = spreadBuffName;
	}

	public boolean isBianshenBtn() {
		return bianshenBtn;
	}

	public void setBianshenBtn(boolean bianshenFlag) {
		this.bianshenBtn = bianshenFlag;
	}

	public long getRecoverHpRate() {
		return recoverHpRate;
	}

	public void setRecoverHpRate(long recoverHpRate) {
		this.recoverHpRate = recoverHpRate;
	}

	public boolean isBianshenSkill() {
		return isBianshenSkill;
	}

	public void setBianshenSkill(boolean isBianshenSkill) {
		this.isBianshenSkill = isBianshenSkill;
	}

}
