package com.fy.engineserver.battlefield.dota;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.npc.FightableNPC;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.npc.NPCFightingAgent;


/**
 * 防御塔
 * 
 * 防御塔免疫一切BUFF和加血
 * 
 * 防御塔的优先攻击顺序，从高到低分别是：

1.有小兵存在的情况下不攻击攻城车

2.生命值非常低的敌人

3.正在进行攻击的敌人

4.生命值最低的敌人

5.距离最近的敌人

攻击目标每n秒改变一次，不会盯着某个单位打到死

 *
 */
public class DotaDefenseTower extends NPC implements FightableNPC{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	static Logger logger = Logger.getLogger(DotaDefenseTower.class);
public	static Logger logger = LoggerFactory.getLogger(DotaDefenseTower.class);
	
	//激活的范围宽度，以怪为中心
	protected int activationWidth = 280;
	//激活的范围高度，以怪为中心
	protected int activationHeight = 360;
	
	//防御塔的技能
	protected int skillId;
	protected int skillLevel;
	
	DotaBattleField battle;
	
	protected Game game;
	
	//多长时间改变一下攻击目标
	protected long changeTargetStep = 7000;
	
	//////////////////////////////////////////////////////////////////////
	//内部数据结构
	
	protected ActiveSkill activeSkill = null;
	
	private long lastTimeForRecoverHP = 0;
	private long deadEndTime = 0;
	
	//技能代理，怪使用技能的代理
	protected ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);
	
	protected boolean 血量少于百分之95通知标记 = false;
	protected boolean 血量少于百分之75通知标记 = false;
	protected boolean 血量少于百分之50通知标记 = false;
	protected boolean 血量少于百分之25通知标记 = false;
	
	
	//防御塔类型
	protected int towerType = 0;
	
	public static int TOWERTYPE_QIAN = 0;
	public static int TOWERTYPE_ZHONG = 1;
	public static int TOWERTYPE_HOU = 2;
	public static int TOWERTYPE_JIDI = 3;
	public static int TOWERTYPE_WENQUAN = 4;
	
	
	//攻击目标
	Fighter attackTarget;
	
	protected long lastChangeAttackTargetTime = 0;
	
	
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
		
		CareerManager cm = CareerManager.getInstance();
		Skill skill = cm.getSkillById(skillId);
		if(skill == null){
//			logger.warn("[DOTA战场] [防御塔技能配置错误] [编号为"+skillId+"的技能不存在]");
			if(logger.isWarnEnabled())
				logger.warn("[DOTA战场] [防御塔技能配置错误] [编号为{}的技能不存在]", new Object[]{skillId});
		}else if( !(skill instanceof ActiveSkill)){
//			logger.warn("[DOTA战场] [防御塔技能配置错误] [编号为"+skillId+"的技能不是主动技能]");
			if(logger.isWarnEnabled())
				logger.warn("[DOTA战场] [防御塔技能配置错误] [编号为{}的技能不是主动技能]", new Object[]{skillId});
		}else{
			activeSkill = (ActiveSkill)skill;
			if(activeSkill.getMaxLevel() < skillLevel){
				skillLevel = activeSkill.getMaxLevel();
			}
		}
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
	 * 攻击目标消失，将此目标从仇恨列表中清除
	 * 
	 * @param target
	 */
	public void targetDisappear(Fighter target){
		
		
		//通知更新敌人列表
		target.notifyEndToBeFighted(this);
		this.notifyEndToFighting(target);
	}
	
	/**
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	protected void killed(Fighter killer) {
		
		battle.notifyKillingTower(killer, this);
	}
	
	
	
	public void heartbeat(long heartBeatStartTime, long interval, Game game){}
	
	/**
	 * 防御塔的优先攻击顺序，从高到低分别是：
	 * 1.有小兵存在的情况下不攻击攻城车
	 * 2.生命值非常低的敌人
	 * 3.正在进行攻击的敌人
	 * 4.生命值最低的敌人
	 * 5.距离最近的敌人
	 * 攻击目标每n秒改变一次，不会盯着某个单位打到死
	 * @return
	 */
	protected Fighter findAttackTarget(){
		
		return null;
	}

	
	public ActiveSkillAgent getActiveSkillAgent() {
		return this.skillAgent;
	}

	
	public int getPursueHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getPursueWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
