package com.fy.engineserver.battlefield.dota;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.npc.NPC;


/**
 * 主将和兵营实现
 * 
 * 主将免疫一切BUFF和加血
 * 
 *
 */
public class DotaGeneral extends NPC{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	static Logger logger = Logger.getLogger(DotaGeneral.class);
public	static Logger logger = LoggerFactory.getLogger(DotaGeneral.class);

	
	DotaBattleField battle;
	
	protected Game game;
	
	//////////////////////////////////////////////////////////////////////
	//内部数据结构
	
	private long lastTimeForRecoverHP = 0;
	private long deadEndTime = 0;
	
	protected boolean 血量少于百分之95通知标记 = false;
	protected boolean 血量少于百分之75通知标记 = false;
	protected boolean 血量少于百分之50通知标记 = false;
	protected boolean 血量少于百分之25通知标记 = false;
	

	/**
	 * 判断是否在战场中
	 */
	public boolean isInBattleField(){
		return true;
	}
	
	
	public byte getSpriteType(){
		return Sprite.SPRITE_TYPE_NPC;
	}
	
	public byte getNpcType(){
		return Sprite.NPC_TYPE_BATTLE_FIELD;
	}
	
	/**
	 * 判断是否死亡，此标记只是标记是否死亡，比如HP = 0
	 * 不同于LivingObject的alive标记。
	 * alive标记用于是否要将生物从游戏中清除。死亡不代表要清除。
	 * @return
	 */
	public boolean isDeath(){
		return (this.hp <= 0 && state == STATE_DEAD);
	}
	
	public void init(){
		super.init();
		
		
	}
	
	/**
	 * 给定一个fighter，返回是敌方，友方，还是中立方。
	 * 
	 * 0 表示敌方
	 * 1 表示中立方
	 * 2 表示友方
	 * 
	 * @param fighter
	 * @return
	 */
	public int getFightingType(Fighter fighter){
		//
//		if(this.getBattleFieldSide() == fighter.getBattleFieldSide()){
//			return Fighter.FIGHTING_TYPE_FRIEND;
//		}else{
//			return Fighter.FIGHTING_TYPE_ENEMY;
//		}
		if(this.getBattleFieldSide() == fighter.getBattleFieldSide()){
			return Fighter.FIGHTING_TYPE_FRIEND;
		}else if(fighter.getBattleFieldSide() == DotaBattleField.BATTLE_SIDE_C){
			return Fighter.FIGHTING_TYPE_NEUTRAL;
		}else{
			return Fighter.FIGHTING_TYPE_ENEMY;
		}		
	}

	public void enrichHP(Fighter caster, int hp){
		//免疫一切加血
	}
	
	public void placeBuff(Buff buff){
		//免疫一切BUFF
	}
	
	public void setMoveTrace(MoveTrace trace) {
		//禁止移动
	}
	
	

	/**
	 * 处理其它生物对此生物造成伤害的事件
	 * 
	 * @param caster
	 *            伤害施加者
	 * @param damage
	 *            预期伤害值
	 * @param damageType
	 *            伤害类型，如：普通物理伤害，魔法伤害，反噬伤害等
	 */
	public void causeDamage(Fighter caster, int damage, int hateParam,int damageType) {}
	
	
	private int countHeroOfOppositeSide(){
		Fighter fs[] = game.getVisbleFighter(this, false);
		int count = 0;
		for(int i = 0 ; i < fs.length ; i++){
			if(fs[i] instanceof Player){
				Player p = (Player)fs[i];
				if(p.getBattleFieldSide() != this.getBattleFieldSide()){
					count ++;
				}
			}
		}
		return count;
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
	public void damageFeedback(Fighter target, int damage,int hateParam,int damageType) {
		
	}
	
	/**
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	protected void killed(Fighter killer) {
		
		battle.notifyKillingGeneral(killer, this);
		
	}
	
	
	public void heartbeat(long heartBeatStartTime, long interval, Game game){}

}
