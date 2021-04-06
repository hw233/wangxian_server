package com.fy.engineserver.sprite.npc;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.GameMap;
import com.fy.engineserver.sprite.Player;

public class DituLimitNPC extends NPC implements Cloneable{
	/** 地图类型  0为新多宝 */
	private int dituType;
	/** 进入等级限制 */
	private int levelLimit;
	
	private long lastHeartbeatTime;
	
	public static int 加时间的间隔分钟时长 = 1;
	
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
							p.updateLastInLimitDituTime(dituType,heartBeatStartTime, 加时间的间隔分钟时长);
						}

						//没有进入时间或等级不够踢出
						if(!p.isValidInXianDiDiTuTime() || p.getMainSoul().getGrade() < levelLimit){
							game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
							Game.logger.warn("[玩家没有进入时间或等级不够踢出] [npcid:"+this.getnPCCategoryId()+"] ["+p.getLogString()+"] [level:"+p.getMainSoul().getGrade()+"] ["+game.gi.displayName+"]");
						}
					}
				}
			}
		}
	}
	
	public Object clone() {
		DituLimitNPC p = new DituLimitNPC();
		p.cloneAllInitNumericalProperty(this);
		
//		p.lastingTime = lastingTime;
		p.country = country;
		
		p.dituType = dituType;
		
		p.levelLimit = levelLimit;
		
		p.setnPCCategoryId(this.getnPCCategoryId());
		
		p.windowId = windowId;
		
		return p;
	}

	public int getDituType() {
		return dituType;
	}

	public void setDituType(int dituType) {
		this.dituType = dituType;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

}
