package com.fy.engineserver.datasource.skill.activeskills;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

public class ResurrectionSkill extends ActiveSkill{

	/**
	 * 后效的类型，比如闪电，落雷等
	 */
	byte effectType = 0;
	
	/**
	 * 后效持续的时间，为毫秒
	 */
	int effectLastTime = 0;
	
	/**
	 * 后效持续的时间过后，爆炸持续的时间
	 */
	int effectExplosionLastTime = 100;
	
	/**
	 * 攻击范围
	 */
	int range;
	
	/**
	 * 各个等级消耗的蓝
	 */
	short mp[];
	
	
	/**
	 * 恢复生命值的比率
	 */
	short hpRate[];
	
	/**
	 * 恢复蓝的比率
	 */
	short mpRate[];
	
	
	
	public byte getEffectType() {
		return effectType;
	}

	public void setEffectType(byte effectType) {
		this.effectType = effectType;
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

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public short[] getHpRate() {
		return hpRate;
	}

	public void setHpRate(short[] hpRate) {
		this.hpRate = hpRate;
	}

	public short[] getMpRate() {
		return mpRate;
	}

	public void setMpRate(short[] mpRate) {
		this.mpRate = mpRate;
	}

	public void setMp(short[] mp) {
		this.mp = mp;
	}

	
	public ResurrectionSkill(){
		this.attackType = 1;
		this.buffName = "";
		this.buffLastingTime = new long[0];
		this.buffLevel = new int[0];
		this.buffProbability = new int[0];
		this.effectiveTimes = new int[]{100};
		this.attackDamages = new int[0];
		this.enableWeaponType = 0;
	}
	
	public int check(Fighter caster, Fighter target, int level) {
		int result = 0;
		if(target != null){
			int dx = caster.getX() - target.getX();
			int dy = caster.getY() - target.getY();
			
			if (dx * dx + dy * dy > range * range) {
				result |= TARGET_TOO_FAR;
			}
		}else{
			result |= TARGET_NOT_EXIST;
		}
		
		if(target != null && caster.getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
			result |= TARGET_NOT_MATCH;
		}else if(target != null && target.isDeath() == false){
			result |= TARGET_NOT_MATCH;
		}
		
		if(caster instanceof Player){
			Player p = (Player)caster;
			if(p.isFighting()){
				result |= FIGHTING_NOT_MATCH;
			}
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
		
		return result;
	}

	public short[] getMp() {
		
		return mp;
	}

	public void run(ActiveSkillEntity skillEntity, Fighter target, Game game,
			int level, int effectIndex, int targetX, int targetY, byte direction) {
		
		
	}

}
