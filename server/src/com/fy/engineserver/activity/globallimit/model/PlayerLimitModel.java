package com.fy.engineserver.activity.globallimit.model;
/**
 * 玩家部分操作限制, 不存库，重启重置
 * @author Administrator
 *
 */
public class PlayerLimitModel {
	/** 玩家id */
	private long playerId;
	/** 每天通过地灵天书boss获得经验次数(用以限制) */
	private int dilingBossTimes;
	
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getDilingBossTimes() {
		return dilingBossTimes;
	}
	public void setDilingBossTimes(int dilingBossTimes) {
		this.dilingBossTimes = dilingBossTimes;
	}
	
	
}
