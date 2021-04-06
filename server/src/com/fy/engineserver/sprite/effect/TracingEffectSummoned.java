package com.fy.engineserver.sprite.effect;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Polygon;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;


/**
 * 飞行路线为最终效果的Summoned
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class TracingEffectSummoned extends EffectSummoned {

	protected int speed;
	protected int effectIndex;
	protected int maxDistance;
	protected int attackWidth;
	protected int attackHeight;
	protected int explosionLastingTime;
	protected Fighter attackTarget;

	protected long deadline = 0;

	protected int sqrtD = 0;

	protected boolean first = true;
	ActiveSkillEntity skillEntity;

	protected Polygon attackRagePoly = null;

	private int targetX = -1;
	private int targetY = -1;
	public Polygon getAttackRangePoly() {
		return attackRagePoly;
	}

	public TracingEffectSummoned(ActiveSkillEntity ase,int effectIndex, int x, int y, String type,String avataRace,String avataSex,
			int speed, int maxDistance, int attackWidth,
			int attackHeight, int explosionLastingTime,Fighter attackTarget) {
		this.id = nextEffectSummonedId();
		this.skillEntity = ase;
		this.effectIndex = effectIndex;
		this.x = x;
		this.y = y;
		this.effectType = type;
		this.avataRace =avataRace;
		this.avataSex = avataSex;

		this.speed = speed;
		this.maxDistance = maxDistance;
		this.attackWidth = attackWidth;
		this.attackHeight = attackHeight;
		this.explosionLastingTime = explosionLastingTime;
		this.attackTarget = attackTarget;

		deadline = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + maxDistance * 1000 / speed;
//		ResourceManager.getInstance().getAvata(this);
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		if(state != STATE_EXPLODE){
			if (game.contains((LivingObject)attackTarget) && (attackTarget.getX() != targetX || attackTarget.getY() != targetY)) {
				
				if(this.getMoveTrace() != null){
					this.getMoveTrace().moveFollowPath(heartBeatStartTime);
					if(this.getMoveTrace() != null){
						maxDistance = maxDistance - this.getMoveTrace().getCurrentLength(heartBeatStartTime);
					}
				}
				
				targetX = (int)attackTarget.getX();
				targetY = (int)attackTarget.getY();
				MoveTrace path = calculatePath(game);
				if (path != null) {
					this.setMoveTrace(path);
					deadline = path.getDestineTimestamp();
					state = STATE_MOVE;
				}
			}else if(game.contains((LivingObject)attackTarget) == false){
				deadline = heartBeatStartTime;
			}
		}
		
		if (maxDistance <= 0 || heartBeatStartTime >= deadline) {
			this.setAlive(false);
			game.removeSummoned(this);
			return;
		}
		
		
		
		if (state == STATE_MOVE) {
			boolean targetAlive = true;
			if(attackTarget instanceof Player){
				Player p = (Player)attackTarget;
				if(!p.isAlive() || p.getState() == Player.STATE_DEAD)
					targetAlive = false;
			}else if(attackTarget instanceof Sprite){
				Sprite p = (Sprite)attackTarget;
				if(!p.isAlive() || p.getState() == Player.STATE_DEAD)
					targetAlive = false;
			}
			
			int dx = targetX - getX();
			int dy = targetY - getY();
			int sqrtD = (int) Math.sqrt(dx * dx + dy * dy);
			if(sqrtD == 0 && targetAlive){
				hitTarget(attackTarget);
				return;
			}
			
			if(sqrtD == 0) return;
			
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
			attackRagePoly = new Polygon(ps);
			
			if (targetAlive && game.contains((LivingObject)attackTarget) && Graphics2DUtil.isPointInConvexPolygon(attackRagePoly,
					attackTarget.getX(), attackTarget.getY(), true)) {
				hitTarget(attackTarget);
			}
		}
		
		super.heartbeat(heartBeatStartTime, interval, game);
	}

	/**
	 * 计算路径
	 * 
	 * @param g
	 * @return
	 */
	public MoveTrace calculatePath(Game g) {

		int dx = targetX - getX();
		int dy = targetY - getY();
		int sqrtD = (int) Graphics2DUtil.sqrt(dx * dx + dy * dy);
		if(sqrtD == 0) return null;
		
		Point s = new Point(x, y);
		Point e = new Point(x + dx * this.maxDistance / sqrtD,
				y + dy * this.maxDistance / sqrtD);


//		Polygon polys[] =  g.getGameInfo().navigator.getPolygons();
//		for (int i = 0; i < polys.length; i++) {
//				for (int j = 0; j < polys[i].edges.length; j++) {
//					Point p = Graphics2DUtil.intersectLineForPolygon(s, e,
//							polys[i].edges[j].s, polys[i].edges[j].e, 4);
//					if (p != null) {
//						if(Math.abs(s.x-p.x) + Math.abs(s.y-p.y) < 4){
//							if(Graphics2DUtil.leftRightMeasure(polys[i].edges[j].s, polys[i].edges[j].e, e.x, e.y) > 0){
//								e = p;
//							}
//						}else{
//							e = p;
//						}
//					}
//				}
//			
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
	 * @return
	 */
	public boolean hitTarget(Fighter t) {
		removeMoveTrace();
		x = t.getX();
		y = t.getY();
		state = STATE_EXPLODE;
		deadline = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + explosionLastingTime;
		skillEntity.hitTarget(t, effectIndex);
		return false;
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

	public int[] getScheme() {
		return new int[32];
	}
}
