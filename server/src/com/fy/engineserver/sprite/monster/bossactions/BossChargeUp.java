package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * BOSS冲锋
 * 
 * 
 *
 */
public class BossChargeUp implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	
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
		int x = target.getX() - boss.getX();
		int y = target.getY() - boss.getY();
		int dx = 0;
		int dy = 0;
		if(x < 0 && Math.abs(y) < Math.abs(x)){
			dx = target.getX() + 20;
			dy = target.getY();
		}else if(x > 0 && Math.abs(y) < Math.abs(x)){
			dx = target.getX() - 20;
			dy = target.getY();
		}else if(y < 0){
			dx = target.getX();
			dy = target.getY()+20;

		}else{
			dx = target.getX();
			dy = target.getY() - 20;

		}
		boss.setSpeed((short)(boss.getSpeed()*2));
		boss.getBossFightingAgent().pathFinding(game, dx, dy);
		boss.setSpeed((short)(boss.getSpeed()/2));
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
