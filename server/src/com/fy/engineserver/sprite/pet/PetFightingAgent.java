package com.fy.engineserver.sprite.pet;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.sprite.Fighter;

/**
 * 宠物 自动战斗代理
 * 
 * 此自动战斗代理包含如下内容：
 * 
 * 激活处理，包括记录激活点，激活点为怪开始攻击某个对象
 * 追击范围，是以激活点为原点的某个区域，当攻击对象不存在，或者攻击对象死亡，或者攻击对象不再追击范围内时，停止攻击，并清理仇恨列表。
 * 
 * 
 * 
 * 
 * 
 *
 */
public class PetFightingAgent {

	static java.util.Random random = new java.util.Random();
	
	public PetFightingAgent(Pet f){
		fighter = f;
	}
	
	//激活点
	Point activePoint;
	
	/**
	 * 当前技能
	 */
	ActiveSkill skill;
	
	/**
	 * 当前的敌人，可以是玩家，也可以是怪
	 */
	Fighter target;
	
	/**
	 * 攻击者
	 */
	Pet fighter;
	
	int level = 0;
	
	protected int pathFindFailTimes = 0;

	private long holdingTime = -1;

	
	public ActiveSkill getSkill() {
		return skill;
	}

	public void setSkill(ActiveSkill skill) {
		this.skill = skill;
	}

	public Fighter getTarget() {
		return target;
	}

	public Pet getFighter() {
		return fighter;
	}

	public void start(ActiveSkill skill,int level,Fighter target){
		this.skill = skill;
		this.target = target;
//		if(target != null)
//			lastHP = target.getHp();
//		this.sameHPCount = 0;
//		hitCount = 0;
		this.level = level;
		fighter.stopAndNotifyOthers();
		activePoint = new Point(fighter.getX(),fighter.getY());
		pathFindFailTimes = 0;
		
	}
	
	public boolean isFighting(){
		if(skill != null && target != null){
			return true;
		}
		return false;
	}
	
	public void cancel(){
		skill = null;
		target = null;
		pathFindFailTimes = 0;
//		this.lastHP = -1;
//		this.sameHPCount = 0;
//		hitCount = 0;
	}
	/**
	 * 自动战斗的心跳
	 * 
	 * 如果目标脱离了追击的范围
	 * @param g
	 */
	public void heartbeat(long heartBeatStartTime,Game g) {
		boolean targetDisappear = false;
		
		if( target != null && !g.contains((LivingObject)target)){
			targetDisappear = true;
		}
	
		if( target != null && target.isDeath()){
			targetDisappear = true;
		}
	
		
		if( target != null && fighter != null && (fighter.getX() < activePoint.x - fighter.pursueWidth/2 || fighter.getX() > activePoint.x + fighter.pursueWidth/2
				|| fighter.getY() < activePoint.y - fighter.pursueHeight/2 || fighter.getY() > activePoint.y + fighter.pursueHeight/2)){
			targetDisappear = true;
		}
		
		if(pathFindFailTimes > 5){
			targetDisappear = true;
		}
		
		if(targetDisappear){
			fighter.targetDisappear(target);
			target = null;
			pathFindFailTimes = 0;
		}
		
		if(skill == null || target == null){
			return;
		}
		
		if(this.fighter.isStun() == false){
			fight(heartBeatStartTime,g);
		}
		
	}
	
	/**
	 * 处理伤害反馈事件。当某个精灵（玩家、怪物等）受到攻击并造成伤害，<br>
	 * 该精灵会调用攻击者的这个方法，通知攻击者，它的攻击对其他精灵造成了伤害
	 * 
	 * @param target
	 *            受到伤害的目标精灵
	 * @param damage
	 *            真实伤害值
	 */
	protected void damageFeedback(Fighter target, int damage,int damageType){
//		if(target == this.target){
//			hitCount = 0;
//			sameTargetPathFindingTimes = 0;
//		}
	}
	
	
	/**
	 * 攻击敌人
	 * 
	 * 需要分清楚几种情况：
	 * 1. 还没有攻击敌人
	 * 2. 正在走过去准备攻击敌人
	 * 3. 正在攻击敌人
	 * 4. 技能处于冷却时间
	 */
	protected void fight(long heartBeatStartTime,Game g){
		if(heartBeatStartTime < holdingTime) return;
		
		ActiveSkillAgent agent = fighter.skillAgent;
		
		if(agent.getExecuting() == null && agent.getWaitting() == null
				&& !agent.isDuringCoolDown(skill.getId())){
			//还没有攻击敌人
			
			target.notifyPrepareToBeFighted(this.fighter);
			
			int result = skill.check(fighter, target, 0);
			
			//Game.logger.debug("[宠物战斗] [target:"+target.getName()+"] [result:"+result+"]");
			
			if(result == ActiveSkill.TARGET_TOO_FAR){
				if(((LivingObject)fighter).getMoveTrace() == null && fighter.isHold() == false && fighter.isStun() == false){
					pathFinding(g,target.getX(),target.getY());
					holdingTime = heartBeatStartTime + 1000;
				}
			}else if(result == 0){
				//if (hitCount < 5) {
					//Game.logger.debug("[宠物使用技能] [target:"+target.getName()+"] [skill:"+skill.getName()+"] [result:"+result+"] ["+skill+"]");
					agent.usingSkill(skill,level,target,(int) target.getX(),(int) target.getY(), new byte[]{target.getClassType()}, new long[]{target.getId()}, fighter.getDirection());
				//	hitCount++;
//				} else if (sameTargetPathFindingTimes < 3) {
//					++sameTargetPathFindingTimes;
//					hitCount = 0;
//					if (fighter.getMoveTrace() == null) {
//						pathFinding(g, target.getX() + (random.nextInt() % 100), target.getY()
//								+ (random.nextInt() % 100));
//						holdingTime = heartBeatStartTime + 1000;
//					}
//				} else {
//					fighter.targetDisappear(target);
//					target = null;
//					lastHP = -1;
//					sameHPCount = 0;
//					sameTargetPathFindingTimes = 0;
//				}
			}
		}
		
		
	}
	
	protected void pathFinding(Game de,int dx,int dy){
		int distance = 20;
		Point s = new Point(fighter.getX(),fighter.getY());
		Point e = new Point(dx,dy);
		int L = Graphics2DUtil.distance(s, e);
		if(L > distance && de.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
			int L1 = L - distance;
			int L2 = distance;
			
			Point ps[] = new Point[2];
			short roadLen[] = new short[1];
			ps[0] = new Point(s.x,s.y);
			ps[1] = new Point((L1 * e.x + L2 * s.x)/L,(L1 * e.y + L2 * s.y)/L);
			roadLen[0] =(short)  L1;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L1*1000/fighter.getSpeed() + 2000L));
			fighter.setMoveTrace(path);
		}else if(L > distance ){
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if(sps == null || sps.length == 0) {
				pathFindFailTimes ++;
				return;
			}
			Point ps[] = new Point[sps.length+1];
			short roadLen[] = new short[sps.length];
			for(int i = 0 ; i < ps.length ; i++){
				if(i == 0){
					ps[i] = new Point(s.x,s.y);
				}else{
					ps[i] = new Point(sps[i-1].x,sps[i-1].y);
				}
			}
			int totalLen = 0;
			for(int i = 0 ; i < roadLen.length ; i++){
				if(i == 0){
					roadLen[i] =(short) Graphics2DUtil.distance(ps[0], ps[1]);
				}else{
					Road r =  de.getGameInfo().navigator.getRoadBySignPost(sps[i-1].id, sps[i].id);
					if(r != null){
						roadLen[i] = r.len;
					}else{
						roadLen[i] = (short)Graphics2DUtil.distance(ps[i], ps[i+1]);
					}
				}
				totalLen+= roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/fighter.getSpeed() + 2000L));
			fighter.setMoveTrace(path);
		}
	}
}
