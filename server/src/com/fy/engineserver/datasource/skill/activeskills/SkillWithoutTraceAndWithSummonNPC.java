package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.BlizzardNPC;
import com.fy.engineserver.sprite.npc.FireWallNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;

/**
 * 无轨迹范围攻击技能种类
 * 
 * 此类技能攻击某个地区的敌人，后效的作用点在这个地区呈现某个规则的分布。
 * 
 * 此类技能的后效个数最大为32
 * 
 * 
 * 
 *
 */
public class SkillWithoutTraceAndWithSummonNPC extends ActiveSkill implements Cloneable{

//	static Logger logger = Logger.getLogger(SkillWithoutTraceAndWithMatrix.class);
public	static Logger logger = LoggerFactory.getLogger(SkillWithoutTraceAndWithSummonNPC.class);
	
	public static final int MAX_EFFECT_NUM = 9;
	public static final byte[][][] matrixData = new byte[][][]{
	//      1      2      3      4     5      6     7     8       9     10     11     12   13      14    15    16      17      18     19    20     21    22    23     24       25      26    27    28     29     30    31     32
	//正方形排列
		{ {0,0},{-1,-1},{1,1},{1,-1},{-1,1},{-1,0},{1,0},{0,1},{0,-1}},
	//菱形排列	
		{{0,0} ,{-2,0} ,{2,0},{0,-2},{0,2},{-1,-1},{1,1},{1,-1},{-1,1}},
	};
	/**
	 * 技能形式：
	 *  0 以自身为中心的火墙
	 *  1 以坐标点为中心的火墙
	 *  2 以自身为中心的暴风雪
	 *  3 以目标为中心的暴风雪
	 *  4 以坐标点为中心的暴风雪
	 */
	byte matrixType = 0;

	/**
	 * 攻击数量
	 */
	private short[] attackNum = new short[0];

	/**
	 * 后效持续的时间，为毫秒
	 */
	int effectLastTime = 300;
	
	/**
	 * 后效持续的时间过后，爆炸持续的时间
	 */
	int effectExplosionLastTime = 500;
	
	/**
	 * 消耗的法力值，跟等级相关
	 */
	private short[] mp = new short[0];
	
	/**
	 * 攻击的有效距离
	 */
	private int range;
	
	/**
	 * 椭圆宽
	 */
	int gapWidth = 320;
	
	/**
	 * 椭圆高
	 */
	int gapHeight = 160;
	
	/**
	 * 粒子总体飞行持续时间，如多批粒子整体时间(暴风雪类技能用)
	 */
	public long maxTimeLength;
	
	/**
	 * 每个粒子飞行速度(暴风雪类技能用)
	 */
	public short speed;
	
	/**
	 * 每个粒子飞行角度(暴风雪类技能用)
	 */
	public short angle;
	
	/**
	 * 粒子飞行高度(暴风雪类技能用)
	 */
	public short heigth;
	
	/**
	 * 高峰时期粒子数量(暴风雪类技能用)
	 */
	public short maxParticleEachTime;
	
	/**
	 * 间隔多长时间攻击一次
	 */
	public long intervalTimeAttack;
	
	public boolean 可以对玩家造成伤害 = false;

	public int getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(int gapWidth) {
		this.gapWidth = gapWidth;
	}

	public int getGapHeight() {
		return gapHeight;
	}

	public void setGapHeight(int gapHeight) {
		this.gapHeight = gapHeight;
	}

	public short[] getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(short[] attackNum) {
		this.attackNum = attackNum;
	}

	public long getMaxTimeLength() {
		return maxTimeLength;
	}

	public void setMaxTimeLength(long maxTimeLength) {
		this.maxTimeLength = maxTimeLength;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public short getAngle() {
		return angle;
	}

	public void setAngle(short angle) {
		this.angle = angle;
	}

	public short getHeigth() {
		return heigth;
	}

	public void setHeigth(short heigth) {
		this.heigth = heigth;
	}

	public short getMaxParticleEachTime() {
		return maxParticleEachTime;
	}

	public void setMaxParticleEachTime(short maxParticleEachTime) {
		this.maxParticleEachTime = maxParticleEachTime;
	}

	public long getIntervalTimeAttack() {
		return intervalTimeAttack;
	}

	public void setIntervalTimeAttack(long intervalTimeAttack) {
		this.intervalTimeAttack = intervalTimeAttack;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}
	
	public byte getMatrixType() {
		return matrixType;
	}

	public void setMatrixType(byte matrixType) {
		this.matrixType = matrixType;
	}

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
//			logger.debug("[技能检查] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+caster.getName()+"] ["+(target != null?target.getName():"-")+"] ["+ Skill.getSkillFailReason(result) +"]");
			if(logger.isDebugEnabled())
				logger.debug("[技能检查] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		
		return result;
	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {

		if(matrixType == 0 || matrixType == 1){
			int px = x;
			int py = y;
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;
//				logger.debug("[执行火墙技能攻击] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [直接进行命中计算，作用于范围内的某个目标]");
			if(logger.isDebugEnabled()){
				logger.debug("[执行技能攻击] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [火墙] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,ase.getOwner().getName()});	
			}
			
			for(int i = 0 ; i < MAX_EFFECT_NUM ; i++){
				byte[][]md = null;
				md = matrixData[0];
				if(matrixType == 0){
					px = ase.getOwner().getX() + md[i][0] * 60;
					py = ase.getOwner().getY() + md[i][1] * 60;
				}else{
					px = x + md[i][0] * 60;
					py = y + md[i][1] * 60;
				}
				if(minX > px) minX = px;
				if(minY > py) minY = py;
				if(maxX < px) maxX = px;
				if(maxY < py) maxY = py;
				
				NPCManager nm = MemoryNPCManager.getNPCManager();
				FireWallNPC fwn = (FireWallNPC)nm.createNPC(2000000);
				fwn.setOwner(ase.getOwner());
				fwn.setRange(50);
				fwn.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + 30*1000);
				fwn.setModulus(100);
				fwn.setX(px);
				fwn.setY(py);
				game.addSprite(fwn);
//					logger.debug("[执行火墙技能攻击] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+fwn.getName()+"] ["+fwn.isAlive()+"] ["+fwn.getX()+"] ["+fwn.getY()+"] [直接进行命中计算，作用于范围内的某个目标]");
				if(logger.isDebugEnabled()){
					logger.debug("[执行技能攻击] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [{}] [{}] [{}] [火墙] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,ase.getOwner().getName(),fwn.getName(),fwn.isAlive(),fwn.getX(),fwn.getY()});
				}
			}

		}else if(matrixType == 2 || matrixType == 3 || matrixType == 4){
			int px = x;
			int py = y;
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			int maxY = Integer.MIN_VALUE;
//				logger.debug("[执行火墙技能攻击] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] [直接进行命中计算，作用于范围内的某个目标]");
			if(logger.isDebugEnabled()){
				logger.debug("[执行技能攻击] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [暴风雪] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,ase.getOwner().getName()});
			}
			byte[][]md = null;
			md = matrixData[0];
			if(matrixType == 2){
				px = ase.getOwner().getX();
				py = ase.getOwner().getY();
			}else{
				px = x;
				py = y;
			}
			if(minX > px) minX = px;
			if(minY > py) minY = py;
			if(maxX < px) maxX = px;
			if(maxY < py) maxY = py;
			
			NPCManager nm = MemoryNPCManager.getNPCManager();
			BlizzardNPC fwn = (BlizzardNPC)nm.createNPC(3000000);
			fwn.setOwner(ase.getOwner());
			fwn.attackWidth = gapWidth;
			fwn.attackHeight = gapHeight;
			fwn.可以对玩家造成伤害 = 可以对玩家造成伤害;
			fwn.时间系数 = calDamageTime;
			fwn.setInValidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + maxTimeLength);
			fwn.startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			fwn.setModulus(100);
			fwn.setX(px);
			fwn.setY(py);
			if(buffName != null && buffLevel != null && buffLevel.length > 0 && buffLevel.length >= level && buffProbability != null && buffProbability.length > 0 && buffProbability.length >= level && buffLastingTime != null && buffLastingTime.length > 0 && buffLastingTime.length >= level){
				fwn.buffName = buffName;
				int index = 0;
				if(level > 0){
					index = level - 1;
				}
				fwn.buffLastingTime = buffLastingTime[index];
				fwn.buffLevel = buffLevel[index];
				fwn.buffProbability = buffProbability[index];
			}
			
			fwn.ownerX = ase.getX();
			fwn.ownerY = ase.getY();
			fwn.intervalTimeAttack = intervalTimeAttack;
			fwn.skillId = id;
			fwn.skillLevel = (byte)level;
			fwn.damage = this.attackDamages[level - 1];
			if(id == 902 && ase.getOwner() instanceof Player){
				int addPoint = SkEnhanceManager.getInst().getLayerAddPoint((Player) ase.getOwner(), 5);
				fwn.damage += addPoint;
				if(addPoint>0){
					logger.debug("仙心902坠天增加每秒造成额外%d伤害",addPoint);
				}
			}
			
			if(id == 903 && ase.getOwner() instanceof Player){
				int addPoint = SkEnhanceManager.getInst().getLayerAddPoint((Player) ase.getOwner(), 5);
				fwn.damage += addPoint;
				logger.debug("血祭增加伤害 {}",addPoint);
				long addT = SkEnhanceManager.getInst().getBuffAddTime((Player) ase.getOwner(), this) * 1000;
				if(addT>0){
					fwn.setInValidTime(fwn.getInValidTime() + addT);
					logger.debug("903血祭人阶10重:获得血祭技能持续时间%d秒",addT/1000);// 2,3,5 s
				}
			}
			
			if(id == 9113 && ase.getOwner() instanceof Player){
				int addPoint = SkEnhanceManager.getInst().getLayerAddPoint((Player) ase.getOwner(), 5);
				fwn.damage += addPoint;
				logger.debug("9113 炼魂 增加伤害 {}", addPoint);
			}
			
			game.addSprite(fwn);
//					logger.debug("[执行火墙技能攻击] [SkillWithoutTraceAndWithMatrix] ["+this.getName()+"] [Lv:"+level+"] ["+ase.getOwner().getName()+"] ["+fwn.getName()+"] ["+fwn.isAlive()+"] ["+fwn.getX()+"] ["+fwn.getY()+"] [直接进行命中计算，作用于范围内的某个目标]");
			if(logger.isDebugEnabled()){
				logger.debug("[执行技能攻击] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [{}] [{}] [{}] [暴风雪] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,ase.getOwner().getName(),fwn.getName(),fwn.isAlive(),fwn.getX(),fwn.getY()});
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

	@Override
		public SkillWithoutTraceAndWithSummonNPC clone() {
			try {
				return (SkillWithoutTraceAndWithSummonNPC) super.clone();
			} catch (CloneNotSupportedException e) {
				Skill.logger.error("克隆失败:",e);
				return null;
			}
		}

}
