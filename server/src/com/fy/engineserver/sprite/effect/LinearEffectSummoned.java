package com.fy.engineserver.sprite.effect;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Polygon;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;

/**
 * 飞行路线为直线效果的Summoned
 * 
 * 需要设置如下参数： 飞行的方向：我们用 x坐标方向分量dx 和 y坐标方向分量dy 表示。dx和dy只表示方向，并不代表速度。
 * 飞行的速度：我们用每秒多少个像素表示 飞行的最大距离：我们用多少个像素表示
 * 攻击范围：是以飞行的直线为中轴线的矩形，我们用其长和宽表示，长为平行于飞行直线的边，宽为垂直于直线的边
 * 矩形为宽一边经过此Summoned的坐标点，另一边为飞行方向的边。 攻击类型：分点打怪，点PK，面打怪，面PK 攻击对象：此参数点攻击的时候有效
 * 击中目标前：飞行的状态值(MOVE) 以及 飞行的持续时间 击中目标后：爆炸的状态值(ATTACK) 以及 爆炸持续的时间 飞行物的类型：飞行物的类型值
 * 
 * 
 * 
 * 
 */
public class LinearEffectSummoned extends EffectSummoned {

	private ActiveSkillEntity skillEntity;
	private int effectIndex;
	protected int dx, dy;
	protected int speed;
	protected int maxDistance;
	protected int attackWidth;
	protected int attackHeight;
	protected int explosionLastingTime;
	
	protected Fighter attackTarget;
	protected int penetrateNum = 0;
	
	
	protected long deadline = 0;

	protected boolean alive = true;
	protected int sqrtD = 0;

	protected boolean first = true;
	
	protected int currentPenetrateNum = 0;

	public LinearEffectSummoned(ActiveSkillEntity ase, int effectIndex, int x,
			int y, String type,String avataRace,String avataSex, int dx, int dy, int speed, int maxDistance,
			int attackWidth, int attackHeight, int explosionLastingTime,
			int penetrateNum,Fighter attackTarget) {
		this.id = nextEffectSummonedId();
		skillEntity = ase;
		this.effectIndex = effectIndex;
		this.x = x;
		this.y = y;
		this.effectType = type;
		this.avataRace = avataRace;
		this.avataSex = avataSex;

		this.dx = dx;
		this.dy = dy;
		this.speed = speed;
		this.maxDistance = maxDistance;
		this.attackWidth = attackWidth;
		this.attackHeight = attackHeight;
		this.explosionLastingTime = explosionLastingTime;

		this.attackTarget = attackTarget;
		this.penetrateNum = penetrateNum;
		
		deadline = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + maxDistance * 1000 / speed;
		sqrtD = (int) Math.sqrt(dx * dx + dy * dy);
		if(sqrtD == 0){
			sqrtD = 1;
		}
//		ResourceManager.getInstance().getAvata(this);
	}
	
	public void notifyPlayerDead(Player player) {
		if (attackTarget != null && attackTarget.getId() == player.getId()) {
			needDisappear = true;
		}
	}
	private boolean needDisappear = false;

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		if (first) {
			first = false;
			MoveTrace path = calculatePath(game);
			if (path != null) {
				this.setMoveTrace(path);
				deadline = path.getDestineTimestamp();
				state = STATE_MOVE;
			}
		}

		super.heartbeat(heartBeatStartTime, interval, game);

		if (needDisappear || com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > deadline) {
			alive = false;
			game.removeSummoned(this);
			return;
		}
		if (state == STATE_MOVE) {
			Point ps[] = new Point[4];
			ps[0] = new Point(x - attackHeight * dy / (2 * sqrtD),
					y + attackHeight * dx / (2 * sqrtD));
			ps[1] = new Point(x + attackHeight * dy / (2 * sqrtD),
					y - attackHeight * dx / (2 * sqrtD));
			ps[2] = new Point(
					x + attackWidth * dx / sqrtD + attackHeight * dy / (2 * sqrtD),
					y + attackWidth * dy / sqrtD - attackHeight * dx / (2 * sqrtD));
			ps[3] = new Point(
					x + attackWidth * dx / sqrtD - attackHeight * dy / (2 * sqrtD),
					y + attackWidth * dy / sqrtD + attackHeight * dx / (2 * sqrtD));
			Polygon poly = new Polygon(ps);
			if (attackTarget != null) {
				if(ActiveSkill.logger.isInfoEnabled())
					ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null]");
				if(attackTarget instanceof Player){
					if(ActiveSkill.logger.isInfoEnabled())
						ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Player]");
					Player p = (Player)attackTarget;
					if(p.isAlive() && p.getState() != Player.STATE_DEAD){
						if (game.contains((LivingObject)attackTarget) && Graphics2DUtil.isPointInConvexPolygon(poly,
								attackTarget.getX(), attackTarget.getY(), true)) {
							hitTarget(attackTarget);
							if(ActiveSkill.logger.isInfoEnabled())
								ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Player] [1]");
						}else{
							if(ActiveSkill.logger.isInfoEnabled())
								ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Player] [2]");
						}
					}
				}else if(attackTarget instanceof Sprite){
					if(ActiveSkill.logger.isInfoEnabled())
						ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Sprite]");
					Sprite p = (Sprite)attackTarget;
					if(p.isAlive() && p.getState() != Player.STATE_DEAD){
						if (Graphics2DUtil.isPointInConvexPolygon(poly,
								attackTarget.getX(), attackTarget.getY(), true)) {
							hitTarget(attackTarget);
							if(ActiveSkill.logger.isInfoEnabled())
								ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Sprite] [1]");
						}else{
							if(ActiveSkill.logger.isInfoEnabled())
								ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget != null] [attackTarget instanceof Sprite] [2]");
						}
					}
				}
				if (penetrateNum > 0) {
					Fighter[] los = game.getVisbleFighter((LivingObject)skillEntity.getOwner(), false);
					for (int i = 0; i < los.length; i++) {
						Fighter f = los[i];
						if(!f.isDeath() && game.contains((LivingObject)f) && skillEntity.getOwner().getFightingType(f) == Fighter.FIGHTING_TYPE_ENEMY
								&& Graphics2DUtil.isPointInConvexPolygon(
										poly, f.getX(), f.getY(), true)){
//							ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null] [1] ["+poly.maxX+"] ["+poly.maxY+"] ["+poly.minX+"] ["+poly.minY+"] [f.getX():"+f.getX()+"] [f.getY():"+f.getY()+"]");
							if (!hitTarget(f)) {
								break;
							}
						}
					}
				}
				
			} else {
				if(ActiveSkill.logger.isInfoEnabled())
					ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null]");
				Fighter[] los = game.getVisbleFighter((LivingObject)skillEntity.getOwner(), false);
				for (int i = 0; i < los.length; i++) {
					Fighter f = los[i];
					if(!f.isDeath() && game.contains((LivingObject)f) && skillEntity.getOwner().getFightingType(f) == Fighter.FIGHTING_TYPE_ENEMY
							&& Graphics2DUtil.isPointInConvexPolygon(
									poly, f.getX(), f.getY(), true)){
//						ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null] [1] ["+poly.maxX+"] ["+poly.maxY+"] ["+poly.minX+"] ["+poly.minY+"] [f.getX():"+f.getX()+"] [f.getY():"+f.getY()+"]");
						if(ActiveSkill.logger.isInfoEnabled())
							ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null] [1] [{}] [{}] [{}] [{}] [f.getX():{}] [f.getY():{}]", new Object[]{poly.maxX,poly.maxY,poly.minX,poly.minY,f.getX(),f.getY()});
						if (!hitTarget(f)) {
							if(ActiveSkill.logger.isInfoEnabled())
								ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null] [2]");
							break;
						}
					}else{
						boolean test = Graphics2DUtil.isPointInConvexPolygon(poly, f.getX(), f.getY(), true);
//						ActiveSkill.logger.info("[LinearEffectSummoned] [attackTarget == null] [3] ["+poly.maxX+"] ["+poly.maxY+"] ["+poly.minX+"] ["+poly.minY+"] [f.getX():"+f.getX()+"] [f.getY():"+f.getY()+"] ["+test+"]");
					}
				}
				
			}
		}
	}


	/**
	 * 计算路径
	 * 
	 * @param g
	 * @return
	 */
	public MoveTrace calculatePath(Game g) {

		Point s = new Point(x, y);
		Point e = new Point(x + dx * this.maxDistance / sqrtD,
				y + dy * this.maxDistance / sqrtD);
		
//		int x1 = Math.min(s.x, e.x);
//		int x2 = Math.max(s.x, e.x);
//		int y1 = Math.min(s.y, e.y);
//		int y2 = Math.max(s.y, e.y);
//
//		Polygon polys[] = g.getGameInfo().navigator.getPolygons();
//		for (int i = 0; i < polys.length; i++) {
//			if (Graphics2DUtil.intersectRect(x1, y1, x2, y2, polys[i].minX,
//					polys[i].minY, polys[i].maxX, polys[i].maxY)) {
//				for (int j = 0; j < polys[i].edges.length; j++) {
//					Point p = Graphics2DUtil.intersectLineForPolygon(s, e,
//							polys[i].edges[j].s, polys[i].edges[j].e, 4);
//					if (p != null) {
//						e = p;
//						x1 = Math.min(s.x, e.x);
//						x2 = Math.max(s.x, e.x);
//						y1 = Math.min(s.y, e.y);
//						y2 = Math.max(s.y, e.y);
//					}
//				}
//			}
//		}
		Point points[] = new Point[2];
		points[0] = new Point(s.x, s.y);
		points[1] = new Point(e.x, e.y);
		short roadLen[] = new short[1];
		roadLen[0] = (short) Graphics2DUtil.distance(s, e);
		MoveTrace path = new MoveTrace(roadLen, points,
				(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + roadLen[0] * 1000 / speed));
		return path;
	}

	/**
	 * 击中目标，返回true表示继续攻击其他目标，返回false表示不再攻击其他目标
	 * 
	 * 默认实现为，清空 path ，设置当前坐标到击中目标位置，并且设置爆炸状态以及爆炸持续的时间
	 * 
	 * @param t
	 *            被击中的目标
	 * @return
	 */
	public boolean hitTarget(Fighter t) {
		currentPenetrateNum++;
		if(currentPenetrateNum > this.penetrateNum){
			removeMoveTrace();
			x = t.getX();
			y = t.getY();
			state = STATE_EXPLODE;
			deadline = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + explosionLastingTime;
			
			skillEntity.getOwner().notifyPrepareToFighting(t);
			t.notifyPrepareToBeFighted(skillEntity.getOwner());
			
			skillEntity.hitTarget(t, effectIndex);
			

			
			return false;
		}else{
			
			skillEntity.getOwner().notifyPrepareToFighting(t);
			t.notifyPrepareToBeFighted(skillEntity.getOwner());
			
			skillEntity.hitTarget(t, effectIndex);
			

			
			return true;
		}
	}

	/**
	 * 判断此效果是否还活着
	 */
	public boolean isAlive() {
		return alive;

	}

	public ActiveSkillEntity getSkillEntity() {
		return skillEntity;
	}

	public void setSkillEntity(ActiveSkillEntity skillEntity) {
		this.skillEntity = skillEntity;
	}

	public int getEffectIndex() {
		return effectIndex;
	}

	public void setEffectIndex(int effectIndex) {
		this.effectIndex = effectIndex;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
