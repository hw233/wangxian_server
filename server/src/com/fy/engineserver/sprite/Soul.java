package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.Arrays;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.seal.SealManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 元神
 * 
 * 
 */

@SimpleEmbeddable
public class Soul {

	public double addPercent = 0.2;

	public static long switchCd = 300 * 1000;

	/** 元神类型-本尊 */
	public static int SOUL_TYPE_BASE = 0;
	/** 元神类型-元神 */
	public static int SOUL_TYPE_SOUL = 1;

	public static int SOUL_OPEN_LEVEL = 40;

	/** skill */
	// 未分配的技能点
	protected int unallocatedSkillPoint;
	// 职业基本技能等级
	protected byte[] careerBasicSkillsLevels;
	// 玩家掌握的第一系技能等级，0表示未掌握，下标表示职业中的第几个技能
	protected byte[] skillOneLevels;
	
	/**
	 * 变身技能等级
	 */
	protected byte[] bianShenLevels = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	// 元神类型 0 本元 1 地元
	private int soulType;

	// 角色的门派或职业
	private byte career;

	// 等级
	private int grade;

	// 坐骑属性
	// 玩家的坐骑列表
	private ArrayList<Long> horseArr = new ArrayList<Long>();
	// 选中的坐骑(默认坐骑)
	private long defaultHorseId = -1;
	// 正在骑的坐骑id(正在骑的坐骑，默认坐骑骑乘产生正在骑的坐骑)
	private long ridingHorseId = -1;
	// 是否正在马上
	protected boolean isUpOrDown;
	// 上次喂养时间
	private String lastFeedTime;
	// 今天喂养次数
	private int feedNum;

	// 基础生命
	private transient int hp;

	// 基础法力
	private transient int mp;

	private transient int hpRecoverBaseA;
	private transient int mpRecoverBaseA;

	// 最大生命
	private transient int maxHp;
	private transient int maxHpA;
	private transient int maxHpB;
	private transient int maxHpC;

	// 最大法力
	private transient int maxMp;
	private transient int maxMpA;
	private transient int maxMpB;
	private transient int maxMpC;

	// 力量
	private transient int strength;
	private transient int strengthA;
	private transient int strengthB;
	private transient int strengthC;

	// 智力 灵力
	private transient int spellpower;
	private transient int spellpowerA;
	private transient int spellpowerB;
	private transient int spellpowerC;

	// 耐力
	private transient int constitution;
	private transient int constitutionA;
	private transient int constitutionB;
	private transient int constitutionC;

	// 敏捷 身法
	private transient int dexterity;
	private transient int dexterityA;
	private transient int dexterityB;
	private transient int dexterityC;

	// 命中等级
	private transient int hit;
	private transient int hitA;
	private transient int hitB;
	private transient int hitC;
	private transient int hitX;

	// 命中率百分比
	private transient int attackRatePercent;
	private transient int attackRatePercentA;
	private transient int attackRatePercentB;
	private transient int attackRatePercentC;
	private transient int attackRatePercentX;

	// 回避
	private transient int dodge;
	private transient int dodgeA;
	private transient int dodgeB;
	private transient int dodgeC;
	private transient int dodgeX;

	// 回避百分比
	private transient int dodgePercent;
	private transient int dodgePercentA;
	private transient int dodgePercentB;
	private transient int dodgePercentC;
	private transient int dodgePercentX;

	// 精准
	private transient int accurate;
	private transient int accurateA;
	private transient int accurateB;
	private transient int accurateC;
	private transient int accurateX;

	// 近战攻击强度
	private transient int phyAttack;
	private transient int phyAttackA;
	private transient int phyAttackB;
	private transient int phyAttackC;
	private transient int phyAttackX;

	// 法术攻击强度
	private transient int magicAttack;
	private transient int magicAttackA;
	private transient int magicAttackB;
	private transient int magicAttackC;
	private transient int magicAttackX;

	// 会心一击，也叫暴击
	private transient int criticalHit;
	private transient int criticalHitA;
	private transient int criticalHitB;
	private transient int criticalHitC;
	private transient int criticalHitX;

	// 会心防御
	private transient int criticalDefence;
	private transient int criticalDefenceA;
	private transient int criticalDefenceB;
	private transient int criticalDefenceC;
	private transient int criticalDefenceX;

	// 暴击率百分比
	private transient int criticalHitPercent;
	private transient int criticalHitPercentA;
	private transient int criticalHitPercentB;
	private transient int criticalHitPercentC;
	private transient int criticalHitPercentX;

	// 物理防御
	private transient int phyDefence;
	private transient int phyDefenceA;
	private transient int phyDefenceB;
	private transient int phyDefenceC;

	// 魔法防御
	private transient int magicDefence;
	private transient int magicDefenceA;
	private transient int magicDefenceB;
	private transient int magicDefenceC;

	// 打断防御
	private transient int breakDefence;
	private transient int breakDefenceA;
	private transient int breakDefenceB;
	private transient int breakDefenceC;

	/*********************************************************** 属性攻击相关 ***********************************************************/

	// 火攻
	private transient int fireAttack;
	private transient int fireAttackA;
	private transient int fireAttackB;
	private transient int fireAttackC;
	private transient int fireAttackX;

	// 金防
	private transient int fireDefence;
	private transient int fireDefenceA;
	private transient int fireDefenceB;
	private transient int fireDefenceC;
	private transient int fireDefenceX;

	// 减少对方金防
	private transient int fireIgnoreDefence;
	private transient int fireIgnoreDefenceA;
	private transient int fireIgnoreDefenceB;
	private transient int fireIgnoreDefenceC;
	private transient int fireIgnoreDefenceX;

	// 冰攻
	private transient int blizzardAttack;
	private transient int blizzardAttackA;
	private transient int blizzardAttackB;
	private transient int blizzardAttackC;
	private transient int blizzardAttackX;

	// 水防
	private transient int blizzardDefence;
	private transient int blizzardDefenceA;
	private transient int blizzardDefenceB;
	private transient int blizzardDefenceC;
	private transient int blizzardDefenceX;

	// 减少对方水防
	private transient int blizzardIgnoreDefence;
	private transient int blizzardIgnoreDefenceA;
	private transient int blizzardIgnoreDefenceB;
	private transient int blizzardIgnoreDefenceC;
	private transient int blizzardIgnoreDefenceX;

	// 风攻
	private transient int windAttack;
	private transient int windAttackA;
	private transient int windAttackB;
	private transient int windAttackC;
	private transient int windAttackX;

	// 火防
	private transient int windDefence;
	private transient int windDefenceA;
	private transient int windDefenceB;
	private transient int windDefenceC;
	private transient int windDefenceX;

	// 减少对方火防
	private transient int windIgnoreDefence;
	private transient int windIgnoreDefenceA;
	private transient int windIgnoreDefenceB;
	private transient int windIgnoreDefenceC;
	private transient int windIgnoreDefenceX;

	// 雷攻
	private transient int thunderAttack;
	private transient int thunderAttackA;
	private transient int thunderAttackB;
	private transient int thunderAttackC;
	private transient int thunderAttackX;

	// 木防
	private transient int thunderDefence;
	private transient int thunderDefenceA;
	private transient int thunderDefenceB;
	private transient int thunderDefenceC;
	private transient int thunderDefenceX;

	// 减少对方木防
	private transient int thunderIgnoreDefence;
	private transient int thunderIgnoreDefenceA;
	private transient int thunderIgnoreDefenceB;
	private transient int thunderIgnoreDefenceC;
	private transient int thunderIgnoreDefenceX;

	public Soul() {
		// TODO Auto-generated constructor stub
	}

	public Soul(int type) {
		this.soulType = type;
		ec = new EquipmentColumn();
		setCareerBasicSkillsLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		setBianShenLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		setSkillOneLevels(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	}

	/**
	 * 装备栏
	 * 
	 * 此数据是要存盘的
	 */
	private EquipmentColumn ec;

	public boolean isMainSoul() {
		return getSoulType() == SOUL_TYPE_BASE;
	}

	public SoulEquipment4Client getSoulEquipment4Client() {
		ArticleEntity[] ees = ec.getEquipmentArrayByCopy();
		long[] eid = new long[ec.getEquipmentIds().length];
		for (int i = 0; i < ees.length; i++) {
			if (ees[i] != null) {
				eid[i] = ees[i].getId();
			} else {
				eid[i] = -1;
			}
		}
		return new SoulEquipment4Client(getSoulType(), eid);
	}

	/**
	 * 计算元神属性值，会进行元神初始化，并且穿戴装备
	 */
	public void account(Player player) {
		boolean isCurrSoul = player.isCurrSoul(this.getSoulType());
		double add = isCurrSoul ? 1 : (addPercent + player.getUpgradeNums() * 0.01);
		计算一级属性();
		if (isCurrSoul) {
			// 策划不需要用一级属性计算二级属性，二级属性直接从表中读出
			player.setMaxHPA(maxHpA);
			player.setMaxMPA(maxMpA);
			player.setPhyAttackA(phyAttackA);
			player.setMagicAttackA(magicAttackA);
			player.setHitA(hitA);
			player.setDodgeA(dodgeA);
			player.setCriticalHitA(criticalHitA);
		} else {
			player.setMaxHPX((int) (maxHpA * add));
			player.setMaxMPX((int) (maxMpA * add));
			player.setPhyAttackX((int) (phyAttackA * add));
			player.setMagicAttackX((int) (magicAttackA * add));
			player.setHitX((int) (hitA * add));
			player.setDodgeX((int) (dodgeA * add));
			player.setCriticalHitX((int) (criticalHitA * add));
		}
		// 重新装载所有的武器
		if (ec != null) {
			Arrays.fill(ec.currEquipmentName, "");
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			long equipmentIds[] = ec.getEquipmentIds();
			// 批量加载装备栏
			aem.getEntityByIds(equipmentIds);
			long equipmentIds_copy[] = new long[equipmentIds.length];

			for (int i = 0; i < equipmentIds.length; i++) {
				equipmentIds_copy[i] = equipmentIds[i];
				equipmentIds[i] = -1;
			}
			for (int i = 0; i < equipmentIds.length; i++) {
				try {
					ArticleEntity entity = aem.getEntity(equipmentIds_copy[i]);
					if (entity != null) {
						if (entity instanceof EquipmentEntity) {
							EquipmentEntity ee = (EquipmentEntity) entity;
							if (ee != null) {
								try {
									ec.putOn(ee, getSoulType());
								} catch (Exception e) {
									if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化角色属性] [穿装备失败] [" + player.getUsername() + "] [" + player.getName() + "] [index:" + i + "] [" + ee.getArticleName() + "] [装备ID：" + equipmentIds_copy[i] + "]", e);
								} finally {
									equipmentIds[i] = equipmentIds_copy[i];
								}
							}
						} else if (entity instanceof NewMagicWeaponEntity) {
							NewMagicWeaponEntity ne = (NewMagicWeaponEntity) entity;
							try {
								MagicWeaponManager.instance.putOn(player, ne, getSoulType());
							} catch (Exception e) {
								if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化角色属性] [穿装备失败] [" + player.getUsername() + "] [" + player.getName() + "] [index:" + i + "] [" + ne.getArticleName() + "] [装备ID：" + equipmentIds_copy[i] + "]", e);
							} finally {
								equipmentIds[i] = equipmentIds_copy[i];
							}
						}
					} else {
						equipmentIds[i] = equipmentIds_copy[i];
					}
				} catch (Exception ex) {
					equipmentIds[i] = equipmentIds_copy[i];
					if (Game.logger.isWarnEnabled()) Game.logger.warn("[初始化角色属性] [获取装备异常] [" + player.getUsername() + "] [" + player.getName() + "] [index:" + i + "] [装备ID：" + equipmentIds_copy[i] + "]", ex);
				}
			}
		}
		// TODO 给角色属性赋值
		if (isCurrSoul) {
			Game.logger.warn("[切换元神技能显示问题] [切换前] [职业："+career+"] ["+player.getName()+"] [bianshenjineng:"+(player.getBianShenLevels()==null?"nul":Arrays.toString(player.getBianShenLevels()))+"] [jichu:"+(player.getCareerBasicSkillsLevels()==null?"nul":Arrays.toString(player.getCareerBasicSkillsLevels()))+"]");
			player.setUnallocatedSkillPoint(this.getUnallocatedSkillPoint());
			player.setCareerBasicSkillsLevels(this.getCareerBasicSkillsLevels());
			player.setSkillOneLevels(this.getSkillOneLevels());
			player.setBianShenLevels(this.getBianShenLevels());
			Game.logger.warn("[切换元神技能显示问题] [切换后] [职业:"+career+"] ["+player.getName()+"] [bianshenjineng:"+(player.getBianShenLevels()==null?"nul":Arrays.toString(player.getBianShenLevels()))+"] [jichu:"+(player.getCareerBasicSkillsLevels()==null?"nul":Arrays.toString(player.getCareerBasicSkillsLevels()))+"]");
			player.重新计算所有的被动技能();
		}
		try {
			SkEnhanceManager.getInst().sendSkInfo(player);
		} catch (Exception e) {
			Skill.logger.error("切换元神刷新技能异常:" + player.getName(), e);
		}
	}

	private void 计算一级属性() {
		清空所有的BCX值();
		// int k = NumericalCalculator.根据等级计算一级属性基本值(career, getGrade(), NumericalCalculator.力量);
		// this.setStrengthA(k);
		// k = NumericalCalculator.根据等级计算一级属性基本值(career, getGrade(), NumericalCalculator.智力);
		// this.setSpellpowerA(k);
		// k = NumericalCalculator.根据等级计算一级属性基本值(career, getGrade(), NumericalCalculator.耐力);
		// this.setConstitutionA(k);
		// k = NumericalCalculator.根据等级计算一级属性基本值(career, getGrade(), NumericalCalculator.敏捷);
		// this.setDexterityA(k);
		//
		// this.setMaxMpA(CombatCaculator.F(getGrade()) * 25);

		this.setMaxHpA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][0]);
		this.setMaxMpA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][1]);
		this.setPhyAttackA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][2]);
		this.setMagicAttackA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][3]);
		this.setHitA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][4]);
		this.setDodgeA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][5]);
		this.setCriticalHitA(PlayerManager.各个职业各个级别的基础属性值[career][grade - 1][6]);
	}

	private void 清空所有的BCX值() {
		this.setPhyAttackB(0);
		this.setPhyAttackX(0);
		this.setPhyAttackC(0);
		this.setMagicAttackB(0);
		this.setMagicAttackX(0);
		this.setMagicAttackC(0);
		this.setPhyDefenceB(0);
		this.setPhyDefenceC(0);
		this.setMagicDefenceB(0);
		this.setMagicDefenceC(0);
		this.setDodgeB(0);
		this.setDodgeX(0);
		this.setDodgeC(0);
		this.setCriticalDefenceB(0);
		this.setCriticalDefenceX(0);
		this.setCriticalDefenceC(0);
		this.setHitB(0);
		this.setHitC(0);
		this.setCriticalHitB(0);
		this.setCriticalHitX(0);
		this.setCriticalHitC(0);
		this.setAccurateB(0);
		this.setAccurateX(0);
		this.setAccurateC(0);
		this.setBreakDefenceB(0);
		this.setBreakDefenceC(0);
		this.setFireAttackB(0);
		this.setFireAttackX(0);
		this.setFireAttackC(0);
		this.setFireDefenceB(0);
		this.setFireDefenceX(0);
		this.setFireDefenceC(0);
		this.setFireIgnoreDefenceB(0);
		this.setFireIgnoreDefenceX(0);
		this.setFireIgnoreDefenceC(0);
		this.setBlizzardAttackB(0);
		this.setBlizzardAttackX(0);
		this.setBlizzardAttackC(0);
		this.setBlizzardDefenceB(0);
		this.setBlizzardDefenceX(0);
		this.setBlizzardDefenceC(0);
		this.setBlizzardIgnoreDefenceB(0);
		this.setBlizzardIgnoreDefenceX(0);
		this.setBlizzardIgnoreDefenceC(0);
		this.setWindAttackB(0);
		this.setWindAttackX(0);
		this.setWindAttackC(0);
		this.setWindDefenceB(0);
		this.setWindDefenceX(0);
		this.setWindDefenceC(0);
		this.setWindIgnoreDefenceB(0);
		this.setWindIgnoreDefenceX(0);
		this.setWindIgnoreDefenceC(0);
		this.setThunderAttackB(0);
		this.setThunderAttackX(0);
		this.setThunderAttackC(0);
		this.setThunderDefenceB(0);
		this.setThunderDefenceX(0);
		this.setThunderDefenceC(0);
		this.setThunderIgnoreDefenceB(0);
		this.setThunderIgnoreDefenceX(0);
		this.setThunderIgnoreDefenceC(0);
		this.strength = 0;
		this.strengthA = 0;
		this.strengthB = 0;
		this.strengthC = 0;
		this.spellpower = 0;
		this.spellpowerA = 0;
		this.spellpowerB = 0;
		this.spellpowerC = 0;
		this.dexterity = 0;
		this.dexterityA = 0;
		this.dexterityB = 0;
		this.dexterityC = 0;
		this.constitution = 0;
		this.constitutionA = 0;
		this.constitutionB = 0;
		this.constitutionC = 0;

		this.maxHp = 0;
		this.maxHpA = 0;
		this.maxHpB = 0;
		this.maxHpC = 0;
		this.maxMp = 0;
		this.maxMpA = 0;
		this.maxMpB = 0;
		this.maxMpC = 0;
		this.phyDefence = 0;
		this.phyDefenceA = 0;
		this.phyDefenceB = 0;
		this.phyDefenceC = 0;
		this.magicDefence = 0;
		this.magicDefenceA = 0;
		this.magicDefenceB = 0;
		this.magicDefenceC = 0;
		this.hit = 0;
		this.hitA = 0;
		this.hitB = 0;
		this.hitC = 0;
		this.hitX = 0;
		this.dodge = 0;
		this.dodgeA = 0;
		this.dodgeB = 0;
		this.dodgeC = 0;
		this.dodgeX = 0;

		this.criticalHit = 0;
		this.criticalHitA = 0;
		this.criticalHitB = 0;
		this.criticalHitC = 0;
		this.criticalHitX = 0;

	}

	public void 计算当前元神一级属性并给人加上(Player player) {
		计算一级属性();
		try {
			player.modifyAttrRate();
		}catch(Exception e) {
			
		}
		// player.setMaxHPA(getMaxHp());
		// player.setMaxMPA(getMaxMp());
		// player.setStrengthA(this.getStrengthA());
		// player.setSpellpowerA(this.getSpellpowerA());
		// player.setConstitutionA(this.getConstitutionA());
		// player.setDexterityA(this.getDexterityA());
		// player.setMaxMPA(this.getMaxMpA());
		// 策划不需要用一级属性计算二级属性，二级属性直接从表中读出
		player.setMaxHPA(maxHpA);
		player.setMaxMPA(maxMpA);
		player.setPhyAttackA(phyAttackA);
		player.setMagicAttackA(magicAttackA);
		player.setHitA(hitA);
		player.setDodgeA(dodgeA);
		player.setCriticalHitA(criticalHitA);
	}

	// public void 计算升级后所加属性点() {
	// setMaxHpA(getGrade() * 10 + 10);
	// }
	//
	// public void 计算当前元神升级后所加属性点并给人加上(Player player) {
	// 计算升级后所加属性点();
	// player.setMaxHPA(getMaxHpA());
	//
	// }

	public int getSoulType() {
		return soulType;
	}

	public void setSoulType(int soulType) {
		this.soulType = soulType;
	}

	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMp() {
		return mp;
	}

	public void setMp(int mp) {
		this.mp = mp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getMaxHpA() {
		return maxHpA;
	}

	public void setMaxHpA(int maxHpA) {
		this.maxHpA = maxHpA;
		setMaxHp((this.maxHp + this.maxHpA + this.maxHpB) * (100 + this.maxHpC) / 100);
	}

	public int getMaxHpB() {
		return maxHpB;
	}

	public void setMaxHpB(int maxHpB) {
		this.maxHpB = maxHpB;
		setMaxHp((this.maxHp + this.maxHpA + this.maxHpB) * (100 + this.maxHpC) / 100);
	}

	public int getMaxHpC() {
		return maxHpC;
	}

	public void setMaxHpC(int maxHpC) {
		this.maxHpC = maxHpC;
		setMaxHp((this.maxHp + this.maxHpA + this.maxHpB) * (100 + this.maxHpC) / 100);
	}

	public int getMaxMp() {
		return maxMp;
	}

	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}

	public int getMaxMpA() {
		return maxMpA;
	}

	public void setMaxMpA(int maxMpA) {
		this.maxMpA = maxMpA;
		setMaxMp((this.maxMp + this.maxMpA + this.maxMpB) * (100 + this.maxMpC) / 100);
	}

	public int getMaxMpB() {
		return maxMpB;
	}

	public void setMaxMpB(int maxMpB) {
		this.maxMpB = maxMpB;
	}

	public int getMaxMpC() {
		return maxMpC;
	}

	public void setMaxMpC(int maxMpC) {
		this.maxMpC = maxMpC;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		// this.strength = strength;
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(strength, NumericalCalculator.力量兑换外功);
		// this.setPhyAttackA(k);
	}

	public int getStrengthA() {
		return strengthA;
	}

	public void setStrengthA(int strengthA) {
		// this.strengthA = strengthA;
		// int a = ((strengthA + strengthB) * (strengthC + 100) / 100);
		// this.setStrength(a);
	}

	public int getStrengthB() {
		return strengthB;
	}

	public void setStrengthB(int strengthB) {
		// this.strengthB = strengthB;
	}

	public int getStrengthC() {
		return strengthC;
	}

	public void setStrengthC(int strengthC) {
		// this.strengthC = strengthC;
	}

	public int getSpellpower() {
		return spellpower;
	}

	public void setSpellpower(int spellpower) {
		// this.spellpower = spellpower;
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(spellpower, NumericalCalculator.灵力兑换内法);
		// this.setMagicAttackA(k);
	}

	public int getSpellpowerA() {
		return spellpowerA;
	}

	public void setSpellpowerA(int spellpowerA) {
		// this.spellpowerA = spellpowerA;
		// int a = ((spellpowerA + spellpowerB) * (spellpowerC + 100) / 100);
		// this.setSpellpower(a);
	}

	public int getSpellpowerB() {
		return spellpowerB;
	}

	public void setSpellpowerB(int spellpowerB) {
		// this.spellpowerB = spellpowerB;
		// int a = ((spellpowerA + spellpowerB) * (spellpowerC + 100) / 100);
		// this.setSpellpower(a);
	}

	public int getSpellpowerC() {
		return spellpowerC;
	}

	public void setSpellpowerC(int spellpowerC) {
		// this.spellpowerC = spellpowerC;
		// int a = ((spellpowerA + spellpowerB) * (spellpowerC + 100) / 100);
		// this.setSpellpower(a);
	}

	public int getConstitution() {
		return constitution;
	}

	public void setConstitution(int constitution) {
		// this.constitution = constitution;
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(constitution, NumericalCalculator.耐力兑换HP);
		// this.setMaxHpA(k);
	}

	public int getConstitutionA() {
		return constitutionA;
	}

	public void setConstitutionA(int constitutionA) {
		// this.constitutionA = constitutionA;
		// int a = ((constitutionA + constitutionB) * (constitutionC + 100) / 100);
		// setConstitution(a);
	}

	public int getConstitutionB() {
		return constitutionB;
	}

	public void setConstitutionB(int constitutionB) {
		// this.constitutionB = constitutionB;
		// int a = ((constitutionA + constitutionB) * (constitutionC + 100) / 100);
		// setConstitution(a);
	}

	public int getConstitutionC() {
		return constitutionC;
	}

	public void setConstitutionC(int constitutionC) {
		// this.constitutionC = constitutionC;
		// int a = ((constitutionA + constitutionB) * (constitutionC + 100) / 100);
		// setConstitution(a);
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		// this.dexterity = dexterity;
		// int k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity, NumericalCalculator.身法兑换命中);
		// this.setHitA(k);
		// k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity, NumericalCalculator.身法兑换闪躲);
		// this.setDodgeA(k);
		// k = NumericalCalculator.根据一级属性计算二级属性基本值(dexterity, NumericalCalculator.身法兑换会心一击);
		// this.setCriticalHitA(k);
	}

	public int getDexterityA() {
		return dexterityA;
	}

	public void setDexterityA(int dexterityA) {
		// this.dexterityA = dexterityA;
		// int a = ((dexterityA + dexterityB) * (dexterityC + 100) / 100);
		// setDexterity(a);
	}

	public int getDexterityB() {
		return dexterityB;
	}

	public void setDexterityB(int dexterityB) {
		// this.dexterityB = dexterityB;
		// int a = ((dexterityA + dexterityB) * (dexterityC + 100) / 100);
		// setDexterity(a);
	}

	public int getDexterityC() {
		return dexterityC;
	}

	public void setDexterityC(int dexterityC) {
		// this.dexterityC = dexterityC;
		// int a = ((dexterityA + dexterityB) * (dexterityC + 100) / 100);
		// setDexterity(a);
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getHitA() {
		return hitA;
	}

	public void setHitA(int hitA) {
		this.hitA = hitA;
		int a = ((hitA + hitB) * (hitC + 100) / 100);
		setHit(a);
	}

	public int getHitB() {
		return hitB;
	}

	public void setHitB(int hitB) {
		this.hitB = hitB;
		int a = ((hitA + hitB) * (hitC + 100) / 100);
		setHit(a);
	}

	public int getHitC() {
		return hitC;
	}

	public void setHitC(int hitC) {
		this.hitC = hitC;
		int a = ((hitA + hitB) * (hitC + 100) / 100);
		setHit(a);
	}

	public int getAttackRatePercent() {
		return attackRatePercent;
	}

	public void setAttackRatePercent(int attackRatePercent) {
		this.attackRatePercent = attackRatePercent;
	}

	public int getAttackRatePercentA() {
		return attackRatePercentA;
	}

	public void setAttackRatePercentA(int attackRatePercentA) {
		this.attackRatePercentA = attackRatePercentA;
	}

	public int getAttackRatePercentB() {
		return attackRatePercentB;
	}

	public void setAttackRatePercentB(int attackRatePercentB) {
		this.attackRatePercentB = attackRatePercentB;
	}

	public int getAttackRatePercentC() {
		return attackRatePercentC;
	}

	public void setAttackRatePercentC(int attackRatePercentC) {
		this.attackRatePercentC = attackRatePercentC;
	}

	public int getAttackRatePercentX() {
		return attackRatePercentX;
	}

	public void setAttackRatePercentX(int attackRatePercentX) {
		this.attackRatePercentX = attackRatePercentX;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getDodgeA() {
		return dodgeA;
	}

	public void setDodgeA(int dodgeA) {
		this.dodgeA = dodgeA;
		setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public int getDodgeB() {
		return dodgeB;
	}

	public void setDodgeB(int dodgeB) {
		this.dodgeB = dodgeB;
		setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public int getDodgeC() {
		return dodgeC;
	}

	public void setDodgeC(int dodgeC) {
		this.dodgeC = dodgeC;
		setDodge((this.dodgeA + this.dodgeB) * (100 + this.dodgeC) / 100);
	}

	public int getDodgeX() {
		return dodgeX;
	}

	public void setDodgeX(int dodgeX) {
		this.dodgeX = dodgeX;
	}

	public int getDodgePercent() {
		return dodgePercent;
	}

	public void setDodgePercent(int dodgePercent) {
		this.dodgePercent = dodgePercent;
	}

	public int getDodgePercentA() {
		return dodgePercentA;
	}

	public void setDodgePercentA(int dodgePercentA) {
		this.dodgePercentA = dodgePercentA;
	}

	public int getDodgePercentB() {
		return dodgePercentB;
	}

	public void setDodgePercentB(int dodgePercentB) {
		this.dodgePercentB = dodgePercentB;
	}

	public int getDodgePercentC() {
		return dodgePercentC;
	}

	public void setDodgePercentC(int dodgePercentC) {
		this.dodgePercentC = dodgePercentC;
	}

	public int getDodgePercentX() {
		return dodgePercentX;
	}

	public void setDodgePercentX(int dodgePercentX) {
		this.dodgePercentX = dodgePercentX;
	}

	public int getAccurate() {
		return accurate;
	}

	public void setAccurate(int accurate) {
		this.accurate = accurate;
	}

	public int getAccurateA() {
		return accurateA;
	}

	public void setAccurateA(int accurateA) {
		this.accurateA = accurateA;
		setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public int getAccurateB() {
		return accurateB;
	}

	public void setAccurateB(int accurateB) {
		this.accurateB = accurateB;
		setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public int getAccurateC() {
		return accurateC;
	}

	public void setAccurateC(int accurateC) {
		this.accurateC = accurateC;
		setAccurate((this.accurateA + this.accurateB) * (100 + this.accurateC) / 100);
	}

	public int getAccurateX() {
		return accurateX;
	}

	public void setAccurateX(int accurateX) {
		this.accurateX = accurateX;
	}

	public int getPhyAttack() {
		return phyAttack;
	}

	public void setPhyAttack(int phyAttack) {
		this.phyAttack = phyAttack;
	}

	public int getPhyAttackA() {
		return phyAttackA;
	}

	public void setPhyAttackA(int phyAttackA) {
		this.phyAttackA = phyAttackA;
		setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackB() {
		return phyAttackB;
	}

	public void setPhyAttackB(int phyAttackB) {
		this.phyAttackB = phyAttackB;
		setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackC() {
		return phyAttackC;
	}

	public void setPhyAttackC(int phyAttackC) {
		this.phyAttackC = phyAttackC;
		setPhyAttack((this.phyAttackA + this.phyAttackB) * (100 + this.phyAttackC) / 100);
	}

	public int getPhyAttackX() {
		return phyAttackX;
	}

	public void setPhyAttackX(int phyAttackX) {
		this.phyAttackX = phyAttackX;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getMagicAttackA() {
		return magicAttackA;
	}

	public void setMagicAttackA(int magicAttackA) {
		this.magicAttackA = magicAttackA;
		setMagicAttack((this.magicAttackA + this.magicAttackB) * (100 + this.magicAttackC) / 100);
	}

	public int getMagicAttackB() {
		return magicAttackB;
	}

	public void setMagicAttackB(int magicAttackB) {
		this.magicAttackB = magicAttackB;
		setMagicAttack((this.magicAttackA + this.magicAttackB) * (100 + this.magicAttackC) / 100);
	}

	public int getMagicAttackC() {
		return magicAttackC;
	}

	public void setMagicAttackC(int magicAttackC) {
		this.magicAttackC = magicAttackC;
		setMagicAttack((this.magicAttackA + this.magicAttackB) * (100 + this.magicAttackC) / 100);
	}

	public int getMagicAttackX() {
		return magicAttackX;
	}

	public void setMagicAttackX(int magicAttackX) {
		this.magicAttackX = magicAttackX;
	}

	public int getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int criticalHit) {
		this.criticalHit = criticalHit;
	}

	public int getCriticalHitA() {
		return criticalHitA;
	}

	public void setCriticalHitA(int criticalHitA) {
		this.criticalHitA = criticalHitA;
		setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public int getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int criticalHitB) {
		this.criticalHitB = criticalHitB;
		setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public int getCriticalHitC() {
		return criticalHitC;
	}

	public void setCriticalHitC(int criticalHitC) {
		this.criticalHitC = criticalHitC;
		setCriticalHit((this.criticalHitA + this.criticalHitB) * (100 + this.criticalHitC) / 100);
	}

	public int getCriticalHitX() {
		return criticalHitX;
	}

	public void setCriticalHitX(int criticalHitX) {
		this.criticalHitX = criticalHitX;
	}

	public int getCriticalDefence() {
		return criticalDefence;
	}

	public void setCriticalDefence(int criticalDefence) {
		this.criticalDefence = criticalDefence;
	}

	public int getCriticalDefenceA() {
		return criticalDefenceA;
	}

	public void setCriticalDefenceA(int criticalDefenceA) {
		this.criticalDefenceA = criticalDefenceA;
		setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public int getCriticalDefenceB() {
		return criticalDefenceB;
	}

	public void setCriticalDefenceB(int criticalDefenceB) {
		this.criticalDefenceB = criticalDefenceB;
		setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public int getCriticalDefenceC() {
		return criticalDefenceC;
	}

	public void setCriticalDefenceC(int criticalDefenceC) {
		this.criticalDefenceC = criticalDefenceC;
		setCriticalDefence((this.criticalDefenceA + this.criticalDefenceB) * (100 + this.criticalDefenceC) / 100);
	}

	public int getCriticalDefenceX() {
		return criticalDefenceX;
	}

	public void setCriticalDefenceX(int criticalDefenceX) {
		this.criticalDefenceX = criticalDefenceX;
	}

	public int getCriticalHitPercent() {
		return criticalHitPercent;
	}

	public void setCriticalHitPercent(int criticalHitPercent) {
		this.criticalHitPercent = criticalHitPercent;
	}

	public int getCriticalHitPercentA() {
		return criticalHitPercentA;
	}

	public void setCriticalHitPercentA(int criticalHitPercentA) {
		this.criticalHitPercentA = criticalHitPercentA;
	}

	public int getCriticalHitPercentB() {
		return criticalHitPercentB;
	}

	public void setCriticalHitPercentB(int criticalHitPercentB) {
		this.criticalHitPercentB = criticalHitPercentB;
	}

	public int getCriticalHitPercentC() {
		return criticalHitPercentC;
	}

	public void setCriticalHitPercentC(int criticalHitPercentC) {
		this.criticalHitPercentC = criticalHitPercentC;
	}

	public int getCriticalHitPercentX() {
		return criticalHitPercentX;
	}

	public void setCriticalHitPercentX(int criticalHitPercentX) {
		this.criticalHitPercentX = criticalHitPercentX;
	}

	public int getFireAttack() {
		return fireAttack;
	}

	public void setFireAttack(int fireAttack) {
		this.fireAttack = fireAttack;
	}

	public int getFireAttackA() {
		return fireAttackA;
	}

	public void setFireAttackA(int fireAttackA) {
		this.fireAttackA = fireAttackA;
		setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	public int getFireAttackB() {
		return fireAttackB;
	}

	public void setFireAttackB(int fireAttackB) {
		this.fireAttackB = fireAttackB;
		setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	public int getFireAttackC() {
		return fireAttackC;
	}

	public void setFireAttackC(int fireAttackC) {
		this.fireAttackC = fireAttackC;
		setFireAttack((this.fireAttackA + this.fireAttackB) * (100 + this.fireAttackC) / 100);
	}

	public int getFireAttackX() {
		return fireAttackX;
	}

	public void setFireAttackX(int fireAttackX) {
		this.fireAttackX = fireAttackX;
	}

	public int getFireDefence() {
		return fireDefence;
	}

	public void setFireDefence(int fireDefence) {
		this.fireDefence = fireDefence;
	}

	public int getFireDefenceA() {
		return fireDefenceA;
	}

	public void setFireDefenceA(int fireDefenceA) {
		this.fireDefenceA = fireDefenceA;
		setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	public int getFireDefenceB() {
		return fireDefenceB;
	}

	public void setFireDefenceB(int fireDefenceB) {
		this.fireDefenceB = fireDefenceB;
		setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	public int getFireDefenceC() {
		return fireDefenceC;
	}

	public void setFireDefenceC(int fireDefenceC) {
		this.fireDefenceC = fireDefenceC;
		setFireDefence((this.fireDefenceA + this.fireDefenceB) * (100 + this.fireDefenceC) / 100);
	}

	public int getFireDefenceX() {
		return fireDefenceX;
	}

	public void setFireDefenceX(int fireDefenceX) {
		this.fireDefenceX = fireDefenceX;
	}

	public int getFireIgnoreDefence() {
		return fireIgnoreDefence;
	}

	public void setFireIgnoreDefence(int fireIgnoreDefence) {
		this.fireIgnoreDefence = fireIgnoreDefence;
	}

	public int getFireIgnoreDefenceA() {
		return fireIgnoreDefenceA;
	}

	public void setFireIgnoreDefenceA(int fireIgnoreDefenceA) {
		this.fireIgnoreDefenceA = fireIgnoreDefenceA;
		setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public int getFireIgnoreDefenceB() {
		return fireIgnoreDefenceB;
	}

	public void setFireIgnoreDefenceB(int fireIgnoreDefenceB) {
		this.fireIgnoreDefenceB = fireIgnoreDefenceB;
		setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public int getFireIgnoreDefenceC() {
		return fireIgnoreDefenceC;
	}

	public void setFireIgnoreDefenceC(int fireIgnoreDefenceC) {
		this.fireIgnoreDefenceC = fireIgnoreDefenceC;
		setFireIgnoreDefence((this.fireIgnoreDefenceA + this.fireIgnoreDefenceB) * (100 + this.fireIgnoreDefenceC) / 100);
	}

	public int getFireIgnoreDefenceX() {
		return fireIgnoreDefenceX;
	}

	public void setFireIgnoreDefenceX(int fireIgnoreDefenceX) {
		this.fireIgnoreDefenceX = fireIgnoreDefenceX;
	}

	public int getBlizzardAttack() {
		return blizzardAttack;
	}

	public void setBlizzardAttack(int blizzardAttack) {
		this.blizzardAttack = blizzardAttack;
	}

	public int getBlizzardAttackA() {
		return blizzardAttackA;
	}

	public void setBlizzardAttackA(int blizzardAttackA) {
		this.blizzardAttackA = blizzardAttackA;
		setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	public int getBlizzardAttackB() {
		return blizzardAttackB;
	}

	public void setBlizzardAttackB(int blizzardAttackB) {
		this.blizzardAttackB = blizzardAttackB;
		setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	public int getBlizzardAttackC() {
		return blizzardAttackC;
	}

	public void setBlizzardAttackC(int blizzardAttackC) {
		this.blizzardAttackC = blizzardAttackC;
		setBlizzardAttack((this.blizzardAttackA + this.blizzardAttackB) * (100 + this.blizzardAttackC) / 100);
	}

	public int getBlizzardAttackX() {
		return blizzardAttackX;
	}

	public void setBlizzardAttackX(int blizzardAttackX) {
		this.blizzardAttackX = blizzardAttackX;
	}

	public int getBlizzardDefence() {
		return blizzardDefence;
	}

	public void setBlizzardDefence(int blizzardDefence) {
		this.blizzardDefence = blizzardDefence;
	}

	public int getBlizzardDefenceA() {
		return blizzardDefenceA;
	}

	public void setBlizzardDefenceA(int blizzardDefenceA) {
		this.blizzardDefenceA = blizzardDefenceA;
		setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	public int getBlizzardDefenceB() {
		return blizzardDefenceB;
	}

	public void setBlizzardDefenceB(int blizzardDefenceB) {
		this.blizzardDefenceB = blizzardDefenceB;
		setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	public int getBlizzardDefenceC() {
		return blizzardDefenceC;
	}

	public void setBlizzardDefenceC(int blizzardDefenceC) {
		this.blizzardDefenceC = blizzardDefenceC;
		setBlizzardDefence((this.blizzardDefenceA + this.blizzardDefenceB) * (100 + this.blizzardDefenceC) / 100);
	}

	public int getBlizzardDefenceX() {
		return blizzardDefenceX;
	}

	public void setBlizzardDefenceX(int blizzardDefenceX) {
		this.blizzardDefenceX = blizzardDefenceX;
	}

	public int getBlizzardIgnoreDefence() {
		return blizzardIgnoreDefence;
	}

	public void setBlizzardIgnoreDefence(int blizzardIgnoreDefence) {
		this.blizzardIgnoreDefence = blizzardIgnoreDefence;
	}

	public int getBlizzardIgnoreDefenceA() {
		return blizzardIgnoreDefenceA;
	}

	public void setBlizzardIgnoreDefenceA(int blizzardIgnoreDefenceA) {
		this.blizzardIgnoreDefenceA = blizzardIgnoreDefenceA;
		setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public int getBlizzardIgnoreDefenceB() {
		return blizzardIgnoreDefenceB;
	}

	public void setBlizzardIgnoreDefenceB(int blizzardIgnoreDefenceB) {
		this.blizzardIgnoreDefenceB = blizzardIgnoreDefenceB;
		setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public int getBlizzardIgnoreDefenceC() {
		return blizzardIgnoreDefenceC;
	}

	public void setBlizzardIgnoreDefenceC(int blizzardIgnoreDefenceC) {
		this.blizzardIgnoreDefenceC = blizzardIgnoreDefenceC;
		setBlizzardIgnoreDefence((this.blizzardIgnoreDefenceA + this.blizzardIgnoreDefenceB) * (100 + this.blizzardIgnoreDefenceC) / 100);
	}

	public int getBlizzardIgnoreDefenceX() {
		return blizzardIgnoreDefenceX;
	}

	public void setBlizzardIgnoreDefenceX(int blizzardIgnoreDefenceX) {
		this.blizzardIgnoreDefenceX = blizzardIgnoreDefenceX;
	}

	public int getWindAttack() {
		return windAttack;
	}

	public void setWindAttack(int windAttack) {
		this.windAttack = windAttack;
	}

	public int getWindAttackA() {
		return windAttackA;
	}

	public void setWindAttackA(int windAttackA) {
		this.windAttackA = windAttackA;
		setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);
	}

	public int getWindAttackB() {
		return windAttackB;
	}

	public void setWindAttackB(int windAttackB) {
		this.windAttackB = windAttackB;
		setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);
	}

	public int getWindAttackC() {
		return windAttackC;
	}

	public void setWindAttackC(int windAttackC) {
		this.windAttackC = windAttackC;
		setWindAttack((this.windAttackA + this.windAttackB) * (100 + this.windAttackC) / 100);
	}

	public int getWindAttackX() {
		return windAttackX;
	}

	public void setWindAttackX(int windAttackX) {
		this.windAttackX = windAttackX;
	}

	public int getWindDefence() {
		return windDefence;
	}

	public void setWindDefence(int windDefence) {
		this.windDefence = windDefence;
	}

	public int getWindDefenceA() {
		return windDefenceA;
	}

	public void setWindDefenceA(int windDefenceA) {
		this.windDefenceA = windDefenceA;
		setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	public int getWindDefenceB() {
		return windDefenceB;
	}

	public void setWindDefenceB(int windDefenceB) {
		this.windDefenceB = windDefenceB;
		setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	public int getWindDefenceC() {
		return windDefenceC;
	}

	public void setWindDefenceC(int windDefenceC) {
		this.windDefenceC = windDefenceC;
		setWindDefence((this.windDefenceA + this.windDefenceB) * (100 + this.windDefenceC) / 100);
	}

	public int getWindDefenceX() {
		return windDefenceX;
	}

	public void setWindDefenceX(int windDefenceX) {
		this.windDefenceX = windDefenceX;
	}

	public int getWindIgnoreDefence() {
		return windIgnoreDefence;
	}

	public void setWindIgnoreDefence(int windIgnoreDefence) {
		this.windIgnoreDefence = windIgnoreDefence;
	}

	public int getWindIgnoreDefenceA() {
		return windIgnoreDefenceA;
	}

	public void setWindIgnoreDefenceA(int windIgnoreDefenceA) {
		this.windIgnoreDefenceA = windIgnoreDefenceA;
		setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public int getWindIgnoreDefenceB() {
		return windIgnoreDefenceB;
	}

	public void setWindIgnoreDefenceB(int windIgnoreDefenceB) {
		this.windIgnoreDefenceB = windIgnoreDefenceB;
		setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public int getWindIgnoreDefenceC() {
		return windIgnoreDefenceC;
	}

	public void setWindIgnoreDefenceC(int windIgnoreDefenceC) {
		this.windIgnoreDefenceC = windIgnoreDefenceC;
		setWindIgnoreDefence((this.windIgnoreDefenceA + this.windIgnoreDefenceB) * (100 + this.windIgnoreDefenceC) / 100);
	}

	public int getWindIgnoreDefenceX() {
		return windIgnoreDefenceX;
	}

	public void setWindIgnoreDefenceX(int windIgnoreDefenceX) {
		this.windIgnoreDefenceX = windIgnoreDefenceX;
	}

	public int getThunderAttack() {
		return thunderAttack;
	}

	public void setThunderAttack(int thunderAttack) {
		this.thunderAttack = thunderAttack;
	}

	public int getThunderAttackA() {
		return thunderAttackA;
	}

	public void setThunderAttackA(int thunderAttackA) {
		this.thunderAttackA = thunderAttackA;
		setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackB() {
		return thunderAttackB;
	}

	public void setThunderAttackB(int thunderAttackB) {
		this.thunderAttackB = thunderAttackB;
		setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackC() {
		return thunderAttackC;
	}

	public void setThunderAttackC(int thunderAttackC) {
		this.thunderAttackC = thunderAttackC;
		setThunderAttack((this.thunderAttackA + this.thunderAttackB) * (100 + this.thunderAttackC) / 100);
	}

	public int getThunderAttackX() {
		return thunderAttackX;
	}

	public void setThunderAttackX(int thunderAttackX) {
		this.thunderAttackX = thunderAttackX;
	}

	public int getThunderDefence() {
		return thunderDefence;
	}

	public void setThunderDefence(int thunderDefence) {
		this.thunderDefence = thunderDefence;
	}

	public int getThunderDefenceA() {
		return thunderDefenceA;
	}

	public void setThunderDefenceA(int thunderDefenceA) {
		this.thunderDefenceA = thunderDefenceA;
		setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	public int getThunderDefenceB() {
		return thunderDefenceB;
	}

	public void setThunderDefenceB(int thunderDefenceB) {
		this.thunderDefenceB = thunderDefenceB;
		setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	public int getThunderDefenceC() {
		return thunderDefenceC;
	}

	public void setThunderDefenceC(int thunderDefenceC) {
		this.thunderDefenceC = thunderDefenceC;
		setThunderDefence((this.thunderDefenceA + this.thunderDefenceB) * (100 + this.thunderDefenceC) / 100);
	}

	public int getThunderDefenceX() {
		return thunderDefenceX;
	}

	public void setThunderDefenceX(int thunderDefenceX) {
		this.thunderDefenceX = thunderDefenceX;
	}

	public int getThunderIgnoreDefence() {
		return thunderIgnoreDefence;
	}

	public void setThunderIgnoreDefence(int thunderIgnoreDefence) {
		this.thunderIgnoreDefence = thunderIgnoreDefence;
	}

	public int getThunderIgnoreDefenceA() {
		return thunderIgnoreDefenceA;
	}

	public void setThunderIgnoreDefenceA(int thunderIgnoreDefenceA) {
		this.thunderIgnoreDefenceA = thunderIgnoreDefenceA;
		setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public int getThunderIgnoreDefenceB() {
		return thunderIgnoreDefenceB;
	}

	public void setThunderIgnoreDefenceB(int thunderIgnoreDefenceB) {
		this.thunderIgnoreDefenceB = thunderIgnoreDefenceB;
		setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public int getThunderIgnoreDefenceC() {
		return thunderIgnoreDefenceC;
	}

	public void setThunderIgnoreDefenceC(int thunderIgnoreDefenceC) {
		this.thunderIgnoreDefenceC = thunderIgnoreDefenceC;
		setThunderIgnoreDefence((this.thunderIgnoreDefenceA + this.thunderIgnoreDefenceB) * (100 + this.thunderIgnoreDefenceC) / 100);
	}

	public int getThunderIgnoreDefenceX() {
		return thunderIgnoreDefenceX;
	}

	public void setThunderIgnoreDefenceX(int thunderIgnoreDefenceX) {
		this.thunderIgnoreDefenceX = thunderIgnoreDefenceX;
	}

	public EquipmentColumn getEc() {
		return ec;
	}

	public void setEc(EquipmentColumn ec) {
		this.ec = ec;
	}

	public int getPhyDefence() {
		return phyDefence;
	}

	public void setPhyDefence(int phyDefence) {
		this.phyDefence = phyDefence;
	}

	public int getPhyDefenceA() {
		return phyDefenceA;
	}

	public void setPhyDefenceA(int phyDefenceA) {
		this.phyDefenceA = phyDefenceA;
		setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (100 + this.phyDefenceC) / 100);
	}

	public int getPhyDefenceB() {
		return phyDefenceB;
	}

	public void setPhyDefenceB(int phyDefenceB) {
		this.phyDefenceB = phyDefenceB;
		setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (100 + this.phyDefenceC) / 100);
	}

	public int getPhyDefenceC() {
		return phyDefenceC;
	}

	public void setPhyDefenceC(int phyDefenceC) {
		this.phyDefenceC = phyDefenceC;
		setPhyDefence((this.phyDefenceA + this.phyDefenceB) * (100 + this.phyDefenceC) / 100);
	}

	public int getMagicDefence() {
		return magicDefence;
	}

	public void setMagicDefence(int magicDefence) {
		this.magicDefence = magicDefence;
	}

	public int getMagicDefenceA() {
		return magicDefenceA;
	}

	public void setMagicDefenceA(int magicDefenceA) {
		this.magicDefenceA = magicDefenceA;
		setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (100 + this.magicDefenceC) / 100);
	}

	public int getMagicDefenceB() {
		return magicDefenceB;
	}

	public void setMagicDefenceB(int magicDefenceB) {
		this.magicDefenceB = magicDefenceB;
		setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (100 + this.magicDefenceC) / 100);
	}

	public int getMagicDefenceC() {
		return magicDefenceC;
	}

	public void setMagicDefenceC(int magicDefenceC) {
		this.magicDefenceC = magicDefenceC;
		setMagicDefence((this.magicDefenceA + this.magicDefenceB) * (100 + this.magicDefenceC) / 100);
	}

	public int getBreakDefence() {
		return breakDefence;
	}

	public void setBreakDefence(int breakDefence) {
		this.breakDefence = breakDefence;
	}

	public int getBreakDefenceA() {
		return breakDefenceA;
	}

	public void setBreakDefenceA(int breakDefenceA) {
		this.breakDefenceA = breakDefenceA;
		setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public int getBreakDefenceB() {
		return breakDefenceB;
	}

	public void setBreakDefenceB(int breakDefenceB) {
		this.breakDefenceB = breakDefenceB;
		setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public int getBreakDefenceC() {
		return breakDefenceC;
	}

	public void setBreakDefenceC(int breakDefenceC) {
		this.breakDefenceC = breakDefenceC;
		setBreakDefence((this.breakDefenceA + this.breakDefenceB) * (100 + this.breakDefenceC) / 100);
	}

	public int getHpRecoverBaseA() {
		return hpRecoverBaseA;
	}

	public void setHpRecoverBaseA(int hpRecoverBaseA) {
		this.hpRecoverBaseA = hpRecoverBaseA;
	}

	public int getMpRecoverBaseA() {
		return mpRecoverBaseA;
	}

	public void setMpRecoverBaseA(int mpRecoverBaseA) {
		this.mpRecoverBaseA = mpRecoverBaseA;
	}

	public int getUnallocatedSkillPoint() {
		return unallocatedSkillPoint;
	}

	public void setUnallocatedSkillPoint(int v) {
		if (v < 0) {
			v = 0;
		}
		this.unallocatedSkillPoint = v;
	}

	public byte[] getCareerBasicSkillsLevels() {
		return careerBasicSkillsLevels;
	}

	public void setCareerBasicSkillsLevels(byte[] careerBasicSkillsLevels) {
		this.careerBasicSkillsLevels = careerBasicSkillsLevels;
	}

	public byte[] getBianShenLevels() {
		return this.bianShenLevels;
	}

	public void setBianShenLevels(byte[] bianShenLevels) {
		this.bianShenLevels = bianShenLevels;
	}

	public byte[] getSkillOneLevels() {
		return skillOneLevels;
	}

	public void setSkillOneLevels(byte[] skillOneLevels) {
		this.skillOneLevels = skillOneLevels;
	}

	public ArrayList<Long> getHorseArr() {
		return horseArr;
	}

	public void setHorseArr(ArrayList<Long> horseArr) {
		this.horseArr = horseArr;
	}

	public long getDefaultHorseId() {
		return defaultHorseId;
	}

	public void setDefaultHorseId(long defaultHorseId) {
		this.defaultHorseId = defaultHorseId;
	}

	public long getRidingHorseId() {
		return ridingHorseId;
	}

	public void setRidingHorseId(long ridingHorseId) {
		this.ridingHorseId = ridingHorseId;
	}

	public boolean isUpOrDown() {
		return isUpOrDown;
	}

	public void setUpOrDown(boolean isUpOrDown) {
		this.isUpOrDown = isUpOrDown;
	}

	public String getLastFeedTime() {
		return lastFeedTime;
	}

	public void setLastFeedTime(String lastFeedTime) {
		this.lastFeedTime = lastFeedTime;
	}

	public int getFeedNum() {
		return feedNum;
	}

	public void setFeedNum(int feedNum) {
		this.feedNum = feedNum;
	}

	public double getAddPercent() {
		return addPercent;
	}

	// 会不会存储？请重启检查
	public void setAddPercent(double addPercent) {
		this.addPercent = addPercent;
	}

	@Override
	public String toString() {
		return "Soul [soulType=" + soulType + ", career=" + career + ", grade=" + grade + ", hp=" + hp + ", mp=" + mp + ", hpRecoverBaseA=" + hpRecoverBaseA + ", mpRecoverBaseA=" + mpRecoverBaseA + ", maxHp=" + maxHp + ", maxHpA=" + maxHpA + ", maxHpB=" + maxHpB + ", maxHpC=" + maxHpC + ", maxMp=" + maxMp + ", maxMpA=" + maxMpA + ", maxMpB=" + maxMpB + ", maxMpC=" + maxMpC + ", hitA=" + hitA + ", hitB=" + hitB + ", hitC=" + hitC + ", hitX=" + hitX + ", attackRatePercent=" + attackRatePercent + ", attackRatePercentA=" + attackRatePercentA + ", attackRatePercentB=" + attackRatePercentB + ", attackRatePercentC=" + attackRatePercentC + ", attackRatePercentX=" + attackRatePercentX + ", dodge=" + dodge + ", dodgeA=" + dodgeA + ", dodgeB=" + dodgeB + ", dodgeC=" + dodgeC + ", dodgeX=" + dodgeX + ", dodgePercent=" + dodgePercent + ", dodgePercentA=" + dodgePercentA + ", dodgePercentB=" + dodgePercentB + ", dodgePercentC=" + dodgePercentC + ", dodgePercentX=" + dodgePercentX + ", accurate=" + accurate + ", accurateA=" + accurateA + ", accurateB=" + accurateB + ", accurateC=" + accurateC + ", accurateX=" + accurateX + ", phyAttack=" + phyAttack + ", phyAttackA=" + phyAttackA + ", phyAttackB=" + phyAttackB + ", phyAttackC=" + phyAttackC + ", phyAttackX=" + phyAttackX + ", magicAttack=" + magicAttack + ", magicAttackA=" + magicAttackA + ", magicAttackB=" + magicAttackB + ", magicAttackC=" + magicAttackC + ", magicAttackX=" + magicAttackX + ", criticalHit=" + criticalHit + ", criticalHitA=" + criticalHitA + ", criticalHitB=" + criticalHitB + ", criticalHitC=" + criticalHitC + ", criticalHitX=" + criticalHitX + ", criticalDefence=" + criticalDefence + ", criticalDefenceA=" + criticalDefenceA + ", criticalDefenceB=" + criticalDefenceB + ", criticalDefenceC=" + criticalDefenceC + ", criticalDefenceX=" + criticalDefenceX + ", criticalHitPercent=" + criticalHitPercent + ", criticalHitPercentA=" + criticalHitPercentA + ", criticalHitPercentB=" + criticalHitPercentB + ", criticalHitPercentC=" + criticalHitPercentC + ", criticalHitPercentX=" + criticalHitPercentX + ", phyDefence=" + phyDefence + ", phyDefenceA=" + phyDefenceA + ", phyDefenceB=" + phyDefenceB + ", phyDefenceC=" + phyDefenceC + ", magicDefence=" + magicDefence + ", magicDefenceA=" + magicDefenceA + ", magicDefenceB=" + magicDefenceB + ", magicDefenceC=" + magicDefenceC + ", breakDefence=" + breakDefence + ", breakDefenceA=" + breakDefenceA + ", breakDefenceB=" + breakDefenceB + ", breakDefenceC=" + breakDefenceC + ", fireAttack=" + fireAttack + ", fireAttackA=" + fireAttackA + ", fireAttackB=" + fireAttackB + ", fireAttackC=" + fireAttackC + ", fireAttackX=" + fireAttackX + ", fireDefence=" + fireDefence + ", fireDefenceA=" + fireDefenceA + ", fireDefenceB=" + fireDefenceB + ", fireDefenceC=" + fireDefenceC + ", fireDefenceX=" + fireDefenceX + ", fireIgnoreDefence=" + fireIgnoreDefence + ", fireIgnoreDefenceA=" + fireIgnoreDefenceA + ", fireIgnoreDefenceB=" + fireIgnoreDefenceB + ", fireIgnoreDefenceC=" + fireIgnoreDefenceC + ", fireIgnoreDefenceX=" + fireIgnoreDefenceX + ", blizzardAttack=" + blizzardAttack + ", blizzardAttackA=" + blizzardAttackA + ", blizzardAttackB=" + blizzardAttackB + ", blizzardAttackC=" + blizzardAttackC + ", blizzardAttackX=" + blizzardAttackX + ", blizzardDefence=" + blizzardDefence + ", blizzardDefenceA=" + blizzardDefenceA + ", blizzardDefenceB=" + blizzardDefenceB + ", blizzardDefenceC=" + blizzardDefenceC + ", blizzardDefenceX=" + blizzardDefenceX + ", blizzardIgnoreDefence=" + blizzardIgnoreDefence + ", blizzardIgnoreDefenceA=" + blizzardIgnoreDefenceA + ", blizzardIgnoreDefenceB=" + blizzardIgnoreDefenceB + ", blizzardIgnoreDefenceC=" + blizzardIgnoreDefenceC + ", blizzardIgnoreDefenceX=" + blizzardIgnoreDefenceX + ", windAttack=" + windAttack + ", windAttackA=" + windAttackA + ", windAttackB=" + windAttackB + ", windAttackC=" + windAttackC + ", windAttackX=" + windAttackX + ", windDefence=" + windDefence + ", windDefenceA=" + windDefenceA + ", windDefenceB=" + windDefenceB + ", windDefenceC=" + windDefenceC + ", windDefenceX=" + windDefenceX + ", windIgnoreDefence=" + windIgnoreDefence + ", windIgnoreDefenceA=" + windIgnoreDefenceA + ", windIgnoreDefenceB=" + windIgnoreDefenceB + ", windIgnoreDefenceC=" + windIgnoreDefenceC + ", windIgnoreDefenceX=" + windIgnoreDefenceX + ", thunderAttack=" + thunderAttack + ", thunderAttackA=" + thunderAttackA + ", thunderAttackB=" + thunderAttackB + ", thunderAttackC=" + thunderAttackC + ", thunderAttackX=" + thunderAttackX + ", thunderDefence=" + thunderDefence + ", thunderDefenceA=" + thunderDefenceA + ", thunderDefenceB=" + thunderDefenceB + ", thunderDefenceC=" + thunderDefenceC + ", thunderDefenceX=" + thunderDefenceX + ", thunderIgnoreDefence=" + thunderIgnoreDefence + ", thunderIgnoreDefenceA=" + thunderIgnoreDefenceA + ", thunderIgnoreDefenceB=" + thunderIgnoreDefenceB + ", thunderIgnoreDefenceC=" + thunderIgnoreDefenceC + ", thunderIgnoreDefenceX=" + thunderIgnoreDefenceX + "]";
	}

}
