package com.fy.engineserver.sprite.effect;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Polygon;
import com.fy.engineserver.datasource.skill.ActiveSkillEntity;
import com.fy.engineserver.sprite.EffectSummoned;
import com.fy.engineserver.sprite.Fighter;

/**
 * 
 * 
 * 
 * 
 */
public class PositionEffectSummoned extends EffectSummoned {
	private ActiveSkillEntity skillEntity;
	private int effectIndex;
	protected int lastingTime;
	protected int explosionLastingTime;
	protected Fighter attackTarget;

	protected long deadline = 0;
	protected long starttime = 0;
	protected boolean alive = true;

	private boolean callHitTarget = false;
	
	public Polygon getAttackRangePoly() {
		return null;
	}

	/**
	 * 构造函数
	 * 
	 * @param ase
	 * @param attackTarget
	 *            攻击对象，必须存在
	 * @param type
	 *            攻击的形式，比如闪电
	 * @param lastingTime
	 *            闪电持续的时间 此时的state为STATE_MOVE
	 * @param explosionLastingTime
	 *            闪电爆炸的持续时间，此时的state为STATE_EXPLODE
	 */
	public PositionEffectSummoned(ActiveSkillEntity ase, int effectIndex,
			Fighter attackTarget, int targetX,int targetY,String type,String avataRace,String avataSex, int lastingTime,
			int explosionLastingTime) {
		this.id = nextEffectSummonedId();
		skillEntity = ase;
		this.effectIndex = effectIndex;
		if(attackTarget != null){
			this.x = attackTarget.getX();
			this.y = attackTarget.getY();
		}else{
			this.x = targetX;
			this.y = targetY;
		}
		this.effectType = type;
		this.avataRace = avataRace;
		this.avataSex = avataSex;
		this.lastingTime = lastingTime;
		this.explosionLastingTime = explosionLastingTime;
		this.attackTarget = attackTarget;

		state = STATE_MOVE;
		starttime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		deadline = starttime + lastingTime + explosionLastingTime;

//		ResourceManager.getInstance().getAvata(this);
	}

	/**
	 * 判断此效果是否还活着
	 */
	public boolean isAlive() {
		return alive;

	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		if(attackTarget != null && callHitTarget == false){
			callHitTarget = true;
			hitTarget(attackTarget);
		}
		if (state != STATE_EXPLODE && heartBeatStartTime > starttime + lastingTime) {
			state = STATE_EXPLODE;
		}
		if (heartBeatStartTime > deadline) {
			alive = false;
			game.removeSummoned(this);
		}
		// 位置跟随目标移动
		if(attackTarget != null){
			this.x = attackTarget.getX();
			this.y = attackTarget.getY();
		}
	}
	
	public boolean hitTarget(Fighter t) {
		x = t.getX();
		y = t.getY();
		state = STATE_EXPLODE;
		deadline = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + explosionLastingTime;
		skillEntity.hitTarget(t, effectIndex);
		return false;
	}
}
