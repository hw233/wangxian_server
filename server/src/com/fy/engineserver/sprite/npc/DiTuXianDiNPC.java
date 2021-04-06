package com.fy.engineserver.sprite.npc;

import java.io.Serializable;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.sprite.Player;

/**
 * 仙蒂地图心跳npc
 * 
 *
 */
public class DiTuXianDiNPC extends NPC implements Cloneable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7406348746950804555L;

	public static int 加时间的间隔分钟时长 = 1;
	
	public static int MIN_LEVEL = 221;

	private long lastHeartbeatTime;

	public void heartbeat(long heartBeatStartTime, long interval, Game game){

		if(heartBeatStartTime > lastHeartbeatTime + 2000){
			lastHeartbeatTime = heartBeatStartTime;
			if(!game.gi.禁止使用召唤玩家道具){
				game.gi.禁止使用召唤玩家道具 = true;
			}
			if(game.gi.时间地图类型 != GameMap.TIME_MAP_TYPE_XIANDI){
				game.gi.时间地图类型 = GameMap.TIME_MAP_TYPE_XIANDI;
			}
			LivingObject[] los = game.getLivingObjects();
			if(los != null){
				for(LivingObject lo : los){
					if(lo instanceof Player){
						Player p = (Player)lo;

						if(heartBeatStartTime >= p.getLastInXianDIDiTuTime() + 加时间的间隔分钟时长*60000){
							p.updateLastInXianDiDiTuTime(heartBeatStartTime, 加时间的间隔分钟时长);
						}

						//没有进入时间或等级不够踢出
						if(!p.isValidInXianDiDiTuTime() || p.getMainSoul().getGrade() < MIN_LEVEL){
							game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
							Game.logger.warn("[玩家没有进入时间或等级不够踢出] ["+p.getLogString()+"] [level:"+p.getMainSoul().getGrade()+"] ["+game.gi.displayName+"]");
						}
					}
				}
			}
		}
	}

	/**
	 * clone出一个对象
	 */
	public Object clone() {
		DiTuXianDiNPC p = new DiTuXianDiNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
