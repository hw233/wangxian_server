package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.sprite.Player;

/**
 * 宠物NPC会时间到时把玩家踢出地图
 * 
 * 
 * 
 *
 */
public class DiTuPetNPC extends NPC implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 64281266653370996L;
	
	public static int 加时间的间隔分钟时长 = 1;
	
	public static int MIN_LEVEL = 71;

	private long lastHeartbeatTime;

	public void heartbeat(long heartBeatStartTime, long interval, Game game){

		if(heartBeatStartTime > lastHeartbeatTime + 2000){
			lastHeartbeatTime = heartBeatStartTime;
			if(!game.gi.禁止使用召唤玩家道具){
				game.gi.禁止使用召唤玩家道具 = true;
			}
			if(game.gi.时间地图类型 != GameMap.TIME_MAP_TYPE_PET){
				game.gi.时间地图类型 = GameMap.TIME_MAP_TYPE_PET;
			}
			LivingObject[] los = game.getLivingObjects();
			if(los != null){
				for(LivingObject lo : los){
					if(lo instanceof Player){
						Player p = (Player)lo;

						if(heartBeatStartTime >= p.getLastInPetDiTuTime() + 加时间的间隔分钟时长*60000){
							p.updateLastInPetDiTuTime(heartBeatStartTime, 加时间的间隔分钟时长);
						}

						//没有进入时间或等级不够踢出
						if(!p.isValidInPetDiTuTime() || p.getMainSoul().getGrade() < MIN_LEVEL){
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
		DiTuPetNPC p = new DiTuPetNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
