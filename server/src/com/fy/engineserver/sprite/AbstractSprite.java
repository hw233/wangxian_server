package com.fy.engineserver.sprite;


import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;


public abstract class AbstractSprite extends com.fy.engineserver.core.LivingObject implements Fighter {
	// 精灵的称号（职业）
	@SimpleColumn(length=500)
	protected String title = "";

	public void setTitle(String value){
	this.title = value;
		aroundMarks[0]=true;
	}

	public String getTitle(){
	return this.title;
	}

	// 精灵的名称（人名）
	protected String name = "";

	public void setName(String value){
	this.name = value;
		aroundMarks[1]=true;
	}

	public String getName(){
	return this.name;
	}

	// 国家0中立
	public byte country;

	public void setCountry(byte value){
	this.country = value;
		aroundMarks[2]=true;
	}

	public byte getCountry(){
	return this.country;
	}

	// 用于NPC的对话内容
	protected String dialogContent = "";

	public void setDialogContent(String value){
	this.dialogContent = value;
	}

	public String getDialogContent(){
	return this.dialogContent;
	}

	// 精灵的等级
	@SimpleColumn(name="level_")
	protected int level;

	public void setLevel(int value){
	this.level = value;
		aroundMarks[3]=true;
	}

	public int getLevel(){
	return this.level;
	}

	// 职业
	protected byte career;

	public void setCareer(byte value){
	this.career = value;
	}

	public byte getCareer(){
	return this.career;
	}

	// 身高
	protected int height;

	public void setHeight(int value){
	this.height = value;
	}

	public int getHeight(){
	return this.height;
	}

	// 精灵的类型，用于区别怪物还是普通NPC，或其他种类的精灵
	protected byte spriteType;

	public void setSpriteType(byte value){
	this.spriteType = value;
		aroundMarks[4]=true;
	}

	public byte getSpriteType(){
	return this.spriteType;
	}

	// 区别不同种类的怪物，只有在spriteType表明是怪物时有意义
	protected byte monsterType;

	public void setMonsterType(byte value){
	this.monsterType = value;
		aroundMarks[5]=true;
	}

	public byte getMonsterType(){
	return this.monsterType;
	}

	// 区别不同种类的NPC，只有在spriteType表明是NPC时有意义
	protected byte npcType;

	public void setNpcType(byte value){
	this.npcType = value;
		aroundMarks[6]=true;
	}

	public byte getNpcType(){
	return this.npcType;
	}

	// 标记NPC身上是否有任务可接
	protected boolean taskMark;

	public void setTaskMark(boolean value){
	this.taskMark = value;
	}

	public boolean isTaskMark(){
	return this.taskMark;
	}

	// 普通攻击的攻击速度（毫秒为单位）
	protected int commonAttackSpeed;

	public void setCommonAttackSpeed(int value){
	this.commonAttackSpeed = value;
		aroundMarks[7]=true;
	}

	public int getCommonAttackSpeed(){
	return this.commonAttackSpeed;
	}

	// 普通攻击的攻击距离（像素为单位）
	protected int commonAttackRange;

	public void setCommonAttackRange(int value){
	this.commonAttackRange = value;
		aroundMarks[8]=true;
	}

	public int getCommonAttackRange(){
	return this.commonAttackRange;
	}

	// 角色的最大生命值
	protected transient int maxHP;

	public void setMaxHP(int value){
	this.maxHP = value;
		aroundMarks[9]=true;
	}

	public int getMaxHP(){
	return this.maxHP;
	}

	protected transient int maxHPA;

	public void setMaxHPA(int value){
	this.maxHPA = value;
	}

	public int getMaxHPA(){
	return this.maxHPA;
	}

	protected transient int maxHPB;

	public void setMaxHPB(int value){
	this.maxHPB = value;
	}

	public int getMaxHPB(){
	return this.maxHPB;
	}

	protected transient int maxHPC;

	public void setMaxHPC(int value){
	this.maxHPC = value;
	}

	public int getMaxHPC(){
	return this.maxHPC;
	}

	// 角色的最大魔法值
	protected transient int maxMP;

	public void setMaxMP(int value){
	this.maxMP = value;
	}

	public int getMaxMP(){
	return this.maxMP;
	}

	protected transient int maxMPA;

	public void setMaxMPA(int value){
	this.maxMPA = value;
	}

	public int getMaxMPA(){
	return this.maxMPA;
	}

	protected transient int maxMPB;

	public void setMaxMPB(int value){
	this.maxMPB = value;
	}

	public int getMaxMPB(){
	return this.maxMPB;
	}

	protected transient int maxMPC;

	public void setMaxMPC(int value){
	this.maxMPC = value;
	}

	public int getMaxMPC(){
	return this.maxMPC;
	}

	// hp恢复速度，每五秒恢复一次
	protected transient int hpRecoverBase;

	public void setHpRecoverBase(int value){
	this.hpRecoverBase = value;
	}

	public int getHpRecoverBase(){
	return this.hpRecoverBase;
	}

	protected int hpRecoverBaseA;

	public void setHpRecoverBaseA(int value){
	this.hpRecoverBaseA = value;
	}

	public int getHpRecoverBaseA(){
	return this.hpRecoverBaseA;
	}

	protected transient int hpRecoverBaseB;

	public void setHpRecoverBaseB(int value){
	this.hpRecoverBaseB = value;
	}

	public int getHpRecoverBaseB(){
	return this.hpRecoverBaseB;
	}

	// 初始移动速度，每秒多少像素，需要策划指定
	protected transient int speed;

	public void setSpeed(int value){
	this.speed = value;
		aroundMarks[10]=true;
	}

	public int getSpeed(){
	return this.speed;
	}

	protected int speedA;

	public void setSpeedA(int value){
	this.speedA = value;
	}

	public int getSpeedA(){
	return this.speedA;
	}

	protected transient int speedC;

	public void setSpeedC(int value){
	this.speedC = value;
	}

	public int getSpeedC(){
	return this.speedC;
	}

	// 武器伤害
	protected int weaponDamage;

	public void setWeaponDamage(int value){
	this.weaponDamage = value;
	}

	public int getWeaponDamage(){
	return this.weaponDamage;
	}

	// 物理防御
	protected transient int phyDefence;

	public void setPhyDefence(int value){
	this.phyDefence = value;
	}

	public int getPhyDefence(){
	return this.phyDefence;
	}

	protected transient int phyDefenceA;

	public void setPhyDefenceA(int value){
	this.phyDefenceA = value;
	}

	public int getPhyDefenceA(){
	return this.phyDefenceA;
	}

	protected transient int phyDefenceB;

	public void setPhyDefenceB(int value){
	this.phyDefenceB = value;
	}

	public int getPhyDefenceB(){
	return this.phyDefenceB;
	}

	protected transient int phyDefenceC;

	public void setPhyDefenceC(int value){
	this.phyDefenceC = value;
	}

	public int getPhyDefenceC(){
	return this.phyDefenceC;
	}

	// 天赋增加的物理防御百分比
	protected transient int phyDefenceD;

	public void setPhyDefenceD(int value){
	this.phyDefenceD = value;
	}

	public int getPhyDefenceD(){
	return this.phyDefenceD;
	}

	// 物理减伤百分比
	protected transient int physicalDecrease;

	public void setPhysicalDecrease(int value){
	this.physicalDecrease = value;
	}

	public int getPhysicalDecrease(){
	return this.physicalDecrease;
	}

	// 法术防御力
	protected transient int magicDefence;

	public void setMagicDefence(int value){
	this.magicDefence = value;
	}

	public int getMagicDefence(){
	return this.magicDefence;
	}

	protected transient int magicDefenceA;

	public void setMagicDefenceA(int value){
	this.magicDefenceA = value;
	}

	public int getMagicDefenceA(){
	return this.magicDefenceA;
	}

	protected transient int magicDefenceB;

	public void setMagicDefenceB(int value){
	this.magicDefenceB = value;
	}

	public int getMagicDefenceB(){
	return this.magicDefenceB;
	}

	protected transient int magicDefenceC;

	public void setMagicDefenceC(int value){
	this.magicDefenceC = value;
	}

	public int getMagicDefenceC(){
	return this.magicDefenceC;
	}

	// 天赋增加的法术防御百分比
	protected transient int magicDefenceD;

	public void setMagicDefenceD(int value){
	this.magicDefenceD = value;
	}

	public int getMagicDefenceD(){
	return this.magicDefenceD;
	}

	// 法术减伤百分比
	protected transient int spellDecrease;

	public void setSpellDecrease(int value){
	this.spellDecrease = value;
	}

	public int getSpellDecrease(){
	return this.spellDecrease;
	}

	// 破防
	protected transient int breakDefence;

	public void setBreakDefence(int value){
	this.breakDefence = value;
	}

	public int getBreakDefence(){
	return this.breakDefence;
	}

	protected transient int breakDefenceA;

	public void setBreakDefenceA(int value){
	this.breakDefenceA = value;
	}

	public int getBreakDefenceA(){
	return this.breakDefenceA;
	}

	protected transient int breakDefenceB;

	public void setBreakDefenceB(int value){
	this.breakDefenceB = value;
	}

	public int getBreakDefenceB(){
	return this.breakDefenceB;
	}

	protected transient int breakDefenceC;

	public void setBreakDefenceC(int value){
	this.breakDefenceC = value;
	}

	public int getBreakDefenceC(){
	return this.breakDefenceC;
	}

	// 破防率
	protected transient int breakDefenceRate;

	public void setBreakDefenceRate(int value){
	this.breakDefenceRate = value;
	}

	public int getBreakDefenceRate(){
	return this.breakDefenceRate;
	}

	// 命中等级
	protected transient int hit;

	public void setHit(int value){
	this.hit = value;
	}

	public int getHit(){
	return this.hit;
	}

	protected transient int hitA;

	public void setHitA(int value){
	this.hitA = value;
	}

	public int getHitA(){
	return this.hitA;
	}

	protected transient int hitB;

	public void setHitB(int value){
	this.hitB = value;
	}

	public int getHitB(){
	return this.hitB;
	}

	protected transient int hitC;

	public void setHitC(int value){
	this.hitC = value;
	}

	public int getHitC(){
	return this.hitC;
	}

	// 天赋增加的命中百分比
	protected transient int hitD;

	public void setHitD(int value){
	this.hitD = value;
	}

	public int getHitD(){
	return this.hitD;
	}

	// 命中率
	protected transient int hitRate;

	public void setHitRate(int value){
	this.hitRate = value;
	}

	public int getHitRate(){
	return this.hitRate;
	}

	// 回避
	protected transient int dodge;

	public void setDodge(int value){
	this.dodge = value;
	}

	public int getDodge(){
	return this.dodge;
	}

	protected transient int dodgeA;

	public void setDodgeA(int value){
	this.dodgeA = value;
	}

	public int getDodgeA(){
	return this.dodgeA;
	}

	protected transient int dodgeB;

	public void setDodgeB(int value){
	this.dodgeB = value;
	}

	public int getDodgeB(){
	return this.dodgeB;
	}

	protected transient int dodgeC;

	public void setDodgeC(int value){
	this.dodgeC = value;
	}

	public int getDodgeC(){
	return this.dodgeC;
	}

	// 天赋增加闪避百分比
	protected transient int dodgeD;

	public void setDodgeD(int value){
	this.dodgeD = value;
	}

	public int getDodgeD(){
	return this.dodgeD;
	}

	// 回避率
	protected transient int dodgeRate;

	public void setDodgeRate(int value){
	this.dodgeRate = value;
	}

	public int getDodgeRate(){
	return this.dodgeRate;
	}

	// 精准
	protected transient int accurate;

	public void setAccurate(int value){
	this.accurate = value;
	}

	public int getAccurate(){
	return this.accurate;
	}

	protected transient int accurateA;

	public void setAccurateA(int value){
	this.accurateA = value;
	}

	public int getAccurateA(){
	return this.accurateA;
	}

	protected transient int accurateB;

	public void setAccurateB(int value){
	this.accurateB = value;
	}

	public int getAccurateB(){
	return this.accurateB;
	}

	protected transient int accurateC;

	public void setAccurateC(int value){
	this.accurateC = value;
	}

	public int getAccurateC(){
	return this.accurateC;
	}

	// 精准率
	protected transient int accurateRate;

	public void setAccurateRate(int value){
	this.accurateRate = value;
	}

	public int getAccurateRate(){
	return this.accurateRate;
	}

	// 力量
	protected transient int strength;

	public void setStrength(int value){
	this.strength = value;
	}

	public int getStrength(){
	return this.strength;
	}

	protected transient int strengthA;

	public void setStrengthA(int value){
	this.strengthA = value;
	}

	public int getStrengthA(){
	return this.strengthA;
	}

	protected transient int strengthB;

	public void setStrengthB(int value){
	this.strengthB = value;
	}

	public int getStrengthB(){
	return this.strengthB;
	}

	protected transient int strengthC;

	public void setStrengthC(int value){
	this.strengthC = value;
	}

	public int getStrengthC(){
	return this.strengthC;
	}

	// 力量，玩家自己加点
	protected int strengthS;

	public void setStrengthS(int value){
	this.strengthS = value;
	}

	public int getStrengthS(){
	return this.strengthS;
	}

	// 智力
	protected transient int spellpower;

	public void setSpellpower(int value){
	this.spellpower = value;
	}

	public int getSpellpower(){
	return this.spellpower;
	}

	protected transient int spellpowerA;

	public void setSpellpowerA(int value){
	this.spellpowerA = value;
	}

	public int getSpellpowerA(){
	return this.spellpowerA;
	}

	protected transient int spellpowerB;

	public void setSpellpowerB(int value){
	this.spellpowerB = value;
	}

	public int getSpellpowerB(){
	return this.spellpowerB;
	}

	protected transient int spellpowerC;

	public void setSpellpowerC(int value){
	this.spellpowerC = value;
	}

	public int getSpellpowerC(){
	return this.spellpowerC;
	}

	// 智力，玩家自己加点
	protected int spellpowerS;

	public void setSpellpowerS(int value){
	this.spellpowerS = value;
	}

	public int getSpellpowerS(){
	return this.spellpowerS;
	}

	// 敏捷
	protected transient int dexterity;

	public void setDexterity(int value){
	this.dexterity = value;
	}

	public int getDexterity(){
	return this.dexterity;
	}

	protected transient int dexterityA;

	public void setDexterityA(int value){
	this.dexterityA = value;
	}

	public int getDexterityA(){
	return this.dexterityA;
	}

	protected transient int dexterityB;

	public void setDexterityB(int value){
	this.dexterityB = value;
	}

	public int getDexterityB(){
	return this.dexterityB;
	}

	protected transient int dexterityC;

	public void setDexterityC(int value){
	this.dexterityC = value;
	}

	public int getDexterityC(){
	return this.dexterityC;
	}

	// 敏捷，玩家自己加点
	protected int dexterityS;

	public void setDexterityS(int value){
	this.dexterityS = value;
	}

	public int getDexterityS(){
	return this.dexterityS;
	}

	// 耐力
	protected transient int constitution;

	public void setConstitution(int value){
	this.constitution = value;
	}

	public int getConstitution(){
	return this.constitution;
	}

	protected transient int constitutionA;

	public void setConstitutionA(int value){
	this.constitutionA = value;
	}

	public int getConstitutionA(){
	return this.constitutionA;
	}

	protected transient int constitutionB;

	public void setConstitutionB(int value){
	this.constitutionB = value;
	}

	public int getConstitutionB(){
	return this.constitutionB;
	}

	protected transient int constitutionC;

	public void setConstitutionC(int value){
	this.constitutionC = value;
	}

	public int getConstitutionC(){
	return this.constitutionC;
	}

	// 耐力，玩家自己加点
	protected int constitutionS;

	public void setConstitutionS(int value){
	this.constitutionS = value;
	}

	public int getConstitutionS(){
	return this.constitutionS;
	}

	// 定力
	protected transient int dingli;

	public void setDingli(int value){
	this.dingli = value;
	}

	public int getDingli(){
	return this.dingli;
	}

	protected transient int dingliA;

	public void setDingliA(int value){
	this.dingliA = value;
	}

	public int getDingliA(){
	return this.dingliA;
	}

	protected transient int dingliB;

	public void setDingliB(int value){
	this.dingliB = value;
	}

	public int getDingliB(){
	return this.dingliB;
	}

	protected transient int dingliC;

	public void setDingliC(int value){
	this.dingliC = value;
	}

	public int getDingliC(){
	return this.dingliC;
	}

	// 定力，玩家自己加点
	protected int dingliS;

	public void setDingliS(int value){
	this.dingliS = value;
	}

	public int getDingliS(){
	return this.dingliS;
	}

	// 会心一击，也叫暴击
	protected transient int criticalHit;

	public void setCriticalHit(int value){
	this.criticalHit = value;
	}

	public int getCriticalHit(){
	return this.criticalHit;
	}

	protected transient int criticalHitA;

	public void setCriticalHitA(int value){
	this.criticalHitA = value;
	}

	public int getCriticalHitA(){
	return this.criticalHitA;
	}

	protected transient int criticalHitB;

	public void setCriticalHitB(int value){
	this.criticalHitB = value;
	}

	public int getCriticalHitB(){
	return this.criticalHitB;
	}

	protected transient int criticalHitC;

	public void setCriticalHitC(int value){
	this.criticalHitC = value;
	}

	public int getCriticalHitC(){
	return this.criticalHitC;
	}

	// 会心一击率
	protected transient int criticalHitRate;

	public void setCriticalHitRate(int value){
	this.criticalHitRate = value;
	}

	public int getCriticalHitRate(){
	return this.criticalHitRate;
	}

	// 会心防御
	protected transient int criticalDefence;

	public void setCriticalDefence(int value){
	this.criticalDefence = value;
	}

	public int getCriticalDefence(){
	return this.criticalDefence;
	}

	protected transient int criticalDefenceA;

	public void setCriticalDefenceA(int value){
	this.criticalDefenceA = value;
	}

	public int getCriticalDefenceA(){
	return this.criticalDefenceA;
	}

	protected transient int criticalDefenceB;

	public void setCriticalDefenceB(int value){
	this.criticalDefenceB = value;
	}

	public int getCriticalDefenceB(){
	return this.criticalDefenceB;
	}

	protected transient int criticalDefenceC;

	public void setCriticalDefenceC(int value){
	this.criticalDefenceC = value;
	}

	public int getCriticalDefenceC(){
	return this.criticalDefenceC;
	}

	// 会心防御率
	protected transient int criticalDefenceRate;

	public void setCriticalDefenceRate(int value){
	this.criticalDefenceRate = value;
	}

	public int getCriticalDefenceRate(){
	return this.criticalDefenceRate;
	}

	// 近战攻击强度
	protected transient int phyAttack;

	public void setPhyAttack(int value){
	this.phyAttack = value;
	}

	public int getPhyAttack(){
	return this.phyAttack;
	}

	protected transient int phyAttackA;

	public void setPhyAttackA(int value){
	this.phyAttackA = value;
	}

	public int getPhyAttackA(){
	return this.phyAttackA;
	}

	protected transient int phyAttackB;

	public void setPhyAttackB(int value){
	this.phyAttackB = value;
	}

	public int getPhyAttackB(){
	return this.phyAttackB;
	}

	protected transient int phyAttackC;

	public void setPhyAttackC(int value){
	this.phyAttackC = value;
	}

	public int getPhyAttackC(){
	return this.phyAttackC;
	}

	// 法术攻击强度
	protected transient int magicAttack;

	public void setMagicAttack(int value){
	this.magicAttack = value;
	}

	public int getMagicAttack(){
	return this.magicAttack;
	}

	protected transient int magicAttackA;

	public void setMagicAttackA(int value){
	this.magicAttackA = value;
	}

	public int getMagicAttackA(){
	return this.magicAttackA;
	}

	protected transient int magicAttackB;

	public void setMagicAttackB(int value){
	this.magicAttackB = value;
	}

	public int getMagicAttackB(){
	return this.magicAttackB;
	}

	protected transient int magicAttackC;

	public void setMagicAttackC(int value){
	this.magicAttackC = value;
	}

	public int getMagicAttackC(){
	return this.magicAttackC;
	}

	// 非物理防御得到的物理减伤率0~1000 表示千分率
	protected transient int phyDefenceRateOther;

	public void setPhyDefenceRateOther(int value){
	this.phyDefenceRateOther = value;
	}

	public int getPhyDefenceRateOther(){
	return this.phyDefenceRateOther;
	}

	// 非法力防御得到的法力减伤率0~1000 表示千分率
	protected transient int magicDefenceRateOther;

	public void setMagicDefenceRateOther(int value){
	this.magicDefenceRateOther = value;
	}

	public int getMagicDefenceRateOther(){
	return this.magicDefenceRateOther;
	}

	// 非暴击得到的暴击率0~1000 表示千分率
	protected transient int criticalHitRateOther;

	public void setCriticalHitRateOther(int value){
	this.criticalHitRateOther = value;
	}

	public int getCriticalHitRateOther(){
	return this.criticalHitRateOther;
	}

	// 非减暴得到的减爆率0~1000 表示千分率
	protected transient int criticalDefenceRateOther;

	public void setCriticalDefenceRateOther(int value){
	this.criticalDefenceRateOther = value;
	}

	public int getCriticalDefenceRateOther(){
	return this.criticalDefenceRateOther;
	}

	// 非命中等级转化的命中率0~1000 表示千分率
	protected transient int hitRateOther;

	public void setHitRateOther(int value){
	this.hitRateOther = value;
	}

	public int getHitRateOther(){
	return this.hitRateOther;
	}

	// 非闪避等级转化的闪避率0~1000 表示千分率
	protected transient int dodgeRateOther;

	public void setDodgeRateOther(int value){
	this.dodgeRateOther = value;
	}

	public int getDodgeRateOther(){
	return this.dodgeRateOther;
	}

	// 非精准等级转化的精准率0~1000 表示千分率
	protected transient int accurateRateOther;

	public void setAccurateRateOther(int value){
	this.accurateRateOther = value;
	}

	public int getAccurateRateOther(){
	return this.accurateRateOther;
	}

	// 非破防等级得到的破防率0~1000 表示千分率
	protected transient int breakDefenceRateOther;

	public void setBreakDefenceRateOther(int value){
	this.breakDefenceRateOther = value;
	}

	public int getBreakDefenceRateOther(){
	return this.breakDefenceRateOther;
	}

	// 非金防等级得到的金防率0~1000 表示千分率
	protected transient int fireDefenceRateOther;

	public void setFireDefenceRateOther(int value){
	this.fireDefenceRateOther = value;
	}

	public int getFireDefenceRateOther(){
	return this.fireDefenceRateOther;
	}

	// 非火减抗等级得到的火减抗率0~1000 表示千分率
	protected transient int fireIgnoreDefenceRateOther;

	public void setFireIgnoreDefenceRateOther(int value){
	this.fireIgnoreDefenceRateOther = value;
	}

	public int getFireIgnoreDefenceRateOther(){
	return this.fireIgnoreDefenceRateOther;
	}

	// 非冰抗得到的冰抗率0~1000 表示千分率
	protected transient int blizzardDefenceRateOther;

	public void setBlizzardDefenceRateOther(int value){
	this.blizzardDefenceRateOther = value;
	}

	public int getBlizzardDefenceRateOther(){
	return this.blizzardDefenceRateOther;
	}

	// 非冰减抗得到的冰减抗率0~1000 表示千分率
	protected transient int blizzardIgnoreDefenceRateOther;

	public void setBlizzardIgnoreDefenceRateOther(int value){
	this.blizzardIgnoreDefenceRateOther = value;
	}

	public int getBlizzardIgnoreDefenceRateOther(){
	return this.blizzardIgnoreDefenceRateOther;
	}

	// 非风抗得到的风抗率0~1000 表示千分率
	protected transient int windDefenceRateOther;

	public void setWindDefenceRateOther(int value){
	this.windDefenceRateOther = value;
	}

	public int getWindDefenceRateOther(){
	return this.windDefenceRateOther;
	}

	// 非风减抗得到的风减抗率0~1000 表示千分率
	protected transient int windIgnoreDefenceRateOther;

	public void setWindIgnoreDefenceRateOther(int value){
	this.windIgnoreDefenceRateOther = value;
	}

	public int getWindIgnoreDefenceRateOther(){
	return this.windIgnoreDefenceRateOther;
	}

	// 非雷抗得到的雷抗率0~1000 表示千分率
	protected transient int thunderDefenceRateOther;

	public void setThunderDefenceRateOther(int value){
	this.thunderDefenceRateOther = value;
	}

	public int getThunderDefenceRateOther(){
	return this.thunderDefenceRateOther;
	}

	// 非雷减抗得到的雷减抗率0~1000 表示千分率
	protected transient int thunderIgnoreDefenceRateOther;

	public void setThunderIgnoreDefenceRateOther(int value){
	this.thunderIgnoreDefenceRateOther = value;
	}

	public int getThunderIgnoreDefenceRateOther(){
	return this.thunderIgnoreDefenceRateOther;
	}

	// 火攻
	protected transient int fireAttack;

	public void setFireAttack(int value){
	this.fireAttack = value;
	}

	public int getFireAttack(){
	return this.fireAttack;
	}

	protected transient int fireAttackA;

	public void setFireAttackA(int value){
	this.fireAttackA = value;
	}

	public int getFireAttackA(){
	return this.fireAttackA;
	}

	protected transient int fireAttackB;

	public void setFireAttackB(int value){
	this.fireAttackB = value;
	}

	public int getFireAttackB(){
	return this.fireAttackB;
	}

	protected transient int fireAttackC;

	public void setFireAttackC(int value){
	this.fireAttackC = value;
	}

	public int getFireAttackC(){
	return this.fireAttackC;
	}

	// 金防
	protected transient int fireDefence;

	public void setFireDefence(int value){
	this.fireDefence = value;
	}

	public int getFireDefence(){
	return this.fireDefence;
	}

	protected transient int fireDefenceA;

	public void setFireDefenceA(int value){
	this.fireDefenceA = value;
	}

	public int getFireDefenceA(){
	return this.fireDefenceA;
	}

	protected transient int fireDefenceB;

	public void setFireDefenceB(int value){
	this.fireDefenceB = value;
	}

	public int getFireDefenceB(){
	return this.fireDefenceB;
	}

	protected transient int fireDefenceC;

	public void setFireDefenceC(int value){
	this.fireDefenceC = value;
	}

	public int getFireDefenceC(){
	return this.fireDefenceC;
	}

	// 金防率
	protected transient int fireDefenceRate;

	public void setFireDefenceRate(int value){
	this.fireDefenceRate = value;
	}

	public int getFireDefenceRate(){
	return this.fireDefenceRate;
	}

	// 减少对方金防
	protected transient int fireIgnoreDefence;

	public void setFireIgnoreDefence(int value){
	this.fireIgnoreDefence = value;
	}

	public int getFireIgnoreDefence(){
	return this.fireIgnoreDefence;
	}

	protected transient int fireIgnoreDefenceA;

	public void setFireIgnoreDefenceA(int value){
	this.fireIgnoreDefenceA = value;
	}

	public int getFireIgnoreDefenceA(){
	return this.fireIgnoreDefenceA;
	}

	protected transient int fireIgnoreDefenceB;

	public void setFireIgnoreDefenceB(int value){
	this.fireIgnoreDefenceB = value;
	}

	public int getFireIgnoreDefenceB(){
	return this.fireIgnoreDefenceB;
	}

	protected transient int fireIgnoreDefenceC;

	public void setFireIgnoreDefenceC(int value){
	this.fireIgnoreDefenceC = value;
	}

	public int getFireIgnoreDefenceC(){
	return this.fireIgnoreDefenceC;
	}

	// 减少对方金防率
	protected transient int fireIgnoreDefenceRate;

	public void setFireIgnoreDefenceRate(int value){
	this.fireIgnoreDefenceRate = value;
	}

	public int getFireIgnoreDefenceRate(){
	return this.fireIgnoreDefenceRate;
	}

	// 冰攻
	protected transient int blizzardAttack;

	public void setBlizzardAttack(int value){
	this.blizzardAttack = value;
	}

	public int getBlizzardAttack(){
	return this.blizzardAttack;
	}

	protected transient int blizzardAttackA;

	public void setBlizzardAttackA(int value){
	this.blizzardAttackA = value;
	}

	public int getBlizzardAttackA(){
	return this.blizzardAttackA;
	}

	protected transient int blizzardAttackB;

	public void setBlizzardAttackB(int value){
	this.blizzardAttackB = value;
	}

	public int getBlizzardAttackB(){
	return this.blizzardAttackB;
	}

	protected transient int blizzardAttackC;

	public void setBlizzardAttackC(int value){
	this.blizzardAttackC = value;
	}

	public int getBlizzardAttackC(){
	return this.blizzardAttackC;
	}

	// 水防
	protected transient int blizzardDefence;

	public void setBlizzardDefence(int value){
	this.blizzardDefence = value;
	}

	public int getBlizzardDefence(){
	return this.blizzardDefence;
	}

	protected transient int blizzardDefenceA;

	public void setBlizzardDefenceA(int value){
	this.blizzardDefenceA = value;
	}

	public int getBlizzardDefenceA(){
	return this.blizzardDefenceA;
	}

	protected transient int blizzardDefenceB;

	public void setBlizzardDefenceB(int value){
	this.blizzardDefenceB = value;
	}

	public int getBlizzardDefenceB(){
	return this.blizzardDefenceB;
	}

	protected transient int blizzardDefenceC;

	public void setBlizzardDefenceC(int value){
	this.blizzardDefenceC = value;
	}

	public int getBlizzardDefenceC(){
	return this.blizzardDefenceC;
	}

	// 水防率
	protected transient int blizzardDefenceRate;

	public void setBlizzardDefenceRate(int value){
	this.blizzardDefenceRate = value;
	}

	public int getBlizzardDefenceRate(){
	return this.blizzardDefenceRate;
	}

	// 减少对方水防
	protected transient int blizzardIgnoreDefence;

	public void setBlizzardIgnoreDefence(int value){
	this.blizzardIgnoreDefence = value;
	}

	public int getBlizzardIgnoreDefence(){
	return this.blizzardIgnoreDefence;
	}

	protected transient int blizzardIgnoreDefenceA;

	public void setBlizzardIgnoreDefenceA(int value){
	this.blizzardIgnoreDefenceA = value;
	}

	public int getBlizzardIgnoreDefenceA(){
	return this.blizzardIgnoreDefenceA;
	}

	protected transient int blizzardIgnoreDefenceB;

	public void setBlizzardIgnoreDefenceB(int value){
	this.blizzardIgnoreDefenceB = value;
	}

	public int getBlizzardIgnoreDefenceB(){
	return this.blizzardIgnoreDefenceB;
	}

	protected transient int blizzardIgnoreDefenceC;

	public void setBlizzardIgnoreDefenceC(int value){
	this.blizzardIgnoreDefenceC = value;
	}

	public int getBlizzardIgnoreDefenceC(){
	return this.blizzardIgnoreDefenceC;
	}

	// 减少对方水防率
	protected transient int blizzardIgnoreDefenceRate;

	public void setBlizzardIgnoreDefenceRate(int value){
	this.blizzardIgnoreDefenceRate = value;
	}

	public int getBlizzardIgnoreDefenceRate(){
	return this.blizzardIgnoreDefenceRate;
	}

	// 风攻
	protected transient int windAttack;

	public void setWindAttack(int value){
	this.windAttack = value;
	}

	public int getWindAttack(){
	return this.windAttack;
	}

	protected transient int windAttackA;

	public void setWindAttackA(int value){
	this.windAttackA = value;
	}

	public int getWindAttackA(){
	return this.windAttackA;
	}

	protected transient int windAttackB;

	public void setWindAttackB(int value){
	this.windAttackB = value;
	}

	public int getWindAttackB(){
	return this.windAttackB;
	}

	protected transient int windAttackC;

	public void setWindAttackC(int value){
	this.windAttackC = value;
	}

	public int getWindAttackC(){
	return this.windAttackC;
	}

	// 火防
	protected transient int windDefence;

	public void setWindDefence(int value){
	this.windDefence = value;
	}

	public int getWindDefence(){
	return this.windDefence;
	}

	protected transient int windDefenceA;

	public void setWindDefenceA(int value){
	this.windDefenceA = value;
	}

	public int getWindDefenceA(){
	return this.windDefenceA;
	}

	protected transient int windDefenceB;

	public void setWindDefenceB(int value){
	this.windDefenceB = value;
	}

	public int getWindDefenceB(){
	return this.windDefenceB;
	}

	protected transient int windDefenceC;

	public void setWindDefenceC(int value){
	this.windDefenceC = value;
	}

	public int getWindDefenceC(){
	return this.windDefenceC;
	}

	// 火防率
	protected transient int windDefenceRate;

	public void setWindDefenceRate(int value){
	this.windDefenceRate = value;
	}

	public int getWindDefenceRate(){
	return this.windDefenceRate;
	}

	// 减少对方水防
	protected transient int windIgnoreDefence;

	public void setWindIgnoreDefence(int value){
	this.windIgnoreDefence = value;
	}

	public int getWindIgnoreDefence(){
	return this.windIgnoreDefence;
	}

	protected transient int windIgnoreDefenceA;

	public void setWindIgnoreDefenceA(int value){
	this.windIgnoreDefenceA = value;
	}

	public int getWindIgnoreDefenceA(){
	return this.windIgnoreDefenceA;
	}

	protected transient int windIgnoreDefenceB;

	public void setWindIgnoreDefenceB(int value){
	this.windIgnoreDefenceB = value;
	}

	public int getWindIgnoreDefenceB(){
	return this.windIgnoreDefenceB;
	}

	protected transient int windIgnoreDefenceC;

	public void setWindIgnoreDefenceC(int value){
	this.windIgnoreDefenceC = value;
	}

	public int getWindIgnoreDefenceC(){
	return this.windIgnoreDefenceC;
	}

	// 减少对方水防率
	protected transient int windIgnoreDefenceRate;

	public void setWindIgnoreDefenceRate(int value){
	this.windIgnoreDefenceRate = value;
	}

	public int getWindIgnoreDefenceRate(){
	return this.windIgnoreDefenceRate;
	}

	// 雷攻
	protected transient int thunderAttack;

	public void setThunderAttack(int value){
	this.thunderAttack = value;
	}

	public int getThunderAttack(){
	return this.thunderAttack;
	}

	protected transient int thunderAttackA;

	public void setThunderAttackA(int value){
	this.thunderAttackA = value;
	}

	public int getThunderAttackA(){
	return this.thunderAttackA;
	}

	protected transient int thunderAttackB;

	public void setThunderAttackB(int value){
	this.thunderAttackB = value;
	}

	public int getThunderAttackB(){
	return this.thunderAttackB;
	}

	protected transient int thunderAttackC;

	public void setThunderAttackC(int value){
	this.thunderAttackC = value;
	}

	public int getThunderAttackC(){
	return this.thunderAttackC;
	}

	// 木防
	protected transient int thunderDefence;

	public void setThunderDefence(int value){
	this.thunderDefence = value;
	}

	public int getThunderDefence(){
	return this.thunderDefence;
	}

	protected transient int thunderDefenceA;

	public void setThunderDefenceA(int value){
	this.thunderDefenceA = value;
	}

	public int getThunderDefenceA(){
	return this.thunderDefenceA;
	}

	protected transient int thunderDefenceB;

	public void setThunderDefenceB(int value){
	this.thunderDefenceB = value;
	}

	public int getThunderDefenceB(){
	return this.thunderDefenceB;
	}

	protected transient int thunderDefenceC;

	public void setThunderDefenceC(int value){
	this.thunderDefenceC = value;
	}

	public int getThunderDefenceC(){
	return this.thunderDefenceC;
	}

	// 木防率
	protected transient int thunderDefenceRate;

	public void setThunderDefenceRate(int value){
	this.thunderDefenceRate = value;
	}

	public int getThunderDefenceRate(){
	return this.thunderDefenceRate;
	}

	// 减少对方木防
	protected transient int thunderIgnoreDefence;

	public void setThunderIgnoreDefence(int value){
	this.thunderIgnoreDefence = value;
	}

	public int getThunderIgnoreDefence(){
	return this.thunderIgnoreDefence;
	}

	protected transient int thunderIgnoreDefenceA;

	public void setThunderIgnoreDefenceA(int value){
	this.thunderIgnoreDefenceA = value;
	}

	public int getThunderIgnoreDefenceA(){
	return this.thunderIgnoreDefenceA;
	}

	protected transient int thunderIgnoreDefenceB;

	public void setThunderIgnoreDefenceB(int value){
	this.thunderIgnoreDefenceB = value;
	}

	public int getThunderIgnoreDefenceB(){
	return this.thunderIgnoreDefenceB;
	}

	protected transient int thunderIgnoreDefenceC;

	public void setThunderIgnoreDefenceC(int value){
	this.thunderIgnoreDefenceC = value;
	}

	public int getThunderIgnoreDefenceC(){
	return this.thunderIgnoreDefenceC;
	}

	// 减少对方木防率
	protected transient int thunderIgnoreDefenceRate;

	public void setThunderIgnoreDefenceRate(int value){
	this.thunderIgnoreDefenceRate = value;
	}

	public int getThunderIgnoreDefenceRate(){
	return this.thunderIgnoreDefenceRate;
	}

	// 基本经验值，死亡后根据计算公式分配给玩家，需要策划指定
	protected long exp;

	public void setExp(long value){
	this.exp = value;
	}

	public long getExp(){
	return this.exp;
	}

	// 当前的状态，如站立、行走等
	protected byte state;

	public void setState(byte value){
	this.state = value;
	}

	public byte getState(){
	return this.state;
	}

	// 阴影层的光环，受光环辅助技能影响，默认值-1
	protected transient byte aura;

	public void setAura(byte value){
	this.aura = value;
		aroundMarks[11]=true;
	}

	public byte getAura(){
	return this.aura;
	}

	// 面朝的方向，如上下左右等
	protected byte direction;

	public void setDirection(byte value){
	this.direction = value;
	}

	public byte getDirection(){
	return this.direction;
	}

	// 当前生命值
	protected int hp;

	public void setHp(int value){
	this.hp = value;
		aroundMarks[12]=true;
	}

	public int getHp(){
	return this.hp;
	}

	// 定身状态，不能移动
	protected transient boolean hold;

	public void setHold(boolean value){
	this.hold = value;
		aroundMarks[13]=true;
	}

	public boolean isHold(){
	return this.hold;
	}

	// 眩晕状态，不能移动，不能使用技能和道具（特定技能除外）
	protected transient boolean stun;

	public void setStun(boolean value){
	this.stun = value;
		aroundMarks[14]=true;
	}

	public boolean isStun(){
	return this.stun;
	}

	// 免疫状态
	protected transient boolean immunity;

	public void setImmunity(boolean value){
	this.immunity = value;
		aroundMarks[15]=true;
	}

	public boolean isImmunity(){
	return this.immunity;
	}

	// 中毒状态
	protected transient boolean poison;

	public void setPoison(boolean value){
	this.poison = value;
		aroundMarks[16]=true;
	}

	public boolean isPoison(){
	return this.poison;
	}

	// 虚弱状态   (客户端会播放震屏击倒效果)
	protected transient boolean weak;

	public void setWeak(boolean value){
	this.weak = value;
		aroundMarks[17]=true;
	}

	public boolean isWeak(){
	return this.weak;
	}

	// 无敌状态
	protected transient boolean invulnerable;

	public void setInvulnerable(boolean value){
	this.invulnerable = value;
		aroundMarks[18]=true;
	}

	public boolean isInvulnerable(){
	return this.invulnerable;
	}

	// 封魔状态，不能使用主动技能
	protected transient boolean silence;

	public void setSilence(boolean value){
	this.silence = value;
		aroundMarks[19]=true;
	}

	public boolean isSilence(){
	return this.silence;
	}

	// 护盾类型，-1代表没有护盾
	protected transient byte shield;

	//是否有降低治疗的buff,
		//返回：0,1,2,3表示buff级别
		protected transient int lowerCureLevel = -1;
	
	public void setShield(byte value){
	this.shield = value;
		aroundMarks[20]=true;
	}

	public byte getShield(){
	return this.shield;
	}


	public int getLowerCureLevel() {
		return lowerCureLevel;
	}

	public void setLowerCureLevel(int lowerCureLevel) {
		this.lowerCureLevel = lowerCureLevel;
	}

	// avata部件类型，比如:武器
	protected transient byte[] avataType;

	public void setAvataType(byte[] value){
	this.avataType = value;
		aroundMarks[21]=true;
	}

	public byte[] getAvataType(){
	return this.avataType;
	}

	protected transient String[] avata;

	public void setAvata(String[] value){
	this.avata = value;
		aroundMarks[22]=true;
	}

	public String[] getAvata(){
	return this.avata;
	}

	// 强制播放动作不懂问单涛
	protected transient String avataAction = "";

	public void setAvataAction(String value){
	this.avataAction = value;
		aroundMarks[23]=true;
	}

	public String getAvataAction(){
	return this.avataAction;
	}

	// 标记某个NPC是否在战场中
	protected transient boolean inBattleField;

	public void setInBattleField(boolean value){
	this.inBattleField = value;
		aroundMarks[24]=true;
	}

	public boolean isInBattleField(){
	return this.inBattleField;
	}

	protected transient byte battleFieldSide;

	public void setBattleFieldSide(byte value){
	this.battleFieldSide = value;
		aroundMarks[25]=true;
	}

	public byte getBattleFieldSide(){
	return this.battleFieldSide;
	}

	// 正在释放技能，用于正在释放暴风雪技能，该状态改变后暴风雪消失
	protected transient boolean skillUsingState;

	public void setSkillUsingState(boolean value){
	this.skillUsingState = value;
		aroundMarks[26]=true;
	}

	public boolean isSkillUsingState(){
	return this.skillUsingState;
	}

	// 名字颜色
	protected transient int nameColor;

	public void setNameColor(int value){
	this.nameColor = value;
		aroundMarks[27]=true;
	}

	public int getNameColor(){
	return this.nameColor;
	}

	// 大小变化值，因为协议不支持float，所以定1000表示大小不变，500表示50%，2000表示200%
	protected transient short objectScale;

	public void setObjectScale(short value){
	this.objectScale = value;
		aroundMarks[28]=true;
	}

	public short getObjectScale(){
	return this.objectScale;
	}

	// 生物颜色值
	public transient int objectColor;

	public void setObjectColor(int value){
	this.objectColor = value;
		aroundMarks[29]=true;
	}

	public int getObjectColor(){
	return this.objectColor;
	}

	// 透明状态，处于透明状态中，怪物无法攻击，只有队友和自己看是半透，其他人看都是全透
	protected transient boolean objectOpacity;

	public void setObjectOpacity(boolean value){
	this.objectOpacity = value;
		aroundMarks[30]=true;
	}

	public boolean isObjectOpacity(){
	return this.objectOpacity;
	}

	// 身上的粒子
	protected transient String particleName = "";

	public void setParticleName(String value){
	this.particleName = value;
		aroundMarks[31]=true;
	}

	public String getParticleName(){
	return this.particleName;
	}

	// 生物脚下中心点为0，人物右向为正，左向为负，例50为50%
	protected transient short particleX;

	public void setParticleX(short value){
	this.particleX = value;
		aroundMarks[32]=true;
	}

	public short getParticleX(){
	return this.particleX;
	}

	// 生物脚下中心点为0，人物向下为正，向上为负，例50为50%
	protected transient short particleY;

	public void setParticleY(short value){
	this.particleY = value;
		aroundMarks[33]=true;
	}

	public short getParticleY(){
	return this.particleY;
	}

	// 生物脚下的粒子
	protected transient String footParticleName = "";

	public void setFootParticleName(String value){
	this.footParticleName = value;
		aroundMarks[34]=true;
	}

	public String getFootParticleName(){
	return this.footParticleName;
	}

	// 生物脚下中心点为0，人物右向为正，左向为负，例50为50%
	protected transient short footParticleX;

	public void setFootParticleX(short value){
	this.footParticleX = value;
		aroundMarks[35]=true;
	}

	public short getFootParticleX(){
	return this.footParticleX;
	}

	// 生物脚下中心点为0，人物向下为正，向上为负，例50为50%
	protected transient short footParticleY;

	public void setFootParticleY(short value){
	this.footParticleY = value;
		aroundMarks[36]=true;
	}

	public short getFootParticleY(){
	return this.footParticleY;
	}

	// 反伤百分比
	protected transient short ironMaidenPercent;

	public void setIronMaidenPercent(short value){
	this.ironMaidenPercent = value;
		aroundMarks[37]=true;
	}

	public short getIronMaidenPercent(){
	return this.ironMaidenPercent;
	}

	private transient boolean[] aroundMarks = new boolean[40];
	public boolean[] getAroundMarks(){
	 	return aroundMarks;
	 }
	public Object getValue(int id){
		switch(id){
			case 0: return title;
			case 1: return name;
			case 2: return country;
			case 3: return level;
			case 4: return spriteType;
			case 5: return monsterType;
			case 6: return npcType;
			case 7: return commonAttackSpeed;
			case 8: return commonAttackRange;
			case 9: return maxHP;
			case 10: return speed;
			case 11: return aura;
			case 12: return hp;
			case 13: return hold;
			case 14: return stun;
			case 15: return immunity;
			case 16: return poison;
			case 17: return weak;
			case 18: return invulnerable;
			case 19: return silence;
			case 20: return shield;
			case 21: return avataType;
			case 22: return avata;
			case 23: return avataAction;
			case 24: return inBattleField;
			case 25: return battleFieldSide;
			case 26: return skillUsingState;
			case 27: return getNameColor();
			case 28: return objectScale;
			case 29: return objectColor;
			case 30: return objectOpacity;
			case 31: return particleName;
			case 32: return particleX;
			case 33: return particleY;
			case 34: return footParticleName;
			case 35: return footParticleX;
			case 36: return footParticleY;
			case 37: return ironMaidenPercent;
		default :break;
		}
		return null;
	}
	public void setValue(int id, Object value) {
	switch (id) {
	case 0: setTitle((String)value);
	break;
	case 1: setName((String)value);
	break;
	case 2: setCountry(((Byte)value).byteValue());
	break;
	case 3: setLevel(((Integer)value).intValue());
	break;
	case 4: setSpriteType(((Byte)value).byteValue());
	break;
	case 5: setMonsterType(((Byte)value).byteValue());
	break;
	case 6: setNpcType(((Byte)value).byteValue());
	break;
	case 7: setCommonAttackSpeed(((Integer)value).intValue());
	break;
	case 8: setCommonAttackRange(((Integer)value).intValue());
	break;
	case 9: setMaxHP(((Integer)value).intValue());
	break;
	case 10: setSpeed(((Integer)value).intValue());
	break;
	case 11: setAura(((Byte)value).byteValue());
	break;
	case 12: setHp(((Integer)value).intValue());
	break;
	case 13: setHold(((Boolean)value).booleanValue());
	break;
	case 14: setStun(((Boolean)value).booleanValue());
	break;
	case 15: setImmunity(((Boolean)value).booleanValue());
	break;
	case 16: setPoison(((Boolean)value).booleanValue());
	break;
	case 17: setWeak(((Boolean)value).booleanValue());
	break;
	case 18: setInvulnerable(((Boolean)value).booleanValue());
	break;
	case 19: setSilence(((Boolean)value).booleanValue());
	break;
	case 20: setShield(((Byte)value).byteValue());
	break;
	case 21: setAvataType((byte[])value);
	break;
	case 22: setAvata((String[])value);
	break;
	case 23: setAvataAction((String)value);
	break;
	case 24: setInBattleField(((Boolean)value).booleanValue());
	break;
	case 25: setBattleFieldSide(((Byte)value).byteValue());
	break;
	case 26: setSkillUsingState(((Boolean)value).booleanValue());
	break;
	case 27: setNameColor(((Integer)value).intValue());
	break;
	case 28: setObjectScale(((Short)value).shortValue());
	break;
	case 29: setObjectColor(((Integer)value).intValue());
	break;
	case 30: setObjectOpacity(((Boolean)value).booleanValue());
	break;
	case 31: setParticleName((String)value);
	break;
	case 32: setParticleX(((Short)value).shortValue());
	break;
	case 33: setParticleY(((Short)value).shortValue());
	break;
	case 34: setFootParticleName((String)value);
	break;
	case 35: setFootParticleX(((Short)value).shortValue());
	break;
	case 36: setFootParticleY(((Short)value).shortValue());
	break;
	case 37: setIronMaidenPercent(((Short)value).shortValue());
	break;
	 default :break;
	}
	}
	public void clear(){
	 	for(int i=aroundMarks.length-1;i>=0 ;i-- ){
	aroundMarks[i]=false;
	}
	}
}
