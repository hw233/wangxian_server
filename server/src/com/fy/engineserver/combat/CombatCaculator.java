package com.fy.engineserver.combat;

import java.util.Random;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.util.ProbabilityUtils;

public class CombatCaculator {

	/**
	 * 成长函数
	 * @param level
	 * @return
	 */
	public static int F(int level) {
//		if (level > 220 && PlayerManager.combatFNums != null && PlayerManager.combatFNums.containsKey(level)) {
//			return PlayerManager.combatFNums.get(level);
//		}
//		if (level >= 300) {
//			return level * 10 + 74490;
//		} else 
		if (level >= 281) {
			return 281 * 10 + 74490;
		} else if (level >= 261) {
			return level * 10 + 48860;
		} else if (level >= 241) {
			return level * 10 + 31840;
		} else if (level >= 221) {
			return level * 10 + 20560;
		} else if (level >= 201) {
			return level * 10 + 13110;
		} else if (level >= 181) {
			return level * 10 + 8210;
		} else if (level >= 161) {
			return level * 10 + 5010;
		} else if (level >= 141) {
			return level * 10 + 2940;
		} else if (level >= 121) {
			return level * 10 + 1630;
		} else if (level >= 101) {
			return level * 10 + 820;
		} else if (level >= 81) {
			return level * 10 + 350;
		} else if (level >= 61) {
			return level * 10 + 100;
		} else if (level >= 41) {
			return level * 10;
		} else if (level >= 21) {
			return level * 10;
		} else if (level <= 20) {
			return level * 10;
		}
		return level + 40;
	}

	public static Random random = new Random(System.currentTimeMillis());

	/**
	 * 是否闪避
	 * @param attacker
	 * @param attackerLevel
	 * @return
	 */
	public static boolean isDodge(Fighter attacker, int attackerCareer, Fighter defender, int defenderCareer) {
		int attackerLevel = attacker.getLevel();
		int defenderLevel = defender.getLevel();
		int defenderDodgeRate = 0;
		if (defender instanceof Monster) {
			defenderDodgeRate = CAL_闪避率_怪(defender.getDodge(), defenderLevel, defenderCareer);
		} else {
			defenderDodgeRate = CAL_闪避率(defender.getDodge(), defenderLevel, defenderCareer);
		}
		int attackerAccurateRate = CAL_精准率(attacker.getAccurate(), attackerLevel);
		defenderDodgeRate += defender.getDodgeRateOther();
		attackerAccurateRate += attacker.getAccurateRateOther();
		int n = defenderDodgeRate - attackerAccurateRate;
		if (n <= 0) {
			return false;
		}
		int ranNum = 0;
		if (attacker instanceof LivingObject) {
			ranNum = ProbabilityUtils.random(((LivingObject) attacker).random, 0, 1000);
		} else {
			ranNum = ProbabilityUtils.random(random, 0, 1000);
		}
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[攻击者:" + attacker.getId() + "," + attacker.getName() + "] [精准:" + attacker.getAccurate() + "-" + attacker.getAccurateRateOther() + "-" + attackerAccurateRate + "]"
					+ "[被攻击者:" + defender.getId() + "," + defender.getName() + "] [闪避:" + defender.getDodge() + "-" + defender.getDodgeRateOther() + "-" + defenderDodgeRate + "] [n:" + n + "] [ranNum:" + ranNum + "]");
		}
		if (ranNum <= n) {
			return true;
		}
		return false;
	}

	/**
	 * 是否命中
	 * @param attacker
	 * @param attackerLevel
	 * @return
	 */
	public static boolean isHit(Fighter attacker) {
		int hitRate = CAL_命中率(attacker.getHit(), attacker.getLevel());
		hitRate += attacker.getHitRateOther();
		int ranNum = 0;
		Random rr = null;
		if (attacker instanceof LivingObject) {
			rr = ((LivingObject) attacker).random;
			ranNum = ProbabilityUtils.random(((LivingObject) attacker).random, 0, 1000);
		} else {
			rr = random;
			ranNum = ProbabilityUtils.random(random, 0, 1000);
		}
		if (ranNum <= hitRate) {
			return true;
		}
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[攻击者:" + attacker.getId() + "] [ranNum:" + ranNum + "] [hitRate:" + hitRate + "] [attacker.getHitRateOther():" + attacker.getHitRateOther() + "] [random:" + rr.hashCode() + "] [classRandom:" + random.hashCode() + "]");
		}
		return false;
	}

	/**
	 * 是否暴击
	 * @param attacker
	 * @param attackerLevel
	 * @param defender
	 * @param defenderLevel
	 * @return
	 */
	public static boolean isCriticalHit(Fighter attacker, Fighter defender) {
		int defenderCriticalDefenceRate = CAL_会心防御率(defender.getCriticalDefence(), defender.getLevel());
		int attackerCriticalHitRate = CAL_会心一击率(attacker.getCriticalHit(), attacker.getLevel());
		defenderCriticalDefenceRate += defender.getCriticalDefenceRateOther();
		attackerCriticalHitRate += attacker.getCriticalHitRateOther();
		int n = attackerCriticalHitRate - defenderCriticalDefenceRate;
		if (n <= 0) {
			return false;
		}
		int ranNum = 0;
		if (attacker instanceof LivingObject) {
			ranNum = ProbabilityUtils.random(((LivingObject) attacker).random, 0, 1000);
		} else {
			ranNum = ProbabilityUtils.random(random, 0, 1000);
		}
		if (ranNum <= n) {
			return true;
		}
		return false;
	}

	/**
	 * 计算战斗伤害
	 * @param attacker
	 *            攻击者
	 * @param defender
	 *            防御者
	 * @return
	 */
	public static int caculateDamage(Fighter attacker, int attackerCareer, Fighter defender, int defenderCareer, int skillAttackTime, boolean isPhyAttack, int skillDamage) {

		int damage = 0;

		
		int attackerLevel = attacker.getLevel();
		int defenderLevel = defender.getLevel();

		int attackerFireDPS = CAL_火属性DPS(attacker.getFireAttack(), attackerLevel);
		int attackerBlizzardDPS = CAL_冰属性DPS(attacker.getBlizzardAttack(), attackerLevel);
		int attackerWindDPS = CAL_风属性DPS(attacker.getWindAttack(), attackerLevel);
		int attackerThunderDPS = CAL_雷属性DPS(attacker.getThunderAttack(), attackerLevel);

		// 攻击方的破防，减抗率
		int attackerFireIgnoreDefenceRate = CAL_火属性减抗率(attacker.getFireIgnoreDefence(), attackerLevel);
		attackerFireIgnoreDefenceRate += attacker.getFireIgnoreDefenceRateOther();

		int attackerBlizzardIgnoreDefenceRate = CAL_冰属性减抗率(attacker.getBlizzardIgnoreDefence(), attackerLevel);
		attackerBlizzardIgnoreDefenceRate += attacker.getBlizzardIgnoreDefenceRateOther();

		int attackerWindIgnoreDefenceRate = CAL_风属性减抗率(attacker.getWindIgnoreDefence(), attackerLevel);
		attackerWindIgnoreDefenceRate += attacker.getWindIgnoreDefenceRateOther();

		int attackerThunderIgnoreDefenceRate = CAL_雷属性减抗率(attacker.getThunderIgnoreDefence(), attackerLevel);
		attackerThunderIgnoreDefenceRate += attacker.getThunderIgnoreDefenceRateOther();

		// 防御方的减伤，抗性
		int defenderFireDefenceRate = CAL_火属性抗率(defender.getFireDefence(), defenderLevel, defenderCareer);
		defenderFireDefenceRate += defender.getFireDefenceRateOther();

		int defenderBlizzardDefenceRate = CAL_冰属性抗率(defender.getBlizzardDefence(), defenderLevel, defenderCareer);
		defenderBlizzardDefenceRate += defender.getBlizzardDefenceRateOther();

		int defenderWindDefenceRate = CAL_风属性抗率(defender.getWindDefence(), defenderLevel, defenderCareer);
		defenderWindDefenceRate += defender.getWindDefenceRateOther();

		int defenderThunderDefenceRate = CAL_雷属性抗率(defender.getThunderDefence(), defenderLevel, defenderCareer);
		defenderThunderDefenceRate += defender.getThunderDefenceRateOther();

		if (attackerFireIgnoreDefenceRate < defenderFireDefenceRate) {
			attackerFireDPS = (int) (1L * attackerFireDPS * (1000 - defenderFireDefenceRate + attackerFireIgnoreDefenceRate) / 1000);
		}

		if (attackerBlizzardIgnoreDefenceRate < defenderBlizzardDefenceRate) {
			attackerBlizzardDPS = (int) (1L * attackerBlizzardDPS * (1000 - defenderBlizzardDefenceRate + attackerBlizzardIgnoreDefenceRate) / 1000);
		}

		if (attackerWindIgnoreDefenceRate < defenderWindDefenceRate) {
			attackerWindDPS = (int) (1L * attackerWindDPS * (1000 - defenderWindDefenceRate + attackerWindIgnoreDefenceRate) / 1000);
		}

		if (attackerThunderIgnoreDefenceRate < defenderThunderDefenceRate) {
			attackerThunderDPS = (int) (1L * attackerThunderDPS * (1000 - defenderThunderDefenceRate + attackerThunderIgnoreDefenceRate) / 1000);
		}

		if (isPhyAttack) {
			int attackerPhyDPS = CAL_外功DPS(attacker.getPhyAttack(), attackerLevel);

			int attackerBreakDefenceRate = CAL_破防率(attacker.getBreakDefence(), attackerLevel);
			attackerBreakDefenceRate += attacker.getBreakDefenceRateOther();

			int defenderPhyDefenceRate = CAL_物理减伤率(defender.getPhyDefence(), defenderLevel, defenderCareer);
			defenderPhyDefenceRate += defender.getPhyDefenceRateOther();
			if (defenderPhyDefenceRate < -1000) {
				defenderPhyDefenceRate = -1000;
			}
			if (defenderPhyDefenceRate > 1000) {		//减伤率不能超过百分之百
				defenderPhyDefenceRate = 1000;
			}

			if (attackerBreakDefenceRate < defenderPhyDefenceRate) {
				skillDamage = (int) (skillDamage * 1L * (1000 - defenderPhyDefenceRate + attackerBreakDefenceRate) / 1000);
			}

			if (attackerBreakDefenceRate < defenderPhyDefenceRate) {
				attackerPhyDPS = (int) (attackerPhyDPS * 1L * (1000 - defenderPhyDefenceRate + attackerBreakDefenceRate) / 1000);
			}

			damage = (int) ((attackerPhyDPS + attackerFireDPS + attackerBlizzardDPS + attackerWindDPS + attackerThunderDPS) * 1L * skillAttackTime / 1000) + (int) (skillDamage * 1L * (1000 - defenderPhyDefenceRate + attackerBreakDefenceRate) / 1000);
		} else {
			int attackerMagicDPS = CAL_内法DPS(attacker.getMagicAttack(), attackerLevel);

			int attackerBreakDefenceRate = CAL_破防率(attacker.getBreakDefence(), attackerLevel);
			attackerBreakDefenceRate += attacker.getBreakDefenceRateOther();

			int defenderMagicDefenceRate = CAL_法术减伤率(defender.getMagicDefence(), defenderLevel, defenderCareer);
			defenderMagicDefenceRate += defender.getMagicDefenceRateOther();
			if (defenderMagicDefenceRate < -1000) {
				defenderMagicDefenceRate = -1000;
			}
			if (defenderMagicDefenceRate > 1000) {		//减伤率不能超过百分之百
				defenderMagicDefenceRate = 1000;
			}

			if (attackerBreakDefenceRate < defenderMagicDefenceRate) {
				float f = (1000 - defenderMagicDefenceRate + attackerBreakDefenceRate) / 1000f;
				skillDamage = (int) (skillDamage * 1L * f);
			}

			if (attackerBreakDefenceRate < defenderMagicDefenceRate) {
				float f = (1000 - defenderMagicDefenceRate + attackerBreakDefenceRate) / 1000f;
				attackerMagicDPS = (int) (attackerMagicDPS * 1L * f);
			}
			float tempF = skillAttackTime / 1000f;
			float tempF2 = (1000 - defenderMagicDefenceRate + attackerBreakDefenceRate) / 1000f;
			damage = (int) ((attackerMagicDPS + attackerFireDPS + attackerBlizzardDPS + attackerWindDPS + attackerThunderDPS) * 1L * tempF) + (int) (skillDamage * 1L * tempF2);

			if (damage >= MagicWeaponConstant.伤害数值) {		//伤害超过一定值打出伤害日志
				if (TransitRobberyManager.logger.isInfoEnabled()) {
					TransitRobberyManager.logger.info("[伤害值超高] [damage:" + damage + "] [attackerMagicDPS:" + attackerMagicDPS + "] [attackerFireDPS:" + attackerFireDPS + "] [attackerBlizzardDPS:" 
							+ attackerBlizzardDPS + "] [attackerWindDPS:" + attackerWindDPS + "] [attackerThunderDPS:" + attackerThunderDPS + "] [tempF:" + tempF + "] [skillDamage:" + skillDamage + "] [tempF2:" + tempF2 
							+ "[攻击者:" + attacker.getId() + "," + attacker.getName() + "] [被攻击者:" + defender.getId() + "," + defender.getName() + "]");
				}
			}
		}
		//防止伤害过高变负
		damage = (int) (damage * ((100 + ProbabilityUtils.random(random, -5, 5)) / 100d));
//		damage = damage * (100 + ProbabilityUtils.random(random, -5, 5)) / 100;
		if (Skill.logger.isDebugEnabled()) {
			Skill.logger.debug("[caculateDamage] [攻击者:" + attacker.getName() + "] [被攻击者:" + defender.getName() + "] [技能伤害:" + skillDamage + "] [结算技能:" + damage + "]");
		}
		return damage;

	}

	/**
	 * 计算治疗值
	 * @param doctor
	 *            医生
	 * @param patient
	 *            病人
	 * @return
	 */
	public static int caculateCure(Fighter doctor, int skillCureTime, int skillCure) {

		int cure = 0;

		int doctorMagicDPS = CAL_内法DPS(doctor.getMagicAttack(), doctor.getLevel());

		cure = doctorMagicDPS * skillCureTime / 1000 + skillCure;

		cure = cure * (100 + ProbabilityUtils.random(random, -5, 5)) / 100;

		return cure;

	}

	public static double A_物理减伤 = 55.14705882;
	public static double A_法术减伤 = 55.14705882;
	public static double A_火抗 = 24.19354839;
	public static double A_冰抗 = 24.19354839;
	public static double A_风抗 = 24.19354839;
	public static double A_雷抗 = 24.19354839;
	public static double A_闪避 = 20;
	public static double A_会心防御 = 17.85714286;
	public static double A_命中 = 25;
	public static double A_会心一击 = 20.83333333;
	public static double A_精准 = 26.04166667;
	public static double A_破防 = 12.5;
	public static double A_减火抗 = 3.125;
	public static double A_减冰抗 = 3.125;
	public static double A_减风抗 = 3.125;
	public static double A_减雷抗 = 3.125;
	public static double A_内物理攻击 = 5.5296;
	public static double A_火攻 = 5.5296;
	public static double A_冰攻 = 5.5296;
	public static double A_风攻 = 5.5296;
	public static double A_雷攻 = 5.5296;

	// 通用,修罗,影魅,仙心,九黎,兽魁,蓬莱
	public static int[] X_减伤 = { 170, 170, 170, 170, 170, 170, 170 };
	public static int[] X_火抗 = { 620, 357, 745, 745, 745, 745, 550 };
	public static int[] X_冰抗 = { 620, 695, 745, 382, 745, 745, 550 };
	public static int[] X_风抗 = { 620, 695, 382, 745, 745, 550, 550 };
	public static int[] X_雷抗 = { 620, 695, 745, 745, 382, 550, 550 };
	public static int[] X_闪避 = { 95, 95, 95, 95, 95, 95, 95 };

	public static int 通用 = 0;
	public static int 修罗 = 1;
	public static int 影魅 = 2;
	public static int 仙心 = 3;
	public static int 九黎 = 4;
	public static int 兽魁 = 5;
	public static int 蓬莱 = 6;

	/**
	 * 计算命中率，本类中所有返回的率均为千分率
	 * @param hit
	 * @param level
	 * @return 千分率
	 */
	public static int CAL_命中率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_命中 * F(level))) + 800;
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 计算会心一击率，千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_会心一击率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_会心一击 * F(level))) + 50;
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_会心防御率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_会心防御 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_闪避率(int X, int level, int career) {
		int n = (int) ((1L * X * X_闪避[career] * 10) / (X + A_闪避 * F(level))) + 50;
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_闪避率_怪(int X, int level, int career) {
		int n = (int) ((1L * X * X_闪避[career] * 10) / (X + A_闪避 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_精准率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_精准 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}
	public static int CAL_精准率(int X, int level, byte career) {
		if (career != 1) {
			return CAL_精准率(X, level);
		}
		int n = (int) ((1L * X * 1000) / (A_精准 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_物理减伤率(int X, int level, int career) {
		int n = (int) ((1L * X * X_减伤[career] * 10) / (X + A_物理减伤 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_法术减伤率(int X, int level, int career) {
		int n = (int) ((1L * X * X_减伤[career] * 10) / (X + A_法术减伤 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_破防率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_破防 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_火属性抗率(int X, int level, int career) {
		int n = (int) ((1L * X * X_火抗[career] * 10) / (X + A_火抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_火属性减抗率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_减火抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_冰属性抗率(int X, int level, int career) {
		int n = (int) ((1L * X * X_冰抗[career] * 10) / (X + A_冰抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_冰属性减抗率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_减冰抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_风属性抗率(int X, int level, int career) {
		int n = (int) ((1L * X * X_风抗[career] * 10) / (X + A_风抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_风属性减抗率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_减风抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @param career
	 * @return
	 */
	public static int CAL_雷属性抗率(int X, int level, int career) {
		int n = (int) ((1L * X * X_雷抗[career] * 10) / (X + A_雷抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	/**
	 * 千分率
	 * @param X
	 * @param level
	 * @return
	 */
	public static int CAL_雷属性减抗率(int X, int level) {
		int n = (int) ((1L * X * 1000) / (A_减雷抗 * F(level)));
		if (n > 1000) {
			return 1000;
		}
		return n;
	}

	public static int CAL_外功DPS(int X, int level) {
		int n = (int) (X / A_内物理攻击);
		return n;
	}

	public static int CAL_内法DPS(int X, int level) {
		int n = (int) (X / A_内物理攻击);
		return n;
	}

	public static int CAL_火属性DPS(int X, int level) {
		int n = (int) (X / A_火攻);
		return n;
	}

	public static int CAL_冰属性DPS(int X, int level) {
		int n = (int) (X / A_冰攻);
		return n;
	}

	public static int CAL_风属性DPS(int X, int level) {
		int n = (int) (X / A_风攻);
		return n;
	}

	public static int CAL_雷属性DPS(int X, int level) {
		int n = (int) (X / A_雷攻);
		return n;
	}
}
