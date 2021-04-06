package com.fy.engineserver.sprite.monster;

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
 * 怪物 自动战斗代理
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
public class BossFightingAgent {

	static java.util.Random random = new java.util.Random();
	
	public BossFightingAgent(BossMonster f){
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
	BossMonster fighter;
	
	int level = 0;
	
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

	public Monster getFighter() {
		return fighter;
	}

	public void start(ActiveSkill skill,int level,Fighter target){
		this.skill = skill;
		this.target = target;
	
		this.level = level;
		fighter.stopAndNotifyOthers();
		activePoint = new Point(fighter.getX(),fighter.getY());
		
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
		
	}
	public void heartbeat(long heartBeatStartTime,Game g){
		this.heartbeat(heartBeatStartTime, g, (byte)0);
	}
	/**
	 * 自动战斗的心跳
	 * 
	 * 如果目标脱离了追击的范围
	 * @param g
	 */
	public void heartbeat(long heartBeatStartTime,Game g, byte locatType) {
		boolean targetDisappear = false;
		
		if( target != null && !g.contains((LivingObject)target)){
			targetDisappear = true;
		}
	
		if( target != null && target.isDeath()){
			targetDisappear = true;
		}
	
		
		if (locatType != 1) {			//翅膀副本npc怪物没有追击范围
			if( target != null && fighter != null && (fighter.getX() < activePoint.x - fighter.pursueWidth/2 || fighter.getX() > activePoint.x + fighter.pursueWidth/2
					|| fighter.getY() < activePoint.y - fighter.pursueHeight/2 || fighter.getY() > activePoint.y + fighter.pursueHeight/2)){
				targetDisappear = true;
				
				if(BossMonster.baLogger.isDebugEnabled()){
	//				BossMonster.baLogger.debug("[BOSS执行技能] [目标跑出追击范围之外，请空当前目标] [BOSS:"+fighter.getName()+"] [目标："+target.getName()+"] [范围：("+activePoint.x+","+activePoint.y+","+fighter.pursueWidth+","+fighter.pursueHeight+")]");
					if(BossMonster.baLogger.isDebugEnabled())
						BossMonster.baLogger.debug("[BOSS执行技能] [目标跑出追击范围之外，请空当前目标] [BOSS:{}] [目标：{}] [范围：({},{},{},{})]", new Object[]{fighter.getName(),target.getName(),activePoint.x,activePoint.y,fighter.pursueWidth,fighter.pursueHeight});
				}
			}
		}
		
		if(targetDisappear){
			fighter.targetDisappear(target);
			target = null;
		}
		
		if(skill == null || target == null){
			return;
		}
		
		if(this.fighter.isStun() == false && this.fighter.isSilence() == false){
				fight(heartBeatStartTime,g, locatType);
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
	protected void fight(long heartBeatStartTime,Game g, int locatType){
		if(heartBeatStartTime < holdingTime) return;
		
		ActiveSkillAgent agent = fighter.skillAgent;
		
		if(agent.getExecuting() == null && agent.getWaitting() == null
				&& !agent.isDuringCoolDown(skill.getId())){
			//还没有攻击敌人
			
			target.notifyPrepareToBeFighted(this.fighter);

			int result = skill.check(fighter, target, 0);
			if(BossMonster.baLogger.isDebugEnabled())
				BossMonster.baLogger.debug("fighter:"+fighter.getName()+" skill.getId():"+skill.getId()+" result:"+result+" (fighter.isHold()=false)"+(fighter.isHold() == false));
			if(result == ActiveSkill.TARGET_TOO_FAR){
				if(((LivingObject)fighter).getMoveTrace() == null && fighter.isHold() == false && fighter.isStun() == false){
					if(pathFinding(g,target.getX(),target.getY())){
						holdingTime = heartBeatStartTime + 1000;
					}else{
						fighter.targetDisappear(target);
						if(BossMonster.baLogger.isDebugEnabled()){
//							BossMonster.baLogger.debug("[BOSS执行技能] [目标太远，寻路失败，请空当前目标] [BOSS:"+fighter.getName()+"] [目标："+target.getName()+"] [技能："+skill.getName()+"]");
							if(BossMonster.baLogger.isDebugEnabled())
								BossMonster.baLogger.debug("[BOSS执行技能] [目标太远，寻路失败，请空当前目标] [BOSS:{}] [目标：{}] [技能：{}]", new Object[]{fighter.getName(),target.getName(),skill.getName()});
						}
						target = null;
						
						
					}
					
				}
			}else if(result == 0){
						if( g.gi.navigator.isVisiable(fighter.getX(), fighter.getY(), target.getX(), target.getY()) == false){
							holdingTime = heartBeatStartTime + 1000;
							if(fighter.isHold() == false){
								if(pathFinding(g,target.getX(),target.getY())){
									pathFinding(g,target.getX(),target.getY());
								}else{
									fighter.targetDisappear(target);
									if(BossMonster.baLogger.isDebugEnabled()){
	//									BossMonster.baLogger.debug("[BOSS执行技能] [目标不可见，寻路失败，请空当前目标] [BOSS:"+fighter.getName()+"] [目标："+target.getName()+"] [技能："+skill.getName()+"]");
										if(BossMonster.baLogger.isDebugEnabled())
											BossMonster.baLogger.debug("[BOSS执行技能] [目标不可见，寻路失败，请空当前目标] [BOSS:{}] [目标：{}] [技能：{}]", new Object[]{fighter.getName(),target.getName(),skill.getName()});
									}
									target = null;
	
									
								}
								
							}
							if(BossMonster.baLogger.isDebugEnabled())
								BossMonster.baLogger.debug("fighter:"+fighter.getName()+" skill.getId():"+skill.getId()+" 1");
						}else{
							agent.usingSkill(skill,level,target, (int)target.getX(),(int) target.getY(),new byte[]{target.getClassType()}, new long[]{target.getId()}, fighter.getDirection());
							if(BossMonster.baLogger.isDebugEnabled())
								BossMonster.baLogger.debug("fighter:"+fighter.getName()+" skill.getId():"+skill.getId()+" 2");
							//只打一下，有BOSS AI来控制
							this.cancel();
						}
					}
			
		}
	}
	
	public boolean hasMoved = false;
	
	long startFightTime = -1;
	
	public boolean pathFindingForLook(Game g, int[] points) {
		try {
			hasMoved = true;
			Point s = new Point(fighter.getX(), fighter.getY());
			Point e = new Point(points[0], points[1]);
			int L = Graphics2DUtil.distance(s, e);
			if(g.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
				Point ps[] = new Point[]{s,e};
				short roadLen[] = new short[1];
				roadLen[0] =  (short)L;
				startFightTime = (long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L*1000/fighter.getSpeed());
				MoveTrace path = new MoveTrace(roadLen,ps, startFightTime);
				fighter.setMoveTrace(path);
				return true;
			}
		} catch(Exception e) {
			
		}
		return false;
	}
	
	/**
	 * ai 
	 * @param de
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean pathFindingForFighting(Game g,Fighter target){
		
		int flag1[] = new int[BossMonster.第一组攻击点.length];
//		int flag2[] = new int[DotaSolider.第二组攻击点.length];
		
		LivingObject los[] = g.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof BossMonster){
				BossMonster d = (BossMonster)los[i];
				
				if(d != fighter && d.isDeath() == false ){
					if(d.是否有正在前往的攻击点){
						if(d.攻击点组 == 1){
							flag1[d.攻击点] = 1;
						}
//						else if(d.攻击点组 == 2){
//							flag2[d.攻击点] = 1;
//						}
					}
					
				}
			}
		}
		int groupIndex = 1;
		int nearestPoint = -1;
		int distance = Integer.MAX_VALUE;
		
		for(int i = 0 ; i < flag1.length ; i++){
			if(flag1[i] == 0){
				int d = (fighter.getX() - (target.getX() + BossMonster.第一组攻击点[i][0])) * (fighter.getX() - (target.getX() + BossMonster.第一组攻击点[i][0]));
				d += (fighter.getY() - (target.getY() + BossMonster.第一组攻击点[i][1])) * (fighter.getY() - (target.getY() + BossMonster.第一组攻击点[i][1]));
				
				if(d < distance){
					distance = d;
					nearestPoint = i;
				}
			}
		}
		
//		if(nearestPoint == -1){
//			groupIndex = 2;
//			for(int i = 0 ; i < flag2.length ; i++){
//				if(flag2[i] == 0){
//					int d = (fighter.getX() - (target.getX() + DotaSolider.第二组攻击点[i][0])) * (fighter.getX() - (target.getX() + DotaSolider.第二组攻击点[i][0]));
//					d += (fighter.getY() - (target.getY() + DotaSolider.第二组攻击点[i][1])) * (fighter.getY() - (target.getY() + DotaSolider.第二组攻击点[i][1]));
//					
//					if(d < distance){
//						distance = d;
//						nearestPoint = i;
//					}
//				}
//			}
//		}
		
		if(nearestPoint == -1){
//			fighter.targetDisappear(target);
//			fighter.attackTarget = null;
			return false;
		}
		
		fighter.是否有正在前往的攻击点 = true;
		fighter.攻击点组 = groupIndex;
		fighter.攻击点 = nearestPoint;
		
		
		int dx = 0;
		int dy = 0;
		if(fighter.攻击点组 == 1){
			dx = target.getX() + BossMonster.第一组攻击点[fighter.攻击点][0];
			dy = target.getY() + BossMonster.第一组攻击点[fighter.攻击点][1];
		}else{
			dx = target.getX() + BossMonster.第二组攻击点[fighter.攻击点][0];
			dy = target.getY() + BossMonster.第二组攻击点[fighter.攻击点][1];
		}
		
		Point s = new Point(fighter.getX(),fighter.getY());
		Point e = new Point(dx,dy);
		int L = Graphics2DUtil.distance(s, e);
		if(g.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
			Point ps[] = new Point[]{s,e};
			short roadLen[] = new short[1];
			roadLen[0] =  (short)L;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L*1000/fighter.getSpeed()));
			fighter.setMoveTrace(path);
		}else{
			SignPost sps[] = g.getGameInfo().navigator.findPath(s, e);
			if(sps == null) return false;
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
					roadLen[i] = (short)Graphics2DUtil.distance(ps[0], ps[1]);
				}else{
					Road r =  g.getGameInfo().navigator.getRoadBySignPost(sps[i-1].id, sps[i].id);
					if(r != null){
						roadLen[i] = r.len;
					}else{
						roadLen[i] = (short)Graphics2DUtil.distance(ps[i], ps[i+1]);
					}
				}
				totalLen+= roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/fighter.getSpeed() + 2000L);
			fighter.setMoveTrace(path);
		}
		return true;
	}
	
	public boolean pathFinding(Game de,int dx,int dy){
		if(fighter.getSpeed() <= 0) return true;
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
			roadLen[0] = (short) L1;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L1*1000/fighter.getSpeed()));
			fighter.setMoveTrace(path);
			
			if(BossMonster.baLogger.isDebugEnabled()){
//				BossMonster.baLogger.debug("[BOSS寻路] [目标不可见，通过A*寻路] [BOSS:"+fighter.getName()+"] [目标："+(target!=null?target.getName():"--")+"] [技能："+(skill!=null?skill.getName():"--")+"] [路径："+path.toString()+"]");
				if(BossMonster.baLogger.isDebugEnabled())
					BossMonster.baLogger.debug("[BOSS寻路] [目标不可见，通过A*寻路] [BOSS:{}] [目标：{}] [技能：{}] [路径：{}]", new Object[]{fighter.getName(),(target!=null?target.getName():"--"),(skill!=null?skill.getName():"--"),path.toString()});
			}
			
			return true;
		}else if(L > distance ){
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if(sps == null || sps.length == 0){
				return false;
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
						roadLen[i] =(short) Graphics2DUtil.distance(ps[i], ps[i+1]);
					}
				}
				totalLen+= roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/fighter.getSpeed());
			fighter.setMoveTrace(path);
			
			if(BossMonster.baLogger.isDebugEnabled()){
//				BossMonster.baLogger.debug("[BOSS寻路] [目标不可见，通过A*寻路] [BOSS:"+fighter.getName()+"] [目标："+(target!=null?target.getName():"--")+"] [技能："+(skill!=null?skill.getName():"--")+"] [路径："+path.toString()+"]");
				if(BossMonster.baLogger.isDebugEnabled())
					BossMonster.baLogger.debug("[BOSS寻路] [目标不可见，通过A*寻路] [BOSS:{}] [目标：{}] [技能：{}] [路径：{}]", new Object[]{fighter.getName(),(target!=null?target.getName():"--"),(skill!=null?skill.getName():"--"),path.toString()});
			}

			
			return true;
		}else{
			return false;
		}
		
	}
}
