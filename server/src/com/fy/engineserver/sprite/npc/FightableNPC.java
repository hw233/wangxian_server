package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.datasource.skill.ActiveSkillAgent;
import com.fy.engineserver.sprite.Fighter;

public interface FightableNPC extends Fighter{

	public void stopAndNotifyOthers();
	
	public void targetDisappear(Fighter target);
	
	public boolean isStun();
	
	public boolean isHold();
	
	public int getSpeed();
	
	public void setMoveTrace(MoveTrace m);
	
	public MoveTrace getMoveTrace();
	
	public ActiveSkillAgent getActiveSkillAgent();
	
	public NPCFightingAgent getNPCFightingAgent();
	public int getPursueWidth();
	
	public int getPursueHeight();
}
