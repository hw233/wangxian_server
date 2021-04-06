package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Random;

import org.w3c.dom.Element;

import com.fy.engineserver.constants.Event;
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
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.datasource.skill.AuraSkill;
import com.fy.engineserver.datasource.skill.AuraSkillAgent;
import com.fy.engineserver.datasource.skill.PassiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 
 * 护送NPC有如下的行为：
 * 
 *  1. 有目地地，自己寻路走过去
 *  2. 在路中发现怪，会去打怪
 *  3. 打完怪，会继续寻路走向目地地
 *  4. 开始记录护送的玩家
 *  5. 在心跳中检查护送的玩家是否脱离广播区域，如果脱离，将他们从护送玩家列表中清楚
 *  6. 所有护送玩家都脱离广播区域，护送NPC消失
 *  7. 护送NPC到达目地地，护送玩家完成任务
 *  8. 护送NPC会在路途中不断的说话
 *  
 *  
 *  此NPC的编辑器需要生成如下的数据：
 *  
 *  <additional-data>
 *  	<convoy-node x='' y='' monsterCategoryId='' count='' sayContent=''/>
 *  	<convoy-node x='' y='' monsterCategoryId='' count='' sayContent=''/>
 *  	<convoy-node x='' y='' monsterCategoryId='' count='' sayContent=''/>
 *  	<convoy-node x='' y='' monsterCategoryId='' count='' sayContent=''/>
 *  </additional-data>
 * 
 * 
 *
 */
public class ConvoyNPC extends NPC implements FightableNPC,Cloneable{

	private static final long serialVersionUID = -1563275511842290358L;

	static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	
	public byte getSpriteType(){
		return Sprite.SPRITE_TYPE_NPC;
	}
	
	public byte getNpcType(){
		return Sprite.NPC_TYPE_CONVOY;
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
	 * 目地地
	 */
	protected String destinationMapName;
	protected int destinationX;
	protected int destinationY;
	
	/**
	 * 途中说的话，
	 * 第一句话在开始的时候说
	 */
	protected String sayContentOnStart = "";
	
	/**
	 * 最后一句话在目的地说
	 */
	protected String sayContentOnEnd = "";
	
	long lastPlantBuffTime;
	
	
	/**
	 * 护送结点信息
	 * 
	 * NPC从开始点出发，先到第一个结点，说一句话，然后刷一些怪出来
	 * 然后到达第二个结点，说一句话，再刷一些怪出来
	 * 
	 * 
	 *
	 */
	public static class ConvoyNode{
		public int x;
		public int y;
		public int monsterCategoryId;
		public int count = 0;
		public String sayContent="";
	}
	
	protected ArrayList<ConvoyNode> nodeList = new ArrayList<ConvoyNode>();
	
	
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
	
	/**
	 * 护送的玩家列表
	 */
	protected transient ArrayList<Player> convoyPlayers = new ArrayList<Player>();
	
	
	//护送的内部状态，注意此状态, 0表示向目的地走，1表示激活攻击怪，2标识到达结点，3标识到达目的地，4标识站在原地不动
	protected transient int innerState = 0;
	
	protected transient long lastTimeForReachDestination = 0;
	
	protected transient String originalNpcName;
	
	/**
	 * 那个任务
	 */
	protected Task task;
	
	//护盾
	protected int faShuHuDunDamage = 0;
	protected int wuLiHuDunDamage = 0;
	
	/**
	 * 表达的是目的地，当nodeIndex == nodeList.size() 标识目的地是最后的目的地
	 */
	protected int nodeIndex = 0;

	protected transient ArrayList<Monster> flushMonsterList = new ArrayList<Monster>();
	
	//站在原地不动结束时间
	private long standTimeFinish = 0;

	
	
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
	 * 设置格外属性
	 */
	public void setAdditionData(Element e){
		Element eles[] = XmlUtil.getChildrenByName(e, "convoy-node");
		for(int i = 0 ; i < eles.length  ;i++){
			int x = XmlUtil.getAttributeAsInteger(eles[i], "x");
			int y = XmlUtil.getAttributeAsInteger(eles[i], "y");
			int monsterCategoryId = XmlUtil.getAttributeAsInteger(eles[i], "monsterCategoryId");
			int count = XmlUtil.getAttributeAsInteger(eles[i], "count");
			String sayContent =  XmlUtil.getAttributeAsString(eles[i], "sayContent","", TransferLanguage.getMap());
			
			ConvoyNode cn = new ConvoyNode();
			cn.x = x;
			cn.y = y;
			cn.monsterCategoryId = monsterCategoryId;
			cn.count = count;
			cn.sayContent = sayContent;
			
			this.nodeList.add(cn);
		}
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

		if(this.viewWidth == 0){
			this.viewWidth = 640;
		}
		if(this.viewHeight == 0){
			this.viewHeight = 480;
		}
		
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
			
			if(p.getCountry() == this.getCountry()){
				return FIGHTING_TYPE_FRIEND;
			}else{
				
				for(DamageRecord dr :hatridList){
					if(dr.f == p){
						return FIGHTING_TYPE_ENEMY;
					}
				}
				
				return Fighter.FIGHTING_TYPE_NEUTRAL;
			}
			
		}else if(fighter instanceof Sprite){
			Sprite s = (Sprite)fighter;
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
	public void causeDamage(Fighter caster, int damage, int damageType) {}

	/**
	 * 处理伤害反馈事件。当某个精灵（玩家、怪物等）受到攻击并造成伤害，<br>
	 * 该精灵会调用攻击者的这个方法，通知攻击者，它的攻击对其他精灵造成了伤害
	 * 
	 * @param target
	 *            受到伤害的目标精灵
	 * @param damage
	 *            真实伤害值
	 */
	public void damageFeedback(Fighter target, int damage,int damageType) {
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


	
	public boolean isSameTeam(Fighter fighter) {
		return false;
	}

	
	public int getMp() {
		return Integer.MAX_VALUE;
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
			float minD = 0;
			for(int i = 0 ; i < fs.length ; i++){
				int ft = this.getFightingType(fs[i]);
				if(ft == Fighter.FIGHTING_TYPE_ENEMY){
					if(fs[i].getX() >= this.x - this.activationWidth/2 && fs[i].getX() <= this.x + this.activationWidth/2 
							&& fs[i].getY() >= this.y - this.activationHeight/2 && fs[i].getY() <= this.y + this.activationHeight/2 ){
						float d = (fs[i].getX() - this.x)*(fs[i].getX() - this.x) + (fs[i].getY() - this.y)*(fs[i].getY() - this.y);
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
				if(fs[i] instanceof ConvoyNPC){
					ConvoyNPC s = (ConvoyNPC)fs[i];
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
	

	
	
	
	/**
	 * 种植一个buff到玩家或者怪的身上， 相同类型的buff会互相排斥，高级别的buff将顶替低级别的buff，无论有效期怎么样
	 * 
	 * @param buff
	 */
	public void placeBuff(Buff buff) {
		Buff old = null;
		if(!(buff instanceof Buff_ZhongDu) && !(buff instanceof Buff_ZhongDuFaGong) && !(buff instanceof Buff_ZhongDuWuGong)){
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
		addNotifyIcon(heartBeatStartTime, game);
	}
	
	public boolean isVisiable(Player p){
		if( p.isOnline() && p.getGame() != null && p.getGame().equals(this.getGameName())
				&& Math.abs(p.getX() - getX()) < this.viewWidth && Math.abs(p.getY() - getY()) < this.viewHeight)
			return true;
		return false;
	}
	
	private void disappear(String reason){
		this.setAlive(false);
		MemoryNPCManager mnm = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
		mnm.removeNPC(this);
		
		MemoryMonsterManager mm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		
		for(Monster m : flushMonsterList){
			mm.removeMonster(m);
			m.setAlive(false);
		}
		StringBuffer sb = new StringBuffer();
		for(Player p : convoyPlayers){
			sb.append(p.getName()+",");
		}
		if (TaskSubSystem.logger.isWarnEnabled()) {
			TaskSubSystem.logger.warn("[护送NPC] [消失] [{}] [{}] [护送玩家：{}]", new Object[]{reason,this.getName(),sb});
		}
		
	}
	
	public void notifyAcceptTeamMemberConvoy(Player p){
		this.convoyPlayers.add(p);
	}
	
	/**
	 * 提示开始护送
	 * @param player
	 */
//	public void notifyStartConvoy(Player player,com.fy.engineserver.task.Task task){}
	
	/**
	 * 到达某个节点
	 * @param cn
	 */
	private void notifyReachNode(Game game,ConvoyNode cn){
		//说话
		if(cn.sayContent != null && cn.sayContent.length() > 0){
			//TODO: NPC说一句话
			
		}
		
		MemoryMonsterManager mm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		for(int i = 0 ; i < cn.count ; i++){
			Monster m = mm.createMonster(cn.monsterCategoryId);
			m.setGameNames(game.gi);
			m.setAlive(true);
			
			
			m.setX(x+ random.nextInt(320) - 320/2);
			m.setY(y + random.nextInt(320) - 320/2);
			if(m.getX() <= 0) m.setX(1);
			if(m.getX() >= game.gi.getWidth()){
				m.setX(game.gi.getWidth()-1);
			}
			if(m.getY() <= 0) m.setY(1);
			if(m.getY() >= game.gi.getHeight()){
				m.setY(game.gi.getHeight()-1);
			}
			
			m.setBornPoint(new Point(m.getX(),m.getY()));
			
			game.addSprite(m);
			
			m.updateDamageRecord(this, 1, 1);
			this.updateDamageRecord(m, 1, 1);
			
			flushMonsterList.add(m);
		}
	}
	
	/**
	 * 到达目的地
	 */
	private void notifyReachDestination(){
		
		//说话
		if(this.sayContentOnEnd != null && this.sayContentOnEnd.length() > 0){
			//TODO: NPC说一句话
		}
		
		if(this.getMoveTrace() != null){
			this.stopAndNotifyOthers();
		}
		
		this.fightingAgent.cancel();
		this.skillAgent.breakExecutingByDead();
		

		// for(Player p : this.convoyPlayers){
		// p.convoyOneNPC(originalNpcName);
		// }
	}
	
	/**
	 * 走向下一个目的地
	 * @param game
	 */
	private void patroling(Game game){
		int dx = 0;
		int dy = 0;
		if(nodeIndex >= nodeList.size()){
			dx = this.destinationX;
			dy = this.destinationY;
		}else{
			ConvoyNode cn = nodeList.get(nodeIndex);
			dx = cn.x;
			dy = cn.y;
		}
		SignPost[] sp  = game.gi.navigator.findPath(new Point(this.x,this.y), new Point(dx,dy));
		
		if (sp != null) {
			short[] roadLength = new short[sp.length + 1];
			Point ps[] = new Point[sp.length + 2];
			int totalLength = 0;
			for (int i = 0; i < sp.length - 1; i++) {
				Road road = game.gi.navigator.getRoadBySignPost(sp[i].id, sp[i + 1].id);
				if (road != null) {
					roadLength[i + 1] = road.len;
					totalLength += road.len;
				} else {
//					Game.logger.warn("["+this.gameName+"] [护送NPC] [寻路] [计算警告] ["+this.getName()+"] [寻路结果路径中，两个相邻的导航点之间没有路！i=" + i + ",id1=" + sp[i].id + ",id2=" + sp[i + 1].id+"]");
					if(Game.logger.isWarnEnabled())
						Game.logger.warn("[{}] [护送NPC] [寻路] [计算警告] [{}] [寻路结果路径中，两个相邻的导航点之间没有路！i={},id1={},id2={}", new Object[]{this.gameName,this.getName(),i,sp[i].id,sp[i + 1].id+"]"});
				}
			}
			for (int i = 0; i < sp.length; i++) {
				ps[i + 1] = new Point( sp[i].x , sp[i].y);
			}
			if (sp.length == 0) {
				roadLength[0] = (short) Graphics2DUtil.sqrt((dx - x) * (dx - x) + (dy - y)*(dy - y));
				ps[0] = new Point( x, y);
				ps[1] = new Point( dx, dy);
				totalLength += roadLength[0];
			} else {
				roadLength[0] = (short) Graphics2DUtil.sqrt((sp[0].x - x) * (sp[0].x - x)
						+ (sp[0].y - y) * (sp[0].y - y));
				ps[0] = new Point( x, y);
				roadLength[sp.length] =(short)  Graphics2DUtil
						.sqrt((sp[sp.length - 1].x - dx) * (sp[sp.length - 1].x - dx)
								+ (sp[sp.length - 1].y - dy) * (sp[sp.length - 1].y - dy));
				ps[sp.length + 1] = new Point( dx, dy);
				totalLength += roadLength[0];
				totalLength += roadLength[sp.length];
			}
			long destTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (totalLength * 1000) / this.getSpeed();
			
			MoveTrace mt = new MoveTrace(roadLength,ps,destTime);
			this.setMoveTrace(mt);
		} else {
//			Game.logger.warn("["+this.gameName+"] [护送NPC] [寻路] [寻路失败] [系统强制清除NPC] ["+this.getName()+"] [start("+x+","+y+")] [end("+dx+","+dy+")]");
			if(Game.logger.isWarnEnabled())
				Game.logger.warn("[{}] [护送NPC] [寻路] [寻路失败] [系统强制清除NPC] [{}] [start({},{})] [end({},{})]", new Object[]{this.gameName,this.getName(),x,y,dx,dy});
			
			this.disappear(Translate.text_5761);
			
		}
	}
	
	public int getSkillLevelById(int sid){
		for(int i = 0 ; activeSkillIds != null && i < this.activeSkillIds.length ; i++){
			if(activeSkillIds[i] == sid && this.activeSkillLevels != null && i < this.activeSkillLevels.length){
				return this.activeSkillLevels[i];
			}
		}
		return 0;
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

	

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		ConvoyNPC p = new ConvoyNPC();
		p.cloneAllInitNumericalProperty(this);
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.country = country;
		
		p.activationHeight = this.activationHeight;
		p.activationType = this.activationType;
		p.activationWidth = this.activationWidth;
		p.activeSkillIds = this.activeSkillIds;
		p.activeSkillLevels = this.activeSkillLevels;

		p.faShuHuDunDamage = this.faShuHuDunDamage;
	
		p.pursueHeight = this.pursueHeight;
		p.pursueWidth = this.pursueWidth;

		p.destinationMapName = destinationMapName;
		p.destinationX = destinationX;
		p.destinationY = destinationY;
		p.sayContentOnEnd = this.sayContentOnEnd;
		p.sayContentOnStart = this.sayContentOnStart;
		p.nodeList.addAll(this.nodeList);
		
		p.windowId = windowId;
		p.id = nextId();
		return p;
	}

	public ActiveSkillAgent getActiveSkillAgent() {
		return this.skillAgent;
	}

	public String getOriginalNpcName() {
		return originalNpcName;
	}

	public void setOriginalNpcName(String originalNpcName) {
		this.originalNpcName = originalNpcName;
	}

	public Task getTask(){
		return task;
	}

	public String getDestinationMapName() {
		return destinationMapName;
	}

	public void setDestinationMapName(String destinationMapName) {
		this.destinationMapName = destinationMapName;
	}

	public int getDestinationX() {
		return destinationX;
	}

	public void setDestinationX(int destinationX) {
		this.destinationX = destinationX;
	}

	public int getDestinationY() {
		return destinationY;
	}

	public void setDestinationY(int destinationY) {
		this.destinationY = destinationY;
	}

	public String getSayContentOnStart() {
		return sayContentOnStart;
	}

	public void setSayContentOnStart(String sayContentOnStart) {
		this.sayContentOnStart = sayContentOnStart;
	}

	public String getSayContentOnEnd() {
		return sayContentOnEnd;
	}

	public void setSayContentOnEnd(String sayContentOnEnd) {
		this.sayContentOnEnd = sayContentOnEnd;
	}
	
	public void addNotifyIcon(long ct, Game game) {
		try {
			if (ct - this.lastPlantBuffTime > 3000 && this.isAlive()
					&& this.getHp() > 0) {

				Fighter fs[] = game.getVisbleFighter(this, true);
				for (int i = 0; i < fs.length; i++) {
					if (fs[i] instanceof Player) {
						Player pp = (Player) fs[i];
						if (pp.isOnline()) {
							NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(
									GameMessageFactory.nextSequnceNum(),
									(byte) 1, this.getId(),
									(byte) Event.DRAW_ICON, 172);
							pp.addMessageToRightBag(req);
						}
					}
				}
				this.lastPlantBuffTime = ct;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return fightingAgent;
	}

}
