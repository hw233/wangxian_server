package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * 检测指定的同伴，是否处于死亡状态
 * 如果处于死亡状态，那么消失
 * 
 * 
 *
 */
public class BossDisappearWithOtherBossDead implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	String targetBossName;
	
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
		BossMonster targetBM = null;
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof BossMonster){
				BossMonster bm = (BossMonster)los[i];
				if(bm.getName().equals(targetBossName)){
					targetBM = bm;
				}
			}
		}
		if(targetBM != null && targetBM.isDeath()){
			boss.setAlive(false);
		}
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
