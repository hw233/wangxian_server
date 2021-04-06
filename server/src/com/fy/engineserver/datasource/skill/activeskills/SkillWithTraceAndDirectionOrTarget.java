package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.effect.LinearEffectSummoned;
import com.fy.engineserver.sprite.effect.TracingEffectSummoned;


/**
 * 有轨迹的攻击指定目标或者方向的技能
 * 
 * 可以设定的参数有：
 * 		
 * 		目标的类型
 * 		后效的类型
 * 		后效飞行轨迹的类型
 * 		后效飞行的速度
 * 		后效飞行的最大距离
 * 		后效碰撞的宽度
 * 		后效爆炸的持续时间
 * 		后效的个数
 * 		每个后效的初始坐标（相对于施法者坐标）
 * 		每个后效的方向（按钟表分12个方向，取值为0~11，0标识12点钟方向，1表示1点钟方向，6表示6点钟方向...）
 * 		每一级需要消耗的魔法值
 * 
 * 此类技能支持对固定目标攻击和固定点进行攻击。
 * 但是当飞行轨迹为追踪时，必须指定攻击目标
 * 
 * 此类为技能的种类，所有的属性都是系统启动的时候加载的，在运行过程中不能被修改。
 * 
 * 一个技能种类，可以产生多个技能。这些技能虽然是一个技能种类，
 * 但是由于他们初始参数不同，可能是完全两样的技能。
 * 
 * 
 * 
 *
 */
public class SkillWithTraceAndDirectionOrTarget extends ActiveSkill implements Cloneable{

//	static Logger logger = Logger.getLogger(SkillWithTraceAndDirectionOrTarget.class);
public	static Logger logger = LoggerFactory.getLogger(SkillWithTraceAndDirectionOrTarget.class);
	
	public static final int DIRECTIONS[][] = {
		{0,-10},
		{5,-8},
		{8,-5},
		{10,0},
		{8,5},
		{5,8},
		{0,10},
		{-5,8},
		{-8,5},
		{-10,0},
		{-8,-5},
		{-5,-8}
	};
	/**
	 * 目标类型，
	 * 	0 表示对某个目标或者位置点进行攻击，无需配置后效方向
	 * 	1 表示对周围环境进行攻击，需配置后效方向
	 */
	private byte targetType = 0;
	
	/**
	 * 后效的类型，比如火球，弓箭，冲击波等等
	 * 此值必须和Mapping中的值相对应，从0开始。
	 */
	private String effectType = "";
	
	String avataRace = "";
	
	String avataSex = "";

	/**
	 * 轨迹的类型，0表示直线，1表示追踪
	 */
	private byte trackType = 1;
	
	/**
	 * 发出召唤物的时机，技能开始多少毫秒后发出召唤物
	 */
	public long sendEffectTime;
	/**
	 * 飞行物体的飞行速度
	 */
	private int speed = 200;
	/**
	 * 飞行物体的飞行距离
	 */
	private int range = 500;
	
	/**
	 * 攻击的宽度
	 */
	private int attackWidth = 32;
	
	/**
	 * 后效爆炸持续的时间
	 */
	private int explosionLastingTime = 800;
	
	/**
	 * 后效的个数
	 */
	private int effectNum = 1;
	
	/**
	 * 每个后效的初始位置，如果有10个后效，那么此参数应该填10值。
	 * 如果填5个值，那么后面5个值默认为0
	 * 每个数值的含义是：沿施法者面朝的方向，向前多少个像素，如果为负值，本质就是向后。
	 * 
	 */
	private int effectInitPositionX[];

	/**
	 * 每个后效的初始位置，如果有10个后效，那么此参数应该填10值。
	 * 如果填5个值，那么后面5个值默认为0
	 * 每个数值的含义是：沿施法者右侧的方向，向右多少个像素，如果为负值，本质就是向左。
	 */
	private int effectInitPositionY[];

	/**
	 * 某个后效的方向
	 * 按钟表分12个方向，取值为0~11，0标识12点钟方向，1表示1点钟方向，6表示6点钟方向..
	 */
	private byte effectInitDirection[];
	
	/**
	 * 消耗的法力值，跟等级相关，一维数组
	 * 长度为最大的等级
	 */
	private short[] mp;
	
	/**
	 * 后效能穿透的次数，0表示不能穿透
	 */
	private int penetrateNum = 0;
	/** 1为金猴天灾技能 */
	private int specialSkillType = 0;

	
	//////////////////////////////////////////////////////////////////////////////////////
	//
	//  以下为服务器端使用的数值计算需要的数据
	//
	//////////////////////////////////////////////////////////////////////////////////////

	public byte getTargetType() {
		return targetType;
	}

	public void setTargetType(byte targetType) {
		this.targetType = targetType;
		if(targetType == 1){
			this.trackType = 0;
		}
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

	public byte[] getEffectInitDirection() {
		return effectInitDirection;
	}


	public void setEffectInitDirection(byte[] effectInitDirection) {
		this.effectInitDirection = effectInitDirection;
	}


	public String getEffectType() {
		return effectType;
	}


	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}


	public byte getTrackType() {
		return trackType;
	}


	public void setTrackType(byte trackType) {
		this.trackType = trackType;
		if(trackType == 1){
			this.targetType = 0;
		}
	}


	public long getSendEffectTime() {
		return sendEffectTime;
	}

	public void setSendEffectTime(long sendEffectTime) {
		this.sendEffectTime = sendEffectTime;
	}

	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}


	public int getRange() {
		return range;
	}


	public void setRange(int range) {
		this.range = range;
	}





	public int getExplosionLastingTime() {
		return explosionLastingTime;
	}


	public void setExplosionLastingTime(int explosionLastingTime) {
		this.explosionLastingTime = explosionLastingTime;
	}


	public int getEffectNum() {
		return effectNum;
	}


	public void setEffectNum(int effectNum) {
		this.effectNum = effectNum;
	}

	public short[] getMp() {
		return mp;
	}


	public void setMp(short[] mp) {
		this.mp = mp;
	}


	public int getAttackWidth() {
		return attackWidth;
	}


	public void setAttackWidth(int attackWidth) {
		this.attackWidth = attackWidth;
	}


	public int getPenetrateNum() {
		return penetrateNum;
	}


	public void setPenetrateNum(int penetrateNum) {
		this.penetrateNum = penetrateNum;
	}


	public int check(Fighter caster, Fighter target, int level) {
		//TODO: 技能还要根据玩家的状态来判断，是否能使用
		
		int result = 0;
		if(targetType == 0){
			if (target != null) {
				int dx = caster.getX() - target.getX();
				int dy = caster.getY() - target.getY();
				if (dx * dx + dy * dy > range * range) {
					result |= TARGET_TOO_FAR;
				}
				
				if(caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY){
					result |= TARGET_NOT_MATCH;
				}
				
				if(target != null && target.isDeath()){
					result |= TARGET_NOT_MATCH;
				}
				
			}else if(trackType == 1){
				result |= TARGET_NOT_EXIST;
			}
		}
		
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
//			logger.debug("[技能检查] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target != null?target.getName():"-")+"] ["+ Skill.getSkillFailReason(result) +"]");
			if(logger.isDebugEnabled())
				logger.debug("[技能检查] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		return result;
	}


	public void run(ActiveSkillEntity ase, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {
		if (specialSkillType == 1) {
			this.run2(ase, target, game, level, effectIndex, x, y, direction);
			if (logger.isDebugEnabled()) {
				logger.debug("[使用金猴天灾特殊技能] [成功] [" + (ase.getSkill() != null ? ase.getSkill().getName() : "") + "]");
			}
			return ;
		}
		int step = 0;
		if(id == 804 && ase.getOwner() instanceof Player){
			//仙心	804	流星	人阶10重:激活流星技能伤害目标+1
			step = SkEnhanceManager.getInst().getSlotStep((Player) ase.getOwner(), pageIndex);
			if(step>0)
			logger.debug("仙心804流星人阶10重:激活流星技能伤害目标 {}",step);
		}
		int penetrateNumLocal = this.penetrateNum + step;
		logger.debug(" penetrateNumLocal {} effectNum{}",penetrateNumLocal,effectNum);
		for(int i = 0 ; i < effectNum ; i++){
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
				if(effectInitPositionX != null && i < effectInitPositionX.length )
					ex = ex + this.effectInitPositionX[i];
				if(effectInitPositionY != null && i < effectInitPositionY.length )
					ey = ey + this.effectInitPositionY[i];
			}else if(direction == LivingObject.DOWN){
				if(effectInitPositionY != null && i < effectInitPositionY.length )
					ey = ey + this.effectInitPositionX[i];
				if(effectInitPositionX != null && i < effectInitPositionX.length )
					ex = ex - this.effectInitPositionY[i];
			}else if(direction == LivingObject.LEFT){
				if(effectInitPositionX != null && i < effectInitPositionX.length )
					ex = ex - this.effectInitPositionX[i];
				if(effectInitPositionY != null && i < effectInitPositionY.length )
					ey = ey - this.effectInitPositionY[i];
			}else {
				if(effectInitPositionY != null && i < effectInitPositionY.length )
					ey = ey - this.effectInitPositionX[i];
				if(effectInitPositionX != null && i < effectInitPositionX.length )
					ex = ex + this.effectInitPositionY[i];
			}
//			ActiveSkill.logger.info("[LinearEffectSummoned] [dx:"+ex+"] [dy:"+ey+"]");
			if(ActiveSkill.logger.isInfoEnabled())
				ActiveSkill.logger.info("[LinearEffectSummoned] [dx:{}] [dy:{}] [技能名字:{}]", new Object[]{ex,ey, ase.getSkill().getName()});
			EffectSummoned s = null;
			try {
				if(trackType == 0){
					if(targetType == 0 && target != null && ase.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
						s = new LinearEffectSummoned(ase,effectIndex,
							ex, ey, effectType,avataRace,avataSex,  target.getX() - ex,
							target.getY() - ey, speed, range*2, speed/HEART_BEATS_PER_SECOND,
							attackWidth, explosionLastingTime,
							 penetrateNumLocal,
							target);
						
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+target.getName()+"] [对敌方目标施放直线飞行的后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [{}] [对敌方目标施放直线飞行的后效]", new Object[]{this.getName(),level,ase.getOwner().getName(),target.getName()});
					}else if(targetType == 0){
						//TODO: 这里需要根据玩家的状态来判断倒是是打怪还是打玩家
						s = new LinearEffectSummoned(ase,effectIndex,
								ex, ey, effectType,avataRace,avataSex,  x - ex,
								y - ey, speed, range*2, speed/HEART_BEATS_PER_SECOND,
								attackWidth, explosionLastingTime,
								 penetrateNumLocal,
								(Fighter)null);
//							logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [----] [对固定坐标点施放直线飞行的后效]");
							if(ActiveSkill.logger.isDebugEnabled())
								ActiveSkill.logger.debug("[执行技能攻击] [SkillWithTraceAndDirectionOrTarget] [{}] [Lv:{}] [{}] [----] [对固定坐标点施放直线飞行的后效]", new Object[]{this.getName(),level,ase.getOwner().getName()});
					}else if(targetType == 1 && effectInitDirection != null && i < effectInitDirection.length
							&& effectInitDirection[i] >= 0 && effectInitDirection[i] < DIRECTIONS.length){
						int k = effectInitDirection[i];
						if(direction == LivingObject.RIGHT){
							k += 3;
						}else if(direction == LivingObject.DOWN){
							k += 6;
						}else if(direction == LivingObject.LEFT){
							k += 9;
						}
						k = k % DIRECTIONS.length;
						
						s = new LinearEffectSummoned(ase,effectIndex,
								ex, ey, effectType,avataRace,avataSex,  DIRECTIONS[k][0],
								DIRECTIONS[k][1], speed, range*2, speed/HEART_BEATS_PER_SECOND,
								attackWidth, explosionLastingTime,
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
							ex, ey, effectType, avataRace,avataSex,
							speed, range*2, speed /HEART_BEATS_PER_SECOND,
							attackWidth, explosionLastingTime, target);
						
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


	


	public int[] getEffectInitPositionX() {
		return effectInitPositionX;
	}


	public void setEffectInitPositionX(int[] effectInitPositionX) {
		this.effectInitPositionX = effectInitPositionX;
	}


	public int[] getEffectInitPositionY() {
		return effectInitPositionY;
	}


	public void setEffectInitPositionY(int[] effectInitPositionY) {
		this.effectInitPositionY = effectInitPositionY;
	}


	@Override
	public SkillWithTraceAndDirectionOrTarget clone() {
		try {
			return (SkillWithTraceAndDirectionOrTarget) super.clone();
		} catch (CloneNotSupportedException e) {
			Skill.logger.error("克隆失败:",e);
			return null;
		}
	}
	
	public static int DIRECTIONS2[][] = {
		{10,0},
		{23,2},{17,3},{11,3},{11,4},{15,7},{8,5},{7,5},{6,5},{4,4},{4,5},{2,3},{5,8},{1,2},{1,3},{1,4},{1,6},{1,12},
		{0,10},	
		
		{23,-2},{17,-3},{11,-3},{11,-4},{15,-7},{8,-5},{7,-5},{6,-5},{4,-4},{4,-5},{2,-3},{5,-8},{1,-2},{1,-3},{1,-4},{1,-6},{1,-12},
		{0,-10},	
		
		{-10,0},
		{-23,2},{-17,3},{-11,3},{-11,4},{-15,7},{-8,5},{-7,5},{-6,5},{-4,4},{-4,5},{-2,3},{-5,8},{-1,2},{-1,3},{-1,4},{-1,6},{-1,12},
		
		{-23,-2},{-17,-3},{-11,-3},{-11,-4},{-15,-7},{-8,-5},{-7,-5},{-6,-5},{-4,-4},{-4,-5},{-2,-3},{-5,-8},{-1,-2},{-1,-3},{-1,-4},{-1,-6},{-1,-12},
	};

	@Override
	public void hit(Fighter caster, Fighter target, int level, int effectIndex) {
		// TODO Auto-generated method stub
		if (specialSkillType == 1) {
			int damage = this.calDamageOrHpForFighter1(caster, level);
//			if (target instanceof Player) {
//				DisasterManager.getInst().causeDamage((Player) target, damage);
//			}
		} else {
			super.hit(caster, target, level, effectIndex);
		}
	}
	/**
	 * 金猴天灾伤害计算
	 */
	public int calDamageOrHpForFighter1(Fighter f, int level) {
		int dmg = this.getAttackDamages()[0];
		return dmg;
	}
	
	public void run2(ActiveSkillEntity ase, Fighter target, Game game,
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

	public int getSpecialSkillType() {
		return specialSkillType;
	}

	public void setSpecialSkillType(int specialSkillType) {
		this.specialSkillType = specialSkillType;
	}

}
