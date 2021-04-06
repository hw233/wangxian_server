package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuFaGong;
import com.fy.engineserver.datasource.buff.Buff_ZhongDuWuGong;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.TRANSIENTENEMY_CHANGE_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;

/**
 * 翅膀副本npc
 * 
 *
 */
public class WingCarbonNPC extends NPC /*implements FightableNPC*/{

	/**
	 * 怪出生后，调用此初始化方法
	 */
	public void init(){
		super.init();
		
		
		this.commonAttackSpeed = 2000;
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
	 * 在心跳函数中调用此方法
	 * 
	 * 表明此怪被杀死，此方法只能被调用一次
	 * 
	 * 此方法将处理经验值掉落，物品掉落等。
	 */
	protected void killed(long heartBeatStartTime, long interval, Game game) {
		super.killed(heartBeatStartTime, interval, game);
	}

	@Override
	/**
	 * 翅膀副本npc  怪为敌人  其他中立
	 */
	public int getFightingType(Fighter fighter) {
		if (fighter instanceof Monster) {
			return Fighter.FIGHTING_TYPE_ENEMY;
		} else if (fighter instanceof BossMonster) {
			return Fighter.FIGHTING_TYPE_ENEMY;
		}
		return Fighter.FIGHTING_TYPE_NEUTRAL;
	}
	public static int color1Percent = 70;
	public static int color2Percent = 30;
	public static String startStr1 = "<f color='0x00ff00'>";
	public static String startStr2 = "<f color='0xffff00'>";
	public static String startStr3 = "<f color='0xff0000'>";
	
	public long wudiTime = -1;
	
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
//		long now = System.currentTimeMillis();
		super.heartbeat(heartBeatStartTime, interval, game);
		if(true){
			return;
		}
//		this.notifyPlayerAndAddTransientEnemyList(game);
		String startS = "";
		String endS = "</f>";
		if (heartBeatStartTime > wudiTime) {
			int percent = (int) (((double)this.getHp() / (double)this.getMaxHP())*100d);
			if (percent >= color1Percent) {
				startS = startStr1;
			} else if (percent >= color2Percent) {
				startS = startStr2;
			} else {
				startS = startStr3;
			}
			this.setTitle(startS + this.getHp() + endS);
		} else {
			int leftTime = (int) ((wudiTime - heartBeatStartTime) / 1000);
			startS = "<f size='30' color='0xffff00'>";
			this.setTitle(startS + leftTime + endS);
		}
//		this.setTitle(this.getHp() + "/" + this.getMaxHP());
		if ((heartBeatStartTime - lastHeartBeatTime) > 500) {
			lastHeartBeatTime = heartBeatStartTime;
			synchronized (buffs) {
				for (int i = buffs.size() - 1; i >= 0; i--) {
					Buff buff = buffs.get(i);
					if (buff != null) {

						// 玩家不在线，不算游戏时间
//						if (buff.getTemplate() != null && buff.getTemplate().getTimeType() == 0 ) continue;

						if (buff.getInvalidTime() <= heartBeatStartTime) {
							buff.end(this);
							if (buff.isForover() || buff.isSyncWithClient()) {
								this.removedBuffs.add(buff);
							}
							buffs.remove(buff);
						} else {
							buff.heartbeat(this, heartBeatStartTime, interval, game);
							if (buff.getInvalidTime() <= heartBeatStartTime) {
								buff.end(this);

								if (buff.isForover() || buff.isSyncWithClient()) {
									this.removedBuffs.add(buff);
								}
								buffs.remove(buff);
							}
						}
					}
				}
			}
		}
//		lastHeartBeatTime = now;
	}
	
	private long lastHeartBeatTime = 0;
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	private transient ArrayList<Buff> removedBuffs = new ArrayList<Buff>();
	private transient ArrayList<Buff> newlyBuffs = new ArrayList<Buff>();
	
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
						//GuozhanOrganizer.logger.debug("[trace_NPC敌人列表B] ["+this.getName()+"] [player:"+fighter.getLogString()+"] [fightType:"+getFightingType(fighter)+"]");
					}
				}
				//GuozhanOrganizer.logger.debug("[trace_NPC敌人列表A] ["+this.getName()+"] [player:"+fighter.getLogString()+"] [fightType:"+getFightingType(fighter)+"] [x:"+fighter.getX()+"] [y:"+fighter.getY()+"]");
			}
		}

	}
	
	
	int activationWidth = 300;
	int activationHeight = 300;
	
	/**
	 * clone出一个对象
	 */
	public Object clone() {
		WingCarbonNPC p = new WingCarbonNPC();

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
}
