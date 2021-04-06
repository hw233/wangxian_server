package com.fy.engineserver.sprite.monster.bossactions;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

public class BossStatDownCityFinish implements BossAction {
//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	int[] monsterIds = new int[0];
	
	
	
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int[] getMonsterIds() {
		return monsterIds;
	}

	public void setMonsterIds(int[] monsterIds) {
		this.monsterIds = monsterIds;
	}

	
	public void doAction(Game game, BossMonster boss, Fighter target) {
		// TODO Auto-generated method stub
		DownCity dc = game.getDownCity();
		if(dc == null){
//			logger.warn("[BOSS执行动作] [BOSS报告副本结束] [失败] [无法获得副本进度] ["+boss.getSpriteCategoryId()+"] ["+boss.getName()+"]");
			if(logger.isWarnEnabled())
				logger.warn("[BOSS执行动作] [BOSS报告副本结束] [失败] [无法获得副本进度] [{}] [{}]", new Object[]{boss.getSpriteCategoryId(),boss.getName()});
			return;
		}
		
		Set<Integer> set = new HashSet<Integer>();
		for(int mid:monsterIds){
			set.add(mid);
		}
		
		LivingObject los[] = game.getLivingObjects();
		
		boolean allDead = true;
		for(LivingObject o : los){
			if(o instanceof BossMonster){
				BossMonster bm = (BossMonster) o;
				if(set.contains(bm.getSpriteCategoryId())){
					if(bm.getHp() > 0){
						allDead = false;
						break;
					}
				}
			}
		}
		
		if(allDead){
			dc.副本通关操作();
//			logger.warn("[BOSS执行动作] [BOSS报告副本结束] [成功] [副本结束] ["+dc.getId()+"]");
			if(logger.isWarnEnabled())
				logger.warn("[BOSS执行动作] [BOSS报告副本结束] [成功] [副本结束] [{}]", new Object[]{dc.getId()});
		}
	}

	
	public int getActionId() {
		// TODO Auto-generated method stub
		return actionId;
	}

	
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	
	public boolean isExeAvailable(BossMonster boss, Fighter target) {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
