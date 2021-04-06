package com.fy.engineserver.sprite.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.articleEnchant.AbnormalStateBuff;
import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.concrete.TournamentField;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.core.res.Avata;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.props.PetEggProps;
import com.fy.engineserver.datasource.article.data.props.PetProps;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_OnceAttributeAttack;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuFaGong;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWuGong;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.NewAuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.activeskills.CommonAttackSkill;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndOnTeamMember;
import com.fy.engineserver.datasource.skill2.CountTimesSkillAgent;
import com.fy.engineserver.datasource.skill2.GenericBuff;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.guozhan.Guozhan;
import com.fy.engineserver.guozhan.GuozhanOrganizer;
import com.fy.engineserver.homestead.cave.PetHookInfo;
import com.fy.engineserver.hotspot.Hotspot;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.PET2_LiZi_RES;
import com.fy.engineserver.message.PET2_QUERY_RES;
import com.fy.engineserver.message.PET_QUERY_REQ;
import com.fy.engineserver.message.PET_QUERY_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticle;
import com.fy.engineserver.sprite.pet.suit.PetSuitArticleEntity;
import com.fy.engineserver.sprite.pet.suit.effect.SuitEffectEnum;
import com.fy.engineserver.sprite.pet2.GradePet;
import com.fy.engineserver.sprite.pet2.LianHunConf;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.fy.engineserver.sprite.pet2.Pet2PropCalc;
import com.fy.engineserver.sprite.pet2.Pet2SkillCalc;
import com.fy.engineserver.sprite.pet2.PetGrade;
import com.fy.engineserver.util.ProbabilityUtils;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;

/**
 * Sprite，为怪，NPC的超类
 * 
 * 
 * 
 */
@SimpleEntity
@SimpleIndices( { @SimpleIndex(members = { "delete" }), @SimpleIndex(members = { "qualityScore" }), @SimpleIndex(members = { "country" }) })
public class Pet extends Sprite implements Cloneable, Cacheable, CacheListener {

	/**
	 * by 康建虎 2013年9月4日11:42:07，需要时请移动此属性至更高的父类。
	 */
	public int critFactor = 200;
	public transient int hpStealPercent;

	private static final long serialVersionUID = -54347551653490358L;

	private static final int 每次死亡寿命值下降值 = 100;
	private static final int 每次死亡寿命值下降值_korea = 20;
	private static final int 每次死亡快乐值下降值 = 100;
	private static final int 规定时间快乐值下降值 = 30;
	private static final int 规定时间寿命下降值 = 10;
	public static final byte 幻化广播的等级 = 10;
	public static int 规定时间寿命下降值_korea = 1;

	public static long MAX_STORE_EXPS = 4500000000L;
	
	public transient PetProps petProps;

	public Pet() {
	}

	public byte getSpriteType() {
		return Sprite.SPRITE_TYPE_PET;
	}

	// 天生技能额外增加的百分比hp值
	private transient int extraHpC;
	// 未替换的妖魂值
	public transient int tempyaohunScore;
	// 妖魂值
	public transient int yaohunScore;
	// 宠物的种类id，决定了形象 需要存储
	private String category;

	// 兽灵 精怪etc
	private String petSort;

	// 宠物职业倾向 需要存储
	// protected byte career;

	// 宠物的性别，只有公母配对才能繁殖 需要存储
	protected byte sex;

	// 剩余生育的次数
	protected byte breedTimes = 0;

	// 已经生育的次数
	protected byte breededTimes;

	/**
	 * 最大快乐值
	 */
	private int maxHappyness;
	// 宠物快乐值
	protected int happyness;
	/**
	 * 最大寿命值
	 */
	private int maxLifeTime;
	// 宠物的剩下的寿命
	protected int lifeTime;

	// 宠物的性格 忠诚 精明 谨慎 狡诈 (0,1,2,3)
	@SimpleColumn(mysqlName = "character1")
	protected byte character;

	// 宠物的品质，非为5档，0~4：普兽（白）、灵兽（绿）、仙兽（蓝）、神兽（紫）、圣兽（橙）
	protected byte quality;

	// 按算品质公式算积分，用于排行榜
	private int qualityScore;

	// 当前等级的经验值
	// protected long exp = 0;

	// 升到下一级所需的等级
	protected transient long nextLevelExp;

	// 拥有者
	protected long ownerId;
	// 冗余设置ownerId时设置
	protected transient String ownerName = "";

	// 宠物物品实体id
	protected long petPropsId;

	// 宠物实体名
	protected transient String petPropsName;
	
	//飞升110状态
	protected transient byte flyNBState;

	// protected boolean dirty = true;

	// 宠物的星级，分为0~20级
	protected byte starClass;

	// 代数，0或1，分为一代宠和二代宠，只有一代宠才能繁殖
	protected byte generation;

	// 宠物是否变异，0为不变异，否则1-5为变异等级
	protected byte variation;

	// 实际力量资质
	protected transient int strengthQuality;

	// 实际身法资质
	protected transient int dexterityQuality;

	// 实际灵力资质
	protected transient int spellpowerQuality;

	// 实际耐力资质
	protected transient int constitutionQuality;

	// 实际定力资质
	protected transient int dingliQuality;

	// 显示力量资质
	public transient int showStrengthQuality;
	// 显示身法资质
	public transient int showDexterityQuality;

	// 显示灵力资质
	public transient int showSpellpowerQuality;

	// 显示 耐力资质
	public transient int showConstitutionQuality;

	// 显示定力资质
	public transient int showDingliQuality;

	/**
	 * 这只宠物已经达到的最高星等，用于幻化时的广播
	 */
	private int alreadyMaxStar;

	/**
	 * 任务给的宠物蛋 1 普通宠物蛋 0
	 */
	private byte eggType;
	/** 宠物饰品 */
	private long[] ornaments = new long[1];

	// 上一次心跳时的寿命。做提示用
	private transient int lastHeartbeatLiftTime;
	private transient long lastHeartbeatTime;

	public int getShowStrengthQuality() {
		return showStrengthQuality;
	}

	public void setShowStrengthQuality(int showStrengthQuality) {
		this.showStrengthQuality = showStrengthQuality;
	}

	public int getShowDexterityQuality() {
		return showDexterityQuality;
	}

	public void setShowDexterityQuality(int showDexterityQuality) {
		this.showDexterityQuality = showDexterityQuality;
	}

	public int getShowSpellpowerQuality() {
		return showSpellpowerQuality;
	}

	public void setShowSpellpowerQuality(int showSpellpowerQuality) {
		this.showSpellpowerQuality = showSpellpowerQuality;
	}

	public int getShowConstitutionQuality() {
		return showConstitutionQuality;
	}

	public void setShowConstitutionQuality(int showConstitutionQuality) {
		this.showConstitutionQuality = showConstitutionQuality;
	}

	public int getShowDingliQuality() {
		return showDingliQuality;
	}

	public void setShowDingliQuality(int showDingliQuality) {
		this.showDingliQuality = showDingliQuality;
	}

	// ///////一下为宠物的成长属性

	// 携带等级 1 45 90(用于显示);
	protected int trainLevel;

	// 实际携带等级 1 45 90(用于计算);
	protected int realTrainLevel;

	// 能升到的最高等级
	protected int maxLevle;

	// 稀有度，0-随处可见,1-百里挑一,2-千载难逢
	protected byte rarity;

	// 随机生成成长品质 分为5个级别 0~4 : 普通、一般、优秀、卓越、完美(主要刷这个属性，别的属性在生成的时候已经确定)
	protected byte growthClass;

	// 成长值
	protected transient int growth;

	// 为分配的属性点
	protected int unAllocatedPoints;

	protected int expPercent;

	// 上一次更新快乐值的时间
	protected long lastUpdateHappynessTime;
	// 上一次更新寿命值时间
	protected long lastUpdateLifeTime;

	// 上一次收回的时间
	protected long lastPackupTime;

	/**
	 * 激活条件，所谓激活就是攻击敌对生物
	 * 
	 * 0 被动式，表示被攻击或者主人被攻击，准备攻击攻击他或他主人的生物
	 * 1 主动式，表示进入宠物的视野范围，宠物看到敌对生物后，就开始主动攻击，同时优先攻击攻击主人的生物
	 * 2 跟随式，只跟随
	 */
	protected int activationType = 0;

	// 追击的范围宽度，以玩家为中心
	protected int pursueWidth = 640;
	// 追击的范围高度，以玩家为中心
	protected int pursueHeight = 640;

	/**
	 * 怪装备的技能ID列表，如果对应的技能为主动技能，那么就是怪攻击时
	 * 使用的技能。
	 * 
	 * 如果对应的光环类技能，那么就是怪拥有的光环类技能。
	 */
	@SimpleColumn(length = 100)
	private int skillIds[] = new int[1];

	/**
	 * 一代天生技能(先天技能)
	 */
	public int talent1Skill;
	/**
	 * 二代天生技能(后天技能)
	 */
	public int talent2Skill;

	public int wuXing;

	/**
	 * @SimpleColumn(length = 100)
	 */
	public int tianFuSkIds[];
	/**
	 * @SimpleColumn(length = 100)
	 */
	public int tianFuSkIvs[];

	public int grade;
	public int addBookTimes;
	/**
	 * 炼魂经验。
	 */
	public int hunExp;

	/**
	 * 怪装备的技能ID列表，对应的各个技能的级别
	 */
	@SimpleColumn(length = 100)
	protected int activeSkillLevels[] = new int[] { 1, 1, 1, 1 };

	protected transient long lastTimeForBuffs;

	protected transient Fighter currentEnemyTarget;
	/** 宠物生命值高于hpPercent4Hit 造成伤害增加 hitIncreaseRate*/
	private transient int[] hpPercent4Hit = new int[0];
	private transient int[] hitIncreaseRate = new int[0];
	/** 宠物生命值低于hpPercent4Hit 造成伤害增加 hitIncreaseRate*/
	private transient int[] hpPercent4Hit2 = new int[0];
	private transient int[] hitIncreaseRate2 = new int[0];
	/** 减少异常状态时间 */
	private transient int decreaseAbnormalStateTimeRate;
	/** 减少中毒反伤伤害 */
	private transient int decreseSpecialDamage;
	/** 每次攻击又signProb概率触发标记，5s内触发第二次标记造成目标血上限damageHpRate比例的伤害 */
	private transient int signProb = 0;
	private transient int damageHpRate = 0;
	public static long flagInvalidTime = 5000;		//标记有效时间
	public transient Map<Long, Long> signIds = new ConcurrentHashMap<Long, Long>();	//标记的id  key=id value=上次标记的时间
	/** 血量没降低百分之hpDecreaseRate 反伤比例增加 addAntiRate 增加上限maxAddAntiRate（默认为没有上限）*/
	private transient int hpDecreaseRate = 0;
	private transient int addAntiRate = 0;
	private transient int maxAddAntiRate = -1;
	/** 免疫负面效果状态   2为免疫定身   （目前仅实现了免疫定身） */
	private transient byte immuType = 0;

	/**
	 * 人物身体上的Buff，这个数组的下标对应buffType 故，同一个buffType的buff在人物身上只能有一个
	 * 
	 * 此数据是要存盘的
	 * 
	 */

	protected ArrayList<Buff> buffs = new ArrayList<Buff>();

	/**
	 * 下一次心跳要通知客户端去除的buff
	 */
	protected transient ArrayList<Buff> removedBuffs = new ArrayList<Buff>();

	/**
	 * 下一次心跳要去通知客户端新增加的buff
	 */
	protected transient ArrayList<Buff> newlyBuffs = new ArrayList<Buff>();

	/**
	 * 这个宠物拥有的技能
	 */
	protected transient ArrayList<ActiveSkill> skillList = new ArrayList<ActiveSkill>();

	// 技能代理，怪使用技能的代理
	protected transient CountTimesSkillAgent skillAgent = new CountTimesSkillAgent(this);

	// 战斗代理
	protected transient PetFightingAgent fightingAgent = new PetFightingAgent(this);

	protected transient AuraSkillAgent auraSkillAgent = new AuraSkillAgent(this);

	protected transient AuraSkill auraSkill = null;

	// 宠物的内部状态，注意此状态, 0表示空闲，1表示激活，2表示返回主人身边中
	protected transient int innerState = 0;

	/**
	 * 被打死的时间
	 */
	protected transient long killedTime = 0;

	protected transient long disapearTime = 0;

	/**
	 * 宠物的主人
	 */
	private transient Player master;

	/**
	 * 宠物主人，用做宠物触发持续掉血buff判断宠物攻击类型
	 */
	private transient Player tempMaster;

	/**
	 * 第二版宠物技能产生的buff。
	 * 康建虎 2013年9月1日13:13:41
	 */
	public transient GenericBuff pet2buff;
	/**
	 * 对幽冥幻域内所有怪物造成1.5倍伤害
	 */
	public transient int towerRate;
	/**
	 * 有几率产生3倍伤害。
	 */
	public transient int dmgScaleRate;
	public transient int dmgScale;
	/**
	 * 多加一个参数，避免先后天暴击冲突
	 */
	public transient int dmgScaleRate2;
	public transient int dmgScale2;
	/** 额外增加的移动速度 */
	public transient int extraSpeedNum;
	/**
	 * 带给主人的经验加成。
	 */
	public transient int exp2playerRatio;
	public transient Map<Integer, Integer> specialTargetFactor;

	public Player getMaster() {
		return master;
	}

	public void setMaster(Player p) {
		// 宠物收回时会设置为null
		master = p;
		if (p != null) {
			tempMaster = p;
		}
	}

	public AuraSkillAgent getAuraSkillAgent() {
		return auraSkillAgent;
	}

	public void setAuraSkillAgent(AuraSkillAgent auraSkillAgent) {
		this.auraSkillAgent = auraSkillAgent;
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

	/**
	 * 通过buff的templateId获得buff
	 * @return
	 */
	public Buff getBuff(int templateId) {
		for (Buff b : buffs) {
			if (b.getTemplateId() == templateId) {
				return b;
			}
		}
		return null;
	}

	public ArrayList<Buff> getRemovedBuffs() {
		return this.removedBuffs;
	}

	public ArrayList<Buff> getNewlyBuffs() {
		return this.newlyBuffs;
	}

	private PetHookInfo hookInfo;

	/**
	 * 宠物挂机状态,如果不为NULL 则正在挂机
	 * @return
	 */
	public PetHookInfo getHookInfo() {
		return hookInfo;
	}

	public void setHookInfo(PetHookInfo hookInfo) {
		this.hookInfo = hookInfo;
		PetManager.em.notifyFieldChange(this, "hookInfo");
	}

	public void setHp(int hp) {
		super.setHp(hp);
		PetManager.em.notifyFieldChange(this, "hp");
	}
	private transient NewAuraSkillAgent nagent = new NewAuraSkillAgent(this);
	
	/**
	 * 计算标记的伤害
	 * @return
	 */
	public int check4SingDamage(Fighter target) {
		if (signProb <= 0) {
			return 0;
		}
		if (target instanceof BossMonster || target instanceof NPC) {
			return 0;
		}
		int result = 0;
		int ran = random.nextInt(100);
		if (ran <= signProb) {
			long now = System.currentTimeMillis();
			Long lastTime = signIds.get(target.getId());
			if (lastTime != null && (lastTime+flagInvalidTime) > now) {
				signIds.put(target.getId(), (long) 0);
				result = (int) (target.getMaxHP() * ((float)damageHpRate / 100f));
			} else {
				signIds.put(target.getId(), now);
			}
		}
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[增加标记伤害] [" + this.getId() + "] [target:" + target.getId() + "] [ran:" + ran + "] [signProb:" + signProb + "] [result:" + result + "]");
		}
		return result;
	}

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init() {

		//宠物经验修改
		if(this.getExp() > MAX_STORE_EXPS){
			PetManager.logger.warn("[修改宠物经验] [拥有者id:"+ownerId+"] [宠物拥有经验:"+this.getExp()+"] [宠物:"+this.getName()+"] [宠物id:"+this.getId()+"] [等级:"+this.getLevel()+"] [品质:" + quality + "] [携带等级:" + realTrainLevel + "] [星级:" + starClass + "] [代数:" + generation + "]");
			if(this.getLevel() >= 220){
				this.setExp(MAX_STORE_EXPS);
			}
		}
		if (this.getRealTrainLevel() == 0) {
			// 以前的宠物没有真实携带等级
			this.setRealTrainLevel(this.getTrainLevel());

		} else if (realTrainLevel == 220) {
			setRealTrainLevel(225);
			Pet2Manager.log.info("Pet.init: 修正数值等级为225 petid {}, name {}", id, name);
		}
		// 计算初始值
		this.nextLevelExp = PetExperienceManager.getInstance().maxExpOfLevel[level];
		Pet2PropCalc inst = Pet2PropCalc.getInst();
		try {
			if (grade <= 0) {
				setGrade(1);
			}
			hpStealPercent = 0;
			extraHpC = 0;
			antiInjuryRate = 0;
			hpRecoverExtend = 0;
			hpRecoverBase = 0;
			maxHPC = 0;
			phyAttackC = 0;
			phyDefenceC = 0;
			dodgeC = 0;
			criticalHitC = 0;
			magicAttackC = 0;
			magicDefenceC = 0;
			hitC = 0;
			criticalHitB = 0;
			dodgeRateOther = 0;
			criticalHitRateOther = 0;
			hitRateOther = 0;
			hpRecoverExtend2 = 0;
			hpPercent4Hit = new int[0];
			hitIncreaseRate = new int[0];
			setHpPercent4Hit2(new int[0]);
			setHitIncreaseRate2(new int[0]);
			decreaseAbnormalStateTimeRate = 0;
			decreseSpecialDamage = 0;
			signProb = 0;
			damageHpRate = 0;
			hpDecreaseRate = 0;
			addAntiRate = 0;
			maxAddAntiRate = 0;
			immuType = 0;
			lasthpRecoverExtend2Time = -1;
			lastReliveTime = -1;
			lastRemoveDebuffTime = -1;
			attributeAddRate = -1;
			
			nagent.setSkillId(-1);
			checkJiChuSkLv();
			Pet2SkillCalc.getInst().initSkill(this);
			this.critFactor = 200 + inst.sumPoint(this, GenericBuff.ATT_BaoJiXiShu);
			// 用于宠物五大属性的显示，这些属性没有参与战斗属性的计算。战斗属性是直接一套公式算出来的，没有基于这些属性。
			setStrengthB(inst.calcStr(this) + inst.sumPoint(this, GenericBuff.ATT_LiLiang));
			setDexterityB(inst.calcShenFa(this) + inst.sumPoint(this, GenericBuff.ATT_ShenFa));
			setSpellpowerB(inst.calcLinLi(this) + inst.sumPoint(this, GenericBuff.ATT_LinLi));
			setConstitutionB(inst.calcNaiLi(this) + inst.sumPoint(this, GenericBuff.ATT_NaiLi));
			setDingliB(inst.calcDingLi(this) + inst.sumPoint(this, GenericBuff.ATT_DingLi));
			//
			setStrengthC(inst.sumPercent(this, GenericBuff.ATT_LiLiang));
			setDexterityC(inst.sumPercent(this, GenericBuff.ATT_ShenFa));
			setSpellpowerC(inst.sumPercent(this, GenericBuff.ATT_LinLi));
			setConstitutionC(inst.sumPercent(this, GenericBuff.ATT_NaiLi));
			setDingliC(inst.sumPercent(this, GenericBuff.ATT_DingLi));
		} catch (Exception e) {
			Pet2Manager.log.error("err pet id {}, name {}", getId(), getName());
			Pet2Manager.log.error("初始化技能失败", e);
		}
		// 删除未确定的临时加点
		initS();
		this.setStrengthA(PetPropertyUtility.计算力量值A(this));
		this.setDexterityA(PetPropertyUtility.计算身法值A(this));
		this.setSpellpowerA(PetPropertyUtility.计算灵力值A(this));
		this.setConstitutionA(PetPropertyUtility.计算耐力值A(this));
		this.setDingliA(PetPropertyUtility.计算定力值A(this));

		this.setStrengthS(this.getStrengthS());
		this.setDexterityS(this.getDexterityS());
		this.setSpellpowerS(this.getSpellpowerS());
		this.setConstitutionS(this.getConstitutionS());
		this.setDingliS(this.getDingliS());
		Logger log = PetManager.logger;
		try {
			long totalAddPoint = 0;
			totalAddPoint += getUnAllocatedPoints();
			totalAddPoint += getStrengthS();
			totalAddPoint += getDexterityS();
			totalAddPoint += getSpellpowerS();
			totalAddPoint += getConstitutionS();
			totalAddPoint += getDingliS();
			if (// totalAddPoint != (this.getLevel() - 1) * 5 || //有了进阶后这个就不准了。
			getUnAllocatedPoints() < 0 || getUnAllocatedPoints() > 10000 || getStrengthS() > 10000 || getDexterityS() > 100000 || getSpellpowerS() > 10000 || getConstitutionS() > 10000 || getDingliS() > 10000) {
				log.warn("[宠物属性异常] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[] { getLogString(), totalAddPoint, getUnAllocatedPoints(), getStrengthS(), getDexterityS(), getSpellpowerS(), getConstitutionS(), getDingliS(), getOwnerId(), getOwnerName() });
				setUnAllocatedPoints((this.getLevel() - 1) * 5);
				this.setStrengthS(0);
				this.setDexterityS(0);
				this.setSpellpowerS(0);
				this.setConstitutionS(0);
				this.setDingliS(0);
			}
		} catch (Exception e) {
			log.error("宠物清理", e);
		}

		this.strengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 2);
		this.dexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 2);
		this.spellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 2);
		this.constitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 2);
		this.dingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 2);

		this.showStrengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 2);
		this.showDexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 2);
		this.showSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 2);
		this.showConstitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 2);
		this.showDingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 2);

		calcShowAttMinMax();

		// this.growth = (int)Math.rint((PetPropertyUtility.得到实际成长值(this)/(100*100)));
		if (ornaments != null) {
			for (int i=0; i<ornaments.length; i++) {
				long ornaId = ornaments[i];
				try {
					if (ornaId > 0) {
						ArticleEntity aee = ArticleEntityManager.getInstance().getEntity(ornaId);
						if (aee instanceof PetSuitArticleEntity) {
							PetSuitArticle psa = (PetSuitArticle) ArticleManager.getInstance().getArticle(aee.getArticleName());
							for (int ii=0; ii<psa.getEffectType().length; ii++) {
								SuitEffectEnum see  = psa.getEffectType()[ii];
								see.getEffect().effect(this, (PetSuitArticleEntity) aee, ii);
							}
						}
					}
				} catch (Exception e) {
					log.warn("[宠物饰品] [加载宠物饰品异常] [petId:" + this.getId() + "] [aeId:" + ornaId + "]", e);
				}
			}
		}
		this.reinitFightingProperties(true);
		inst.calcFightingAtt(this, Pet2Manager.log);

		if (log.isDebugEnabled()) {
			log.debug("[" + this.getLogString() + "] [力量:" + getStrengthA() + "] [身法:" + getDexterityA() + "] [灵力:" + getSpellpowerA() + "] [耐力:" + getConstitutionA() + "] [定力:" + getDingliA() + "]");
			log.debug("[" + this.getLogString() + "] [力量资质:" + getStrengthQuality() + "] [身法资质:" + getDexterityQuality() + "] [灵力资质:" + getSpellpowerQuality() + "] [耐力资质:" + getConstitutionQuality() + "] [定力资质:" + getDingliQuality() + "]");
		}

		if (this.getHp() < 0) {
			setHp((int) (this.getMaxHP() / 2));
		}
		CareerManager cm = CareerManager.getInstance();

		if (cm != null) {
			boolean bln = true;
			for (int idd : skillIds) {
				if (idd != 0) {
					bln = false;
					break;
				}
			}
			if (!bln) {
				for (int i = 0; i < skillIds.length; i++) {
					Skill skill = cm.getSkillById(skillIds[i]);
					if (skill != null && skill instanceof ActiveSkill) {
						skillList.add((ActiveSkill) skill);
					} else if (skill != null && skill instanceof PassiveSkill) {

					} else if (skill != null && skill instanceof AuraSkill) {
						auraSkill = (AuraSkill) skill;
					}
				}
			}

		}
		if (auraSkill != null) {
			this.auraSkillAgent.openAuraSkill(auraSkill);
		}

		Avata a = ResourceManager.getInstance().getAvata(this);
		this.avata = a.avata;
		this.avataType = a.avataType;

		if (this.getPetSort() == null) {
			String category = this.getCategory();
			Article a1 = ArticleManager.getInstance().getArticle(category);
			if (a1 != null && a1 instanceof PetProps) {
				PetProps pp = (PetProps) a1;
				this.setObjectScale((short) 1000);
				String egg = pp.getEggAticleName();
				Article a2 = ArticleManager.getInstance().getArticle(egg);
				if (a2 != null && a2 instanceof PetEggProps) {
					this.setPetSort(a2.get物品二级分类());
				} else {
					log.error("[初始化pet分类错误] [没有宠物蛋道具] [" + egg + "]");
				}
			} else {
				log.error("[初始化pet分类错误] [没有宠物道具] [" + category + "]");
			}
		}

		// 修改合体后没加上的技能级别
		if (this.getSkillIds().length != this.getActiveSkillLevels().length) {
			int[] skillLevels = new int[this.getSkillIds().length];
			for (int i = 0; i < this.getSkillIds().length; i++) {
				skillLevels[i] = 1;
			}
			setActiveSkillLevels(skillLevels);
		}

		Article petProps = null;
		// 初始化icon
		long petPropsId = this.getPetPropsId();
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(petPropsId);
		log.debug("Pet.init: ArticleEntity is {} ", ae == null ? "null" : ae.getClass().getSimpleName());
		if (ae != null) {// && ae instanceof PetPropsEntity) {
			this.setPetPropsName(ae.getArticleName());
			petProps = ArticleManager.getInstance().getArticle(ae.getArticleName());
			if (petProps != null) {
				this.setIcon(petProps.getIconId());
			} else {
				log.warn("[初始化宠物icon错误A] [article null] [" + ae.getArticleName() + "]");
			}
		} else {
			log.warn("[初始化宠物icon错误B] [不是宠物] [" + (ae != null ? ae.getClass() : "null") + "] [" + petPropsId + "]");
		}
		log.debug("Pet.init: petProps is {} ", petProps == null ? "null" : petProps.getClass().getSimpleName());
		// 初始化大小
		short scale = 1000;
		int color = -1;
		boolean objectOpacity = false;
		String particleName = "";
		PetProps pp = null;
		if (petProps != null) {// &&
			if (petProps instanceof PetProps) {
				pp = (PetProps) petProps;
				log.debug("Pet.init: PetProps 1 {}", pp);
			} else if (petProps instanceof PetEggProps) {
				PetEggProps pep = (PetEggProps) petProps;
				String petArticleName = pep.getPetArticleName();
				Article petArticle = ArticleManager.getInstance().getArticle(petArticleName);
				if (petArticle instanceof PetProps) {
					pp = (PetProps) petArticle;
					log.debug("Pet.init: PetEggProps {} to {}", petProps.getName(), petArticleName);
				} else {
					log.error("Pet.init: error article {}", petArticle);
				}
			}
		}
		if (pp != null) {
			scale = pp.getObjectScale();
			color = pp.getObjectColor();
			objectOpacity = pp.isObjectOpacity();
			particleName = pp.getParticleName();
			this.petProps = pp;
		} else {
			log.error("Pet.init: pet props error {}", petProps);
			log.error("[初始化宠物的大小错误] [" + this.getLogString() + "] [onwerId:" + this.ownerId + "] [" + petProps + "]");
		}
		this.setObjectScale(scale);
		this.setObjectOpacity(objectOpacity);
		this.setParticleName(particleName);
		Pet2Manager.getInst().fixJinJieXingXiang(this);
		if (color != -1) {
			this.setObjectColor(color);
		}
		int oldCommonId = this.getCommonSkillId();
		try {
			int 携带等级 = this.getRealTrainLevel();

			int trainLevelIndex = PetPropertyUtility.获得携带等级索引(携带等级);
			int commonId = -1;
			if (this.getCareer() == PetPropertyUtility.物攻型 || this.getCareer() == PetPropertyUtility.敏捷型) {
				commonId = phySkillIds[trainLevelIndex];
			} else {
				commonId = magicSkillIds[trainLevelIndex];
			}
			if (oldCommonId != commonId) {
				Skill skill = cm.getSkillById(commonId);
				if (skill == null) {
					PetManager.logger.error("[宠物绑定技能错误] [普通技能为null] [技能id:" + commonId + "] [" + this.getLogString() + "]");
					return;
				}
				setCommonSkillId(commonId);
				if (log.isDebugEnabled()) {
					log.debug("[宠物普通技能变化] [petid:" + this.getId() + "] [携带等级:" + 携带等级 + "] [之前普攻id:" + oldCommonId + "] [新的普攻id:" + commonId + "]");
				}
			}
		} catch (Exception e) {
			setCommonSkillId(oldCommonId);
			log.error("[初始化宠物通用技能错误] [petId : " + this.getId() + "] [携带等级:" + this.getRealTrainLevel() + "]", e);
		}

		// 最合适才最好
		// 十全十美仙宠
		try {
			if (this.getOwnerId() > 0) {
				int matchNum = 0;
				for (int skillId : this.getSkillIds()) {
					Map<Integer, PetSkillReleaseProbability> map = PetManager.getInstance().getMap();
					if (map != null) {
						PetSkillReleaseProbability pb = map.get(skillId);
						if (pb != null) {
							if (pb.getCharacter() == this.getCharacter()) {
								++matchNum;
							}
						}
					}
				}
				if (matchNum > 0) {
					if (PlayerManager.getInstance().isOnline(this.getOwnerId())) {
						Player player = PlayerManager.getInstance().getPlayer(this.getOwnerId());
						AchievementManager.getInstance().record(player, RecordAction.宠物对应性格技能数量, matchNum);
						if (AchievementManager.logger.isWarnEnabled()) {
							AchievementManager.logger.warn("[成就统计宠物对应性格技能数量] [" + player.getLogString() + "] [" + matchNum + "]");
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("[宠物初始化加技能成就异常] [" + this.getLogString() + "] [ownerId:" + this.getOwnerId() + "]", e);
		}
		try {
			initPetFlyAvata("init");
		} catch (Exception e) {
			e.printStackTrace();
		}
		initTitle();
		if (this.getOwnerId() > 0 && GamePlayerManager.getInstance().isOnline(this.getOwnerId())) {
			try {
				// if (activeSkillLevels != null && activeSkillLevels.length > 0) {
				// int lv2Num = 0; //高于二级的技能个数
				// int lv3Num = 0;
				// for (int i=0; i<activeSkillLevels.length; i++) {
				// if (activeSkillLevels[i] >= 2) {
				// lv2Num ++ ;
				// if (activeSkillLevels[i] >= 3) {
				// lv3Num ++ ;
				// }
				// }
				// }
				// if (lv2Num > 0) {
				// EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getOwnerId(),
				// RecordAction.宠物拥有2级基础技能个数, lv2Num });
				// EventRouter.getInst().addEvent(evt3);
				// if (lv3Num > 0) {
				// EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getOwnerId(),
				// RecordAction.宠物拥有3级基础技能个数, lv3Num });
				// EventRouter.getInst().addEvent(evt2);
				// }
				// }
				// }
				if (this.petProps != null && this.petProps.getName_stat().indexOf(PlayerAimManager.金宝蛇皇) >= 0) {
					EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getOwnerId(), RecordAction.获得金宝蛇皇, 1L });
					EventRouter.getInst().addEvent(evt2);
				}
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [统计宠物升级技能] [异常] [playerId:" + this.getOwnerId() + "] [pet.id:" + this.getId() + "]", e);
			}
		}
	}

	public void initPetFlyAvata(String reason) {
		PetFlyState pstate = PetManager.getInstance().getPetFlyState(this.getId(), master);
		if (pstate != null) {
			int historyLXValue = pstate.getHistoryLingXingPoint();
			GradePet gradePet = null;
			if (historyLXValue >= 50) {
				ArticleEntity artE = ArticleEntityManager.getInstance().getEntity(this.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					String oldAvata[] = this.getAvata();
					byte[] oldAvataType = this.getAvataType();
					String oldAvataRace = this.getAvataRace();
					String oldavataSex = this.getAvataSex();
					if (gradePet != null) {
						if (gradePet.flyAvata != null && !gradePet.flyAvata.isEmpty() && gradePet.flyAvata.contains("_")) {
							this.setAvataRace(gradePet.flyAvata.split("_")[0]);
							this.setAvataSex(gradePet.flyAvata.split("_")[1]);
							Avata a = ResourceManager.getInstance().getAvata(this);
							this.setAvata(a.avata);
							this.setAvataType(a.avataType);
							this.setIcon(gradePet.flyIcon);
						}
					}
					PetManager.logger.warn("[更新宠物飞升形象] [成功] [" + reason + "] [" + this.getName() + "] [oldAvata:" + Arrays.toString(oldAvata) + "] [oldAvataType:" + Arrays.toString(oldAvataType) + "] [oldAvataRace:" + oldAvataRace + "] [oldavataSex:" + oldavataSex + "] [historyLXValue:" + historyLXValue + "] [" + (gradePet == null) + "] [avata:" + Arrays.toString(this.getAvata()) + "] [AvataType:" + Arrays.toString(this.getAvataType()) + "] [" + this.getAvataRace() + "] [" + this.getAvataSex() + "]");
				}
			}
		}
	}

	public synchronized void initFlySkill(int handleType, String reason) {
		if (tempMaster == null) {
			PetManager.logger.warn("[宠物计算飞升属性] [错误:主人为空] [宠物名:" + this.getName() + "] [id:" + this.getId() + "] [reason:" + reason + "]");
			return;
		}
		long now = System.currentTimeMillis();
		PetFlyState state = PetManager.getInstance().getPetFlyState(this.getId(),"飞升技能");
		if (state != null && state.getSkillId() > 0) {
			PetFlySkillInfo skillInfo = PetManager.petFlySkills.get(state.getSkillId());
			if (skillInfo != null) {
				SkillProp skills[] = skillInfo.skills;
				for (SkillProp sk : skills) {
					if (sk != null && sk.effectTarget > 0) {
						int addNum = sk.propValue;
//						if (sk.valueType == 1) {
//							addNum = getPetFlySkillProp(master, sk.propType, sk.effectTarget, sk.propValue);
//						} else if (sk.valueType == 2) {
//						}
						if (handleType == 1) {
							addPetFlySkillProp(tempMaster, sk.propType, addNum, sk.effectTarget);
						} 
						else if (handleType == 2) {
							minusPetFlySkillProp(tempMaster, sk.propType, addNum, sk.effectTarget);
						}
						String targetStr = "";
						if (sk.effectTarget == 1) {
							targetStr = "宠物本身";
						} else if (sk.effectTarget == 2) {
							targetStr = "主人";
						} else if (sk.effectTarget == 3) {
							targetStr = "主人和宠物";
						}
						String propStr = String.valueOf(sk.propType);
						if (sk.propType < MagicWeaponConstant.skillStr.length) {
							propStr = MagicWeaponConstant.skillStr[sk.propType];
						}
						PetManager.logger.warn("[宠物计算飞升属性] [" + reason + "] [主人:" + (tempMaster == null ? "nul" :tempMaster.getName()) + "] [宠物名:" + this.getName() + "] [id:" + this.getId() + "] [" + (handleType == 1 ? "加属性" : "减属性") + "] [值:" + addNum + "] " + "[属性类型:" + propStr + "] [值类型:" + (sk.valueType == 1 ? "百分比" : "值") + "] [作用目标:" + targetStr + "] [cost:"+(System.currentTimeMillis()-now)+"ms]");
					}
				}
			} else {
				PetManager.logger.warn("[宠物计算飞升属性] [出错:查看技能表id是否修改] [handleType:" + handleType + "] [宠物名:" + this.getName() + "] [id:" + this.getId() + "] [" + skillInfo + "]");
			}
		}
	}

	public int getPetFlySkillProp(Player player, int addType, int addTarget, int percentNum) {
		if (player == null) {
			return 0;
		}
		double addNum = 0;
		int oldValue = 0;
		switch (addType) {
		case MagicWeaponConstant.hpNum: // 血
			if (addTarget == 2) {
				oldValue = player.getMaxHP();
				addNum = player.getMaxHP() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getMaxHP();
				addNum = this.getMaxHP() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.physicAttNum: // 外攻
			if (addTarget == 2) {
				oldValue = player.getPhyAttack();
				addNum = player.getPhyAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getPhyAttack();
				addNum = this.getPhyAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.physicDefanceNum: // 外防
			if (addTarget == 2) {
				oldValue = player.getPhyDefence();
				addNum = player.getPhyDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getPhyDefence();
				addNum = this.getPhyDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.破甲: // 破甲
			if (addTarget == 2) {
				oldValue = player.getBreakDefence();
				addNum = player.getBreakDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getBreakDefence();
				addNum = this.getBreakDefence() * ((double)percentNum / 100);
			}
			break;

		case MagicWeaponConstant.dodgeNum: // 闪避
			if (addTarget == 2) {
				oldValue = player.getDodge();
				addNum = player.getDodge() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getDodge();
				addNum = this.getDodge() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.cirtNum: // 暴击
			if (addTarget == 2) {
				oldValue = player.getCriticalHit();
				addNum = player.getCriticalHit() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getCriticalHit();
				addNum = this.getCriticalHit() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.mpNum: // 仙法
			if (addTarget == 2) {
				oldValue = player.getMaxMP();
				addNum = player.getMaxMP() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getMaxMP();
				addNum = this.getMaxMP() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.magicAttNum: // 法攻
			if (addTarget == 2) {
				oldValue = player.getMagicAttack();
				addNum = player.getMagicAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getMagicAttack();
				addNum = this.getMagicAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.magicDefanceNum: // 法防
			if (addTarget == 2) {
				oldValue = player.getMagicDefence();
				addNum = player.getMagicDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getMagicDefence();
				addNum = this.getMagicDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.hitNum: // 命中
			if (addTarget == 2) {
				oldValue = player.getHit();
				addNum = player.getHit() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getHit();
				addNum = this.getHit() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.精准: // 精准
			if (addTarget == 2) {
				oldValue = player.getAccurate();
				addNum = player.getAccurate() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getAccurate();
				addNum = this.getAccurate() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.免爆: // 免爆
			if (addTarget == 2) {
				oldValue = player.getCriticalDefence();
				addNum = player.getCriticalDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getCriticalDefence();
				addNum = this.getCriticalDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.火攻: // 火攻
			if (addTarget == 2) {
				oldValue = player.getFireAttack();
				addNum = player.getFireAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getFireAttack();
				addNum = this.getFireAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.火炕: // 火炕
			if (addTarget == 2) {
				oldValue = player.getFireDefence();
				addNum = player.getFireDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getFireDefence();
				addNum = this.getFireDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.火减抗: // 外攻
			if (addTarget == 2) {
				oldValue = player.getFireIgnoreDefence();
				addNum = player.getFireIgnoreDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getFireIgnoreDefence();
				addNum = this.getFireIgnoreDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.风攻: // 外攻
			if (addTarget == 2) {
				oldValue = player.getWindAttack();
				addNum = player.getWindAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getWindAttack();
				addNum = this.getWindAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.风抗: // 风抗
			if (addTarget == 2) {
				oldValue = player.getWindDefence();
				addNum = player.getWindDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getWindDefence();
				addNum = this.getWindDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.风减抗: // 风减抗
			if (addTarget == 2) {
				oldValue = player.getWindIgnoreDefence();
				addNum = player.getWindIgnoreDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getWindIgnoreDefence();
				addNum = this.getWindIgnoreDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.冰攻: // 冰攻
			if (addTarget == 2) {
				oldValue = player.getBlizzardAttack();
				addNum = player.getBlizzardAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getBlizzardAttack();
				addNum = this.getBlizzardAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.冰抗: // 冰抗
			if (addTarget == 2) {
				oldValue = player.getBlizzardDefence();
				addNum = player.getBlizzardDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getBlizzardDefence();
				addNum = this.getBlizzardDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.冰减抗: // 冰减抗
			if (addTarget == 2) {
				oldValue = player.getBlizzardIgnoreDefence();
				addNum = player.getBlizzardIgnoreDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getBlizzardIgnoreDefence();
				addNum = this.getBlizzardIgnoreDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.雷攻: // 雷攻
			if (addTarget == 2) {
				oldValue = player.getThunderAttack();
				addNum = player.getThunderAttack() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getThunderAttack();
				addNum = this.getThunderAttack() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.雷抗: // 雷抗
			if (addTarget == 2) {
				oldValue = player.getThunderDefence();
				addNum = player.getThunderDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getThunderDefence();
				addNum = this.getThunderDefence() * ((double)percentNum / 100);
			}
			break;
		case MagicWeaponConstant.雷减抗: // 雷减抗
			if (addTarget == 2) {
				oldValue = player.getThunderIgnoreDefence();
				addNum = player.getThunderIgnoreDefence() * ((double)percentNum / 100);
			} else if (addTarget == 1) {
				oldValue = this.getThunderIgnoreDefence();
				addNum = this.getThunderIgnoreDefence() * ((double)percentNum / 100);
			}
			break;

		default:
			break;
		}
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[获取宠物飞升技能属性] [addType:" + addType + "] [oldValue:"+oldValue+"] [addNum:" + (int)addNum + "] [percentNum:"+percentNum+"] [addTarget:" + addTarget + "]" + player.getPlayerPropsString() + this.getPetPropsString());
		}
		return (int)addNum;
	}

	/**
	 * 增加宠物飞升技能属性
	 * @param addType
	 *            增加属性的类型
	 * @param player
	 * @param addTarget
	 *            1:宠物本身，2:主人，3:宠物和主人
	 * @param handleType
	 *            1:增加属性，2:减少属性
	 */
	public void addPetFlySkillProp(Player player, int addType, int addNum, int addTarget) {
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[增加宠物飞升技能前] [addType:" + addType + "] [addNum:" + addNum + "] [addTarget:" + addTarget + "]" + (player == null ? "null" : player.getPlayerPropsString()) + this.getPetPropsString());
		}
		if (player == null) {
			return;
		}
		switch (addType) {
		case MagicWeaponConstant.hpNum: // 血
			if (addTarget == 3) {
				player.setMaxHPC(player.getMaxHPC() + addNum);
				this.setMaxHPC(this.getMaxHPC() + addNum);
			} else if (addTarget == 2) {
				player.setMaxHPC(player.getMaxHPC() + addNum);
			} else if (addTarget == 1) {
				this.setMaxHPC(this.getMaxHPC() + addNum);
			}
			break;
		case MagicWeaponConstant.physicAttNum: // 外攻
			if (addTarget == 3) {
				this.setPhyAttackC(this.getPhyAttackC() + addNum);
				player.setPhyAttackC(player.getPhyAttackC() + addNum);
				this.setShowPhyAttack(this.getPhyAttack());
			} else if (addTarget == 2) {
				player.setPhyAttackC(player.getPhyAttackC() + addNum);
			} else if (addTarget == 1) {
				this.setPhyAttackC(this.getPhyAttackC() + addNum);
				this.setShowPhyAttack(this.getPhyAttack());
			}
			break;
		case MagicWeaponConstant.physicDefanceNum: // 外防
			if (addTarget == 3) {
				this.setPhyDefenceC(this.getPhyDefenceC() + addNum);
				player.setPhyDefenceC(player.getPhyDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setPhyDefenceC(player.getPhyDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setPhyDefenceC(this.getPhyDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.破甲: // 破甲
			if (addTarget == 3) {
				this.setBreakDefenceC(this.getBreakDefenceC() + addNum);
				player.setBreakDefenceC(player.getBreakDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setBreakDefenceC(player.getBreakDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setBreakDefenceC(this.getBreakDefenceB() + addNum);
			}
			break;

		case MagicWeaponConstant.dodgeNum: // 闪避
			if (addTarget == 3) {
				this.setDodgeC(this.getDodgeC() + addNum);
				player.setDodgeC(player.getDodgeC() + addNum);
			} else if (addTarget == 2) {
				player.setDodgeC(player.getDodgeC() + addNum);
			} else if (addTarget == 1) {
				this.setDodgeC(this.getDodgeC() + addNum);
			}
			break;
		case MagicWeaponConstant.cirtNum: // 暴击
			if (addTarget == 3) {
				this.setCriticalHitC(this.getCriticalHitC() + addNum);
				player.setCriticalHitC(player.getCriticalHitC() + addNum);
			} else if (addTarget == 2) {
				player.setCriticalHitC(player.getCriticalHitC() + addNum);
			} else if (addTarget == 1) {
				this.setCriticalHitC(this.getCriticalHitC() + addNum);
			}
			break;
		case MagicWeaponConstant.mpNum: // 仙法
			if (addTarget == 3) {
				this.setMaxMPC(this.getMaxMPC() + addNum);
				player.setMaxMPB(player.getMaxMPB() + addNum);
			} else if (addTarget == 2) {
				player.setMaxMPB(player.getMaxMPB() + addNum);
			} else if (addTarget == 1) {
				this.setMaxMPC(this.getMaxMPC() + addNum);
			}
			break;
		case MagicWeaponConstant.magicAttNum: // 法攻
			if (addTarget == 3) {
				this.setMagicAttackC(this.getMagicAttackC() + addNum);
				this.setShowMagicAttack(this.getMagicAttack());
				player.setMagicAttackC(player.getMagicAttackC() + addNum);
			} else if (addTarget == 2) {
				player.setMagicAttackC(player.getMagicAttackC() + addNum);
			} else if (addTarget == 1) {
				this.setMagicAttackC(this.getMagicAttackC() + addNum);
				this.setShowMagicAttack(this.getMagicAttack());
			}
			break;
		case MagicWeaponConstant.magicDefanceNum: // 法防
			if (addTarget == 3) {
				this.setMagicDefenceC(this.getMagicDefenceC() + addNum);
				player.setMagicDefenceC(player.getMagicDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setMagicDefenceC(player.getMagicDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setMagicDefenceC(this.getMagicDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.hitNum: // 命中
			if (addTarget == 3) {
				this.setHitC(this.getHitC() + addNum);
				player.setHitC(player.getHitC() + addNum);
			} else if (addTarget == 2) {
				player.setHitC(player.getHitC() + addNum);
			} else if (addTarget == 1) {
				this.setHitC(this.getHitC() + addNum);
			}
			break;
		case MagicWeaponConstant.精准: // 精准
			if (addTarget == 3) {
				this.setAccurateC(this.getAccurateC() + addNum);
				player.setAccurateC(player.getAccurateC() + addNum);
			} else if (addTarget == 2) {
				player.setAccurateC(player.getAccurateC() + addNum);
			} else if (addTarget == 1) {
				this.setAccurateC(this.getAccurateC() + addNum);
			}
			break;
		case MagicWeaponConstant.免爆: // 免爆
			if (addTarget == 3) {
				this.setCriticalDefenceC(this.getCriticalDefenceC() + addNum);
				player.setCriticalDefenceC(player.getCriticalDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setCriticalDefenceC(player.getCriticalDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setCriticalDefenceC(this.getCriticalDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.火攻: // 火攻
			if (addTarget == 3) {
				this.setFireAttackC(this.getFireAttackC() + addNum);
				player.setFireAttackC(player.getFireAttackC() + addNum);
			} else if (addTarget == 2) {
				player.setFireAttackC(player.getFireAttackC() + addNum);
			} else if (addTarget == 1) {
				this.setFireAttackC(this.getFireAttackC() + addNum);
			}
			break;
		case MagicWeaponConstant.火炕: // 火炕
			if (addTarget == 3) {
				this.setFireDefenceC(this.getFireDefenceC() + addNum);
				player.setFireDefenceC(player.getFireDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setFireDefenceC(player.getFireDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setFireDefenceC(this.getFireDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.火减抗: // 外攻
			if (addTarget == 3) {
				this.setFireIgnoreDefenceC(this.getFireIgnoreDefenceC() + addNum);
				player.setFireIgnoreDefenceC(player.getFireIgnoreDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setFireIgnoreDefenceC(player.getFireIgnoreDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setFireIgnoreDefenceC(this.getFireIgnoreDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.风攻: // 外攻
			if (addTarget == 3) {
				this.setWindAttackC(this.getWindAttackC() + addNum);
				player.setWindAttackC(player.getWindAttackC() + addNum);
			} else if (addTarget == 2) {
				player.setWindAttackC(player.getWindAttackC() + addNum);
			} else if (addTarget == 1) {
				this.setWindAttackC(this.getWindAttackC() + addNum);
			}
			break;
		case MagicWeaponConstant.风抗: // 风抗
			if (addTarget == 3) {
				this.setWindDefenceC(this.getWindDefenceC() + addNum);
				player.setWindDefenceC(player.getWindDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setWindDefenceC(player.getWindDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setWindDefenceC(this.getWindDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.风减抗: // 风减抗
			if (addTarget == 3) {
				this.setWindIgnoreDefenceC(this.getWindIgnoreDefenceC() + addNum);
				player.setWindIgnoreDefenceC(player.getWindIgnoreDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setWindIgnoreDefenceC(player.getWindIgnoreDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setWindIgnoreDefenceC(this.getWindIgnoreDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.冰攻: // 冰攻
			if (addTarget == 3) {
				this.setBlizzardAttackC(this.getBlizzardAttackC() + addNum);
				player.setBlizzardAttackC(player.getBlizzardAttackC() + addNum);
			} else if (addTarget == 2) {
				player.setBlizzardAttackC(player.getBlizzardAttackC() + addNum);
			} else if (addTarget == 1) {
				this.setBlizzardAttackC(this.getBlizzardAttackC() + addNum);
			}
			break;
		case MagicWeaponConstant.冰抗: // 冰抗
			if (addTarget == 3) {
				this.setBlizzardDefenceB(this.getBlizzardDefenceB() + addNum);
				player.setBlizzardDefenceB(player.getBlizzardDefenceB() + addNum);
			} else if (addTarget == 2) {
				player.setBlizzardDefenceB(player.getBlizzardDefenceB() + addNum);
			} else if (addTarget == 1) {
				this.setBlizzardDefenceB(this.getBlizzardDefenceB() + addNum);
			}
			break;
		case MagicWeaponConstant.冰减抗: // 冰减抗
			if (addTarget == 3) {
				this.setBlizzardIgnoreDefenceB(this.getBlizzardIgnoreDefenceB() + addNum);
				player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + addNum);
			} else if (addTarget == 2) {
				player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + addNum);
			} else if (addTarget == 1) {
				this.setBlizzardIgnoreDefenceB(this.getBlizzardIgnoreDefenceB() + addNum);
			}
			break;
		case MagicWeaponConstant.雷攻: // 雷攻
			if (addTarget == 3) {
				this.setThunderAttackB(this.getThunderAttackB() + addNum);
				player.setThunderAttackB(player.getThunderAttackB() + addNum);
			} else if (addTarget == 2) {
				player.setThunderAttackB(player.getThunderAttackB() + addNum);
			} else if (addTarget == 1) {
				this.setThunderAttackB(this.getThunderAttackB() + addNum);
			}
			break;
		case MagicWeaponConstant.雷抗: // 雷抗
			if (addTarget == 3) {
				this.setThunderDefenceC(this.getThunderDefenceC() + addNum);
				player.setThunderDefenceB(player.getThunderDefenceB() + addNum);
			} else if (addTarget == 2) {
				player.setThunderDefenceB(player.getThunderDefenceB() + addNum);
			} else if (addTarget == 1) {
				this.setThunderDefenceC(this.getThunderDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.雷减抗: // 雷减抗
			if (addTarget == 3) {
				this.setThunderIgnoreDefenceC(this.getThunderIgnoreDefenceC() + addNum);
				player.setThunderIgnoreDefenceC(player.getThunderIgnoreDefenceC() + addNum);
			} else if (addTarget == 2) {
				player.setThunderIgnoreDefenceC(player.getThunderIgnoreDefenceC() + addNum);
			} else if (addTarget == 1) {
				this.setThunderIgnoreDefenceC(this.getThunderIgnoreDefenceC() + addNum);
			}
			break;
		case MagicWeaponConstant.移动速度: // 雷减抗
			if (addTarget == 3) {
				this.setSpeedA(this.getSpeedA() + addNum);
				player.setSpeedA(player.getSpeedA() + addNum);
			} else if (addTarget == 2) {
				player.setSpeedA(player.getSpeedA() + addNum);
			} else if (addTarget == 1) {
				this.setSpeedA(this.getSpeedA() + addNum);
			}
			break;
		default:
			break;
		}
		//同步数据
		long seq = GameMessageFactory.nextSequnceNum();
		PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, this.getId());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(oldReq, player);
		PET2_QUERY_RES res = Pet2Manager.getInst().makeNewPetQueryRes(seq, oldRes);
		PET2_LiZi_RES ret = Pet2Manager.getInst().makePetInfoWithLizi(this, seq, res);
		player.addMessageToRightBag(ret);
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[增加宠物飞升技能后] [addType:" + addType + "] [addNum:" + addNum + "] [addTarget:" + addTarget + "]" + player.getPlayerPropsString() + this.getPetPropsString());
		}
	}

	public void minusPetFlySkillProp(Player player, int addType, int addNum, int addTarget) {
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[减去宠物飞升技能前] [addType:" + addType + "] [addNum:" + addNum + "] [addTarget:" + addTarget + "]" + (player == null ? "null" : player.getPlayerPropsString()) + this.getPetPropsString());
		}
		if(addNum <= -100){
			addNum = 0;
		}
		if (player == null) {
			return;
		}
		switch (addType) {
		case MagicWeaponConstant.hpNum: // 血
			if (addTarget == 3) {
				this.setMaxHPC(this.getMaxHPC() - addNum);
				player.setMaxHPC(player.getMaxHPC() - addNum);
			} else if (addTarget == 2) {
				player.setMaxHPC(player.getMaxHPC() - addNum);
			} else if (addTarget == 1) {
				this.setMaxHPC(this.getMaxHPC() - addNum);
			}
			break;
		case MagicWeaponConstant.physicAttNum: // 外攻
			if (addTarget == 3) {
				this.setPhyAttackC(this.getPhyAttackC() - addNum);
				this.setShowPhyAttack(this.getPhyAttack());
				player.setPhyAttackC(player.getPhyAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setPhyAttackC(player.getPhyAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setPhyAttackC(this.getPhyAttackC() - addNum);
				this.setShowPhyAttack(this.getPhyAttack());
			}
			break;
		case MagicWeaponConstant.physicDefanceNum: // 外防
			if (addTarget == 3) {
				this.setPhyDefenceC(this.getPhyDefenceC() - addNum);
				player.setPhyDefenceC(player.getPhyDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setPhyDefenceC(player.getPhyDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setPhyDefenceC(this.getPhyDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.破甲: // 破甲
			if (addTarget == 3) {
				this.setBreakDefenceC(this.getBreakDefenceC() - addNum);
				player.setBreakDefenceC(player.getBreakDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setBreakDefenceC(player.getBreakDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setBreakDefenceC(this.getBreakDefenceC() - addNum);
			}
			break;

		case MagicWeaponConstant.dodgeNum: // 闪避
			if (addTarget == 3) {
				this.setDodgeC(this.getDodgeC() - addNum);
				player.setDodgeC(player.getDodgeC() - addNum);
			} else if (addTarget == 2) {
				player.setDodgeC(player.getDodgeC() - addNum);
			} else if (addTarget == 1) {
				this.setDodgeC(this.getDodgeC() - addNum);
			}
			break;
		case MagicWeaponConstant.cirtNum: // 暴击
			if (addTarget == 3) {
				this.setCriticalHitC(this.getCriticalHitC() - addNum);
				player.setCriticalHitC(player.getCriticalHitC() - addNum);
			} else if (addTarget == 2) {
				player.setCriticalHitC(player.getCriticalHitC() - addNum);
			} else if (addTarget == 1) {
				this.setCriticalHitC(this.getCriticalHitC() - addNum);
			}
			break;
		case MagicWeaponConstant.mpNum: // 仙法
			if (addTarget == 3) {
				this.setMaxMPC(this.getMaxMPC() - addNum);
				player.setMaxMPB(player.getMaxMPB() - addNum);
			} else if (addTarget == 2) {
				player.setMaxMPB(player.getMaxMPB() - addNum);
			} else if (addTarget == 1) {
				this.setMaxMPC(this.getMaxMPC() - addNum);
			}
			break;
		case MagicWeaponConstant.magicAttNum: // 法攻
			if (addTarget == 3) {
				this.setMagicAttackC(this.getMagicAttackC() - addNum);
				this.setShowMagicAttack(this.getMagicAttack());
				player.setMagicAttackC(player.getMagicAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setMagicAttackC(player.getMagicAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setMagicAttackC(this.getMagicAttackC() - addNum);
				this.setShowMagicAttack(this.getMagicAttack());
			}
			break;
		case MagicWeaponConstant.magicDefanceNum: // 法防
			if (addTarget == 3) {
				this.setMagicDefenceC(this.getMagicDefenceC() - addNum);
				player.setMagicDefenceC(player.getMagicDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setMagicDefenceC(player.getMagicDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setMagicDefenceC(this.getMagicDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.hitNum: // 命中
			if (addTarget == 3) {
				this.setHitC(this.getHitC() - addNum);
				player.setHitC(player.getHitC() - addNum);
			} else if (addTarget == 2) {
				player.setHitC(player.getHitC() - addNum);
			} else if (addTarget == 1) {
				this.setHitC(this.getHitC() - addNum);
			}
			break;
		case MagicWeaponConstant.精准: // 精准
			if (addTarget == 3) {
				this.setAccurateC(this.getAccurateC() - addNum);
				player.setAccurateC(player.getAccurateC() - addNum);
			} else if (addTarget == 2) {
				player.setAccurateC(player.getAccurateC() - addNum);
			} else if (addTarget == 1) {
				this.setAccurateC(this.getAccurateC() - addNum);
			}
			break;
		case MagicWeaponConstant.免爆: // 免爆
			if (addTarget == 3) {
				this.setCriticalDefenceC(this.getCriticalDefenceC() - addNum);
				player.setCriticalDefenceC(player.getCriticalDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setCriticalDefenceC(player.getCriticalDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setCriticalDefenceC(this.getCriticalDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.火攻: // 火攻
			if (addTarget == 3) {
				this.setFireAttackC(this.getFireAttackC() - addNum);
				player.setFireAttackC(player.getFireAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setFireAttackC(player.getFireAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setFireAttackC(this.getFireAttackC() - addNum);
			}
			break;
		case MagicWeaponConstant.火炕: // 火炕
			if (addTarget == 3) {
				this.setFireDefenceC(this.getFireDefenceC() - addNum);
				player.setFireDefenceC(player.getFireDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setFireDefenceC(player.getFireDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setFireDefenceC(this.getFireDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.火减抗: // 外攻
			if (addTarget == 3) {
				this.setFireIgnoreDefenceC(this.getFireIgnoreDefenceC() - addNum);
				player.setFireIgnoreDefenceC(player.getFireIgnoreDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setFireIgnoreDefenceC(player.getFireIgnoreDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setFireIgnoreDefenceC(this.getFireIgnoreDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.风攻: // 外攻
			if (addTarget == 3) {
				this.setWindAttackC(this.getWindAttackC() - addNum);
				player.setWindAttackC(player.getWindAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setWindAttackC(player.getWindAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setWindAttackC(this.getWindAttackC() - addNum);
			}
			break;
		case MagicWeaponConstant.风抗: // 风抗
			if (addTarget == 3) {
				this.setWindDefenceC(this.getWindDefenceC() - addNum);
				player.setWindDefenceC(player.getWindDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setWindDefenceC(player.getWindDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setWindDefenceC(this.getWindDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.风减抗: // 风减抗
			if (addTarget == 3) {
				this.setWindIgnoreDefenceC(this.getWindIgnoreDefenceC() - addNum);
				player.setWindIgnoreDefenceC(player.getWindIgnoreDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setWindIgnoreDefenceC(player.getWindIgnoreDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setWindIgnoreDefenceC(this.getWindIgnoreDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.冰攻: // 冰攻
			if (addTarget == 3) {
				this.setBlizzardAttackC(this.getBlizzardAttackC() - addNum);
				player.setBlizzardAttackC(player.getBlizzardAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setBlizzardAttackC(player.getBlizzardAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setBlizzardAttackC(this.getBlizzardAttackC() - addNum);
			}
			break;
		case MagicWeaponConstant.冰抗: // 冰抗
			if (addTarget == 3) {
				this.setBlizzardDefenceC(this.getBlizzardDefenceC() - addNum);
				player.setBlizzardDefenceC(player.getBlizzardDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setBlizzardDefenceC(player.getBlizzardDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setBlizzardDefenceC(this.getBlizzardDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.冰减抗: // 冰减抗
			if (addTarget == 3) {
				this.setBlizzardIgnoreDefenceC(this.getBlizzardIgnoreDefenceC() - addNum);
				player.setBlizzardIgnoreDefenceC(player.getBlizzardIgnoreDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setBlizzardIgnoreDefenceC(player.getBlizzardIgnoreDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setBlizzardIgnoreDefenceC(this.getBlizzardIgnoreDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.雷攻: // 雷攻
			if (addTarget == 3) {
				this.setThunderAttackC(this.getThunderAttackC() - addNum);
				player.setThunderAttackC(player.getThunderAttackC() - addNum);
			} else if (addTarget == 2) {
				player.setThunderAttackC(player.getThunderAttackC() - addNum);
			} else if (addTarget == 1) {
				this.setThunderAttackC(this.getThunderAttackC() - addNum);
			}
			break;
		case MagicWeaponConstant.雷抗: // 雷抗
			if (addTarget == 3) {
				this.setThunderDefenceC(this.getThunderDefenceC() - addNum);
				player.setThunderDefenceC(player.getThunderDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setThunderDefenceC(player.getThunderDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setThunderDefenceC(this.getThunderDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.雷减抗: // 雷减抗
			if (addTarget == 3) {
				this.setThunderIgnoreDefenceC(this.getThunderIgnoreDefenceC() - addNum);
				player.setThunderIgnoreDefenceC(player.getThunderIgnoreDefenceC() - addNum);
			} else if (addTarget == 2) {
				player.setThunderIgnoreDefenceC(player.getThunderIgnoreDefenceC() - addNum);
			} else if (addTarget == 1) {
				this.setThunderIgnoreDefenceC(this.getThunderIgnoreDefenceC() - addNum);
			}
			break;
		case MagicWeaponConstant.移动速度: // 雷减抗
			if (addTarget == 3) {
				this.setSpeedA(this.getSpeedA() - addNum);
				player.setSpeedA(player.getSpeedA() - addNum);
			} else if (addTarget == 2) {
				player.setSpeedA(player.getSpeedA() - addNum);
			} else if (addTarget == 1) {
				this.setSpeedA(this.getSpeedA() - addNum);
			}
			break;

		default:
			break;
		}
		//同步数据
		long seq = GameMessageFactory.nextSequnceNum();
		PET_QUERY_REQ oldReq = new PET_QUERY_REQ(seq, this.getId());
		PET_QUERY_RES oldRes = PetSubSystem.getInstance().handle_PET_QUERY_REQ(oldReq, player);
		PET2_QUERY_RES res = Pet2Manager.getInst().makeNewPetQueryRes(seq, oldRes);
		PET2_LiZi_RES ret = Pet2Manager.getInst().makePetInfoWithLizi(this, seq, res);
		player.addMessageToRightBag(ret);
		if (PetManager.logger.isWarnEnabled()) {
			PetManager.logger.warn("[减去宠物飞升技能后] [addType:" + addType + "] [addNum:" + addNum + "] [addTarget:" + addTarget + "]" + player.getPlayerPropsString() + this.getPetPropsString());
		}
	}

	public void checkJiChuSkLv() {
		int[] arr = activeSkillLevels;
		if (arr == null) {
			return;
		}
		if (arr.length > PetManager.宠物最大技能数) {
			PetManager.logger.error("Pet.checkJiChuSkLv: 修正宠物等级个数开始 {}", id);
			PetManager.logger.error("Pet.checkJiChuSkLv: {} {}", ArrayUtils.toString(skillIds), ArrayUtils.toString(activeSkillLevels));
			arr = ArrayUtils.subarray(arr, 0, PetManager.宠物最大技能数);

			setActiveSkillLevels(arr);
			setSkillIds(ArrayUtils.subarray(skillIds, 0, PetManager.宠物最大技能数));

			PetManager.logger.error("Pet.checkJiChuSkLv: {} {}", ArrayUtils.toString(skillIds), ArrayUtils.toString(activeSkillLevels));
			PetManager.logger.error("Pet.checkJiChuSkLv: 修正宠物等级个数完毕 {}", id);
		}
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == 0) {
				arr[i] = 1;
				Pet2Manager.log.info("Pet.checkJiChuSkLv: fix lv 0 at {} {}", i, name);
			}
		}
	}

	public void calcShowAttMinMax() {
		this.showMaxStrengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 0);
		this.showMaxDexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 0);
		this.showMaxSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 0);
		this.showMaxConstitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 0);
		this.showMaxDingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 0);

		this.showMinStrengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 1);
		this.showMinDexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 1);
		this.showMinSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 1);
		this.showMinConstitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 1);
		this.showMinDingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 1);
	}

	/**
	 * 增加经验值
	 * @param exp
	 * @param reason
	 */
	public String addExp(long exp, int reason) {

		if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("给宠物加经验[{}] [{}] [{}] [{}]", new Object[] { id, exp, nextLevelExp, this.isSummoned() });

		if ((reason == PetExperienceManager.ADDEXP_REASON_FIGHTING || reason == PetExperienceManager.ADDEXP_REASON_FIRE_NPC) && exp > 0) {
			try {
				Player owner = GamePlayerManager.getInstance().getPlayer(ownerId);
				exp = exp * (100 + this.getExpPercent() + owner.getPetExpPercent()) / 100;
			} catch (Exception e) {
				exp = exp * (100 + this.getExpPercent()) / 100;
				MagicWeaponManager.logger.error("[宠物额外经验加成出错] [" + ownerId + "]", e);
			}
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		PetExperienceManager em = PetExperienceManager.getInstance();

		// if(getLevel() >= em.maxLevel){
		// if(PetManager.logger.isWarnEnabled())
		// PetManager.logger.warn("[宠物获得经验错误] [宠物级别到了顶级] [ownerId:{}] [{}] [{}] [petId:{}] [petName:{}] [{}] [{}]", new
		// Object[]{ownerId,master.getName(),master.getUsername(),id,name,exp,level});
		// return Translate.此宠物级别到了顶级;
		// }

		long ownerId = this.getOwnerId();
		Player master = null;
		try {
			master = PlayerManager.getInstance().getPlayer(ownerId);

			if (this.getLevel() >= master.getLevel() + 5) {
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("[宠物获得经验错误] [宠物级别大于玩家级别5级] [ownerId:{}] [{}] [{}] [petId:{}] [petName:{}] [{}] [{}] [playerLevel:{}]", new Object[] { ownerId, master.getName(), master.getUsername(), id, name, exp, level, master.getLevel() });
				}
				return Translate.此宠物级别大于玩家级别5级;
			}
		} catch (Exception e) {
			PetManager.logger.error("[宠物加经验异常] [" + ownerId + "] [" + this.getLogString() + "]", e);
			return Translate.这个宠物没有主人;
		}

		if (exp > 0) {
			// master.send_HINT_REQ(Translate.translateString(Translate.宠物获得xx经验, new String[][]{{Translate.COUNT_1,exp+""}}));
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.error("[宠物获得经验值] [经验为" + exp + "] [" + this.getLogString() + "] [主人id:" + this.getOwnerId() + "]");
			}
		} else {
			PetManager.logger.error("[宠物获得经验值] [经验为" + exp + "] [" + this.getLogString() + "] [主人id:" + this.getOwnerId() + "]");
			return "";
		}

		long oldValue = this.getExp();

		if (em.isUpgradeLevel(level, this.exp, exp, master.getLevel() > 220)) {
			int oldLevel = level;
			int newLevel = em.calculateLevel(level, this.exp, exp,master.getLevel() > 220);
			long newExp = em.calculateLeftExp(level, this.exp, exp,master.getLevel() > 220);
			HotspotManager.getInstance().overHotspot(master, Hotspot.OVERTYPE_宠物升级, "" + newLevel);

			// if(newLevel >= master.getLevel()+PetManager.宠物大于人物等级){
			// this.setLevel(master.getLevel()+PetManager.宠物大于人物等级);
			// this.setExp(0);
			// master.sendError("宠物"+this.getName()+"不能大于人物等级的十级，它不能获得经验。");
			// PetManager.logger.error("[宠物升级错误] [大于等级十级] [能升到等级:"+newLevel+"]");
			// }else{
			// setLevel(newLevel);
			// this.setExp(newExp);
			// }

			setLevel(newLevel);
			this.setExp(newExp);

			this.setNextLevelExp(em.maxExpOfLevel[this.level]);

			this.setStrengthA(PetPropertyUtility.计算力量值A(this));
			this.setDexterityA(PetPropertyUtility.计算身法值A(this));
			this.setSpellpowerA(PetPropertyUtility.计算灵力值A(this));
			this.setConstitutionA(PetPropertyUtility.计算耐力值A(this));
			this.setDingliA(PetPropertyUtility.计算定力值A(this));

			this.strengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 2);
			this.dexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 2);
			this.spellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 2);
			this.constitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 2);
			this.dingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 2);

			this.showStrengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 2);
			this.showDexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 2);
			this.showSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 2);
			this.showConstitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 2);
			this.showDingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 2);

			this.showMaxStrengthQuality = PetPropertyUtility.得到实际力量资质(this, true, 0);
			this.showMaxDexterityQuality = PetPropertyUtility.得到实际身法资质(this, true, 0);
			this.showMaxSpellpowerQuality = PetPropertyUtility.得到实际灵力资质(this, true, 0);
			this.showMaxConstitutionQuality = PetPropertyUtility.得到实际耐力资质(this, true, 0);
			this.showMaxDingliQuality = PetPropertyUtility.得到实际定力资质(this, true, 0);

			this.setUnAllocatedPoints(this.unAllocatedPoints + (newLevel - oldLevel) * 5);

			this.reinitFightingProperties(true);
			Pet2PropCalc.getInst().calcFightingAtt(this, Pet2Manager.log);

			this.setHp(this.maxHP);
			this.setMp(this.maxMP);

			// 一代非变异宠物才能繁殖
			if (this.getGeneration() == 0 && this.variation == 0) {
				int add = PetPropertyUtility.是否增加繁殖次数(oldLevel, newLevel);
				if (add > 0) {
					this.setBreedTimes((byte) (breedTimes + add));
				}
			}
			if (this.isSummoned()) {

				NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, this.id, (byte) Event.LEVEL_UPGRADE, this.level);
				master.addMessageToRightBag(req);

			}
			if (newLevel > oldLevel) {
				this.init();
			}

			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[宠物升级] [" + oldLevel + "->" + newLevel + "] [" + this.getLogString() + "] [主人:" + master.getLogString() + "]");
			}
		} else {
			long exps = this.exp + exp;
			//协议传输的exp是int类型的
//			if(exps > Integer.MAX_VALUE){
//				exps = Integer.MAX_VALUE;
//			}
			this.setExp(exps);
		}
		return "";
	}

	public void initTitle() {
		try {
			if (!isIdentity()) {
				setTitle("");
				return;
			}
			String colorStrings[] = { "#FFFFFF", "#00FF00", "#0000FF", "#A000FF", "#FFA000" };
			int fontSize = 30;
			StringBuffer showTitle1 = new StringBuffer("");
			PetFlyState stat = PetManager.getInstance().getPetFlyState(this.getId(),"初始化称号");
			if(stat != null && stat.getLingXingPoint() == 110){
				showTitle1.append("<i imagePath='/ui/textue_title2.png' rectX='0' rectY='97' rectW='164' rectH='81' width='130' height='65'>").append("</i>");
				showTitle1.append("\n");
			}
			if (getStarClass() > 0) {
				if (getStarClass() <= 5) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='607' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				} else if (getStarClass() <= 10) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='643' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				} else if (getStarClass() <= 13) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='679' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				} else if (getStarClass() <= 16) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='715' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				} else if (getStarClass() <= 18) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='751' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				} else if (getStarClass() > 18) {
					showTitle1.append("<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='24' height='24'>").append("</i>");
				}
			} else {
				showTitle1.append("  ");
			}
			showTitle1.append("<f size='").append(fontSize).append("' color='").append(colorStrings[4]).append("'>");
//			int jiNum = skillIds.length;
//			int tianNum = 0;
//			if (getTianFuSkIds() != null) {
//				tianNum = getTianFuSkIds().length;
//			}
//			showTitle1.append("[").append(jiNum).append(Translate.技).append(tianNum).append(Translate.text_6341).append("]");
			showTitle1.append("</f>");
//			showTitle1.append("\n");
			// 阶位
			showTitle1.append("<f size='" + fontSize + "' color='");
			if (grade == 1) {
				showTitle1.append(colorStrings[0]);
			} else if (grade == 2) {
				showTitle1.append(colorStrings[1]);
			} else if (grade == 3 || grade == 4) {
				showTitle1.append(colorStrings[2]);
			} else if (grade == 5 || grade == 6) {
				showTitle1.append(colorStrings[3]);
			} else if (grade > 6) {
				showTitle1.append(colorStrings[4]);
			} else {
				showTitle1.append(colorStrings[0]);
			}
			showTitle1.append("'>");
			showTitle1.append(PetManager.宠物品阶[grade - 1]).append("</f>");
			// 成长
			showTitle1.append("<f size='").append(fontSize).append("' color='");
			showTitle1.append(colorStrings[growthClass]);
			showTitle1.append("'>");
			showTitle1.append(PetManager.宠物成长品质[growthClass]).append("</f>");
			// 圣兽
			showTitle1.append("<f size='").append(fontSize).append("' color='");
			if (qualityScore <= 1000) {
				showTitle1.append(colorStrings[0]);
				showTitle1.append("'>");
				showTitle1.append(PetManager.宠物新品质[0]).append("</f>");
			} else if (qualityScore <= 5000) {
				showTitle1.append(colorStrings[1]);
				showTitle1.append("'>");
				showTitle1.append(PetManager.宠物新品质[1]).append("</f>");
			} else if (qualityScore <= 50000) {
				showTitle1.append(colorStrings[2]);
				showTitle1.append("'>");
				showTitle1.append(PetManager.宠物新品质[2]).append("</f>");
			} else if (qualityScore <= 500000) {
				showTitle1.append(colorStrings[3]);
				showTitle1.append("'>");
				showTitle1.append(PetManager.宠物新品质[3]).append("</f>");
			} else if (qualityScore > 500000) {
				showTitle1.append(colorStrings[4]);
				showTitle1.append("'>");
				showTitle1.append(PetManager.宠物新品质[4]).append("</f>");
			}

			setTitle(showTitle1.toString());
		} catch (Exception e) {
			PetManager.logger.error("initTitle", e);
		}
	}

	public void setLevel(int level) {
		super.setLevel(level);
		EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.PET_LEVEL_UP, this);
		EventRouter.getInst().addEvent(evt);
		PetManager.em.notifyFieldChange(this, "level");
	}

	private transient int showPhyAttack;
	private transient int showMagicAttack;

	public int getShowPhyAttack() {
		return showPhyAttack;
	}

	public void setShowPhyAttack(int showPhyAttack) {
		this.showPhyAttack = showPhyAttack;
	}

	public int getShowMagicAttack() {
		return showMagicAttack;
	}

	public void setShowMagicAttack(int showMagicAttack) {
		this.showMagicAttack = showMagicAttack;
	}

	/**
	 * 重新计算设置战斗属性，在宠物的基础属性、等级、品阶发生变化时，需要重新计算战斗属性
	 */
	public void reinitFightingProperties(boolean real) {

		this.setPhyAttackA(PetPropertyUtility.计算物理攻击(this, real));
		this.setMagicAttackA(PetPropertyUtility.计算法术攻击(this, real));

		this.setShowPhyAttack(this.getPhyAttack());
		this.setShowMagicAttack(this.getMagicAttack());
		// //显示的外功法术攻击特殊处理
		// if(this.getCareer() == PetPropertyUtility.物攻型 || this.getCareer() == PetPropertyUtility.敏捷型){
		// this.setShowPhyAttack(this.getPhyAttack());
		// this.setShowMagicAttack((int)(Math.rint(0.1*this.getMagicAttack())));
		// }else{
		// this.setShowPhyAttack((int)(Math.rint(0.1*this.getPhyAttack())));
		// this.setShowMagicAttack(this.getMagicAttack());
		// }
		this.setPhyDefenceA(PetPropertyUtility.计算物理防御(this, real));
		this.setMagicDefenceA(PetPropertyUtility.计算法术防御(this, real));
		this.setMaxHPA(PetPropertyUtility.计算最大血量(this, real));
		this.setCriticalHitA(PetPropertyUtility.计算暴击(this, real));
		this.setHitA(PetPropertyUtility.计算命中(this, real));
		this.setDodgeA(PetPropertyUtility.计算闪躲(this, real));
		this.setFireDefenceA(0);
		this.setBlizzardDefenceA(0);
		this.setWindDefenceA(0);
		this.setThunderDefenceA(0);
		this.setHitRate(PetPropertyUtility.计算命中率(this, real));
		this.setDodgeRate(PetPropertyUtility.计算闪躲率(this, real));
		this.setCriticalHitRate(PetPropertyUtility.计算暴击率(this, real));
		this.setPhysicalDecrease(PetPropertyUtility.计算外攻防御减伤率(this, real));
		this.setSpellDecrease(PetPropertyUtility.计算法术防御减伤率(this, real));

		// 设置宠物得分
		this.setPetScore();
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[" + this.getLogString() + "] [外功:" + this.getPhyAttack() + "] [外功A:" + this.getPhyAttackA() + "] [外功b:" + this.getPhyAttackB() + "] [外功c:" + this.getPhyAttackC() + "] [内功:" + getMagicAttack() + "] [内功a:" + this.getMagicAttackA() + "] [内功b:" + this.getMagicAttackB() + "] [内功c:" + this.getMagicAttackC() + "] [max血量:" + getMaxHP() + "] [暴击:" + getCriticalHit() + "] [命中:" + getHit() + "] [闪躲:" + getDodge() + "]");
			PetManager.logger.debug("[" + this.getLogString() + "] [命中率:" + getHitRate() + "] [闪躲率:" + getDodgeRate() + "] [暴击率:" + getCriticalHitRate() + "] [外攻防御减伤率：" + getPhysicalDecrease() + "] [法术防御减伤率：" + getSpellDecrease() + "]");
		}
	}

	/**
	 * 判断是否死亡，此标记只是标记是否死亡，比如HP = 0
	 * 不同于LivingObject的alive标记。
	 * alive标记用于是否要将生物从游戏中清除。死亡不代表要清除。
	 * @return
	 */
	public boolean isDeath() {
		return (this.hp <= 0 && state == STATE_DEAD);
	}
	/**
	 * 宠物暴击率单独计算
	 */
	@Override
	public void setCriticalHit(int value) {
		/*this.criticalHit = value;1
		int hitrate = CombatCaculator.CAL_会心一击率(value, level);
		this.setCriticalHitRate(hitrate);*/
//		this.setCriticalHitA(PetPropertyUtility.计算暴击(this, true));
		super.setCriticalHit(value);
		this.setCriticalHitRate(PetPropertyUtility.计算暴击率(this, true));
	}

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
	public int getFightingType(Fighter fighter) {
		Player owner = this.getMaster();
		if (owner != null) {
			return owner.getFightingType(fighter);
		} else if (tempMaster != null) {
			return tempMaster.getFightingType(fighter);
		} else {
			if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[宠物释放技能错误] [已经召回owner为null]");
			return 1;
		}

	}

	@Override
	public int getViewWidth() {

		return 800;
	}

	@Override
	public int getViewHeight() {

		return 480;
	}

	/**
	 * 宠物攻击
	 */
	public void notifyUseSkill() {
		try {
			ArrayList<Buff> list = this.buffs;
			for (Buff bf : list) {
				if (bf instanceof Buff_OnceAttributeAttack) {
					((Buff_OnceAttributeAttack) bf).notifyAttack(this);
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 使用有目标的技能，攻击目标
	 * 
	 * @param skill
	 * @param target
	 */
	public void useTargetSkill(ActiveSkill skill, Fighter target, byte[] targetTypes, long[] targetIds, byte direction) {
		if (master == null) {
			// Game.logger.warn("[宠物使用无目标技能] [错误] ["+this.getId()+"] ["+this.getName()+"] [master == null] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物使用有目标技能] [错误] [{}] [{}] [master == null] [Skill:{}]", new Object[] { this.getId(), this.getName(), (skill == null ? "NULL" : skill.getName()) });
			return;
		}

		if (master.isInBattleField() && master.getDuelFieldState() == 1) {
			if (master.battleField != null && master.battleField instanceof TournamentField) {
				TournamentField df = (TournamentField) master.battleField;
				if (df.getState() != TournamentField.STATE_FIGHTING) {
					return;
				}
			}
		}
		

		if (skill instanceof CommonAttackSkill) {

			int r = skill.check(this, target, 1);
			if (r == 0) {
				skillAgent.usingSkill(skill, 1, target, (int) target.getX(), (int) target.getY(), targetTypes, targetIds, direction);
				// Game.logger.debug("[宠物使用基本技能] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "] ["+r+"]");
				if (Game.logger.isDebugEnabled()) Game.logger.debug("[宠物使用基本技能] [Skill:{}] [{}]", new Object[] { (skill == null ? "NULL" : skill.getName()), r });
			} else {
				// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.SKILL_ERROR, 0);
				// this.addMessageToRightBag(req);
				// Game.logger.debug("[宠物使用基本技能失败] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "] ["+r+"]");
				if (Game.logger.isDebugEnabled()) Game.logger.debug("[宠物使用基本技能失败] [Skill:{}] [{}]", new Object[] { (skill == null ? "NULL" : skill.getName()), r });
			}
		} else {
			int r = skill.check(this, target, this.getSkillLevelById(skill.getId()));
			if (r == 0) {
				skillAgent.usingSkill(skill, this.getSkillLevelById(skill.getId()), target, (int) target.getX(), (int) target.getY(), targetTypes, targetIds, direction);
				// Game.logger.debug("[宠物使用主动技能] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "] ["+r+"]");
				if (Game.logger.isDebugEnabled()) Game.logger.debug("[宠物使用主动技能] [Skill:{}] [{}]", new Object[] { (skill == null ? "NULL" : skill.getName()), r });
			} else {
				// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.SKILL_ERROR, 0);
				// this.addMessageToRightBag(req);
				// Game.logger.debug("[宠物使用主动技能失败] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "] ["+r+"]");
				if (Game.logger.isDebugEnabled()) Game.logger.debug("[宠物使用主动技能失败] [Skill:{}] [{}]", new Object[] { (skill == null ? "NULL" : skill.getName()), r });
				if (skill.getDuration3() >= 10000) {
					// SKILL_CD_MODIFY_REQ req2 = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), (short) skill.getId(), (byte) ActiveSkillEntity.STATUS_END,
					// 0);
					// addMessageToRightBag(req2);
				}
			}
		}

		if (target != null) {
			if (skill instanceof SkillWithoutTraceAndOnTeamMember) {
				SkillWithoutTraceAndOnTeamMember st = (SkillWithoutTraceAndOnTeamMember) skill;
				if (target != null && getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY && (st.getRangeType() == 1 || st.getRangeType() == 6)) {
					notifyPrepareToFighting(target);
					target.notifyEndToBeFighted(this);
				}
			} else if (getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY) {
				notifyPrepareToFighting(target);
				target.notifyPrepareToBeFighted(this);
			}
		}

	}

	/**
	 * 使用无目标的技能，攻击
	 * 
	 * @param skill
	 * @param x
	 * @param y
	 */
	public void useNonTargetSkill(ActiveSkill skill, int x, int y, byte[] targetTypes, long[] targetIds, byte direction) {
		if (master == null) {
			// Game.logger.warn("[宠物使用无目标技能] [错误] ["+this.getId()+"] ["+this.getName()+"] [master == null] [Skill:" + (skill == null ? "NULL" : skill.getName()) + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物使用无目标技能] [错误] [{}] [{}] [master == null] [Skill:{}]", new Object[] { this.getId(), this.getName(), (skill == null ? "NULL" : skill.getName()) });
			return;
		}

		// 战场结束，不能打架
		if (this.isInBattleField() && master.getBattleField() != null && master.getBattleField().getState() == BattleField.STATE_STOPFIGHTING) {
			// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.SKILL_ERROR, 0);
			// this.addMessageToRightBag(req);
			// Game.logger.warn("[宠物使用无目标技能] [错误] [战场结束，不能打架] ["+this.getId()+"] ["+this.getName()+"] ["+master.getUsername()+"] ["+master.getId()+"] ["+master.getName()+"] [Skill:"
			// + (skill == null ? "NULL" : skill.getName()) + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物使用无目标技能] [错误] [战场结束，不能打架] [{}] [{}] [{}] [{}] [{}] [Skill:{}]", new Object[] { this.getId(), this.getName(), master.getUsername(), master.getId(), master.getName(), (skill == null ? "NULL" : skill.getName()) });
			return;
		}

		if (master.isInBattleField() && master.getDuelFieldState() == 1) {
			if (master.battleField != null && master.battleField instanceof TournamentField) {
				TournamentField df = (TournamentField) master.battleField;
				if (df.getState() != TournamentField.STATE_FIGHTING) {
					return;
				}
			}
		}

		int r = skill.check(this, null, this.getSkillLevelById(skill.getId()));
		if (r == 0) {
			skillAgent.usingSkill(skill, getSkillLevelById(skill.getId()), null, x, y, targetTypes, targetIds, direction);
			// Game.logger.warn("[宠物使用无目标技能] [进入技能代理] ["+this.getId()+"] ["+this.getName()+"] ["+master.getUsername()+"] ["+master.getId()+"] ["+master.getName()+"] [Skill:" +
			// (skill == null ? "NULL" : skill.getName()) + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物使用无目标技能] [进入技能代理] [{}] [{}] [{}] [{}] [{}] [Skill:{}]", new Object[] { this.getId(), this.getName(), master.getUsername(), master.getId(), master.getName(), (skill == null ? "NULL" : skill.getName()) });
		} else {
			// Game.logger.warn("[宠物使用无目标技能] [错误] [检查失败"+r+"] ["+this.getId()+"] ["+this.getName()+"] ["+master.getUsername()+"] ["+master.getId()+"] ["+master.getName()+"] [Skill:"
			// + (skill == null ? "NULL" : skill.getName()) + "]");
			if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物使用无目标技能] [错误] [检查失败{}] [{}] [{}] [{}] [{}] [{}] [Skill:{}]", new Object[] { r, this.getId(), this.getName(), master.getUsername(), master.getId(), master.getName(), (skill == null ? "NULL" : skill.getName()) });
			// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, getId(), (byte) Event.SKILL_ERROR, 0);
			// this.addMessageToRightBag(req);
			//
			// if (skill.getDuration3() >= 10000) {
			// SKILL_CD_MODIFY_REQ req2 = new SKILL_CD_MODIFY_REQ(GameMessageFactory.nextSequnceNum(), getId(), (short) skill.getId(), (byte) ActiveSkillEntity.STATUS_END, 0);
			// addMessageToRightBag(req2);
			// }
		}

	}

	public transient boolean deathNotify = false;

	/**
	 * 处理其它生物对此生物造成伤害的事件
	 * 
	 * @param caster
	 *            伤害施加者
	 * @param damage
	 *            预期伤害值
	 * @param damageType
	 *            伤害类型，如：普通物理伤害，魔法伤害，反噬伤害等
	 */
	public void causeDamage(Fighter caster, int damage, int hateParam, int damageType) {

		if (caster.getFightingType(this) != Fighter.FIGHTING_TYPE_ENEMY) {
			return;
		}
		long now = System.currentTimeMillis();
		// pk相关
		if (this.getMaster() == null) return;
		if (now - lastReliveTime < InvincibleTime) {
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[宠物无敌] ["+this.getId()+"] [上次触发回血时间:" + lastReliveTime + "] [无敌持续时间:" + InvincibleTime + "]");
			}
			return ;
		}
		this.getMaster().攻击玩家后对攻击者的操作(caster);

		// 增加敌人
		if (this.isDeath() == false && caster instanceof LivingObject) this.getMaster().enemyList.add(caster, false, this.heartBeatStartTime);

		int dmgRate = 1;
		if (this.talent1Skill == 110128 || this.talent2Skill == 110128) {		//女性角色对宠物造成三倍伤害
			if (caster instanceof Player) {
				Player p = (Player) caster;
				if (p.getSex() == 1) {
					damage *= 3;
				}
			}
		}
		if (this.talent1Skill == 110131 || this.talent2Skill == 110131) {		//男性玩家对宠物造成三倍伤害
			if (caster instanceof Player) {
				Player p = (Player) caster;
				if (p.getSex() == 0) {
					damage *= 3;
				}
			}
		}
		if (this.talent1Skill == 110135 || this.talent2Skill == 110135) {		//被玩家攻击受到2.5倍伤害
			if (caster instanceof Player) {
				Player p = (Player) caster;
				damage *= 2.5;
			}
		}
		int ddd = damage;
		if (ddd > hp) {
			ddd = hp;
		}
		try {
			if (master.isInBattleField()) {
				master.getBattleField().notifyCauseDamageOfReal(caster, ddd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((caster instanceof Player) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Player p = (Player) caster;

			if (p.isIsGuozhan() && p.getCountry() != master.getCountry()) {
				Guozhan guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(p);
				if (guozhan != null && (guozhan.getAttacker().getCountryId() == p.getCountry() || guozhan.getDefender().getCountryId() == p.getCountry())) {
					guozhan.notifyDamage(p, damage);
				}
			}

			if (p.getState() != Player.STATE_DEAD && p.getMpStealPercent() > 0) {
				if (p.getMp() < p.getMaxMP()) {
					int mp = p.getMp() + p.getMpStealPercent() * ddd / 100;
					if (mp > p.getMaxMP()) mp = p.getMaxMP();
					p.setMp(mp);

					// 加蓝，是否要通知客户端？ // debug dot
					//
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					// .getId(), (byte) Event.MP_INCREASED, p.getMpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);

				} else {
					// debug dot
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					// .getId(), (byte) Event.MP_INCREASED, p.getMpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);

				}
			}

			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					if(p.isCanNotIncHp()){
						if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽宠物吸血] [" + p.getLogString() + "] [血]");
					}else{
						p.setHp(mp);
						Skill.logger.debug("Monster.causeDamage: 宠物吸血 {}", p.getName());
						
						// 加血，是否要通知客户端？debug dot
						
						// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
						// .getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
						// p.addMessageToRightBag(req);
					}
				} else {
					// debug dot
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					// .getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);
				}
			}
		}
		if ((caster instanceof Pet) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Pet p = (Pet) caster;
			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					p.setHp(mp);

					// 加蓝，是否要通知客户端？

					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd
					// / 100);
					// p.addMessageToRightBag(req);
				} else {
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd
					// / 100);
					// p.addMessageToRightBag(req);
				}
			}
		}
		if (this.ironMaidenPercent > 0 && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			int iron = ddd * this.ironMaidenPercent / 100;
			if (iron > 0) {
				caster.causeDamage((Fighter) this, iron, hateParam, Fighter.DAMAGETYPE_FANSHANG);
				this.damageFeedback(caster, iron, hateParam, Fighter.DAMAGETYPE_FANSHANG);
			}
		}
		if (this.getAntiInjuryRate() > 0 && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			if (caster instanceof Pet || caster instanceof Player) {
				double tempRate = this.getAntiInjuryRate() / 100d;
				int injury = (int) (ddd * tempRate);
				if (injury > 0) {
					if (caster instanceof Pet) {
						injury = ((Pet)caster).checkInjuryAndPosiDamage(injury);
					}
					caster.causeDamage((Fighter) this, injury, hateParam, Fighter.DAMAGETYPE_FANSHANG);
					this.damageFeedback(caster, injury, hateParam, Fighter.DAMAGETYPE_FANSHANG);
				}
			}
		}

		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {
			if (hp > damage) {
				setHp(hp - damage);
			} else {
				if (lastReliveTime > 0 && now - lastReliveTime >= reliveIntever) {
					lastReliveTime = now;
					setHp(getMaxHP());
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.debug("[受到致命一击] [回满血] [" + this.getId() + "]");
					}
				} else {
					setHp(0);
				}
			}

		}
		if (this.getMaster() != null) {
			this.getMaster().add2transientEnemyList(caster);
		} else if (this.getTempMaster() != null) {
			this.getTempMaster().add2transientEnemyList(caster);
		}

		// if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType !=
		// Fighter.DAMAGETYPE_XISHOU) {
		// // 副本统计
		// Game game = this.currentGame;
		// if (game != null && game.getDownCity() != null) {
		// DownCity dc = game.getDownCity();
		// dc.statPlayerBeAttack(this, damageType, damage);
		// }
		// }
		//
		// // 掉耐久
		// EquipmentColumn ec = ecs;
		// ec.beAttacked();

		byte targetType = 1;

		NOTIFY_EVENT_REQ req = null;
		switch (damageType) {
		case DAMAGETYPE_PHYSICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_PHYSICAL, damage);
			break;
		case DAMAGETYPE_SPELL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_SPELL, damage);
			break;
		case DAMAGETYPE_PHYSICAL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_PHYSICAL_BAOJI, damage);
			break;
		case DAMAGETYPE_SPELL_CRITICAL:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_SPELL_BAOJI, damage);
			break;
		case DAMAGETYPE_DODGE:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.DODGE, damage);
			break;
		case DAMAGETYPE_MISS:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.MISS, damage);
			break;
		case DAMAGETYPE_ZHAONGDU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_ZHONGDU, damage);
			break;
		case DAMAGETYPE_FANSHANG:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_DECREASED_FANSHANG, damage);
			break;
		case DAMAGETYPE_MIANYI:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_MIANYI, damage);
			break;
		case DAMAGETYPE_XISHOU:
			req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, getId(), (byte) Event.HP_XISHOU, damage);
			break;
		}
		if (req != null) {
			getMaster().addMessageToRightBag(req);
		}
		// if (req != null && this.isInBattleField() && this.getDuelFieldState() == 1) {
		// BattleField bf = this.getBattleField();
		// if (bf != null) {
		// Player[] ps = bf.getPlayersBySide(BattleField.BATTLE_SIDE_C);
		// for (Player p : ps) {
		// NOTIFY_EVENT_REQ req2 = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), req.getTargetType(), req.getTargetId(), req.getEventType(), req.getEventData());
		// p.addMessageToRightBag(req2);
		// }
		// }
		// }

//		JJCManager.logger.warn("[jjc通知生物死亡0]  [--0--] [this.isInBattleField():" + this.isInBattleField() + "]  [deathNotify:"+deathNotify+"] [hp:"+this.getHp()+"] [state:"+this.getState()+"] [宠物名:" + this.getName() + "]");
		if (deathNotify == false && this.getHp() <= 0 && this.getState() != Player.STATE_DEAD) {
			deathNotify = true;
//			JJCManager.logger.warn("[jjc通知生物死亡0]  [--1--] [this.isInBattleField():" + this.isInBattleField() + "] [宠物名:" + this.getName() + "]");
			if (this.isInBattleField()) {
				// 战场中，通知战场，玩家被杀死
				this.master.getBattleField().notifyKilling(caster, this);
			} else {
				if (caster instanceof Player) {
					Player p = (Player) caster;
					// if (p.Cou() != this.getPoliticalCamp()) {
					getMaster().send_HINT_REQ(Translate.translateString(Translate.您的宠物被谁杀死了, new String[][] { { Translate.PLAYER_NAME_1, p.getName() } }));
					p.send_HINT_REQ(Translate.translateString(Translate.您杀死了谁的宠物, new String[][] { { Translate.PLAYER_NAME_1, master.getName() }, { Translate.STRING_1, this.name } }));

					// Game.logger.warn("[宠物被杀] [被玩家杀死] [宠物：" + this.getName() + "] [宠物ID：" + this.getId() + "] [击杀者：" + p.getName() + "] [击杀者ID：" + p.getId() + "] [击杀者等级：" +
					// p.getLevel() + "]");
					if (Game.logger.isWarnEnabled()) Game.logger.warn("[宠物被杀] [被玩家杀死] [宠物：{}] [宠物ID：{}] [击杀者：{}] [击杀者ID：{}] [击杀者等级：{}]", new Object[] { this.getName(), this.getId(), p.getName(), p.getId(), p.getLevel() });
					// }

					// 谁把我杀了，就加入仇人列表
					// SocialManager.getInstance().addChouren(this, p.getId());
				}
				// else if(caster instanceof NPC){
				// NPC n = (NPC)caster;
				// if(n.getPoliticalCamp() == GameConstant.中立阵营){
				// this.send_HINT_REQ(Translate.translate.text_5826+n.getName()+Translate.translate.text_5824);
				// }else{
				// this.send_HINT_REQ(Translate.translate.text_5823+n.getName()+Translate.translate.text_5824);
				// }
				// Game.logger.warn("[玩家被杀] [被NPC杀死] [玩家："+this.getName()+"] [玩家ID："+this.getId()+"] [击杀者："+n.getName()+"] [击杀者ID："+n.getId()+"]");
				// }
				// }
				// if(caster instanceof Player){
				// Player p = (Player)caster;
				// if(p.getLevel()-this.getLevel()<=5){
				// StatDataUpdateManager sdum=StatDataUpdateManager.getInstance();
				// if(sdum!=null){
				// sdum.killingUpdate(p, 1, com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
				// }
				// }
			}
			// 玩家杀死玩家后的惩罚(caster);
			// 玩家被杀死后的掉落(caster);
		}
	}
	
	public int checkInjuryAndPosiDamage(int damage) {
		int result = damage;
		if (this.decreseSpecialDamage > 0) {
			result = (int) (damage * (1 - (float)decreseSpecialDamage / 100f));
			if (result <= 0) {
				result = 1;
			}
		}
		return result;
	}

	/**
	 * 反伤比例，此反伤只适用于宠物和玩家
	 */
	public transient int antiInjuryRate = 0;

	/**
	 * 处理伤害反馈事件。当某个精灵（玩家、怪物等）受到攻击并造成伤害，<br>
	 * 该精灵会调用攻击者的这个方法，通知攻击者，它的攻击对其他精灵造成了伤害
	 * 
	 * @param target
	 *            受到伤害的目标精灵
	 * @param damage
	 *            真实伤害值
	 */
	public void damageFeedback(Fighter target, int damage, int hateParam, int damageType) {
		this.fightingAgent.damageFeedback(target, damage, damageType);
		try {
			// 增加敌人
			if (this.isDeath() == false && this.getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY && target instanceof LivingObject && master != null) {
				master.enemyList.add(target, true, this.heartBeatStartTime);
			}

			if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {
				//
				// // 通知战场，我给别人伤害了
				// if (this.isInBattleField()) {
				// master.battleField.notifyCauseDamage(this, target, damage);
				// }
				try {
//					if (master != null && JJCManager.isJJCBattle(master.getCurrentGame().gi.name)) {
//						master.battleField.notifyCauseDamage(this, target, damage);
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				// // 副本统计
				// Game game = master.currentGame;
				// if (game != null && game.getDownCity() != null) {
				// DownCity dc = game.getDownCity();
				// dc.statPlayerAttack(this, damageType, damage);
				// }
			}

			byte targetType = 0;
			if (target instanceof Player) {
				targetType = 0;
			} else if (target instanceof Sprite) {
				targetType = 1;
			}

			NOTIFY_EVENT_REQ req = null;
			switch (damageType) {
			case DAMAGETYPE_PHYSICAL:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_PHYSICAL_4_PET, damage);
				break;
			case DAMAGETYPE_SPELL:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_SPELL_4_PET, damage);
				break;
			case DAMAGETYPE_PHYSICAL_CRITICAL:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_PHYSICAL_BAOJI_4_PET, damage);
				break;
			case DAMAGETYPE_SPELL_CRITICAL:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_SPELL_BAOJI_4_PET, damage);
				break;
			case DAMAGETYPE_DODGE:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.DODGE, damage);
				break;
			case DAMAGETYPE_MISS:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.MISS, damage);
				break;
			case DAMAGETYPE_ZHAONGDU:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_ZHONGDU, damage);
				break;
			case DAMAGETYPE_FANSHANG:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_DECREASED_FANSHANG, damage);
				break;
			case DAMAGETYPE_MIANYI:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_MIANYI, damage);
				break;
			case DAMAGETYPE_XISHOU:
				req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), targetType, target.getId(), (byte) Event.HP_XISHOU, damage);
				break;
			}
			if (req != null) {
				if (master != null) {
					master.addMessageToRightBag(req);
				} else if (tempMaster != null) {
					tempMaster.addMessageToRightBag(req);
				}
			}

		} catch (Exception ex) {

		}
	}

	/**
	 * 攻击目标消失
	 * 
	 * @param target
	 */
	public void targetDisappear(Fighter target) {
	}

	/**
	 * 是否一个队伍的
	 */
	public boolean isSameTeam(Fighter fighter) {
		if (fighter == this) {
			return true;
		} else if (fighter instanceof Player) {
			Player p = (Player) fighter;
			return p == master || p.isSameTeam(master);
		} else if (fighter instanceof Pet) {
			Pet p = (Pet) fighter;
			return p.getMaster().isSameTeam(master);
		}
		return false;
	}

	public int getMp() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 主动式激活后，寻找激活范围内的可攻击的对象，优先攻击攻击玩家的活物
	 * @return
	 */
	// protected Fighter findTargetInActivationRange(Game game){
	// //优先攻击主人正在攻击的目标
	// Fighter masterTarget = master.getCurrentEnemyTarget();
	// if(masterTarget != null) {
	// return masterTarget;
	// }
	// //找到离自己最近的敌人
	// Fighter fs[] = game.getVisbleFighter(this, false);
	// Fighter f = null;
	// int minD = 0;
	// for (int i = 0; i < fs.length; i++) {
	// if (fs[i].getX() >= master.getX() - this.pursueWidth / 2
	// && fs[i].getX() <= master.getX() + this.pursueWidth / 2
	// && fs[i].getY() >= master.getY() - this.pursueHeight / 2
	// && fs[i].getY() <= master.getY() + this.pursueHeight/ 2) {
	// int d = (fs[i].getX() - master.getX()) * (fs[i].getX() - master.getX()) + (fs[i].getY() - master.getY())
	// * (fs[i].getY() - master.getY());
	// if (f == null && fs[i] != master) {
	// f = fs[i];
	// minD = d;
	// } else if (d < minD && fs[i] != master) {
	// f = fs[i];
	// minD = d;
	// }
	// }
	// }
	// return f;
	// }

	/**
	 * 种植一个buff到玩家或者宠物或者怪的身上， 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff) {
		Buff old = null;
		if (buff instanceof AbnormalStateBuff) {
			if (this.getDecreaseAbnormalStateTimeRate() > 0) {
				try {
					long now = System.currentTimeMillis();
					float temp = getDecreaseAbnormalStateTimeRate();
					if (temp > 100) {
						temp = 100;
					}
					long time = buff.getInvalidTime() - now;
					long resultTime = (long) (time * (1 - temp / 100f));
					buff.setInvalidTime(now + resultTime);
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.debug("[减少宠物受到异常状态持续时间] [buff:" + buff.getClass() + "," + buff.getTemplateName() + "] [time:" + time + " -> " + resultTime + "]");
					}
				} catch (Exception e) {
					PetManager.logger.warn("[减少宠物受到异常状态时间] [异常] [" + this.getId() + "]", e);
				}
			}
		}
		if ((buff instanceof Buff_ZhongDu) || (buff instanceof Buff_ZhongDuFaGong) || (buff instanceof Buff_ZhongDuWuGong)) {

			for (Buff b : buffs) {
				if (buff.getTemplate() == b.getTemplate() && buff.getCauser() == b.getCauser()) {
					old = b;
					break;
				}
			}
			if (old != null) {
				if (buff.getLevel() >= old.getLevel()) {
					old.end(this);
					buffs.remove(old);
					if (old.isSyncWithClient()) {
						this.removedBuffs.add(old);
					}
				} else {
					return;
				}
			}

		} else {

			for (Buff b : buffs) {
				if (buff.getTemplate() == b.getTemplate()) {
					old = b;
					break;
				}
			}
			if (old != null) {
				if (buff.getLevel() >= old.getLevel()) {
					old.end(this);
					buffs.remove(old);
					if (old.isSyncWithClient()) {
						this.removedBuffs.add(old);
					}
				} else {
					return;
				}
			}

			for (int i = buffs.size() - 1; i >= 0; i--) {
				Buff b = buffs.get(i);
				if (buff.getCauser() == b.getCauser() && buff.getGroupId() == b.getGroupId()) {
					buffs.remove(i);
					b.end(this);
					if (b.isSyncWithClient()) {
						this.removedBuffs.add(b);
					}
				}
			}
		}
		buffs.add(buff);
		buff.start(this);
		if (buff.isSyncWithClient()) {
			this.newlyBuffs.add(buff);
		}

	}
	/** 半秒回血 */
	private  transient int hpRecoverExtend;
	private transient long lastTimeForRecoverHPBAndMPB;
	/**
	 * 五秒回血百分比
	 */
	private  transient int hpRecoverExtend2;
	private  transient long lasthpRecoverExtend2Time = -1;
	
	/*** 触发无敌 */
	public transient long lastReliveTime = -1;
	public static transient long reliveIntever = 60000;
	public static transient long InvincibleTime = 5000;
	
	public transient long lastRemoveDebuffTime = -1;
	public transient long removeIntever = 15000;
	
	public transient int attributeAddRate = -1;
	public transient long lastChangeTime = System.currentTimeMillis();
	
	private transient long lastDeadlyAttackTime;
	/**
	 * 心跳处理的顺序
	 * 
	 * 1. 先处理父类的心跳
	 * 2. 处理是否被打死，如果是调用killed方法，同时设置state为STATE_DEAD状态
	 * 3. 判断是否已经超过死亡过程设定的时间，如果是，设置alive标记
	 * 
	 */
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		try {
			super.heartbeat(heartBeatStartTime, interval, game);

			if (master == null) {
				game.removeSprite(this);
				if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[玩家为null删除宠物] [" + this.getLogString() + "]");
				return;
			}

			if (master != null && !master.isOnline()) {
				if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[玩家不在线收回宠物] [{}] [{}] [{}] [{}] [{}]", new Object[] { master.getId(), master.getUsername(), master.getName(), this.name, this.id });
				boolean result = master.packupPet(false);
				if (!result) {
					game.removeSprite(this);

					PetManager.logger.warn("[收回宠物错误] [playerId:" + this.getOwnerId() + "] [" + this.getLogString() + "]");

				}
				return;
			}
			if (master != null) {
				if (master.getHp() <= 0 || master.getState() == Player.STATE_DEAD) {
					master.packupPet(true);
					return;
				}
			}

			if (state != STATE_DEAD) {
				if (this.getHp() <= 0) {
					this.removeMoveTrace();
					this.setState(STATE_DEAD);
						// 降低快乐值
						this.setHappyness(this.getHappyness() - 每次死亡快乐值下降值);
						// 降低寿命
						if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
							this.setLifeTime(this.getLifeTime() - 每次死亡寿命值下降值_korea);
						} else {
							this.setLifeTime(this.getLifeTime() - 每次死亡寿命值下降值);
						}
					// 30s后宠物尸体消失
					this.disapearTime = heartBeatStartTime + 30000;
					this.setStun(false);
					this.setImmunity(false);
					this.setInvulnerable(false);
					this.setPoison(false);
					this.setAura((byte) -1);
					this.setHold(false);
					this.setWeak(false);
					this.setSilence(false);
					this.setShield((byte) -1);

					// 循环清楚buff
					if (buffs != null) {
						for (int i = buffs.size() - 1; i >= 0; i--) {
							Buff bu = buffs.get(i);
							if (bu != null) {
								bu.end(this);
								if (bu.isForover() || bu.isSyncWithClient()) {
									this.removedBuffs.add(bu);
								}
								buffs.remove(i);

								if (ActiveSkill.logger.isDebugEnabled()) {
									// ActiveSkill.logger.debug("[死亡去除BUFF] [" + getName() + "] [死亡] [" +
									// (bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".") + 1)) + ":" + bu.getTemplateName() + "] [time:" +
									// bu.getInvalidTime() + "]");
									if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("[死亡去除BUFF] [{}] [死亡] [{}:{}] [time:{}]", new Object[] { getName(), (bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".") + 1)), bu.getTemplateName(), bu.getInvalidTime() });
								}
							}
						}
					}

					if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[宠物死亡收回宠物] [{}] [{}] [{}] [{}] [{}]", new Object[] { master.getId(), master.getUsername(), master.getName(), this.name, this.id });
					master.packupPet(true);
				}
			}

			if (state == STATE_DEAD) {
				return;
			}

			// 出战状态下，每10分钟降低一些快乐值
			long period = heartBeatStartTime - this.lastUpdateHappynessTime;
			if (period > 10 * 60 * 1000) {
				this.setLastUpdateHappynessTime(heartBeatStartTime);
				this.setLastUpdateLifeTime(heartBeatStartTime);
				this.setHappyness(this.getHappyness() - 规定时间快乐值下降值);
				// this.setLifeTime(this.getLifeTime() - 规定时间寿命下降值);
			}

			if (lastHeartbeatTime > 0 && heartBeatStartTime - lastHeartbeatTime > 600000) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
					this.setLifeTime(this.getLifeTime() - 规定时间寿命下降值_korea);
				}
				lastHeartbeatTime = heartBeatStartTime;
			}
			if (lastHeartbeatTime == 0) {
				lastHeartbeatTime = heartBeatStartTime;
			}
			if (this.happyness <= 0) {
				this.setAlive(false);
				if (master != null) {
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.debug("[宠物快乐度小于0收回宠物] [{}] [{}] [{}] [{}] [{}]", new Object[] { master.getId(), master.getUsername(), master.getName(), this.name, this.id });
					}

					master.send_HINT_REQ(Translate.translateString(Translate.宠物xx快乐值小于0召回了, new String[][] { { Translate.STRING_1, this.name } }));
					master.packupPet(true);
				}
				return;
			}

			if (this.lifeTime <= 0) {
				this.setAlive(false);
				if (master != null) {
					if (PetManager.logger.isDebugEnabled()) {
						PetManager.logger.debug("[宠物寿命小于0收回宠物] [{}] [{}] [{}] [{}] [{}]", new Object[] { master.getId(), master.getUsername(), master.getName(), this.name, this.id });
					}
					master.send_HINT_REQ(Translate.translateString(Translate.宠物xx寿命值小于0召回了, new String[][] { { Translate.STRING_1, this.name } }));
					master.packupPet(true);
				}
				return;
			}

			// 定身或者眩晕的情况下，停止移动
			if (this.isHold() || this.isStun()) {
				if (this.getMoveTrace() != null) {
					stopAndNotifyOthers();
				}
			}
			if (heartBeatStartTime - lastTimeForRecoverHPBAndMPB > 500) {
				lastTimeForRecoverHPBAndMPB = heartBeatStartTime;
				int hpa = this.getHpRecoverExtend();
				// 是否要考虑越界的问题？
				if ((hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)) {
					this.setHp(getHp() + hpa);
				}
			}
			if (heartBeatStartTime - lasthpRecoverExtend2Time > 5000) {
				lasthpRecoverExtend2Time = heartBeatStartTime;
				int hpa = this.getHpRecoverExtend2();
				// 是否要考虑越界的问题？
				if ((hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)) {
					float f = hpRecoverExtend2 / 1000F;
					int aa = (int) (this.getMaxHP() * f);
					int resultHp = getHp() + aa;
					if (resultHp > this.getMaxHP()) {
						this.setHp(getMaxHP());
					} else {
						this.setHp(resultHp);
					}
				}
			}
			if (lastHeartbeatLiftTime >= 10 && this.getLifeTime() < 10) {
				if (PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {// 只有韩国提示
					if (master != null) {
						master.sendError(Translate.宠物寿命不足提示);
					}
				}
			}
			lastHeartbeatLiftTime = this.getLifeTime();// 记录寿命

			this.skillAgent.heartbeat(game);

			// buff
			if (heartBeatStartTime - lastTimeForBuffs > 500) {
				lastTimeForBuffs = heartBeatStartTime;

				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);
					if (buff != null) {
						if (buff.getInvalidTime() <= heartBeatStartTime) {
							buff.end(this);

							if (buff.isForover() || buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(i);
						} else {
							buff.heartbeat(this, heartBeatStartTime, interval, game);
							if (buff.getInvalidTime() <= heartBeatStartTime) {
								buff.end(this);

								if (buff.isForover() || buff.isSyncWithClient()) {
									this.removedBuffs.add(buff);
								}
								buffs.remove(i);
							}
						}
					}
				}

				// 光环技能
				auraSkillAgent.heartbeat(heartBeatStartTime, interval, game);
				nagent.heartBeat(heartBeatStartTime, interval, game);
				try {
					Player p = this.getMaster();
					if (this.attributeAddRate > 0 && p != null && (heartBeatStartTime - lastChangeTime >= 3000)) {
						lastChangeTime = heartBeatStartTime;
						p.notifyAttributeAttackChange();
					}
				} catch (Exception e) {
					PetManager.logger.warn("[全能法戒转换] [异常] [" + this.getId() + "]", e);
				}
				try {
					if (lastRemoveDebuffTime > 0 && (heartBeatStartTime - lastRemoveDebuffTime) >= removeIntever) {
						lastRemoveDebuffTime = heartBeatStartTime;
						removeOneDebuff(this.getMaster());
					}
				} catch (Exception e) {
					PetManager.logger.warn("[移除负面状态] [异常] [" + this.getId() + "]", e);
				}
			}
		} catch (Exception e) {
			Game.logger.error("宠物心跳异常", e);
		}
	}
	
	public void removeOneDebuff(Player player) {
		Player resultP = player;
		if (player.getTeam() != null) {
			List<Player> lists = new ArrayList<Player>();
			if (player.getCurrentGame() != player.getTeam().getCaptain().getCurrentGame()) {
				lists.add(player.getTeam().getCaptain());
			}
			for (Player p : player.getTeam().getMembers()) {
				if (player.getCurrentGame() == p.getCurrentGame() && p.hasDebuff()) {
					lists.add(p);
				}
			}
			if (lists.size() > 0) {
				int index = player.random.nextInt(lists.size());
				resultP = lists.get(index);
			}
		}
		if (resultP != null) {
			Buff buff = resultP.removeDebuff();
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[移除玩家负面buff] [宠物id:" + this.getId() + "] [玩家:" + resultP.getLogString() + "] [buff:" + (buff == null ? "null" :buff.getTemplateName()) + "]");
			}
		} else {
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[移除玩家负面buff] [失败] [没有找到合适的玩家] [宠物id:" + this.getId() + "]");
			}
		}
	}

	/**
	 * 跟随主人
	 */
	// public void followWithMaster(Game game) {
	// long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	// if(master == null) {
	// Game.logger.warn("[宠物跟随主人] [警告：宠物无主人或者未被召唤！] ["+this.id+"] ["+game.getGameInfo().getName()+"]", start);
	// return;
	// }
	// if((this.getX() > master.getX() - stopPostion && this.getX() < master.getX() + stopPostion) &&
	// (this.getY() > master.getY() - stopPostion && this.getY() < master.getY() + stopPostion)) {
	// if(path != null) {
	// this.removeMoveTrace();
	// if(this.getMoveSpeed() != master.getSpeed()) {
	// this.setMoveSpeed(master.getSpeed());
	// }
	// Game.logger.info("[宠物跟随主人] [已经达到主人旁边] ["+this.id+"] ["+master.getId()+"] ["+master.getName()+"] ["+master.getUsername()+"] [game:"+game.getGameInfo().getName()+"]", start);
	// }
	// } else {
	// //如果离主人距离过远，则跑回主人身边
	// //规则是，如果大于距离D，那么直接瞬移到玩家身边，否则如果距离大于C，那么快速返回玩家身边，如果只是大于B，那么以自身速度跑回玩家身边。
	// int disX = Math.abs(this.getX()-master.getX());
	// int disY = Math.abs(this.getY()-master.getY());
	// if(disX > this.distanceD || disY > this.distanceD){
	// //直接瞬移到玩家身边，实现上以速度放大5倍
	// this.setMoveSpeed(master.getSpeed()*5);
	// this.fightingAgent.pathFinding(game, master.getX(), master.getY());
	// Game.logger.info("[宠物跟随主人] [远离主人距离D，以5倍速度返回主人身边] ["+this.id+"] ["+master.getId()+"] ["+master.getName()+"] ["+master.getUsername()+"] [game:"+game.getGameInfo().getName()+"]",
	// start);
	// } else if(disX > this.distanceC || disY > this.distanceC){
	// //放大宠物速度为原来的2倍，跑回玩家身边
	// this.setMoveSpeed(master.getSpeed()*2);
	// this.fightingAgent.pathFinding(game, master.getX(), master.getY());
	// Game.logger.info("[宠物跟随主人] [远离主人距离C，以2倍速度返回主人身边] ["+this.id+"] ["+master.getId()+"] ["+master.getName()+"] ["+master.getUsername()+"] [game:"+game.getGameInfo().getName()+"]",
	// start);
	// } else if(disX > this.stopPostion || disY > this.stopPostion) {
	// this.setMoveSpeed(master.getSpeed());
	// this.fightingAgent.pathFinding(game, master.getX(), master.getY());
	// Game.logger.info("[宠物跟随主人] [远离主人距离B，以正常速度返回主人身边] ["+this.id+"] ["+master.getId()+"] ["+master.getName()+"] ["+master.getUsername()+"] [game:"+game.getGameInfo().getName()+"]",
	// start);
	// }
	// }
	// this.innerState = 0;
	// }

	// /**
	// * 通知主人收回宠物
	// */
	// public void notifyPackupFromOwner(boolean bln) {
	//
	// if (master != null) {
	// master.packupPet(bln);
	// // temp.setActivePetId(this.getId());
	// }
	// }

	public Skill getSkillById(long skillId) {

		if (skillId == commonSkillId) {
			CareerManager cm = CareerManager.getInstance();
			return cm.getSkillById((int) skillId);
		}
		for (int i = 0; skillIds != null && i < this.skillIds.length; i++) {
			if (skillIds[i] == skillId && this.activeSkillLevels != null && i < this.activeSkillLevels.length) {
				CareerManager cm = CareerManager.getInstance();
				return cm.getSkillById(skillIds[i]);
			}
		}
		return null;
	}

	/**
	 * 如果不存在此技能，返回0
	 * 否则至少是返回1
	 * @param sid
	 * @return
	 */
	public int getSkillLevelById(int sid) {

		if (sid == commonSkillId) {
			return 1;
		}

		for (int i = 0; skillIds != null && i < this.skillIds.length; i++) {
			if (skillIds[i] == sid && this.activeSkillLevels != null && i < this.activeSkillLevels.length) {
				if (activeSkillLevels[i] == 0) return 1;
				return activeSkillLevels[i];
			}
		}
		return 0;
	}

	public int getActivationType() {
		return activationType;
	}

	public int getPursueWidth() {
		return pursueWidth;
	}

	public int getPursueHeight() {
		return pursueHeight;
	}

	public ArrayList<ActiveSkill> getSkillList() {
		return skillList;
	}

	public CountTimesSkillAgent getSkillAgent() {
		return skillAgent;
	}

	public PetFightingAgent getFightingAgent() {
		return fightingAgent;
	}

	public int getInnerState() {
		return innerState;
	}

	public void setPursueWidth(int pursueWidth) {
		this.pursueWidth = pursueWidth;
	}

	public void setPursueHeight(int pursueHeight) {
		this.pursueHeight = pursueHeight;
	}

	public int[] getSkillIds() {
		return skillIds;
	}

	public void setSkillIds(int[] skillIds) {
		this.skillIds = skillIds;
		PetManager.em.notifyFieldChange(this, "skillIds");
	}

	public int[] getActiveSkillLevels() {
		return activeSkillLevels;
	}

	public void setActiveSkillLevels(int[] activeSkillLevels) {
		this.activeSkillLevels = activeSkillLevels;
		PetManager.em.notifyFieldChange(this, "activeSkillLevels");
	}

	public void setActivationType(int activationType) {
		this.activationType = activationType;
		PetManager.em.notifyFieldChange(this, "activationType");
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
		PetManager.em.notifyFieldChange(this, "career");
	}

	// public int getLevel() {
	// return level;
	// }
	//
	// public void setLevel(int level) {
	// this.level = level;
	// }

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
		PetManager.em.notifyFieldChange(this, "sex");
	}

	public byte getCharacter() {
		return character;
	}

	public void setCharacter(byte character) {
		this.character = character;
		PetManager.em.notifyFieldChange(this, "character");
	}

	public byte getQuality() {
		return quality;
	}

	public void setQuality(byte quality) {
		this.quality = quality;
		PetManager.em.notifyFieldChange(this, "quality");
	}

	public int getQualityScore() {
		return qualityScore;
	}

	public void setQualityScore(int qualityScore) {
		if (this.qualityScore < qualityScore && getTempPoints() == null) {// 防止加点过程中造成排名变化。
			com.fy.engineserver.event.Event evt = new EventWithObjParam(com.fy.engineserver.event.Event.PET_SCORE_CHANGE, this);
			EventRouter.getInst().addEvent(evt);
		}
		this.qualityScore = qualityScore;
		PetManager.em.notifyFieldChange(this, "qualityScore");
	}

	public int getHappyness() {
		return happyness;
	}

	public void setHappyness(int happyness) {
		if (happyness < 0) {
			happyness = 0;
		}
		this.happyness = happyness;
		this.setLastUpdateHappynessTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		PetManager.em.notifyFieldChange(this, "happyness");
	}

	public byte getStarClass() {
		return starClass;
	}

	public void setStarClass(byte starClass) {
		this.starClass = starClass;
		PetManager.em.notifyFieldChange(this, "starClass");
	}

	public byte getGeneration() {
		return generation;
	}

	public void setGeneration(byte generation) {
		this.generation = generation;
		PetManager.em.notifyFieldChange(this, "generation");
	}

	public byte getVariation() {
		return variation;
	}

	public void setVariation(byte variation) {
		this.variation = variation;
		PetManager.em.notifyFieldChange(this, "variation");
	}

	public int getStrengthQuality() {
		return strengthQuality;
	}

	public void setStrengthQuality(int strengthQuality) {
		this.strengthQuality = strengthQuality;
	}

	public int getDexterityQuality() {
		return dexterityQuality;
	}

	public void setDexterityQuality(int dexterityQuality) {
		this.dexterityQuality = dexterityQuality;
	}

	public int getSpellpowerQuality() {
		return spellpowerQuality;
	}

	public void setSpellpowerQuality(int spellpowerQuality) {
		this.spellpowerQuality = spellpowerQuality;
	}

	public int getConstitutionQuality() {
		return constitutionQuality;
	}

	public void setConstitutionQuality(int constitutionQuality) {
		this.constitutionQuality = constitutionQuality;
	}

	public int getDingliQuality() {
		return dingliQuality;
	}

	public void setDingliQuality(int dingliQuality) {
		this.dingliQuality = dingliQuality;
	}

	public int getTrainLevel() {
		return trainLevel;
	}

	public void setTrainLevel(int trainLevel) {
		this.trainLevel = trainLevel;
		PetManager.em.notifyFieldChange(this, "trainLevel");
	}

	public byte getRarity() {
		return rarity;
	}

	public void setRarity(byte rarity) {
		this.rarity = rarity;
		PetManager.em.notifyFieldChange(this, "rarity");
	}

	public byte getGrowthClass() {
		return growthClass;
	}

	public void setGrowthClass(byte growthClass) {
		this.growthClass = growthClass;
		PetManager.em.notifyFieldChange(this, "growthClass");
	}

	public byte getBreedTimes() {
		return breedTimes;
	}

	public void setBreedTimes(byte breedTimes) {
		this.breedTimes = breedTimes;
		PetManager.em.notifyFieldChange(this, "breedTimes");
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		if (lifeTime < 0) {
			lifeTime = 0;
		}
		this.lifeTime = lifeTime;
		PetManager.em.notifyFieldChange(this, "lifeTime");
	}

	public void setExp(long exp) {
		this.exp = exp;
		PetManager.em.notifyFieldChange(this, "exp");
	}

	public int getGrowth() {
		return growth;
	}

	public void setGrowth(int growth) {
		this.growth = growth;
	}

	public byte getBreededTimes() {
		return breededTimes;
	}

	public void setBreededTimes(byte breededTimes) {
		this.breededTimes = breededTimes;
		PetManager.em.notifyFieldChange(this, "breededTimes");
	}

	public long getNextLevelExp() {
		return nextLevelExp;
	}

	public void setNextLevelExp(long nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		PetManager.em.notifyFieldChange(this, "category");
	}

	public boolean isSummoned() {
		if (master != null) {
			return true;
		}
		return false;
	}

	// public void setSummoned(boolean summoned) {
	// this.summoned = summoned;
	// }

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		PetManager.logger.debug("[" + this.getLogString() + "] [设置ownerId] [ownerIdold:" + this.ownerId + "] [" + ownerId + "]");
		this.ownerId = ownerId;
		PetManager.em.notifyFieldChange(this, "ownerId");
	}

	/**
	 * 修改力量，会修改物理攻击上限4，物理攻击下限4，物理防御1
	 */
	@Override
	public void setStrength(int value) {
		this.strength = value < 0 ? 0 : value;
	}

	@Override
	public void setStrengthA(int value) {
		this.strengthA = value;
		int a = (strengthA + strengthB + strengthS) * (strengthC + 100) / 100;
		this.setStrength(a);
	}

	@Override
	public void setStrengthB(int value) {
		this.strengthB = value;
		int a = (strengthA + strengthB + strengthS) * (strengthC + 100) / 100;
		this.setStrength(a);
	}

	@Override
	public void setStrengthC(int value) {
		this.strengthC = value;
		int a = (strengthA + strengthB + strengthS) * (strengthC + 100) / 100;
		this.setStrength(a);
	}

	@Override
	public void setStrengthS(int value) {
		this.strengthS = value;
		int a = (strengthA + strengthB + strengthS) * (strengthC + 100) / 100;
		this.setStrength(a);
		PetManager.em.notifyFieldChange(this, "strengthS");
	}

	/**
	 * 修改内力
	 */
	@Override
	public void setSpellpower(int value) {
		this.spellpower = value < 0 ? 0 : value;
	}

	@Override
	public void setSpellpowerA(int value) {
		this.spellpowerA = value;
		int a = (spellpowerA + spellpowerB + spellpowerS) * (100 + spellpowerC) / 100;
		this.setSpellpower(a);
	}

	@Override
	public void setSpellpowerB(int value) {
		this.spellpowerB = value;
		int a = (spellpowerA + spellpowerB + spellpowerS) * (100 + spellpowerC) / 100;
		this.setSpellpower(a);
	}

	@Override
	public void setSpellpowerC(int value) {
		this.spellpowerC = value;
		int a = (spellpowerA + spellpowerB + spellpowerS) * (100 + spellpowerC) / 100;
		this.setSpellpower(a);
	}

	@Override
	public void setSpellpowerS(int value) {
		this.spellpowerS = value;
		int a = (spellpowerA + spellpowerB + spellpowerS) * (100 + spellpowerC) / 100;
		this.setSpellpower(a);
		PetManager.em.notifyFieldChange(this, "spellpowerS");
	}

	/**
	 * 修改身法
	 */
	@Override
	public void setDexterity(int value) {
		this.dexterity = value < 0 ? 0 : value;
	}

	@Override
	public void setDexterityA(int value) {
		this.dexterityA = value;
		int a = (dexterityA + dexterityB + dexterityS) * (dexterityC + 100) / 100;
		this.setDexterity(a);
	}

	@Override
	public void setDexterityB(int value) {
		this.dexterityB = value;
		int a = (dexterityA + dexterityB + dexterityS) * (dexterityC + 100) / 100;
		this.setDexterity(a);
	}

	@Override
	public void setDexterityC(int value) {
		this.dexterityC = value;
		int a = (dexterityA + dexterityB + dexterityS) * (dexterityC + 100) / 100;
		this.setDexterity(a);
	}

	@Override
	public void setDexterityS(int value) {
		this.dexterityS = value;
		int a = (dexterityA + dexterityB + dexterityS) * (dexterityC + 100) / 100;
		this.setDexterity(a);
		PetManager.em.notifyFieldChange(this, "dexterityS");
	}

	/**
	 * 修改体力
	 */
	@Override
	public void setConstitution(int value) {
		this.constitution = value < 0 ? 0 : value;
	}

	@Override
	public void setConstitutionA(int value) {
		this.constitutionA = value;
		int a = (constitutionA + constitutionB + constitutionS) * (constitutionC + 100) / 100;
		this.setConstitution(a);
	}

	@Override
	public void setConstitutionB(int value) {
		this.constitutionB = value;
		int a = (constitutionA + constitutionB + constitutionS) * (constitutionC + 100) / 100;
		this.setConstitution(a);
	}

	@Override
	public void setConstitutionC(int value) {
		this.constitutionC = value;
		int a = (constitutionA + constitutionB + constitutionS) * (constitutionC + 100) / 100;
		this.setConstitution(a);
	}

	@Override
	public void setConstitutionS(int value) {
		this.constitutionS = value;
		int a = (constitutionA + constitutionB + constitutionS) * (constitutionC + 100) / 100;
		this.setConstitution(a);
		PetManager.em.notifyFieldChange(this, "constitutionS");
	}

	@Override
	public void setDingli(int value) {
		this.dingli = value < 0 ? 0 : value;
	}

	@Override
	public void setDingliA(int value) {
		this.dingliA = value;
		int a = (dingliA + dingliB + dingliS) * (dingliC + 100) / 100;
		this.setDingli(a);
	}

	@Override
	public void setDingliB(int value) {
		this.dingliB = value;
		int a = (dingliA + dingliB + dingliS) * (dingliC + 100) / 100;
		this.setDingli(a);
	}

	@Override
	public void setDingliC(int value) {
		this.dingliC = value;
		int a = (dingliA + dingliB + dingliS) * (dingliC + 100) / 100;
		this.setDingli(a);
	}

	@Override
	public void setDingliS(int value) {
		this.dingliS = value;
		int a = (dingliA + dingliB + dingliS) * (dingliC + 100) / 100;
		this.setDingli(a);
		PetManager.em.notifyFieldChange(this, "dingliS");
	}

	/**
	 * 得到上次更新快乐值的时间
	 * @return
	 */
	public long getLastUpdateHappynessTime() {
		return lastUpdateHappynessTime;
	}

	/**
	 * 设置快乐值
	 * @param lastUpdateHappynessTime
	 */
	public void setLastUpdateHappynessTime(long lastUpdateHappynessTime) {
		this.lastUpdateHappynessTime = lastUpdateHappynessTime;
		PetManager.em.notifyFieldChange(this, "lastUpdateHappynessTime");
	}

	public long getLastPackupTime() {
		return lastPackupTime;
	}

	public void setLastPackupTime(long lastPackupTime) {
		this.lastPackupTime = lastPackupTime;
		PetManager.em.notifyFieldChange(this, "lastPackupTime");
	}

	public long getPetPropsId() {
		return petPropsId;
	}

	public void setPetPropsId(long petPropsId) {
		PetManager.logger.error("[" + this.getLogString() + "] [设置ownerId] [entityIdold:" + this.petPropsId + "] [" + petPropsId + "]");
		this.petPropsId = petPropsId;
		PetManager.em.notifyFieldChange(this, "petPropsId");
	}

	public int getUnAllocatedPoints() {
		return unAllocatedPoints;
	}

	public void setUnAllocatedPoints(int unAllocatedPoints) {
		this.unAllocatedPoints = unAllocatedPoints;
		PetManager.em.notifyFieldChange(this, "unAllocatedPoints");
	}

	public int getExpPercent() {
		return expPercent;
	}

	public void setExpPercent(int expPercent) {
		this.expPercent = expPercent;
		PetManager.em.notifyFieldChange(this, "expPercent");
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		Pet p = new Pet();

		p.cloneAllInitNumericalProperty(this);

		p.category = this.category;

		p.activationType = this.activationType;
		p.skillIds = this.skillIds;
		p.activeSkillLevels = this.activeSkillLevels;

		p.career = this.career;
		p.monsterType = this.monsterType;
		p.pursueHeight = this.pursueHeight;
		p.pursueWidth = this.pursueWidth;

		return p;
	}

	public int getSize() {
		return 10;
	}

	public void remove(int arg0) {
		try {
			PetManager.em.save(this);
		} catch (Exception e) {
			PetManager.logger.error("[从缓存删除异常]", e);
		}
	}

	/**
	 * 是否鉴定过
	 */
	private boolean identity = false;

	public String getLogString() {
		return "id:" + id + "] [name:" + name + "] [level:" + level + "] [article:" + category + "";
	}

	public String getLogOfInterest() {
		return "后面是宠物信息] [主人id:" + ownerId + "] [宠物名:" + name + "] [宠物id:" + id + "] [跟随类型:" + activationType + "] [品质:" + quality + "] [等级:" + level + "] [携带等级:" + realTrainLevel + "] [星级:" + starClass + "] [代数:" + generation + "] [宠物实体名：" + petPropsName + "] [实体ID:" + petPropsId;
	}

	public int getMaxLevle() {
		return maxLevle;
	}

	public void setMaxLevle(int maxLevle) {
		this.maxLevle = maxLevle;
		PetManager.em.notifyFieldChange(this, "maxLevle");
	}

	public long getLastUpdateLifeTime() {
		return lastUpdateLifeTime;
	}

	public void setLastUpdateLifeTime(long lastUpdateLifeTime) {
		this.lastUpdateLifeTime = lastUpdateLifeTime;
		PetManager.em.notifyFieldChange(this, "lastUpdateLifeTime");
	}

	public int getMaxHappyness() {
		return maxHappyness;
	}

	public void setMaxHappyness(int maxHappyness) {
		this.maxHappyness = maxHappyness;
	}

	public int getMaxLifeTime() {
		return maxLifeTime;
	}

	public void setMaxLifeTime(int maxLifeTime) {
		this.maxLifeTime = maxLifeTime;
		PetManager.em.notifyFieldChange(this, "maxLifeTime");
	}

	public boolean isIdentity() {
		return identity;
	}

	public void setIdentity(boolean identity) {
		this.identity = identity;
		PetManager.em.notifyFieldChange(this, "identity");
	}

	// 扩大100
	@SimpleColumn(length = 150)
	private float[] realRandom = new float[10];

	// 扩大100
	@SimpleColumn(length = 150)
	private float[] beforeReplaceRandom = new float[10];

	private byte tempGrowthClass;

	@SimpleColumn(mysqlName = "replace1")
	private boolean replace = true;

	// 显示力量资质最小值
	private transient int showMinStrengthQuality;
	// 显示身法资质最小值
	private transient int showMinDexterityQuality;
	// 显示灵力资质最小值
	private transient int showMinSpellpowerQuality;
	// 显示耐力资质最小值
	private transient int showMinConstitutionQuality;
	// 显示定力资质最小值
	private transient int showMinDingliQuality;

	// 显示力量资质满值
	private transient int showMaxStrengthQuality;
	// 显示身法资质满值
	private transient int showMaxDexterityQuality;
	// 显示灵力资质满值
	private transient int showMaxSpellpowerQuality;
	// 显示耐力资质满值
	private transient int showMaxConstitutionQuality;
	// 显示定力资质满值
	private transient int showMaxDingliQuality;
	private int repairNum;

	// private int tempMinRandom;
	//
	// public int getTempMinRandom() {
	// return tempMinRandom;
	// }
	//
	// public void setTempMinRandom(int tempMinRandom) {
	// this.tempMinRandom = tempMinRandom;
	// PetManager.em.notifyFieldChange(this, "tempMinRandom");
	// }

	// private int minRandom;
	// public int getMinRandom() {
	// return minRandom;
	// }
	//
	// public void setMinRandom(int minRandom) {
	// this.minRandom = minRandom;
	// PetManager.em.notifyFieldChange(this, "minRandom");
	// }

	/**
	 * 生成随机数扩大10
	 * @param real
	 *            (true)实际随机数 (false)刷新随机数
	 */
	public int createRandom(boolean real, int level) {

		if (real) {
			for (int i = 0; i < 5; i++) {
				int base1Property = PetManager.random.nextInt(10);
				realRandom[i] = 51 + base1Property;
			}
			for (int j = 5; j < 10; j++) {
				int base2Property = PetManager.random.nextInt(10);
				realRandom[j] = 80 + base2Property;
			}
			setRealRandom(realRandom);
			return 0;
		} else {
			return createRandomByFlushLevel(level);

		}
	}

	/**
	 * 返回值用成就统计
	 * @param level
	 * @return
	 */
	private int createRandomByFlushLevel(int level) {
		if (level >= 0 && level <= 2) {

			int upNum = PetManager.刷新对应必升个数[level];
			// 跟刷新属性对应 true 升 false 降
			boolean[] propertys = new boolean[10];
			for (int i = 0; i < upNum; i++) {
				int index = random.nextInt(10);
				propertys[index] = true;
			}

			for (int i = 0; i < propertys.length; i++) {
				if (!propertys[i]) {
					int tempNum = PetManager.random.nextInt(100);
					if (tempNum < 50) {
						// 升
						propertys[i] = true;
					}
				}
			}
			float[] real = this.getRealRandom();
			float[] temp = new float[10];
			int achieveStat = 0;
			for (int j = 0; j < propertys.length; j++) {
				float upOrUpdownValue = 0;
				int value = 0;
				if (level == 0) {
					// 1 -2
					value = PetManager.random.nextInt(10);
					value += 11;
					upOrUpdownValue = 1f * value / 10;

				} else if (level == 1) {
					// 3 -4
					value = PetManager.random.nextInt(10);
					value += 31;
					upOrUpdownValue = 1f * value / 10;
				} else if (level == 2) {
					// 5
					upOrUpdownValue = 5;
				}
				if (propertys[j]) {
					// 升不能太高
					if (j < 5) {
						// 前5个属性
						if (real[j] + upOrUpdownValue >= PetPropertyUtility.属性初值最大) {
							temp[j] = PetPropertyUtility.属性初值最大;
							achieveStat++;
						} else {
							temp[j] = real[j] + upOrUpdownValue;
						}
					} else {
						// 前5个属性
						if (real[j] + upOrUpdownValue >= PetPropertyUtility.资质初值最大) {
							temp[j] = PetPropertyUtility.资质初值最大;
							achieveStat++;
						} else {
							temp[j] = real[j] + upOrUpdownValue;
						}
					}
				} else {
					// 降不能太低
					if (j < 5) {
						// 前5个属性
						if (real[j] - upOrUpdownValue <= PetPropertyUtility.属性初值最小) {
							temp[j] = PetPropertyUtility.属性初值最小;
						} else {
							temp[j] = real[j] - upOrUpdownValue;
						}
					} else {
						// 前5个属性
						if (real[j] - upOrUpdownValue <= PetPropertyUtility.资质初值最小) {
							temp[j] = PetPropertyUtility.资质初值最小;
						} else {
							temp[j] = real[j] - upOrUpdownValue;
						}
					}
				}
			}

			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			StringBuffer sb3 = new StringBuffer();
			for (int z = 0; z < propertys.length; z++) {
				sb1.append(propertys[z]);
				sb1.append(": ");
				sb2.append(real[z]);
				sb2.append(": ");
				sb3.append(temp[z]);
				sb3.append(": ");
			}
			setBeforeReplaceRandom(temp);

			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[刷新生成升降] [" + level + "] [" + this.getLogString() + "] [" + sb1.toString() + "]");
				PetManager.logger.warn("[刷新生成升降前的随机数] [" + level + "] [" + this.getLogString() + "] [" + sb2.toString() + "]");
				PetManager.logger.warn("[刷新生成升降后的随机数] [" + level + "] [" + this.getLogString() + "] [" + sb3.toString() + "]");

			}
			return achieveStat;
		}
		return -1;
	}

	/**
	 * 返回随机数 扩大100
	 * @param real
	 *            true真实随机数 false刷新后的随机数
	 * @param siFangType
	 * @return
	 */
	public float getRandom(boolean real, PetRandomType randomType) {
		if (real) {
			int index = randomType.type;
			if (index >= realRandom.length) {
				PetManager.logger.error("[得到实际随机数错误] [" + this.getLogString() + "]");
				return 100;
			} else {
				return realRandom[index];
			}
		} else {
			int index = randomType.type;
			if (index >= beforeReplaceRandom.length) {
				PetManager.logger.error("[得到刷新随机数错误] [" + this.getLogString() + "]");
				return 100;
			} else {
				return beforeReplaceRandom[index];
			}
		}
	}

	public float[] getRealRandom() {
		return realRandom;
	}

	public void setRealRandom(float[] realRandom) {
		this.realRandom = realRandom;
		PetManager.em.notifyFieldChange(this, "realRandom");
	}

	public float[] getBeforeReplaceRandom() {
		return beforeReplaceRandom;
	}

	public void setBeforeReplaceRandom(float[] beforeReplaceRandom) {
		this.beforeReplaceRandom = beforeReplaceRandom;
		PetManager.em.notifyFieldChange(this, "beforeReplaceRandom");
	}

	public boolean isReplace() {
		return replace;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
		PetManager.em.notifyFieldChange(this, "replace");
	}

	enum PetRandomType {

		随机力量((byte) 0),
		随机灵力((byte) 1),
		随机身法((byte) 2),
		随机耐力((byte) 3),
		随机定力((byte) 4),
		随机力量资质((byte) 5),
		随机灵力资质((byte) 6),
		随机身法资质((byte) 7),
		随机耐力资质((byte) 8),
		随机定力资质((byte) 9);

		public byte type;

		private PetRandomType(byte type) {
			this.type = type;
		}

	}

	public int getRepairNum() {
		return repairNum;
	}

	public void setRepairNum(int repairNum) {
		this.repairNum = repairNum;
		PetManager.em.notifyFieldChange(this, "repairNum");
	}

	public int getShowMaxStrengthQuality() {
		return showMaxStrengthQuality;
	}

	public void setShowMaxStrengthQuality(int showMaxStrengthQuality) {
		this.showMaxStrengthQuality = showMaxStrengthQuality;
	}

	public int getShowMaxDexterityQuality() {
		return showMaxDexterityQuality;
	}

	public void setShowMaxDexterityQuality(int showMaxDexterityQuality) {
		this.showMaxDexterityQuality = showMaxDexterityQuality;
	}

	public int getShowMaxSpellpowerQuality() {
		return showMaxSpellpowerQuality;
	}

	public void setShowMaxSpellpowerQuality(int showMaxSpellpowerQuality) {
		this.showMaxSpellpowerQuality = showMaxSpellpowerQuality;
	}

	public int getShowMaxConstitutionQuality() {
		return showMaxConstitutionQuality;
	}

	public void setShowMaxConstitutionQuality(int showMaxConstitutionQuality) {
		this.showMaxConstitutionQuality = showMaxConstitutionQuality;
	}

	public int getShowMaxDingliQuality() {
		return showMaxDingliQuality;
	}

	public void setShowMaxDingliQuality(int showMaxDingliQuality) {
		this.showMaxDingliQuality = showMaxDingliQuality;
	}

	public int getShowMinStrengthQuality() {
		return showMinStrengthQuality;
	}

	public void setShowMinStrengthQuality(int showMinStrengthQuality) {
		this.showMinStrengthQuality = showMinStrengthQuality;
	}

	public int getShowMinDexterityQuality() {
		return showMinDexterityQuality;
	}

	public void setShowMinDexterityQuality(int showMinDexterityQuality) {
		this.showMinDexterityQuality = showMinDexterityQuality;
	}

	public int getShowMinSpellpowerQuality() {
		return showMinSpellpowerQuality;
	}

	public void setShowMinSpellpowerQuality(int showMinSpellpowerQuality) {
		this.showMinSpellpowerQuality = showMinSpellpowerQuality;
	}

	public int getShowMinConstitutionQuality() {
		return showMinConstitutionQuality;
	}

	public void setShowMinConstitutionQuality(int showMinConstitutionQuality) {
		this.showMinConstitutionQuality = showMinConstitutionQuality;
	}

	public int getShowMinDingliQuality() {
		return showMinDingliQuality;
	}

	public void setShowMinDingliQuality(int showMinDingliQuality) {
		this.showMinDingliQuality = showMinDingliQuality;
	}

	public byte getTempGrowthClass() {
		return tempGrowthClass;
	}

	public void setTempGrowthClass(byte tempGrowthClass) {
		this.tempGrowthClass = tempGrowthClass;
		PetManager.em.notifyFieldChange(this, "tempGrowthClass");
	}

	/**
	 * 临时加点 分配的点：0力量1灵力2身法3耐力4定力
	 */
	@SimpleColumn(length = 1000)
	private int[] tempPoints;

	public void initS() {
		if (tempPoints != null) {
			for (int i = 0; i < tempPoints.length; i++) {
				this.setUnAllocatedPoints(this.getUnAllocatedPoints() + tempPoints[i]);
				switch (i) {
				case PetPropertyUtility.加点力量:
					this.setStrengthS(this.getStrengthS() - tempPoints[i]);
					break;
				case PetPropertyUtility.加点灵力:
					this.setSpellpowerS(this.getSpellpowerS() - tempPoints[i]);
					break;
				case PetPropertyUtility.加点身法:
					this.setDexterityS(this.getDexterityS() - tempPoints[i]);
					break;
				case PetPropertyUtility.加点耐力:
					this.setConstitutionS(this.getConstitutionS() - tempPoints[i]);
					break;
				case PetPropertyUtility.加点定力:
					this.setDingliS(this.getDingliS() - tempPoints[i]);
					break;
				}
			}
			setTempPoints(null);
			PetFlyState pstate = PetManager.getInstance().getPetFlyState(this.getId(), master);
			if (pstate != null) {
				pstate.setTempPointRecord(new int[5]);
			}
			if (PetManager.logger.isWarnEnabled()) {
				PetManager.logger.warn("[取消玩家未还原的属性点] [" + this.getLogString() + "] [" + this.ownerId + "]");
			}
		}
	}

	public int[] getTempPoints() {
		return tempPoints;
	}

	public void setTempPoints(int[] tempPoints) {
		this.tempPoints = tempPoints;
		PetManager.em.notifyFieldChange(this, "tempPoints");
	}

	/**
	 * 取消临时分配的点
	 */
	public void cancleUnAllocate() {
		if (tempPoints != null) {
			int total = 0;
			for (int i : tempPoints) {
				total += i;
			}
			if (total > 0) {
				if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[取消分配加点成功] [取消前未分配加点:" + unAllocatedPoints + "] [取消后未分配加点:" + (unAllocatedPoints + total) + "]");
				// setUnAllocatedPoints(unAllocatedPoints+total);
			}
			this.init();
		}
		try {
			PetFlyState pstate = PetManager.getInstance().getPetFlyState(this.getId(), master);
			if(pstate != null){
				PetManager.logger.warn("[取消分配加点] [----] [{}] [{}]",new Object[]{pstate.getTempPoint(),(master == null?"nul":master.getLogString())});
				pstate.setTempPointRecord(new int[5]);
				if (pstate.getTempPoint() > 0) {
					pstate.setQianNengPoint(pstate.getQianNengPoint() + pstate.getTempPoint());
					this.setUnAllocatedPoints(this.getUnAllocatedPoints() - pstate.getTempPoint());// 把之前从灵性潜能点分配过去的点还回来
					pstate.setTempPoint(0);
					PetManager.getInstance().savePetFlyState(pstate, this.getId(), master);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			PetManager.logger.warn("[取消分配加点] [异常] [{}] [{}] [{}]", new Object[] { this.getLogString(), (master != null ? master.getLogString() : "nul"), e });
		}
		setTempPoints(null);
	}

	/**
	 * 确定临时分配的点
	 */
	public void confirmUnAllocate() {
		try {
			PetFlyState pstate = PetManager.getInstance().getPetFlyState(this.getId(), master);
			if (pstate != null) {
				if(pstate.getTempPoint() > 0){
					pstate.setTempPoint(0);
				}
				int [] points = pstate.getPointRecord();
				if(pstate.getPointRecord() == null){
					points = new int[5];
				}
				if(pstate.getPointRecord() != null){
					for (int i = 0; i < points.length; i++) {
						points[i] += pstate.getTempPointRecord()[i];
					}
				}
				pstate.setTempPointRecord(new int[5]);
				pstate.setPointRecord(points);
				PetManager.getInstance().savePetFlyState(pstate, this.getId(), master);
			}
		} catch (Exception e) {
			e.printStackTrace();
			PetManager.logger.warn("[确定分配加点] [异常] [{}] [{}] [{}]", new Object[] { this.getLogString(), (master != null ? master.getLogString() : "nul"), e });
		}
		setTempPoints(null);
		int q = qualityScore;
		this.init();
		//
		if (qualityScore > q) {
			com.fy.engineserver.event.Event evt = new EventWithObjParam(com.fy.engineserver.event.Event.PET_SCORE_CHANGE, this);
			EventRouter.getInst().addEvent(evt);
		}
	}

	public String getPetSort() {
		return petSort;
	}

	public void setPetSort(String petSort) {
		this.petSort = petSort;
		PetManager.em.notifyFieldChange(this, "petSort");
	}

	public static int[] phySkillIds = { 200, 201, 202, 203, 204, 204 };
	public static int[] magicSkillIds = { 205, 206, 207, 208, 209, 209 };

	private int commonSkillId;

	/**
	 * 宠物孵化后产生技能
	 */
	public void bindSkill() {

		CareerManager cm = CareerManager.getInstance();
		// 普通攻击
		int 携带等级 = this.getRealTrainLevel();

		int trainLevelIndex = PetPropertyUtility.获得携带等级索引(携带等级);
		int commonId = -1;
		if (this.getCareer() == PetPropertyUtility.物攻型 || this.getCareer() == PetPropertyUtility.敏捷型) {
			commonId = phySkillIds[trainLevelIndex];
		} else {
			commonId = magicSkillIds[trainLevelIndex];
		}
		Skill skill = cm.getSkillById(commonId);
		if (skill == null) {
			PetManager.logger.error("[宠物绑定技能错误] [普通技能为null] [技能id:" + commonId + "] [" + this.getLogString() + "]");
			return;
		}
		setCommonSkillId(commonId);

		// 随机技能个数
		int skillNum = PetPropertyUtility.randomSkillNum(this);

		int[] realSkill = new int[skillNum];
		int[] realSkillLevel = new int[skillNum];

		for (int i = 0; i < skillNum;) {

			int 性格 = this.getCharacter();
			Map<Integer, Map<Integer, List<Integer>>> map = PetManager.getInstance().getPetSkillMap();
			if (map != null) {

				Map<Integer, List<Integer>> map1 = map.get(trainLevel);
				if (map1 != null) {
					double[] d;
					try {
						d = PetPropertyUtility.技能出现几率[性格];
					} catch (Exception e) {
						PetManager.logger.error("[宠物绑定技能错误] [{}]", e);
						return;
					}
					int index = ProbabilityUtils.randomProbability(random, d);

					List<Integer> list = map1.get(index);
					int skillMax = list.size();
					int indexId = random.nextInt(skillMax);
					int skillId = (int) list.get(indexId);
					boolean have = false;
					for (int id : realSkill) {
						if (id == skillId) {
							have = true;
							break;
						}
					}
					if (!have) {

						skill = cm.getSkillById(skillId);
						if (skill != null) {
							realSkill[i] = skillId;
							realSkillLevel[i] = 1;
							i++;
						} else {
							PetManager.logger.error("[宠物绑定技能错误] [指定技能为null] [指定技能id:" + skillId + "]");
							realSkill = new int[0];
							realSkillLevel = new int[0];
							break;
						}
					}
				} else {
					PetManager.logger.error("[宠物绑定技能错误] [携带等级对应map为null]");
					realSkill = new int[0];
					realSkillLevel = new int[0];
					break;
				}

			} else {
				PetManager.logger.error("[宠物绑定技能错误] [map为null]");
				realSkill = new int[0];
				realSkillLevel = new int[0];
				break;
			}
		}

		setSkillIds(realSkill);

		setActiveSkillLevels(realSkillLevel);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < realSkill.length; i++) {
			sb.append("技能id:" + realSkill[i] + "");
		}
		if (PetManager.logger.isWarnEnabled()) PetManager.logger.warn("[宠物绑定技能成功] [" + this.getLogString() + "] [随机技能个数:" + skillNum + "] [实际技能个数:" + realSkill.length + "] [技能ids:" + sb.toString() + "]");

	}

	public int getCommonSkillId() {
		return commonSkillId;
	}

	public void setCommonSkillId(int commonSkillId) {
		this.commonSkillId = commonSkillId;
		PetManager.em.notifyFieldChange(this, "commonSkillId");
	}

	public byte getFlyNBState() {
		return flyNBState;
	}

	public void setFlyNBState(byte flyNBState) {
		this.flyNBState = flyNBState;
		getAroundMarks()[39] = true;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
		getAroundMarks()[38] = true;
	}

	public int getAlreadyMaxStar() {
		return alreadyMaxStar;
	}

	public void setAlreadyMaxStar(int alreadyMaxStar) {
		this.alreadyMaxStar = alreadyMaxStar;
		PetManager.em.notifyFieldChange(this, "alreadyMaxStar");
	}

	public byte getEggType() {
		return eggType;
	}

	public void setEggType(byte eggType) {
		this.eggType = eggType;
		PetManager.em.notifyFieldChange(this, "eggType");
	}

	public int getRealTrainLevel() {
		return realTrainLevel;
	}

	public void setRealTrainLevel(int realTrainLevel) {
		this.realTrainLevel = realTrainLevel;
		PetManager.em.notifyFieldChange(this, "realTrainLevel");
	}

	@SimpleColumn(name = "deleted")
	private boolean delete;

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
		PetManager.em.notifyFieldChange(this, "delete");
	}

	@Override
	public boolean canFreeFromBeDamaged(Fighter fighter) {
		if (master != null) {
			return master.canFreeFromBeDamaged(fighter);
		}
		return false;
	}

	public void setName(String newName) {
		super.setName(newName);
		PetManager.em.notifyFieldChange(this, "name");
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
		PetManager.em.notifyFieldChange(this, "country");
	}

	// 取物理宠物的物理攻击或法系宠物的法术攻击*技能性格加成
	// 技能性格加成=技能1的级别*是否相符+技能2的级别*是否相符……技能10的级别*是否相符
	// 初级 2% 相符 100%
	// 中级 3% 不符 50%
	// 高级 5%
	// 特级 7%
	// 终极 10%
	public void setPetScore() {
		/*
		 * float scoreTemp = 0;
		 * int attackTemp = 0;
		 * float score = 0;
		 * int[] skills = this.getSkillIds();
		 * if (skills != null && skills.length > 0) {
		 * int match = 100;
		 * int noMatch = 50;
		 * Map<Integer, PetSkillReleaseProbability> map = PetManager.getInstance().getMap();
		 * for (int id : skills) {
		 * Skill skill = CareerManager.getInstance().getSkillById(id);
		 * if (skill != null) {
		 * PetSkillReleaseProbability pp = map.get(id);
		 * if (pp != null) {
		 * if (pp.getCharacter() == this.getCharacter()) {
		 * score += 1.0 * match * PetPropertyUtility.根据技能名得到积分(skill);
		 * } else {
		 * score += 1.0 * noMatch * PetPropertyUtility.根据技能名得到积分(skill);
		 * }
		 * } else {
		 * PetManager.logger.error("[计算宠物品质值错误] [宠物配置没有这个技能] [id:" + id + "] [" + this.getLogString() + "]");
		 * }
		 * }
		 * }
		 * int attack = 0;
		 * if (this.getCareer() == PetPropertyUtility.物攻型 || this.getCareer() == PetPropertyUtility.敏捷型) {
		 * attack = this.getPhyAttack();
		 * } else {
		 * attack = this.getMagicAttack();
		 * }
		 * attackTemp = attack;
		 * scoreTemp = score;
		 * score = (int) Math.rint(1.0 * attack * score / 100);
		 * }
		 * setQualityScore((int) score);
		 * PetManager.logger.warn("[计算宠物排行榜积分] [" + this.getLogString() + "] [积分:" + score + "] [技能得分:" + scoreTemp + "] [攻击得分:" + attackTemp + "]");
		 */
		int[] skills = getSkillIds();
		int match = 0;
		int noMatch = 0;
		if (skills != null && skills.length > 0) {
			Map<Integer, PetSkillReleaseProbability> map = PetManager.getInstance().getMap();

			for (int id : skills) {
				PetSkillReleaseProbability pp = map.get(id);
				if (pp != null) {
					if (pp.getCharacter() == getCharacter()) {
						++match;
					} else {
						++noMatch;
					}
				} else {
					PetManager.logger.error("[计算宠物品质值错误] [宠物配置没有这个技能] [id:" + id + "]");
				}
			}

			if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[计算宠物品质值] [计算技能值完成]");
		}
		// 2013年9月20日9:42:40 康建虎修改为新的计算公式。
		/*
		 * int(攻击强度*命中率*（1+暴击率）*（1+符合性格技能个数（符合）*技能加成（符合）+技能个数（不符）*技能加成（不符））)
		 */
		Pet pet = this;
		int 攻击强度 = pet.getCareer() <= 1 ? pet.getPhyAttack() : pet.getMagicAttack();
		float 命中率 = (pet.getHitRate() + pet.getHitRateOther()) * 1.0f / 100;
		命中率 = Math.min(1, 命中率);
		float 暴击率 = (pet.getCriticalHitRate() + pet.getCriticalHitRateOther()) * 1.0f / 100;
		暴击率 = Math.min(0.1f, 暴击率);
		float newV = (攻击强度 * 命中率 * (1 + 暴击率)) * (1 + match * 0.05f + noMatch * 0.01f);
		int tianShSk = 0;
		// 一代/二代天生技能2W分，一代技能在二代宝宝身上3W分
		if (pet.getGeneration() == 0) {
			tianShSk += pet.talent1Skill > 0 ? 20000 : 0;
		} else {
			tianShSk += pet.talent2Skill > 0 ? 30000 : 0;
			tianShSk += pet.talent1Skill > 0 ? 20000 : 0;
		}
		PetManager.logger.debug("PetPropertyUtility.计算宠物品质值: [tianShSk:{}], [newV:{}]", tianShSk, newV);
		int vv = Math.round(newV) + tianShSk;
		setQualityScore(vv);
		PetManager.logger.info("Pet.setPetScore: [name:{}] [vv:{}]", name, vv);
	}

	public int getTalent1Skill() {
		return talent1Skill;
	}

	public String canUpgrade(Player player) {
		if (PetManager.isTest) {
			return "ok";
		}
		TransitRobberyEntity tentity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if (tentity != null && tentity.getFeisheng() != 1) {
			return Translate.需要玩家飞升宠物才可以飞升;
		}

		if (this.getRealTrainLevel() < 220) {
			return Translate.携带等级不满足飞升;
		}

		int tianFuSkillNums = 0;

		if (tianFuSkIds != null) {
			for (int id : tianFuSkIds) {
				if (id > 0) {
					tianFuSkillNums++;
				}
			}
		}
		if (tianFuSkillNums < 5) {
			return Translate.天赋技能个数不满足飞升;
		}

		boolean isMaxTuJianLevel = true;
		boolean isCanFly = true;
		ArticleEntity artE = player.getArticleEntity(petPropsId);
		if (artE != null && artE instanceof PetPropsEntity) {
			PetPropsEntity entity = (PetPropsEntity) artE;
			GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
			if (gradePet != null) {
				if (this.grade < gradePet.maxGrade) {
					isMaxTuJianLevel = false;
				}
				if (gradePet.flyType != 1) {
					isCanFly = false;
				}
			}
		}
		if (!isMaxTuJianLevel) {
			return Translate.飞升需要达到图鉴最高阶;
		}
		if (!isCanFly) {
			return Translate.此宠物不可飞升;
		}
		LianHunConf confCur = PetGrade.lianHunMap.get(this.getTrainLevel());
		if (confCur != null) {
			if (this.hunExp < confCur.toNextLvExp) {
				return Translate.此宠物魂值不满;
			}
		}
		return "ok";
	}

	public void setTalent1Skill(int talent1Skill) {
		this.talent1Skill = talent1Skill;
		PetManager.em.notifyFieldChange(this, "talent1Skill");
		try {
			if (this.getOwnerId() > 0 && talent1Skill > 0) {
				EventWithObjParam evt3 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getOwnerId(), RecordAction.获得一只拥有先天技能的宠物, 1L });
				EventRouter.getInst().addEvent(evt3);
			}
		} catch (Exception eex) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家拥有先天技能宠物异常] [" + this.getOwnerId() + "]", eex);
		}
	}

	public String getPetPropsString() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("[宠物属性] [username:" + getName() + ",id:" + getId() + "]");
		sbf.append("[血量:" + getMaxHP() + "血A:" + getMaxHPA() + "血B:" + getMaxHPB() + "血C:" + getMaxHPC() + ",法:" + getMaxMP() + ",速度:" + getSpeed() + ",外功:" + getPhyAttack() + ",外功A:" + getPhyAttackA() + ",外功B:" + getPhyAttackB() + ",外功C:" + getPhyAttackC() + ",外防:" + getPhyDefence() + ",法攻:" + getMagicAttack() + ",法攻A:" + getMagicAttackA() + ",法攻B:" + getMagicAttackB() + ",法攻C:" + getMagicAttackC() + ",法防:" + getMagicDefence() + ",破甲:" + getBreakDefence() + ",命中:" + getHit() + ",闪躲:" + getDodge() + ",精准:" + getAccurate() + ",暴击:" + getCriticalHit() + ",免暴:" + getCriticalDefence() + ",雷攻:" + getThunderAttack() + ",雷抗:" + getThunderDefence() + ",雷减:" + getThunderIgnoreDefence() + ",冰攻:" + getBlizzardAttack() + ",冰抗:" + getBlizzardDefence() + ",冰减:" + getBlizzardIgnoreDefence() + ",风攻:" + getWindAttack() + ",风抗:" + getWindDefence() + ",风减:" + getWindIgnoreDefence() + ",火攻:" + getFireAttack() + ",火抗:" + getFireDefence() + ",火减:" + getFireIgnoreDefence() + "]");
		return sbf.toString();
	}

	public int getTalent2Skill() {
		return talent2Skill;
	}

	public void setTalent2Skill(int talent2Skill) {
		this.talent2Skill = talent2Skill;
		PetManager.em.notifyFieldChange(this, "talent2Skill");
	}

	public int getWuXing() {
		return wuXing;
	}

	public void setWuXing(int wuXing) {
		this.wuXing = wuXing;
		PetManager.em.notifyFieldChange(this, "wuXing");
	}

	public int[] getTianFuSkIds() {
		return tianFuSkIds;
	}

	public void setTianFuSkIds(int[] tianFuSkIds) {
		this.tianFuSkIds = tianFuSkIds;
		PetManager.em.notifyFieldChange(this, "tianFuSkIds");
	}

	public int[] getTianFuSkIvs() {
		return tianFuSkIvs;
	}

	public void setTianFuSkIvs(int[] tianFuSkIvs) {
		this.tianFuSkIvs = tianFuSkIvs;
		PetManager.em.notifyFieldChange(this, "tianFuSkIvs");
	}
	
	public int getCriticalHitRate(){
		int result = this.criticalHitRate + this.getCriticalHitRateOther() / 10;
		if (result > 100) {
			return 100;
		}
		return result;
		}
	public int getHitRate(){
		int result = this.hitRate + this.getHitRateOther() / 10;
		if (result > 100) {
			return 100;
		}
		return result;
		}
	public int getDodgeRate(){
		int result = this.dodgeRate + this.getDodgeRateOther() / 10;
		if (result > 100) {
			return 100;
		}
		return result;
		}

	@Override
	public int getCritFactor() {
		return critFactor;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
		initTitle();
		PetManager.em.notifyFieldChange(this, "grade");
	}

	public int getAddBookTimes() {
		return addBookTimes;
	}

	public void setAddBookTimes(int addBookTimes) {
		this.addBookTimes = addBookTimes;
		PetManager.em.notifyFieldChange(this, "addBookTimes");
	}

	public int getHunExp() {
		return hunExp;
	}

	public void setHunExp(int hunExp) {
		this.hunExp = hunExp;
		PetManager.em.notifyFieldChange(this, "hunExp");
	}

	@Override
	public void setAvataRace(String avataRace) {
		super.setAvataRace(avataRace);
		PetManager.em.notifyFieldChange(this, "avataRace");
	}

	@Override
	public void setAvataSex(String avataSex) {
		super.setAvataSex(avataSex);
		PetManager.em.notifyFieldChange(this, "avataSex");
	}

	public int getHpStealPercent() {
		return hpStealPercent;
	}

	@Override
	public void setSpeed(int value) {
		super.setSpeed(value + extraSpeedNum);
	}
	
	@Override
	public void setCriticalHitRate(int value) {
		super.setCriticalHitRate(value + Pet2PropCalc.getInst().sumPoint(this, GenericBuff.ATT_BaoJiLv));
	}

	@Override
	public String toString() {
		return "Pet [critFactor=" + critFactor + ", hpStealPercent=" + hpStealPercent + ", category=" + category + ", petSort=" + petSort + ", sex=" + sex + ", maxHappyness=" + maxHappyness + ", happyness=" + happyness + ", maxLifeTime=" + maxLifeTime + ", lifeTime=" + lifeTime + ", character=" + character + ", quality=" + quality + ", qualityScore=" + qualityScore + ", nextLevelExp=" + nextLevelExp + ", ownerId=" + ownerId + ", ownerName=" + ownerName + ", petPropsId=" + petPropsId + ", starClass=" + starClass + ", generation=" + generation + ", variation=" + variation + ", strengthQuality=" + strengthQuality + ", dexterityQuality=" + dexterityQuality + ", spellpowerQuality=" + spellpowerQuality + ", constitutionQuality=" + constitutionQuality + ", dingliQuality=" + dingliQuality + ", showStrengthQuality=" + showStrengthQuality + ", alreadyMaxStar=" + alreadyMaxStar + ", eggType=" + eggType + ", trainLevel=" + trainLevel + ", realTrainLevel=" + realTrainLevel + ", maxLevle=" + maxLevle + ", rarity=" + rarity + ", growthClass=" + growthClass + ", growth=" + growth + ", unAllocatedPoints=" + unAllocatedPoints + ", expPercent=" + expPercent + ", lastUpdateHappynessTime=" + lastUpdateHappynessTime + ", lastUpdateLifeTime=" + lastUpdateLifeTime + ", lastPackupTime=" + lastPackupTime + ", activationType=" + activationType + ", pursueWidth=" + pursueWidth + ", pursueHeight=" + pursueHeight + ", skillIds=" + Arrays.toString(skillIds) + ", talent1Skill=" + talent1Skill + ", talent2Skill=" + talent2Skill + ", wuXing=" + wuXing + ", tianFuSkIds=" + Arrays.toString(tianFuSkIds) + ", tianFuSkIvs=" + Arrays.toString(tianFuSkIvs) + ", grade=" + grade + ", addBookTimes=" + addBookTimes + ", hunExp=" + hunExp + ", activeSkillLevels=" + Arrays.toString(activeSkillLevels) + ", lastTimeForBuffs=" + lastTimeForBuffs + ", currentEnemyTarget=" + currentEnemyTarget + ", buffs=" + buffs + ", removedBuffs=" + removedBuffs + ", newlyBuffs=" + newlyBuffs + ", skillList=" + skillList + ", skillAgent=" + skillAgent + ", fightingAgent=" + fightingAgent + ", auraSkillAgent=" + auraSkillAgent + ", auraSkill=" + auraSkill + ", innerState=" + innerState + ", killedTime=" + killedTime + ", disapearTime=" + disapearTime + ", master=" + master + ", pet2buff=" + pet2buff + ", towerRate=" + towerRate + ", dmgScaleRate=" + dmgScaleRate + ", dmgScale=" + dmgScale + ", exp2playerRatio=" + exp2playerRatio + ", specialTargetFactor=" + specialTargetFactor + ", hookInfo=" + hookInfo + ", showPhyAttack=" + showPhyAttack + ", showMagicAttack=" + showMagicAttack + ", deathNotify=" + deathNotify + ", identity=" + identity + ", realRandom=" + Arrays.toString(realRandom) + ", beforeReplaceRandom=" + Arrays.toString(beforeReplaceRandom) + ", tempGrowthClass=" + tempGrowthClass + ", replace=" + replace + ", showMinStrengthQuality=" + showMinStrengthQuality + ", showMinDexterityQuality=" + showMinDexterityQuality + ", showMinSpellpowerQuality=" + showMinSpellpowerQuality + ", showMinConstitutionQuality=" + showMinConstitutionQuality + showMaxDingliQuality + ", repairNum=" + repairNum + ", tempPoints=" + Arrays.toString(tempPoints) + ", commonSkillId=" + commonSkillId + ", delete=" + delete + ", icon=" + icon + ", flushFrequency=" + flushFrequency + ", deadLastingTime=" + deadLastingTime + ", gameName=" + gameName + ", bornPoint=" + bornPoint + ", huDunDamage=" + huDunDamage + ", classLevel=" + classLevel + ",  title=" + title + ", name=" + name + ", country=" + country + ", dialogContent=" + dialogContent + ", level=" + level + ", career=" + career + ", height=" + height + ", spriteType=" + spriteType + ", monsterType=" + monsterType + ", npcType=" + npcType + ", taskMark=" + taskMark + ", commonAttackSpeed=" + commonAttackSpeed + ", commonAttackRange=" + commonAttackRange + ", maxHP=" + maxHP + ", maxHPA=" + maxHPA + ", maxHPB=" + maxHPB + ", maxHPC=" + maxHPC + ", maxMP=" + maxMP + ", maxMPA=" + maxMPA + ", maxMPB=" + maxMPB + ", maxMPC=" + maxMPC + ", speed=" + speed + ", speedA=" + speedA + ", speedC=" + speedC + ", weaponDamage=" + weaponDamage + ", phyDefence=" + phyDefence + ", phyDefenceA=" + phyDefenceA + ", phyDefenceB=" + phyDefenceB + ", phyDefenceC=" + phyDefenceC + ", phyDefenceD=" + phyDefenceD + ", physicalDecrease=" + physicalDecrease + ", magicDefence=" + magicDefence + ", magicDefenceA=" + magicDefenceA + ", magicDefenceB=" + magicDefenceB + ", magicDefenceC=" + magicDefenceC + ", magicDefenceD=" + magicDefenceD + ", spellDecrease=" + spellDecrease + ", breakDefence=" + breakDefence + ", breakDefenceA=" + breakDefenceA + ", breakDefenceB=" + breakDefenceB + ", breakDefenceC=" + breakDefenceC + ", breakDefenceRate=" + breakDefenceRate + ", hit=" + hit + ", hitA=" + hitA + ", hitB=" + hitB + ", hitC=" + hitC + ", hitD=" + hitD + ", hitRate=" + hitRate + ", dodge=" + dodge + ", dodgeA=" + dodgeA + ", dodgeB=" + dodgeB + ", dodgeC=" + dodgeC + ", dodgeD=" + dodgeD + ", dodgeRate=" + dodgeRate + ", accurate=" + accurate + ", accurateA=" + accurateA + ", accurateB=" + accurateB + ", accurateC=" + accurateC + ", accurateRate=" + accurateRate + ", strength=" + strength + ", strengthA=" + strengthA + ", strengthB=" + strengthB + ", strengthC=" + strengthC + ", strengthS=" + strengthS + ", spellpower=" + spellpower + ", spellpowerA=" + spellpowerA + ", spellpowerB=" + spellpowerB + ", spellpowerC=" + spellpowerC + ", spellpowerS=" + spellpowerS + ", dexterity=" + dexterity + ", dexterityA=" + dexterityA + ", dexterityB=" + dexterityB + ", dexterityC=" + dexterityC + ", dexterityS=" + dexterityS + ", constitution=" + constitution + ", constitutionA=" + constitutionA + ", constitutionB=" + constitutionB + ", constitutionC=" + constitutionC + ", constitutionS=" + constitutionS + ", dingli=" + dingli + ", dingliA=" + dingliA + ", dingliB=" + dingliB + ", dingliC=" + dingliC + ", dingliS=" + dingliS + ", criticalHit=" + criticalHit + ", criticalHitA=" + criticalHitA + ", criticalHitB=" + criticalHitB + ", criticalHitC=" + criticalHitC + ", criticalHitRate=" + criticalHitRate + ", criticalDefence=" + criticalDefence + ", criticalDefenceA=" + criticalDefenceA + ", criticalDefenceB=" + criticalDefenceB + ", criticalDefenceC=" + criticalDefenceC + ", criticalDefenceRate=" + criticalDefenceRate + ", phyAttack=" + phyAttack + ", phyAttackA=" + phyAttackA + ", phyAttackB=" + phyAttackB + ", phyAttackC=" + phyAttackC + ", magicAttack=" + magicAttack + ", magicAttackA=" + magicAttackA + ", magicAttackB=" + magicAttackB + ", magicAttackC=" + magicAttackC + ", phyDefenceRateOther=" + phyDefenceRateOther + ", magicDefenceRateOther=" + magicDefenceRateOther + ", criticalHitRateOther=" + criticalHitRateOther + ", criticalDefenceRateOther=" + criticalDefenceRateOther + ", hitRateOther=" + hitRateOther + ", dodgeRateOther=" + dodgeRateOther + ", accurateRateOther=" + accurateRateOther + ", breakDefenceRateOther=" + breakDefenceRateOther + ", fireDefenceRateOther=" + fireDefenceRateOther + ", fireIgnoreDefenceRateOther=" + fireIgnoreDefenceRateOther + ", blizzardDefenceRateOther=" + blizzardDefenceRateOther + ", blizzardIgnoreDefenceRateOther=" + blizzardIgnoreDefenceRateOther + ", windDefenceRateOther=" + windDefenceRateOther + ", windIgnoreDefenceRateOther=" + windIgnoreDefenceRateOther + ", thunderDefenceRateOther=" + thunderDefenceRateOther + ", thunderIgnoreDefenceRateOther=" + thunderIgnoreDefenceRateOther + ", fireAttack=" + fireAttack + ", fireAttackA=" + fireAttackA + ", fireAttackB=" + fireAttackB + ", fireAttackC=" + fireAttackC + ", fireDefence=" + fireDefence + ", fireDefenceA=" + fireDefenceA + ", fireDefenceB=" + fireDefenceB + ", fireDefenceC=" + fireDefenceC + ", fireDefenceRate=" + fireDefenceRate + ", fireIgnoreDefence=" + fireIgnoreDefence + ", fireIgnoreDefenceA=" + fireIgnoreDefenceA + ", fireIgnoreDefenceB=" + fireIgnoreDefenceB + ", fireIgnoreDefenceC=" + fireIgnoreDefenceC + ", fireIgnoreDefenceRate=" + fireIgnoreDefenceRate + ", blizzardAttack=" + blizzardAttack + ", blizzardAttackA=" + blizzardAttackA + ", blizzardAttackB=" + blizzardAttackB + ", blizzardAttackC=" + blizzardAttackC + ", blizzardDefence=" + blizzardDefence + ", blizzardDefenceA=" + blizzardDefenceA + ", blizzardDefenceB=" + blizzardDefenceB + ", blizzardDefenceC=" + blizzardDefenceC + ", blizzardDefenceRate=" + blizzardDefenceRate + ", blizzardIgnoreDefence=" + blizzardIgnoreDefence + ", blizzardIgnoreDefenceA=" + blizzardIgnoreDefenceA + ", blizzardIgnoreDefenceB=" + blizzardIgnoreDefenceB + ", blizzardIgnoreDefenceC=" + blizzardIgnoreDefenceC + ", blizzardIgnoreDefenceRate=" + blizzardIgnoreDefenceRate + ", windAttack=" + windAttack + ", windAttackA=" + windAttackA + ", windAttackB=" + windAttackB + ", windAttackC=" + windAttackC + ", windDefence=" + windDefence + ", windDefenceA=" + windDefenceA + ", windDefenceB=" + windDefenceB + ", windDefenceC=" + windDefenceC + ", windDefenceRate=" + windDefenceRate + ", windIgnoreDefence=" + windIgnoreDefence + ", windIgnoreDefenceA=" + windIgnoreDefenceA + ", windIgnoreDefenceB=" + windIgnoreDefenceB + ", windIgnoreDefenceC=" + windIgnoreDefenceC + ", windIgnoreDefenceRate=" + windIgnoreDefenceRate + ", thunderAttack=" + thunderAttack + ", thunderAttackA=" + thunderAttackA + ", thunderAttackB=" + thunderAttackB + ", thunderAttackC=" + thunderAttackC + ", thunderDefence=" + thunderDefence + ", thunderDefenceA=" + thunderDefenceA + ", thunderDefenceB=" + thunderDefenceB + ", thunderDefenceC=" + thunderDefenceC + ", thunderDefenceRate=" + thunderDefenceRate + ", thunderIgnoreDefence=" + thunderIgnoreDefence + ", thunderIgnoreDefenceA=" + thunderIgnoreDefenceA + ", thunderIgnoreDefenceB=" + thunderIgnoreDefenceB + ", thunderIgnoreDefenceC=" + thunderIgnoreDefenceC + ", thunderIgnoreDefenceRate=" + thunderIgnoreDefenceRate + ", exp=" + exp + ", state=" + state + ", aura=" + aura + ", direction=" + direction + ", hp=" + hp + ", hold=" + hold + ", stun=" + stun + ", immunity=" + immunity + ", poison=" + poison + ", weak=" + weak + ", invulnerable=" + invulnerable + ", silence=" + silence + ", shield=" + shield + ", lowerCureLevel=" + lowerCureLevel + ", avataType=" + Arrays.toString(avataType) + ", avata=" + Arrays.toString(avata) + ", avataAction=" + avataAction + ", inBattleField=" + inBattleField + ", battleFieldSide=" + battleFieldSide + ", skillUsingState=" + skillUsingState + ", nameColor=" + nameColor + ", objectScale=" + objectScale + ", objectColor=" + objectColor + ", objectOpacity=" + objectOpacity + ", particleName=" + particleName + ", particleX=" + particleX + ", particleY=" + particleY + ", footParticleName=" + footParticleName + ", footParticleX=" + footParticleX + ", footParticleY=" + footParticleY + ", ironMaidenPercent=" + ironMaidenPercent + ", lastLiveingSetContainsFlag=" + lastLiveingSetContainsFlag + ", random=" + random + ", id=" + id + ", version=" + version + ", x=" + x + ", y=" + y + ", avataRace=" + avataRace + ", avataSex=" + avataSex + ", lastHeartBeatX=" + lastHeartBeatX + ", lastHeartBeatY=" + lastHeartBeatY + ", viewWidth=" + viewWidth + ", viewHeight=" + viewHeight + ", path=" + path + ", heartBeatStartTime=" + heartBeatStartTime + ", interval=" + interval + ", filedChangeEventList=" + filedChangeEventList + ", getSpriteType()=" + getSpriteType() + ", getShowStrengthQuality()=" + getShowStrengthQuality() + ", getShowDexterityQuality()=" + getShowDexterityQuality() + ", getShowSpellpowerQuality()=" + getShowSpellpowerQuality() + ", getShowConstitutionQuality()=" + getShowConstitutionQuality() + ", getShowDingliQuality()=" + getShowDingliQuality() + ", getMaster()=" + getMaster() + ", getAuraSkillAgent()=" + getAuraSkillAgent() + ", getBuffs()=" + getBuffs() + ", getRemovedBuffs()=" + getRemovedBuffs() + ", getNewlyBuffs()=" + getNewlyBuffs() + ", getHookInfo()=" + getHookInfo() + ", getShowPhyAttack()=" + getShowPhyAttack() + ", getShowMagicAttack()=" + getShowMagicAttack() + ", isDeath()=" + isDeath() + ", getViewWidth()=" + getViewWidth() + ", getViewHeight()=" + getViewHeight() + ", getMp()=" + getMp() + ", getActivationType()=" + getActivationType() + ", getPursueWidth()=" + getPursueWidth() + ", getPursueHeight()=" + getPursueHeight() + ", getSkillList()=" + getSkillList() + ", getSkillAgent()=" + getSkillAgent() + ", getFightingAgent()=" + getFightingAgent() + ", getInnerState()=" + getInnerState() + ", getSkillIds()=" + Arrays.toString(getSkillIds()) + ", getActiveSkillLevels()=" + Arrays.toString(getActiveSkillLevels()) + ", getCareer()=" + getCareer() + ", getSex()=" + getSex() + ", getCharacter()=" + getCharacter() + ", getQuality()=" + getQuality() + ", getQualityScore()=" + getQualityScore() + ", getHappyness()=" + getHappyness() + ", getStarClass()=" + getStarClass() + ", getGeneration()=" + getGeneration() + ", getVariation()=" + getVariation() + ", getStrengthQuality()=" + getStrengthQuality() + ", getDexterityQuality()=" + getDexterityQuality() + ", getSpellpowerQuality()=" + getSpellpowerQuality() + ", getConstitutionQuality()=" + getConstitutionQuality() + ", getDingliQuality()=" + getDingliQuality() + ", getTrainLevel()=" + getTrainLevel() + ", getRarity()=" + getRarity() + ", getGrowthClass()=" + getGrowthClass() + ", getBreedTimes()=" + getBreedTimes() + ", getLifeTime()=" + getLifeTime() + ", getGrowth()=" + getGrowth() + ", getBreededTimes()=" + getBreededTimes() + ", getNextLevelExp()=" + getNextLevelExp() + ", getCategory()=" + getCategory() + ", isSummoned()=" + isSummoned() + ", getOwnerId()=" + getOwnerId() + ", getLastUpdateHappynessTime()=" + getLastUpdateHappynessTime() + ", getLastPackupTime()=" + getLastPackupTime() + ", getPetPropsId()=" + getPetPropsId() + ", getUnAllocatedPoints()=" + getUnAllocatedPoints() + ", getExpPercent()=" + getExpPercent() + ", getSize()=" + getSize() + ", getLogString()=" + getLogString() + ", getMaxLevle()=" + getMaxLevle() + ", getLastUpdateLifeTime()=" + getLastUpdateLifeTime() + ", getMaxHappyness()=" + getMaxHappyness() + ", getMaxLifeTime()=" + getMaxLifeTime() + ", isIdentity()=" + isIdentity() + ", getRealRandom()=" + Arrays.toString(getRealRandom()) + ", getBeforeReplaceRandom()=" + Arrays.toString(getBeforeReplaceRandom()) + ", isReplace()=" + isReplace() + ", getRepairNum()=" + getRepairNum() + ", getShowMaxStrengthQuality()=" + getShowMaxStrengthQuality() + ", getShowMaxDexterityQuality()=" + getShowMaxDexterityQuality() + ", getShowMaxSpellpowerQuality()=" + getShowMaxSpellpowerQuality() + ", getShowMaxConstitutionQuality()=" + getShowMaxConstitutionQuality() + ", getShowMaxDingliQuality()=" + getShowMaxDingliQuality() + ", getShowMinStrengthQuality()=" + getShowMinStrengthQuality() + ", getShowMinDexterityQuality()=" + getShowMinDexterityQuality() + ", getShowMinSpellpowerQuality()=" + getShowMinSpellpowerQuality() + ", getShowMinConstitutionQuality()=" + getShowMinConstitutionQuality() + ", getShowMinDingliQuality()=" + getShowMinDingliQuality() + ", getTempGrowthClass()=" + getTempGrowthClass() + ", getTempPoints()=" + Arrays.toString(getTempPoints()) + ", getPetSort()=" + getPetSort() + ", getCommonSkillId()=" + getCommonSkillId() + ", getOwnerName()=" + getOwnerName() + ", getAlreadyMaxStar()=" + getAlreadyMaxStar() + ", getEggType()=" + getEggType() + ", getRealTrainLevel()=" + getRealTrainLevel() + ", isDelete()=" + isDelete() + ", canFreeFromBeDamaged()=" + canFreeFromBeDamaged(null) + ", getCountry()=" + getCountry() + ", getTalent1Skill()=" + getTalent1Skill() + ", getTalent2Skill()=" + getTalent2Skill() + ", getWuXing()=" + getWuXing() + ", getTianFuSkIds()=" + Arrays.toString(getTianFuSkIds()) + ", getTianFuSkIvs()=" + Arrays.toString(getTianFuSkIvs()) + ", getCritFactor()=" + getCritFactor() + ", getGrade()=" + getGrade() + ", getAddBookTimes()=" + getAddBookTimes() + ", getHunExp()=" + getHunExp() + ", getHpStealPercent()=" + getHpStealPercent() + ", getPutOnEquipmentAvata()=" + getPutOnEquipmentAvata() + ", getClassLevel()=" + getClassLevel() + ", getSkillDamageIncreaseRate()=" + getSkillDamageIncreaseRate() + ", getSkillDamageDecreaseRate()=" + getSkillDamageDecreaseRate() + ", getReputationNameOnKillingSprite()=" + getReputationNameOnKillingSprite() + ", getReputationValueOnKillingSprite()=" + getReputationValueOnKillingSprite() + ", getHuDunDamage()=" + getHuDunDamage() + ", getIcon()=" + getIcon() + ", getBornPoint()=" + getBornPoint() + ", getClassType()=" + getClassType() + ", getGameName()=" + getGameName() + ", getGameCNName()=" + getGameCNName() + ", getFlushFrequency()=" + getFlushFrequency() + ", getDeadLastingTime()=" + getDeadLastingTime() + ", isIceState()=" + isIceState() + ", getRefreshSpriteData()=" + getRefreshSpriteData() + ", getTitle()=" + getTitle() + ", getName()=" + getName() + ", getDialogContent()=" + getDialogContent() + ", getLevel()=" + getLevel() + ", getHeight()=" + getHeight() + ", getMonsterType()=" + getMonsterType() + ", getNpcType()=" + getNpcType() + ", isTaskMark()=" + isTaskMark() + ", getCommonAttackSpeed()=" + getCommonAttackSpeed() + ", getCommonAttackRange()=" + getCommonAttackRange() + ", getMaxHP()=" + getMaxHP() + ", getMaxHPA()=" + getMaxHPA() + ", getMaxHPB()=" + getMaxHPB() + ", getMaxHPC()=" + getMaxHPC() + ", getMaxMP()=" + getMaxMP() + ", getMaxMPA()=" + getMaxMPA() + ", getMaxMPB()=" + getMaxMPB() + ", getMaxMPC()=" + getMaxMPC() + ", getHpRecoverBase()=" + getHpRecoverBase() + ", getHpRecoverBaseA()=" + getHpRecoverBaseA() + ", getHpRecoverBaseB()=" + getHpRecoverBaseB() + ", getSpeed()=" + getSpeed() + ", getSpeedA()=" + getSpeedA() + ", getSpeedC()=" + getSpeedC() + ", getWeaponDamage()=" + getWeaponDamage() + ", getPhyDefence()=" + getPhyDefence() + ", getPhyDefenceA()=" + getPhyDefenceA() + ", getPhyDefenceB()=" + getPhyDefenceB() + ", getPhyDefenceC()=" + getPhyDefenceC() + ", getPhyDefenceD()=" + getPhyDefenceD() + ", getPhysicalDecrease()=" + getPhysicalDecrease() + ", getMagicDefence()=" + getMagicDefence() + ", getMagicDefenceA()=" + getMagicDefenceA() + ", getMagicDefenceB()=" + getMagicDefenceB() + ", getMagicDefenceC()=" + getMagicDefenceC() + ", getMagicDefenceD()=" + getMagicDefenceD() + ", getSpellDecrease()=" + getSpellDecrease() + ", getBreakDefence()=" + getBreakDefence() + ", getBreakDefenceA()=" + getBreakDefenceA() + ", getBreakDefenceB()=" + getBreakDefenceB() + ", getBreakDefenceC()=" + getBreakDefenceC() + ", getBreakDefenceRate()=" + getBreakDefenceRate() + ", getHit()=" + getHit() + ", getHitA()=" + getHitA() + ", getHitB()=" + getHitB() + ", getHitC()=" + getHitC() + ", getHitD()=" + getHitD() + ", getHitRate()=" + getHitRate() + ", getDodge()=" + getDodge() + ", getDodgeA()=" + getDodgeA() + ", getDodgeB()=" + getDodgeB() + ", getDodgeC()=" + getDodgeC() + ", getDodgeD()=" + getDodgeD() + ", getDodgeRate()=" + getDodgeRate() + ", getAccurate()=" + getAccurate() + ", getAccurateA()=" + getAccurateA() + ", getAccurateB()=" + getAccurateB() + ", getAccurateC()=" + getAccurateC() + ", getAccurateRate()=" + getAccurateRate() + ", getStrength()=" + getStrength() + ", getStrengthA()=" + getStrengthA() + ", getStrengthB()=" + getStrengthB() + ", getStrengthC()=" + getStrengthC() + ", getStrengthS()=" + getStrengthS() + ", getSpellpower()=" + getSpellpower() + ", getSpellpowerA()=" + getSpellpowerA() + ", getSpellpowerB()=" + getSpellpowerB() + ", getSpellpowerC()=" + getSpellpowerC() + ", getSpellpowerS()=" + getSpellpowerS() + ", getDexterity()=" + getDexterity() + ", getDexterityA()=" + getDexterityA() + ", getDexterityB()=" + getDexterityB() + ", getDexterityC()=" + getDexterityC() + ", getDexterityS()=" + getDexterityS() + ", getConstitution()=" + getConstitution() + ", getConstitutionA()=" + getConstitutionA() + ", getConstitutionB()=" + getConstitutionB() + ", getConstitutionC()=" + getConstitutionC() + ", getConstitutionS()=" + getConstitutionS() + ", getDingli()=" + getDingli() + ", getDingliA()=" + getDingliA() + ", getDingliB()=" + getDingliB() + ", getDingliC()=" + getDingliC() + ", getDingliS()=" + getDingliS() + ", getCriticalHit()=" + getCriticalHit() + ", getCriticalHitA()=" + getCriticalHitA() + ", getCriticalHitB()=" + getCriticalHitB() + ", getCriticalHitC()=" + getCriticalHitC() + ", getCriticalHitRate()=" + getCriticalHitRate() + ", getCriticalDefence()=" + getCriticalDefence() + ", getCriticalDefenceA()=" + getCriticalDefenceA() + ", getCriticalDefenceB()=" + getCriticalDefenceB() + ", getCriticalDefenceC()=" + getCriticalDefenceC() + ", getCriticalDefenceRate()=" + getCriticalDefenceRate() + ", getPhyAttack()=" + getPhyAttack() + ", getPhyAttackA()=" + getPhyAttackA() + ", getPhyAttackB()=" + getPhyAttackB() + ", getPhyAttackC()=" + getPhyAttackC() + ", getMagicAttack()=" + getMagicAttack() + ", getMagicAttackA()=" + getMagicAttackA() + ", getMagicAttackB()=" + getMagicAttackB() + ", getMagicAttackC()=" + getMagicAttackC() + ", getPhyDefenceRateOther()=" + getPhyDefenceRateOther() + ", getMagicDefenceRateOther()=" + getMagicDefenceRateOther() + ", getCriticalHitRateOther()=" + getCriticalHitRateOther() + ", getCriticalDefenceRateOther()=" + getCriticalDefenceRateOther() + ", getHitRateOther()=" + getHitRateOther() + ", getDodgeRateOther()=" + getDodgeRateOther() + ", getAccurateRateOther()=" + getAccurateRateOther() + ", getBreakDefenceRateOther()=" + getBreakDefenceRateOther() + ", getFireDefenceRateOther()=" + getFireDefenceRateOther() + ", getFireIgnoreDefenceRateOther()=" + getFireIgnoreDefenceRateOther() + ", getBlizzardDefenceRateOther()=" + getBlizzardDefenceRateOther() + ", getBlizzardIgnoreDefenceRateOther()=" + getBlizzardIgnoreDefenceRateOther() + ", getWindDefenceRateOther()=" + getWindDefenceRateOther() + ", getWindIgnoreDefenceRateOther()=" + getWindIgnoreDefenceRateOther() + ", getThunderDefenceRateOther()=" + getThunderDefenceRateOther() + ", getThunderIgnoreDefenceRateOther()=" + getThunderIgnoreDefenceRateOther() + ", getFireAttack()=" + getFireAttack() + ", getFireAttackA()=" + getFireAttackA() + ", getFireAttackB()=" + getFireAttackB() + ", getFireAttackC()=" + getFireAttackC() + ", getFireDefence()=" + getFireDefence() + ", getFireDefenceA()=" + getFireDefenceA() + ", getFireDefenceB()=" + getFireDefenceB() + ", getFireDefenceC()=" + getFireDefenceC() + ", getFireDefenceRate()=" + getFireDefenceRate() + ", getFireIgnoreDefence()=" + getFireIgnoreDefence() + ", getFireIgnoreDefenceA()=" + getFireIgnoreDefenceA() + ", getFireIgnoreDefenceB()=" + getFireIgnoreDefenceB() + ", getFireIgnoreDefenceC()=" + getFireIgnoreDefenceC() + ", getFireIgnoreDefenceRate()=" + getFireIgnoreDefenceRate() + ", getBlizzardAttack()=" + getBlizzardAttack() + ", getBlizzardAttackA()=" + getBlizzardAttackA() + ", getBlizzardAttackB()=" + getBlizzardAttackB() + ", getBlizzardAttackC()=" + getBlizzardAttackC() + ", getBlizzardDefence()=" + getBlizzardDefence() + ", getBlizzardDefenceA()=" + getBlizzardDefenceA() + ", getBlizzardDefenceB()=" + getBlizzardDefenceB() + ", getBlizzardDefenceC()=" + getBlizzardDefenceC() + ", getBlizzardDefenceRate()=" + getBlizzardDefenceRate() + ", getBlizzardIgnoreDefence()=" + getBlizzardIgnoreDefence() + ", getBlizzardIgnoreDefenceA()=" + getBlizzardIgnoreDefenceA() + ", getBlizzardIgnoreDefenceB()=" + getBlizzardIgnoreDefenceB() + ", getBlizzardIgnoreDefenceC()=" + getBlizzardIgnoreDefenceC() + ", getBlizzardIgnoreDefenceRate()=" + getBlizzardIgnoreDefenceRate() + ", getWindAttack()=" + getWindAttack() + ", getWindAttackA()=" + getWindAttackA() + ", getWindAttackB()=" + getWindAttackB() + ", getWindAttackC()=" + getWindAttackC() + ", getWindDefence()=" + getWindDefence() + ", getWindDefenceA()=" + getWindDefenceA() + ", getWindDefenceB()=" + getWindDefenceB() + ", getWindDefenceC()=" + getWindDefenceC() + ", getWindDefenceRate()=" + getWindDefenceRate() + ", getWindIgnoreDefence()=" + getWindIgnoreDefence() + ", getWindIgnoreDefenceA()=" + getWindIgnoreDefenceA() + ", getWindIgnoreDefenceB()=" + getWindIgnoreDefenceB() + ", getWindIgnoreDefenceC()=" + getWindIgnoreDefenceC() + ", getWindIgnoreDefenceRate()=" + getWindIgnoreDefenceRate() + ", getThunderAttack()=" + getThunderAttack() + ", getThunderAttackA()=" + getThunderAttackA() + ", getThunderAttackB()=" + getThunderAttackB() + ", getThunderAttackC()=" + getThunderAttackC() + ", getThunderDefence()=" + getThunderDefence() + ", getThunderDefenceA()=" + getThunderDefenceA() + ", getThunderDefenceB()=" + getThunderDefenceB() + ", getThunderDefenceC()=" + getThunderDefenceC() + ", getThunderDefenceRate()=" + getThunderDefenceRate() + ", getThunderIgnoreDefence()=" + getThunderIgnoreDefence() + ", getThunderIgnoreDefenceA()=" + getThunderIgnoreDefenceA() + ", getThunderIgnoreDefenceB()=" + getThunderIgnoreDefenceB() + ", getThunderIgnoreDefenceC()=" + getThunderIgnoreDefenceC() + ", getThunderIgnoreDefenceRate()=" + getThunderIgnoreDefenceRate() + ", getExp()=" + getExp() + ", getState()=" + getState() + ", getAura()=" + getAura() + ", getDirection()=" + getDirection() + ", getHp()=" + getHp() + ", isHold()=" + isHold() + ", isStun()=" + isStun() + ", isImmunity()=" + isImmunity() + ", isPoison()=" + isPoison() + ", isWeak()=" + isWeak() + ", isInvulnerable()=" + isInvulnerable() + ", isSilence()=" + isSilence() + ", getShield()=" + getShield() + ", getLowerCureLevel()=" + getLowerCureLevel() + ", getAvataType()=" + Arrays.toString(getAvataType()) + ", getAvata()=" + Arrays.toString(getAvata()) + ", getAvataAction()=" + getAvataAction() + ", isInBattleField()=" + isInBattleField() + ", getBattleFieldSide()=" + getBattleFieldSide() + ", isSkillUsingState()=" + isSkillUsingState() + ", getNameColor()=" + getNameColor() + ", isObjectOpacity()=" + isObjectOpacity() + ", getParticleName()=" + getParticleName() + ", getParticleX()=" + getParticleX() + ", getParticleY()=" + getParticleY() + ", getFootParticleName()=" + ", getId()=" + getId() + ", getAvataRace()=" + getAvataRace() + ", getAvataSex()=" + getAvataSex() + ", getInterval()=" + getInterval() + "]";
	}

	public Player getTempMaster() {
		return tempMaster;
	}

	public void setTempMaster(Player tempMaster) {
		this.tempMaster = tempMaster;
	}

	/**
	 * 宠物进入战场
	 */
	public void enterBattleField() {
		this.setInBattleField(true);
	}

	/**
	 * 宠物离开战场
	 */
	public void leaveBattleField() {
		this.setBattleFieldSide((byte) 0);
		this.setInBattleField(false);
	}

	public String getPetPropsName() {
		return this.petPropsName;
	}

	public void setPetPropsName(String petPropsName) {
		this.petPropsName = petPropsName;
	}

	public int getAntiInjuryRate() {
		int result = antiInjuryRate;
		int extraRate = 0;
		try {
			if (hpDecreaseRate <= 0 || addAntiRate <= 0) {
				return result;
			}
			 int rate = (int) (100 - (float)this.getHp() / (float)this.getMaxHP() * 100f);
			 if (rate >= hpDecreaseRate) {
				 extraRate = rate / hpDecreaseRate * addAntiRate;
				 if (maxAddAntiRate > 0 && extraRate > maxAddAntiRate) {
					 extraRate = maxAddAntiRate;
				 }
				 result += extraRate;
				 if (PetManager.logger.isDebugEnabled()) {
					 PetManager.logger.debug("[增加宠物反伤比例] [成功] [原比例:" + antiInjuryRate + "] [增加后:" + extraRate + "] [当前血:" + this.getHp() + "] [最大血:" + this.getMaxHP() + "]");
				 }
				 return result;
			 }
		} catch (Exception e) {
			PetManager.logger.warn("[增加宠物反伤比例] [异常] [" + this.getId() + "]", e);
		}
		return result;
	}

	public void setAntiInjuryRate(int antiInjuryRate) {
		this.antiInjuryRate = antiInjuryRate;
	}

	public int getExtraHpC() {
		return extraHpC;
	}

	public void setExtraHpC(int extraHpC) {
		this.extraHpC = extraHpC;
		int tempHp = (int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC + this.extraHpC) / 100);
		if (this.getMaxHP() != tempHp) {
			this.setMaxHP(tempHp);
		}
	}

	@Override
	public void setMaxHPC(int value) {
		// TODO Auto-generated method stub
		this.maxHPC = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC + this.extraHpC) / 100));
	}

	@Override
	public void setMaxHPB(int value) {
		// TODO Auto-generated method stub
		this.maxHPB = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC + this.extraHpC) / 100));
	}

	@Override
	public void setMaxHPA(int value) {
		// TODO Auto-generated method stub
		this.maxHPA = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC + this.extraHpC) / 100));
	}

	public NewAuraSkillAgent getNagent() {
		return nagent;
	}

	public void setNagent(NewAuraSkillAgent nagent) {
		this.nagent = nagent;
	}
	
	@Override
	public Object getValue(int id) {
		switch (id) {
		case 38:
			return ownerName;
		case 39:
			return flyNBState;
		default:
			return super.getValue(id);
		}
	}

	public int getHpRecoverExtend() {
		return hpRecoverExtend;
	}

	public void setHpRecoverExtend(int hpRecoverExtend) {
		this.hpRecoverExtend = hpRecoverExtend;
	}

	public long[] getOrnaments() {
		return ornaments;
	}

	public void setOrnaments(long[] ornaments) {
		this.ornaments = ornaments;
		PetManager.em.notifyFieldChange(this, "ornaments");
	}

	public int getHpRecoverExtend2() {
		return hpRecoverExtend2;
	}

	public void setHpRecoverExtend2(int hpRecoverExtend2) {
		this.hpRecoverExtend2 = hpRecoverExtend2;
	}

	public long getLasthpRecoverExtend2Time() {
		return lasthpRecoverExtend2Time;
	}

	public void setLasthpRecoverExtend2Time(long lasthpRecoverExtend2Time) {
		this.lasthpRecoverExtend2Time = lasthpRecoverExtend2Time;
	}

	public int[] getHpPercent4Hit() {
		return hpPercent4Hit;
	}

	public void setHpPercent4Hit(int[] hpPercent4Hit) {
		this.hpPercent4Hit = hpPercent4Hit;
	}

	public int[] getHitIncreaseRate() {
		return hitIncreaseRate;
	}

	public void setHitIncreaseRate(int[] hitIncreaseRate) {
		this.hitIncreaseRate = hitIncreaseRate;
	}

	public int getDecreaseAbnormalStateTimeRate() {
		return decreaseAbnormalStateTimeRate;
	}

	public void setDecreaseAbnormalStateTimeRate(int decreaseAbnormalStateTimeRate) {
		this.decreaseAbnormalStateTimeRate = decreaseAbnormalStateTimeRate;
	}

	public int getDecreseSpecialDamage() {
		return decreseSpecialDamage;
	}

	public void setDecreseSpecialDamage(int decreseSpecialDamage) {
		this.decreseSpecialDamage = decreseSpecialDamage;
	}

	public int getSignProb() {
		return signProb;
	}

	public void setSignProb(int signProb) {
		this.signProb = signProb;
	}

	public int getDamageHpRate() {
		return damageHpRate;
	}

	public void setDamageHpRate(int damageHpRate) {
		this.damageHpRate = damageHpRate;
	}

	public int getHpDecreaseRate() {
		return hpDecreaseRate;
	}

	public void setHpDecreaseRate(int hpDecreaseRate) {
		this.hpDecreaseRate = hpDecreaseRate;
	}

	public int getAddAntiRate() {
		return addAntiRate;
	}

	public void setAddAntiRate(int addAntiRate) {
		this.addAntiRate = addAntiRate;
	}

	public int getMaxAddAntiRate() {
		return maxAddAntiRate;
	}

	public void setMaxAddAntiRate(int maxAddAntiRate) {
		this.maxAddAntiRate = maxAddAntiRate;
	}

	public byte getImmuType() {
		return immuType;
	}

	public void setImmuType(byte immuType) {
		this.immuType = immuType;
	}

	public int[] getHpPercent4Hit2() {
		return hpPercent4Hit2;
	}

	public void setHpPercent4Hit2(int[] hpPercent4Hit2) {
		this.hpPercent4Hit2 = hpPercent4Hit2;
	}

	public int[] getHitIncreaseRate2() {
		return hitIncreaseRate2;
	}

	public void setHitIncreaseRate2(int[] hitIncreaseRate2) {
		this.hitIncreaseRate2 = hitIncreaseRate2;
	}

	public long getLastDeadlyAttackTime() {
		return lastDeadlyAttackTime;
	}

	public void setLastDeadlyAttackTime(long lastDeadlyAttackTime) {
		this.lastDeadlyAttackTime = lastDeadlyAttackTime;
	}
	
}
