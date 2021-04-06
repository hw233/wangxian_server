package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.effect.PositionEffectSummoned;

/**
 * 无轨迹有目标的攻击技能种类
 * 
 * 
 * 
 * 
 */
public class SkillWithoutTraceAndWithTargetOrPosition extends ActiveSkill implements Cloneable{

	// static Logger logger = Logger.getLogger(SkillWithoutTraceAndWithTargetOrPosition.class);
	public static Logger logger = LoggerFactory.getLogger(SkillWithoutTraceAndWithTargetOrPosition.class);

	/**
	 * 后效的类型，比如闪电，落雷等
	 */
	String effectType = "";

	String avataRace = "";

	String avataSex = "";

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

	/**
	 * 攻击的有效距离
	 */
	private int range;

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

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
		}

		if (this.getEnableWeaponType() == 1 && caster instanceof Player) {
			Player p = (Player) caster;
			if (p.getWeaponType() != this.getWeaponTypeLimit()) {
				result |= WEAPON_NOT_MATCH;
			}
		}

		if (target != null) {
			if (caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
				result |= TARGET_NOT_MATCH;
			}
			int dx = caster.getX() - target.getX();
			int dy = caster.getY() - target.getY();
			if (dx * dx + dy * dy > range * range) {
				result |= TARGET_TOO_FAR;
			}
		}

		if (target != null && target.isDeath()) {
			result |= TARGET_NOT_MATCH;
		}
		
		try {
			if (caster instanceof Player && ((Player)caster).getCareer() == 5) {
				if (this.isBianshenSkill() && !((Player)caster).isShouStatus()) {
					result |= STATUS_NOT_ENOUGH;
				} else if (!this.isBianshenSkill() && ((Player)caster).isShouStatus()) {
					result |= STATUS_NOT_ENOUGH;
				}
			}
		} catch (Exception e) {
			logger.warn("[检测兽魁使用技能状态] [异常]", e);
		}
		if (logger.isDebugEnabled()) {
			// logger.debug("[技能检查] [SkillWithoutTraceAndWithTargetOrPosition] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target !=
			// null?target.getName():"-")+"] ["+ Skill.getSkillFailReason(result) +"]");
			if (logger.isDebugEnabled()) logger.debug("[技能检查] [SkillWithoutTraceAndWithTargetOrPosition] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[] { this.getName(), level, caster.getName(), (target != null ? target.getName() : "-"), Skill.getSkillFailReason(result) });
		}
		return result;
	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game, int level, int effectIndex, int x, int y, byte direction) {
		if (target != null) {
			if (ase.getOwner().getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
				// logger.warn("[技能攻击失败] [SkillWithoutTraceAndWithTargetOrPosition] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [目标不能被攻击]");
				if (logger.isWarnEnabled()) logger.warn("[技能攻击失败] [SkillWithoutTraceAndWithTargetOrPosition] [{}] [Lv:{}] [{}] [{}] [目标不能被攻击]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
				return;
			}
		}
		PositionEffectSummoned s = new PositionEffectSummoned(ase, effectIndex, target, x, y, effectType, avataRace, avataSex, effectLastTime, effectExplosionLastTime);
		game.addSummoned(s);

		if (target != null) {
			ase.getOwner().notifyPrepareToFighting(target);
			target.notifyPrepareToBeFighted(ase.getOwner());
		}

		if (logger.isDebugEnabled()) {
			// logger.debug("[执行技能攻击] [CommonAttackSkill] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+(target !=
			// null?target.getName():"无目标")+"] [释放位置后效]");
			if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [CommonAttackSkill] [{}] [Lv:{}] [{}] [{}] [释放位置后效]", new Object[] { this.getName(), level, ase.getOwner().getName(), (target != null ? target.getName() : "无目标") });
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

	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}

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

	
	public SkillWithoutTraceAndWithTargetOrPosition clone()  {
		try {
			return (SkillWithoutTraceAndWithTargetOrPosition) super.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("克隆出错:",e);
			return null;
		}
	}	
}
