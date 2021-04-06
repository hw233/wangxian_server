package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.npc.DoorNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;

/**
 * 关门
 * 
 * 
 *
 */
public class BossCloseDoor implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	int [] doorNpcCategoryId;
	int[] doorX;
	int[] doorY;
	
	
	public int[] getDoorNpcCategoryId() {
		return doorNpcCategoryId;
	}

	public void setDoorNpcCategoryId(int[] doorNpcCategoryId) {
		this.doorNpcCategoryId = doorNpcCategoryId;
	}

	public int[] getDoorX() {
		return doorX;
	}

	public void setDoorX(int[] doorX) {
		this.doorX = doorX;
	}

	public int[] getDoorY() {
		return doorY;
	}

	public void setDoorY(int[] doorY) {
		this.doorY = doorY;
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
		
		MemoryNPCManager mmm = (MemoryNPCManager)com.fy.engineserver.sprite.npc.MemoryNPCManager.getNPCManager();
		for(int i = 0 ; doorNpcCategoryId != null && i < doorNpcCategoryId.length ; i++){
			DoorNpc m = (DoorNpc)mmm.createNPC(doorNpcCategoryId[i]);
			if(m != null){
				m.setX(doorX[i]);
				m.setY(doorY[i]);
				m.setBornPoint(new Point(doorX[i],doorY[i]));
				m.setAlive(true);
				game.addSprite(m);
				boss.getFlushNPCList().add(m);
				m.closeDoor(game);
				if(BossMonster.baLogger.isInfoEnabled()){
//					BossMonster.baLogger.info("[BOSS刷出门NPC] [成功] ["+boss.getName()+"] ["+m.getName()+"]");
					if(BossMonster.baLogger.isInfoEnabled())
						BossMonster.baLogger.info("[BOSS刷出门NPC] [成功] [{}] [{}]", new Object[]{boss.getName(),m.getName()});
				}
			}else{
				if(BossMonster.baLogger.isInfoEnabled()){
//					BossMonster.baLogger.info("[BOSS刷出门NPC] [失败] ["+boss.getName()+"] [对应的npc"+doorNpcCategoryId[i]+"不存在]");
					if(BossMonster.baLogger.isInfoEnabled())
						BossMonster.baLogger.info("[BOSS刷出门NPC] [失败] [{}] [对应的npc{}不存在]", new Object[]{boss.getName(),doorNpcCategoryId[i]});
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
