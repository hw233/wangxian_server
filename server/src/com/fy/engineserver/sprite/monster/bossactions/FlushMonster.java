package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * BOSS刷出新的怪来
 *  
 * 
 *
 */
public class FlushMonster implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	//什么点上，刷出什么怪
	int monsterCategoryId[];
	
	//刷新其他怪后，这个怪是否消失，默认不消失
	public boolean afterFlushDisappear = false;
	
	public int[] getMonsterCategoryId() {
		return monsterCategoryId;
	}

	public void setMonsterCategoryId(int[] monsterCategoryId) {
		this.monsterCategoryId = monsterCategoryId;
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

	Random random = new Random();
	public void doAction(Game game, BossMonster boss,Fighter target) {
		
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		if(monsterCategoryId != null){
			for(int i = 0 ; i < monsterCategoryId.length ; i++){
				Monster m = mmm.createMonster(monsterCategoryId[i]);
				if(m != null){
					m.setX(boss.getX()+random.nextInt(100) - 50);
					m.setY(boss.getY()+random.nextInt(100) - 50);
					m.setBornPoint(new Point(m.getX(),m.getY()));
					m.setAlive(true);
					game.addSprite(m);
					
					boss.getFlushMonsterList().add(m);
				}
			}
		}
		
		if(afterFlushDisappear){
			game.removeSprite(boss);
		}
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
