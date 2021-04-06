package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.Buff_ChenMo;
import com.fy.engineserver.datasource.buff.Buff_CouHenDiYi;
import com.fy.engineserver.datasource.buff.Buff_DingSheng;
import com.fy.engineserver.datasource.buff.Buff_JianShu;
import com.fy.engineserver.datasource.buff.Buff_Silence;
import com.fy.engineserver.datasource.buff.Buff_XuanYun;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.LASTING_SKILL_BROADCAST_REQ;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 暴风雪NPC，此npc会持续一段时间，在这段时间内对它范围内的敌人进行伤害
 * 
 * 
 * 
 *
 */
public class BlizzardNPC extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370993L;

	/**
	 * 暴风雪释放者，该释放者死亡后，暴风雪消失
	 */
	private Fighter owner;
	
	public boolean 可以对玩家造成伤害 = false;
	
	public Fighter getOwner() {
		return owner;
	}

	public void setOwner(Fighter owner) {
		this.owner = owner;
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
	/**
	 * 暴风雪伤害范围宽
	 */
	public int attackWidth = 320;
	
	/**
	 * 暴风雪伤害范围高
	 */
	public int attackHeight = 160;
	
	/**
	 * 暴风雪伤害系数
	 */
	public double modulus = 1;
	
	public int 时间系数 = 1000;
	
	public long inValidTime;
	
	public long startTime;
	
	private long lastHeartbeatTime;
	
	public int ownerX;
	
	public int ownerY;
	
	public long intervalTimeAttack;
	
	public int damage = 1;
	
	public String buffName;
	
	public int buffLevel;
	
	public int buffProbability;
	
	public long buffLastingTime = -1;

	/**
	 * 已经通知过的玩家id
	 */
	public HashMap<Long,Player> hasNotifyPlayer = new HashMap<Long,Player>();
	
	public short width = 600;
	public short height = 600;
	
	public int skillId;
	public byte skillLevel;
	
	/**
	 * 通知新进入的玩家暴风雪技能的一些参数
	 */
	public void notifyNewPlayerBlizzardSkill(Game game){
		LivingObject[] los = game.getLivingObjects();
		if(los != null){
			byte casterType = 0;
			long casterId = 0;
			short targetX = 0;
			short targetY = 0;
			int skillId = 0;
			byte level = 0;
			long beginTime = 0;
			long endTime = 0;
			if(owner != null){
				casterType = owner.getClassType();
				casterId = owner.getId();
				targetX = (short)x;
				targetY = (short)y;
				skillId = this.skillId;
				level = this.skillLevel;
				beginTime = this.startTime;
				//客户端要求为当前时间，这样可以得到已经释放的时长
				endTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			}
			for(LivingObject lo : los){
				if(lo instanceof Player){
					Player p = (Player)lo;
					if((this.x - width <= p.getX()) && (this.x + width >= p.getX()) && (this.y - height <= p.getY()) && (this.y + height >= p.getY())){
						if(hasNotifyPlayer.get(p.getId()) == null){
							hasNotifyPlayer.put(p.getId(), p);
							LASTING_SKILL_BROADCAST_REQ req = new LASTING_SKILL_BROADCAST_REQ(GameMessageFactory.nextSequnceNum(), casterType, casterId, targetX, targetY, skillId, level, beginTime, endTime);
							p.addMessageToRightBag(req);
						}
					}
				}
			}
		}
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game){
			if (ActiveSkill.logger.isDebugEnabled()) ActiveSkill.logger.debug("[暴风雪开始心跳] [SkillWithoutTraceAndWithSummonNPC] [{}] [Lv:{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,owner.getName(),getName(),isAlive(),getX(),getY()});
		notifyNewPlayerBlizzardSkill(game);
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
				for(LivingObject lo : los){
					if(lo instanceof Monster){
						
					
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
								对敌人造成伤害((Fighter)lo, this.damage);
							}
						}
					}
				}
				
				if(可以对玩家造成伤害){
					ArrayList<LivingObject> llist = new ArrayList<LivingObject>();
					for(LivingObject lo : los){
						if(可以对玩家造成伤害 && (lo instanceof Player || lo instanceof Pet)){
							if(lo instanceof Fighter && owner.getFightingType((Fighter)lo) == Fighter.FIGHTING_TYPE_ENEMY && ((Fighter)lo).getHp() > 0){
								if((Fighter)lo != null && ((Fighter)lo).getHp() <= 0){
									continue;
								}
								if(((Fighter)lo) instanceof Player && owner instanceof Player && ((Fighter)lo).getLevel() <= PlayerManager.保护最大级别){
									Player p = (Player)lo;
									if(p.getCountry() == p.getCurrentGameCountry()){
										return;
									}
								}
								if(((Fighter)lo) instanceof Player && owner instanceof Pet && ((Fighter)lo).getLevel() <= PlayerManager.保护最大级别){
									Player p = (Player)lo;
									if(p.getCountry() == p.getCurrentGameCountry()){
										return;
									}
								}
								if(((Fighter)lo).canFreeFromBeDamaged(null)){
									continue;
								}
								if(rangeValid(lo.getX(), lo.getY())){
									llist.add(lo);
								}
							}
						}
					}
					
					if(llist != null){
						//打乱顺序
						Collections.shuffle(llist);
						int lsize = llist.size();
						for(int i = 0; i < 10 && i < lsize; i++){
							对敌人造成伤害((Fighter)llist.get(i), this.damage);
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

		if (damage != -100) {
			damage = CombatCaculator.caculateDamage(owner, attackerCareer, target, defenderCareer, 时间系数, false, damage);
		}
		
		/**仙心 坠天  获得灵印状态下使伤害值的1%转换成法力值**/
		{
			if(owner instanceof Player){
				Player player = (Player)owner;
				if(player!=null && damage>0){
					ActiveSkill.logger.debug("仙心 《坠天》《暴风雪》["+((Player)owner).getName()+"] [damage:"+damage+"] ["+this.getName()+"]");
					 if(player != null && player.getCareer()==3){
						Buff b = player.getBuffByName("灵印");
						if(b!=null){
							int step = SkEnhanceManager.getInst().getSlotStep(player, 5);
							int moreMp = 0;
							if (step > 0) {
								if(step==1){
									moreMp = (int)(damage * 0.01);
								}else if(step==2){
									moreMp = (int)(damage * 0.02);
								}else if(step==3){
									moreMp = (int)(damage * 0.03);
								}
							}
							ActiveSkill.logger.debug("仙心 《坠天》《暴风雪》 伤害{} 新法力值 {} 增加额外法力值{} 施法者{} 目标{} 技能{} step{}", new Object[]{damage,(player==null?"":player.getMp()),moreMp,(player==null?"":player.getName()),(target==null?"": target.getName()),this.getName(),step});
							if(moreMp>0){
								player.setMp(player.getMp() + moreMp);
								NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, player.getId(), (byte) Event.MP_INCREASED, moreMp);
								player.addMessageToRightBag(req);
							}
						}
					}
				}
			}
			
		}
		
		
		if (target instanceof Player) {
			Player p = (Player) target;
			if (p.isInvulnerable()) { // 无敌
				target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				
				if(ActiveSkill.logger.isInfoEnabled()){
					ActiveSkill.logger.info("[暴风雪技能命中计算] [{}] [{}] [{}] [{}] [{}] [目标处于无敌状态，减免所有伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI),damage});
				}
			} else {
				if (damage != -100) {
					target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_PHYSICAL);
					owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_PHYSICAL);
				}
				
				if(owner instanceof Player && target instanceof Player){
					Player pp = (Player)target;
					pp.notifyAttack((Player)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}else if(owner instanceof Pet && target instanceof Player){
					Player pp = (Player)target;
					pp.notifyAttack((Pet)owner, getName(),level, Fighter.DAMAGETYPE_PHYSICAL, damage);
				}
				fireBuff(owner, target);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[暴风雪技能命中计算] [{}] [{}] [{}] [{}] [targetHP:{}] [{}] [造成实际伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),target.getHp(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL),(damage)});
				}
			}
		} else if (target instanceof Sprite) {
			Sprite p = (Sprite) target;
			if (p.isInvulnerable()) { // 无敌
				target.causeDamage(owner, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				owner.damageFeedback(target, damage, 10,Fighter.DAMAGETYPE_MIANYI);
				
				if(ActiveSkill.logger.isInfoEnabled()){
					ActiveSkill.logger.info("[暴风雪技能命中计算] [{}] [{}] [{}] [{}] [targetHP:{}] [{}] [目标处于无敌状态，减免所有伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),target.getHp(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_MIANYI),damage});
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
				
				fireBuff(owner, target);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[暴风雪技能命中计算] [{}] [{}] [{}] [{}] [{}] [造成实际伤害:{}]", new Object[]{getClass().getName().substring(getClass().getName().lastIndexOf(".")+1),this.getName(),owner.getName(),target.getName(),ActiveSkill.getDamageTypeName(Fighter.DAMAGETYPE_PHYSICAL),(damage)});
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
		if(this.x - attackWidth <= x && this.x + attackWidth >= x && this.y - attackHeight <= y && this.y + attackHeight >= y){
			valid = true;
		}
		return valid;
	}
	
	
	/**
	 * 释放Buff，调用此方法表示技能已经命中目标，
	 * 准备释放Buff
	 * 
	 * @param caster
	 * @param target
	 * @param level 从1 开始
	 * @param effectIndex
	 */
	protected void fireBuff(Fighter caster, Fighter target){
		if(buffName == null || buffName.trim().equals("")){
			if(ActiveSkill.logger.isDebugEnabled()){
				ActiveSkill.logger.debug("[技能释放BUFF] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [没有配置]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buffName});
			}
			return;
		}
		
		if(level == 0){
			if(ActiveSkill.logger.isInfoEnabled()){
				ActiveSkill.logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [技能等级为0]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buffName});
			}
			return;
		}
		
		int probability = buffProbability;
		
		if(random.nextInt(100) + 1 > probability) {
			
			if(ActiveSkill.logger.isDebugEnabled()){
				ActiveSkill.logger.debug("[技能释放BUFF] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [概率不执行]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buffName,probability});
			}
			
			return;
		}
		
		BuffTemplateManager bm = BuffTemplateManager.getInstance();
		BuffTemplate bt = bm.getBuffTemplateByName(buffName);
		if(bt == null){
			//TODO: ActiveSkill.logger
			if(ActiveSkill.logger.isInfoEnabled()){
				ActiveSkill.logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [buff模板不存在]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buffName,probability});
			}
			
			return;
		}
		int bl = buffLevel;
		if(bl > 0)
			bl = bl - 1; //因为配置的时候bl从1开始，但是buff的数据数组下标从0开始
		Buff buff = bt.createBuff(bl);
		buff.setTemplate(bt);
		buff.setGroupId(bt.getGroupId());
		
		long bi = buffLastingTime;

		if(buff == null){
			if(ActiveSkill.logger.isInfoEnabled()){
				ActiveSkill.logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [创建buff失败]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),bt.getName(),probability});
			}
			
			return;
		}
		
		if(bi == -1){
			
			if(ActiveSkill.logger.isInfoEnabled()){
				ActiveSkill.logger.info("[技能释放BUFF失败] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [没有设置buff持续时间]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),bt.getName(),probability});
			}
			
			return;
		}

		buff.setLevel(bl);
		buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + bi);
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setCauser(caster);

		if(target instanceof BossMonster){
			BossMonster boss = (BossMonster)target;
			if(boss.isImmuneHoldFlag() && buff instanceof Buff_DingSheng){
				
				caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}else if(boss.isImmuneSilenceFlag() && (buff instanceof Buff_Silence || buff instanceof Buff_ChenMo)){
				caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}else if(boss.isImmuneSneerFlag() && buff instanceof Buff_CouHenDiYi){
				caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}else if(boss.isImmuneStunFlag() && buff instanceof Buff_XuanYun){
				caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}else if(boss.isImmuneSpeedDownFlag() && buff instanceof Buff_JianShu){
				caster.damageFeedback(target, 0, 0, Fighter.DAMAGETYPE_MIANYI);
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF失败，BOSS免疫] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}
			else{
				target.placeBuff(buff);
				
				if(ActiveSkill.logger.isDebugEnabled()){
					ActiveSkill.logger.debug("[技能释放BUFF成功] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability});
				}
			}
		}else{
		
			target.placeBuff(buff);
//				notifyBuffIcon(target,buff);
//			HunshiManager.instance.dealWithInfectSkill(caster, target, buff);
			if(ActiveSkill.logger.isDebugEnabled()){
				ActiveSkill.logger.debug("[技能释放BUFF成功] [{}] [{}] [Lv:{},In:{}] [{}] [{}] [{}] [{}] [{}] [概率：{}] [施加给目标] [开始时间:{}] [结束时间:{}]", new Object[]{(getClass().getName().substring(getClass().getName().lastIndexOf(".")+1)),this.getName(),caster.getName(),target.getName(),caster.getName(),target.getName(),buff.getDescription(),probability,buff.getStartTime(),buff.getInvalidTime()});
			}
		}
		
	}
	
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		BlizzardNPC p = new BlizzardNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
