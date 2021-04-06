package com.fy.engineserver.datasource.skill;

import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.skill.activeskills.SkillWithTraceAndDirectionOrTarget;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.effect.LinearEffectSummoned;
import com.fy.engineserver.sprite.effect.TracingEffectSummoned;
/**
 * 废弃的类
 * 
 * @date on create 2016年3月7日 下午1:54:52
 */
@Deprecated
public class DisasterSkill extends SkillWithTraceAndDirectionOrTarget{
	
	public static int DIRECTIONS2[][] = {
		{10,0},
		{23,2},{17,3},{11,3},{11,4},{15,7},{8,5},{7,5},{6,5},{4,4},{4,5},{2,3},{5,8},{1,2},{1,3},{1,4},{1,6},{1,12},
		{0,10},	//第二象限	
		
//		{10,-0},
		{23,-2},{17,-3},{11,-3},{11,-4},{15,-7},{8,-5},{7,-5},{6,-5},{4,-4},{4,-5},{2,-3},{5,-8},{1,-2},{1,-3},{1,-4},{1,-6},{1,-12},
		{0,-10},	//第一象限	
		
		{-10,0},
		{-23,2},{-17,3},{-11,3},{-11,4},{-15,7},{-8,5},{-7,5},{-6,5},{-4,4},{-4,5},{-2,3},{-5,8},{-1,2},{-1,3},{-1,4},{-1,6},{-1,12},
//		{-0,10},	//第四象限	
		
//		{-10,-0},
		{-23,-2},{-17,-3},{-11,-3},{-11,-4},{-15,-7},{-8,-5},{-7,-5},{-6,-5},{-4,-4},{-4,-5},{-2,-3},{-5,-8},{-1,-2},{-1,-3},{-1,-4},{-1,-6},{-1,-12},
//		{0,-10},	//第三象限	
	};

	@Override
	public void hit(Fighter caster, Fighter target, int level, int effectIndex) {
		// TODO Auto-generated method stub
		int damage = this.calDamageOrHpForFighter(caster, level);
		if (target instanceof Player) {
			DisasterManager.getInst().causeDamage((Player) target, damage);
		}
		if (DisasterManager.logger.isDebugEnabled()) {
			DisasterManager.logger.debug("[技能造成伤害] [" + this.getName() + "] [caster:" + caster.getId() + "," + caster.getName() + "] [target:" + target.getId() + "," + target.getName() + "] [damage:" + damage + "]");
		}
	}
	/**
	 * 金猴天灾伤害计算
	 */
	public int calDamageOrHpForFighter(Fighter f, int level) {
		int dmg = this.getAttackDamages()[0];
		return dmg;
	}
	
	public void run(ActiveSkillEntity ase, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {
		int step = 0;
		if(id == 804 && ase.getOwner() instanceof Player){
			//仙心	804	流星	人阶10重:激活流星技能伤害目标+1
			step = SkEnhanceManager.getInst().getSlotStep((Player) ase.getOwner(), pageIndex);
			if(step>0)
			logger.debug("仙心804流星人阶10重:激活流星技能伤害目标 {}",step);
		}
		int penetrateNumLocal = this.getPenetrateNum() + step;
		logger.debug(" penetrateNumLocal {} effectNum{}",penetrateNumLocal,getEffectNum());
		for(int i = 0 ; i < getEffectNum() ; i++){
			int ex = ase.getOwner().getX();
			int ey = ase.getOwner().getY();

//			byte direction = 0;
//			direction = ((LivingObject)ase.getOwner()).getDirection();
			//8方向
			if(direction < 0 || direction > 7){
//				ActiveSkill.logger.warn("[SkillWithTraceAndDirectionOrTarget] [出现错误，客户端发送了一个服务器没有的方向] [direction:"+direction+"] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"]");
				if(ActiveSkill.logger.isWarnEnabled())
					ActiveSkill.logger.warn("[SkillWithTraceAndDirectionOrTarget] [出现错误，客户端发送了一个服务器没有的方向] [direction:{}] [{}] [Lv:{}] [{}]", new Object[]{direction,this.getName(),level,ase.getOwner().getName()});
				return;
			}
			if(direction == LivingObject.RIGHT){
				if(getEffectInitPositionX() != null && i < getEffectInitPositionX().length )
					ex = ex + this.getEffectInitPositionX()[i];
				if(getEffectInitPositionY() != null && i < getEffectInitPositionY().length )
					ey = ey + this.getEffectInitPositionY()[i];
			}else if(direction == LivingObject.DOWN){
				if(getEffectInitPositionY() != null && i < getEffectInitPositionY().length )
					ey = ey + this.getEffectInitPositionX()[i];
				if(getEffectInitPositionX() != null && i < getEffectInitPositionX().length )
					ex = ex - this.getEffectInitPositionY()[i];
			}else if(direction == LivingObject.LEFT){
				if(getEffectInitPositionX() != null && i < getEffectInitPositionX().length )
					ex = ex - this.getEffectInitPositionX()[i];
				if(getEffectInitPositionY() != null && i < getEffectInitPositionY().length )
					ey = ey - this.getEffectInitPositionY()[i];
			}else {
				if(getEffectInitPositionY() != null && i < getEffectInitPositionY().length )
					ey = ey - this.getEffectInitPositionX()[i];
				if(getEffectInitPositionX() != null && i < getEffectInitPositionX().length )
					ex = ex + this.getEffectInitPositionY()[i];
			}
//			ActiveSkill.logger.info("[LinearEffectSummoned] [dx:"+ex+"] [dy:"+ey+"]");
			if(ActiveSkill.logger.isInfoEnabled())
				ActiveSkill.logger.info("[LinearEffectSummoned] [dx:{}] [dy:{}] [技能名字:{}]", new Object[]{ex,ey, ase.getSkill().getName()});
			EffectSummoned s = null;
			try {
				if(getTrackType() == 0){
					if(getTargetType() == 0 && target != null && ase.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
						s = new LinearEffectSummoned(ase,effectIndex,
							ex, ey, getEffectType(),getAvataRace(),getAvataSex(),  target.getX() - ex,
							target.getY() - ey, getSpeed(), getRange()*2, getSpeed()/HEART_BEATS_PER_SECOND,
							getAttackWidth(), getExplosionLastingTime(),
							 penetrateNumLocal,
							target);
						
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [对敌方目标施放直线飞行的后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [{}] [对敌方目标施放直线飞行的后效]", new Object[]{this.getName(),level,ase.getOwner().getName(),target.getName()});
					}else if(getTargetType() == 0){
						//TODO: 这里需要根据玩家的状态来判断倒是是打怪还是打玩家
						s = new LinearEffectSummoned(ase,effectIndex,
								ex, ey, getEffectType(),getAvataRace(),getAvataSex(),  x - ex,
								y - ey, getSpeed(), getRange()*2, getSpeed()/HEART_BEATS_PER_SECOND,
								getAttackWidth(), getExplosionLastingTime(),
								 penetrateNumLocal,
								(Fighter)null);
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [----] [对固定坐标点施放直线飞行的后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [----] [对固定坐标点施放直线飞行的后效]", new Object[]{this.getName(),level,ase.getOwner().getName()});
					}else if(getTargetType() == 1 && getEffectInitDirection() != null && i < getEffectInitDirection().length
							&& getEffectInitDirection()[i] >= 0 && getEffectInitDirection()[i] < DIRECTIONS2.length){
						int k = getEffectInitDirection()[i];
						if(direction == LivingObject.RIGHT){
							k += 18;
						}else if(direction == LivingObject.DOWN){
							k += 36;
						}else if(direction == LivingObject.LEFT){
							k += 54;
						}
						k = k % DIRECTIONS2.length;
						
						s = new LinearEffectSummoned(ase,effectIndex,
								ex, ey, getEffectType(),getAvataRace(),getAvataSex(),  DIRECTIONS2[k][0],
								DIRECTIONS2[k][1], getSpeed(), getRange()*2, getSpeed()/HEART_BEATS_PER_SECOND,
								getAttackWidth(), getExplosionLastingTime(),
								penetrateNumLocal,
								(Fighter)null);
						
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [----] [对固定方向施放直线飞行的后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [----] [对固定方向施放直线飞行的后效]", new Object[]{this.getName(),level,ase.getOwner().getName()});
					}else{
//							logger.debug("[执行技能失败] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [----] [不满足任何一种释放后效的情况]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能失败] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [----] [不满足任何一种释放后效的情况]", new Object[]{this.getName(),level,ase.getOwner().getName()});
					}
				}else{
					if(target != null && ase.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
						s = new TracingEffectSummoned(ase,effectIndex,
							ex, ey, getEffectType(), getAvataRace(),getAvataSex(),
							getSpeed(), getRange()*2, getSpeed() /HEART_BEATS_PER_SECOND,
							getAttackWidth(), getExplosionLastingTime(), target);
						
						ase.getOwner().notifyPrepareToFighting(target);
						target.notifyPrepareToBeFighted(ase.getOwner());
						
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [对目标释放追踪的飞行后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [{}] [对目标释放追踪的飞行后效]", new Object[]{this.getName(),level,ase.getOwner().getName(),target.getName()});
					}else{
//							logger.debug("[执行技能失败] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [不满足对目标释放追踪的飞行后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能失败] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [{}] [不满足对目标释放追踪的飞行后效]", new Object[]{this.getName(),level,ase.getOwner().getName(),target.getName()});
					}
				}
			} catch(Exception e) {
				ActiveSkill.logger.error("Skill ======  :", e);
			}
			
			if(s != null){
				game.addSummoned(s);
			}
		}
	}
}
