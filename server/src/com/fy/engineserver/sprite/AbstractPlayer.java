package com.fy.engineserver.sprite;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;

public abstract class AbstractPlayer extends com.fy.engineserver.core.LivingObject implements
		Fighter {
	// 角色的名字

	protected String name = "";

	public void setName(String value) {
		this.name = value;
		aroundMarks[71] = true;
		selfMarks[141] = true;
	}

	public String getName() {
		return this.name;
	}

	// 性别

	protected byte sex;

	public void setSex(byte value) {
		this.sex = value;
	}

	public byte getSex() {
		return this.sex;
	}

	// 国家0中立

	protected byte country;

	public void setCountry(byte value) {
		this.country = value;
		aroundMarks[0] = true;
	}

	public byte getCountry() {
		return this.country;
	}

	// 职位

	protected transient int countryPosition;

	public void setCountryPosition(int value) {
		this.countryPosition = value;
		aroundMarks[1] = true;
	}

	public int getCountryPosition() {
		return this.countryPosition;
	}

	// 角色的门派或职业

	protected byte career;

	public void setCareer(byte value) {
		this.career = value;
		aroundMarks[2] = true;
	}

	public byte getCareer() {
		return this.career;
	}

	// 门派贡献度

	protected int careerContribution;

	public void setCareerContribution(int value) {
		this.careerContribution = value;
		selfMarks[0] = true;
	}

	public int getCareerContribution() {
		return this.careerContribution;
	}

	// 等级
	@SimpleColumn(name = "playerLevel")
	protected int level;

	public void setLevel(int value) {
		this.level = value;
		aroundMarks[3] = true;
	}

	public int getLevel() {
		return this.level;
	}

	// 元神等级

	protected int soulLevel;

	public void setSoulLevel(int value) {
		this.soulLevel = value;
		aroundMarks[4] = true;
	}

	public int getSoulLevel() {
		return this.soulLevel;
	}

	// 角色自身的元宝，不绑定，不在不同角色间共享

	protected long rmbyuanbao;

	public void setRmbyuanbao(long value) {
		this.rmbyuanbao = value;
	}

	public long getRmbyuanbao() {
		return this.rmbyuanbao;
	}

	// 玩家充值的人民币
	@SimpleColumn(saveInterval = 1)
	protected long RMB;

	public void setRMB(long value) {
		this.RMB = value;
		selfMarks[1] = true;
	}

	public long getRMB() {
		return this.RMB;
	}

	// vip等级

	protected transient byte vipLevel;

	public void setVipLevel(byte value) {
		this.vipLevel = value;
		aroundMarks[5] = true;
	}

	public byte getVipLevel() {
		return this.vipLevel;
	}

	// 角色的全部元宝，包括账号充值得到的元宝

	protected transient long totalRmbyuanbao;

	public void setTotalRmbyuanbao(long value) {
		this.totalRmbyuanbao = value;
		selfMarks[2] = true;
	}

	public long getTotalRmbyuanbao() {
		return this.totalRmbyuanbao;
	}

	// 绑定银子

	protected long bindSilver;

	public void setBindSilver(long value) {
		this.bindSilver = value;
		selfMarks[3] = true;
	}

	public long getBindSilver() {
		return this.bindSilver;
	}

	// 银子
	@SimpleColumn(saveInterval = 1)
	protected long silver;

	public void setSilver(long value) {
		this.silver = value;
		selfMarks[4] = true;
	}

	public long getSilver() {
		return this.silver;
	}

	// 今天使用的绑银数

	protected long todayUsedBindSilver;

	public void setTodayUsedBindSilver(long value) {
		this.todayUsedBindSilver = value;
		selfMarks[5] = true;
	}

	public long getTodayUsedBindSilver() {
		return this.todayUsedBindSilver;
	}

	// 工资

	protected long wage;

	public void setWage(long value) {
		this.wage = value;
		selfMarks[6] = true;
	}

	public long getWage() {
		return this.wage;
	}

	// 体力

	protected int vitality;

	public void setVitality(int value) {
		this.vitality = value;
		selfMarks[7] = true;
		aroundMarks[72] = true;
	}

	public int getVitality() {
		return this.vitality;
	}

	// 最大体力数值

	protected transient int maxVitality;

	public void setMaxVitality(int value) {
		this.maxVitality = value;
		selfMarks[8] = true;
		aroundMarks[73] = true;
	}

	public int getMaxVitality() {
		return this.maxVitality;
	}

	// 元气

	protected int energy;

	public void setEnergy(int value) {
		this.energy = value;
		selfMarks[9] = true;
	}

	public int getEnergy() {
		return this.energy;
	}

	// 功勋

	protected long gongxun;

	public void setGongxun(long value) {
		this.gongxun = value;
		selfMarks[10] = true;
	}

	public long getGongxun() {
		return this.gongxun;
	}

	// 摆摊状态，true为摆摊

	protected transient boolean boothState;

	public void setBoothState(boolean value) {
		this.boothState = value;
		aroundMarks[6] = true;
	}

	public boolean isBoothState() {
		return this.boothState;
	}

	// 摆摊摊位名称

	protected transient String boothName = "";

	public void setBoothName(String value) {
		this.boothName = value;
		aroundMarks[7] = true;
	}

	public String getBoothName() {
		return this.boothName;
	}

	// 当前经验值

	protected long exp;

	public void setExp(long value) {
		this.exp = value;
		selfMarks[11] = true;
	}

	public long getExp() {
		return this.exp;
	}

	// 升级所需经验值

	protected transient long nextLevelExp;

	public void setNextLevelExp(long value) {
		this.nextLevelExp = value;
		selfMarks[12] = true;
	}

	public long getNextLevelExp() {
		return this.nextLevelExp;
	}

	// 经验池能容纳经验的总量

	protected transient long nextLevelExpPool;

	public void setNextLevelExpPool(long value) {
		this.nextLevelExpPool = value;
		selfMarks[13] = true;
	}

	public long getNextLevelExpPool() {
		return this.nextLevelExpPool;
	}

	// 武器类型，包括空手。可以用于技能的判断条件

	protected transient byte weaponType;

	public void setWeaponType(byte value) {
		this.weaponType = value;
		aroundMarks[8] = true;
	}

	public byte getWeaponType() {
		return this.weaponType;
	}

	// 角色当前的状态，如站立、行走等

	protected transient byte state;

	public void setState(byte value) {
		this.state = value;
	}

	public byte getState() {
		return this.state;
	}

	// 角色面朝的方向，如上下左右等

	protected transient byte direction;

	public void setDirection(byte value) {
		this.direction = value;
	}

	public byte getDirection() {
		return this.direction;
	}

	// avata部件类型，比如:武器

	protected transient byte[] avataType;

	public void setAvataType(byte[] value) {
		this.avataType = value;
		aroundMarks[9] = true;
	}

	public void synchAvata() {
		aroundMarks[9] = true;
		aroundMarks[10] = true;
	}

	public byte[] getAvataType() {
		return this.avataType;
	}

	// avata部件全名，比如:明月刀_人类_男

	protected transient String[] avata;

	public void setAvata(String[] value) {
		this.avata = value;
		aroundMarks[10] = true;
	}

	public String[] getAvata() {
		return this.avata;
	}

	// 时装道具id

	protected long avataPropsId;

	public void setAvataPropsId(long value) {
		this.avataPropsId = value;
		aroundMarks[11] = true;
	}

	public long getAvataPropsId() {
		return this.avataPropsId;
	}

	// 阴影层的光环，受光环辅助技能影响，默认值-1

	protected transient byte aura;

	public void setAura(byte value) {
		this.aura = value;
		aroundMarks[12] = true;
	}

	public byte getAura() {
		return this.aura;
	}

	// 角色周身的外罩，一种透明的光影效果

	protected transient byte encloser;

	public void setEncloser(byte value) {
		this.encloser = value;
		aroundMarks[13] = true;
	}

	public byte getEncloser() {
		return this.encloser;
	}

	// 标记有马无马，0-无马，1-有马

	protected transient boolean isUpOrDown;

	public void setIsUpOrDown(boolean value) {
		this.isUpOrDown = value;
		aroundMarks[14] = true;
	}

	public boolean isIsUpOrDown() {
		return this.isUpOrDown;
	}

	// 标记玩家是否在飞行状态

	protected transient boolean flying;

	public void setFlying(boolean value) {
		this.flying = value;
		aroundMarks[15] = true;
	}

	public boolean isFlying() {
		return this.flying;
	}

	// 普通攻击的攻击速度（毫秒为单位）

	protected transient int commonAttackSpeed;

	public void setCommonAttackSpeed(int value) {
		this.commonAttackSpeed = value;
		selfMarks[14] = true;
	}

	public int getCommonAttackSpeed() {
		return this.commonAttackSpeed;
	}

	// 普通攻击的攻击距离（像素为单位）

	protected transient int commonAttackRange;

	public void setCommonAttackRange(int value) {
		this.commonAttackRange = value;
		selfMarks[15] = true;
	}

	public int getCommonAttackRange() {
		return this.commonAttackRange;
	}

	// 定身状态，不能移动

	protected transient boolean hold;

	public void setHold(boolean value) {
		this.hold = value;
		aroundMarks[16] = true;
	}

	public boolean isHold() {
		return this.hold;
	}

	// 眩晕状态，不能移动，不能使用技能和道具（特定技能除外）

	protected transient boolean stun;

	public void setStun(boolean value) {
		this.stun = value;
		aroundMarks[17] = true;
	}

	public boolean isStun() {
		return this.stun;
	}

	// 免疫状态，不受负面buff影响

	protected transient boolean immunity;

	public void setImmunity(boolean value) {
		this.immunity = value;
		aroundMarks[18] = true;
	}

	public boolean isImmunity() {
		return this.immunity;
	}

	// 中毒状态

	protected transient boolean poison;

	public void setPoison(boolean value) {
		this.poison = value;
		aroundMarks[19] = true;
	}

	public boolean isPoison() {
		return this.poison;
	}

	// 虚弱状态

	protected transient boolean weak;

	public void setWeak(boolean value) {
		this.weak = value;
		aroundMarks[20] = true;
	}

	public boolean isWeak() {
		return this.weak;
	}

	// 无敌状态

	protected transient boolean invulnerable;

	public void setInvulnerable(boolean value) {
		this.invulnerable = value;
		aroundMarks[21] = true;
	}

	public boolean isInvulnerable() {
		return this.invulnerable;
	}

	// 缓疗状态

	protected transient boolean cure;

	public void setCure(boolean value) {
		this.cure = value;
		aroundMarks[22] = true;
	}

	public boolean isCure() {
		return this.cure;
	}

	// 封魔状态，不能使用主动技能

	protected transient boolean silence;

	public void setSilence(boolean value) {
		this.silence = value;
		aroundMarks[23] = true;
	}

	public boolean isSilence() {
		return this.silence;
	}

	// 流血状态

	protected transient boolean liuxueState;

	public void setLiuxueState(boolean value) {
		this.liuxueState = value;
		aroundMarks[24] = true;
	}

	public boolean isLiuxueState() {
		return this.liuxueState;
	}

	// 减速状态

	protected transient boolean jiansuState;

	public void setJiansuState(boolean value) {
		this.jiansuState = value;
		aroundMarks[25] = true;
	}

	public boolean isJiansuState() {
		return this.jiansuState;
	}

	// 破甲状态

	protected transient boolean pojiaState;

	public void setPojiaState(boolean value) {
		this.pojiaState = value;
		aroundMarks[26] = true;
	}

	public boolean isPojiaState() {
		return this.pojiaState;
	}

	// 灼热状态

	protected transient boolean zhuoreState;

	public void setZhuoreState(boolean value) {
		this.zhuoreState = value;
		aroundMarks[27] = true;
	}

	public boolean isZhuoreState() {
		return this.zhuoreState;
	}

	// 寒冷状态

	protected transient boolean hanlengState;

	public void setHanlengState(boolean value) {
		this.hanlengState = value;
		aroundMarks[28] = true;
	}

	public boolean isHanlengState() {
		return this.hanlengState;
	}

	// 提高暴击状态

	protected transient boolean tigaoBaojiState;

	public void setTigaoBaojiState(boolean value) {
		this.tigaoBaojiState = value;
		aroundMarks[29] = true;
	}

	public boolean isTigaoBaojiState() {
		return this.tigaoBaojiState;
	}

	// 提高外功状态

	protected transient boolean tigaoWaigongState;

	public void setTigaoWaigongState(boolean value) {
		this.tigaoWaigongState = value;
		aroundMarks[30] = true;
	}

	public boolean isTigaoWaigongState() {
		return this.tigaoWaigongState;
	}

	// 提高内功状态

	protected transient boolean tigaoNeigongState;

	public void setTigaoNeigongState(boolean value) {
		this.tigaoNeigongState = value;
		aroundMarks[31] = true;
	}

	public boolean isTigaoNeigongState() {
		return this.tigaoNeigongState;
	}

	// 提高外防状态

	protected transient boolean tigaoWaifangState;

	public void setTigaoWaifangState(boolean value) {
		this.tigaoWaifangState = value;
		aroundMarks[32] = true;
	}

	public boolean isTigaoWaifangState() {
		return this.tigaoWaifangState;
	}

	// 提高内防状态

	protected transient boolean tigaoNeifangState;

	public void setTigaoNeifangState(boolean value) {
		this.tigaoNeifangState = value;
		aroundMarks[33] = true;
	}

	public boolean isTigaoNeifangState() {
		return this.tigaoNeifangState;
	}

	// 护盾类型，-1代表没有护盾，玩家下线此变量需保存

	protected byte shield;

	public void setShield(byte value) {
		this.shield = value;
		aroundMarks[34] = true;
	}

	public byte getShield() {
		return this.shield;
	}

	// 是否有降低治疗的buff,
	// 返回：0,1,2,3表示buff级别
	protected transient int lowerCureLevel = -1;

	// 战斗状态

	public int getLowerCureLevel() {
		return lowerCureLevel;
	}

	public void setLowerCureLevel(int lowerCureLevel) {
		this.lowerCureLevel = lowerCureLevel;
	}

	protected transient boolean fighting;

	public void setFighting(boolean value) {
		this.fighting = value;
		selfMarks[16] = true;
	}

	public boolean isFighting() {
		return this.fighting;
	}

	// 角色当前的生命值
	@SimpleColumn(saveInterval = 600)
	protected int hp;

	public void setHp(int value) {
		this.hp = value;
		aroundMarks[35] = true;
	}

	public int getHp() {
		return this.hp;
	}

	// 角色当前的魔法值
	@SimpleColumn(saveInterval = 600)
	protected int mp;

	public void setMp(int value) {
		this.mp = value;
		aroundMarks[36] = true;
	}

	public int getMp() {
		return this.mp;
	}

	// 基础法力值，由角色的裸体智力值计算得出，用于计算技能的魔法消耗

	protected transient int baseMp;

	public void setBaseMp(int value) {
		this.baseMp = value;
		selfMarks[17] = true;
	}

	public int getBaseMp() {
		return this.baseMp;
	}

	// 角色的最大生命值

	protected transient int maxHP;

	public void setMaxHP(int value) {
		this.maxHP = value;
		aroundMarks[37] = true;
	}

	public int getMaxHP() {
		return this.maxHP;
	}

	protected transient int maxHPA;

	public void setMaxHPA(int value) {
		this.maxHPA = value;
	}

	public int getMaxHPA() {
		return this.maxHPA;
	}

	protected transient int maxHPB;

	public void setMaxHPB(int value) {
		this.maxHPB = value;
	}

	public int getMaxHPB() {
		return this.maxHPB;
	}

	protected transient double maxHPC;

	public void setMaxHPC(int value) {
		this.maxHPC = value;
	}

	public double getMaxHPC() {
		return this.maxHPC;
	}

	// 角色的最大生命值X，激活本源时元神使用，激活元神时本源使用

	protected transient int maxHPX;

	public void setMaxHPX(int value) {
		this.maxHPX = value;
		selfMarks[18] = true;
	}

	public int getMaxHPX() {
		return this.maxHPX;
	}

	// 角色的最大魔法值

	protected transient int maxMP;

	public void setMaxMP(int value) {
		this.maxMP = value;
		aroundMarks[38] = true;
	}

	public int getMaxMP() {
		return this.maxMP;
	}

	protected transient int maxMPA;

	public void setMaxMPA(int value) {
		this.maxMPA = value;
	}

	public int getMaxMPA() {
		return this.maxMPA;
	}

	protected transient int maxMPB;

	public void setMaxMPB(int value) {
		this.maxMPB = value;
	}

	public int getMaxMPB() {
		return this.maxMPB;
	}

	protected transient int maxMPC;

	public void setMaxMPC(int value) {
		this.maxMPC = value;
	}

	public int getMaxMPC() {
		return this.maxMPC;
	}

	// 角色的最大魔法值X，激活本源时元神使用，激活元神时本源使用

	protected transient int maxMPX;

	public void setMaxMPX(int value) {
		this.maxMPX = value;
		selfMarks[19] = true;
	}

	public int getMaxMPX() {
		return this.maxMPX;
	}

	// hp恢复速度，每五秒恢复一次

	protected transient int hpRecoverBase;

	public void setHpRecoverBase(int value) {
		this.hpRecoverBase = value;
		selfMarks[20] = true;
	}

	public int getHpRecoverBase() {
		return this.hpRecoverBase;
	}

	// 受体质影响

	protected transient int hpRecoverBaseA;

	public void setHpRecoverBaseA(int value) {
		this.hpRecoverBaseA = value;
	}

	public int getHpRecoverBaseA() {
		return this.hpRecoverBaseA;
	}

	protected transient int hpRecoverBaseB;

	public void setHpRecoverBaseB(int value) {
		this.hpRecoverBaseB = value;
	}

	public int getHpRecoverBaseB() {
		return this.hpRecoverBaseB;
	}

	// mp恢复速度，每五秒恢复一次

	protected transient int mpRecoverBase;

	public void setMpRecoverBase(int value) {
		this.mpRecoverBase = value;
		selfMarks[21] = true;
	}

	public int getMpRecoverBase() {
		return this.mpRecoverBase;
	}

	// 受内力影响

	protected transient int mpRecoverBaseA;

	public void setMpRecoverBaseA(int value) {
		this.mpRecoverBaseA = value;
	}

	public int getMpRecoverBaseA() {
		return this.mpRecoverBaseA;
	}

	protected transient int mpRecoverBaseB;

	public void setMpRecoverBaseB(int value) {
		this.mpRecoverBaseB = value;
	}

	public int getMpRecoverBaseB() {
		return this.mpRecoverBaseB;
	}

	// hp恢复速度，每半秒恢复一次

	protected transient int hpRecoverExtend;

	public void setHpRecoverExtend(int value) {
		this.hpRecoverExtend = value;
	}

	public int getHpRecoverExtend() {
		return this.hpRecoverExtend;
	}

	// mp恢复速度，每半秒恢复一次

	protected transient int mpRecoverExtend;

	public void setMpRecoverExtend(int value) {
		this.mpRecoverExtend = value;
	}

	public int getMpRecoverExtend() {
		return this.mpRecoverExtend;
	}

	// 当前怒气值。怒气值是使用必杀技的条件之一

	protected transient int xp;

	public void setXp(int value) {
		this.xp = value;
		selfMarks[22] = true;
	}

	public int getXp() {
		return this.xp;
	}

	// 怒气值的满值

	protected transient int totalXp;

	public void setTotalXp(int value) {
		this.totalXp = value;
		selfMarks[23] = true;
	}

	public int getTotalXp() {
		return this.totalXp;
	}

	// 角色的移动速度，单位：像素/秒

	protected transient int speed;

	public void setSpeed(int value) {
		this.speed = value;
		aroundMarks[39] = true;
	}

	public int getSpeed() {
		return this.speed;
	}

	// 移动速度的基本值，理论上为常量

	protected transient int speedA;

	public void setSpeedA(int value) {
		this.speedA = value;
	}

	public int getSpeedA() {
		return this.speedA;
	}

	protected transient int speedC;

	public void setSpeedC(int value) {
		this.speedC = value;
	}

	public int getSpeedC() {
		return this.speedC;
	}

	// 力量

	protected transient int strength;

	public void setStrength(int value) {
		this.strength = value;
		selfMarks[24] = true;
	}

	public int getStrength() {
		return this.strength;
	}

	protected transient int strengthA;

	public void setStrengthA(int value) {
		this.strengthA = value;
	}

	public int getStrengthA() {
		return this.strengthA;
	}

	protected transient int strengthB;

	public void setStrengthB(int value) {
		this.strengthB = value;
	}

	public int getStrengthB() {
		return this.strengthB;
	}

	protected transient int strengthC;

	public void setStrengthC(int value) {
		this.strengthC = value;
	}

	public int getStrengthC() {
		return this.strengthC;
	}

	// 力量X，激活本源时元神使用，激活元神时本源使用

	protected transient int strengthX;

	public void setStrengthX(int value) {
		this.strengthX = value;
		selfMarks[25] = true;
	}

	public int getStrengthX() {
		return this.strengthX;
	}

	// 力量，玩家自己加点

	protected transient int strengthS;

	public void setStrengthS(int value) {
		this.strengthS = value;
	}

	public int getStrengthS() {
		return this.strengthS;
	}

	// 智力

	protected transient int spellpower;

	public void setSpellpower(int value) {
		this.spellpower = value;
		selfMarks[26] = true;
	}

	public int getSpellpower() {
		return this.spellpower;
	}

	protected transient int spellpowerA;

	public void setSpellpowerA(int value) {
		this.spellpowerA = value;
	}

	public int getSpellpowerA() {
		return this.spellpowerA;
	}

	protected transient int spellpowerB;

	public void setSpellpowerB(int value) {
		this.spellpowerB = value;
	}

	public int getSpellpowerB() {
		return this.spellpowerB;
	}

	protected transient int spellpowerC;

	public void setSpellpowerC(int value) {
		this.spellpowerC = value;
	}

	public int getSpellpowerC() {
		return this.spellpowerC;
	}

	// 智力X，激活本源时元神使用，激活元神时本源使用

	protected transient int spellpowerX;

	public void setSpellpowerX(int value) {
		this.spellpowerX = value;
		selfMarks[27] = true;
	}

	public int getSpellpowerX() {
		return this.spellpowerX;
	}

	// 智力，玩家自己加点

	protected transient int spellpowerS;

	public void setSpellpowerS(int value) {
		this.spellpowerS = value;
	}

	public int getSpellpowerS() {
		return this.spellpowerS;
	}

	// 敏捷

	protected transient int dexterity;

	public void setDexterity(int value) {
		this.dexterity = value;
		selfMarks[28] = true;
	}

	public int getDexterity() {
		return this.dexterity;
	}

	protected transient int dexterityA;

	public void setDexterityA(int value) {
		this.dexterityA = value;
	}

	public int getDexterityA() {
		return this.dexterityA;
	}

	protected transient int dexterityB;

	public void setDexterityB(int value) {
		this.dexterityB = value;
	}

	public int getDexterityB() {
		return this.dexterityB;
	}

	protected transient int dexterityC;

	public void setDexterityC(int value) {
		this.dexterityC = value;
	}

	public int getDexterityC() {
		return this.dexterityC;
	}

	// 敏捷X，激活本源时元神使用，激活元神时本源使用

	protected transient int dexterityX;

	public void setDexterityX(int value) {
		this.dexterityX = value;
		selfMarks[29] = true;
	}

	public int getDexterityX() {
		return this.dexterityX;
	}

	// 敏捷，玩家自己加点

	protected transient int dexterityS;

	public void setDexterityS(int value) {
		this.dexterityS = value;
	}

	public int getDexterityS() {
		return this.dexterityS;
	}

	// 耐力

	protected transient int constitution;

	public void setConstitution(int value) {
		this.constitution = value;
		selfMarks[30] = true;
	}

	public int getConstitution() {
		return this.constitution;
	}

	protected transient int constitutionA;

	public void setConstitutionA(int value) {
		this.constitutionA = value;
	}

	public int getConstitutionA() {
		return this.constitutionA;
	}

	protected transient int constitutionB;

	public void setConstitutionB(int value) {
		this.constitutionB = value;
	}

	public int getConstitutionB() {
		return this.constitutionB;
	}

	protected transient int constitutionC;

	public void setConstitutionC(int value) {
		this.constitutionC = value;
	}

	public int getConstitutionC() {
		return this.constitutionC;
	}

	// 耐力X，激活本源时元神使用，激活元神时本源使用

	protected transient int constitutionX;

	public void setConstitutionX(int value) {
		this.constitutionX = value;
		selfMarks[31] = true;
	}

	public int getConstitutionX() {
		return this.constitutionX;
	}

	// 耐力，玩家自己加点

	protected transient int constitutionS;

	public void setConstitutionS(int value) {
		this.constitutionS = value;
	}

	public int getConstitutionS() {
		return this.constitutionS;
	}

	// 武器伤害力的上限

	protected transient int weaponDamageUpperLimit;

	public void setWeaponDamageUpperLimit(int value) {
		this.weaponDamageUpperLimit = value;
	}

	public int getWeaponDamageUpperLimit() {
		return this.weaponDamageUpperLimit;
	}

	// 武器伤害力的下限

	protected transient int weaponDamageLowerLimit;

	public void setWeaponDamageLowerLimit(int value) {
		this.weaponDamageLowerLimit = value;
	}

	public int getWeaponDamageLowerLimit() {
		return this.weaponDamageLowerLimit;
	}

	// 物理攻击伤害力的上限

	protected transient int physicalDamageUpperLimit;

	public void setPhysicalDamageUpperLimit(int value) {
		this.physicalDamageUpperLimit = value;
		selfMarks[32] = true;
	}

	public int getPhysicalDamageUpperLimit() {
		return this.physicalDamageUpperLimit;
	}

	// 物理攻击伤害力的下限

	protected transient int physicalDamageLowerLimit;

	public void setPhysicalDamageLowerLimit(int value) {
		this.physicalDamageLowerLimit = value;
		selfMarks[33] = true;
	}

	public int getPhysicalDamageLowerLimit() {
		return this.physicalDamageLowerLimit;
	}

	// 物理防御

	protected transient int phyDefence;

	public void setPhyDefence(int value) {
		this.phyDefence = value;
		selfMarks[34] = true;
	}

	public int getPhyDefence() {
		return this.phyDefence;
	}

	protected transient int phyDefenceA;

	public void setPhyDefenceA(int value) {
		this.phyDefenceA = value;
	}

	public int getPhyDefenceA() {
		return this.phyDefenceA;
	}

	protected transient int phyDefenceB;

	public void setPhyDefenceB(int value) {
		this.phyDefenceB = value;
	}

	public int getPhyDefenceB() {
		return this.phyDefenceB;
	}

	protected transient double phyDefenceC;

	public void setPhyDefenceC(int value) {
		this.phyDefenceC = value;
	}

	public double getPhyDefenceC() {
		return this.phyDefenceC;
	}

	// 非物理防御得到的物理减伤率0~1000 表示千分率

	protected transient int phyDefenceRateOther;

	public void setPhyDefenceRateOther(int value) {
		this.phyDefenceRateOther = value;
		selfMarks[35] = true;
	}

	public int getPhyDefenceRateOther() {
		return this.phyDefenceRateOther;
	}

	// 物理防御X，激活本源时元神使用，激活元神时本源使用

	protected transient int phyDefenceX;

	public void setPhyDefenceX(int value) {
		this.phyDefenceX = value;
		selfMarks[36] = true;
	}

	public int getPhyDefenceX() {
		return this.phyDefenceX;
	}

	// 物理减伤百分比

	protected transient int physicalDecrease;

	public void setPhysicalDecrease(int value) {
		this.physicalDecrease = value;
		selfMarks[37] = true;
	}

	public int getPhysicalDecrease() {
		return this.physicalDecrease;
	}

	// 法术防御力

	protected transient int magicDefence;

	public void setMagicDefence(int value) {
		this.magicDefence = value;
		selfMarks[38] = true;
	}

	public int getMagicDefence() {
		return this.magicDefence;
	}

	protected transient int magicDefenceA;

	public void setMagicDefenceA(int value) {
		this.magicDefenceA = value;
	}

	public int getMagicDefenceA() {
		return this.magicDefenceA;
	}

	protected transient int magicDefenceB;

	public void setMagicDefenceB(int value) {
		this.magicDefenceB = value;
	}

	public int getMagicDefenceB() {
		return this.magicDefenceB;
	}

	protected transient double magicDefenceC;

	public void setMagicDefenceC(int value) {
		this.magicDefenceC = value;
	}

	public double getMagicDefenceC() {
		return this.magicDefenceC;
	}

	// 非法术防御得到的法术减伤率0~1000 表示千分率

	protected transient int magicDefenceRateOther;

	public void setMagicDefenceRateOther(int value) {
		this.magicDefenceRateOther = value;
		selfMarks[39] = true;
	}

	public int getMagicDefenceRateOther() {
		return this.magicDefenceRateOther;
	}

	// 法术防御力X，激活本源时元神使用，激活元神时本源使用

	protected transient int magicDefenceX;

	public void setMagicDefenceX(int value) {
		this.magicDefenceX = value;
		selfMarks[40] = true;
	}

	public int getMagicDefenceX() {
		return this.magicDefenceX;
	}

	// 法术减伤百分比

	protected transient int spellDecrease;

	public void setSpellDecrease(int value) {
		this.spellDecrease = value;
		selfMarks[41] = true;
	}

	public int getSpellDecrease() {
		return this.spellDecrease;
	}

	// 破防

	protected transient int breakDefence;

	public void setBreakDefence(int value) {
		this.breakDefence = value;
		selfMarks[42] = true;
	}

	public int getBreakDefence() {
		return this.breakDefence;
	}

	protected transient int breakDefenceA;

	public void setBreakDefenceA(int value) {
		this.breakDefenceA = value;
	}

	public int getBreakDefenceA() {
		return this.breakDefenceA;
	}

	protected transient int breakDefenceB;

	public void setBreakDefenceB(int value) {
		this.breakDefenceB = value;
	}

	public int getBreakDefenceB() {
		return this.breakDefenceB;
	}

	protected transient double breakDefenceC;

	public void setBreakDefenceC(double value) {
		this.breakDefenceC = value;
	}

	public double getBreakDefenceC() {
		return this.breakDefenceC;
	}

	// 破防X，激活本源时元神使用，激活元神时本源使用

	protected transient int breakDefenceX;

	public void setBreakDefenceX(int value) {
		this.breakDefenceX = value;
		selfMarks[43] = true;
	}

	public int getBreakDefenceX() {
		return this.breakDefenceX;
	}

	// 破防率，该值是额外破防率加成，0~1000 表示千分率

	protected transient int breakDefenceRateOther;

	public void setBreakDefenceRateOther(int value) {
		this.breakDefenceRateOther = value;
	}

	public int getBreakDefenceRateOther() {
		return this.breakDefenceRateOther;
	}

	// 破防率

	protected transient int breakDefenceRate;

	public void setBreakDefenceRate(int value) {
		this.breakDefenceRate = value;
		selfMarks[44] = true;
	}

	public int getBreakDefenceRate() {
		return this.breakDefenceRate;
	}

	// 命中等级

	protected transient int hit;

	public void setHit(int value) {
		this.hit = value;
		selfMarks[45] = true;
	}

	public int getHit() {
		return this.hit;
	}

	protected transient int hitA;

	public void setHitA(int value) {
		this.hitA = value;
	}

	public int getHitA() {
		return this.hitA;
	}

	protected transient int hitB;

	public void setHitB(int value) {
		this.hitB = value;
	}

	public int getHitB() {
		return this.hitB;
	}

	protected transient double hitC;

	public void setHitC(double value) {
		this.hitC = value;
	}

	public double getHitC() {
		return this.hitC;
	}

	// 天赋增加的命中百分比

	protected transient int hitD;

	public void setHitD(int value) {
		this.hitD = value;
	}

	public int getHitD() {
		return this.hitD;
	}

	// 命中等级X，激活本源时元神使用，激活元神时本源使用

	protected transient int hitX;

	public void setHitX(int value) {
		this.hitX = value;
		selfMarks[46] = true;
	}

	public int getHitX() {
		return this.hitX;
	}

	// 命中率，该值是额外命中率加成，0~1000 表示千分率

	protected transient int hitRateOther;

	public void setHitRateOther(int value) {
		this.hitRateOther = value;
	}

	public int getHitRateOther() {
		return this.hitRateOther;
	}

	// 命中率

	protected transient int hitRate;

	public void setHitRate(int value) {
		this.hitRate = value;
		selfMarks[47] = true;
	}

	public int getHitRate() {
		return this.hitRate;
	}

	// 回避

	protected transient int dodge;

	public void setDodge(int value) {
		this.dodge = value;
		selfMarks[48] = true;
	}

	public int getDodge() {
		return this.dodge;
	}

	protected transient int dodgeA;

	public void setDodgeA(int value) {
		this.dodgeA = value;
	}

	public int getDodgeA() {
		return this.dodgeA;
	}

	protected transient int dodgeB;

	public void setDodgeB(int value) {
		this.dodgeB = value;
	}

	public int getDodgeB() {
		return this.dodgeB;
	}

	protected transient double dodgeC;

	public void setDodgeC(double value) {
		this.dodgeC = value;
	}

	public double getDodgeC() {
		return this.dodgeC;
	}

	// 回避X，激活本源时元神使用，激活元神时本源使用

	protected transient int dodgeX;

	public void setDodgeX(int value) {
		this.dodgeX = value;
		selfMarks[49] = true;
	}

	public int getDodgeX() {
		return this.dodgeX;
	}

	// 闪避率，该值是额外闪避率加成，0~1000 表示千分率

	protected transient int dodgeRateOther;

	public void setDodgeRateOther(int value) {
		this.dodgeRateOther = value;
	}

	public int getDodgeRateOther() {
		return this.dodgeRateOther;
	}

	// 闪避率

	protected transient int dodgeRate;

	public void setDodgeRate(int value) {
		this.dodgeRate = value;
		selfMarks[50] = true;
	}

	public int getDodgeRate() {
		return this.dodgeRate;
	}

	// 精准

	protected transient int accurate;

	public void setAccurate(int value) {
		this.accurate = value;
		selfMarks[51] = true;
	}

	public int getAccurate() {
		return this.accurate;
	}

	protected transient int accurateA;

	public void setAccurateA(int value) {
		this.accurateA = value;
	}

	public int getAccurateA() {
		return this.accurateA;
	}

	protected transient int accurateB;

	public void setAccurateB(int value) {
		this.accurateB = value;
	}

	public int getAccurateB() {
		return this.accurateB;
	}

	protected transient double accurateC;

	public void setAccurateC(double value) {
		this.accurateC = value;
	}

	public double getAccurateC() {
		return this.accurateC;
	}

	// 精准X，激活本源时元神使用，激活元神时本源使用

	protected transient int accurateX;

	public void setAccurateX(int value) {
		this.accurateX = value;
		selfMarks[52] = true;
	}

	public int getAccurateX() {
		return this.accurateX;
	}

	// 精准率，该值是额外精准率加成，0~1000 表示千分率

	protected transient int accurateRateOther;

	public void setAccurateRateOther(int value) {
		this.accurateRateOther = value;
	}

	public int getAccurateRateOther() {
		return this.accurateRateOther;
	}

	// 精准率

	protected transient int accurateRate;

	public void setAccurateRate(int value) {
		this.accurateRate = value;
		selfMarks[53] = true;
	}

	public int getAccurateRate() {
		return this.accurateRate;
	}

	// 近战攻击强度

	protected transient int phyAttack;

	public void setPhyAttack(int value) {
		this.phyAttack = value;
		selfMarks[54] = true;
	}

	public int getPhyAttack() {
		return this.phyAttack;
	}

	protected transient int phyAttackA;

	public void setPhyAttackA(int value) {
		this.phyAttackA = value;
	}

	public int getPhyAttackA() {
		return this.phyAttackA;
	}

	protected transient int phyAttackB;

	public void setPhyAttackB(int value) {
		this.phyAttackB = value;
	}

	public int getPhyAttackB() {
		return this.phyAttackB;
	}

	protected transient double phyAttackC;

	public void setPhyAttackC(int value) {
		this.phyAttackC = value;
	}

	public double getPhyAttackC() {
		return this.phyAttackC;
	}

	// 近战攻击强度X，激活本源时元神使用，激活元神时本源使用

	protected transient int phyAttackX;

	public void setPhyAttackX(int value) {
		this.phyAttackX = value;
		selfMarks[55] = true;
	}

	public int getPhyAttackX() {
		return this.phyAttackX;
	}

	// 法术攻击强度

	protected transient int magicAttack;

	public void setMagicAttack(int value) {
		this.magicAttack = value;
		selfMarks[56] = true;
	}

	public int getMagicAttack() {
		return this.magicAttack;
	}

	protected transient int magicAttackA;

	public void setMagicAttackA(int value) {
		this.magicAttackA = value;
	}

	public int getMagicAttackA() {
		return this.magicAttackA;
	}

	protected transient int magicAttackB;

	public void setMagicAttackB(int value) {
		this.magicAttackB = value;
	}

	public int getMagicAttackB() {
		return this.magicAttackB;
	}

	protected transient double magicAttackC;

	public void setMagicAttackC(int value) {
		this.magicAttackC = value;
	}

	public double getMagicAttackC() {
		return this.magicAttackC;
	}

	// 法术攻击强度X，激活本源时元神使用，激活元神时本源使用

	protected transient int magicAttackX;

	public void setMagicAttackX(int value) {
		this.magicAttackX = value;
		selfMarks[57] = true;
	}

	public int getMagicAttackX() {
		return this.magicAttackX;
	}

	// Y值类似于X值 不享受C值加成 增加具体数值
	protected transient int phyAttackY;

	protected transient int magicAttackY;

	protected transient int maxHpY;

	protected transient int criticalHitY;

	protected transient int hitY;

	protected transient int accurateY;

	protected transient int breakDefenceY;

	protected transient int magicDefenceY;

	protected transient int phyDefenceY;
	
	
	//宠物给角色带来的加成
	protected transient int phyAttackZ;
	protected transient int magicAttackZ;
	protected transient int phyDefenceZ;
	protected transient int magicDefenceZ;
	
	protected transient int hitZ;
	protected transient int dodgeZ;
	protected transient int criticalHitZ;
	
	public int getPhyAttackZ() {
		return phyAttackZ;
	}

	public void setPhyAttackZ(int phyAttackZ) {
		this.phyAttackZ = phyAttackZ;
	}

	public int getMagicAttackZ() {
		return magicAttackZ;
	}

	public void setMagicAttackZ(int magicAttackZ) {
		this.magicAttackZ = magicAttackZ;
	}

	public int getPhyDefenceZ() {
		return phyDefenceZ;
	}

	public void setPhyDefenceZ(int phyDefenceZ) {
		this.phyDefenceZ = phyDefenceZ;
	}

	public int getMagicDefenceZ() {
		return magicDefenceZ;
	}

	public void setMagicDefenceZ(int magicDefenceZ) {
		this.magicDefenceZ = magicDefenceZ;
	}

	public int getHitZ() {
		return hitZ;
	}

	public void setHitZ(int hitZ) {
		this.hitZ = hitZ;
	}

	public int getDodgeZ() {
		return dodgeZ;
	}

	public void setDodgeZ(int dodgeZ) {
		this.dodgeZ = dodgeZ;
	}

	public int getCriticalHitZ() {
		return criticalHitZ;
	}

	public void setCriticalHitZ(int criticalHitZ) {
		this.criticalHitZ = criticalHitZ;
	}

	public int getMagicAttackY() {
		return magicAttackY;
	}

	public void setMagicAttackY(int magicAttackY) {
		this.magicAttackY = magicAttackY;
	}

	public int getMaxHpY() {
		return maxHpY;
	}

	public void setMaxHpY(int maxHpY) {
		this.maxHpY = maxHpY;
	}

	public int getCriticalHitY() {
		return criticalHitY;
	}

	public void setCriticalHitY(int criticalHitY) {
		this.criticalHitY = criticalHitY;
	}

	public int getHitY() {
		return hitY;
	}

	public void setHitY(int hitY) {
		this.hitY = hitY;
	}

	public int getAccurateY() {
		return accurateY;
	}

	public void setAccurateY(int accurateY) {
		this.accurateY = accurateY;
	}

	public int getBreakDefenceY() {
		return breakDefenceY;
	}

	public void setBreakDefenceY(int breakDefenceY) {
		this.breakDefenceY = breakDefenceY;
	}

	protected transient int fireAttack;

	public void setFireAttack(int value) {
		this.fireAttack = value;
		selfMarks[58] = true;
	}

	public int getFireAttack() {
		return this.fireAttack;
	}

	protected transient int fireAttackA;

	public void setFireAttackA(int value) {
		this.fireAttackA = value;
	}

	public int getFireAttackA() {
		return this.fireAttackA;
	}

	protected transient int fireAttackB;

	public void setFireAttackB(int value) {
		this.fireAttackB = value;
	}

	public int getFireAttackB() {
		return this.fireAttackB;
	}

	protected transient int fireAttackC;

	public void setFireAttackC(int value) {
		this.fireAttackC = value;
	}

	public int getFireAttackC() {
		return this.fireAttackC;
	}

	// 火攻X，激活本源时元神使用，激活元神时本源使用

	protected transient int fireAttackX;

	public void setFireAttackX(int value) {
		this.fireAttackX = value;
		selfMarks[59] = true;
	}

	public int getFireAttackX() {
		return this.fireAttackX;
	}

	// 金防

	protected transient int fireDefence;

	public void setFireDefence(int value) {
		this.fireDefence = value;
		selfMarks[60] = true;
	}

	public int getFireDefence() {
		return this.fireDefence;
	}

	protected transient int fireDefenceA;

	public void setFireDefenceA(int value) {
		this.fireDefenceA = value;
	}

	public int getFireDefenceA() {
		return this.fireDefenceA;
	}

	protected transient int fireDefenceB;

	public void setFireDefenceB(int value) {
		this.fireDefenceB = value;
	}

	public int getFireDefenceB() {
		return this.fireDefenceB;
	}

	protected transient int fireDefenceC;

	public void setFireDefenceC(int value) {
		this.fireDefenceC = value;
	}

	public int getFireDefenceC() {
		return this.fireDefenceC;
	}

	// 金防X，激活本源时元神使用，激活元神时本源使用

	protected transient int fireDefenceX;

	public void setFireDefenceX(int value) {
		this.fireDefenceX = value;
		selfMarks[61] = true;
	}

	public int getFireDefenceX() {
		return this.fireDefenceX;
	}

	// 金防率，该值是金防率加成，0~1000 表示千分率

	protected transient int fireDefenceRateOther;

	public void setFireDefenceRateOther(int value) {
		this.fireDefenceRateOther = value;
	}

	public int getFireDefenceRateOther() {
		return this.fireDefenceRateOther;
	}

	// 金防率

	protected transient int fireDefenceRate;

	public void setFireDefenceRate(int value) {
		this.fireDefenceRate = value;
		selfMarks[62] = true;
	}

	public int getFireDefenceRate() {
		return this.fireDefenceRate;
	}

	// 减少对方金防

	protected transient int fireIgnoreDefence;

	public void setFireIgnoreDefence(int value) {
		this.fireIgnoreDefence = value;
		selfMarks[63] = true;
	}

	public int getFireIgnoreDefence() {
		return this.fireIgnoreDefence;
	}

	protected transient int fireIgnoreDefenceA;

	public void setFireIgnoreDefenceA(int value) {
		this.fireIgnoreDefenceA = value;
	}

	public int getFireIgnoreDefenceA() {
		return this.fireIgnoreDefenceA;
	}

	protected transient int fireIgnoreDefenceB;

	public void setFireIgnoreDefenceB(int value) {
		this.fireIgnoreDefenceB = value;
	}

	public int getFireIgnoreDefenceB() {
		return this.fireIgnoreDefenceB;
	}

	protected transient int fireIgnoreDefenceC;

	public void setFireIgnoreDefenceC(int value) {
		this.fireIgnoreDefenceC = value;
	}

	public int getFireIgnoreDefenceC() {
		return this.fireIgnoreDefenceC;
	}

	// 减少对方金防X，激活本源时元神使用，激活元神时本源使用

	protected transient int fireIgnoreDefenceX;

	public void setFireIgnoreDefenceX(int value) {
		this.fireIgnoreDefenceX = value;
		selfMarks[64] = true;
	}

	public int getFireIgnoreDefenceX() {
		return this.fireIgnoreDefenceX;
	}

	// 减少对方金防率，该值是额外减少对方金防率加成，0~1000 表示千分率

	protected transient int fireIgnoreDefenceRateOther;

	public void setFireIgnoreDefenceRateOther(int value) {
		this.fireIgnoreDefenceRateOther = value;
	}

	public int getFireIgnoreDefenceRateOther() {
		return this.fireIgnoreDefenceRateOther;
	}

	// 减少对方金防率

	protected transient int fireIgnoreDefenceRate;

	public void setFireIgnoreDefenceRate(int value) {
		this.fireIgnoreDefenceRate = value;
		selfMarks[65] = true;
	}

	public int getFireIgnoreDefenceRate() {
		return this.fireIgnoreDefenceRate;
	}

	// 冰攻

	protected transient int blizzardAttack;

	public void setBlizzardAttack(int value) {
		this.blizzardAttack = value;
		selfMarks[66] = true;
	}

	public int getBlizzardAttack() {
		return this.blizzardAttack;
	}

	protected transient int blizzardAttackA;

	public void setBlizzardAttackA(int value) {
		this.blizzardAttackA = value;
	}

	public int getBlizzardAttackA() {
		return this.blizzardAttackA;
	}

	protected transient int blizzardAttackB;

	public void setBlizzardAttackB(int value) {
		this.blizzardAttackB = value;
	}

	public int getBlizzardAttackB() {
		return this.blizzardAttackB;
	}

	protected transient int blizzardAttackC;

	public void setBlizzardAttackC(int value) {
		this.blizzardAttackC = value;
	}

	public int getBlizzardAttackC() {
		return this.blizzardAttackC;
	}

	// 冰攻X，激活本源时元神使用，激活元神时本源使用

	protected transient int blizzardAttackX;

	public void setBlizzardAttackX(int value) {
		this.blizzardAttackX = value;
		selfMarks[67] = true;
	}

	public int getBlizzardAttackX() {
		return this.blizzardAttackX;
	}

	// 水防

	protected transient int blizzardDefence;

	public void setBlizzardDefence(int value) {
		this.blizzardDefence = value;
		selfMarks[68] = true;
	}

	public int getBlizzardDefence() {
		return this.blizzardDefence;
	}

	protected transient int blizzardDefenceA;

	public void setBlizzardDefenceA(int value) {
		this.blizzardDefenceA = value;
	}

	public int getBlizzardDefenceA() {
		return this.blizzardDefenceA;
	}

	protected transient int blizzardDefenceB;

	public void setBlizzardDefenceB(int value) {
		this.blizzardDefenceB = value;
	}

	public int getBlizzardDefenceB() {
		return this.blizzardDefenceB;
	}

	protected transient int blizzardDefenceC;

	public void setBlizzardDefenceC(int value) {
		this.blizzardDefenceC = value;
	}

	public int getBlizzardDefenceC() {
		return this.blizzardDefenceC;
	}

	// 水防X，激活本源时元神使用，激活元神时本源使用

	protected transient int blizzardDefenceX;

	public void setBlizzardDefenceX(int value) {
		this.blizzardDefenceX = value;
		selfMarks[69] = true;
	}

	public int getBlizzardDefenceX() {
		return this.blizzardDefenceX;
	}

	// 水防率，该值是水防率加成，0~1000 表示千分率

	protected transient int blizzardDefenceRateOther;

	public void setBlizzardDefenceRateOther(int value) {
		this.blizzardDefenceRateOther = value;
	}

	public int getBlizzardDefenceRateOther() {
		return this.blizzardDefenceRateOther;
	}

	// 水防率

	protected transient int blizzardDefenceRate;

	public void setBlizzardDefenceRate(int value) {
		this.blizzardDefenceRate = value;
		selfMarks[70] = true;
	}

	public int getBlizzardDefenceRate() {
		return this.blizzardDefenceRate;
	}

	// 减少对方水防

	protected transient int blizzardIgnoreDefence;

	public void setBlizzardIgnoreDefence(int value) {
		this.blizzardIgnoreDefence = value;
		selfMarks[71] = true;
	}

	public int getBlizzardIgnoreDefence() {
		return this.blizzardIgnoreDefence;
	}

	protected transient int blizzardIgnoreDefenceA;

	public void setBlizzardIgnoreDefenceA(int value) {
		this.blizzardIgnoreDefenceA = value;
	}

	public int getBlizzardIgnoreDefenceA() {
		return this.blizzardIgnoreDefenceA;
	}

	protected transient int blizzardIgnoreDefenceB;

	public void setBlizzardIgnoreDefenceB(int value) {
		this.blizzardIgnoreDefenceB = value;
	}

	public int getBlizzardIgnoreDefenceB() {
		return this.blizzardIgnoreDefenceB;
	}

	protected transient int blizzardIgnoreDefenceC;

	public void setBlizzardIgnoreDefenceC(int value) {
		this.blizzardIgnoreDefenceC = value;
	}

	public int getBlizzardIgnoreDefenceC() {
		return this.blizzardIgnoreDefenceC;
	}

	// 减少对方水防X，激活本源时元神使用，激活元神时本源使用

	protected transient int blizzardIgnoreDefenceX;

	public void setBlizzardIgnoreDefenceX(int value) {
		this.blizzardIgnoreDefenceX = value;
		selfMarks[72] = true;
	}

	public int getBlizzardIgnoreDefenceX() {
		return this.blizzardIgnoreDefenceX;
	}

	// 减少对方水防率，该值是额外减少对方水防率加成，0~1000 表示千分率

	protected transient int blizzardIgnoreDefenceRateOther;

	public void setBlizzardIgnoreDefenceRateOther(int value) {
		this.blizzardIgnoreDefenceRateOther = value;
	}

	public int getBlizzardIgnoreDefenceRateOther() {
		return this.blizzardIgnoreDefenceRateOther;
	}

	// 减少对方水防率

	protected transient int blizzardIgnoreDefenceRate;

	public void setBlizzardIgnoreDefenceRate(int value) {
		this.blizzardIgnoreDefenceRate = value;
		selfMarks[73] = true;
	}

	public int getBlizzardIgnoreDefenceRate() {
		return this.blizzardIgnoreDefenceRate;
	}

	// 风攻

	protected transient int windAttack;

	public void setWindAttack(int value) {
		this.windAttack = value;
		selfMarks[74] = true;
	}

	public int getWindAttack() {
		return this.windAttack;
	}

	protected transient int windAttackA;

	public void setWindAttackA(int value) {
		this.windAttackA = value;
	}

	public int getWindAttackA() {
		return this.windAttackA;
	}

	protected transient int windAttackB;

	public void setWindAttackB(int value) {
		this.windAttackB = value;
	}

	public int getWindAttackB() {
		return this.windAttackB;
	}

	protected transient int windAttackC;

	public void setWindAttackC(int value) {
		this.windAttackC = value;
	}

	public int getWindAttackC() {
		return this.windAttackC;
	}

	// 风攻X，激活本源时元神使用，激活元神时本源使用

	protected transient int windAttackX;

	public void setWindAttackX(int value) {
		this.windAttackX = value;
		selfMarks[75] = true;
	}

	public int getWindAttackX() {
		return this.windAttackX;
	}

	// 火防

	protected transient int windDefence;

	public void setWindDefence(int value) {
		this.windDefence = value;
		selfMarks[76] = true;
	}

	public int getWindDefence() {
		return this.windDefence;
	}

	protected transient int windDefenceA;

	public void setWindDefenceA(int value) {
		this.windDefenceA = value;
	}

	public int getWindDefenceA() {
		return this.windDefenceA;
	}

	protected transient int windDefenceB;

	public void setWindDefenceB(int value) {
		this.windDefenceB = value;
	}

	public int getWindDefenceB() {
		return this.windDefenceB;
	}

	protected transient int windDefenceC;

	public void setWindDefenceC(int value) {
		this.windDefenceC = value;
	}

	public int getWindDefenceC() {
		return this.windDefenceC;
	}

	// 火防X，激活本源时元神使用，激活元神时本源使用

	protected transient int windDefenceX;

	public void setWindDefenceX(int value) {
		this.windDefenceX = value;
		selfMarks[77] = true;
	}

	public int getWindDefenceX() {
		return this.windDefenceX;
	}

	// 火防率，该值是火防率加成，0~1000 表示千分率

	protected transient int windDefenceRateOther;

	public void setWindDefenceRateOther(int value) {
		this.windDefenceRateOther = value;
	}

	public int getWindDefenceRateOther() {
		return this.windDefenceRateOther;
	}

	// 火防率

	protected transient int windDefenceRate;

	public void setWindDefenceRate(int value) {
		this.windDefenceRate = value;
		selfMarks[78] = true;
	}

	public int getWindDefenceRate() {
		return this.windDefenceRate;
	}

	// 减少对方火防

	protected transient int windIgnoreDefence;

	public void setWindIgnoreDefence(int value) {
		this.windIgnoreDefence = value;
		selfMarks[79] = true;
	}

	public int getWindIgnoreDefence() {
		return this.windIgnoreDefence;
	}

	protected transient int windIgnoreDefenceA;

	public void setWindIgnoreDefenceA(int value) {
		this.windIgnoreDefenceA = value;
	}

	public int getWindIgnoreDefenceA() {
		return this.windIgnoreDefenceA;
	}

	protected transient int windIgnoreDefenceB;

	public void setWindIgnoreDefenceB(int value) {
		this.windIgnoreDefenceB = value;
	}

	public int getWindIgnoreDefenceB() {
		return this.windIgnoreDefenceB;
	}

	protected transient int windIgnoreDefenceC;

	public void setWindIgnoreDefenceC(int value) {
		this.windIgnoreDefenceC = value;
	}

	public int getWindIgnoreDefenceC() {
		return this.windIgnoreDefenceC;
	}

	// 减少对方火防X，激活本源时元神使用，激活元神时本源使用

	protected transient int windIgnoreDefenceX;

	public void setWindIgnoreDefenceX(int value) {
		this.windIgnoreDefenceX = value;
		selfMarks[80] = true;
	}

	public int getWindIgnoreDefenceX() {
		return this.windIgnoreDefenceX;
	}

	// 减少对方火防率，该值是额外减少对方火防率加成，0~1000 表示千分率

	protected transient int windIgnoreDefenceRateOther;

	public void setWindIgnoreDefenceRateOther(int value) {
		this.windIgnoreDefenceRateOther = value;
	}

	public int getWindIgnoreDefenceRateOther() {
		return this.windIgnoreDefenceRateOther;
	}

	// 减少对方火防率

	protected transient int windIgnoreDefenceRate;

	public void setWindIgnoreDefenceRate(int value) {
		this.windIgnoreDefenceRate = value;
		selfMarks[81] = true;
	}

	public int getWindIgnoreDefenceRate() {
		return this.windIgnoreDefenceRate;
	}

	// 雷攻

	protected transient int thunderAttack;

	public void setThunderAttack(int value) {
		this.thunderAttack = value;
		selfMarks[82] = true;
	}

	public int getThunderAttack() {
		return this.thunderAttack;
	}

	protected transient int thunderAttackA;

	public void setThunderAttackA(int value) {
		this.thunderAttackA = value;
	}

	public int getThunderAttackA() {
		return this.thunderAttackA;
	}

	protected transient int thunderAttackB;

	public void setThunderAttackB(int value) {
		this.thunderAttackB = value;
	}

	public int getThunderAttackB() {
		return this.thunderAttackB;
	}

	protected transient int thunderAttackC;

	public void setThunderAttackC(int value) {
		this.thunderAttackC = value;
	}

	public int getThunderAttackC() {
		return this.thunderAttackC;
	}

	// 雷攻X，激活本源时元神使用，激活元神时本源使用

	protected transient int thunderAttackX;

	public void setThunderAttackX(int value) {
		this.thunderAttackX = value;
		selfMarks[83] = true;
	}

	public int getThunderAttackX() {
		return this.thunderAttackX;
	}

	// 木防

	protected transient int thunderDefence;

	public void setThunderDefence(int value) {
		this.thunderDefence = value;
		selfMarks[84] = true;
	}

	public int getThunderDefence() {
		return this.thunderDefence;
	}

	protected transient int thunderDefenceA;

	public void setThunderDefenceA(int value) {
		this.thunderDefenceA = value;
	}

	public int getThunderDefenceA() {
		return this.thunderDefenceA;
	}

	protected transient int thunderDefenceB;

	public void setThunderDefenceB(int value) {
		this.thunderDefenceB = value;
	}

	public int getThunderDefenceB() {
		return this.thunderDefenceB;
	}

	protected transient int thunderDefenceC;

	public void setThunderDefenceC(int value) {
		this.thunderDefenceC = value;
	}

	public int getThunderDefenceC() {
		return this.thunderDefenceC;
	}

	// 木防X，激活本源时元神使用，激活元神时本源使用

	protected transient int thunderDefenceX;

	public void setThunderDefenceX(int value) {
		this.thunderDefenceX = value;
		selfMarks[85] = true;
	}

	public int getThunderDefenceX() {
		return this.thunderDefenceX;
	}

	// 木防率，该值是木防率加成，0~1000 表示千分率

	protected transient int thunderDefenceRateOther;

	public void setThunderDefenceRateOther(int value) {
		this.thunderDefenceRateOther = value;
	}

	public int getThunderDefenceRateOther() {
		return this.thunderDefenceRateOther;
	}

	// 木防率

	protected transient int thunderDefenceRate;

	public void setThunderDefenceRate(int value) {
		this.thunderDefenceRate = value;
		selfMarks[86] = true;
	}

	public int getThunderDefenceRate() {
		return this.thunderDefenceRate;
	}

	// 减少对方木防

	protected transient int thunderIgnoreDefence;

	public void setThunderIgnoreDefence(int value) {
		this.thunderIgnoreDefence = value;
		selfMarks[87] = true;
	}

	public int getThunderIgnoreDefence() {
		return this.thunderIgnoreDefence;
	}

	protected transient int thunderIgnoreDefenceA;

	public void setThunderIgnoreDefenceA(int value) {
		this.thunderIgnoreDefenceA = value;
	}

	public int getThunderIgnoreDefenceA() {
		return this.thunderIgnoreDefenceA;
	}

	protected transient int thunderIgnoreDefenceB;

	public void setThunderIgnoreDefenceB(int value) {
		this.thunderIgnoreDefenceB = value;
	}

	public int getThunderIgnoreDefenceB() {
		return this.thunderIgnoreDefenceB;
	}

	protected transient int thunderIgnoreDefenceC;

	public void setThunderIgnoreDefenceC(int value) {
		this.thunderIgnoreDefenceC = value;
	}

	public int getThunderIgnoreDefenceC() {
		return this.thunderIgnoreDefenceC;
	}

	// 减少对方木防X，激活本源时元神使用，激活元神时本源使用

	protected transient int thunderIgnoreDefenceX;

	public void setThunderIgnoreDefenceX(int value) {
		this.thunderIgnoreDefenceX = value;
		selfMarks[88] = true;
	}

	public int getThunderIgnoreDefenceX() {
		return this.thunderIgnoreDefenceX;
	}

	// 减少对方木防率，该值是额外减少对方木防率加成，0~1000 表示千分率

	protected transient int thunderIgnoreDefenceRateOther;

	public void setThunderIgnoreDefenceRateOther(int value) {
		this.thunderIgnoreDefenceRateOther = value;
	}

	public int getThunderIgnoreDefenceRateOther() {
		return this.thunderIgnoreDefenceRateOther;
	}

	// 减少对方木防率

	protected transient int thunderIgnoreDefenceRate;

	public void setThunderIgnoreDefenceRate(int value) {
		this.thunderIgnoreDefenceRate = value;
		selfMarks[89] = true;
	}

	public int getThunderIgnoreDefenceRate() {
		return this.thunderIgnoreDefenceRate;
	}

	// 技能冷却时间的百分比

	protected transient int coolDownTimePercent;

	public void setCoolDownTimePercent(int value) {
		this.coolDownTimePercent = value;
		selfMarks[90] = true;
	}

	public int getCoolDownTimePercent() {
		return this.coolDownTimePercent;
	}

	protected transient int coolDownTimePercentA;

	public void setCoolDownTimePercentA(int value) {
		this.coolDownTimePercentA = value;
	}

	public int getCoolDownTimePercentA() {
		return this.coolDownTimePercentA;
	}

	protected transient int coolDownTimePercentB;

	public void setCoolDownTimePercentB(int value) {
		this.coolDownTimePercentB = value;
	}

	public int getCoolDownTimePercentB() {
		return this.coolDownTimePercentB;
	}

	// 技能僵直时间的百分比

	protected transient int setupTimePercent;

	public void setSetupTimePercent(int value) {
		this.setupTimePercent = value;
		selfMarks[91] = true;
	}

	public int getSetupTimePercent() {
		return this.setupTimePercent;
	}

	protected transient int setupTimePercentA;

	public void setSetupTimePercentA(int value) {
		this.setupTimePercentA = value;
	}

	public int getSetupTimePercentA() {
		return this.setupTimePercentA;
	}

	protected transient int setupTimePercentB;

	public void setSetupTimePercentB(int value) {
		this.setupTimePercentB = value;
	}

	public int getSetupTimePercentB() {
		return this.setupTimePercentB;
	}

	// 会心一击，也叫暴击

	protected transient int criticalHit;

	public void setCriticalHit(int value) {
		this.criticalHit = value;
		selfMarks[92] = true;
	}

	public int getCriticalHit() {
		return this.criticalHit;
	}

	protected transient int criticalHitA;

	public void setCriticalHitA(int value) {
		this.criticalHitA = value;
	}

	public int getCriticalHitA() {
		return this.criticalHitA;
	}

	protected transient int criticalHitB;

	public void setCriticalHitB(int value) {
		this.criticalHitB = value;
	}

	public int getCriticalHitB() {
		return this.criticalHitB;
	}

	protected transient double criticalHitC;

	public void setCriticalHitC(double value) {
		this.criticalHitC = value;
	}

	public double getCriticalHitC() {
		return this.criticalHitC;
	}

	// 暴击X，激活本源时元神使用，激活元神时本源使用

	protected transient int criticalHitX;

	public void setCriticalHitX(int value) {
		this.criticalHitX = value;
		selfMarks[93] = true;
	}

	public int getCriticalHitX() {
		return this.criticalHitX;
	}

	// 暴击率，该值是暴击率加成，0~1000 表示千分率

	protected transient int criticalHitRateOther;

	public void setCriticalHitRateOther(int value) {
		this.criticalHitRateOther = value;
		selfMarks[97] = true;
	}

	public int getCriticalHitRateOther() {
		return this.criticalHitRateOther;
	}

	// 会心防御

	protected transient int criticalDefence;

	public void setCriticalDefence(int value) {
		this.criticalDefence = value;
		selfMarks[94] = true;
	}

	public int getCriticalDefence() {
		return this.criticalDefence;
	}

	protected transient int criticalDefenceA;

	public void setCriticalDefenceA(int value) {
		this.criticalDefenceA = value;
	}

	public int getCriticalDefenceA() {
		return this.criticalDefenceA;
	}

	protected transient int criticalDefenceB;

	public void setCriticalDefenceB(int value) {
		this.criticalDefenceB = value;
	}

	public int getCriticalDefenceB() {
		return this.criticalDefenceB;
	}

	protected transient double criticalDefenceC;

	public void setCriticalDefenceC(double value) {
		this.criticalDefenceC = value;
	}

	public double getCriticalDefenceC() {
		return this.criticalDefenceC;
	}

	// 会心防御X，激活本源时元神使用，激活元神时本源使用

	protected transient int criticalDefenceX;

	public void setCriticalDefenceX(int value) {
		this.criticalDefenceX = value;
		selfMarks[95] = true;
	}

	public int getCriticalDefenceX() {
		return this.criticalDefenceX;
	}

	// 会心防御率，该值是会心防御率加成，0~1000 表示千分率

	protected transient int criticalDefenceRateOther;

	public void setCriticalDefenceRateOther(int value) {
		this.criticalDefenceRateOther = value;
	}

	public int getCriticalDefenceRateOther() {
		return this.criticalDefenceRateOther;
	}

	// 会心防御率

	protected transient int criticalDefenceRate;

	public void setCriticalDefenceRate(int value) {
		this.criticalDefenceRate = value;
		selfMarks[96] = true;
	}

	public int getCriticalDefenceRate() {
		return this.criticalDefenceRate;
	}

	// 暴击率

	protected transient int criticalHitRate;

	public void setCriticalHitRate(int value) {
		this.criticalHitRate = value;
		selfMarks[97] = true;
	}

	public int getCriticalHitRate() {
		return this.criticalHitRate;
	}

	// 韧性，克制对方暴击的能力

	protected transient int toughness;

	public void setToughness(int value) {
		this.toughness = value;
	}

	public int getToughness() {
		return this.toughness;
	}

	// 反噬伤害的百分比，缺省为零

	protected transient int ironMaidenPercent;
	
	transient long honorPoint;

	public void setIronMaidenPercent(int value) {
		this.ironMaidenPercent = value;
	}

	public int getIronMaidenPercent() {
		return this.ironMaidenPercent;
	}

	// 攻击时吸取生命值的百分比，吸取的生命值加到攻击者身上

	protected transient int hpStealPercent;

	public void setHpStealPercent(int value) {
		this.hpStealPercent = value;
	}

	public int getHpStealPercent() {
		return this.hpStealPercent;
	}

	// 攻击时吸取魔法值的百分比，吸取的魔法值加到攻击者身上

	protected transient int mpStealPercent;

	public void setMpStealPercent(int value) {
		this.mpStealPercent = value;
	}

	public int getMpStealPercent() {
		return this.mpStealPercent;
	}

	// 幸运值

	protected transient int luck;

	public void setLuck(int value) {
		this.luck = value;
	}

	public int getLuck() {
		return this.luck;
	}

	protected transient int luckA;

	public void setLuckA(int value) {
		this.luckA = value;
	}

	public int getLuckA() {
		return this.luckA;
	}

	protected transient int luckB;

	public void setLuckB(int value) {
		this.luckB = value;
	}

	public int getLuckB() {
		return this.luckB;
	}

	// 经验值获得的百分比，缺省为100

	protected transient int expPercent;

	public void setExpPercent(int value) {
		this.expPercent = value;
	}

	public int getExpPercent() {
		return this.expPercent;
	}

	// 未分配的属性点

	protected transient int unallocatedPropertyPoint;

	public void setUnallocatedPropertyPoint(int value) {
		this.unallocatedPropertyPoint = value;
	}

	public int getUnallocatedPropertyPoint() {
		return this.unallocatedPropertyPoint;
	}

	// 未分配的技能点

	protected transient int unallocatedSkillPoint;

	public void setUnallocatedSkillPoint(int value) {
		this.unallocatedSkillPoint = value;
		selfMarks[98] = true;
	}

	public int getUnallocatedSkillPoint() {
		return this.unallocatedSkillPoint;
	}

	// 职业基本技能等级

	protected transient byte[] careerBasicSkillsLevels;

	public void setCareerBasicSkillsLevels(byte[] value) {
		this.careerBasicSkillsLevels = value;
		selfMarks[99] = true;
	}

	public byte[] getCareerBasicSkillsLevels() {
		return this.careerBasicSkillsLevels;
	}

	// 玩家掌握的第一系技能等级，0表示未掌握，下标表示职业中的第几个技能

	protected transient byte[] skillOneLevels;

	public void setSkillOneLevels(byte[] value) {
		this.skillOneLevels = value;
		selfMarks[100] = true;
	}

	public byte[] getSkillOneLevels() {
		return this.skillOneLevels;
	}

	// 玩家掌握的第二系技能等级，0表示未掌握，下标表示职业中的第几个技能

	protected transient byte[] skillTwoLevels;

	public void setSkillTwoLevels(byte[] value) {
		this.skillTwoLevels = value;
		selfMarks[101] = true;
	}

	public byte[] getSkillTwoLevels() {
		return this.skillTwoLevels;
	}

	// 怒气技能等级

	protected byte[] nuqiSkillsLevels;

	public void setNuqiSkillsLevels(byte[] value) {
		this.nuqiSkillsLevels = value;
		selfMarks[102] = true;
	}

	public byte[] getNuqiSkillsLevels() {
		return this.nuqiSkillsLevels;
	}

	// 心法等级

	protected byte[] xinfaLevels;

	public void setXinfaLevels(byte[] value) {
		this.xinfaLevels = value;
		selfMarks[103] = true;
	}

	public byte[] getXinfaLevels() {
		return this.xinfaLevels;
	}

	// 当前启用的光环技能编号

	protected transient int openedAuraSkillID;

	public void setOpenedAuraSkillID(int value) {
		this.openedAuraSkillID = value;
		selfMarks[104] = true;
	}

	public int getOpenedAuraSkillID() {
		return this.openedAuraSkillID;
	}

	// 表明当前启用的是哪一套装备

	protected byte currentSuit;

	public void setCurrentSuit(byte value) {
		this.currentSuit = value;
		selfMarks[105] = true;
	}

	public byte getCurrentSuit() {
		return this.currentSuit;
	}

	// 标注玩家的组队状态：队长、队员、未组队

	protected transient byte teamMark;

	public void setTeamMark(byte value) {
		this.teamMark = value;
		aroundMarks[40] = true;
	}

	public byte getTeamMark() {
		return this.teamMark;
	}

	// 回城点所在的地图

	protected String homeMapName = "";

	public void setHomeMapName(String value) {
		this.homeMapName = value;
	}

	public String getHomeMapName() {
		return this.homeMapName;
	}

	// 回城点的X坐标

	protected int homeX;

	public void setHomeX(int value) {
		this.homeX = value;
	}

	public int getHomeX() {
		return this.homeX;
	}

	// 回城点的Y坐标

	protected int homeY;

	public void setHomeY(int value) {
		this.homeY = value;
	}

	public int getHomeY() {
		return this.homeY;
	}

	// 复活点所在的地图

	protected String resurrectionMapName = "";

	public void setResurrectionMapName(String value) {
		this.resurrectionMapName = value;
	}

	public String getResurrectionMapName() {
		return this.resurrectionMapName;
	}

	// 复活点的X坐标

	protected int resurrectionX;

	public void setResurrectionX(int value) {
		this.resurrectionX = value;
	}

	public int getResurrectionX() {
		return this.resurrectionX;
	}

	// 复活点的Y坐标

	protected int resurrectionY;

	public void setResurrectionY(int value) {
		this.resurrectionY = value;
	}

	public int getResurrectionY() {
		return this.resurrectionY;
	}

	// 世界地图上的X坐标

	protected transient int worldMapX;

	public void setWorldMapX(int value) {
		this.worldMapX = value;
	}

	public int getWorldMapX() {
		return this.worldMapX;
	}

	// 世界地图上的Y坐标

	protected transient int worldMapY;

	public void setWorldMapY(int value) {
		this.worldMapY = value;
	}

	public int getWorldMapY() {
		return this.worldMapY;
	}

	// 下线时间
	@SimpleColumn(saveInterval = 600)
	protected long quitGameTime;

	public void setQuitGameTime(long value) {
		this.quitGameTime = value;
	}

	public long getQuitGameTime() {
		return this.quitGameTime;
	}

	// 累积的总离线时间（包含可转化时间）
	@SimpleColumn(saveInterval = 600)
	protected long totalOfflineTime;

	public void setTotalOfflineTime(long value) {
		this.totalOfflineTime = value;
	}

	public long getTotalOfflineTime() {
		return this.totalOfflineTime;
	}

	// 玩家所加入的公会名称

	protected transient String gangName = "";

	public void setGangName(String value) {
		this.gangName = value;
		aroundMarks[41] = true;
	}

	public String getGangName() {
		return this.gangName;
	}

	// 玩家在公会中的职位

	protected transient byte gangTitle;

	public void setGangTitle(byte value) {
		this.gangTitle = value;
		aroundMarks[42] = true;
	}

	public byte getGangTitle() {
		return this.gangTitle;
	}

	// 玩家在公会中的贡献度

	protected transient int gangContribution;

	public void setGangContribution(int value) {
		this.gangContribution = value;
		selfMarks[106] = true;
	}

	public int getGangContribution() {
		return this.gangContribution;
	}

	// 标记是否有新邮件

	protected transient boolean mailMark;

	public void setMailMark(boolean value) {
		this.mailMark = value;
		selfMarks[107] = true;
	}

	public boolean isMailMark() {
		return this.mailMark;
	}

	// 新邮件数量

	protected transient int newMailCount;

	public void setNewMailCount(int value) {
		this.newMailCount = value;
		selfMarks[108] = true;
	}

	public int getNewMailCount() {
		return this.newMailCount;
	}

	// 标记用户是否在战场中

	protected transient boolean inBattleField;

	public void setInBattleField(boolean value) {
		this.inBattleField = value;
		aroundMarks[43] = true;
	}

	public boolean isInBattleField() {
		return this.inBattleField;
	}

	// 玩家在战场中的哪一方，0标识中立，1标识A方，2标识B方

	protected transient byte battleFieldSide;

	public void setBattleFieldSide(byte value) {
		this.battleFieldSide = value;
		aroundMarks[44] = true;
	}

	public byte getBattleFieldSide() {
		return this.battleFieldSide;
	}

	// 境界

	protected short classLevel;

	public void setClassLevel(short value) {
		this.classLevel = value;
		aroundMarks[45] = true;
	}

	public short getClassLevel() {
		return this.classLevel;
	}

	// pk模式，分为和平，全体，组队，家族，宗派，国家，盟国

	protected byte pkMode;

	public void setPkMode(byte value) {
		this.pkMode = value;
		selfMarks[109] = true;
	}

	public byte getPkMode() {
		return this.pkMode;
	}

	// 罪恶值
	@SimpleColumn(saveInterval = 600)
	protected int evil;

	public void setEvil(int value) {
		this.evil = value;
		selfMarks[110] = true;
	}

	public int getEvil() {
		return this.evil;
	}

	// 名字颜色类型

	protected transient byte nameColorType;

	public void setNameColorType(byte value) {
		this.nameColorType = value;
		aroundMarks[46] = true;
	}

	public byte getNameColorType() {
		return this.nameColorType;
	}

	// 家族id

	protected long jiazuId;

	public void setJiazuId(long value) {
		this.jiazuId = value;
		selfMarks[111] = true;
	}

	public long getJiazuId() {
		return this.jiazuId;
	}

	// 家族称号

	protected transient String jiazuTitle = "";

	public void setJiazuTitle(String value) {
		this.jiazuTitle = value;
		aroundMarks[47] = true;
	}

	public String getJiazuTitle() {
		return this.jiazuTitle;
	}

	// 家族徽章图标

	protected transient String jiazuIcon = "";

	public void setJiazuIcon(String value) {
		this.jiazuIcon = value;
		aroundMarks[48] = true;
	}

	public String getJiazuIcon() {
		return this.jiazuIcon;
	}

	// 家族的名字

	protected transient String jiazuName = "";

	public void setJiazuName(String value) {
		this.jiazuName = value;
		aroundMarks[49] = true;
	}

	public String getJiazuName() {
		return this.jiazuName;
	}

	// 宗派的名字

	protected transient String zongPaiName = "";

	public void setZongPaiName(String value) {
		this.zongPaiName = value;
		aroundMarks[50] = true;
		if (value != null && !value.isEmpty()) {
			try {
				EventWithObjParam evt2 = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { this.getId(), RecordAction.加入宗派次数, 1L });
				EventRouter.getInst().addEvent(evt2);
			} catch (Exception eex) {
				PlayerAimManager.logger.error("[目标系统] [统计加入宗派次数异常] [" + this.getId() + "] [ " + this.getName() + "]", eex);
			}
		}
	}

	public String getZongPaiName() {
		return this.zongPaiName;
	}

	// 称号

	protected transient String title = "";
	// 称号加的buff名
	private transient String titleBuffName = "";

	public void setTitle(String value) {
		this.title = value;
		aroundMarks[51] = true;
	}

	public String getTitle() {
		return this.title;
	}

	// 传功状态

	protected transient boolean chuangongState;

	public void setChuangongState(boolean value) {
		this.chuangongState = value;
		aroundMarks[52] = true;
	}

	public boolean isChuangongState() {
		return this.chuangongState;
	}

	// 完成境界任务次数

	protected int deliverBournTaskNum;

	public void setDeliverBournTaskNum(int value) {
		this.deliverBournTaskNum = value;
		selfMarks[112] = true;
	}

	public int getDeliverBournTaskNum() {
		return this.deliverBournTaskNum;
	}

	// 最后一次境界任务完成时间

	protected long lastDeliverTimeOfBournTask;

	public void setLastDeliverTimeOfBournTask(long value) {
		this.lastDeliverTimeOfBournTask = value;
		selfMarks[113] = true;
	}

	public long getLastDeliverTimeOfBournTask() {
		return this.lastDeliverTimeOfBournTask;
	}

	// 剩余境界打坐时间

	protected long leftZazenTime;

	public void setLeftZazenTime(long value) {
		this.leftZazenTime = value;
		selfMarks[114] = true;
	}

	public long getLeftZazenTime() {
		return this.leftZazenTime;
	}

	// 是否在境界打坐状态

	protected transient boolean zazening;

	public void setZazening(boolean value) {
		this.zazening = value;
		selfMarks[115] = true;
	}

	public boolean isZazening() {
		return this.zazening;
	}

	// 当前境界任务上限

	protected int maxBournTaskNum;

	public void setMaxBournTaskNum(int value) {
		this.maxBournTaskNum = value;
		selfMarks[116] = true;
	}

	public int getMaxBournTaskNum() {
		return this.maxBournTaskNum;
	}

	// 当前境界经验

	protected int bournExp;

	public void setBournExp(int value) {
		this.bournExp = value;
		selfMarks[117] = true;
	}

	public int getBournExp() {
		return this.bournExp;
	}

	// 封印状态

	protected transient boolean sealState;

	public void setSealState(boolean value) {
		this.sealState = value;
		selfMarks[118] = true;
	}

	public boolean isSealState() {
		return this.sealState;
	}

	// 正在释放技能，用于正在释放暴风雪技能，该状态改变后暴风雪消失

	protected transient boolean skillUsingState;

	public void setSkillUsingState(boolean value) {
		this.skillUsingState = value;
		aroundMarks[53] = true;
	}

	public boolean isSkillUsingState() {
		return this.skillUsingState;
	}

	// 冰冻状态，处于冰冻状态中，所有伤害免疫，但同时也不能移动和释放技能

	protected transient boolean iceState;

	public void setIceState(boolean value) {
		this.iceState = value;
		aroundMarks[54] = true;
	}

	public boolean isIceState() {
		return this.iceState;
	}

	// 武器的粒子效果，如果为空字符串表示武器没有粒子效果

	protected transient String weaponParticle = "";

	public void setWeaponParticle(String value) {
		this.weaponParticle = value;
		aroundMarks[55] = true;
	}

	public String getWeaponParticle() {
		return this.weaponParticle;
	}

	// 身上的套装粒子效果

	protected transient String suitBodyParticle = "";

	public void setSuitBodyParticle(String value) {
		this.suitBodyParticle = value;
		aroundMarks[56] = true;
	}

	public String getSuitBodyParticle() {
		return this.suitBodyParticle;
	}

	// 脚下的套装粒子效果

	protected transient String suitFootParticle = "";

	public void setSuitFootParticle(String value) {
		this.suitFootParticle = value;
		aroundMarks[57] = true;
	}

	public String getSuitFootParticle() {
		return this.suitFootParticle;
	}

	// 坐骑的粒子效果，可以空字符串

	protected transient String horseParticle = "";

	public void setHorseParticle(String value) {
		this.horseParticle = value;
		aroundMarks[58] = true;
	}

	public String getHorseParticle() {
		return this.horseParticle;
	}

	// 家族是否有宣战

	protected transient boolean jiazuXuanZhanFlag;

	public void setJiazuXuanZhanFlag(boolean value) {
		this.jiazuXuanZhanFlag = value;
		selfMarks[119] = true;
	}

	public boolean isJiazuXuanZhanFlag() {
		return this.jiazuXuanZhanFlag;
	}

	// 宣战家族的id

	protected transient long[] jiazuXuanZhanData = new long[0];

	public void setJiazuXuanZhanData(long[] value) {
		this.jiazuXuanZhanData = value;
		selfMarks[120] = true;
	}

	public long[] getJiazuXuanZhanData() {
		return this.jiazuXuanZhanData;
	}

	// 当前地图归属

	protected int currentGameCountry;

	public void setCurrentGameCountry(int value) {
		this.currentGameCountry = value;
		selfMarks[121] = true;
	}

	public int getCurrentGameCountry() {
		return this.currentGameCountry;
	}

	// 刺探状态级别，用于显示刺探颜色

	protected transient byte citanStateLevel;

	public void setCitanStateLevel(byte value) {
		this.citanStateLevel = value;
		aroundMarks[59] = true;
	}

	public byte getCitanStateLevel() {
		return this.citanStateLevel;
	}

	// 偷砖状态级别，用于显示偷砖颜色

	protected transient byte touzhuanStateLevel;

	public void setTouzhuanStateLevel(byte value) {
		this.touzhuanStateLevel = value;
		aroundMarks[60] = true;
	}

	public byte getTouzhuanStateLevel() {
		return this.touzhuanStateLevel;
	}

	// 大小变化值，因为协议不支持float，所以定1000表示大小不变，500表示50%，2000表示200%

	protected transient short objectScale;

	public void setObjectScale(short value) {
		this.objectScale = value;
		aroundMarks[61] = true;
	}

	public short getObjectScale() {
		return this.objectScale;
	}

	// 生物颜色值

	protected transient int objectColor;

	public void setObjectColor(int value) {
		this.objectColor = value;
		aroundMarks[62] = true;
	}

	public int getObjectColor() {
		return this.objectColor;
	}

	// 透明状态，处于透明状态中，怪物无法攻击，只有队友和自己看是半透，其他人看都是全透

	protected transient boolean objectOpacity;

	public void setObjectOpacity(boolean value) {
		this.objectOpacity = value;
		aroundMarks[63] = true;
	}

	public boolean isObjectOpacity() {
		return this.objectOpacity;
	}

	// 文采

	protected int culture;

	public void setCulture(int value) {
		this.culture = value;
		selfMarks[122] = true;
	}

	public int getCulture() {
		return this.culture;
	}

	// 坐的状态true为坐

	protected transient boolean sitdownState;

	public void setSitdownState(boolean value) {
		this.sitdownState = value;
		aroundMarks[64] = true;
	}

	public boolean isSitdownState() {
		return this.sitdownState;
	}

	// 配偶名字

	protected transient String spouse = "";

	public void setSpouse(String value) {
		this.spouse = value;
		aroundMarks[65] = true;
	}

	public String getSpouse() {
		return this.spouse;
	}

	// 喝酒的状态

	protected transient byte beerState;

	public void setBeerState(byte value) {
		this.beerState = value;
		aroundMarks[66] = true;
	}

	public byte getBeerState() {
		return this.beerState;
	}

	// 城战状态，0表示正常，1表示进攻，2表示防守

	protected transient byte cityFightSide;

	public void setCityFightSide(byte value) {
		this.cityFightSide = value;
		aroundMarks[67] = true;
	}

	public byte getCityFightSide() {
		return this.cityFightSide;
	}

	// 国战状态

	protected transient boolean isGuozhan;

	public void setIsGuozhan(boolean value) {
		this.isGuozhan = value;
		aroundMarks[68] = true;
	}

	public boolean isIsGuozhan() {
		return this.isGuozhan;
	}

	// 国战星星

	protected transient int guozhanLevel;

	public void setGuozhanLevel(int value) {
		this.guozhanLevel = value;
		aroundMarks[69] = true;
	}

	public int getGuozhanLevel() {
		return this.guozhanLevel;
	}

	// 历练

	protected long lilian;

	public void setLilian(long value) {
		this.lilian = value;
		selfMarks[123] = true;
	}

	public long getLilian() {
		return this.lilian;
	}

	// 是否可以开启随身修理

	protected transient boolean repairCarry;

	public void setRepairCarry(boolean value) {
		this.repairCarry = value;
		selfMarks[124] = true;
	}

	public boolean isRepairCarry() {
		return this.repairCarry;
	}

	// 是否可以开启随身邮件

	protected transient boolean mailCarry;

	public void setMailCarry(boolean value) {
		this.mailCarry = value;
		selfMarks[125] = true;
	}

	public boolean isMailCarry() {
		return this.mailCarry;
	}

	// 是否可以开启随身仓库

	protected transient boolean wareHouseCarry;

	public void setWareHouseCarry(boolean value) {
		this.wareHouseCarry = value;
		selfMarks[126] = true;
	}

	public boolean isWareHouseCarry() {
		return this.wareHouseCarry;
	}

	// 是否可以开启随身求购

	protected transient boolean quickBuyCarry;

	public void setQuickBuyCarry(boolean value) {
		this.quickBuyCarry = value;
		selfMarks[127] = true;
	}

	public boolean isQuickBuyCarry() {
		return this.quickBuyCarry;
	}

	// 是否开启vip经验树种植

	protected transient boolean vipJingYanShuFlag;

	public void setVipJingYanShuFlag(boolean value) {
		this.vipJingYanShuFlag = value;
		selfMarks[128] = true;
	}

	public boolean isVipJingYanShuFlag() {
		return this.vipJingYanShuFlag;
	}

	// 是否开启vip摇钱树种植

	protected transient boolean vipYaoQianShuFlag;

	public void setVipYaoQianShuFlag(boolean value) {
		this.vipYaoQianShuFlag = value;
		selfMarks[129] = true;
	}

	public boolean isVipYaoQianShuFlag() {
		return this.vipYaoQianShuFlag;
	}

	// 是否开启vip中级炼妖

	protected transient boolean vipZhongJiLianYaoFlag;

	public void setVipZhongJiLianYaoFlag(boolean value) {
		this.vipZhongJiLianYaoFlag = value;
		selfMarks[130] = true;
	}

	public boolean isVipZhongJiLianYaoFlag() {
		return this.vipZhongJiLianYaoFlag;
	}

	// 是否开启vip刷新境界杂念

	protected transient boolean vipJingJieZaNianFlushFlag;

	public void setVipJingJieZaNianFlushFlag(boolean value) {
		this.vipJingJieZaNianFlushFlag = value;
		selfMarks[131] = true;
	}

	public boolean isVipJingJieZaNianFlushFlag() {
		return this.vipJingJieZaNianFlushFlag;
	}

	// 是否开启vip仙府鬼斧神工令

	protected transient boolean vipXianFuLingFlag;

	public void setVipXianFuLingFlag(boolean value) {
		this.vipXianFuLingFlag = value;
		selfMarks[132] = true;
	}

	public boolean isVipXianFuLingFlag() {
		return this.vipXianFuLingFlag;
	}

	// 是否开启vip捐献家族资金

	protected transient boolean vipJuanXianZiJinFlag;

	public void setVipJuanXianZiJinFlag(boolean value) {
		this.vipJuanXianZiJinFlag = value;
		selfMarks[133] = true;
	}

	public boolean isVipJuanXianZiJinFlag() {
		return this.vipJuanXianZiJinFlag;
	}

	// 是否开启vip幽冥幻域刷新

	protected transient boolean vipTowerFlushFlag;

	public void setVipTowerFlushFlag(boolean value) {
		this.vipTowerFlushFlag = value;
		selfMarks[134] = true;
	}

	public boolean isVipTowerFlushFlag() {
		return this.vipTowerFlushFlag;
	}

	// 是否开启vip高级炼妖

	protected transient boolean vipGaoJiLianYaoFlag;

	public void setVipGaoJiLianYaoFlag(boolean value) {
		this.vipGaoJiLianYaoFlag = value;
		selfMarks[135] = true;
	}

	public boolean isVipGaoJiLianYaoFlag() {
		return this.vipGaoJiLianYaoFlag;
	}

	// vip已经领取的vip奖励级别(每一级vip只能领取一次，客户端可以根据这个级别来显示领取奖励按钮)

	protected byte vipPickedRewardLevel;

	public void setVipPickedRewardLevel(byte value) {
		this.vipPickedRewardLevel = value;
		selfMarks[136] = true;
	}

	public byte getVipPickedRewardLevel() {
		return this.vipPickedRewardLevel;
	}

	/**
	 * 变身技能豆
	 */
	public transient int skillPoints = 3;

	/**
	 * 0:人形态，1:兽形态
	 */
	public transient volatile int shouStat;

	/**
	 * 变身技能等级
	 */
	protected transient byte[] bianShenLevels;

	public int getSkillPoints() {
		return this.skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
		selfMarks[138] = true;
	}

	public int getShouStat() {
		return this.shouStat;
	}

	public void setShouStat(int shouStat) {
		this.shouStat = shouStat;
		selfMarks[139] = true;
	}

	public byte[] getBianShenLevels() {
		return this.bianShenLevels;
	}
	
	public void changeBianShenLvs() {
		selfMarks[140] = true;
	}
	
	public void setBianShenLevels(byte[] bianShenLevels) {
		this.bianShenLevels = bianShenLevels;
		selfMarks[140] = true;
	}

	private transient boolean[] aroundMarks = new boolean[74];

	public boolean[] getAroundMarks() {
		return aroundMarks;
	}

	public Object getValue(int id) {
		switch (id) {
		case 0:
			return country;
		case 1:
			return countryPosition;
		case 2:
			return career;
		case 3:
			return level;
		case 4:
			return soulLevel;
		case 5:
			return vipLevel;
		case 6:
			return boothState;
		case 7:
			return boothName;
		case 8:
			return weaponType;
		case 9:
			return avataType;
		case 10:
			return avata;
		case 11:
			return avataPropsId;
		case 12:
			return aura;
		case 13:
			return encloser;
		case 14:
			return isUpOrDown;
		case 15:
			return flying;
		case 16:
			return hold;
		case 17:
			return stun;
		case 18:
			return immunity;
		case 19:
			return poison;
		case 20:
			return weak;
		case 21:
			return invulnerable;
		case 22:
			return cure;
		case 23:
			return silence;
		case 24:
			return liuxueState;
		case 25:
			return jiansuState;
		case 26:
			return pojiaState;
		case 27:
			return zhuoreState;
		case 28:
			return hanlengState;
		case 29:
			return tigaoBaojiState;
		case 30:
			return tigaoWaigongState;
		case 31:
			return tigaoNeigongState;
		case 32:
			return tigaoWaifangState;
		case 33:
			return tigaoNeifangState;
		case 34:
			return shield;
		case 35:
			return hp;
		case 36:
			return mp;
		case 37:
			return maxHP;
		case 38:
			return maxMP;
		case 39:
			return speed;
		case 40:
			return teamMark;
		case 41:
			return gangName;
		case 42:
			return gangTitle;
		case 43:
			return inBattleField;
		case 44:
			return battleFieldSide;
		case 45:
			return classLevel;
		case 46:
			return this.getNameColorType();
		case 47:
			return jiazuTitle;
		case 48:
			return jiazuIcon;
		case 49:
			return jiazuName;
		case 50:
			return zongPaiName;
		case 51:
			return title;
		case 52:
			return chuangongState;
		case 53:
			return skillUsingState;
		case 54:
			return iceState;
		case 55:
			return weaponParticle;
		case 56:
			return suitBodyParticle;
		case 57:
			return suitFootParticle;
		case 58:
			return horseParticle;
		case 59:
			return citanStateLevel;
		case 60:
			return touzhuanStateLevel;
		case 61:
			return objectScale;
		case 62:
			return objectColor;
		case 63:
			return objectOpacity;
		case 64:
			return sitdownState;
		case 65:
			return spouse;
		case 66:
			return beerState;
		case 67:
			return cityFightSide;
		case 68:
			return isGuozhan;
		case 69:
			return guozhanLevel;
		case 70:
			return ChestFightManager.inst.getChestStateByPlayer(this);
		case 71:
			return name;
		case 72:
			return vitality;	
		case 73:
			return maxVitality;
		default:
			break;
		}
		return null;
	}

	public void setValue(int id, Object value) {
		switch (id) {
		case 0:
			setCountry(((Byte) value).byteValue());
			break;
		case 1:
			setCountryPosition(((Integer) value).intValue());
			break;
		case 2:
			setCareer(((Byte) value).byteValue());
			break;
		case 3:
			setLevel(((Integer) value).intValue());
			break;
		case 4:
			setSoulLevel(((Integer) value).intValue());
			break;
		case 5:
			setVipLevel(((Byte) value).byteValue());
			break;
		case 6:
			setBoothState(((Boolean) value).booleanValue());
			break;
		case 7:
			setBoothName((String) value);
			break;
		case 8:
			setWeaponType(((Byte) value).byteValue());
			break;
		case 9:
			setAvataType((byte[]) value);
			break;
		case 10:
			setAvata((String[]) value);
			break;
		case 11:
			setAvataPropsId(((Long) value).longValue());
			break;
		case 12:
			setAura(((Byte) value).byteValue());
			break;
		case 13:
			setEncloser(((Byte) value).byteValue());
			break;
		case 14:
			setIsUpOrDown(((Boolean) value).booleanValue());
			break;
		case 15:
			setFlying(((Boolean) value).booleanValue());
			break;
		case 16:
			setHold(((Boolean) value).booleanValue());
			break;
		case 17:
			setStun(((Boolean) value).booleanValue());
			break;
		case 18:
			setImmunity(((Boolean) value).booleanValue());
			break;
		case 19:
			setPoison(((Boolean) value).booleanValue());
			break;
		case 20:
			setWeak(((Boolean) value).booleanValue());
			break;
		case 21:
			setInvulnerable(((Boolean) value).booleanValue());
			break;
		case 22:
			setCure(((Boolean) value).booleanValue());
			break;
		case 23:
			setSilence(((Boolean) value).booleanValue());
			break;
		case 24:
			setLiuxueState(((Boolean) value).booleanValue());
			break;
		case 25:
			setJiansuState(((Boolean) value).booleanValue());
			break;
		case 26:
			setPojiaState(((Boolean) value).booleanValue());
			break;
		case 27:
			setZhuoreState(((Boolean) value).booleanValue());
			break;
		case 28:
			setHanlengState(((Boolean) value).booleanValue());
			break;
		case 29:
			setTigaoBaojiState(((Boolean) value).booleanValue());
			break;
		case 30:
			setTigaoWaigongState(((Boolean) value).booleanValue());
			break;
		case 31:
			setTigaoNeigongState(((Boolean) value).booleanValue());
			break;
		case 32:
			setTigaoWaifangState(((Boolean) value).booleanValue());
			break;
		case 33:
			setTigaoNeifangState(((Boolean) value).booleanValue());
			break;
		case 34:
			setShield(((Byte) value).byteValue());
			break;
		case 35:
			setHp(((Integer) value).intValue());
			break;
		case 36:
			setMp(((Integer) value).intValue());
			break;
		case 37:
			setMaxHP(((Integer) value).intValue());
			break;
		case 38:
			setMaxMP(((Integer) value).intValue());
			break;
		case 39:
			setSpeed(((Integer) value).intValue());
			break;
		case 40:
			setTeamMark(((Byte) value).byteValue());
			break;
		case 41:
			setGangName((String) value);
			break;
		case 42:
			setGangTitle(((Byte) value).byteValue());
			break;
		case 43:
			setInBattleField(((Boolean) value).booleanValue());
			break;
		case 44:
			setBattleFieldSide(((Byte) value).byteValue());
			break;
		case 45:
			setClassLevel(((Short) value).shortValue());
			break;
		case 46:
			setNameColorType(((Byte) value).byteValue());
			break;
		case 47:
			setJiazuTitle((String) value);
			break;
		case 48:
			setJiazuIcon((String) value);
			break;
		case 49:
			setJiazuName((String) value);
			break;
		case 50:
			setZongPaiName((String) value);
			break;
		case 51:
			setTitle((String) value);
			break;
		case 52:
			setChuangongState(((Boolean) value).booleanValue());
			break;
		case 53:
			setSkillUsingState(((Boolean) value).booleanValue());
			break;
		case 54:
			setIceState(((Boolean) value).booleanValue());
			break;
		case 55:
			setWeaponParticle((String) value);
			break;
		case 56:
			setSuitBodyParticle((String) value);
			break;
		case 57:
			setSuitFootParticle((String) value);
			break;
		case 58:
			setHorseParticle((String) value);
			break;
		case 59:
			setCitanStateLevel(((Byte) value).byteValue());
			break;
		case 60:
			setTouzhuanStateLevel(((Byte) value).byteValue());
			break;
		case 61:
			setObjectScale(((Short) value).shortValue());
			break;
		case 62:
			setObjectColor(((Integer) value).intValue());
			break;
		case 63:
			setObjectOpacity(((Boolean) value).booleanValue());
			break;
		case 64:
			setSitdownState(((Boolean) value).booleanValue());
			break;
		case 65:
			setSpouse((String) value);
			break;
		case 66:
			setBeerState(((Byte) value).byteValue());
			break;
		case 67:
			setCityFightSide(((Byte) value).byteValue());
			break;
		case 68:
			setIsGuozhan(((Boolean) value).booleanValue());
			break;
		case 69:
			setGuozhanLevel(((Integer) value).intValue());
			break;
		case 72:
			setVitality(((Integer) value).intValue());
			break;
		case 73:
			setMaxVitality(((Integer) value).intValue());
			break;
		default:
			break;
		}
	}

	public void clear() {
		for (int i = aroundMarks.length - 1; i >= 0; i--) {
			aroundMarks[i] = false;
		}
	}

	public transient boolean[] selfMarks = new boolean[144];

	public boolean[] getSelfMarks() {
		return selfMarks;
	}

	public void setChestType() {
		aroundMarks[70] = true;
	}

	public void setParaticeValue() {
		selfMarks[137] = true;
	}

	public void setHonorPoint(long honorPoint){
		this.honorPoint = honorPoint;
		selfMarks[143] = true;
	}
	
	public Object getSelfValue(int id) {
		switch (id) {
		case 0:
			return careerContribution;
		case 1:
			return RMB;
		case 2:
			return totalRmbyuanbao;
		case 3:
			return bindSilver;
		case 4:
			return silver;
		case 5:
			return todayUsedBindSilver;
		case 6:
			return wage;
		case 7:
			return vitality;
		case 8:
			return maxVitality;
		case 9:
			return energy;
		case 10:
			return gongxun;
		case 11:
			return exp;
		case 12:
			return nextLevelExp;
		case 13:
			return nextLevelExpPool;
		case 14:
			return commonAttackSpeed;
		case 15:
			return commonAttackRange;
		case 16:
			return fighting;
		case 17:
			return baseMp;
		case 18:
			return maxHPX;
		case 19:
			return maxMPX;
		case 20:
			return hpRecoverBase;
		case 21:
			return mpRecoverBase;
		case 22:
			return xp;
		case 23:
			return totalXp;
		case 24:
			return strength;
		case 25:
			return strengthX;
		case 26:
			return spellpower;
		case 27:
			return spellpowerX;
		case 28:
			return dexterity;
		case 29:
			return dexterityX;
		case 30:
			return constitution;
		case 31:
			return constitutionX;
		case 32:
			return physicalDamageUpperLimit;
		case 33:
			return physicalDamageLowerLimit;
		case 34:
			return phyDefence;
		case 35:
			return phyDefenceRateOther;
		case 36:
			return phyDefenceX;
		case 37:
			int a = physicalDecrease+this.getPhyDefenceRateOther();
			if (a > 1000) {
				a = 1000;
			} else if (a < 0) {
				a = 0;
			}
			return a;
		case 38:
			return magicDefence;
		case 39:
			return magicDefenceRateOther;
		case 40:
			return magicDefenceX;
		case 41:
			int b = spellDecrease+this.getMagicDefenceRateOther();
			if (b > 1000) {
				b = 1000;
			} else if(b < 0){
				b = 0;
			}
			return b;
		case 42:
			return breakDefence;
		case 43:
			return breakDefenceX;
		case 44:
			return breakDefenceRate;
		case 45:
			return hit;
		case 46:
			return hitX;
		case 47:
			return hitRate>1000?1000:hitRate;
		case 48:
			return dodge;
		case 49:
			return dodgeX;
		case 50:
			return dodgeRate;
		case 51:
			return accurate;
		case 52:
			return accurateX;
		case 53:
			return accurateRate;
		case 54:
			return phyAttack;
		case 55:
			return phyAttackX;
		case 56:
			return magicAttack;
		case 57:
			return magicAttackX;
		case 58:
			return fireAttack;
		case 59:
			return fireAttackX;
		case 60:
			return fireDefence;
		case 61:
			return fireDefenceX;
		case 62:
			return fireDefenceRate;
		case 63:
			return fireIgnoreDefence;
		case 64:
			return fireIgnoreDefenceX;
		case 65:
			return fireIgnoreDefenceRate;
		case 66:
			return blizzardAttack;
		case 67:
			return blizzardAttackX;
		case 68:
			return blizzardDefence;
		case 69:
			return blizzardDefenceX;
		case 70:
			return blizzardDefenceRate;
		case 71:
			return blizzardIgnoreDefence;
		case 72:
			return blizzardIgnoreDefenceX;
		case 73:
			return blizzardIgnoreDefenceRate;
		case 74:
			return windAttack;
		case 75:
			return windAttackX;
		case 76:
			return windDefence;
		case 77:
			return windDefenceX;
		case 78:
			return windDefenceRate;
		case 79:
			return windIgnoreDefence;
		case 80:
			return windIgnoreDefenceX;
		case 81:
			return windIgnoreDefenceRate;
		case 82:
			return thunderAttack;
		case 83:
			return thunderAttackX;
		case 84:
			return thunderDefence;
		case 85:
			return thunderDefenceX;
		case 86:
			return thunderDefenceRate;
		case 87:
			return thunderIgnoreDefence;
		case 88:
			return thunderIgnoreDefenceX;
		case 89:
			return thunderIgnoreDefenceRate;
		case 90:
			return coolDownTimePercent;
		case 91:
			return setupTimePercent;
		case 92:
			return criticalHit;
		case 93:
			return criticalHitX;
		case 94:
			return criticalDefence;
		case 95:
			return criticalDefenceX;
		case 96:
			return criticalDefenceRate;
		case 97:
			return criticalHitRate + this.getCriticalHitRateOther();
		case 98:
			return unallocatedSkillPoint;
		case 99:
			return careerBasicSkillsLevels;
		case 100:
			return skillOneLevels;
		case 101:
			return skillTwoLevels;
		case 102:
			return nuqiSkillsLevels;
		case 103:
			return xinfaLevels;
		case 104:
			return openedAuraSkillID;
		case 105:
			return currentSuit;
		case 106:
			return gangContribution;
		case 107:
			return mailMark;
		case 108:
			return newMailCount;
		case 109:
			return pkMode;
		case 110:
			return evil;
		case 111:
			return jiazuId;
		case 112:
			return deliverBournTaskNum;
		case 113:
			return lastDeliverTimeOfBournTask;
		case 114:
			return leftZazenTime;
		case 115:
			return zazening;
		case 116:
			return maxBournTaskNum;
		case 117:
			return bournExp;
		case 118:
			return sealState;
		case 119:
			return jiazuXuanZhanFlag;
		case 120:
			return jiazuXuanZhanData;
		case 121:
			return currentGameCountry;
		case 122:
			return culture;
		case 123:
			return lilian;
		case 124:
			return repairCarry;
		case 125:
			return mailCarry;
		case 126:
			return wareHouseCarry;
		case 127:
			return quickBuyCarry;
		case 128:
			return vipJingYanShuFlag;
		case 129:
			return vipYaoQianShuFlag;
		case 130:
			return vipZhongJiLianYaoFlag;
		case 131:
			return vipJingJieZaNianFlushFlag;
		case 132:
			return vipXianFuLingFlag;
		case 133:
			return vipJuanXianZiJinFlag;
		case 134:
			return vipTowerFlushFlag;
		case 135:
			return vipGaoJiLianYaoFlag;
		case 136:
			return vipPickedRewardLevel;
		case 137:
			return this.得到家族修炼();
		case 138:
			return skillPoints;
		case 139:
			return shouStat;
		case 140:
			return bianShenLevels;
		case 141:
			return name;
		case 142:
			return getUnuseSoulLevel();
		case 143:
			return honorPoint;
		default:
			break;
		}
		return null;
	}
	
	public void changeSoulLv() {
		selfMarks[142] = true;
	}
	
	public int getUnuseSoulLevel(){
		return this.getLevel();
	}

	public long 得到家族修炼() {
		return 0;
	}

	public void setSelfValue(int id, Object value) {
		switch (id) {
		case 0:
			setCareerContribution(((Integer) value).intValue());
			break;
		case 1:
			setRMB(((Long) value).longValue());
			break;
		case 2:
			setTotalRmbyuanbao(((Long) value).longValue());
			break;
		case 3:
			setBindSilver(((Long) value).longValue());
			break;
		case 4:
			setSilver(((Long) value).longValue());
			break;
		case 5:
			setTodayUsedBindSilver(((Long) value).longValue());
			break;
		case 6:
			setWage(((Long) value).longValue());
			break;
		case 7:
			setVitality(((Integer) value).intValue());
			break;
		case 8:
			setMaxVitality(((Integer) value).intValue());
			break;
		case 9:
			setEnergy(((Integer) value).intValue());
			break;
		case 10:
			setGongxun(((Long) value).longValue());
			break;
		case 11:
			setExp(((Long) value).longValue());
			break;
		case 12:
			setNextLevelExp(((Long) value).longValue());
			break;
		case 13:
			setNextLevelExpPool(((Long) value).longValue());
			break;
		case 14:
			setCommonAttackSpeed(((Integer) value).intValue());
			break;
		case 15:
			setCommonAttackRange(((Integer) value).intValue());
			break;
		case 16:
			setFighting(((Boolean) value).booleanValue());
			break;
		case 17:
			setBaseMp(((Integer) value).intValue());
			break;
		case 18:
			setMaxHPX(((Integer) value).intValue());
			break;
		case 19:
			setMaxMPX(((Integer) value).intValue());
			break;
		case 20:
			setHpRecoverBase(((Integer) value).intValue());
			break;
		case 21:
			setMpRecoverBase(((Integer) value).intValue());
			break;
		case 22:
			setXp(((Integer) value).intValue());
			break;
		case 23:
			setTotalXp(((Integer) value).intValue());
			break;
		case 24:
			setStrength(((Integer) value).intValue());
			break;
		case 25:
			setStrengthX(((Integer) value).intValue());
			break;
		case 26:
			setSpellpower(((Integer) value).intValue());
			break;
		case 27:
			setSpellpowerX(((Integer) value).intValue());
			break;
		case 28:
			setDexterity(((Integer) value).intValue());
			break;
		case 29:
			setDexterityX(((Integer) value).intValue());
			break;
		case 30:
			setConstitution(((Integer) value).intValue());
			break;
		case 31:
			setConstitutionX(((Integer) value).intValue());
			break;
		case 32:
			setPhysicalDamageUpperLimit(((Integer) value).intValue());
			break;
		case 33:
			setPhysicalDamageLowerLimit(((Integer) value).intValue());
			break;
		case 34:
			setPhyDefence(((Integer) value).intValue());
			break;
		case 35:
			setPhyDefenceRateOther(((Integer) value).intValue());
			break;
		case 36:
			setPhyDefenceX(((Integer) value).intValue());
			break;
		case 37:
			setPhysicalDecrease(((Integer) value).intValue());
			break;
		case 38:
			setMagicDefence(((Integer) value).intValue());
			break;
		case 39:
			setMagicDefenceRateOther(((Integer) value).intValue());
			break;
		case 40:
			setMagicDefenceX(((Integer) value).intValue());
			break;
		case 41:
			setSpellDecrease(((Integer) value).intValue());
			break;
		case 42:
			setBreakDefence(((Integer) value).intValue());
			break;
		case 43:
			setBreakDefenceX(((Integer) value).intValue());
			break;
		case 44:
			setBreakDefenceRate(((Integer) value).intValue());
			break;
		case 45:
			setHit(((Integer) value).intValue());
			break;
		case 46:
			setHitX(((Integer) value).intValue());
			break;
		case 47:
			setHitRate(((Integer) value).intValue());
			break;
		case 48:
			setDodge(((Integer) value).intValue());
			break;
		case 49:
			setDodgeX(((Integer) value).intValue());
			break;
		case 50:
			setDodgeRate(((Integer) value).intValue());
			break;
		case 51:
			setAccurate(((Integer) value).intValue());
			break;
		case 52:
			setAccurateX(((Integer) value).intValue());
			break;
		case 53:
			setAccurateRate(((Integer) value).intValue());
			break;
		case 54:
			setPhyAttack(((Integer) value).intValue());
			break;
		case 55:
			setPhyAttackX(((Integer) value).intValue());
			break;
		case 56:
			setMagicAttack(((Integer) value).intValue());
			break;
		case 57:
			setMagicAttackX(((Integer) value).intValue());
			break;
		case 58:
			setFireAttack(((Integer) value).intValue());
			break;
		case 59:
			setFireAttackX(((Integer) value).intValue());
			break;
		case 60:
			setFireDefence(((Integer) value).intValue());
			break;
		case 61:
			setFireDefenceX(((Integer) value).intValue());
			break;
		case 62:
			setFireDefenceRate(((Integer) value).intValue());
			break;
		case 63:
			setFireIgnoreDefence(((Integer) value).intValue());
			break;
		case 64:
			setFireIgnoreDefenceX(((Integer) value).intValue());
			break;
		case 65:
			setFireIgnoreDefenceRate(((Integer) value).intValue());
			break;
		case 66:
			setBlizzardAttack(((Integer) value).intValue());
			break;
		case 67:
			setBlizzardAttackX(((Integer) value).intValue());
			break;
		case 68:
			setBlizzardDefence(((Integer) value).intValue());
			break;
		case 69:
			setBlizzardDefenceX(((Integer) value).intValue());
			break;
		case 70:
			setBlizzardDefenceRate(((Integer) value).intValue());
			break;
		case 71:
			setBlizzardIgnoreDefence(((Integer) value).intValue());
			break;
		case 72:
			setBlizzardIgnoreDefenceX(((Integer) value).intValue());
			break;
		case 73:
			setBlizzardIgnoreDefenceRate(((Integer) value).intValue());
			break;
		case 74:
			setWindAttack(((Integer) value).intValue());
			break;
		case 75:
			setWindAttackX(((Integer) value).intValue());
			break;
		case 76:
			setWindDefence(((Integer) value).intValue());
			break;
		case 77:
			setWindDefenceX(((Integer) value).intValue());
			break;
		case 78:
			setWindDefenceRate(((Integer) value).intValue());
			break;
		case 79:
			setWindIgnoreDefence(((Integer) value).intValue());
			break;
		case 80:
			setWindIgnoreDefenceX(((Integer) value).intValue());
			break;
		case 81:
			setWindIgnoreDefenceRate(((Integer) value).intValue());
			break;
		case 82:
			setThunderAttack(((Integer) value).intValue());
			break;
		case 83:
			setThunderAttackX(((Integer) value).intValue());
			break;
		case 84:
			setThunderDefence(((Integer) value).intValue());
			break;
		case 85:
			setThunderDefenceX(((Integer) value).intValue());
			break;
		case 86:
			setThunderDefenceRate(((Integer) value).intValue());
			break;
		case 87:
			setThunderIgnoreDefence(((Integer) value).intValue());
			break;
		case 88:
			setThunderIgnoreDefenceX(((Integer) value).intValue());
			break;
		case 89:
			setThunderIgnoreDefenceRate(((Integer) value).intValue());
			break;
		case 90:
			setCoolDownTimePercent(((Integer) value).intValue());
			break;
		case 91:
			setSetupTimePercent(((Integer) value).intValue());
			break;
		case 92:
			setCriticalHit(((Integer) value).intValue());
			break;
		case 93:
			setCriticalHitX(((Integer) value).intValue());
			break;
		case 94:
			setCriticalDefence(((Integer) value).intValue());
			break;
		case 95:
			setCriticalDefenceX(((Integer) value).intValue());
			break;
		case 96:
			setCriticalDefenceRate(((Integer) value).intValue());
			break;
		case 97:
			setCriticalHitRate(((Integer) value).intValue());
			break;
		case 98:
			setUnallocatedSkillPoint(((Integer) value).intValue());
			break;
		case 99:
			setCareerBasicSkillsLevels((byte[]) value);
			break;
		case 100:
			setSkillOneLevels((byte[]) value);
			break;
		case 101:
			setSkillTwoLevels((byte[]) value);
			break;
		case 102:
			setNuqiSkillsLevels((byte[]) value);
			break;
		case 103:
			setXinfaLevels((byte[]) value);
			break;
		case 104:
			setOpenedAuraSkillID(((Integer) value).intValue());
			break;
		case 105:
			setCurrentSuit(((Byte) value).byteValue());
			break;
		case 106:
			setGangContribution(((Integer) value).intValue());
			break;
		case 107:
			setMailMark(((Boolean) value).booleanValue());
			break;
		case 108:
			setNewMailCount(((Integer) value).intValue());
			break;
		case 109:
			setPkMode(((Byte) value).byteValue());
			break;
		case 110:
			setEvil(((Integer) value).intValue());
			break;
		case 111:
			setJiazuId(((Long) value).longValue());
			break;
		case 112:
			setDeliverBournTaskNum(((Integer) value).intValue());
			break;
		case 113:
			setLastDeliverTimeOfBournTask(((Long) value).longValue());
			break;
		case 114:
			setLeftZazenTime(((Long) value).longValue());
			break;
		case 115:
			setZazening(((Boolean) value).booleanValue());
			break;
		case 116:
			setMaxBournTaskNum(((Integer) value).intValue());
			break;
		case 117:
			setBournExp(((Integer) value).intValue());
			break;
		case 118:
			setSealState(((Boolean) value).booleanValue());
			break;
		case 119:
			setJiazuXuanZhanFlag(((Boolean) value).booleanValue());
			break;
		case 120:
			setJiazuXuanZhanData((long[]) value);
			break;
		case 121:
			setCurrentGameCountry(((Integer) value).intValue());
			break;
		case 122:
			setCulture(((Integer) value).intValue());
			break;
		case 123:
			setLilian(((Long) value).longValue());
			break;
		case 124:
			setRepairCarry(((Boolean) value).booleanValue());
			break;
		case 125:
			setMailCarry(((Boolean) value).booleanValue());
			break;
		case 126:
			setWareHouseCarry(((Boolean) value).booleanValue());
			break;
		case 127:
			setQuickBuyCarry(((Boolean) value).booleanValue());
			break;
		case 128:
			setVipJingYanShuFlag(((Boolean) value).booleanValue());
			break;
		case 129:
			setVipYaoQianShuFlag(((Boolean) value).booleanValue());
			break;
		case 130:
			setVipZhongJiLianYaoFlag(((Boolean) value).booleanValue());
			break;
		case 131:
			setVipJingJieZaNianFlushFlag(((Boolean) value).booleanValue());
			break;
		case 132:
			setVipXianFuLingFlag(((Boolean) value).booleanValue());
			break;
		case 133:
			setVipJuanXianZiJinFlag(((Boolean) value).booleanValue());
			break;
		case 134:
			setVipTowerFlushFlag(((Boolean) value).booleanValue());
			break;
		case 135:
			setVipGaoJiLianYaoFlag(((Boolean) value).booleanValue());
			break;
		case 136:
			setVipPickedRewardLevel(((Byte) value).byteValue());
			break;
		case 138:
			setSkillPoints(((Integer) value).byteValue());
			break;
		case 139:
			setShouStat(((Integer) value).byteValue());
			break;
		case 140:
			setBianShenLevels((byte[]) value);
			break;
		default:
			break;
		}
	}

	public void clearSelfMarks() {
		for (int i = selfMarks.length - 1; i >= 0; i--) {
			selfMarks[i] = false;
		}
	}

	public String getTitleBuffName() {
		return titleBuffName;
	}

	public void setTitleBuffName(String titleBuffName) {
		this.titleBuffName = titleBuffName;
	}

	public int getPhyAttackY() {
		return phyAttackY;
	}

	public void setPhyAttackY(int phyAttackY) {
		this.phyAttackY = phyAttackY;
	}

	public int getMagicDefenceY() {
		return magicDefenceY;
	}

	public void setMagicDefenceY(int magicDefenceY) {
		this.magicDefenceY = magicDefenceY;
	}

	public int getPhyDefenceY() {
		return phyDefenceY;
	}

	public void setPhyDefenceY(int phyDefenceY) {
		this.phyDefenceY = phyDefenceY;
	}
}
