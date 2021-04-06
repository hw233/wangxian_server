package com.fy.engineserver.activity.chestFight.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.chestFight.model.ChestFightModel;

public class ChestFighyPlayer {
	/** 角色id */
	private long playerId;
	/** 进入时间 */
	private long enterTime; 
	/** 离开时间 */
	private long leaveTime;
	/** 是否还在乱斗中 */
	private boolean liveInFight;
	/** 获取到的箱子列表 */
	private List<ChestFightModel> obtainChests = new ArrayList<ChestFightModel>();
	
	
	public ChestFighyPlayer(long playerId, long enterTime) {
		super();
		this.playerId = playerId;
		this.enterTime = enterTime;
		this.liveInFight = true;
	}
	/**
	 * 
	 * @param now
	 * @return   开启的宝箱数
	 */
	public int notifyOpenChest(long now,ChestFight c) {
		int result = 0;
		try {
			for (int i=0; i<obtainChests.size(); i++) {
				if (obtainChests.get(i).openChest(playerId,now,c)) {
					result += 1;
				}
			}
		} catch (Exception e) {
			ChestFightManager.logger.warn("[宝箱争夺] [通知开启宝箱] [异常] [" + playerId + "]", e);
		}
		return result;
	}
	/**
	 * 玩家身上拥有的宝箱数量 （只计算未开启的）
	 * @param chestType
	 * @return
	 */
	public int getChestNumByType(int chestType) {
		int result = 0;
		if (obtainChests != null && obtainChests.size() > 0) {
			for (int i=0; i<obtainChests.size(); i++) {
				if (!obtainChests.get(i).isOpen() && obtainChests.get(i).getChestType() == chestType) {
					result += 1;
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "ChestFighyPlayer [playerId=" + playerId + ", enterTime=" + enterTime + ", leaveTime=" + leaveTime + ", liveInFight=" + liveInFight + ", obtainChests=" + obtainChests + "]";
	}
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public long getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}
	public long getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}
	public List<ChestFightModel> getObtainChests() {
		return obtainChests;
	}
	public void setObtainChests(List<ChestFightModel> obtainChests) {
		this.obtainChests = obtainChests;
	}
	public boolean isLiveInFight() {
		return liveInFight;
	}
	public void setLiveInFight(boolean liveInFight) {
		this.liveInFight = liveInFight;
	}
	
	
}
