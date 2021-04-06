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
 * 普通攻击，每种普通攻击只适用一种武器，所以普通攻击必须设置武器限制
 * 
 * 
 *
 */
public class CommonAttackSkill extends ActiveSkill{

//	static Logger logger = Logger.getLogger(CommonAttackSkill.class);
public	static Logger logger = LoggerFactory.getLogger(CommonAttackSkill.class);
	
	/**
	 * 后效的类型，比如闪电，落雷等
	 */
	String effectType = "";
	
	String avataRace = "";
	
	String avataSex = "";
	
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

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public CommonAttackSkill(){
		this.attackType = 0;
		this.buffName = "";
		this.buffLastingTime = new long[0];
		this.buffLevel = new int[0];
		this.maxLevel = 1;
		this.buffProbability = new int[0];
		this.effectiveTimes = new int[]{100};
		this.attackDamages = new int[0];
		this.enableWeaponType = 1;
	}
	
	public int check(Fighter caster, Fighter target, int level) {
		int result = 0;
		if(target != null){
			float dx = caster.getX() - target.getX();
			float dy = caster.getY() - target.getY();
			
			if (dx * dx + dy * dy > range * range) {
				result |= TARGET_TOO_FAR;
			}
		}else{
			result |= TARGET_NOT_EXIST;
		}
		
		if(target != null && caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY){
			result |= TARGET_NOT_MATCH;
		}
		
		if(target != null && target.isDeath()){
			result |= TARGET_NOT_MATCH;
		}
		
		if(this.getEnableWeaponType() == 1 && caster instanceof Player){
			Player p = (Player)caster;
			if( p.getWeaponType() != this.getWeaponTypeLimit()){
				result |= WEAPON_NOT_MATCH;
			}
		}
//		try {
//			if (caster instanceof Player && ((Player)caster).getCareer() == 5) {
//				if (this.isBianshenSkill() && !((Player)caster).isShouStatus()) {
//					result |= STATUS_NOT_ENOUGH;
//				} else if (!this.isBianshenSkill() && ((Player)caster).isShouStatus()) {
//					result |= STATUS_NOT_ENOUGH;
//				}
//			}
//		} catch (Exception e) {
//			logger.warn("[检测兽魁使用技能状态] [异常]", e);
//		}
		
		if(logger.isDebugEnabled()){
//			logger.debug("[技能检查] [CommonAttackSkill] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target != null?target.getName():"-")+"] ["+ Skill.getSkillFailReason(result) +"]");
			if(logger.isDebugEnabled())
				logger.debug("[技能检查] [CommonAttackSkill] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		
		return result;
	}

	
	public short[] getMp() {
		return new short[0];
	}

	
	public void run(ActiveSkillEntity skillEntity, Fighter target, Game game,
			int level, int effectIndex, int targetX, int targetY, byte direction) {
		if(target != null){
			if(skillEntity.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
				PositionEffectSummoned s = new PositionEffectSummoned(skillEntity,0,target,target.getX(),target.getY(),effectType,avataRace,avataSex,
						effectLastTime,effectExplosionLastTime);
				game.addSummoned(s);
				if(logger.isDebugEnabled()){
					logger.debug("[执行技能攻击] [CommonAttackSkill] [{}] [Lv:{}] [{}] [{}] [释放位置后效]", new Object[]{this.getName(),level,skillEntity.getOwner().getName(),target.getName()});
				}
				
			}else{
				if(logger.isWarnEnabled())
					logger.warn("[技能攻击失败] [CommonAttackSkill] [{}] [Lv:{}] [{}] [{}] [目标不能被攻击]", new Object[]{this.getName(),level,skillEntity.getOwner().getName(),target.getName()});
			}
		}else{
			if(logger.isWarnEnabled())
				logger.warn("[技能攻击失败] [CommonAttackSkill] [{}] [Lv:{}] [{}] [-] [目标不存在]", new Object[]{this.getName(),level,skillEntity.getOwner().getName()});
		}
	}

	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
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

}
