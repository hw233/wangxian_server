package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Random;

import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.TRANSIENTENEMY_CHANGE_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.GuardNPC.DamageRecord;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;
import com.fy.engineserver.sprite.pet.Pet;

/**
 * 封印任务刷新npc
 * 
 *
 */
public class SealTaskFlushNPC extends NPC implements FightableNPC{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370998L;
	
	/**
	 * 刷新人的id
	 */
	public long flushPlayerId;

	/**
	 * 拥有者id，谁该享有杀死这个npc的奖励
	 */
	public long ownerId;
	
	/**
	 * 封印级别
	 */
	public int sealLevel;
	
	///////////下面为npc战斗相关
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
	protected void killed(long heartBeatStartTime, long interval, Game game) {
		PlayerManager pm = PlayerManager.getInstance();
		Player p;
		try {
			p = pm.getPlayer(flushPlayerId);
			p.killOneSprite(this);
			TaskManager tm = TaskManager.getInstance();
			if(tm != null){
				Task task = tm.getTask(Translate.封印任务,p.getCountry());
				if(task != null){
					int result = p.getTaskStatus(task);
					Player owner = null;
					try{
						owner = pm.getPlayer(ownerId);
					}catch(Exception ex){
						
					}
					if(owner != null){
						//发布封印任务的玩家完成任务，给杀死这个npc的人奖励
						owner.addExp(owner.getLevel()*1000, 0);
						BillingCenter bc = BillingCenter.getInstance();
						bc.playerSaving(owner, 25000, CurrencyType.SHOPYINZI, SavingReasonType.JIANXI, "击杀奸细");
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, Translate.恭喜你杀死奸细);
						owner.addMessageToRightBag(hreq);
					}
					if(result == TaskManager.TASK_STATUS_COMPLETE){
						String ownerName = Translate.无名氏;
						if(owner != null){
							ownerName = owner.getName();
						}
						ChatMessageService cms = ChatMessageService.getInstance();
						String description = Translate.translateString(Translate.玩家击杀了要捉拿的获得了25两银子和经验, new String[][]{{Translate.STRING_1,ownerName},{Translate.STRING_2,p.getName()},{Translate.STRING_3,this.getName()}});
						cms.sendMessageToSystem(description);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)5, description);
						p.addMessageToRightBag(hreq);
					}
					Game.logger.warn("[玩家击杀奸细] [成功] ["+this.getName()+"] [击杀人:"+(owner != null? owner.getLogString():"")+"] [刷新者:"+p.getLogString()+"] [是否完成任务:"+(result == TaskManager.TASK_STATUS_COMPLETE)+"，没有完成任务可能是因为玩家放弃了任务]");
				}else{
					Game.logger.warn("[玩家击杀奸细] [发布任务失败] ["+this.getName()+"] [刷新者:"+p.getLogString()+"]");
				}
			}
			
		} catch (Exception e) {
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[封印任务npc没有对应的发布任务的玩家] [玩家id:{}]",new Object[]{flushPlayerId});
		}
	}

	Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	public Point 得到位置() {
		Point point = new Point();
		point.x = this.x + random.nextInt(100);
		point.y = this.y + random.nextInt(100);
		return point;
	}

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

	@Override
	/**
	 * 封印任务刷新出的奸细npc
	 * 只能是发布任务的本国未到封印级别的人才可以攻击这个npc
	 */
	public int getFightingType(Fighter fighter) {
		if(!(fighter instanceof Player) && !(fighter instanceof Pet)){
			return Fighter.FIGHTING_TYPE_NEUTRAL;
		}
		if(fighter instanceof Player){
			if(((Player)fighter).getLevel() >= sealLevel){
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}
			if(getCountry() != ((Player)fighter).getCountry()){
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}
		}
		if(fighter instanceof Pet){
			Player owner = ((Pet)fighter).getMaster();
			if(owner != null){
				if(owner.getLevel() >= sealLevel){
					return Fighter.FIGHTING_TYPE_NEUTRAL;
				}
				if(getCountry() != owner.getCountry()){
					return Fighter.FIGHTING_TYPE_NEUTRAL;
				}
			}else{
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}
		}
		// TODO Auto-generated method stub
		return Fighter.FIGHTING_TYPE_ENEMY;
	}
	long test = 0;
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
		super.heartbeat(heartBeatStartTime, interval, game);
		this.notifyPlayerAndAddTransientEnemyList(game);
		if(heartBeatStartTime > test + 6000){
			test = heartBeatStartTime;
			if(Game.logger.isInfoEnabled())
				Game.logger.info(this.name+"在"+game.gi.getName()+"的("+x+"m"+y+")");
		}
		//定身或者眩晕的情况下，停止移动
		if(this.isHold() || this.isStun()){
			if(this.getMoveTrace() != null){
				stopAndNotifyOthers();
			}
		}
		
		this.skillAgent.heartbeat(game);
		this.fightingAgent.heartbeat(heartBeatStartTime, game);

		//光环技能
		auraSkillAgent.heartbeat(heartBeatStartTime, interval, game);
		if(innerState == 0){
			Fighter f = findTargetInActivationRange(game);
			if(f != null){
				updateDamageRecord(f,0,1);
			}
			if(hatridList.size() == 0){

			}else{
				stopAndNotifyOthers();
				innerState = 1;
			}
		}
		
		if(innerState == 1){
			if(hatridList.size() == 0){
//				if(this.isHold() == false && this.isStun() == false){
					innerState = 0;
//				}
			}
		}

		if (state != STATE_DEAD) {
			if(getHp() > 0){
				Fighter target = getMaxHatredFighter();
				if(target instanceof Player){
					ownerId = target.getId();
				}
				if(target instanceof Pet){
					ownerId = ((Pet)target).getOwnerId();
				}
			}
			
		}

//		if(innerState == 2){
//
//			if(this.getMoveTrace() == null && getX() == this.bornPoint.x && getY() == this.bornPoint.y){
//				innerState = 0;
//				this.setHp(this.getMaxHP());
//			}
//			
//			if(hatridList.size() > 0){
//				if(this.isStun() == false){
//					Fighter target = getMaxHatredFighter();
//					if(target != null){
//						ActiveSkill as = getActiveSkill(target);
//						if(as != null){
//							this.fightingAgent.start(as, getSkillLevelById(as.getId()),target);
//							innerState = 1;
//						}
//					}
//				}
//			}
//			
//		}
	}
	
	/**
	 * 通知玩家并且加入玩家临时敌人列表
	 * @param fighter
	 */
	public void notifyPlayerAndAddTransientEnemyList(Game game){
		LivingObject[] los = game.getLivingObjects();
		if(los == null){
			return;
		}
		for(int i = 0; i < los.length; i++){
			if(los[i] instanceof Player){
				Player fighter = (Player)los[i];
				if(getFightingType(fighter) == Fighter.FIGHTING_TYPE_ENEMY){
					if(los[i].getX() >= this.x - 600 && los[i].getX() <= this.x + 600 
							&& los[i].getY() >= this.y - 600 && los[i].getY() <= this.y + 600 ){
						synchronized(fighter.transientEnemyList){
							if(fighter.transientEnemyList == null || !fighter.transientEnemyList.contains(this)){
								TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(),
										(byte) 0, (byte) 1, this.getId());
								fighter.addMessageToRightBag(req);
								if(fighter.transientEnemyList == null){
									fighter.transientEnemyList = new ArrayList<Fighter>();
								}
								fighter.transientEnemyList.add(this);
							}
						}
					}
				}
			}
		}

	}
	int activationWidth = 300;
	int activationHeight = 300;
	/**
	 * 在激活范围内，寻找可攻击的对象，
	 * 如果范围内没有可攻击的对象，
	 * 就查看同类的怪，是否有被其他玩家攻击，如果有，协调攻击。
	 * @return
	 */
	protected Fighter findTargetInActivationRange(Game game){
//			Fighter fs[] = game.getVisbleFighter(this, false);
			LivingObject[] los = game.getLivingObjects();
			if(los == null){
				return null;
			}
			Fighter f = null;
			int minD = 0;
			for(int i = 0 ; i < los.length ; i++){
				if(los[i] instanceof Fighter){
					int ft = this.getFightingType((Fighter)los[i]);
					if(ft == Fighter.FIGHTING_TYPE_ENEMY){
						if(los[i].getX() >= this.x - this.activationWidth/2 && los[i].getX() <= this.x + this.activationWidth/2 
								&& los[i].getY() >= this.y - this.activationHeight/2 && los[i].getY() <= this.y + this.activationHeight/2 ){
								int d = (los[i].getX() - this.x)*(los[i].getX() - this.x) + (los[i].getY() - this.y)*(los[i].getY() - this.y);
								if(f == null){
									f = (Fighter)los[i];
									minD = d;
								}else if(d < minD){
									f = (Fighter)los[i];
									minD = d;
								}
						}
					}
				}
			}
			if(f != null){
				return f;
			}
		return null;
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

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		SealTaskFlushNPC p = new SealTaskFlushNPC();

		p.cloneAllInitNumericalProperty(this);

		p.country = country;
		p.npcMark = npcMark;
		p.hpMark = hpMark;
		p.apMark = apMark;
		p.setnPCCategoryId(this.getnPCCategoryId());

		if (items != null) {
			p.items = new NpcExecuteItem[this.items.length];
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					try {
						p.items[i] = (NpcExecuteItem) items[i].clone();
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		p.windowId = windowId;
		p.id = nextId();
		return p;
	}

	public ActiveSkillAgent getActiveSkillAgent() {
		return this.skillAgent;
	}

	@Override
	public int getPursueHeight() {
		// TODO Auto-generated method stub
		return activationHeight;
	}

	@Override
	public int getPursueWidth() {
		// TODO Auto-generated method stub
		return activationWidth;
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return fightingAgent;
	}

}
