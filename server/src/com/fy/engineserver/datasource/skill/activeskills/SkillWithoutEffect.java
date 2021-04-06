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

public class SkillWithoutEffect extends ActiveSkill {

	// static Logger logger = Logger.getLogger(SkillWithoutEffect.class);
	public static Logger logger = LoggerFactory.getLogger(ActiveSkill.class);

	/**
	 * 普通攻击的有效距离
	 */
	private int range;

	public short[] getMp() {
		return new short[20];
	}

	public int check(Fighter caster, Fighter target, int level) {
		int result = 0;
		if (target != null) {
			int dx = caster.getX() - target.getX();
			int dy = caster.getY() - target.getY();
			if (dx * dx + dy * dy > range * range) {
				result |= TARGET_TOO_FAR;
			}
		} else {
			result |= TARGET_NOT_EXIST;
		}

		if (this.getEnableWeaponType() == 1 && caster instanceof Player) {
			Player p = (Player) caster;
			if (p.getWeaponType() != this.getWeaponTypeLimit()) {
				result |= WEAPON_NOT_MATCH;
			}
		}

		if (target != null && caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY) {
			result |= TARGET_NOT_MATCH;
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
			// logger.debug("[技能检查] [SkillWithoutEffect] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target != null?target.getName():"-")+"] ["+
			// Skill.getSkillFailReason(result) +"]");
			if (logger.isDebugEnabled()) logger.debug("[技能检查] [SkillWithoutEffect] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[] { this.getName(), level, caster.getName(), (target != null ? target.getName() : "-"), Skill.getSkillFailReason(result) });
		}

		return result;
	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game, int level, int effectIndex, int x, int y, byte direction) {
		if (ase.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY) {
			hit(ase.getOwner(), target, level, effectIndex);
			if (logger.isDebugEnabled()) {
				// logger.debug("[执行技能攻击] [SkillWithoutEffect] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [直接调用命中计算]");
				if (logger.isDebugEnabled()) logger.debug("[执行技能攻击] [SkillWithoutEffect] [{}] [Lv:{}] [{}] [{}] [直接调用命中计算]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
			}

		} else {

			// logger.debug("[技能攻击失败] [SkillWithoutEffect] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [目标不是敌方]");
			if (logger.isWarnEnabled()) logger.warn("[技能攻击失败] [SkillWithoutEffect] [{}] [Lv:{}] [{}] [{}] [目标不是敌方]", new Object[] { this.getName(), level, ase.getOwner().getName(), target.getName() });
		}
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}

}
