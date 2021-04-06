package com.fy.engineserver.battlefield.dota;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuFaGong;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWuGong;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
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
 * 小兵
 * 小兵攻击的优先顺序，从高到低分别是：
 * 1.正在攻击我方英雄的敌人
 * 2.生命值非常低的敌人
 * 3.正在进行攻击的敌人
 * 4.生命值最低的敌人
 * 5.距离最近的敌人
 * 攻击目标每n秒改变一次，不会盯着某个单位打到死

 *
 */
public class DotaSolider extends NPC implements FightableNPC{
	
	public static final int SOLIDER_TYPE_近程 = 0;
	
	public static final int SOLIDER_TYPE_远程 = 1;
	
	public static final int SOLIDER_TYPE_攻城 = 2;

	public static final String[] NAMES = new String[]{Translate.text_1944,Translate.text_1945,Translate.text_1946};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	static Logger logger = Logger.getLogger(DotaSolider.class);
public	static Logger logger = LoggerFactory.getLogger(DotaSolider.class);

	public static final int[][] 第一组攻击点 = {
		{-50,0},{-43,25},{-25,43},{0,50},{25,43},{43,25},
		{50,0},{43,-25},{25,-43},{0,-50},{-25,-43},{-43,-25}
	};
	
	public static final int[][] 第二组攻击点 = {
		{-48,13},{-35,35},{-13,48},{13,48},{35,35},{48,13},
		{48,-13},{35,-35},{13,-48},{-13,-48},{-35,-35},{-48,-13}
	};
	
	
	//追击的范围宽度，以怪为中心
	protected int activationWidth = 200;
	//追击的范围高度，以怪为中心
	protected int activationHeight = 280;
	
	//防御塔的技能
	protected int skillId;
	protected int skillLevel;

	DotaBattleField battle;
	
	protected Game game;
	
	//多长时间改变一下攻击目标
	protected long changeTargetStep = 7000;
	
	//按固定路线巡逻，巡逻路径中的关键点，系统会自动找出一条经过这个点的路径
	protected int patrolingX[] = new int[0];
	protected int patrolingY[] = new int[0];
	
	//兵种类型
	protected int soliderType;
	
	//////////////////////////////////////////////////////////////////////
	//内部数据结构
	
	/**
	 * 人物身体上的Buff，这个数组的下标对应buffType 故，同一个buffType的buff在人物身上只能有一个
	 * 
	 * 此数据是要存盘的
	 * 
	 */
	private ArrayList<Buff> buffs = new ArrayList<Buff>();

	/**
	 * 下一次心跳要通知客户端去除的buff
	 */
	private transient ArrayList<Buff> removedBuffs = new ArrayList<Buff>();
	
	/**
	 * 下一次心跳要去通知客户端新增加的buff
	 */
	private transient ArrayList<Buff> newlyBuffs = new ArrayList<Buff>();
	
	
	
	protected ActiveSkill activeSkill = null;
	
	private long lastTimeForRecoverHP = 0;
	private long deadEndTime = 0;
	
	//技能代理，怪使用技能的代理
	protected ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);
	
	
	//攻击目标
	Fighter attackTarget;
	
	protected long lastChangeAttackTargetTime = 0;
	
	//巡逻的下一个点的下标
	protected int patrolingIndex = 0;
	
	private long lastTimeForBuffs = 0;
	
	
	
	private boolean 是否有正在前往的攻击点 = false;
	private int 攻击点组 = 0;
	private int 攻击点 = 0;
	
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
//			logger.warn("[DOTA战场] [兵技能配置错误] [编号为"+skillId+"的技能不存在]");
			if(logger.isWarnEnabled())
				logger.warn("[DOTA战场] [兵技能配置错误] [编号为{}的技能不存在]", new Object[]{skillId});
		}else if( !(skill instanceof ActiveSkill)){
//			logger.warn("[DOTA战场] [兵技能配置错误] [编号为"+skillId+"的技能不是主动技能]");
			if(logger.isWarnEnabled())
				logger.warn("[DOTA战场] [兵技能配置错误] [编号为{}的技能不是主动技能]", new Object[]{skillId});
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

	public ArrayList<Buff> getBuffs(){
		return buffs;
	}
	
	/**
	 * 通过buff的templateId获得buff
	 * @return
	 */
	public Buff getBuff(int templateId){
		for(Buff b : buffs){
			if(b.getTemplateId() == templateId){
				return b;
			}
		}
		return null;
	}
	
	public ArrayList<Buff> getRemovedBuffs() {
		return this.removedBuffs;
	}
	
	public ArrayList<Buff> getNewlyBuffs() {
		return this.newlyBuffs;
	}
	
	/**
	 * 种植一个buff到玩家或者怪的身上， 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff) {
		Buff old = null;
		if((buff instanceof Buff_ZhongDu) || (buff instanceof Buff_ZhongDuFaGong) || (buff instanceof Buff_ZhongDuWuGong)){
			
			for(Buff b : buffs){
				if(buff.getTemplate() == b.getTemplate() && buff.getCauser() == b.getCauser()){
					old = b;
					break;
				}
			}
			if(old != null){
				if(buff.getLevel() >= old.getLevel()){
					old.end(this);
					buffs.remove(old);
					if(old.isSyncWithClient()){
						this.removedBuffs.add(old);
					}
				}else{
					return;
				}
			}
			
		}else{

			for(Buff b : buffs){
				if(buff.getTemplate() == b.getTemplate()){
					old = b;
					break;
				}
			}
			if(old != null){
				if(buff.getLevel() >= old.getLevel()){
					old.end(this);
					buffs.remove(old);
					if(old.isSyncWithClient()){
						this.removedBuffs.add(old);
					}
				}else{
					return;
				}
			}
			
			for(int i = buffs.size() -1 ; i >= 0 ; i--){
				Buff b = buffs.get(i);
				if(buff.getCauser() == b.getCauser() && buff.getGroupId() == b.getGroupId()){
					buffs.remove(i);
					b.end(this);
					if(b.isSyncWithClient()){
						this.removedBuffs.add(b);
					}
				}
			}
		}
		buffs.add(buff);
		buff.start(this);
		if (buff.isSyncWithClient()) {
			this.newlyBuffs.add(buff);
		}

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
		battle.notifyKillingSolider(killer, this);
	}
	
	
	
	
	/**
	 * 巡逻
	 * 
	 * 运动轨迹
	 * 0 原地不动
	 * 1 按一定的轨迹巡逻
	 * 2 按给定的一条路径巡逻
	 * @param game
	 */
	protected void patroling(Game game){
		
		if(patrolingIndex >= 0 && this.patrolingIndex < patrolingX.length){
			pathFinding(game, patrolingX[patrolingIndex], patrolingY[patrolingIndex]);
		}
	}
	

	public void heartbeat(long heartBeatStartTime, long interval, Game game){
		super.heartbeat(heartBeatStartTime, interval, game);
		
		if(state != STATE_DEAD){
			if(this.getHp() <= 0){
				this.removeMoveTrace();
				this.state = STATE_DEAD;
				killed(heartBeatStartTime,interval,game);
				
				deadEndTime = heartBeatStartTime + this.deadLastingTime;
				
				
				this.setStun(false);
				this.setImmunity(false);
				this.setInvulnerable(false);
				this.setPoison(false);
				this.setAura((byte)-1);
				this.setHold(false);
				this.setWeak(false);
				
			}
		}
		
		if(state == STATE_DEAD){
			
			if(heartBeatStartTime > deadEndTime){
				this.setAlive(false);
			}
			return;
		}

		this.skillAgent.heartbeat(game);
		
		if(heartBeatStartTime - lastTimeForRecoverHP > 1000){
			lastTimeForRecoverHP = heartBeatStartTime;
			this.setHp(this.getHp() + this.getHpRecoverBase());
		}
		
		//
		//buff
		if( heartBeatStartTime - lastTimeForBuffs > 500){
			lastTimeForBuffs = heartBeatStartTime;
			
			for (int i = buffs.size()-1; i >= 0; i--) {
				Buff buff = buffs.get(i);
				if (buff != null) {
					if (buff.getInvalidTime() <= heartBeatStartTime) {
						buff.end(this);

						if (buff.isForover() && buff.isSyncWithClient()) {
							this.removedBuffs.add(buff);
						}
						buffs.remove(i);
					} else {
						buff.heartbeat(this, heartBeatStartTime, interval, game);
						if (buff.getInvalidTime() <= heartBeatStartTime) {
							buff.end(this);

							if (buff.isForover() && buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(i);
						}
					}
				}
			}

		}
		//到达一个目标点，就将目标设置为下一个目标点
		if(patrolingIndex >= 0 && this.patrolingIndex < patrolingX.length){
			int d = (patrolingX[patrolingIndex] - getX()) * (patrolingX[patrolingIndex] - getX());
			d += (patrolingY[patrolingIndex] - getY()) * (patrolingY[patrolingIndex] - getY());
			if(d < 1000){
				patrolingIndex++;
			}
		}
		
		
		if(this.skillAgent.isExecuting()){
			return;
		}
		
		if(attackTarget != null){
			if(attackTarget.getX() >= this.x - this.activationWidth/2 && attackTarget.getX() <= this.x + this.activationWidth/2 
					&& attackTarget.getY() >= this.y - this.activationHeight/2 && attackTarget.getY() <= this.y + this.activationHeight/2 ){
			}else{
				this.targetDisappear(attackTarget);
				
				this.是否有正在前往的攻击点 = false;
				this.攻击点组 = 0;
				this.攻击点 = -1;
				this.attackTarget = null;
			}
			
			if(attackTarget instanceof Player){
				if( ((Player)attackTarget).isInvulnerable()){
					this.targetDisappear(attackTarget);
					this.是否有正在前往的攻击点 = false;
					this.攻击点组 = 0;
					this.攻击点 = -1;
					this.attackTarget = null;

				}
			}
		}
		
		if(attackTarget == null){
			attackTarget = findAttackTarget();
			
			if(attackTarget != null){
				this.stopAndNotifyOthers();
			}
			
			lastChangeAttackTargetTime = heartBeatStartTime;
		}else if(heartBeatStartTime - lastChangeAttackTargetTime > this.changeTargetStep){
			
			this.是否有正在前往的攻击点 = false;
			this.攻击点组 = 0;
			this.攻击点 = -1;
			this.attackTarget = null;
			
			attackTarget = findAttackTarget();
			
			if(attackTarget != null){
				this.stopAndNotifyOthers();
			}
			
			lastChangeAttackTargetTime = heartBeatStartTime;
		}
		
		if(this.getMoveTrace() == null && attackTarget != null && attackTarget.isDeath() == false && this.skillAgent.isDuringCoolDown(skillId) == false){
			int r = activeSkill.check(this, attackTarget, skillLevel);
			if(r == Skill.OK){
//				this.skillAgent.usingSkill(activeSkill, skillLevel, attackTarget, (int)attackTarget.getX(),(int) attackTarget.getY(),this.getDirection());
			}else if(r == Skill.TARGET_TOO_FAR){
				if(this.getMoveTrace() == null){
					pathFindingForFighting(game,attackTarget);
				}
			}else{
				this.targetDisappear(attackTarget);
				this.是否有正在前往的攻击点 = false;
				this.攻击点组 = 0;
				this.攻击点 = -1;
				this.attackTarget = null;
			}
		}else if(attackTarget != null && attackTarget.isDeath()){
			this.targetDisappear(attackTarget);
			this.是否有正在前往的攻击点 = false;
			this.攻击点组 = 0;
			this.攻击点 = -1;
			this.attackTarget = null;
			
		}else if(attackTarget == null){
			if(this.getMoveTrace() == null){
				if(this.isHold() == false && this.isStun() == false){
					patroling(game); 
				}
			}
		}
	}
	
	
	protected void pathFindingForFighting(Game g,Fighter target){
		
		int flag1[] = new int[DotaSolider.第一组攻击点.length];
		int flag2[] = new int[DotaSolider.第二组攻击点.length];
		
		LivingObject los[] = g.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof DotaSolider){
				DotaSolider d = (DotaSolider)los[i];
				
				if(d != this && d.isDeath() == false && d.attackTarget != null && d.attackTarget == target){
					if(d.是否有正在前往的攻击点){
						if(d.攻击点组 == 1){
							flag1[d.攻击点] = 1;
						}else if(d.攻击点组 == 2){
							flag2[d.攻击点] = 1;
						}
					}
					
				}
			}
		}
		int groupIndex = 1;
		int nearestPoint = -1;
		int distance = Integer.MAX_VALUE;
		
		for(int i = 0 ; i < flag1.length ; i++){
			if(flag1[i] == 0){
				int d = (getX() - (target.getX() + DotaSolider.第一组攻击点[i][0])) * (getX() - (target.getX() + DotaSolider.第一组攻击点[i][0]));
				d += (getY() - (target.getY() + DotaSolider.第一组攻击点[i][1])) * (getY() - (target.getY() + DotaSolider.第一组攻击点[i][1]));
				
				if(d < distance){
					distance = d;
					nearestPoint = i;
				}
			}
		}
		
		if(nearestPoint == -1){
			groupIndex = 2;
			for(int i = 0 ; i < flag2.length ; i++){
				if(flag2[i] == 0){
					int d = (getX() - (target.getX() + DotaSolider.第二组攻击点[i][0])) * (getX() - (target.getX() + DotaSolider.第二组攻击点[i][0]));
					d += (getY() - (target.getY() + DotaSolider.第二组攻击点[i][1])) * (getY() - (target.getY() + DotaSolider.第二组攻击点[i][1]));
					
					if(d < distance){
						distance = d;
						nearestPoint = i;
					}
				}
			}
		}
		
		if(nearestPoint == -1){
			this.targetDisappear(target);
			this.attackTarget = null;
			return;
		}
		
		this.是否有正在前往的攻击点 = true;
		this.攻击点组 = groupIndex;
		this.攻击点 = nearestPoint;
		
		
		int dx = 0;
		int dy = 0;
		if(攻击点组 == 1){
			dx = target.getX() + 第一组攻击点[攻击点][0];
			dy = target.getY() + 第一组攻击点[攻击点][1];
		}else{
			dx = target.getX() + 第二组攻击点[攻击点][0];
			dy = target.getY() + 第二组攻击点[攻击点][1];
		}
		
		Point s = new Point(getX(),getY());
		Point e = new Point(dx,dy);
		int L = Graphics2DUtil.distance(s, e);
		if(g.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
			Point ps[] = new Point[]{s,e};
			short roadLen[] = new short[1];
			roadLen[0] =  (short)L;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L*1000/getSpeed()));
			setMoveTrace(path);
		}else{
			SignPost sps[] = g.getGameInfo().navigator.findPath(s, e);
			if(sps == null) return;
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
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/getSpeed() + 2000L);
			setMoveTrace(path);
		}
	}
	/**
	 *小兵攻击的优先顺序，从高到低分别是：
		1.正在攻击我方英雄的敌人
		2.生命值非常低的敌人
		3.正在进行攻击的敌人
		4.生命值最低的敌人
		5.距离最近的敌人
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
	

	protected void pathFinding(Game de,int dx,int dy){
		int distance = 20;
		Point s = new Point(getX(),getY());
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
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L1*1000/getSpeed()));
			setMoveTrace(path);
		}else if(L > distance ){
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if(sps == null) return;
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
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/getSpeed());
			setMoveTrace(path);
		}
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return null;
	}

}
