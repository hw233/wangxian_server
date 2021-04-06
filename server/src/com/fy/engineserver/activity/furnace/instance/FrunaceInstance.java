package com.fy.engineserver.activity.furnace.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.furnace.FurnaceManager;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.FrunaceNPC;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;

public class FrunaceInstance {
	public long playerId;
	
	public Game game;
	
	public int threadIndex;
	
	public byte state;						//状态
	
	public static final byte 结束 = 1;
	
//	public int[] propsNum = new int[5];		//残渣 普通 精品  无暇  完美仙丹的获取个数--最后结束时根据此数组给经验
	
//	public volatile boolean awardProps = false;
	
	public int maxPickTimes = 0;
	
	public Object lock = new Object();
	
	public List<Long> hasPickNpcIds = new ArrayList<Long>();
	
	public volatile int pickTimes = 0;	//采集过几次
	
	public void heartBeat() {
		try {
			this.game.heartbeat();
		}  catch (Exception e) {
			FurnaceManager.logger.warn("[炼丹] [心跳异常] [" + playerId + "] ", e);
		}
	}
	
	public void notifyLeaveGame(Player player) {
		if (player.getId() == playerId) {
			this.state = 结束;
		}
		if (FurnaceManager.logger.isInfoEnabled()) {
			FurnaceManager.logger.info("[炼丹] [玩家离开地图] [" + player.getLogString() + "]");
		}
	}
	/**
	 * 
	 * @param player
	 * @param stayIndex   残渣 普通 精品  无暇  完美
	 */
	public void notifyPickup(Player player, int stayIndex,long npcId) {
		if (pickTimes >= maxPickTimes) {
			return ;
		}
		if (player.getId() != playerId) {
			return ;
		}
//		if (stayIndex >= 5 || stayIndex < 0) {
//			return ;
//		}
		synchronized (lock) {
			if (hasPickNpcIds.contains(npcId)) {
				return ;
			}
			pickTimes++;
			hasPickNpcIds.add(npcId);
		}
		try {
			NPC npc = MemoryNPCManager.getNPCManager().getNPC(npcId);
			if (npc instanceof FrunaceNPC) {
				((FrunaceNPC)npc).setHp(0);
//				((FrunaceNPC)npc).setAvataAction(FrunaceNPC.采集过动作);
				((FrunaceNPC)npc).setState(LivingObject.STATE_DEAD);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long exp = 5000;				//经验计算公式
		int level = player.getSoulLevel();
		if (ExperienceManager.maxExpOfLevel != null &&  level < ExperienceManager.maxExpOfLevel.length) {
			int rate = FurnaceManager.仙丹经验比例[7];
			if (stayIndex >= 0 && stayIndex < FurnaceManager.仙丹经验比例.length) {
				rate =  FurnaceManager.仙丹经验比例[stayIndex];
			}
			exp = ExperienceManager.maxExpOfLevel[level] * rate / 10000;
		}
		if (exp > 0) {
			player.addExp(exp, ExperienceManager.飞升炼丹);
		}
		//propsNum[stayIndex] += 1;
		if (FurnaceManager.logger.isInfoEnabled()) {
			FurnaceManager.logger.info("[炼丹] [采集] [成功] [" + player.getLogString() + "] [stayIndex : " + stayIndex + "] [level:"+level+"] [addExp:" + exp + "]");
		}
	}
	
	public boolean isPicked(Player player, long npcId) {
		if (hasPickNpcIds.contains(npcId)) {
			return true;
		}
		return false;
	}
	/**
	 * 活动结束给予奖励  (改为单次采集给经验)
	 */
	/*public void awardExp() {
		if (!awardProps) {
			awardProps = true;
			try {
				Player player = GamePlayerManager.getInstance().getPlayer(playerId);
				long exp = 0;
				if (exp > 0) {
					player.addExp(exp, ExperienceManager.飞升炼丹);
				}
				if (FurnaceManager.logger.isInfoEnabled()) {
					FurnaceManager.logger.info("[炼丹] [结束给予经验] [" + player.getLogString() + "] [exp:" + exp + "]");
				}
			} catch (Exception e) {
				FurnaceManager.logger.warn("[炼丹] [结束给予经验] [" + playerId + "]", e);
			}
		}
	}*/
	

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getThreadIndex() {
		return threadIndex;
	}

	public void setThreadIndex(int threadIndex) {
		this.threadIndex = threadIndex;
	}
	
	
}
