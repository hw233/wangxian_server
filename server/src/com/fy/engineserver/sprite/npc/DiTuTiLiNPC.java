package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 体力NPC会消耗本地图上的玩家的体力，当玩家体力为0或者时间到时把玩家踢出地图
 * 
 * 
 * 
 *
 */
public class DiTuTiLiNPC extends NPC implements Cloneable{

	private static final long serialVersionUID = 1L;
	
	public static int 扣体力的间隔分钟时长 = 5;
	
	public static int MIN_LEVEL = 71;

	private long lastHeartbeatTime;

	public void heartbeat(long heartBeatStartTime, long interval, Game game){

		if(heartBeatStartTime > lastHeartbeatTime + 2000){
			lastHeartbeatTime = heartBeatStartTime;
			if(!game.gi.禁止使用召唤玩家道具){
				game.gi.禁止使用召唤玩家道具 = true;
			}
			if(game.gi.时间地图类型 != GameMap.TIME_MAP_TYPE_TILI){
				game.gi.时间地图类型 = GameMap.TIME_MAP_TYPE_TILI;
			}
			LivingObject[] los = game.getLivingObjects();
			if(los != null){
				for(LivingObject lo : los){
					if(lo instanceof Player){
						Player p = (Player)lo;
						if(p.getVitality() > 0){
							if(heartBeatStartTime >= p.getLastInTiLiDiTuTime() + 扣体力的间隔分钟时长*60000){
								p.updateLastInTiLiDiTuTime(heartBeatStartTime, 扣体力的间隔分钟时长);
								p.setVitality(p.getVitality() - 1);
								if (GamePlayerManager.logger.isWarnEnabled()) {
									GamePlayerManager.logger.warn("["+p.getLogString()+"]" + "[地图扣除体力] [扣除后:" + p.getVitality() + "]");
								}
							}
						}else{
							game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
							Game.logger.warn("[玩家体力耗光踢出] ["+p.getLogString()+"] ["+game.gi.displayName+"]");
						}
						//没有进入时间或等级不够踢出
						if(!p.isValidInTiLiDiTuTime() || p.getMainSoul().getGrade() < MIN_LEVEL){
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
		DiTuTiLiNPC p = new DiTuTiLiNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}


	
}
