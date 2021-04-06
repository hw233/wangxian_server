package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;

/**
 * 金猴天灾火圈NPC
 * 
 * @date on create 2016年3月7日 上午10:50:45
 */
public class DisasterFireNPC extends NPC implements Cloneable, FightableNPC{
	/** npc状态，0正常 1预热 2燃烧（燃烧时带伤害） */
	private byte npcStatus;
	/** 玩家上次受到伤害时间 */
	private Map<Long, Long> lastDmgTime = new ConcurrentHashMap<Long, Long>();
	/** 玩家站在火圈里边受到伤害间隔 */
	public static final long DMG_INTEVAL = 1000;
	/** 上次改变状态时间 */
	private long lastChangeStatusTime;
	/** 触发CD */
	private long[] triggerCDs;
	/** 伤害值 */
	private int damage;
	/** 每次触发后随机触发cd */
	private long triggerCD;
	/** 椭圆的a^2 b^2 c^2 */
	public static float[] range = new float[] { 12000, 1800, 1225};
	
	public static int temp11 = -30;
	//怪的内部状态，注意此状态, 0表示空闲，1表示激活，2表示回程
	protected transient int innerState = 0;
	
	public static Random ran = new Random(System.currentTimeMillis());

	public static long 预热时间 = 3000;
	public static long 火焰时间 = 1000;
	
	protected NPCFightingAgent fightAgent = new NPCFightingAgent(this);
	
	protected ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);

	@Override
	public byte getNpcType() {
		return NPC_TYPE_DISASTERFIRE;
	}

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		// TODO Auto-generated method stub
		super.heartbeat(heartBeatStartTime, interval, game);
		// 
		this.skillAgent.heartbeat(game);
		this.fightAgent.heartbeat(heartBeatStartTime, game);
		if (npcStatus == 2) { // 燃烧状态
			List<Player> playerList = new ArrayList<Player>(); // 在火圈中的玩家列表
			LivingObject[] los = game.getLivingObjects();
			if (los != null) {
				for (LivingObject lo : los) {
					if (lo instanceof Player) {
						float dx = lo.getX() - this.getX();
						float dy = lo.getY() - (this.getY()-temp11);
						float f = dx * dx / range[0] +dy * dy / range[1]; 
						if (f <= 1) {
							playerList.add((Player) lo);
						}
					}
				}
			}
			
			for (int i = 0; i < playerList.size(); i++) {
				Long lastTime = lastDmgTime.get(playerList.get(i).getId());
				if (lastTime != null && (heartBeatStartTime - lastTime) <= DMG_INTEVAL) {
					continue;
				}
				lastDmgTime.put(playerList.get(i).getId(), heartBeatStartTime);
				// 玩家受到伤害
				DisasterManager.getInst().causeDamage(playerList.get(i), damage);
			}
		}
		this.checkStatusChange(heartBeatStartTime);
	}
	
//	public void playParticl() {
//		ParticleData[] particleDatas = new ParticleData[1];
//		particleDatas[0] = new ParticleData(this.getId(), "活动特效/空岛熔岩", 2000, 2, 1, 1);
//		NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
//	}
	
	public static String[] avataActions = new String[]{"攻击1", "攻击2", "攻击3"};
	
	public static String[] partNames = new String[]{"活动特效/空岛熔岩预警", "活动特效/空岛熔岩爆炸"};

	/**
	 * 检测npc状态
	 * @param heartBeatTime
	 */
	public void checkStatusChange(long heartBeatTime) {
		if (lastChangeStatusTime <= 0) {
			lastChangeStatusTime = heartBeatTime;
		}
		if (!avataActions[0].equals(this.getAvataAction())) {
			this.setAvataAction(avataActions[0]);
		}
		if (npcStatus == 0 && (heartBeatTime - lastChangeStatusTime) >= triggerCD) {
			// 变成预热状态
			npcStatus = 1;
			lastChangeStatusTime = heartBeatTime;
//			this.setAvataAction(avataActions[1]);
			this.setFootParticleName(partNames[0]);
			if (triggerCDs.length > 1) {
				triggerCD = triggerCDs[ran.nextInt(triggerCDs.length)];
			}
		}
		if (npcStatus == 1 && (heartBeatTime - lastChangeStatusTime) >= 预热时间) {
			npcStatus = 2;
			this.setFootParticleName(partNames[1]);
			lastChangeStatusTime = heartBeatTime;
//			this.setAvataAction(avataActions[2]);
		}
		if (npcStatus == 2 && (heartBeatTime - lastChangeStatusTime) >= 火焰时间) {
			npcStatus = 0;
			lastChangeStatusTime = heartBeatTime;
			this.setFootParticleName("");
		}
	}

	@Override
	public Object clone() {
		DisasterFireNPC p = new DisasterFireNPC();
		p.cloneAllInitNumericalProperty(this);
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.setNpcType(NPC_TYPE_DISASTERFIRE);
		p.setTriggerCDs(this.getTriggerCDs());
		p.setDamage(this.getDamage());
		int tempIndex = 0;
		if (this.getTriggerCDs().length > 1) {
			tempIndex = ran.nextInt(this.getTriggerCDs().length);
		}
		p.setTriggerCD(this.getTriggerCDs()[tempIndex]);
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
		return p;

	}

	public byte getNpcStatus() {
		return npcStatus;
	}

	public void setNpcStatus(byte npcStatus) {
		this.npcStatus = npcStatus;
	}

	public Map<Long, Long> getLastDmgTime() {
		return lastDmgTime;
	}

	public void setLastDmgTime(Map<Long, Long> lastDmgTime) {
		this.lastDmgTime = lastDmgTime;
	}

	public long getLastChangeStatusTime() {
		return lastChangeStatusTime;
	}

	public void setLastChangeStatusTime(long lastChangeStatusTime) {
		this.lastChangeStatusTime = lastChangeStatusTime;
	}

	public long[] getTriggerCDs() {
		return triggerCDs;
	}

	public void setTriggerCDs(long[] triggerCDs) {
		this.triggerCDs = triggerCDs;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public long getTriggerCD() {
		return triggerCD;
	}

	public void setTriggerCD(long triggerCD) {
		this.triggerCD = triggerCD;
	}
	
	@Override
	/**
	 * 火圈NPC视所有生物为敌人
	 */
	public int getFightingType(Fighter fighter) {
		return Fighter.FIGHTING_TYPE_ENEMY;
	}

	@Override
	public void targetDisappear(Fighter target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActiveSkillAgent getActiveSkillAgent() {
		// TODO Auto-generated method stub
		return skillAgent;
	}

	@Override
	public NPCFightingAgent getNPCFightingAgent() {
		// TODO Auto-generated method stub
		return fightAgent;
	}

	@Override
	public int getPursueWidth() {
		// TODO Auto-generated method stub
		return DisasterConstant.viewWith;
	}

	@Override
	public int getPursueHeight() {
		// TODO Auto-generated method stub
		return DisasterConstant.viewHeight;
	}
}
