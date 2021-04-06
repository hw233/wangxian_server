package com.fy.engineserver.sprite.monster.bossactions;


//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;


/**
 * BOSS逃跑，逃跑的目的的是给定的点中随机
 * 
 * 
 *
 */
public class RunawayWithGivenPoints implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	int pointX[];
	int pointY[];

	
	


	public int[] getPointX() {
		return pointX;
	}

	public void setPointX(int[] pointX) {
		this.pointX = pointX;
	}

	public int[] getPointY() {
		return pointY;
	}

	public void setPointY(int[] pointY) {
		this.pointY = pointY;
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
		int k = (int)(Math.random() * pointX.length);
		boss.getBossFightingAgent().pathFinding(game, pointX[k], pointY[k]);
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
