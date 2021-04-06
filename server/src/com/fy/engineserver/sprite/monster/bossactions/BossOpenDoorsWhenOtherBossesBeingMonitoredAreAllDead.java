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

public class BossOpenDoorsWhenOtherBossesBeingMonitoredAreAllDead implements BossAction{
	String[] bossNames;
	String[] doorNames;
	
//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	public void doAction(Game game, BossMonster boss,Fighter target) {
		if(bossNames == null || bossNames.length == 0){
			if(logger.isWarnEnabled())
				logger.warn("[BOSS动作][BossOpenDoorsWhenOtherBossesBeingMonitoredAreAllDead][没有配置监视的BOSS]");
			return;
		}
		
		if(doorNames == null || doorNames.length == 0){
			if(logger.isWarnEnabled())
				logger.warn("[BOSS动作][BossOpenDoorsWhenOtherBossesBeingMonitoredAreAllDead][没有配置门]");
			return;
		}
		
		
		String dcName = "--";
		if(game.getDownCity() != null){
			dcName = game.getDownCity().getId();
		}
		
		
		LivingObject los[] = null;
		
		boolean allDead = true;
		
		los = game.getLivingObjects();
		
		for(String bossName : bossNames){
			boolean exist = false;
			for(LivingObject lo : los){
				if(lo instanceof BossMonster){
					BossMonster bm = (BossMonster) lo;
					if(bm.getName().equals(bossName) && bm.getHp() > 0){
						exist = true;
						
//						BossMonster.baLogger.info("["+dcName+"] ["+game.getGameInfo().getName()+"] [执行其他BOSS死亡开门条目] [有BOSS没有死] [没有死的boss："+bm.getName()+"] ["+boss.getName()+"] ["+description+"]");
						if(BossMonster.baLogger.isInfoEnabled())
							BossMonster.baLogger.info("[{}] [{}] [执行其他BOSS死亡开门条目] [有BOSS没有死] [没有死的boss：{}] [{}] [{}]", new Object[]{dcName,game.getGameInfo().getName(),bm.getName(),boss.getName(),description});
						
						break;
					}
				}
			}
			
			if(exist){
				allDead = false;
				
				break;
			}
		}
		
		if(!allDead){
			return;
		}
		
		los = game.getLivingObjects();
		
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof DoorNpc){
				DoorNpc dn = (DoorNpc)los[i];
				for(String dns : doorNames){
					if(dn.getName().equals(dns)){
						dn.openDoor(game);
//						BossMonster.baLogger.info("["+dcName+"] ["+game.getGameInfo().getName()+"] [执行其他BOSS死亡开门条目] [开门] ["+boss.getName()+"] ["+dn.getName()+"] ["+description+"]");
						if(BossMonster.baLogger.isInfoEnabled())
							BossMonster.baLogger.info("[{}] [{}] [执行其他BOSS死亡开门条目] [开门] [{}] [{}] [{}]", new Object[]{dcName,game.getGameInfo().getName(),boss.getName(),dn.getName(),description});

					}					
				}
			}
		}
	}
	
	public boolean isExeAvailable(BossMonster boss, Fighter target) {
		return true;
	}
	

	public String[] getBossNames() {
		return bossNames;
	}

	public void setBossNames(String[] bossNames) {
		this.bossNames = bossNames;
	}

	public String[] getDoorNames() {
		return doorNames;
	}


	public void setDoorNames(String[] doorNames) {
		this.doorNames = doorNames;
	}


	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}
