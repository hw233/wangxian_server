package com.fy.engineserver.sprite.monster;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.RefreshSpriteManager.RefreshSpriteData;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.globallimit.manager.GlobalLimitManager;
import com.fy.engineserver.activity.monsterDrop.MonsterDropActivityManager;
import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.calculate.NumericalCalculator;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.FlopSchemeEntity;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuFaGong;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWuGong;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.downcity.downcity3.BossCityManager;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.flop.FlopManager;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.newtask.ActivityTaskExp;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.SpriteSubSystem;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.TeamSubSystem;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.sprite.npc.WingCarbonNPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ProbabilityUtils;
import com.xuanzhi.stat.model.PlayerActionFlow;
import com.xuanzhi.tools.text.StringUtil;

/**
 * Sprite，为怪，NPC的超类
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class Monster extends Sprite implements Cloneable {

	private static final long serialVersionUID = -1563275511842290358L;

	// public static Logger attackRecordLogger = Logger.getLogger("monster.attackrecord");
	public static Logger attackRecordLogger = LoggerFactory.getLogger("monster.attackrecord");

	// public static Logger monsterFlopLogger = Logger.getLogger("monster.flop");
	public static Logger monsterFlopLogger = LoggerFactory.getLogger("monster.flop");
	/** 怪物所处位置，方便副本等地方对怪物有特殊操作  1为翅膀副本 2为空岛大冒险*/
	private transient byte monsterLocat = 0;

	/**
	 * 怪物颜色，颜色不同怪物能力不同，白绿蓝紫橙
	 */
	protected int color;

	/**
	 * 怪物能力调整
	 */
	protected double monsterMark;

	/**
	 * 怪物能力调整
	 */
	protected double hpMark;

	/**
	 * 怪物能力调整
	 */
	protected double apMark;

	/**
	 * 是否能够免疫
	 */
	protected boolean isImmunity;
	/** 召唤boss人的id（越狱boss时用） */
	public transient long callerId;

	/**
	 * 免疫类型，只有为免疫状态时才有用
	 */
	protected byte[] immunityType;

	public boolean isImmunity() {
		return isImmunity;
	}

	public void setImmunity(boolean isImmunity) {
		this.isImmunity = isImmunity;
	}

	public byte[] getImmunityType() {
		return immunityType;
	}

	public void setImmunityType(byte[] immunityType) {
		this.immunityType = immunityType;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		this.objectColor = color;
	}

	public double getMonsterMark() {
		return monsterMark;
	}

	public void setMonsterMark(double monsterMark) {
		this.monsterMark = monsterMark;
	}

	public double getHpMark() {
		return hpMark;
	}

	public void setHpMark(double hpMark) {
		this.hpMark = hpMark;
	}

	public double getApMark() {
		return apMark;
	}

	public void setApMark(double apMark) {
		this.apMark = apMark;
	}

	public byte getSpriteType() {
		return Sprite.SPRITE_TYPE_MONSTER;
	}

	// 伤害记录，记录掉血的记录，以及仇恨记录
	public static class DamageRecord {
		public Fighter f;
		public int damage = 0;
		public int hatred = 0;
		public long time;

		public DamageRecord() {

		}

		@Override
		protected void finalize() throws Throwable {

		}

		public DamageRecord(Fighter f, int d, int h) {
			this();
			this.f = f;
			this.damage = d;
			this.hatred = h;
			time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
	}

	public static class AttackRecord {
		public long createTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		public LivingObject living;
		public int meleeAttackIntensity;
		public int spellAttackIntensity;
		public String skillName;
		public int skillLevel;
		public int damage;
		public int hp;
		public int damageType;

		public AttackRecord() {

		}

		@Override
		protected void finalize() throws Throwable {

		}

		public AttackRecord(LivingObject p, String skillName, int skillLevel, int damageType, int damage, int hp) {
			this();
			living = p;
			this.skillName = skillName;
			this.damage = damage;
			this.hp = hp;
			this.skillLevel = skillLevel;
			if (p instanceof Player) {
				this.meleeAttackIntensity = ((Player) p).getPhyAttack();
				this.spellAttackIntensity = ((Player) p).getMagicAttack();
			} else if (p instanceof Pet) {
				this.meleeAttackIntensity = ((Pet) p).getPhyAttack();
				this.spellAttackIntensity = ((Pet) p).getMagicAttack();
			}

			this.damageType = damageType;
		}
	}

	public ArrayList<AttackRecord> attackRecordList = new ArrayList<AttackRecord>(0);

	// 记录开始打怪的时间
	private long fightingStartTime;

	/**
	 * 怪的种类ID，此ID在编辑的时候必须唯一
	 */
	protected int spriteCategoryId;

	// 怪物种族
	protected byte monsterRace;

	/**
	 * 运动轨迹
	 * 0 原地不动
	 * 1 按随机巡逻
	 * 2 按给定的一条路径巡逻
	 */
	protected int traceType = 0;

	/**
	 * 巡逻的范围宽度
	 */
	protected int patrolingWidth = 320;

	/**
	 * 巡逻的范围高度
	 */
	protected int patrolingHeight = 320;

	/**
	 * 巡逻的时间间隔
	 */
	protected long patrolingTimeInterval = 15000;

	/**
	 * 激活条件，所谓激活就是攻击玩家
	 * 
	 * 0 表示被攻击，准备攻击攻击他的玩家
	 * 1 表示进入怪的视野范围，怪看到玩家后，就开始主动攻击玩家，同时如果怪被攻击，优先攻击攻击他的玩家
	 * 2 满足0和1，同时视野范围内的同类怪被攻击，就开始主动攻击玩家
	 * 
	 */
	protected int activationType = 0;

	// 激活的范围宽度，以怪为中心
	protected int activationWidth = 320;
	// 激活的范围高度，以怪为中心
	protected int activationHeight = 320;

	// 追击的范围宽度，以怪为中心
	protected int pursueWidth = 640;
	// 追击的范围高度，以怪为中心
	protected int pursueHeight = 640;

	/**
	 * 回出生点的速度是移动速度的百分之多少。150表示1.5倍
	 */
	protected int backBornPointMoveSpeedPercent = 150;

	/**
	 * 回程路上的补血量，每0.5秒钟的补血
	 */
	protected int backBornPointHp = 100;

	/**
	 * 怪装备的技能ID列表，如果对应的技能为主动技能，那么就是怪攻击时
	 * 使用的技能。
	 * 
	 * 如果对应的光环类技能，那么就是怪拥有的光环类技能。
	 */
	private int activeSkillIds[];

	/**
	 * 怪装备的技能ID列表，对应的各个技能的级别
	 */
	protected int activeSkillLevels[];

	/**
	 * 有物品掉落的情况下，死亡持续的时间
	 */
	protected long deadLastingTimeForFloop = 5 * 60;

	/**
	 * 掉落方式，0为按照几率，独立计算掉落。1为从集合中选出一个集合
	 */
	public byte flopFormat;

	public byte getFlopFormat() {
		return flopFormat;
	}

	public void setFlopFormat(byte flopFormat) {
		this.flopFormat = flopFormat;
	}

	/**
	 * 怪物的掉落方案
	 * 当怪物被打死后，会根据此掉落方案
	 * 掉落一个掉落方案的实体
	 */
	protected FlopSet[] fsList;

	/**
	 * 掉落集合的掉落几率
	 * 当掉落方式为0既为独立掉落时，基数为10000
	 * 当掉落方式为1既为从集合列表中选择一个，基数为所有数值的和
	 */
	protected Integer[] fsProbabilitis;

	public FlopSet[] getFsList() {
		return fsList;
	}

	public void setFsList(FlopSet[] fsList) {
		this.fsList = fsList;
	}

	public Integer[] getFsProbabilitis() {
		return fsProbabilitis;
	}

	public void setFsProbabilitis(Integer[] fsProbabilitis) {
		this.fsProbabilitis = fsProbabilitis;
	}

	/**
	 * 得到怪物身上所有的掉落集合
	 * @return
	 */
	public FlopSet[] getFlopSets() {
		return fsList;
	}

	/**
	 * 得到掉落集合的掉率
	 * @return
	 */
	public Integer[] getFlopSetProbabilitis() {
		return fsProbabilitis;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	// 以下是怪的内部数据结构
	// /////////////////////////////////////////////////////////////////////////////////

	// 怪被打死的时间
	protected transient long deadEndTime = 0;
	protected transient boolean flopEntityNotEmtpy = true;
	protected transient long lastTimeForPatroling;

	protected transient long lastTimeForRecoverHP;

	protected transient long lastTimeForBuffs;

	/**
	 * 人物身体上的Buff，这个数组的下标对应buffType 故，同一个buffType的buff在人物身上只能有一个
	 * 
	 * 此数据是要存盘的
	 * 
	 */
	protected ArrayList<Buff> buffs = new ArrayList<Buff>(0);

	/**
	 * 下一次心跳要通知客户端去除的buff
	 */
	protected transient ArrayList<Buff> removedBuffs = new ArrayList<Buff>(0);

	/**
	 * 下一次心跳要去通知客户端新增加的buff
	 */
	protected transient ArrayList<Buff> newlyBuffs = new ArrayList<Buff>(0);

	/**
	 * 这个怪物拥有的技能
	 */
	protected transient ArrayList<ActiveSkill> skillList = new ArrayList<ActiveSkill>(0);

	// 技能代理，怪使用技能的代理
	protected transient ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);

	// 战斗代理
	protected transient MonsterFightingAgent fightingAgent = new MonsterFightingAgent(this);

	protected transient AuraSkillAgent auraSkillAgent = new AuraSkillAgent(this);

	protected transient AuraSkill auraSkill = null;
	/**
	 * 记录各个攻击者对我的伤害
	 */
	protected transient ArrayList<DamageRecord> hatridList = new ArrayList<DamageRecord>(0);

	/**
	 * 怪的内部状态，注意此状态, 0表示空闲，1表示激活，2表示回程
	 */
	protected transient int innerState = 0;

	/**
	 * 怪的拥有者，所有掉落的物品都是怪的拥有者的
	 */
	protected transient Player owner;
	/**
	 * 怪物的最后一击
	 */
	protected transient Player lastAttacker;

	/**
	 * 掉落方案的实例
	 * 
	 * 此实例将记录有哪些物品，并记录拾取的记录
	 */
	protected transient FlopSchemeEntity flopSchemeEntity;

	public FlopSchemeEntity getFlopSchemeEntity() {
		return flopSchemeEntity;
	}

	public AuraSkillAgent getAuraSkillAgent() {
		return auraSkillAgent;
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

	public void setAdditionData(Element e) {

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

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init() {
		super.init();
		CareerManager cm = CareerManager.getInstance();

		if (activeSkillIds == null || activeSkillIds.length == 0) {
			//
			Skill[] skills = cm.getMonsterSkills();
			for (int i = skills.length - 1; i >= 0; i--) {
				if (skills[i] instanceof ActiveSkill) {
					activeSkillIds = new int[] { skills[i].getId() };
					break;
				}
			}
		}

		for (int i = 0; i < activeSkillIds.length; i++) {
			Skill skill = cm.getSkillById(activeSkillIds[i]);
			// Game.logger.debug("[trace_monster_init] [" + activeSkillIds[i] + "] [" + (skill == null ? "NULL" : (skill.getClass().getName() + "==" + (skill instanceof
			// ActiveSkill))) + "]");
			if (MonsterManager.logger.isDebugEnabled()) {
				MonsterManager.logger.debug("[trace_monster_init] [{}] [{}]", new Object[] { activeSkillIds[i], (skill == null ? "NULL" : (skill.getClass().getName() + "==" + (skill instanceof ActiveSkill))) });
			}
			if (skill != null && skill instanceof ActiveSkill) {
				skillList.add((ActiveSkill) skill);
			} else if (skill != null && skill instanceof PassiveSkill) {

			} else if (skill != null && skill instanceof AuraSkill) {
				auraSkill = (AuraSkill) skill;
			}
		}
		if (auraSkill != null) {
			this.auraSkillAgent.openAuraSkill(auraSkill);
		}
		this.setObjectColor(color);
		// Game.logger.debug("[初始化怪] [activeSkillIds:" + activeSkillIds.length + "] [skillList:" + skillList.size() + "]");
		if (MonsterManager.logger.isDebugEnabled()) MonsterManager.logger.debug("[初始化怪] [activeSkillIds:{}] [skillList:{}]", new Object[] { activeSkillIds.length, skillList.size() });

	}

	// 怪物从内存中消失的时候，调用此方法
	public void notifyRemoved() {

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
		if (fighter == this) return FIGHTING_TYPE_FRIEND;
		if (fighter instanceof WingCarbonNPC) {
			return FIGHTING_TYPE_ENEMY;
		}
		if (fighter instanceof Monster) {
			return FIGHTING_TYPE_FRIEND;
		} else if (fighter instanceof Player || fighter instanceof Pet) {
			return FIGHTING_TYPE_ENEMY;
		} else if (fighter instanceof Sprite) {
			// Sprite s = (Sprite) fighter;
			// if (s.getCountry() == GameConstant.中立阵营) {
			return Fighter.FIGHTING_TYPE_NEUTRAL;
			// }
		}
		return FIGHTING_TYPE_ENEMY;
	}

	/**
	 * 通知怪，某个玩家b给玩家a加血
	 * 对应的，要增加b的仇恨值
	 * @param a
	 * @param b
	 * @param hp
	 */
	public void notifyHPAdded(Fighter a, Fighter b, int hp) {
		if (this.hatridList.size() > 0) {
			updateDamageRecord(b, 0, hp / 2);
			b.notifyPrepareToBeFighted(this);
		}
	}

	/**
	 * 某个敌人，选择某个技能
	 * @param target
	 * @return
	 */
	protected ActiveSkill getActiveSkill(Fighter target) {
		if (skillList.size() > 0) return skillList.get(0);
		return null;
	}

	/**
	 * 得到最大的仇恨值敌人
	 * @return
	 */
	protected Fighter getMaxHatredFighter() {
		int maxHatred = -1;
		DamageRecord d = null;
		for (int i = 0; i < hatridList.size(); i++) {
			DamageRecord dr = hatridList.get(i);
			if (dr.hatred > maxHatred) {
				maxHatred = dr.hatred;
				d = dr;
			}
		}
		if (d != null) {
			return d.f;
		} else {
			return null;
		}
	}

	/**
	 * 更新仇恨列表
	 * @param caster
	 * @param damage
	 * @param hatred
	 */
	public void updateDamageRecord(Fighter caster, int damage, int hatred) {

		if (caster.isDeath()) return;

		if (caster instanceof Player) {
			hatred = hatred * (((Player) caster).getHatridRate() + 100) / 100;
		}

		DamageRecord record = null;
		for (int i = 0; i < hatridList.size(); i++) {
			DamageRecord dr = hatridList.get(i);
			if (dr.f == caster) {
				record = dr;
			}
		}
		if (record == null) {
			if (this.monsterLocat != 1) {			//2014年9月1日10:36:01  翅膀副本怪物被攻击不会记录仇恨  
				record = new DamageRecord(caster, damage, hatred);
				hatridList.add(record);
			}
		} else {
			record.damage += damage;
			record.hatred += hatred;
			record.time = heartBeatStartTime;
		}
	}

	public void notifyAttack(Player player, String skillName, int skillLevel, int damageType, int damage) {
		if (attackRecordLogger.isDebugEnabled()) {
			AttackRecord ar = new AttackRecord(player, skillName, skillLevel, damageType, damage, this.getHp());
			this.attackRecordList.add(ar);
		}
	}

	public void notifyAttack(Pet pet, String skillName, int skillLevel, int damageType, int damage) {
		if (attackRecordLogger.isDebugEnabled()) {
			AttackRecord ar = new AttackRecord(pet, skillName, skillLevel, damageType, damage, this.getHp());
			this.attackRecordList.add(ar);
		}
	}

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

		int ddd = damage;
		if (ddd > hp) {
			ddd = hp;
		}

		if (fightingStartTime == 0) {
			fightingStartTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}

		BossCityManager.getInstance().updateBattleDamage(caster, damage);
		JiazuManager2.instance.updateBattleDamage(caster, damage);
		
		if ((caster instanceof Player) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Player p = (Player) caster;

			if (p.getState() != Player.STATE_DEAD && p.getMpStealPercent() > 0) {
				if (p.getMp() < p.getMaxMP()) {
					int mp = p.getMp() + p.getMpStealPercent() * ddd / 100;
					if (mp > p.getMaxMP()) mp = p.getMaxMP();
					p.setMp(mp);

					// 加蓝，是否要通知客户端？

					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.MP_INCREASED, p.getMpStealPercent() * ddd / 100);
					p.addMessageToRightBag(req);

				} else {
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.MP_INCREASED, p.getMpStealPercent() * ddd / 100);
					p.addMessageToRightBag(req);
				}
			}

			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					if(p.isCanNotIncHp()){
						if(Skill.logger.isDebugEnabled())Skill.logger.debug("[无法回血状态] [屏蔽吸收回血] [" + p.getLogString() + "] [血]");
					}else{
						p.setHp(mp);
						
						// 加蓝，是否要通知客户端？
						
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
						p.addMessageToRightBag(req);
					}
				} else {
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
					p.addMessageToRightBag(req);
				}
			}
		}
		Skill.logger.debug("[测试宠物] [" + caster.getClass().getName() + "] [damageType:" + damageType + "] [damage:" + damage + "] [" + this.getName() + "]");
		if ((caster instanceof Pet) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Pet p = (Pet)caster;
			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					p.setHp(mp);
					Skill.logger.debug("Monster.causeDamage: 宠物吸血 {}",p.getName());
					// 加蓝，是否要通知客户端？

//					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
//					p.addMessageToRightBag(req);
				} else {
//					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
//					p.addMessageToRightBag(req);
				}
			}
		}
		if (damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_XISHOU) {

			try {
				if (damage < 0) {			//受到伤害后回血，异常---加日志查问题
					try {
						throw new Exception("[怪物受到伤害为负] [" + damage + "]");
					} catch (Exception e) {
						EnchantManager.logger.warn("[怪物受到伤害异常] [" + damage + "] [" + (caster==null?"攻击者为空":caster.getName()) + "] [" + (caster==null?"-1":caster.getId()) + "]", e);
					}
				}
			} catch (Exception e) {
				
			}
			if (hp > damage) {
				setHp(hp - damage);
				updateDamageRecord(caster, damage, damage * hateParam);
				if (caster instanceof Pet) {
					if (((Pet) caster).getMaster() != null) {
						updateDamageRecord(((Pet) caster).getMaster(), 1, 1);
					}
				}
			} else if (hp > 0) {
				setHp(0);
				updateDamageRecord(caster, hp, hp * hateParam);
				if (caster instanceof Pet) {
					if (((Pet) caster).getMaster() != null) {
						updateDamageRecord(((Pet) caster).getMaster(), 1, 1);
						lastAttacker = ((Pet) caster).getMaster();
					}
				} else if (caster instanceof Player) {
					lastAttacker = (Player) caster;
				}
			} else {
				updateDamageRecord(caster, 1, 1 * hateParam);
				if (caster instanceof Pet) {
					if (((Pet) caster).getMaster() != null) {
						updateDamageRecord(((Pet) caster).getMaster(), 1, 1);
					}
				}
			}
		} else {
			updateDamageRecord(caster, 1, 1 * hateParam);
			updateDamageRecord(caster, damage, damage * hateParam);
			if (caster instanceof Pet) {
				if (((Pet) caster).getMaster() != null) {
					updateDamageRecord(((Pet) caster).getMaster(), 1, 1);
				}
			}
		}

		if (owner == null && (caster instanceof Player)) {
			owner = (Player) caster;
		} else if (owner == null && (caster instanceof Pet)) {
			owner = ((Pet) caster).getMaster();
		}
	}
	
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
	}

	public long 得到怪物经验(Player player) {
		long exp = this.getExp();
		if (MemoryMonsterManager.levelExpMap != null && MemoryMonsterManager.levelExpMap.get(level) != null) {
			exp += MemoryMonsterManager.levelExpMap.get(level);
		}
		if (this.getMonsterType() == MONSTER_EXP) {
			exp = (long) (exp * 1.5);
		}
		if (this.getMonsterType() < 0) { // 根据玩家等级给予经验,此类型与任务表中的经验ID匹配
			ActivityTaskExp activityTaskExp = TaskManager.getInstance().activityPrizeMap.get(Integer.valueOf(this.getMonsterType()));
			if (activityTaskExp == null) {
				TaskSubSystem.logger.error(player.getLogString() + "[杀死怪物] [" + this.getName() + "] [经验奖励异常] [经验配置不存在] [getMonsterType:" + this.getMonsterType() + "]");
				return 0;
			}
			if (player.getLevel() > activityTaskExp.getExpPrize().length) {
				TaskSubSystem.logger.error(player.getLogString() + "[杀死怪物] [" + this.getName() + "] [经验奖励异常] [playerLevel:" + player.getLevel() + "] [配置的等级上限:" + activityTaskExp.getExpPrize().length + "]");
				return 0;
			}
			exp = activityTaskExp.getExpPrize()[player.getLevel() - 1];
			TaskSubSystem.logger.error(player.getLogString() + "[杀死怪物] [" + this.getName() + "] [经验奖励] [getMonsterType:" + this.getMonsterType() + "] [经验:" + exp + "]");
		}
		return exp;
	}

	protected void calculateExpFlop(Game game) {
		if (owner != null) {
			int teamMemberNum = 0;
			int teamMemberLevel = 0;
			Player players[] = owner.getTeamMembers();
			for (int i = 0; i < players.length; i++) {
				if (game.contains(players[i]) && players[i].isDeath() == false) {
					teamMemberNum++;
					teamMemberLevel += players[i].getLevel();
				}
			}
			boolean societyFlag = false;
			Team team = owner.getTeam();
			if (team != null) {
				societyFlag = team.isSocietyFlag();
			}

			int exp = (int) 得到怪物经验(owner);
			if (players.length != 0) {
				exp = exp / players.length;
			}
			if (exp < 0) {
				if (SpriteSubSystem.logger.isWarnEnabled()) SpriteSubSystem.logger.warn("[怪物经验错误] [{}]", new Object[] { name });
				exp = 0;
			}
			for (int i = 0; i < players.length; i++) {
				if (game.contains(players[i]) && players[i].isDeath() == false) {

					boolean masterInTeam = false;
					if (GlobalLimitManager.getInst().canAddExp(players[i], this.getSpriteCategoryId())) {
//						exp = (int) (exp * DoomsdayManager.getInstance().getDoomsdayExp(players[i], this, game));
						if(DevilSquareManager.mulMonsterIds != null && DevilSquareManager.mulMonsterIds.containsKey(this.getSpriteCategoryId())) {
							exp = (int) (exp * DevilSquareManager.mulMonsterIds.get(this.getSpriteCategoryId()));
						}
						players[i].addExp(exp, ExperienceManager.ADDEXP_REASON_FIGHTING);
					} else {
						players[i].sendError(Translate.今天通过boss获得经验上限);
					}

					// 通知玩家杀死了某个怪
					// players[i].killOneSprite(this);

				}
			}
		}
	}

	protected int calculateMoneyFlop(Game game) {
		return NumericalCalculator.计算杀死怪物掉落的金币(this.getLevel());
	}

	/**
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	public void killed(long heartBeatStartTime, long interval, Game game) {

		if (owner == null) {
			if (MonsterManager.logger.isWarnEnabled()) MonsterManager.logger.warn("[怪被打死] [{}] [{},{}] [宿主不存在]", new Object[] { this.getGameName(), this.getTitle(), this.getName() });
			return;
		}
		// 更新杀怪记录
		owner.updateKillMonster();
		// 处理任务
		owner.killOneSprite(this);
		//天劫杀怪记录
		TransitRobberyEntityManager.getInstance().add2KilledList(owner.getId(), this.getSpriteCategoryId());
		//恶魔广场副本怪物击杀通知
		DevilSquareManager ds = DevilSquareManager.instance;
		if(ds != null && DevilSquareManager.carbonMaps.contains(this.getGameName())) {
			ds.notifyMonsterKilled(this);
		}
		try {
			
			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.MONSTER_KILLED_Simple, new Object[] { new SimpleMonster(this.getId(), this.getSpriteCategoryId(), this.getName(), this.getOwner().getId(), this.getGameName(), (this instanceof BossMonster), this.level) });
			EventRouter.getInst().addEvent(evt);
//			EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.MONSTER_KILLED, new Object[] { this });
//			EventRouter.getInst().addEvent(evt);
			if (PlayerAimManager.logger.isDebugEnabled()) {
				PlayerAimManager.logger.debug("[怪物死亡事件通知] [怪物id:" + this.getId() + "] [怪物名:" + this.getName() + "][模板id:" + this.getSpriteCategoryId() + "]");
			}
		} catch (Exception e) {
			MemoryMonsterManager.logger.error("[怪物死亡事件通知异常][怪物id:" + this.getId() + "] [怪物名:" + this.getName() + "][模板id:" + this.getSpriteCategoryId() + "]", e);
		}
		RefreshSpriteData refreshSpriteData = this.getRefreshSpriteData();
		if (refreshSpriteData != null) {
			refreshSpriteData.noticeSpriteDead(this,game);
		}
		if(XianLingChallengeManager.instance.mapNames.contains(game.gi.name)){
			if(XianLingManager.logger.isDebugEnabled()) XianLingManager.logger.debug("[仙灵副本屏蔽掉落]");
			return;
		}
		
		// 玩家比怪物高20级没有任何收益，除了怪物类型为宠物和boss
		if (this.getMonsterType() != Sprite.MONSTER_TYPE_PET) {
			if (this instanceof BossMonster) {
				BossMonster bm = (BossMonster) this;
				if (!bm.canDrop(owner)) {
					MemoryMonsterManager.logger.warn("[boss死亡] [" + this.getName() + "] [主人等级过高不掉落] [主人等级:" + owner.getLevel() + "] [等级限制:" + bm.getDropLevelLimit() + "]");
					return;
				}
			} else if (this.getLevel() < owner.getMainSoul().getGrade() - 20) {
				return;
			}
		}
		// 杀怪频率统计
		if (attackRecordLogger.isDebugEnabled()) {
			String sessionId = StringUtil.randomIntegerString(15);
			CareerManager cm = CareerManager.getInstance();
			AttackRecord ars[] = this.attackRecordList.toArray(new AttackRecord[0]);
			for (int i = 0; i < ars.length; i++) {
				AttackRecord a = ars[i];
				StringBuffer sb = new StringBuffer();
				if (a.living instanceof Player) {
					Player p = (Player) a.living;
					sb.append("[" + p.getUsername() + "] [" + p.getName() + "] [" + cm.getCareer(p.getCareer()).getName() + "] [lv:" + p.getLevel() + "]");
				} else if (a.living instanceof Pet) {
					Pet pet = (Pet) a.living;
					Player p = pet.getMaster();
					if (p != null) {
						sb.append("[" + p.getUsername() + "] [" + p.getName() + "] [" + cm.getCareer(p.getCareer()).getName() + "] [lv:" + p.getLevel() + "]");
					}
					sb.append(" [" + pet.getName() + "] [" + pet.getId() + "]");
				}
				if (attackRecordLogger.isDebugEnabled()) attackRecordLogger.debug("[{}] [怪物被攻击统计] [{}] [lv:{}] [hp:{}/{}] [ms:{}] -- [{}ms] {} [ma:{}] [sa:{}] [{}] [{}] [{}] [{}]", new Object[] { sessionId, this.getName(), this.getLevel(), a.hp, this.getMaxHP(), (heartBeatStartTime - this.fightingStartTime), (a.createTime - this.fightingStartTime), sb, a.meleeAttackIntensity, a.spellAttackIntensity, a.skillName, a.skillLevel, Fighter.DAMAGETYPE_NAMES[a.damageType], a.damage });
			}
			if (owner != null) {
				if (attackRecordLogger.isDebugEnabled()) attackRecordLogger.debug("[{}] [怪物死亡] [{}] [lv:{}] [hp:{}] [ms:{}] -- [{}] [{}] [{}] [lv:{}] [ma:{}] [sa:{}]", new Object[] { sessionId, this.getName(), this.getLevel(), this.getMaxHP(), (heartBeatStartTime - this.fightingStartTime), owner.getUsername(), owner.getName(), cm.getCareer(owner.getCareer()).getName(), owner.getLevel(), owner.getPhyAttack(), owner.getMagicAttack() });
			} else {
				if (attackRecordLogger.isDebugEnabled()) attackRecordLogger.debug("[{}] [怪物死亡] [{}] [lv:{}] [hp:{}] [ms:{}]", new Object[] { sessionId, this.getName(), this.getLevel(), this.getMaxHP(), (heartBeatStartTime - this.fightingStartTime) });
			}
		}
		
		
		
		// 目前的实现，经验值分配给怪拥有者
		calculateExpFlop(game);

		// 清空owner身上的target
		if (owner.getCurrentEnemyTarget() == this) {
			owner.setCurrentEnemyTarget(null);
		}

		// 如果怪物级别大于封印级别10级，那么没有任何掉落
		if (SealManager.getInstance() != null) {
			if (level > SealManager.getInstance().getSealLevel() + 10) {
				if (Game.logger.isInfoEnabled()) {
					Game.logger.info("[怪物掉落] [" + owner.getLogString() + "] [" + name + "] [大于封印等级10级没有任何掉落]");
				}
				return;
			}
		} else {
			if (Game.logger.isInfoEnabled()) {
				Game.logger.info("[怪物掉落] [" + owner.getLogString() + "] [" + name + "] [封印管理器为空没有任何掉落]");
			}
			return;
		}

		// 掉落物品
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();

		double 疲劳惩罚 = Player.得到疲劳玩家获得掉落的惩罚百分比(owner);
		if (this instanceof BossMonster) {
			疲劳惩罚 = 1;
		}
		if (this.getName().indexOf(Translate.宝宝) >= 0 || this.getName().indexOf(Translate.精英) >= 0) {
			疲劳惩罚 = 1;
		}
		
		
		
		
		try{
			if(PetDaoManager.getInstance().isPetDao(game.getGameInfo().getMapName()))
				疲劳惩罚 = 1;
		}catch(Exception e){
			PetDaoManager.log.error("[圣兽阁免疫疲劳] [异常] [疲劳惩罚:"+疲劳惩罚+"] [" + owner.getLogString() + "] [" + name + "] ["+e+"]");
		}

		// 通过掉落方案创建掉落方案实例
		flopSchemeEntity = new FlopSchemeEntity(this);
		Player players[] = null;
		Team team = owner.getTeam();
		boolean captainInHere = false;
		if (owner.getTeamMark() != Player.TEAM_MARK_NONE && owner.getTeam() != null) {
			ArrayList<Player> al = new ArrayList<Player>();
			players = owner.getTeamMembers();
			for (int i = 0; i < players.length; i++) {
				if (game.contains(players[i]) && Math.abs(players[i].getX() - getX()) <= 320 && Math.abs(players[i].getY() - getY()) <= 320) {
					al.add(players[i]);
					if (players[i] == team.getCaptain()) {
						captainInHere = true;
					}
				}
			}
			players = al.toArray(new Player[0]);
			flopSchemeEntity.setPlayers(players);
			flopSchemeEntity.setTeam(team);

		} else {
			players = new Player[] { owner };
			flopSchemeEntity.setPlayers(players);
		}

		StringBuffer playersInfo = new StringBuffer();
		for (int i = 0; i < players.length; i++) {
			playersInfo.append(players[i].getUsername() + "/" + players[i].getId() + "/" + players[i].getName());
			if (i < players.length - 1) {
				playersInfo.append(",");
			}
		}
		ArrayList<FlopSchemeEntity.ShareFlopEntity> al = new ArrayList<FlopSchemeEntity.ShareFlopEntity>();
		ArrayList<FlopSchemeEntity.PrivateFlopEntity> al2 = new ArrayList<FlopSchemeEntity.PrivateFlopEntity>();
		int flopratePercent = owner.getFlopratePercent();
		double pk掉率惩罚 = Player.得到玩家打怪掉落的pk惩罚百分比(owner);

		ArrayList<FlopSet> needFlopList = new ArrayList<FlopSet>();

		String mapName = game.gi.getName();
		MemoryMonsterManager mmm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
		FlopSet mapFS[] = mmm.map2FlopListMap.get(mapName);
		Integer[] probs = mmm.map2FlopListProbabilityMap.get(mapName);
		for (int i = 0; mapFS != null && i < mapFS.length; i++) {
			FlopSet fs = mapFS[i];
			double floprate = 0;
			if (probs[i] != null) {
				floprate = probs[i] / MemoryMonsterManager.分母;
			}
			if (ProbabilityUtils.randomProbability(random, 1.0 * floprate * (100 + flopratePercent) * pk掉率惩罚 * 疲劳惩罚 / 100)) { // 随机掉
				needFlopList.add(fs);
			}
		}
		boolean noLevelFlop = false;
		DevilSquareManager dsmng = DevilSquareManager.instance;
		if(dsmng != null) {
			noLevelFlop = dsmng.isPlayerIndevilSquareMap(game);
		}
		if(DevilSquareManager.logger.isDebugEnabled()) {
			DevilSquareManager.logger.debug("[物品掉落] [是否有等级掉落] [result:" + noLevelFlop + "]");
		}
		if(!noLevelFlop) {					//有些地图不需要走等级掉落
			int levels[][] = mmm.level2FlopListMap.keySet().toArray(new int[0][]);
			int levelProbs[][] = mmm.level2FlopListProbabilityMap.keySet().toArray(new int[0][]);
			for (int i = 0; i < levels.length; i++) {
				int lowLevel = levels[i][0];
				int highLevel = levels[i][1];
				if (this.getLevel() >= lowLevel && this.getLevel() <= highLevel) {
					FlopSet[] fss = mmm.level2FlopListMap.get(levels[i]);
					Integer[] fsProbs = mmm.level2FlopListProbabilityMap.get(levelProbs[i]);
					for (int j = 0; fss != null && j < fss.length; j++) {
						FlopSet fs = fss[j];
						double floprate = 0;
						if (fsProbs[j] != null) {
							floprate = fsProbs[j] / MemoryMonsterManager.分母;
						}
						if (ProbabilityUtils.randomProbability(random, 1.0 * floprate * (100 + flopratePercent) * pk掉率惩罚 * 疲劳惩罚 / 100)) { // 随机掉
							needFlopList.add(fs);
						}
					}
				}
			}
		}
		double extraRate = 0;
		try {
			CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.addFlopRate, this.getGameName());
			if(cr != null && cr.getBooleanValue()) {
				extraRate = cr.getDoubleValue() * 100L;
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[怪物掉率翻倍活动] [异常]", e);
		}
		if (fsList != null && fsList.length > 0) {
			if (flopFormat == 0) {
				for (int i = 0; i < fsList.length; i++) {
					FlopSet fs = fsList[i];
					double floprate = 0;
					if (fsProbabilitis[i] != null) {
						floprate = fsProbabilitis[i] / MemoryMonsterManager.分母;
					}
					if (ProbabilityUtils.randomProbability(random, 1.0 * floprate * (100 + flopratePercent + extraRate) * pk掉率惩罚 * 疲劳惩罚 / 100)) { // 随机掉
						needFlopList.add(fs);
					}
				}
			} else {
				Integer[] ints = getFlopSetProbabilitis();
				double[] probss = new double[ints.length];
				int 分母 = 0;
				for (int i = 0; i < ints.length; i++) {
					分母 += ints[i];
				}
				for (int i = 0; i < ints.length; i++) {
					probss[i] = ints[i] * 1d / 分母;
				}
				int index = ProbabilityUtils.randomProbability(random, probss);
				needFlopList.add(fsList[index]);
			}
		}

		for (int i = 0; i < needFlopList.size(); i++) {
			FlopSet fs = needFlopList.get(i);

			if (!fs.isWithinTimeLimit()) {
				if (MonsterManager.logger.isInfoEnabled()) {
					MonsterManager.logger.info("[怪物死亡] [掉落装备] [掉落时间不合法] [{}] [耗时:{}] [{}] [{}] [Lv:{}] [{}] [玩家：{}]", new Object[] { fs.getTimeLimitAsString(), (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), playersInfo });
				}
				continue;
			}

			Article articles[] = fs.randomNext();
			for (int j = 0; j < articles.length; j++) {
				Article article = articles[j];

				// 是否可以掉落，需要查看玩家的掉落限制
				{
					if (article != null) {
						FlopManager fm = FlopManager.getInstance();
						if (fm != null) {
							boolean canFlop = fm.canFlopArticle(article.get物品二级分类(), fs.color, owner);
							if (canFlop) {
								canFlop = fm.打金工作室处理是否掉落(article, fs.color, owner);
								if (!canFlop) {
									if (FlopManager.logger.isInfoEnabled()) {
										FlopManager.logger.info("[今天打金掉落已满] [{}] [color:{}] [{}] [{}]", new Object[] { article.get物品二级分类(), fs.color, article.getName(), owner.getLogString() });
									}
								}
							}
							if (canFlop) {
								fm.addFlopArticle(article.get物品二级分类(), fs.color, owner);
								fm.通知工作室打怪掉落(article, fs.color, owner);
								if (FlopManager.logger.isInfoEnabled()) {
									FlopManager.logger.info("[尝试添加] [{}] [color:{}] [{}] [{}]", new Object[] { article.get物品二级分类(), fs.color, article.getName(), owner.getLogString() });
								}
							} else {
								if (FlopManager.logger.isInfoEnabled()) {
									FlopManager.logger.info("[今天掉落已满] [{}] [color:{}] [{}] [{}]", new Object[] { article.get物品二级分类(), fs.color, article.getName(), owner.getLogString() });
								}
								continue;
							}
						}
					}
				}

				if (fs.getFlopType() != 0) {
					for (int ii = 0; ii < players.length; ii++) {
						Player p = players[ii];
						FlopSchemeEntity.PrivateFlopEntity fp = new FlopSchemeEntity.PrivateFlopEntity();
						boolean bind = false;
						if (article != null && (article.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP || article.getBindStyle() == Article.BINDTYPE_PICKUP)) {
							bind = true;
						}
						if (article != null && article instanceof Equipment) {
							Equipment e = (Equipment) article;
							//

//							if (SpecialEquipmentManager.isSpecial(e)) {
//								try {
//									if (SpecialEquipmentManager.logger.isDebugEnabled()) {
//										SpecialEquipmentManager.logger.debug("[需要生成特殊装备] [] [" + e.getName() + "] [" + this.getName() + "]");
//									}
//
//									if (SpecialEquipmentManager.getInstance().ishaving(e)) {
//										fp.setEntity(null);
//										if (SpecialEquipmentManager.logger.isDebugEnabled()) {
//											SpecialEquipmentManager.logger.debug("[生成特殊装备失败] [已经有这种装备] [" + e.getName() + "] [" + this.getName() + "]");
//										}
//									} else {
//
//										EquipmentEntity ee = SpecialEquipmentManager.getInstance().createEntity(e, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP);
//										if (ee != null) {
//											fp.setEntity(ee);
//											if (MonsterManager.logger.isInfoEnabled()) {
//												MonsterManager.logger.info("[怪物死亡] [私有掉落特殊装备] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [品质：{}] [类型：{}] [玩家：{}/{}/{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ee.getId(), ee.getArticleName(), e.getColorType(), e.getEquipmentType(), p.getUsername(), p.getId(), p.getName() });
//											}
//										}
//									}
//								} catch (Exception ex) {
//									if (MonsterManager.logger.isInfoEnabled()) {
//										MonsterManager.logger.info("[怪物死亡] [掉私有掉落特殊装备]", ex);
//									}
//								}
//							} else
							{
								try {
									EquipmentEntity ee = (EquipmentEntity) aem.createEntity(e, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);
									fp.setEntity(ee);

									if (MonsterManager.logger.isInfoEnabled()) {
										MonsterManager.logger.info("[怪物死亡] [私有掉落装备] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [品质：{}] [类型：{}] [玩家：{}/{}/{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ee.getId(), ee.getArticleName(), e.getColorType(), e.getEquipmentType(), p.getUsername(), p.getId(), p.getName() });
									}
								} catch (Exception ex) {
									if (MonsterManager.logger.isInfoEnabled()) {
										MonsterManager.logger.info("[怪物死亡] [私有掉落装备]", ex);
									}
								}
							}

						} else if (article != null && article instanceof Props) {
							try {
								PropsEntity pe = (PropsEntity) aem.createEntity((Props) article, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);
								fp.setEntity(pe);

								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [私有掉落道具] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [玩家：{}/{}/{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), pe.getId(), pe.getArticleName(), p.getUsername(), p.getId(), p.getName() });
								}
							} catch (Exception ex) {
								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [私有掉落道具]", ex);
								}
							}
						} else if (article != null) {
							try {
								ArticleEntity ae = aem.createEntity(article, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);
								fp.setEntity(ae);

								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [私有掉落物品] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [玩家：{}/{}/{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ae.getId(), ae.getArticleName(), p.getUsername(), p.getId(), p.getName() });
								}
							} catch (Exception ex) {
								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [私有掉落物品]", ex);
								}
							}
						} else {
							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪物死亡] [无掉落] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [玩家：{}/{}/{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), p.getUsername(), p.getId(), p.getName() });
							}
						}
						fp.setPlayer(p);

						if (fp.getEntity() != null) {
							al2.add(fp);
							DevilSquareManager.instance.notifyTest(fp.getEntity().getArticleName(), fp.getEntity().getColorType());
						}
					}

				} else {
					boolean bind = false;
					if (article != null && (article.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP || article.getBindStyle() == Article.BINDTYPE_PICKUP)) {
						bind = true;
					}
					if (article != null && article instanceof Equipment) {

						Equipment e = (Equipment) article;
						EquipmentEntity ee = null;
//						if (SpecialEquipmentManager.isSpecial(e)) {
//
//							if (SpecialEquipmentManager.logger.isDebugEnabled()) {
//								SpecialEquipmentManager.logger.debug("[需要生成特殊装备] [] [" + e.getName() + "] [" + this.getName() + "]");
//							}
//
//							if (SpecialEquipmentManager.getInstance().ishaving(e)) {
//								if (SpecialEquipmentManager.logger.isDebugEnabled()) {
//									SpecialEquipmentManager.logger.debug("[生成特殊装备失败] [已经有这种装备] [" + e.getName() + "] [" + this.getName() + "]");
//								}
//							} else {
//								ee = SpecialEquipmentManager.getInstance().createEntity(e, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP);
//								if (ee != null) {
//									if (MonsterManager.logger.isInfoEnabled()) {
//										MonsterManager.logger.info("[怪物死亡] [掉落装备] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [品质：{}] [类型：{}] [玩家：{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ee.getId(), ee.getArticleName(), e.getColorType(), e.getEquipmentType(), playersInfo });
//									}
//								}
//							}
//						} else 
						{
							try {
								ee = (EquipmentEntity) aem.createEntity(e, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);
								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [掉落装备] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [品质：{}] [类型：{}] [玩家：{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ee.getId(), ee.getArticleName(), e.getColorType(), e.getEquipmentType(), playersInfo });
								}
							} catch (Exception ex) {
								if (MonsterManager.logger.isInfoEnabled()) {
									MonsterManager.logger.info("[怪物死亡] [掉落装备异常]", ex);
								}
							}
						}
						if (ee != null) {
							FlopSchemeEntity.ShareFlopEntity sfe = new FlopSchemeEntity.ShareFlopEntity(flopSchemeEntity);
							sfe.setEntity(ee);
							sfe.setPickUpFlag(false);
							byte bytes[] = new byte[players.length];
							for (int x = 0; x < players.length; x++) {
								if (team == null) {
									bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
								} else {
									if (team.getAssignRule() == Team.ASSIGN_RULE_BY_FREE) {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
									} else if (team.getAssignRule() == Team.ASSIGN_RULE_BY_CAPTAIN && captainInHere) {
										if (e.getColorType() >= team.getAssignColorType()) {
											bytes[x] = FlopSchemeEntity.ShareFlopEntity.CAPTAIN_PICKUP;
										} else {
											bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
										}
									} else {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.TEAM_PICKUP;
									}
								}
							}
							sfe.setAssgins(bytes);
							al.add(sfe);
							if(sfe.getEntity() != null) {
								DevilSquareManager.instance.notifyTest(sfe.getEntity().getArticleName(), sfe.getEntity().getColorType());
							}

						}
					} else if (article != null && article instanceof Props) {
						try {
							PropsEntity pe = (PropsEntity) aem.createEntity((Props) article, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);

							FlopSchemeEntity.ShareFlopEntity sfe = new FlopSchemeEntity.ShareFlopEntity(flopSchemeEntity);
							sfe.setEntity(pe);
							sfe.setPickUpFlag(false);
							byte bytes[] = new byte[players.length];
							for (int x = 0; x < players.length; x++) {
								if (team == null) {
									bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
								} else {

									if (team.getAssignRule() == Team.ASSIGN_RULE_BY_FREE) {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
									} else {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.TEAM_PICKUP;
									}
								}
							}

							sfe.setAssgins(bytes);
							al.add(sfe);
							if(sfe.getEntity() != null) {
								DevilSquareManager.instance.notifyTest(sfe.getEntity().getArticleName(), sfe.getEntity().getColorType());
							}

							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪物死亡] [掉落道具] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [玩家：{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), pe.getId(), pe.getArticleName(), playersInfo });
							}
						} catch (Exception ex) {
							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪物死亡] [掉落道具异常]", ex);
							}
						}
					} else if (article != null) {
						try {
							ArticleEntity ae = aem.createEntity(article, bind, ArticleEntityManager.CREATE_REASON_SPRITE_FLOP, null, fs.color, 1, false);

							FlopSchemeEntity.ShareFlopEntity sfe = new FlopSchemeEntity.ShareFlopEntity(flopSchemeEntity);
							sfe.setEntity(ae);
							sfe.setPickUpFlag(false);
							byte bytes[] = new byte[players.length];
							for (int x = 0; x < players.length; x++) {
								if (team == null) {
									bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
								} else {

									if (team.getAssignRule() == Team.ASSIGN_RULE_BY_FREE) {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.FREE_PICKUP;
									} else {
										bytes[x] = FlopSchemeEntity.ShareFlopEntity.TEAM_PICKUP;
									}
								}
							}
							sfe.setAssgins(bytes);
							al.add(sfe);
							if(sfe.getEntity() != null) {
								DevilSquareManager.instance.notifyTest(sfe.getEntity().getArticleName(), sfe.getEntity().getColorType());
							}

							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪物死亡] [掉落物品] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [Id：{}] [{}] [玩家：{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), ae.getId(), ae.getArticleName(), playersInfo });
							}
						} catch (Exception ex) {
							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪物死亡] [掉落物品]", ex);
							}
						}
					} else {

						if (MonsterManager.logger.isInfoEnabled()) {
							MonsterManager.logger.info("[怪物死亡] [无掉落] [耗时:{}] [{}] [{}] [Lv:{}] [{}] -- [玩家：{}]", new Object[] { (heartBeatStartTime - this.fightingStartTime), this.getId(), this.getName(), this.getLevel(), this.getGameName(), playersInfo });
						}
					}
				}
			}
		}
		// 判断是否掉落特殊装备，加入到共享当中
//		if (SpecialEquipmentManager.getInstance().isSpecialMonster(this)) {
//			SpecialEquipmentManager.logger.warn("[特殊怪物死亡] [" + this.getSpriteCategoryId() + "] [" + (owner != null ? owner.getLogString() : "null") + "]");
//			SpecialEquipmentManager.getInstance().specialMonsterKilled(this, al, players, flopSchemeEntity);
//		}

		// //////////在怪物周围掉落物品
		怪物掉落物品(game, owner, al, players);
		怪物掉落物品(game, owner, al2, players);
		// //////
		// 中秋国庆双节活动掉落
		MonsterDropActivityManager mdam = MonsterDropActivityManager.getInstance();
		if (mdam != null) {
			mdam.doDrop(owner);
		}
		
		try {
			if(DownCityManager2.instance.cityMap.containsKey(new Long(owner.getId()))){
				DownCityManager2.instance.cityMap.get(new Long(owner.getId())).killMonster(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// flopSchemeEntity.setShareEntities(al.toArray(new FlopSchemeEntity.ShareFlopEntity[0]));
		// flopSchemeEntity.setPrivateFlopEntities(al2.toArray(new FlopSchemeEntity.PrivateFlopEntity[0]));
		//
		// if (singlePlayerTaskArticleFlag) {
		// FlopSchemeEntity fse = flopSchemeEntity;
		// ArticleEntity aes[] = fse.getAvailableArticleEntities(owner);
		// byte[] assigns = fse.getAvailableArticleEntitiesAssign(owner);
		//
		// // 拾取就绑定的问题
		// com.fy.engineserver.datasource.article.entity.client.ArticleEntity cells[] = new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[aes.length];
		// for (int i = 0; i < aes.length; i++) {
		// cells[i] = CoreSubSystem.translate(aes[i]);
		// cells[i].setAssignFlag(assigns[i]);
		// }
		//
		// owner.addMessageToRightBag(new QUERY_FLOPAVAILABLE_RES(GameMessageFactory.nextSequnceNum(), this.getId(), fse.getMoney(), cells));
		// }

		// 副本统计
		if (game != null && game.getDownCity() != null) {
			DownCity dc = game.getDownCity();
			FlopSchemeEntity.ShareFlopEntity ses[] = flopSchemeEntity.getShareEntities();
			FlopSchemeEntity.PrivateFlopEntity pes[] = flopSchemeEntity.getPrivateFlopEntities();
			StringBuffer sb = new StringBuffer();
			for (FlopSchemeEntity.ShareFlopEntity se : ses) {
				if (se != null && se.getEntity() != null) {
					sb.append(se.getEntity().getArticleName() + ",");
				}
			}
			for (FlopSchemeEntity.PrivateFlopEntity se : pes) {
				if (se != null && se.getEntity() != null) {
					sb.append(se.getEntity().getArticleName() + ",");
				}
			}
			dc.statDowncityOutput(this.getName(), sb.toString(), 0);
		}
		if (MonsterManager.logger.isInfoEnabled()) {
			MonsterManager.logger.info("[怪被打死] [{}] [{},{}] [物品掉落成功] 【{}】", new Object[] { this.getGameName(), this.getTitle(), this.getName(), flopSchemeEntity.toString() });
		}
		// 30前玩家打死怪的统计
		// if(owner.getLevel() <= 30){
		Player.sendPlayerAction(owner, PlayerActionFlow.行为类型_打怪, this.getName(), 0, new Date(), GamePlayerManager.isAllowActionStat());
		// }
	}

	public void 怪物掉落物品(Game game, Player player, List os, Player[] canPickPlayers) {
		try {
			if (os != null) {
				int size = os.size();
				if (TeamSubSystem.logger.isDebugEnabled()) TeamSubSystem.logger.debug("[怪物死亡掉落物品] [" + this.getName() + "] [个数:" + size + "]");
//				if (SiFangManager.getInstance().isSiFangBOSS(player, game, this, os)) {
//					return;
//				}
				NPCManager nm = MemoryNPCManager.getNPCManager();
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				for (int i = 0; i < size; i++) {
					Object o = os.get(i);
					if (o instanceof FlopSchemeEntity.ShareFlopEntity) {

						FlopSchemeEntity.ShareFlopEntity sfe = (FlopSchemeEntity.ShareFlopEntity) o;
						ArticleEntity ae = sfe.getEntity();
						if (ae == null) {
							continue;
						}

						FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
						fcn.setStartTime(now);
						fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
						fcn.setAllCanPickAfterTime(MemoryNPCManager.所有人都可以拾取的时长);
						fcn.setOwner(this.getOwner());
						fcn.setAe(sfe.getEntity());
						fcn.isMonsterFlop = true;
						if (this.getOwner() != null && this.getOwner().getTeam() != null) {
							Team team = this.getOwner().getTeam();
							if (team.getAssignRule() == (byte) 1) {
								// 顺序拾取
								fcn.setFlopType((byte) 1);
								List<Player> list = team.getMembers();
								long lastPickUpPlayer = team.getLastPickUpPlayerId();
								long nextPickUpId = -1;
								int num = 0;
								boolean having = false;
								for (Player member : list) {
									++num;
									if (member.getId() == lastPickUpPlayer) {
										having = true;
										break;
									}
								}
								int index = 0;
								int teamSize = team.getSize();
								if (having) {
									index = num - 1;
								}
								Player nextPickPlayer = null;
								if (canPickPlayers.length > 0) {

									boolean run = true;
									while (run) {

										++index;
										if (index >= teamSize) {
											index = 0;
										}
										nextPickUpId = list.get(index).getId();
										for (Player pickPlayer : canPickPlayers) {
											if (pickPlayer.getId() == nextPickUpId) {
												nextPickPlayer = list.get(index);
												team.setLastPickUpPlayerId(nextPickUpId);
												run = false;
											}
										}
									}
									fcn.setOwner(nextPickPlayer);
								}
							}
							fcn.setTeam(this.getOwner().getTeam());
						}
						if(DevilSquareManager.carbonMaps.contains(game.gi.getName())) {
							fcn.setAllCanPickAfterTime(0);					//如果地图是恶魔广场副本则物品没有保护时间
							fcn.setFlopType((byte) 0);
						}
						Point point = 得到位置();
						fcn.setX(point.x);
						fcn.setY(point.y);
						ArticleManager am = ArticleManager.getInstance();
						Article article = am.getArticle(ae.getArticleName());
						int color = ArticleManager.COLOR_WHITE;
						try {
							if (article.getFlopNPCAvata() != null) {
								fcn.setAvataSex(article.getFlopNPCAvata());
								if (article.getFlopNPCAvata().equals("yinyuanbao")) {
									fcn.setAvataSex("baoxiang");
								}
								ResourceManager.getInstance().getAvata(fcn);
							}
							color = ArticleManager.getColorValue(article, ae.getColorType());
						} catch (Exception ex) {
							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪被打死] [物品掉落异常] [" + ae.getArticleName() + "] [" + ae.getId() + "]", ex);
							}
						}

						fcn.setName(ae.getArticleName());
						fcn.setNameColor(color);

						fcn.setTitle("");
						game.addSprite(fcn);

						// 物品掉落声音广播
						((FlopCaijiNpc) fcn).notifyAroundPlayersPlaySound(game);
						if (MonsterManager.logger.isInfoEnabled()) {
							MonsterManager.logger.info("[怪被打死] [物品掉落] [" + ae.getArticleName() + "] [" + ae.getId() + "]");
						}

					}

					if (o instanceof FlopSchemeEntity.PrivateFlopEntity) {
						FlopSchemeEntity.PrivateFlopEntity sfe = (FlopSchemeEntity.PrivateFlopEntity) o;
						ArticleEntity ae = sfe.getEntity();
						if (ae == null) {
							continue;
						}

						FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
						fcn.setFlopType((byte) 1);
						fcn.setStartTime(now);
						fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
						fcn.setAllCanPickAfterTime(MemoryNPCManager.掉落NPC的存在时间 + 1000);
						fcn.setOwner(sfe.getPlayer());
						fcn.setAe(sfe.getEntity());
						fcn.isMonsterFlop = true;
						Point point = 得到位置();
						fcn.setX(point.x);
						fcn.setY(point.y);
						StringBuffer sb = new StringBuffer();
						ArticleManager am = ArticleManager.getInstance();
						Article article = am.getArticle(ae.getArticleName());
						int color = ArticleManager.COLOR_WHITE;
						try {
							if (article.getFlopNPCAvata() != null) {
								fcn.setAvataSex(article.getFlopNPCAvata());
								if (article.getFlopNPCAvata().equals("yinyuanbao")) {
									fcn.setAvataSex("baoxiang");
								}
								ResourceManager.getInstance().getAvata(fcn);
							}
							color = ArticleManager.getColorValue(article, ae.getColorType());
						} catch (Exception ex) {
							if (MonsterManager.logger.isInfoEnabled()) {
								MonsterManager.logger.info("[怪被打死] [" + this.getName() + "] [物品掉落] [" + (sfe.getPlayer() == null ? "--" : sfe.getPlayer().getLogString()) + "] [" + article.getLogString() + "] [" + ae.getId() + "]", ex);
							}
						}
						sb.append(ae.getArticleName());
						fcn.setName(sb.toString());
						fcn.setNameColor(color);
						fcn.setTitle("");
						game.addSprite(fcn);

						// 物品掉落声音广播
						((FlopCaijiNpc) fcn).notifyAroundPlayersPlaySound(game);
						if (MonsterManager.logger.isInfoEnabled()) {
							MonsterManager.logger.info("[怪被打死] [" + this.getName() + "] [物品掉落] [" + (sfe.getPlayer() == null ? "--" : sfe.getPlayer().getLogString()) + "] [" + article.getLogString() + "] [" + ae.getId() + "]");
						}
					}
				}
			}
		} catch (Exception ex) {
			if (MonsterManager.logger.isInfoEnabled()) {
				MonsterManager.logger.info("[怪被打死] [" + this.getName() + "] [物品掉落异常]", ex);
			}
		}
	}

	public Point 得到位置() {
		Point point = new Point();
		point.x = this.x + random.nextInt(400) - 200;
		point.y = this.y + random.nextInt(250) - 125;
		return point;
	}

	/**
	 * 攻击目标消失，将此目标从仇恨列表中清除
	 * 
	 * @param target
	 */
	public void targetDisappear(Fighter target) {
		DamageRecord record = null;
		for (int i = 0; i < hatridList.size(); i++) {
			DamageRecord dr = hatridList.get(i);
			if (dr.f == target) {
				record = dr;
			}
		}
		if (record != null) {
			hatridList.remove(record);
		}
		if (target == owner) {
			owner = null;
		}

		if (hatridList.size() == 0) {
			attackRecordList.clear();
			fightingStartTime = 0;
		}

		// 通知更新敌人列表
		target.notifyEndToBeFighted(this);
		this.notifyEndToFighting(target);
	}

	public boolean isSameTeam(Fighter fighter) {
		return false;
	}

	public int getMp() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 在激活范围内，寻找可攻击的对象，
	 * 如果范围内没有可攻击的对象，
	 * 就查看同类的怪，是否有被其他玩家攻击，如果有，协调攻击。
	 * @return
	 */
	protected Fighter findTargetInActivationRange(Game game) {
		if (activationType == 1 || activationType == 2) {
			Fighter fs[] = game.getVisbleFighter(this, false);
			Fighter f = null;
			int minD = 0;
			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Player) {
					if (!fs[i].isObjectOpacity() && fs[i].getX() >= this.x - this.activationWidth / 2 && fs[i].getX() <= this.x + this.activationWidth / 2 && fs[i].getY() >= this.y - this.activationHeight / 2 && fs[i].getY() <= this.y + this.activationHeight / 2) {
						int d = (fs[i].getX() - this.x) * (fs[i].getX() - this.x) + (fs[i].getY() - this.y) * (fs[i].getY() - this.y);
						if (f == null) {
							f = fs[i];
							minD = d;
						} else if (d < minD) {
							f = fs[i];
							minD = d;
						}
					}
				}
				if (fs[i] instanceof Pet) {
					if (!fs[i].isObjectOpacity() && fs[i].getX() >= this.x - this.activationWidth / 2 && fs[i].getX() <= this.x + this.activationWidth / 2 && fs[i].getY() >= this.y - this.activationHeight / 2 && fs[i].getY() <= this.y + this.activationHeight / 2) {
						int d = (fs[i].getX() - this.x) * (fs[i].getX() - this.x) + (fs[i].getY() - this.y) * (fs[i].getY() - this.y);
						if (f == null) {
							f = fs[i];
							minD = d;
						} else if (d < minD) {
							f = fs[i];
							minD = d;
						}
					}
				}
			}
			if (f != null) {
				// Game.logger.debug("[DEBUG怪物获得视野范围内fighter] [" + game.getGameInfo().getName() + "] [" + this.getName() + "] [" + f.getName() + "]");
				if (MonsterManager.logger.isDebugEnabled()) MonsterManager.logger.debug("[DEBUG怪物获得视野范围内fighter] [{}] [{}] [{}]", new Object[] { game.getGameInfo().getName(), this.getName(), f.getName() });
				return f;
			}
			if (activationType == 1) return null;

			for (int i = 0; i < fs.length; i++) {
				if (fs[i] instanceof Monster) {
					Monster s = (Monster) fs[i];
					// if(s.title == null || !s.title.equals(title)) continue;
					if (s.getX() >= this.x - this.activationWidth / 2 && s.getX() <= this.x + this.activationWidth / 2 && s.getY() >= this.y - this.activationHeight / 2 && s.getY() <= this.y + this.activationHeight / 2) {
						Fighter target = s.getMaxHatredFighter();
						if (target != null) {
							f = target;
							break;
						}
					}
				}
			}
			if (f != null) return f;

		}

		return null;
	}

	/**
	 * 巡逻
	 * @param game
	 */
	protected void patroling(Game game) {
		if (heartBeatStartTime - this.lastTimeForPatroling > this.patrolingTimeInterval) {
			if (random.nextInt(100) > 50) {
				return;
			}
			lastTimeForPatroling = heartBeatStartTime;

			int px = this.bornPoint.x + random.nextInt(this.patrolingWidth) - this.patrolingWidth / 2;
			int py = this.bornPoint.y + random.nextInt(this.patrolingHeight) - this.patrolingHeight / 2;
			if (px > 0 && px < game.getGameInfo().getWidth() && py > 0 && py < game.getGameInfo().getHeight()) {
				Point s = new Point(getX(), getY());
				Point e = new Point(px, py);

				if (game.getGameInfo().navigator.isStrictVisiable(s.x, s.y, e.x, e.y)) {
					short roadLen[] = new short[1];
					roadLen[0] = (short) Graphics2DUtil.distance(s, e);
					if (speed <= 2) {
						MoveTrace path = new MoveTrace(roadLen, new Point[] { s, e }, (long) (heartBeatStartTime + roadLen[0] * 1000 / 1));
						setMoveTrace(path);
					} else {
						MoveTrace path = new MoveTrace(roadLen, new Point[] { s, e }, (long) (heartBeatStartTime + roadLen[0] * 1000 / (speed / 2)));
						setMoveTrace(path);
					}
				}
			}
		}
	}

	/**
	 * 种植一个buff到玩家或者怪的身上， 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff) {
		Buff old = null;
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

	/**
	 * 心跳处理的顺序
	 * 
	 * 1. 先处理父类的心跳
	 * 2. 处理是否被打死，如果是调用killed方法，同时设置state为STATE_DEAD状态
	 * 3. 判断是否已经超过死亡过程设定的时间，如果是，标记此怪不再存活，设置alive标记
	 * 
	 */
	long recordtime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		super.heartbeat(heartBeatStartTime, interval, game);

		if (state != STATE_DEAD) {
			if (this.getHp() <= 0) {
				this.removeMoveTrace();
				this.state = STATE_DEAD;
				try{
					killed(heartBeatStartTime, interval, game);
				}catch (Exception e) {
					e.printStackTrace();
					Game.logger.warn("处理怪物死亡异常,",e);
				}
				// if (this.flopSchemeEntity != null && this.flopSchemeEntity.isEmpty() == false) {
				// deadEndTime = heartBeatStartTime + this.deadLastingTimeForFloop;
				// } else {
				deadEndTime = heartBeatStartTime + this.deadLastingTime;
				// }

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
								// ActiveSkill.logger.debug("[死亡去除BUFF] [" + getName() + "] [死亡] [" + (bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".") +
								// 1)) + ":" + bu.getTemplateName() + "] [time:" + bu.getInvalidTime() + "]");
								if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("[死亡去除BUFF] [{}] [死亡] [{}:{}] [time:{}]", new Object[] { getName(), (bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".") + 1)), bu.getTemplateName(), bu.getInvalidTime() });
							}
						}
					}
				}

			}
		}

		if (state == STATE_DEAD) {

			// if (flopSchemeEntity != null) {
			// flopSchemeEntity.heartbeat(heartBeatStartTime, interval, game);

			// if (flopEntityNotEmtpy && flopSchemeEntity.isEmpty()) {
			// flopEntityNotEmtpy = false;
			// if (heartBeatStartTime + deadLastingTime < this.deadEndTime) {
			// this.deadEndTime = heartBeatStartTime + deadLastingTime;
			// }
			//
			// }
			// }

			if (heartBeatStartTime > deadEndTime) {
				this.setAlive(false);
			}

			return;
		}

		// 定身或者眩晕的情况下，停止移动
		if (this.isHold() || this.isStun()) {
			if (this.getMoveTrace() != null) {
				stopAndNotifyOthers();
			}
		}

		this.skillAgent.heartbeat(game);
		this.fightingAgent.heartbeat(heartBeatStartTime, game, monsterLocat);

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
		}

		if (innerState == 0) {
			Fighter f = findTargetInActivationRange(game);
			if (f != null) {
				updateDamageRecord(f, 0, 1);
			}
			if (hatridList.size() == 0) {
				if (this.getMoveTrace() == null && this.traceType == 1) {
					if (this.isHold() == false && this.isStun() == false) {
						patroling(game);
					}
				}
			} else {
				innerState = 1;
			}
		}

		if (innerState == 1) {
			if (hatridList.size() == 0) {
				if (this.isHold() == false && this.isStun() == false) {

					if (bornPoint != null) {
						this.fightingAgent.pathFinding(game, this.bornPoint.x, this.bornPoint.y);
					}
					backToBornPoint(game);

					innerState = 2;
				}
			} else {

				if (this.isStun() == false) {
					Fighter target = getMaxHatredFighter();
					if (target != null && this.fightingAgent.isFighting() == false) {
						ActiveSkill as = getActiveSkill(target);
						if (MonsterManager.logger.isDebugEnabled()) MonsterManager.logger.debug("[怪物心跳] [攻击目标:{}] [使用技能:{}] [{}] [{}] [{{}}{{}}{{}}]", new Object[] { target.getName(), (as == null ? "NULL" : as.getName()), name, this.gameName, this.avataRace, this.avataType[0], this.avata[0] });
						if (as != null) {
							this.fightingAgent.start(as, getSkillLevelById(as.getId()), target);
						}
					} else if (target != null && this.fightingAgent.target != target) {
						ActiveSkill as = getActiveSkill(target);
						if (MonsterManager.logger.isDebugEnabled()) MonsterManager.logger.debug("[怪物心跳] [切换目标:{}] [使用技能:{}] [{}] [{}] [{{}}{{}}{{}}]", new Object[] { target.getName(), (as == null ? "NULL" : as.getName()), name, this.gameName, this.avataRace, this.avataType[0], this.avata[0] });
						if (as != null) {
							this.fightingAgent.start(as, getSkillLevelById(as.getId()), target);
						}
					}
				}
			}
		}

		if (innerState == 2) {
			if (heartBeatStartTime - lastTimeForRecoverHP > 500) {
				lastTimeForRecoverHP = heartBeatStartTime;
				int hpa = this.backBornPointHp;

				if ((hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)) {
					this.setHp(getHp() + hpa);
				}
			}

			if (this.getMoveTrace() == null) {
				int dx = this.x - bornPoint.x;
				int dy = this.y - bornPoint.y;
				if (dx * dx + dy * dy <= 500) {
					innerState = 0;
					this.setHp(this.getMaxHP());
				}
			}

			if (hatridList.size() > 0) {
				if (this.isStun() == false) {
					Fighter target = getMaxHatredFighter();
					if (target != null) {
						ActiveSkill as = getActiveSkill(target);
						// Game.logger.debug("[怪物心跳] [找到新目标:" + target.getName() + "] [使用技能:" + (as == null ? "NULL" : as.getName()) + "] [" + name + "] [" + this.gameName + "] [{"
						// + this.avataRace + "}{" + this.avataType[0] + "}{" + this.avata[0] + "}]");
						if (MonsterManager.logger.isDebugEnabled()) MonsterManager.logger.debug("[怪物心跳] [找到新目标:{}] [使用技能:{}] [{}] [{}] [{{}}{{}}{{}}]", new Object[] { target.getName(), (as == null ? "NULL" : as.getName()), name, this.gameName, this.avataRace, this.avataType[0], this.avata[0] });
						if (as != null) {
							this.fightingAgent.start(as, getSkillLevelById(as.getId()), target);
							innerState = 1;
						}
					}
				}
			}
		}

		// Game.logger.debug("[怪物心跳] ["+name+"] ["+this.gameName+"] [{"+this.avataRace+"}{"+this.avataType[0]+"}{"+this.avata[0]+"}]");
		long cur = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long cost = cur - start;

		fps++;

		if (cur - recordtime > 5000) {
			recordtime = cur;
			fps = 0;

		}
	}

	long fps = 0;

	/**
	 * 如果不存在此技能，返回0
	 * 否则至少是返回1
	 * @param sid
	 * @return
	 */
	public int getSkillLevelById(int sid) {
		for (int i = 0; activeSkillIds != null && i < this.activeSkillIds.length; i++) {
			if (activeSkillIds[i] == sid && this.activeSkillLevels != null && i < this.activeSkillLevels.length) {
				if (activeSkillLevels[i] == 0) return 1;
				return activeSkillLevels[i];
			}
		}
		return 0;
	}

	protected void backToBornPoint(Game de) {
		Point s = new Point(getX(), getY());
		Point e = this.bornPoint;

		SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
		if (sps == null) return;
		Point ps[] = new Point[sps.length + 2];
		short roadLen[] = new short[sps.length + 1];
		for (int i = 0; i < ps.length; i++) {
			if (i == 0) {
				ps[i] = new Point(s.x, s.y);
			} else if (i <= sps.length) {
				ps[i] = new Point(sps[i - 1].x, sps[i - 1].y);
			} else {
				ps[i] = new Point(e.x, e.y);
			}
		}
		int totalLen = 0;
		for (int i = 0; i < roadLen.length; i++) {
			if (i == 0) {
				roadLen[i] = (short) Graphics2DUtil.distance(ps[0], ps[1]);
			} else if (i < sps.length) {
				Road r = de.getGameInfo().navigator.getRoadBySignPost(sps[i - 1].id, sps[i].id);
				if (r != null) {
					roadLen[i] = r.len;
				} else {
					roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
				}
			} else {
				roadLen[i] = (short) Graphics2DUtil.distance(ps[i], ps[i + 1]);
			}
			totalLen += roadLen[i];
		}
		if (speed <= 0) {
			MoveTrace path = new MoveTrace(roadLen, ps, heartBeatStartTime + totalLen * 1000 / 1 + 2000);
			setMoveTrace(path);
		} else {
			MoveTrace path = new MoveTrace(roadLen, ps, heartBeatStartTime + totalLen * 1000 / (speed) + 2000);
			setMoveTrace(path);
		}

	}

	public int getSpriteCategoryId() {
		return spriteCategoryId;
	}

	public void setSpriteCategoryId(int spriteCategoryId) {
		this.spriteCategoryId = spriteCategoryId;
	}

	public int getTraceType() {
		return traceType;
	}

	public int getPatrolingWidth() {
		return patrolingWidth;
	}

	public int getPatrolingHeight() {
		return patrolingHeight;
	}

	public long getPatrolingTimeInterval() {
		return patrolingTimeInterval;
	}

	public int getActivationType() {
		return activationType;
	}

	public int getActivationWidth() {
		return activationWidth;
	}

	public int getActivationHeight() {
		return activationHeight;
	}

	public int getPursueWidth() {
		return pursueWidth;
	}

	public int getPursueHeight() {
		return pursueHeight;
	}

	public int getBackBornPointMoveSpeedPercent() {
		return backBornPointMoveSpeedPercent;
	}

	public int getBackBornPointHp() {
		return backBornPointHp;
	}

	public long getLastTimeForPatroling() {
		return lastTimeForPatroling;
	}

	public long getLastTimeForRecoverHP() {
		return lastTimeForRecoverHP;
	}

	public ArrayList<ActiveSkill> getSkillList() {
		return skillList;
	}

	public ActiveSkillAgent getSkillAgent() {
		return skillAgent;
	}

	public MonsterFightingAgent getFightingAgent() {
		return fightingAgent;
	}

	public ArrayList<DamageRecord> getHatridList() {
		return hatridList;
	}

	public int getInnerState() {
		return innerState;
	}

	public Player getOwner() {
		return owner;
	}

	public void setTraceType(int traceType) {
		this.traceType = traceType;
	}

	public void setPatrolingWidth(int patrolingWidth) {
		this.patrolingWidth = patrolingWidth;
	}

	public void setPatrolingHeight(int patrolingHeight) {
		this.patrolingHeight = patrolingHeight;
	}

	public void setPatrolingTimeInterval(long patrolingTimeInterval) {
		this.patrolingTimeInterval = patrolingTimeInterval;
	}

	public void setPursueWidth(int pursueWidth) {
		this.pursueWidth = pursueWidth;
	}

	public void setPursueHeight(int pursueHeight) {
		this.pursueHeight = pursueHeight;
	}

	public int[] getActiveSkillIds() {
		return activeSkillIds;
	}

	public void setActiveSkillIds(int[] activeSkillIds) {
		this.activeSkillIds = activeSkillIds;
	}

	public int[] getActiveSkillLevels() {
		return activeSkillLevels;
	}

	public void setActiveSkillLevels(int[] activeSkillLevels) {
		this.activeSkillLevels = activeSkillLevels;
	}

	public void setActivationType(int activationType) {
		this.activationType = activationType;
	}

	public void setActivationWidth(int activationWidth) {
		this.activationWidth = activationWidth;
	}

	public void setActivationHeight(int activationHeight) {
		this.activationHeight = activationHeight;
	}

	public void setBackBornPointMoveSpeedPercent(int backBornPointMoveSpeedPercent) {
		this.backBornPointMoveSpeedPercent = backBornPointMoveSpeedPercent;
	}

	public void setBackBornPointHp(int backBornPointHp) {
		this.backBornPointHp = backBornPointHp;
	}

	public byte getMonsterRace() {
		return monsterRace;
	}

	public void setMonsterRace(byte monsterRace) {
		this.monsterRace = monsterRace;
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		Monster p = new Monster();

		p.cloneAllInitNumericalProperty(this);
		p.deadLastingTimeForFloop = this.deadLastingTimeForFloop;

		p.spriteCategoryId = spriteCategoryId;

		p.activationHeight = this.activationHeight;
		p.activationType = this.activationType;
		p.activationWidth = this.activationWidth;
		p.activeSkillIds = this.activeSkillIds;
		p.activeSkillLevels = this.activeSkillLevels;
		p.backBornPointHp = this.backBornPointHp;
		p.backBornPointMoveSpeedPercent = this.backBornPointMoveSpeedPercent;

		p.career = this.career;
		p.monsterRace = this.monsterRace;
		p.icon = this.icon;
		p.color = this.color;
		p.monsterMark = this.monsterMark;
		p.hpMark = this.hpMark;
		p.apMark = this.apMark;
		p.monsterType = this.monsterType;
		p.patrolingHeight = this.patrolingHeight;
		p.patrolingTimeInterval = this.patrolingTimeInterval;
		p.patrolingWidth = this.patrolingWidth;
		p.pursueHeight = this.pursueHeight;
		p.pursueWidth = this.pursueWidth;
		p.traceType = this.traceType;
		p.avataRace = this.avataRace;
		p.avataSex = this.avataSex;

		p.fsList = this.fsList;
		p.fsProbabilitis = this.fsProbabilitis;

		p.setMaxHPA(this.maxHPA);
		p.setPhyAttackA(this.phyAttackA);
		p.setMagicAttackA(this.magicAttackA);
		p.setPhyDefenceA(this.phyDefenceA);
		p.setMagicDefenceA(this.magicDefenceA);
		p.setMaxMPA(this.maxMPA);
		p.setBreakDefenceA(this.breakDefenceA);
		p.setHitA(this.hitA);
		p.setDodgeA(this.dodgeA);
		p.setAccurateA(this.accurateA);
		p.setCriticalHitA(this.criticalHitA);
		p.setCriticalDefenceA(this.criticalDefenceA);
		p.setFireAttackA(this.fireAttackA);
		p.setBlizzardAttackA(this.blizzardAttackA);
		p.setWindAttackA(this.windAttackA);
		p.setThunderAttackA(this.thunderAttackA);
		p.setFireDefenceA(this.fireDefenceA);
		p.setBlizzardDefenceA(this.blizzardDefenceA);
		p.setWindDefenceA(this.windDefenceA);
		p.setThunderDefenceA(this.thunderDefenceA);
		p.setFireIgnoreDefenceA(this.fireIgnoreDefenceA);
		p.setBlizzardIgnoreDefenceA(this.blizzardIgnoreDefenceA);
		p.setWindIgnoreDefenceA(this.windIgnoreDefenceA);
		p.setThunderIgnoreDefenceA(this.thunderIgnoreDefenceA);

		return p;
	}

	public long getDeadLastingTimeForFloop() {
		return deadLastingTimeForFloop;
	}

	public void setDeadLastingTimeForFloop(long deadLastingTimeForFloop) {
		this.deadLastingTimeForFloop = deadLastingTimeForFloop;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * 驯化为宠物
	 * @return
	 */
	public Pet tameFromMonster(Player master) {
		return null;
	}

	public static void main(String[] args) {
		ArrayList<String> l = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			l.add(i + "");
		}
		String[] a = l.toArray(new String[0]);
		String[] b = l.toArray(new String[0]);
		// System.out.println((a == b) + "" + a + "" + b);
	}

	public Player getLastAttacker() {
		return lastAttacker;
	}

	public void setLastAttacker(Player lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	public byte getMonsterLocat() {
		return monsterLocat;
	}

	public void setMonsterLocat(byte monsterLocat) {
		this.monsterLocat = monsterLocat;
	}

}
