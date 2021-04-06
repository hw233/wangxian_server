package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Random;

import com.fy.engineserver.battlefield.dota.DotaBattleField;
import com.fy.engineserver.constants.GameConstant;
import com.fy.engineserver.core.Game;
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
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * 
 * 守卫NPC
 * 
 * 守卫NPC有如下的行为：
 * 
 * 1. 非中立的守卫，会视其他阵营的玩家为敌人，会根据配置进行主动攻击或者被动攻击
 * 
 * 2. 中立的守卫，会检查周围是否有人在打架，如果有，会是打架的人为敌人
 * 
 *  
 * 
 * 
 *
 */
public class GuardNPC extends NPC implements FightableNPC,Cloneable{

	private static final long serialVersionUID = -1563275511842290358L;

	public byte getSpriteType(){
		return Sprite.SPRITE_TYPE_NPC;
	}
	
	public byte getNpcType(){
		return Sprite.NPC_TYPE_GUARD;
	}
	
	//伤害记录，记录掉血的记录，以及仇恨记录
	public static class DamageRecord {
		public Fighter f;
		public int damage = 0;
		public int hatred = 0;
		public long time;

		public DamageRecord(Fighter f, int d,int h) {
			this.f = f;
			this.damage = d;
			this.hatred = h;
			time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
	}
	
	/**
	 * 运动轨迹
	 * 0 原地不动
	 * 1 按一定的轨迹巡逻
	 */
	protected int traceType = 0;
	
	/**
	 * 巡逻的范围宽度
	 */
	protected int patrolingWidth = 320;
	
	/**
	 * 巡逻的范围高度
	 */
	protected int patrolingHeight = 320;
	
	/**
	 * 巡逻的时间间隔
	 */
	protected long patrolingTimeInterval = 15000;
	
	/**
	 * 激活条件，所谓激活就是攻击玩家
	 * 
	 * 0  表示被攻击，准备攻击攻击他的玩家
	 * 1  表示进入怪的视野范围，怪看到玩家后，就开始主动攻击玩家，同时如果怪被攻击，优先攻击攻击他的玩家
	 * 2  满足0和1，同时视野范围内的同类怪被攻击，就开始主动攻击玩家
	 * 
	 */
	protected int activationType = 0;
	
	//激活的范围宽度，以怪为中心
	protected int activationWidth = 320;
	//激活的范围高度，以怪为中心
	protected int activationHeight = 320;
	
	//追击的范围宽度，以怪为中心
	protected int pursueWidth = 640;
	//追击的范围高度，以怪为中心
	protected int pursueHeight = 640;
	
	/**
	 * 回出生点的速度是移动速度的百分之多少。150表示1.5倍
	 */
	protected int backBornPointMoveSpeedPercent = 150;
	
	/**
	 * 回程路上的补血量，每0.5秒钟的补血
	 */
	protected int backBornPointHp = 100;
	
	/**
	 * 怪装备的技能ID列表，如果对应的技能为主动技能，那么就是怪攻击时
	 * 使用的技能。
	 * 
	 * 如果对应的光环类技能，那么就是怪拥有的光环类技能。
	 */
	private int activeSkillIds[];
	
	/**
	 * 怪装备的技能ID列表，对应的各个技能的级别
	 */
	protected int activeSkillLevels[];
	
	
	////////////////////////////////////////////////////////////////////////////////////
	// 以下是怪的内部数据结构
	///////////////////////////////////////////////////////////////////////////////////

	// 怪被打死的时间
	private transient long deadEndTime = 0;

	private transient long lastTimeForPatroling;
	
	private transient long lastTimeForRecoverHP;
	
	private transient long lastTimeForBuffs;
	
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
	
	/**
	 * 这个怪物拥有的技能
	 */
	protected transient ArrayList<ActiveSkill> skillList = new ArrayList<ActiveSkill>();
	
	//技能代理，怪使用技能的代理
	protected transient ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);
	
	//战斗代理
	protected transient NPCFightingAgent fightingAgent = new NPCFightingAgent(this);
	
	private transient AuraSkillAgent auraSkillAgent = new AuraSkillAgent(this);
	
	private transient AuraSkill auraSkill = null;
	/**
	 * 记录各个攻击者对我的伤害
	 */
	protected transient ArrayList<DamageRecord> hatridList = new ArrayList<DamageRecord>();
	
	//怪的内部状态，注意此状态, 0表示空闲，1表示激活，2表示回程
	protected transient int innerState = 0;
	
	
	//护盾
	protected int faShuHuDunDamage = 0;
	protected int wuLiHuDunDamage = 0;
	


	protected Player owner = null;
	
	
	public AuraSkillAgent getAuraSkillAgent(){
		return auraSkillAgent;
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
	 * 怪出生后，调用此初始化方法
	 */
	public void init(){
		super.init();
		CareerManager cm = CareerManager.getInstance();
		
		if(activeSkillIds == null || activeSkillIds.length == 0){
			//
			Skill[] skills = cm.getMonsterSkills();
			for(int i = skills.length -1  ; i >= 0 ; i--){
				if(skills[i] instanceof ActiveSkill){
					activeSkillIds = new int[]{skills[i].getId()};
					break;
				}
			}
		}
		
		for(int i = 0 ; i < activeSkillIds.length ; i++){
			Skill skill = cm.getSkillById(activeSkillIds[i]);
			if(skill != null && skill instanceof ActiveSkill){
				skillList.add((ActiveSkill)skill);
			}else if(skill != null && skill instanceof PassiveSkill){

			}else if(skill != null && skill instanceof AuraSkill){
				auraSkill = (AuraSkill)skill;
			}
		}
		if(auraSkill != null){
			this.auraSkillAgent.openAuraSkill(auraSkill);
		}
		
		this.commonAttackSpeed = 2000;
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
		
		if(fighter == this) return FIGHTING_TYPE_FRIEND;
		
		
		if(fighter instanceof Monster){
			return FIGHTING_TYPE_ENEMY;
		}else if(fighter instanceof Player){
			Player p = (Player)fighter;
			
			if(this.isInBattleField()){
				if(this.getBattleFieldSide() == fighter.getBattleFieldSide()){
					return FIGHTING_TYPE_FRIEND; 
				}else if(fighter.getBattleFieldSide() == DotaBattleField.BATTLE_SIDE_C){
					return FIGHTING_TYPE_NEUTRAL;
				}else if(this.getBattleFieldSide() == DotaBattleField.BATTLE_SIDE_C){
					return FIGHTING_TYPE_NEUTRAL;
				}else{
					return FIGHTING_TYPE_ENEMY;
				}
				
			}
			if(p.getCountry() == GameConstant.中立阵营){
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}else if(p.getCountry() == this.getCountry()){
				return FIGHTING_TYPE_FRIEND;
			}else if(this.getCountry() == GameConstant.中立阵营){
				
				for(DamageRecord dr :hatridList){
					if(dr.f == p){
						return FIGHTING_TYPE_ENEMY;
					}
				}
				
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}else{
				return FIGHTING_TYPE_ENEMY;
			}
			
		}else if(fighter instanceof Sprite){
			Sprite s = (Sprite)fighter;
			
			if(this.isInBattleField()){
				if(this.getBattleFieldSide() == fighter.getBattleFieldSide()){
					return FIGHTING_TYPE_FRIEND; 
				}else if(fighter.getBattleFieldSide() == DotaBattleField.BATTLE_SIDE_C){
					return FIGHTING_TYPE_NEUTRAL;
				}else if(this.getBattleFieldSide() == DotaBattleField.BATTLE_SIDE_C){
					return FIGHTING_TYPE_NEUTRAL;
				}else{
					return FIGHTING_TYPE_ENEMY;
				}
				
			}
			
			if(s.getCountry() == this.getCountry()){
				return FIGHTING_TYPE_FRIEND;
			}else if(this.getCountry() == GameConstant.中立阵营){
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}else if(s.getCountry() == GameConstant.中立阵营){
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}else{
				return FIGHTING_TYPE_ENEMY;
			}
			
		}else{
			return FIGHTING_TYPE_NEUTRAL;
		}
	}
	

	/**
	 * 通知怪，某个玩家b给玩家a加血
	 * 对应的，要增加b的仇恨值
	 * @param a
	 * @param b
	 * @param hp
	 */
	public void notifyHPAdded(Player a,Player b,int hp){
		updateDamageRecord(b,0,hp/2);
	}
	
	/**
	 * 某个敌人，选择某个技能
	 * @param target
	 * @return
	 */
	protected ActiveSkill getActiveSkill(Fighter target){
		if(skillList.size() > 0) return skillList.get(0);
		return null;
	}

	/**
	 * 得到最大的仇恨值敌人
	 * @return
	 */
	public Fighter getMaxHatredFighter(){
		int maxHatred = -1;
		DamageRecord d = null;
		for(int i = 0 ; i < hatridList.size() ; i++){
			DamageRecord dr = hatridList.get(i);
			if(dr.hatred > maxHatred){
				maxHatred = dr.hatred;
				d = dr;
			}
		}
		if(d != null){
			return d.f;
		}else{
			return null;
		}
	}
	/**
	 * 更新仇恨列表
	 * @param caster
	 * @param damage
	 * @param hatred
	 */
	protected void updateDamageRecord(Fighter caster,int damage,int hatred){
		
		if(caster.isDeath()) return;
		
		if(caster instanceof Player){
			hatred = hatred * (((Player)caster).getHatridRate() + 100)/100;
		}
		
		DamageRecord record = null;
		for(int i = 0 ; i < hatridList.size() ; i++){
			DamageRecord dr = hatridList.get(i);
			if(dr.f == caster){
				record = dr;
			}
		}
		if(record == null){
			record = new DamageRecord(caster,damage,hatred);
			hatridList.add(record);
		}else{
			record.damage += damage;
			record.hatred += hatred;
			record.time = heartBeatStartTime;
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
	public void causeDamage(Fighter caster, int damage, int hateParam,int damageType) {
		//debug dot
		super.causeDamage(caster, damage, hateParam, damageType);
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
		this.fightingAgent.damageFeedback(target, damage, damageType);
	}

	/**
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	protected void killed(long heartBeatStartTime, long interval, Game game) {}
	
	/**
	 * 攻击目标消失，将此目标从仇恨列表中清除
	 * 
	 * @param target
	 */
	public void targetDisappear(Fighter target){
		DamageRecord record = null;
		for(int i = 0 ; i < hatridList.size() ; i++){
			DamageRecord dr = hatridList.get(i);
			if(dr.f == target){
				record = dr;
			}
		}
		if(record != null){
			hatridList.remove(record);
		}
		
		//通知更新敌人列表
		target.notifyEndToBeFighted(this);
		this.notifyEndToFighting(target);
	}


	
	public boolean isSameTeam(Fighter fighter) {
		return false;
	}

	

	public int getMp() {
		return Integer.MAX_VALUE;
	}
	
	/**
	 * 通知NPC，A先动手打B
	 * @param a
	 * @param b
	 */
	public void notifyAFightingB(Player a,Player b){
		if(this.getCountry() == GameConstant.中立阵营 || this.getCountry() == b.getCountry()){
			this.updateDamageRecord(a, 1, 1);
			if(innerState ==  0){
				innerState = 1;
			}
		}
	}
	
	/**
	 * 在激活范围内，寻找可攻击的对象，
	 * 如果范围内没有可攻击的对象，
	 * 就查看同类的怪，是否有被其他玩家攻击，如果有，协调攻击。
	 * @return
	 */
	protected Fighter findTargetInActivationRange(Game game){
		if(activationType == 1 || activationType == 2){
			Fighter fs[] = game.getVisbleFighter(this, false);
			Fighter f = null;
			int minD = 0;
			for(int i = 0 ; i < fs.length ; i++){
				int ft = this.getFightingType(fs[i]);
				if(ft == Fighter.FIGHTING_TYPE_ENEMY){
					if(fs[i].getX() >= this.x - this.activationWidth/2 && fs[i].getX() <= this.x + this.activationWidth/2 
							&& fs[i].getY() >= this.y - this.activationHeight/2 && fs[i].getY() <= this.y + this.activationHeight/2 ){
							int d = (fs[i].getX() - this.x)*(fs[i].getX() - this.x) + (fs[i].getY() - this.y)*(fs[i].getY() - this.y);
							if(f == null){
								f = fs[i];
								minD = d;
							}else if(d < minD){
								f = fs[i];
								minD = d;
							}
					}
				}
			}
			if(f != null) return f;
			if(activationType == 1) return null;
			
			for(int i = 0 ; i < fs.length ; i++){
				if(fs[i] instanceof GuardNPC){
					GuardNPC s = (GuardNPC)fs[i];
					if(s.title == null || !s.title.equals(title)) continue;
					if(s.getX() >= this.x - this.activationWidth/2 && s.getX() <= this.x + this.activationWidth/2 
							&& s.getY() >= this.y - this.activationHeight/2 && s.getY() <= this.y + this.activationHeight/2 ){
							Fighter target = s.getMaxHatredFighter();
							if(target != null){
								f = target;
								break;
							}
					}
				}
			}
			if(f != null) return f;
			
		}
		
		return null;
	}
	Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	/**
	 * 巡逻
	 * @param game
	 */
	protected void patroling(Game game){
		if(heartBeatStartTime - this.lastTimeForPatroling > this.patrolingTimeInterval){
			if(random.nextInt(100) > 50){
				return;
			}
			lastTimeForPatroling = heartBeatStartTime;
			
			int px = this.bornPoint.x + random.nextInt(this.patrolingWidth) - this.patrolingWidth/2;
			int py = this.bornPoint.y + random.nextInt(this.patrolingHeight) - this.patrolingHeight/2;
			if(px > 0 && px < game.getGameInfo().getWidth()
					&& py > 0 && py < game.getGameInfo().getHeight() )
			{
				Point s = new Point(getX(),getY());
				Point e = new Point(px,py);
				
				if(game.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
					short roadLen[] = new short[1];
					roadLen[0] = (short) Graphics2DUtil.distance(s, e);
					MoveTrace path = new MoveTrace(roadLen,new Point[]{s,e},(long)(heartBeatStartTime + roadLen[0]*1000/(speed/2)));
					setMoveTrace(path);
				}
			}
		}
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
	 * 心跳处理的顺序
	 * 
	 * 1. 先处理父类的心跳
	 * 2. 处理是否被打死，如果是调用killed方法，同时设置state为STATE_DEAD状态
	 * 3. 判断是否已经超过死亡过程设定的时间，如果是，标记此怪不再存活，设置alive标记
	 * 
	 */
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
				//循环清楚buff
				if(buffs != null){
					for(int i = buffs.size() - 1; i >= 0; i--){
						Buff bu = buffs.get(i);
						if(bu != null){
							bu.end(this);
							if (bu.isForover() || bu.isSyncWithClient()) {
								this.removedBuffs.add(bu);
							}
							buffs.remove(i);
							
							if(ActiveSkill.logger.isDebugEnabled()){
//								ActiveSkill.logger.debug("[死亡去除BUFF] ["+getName()+"] [死亡] ["+bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".")+1)+":"+bu.getTemplateName()+"] [time:"+bu.getInvalidTime()+"]");
								if(ActiveSkill.logger.isDebugEnabled())
									ActiveSkill.logger.debug("[死亡去除BUFF] [{}] [死亡] [{}:{}] [time:{}]", new Object[]{getName(),bu.getClass().getName().substring(bu.getClass().getName().lastIndexOf(".")+1),bu.getTemplateName(),bu.getInvalidTime()});
							}
						}
					}
				}
			}
		}
		
		if(state == STATE_DEAD){
			
			if(heartBeatStartTime > deadEndTime){
				this.setAlive(false);
			}
			
			
			return;
		}
		
		//定身或者眩晕的情况下，停止移动
		if(this.isHold() || this.isStun()){
			if(this.getMoveTrace() != null){
				stopAndNotifyOthers();
			}
		}
		
		this.skillAgent.heartbeat(game);
		this.fightingAgent.heartbeat(heartBeatStartTime, game);
		
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
			
			//光环技能
			auraSkillAgent.heartbeat(heartBeatStartTime, interval, game);
		}
		
		if(innerState == 0){
			Fighter f = findTargetInActivationRange(game);
			if(f != null){
				updateDamageRecord(f,0,1);
			}
			if(hatridList.size() == 0){
				if(this.getMoveTrace() == null && this.traceType == 1){
					if(this.isHold() == false && this.isStun() == false){
						patroling(game); 
					}
				}
			}else{
				innerState = 1;
			}
		}
		
		if(innerState == 1){
			if(hatridList.size() == 0){
				if(this.isHold() == false && this.isStun() == false){
					backToBornPoint(game);
					innerState = 2;
				}
			}else{
				if(this.isStun() == false){
					Fighter target = getMaxHatredFighter();
					if(target != null && this.fightingAgent.isFighting() == false){
						ActiveSkill as = getActiveSkill(target);
						if(as != null){
							this.fightingAgent.start(as, getSkillLevelById(as.getId()),target);
						}
					}else if(target != null && this.fightingAgent.target != target){
						ActiveSkill as = getActiveSkill(target);
						if(as != null){
							this.fightingAgent.start(as, getSkillLevelById(as.getId()),target);
						}
					}
				}
			}
		}
		
		if(innerState == 2){
			if(heartBeatStartTime - lastTimeForRecoverHP > 500){
				lastTimeForRecoverHP = heartBeatStartTime;
				int hpa = this.backBornPointHp;
				
				if( (hpa > 0 && getHp() < getMaxHP()) || (hpa < 0 && getHp() > 0)){
					this.setHp(getHp() + hpa);
				}
			}
			
			if(this.getMoveTrace() == null && getX() == this.bornPoint.x && getY() == this.bornPoint.y){
				innerState = 0;
				this.setHp(this.getMaxHP());
			}
			
			if(hatridList.size() > 0){
				if(this.isStun() == false){
					Fighter target = getMaxHatredFighter();
					if(target != null){
						ActiveSkill as = getActiveSkill(target);
						if(as != null){
							this.fightingAgent.start(as, getSkillLevelById(as.getId()),target);
							innerState = 1;
						}
					}
				}
			}
			
		}
		
	}
	
	public int getSkillLevelById(int sid){
		for(int i = 0 ; activeSkillIds != null && i < this.activeSkillIds.length ; i++){
			if(activeSkillIds[i] == sid && this.activeSkillLevels != null && i < this.activeSkillLevels.length){
				if( this.activeSkillLevels[i] == 0) return 1;
				return activeSkillLevels[i] ;
			}
		}
		return 0;
	}
	
	protected void backToBornPoint(Game de){
		Point s = new Point(getX(),getY());
		Point e = this.bornPoint;
		
		SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
		if(sps == null) return;
		Point ps[] = new Point[sps.length+2];
		short roadLen[] = new short[sps.length+1];
		for(int i = 0 ; i < ps.length ; i++){
			if(i == 0){
				ps[i] = new Point(s.x,s.y);
			}else if(i <= sps.length){
				ps[i] = new Point(sps[i-1].x,sps[i-1].y);
			}else{
				ps[i] = new Point(e.x,e.y);
			}
		}
		int totalLen = 0;
		for(int i = 0 ; i < roadLen.length ; i++){
			if(i == 0){
				roadLen[i] =(short) Graphics2DUtil.distance(ps[0], ps[1]);
			}else if(i < sps.length){
				Road r =  de.getGameInfo().navigator.getRoadBySignPost(sps[i-1].id, sps[i].id);
				if(r != null){
					roadLen[i] = r.len;
				}else{
					roadLen[i] =(short) Graphics2DUtil.distance(ps[i], ps[i+1]);
				}
			}else{
				roadLen[i] =(short) Graphics2DUtil.distance(ps[i], ps[i+1]);
			}
			totalLen+= roadLen[i];
		}
		MoveTrace path = new MoveTrace(roadLen,ps,heartBeatStartTime + totalLen*1000/(speed*this.backBornPointMoveSpeedPercent/100));
		setMoveTrace(path);
		
	}

	

	public int getTraceType() {
		return traceType;
	}

	public int getPatrolingWidth() {
		return patrolingWidth;
	}

	public int getPatrolingHeight() {
		return patrolingHeight;
	}

	public long getPatrolingTimeInterval() {
		return patrolingTimeInterval;
	}

	public int getActivationType() {
		return activationType;
	}

	public int getActivationWidth() {
		return activationWidth;
	}

	public int getActivationHeight() {
		return activationHeight;
	}

	public int getPursueWidth() {
		return pursueWidth;
	}

	public int getPursueHeight() {
		return pursueHeight;
	}

	public int getBackBornPointMoveSpeedPercent() {
		return backBornPointMoveSpeedPercent;
	}

	public int getBackBornPointHp() {
		return backBornPointHp;
	}


	public long getLastTimeForPatroling() {
		return lastTimeForPatroling;
	}

	public long getLastTimeForRecoverHP() {
		return lastTimeForRecoverHP;
	}

	public ArrayList<ActiveSkill> getSkillList() {
		return skillList;
	}

	public ActiveSkillAgent getSkillAgent() {
		return skillAgent;
	}

	public NPCFightingAgent getFightingAgent() {
		return fightingAgent;
	}

	public ArrayList<DamageRecord> getHatridList() {
		return hatridList;
	}

	public int getInnerState() {
		return innerState;
	}

	public int getFaShuHuDunDamage() {
		return faShuHuDunDamage;
	}

	public void setFaShuHuDunDamage(int faShuHuDunDamage) {
		this.faShuHuDunDamage = faShuHuDunDamage;
	}

	public int getWuLiHuDunDamage() {
		return wuLiHuDunDamage;
	}

	public void setWuLiHuDunDamage(int wuLiHuDunDamage) {
		this.wuLiHuDunDamage = wuLiHuDunDamage;
	}

	

	public void setTraceType(int traceType) {
		this.traceType = traceType;
	}

	public void setPatrolingWidth(int patrolingWidth) {
		this.patrolingWidth = patrolingWidth;
	}

	public void setPatrolingHeight(int patrolingHeight) {
		this.patrolingHeight = patrolingHeight;
	}

	public void setPatrolingTimeInterval(long patrolingTimeInterval) {
		this.patrolingTimeInterval = patrolingTimeInterval;
	}

	public void setPursueWidth(int pursueWidth) {
		this.pursueWidth = pursueWidth;
	}

	public void setPursueHeight(int pursueHeight) {
		this.pursueHeight = pursueHeight;
	}

	public int[] getActiveSkillIds() {
		return activeSkillIds;
	}

	public void setActiveSkillIds(int[] activeSkillIds) {
		this.activeSkillIds = activeSkillIds;
	}

	public int[] getActiveSkillLevels() {
		return activeSkillLevels;
	}

	public void setActiveSkillLevels(int[] activeSkillLevels) {
		this.activeSkillLevels = activeSkillLevels;
	}

	public void setActivationType(int activationType) {
		this.activationType = activationType;
	}

	public void setActivationWidth(int activationWidth) {
		this.activationWidth = activationWidth;
	}

	public void setActivationHeight(int activationHeight) {
		this.activationHeight = activationHeight;
	}

	public void setBackBornPointMoveSpeedPercent(int backBornPointMoveSpeedPercent) {
		this.backBornPointMoveSpeedPercent = backBornPointMoveSpeedPercent;
	}

	public void setBackBornPointHp(int backBornPointHp) {
		this.backBornPointHp = backBornPointHp;
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		GuardNPC p = new GuardNPC();
		p.cloneAllInitNumericalProperty(this);

		p.setnPCCategoryId(this.getnPCCategoryId());

		p.country = country;
		
		p.activationHeight = this.activationHeight;
		p.activationType = this.activationType;
		p.activationWidth = this.activationWidth;
		p.activeSkillIds = this.activeSkillIds;
		p.activeSkillLevels = this.activeSkillLevels;
		p.backBornPointHp = this.backBornPointHp;
		p.backBornPointMoveSpeedPercent = this.backBornPointMoveSpeedPercent;
		p.bornPoint = this.bornPoint;
		p.commonAttackRange = this.commonAttackRange;
		p.commonAttackSpeed = this.commonAttackSpeed;
		p.dialogContent  = this.dialogContent;
		p.faShuHuDunDamage = this.faShuHuDunDamage;
		p.height = this.height;

		p.patrolingHeight = this.patrolingHeight;
		p.patrolingTimeInterval = this.patrolingTimeInterval;
		p.patrolingWidth = this.patrolingWidth;
		p.pursueHeight = this.pursueHeight;
		p.pursueWidth = this.pursueWidth;
		p.taskMark = this.taskMark;
		p.traceType  = this.traceType;
		p.viewHeight = this.viewHeight;
		p.viewWidth = this.viewWidth;
		
		
		
		p.windowId = windowId;
		p.id = nextId();
		return p;
	}

	public ActiveSkillAgent getActiveSkillAgent() {
		return this.skillAgent;
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return fightingAgent;
	}

	
}
