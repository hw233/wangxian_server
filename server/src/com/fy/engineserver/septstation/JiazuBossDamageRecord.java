package com.fy.engineserver.septstation;

/**
 * 家族BOSS伤害统计
 * 
 * 
 */
public class JiazuBossDamageRecord implements Comparable<JiazuBossDamageRecord> {

	private long playerId;
	private String playerName;
	private long damage;

	public JiazuBossDamageRecord() {
		// TODO Auto-generated constructor stub
	}

	public JiazuBossDamageRecord(long playerId, String playerName, long damage) {
		super();
		this.playerId = playerId;
		this.playerName = playerName;
		this.damage = damage;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}

	@Override
	public String toString() {
		return "JiazuBossDamageRecord [playerId=" + playerId + ", playerName=" + playerName + ", damage=" + damage + "]";
	}

	@Override
	public int compareTo(JiazuBossDamageRecord o) {
		return this.getDamage() < o.getDamage() ? 1 : -1;
	}
}