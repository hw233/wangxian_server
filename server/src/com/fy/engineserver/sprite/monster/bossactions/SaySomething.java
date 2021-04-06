package com.fy.engineserver.sprite.monster.bossactions;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.xuanzhi.tools.text.StringUtil;

/**
 * BOSS说话
 * 
 * 支持 $t 标识目标的名字
 * 
 *
 */
public class SaySomething implements BossAction{

//	static Logger logger = Logger.getLogger(BossAction.class);
public	static Logger logger = LoggerFactory.getLogger(BossAction.class);
	
	//动作的唯一标识
	int actionId;
	//动作使用的主动技能Id

	String description;
	
	String content;

	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		
		String newC = StringUtil.replace(content, "$t", target.getName());
		
		int w = boss.getViewWidth();
		int h = boss.getViewHeight();
		boss.setViewWidth(640);
		boss.setViewHeight(640);

		Fighter fs[] = game.getVisbleFighter(boss, false);
		for(int i = 0 ; i < fs.length ; i++){
			if(fs[i] instanceof Player){
				Player p = (Player)fs[i];
				
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)5,newC);
				p.addMessageToRightBag(req);
			}
		}
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
