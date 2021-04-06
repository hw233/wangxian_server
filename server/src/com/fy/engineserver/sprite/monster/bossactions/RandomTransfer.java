package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.SET_POSITION_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;

/**
 * 随机将BOSS传送到一个地点
 * 地点是配置好的一组地点中的一个
 * 
 * 
 *
 */
public class RandomTransfer implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	int x[];
	int y[];
	
	
	
	public int[] getX() {
		return x;
	}

	public void setX(int[] x) {
		this.x = x;
	}

	public int[] getY() {
		return y;
	}

	public void setY(int[] y) {
		this.y = y;
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
		boss.stopAndNotifyOthers();
		int k = (int)(Math.random() * x.length);
		
		SET_POSITION_REQ req = new SET_POSITION_REQ(
				GameMessageFactory.nextSequnceNum(), (byte)Game.REASON_CLIENT_STOP,
				boss.getClassType(), boss.getId(), (short) x[k], (short) y[k]);
		
		int w = boss.getViewWidth();
		int h = boss.getViewHeight();
		boss.setViewWidth(1280);
		boss.setViewHeight(1280);
		Fighter fs[] = game.getVisbleFighter(boss, false);
		for(int i = 0 ; i < fs.length ; i++){
			if(fs[i] instanceof Player){
				Player p = (Player)fs[i];
				p.addMessageToRightBag(req);
			}
		}
		boss.setX(x[k]);
		boss.setY(y[k]);
		boss.setViewWidth(w);
		boss.setViewHeight(h);
		
	}

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

}
