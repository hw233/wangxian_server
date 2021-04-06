package com.fy.engineserver.sprite.npc.npcaction;

//import org.apache.log4j.Logger;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.MoveTrace;
import com.fy.engineserver.core.g2d.Graphics2DUtil;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.g2d.Road;
import com.fy.engineserver.core.g2d.SignPost;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.BossAction;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * BOSS说话
 * 
 * 支持 $t 标识目标的名字
 * 
 *
 */
public class FindMovePathAndMove implements NpcAction, Cloneable{

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
		if(GameManager.logger.isInfoEnabled())
			GameManager.logger.info("["+npc.getName()+"] [start find path]");
		SignPost sp = randomGamePoint(game);
		pathFinding(game, npc, sp.x, sp.y);
		if(GameManager.logger.isInfoEnabled())
			GameManager.logger.info("["+npc.getName()+"] ["+randomGamePoint(game).x+"] ["+randomGamePoint(game).y+"]");
	}

	@Override
	public boolean isExeAvailable(NPC npc, NpcExecuteItem item, Fighter target, long heartBeatStartTime) {
		// TODO Auto-generated method stub
		if(!item.turnOnFlag){
			item.turnOnFlag = true;
			item.turnOnTime = heartBeatStartTime;
		}
		if(npc.getMoveTrace() != null){
			return false;
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

	long lastTimeForPatroling;
	public long patrolingTimeInterval = 5000;
	public short range = 600;
//	short patrolingHeight;
	Random random = new Random();

	public SignPost randomGamePoint(Game game){
		SignPost sp = game.gi.navigator.getSignPost(random.nextInt(game.gi.navigator.getSignPostNum()));
		return sp;
	}

	protected void pathFinding(Game de,Sprite sprite,int dx,int dy){
		int distance = 20;
		Point s = new Point(sprite.getX(),sprite.getY());
		Point e = new Point(dx,dy);
		int L = Graphics2DUtil.distance(s, e);
		int speed = 1;
		if(sprite.getSpeed() > 0){
			speed = sprite.getSpeed();
		}
		if(L > distance && de.getGameInfo().navigator.isVisiable(s.x,s.y,e.x,e.y)){
			int L1 = L - distance;
			int L2 = distance;
			
			Point ps[] = new Point[2];
			short roadLen[] = new short[1];
			ps[0] = new Point(s.x,s.y);
			ps[1] = new Point((L1 * e.x + L2 * s.x)/L,(L1 * e.y + L2 * s.y)/L);
			roadLen[0] = (short) L1;
			MoveTrace path = new MoveTrace(roadLen,ps,(long)(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + (long)L1*1000/speed));
			sprite.setMoveTrace(path);
		}else if(L > distance ){
			SignPost sps[] = de.getGameInfo().navigator.findPath(s, e);
			if(sps == null) return;
			Point ps[] = new Point[sps.length+1];
			short roadLen[] = new short[sps.length];
			for(int i = 0 ; i < ps.length ; i++){
				if(i == 0){
					ps[i] = new Point(s.x,s.y);
				}else{
					ps[i] = new Point(sps[i-1].x,sps[i-1].y);
				}
			}
			int totalLen = 0;
			for(int i = 0 ; i < roadLen.length ; i++){
				if(i == 0){
					roadLen[i] =(short) Graphics2DUtil.distance(ps[0], ps[1]);
				}else{
					Road r =  de.getGameInfo().navigator.getRoadBySignPost(sps[i-1].id, sps[i].id);
					if(r != null){
						roadLen[i] = r.len;
					}else{
						roadLen[i] = (short)Graphics2DUtil.distance(ps[i], ps[i+1]);
					}
				}
				totalLen+= roadLen[i];
			}
			MoveTrace path = new MoveTrace(roadLen,ps,com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + totalLen*1000/speed);
			sprite.setMoveTrace(path);
		}
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
