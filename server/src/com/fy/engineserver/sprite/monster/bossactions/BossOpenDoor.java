package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.npc.DoorNpc;

/**
 * 关门
 * 
 * 
 *
 */
public class BossOpenDoor implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	String[] doorName;
	
	public String[] getDoorName() {
		return doorName;
	}

	public void setDoorName(String[] doorName) {
		this.doorName = doorName;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isExeAvailable(BossMonster boss,Fighter target){
		return true;
	}
	
	public void doAction(Game game, BossMonster boss,Fighter target) {
		LivingObject los[] = game.getLivingObjects();
		
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof DoorNpc){
				DoorNpc dn = (DoorNpc)los[i];
				for(String dns : doorName){
					if(dn.getName().equals(dns)){
						dn.openDoor(game);
//						BossMonster.baLogger.info("[执行BOSS开门条目] ["+boss.getName()+"] ["+dn.getName()+"] ["+description+"]");
						if(BossMonster.baLogger.isInfoEnabled())
							BossMonster.baLogger.info("[执行BOSS开门条目] [{}] [{}] [{}]", new Object[]{boss.getName(),dn.getName(),description});
					}					
				}
			}
		}
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
