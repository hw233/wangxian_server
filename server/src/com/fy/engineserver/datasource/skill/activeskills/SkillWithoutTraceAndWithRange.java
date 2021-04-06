package com.fy.engineserver.datasource.skill.activeskills;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill2.GenericBuffCalc;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 无轨迹范围攻击技能种类
 * 
 * 
 * 
 *
 */
public class SkillWithoutTraceAndWithRange extends ActiveSkill implements Cloneable{

public	static Logger logger = LoggerFactory.getLogger(Skill.class);
	/**
	 * 范围的类型：
	 * 	0 以施法者自身为中心的矩形，不包括自己
	 * 	1 正前方的矩形，不包括自己
	 */
	byte rangeType = 0;
	
	/**
	 * 范围的宽度，默认为240
	 */
	int rangeWidth = 240;
	
	/**
	 * 范围的高度，默认为320
	 */
	int rangeHeight = 320;
	
	/**
	 * 攻击最大个数
	 */
	private short[] maxAttackNums = new short[0];
	
	int maxAttackNum = 3;
	
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
	
	
	//////////////////////////////////////////////////////////////////////////////////////
	//
	//  以下为服务器端使用的数值计算需要的数据
	//
	//////////////////////////////////////////////////////////////////////////////////////

	
	public int check(Fighter caster, Fighter target, int level) {
		int result = 0;
		
		if (caster instanceof Player) {
			Player p = (Player)caster;
			if(nuqiFlag){
//				if(p.getXp() < p.getTotalXp()){
//					result |= NUQI_NOT_ENOUGH;
//				}
			}else{
				int mp = calculateMp(p,level);
				if(p.getMp() < mp){
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
		
		if(this.getEnableWeaponType() == 1 && caster instanceof Player){
			Player p = (Player)caster;
			if( p.getWeaponType() != this.getWeaponTypeLimit()){
				result |= WEAPON_NOT_MATCH;
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
		if(logger.isDebugEnabled()){
			logger.debug("[技能检查] [SkillWithoutTraceAndWithRange] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		return result;
	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {

		//TODO: 需要根据玩家的状态来选择攻击什么目标，我们先以攻击玩家为例
		
		Fighter[] los = new Fighter[0];
		byte[] targetTypes = ase.targetTypes;
		long[] targetIds = ase.targetIds;
		if(logger.isDebugEnabled()) {
			logger.debug("[SkillWithoutTraceAndWithRange.run()1] [target : " + target + "][" + (ase==null?"-":ase.targetTypes) + "]");
		}
		if(targetTypes != null && targetIds != null){
			if(logger.isDebugEnabled()) {
				logger.debug("[SkillWithoutTraceAndWithRange.run()2] [target : " + target + "][targetTypes.length:" + targetTypes.length + "]");
			}
			los = new Fighter[targetTypes.length];
			GameManager gm = GameManager.getInstance();
			for(int i = 0; i < targetTypes.length && i < targetIds.length; i++){
				if(logger.isDebugEnabled()) {
					logger.debug("[SkillWithoutTraceAndWithRange.run()2] [target : " + target + "][targetTypes:" + targetTypes[i] + "]");
				}
				long targetId = targetIds[i];
				switch(targetTypes[i]){
				case 0:
					try {
						target = gm.playerManager.getPlayer(targetId);
						los[i] = target;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 1:
					target = gm.getMonsterManager().getMonster(targetId);
					if (target == null) {
						target = gm.getNpcManager().getNPC(targetId);
					}
					if (target == null) {
						target = PetManager.getInstance().getPetInMemory(targetId);
					}
					if(target != null){
						los[i] = target;
					}
					break;
				}
			}
		}
	
		ArrayList<Fighter> v = new ArrayList<Fighter>();
		boolean needExtraBuff = false;
		
		Fighter tempMainSpreadId = null;				//兽魁传播buff技能用，传播者不需要上传播的buff
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof Fighter == false) continue;
			
			Fighter t = (Fighter)los[i];
			
			int ft = ase.getOwner().getFightingType(t);
			if(ft != Fighter.FIGHTING_TYPE_ENEMY) continue;
			if(t.isDeath()) continue;
			
			if( Math.abs(t.getX() - ase.getOwner().getX()) < rangeWidth*3 &&
					Math.abs(t.getY() - ase.getOwner().getY()) < rangeHeight*3){
				if(!v.contains(t)){
					v.add(t);
					try {
						if (!needExtraBuff && t.getSpriteStatus(ase.getOwner()) == Constants.shihunFlag) {
							tempMainSpreadId = t;
							needExtraBuff = true;
						}
					} catch (Exception e) {
						logger.warn("[兽魁特殊技能] [传播buff] [异常] [" + this.getId() + "] ", e);
					}
				}
			}
		}
		if (needExtraBuff) {
			if (!this.isSpecialSkillFlag() || this.getSpreadBuffName() == null || this.getSpreadBuffName().isEmpty()) {
				needExtraBuff = false;
			}
		}
		
		int maxAttackNum = 0;
		if(maxAttackNums != null && maxAttackNums.length > 0 && maxAttackNums.length >= level){
			int index = 0;
			if(level > 0){
				index = level - 1;
			}
			maxAttackNum = maxAttackNums[index];
		}
		{
			maxAttackNum += SkEnhanceManager.getInst().getTargetAdd(id, ase.getOwner(), this);
		}
		logger.debug("SkillWithoutTraceAndWithRange.run: 发来个数 {}",targetIds.length);
		logger.debug("SkillWithoutTraceAndWithRange.run: 候选目标个数 {},目标个数{}",v.size(),maxAttackNum);
		if(maxAttackNum == 0 || v.size() <= maxAttackNum){
			for(int i = 0 ; i < v.size() ; i++){
				Fighter t = v.get(i);
//				PositionEffectSummoned s = new PositionEffectSummoned(ase,effectIndex,t,t.getX(),t.getY(),effectType,avataRace,avataSex,
//						effectLastTime,effectExplosionLastTime);
//				game.addSummoned(s);
				
				ase.hitTarget(t, effectIndex);
				ase.getOwner().notifyPrepareToFighting(t);
				t.notifyPrepareToBeFighted(ase.getOwner());
				if (needExtraBuff) {
					String result = this.spreadBuff(ase.getOwner(), t, tempMainSpreadId, this.getSpreadBuffName(), ase.getLevel());
					if (logger.isDebugEnabled()) {
						logger.debug("["+this.getClass().getSimpleName()+"] [额外buff] [" + t.getId() + "] [" + t.getName() + "] [result : " + result + "]");
					}
				}
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("[宠物使用群体攻击] [WithRange] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + this.getName() + "] [" + t.getName() + "]");
					}
					if (ase.getOwner() instanceof Pet && target != null && (!target.canFreeFromBeDamaged(ase.getOwner()))) {
						boolean isProtect = false;
						if (target instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
							Player p = (Player) target;
							if (p.getCountry() == p.getCurrentGameCountry()) {
								isProtect = true;
							}
						}
						if (!isProtect) {
							Pet pet = (Pet) ase.getOwner();
							GenericBuffCalc.getInst().procBuff(t, pet, pet.getSkillAgent().atkTimes, logger);
						}
					}
				} catch (Exception e) {
					logger.error("[宠物群体攻击判断触发buff] [异常] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "]", e);
				}
				
				if(logger.isDebugEnabled()){
					logger.debug("[执行技能攻击A{}]  [{}] [Lv:{}] [{}] [{}] ]", new Object[]{(i+1),this.getName(),level,ase.getOwner().getName(),t.getName()});
				}
			}
		}else{
//			int distances[] = new int[v.size()];
//			Fighter fs[] = new Fighter[v.size()];
//			for(int i = 0 ; i < v.size() ; i++){
//				Fighter lo = (Fighter)v.get(i);
//				fs[i] = lo;
//				distances[i] = (lo.getX() - ase.getOwner().getX()) * (lo.getX() - ase.getOwner().getX());
//				distances[i] += (lo.getY() - ase.getOwner().getY()) * (lo.getY() - ase.getOwner().getY());
//			}
//			
//			for(int i = 0 ; i < fs.length ; i++){
//				for(int j = i+1 ; j < fs.length ; j++){
//					if(distances[j] < distances[i]){
//						int t = distances[i];
//						distances[i] = distances[j];
//						distances[j] = t;
//						Fighter lo = fs[i];
//						fs[i] = fs[j];
//						fs[j] = lo;
//					}
//				}
//			}
			
			for(int i = 0 ; i < v.size() && i < maxAttackNum; i++){
				Fighter t = v.get(i);
//				PositionEffectSummoned s = new PositionEffectSummoned(ase,effectIndex,t,t.getX(),t.getY(),effectType,avataRace,avataSex,
//						effectLastTime,effectExplosionLastTime);
//				game.addSummoned(s);
				ase.hitTarget(t, effectIndex);
				ase.getOwner().notifyPrepareToFighting(t);
				t.notifyPrepareToBeFighted(ase.getOwner());
				if (needExtraBuff) {
					String result = this.spreadBuff(ase.getOwner(), t, tempMainSpreadId, this.getSpreadBuffName(), ase.getLevel());
					if (logger.isDebugEnabled()) {
						logger.debug("["+this.getClass().getSimpleName()+"] [额外buff] [" + t.getId() + "] [" + t.getName() + "] [result : " + result + "]");
					}
				}
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("[宠物使用群体攻击] [WithRange] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + this.getName() + "] [" + t.getName() + "]");
					}
					if (ase.getOwner() instanceof Pet && target != null && (!target.canFreeFromBeDamaged(ase.getOwner()))) {
						boolean isProtect = false;
						if (target instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
							Player p = (Player) target;
							if (p.getCountry() == p.getCurrentGameCountry()) {
								isProtect = true;
							}
						}
						if (!isProtect) {
							Pet pet = (Pet) ase.getOwner();
							GenericBuffCalc.getInst().procBuff(t, pet, pet.getSkillAgent().atkTimes, logger);
						}
					}
				} catch (Exception e) {
					logger.error("[宠物群体攻击判断触发buff] [异常] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "]", e);
				}
				
				if(logger.isDebugEnabled()){
					logger.debug("[执行技能攻击B{}]  [{}] [Lv:{}] [{}] [{}] ", new Object[]{(i+1),this.getName(),level,ase.getOwner().getName(),t.getName()});
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


	public int getRangeWidth() {
		return rangeWidth;
	}

	public void setRangeWidth(int rangeWidth) {
		this.rangeWidth = rangeWidth;
	}

	public byte getRangeType() {
		return rangeType;
	}

	public void setRangeType(byte rangeType) {
		this.rangeType = rangeType;
	}

	public int getRangeHeight() {
		return rangeHeight;
	}

	public void setRangeHeight(int rangeHeight) {
		this.rangeHeight = rangeHeight;
	}

	public short[] getMaxAttackNums() {
		return maxAttackNums;
	}

	public void setMaxAttackNums(short[] maxAttackNums) {
		this.maxAttackNums = maxAttackNums;
	}

	public int getMaxAttackNum() {
		return maxAttackNum;
	}

	public void setMaxAttackNum(int maxAttackNum) {
		this.maxAttackNum = maxAttackNum;
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

		public SkillWithoutTraceAndWithRange clone() {
			// TODO Auto-generated method stub
			try {
				return (SkillWithoutTraceAndWithRange) super.clone();
			} catch (CloneNotSupportedException e) {
				Skill.logger.error("克隆出错", e);
			}
			return null;
		}

}
