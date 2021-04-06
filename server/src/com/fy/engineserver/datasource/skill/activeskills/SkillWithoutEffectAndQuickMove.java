package com.fy.engineserver.datasource.skill.activeskills;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.ParticleData;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_PLAY_PARTICLE_RES;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;


public class SkillWithoutEffectAndQuickMove extends ActiveSkill {

//	static Logger logger = Logger.getLogger(SkillWithoutEffectAndQuickMove.class);
public	static Logger logger = LoggerFactory.getLogger(SkillWithoutEffectAndQuickMove.class);
	/**
	 * 攻击的有效距离
	 */
	private int range;
	
	/**
	 * 路径类型，0表示直线，1表示寻路出来的路径
	 */
	protected int traceType = 0;

	/**
	 * 距离目标点多远停下来
	 */
	protected int distance = 20;
	
	/**
	 * 消耗的法力值，跟等级相关
	 */
	private short[] mp = new short[0];
	
	/**
	 * 0为冲锋，1为闪现
	 */
	private byte skillPlayType;
	
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
		if(skillPlayType == 0 || skillPlayType == 2){		
			if(target == null){
				result |= TARGET_NOT_EXIST;
			}else{
				int dx = caster.getX() - target.getX();
				int dy = caster.getY() - target.getY();
				if (dx * dx + dy * dy > getRange(level) * getRange(level)) {
					result |= TARGET_TOO_FAR;
				}
			}
		}

		
		if(this.getEnableWeaponType() == 1 && caster instanceof Player){
			Player p = (Player)caster;
			if( p.getWeaponType() != this.getWeaponTypeLimit()){
				result |= WEAPON_NOT_MATCH;
			}
		}
		
		if(target != null && caster.getFightingType(target) != Fighter.FIGHTING_TYPE_ENEMY){
			result |= TARGET_NOT_MATCH;
		}
		
		if(target != null && target.isDeath()){
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
		
		if(logger.isDebugEnabled()){
			logger.debug("[技能检查] [SkillWithoutEffectAndQuickMove] [{}] [Lv:{}] [{}] [{}] [{}]", new Object[]{this.getName(),level,caster.getName(),(target != null?target.getName():"-"),Skill.getSkillFailReason(result)});
		}
		
		return result;
		
	
	}
	
	public static String pratice = "修罗/震魂身上";

	/**
	 * 僵直阶段结束，攻击阶段开始
	 * 当僵直阶段结束，攻击阶段开始的时候，系统会调用此方法
	 */ 
	public void duration2Start(ActiveSkillEntity ase,Game de){
		
		super.duration2Start(ase, de);
		if(skillPlayType == 0){
			long t = ase.getStartTime() + this.getDuration1() + getEffectiveTime(0);
			
			Point s = new Point(ase.getOwner().getX(),ase.getOwner().getY());
			Point e = new Point(ase.getTarget().getX(),ase.getTarget().getY());
			int L = Graphics2DUtil.distance(s, e);
			if(L > distance){
				short pointX[] = new short[2];
				short pointY[] = new short[2];
				short roadLen[] = new short[1];
				pointX[0] = (short) s.x;
				pointY[0] = (short) s.y;
				
				int L1 = L - distance;
				int L2 = distance;
				pointX[1] = (short)((L1 * e.x + L2 * s.x)/L);
				pointY[1] = (short)((L1 * e.y + L2 * s.y)/L);
				
				roadLen[0] = (short) L1;
				MoveTrace path = new MoveTrace(roadLen, new Point[]{new Point(pointX[0],pointY[0]),new Point(pointX[1],pointY[1])},t);
				
				((LivingObject)ase.getOwner()).setMoveTrace(path);
			}
		} else if (skillPlayType == 2) {			//僵直阶段播放施放技能前粒子
			if (ase.getOwner() instanceof Player) {
				ParticleData[] particleDatas = new ParticleData[1];
				particleDatas[0] = new ParticleData(ase.getOwner().getId(), pratice, -1, 0, 0, 0);
				NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
				((Player) ase.getOwner()).addMessageToRightBag(resp);
			}
		}
	
	}
	
	public void run(ActiveSkillEntity caster, Fighter target, Game game,
			int level, int effectIndex, int x, int y, byte direction) {
		Fighter f = caster.getOwner();
		if(skillPlayType == 0){
			hit(f,target,level,effectIndex);
		} else if (skillPlayType == 2) {
			if (f instanceof Player) {
				changePlayerPosition((Player) f, target);
			}
		} else{
			fireBuff(f, target, level, effectIndex);
//			if(f instanceof Player){
//				game.transferGame((Player)f, new TransportData(0, 0, 0, 0, game.gi.getName(), x, y));
//			}
		}
		
		if(logger.isDebugEnabled()){
			if(skillPlayType == 0){
				if(logger.isDebugEnabled())
					logger.debug("[执行技能攻击] [SkillWithoutEffectAndQuickMove] [{}] [Lv:{}] [{}] [{}] [直接调用命中计算]", new Object[]{this.getName(),level,caster.getOwner().getName(),target.getName()});
			}
		}
	}
	
	public void playPraticle(Player player) {
		ParticleData[] particleDatas = new ParticleData[1];
		particleDatas[0] = new ParticleData(player.getId(), pratice, -1, 0, 0, 0);
		NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
		player.addMessageToRightBag(resp);
		if (player.getCurrentGame() != null) {
			for (LivingObject lo : player.getCurrentGame().getLivingObjects()) {
				if (lo instanceof Player) {
					if (lo.getId() != player.getId() && rangeValid(player, lo.getX(), lo.getY())) {
						((Player)lo).addMessageToRightBag(resp);
					}
				}
			}
		}
	}
	
	private boolean rangeValid(Player player,int x,int y){
		boolean valid = false;
		if(player.getX() - CoreSubSystem.DEFAULT_PLAYER_VIEWWIDTH <= x &&player.getX() + CoreSubSystem.DEFAULT_PLAYER_VIEWWIDTH >= x && player.getY() - CoreSubSystem.DEFAULT_PLAYER_VIEWHEIGHT <= y && player.getY() + CoreSubSystem.DEFAULT_PLAYER_VIEWHEIGHT >= y){
			valid = true;
		}
		return valid;
	}
	
	
	
	/**
	 * 闪现到目标周围
	 * @param player
	 * @param target
	 */
	public void changePlayerPosition(Player player, Fighter target) {
		if (player == null || target == null) {
			return ;
		}
		SET_POSITION_REQ  req = new SET_POSITION_REQ(GameMessageFactory.nextSequnceNum(), (byte)Game.REASON_CLIENT_STOP,
				player.getClassType(), player.getId(), (short)target.getX(), (short)target.getY());
		player.addMessageToRightBag(req);
		player.setX(target.getX());
		player.setY(target.getY());
		
//		long t = ase.getStartTime() + this.getDuration1() + getEffectiveTime(0);
		
		Point s = new Point(player.getX(),player.getY());
		Point e = new Point(player.getX() + 30,player.getY());
		int L = Graphics2DUtil.distance(s, e);
		if(L > 20){
			short pointX[] = new short[2];
			short pointY[] = new short[2];
			short roadLen[] = new short[1];
			pointX[0] = (short) s.x;
			pointY[0] = (short) s.y;
			
			int L1 = L - 20;
			int L2 = 20;
			pointX[1] = (short)((L1 * e.x + L2 * s.x)/L);
			pointY[1] = (short)((L1 * e.y + L2 * s.y)/L);
			
			roadLen[0] = (short) L1;
			MoveTrace path = new MoveTrace(roadLen, new Point[]{new Point(pointX[0],pointY[0]),new Point(pointX[1],pointY[1])},50);
			
			player.setMoveTrace(path);
		}
		
		playPraticle(player);
		
		
	}

	public int getTraceType() {
		return traceType;
	}

	public void setTraceType(int traceType) {
		this.traceType = traceType;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public short[] getMp() {
		return mp;
	}

	public void setMp(short[] mp) {
		this.mp = mp;
	}
	
	public int getRange(int level) {
		if (this.getId() != 306) {
			return this.getRange();
		}
		if (level <= 0) {
			return range;
		} else {
			return range + (level-1)*10;
		} /*else if (level <= 8) {
			return range + 40 + (level-5)*20;
		} else {
			return range + 100 + (level - 8)*10;
		}*/
		
	}
	
	/**
	 * 需要根据技能等级计算距离
	 * */
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public byte getSkillPlayType() {
		return skillPlayType;
	}

	public void setSkillPlayType(byte skillPlayType) {
		this.skillPlayType = skillPlayType;
	}

	
}
