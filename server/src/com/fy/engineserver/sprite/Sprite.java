package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import com.fy.engineserver.activity.RefreshSpriteManager.RefreshSpriteData;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 怪和NPC的超类
 * 怪的avatar属性，此属性为静态属性，
 * 在怪物编辑器中需要编辑此属性，此属性的值是由美术来整理好的
 * 
 * 比如：
 * 对于猫科类的动物
 * [0,0,0,0,0] 标识老虎
 * [1,0,0,1,0] 标识豹子
 * [1,0,1,0,1] 标识猫
 * 对于爬行类的动物
 * [1,0,1] 标识蛇
 * [0,0,0] 标识蜈蚣
 * 
 * 
 * 
 */

public class Sprite extends AbstractSprite {

	// 常量定义
	// 怪
	public static final byte SPRITE_TYPE_MONSTER = 0;

	// NPC
	public static final byte SPRITE_TYPE_NPC = 1;

	public static final byte SPRITE_TYPE_PET = 2;
	
	public static final byte SPRITE_TYPE_PLAYER = 3;

	// 普通的怪
	public static final byte MONSTER_TYPE_NORMAL = 0;
	// 主动的怪
	public static final byte MONSTER_TYPE_ACTIVE = 1;
	// 精英怪
	public static final byte MONSTER_TYPE_ELITE = 2;

	// 副本BOSS
	public static final byte MONSTER_TYPE_BOSS = 3;

	// 宠物
	public static final byte MONSTER_TYPE_PET = 4;

	// 经验怪专用
	public static final byte MONSTER_EXP = 99;

	// 普通的NPC
	public static final byte NPC_TYPE_NORMAL = 0;

	// 银子商店NPC
	public static final byte NPC_TYPE_SHOP_YINZI = 1;

	// 绑银商店NPC
	public static final byte NPC_TYPE_SHOP_BANGYIN = 2;

	// 工资商店NPC
	public static final byte NPC_TYPE_SHOP_GONGZI = 3;

	// 仓库NPC
	public static final byte NPC_TYPE_DEPOT = 4;

	// 大夫
	public static final byte NPC_TYPE_DOCTOR = 5;

	// 修理大师
	public static final byte NPC_TYPE_REPAIRE = 6;

	// 车夫
	public static final byte NPC_TYPE_DELIVER = 7;

	// 驿站，拉镖用
	public static final byte NPC_TYPE_YIZHAN = 8;

	// 采集NPC
	// public static final byte NPC_TYPE_CAIJI = 9;

	// 门 NPC
	public static final byte NPC_TYPE_DOOR = 10;

	// 战场NPC
	public static final byte NPC_TYPE_BATTLE_FIELD = 11;

	// 拾取掉落物品
	public static final byte NPC_TYPE_GET_DROP = 12;

	// 活动
	public static final byte NPC_TYPE_HUODONG = 13;

	// 洞府
	public static final byte NPC_TYPE_DONGFU = 14;

	// 刺探
	public static final byte NPC_TYPE_CITAN = 15;

	// 守卫
	public static final byte NPC_TYPE_GUARD = 16;

	// 护送NPC
	public static final byte NPC_TYPE_CONVOY = 17;

	// 镖车NPC
	public static final byte NPC_TYPE_BIAOCHE = 18;

	// 地物形式的NPC
	public static final byte NPC_TYPE_GROUND = 19;
	// 洞府门NPCNPC
	public static final byte NPC_TYPE_CAVE_DOOR = 20;

	public static final byte NPC_TYPE_FIREWALL = 21;
	// 采集NPC
	public static final byte NPC_TYPE_COLLECTION = 22;

	// 仙缘npc
	public static final byte NPC_TYPE_FATE = 23;
	/** 地表NPC */
	public static final byte NPC_TYPE_SURFACE = 24;
	/** 法宝 */
	public static final byte NPC_TYPE_MAGIC_WEAPON = 25;
	/** 丹炉NPC */
	public static final byte NPC_TYPE_FRUNACE = 27;
	/** 宝箱争夺中的宝箱 */
	public static final byte NPC_TYPE_CHESTFIGHT = 28;
	/** 金猴天灾火圈NPC */
	public static final byte NPC_TYPE_DISASTERFIRE = 29;

	@Override
	public void setState(byte value) {
		if (state == LivingObject.STATE_DEAD && hp <= 0) {

		} else {
			super.setState(value);
		}
	}

	public Sprite() {
		this.direction = DOWN;
		if(objectColor <= 1){
			this.objectColor = 0xFFFFFF;
		}
		this.particleName = "";
		this.footParticleName = "";
		this.objectScale = 1000;
	}

	/**
	 * //普通的NPC= 0;
	 * //银子商店NPC= 1;
	 * //绑银商店NPC= 2;
	 * //工资商店NPC= 3;
	 * //仓库NPC= 4;
	 * //大夫= 5;
	 * //修理大师= 6;
	 * //车夫= 7;
	 * //驿站，拉镖用= 8;
	 * //采集NPC= 9;
	 * //门 NPC= 10;
	 * //战场NPC= 11;
	 * //拾取掉落物品= 12;
	 * //活动= 13;
	 * //洞府= 14;
	 * //刺探= 15;
	 * //守卫= 16;
	 * //护送NPC= 17;
	 * //镖车NPC= 18;
	 * //地物NPC= 19;
	 * //CAVEDOORNPC= 20;
	 */
	public static String 普 = "普";
	public static String 商 = "商";
	public static String 仓 = "仓";
	public static String 医 = "医";
	public static String 修 = "修";
	public static String 武 = "武";
	public static String 车 = "车";
	public static String 卫 = "卫";
	public static String 地 = "地";
	public static String 银 = "银";
	public static String 绑 = "绑";
	public static String 工 = "工";
	public static String 驿 = "驿";
	public static String 采 = "采";
	public static String 门 = "门";
	public static String 战 = "战";
	public static String 拾 = "拾";
	public static String 活 = "活";
	public static String 洞 = "洞";
	public static String 刺 = "刺";
	public static String 守 = "守";
	public static String 护 = "护";
	public static String 镖 = "镖";
	public static final String NPC_SHORT_NAMES[] = new String[] { Translate.普, Translate.银, Translate.绑, Translate.工, Translate.仓, Translate.医, Translate.修, Translate.车, Translate.驿, Translate.采, Translate.门, Translate.战, Translate.拾, Translate.活, Translate.洞, Translate.刺, Translate.守, Translate.护, Translate.镖, Translate.地, Translate.门, "", "", "" };
	public static final String NPC_ICONS[] = new String[] { "", "yuanbaoshangcheng_L", "jinbishangchang_L", "shangchang_L", "cangku_s", "cangku_s", "xiuli_L", "chefu_L", "yizhan_L", "", "", "", "", "huodong_L", "dongfu_L", "citan_L", "", "", "", "", "", "", "", "" };
	public static String extra_icon = "danlu";

	public static String getNpcIcon(int npcType) {
		if (npcType >= 0 && npcType < NPC_ICONS.length) {
			return NPC_ICONS[npcType];
		} else if (npcType == NPC_TYPE_FRUNACE) {
			return extra_icon;
		}
		return "";
	}

	private static int id_seq = 1000;

	public static synchronized int nextId() {
		return id_seq++;
	}

	private static final long serialVersionUID = -1563275511842290358L;

	// protected static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	public static Random random = new Random(System.currentTimeMillis());

	public static final byte STATE_DEAD = 9;

	public String icon = "";

	// 刷新频率，表示此对应的怪消失后，多少毫秒内出现
	protected long flushFrequency = 5000L;

	// 死亡，躺在地上的时间
	protected long deadLastingTime = 1000L;

	// //////////////////////////////////////////////////////////////////////////////////
	// 以下是精灵的内部数据结构
	// /////////////////////////////////////////////////////////////////////////////////

	protected String gameName;

	// 出生点
	protected transient Point bornPoint = new Point(100, 100);

	protected int huDunDamage = 0;

	/**
	 * 杀死该精灵获得的声望的名称
	 */
	String reputationNameOnKillingSprite;

	int reputationValueOnKillingSprite;

	/**
	 * 提高玩家整体输出伤害比例
	 */
	private int skillDamageIncreaseRate = 0;

	/**
	 * 境界
	 */
	protected short classLevel;

	/**
	 * sprite可以显示穿装备后的样子，用；分隔
	 * 1为武器2装备3坐骑
	 * 如 大件;衣服;老虎
	 */
	transient String putOnEquipmentAvata = "";

	public String getPutOnEquipmentAvata() {
		return putOnEquipmentAvata;
	}

	public void setPutOnEquipmentAvata(String putOnEquipmentAvata) {
		this.putOnEquipmentAvata = putOnEquipmentAvata;
	}

	public short getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(short classLevel) {
		this.classLevel = classLevel;
	}

	public void setSkillDamageIncreaseRate(int r) {
		skillDamageIncreaseRate = r;
	}

	public int getSkillDamageIncreaseRate() {
		return skillDamageIncreaseRate;
	}

	/**
	 * 降低玩家整体输出伤害比例
	 */
	private int skillDamageDecreaseRate = 0;

	public void setSkillDamageDecreaseRate(int r) {
		skillDamageDecreaseRate = r;
	}

	public int getSkillDamageDecreaseRate() {
		return skillDamageDecreaseRate;
	}

	public String getReputationNameOnKillingSprite() {
		return reputationNameOnKillingSprite;
	}

	public void setReputationNameOnKillingSprite(String reputationNameOnKillingSprite) {
		this.reputationNameOnKillingSprite = reputationNameOnKillingSprite;
	}

	public int getReputationValueOnKillingSprite() {
		return reputationValueOnKillingSprite;
	}

	public void setReputationValueOnKillingSprite(int reputationValueOnKillingSprite) {
		this.reputationValueOnKillingSprite = reputationValueOnKillingSprite;
	}

	public ArrayList<Buff> getNewlyBuffs() {
		return new ArrayList<Buff>(0);
	}

	public ArrayList<Buff> getBuffs() {
		return new ArrayList<Buff>(0);
	}

	public ArrayList<Buff> getRemovedBuffs() {
		return new ArrayList<Buff>(0);
	}

	/**
	 * 护盾能吸收伤害的值
	 */
	public int getHuDunDamage() {
		return huDunDamage;
	}

	/**
	 * 设置护盾能吸收伤害的值
	 */
	public void setHuDunDamage(int hudun) {
		huDunDamage = hudun;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public void setMagicAttackA(int value) {
		this.magicAttackA = value;
		this.setMagicAttack((int) ((this.magicAttackA + magicAttackB) * 1l * (100 + this.magicAttackC) / 100));
	}

	@Override
	public void setMagicAttackB(int value) {
		this.magicAttackB = value;
		this.setMagicAttack((int) ((this.magicAttackA + magicAttackB) * 1l * (100 + this.magicAttackC) / 100));
	}

	@Override
	public void setMagicAttackC(int value) {
		this.magicAttackC = value;
		this.setMagicAttack((int) ((this.magicAttackA + magicAttackB) * 1l * (100 + this.magicAttackC) / 100));
	}

	@Override
	public void setMagicAttack(int value) {
		this.magicAttack = value;
	}

	@Override
	public void setPhyAttackA(int value) {
		this.phyAttackA = value;
		this.setPhyAttack((int) ((this.phyAttackA + phyAttackB) * 1l * (100 + this.phyAttackC) / 100));
	}

	@Override
	public void setPhyAttackB(int value) {
		this.phyAttackB = value;
		this.setPhyAttack((int) ((this.phyAttackA + phyAttackB) * 1l * (100 + this.phyAttackC) / 100));
	}

	@Override
	public void setPhyAttackC(int value) {
		this.phyAttackC = value;
		this.setPhyAttack((int) ((this.phyAttackA + phyAttackB) * 1l * (100 + this.phyAttackC) / 100));
	}

	@Override
	public void setPhyAttack(int value) {
		this.phyAttack = value;
	}

	@Override
	public void setCriticalHitA(int value) {
		// TODO Auto-generated method stub
		this.criticalHitA = value;
		this.setCriticalHit((int) ((criticalHitA + criticalHitB) * 1l * (100 + criticalHitC) / 100));
	}

	@Override
	public void setCriticalHitB(int value) {
		// TODO Auto-generated method stub
		this.criticalHitB = value;
		this.setCriticalHit((int) ((criticalHitA + criticalHitB) * 1l * (100 + criticalHitC) / 100));
	}

	@Override
	public void setCriticalHitC(int value) {
		// TODO Auto-generated method stub
		this.criticalHitC = value;
		this.setCriticalHit((int) ((criticalHitA + criticalHitB) * 1l * (100 + criticalHitC) / 100));
	}

	@Override
	public void setCriticalHit(int value) {
		this.criticalHit = value;
		int hitrate = CombatCaculator.CAL_会心一击率(value, level);
		this.setCriticalHitRate(hitrate);
	}

	@Override
	public void setDodgeA(int value) {
		// TODO Auto-generated method stub
		this.dodgeA = value;
		this.setDodge((int) ((dodgeA + dodgeB) * 1l * (100 + dodgeC + dodgeD) / 100));
	}

	@Override
	public void setDodgeB(int value) {
		// TODO Auto-generated method stub
		this.dodgeB = value;
		this.setDodge((int) ((dodgeA + dodgeB) * 1l * (100 + dodgeC + dodgeD) / 100));
	}

	@Override
	public void setDodgeC(int value) {
		// TODO Auto-generated method stub
		this.dodgeC = value;
		this.setDodge((int) ((dodgeA + dodgeB) * 1l * (100 + dodgeC + dodgeD) / 100));
	}

	@Override
	public void setDodgeD(int value) {
		// TODO Auto-generated method stub
		this.dodgeD = value;
		this.setDodge((int) ((dodgeA + dodgeB) * 1l * (100 + dodgeC + dodgeD) / 100));
	}

	@Override
	public void setDodge(int value) {
		this.dodge = value;
		int hitrate = CombatCaculator.CAL_闪避率(value, this.getLevel(), this.getCareer());
		this.setDodgeRate(hitrate);
	}

	@Override
	public void setHitA(int value) {
		// TODO Auto-generated method stub
		this.hitA = value;
		this.setHit((int) ((hitA + hitB) * 1l * (100 + hitC + hitD) / 100));
	}

	@Override
	public void setHitB(int value) {
		// TODO Auto-generated method stub
		this.hitB = value;
		this.setHit((int) ((hitA + hitB) * 1l * (100 + hitC + hitD) / 100));
	}

	@Override
	public void setHitC(int value) {
		// TODO Auto-generated method stub
		this.hitC = value;
		this.setHit((int) ((hitA + hitB) * 1l * (100 + hitC + hitD) / 100));
	}

	@Override
	public void setHitD(int value) {
		// TODO Auto-generated method stub
		this.hitD = value;
		this.setHit((int) ((hitA + hitB) * 1l * (100 + hitC + hitD) / 100));
	}

	@Override
	public void setHit(int value) {
		this.hit = value;
		int hitrate = CombatCaculator.CAL_命中率(value, level);
		this.setHitRate(hitrate);
	}

	@Override
	public void setMagicDefenceA(int value) {
		// TODO Auto-generated method stub
		this.magicDefenceA = value;
		this.setMagicDefence((int) ((magicDefenceA + magicDefenceB) * 1l * (100 + magicDefenceC + magicDefenceD) / 100));
	}

	@Override
	public void setMagicDefenceB(int value) {
		// TODO Auto-generated method stub
		this.magicDefenceB = value;
		this.setMagicDefence((int) ((magicDefenceA + magicDefenceB) * 1l * (100 + magicDefenceC + magicDefenceD) / 100));
	}

	@Override
	public void setMagicDefenceC(int value) {
		// TODO Auto-generated method stub
		this.magicDefenceC = value;
		this.setMagicDefence((int) ((magicDefenceA + magicDefenceB) * 1l * (100 + magicDefenceC + magicDefenceD) / 100));
	}

	@Override
	public void setMagicDefenceD(int value) {
		// TODO Auto-generated method stub
		this.magicDefenceD = value;
		this.setMagicDefence((int) ((magicDefenceA + magicDefenceB) * 1l * (100 + magicDefenceC + magicDefenceD) / 100));
	}

	@Override
	public void setMagicDefence(int value) {
		this.magicDefence = value;
	}

	@Override
	public void setMaxHPA(int value) {
		// TODO Auto-generated method stub
		this.maxHPA = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC) / 100));
	}

	@Override
	public void setMaxHPB(int value) {
		// TODO Auto-generated method stub
		this.maxHPB = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC) / 100));
	}

	@Override
	public void setMaxHPC(int value) {
		// TODO Auto-generated method stub
		this.maxHPC = value;
		this.setMaxHP((int) ((maxHPA + maxHPB) * 1l * (100 + maxHPC) / 100));
	}

	@Override
	public void setMaxHP(int value) {
		this.maxHP = value;
	}

	@Override
	public void setPhyDefenceA(int value) {
		// TODO Auto-generated method stub
		this.phyDefenceA = value;
		this.setPhyDefence((int) ((phyDefenceA + phyDefenceB) * 1l * (100 + phyDefenceC + phyDefenceD) / 100));
	}

	@Override
	public void setPhyDefenceB(int value) {
		// TODO Auto-generated method stub
		this.phyDefenceB = value;
		this.setPhyDefence((int) ((phyDefenceA + phyDefenceB) * 1l * (100 + phyDefenceC + phyDefenceD) / 100));
	}

	@Override
	public void setPhyDefenceC(int value) {
		// TODO Auto-generated method stub
		this.phyDefenceC = value;
		this.setPhyDefence((int) ((phyDefenceA + phyDefenceB) * 1l * (100 + phyDefenceC + phyDefenceD) / 100));
	}

	@Override
	public void setPhyDefenceD(int value) {
		// TODO Auto-generated method stub
		this.phyDefenceD = value;
		this.setPhyDefence((int) ((phyDefenceA + phyDefenceB) * 1l * (100 + phyDefenceC + phyDefenceD) / 100));
	}

	@Override
	public void setPhyDefence(int value) {
		this.phyDefence = value;
	}

	@Override
	public void setBlizzardDefenceA(int value) {
		// TODO Auto-generated method stub
		this.blizzardDefenceA = value;
		this.setBlizzardDefence((int) ((blizzardDefenceA + blizzardDefenceB) * 1l * (100 + blizzardDefenceC) / 100));
	}

	@Override
	public void setBlizzardDefenceB(int value) {
		// TODO Auto-generated method stub
		this.blizzardDefenceB = value;
		this.setBlizzardDefence((int) ((blizzardDefenceA + blizzardDefenceB) * 1l * (100 + blizzardDefenceC) / 100));
	}

	@Override
	public void setBlizzardDefenceC(int value) {
		// TODO Auto-generated method stub
		this.blizzardDefenceC = value;
		this.setBlizzardDefence((int) ((blizzardDefenceA + blizzardDefenceB) * 1l * (100 + blizzardDefenceC) / 100));
	}

	@Override
	public void setBlizzardDefence(int value) {
		this.blizzardDefence = value;
	}

	@Override
	public void setFireDefenceA(int value) {
		// TODO Auto-generated method stub
		this.fireDefenceA = value;
		this.setFireDefence((int) ((fireDefenceA + fireDefenceB) * 1l * (100 + fireDefenceC) / 100));
	}

	@Override
	public void setFireDefenceB(int value) {
		// TODO Auto-generated method stub
		this.fireDefenceB = value;
		this.setFireDefence((int) ((fireDefenceA + fireDefenceB) * 1l * (100 + fireDefenceC) / 100));
	}

	@Override
	public void setFireDefenceC(int value) {
		// TODO Auto-generated method stub
		this.fireDefenceC = value;
		this.setFireDefence((int) ((fireDefenceA + fireDefenceB) * 1l * (100 + fireDefenceC) / 100));
	}

	@Override
	public void setFireDefence(int value) {
		this.fireDefence = value;
	}

	@Override
	public void setThunderDefenceA(int value) {
		// TODO Auto-generated method stub
		this.thunderDefenceA = value;
		this.setThunderDefence((int) ((thunderDefenceA + thunderDefenceB) * 1l * (100 + thunderDefenceC) / 100));
	}

	@Override
	public void setThunderDefenceB(int value) {
		// TODO Auto-generated method stub
		this.thunderDefenceB = value;
		this.setThunderDefence((int) ((thunderDefenceA + thunderDefenceB) * 1l * (100 + thunderDefenceC) / 100));
	}

	@Override
	public void setThunderDefenceC(int value) {
		// TODO Auto-generated method stub
		this.thunderDefenceC = value;
		this.setThunderDefence((int) ((thunderDefenceA + thunderDefenceB) * 1l * (100 + thunderDefenceC) / 100));
	}

	@Override
	public void setThunderDefence(int value) {
		this.thunderDefence = value;
	}

	@Override
	public void setWindDefenceA(int value) {
		// TODO Auto-generated method stub
		this.windDefenceA = value;
		this.setWindDefence((int) ((windDefenceA + windDefenceB) * 1l * (100 + windDefenceC) / 100));
	}

	@Override
	public void setWindDefenceB(int value) {
		// TODO Auto-generated method stub
		this.windDefenceB = value;
		this.setWindDefence((int) ((windDefenceA + windDefenceB) * 1l * (100 + windDefenceC) / 100));
	}

	@Override
	public void setWindDefenceC(int value) {
		// TODO Auto-generated method stub
		this.windDefenceC = value;
		this.setWindDefence((int) ((windDefenceA + windDefenceB) * 1l * (100 + windDefenceC) / 100));
	}

	@Override
	public void setWindDefence(int value) {
		this.windDefence = value;
	}

	@Override
	public void setAccurate(int value) {
		// TODO Auto-generated method stub
		super.setAccurate(value);
	}

	@Override
	public void setAccurateA(int value) {
		// TODO Auto-generated method stub
		super.setAccurateA(value);
		this.setAccurate((int) ((accurateA + accurateB) * 1l * (100 + accurateC) / 100));
	}

	@Override
	public void setAccurateB(int value) {
		// TODO Auto-generated method stub
		super.setAccurateB(value);
		this.setAccurate((int) ((accurateA + accurateB) * 1l * (100 + accurateC) / 100));
	}

	@Override
	public void setAccurateC(int value) {
		// TODO Auto-generated method stub
		super.setAccurateC(value);
		this.setAccurate((int) ((accurateA + accurateB) * 1l * (100 + accurateC) / 100));
	}

	@Override
	public void setBlizzardAttack(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardAttack(value);
	}

	@Override
	public void setBlizzardAttackA(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardAttackA(value);
		this.setBlizzardAttack((int) ((blizzardAttackA + blizzardAttackB) * 1l * (100 + blizzardAttackC) / 100));
	}

	@Override
	public void setBlizzardAttackB(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardAttackB(value);
		this.setBlizzardAttack((int) ((blizzardAttackA + blizzardAttackB) * 1l * (100 + blizzardAttackC) / 100));
	}

	@Override
	public void setBlizzardAttackC(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardAttackC(value);
		this.setBlizzardAttack((int) ((blizzardAttackA + blizzardAttackB) * 1l * (100 + blizzardAttackC) / 100));
	}

	@Override
	public void setBlizzardIgnoreDefence(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardIgnoreDefence(value);
	}

	@Override
	public void setBlizzardIgnoreDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardIgnoreDefenceA(value);
		this.setBlizzardIgnoreDefence((int) ((blizzardIgnoreDefenceA + blizzardIgnoreDefenceB) * 1l * (100 + blizzardIgnoreDefenceC) / 100));
	}

	@Override
	public void setBlizzardIgnoreDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardIgnoreDefenceB(value);
		this.setBlizzardIgnoreDefence((int) ((blizzardIgnoreDefenceA + blizzardIgnoreDefenceB) * 1l * (100 + blizzardIgnoreDefenceC) / 100));
	}

	@Override
	public void setBlizzardIgnoreDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setBlizzardIgnoreDefenceC(value);
		this.setBlizzardIgnoreDefence((int) ((blizzardIgnoreDefenceA + blizzardIgnoreDefenceB) * 1l * (100 + blizzardIgnoreDefenceC) / 100));
	}

	@Override
	public void setBreakDefence(int value) {
		// TODO Auto-generated method stub
		super.setBreakDefence(value);
	}

	@Override
	public void setBreakDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setBreakDefenceA(value);
		this.setBreakDefence((int) ((breakDefenceA + breakDefenceB) * 1l * (100 + breakDefenceC) / 100));
	}

	@Override
	public void setBreakDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setBreakDefenceB(value);
		this.setBreakDefence((int) ((breakDefenceA + breakDefenceB) * 1l * (100 + breakDefenceC) / 100));
	}

	@Override
	public void setBreakDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setBreakDefenceC(value);
		this.setBreakDefence((int) ((breakDefenceA + breakDefenceB) * 1l * (100 + breakDefenceC) / 100));
	}

	@Override
	public void setCriticalDefence(int value) {
		// TODO Auto-generated method stub
		super.setCriticalDefence(value);
	}

	@Override
	public void setCriticalDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setCriticalDefenceA(value);
		this.setCriticalDefence((int) ((criticalDefenceA + criticalDefenceB) * 1l * (100 + criticalDefenceC) / 100));
	}

	@Override
	public void setCriticalDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setCriticalDefenceB(value);
		this.setCriticalDefence((int) ((criticalDefenceA + criticalDefenceB) * 1l * (100 + criticalDefenceC) / 100));
	}

	@Override
	public void setCriticalDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setCriticalDefenceC(value);
		this.setCriticalDefence((int) ((criticalDefenceA + criticalDefenceB) * 1l * (100 + criticalDefenceC) / 100));
	}

	@Override
	public void setFireAttackA(int value) {
		// TODO Auto-generated method stub
		super.setFireAttackA(value);
		this.setFireAttack((int) ((fireAttackA + fireAttackB) * 1l * (100 + fireAttackC) / 100));
	}

	@Override
	public void setFireAttackB(int value) {
		// TODO Auto-generated method stub
		super.setFireAttackB(value);
		this.setFireAttack((int) ((fireAttackA + fireAttackB) * 1l * (100 + fireAttackC) / 100));
	}

	@Override
	public void setFireAttackC(int value) {
		// TODO Auto-generated method stub
		super.setFireAttackC(value);
		this.setFireAttack((int) ((fireAttackA + fireAttackB) * 1l * (100 + fireAttackC) / 100));
	}

	@Override
	public void setFireIgnoreDefence(int value) {
		// TODO Auto-generated method stub
		super.setFireIgnoreDefence(value);
	}

	@Override
	public void setFireIgnoreDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setFireIgnoreDefenceA(value);
		this.setFireIgnoreDefence((int) ((fireIgnoreDefenceA + fireIgnoreDefenceB) * 1l * (100 + fireIgnoreDefenceC) / 100));
	}

	@Override
	public void setFireIgnoreDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setFireIgnoreDefenceB(value);
		this.setFireIgnoreDefence((int) ((fireIgnoreDefenceA + fireIgnoreDefenceB) * 1l * (100 + fireIgnoreDefenceC) / 100));
	}

	@Override
	public void setFireIgnoreDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setFireIgnoreDefenceC(value);
		this.setFireIgnoreDefence((int) ((fireIgnoreDefenceA + fireIgnoreDefenceB) * 1l * (100 + fireIgnoreDefenceC) / 100));
	}

	@Override
	public void setMaxMP(int value) {
		// TODO Auto-generated method stub
		super.setMaxMP(value);
	}

	@Override
	public void setMaxMPA(int value) {
		// TODO Auto-generated method stub
		super.setMaxMPA(value);
		this.setMaxMP((int) ((maxMPA + maxMPB) * 1l * (100 + maxMPC) / 100));
	}

	@Override
	public void setMaxMPB(int value) {
		// TODO Auto-generated method stub
		super.setMaxMPB(value);
		this.setMaxMP((int) ((maxMPA + maxMPB) * 1l * (100 + maxMPC) / 100));
	}

	@Override
	public void setMaxMPC(int value) {
		// TODO Auto-generated method stub
		super.setMaxMPC(value);
		this.setMaxMP((int) ((maxMPA + maxMPB) * 1l * (100 + maxMPC) / 100));
	}

	@Override
	public void setThunderAttack(int value) {
		// TODO Auto-generated method stub
		super.setThunderAttack(value);
	}

	@Override
	public void setThunderAttackA(int value) {
		// TODO Auto-generated method stub
		super.setThunderAttackA(value);
		this.setThunderAttack((int) ((thunderAttackA + thunderAttackB) * 1l * (100 + thunderAttackC) / 100));
	}

	@Override
	public void setThunderAttackB(int value) {
		// TODO Auto-generated method stub
		super.setThunderAttackB(value);
		this.setThunderAttack((int) ((thunderAttackA + thunderAttackB) * 1l * (100 + thunderAttackC) / 100));
	}

	@Override
	public void setThunderAttackC(int value) {
		// TODO Auto-generated method stub
		super.setThunderAttackC(value);
		this.setThunderAttack((int) ((thunderAttackA + thunderAttackB) * 1l * (100 + thunderAttackC) / 100));
	}

	@Override
	public void setThunderIgnoreDefence(int value) {
		// TODO Auto-generated method stub
		super.setThunderIgnoreDefence(value);
	}

	@Override
	public void setThunderIgnoreDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setThunderIgnoreDefenceA(value);
		this.setThunderIgnoreDefence((int) ((thunderIgnoreDefenceA + thunderIgnoreDefenceB) * 1l * (100 + thunderIgnoreDefenceC) / 100));
	}

	@Override
	public void setThunderIgnoreDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setThunderIgnoreDefenceB(value);
		this.setThunderIgnoreDefence((int) ((thunderIgnoreDefenceA + thunderIgnoreDefenceB) * 1l * (100 + thunderIgnoreDefenceC) / 100));
	}

	@Override
	public void setThunderIgnoreDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setThunderIgnoreDefenceC(value);
		this.setThunderIgnoreDefence((int) ((thunderIgnoreDefenceA + thunderIgnoreDefenceB) * 1l * (100 + thunderIgnoreDefenceC) / 100));
	}

	@Override
	public void setWindAttack(int value) {
		// TODO Auto-generated method stub
		super.setWindAttack(value);
	}

	@Override
	public void setWindAttackA(int value) {
		// TODO Auto-generated method stub
		super.setWindAttackA(value);
		this.setWindAttack((int) ((windAttackA + windAttackB) * 1l * (100 + windAttackC) / 100));
	}

	@Override
	public void setWindAttackB(int value) {
		// TODO Auto-generated method stub
		super.setWindAttackB(value);
		this.setWindAttack((int) ((windAttackA + windAttackB) * 1l * (100 + windAttackC) / 100));
	}

	@Override
	public void setWindAttackC(int value) {
		// TODO Auto-generated method stub
		super.setWindAttackC(value);
		this.setWindAttack((int) ((windAttackA + windAttackB) * 1l * (100 + windAttackC) / 100));
	}

	@Override
	public void setWindIgnoreDefence(int value) {
		// TODO Auto-generated method stub
		super.setWindIgnoreDefence(value);
	}

	@Override
	public void setWindIgnoreDefenceA(int value) {
		// TODO Auto-generated method stub
		super.setWindIgnoreDefenceA(value);
		this.setWindIgnoreDefence((int) ((windIgnoreDefenceA + windIgnoreDefenceB) * 1l * (100 + windIgnoreDefenceC) / 100));
	}

	@Override
	public void setWindIgnoreDefenceB(int value) {
		// TODO Auto-generated method stub
		super.setWindIgnoreDefenceB(value);
		this.setWindIgnoreDefence((int) ((windIgnoreDefenceA + windIgnoreDefenceB) * 1l * (100 + windIgnoreDefenceC) / 100));
	}

	@Override
	public void setWindIgnoreDefenceC(int value) {
		// TODO Auto-generated method stub
		super.setWindIgnoreDefenceC(value);
		this.setWindIgnoreDefence((int) ((windIgnoreDefenceA + windIgnoreDefenceB) * 1l * (100 + windIgnoreDefenceC) / 100));
	}

	@Override
	public void setHpRecoverBase(int value) {
		// TODO Auto-generated method stub
		super.setHpRecoverBase(value);
	}

	@Override
	public void setHpRecoverBaseA(int value) {
		// TODO Auto-generated method stub
		super.setHpRecoverBaseA(value);
		this.setHpRecoverBase(hpRecoverBaseA + hpRecoverBaseB);
	}

	@Override
	public void setHpRecoverBaseB(int value) {
		// TODO Auto-generated method stub
		super.setHpRecoverBaseB(value);
		this.setHpRecoverBase(hpRecoverBaseA + hpRecoverBaseB);
	}

	@Override
	public void setSpeed(int value) {
		// TODO Auto-generated method stub
		super.setSpeed(value);
		if (path != null) {
			path.speedChanged(speed, 2000L);
		}
	}

	@Override
	public void setSpeedA(int value) {
		// TODO Auto-generated method stub
		super.setSpeedA(value);
		this.setSpeed(((speedA) * (100 + speedC) / 100));
	}

	@Override
	public void setSpeedC(int value) {
		// TODO Auto-generated method stub
		super.setSpeedC(value);
		this.setSpeed(((speedA) * (100 + speedC) / 100));
	}

	/**
	 * 从模板中拷贝基础的属性
	 * 
	 * @param template
	 */
	public void cloneAllInitNumericalProperty(Sprite template) {

		// this.setAnimationType(template.getAnimationType());
		this.setMaxHP(template.getMaxHP());
		this.setName(template.getName());
		this.setWeaponDamage(template.getWeaponDamage());
		this.setTitle(template.getTitle() + "");
		this.setAura(template.getAura());
		this.icon = template.icon;
		this.setCommonAttackRange(template.getCommonAttackRange());
		this.setCommonAttackSpeed(template.getCommonAttackSpeed());
		this.setBornPoint(template.getBornPoint());
		this.setDeadLastingTime(template.getDeadLastingTime());
		this.setDialogContent(template.getDialogContent() + "");
		this.setAvataRace(template.getAvataRace());
		this.setAvataSex(template.getAvataSex());
		this.setPutOnEquipmentAvata(template.getPutOnEquipmentAvata());
		this.setFlushFrequency(template.getFlushFrequency());
		this.setGameName(template.getGameName());
		this.setLevel(template.getLevel());
		this.setSpriteType(template.getSpriteType());
		this.setNpcType(template.getNpcType());
		this.setClassLevel(template.getClassLevel());
		// this.setType(template.getType());
		if (template.getAvata() != null) this.setAvata(template.getAvata().clone());

		if (template.getAvataType() != null) this.setAvataType(template.getAvataType().clone());

		// this.setStyle(template.getStyle());
		this.setParticleName(template.getParticleName());
		this.setParticleX(template.getParticleX());
		this.setParticleY(template.getParticleY());
		this.setFootParticleName(template.getFootParticleName());
		this.setFootParticleX(template.getFootParticleX());
		this.setFootParticleY(template.getFootParticleY());
		this.setObjectScale(template.getObjectScale());
		this.setDirection(template.getDirection());
		this.setShield(template.getShield());
		this.setHeight(template.getHeight());
		this.setMonsterType(template.getMonsterType());
		this.setSpeedA(template.getSpeedA());
		this.setTaskMark(template.taskMark);
		this.setViewHeight(template.getViewHeight());
		this.setViewWidth(template.getViewWidth());
		this.setReputationNameOnKillingSprite(template.getReputationNameOnKillingSprite());
		this.setReputationValueOnKillingSprite(template.getReputationValueOnKillingSprite());

		this.setPhyAttackA(template.getPhyAttackA());
		this.setPhyDefenceA(template.getPhyDefenceA());
		this.setMagicAttackA(template.getMagicAttackA());
		this.setMagicDefenceA(template.getMagicDefenceA());

	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init() {
		this.viewHeight = 480;
		this.viewWidth = 320;
		this.hp = this.maxHP;
		this.x = this.bornPoint.x;
		this.y = this.bornPoint.y;
		this.aura = -1;
		this.state = Sprite.STATE_STAND;

		this.stun = false;
		this.hold = false;
		this.immunity = false;
		this.invulnerable = false;
		this.poison = false;
		this.weak = false;

		if (this.avataType == null) {
			this.avataType = new byte[0];
		}
		ResourceManager rm = ResourceManager.getInstance();
		rm.getAvata(this);
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
		return 0;
	}

	public Point getBornPoint() {
		return bornPoint;
	}

	public void setBornPoint(Point bornPoint) {
		this.bornPoint = bornPoint;
	}

	/**
	 * 更新仇恨列表
	 * @param caster
	 * @param damage
	 * @param hatred
	 */
	protected void updateDamageRecord(Fighter caster, int damage, int hatred) {
	}

	/**
	 * 加生命值，由caster对自己加生命值
	 */
	public void enrichHP(Fighter caster, int hp, boolean baoji) {

		int hp2 = this.getHp() + hp;
		if (hp2 > this.getMaxHP()) {
			this.setHp(this.getMaxHP());
		} else {
			this.setHp(hp2);
		}
	}

	public void setHp(int value) {

		super.setHp(value);
	}

	/**
	 * 给目标加生命值，有自己对别人加生命值
	 */
	public void enrichHPFeedback(Fighter target, int hp, boolean baoji) {
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

		if (caster.getFightingType(this) != Fighter.FIGHTING_TYPE_ENEMY) {
			return;
		}

		// 增加敌人
		// if (this.isDeath() == false && caster instanceof LivingObject) this.enemyList.add(caster, false, this.heartBeatStartTime);

		int ddd = damage;
		if (ddd > hp) {
			ddd = hp;
		}

		if ((caster instanceof Player) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Player p = (Player) caster;

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
					}
					Skill.logger.debug("Monster.causeDamage: 宠物吸血 {}",p.getName());
					// 加血，是否要通知客户端？debug dot

					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					// .getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);
				} else {
					// debug dot
					// NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p
					// .getId(), (byte) Event.HP_INCREASED, p.getHpStealPercent() * ddd / 100);
					// p.addMessageToRightBag(req);
				}
			}
		}
		if ((caster instanceof Pet) && damageType != Fighter.DAMAGETYPE_DODGE && damageType != Fighter.DAMAGETYPE_MIANYI && damageType != Fighter.DAMAGETYPE_MISS && damageType != Fighter.DAMAGETYPE_FANSHANG && damageType != Fighter.DAMAGETYPE_ZHAONGDU && damageType != Fighter.DAMAGETYPE_XISHOU) {
			Pet p = (Pet)caster;
			if (p.getState() != Player.STATE_DEAD && p.getHpStealPercent() > 0) {
				if (p.getHp() < p.getMaxHP()) {
					int mp = p.getHp() + p.getHpStealPercent() * ddd / 100;
					if (mp > p.getMaxHP()) mp = p.getMaxHP();
					p.setHp(mp);

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
			if (hp > damage) {
				setHp(hp - damage);
			} else {
				setHp(0);
			}

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

	}

	public byte getClassType() {
		return 1;
	}

	public boolean isSameTeam(Fighter fighter) {
		return false;
	}

	public int getMp() {
		return Integer.MAX_VALUE;
	}

	/**
	 * 停止移动，并且通知其他客户端
	 * 
	 * @param reason
	 * @param description
	 */
	public void stopAndNotifyOthers() {
		if (this instanceof Monster) {
			Monster m = (Monster) this;
		}
		SET_POSITION_REQ req = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte) Game.REASON_CLIENT_STOP, getClassType(), id, (short) x, (short) y);

		// 通知广播区里的其他玩家
		MoveTrace path = getMoveTrace();
		if (path != null) {
			Collection<LivingObject> livings = path.getLivingNotifySet();
			if (livings != null) {
				try {
					for (LivingObject living : livings) {
						if (living instanceof Player) {
							Player p = (Player) living;
							if(p.getActiveMagicWeaponId() == this.getId()) {
								continue;
							}
							((Player) living).addMessageToRightBag(req, Translate.text_5873);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			removeMoveTrace();
		}
	}

	public Buff getBuff(byte buffType) {
		return null;
	}

	/**
	 * 种植一个buff到玩家或者怪的身上，
	 * 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff) {
	}

	/**
	 * 通过buff的templateId获得buff
	 * @return
	 */
	public Buff getBuff(int templateId) {
		return null;
	}

	/**
	 * 心跳处理的顺序
	 * 
	 * 1. 先处理父类的心跳
	 * 2. 处理是否被打死，如果是调用killed方法，同时设置state为STATE_DEAD状态
	 * 3. 判断是否已经超过死亡过程设定的时间，如果是，标记此怪不再存活，设置alive标记
	 * 
	 */
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	public void setGameNames(GameInfo gi) {
		setGameName(gi.name);
		setGameCNName(gi.displayName);
	}

	public String getGameName() {
		return gameName;
	}

	private void setGameName(String gameName) {
		this.gameName = gameName;
	}

	private String gameCNName;

	public String getGameCNName() {
		return gameCNName;
	}

	private void setGameCNName(String gameCNName) {
		this.gameCNName = gameCNName;
	}

	/**
	 * 通知此对象，我准备攻击a
	 * @param a
	 */
	public void notifyPrepareToFighting(Fighter a) {
		// 增加敌人
		// enemyList.add(a, true,this.heartBeatStartTime);
	}

	/**
	 * 通知此对象，a准备攻击我
	 * 当a为怪的时候，怪将我作为敌人的时候就通知
	 * 当a为玩家的时候，服务器收到有目标攻击指令时通知
	 * @param a
	 */
	public void notifyPrepareToBeFighted(Fighter a) {
		// 增加敌人
		// enemyList.add(a, false,this.heartBeatStartTime);
	}

	/**
	 * 通知此对象，a不再攻击我。
	 * 当a为怪的时候，a不在把我作为攻击目标时通知
	 * 当a为玩家的时候，我从玩家的广播区域里消失，通知
	 * @param a
	 */
	public void notifyEndToBeFighted(Fighter a) {
		// enemyList.passiveBreak(a);
	}

	/**
	 * 通知此对象，a从我的广播区域里消失。
	 * 此时，将a从我的敌人列表中清除
	 * @param a
	 */
	public void notifyEndToFighting(Fighter a) {
		// enemyList.activeBreak(a);
	}

	public long getFlushFrequency() {
		return flushFrequency;
	}

	public void setFlushFrequency(long flushFrequency) {
		this.flushFrequency = flushFrequency;
	}

	public long getDeadLastingTime() {
		return deadLastingTime;
	}

	public void setDeadLastingTime(long deadLastingTime) {
		this.deadLastingTime = deadLastingTime;
	}

	@Override
	public void setMp(int mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canFreeFromBeDamaged(Fighter fighter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIceState() {
		return false;
	}
	
	@Override
	public int getCritFactor() {
		return 200;
	}
	/**
	 * 刷新数据,用于回调时候做特殊处理
	 */
	private transient RefreshSpriteData refreshSpriteData;

	public RefreshSpriteData getRefreshSpriteData() {
		return refreshSpriteData;
	}

	public void setRefreshSpriteData(RefreshSpriteData refreshSpriteData) {
		this.refreshSpriteData = refreshSpriteData;
	}
	
	/** 中兽魁毒标记，通过此标记有些技能需要特殊处理   key=playerId */
	private transient Map<Long, Byte> duFlag = new Hashtable<Long, Byte>();

	@Override
	public byte getSpriteStatus(Fighter caster) {
		// TODO Auto-generated method stub
		if (!(caster instanceof Player) || !duFlag.containsKey(caster.getId())) {
			return (byte)0;
		}
		return duFlag.get(caster.getId());
	}
	
	public void changeDuFlag(Player player, byte flag) {
		if (flag <= 0) {
			duFlag.remove(player.getId());
		} else {
			duFlag.put(player.getId(), flag);
		}
	}

}
