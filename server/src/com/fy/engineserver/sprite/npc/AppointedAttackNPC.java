package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.npcaction.NpcExecuteItem;
/**
 * 攻击特定目标的npc
 * 
 * @date on create 2016年4月6日 下午3:51:34
 */
public class AppointedAttackNPC extends NPC implements Cloneable, FightableNPC{
	
	protected NPCFightingAgent fightAgent = new NPCFightingAgent(this);
	
	protected ActiveSkillAgent skillAgent = new ActiveSkillAgent(this);
	/** 攻击目标(需要在创建时指定) */
	private Fighter f;
	
	@Override
	public byte getNpcType() {
		return NPC_TYPE_DISASTERFIRE;
	}
	
	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game g) {
		// TODO Auto-generated method stub
		super.heartbeat(heartBeatStartTime, interval, g);
		fightAgent.heartbeat(heartBeatStartTime, g);
		skillAgent.heartbeat(g);
	}


	@Override
	public Object clone() {
		AppointedAttackNPC p = new AppointedAttackNPC();
		p.cloneAllInitNumericalProperty(this);
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
		return p;

	}
	
	
	@Override
	public Fighter getMaxHatredFighter() {
		return getF();
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
	public int getFightingType(Fighter fighter){
		if (fighter == null) {
			return Fighter.FIGHTING_TYPE_FRIEND;
		}
		if (fighter instanceof Player) {
			return FIGHTING_TYPE_ENEMY;
		}
		if (fighter.getId() != this.getF().getId()) {
			return Fighter.FIGHTING_TYPE_FRIEND;
		}
		return Fighter.FIGHTING_TYPE_ENEMY;
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


	public Fighter getF() {
		return f;
	}


	public void setF(Fighter f) {
		this.f = f;
	}

	public int getViewWidth() {
		return DisasterConstant.viewWith;
	}

	public int getViewHeight() {
		return DisasterConstant.viewHeight;
	}
}
