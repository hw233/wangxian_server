package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.calculate.NumericalCalculator;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.ActiveSkillParam;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.effect.PositionEffectSummoned;
import com.fy.engineserver.sprite.npc.GuardNPC;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.util.ProbabilityUtils;

/**
 * 作用于团队，中立方，或者敌方的技能，比如用于加血 此技能不计算伤害输入，只种植Buff
 * 
 * 
 * 
 * 
 */
public class SkillWithoutTraceAndOnTeamMember extends ActiveSkill implements Cloneable{

	// static Logger logger = Logger.getLogger(SkillWithoutTraceAndOnTeamMember.class);
	public static Logger logger = LoggerFactory.getLogger(SkillWithoutTraceAndOnTeamMember.class);

	/**
	 * 范围的类型：<br>
	 * 0 作用于选择的目标，目标必须是队友或中立方，目标必须在范围内
	 * 1 作用于选择的目标，目标必须是敌方，目标必须在范围内
	 * 2 作用于自己，无视范围
	 * 3 范围内的所有的队友，包括自己<br>
	 * 4 范围内的所有的队友，中立方，和自己
	 * 5 范围内的所有的中立方
	 * 6 范围内的所有的敌方
	 * 7 范围内的所有的中立方，敌方
	 */
	byte rangeType = 0;

	/**
	 * 以玩家自身为中心的，一个矩形，此参数为矩形的宽度
	 */
	int rangeWidth = 320;

	/**
	 * 以玩家自身为中心的，一个矩形，此参数为矩形的宽度
	 */
	int rangeHeight = 240;

	/**
	 * 后效的类型，比如闪电，落雷等
	 */
	String effectType = "";

	String avataRace = "";

	String avataSex = "";

	public String getAvataRace() {
		return avataRace;
	}

	public void setAvataRace(String avataRace) {
		this.avataRace = avataRace;
	}

	public String getAvataSex() {
		return avataSex;
	}

	public void setAvataSex(String avataSex) {
		this.avataSex = avataSex;
	}

	/**
	 * 后效持续的时间，为毫秒
	 */
	int effectLastTime = 100;

	/**
	 * 后效持续的时间过后，爆炸持续的时间
	 */
	int effectExplosionLastTime = 100;

	/**
	 * 消耗的法力值，跟等级相关
	 */
	private short[] mp = new short[0];

	// ////////////////////////////////////////////////////////////////////////////////////
	//
	// 以下为服务器端使用的数值计算需要的数据
	//
	// ////////////////////////////////////////////////////////////////////////////////////

	public int check(Fighter caster, Fighter target, int level) {
		int result = 0;

		if (caster instanceof Player) {
			Player p = (Player) caster;
			if (nuqiFlag) {
				// if(p.getXp() < p.getTotalXp()){
				// result |= NUQI_NOT_ENOUGH;
				// }
			} else {
				int mp = calculateMp(p, level);
				if (p.getMp() < mp) {
					result |= MP_NOT_ENOUGH;
				}
			}
			if (this.isDouFlag()) {
				int tempDou = this.calculateDou(p, level);
				if (tempDou < 0 && (p.getShoukuiDouNum() + tempDou) < 0) {		//负数为需要消耗豆
					result |= DOU_NOT_ENOUGH;
				}
			}
			if (this.isBianshenBtn() && p.getCareer() == 5 && !p.isShouStatus()) {		//人变兽需要5秒cd
				long now = System.currentTimeMillis();
				if (p.lastShouStatusTime + Constants.变身CD > now) {
					result |= CD_NOW_ENOUGH;
				}
			}
		}
		try {	
			if (!this.isBianshenBtn()) {			//变身按钮不需要判断状态
				if (caster instanceof Player && ((Player)caster).getCareer() == 5) {
					if (this.isBianshenSkill() && !((Player)caster).isShouStatus()) {
						result |= STATUS_NOT_ENOUGH;
					} else if (!this.isBianshenSkill() && ((Player)caster).isShouStatus()) {
						result |= STATUS_NOT_ENOUGH;
					}
				}
			}
		} catch (Exception e) {
			logger.warn("[检测兽魁使用技能状态] [异常]", e);
		}

		if (this.getEnableWeaponType() == 1 && caster instanceof Player) {
			Player p = (Player) caster;
			if (p.getWeaponType() != this.getWeaponTypeLimit()) {
				result |= WEAPON_NOT_MATCH;
			}
		}

		/**
		 * 范围的类型：<br>
		 * 0 作用于选择的目标，目标必须是队友或中立方，目标必须在范围内
		 * 1 作用于选择的目标，目标必须是敌方，目标必须在范围内
		 * 2 作用于自己，无视范围
		 * 3 范围内的所有的队友，包括自己<br>
		 * 4 范围内的所有的队友，中立方，和自己
		 * 5 范围内的所有的中立方
		 * 6 范围内的所有的敌方
		 * 7 范围内的所有的中立方，敌方
		 */
		switch (rangeType) {
		case 0:// 友方或中立方
			if (target != null && target != caster && caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
				int dx = caster.getX() - target.getX();
				int dy = caster.getY() - target.getY();
				if (Math.abs(dx) > this.rangeWidth / 2 || Math.abs(dy) > this.rangeHeight / 2) {
					result |= TARGET_TOO_FAR;
				} else if (target.isDeath()) {
					result |= TARGET_NOT_EXIST;
				}
			}
			break;

		case 1:// 敌方
			if (target == null) {
				result |= TARGET_NOT_EXIST;
			} else if (target.isDeath() || caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
				result |= TARGET_NOT_MATCH;
			} else {
				int dx = caster.getX() - target.getX();
				int dy = caster.getY() - target.getY();
				if (Math.abs(dx) > this.rangeWidth / 2 || Math.abs(dy) > this.rangeHeight / 2) {
					result |= TARGET_TOO_FAR;
				}
			}
			break;
		}
		if (logger.isDebugEnabled()) {
			// logger.debug("[技能检查] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target != null?target.getName():"-")+"] ["+
			// Skill.getSkillFailReason(result) +"]");
			if (logger.isDebugEnabled()) logger.debug("[技能检查] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [{}] [result:{}]", new Object[] { this.getName(), level, caster.getName(), (target != null ? target.getName() : "-"), Skill.getSkillFailReason(result), result });
//			logger.warn(caster.getName() + " 使用技能:" + this.getName(), new Exception());
		}
		return result;
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
		
		boolean addBuff = true;					//兽变人不加buff
		if (this.isBianshenBtn() &&  caster instanceof Player) {
			Player p = (Player) caster;
			addBuff = !p.isShouStatus();
			if (p.getCareer() == 5) {		//变身只对兽魁有用
				p.changeShoukuiStatus(level);
			}
		}

		if (addBuff) {
			fireBuff(caster, target, level, effectIndex);
		}
		if (target != null && caster.getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY && (rangeType == 1 || rangeType == 6)) {
			caster.notifyPrepareToFighting(target);
			target.notifyEndToBeFighted(caster);
		}

		int hp = calDamageOrHpForFighter(caster, level);

		int 暴击倍数 = 2;
		boolean cd = calcCriticalDamage(caster, target, this);
		if (cd) {
			hp = hp * 暴击倍数;
		}

		if (hp > 0) {
			target.enrichHP(caster, hp, cd);
			caster.enrichHPFeedback(target, hp, cd);
		}

	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game, int level, int effectIndex, int x, int y, byte direction) {
		/**
		 * 范围的类型：<br>
		 * 0 作用于选择的目标，目标必须是队友或中立方，目标必须在范围内
		 * 1 作用于选择的目标，目标必须是敌方，目标必须在范围内
		 * 2 作用于自己，无视范围
		 * 3 范围内的所有的队友，包括自己<br>
		 * 4 范围内的所有的队友，中立方，和自己
		 * 5 范围内的所有的中立方
		 * 6 范围内的所有的敌方
		 * 7 范围内的所有的友方，不包括自己
		 */
		if (rangeType == 0) {
			if (target == null) {
				target = ase.getOwner();
			}

			int ft = ase.getOwner().getFightingType(target);
			if (ft == Fighter.FIGHTING_TYPE_ENEMY) {
				target = ase.getOwner();
			}

			if (target instanceof NPC && (target instanceof GuardNPC) == false) {
				target = ase.getOwner();
			}

			boolean inRange = true;
			if (ase.getOwner() != target) {
				int dx = ase.getOwner().getX() - target.getX();
				int dy = ase.getOwner().getY() - target.getY();
				inRange = Math.abs(dx) <= this.rangeWidth / 2 && Math.abs(dy) <= this.rangeHeight / 2;
			}
			if (inRange) {
				PositionEffectSummoned s = new PositionEffectSummoned(ase, effectIndex, target, target.getX(), target.getY(), effectType, avataRace, avataSex, effectLastTime, effectExplosionLastTime);
				game.addSummoned(s);

				if (logger.isDebugEnabled()) {
					// logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] [" + this.getName() + "] [Lv:" + level + "] [" + ase.getOwner().getName() + "] [" +
					// target.getName() + "] [释放位置后效]");
					if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [释放位置后效]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
				}
			} else {

				// logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] [" + this.getName() + "] [Lv:" + level + "] [" + ase.getOwner().getName() + "] [" + target.getName() +
				// "] [目标不在规定的范围内]");
				if (logger.isWarnEnabled()) logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [目标不在规定的范围内]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });

			}
		} else if (rangeType == 1) {
			int ft = ase.getOwner().getFightingType(target);
			if (ft == Fighter.FIGHTING_TYPE_ENEMY) {
				int dx = ase.getOwner().getX() - target.getX();
				int dy = ase.getOwner().getY() - target.getY();
				if (Math.abs(dx) <= this.rangeWidth / 2 && Math.abs(dy) <= this.rangeHeight / 2) {
					PositionEffectSummoned s = new PositionEffectSummoned(ase, effectIndex, target, target.getX(), target.getY(), effectType, avataRace, avataSex, effectLastTime, effectExplosionLastTime);
					game.addSummoned(s);

					if (logger.isDebugEnabled()) {
						// logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [释放位置后效]");
						if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [释放位置后效]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
					}
				} else {
					// logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [目标不在规定的范围内]");
					if (logger.isWarnEnabled()) logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [目标不在规定的范围内]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
				}
			} else {
				// logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [目标不是敌方]");
				if (logger.isWarnEnabled()) logger.warn("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [目标不是敌方]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
			}
		} else if (rangeType == 2) {
			PositionEffectSummoned s = new PositionEffectSummoned(ase, effectIndex, ase.getOwner(), ase.getOwner().getX(), ase.getOwner().getY(), effectType, avataRace, avataSex, effectLastTime, effectExplosionLastTime);
			game.addSummoned(s);

			if (logger.isDebugEnabled()) {
				// logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [----] [释放位置后效，作用于自己]");
				if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [----] [释放位置后效，作用于自己]", new Object[] { this.getName(), level, ase.getOwner().getName() });
			}

		} else {
			Fighter[] los = null;
			if (ase.getOwner() instanceof LivingObject) {
				los = game.getVisbleFighter((LivingObject) ase.getOwner(), true);
			}
			if (los != null) {
				int count = 0;
				for (int i = 0; i < los.length; i++) {
					Fighter t = los[i];

					int dx = ase.getOwner().getX() - t.getX();
					int dy = ase.getOwner().getY() - t.getY();
					if (t.isDeath() == false && Math.abs(dx) <= this.rangeWidth / 2 && Math.abs(dy) <= this.rangeHeight / 2) {
						boolean fire = false;
						int ft = ase.getOwner().getFightingType(t);
						if (rangeType == 3) {

							if (ft == Fighter.FIGHTING_TYPE_FRIEND && ase.getOwner().isSameTeam(t)) fire = true;
							if (ft == Fighter.FIGHTING_TYPE_FRIEND && (t instanceof Player) && ase.getOwner().isInBattleField()) fire = true;

						} else if (rangeType == 4) {
							if (ft == Fighter.FIGHTING_TYPE_FRIEND || ft == Fighter.FIGHTING_TYPE_NEUTRAL) fire = true;
						} else if (rangeType == 5) {
							if (ft == Fighter.FIGHTING_TYPE_NEUTRAL) fire = true;
						} else if (rangeType == 6) {
							if (ft == Fighter.FIGHTING_TYPE_ENEMY) fire = true;
						} else if (rangeType == 7) {
							if (ft == Fighter.FIGHTING_TYPE_FRIEND) fire = true;
							if (ase.getOwner() == t) {
								fire = false;
							}
						}
						if (fire) {
							PositionEffectSummoned s = new PositionEffectSummoned(ase, effectIndex, t, t.getX(), t.getY(), effectType, avataRace, avataSex, effectLastTime, effectExplosionLastTime);
							game.addSummoned(s);

							count++;

							if (logger.isDebugEnabled()) {
								// logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+t.getName()+"] [释放位置后效，作用于范围内的某个目标]");
								if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [{}] [释放位置后效，作用于范围内的某个目标]", new Object[] { this.getName(), level, ase.getOwner().getName(), t.getName() });
							}
						}
					}
				}
				if (count == 0) {
					if (logger.isDebugEnabled()) {
						// logger.debug("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [---] [范围内没有任何可以用于施法后效的目标]");
						if (logger.isDebugEnabled()) logger.debug("[执行技能失败] [SkillWithoutTraceAndOnTeamMember] [{}] [Lv:{}] [{}] [---] [范围内没有任何可以用于施法后效的目标]", new Object[] { this.getName(), level, ase.getOwner().getName() });
					}
				}
			}
		}
	}

	public int getEffectLastTime() {
		return effectLastTime;
	}

	public void setEffectLastTime(int effectLastTime) {
		this.effectLastTime = effectLastTime;
	}

	public int getEffectExplosionLastTime() {
		return effectExplosionLastTime;
	}

	public void setEffectExplosionLastTime(int effectExplosionLastTime) {
		this.effectExplosionLastTime = effectExplosionLastTime;
	}

	public short[] getMp() {
		return mp;
	}

	public void setMp(short[] mp) {
		this.mp = mp;
	}

	public byte getRangeType() {
		return rangeType;
	}

	public void setRangeType(byte rangeType) {
		this.rangeType = rangeType;
	}

	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}

	public int getRangeWidth() {
		return rangeWidth;
	}

	public void setRangeWidth(int rangeWidth) {
		this.rangeWidth = rangeWidth;
	}

	public int getRangeHeight() {
		return rangeHeight;
	}

	public void setRangeHeight(int rangeHeight) {
		this.rangeHeight = rangeHeight;
	}

	public int getRange() {
		return rangeWidth;
	}

	/**
	 * 爆击率 = 暴击值/1000 ，最小5%，最大75%
	 * @param caster
	 * @return
	 */
	public boolean calcCriticalDamage(Fighter caster, Fighter target, ActiveSkill skill) {

		int l = caster.getLevel() - target.getLevel() - NumericalCalculator.暴击率等级差基线;
		int hitrate = 0;
		if (l < 0) {
			hitrate = NumericalCalculator.暴击率等级差数据表[0];
		} else if (l >= NumericalCalculator.暴击率等级差数据表.length) {
			hitrate = NumericalCalculator.暴击率等级差数据表[NumericalCalculator.暴击率等级差数据表.length - 1];
		} else {
			hitrate = NumericalCalculator.暴击率等级差数据表[l];
		}

		// 50个暴击值相当于1%，
		// 100个韧性值相当于1%的减爆

		int r = 0;
		int tough = 0;
		if (caster instanceof Player && target instanceof Player) {
			Player c = (Player) caster;
			Player t = (Player) target;
			r = c.getCriticalHit();
			tough = t.getToughness();
		} else if (caster instanceof Player && target instanceof Sprite) {
			Player c = (Player) caster;
			r = c.getCriticalHit();
		}
		if (caster instanceof Sprite && target instanceof Player) {
			Sprite c = (Sprite) caster;
			Player t = (Player) target;
			r = c.getCriticalHit();
			tough = t.getToughness();
		}
		if (caster instanceof Sprite && target instanceof Sprite) {
			Sprite c = (Sprite) caster;
			r = c.getCriticalHit();
		}

		tough = (int) (tough * 1000 / (4 * caster.getLevel() * caster.getLevel() + 150 * caster.getLevel() + 10000));

		hitrate += r * 1000 / (4 * caster.getLevel() * caster.getLevel() + 150 * caster.getLevel() + 10000) - tough;

		if (hitrate < 0) hitrate = 0;

		if (caster instanceof Player) {
			Player p = (Player) caster;

			hitrate += p.getCriticalHitRateOther();

			ActiveSkillParam param = p.getActiveSkillParam(skill);
			if (param != null) {
				hitrate += param.getBaojiPercent();
			}
		}

		return ProbabilityUtils.randomProbability(random, hitrate / 100.0);
	}
	
	@Override
	public SkillWithoutTraceAndOnTeamMember clone() {
			try {
				return (SkillWithoutTraceAndOnTeamMember) super.clone();
			} catch (CloneNotSupportedException e) {
				logger.error("克隆出错", e);
			}
			return null;
		}
}
