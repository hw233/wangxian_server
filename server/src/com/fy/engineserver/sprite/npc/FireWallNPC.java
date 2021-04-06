package com.fy.engineserver.sprite.npc;

import java.util.Random;

import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 火墙NPC，此npc会持续一段时间，在这段时间内对它范围内的敌人进行伤害
 * 
 * 
 * 
 *
 */
public class FireWallNPC extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370993L;

	/**
	 * 火墙释放者，该释放者死亡后，火墙消失
	 */
	private Fighter owner;
	
	public Fighter getOwner() {
		return owner;
	}

	public void setOwner(Fighter owner) {
		this.owner = owner;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getModulus() {
		return modulus;
	}

	public void setModulus(double modulus) {
		this.modulus = modulus;
	}

	public long getInValidTime() {
		return inValidTime;
	}

	public void setInValidTime(long inValidTime) {
		this.inValidTime = inValidTime;
	}
	@Override
	public byte getNpcType() {
		// TODO Auto-generated method stub
		return Sprite.NPC_TYPE_FIREWALL;
	}
	/**
	 * 火墙伤害范围
	 */
	public int range = 20;
	
	/**
	 * 火墙伤害系数
	 */
	public double modulus = 1;
	
	public long inValidTime;
	
	private long lastHeartbeatTime;
	
	/**
	 * 多长时间攻击一次
	 */
	public long intervalTimeAttack;
	/**
	 * 
	 * @param owner
	 * @param range 火墙npc的攻击范围
	 * @param modulus 计算伤害系数
	 * @param inValidTime 火墙结束时间
	 */
//	public FireWallNpc(Fighter owner,int range,double modulus,long inValidTime){
//		this.owner = owner;
//		this.range = range;
//		this.modulus = modulus;
//		this.inValidTime = inValidTime;
//	}
//	
//	private FireWallNpc(){
//		
//	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game){
//		ActiveSkill.logger.debug("[火墙开始心跳] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+owner.getName()+"] ["+getName()+"] ["+isAlive()+"] ["+getX()+"] ["+getY()+"]");
			if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("[火墙开始心跳] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,owner.getName(),getName(),isAlive(),getX(),getY()});
		if(owner == null || owner.isDeath() || heartBeatStartTime >= inValidTime){
			this.setAlive(false);
		}
		if(this.isAlive() == false){
			return;
		}

		if(intervalTimeAttack <= 0){
			intervalTimeAttack = 2*1000;
		}
		if(heartBeatStartTime > lastHeartbeatTime + intervalTimeAttack){
			lastHeartbeatTime = heartBeatStartTime;
			LivingObject[] los = game.getLivingObjects();
			if(los != null){
				int damage = 根据系数计算数值();
				for(LivingObject lo : los){
					if(lo instanceof Fighter && owner.getFightingType((Fighter)lo) == Fighter.FIGHTING_TYPE_ENEMY && ((Fighter)lo).getHp() > 0){

						if((Fighter)lo != null && ((Fighter)lo).getHp() <= 0){
							continue;
						}
						
						if(((Fighter)lo) instanceof Player && owner instanceof Player && ((Fighter)lo).getLevel() <= PlayerManager.保护最大级别){
							continue;
						}
						
						if(((Fighter)lo) instanceof Player && owner instanceof Pet && ((Fighter)lo).getLevel() <= PlayerManager.保护最大级别){
							continue;
						}
						
						if(((Fighter)lo).canFreeFromBeDamaged(null)){
							continue;
						}

						if(rangeValid(lo.getX(), lo.getY())){
							对敌人造成伤害((Fighter)lo, damage);
						}
					}
				}
			}
		}
	}
	
	public void 对敌人造成伤害(Fighter target, int damage){
		//调用caculateDamage方法计算出了技能伤害外的其他伤害值
		int attackerCareer = 0;
		if(owner instanceof Player){
			attackerCareer = ((Player)owner).getCareer();
		}else if(owner instanceof Sprite){
			attackerCareer = ((Sprite)owner).getCareer();
		}
		int defenderCareer = 0;
		if(target instanceof Player){
			defenderCareer = ((Player)target).getCareer();
		}else if(target instanceof Sprite){
			defenderCareer = ((Sprite)target).getCareer();
		}
		damage = CombatCaculator.caculateDamage(owner, attackerCareer, target, defenderCareer, 2000, false, damage);
		if (target instanceof Player) {
			Player p = (Player) target;
			if (p.isInvulnerable()) { // 无敌
				target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				
				if(ActiveSkill.logger.isInfoEnabled()){
//ActiveSkill.logger.info("[火墙技能命中计算] ["+getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)
//+"] ["+this.getName()+"] ["+owner.getName()+"] ["+target.getName()+"] ["+ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI)+"] [目标处于无敌状态，减免所有伤害:"+damage+"]");
if(ActiveSkill.logger.isInfoEnabled())
	ActiveSkill.logger.info("[火墙技能命中计算] [{}] [{}] [{}] [{}] [{}] [目标处于无敌状态，减免所有伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI),damage});
				}
			} else {
				target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_PHYSICAL);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_PHYSICAL);
				
				if(owner instanceof Player && target instanceof Player){
					Player pp = (Player)target;
					pp.notifyAttack((Player)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}else if(owner instanceof Pet && target instanceof Player){
					Player pp = (Player)target;
					pp.notifyAttack((Pet)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}
				
				if(ActiveSkill.logger.isDebugEnabled()){
//ActiveSkill.logger.info("[火墙技能命中计算] ["+getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)
//+"] ["+this.getName()+"] ["+owner.getName()+"] ["+target.getName()+"] [targetHP:"+target.getHp()+"] ["+ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL)+"] [造成实际伤害:"+(damage)+"]");
if(ActiveSkill.logger.isDebugEnabled())
	ActiveSkill.logger.debug("[火墙技能命中计算] [{}] [{}] [{}] [{}] [targetHP:{}] [{}] [造成实际伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),target.getHp(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL),(damage)});
				}
			}
		} else if (target instanceof Sprite) {
			Sprite p = (Sprite) target;
			if (p.isInvulnerable()) { // 无敌
				target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				
				if(ActiveSkill.logger.isInfoEnabled()){
//ActiveSkill.logger.info("[火墙技能命中计算] ["+getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)
//+"] ["+this.getName()+"] ["+owner.getName()+"] ["+target.getName()+"] [targetHP:"+target.getHp()+"] ["+ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI)+"] [目标处于无敌状态，减免所有伤害:"+damage+"]");
if(ActiveSkill.logger.isInfoEnabled())
	ActiveSkill.logger.info("[火墙技能命中计算] [{}] [{}] [{}] [{}] [targetHP:{}] [{}] [目标处于无敌状态，减免所有伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),target.getHp(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI),damage});
				}
				
			} else {
				
				target.causeDamage(owner, damage , 10,Fighter.DAMAGETYPE_PHYSICAL);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_PHYSICAL);
				
				if(owner instanceof Player && target instanceof Monster){
					Monster m = (Monster)target;
					m.notifyAttack((Player)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}else if(owner instanceof Pet && target instanceof Monster){
					Monster pp = (Monster)target;
					pp.notifyAttack((Pet)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}
				
				
				if(ActiveSkill.logger.isDebugEnabled()){
//ActiveSkill.logger.info("[火墙技能命中计算] ["+getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)
//+"] ["+this.getName()+"] ["+owner.getName()+"] ["+target.getName()+"] ["+ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL)+"] [造成实际伤害:"+(damage)+"]");
if(ActiveSkill.logger.isDebugEnabled())
	ActiveSkill.logger.debug("[火墙技能命中计算] [{}] [{}] [{}] [{}] [{}] [造成实际伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL),(damage)});
				}
			}
		}
	}
	
	Random random = new Random();
	public int 根据系数计算数值(){
		int value = 1;
		value = (int)(modulus * 10);
		return value;
	}
	
	/**
	 * 判断合适距离
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean rangeValid(int x,int y){
		boolean valid = false;
		double resultValue = (this.x - x)*(this.x - x)+(this.y - y)*(this.y - y);
		double result = Math.sqrt(resultValue);
		if(result <= range){
			valid = true;
		}
		return valid;
	}
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		FireWallNPC p = new FireWallNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
