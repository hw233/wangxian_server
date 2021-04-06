package com.fy.engineserver.sprite.npc.npcaction;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.npc.NPC;
import com.xuanzhi.tools.text.StringUtil;

/**
 * BOSS说话
 * 
 * 支持 $t 标识目标的名字
 * 
 *
 */
public class SaySomething implements NpcAction, Cloneable{

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

	public int getActionId() {
		return actionId;
	}

	
	public String getDescription() {
		return description;
	}

	@Override
	public void doAction(Game game, NPC npc, NpcExecuteItem item, Fighter target, long heartBeatStartTime) {
		if(!isExeAvailable(npc, item, target, heartBeatStartTime)){
			return;
		}
		item.exeTimes++;
		item.lastExeTime = heartBeatStartTime;
		String name = "";
		if(target != null){
			name = target.getName();
		}
		String newC = StringUtil.replace(content, "$t", name);
		int w = npc.getViewWidth();
		int h = npc.getViewHeight();
		npc.setViewWidth(640);
		npc.setViewHeight(640);

		LivingObject fs[] = game.getVisbleLivings(npc, false);
		for(int i = 0 ; i < fs.length ; i++){
			if(fs[i] instanceof Player){
				Player p = (Player)fs[i];
				
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,newC+"第"+item.exeTimes+"次");
				p.addMessageToRightBag(req);
			}
		}
		npc.setViewWidth(w);
		npc.setViewHeight(h);
	
				
	}

	@Override
	public boolean isExeAvailable(NPC npc, NpcExecuteItem item, Fighter target, long heartBeatStartTime) {
		// TODO Auto-generated method stub
		if(!item.turnOnFlag){
			item.turnOnFlag = true;
			item.turnOnTime = heartBeatStartTime;
		}
		if(item.exeTimeLimit > heartBeatStartTime - item.turnOnTime){
			return false;
//			int d = (int)Math.sqrt( (x - target.getX())*(x - target.getX()) + (y - target.getY())*(y - target.getY()));
//			if(d >= item.minDistance && d <= item.maxDistance){
//				if( (item.hpPercent > 0 && 100 * getHp()/this.getTotalHP() <= item.hpPercent)
//						|| (item.hpPercent == 0 && getHp() == 0)){
//					
//				}
//			}
		}
		if(item.exeTimes >= item.maxExeTimes){
			return false;
		}
		if(item.lastExeTime + item.exeTimeGap > heartBeatStartTime){
			return false;
		}
		return true;
	}
@Override
	public Object clone(){
		// TODO Auto-generated method stub
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
