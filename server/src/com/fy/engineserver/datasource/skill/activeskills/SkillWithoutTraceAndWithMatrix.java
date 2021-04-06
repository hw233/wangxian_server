package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Polygon;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.datasource.skill2.GenericBuffCalc;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

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
public class SkillWithoutTraceAndWithMatrix extends ActiveSkill implements Cloneable{

//	static Logger logger = Logger.getLogger(SkillWithoutTraceAndWithMatrix.class);
public	static Logger logger = LoggerFactory.getLogger(Skill.class);
	
	public static final int MAX_EFFECT_NUM = 9;
	public static final byte[][][] matrixData = new byte[][][]{
	//      1      2      3      4     5      6     7     8       9     10     11     12   13      14    15    16      17      18     19    20     21    22    23     24       25      26    27    28     29     30    31     32
	//正方形排列
		{ {0,0},{-1,-1},{1,1},{1,-1},{-1,1},{-1,0},{1,0},{0,1},{0,-1}},
	//菱形排列	
		{{0,0} ,{-2,0} ,{2,0},{0,-2},{0,2},{-1,-1},{1,1},{1,-1},{-1,1}},
	};
	/**
	 * 后效分布的类型：
	 * 	0 以自身为中心的正方形排列
	 * 	1 以自身为中心的菱形排列
	 *  2 以目标为中心的正方形排列
	 *  3 以目标为中心的菱形排列
	 *  4 以自身为起点，正前方为一条直线的排列，gapWidth为排列间隔，gapHeight为直线宽度
	 *  5 以自身为中心的十字形排列，技能后效固定为9个
	 *  6 以目标为中心的十字形排列，技能后效固定为9个
	 *  7 以自身为中心的环形，技能后效固定为12，gapWidth为外环半径，gapHeight为内环半径
	 *  8 以目标为中心的环形，技能后效固定为12，gapWidth为外环半径，gapHeight为内环半径
	 *  9 以自身为中心的火墙
	 *  10 以坐标点为中心的火墙
	 *  11 以自身为中心的圆
	 *  12 以坐标点为中心的圆
	 */
	byte matrixType = 0;
	
	/**
	 * 后效排列之间的间隔,
	 */
	int gapWidth = 24;
	
	/**
	 * 范围的高度，默认为320,当：matrixType类型为11或12即技能形式为圆的时候，这个值代表圆的半径
	 */
	int gapHeight = 320;
	
	/**
	 × 后效的个数，越多后效排列的约紧密
	 */
	int effectNum = 32;
	
	/**
	 * 后效的类型，比如闪电，落雷等
	 */
	String effectType = "";
	
	String avataRace = "";
	
	String avataSex = "";
	
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
	 * 攻击最大个数
	 */
	private short[] maxAttackNums = new short[0];
	
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
	
	public byte getMatrixType() {
		return matrixType;
	}

	public void setMatrixType(byte matrixType) {
		this.matrixType = matrixType;
	}

	public int getGapWidth() {
		return gapWidth;
	}

	public void setGapWidth(int w) {
		this.gapWidth = w;
	}

	public int getGapHeight() {
		return gapHeight;
	}

	public void setGapHeight(int gapHeight) {
		this.gapHeight = gapHeight;
	}

	public int getEffectNum() {
		return effectNum;
	}

	public void setEffectNum(int effectNum) {
		this.effectNum = effectNum;
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

	public String getEffectType() {
		return effectType;
	}

	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}

	public short[] getMaxAttackNums() {
		return maxAttackNums;
	}

	public void setMaxAttackNums(short[] maxAttackNums) {
		this.maxAttackNums = maxAttackNums;
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
		
		if(this.matrixType == 2 || this.matrixType == 3 ||
				this.matrixType == 6 || this.matrixType == 8){
			if(target == null){
				result |= TARGET_NOT_EXIST;
			}else{
				int dx = caster.getX() - target.getX();
				int dy = caster.getY() - target.getY();
				if (dx * dx + dy * dy > range * range) {
					result |= TARGET_TOO_FAR;
				}
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
			logger.debug("[技能检查] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		
		return result;
	}

	public void run(ActiveSkillEntity ase, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {

		Fighter m_target = target;
		Fighter[] los = new Fighter[0];
		//los = game.getVisbleFighter((LivingObject)ase.getOwner(), false);
		byte[] targetTypes = ase.targetTypes;
		long[] targetIds = ase.targetIds;
		if(targetTypes != null && targetIds != null){
			los = new Fighter[targetTypes.length];
			GameManager gm = GameManager.getInstance();
			for(int i = 0; i < targetTypes.length && i < targetIds.length; i++){
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
		
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof Fighter == false) continue;
			
			Fighter t = (Fighter)los[i];
			
			int ft = ase.getOwner().getFightingType(t);
			if(ft != Fighter.FIGHTING_TYPE_ENEMY) continue;
			if(t.isDeath()) continue;
			if(this.matrixType == 2 || this.matrixType == 3 ||
					this.matrixType == 6 || this.matrixType == 8){
				Fighter finalTarget=m_target;
				if(finalTarget==null){
					finalTarget=ase.getOwner();
				}
				if(m_target!=null){
					target=m_target;
				}
				
				logger.warn("[SkillWithoutTraceAndWithMatrix][targetIds length={}] [ Fighter name={},id={}][matrixType = {}][range={}]",new Object[]{ ase.targetIds.length, finalTarget.getName(),finalTarget.getId(),this.matrixType,range});
				if( (t.getX() - finalTarget.getX())*(t.getX() - finalTarget.getX()) +  (t.getY() - finalTarget.getY())*(t.getY() - finalTarget.getY())<= 360000){
					if(!v.contains(t)){
						v.add(t);
					}
				}
			}else{
				if( (t.getX() - ase.getOwner().getX())*(t.getX() - ase.getOwner().getX()) +  (t.getY() - ase.getOwner().getY())*(t.getY() - ase.getOwner().getY())<= 360000){
					if(!v.contains(t)){
						v.add(t);
					}
				}
			}
		}
		
		Logger log = Skill.logger;
		int  count = 0;
		if(target != null){
			if(ase.getOwner().getFightingType(target) == Fighter.FIGHTING_TYPE_ENEMY){
				if( target.isDeath() == false){
					
					ase.getOwner().notifyPrepareToFighting(target);
					target.notifyPrepareToBeFighted(ase.getOwner());
					
					ase.hitTarget(target, effectIndex);
					
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("[宠物使用群体攻击] [WithMatrix] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + this.getName() + "] [" + target.getName() + "]");
						}
						if (ase.getOwner() instanceof Pet && (!target.canFreeFromBeDamaged(ase.getOwner()))) {
							boolean isProtect = false;
							if (target instanceof Player && target.getLevel() <= PlayerManager.保护最大级别) {
								Player p = (Player) target;
								if (p.getCountry() == p.getCurrentGameCountry()) {
									isProtect = true;
								}
							}
							if (!isProtect) {
								Pet pet = (Pet) ase.getOwner();
								GenericBuffCalc.getInst().procBuff(target, pet, pet.getSkillAgent().atkTimes, logger);
							}
						}
					} catch (Exception e) {
						logger.error("[宠物群体攻击判断触发buff] [异常] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + target.getName() + "] [" + this.getName() + "]", e);
					}

					count++;
					
					if(logger.isDebugEnabled()){
						logger.debug("[执行技能攻击] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{this.getName(),level,ase.getOwner().getName(),target.getName()});
					}
				}
			}
		}
		log.debug("SkillWithoutTraceAndWithMatrix.run: size {} 发来个数 {}",v.size(),targetIds.length);
		int tgtAdd = SkEnhanceManager.getInst().getTargetAdd(id, ase.getOwner(), this);
		for(int i = 0 ; i < v.size() ; i++){
			Fighter t = v.get(i);
			if(t == null){
				continue;
			}
			if(t == target){
				continue;
			}
			if(ase.getOwner().getFightingType(t) == Fighter.FIGHTING_TYPE_ENEMY){
				if( t.isDeath() == false){
					

					ase.getOwner().notifyPrepareToFighting(t);
					t.notifyPrepareToBeFighted(ase.getOwner());
					
					ase.hitTarget(t, effectIndex);
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("[宠物使用群体攻击] [WithMatrix] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + this.getName() + "] [" + t.getName() + "]");
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
						logger.error("[宠物群体攻击判断触发buff] [异常] [" + ase.getOwner().getId() + "] [" + ase.getOwner().getName() + "] [" + t.getName() + "] [" + this.getName() + "]", e);
					}

					count++;
					
					if(logger.isDebugEnabled()){
						logger.debug("[执行技能攻击 {}] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [{}] [直接进行命中计算，作用于范围内的某个目标]", new Object[]{count,this.getName(),level,ase.getOwner().getName(),t.getName()});
					}
					short[] arr = maxAttackNums;
					if(arr != null && arr.length > 0 && arr.length >= level){
						int index = 0;
						if(level > 0){
							index = level -1;
						}
						if(logger.isDebugEnabled()){
							logger.debug("技能目标个数 {}, 额外增加 {}",arr[index], tgtAdd);
						}
						if(count >= arr[index] + tgtAdd){
							break;
						}
					}
				}
			}
		}
		
		if(count == 0){
			if(logger.isDebugEnabled()){
				logger.debug("[执行技能失败] [SkillWithoutTraceAndWithMatrix] [{}] [Lv:{}] [{}] [----] [正方形范围内没有任何可以攻击的目标]", new Object[]{this.getName(),level,ase.getOwner().getName()});
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
	public SkillWithoutTraceAndWithMatrix clone(){
			try {
				return (SkillWithoutTraceAndWithMatrix) super.clone();
			} catch (CloneNotSupportedException e) {
				Skill.logger.error("克隆出错B",e);
			}
			return null;
		}

}
