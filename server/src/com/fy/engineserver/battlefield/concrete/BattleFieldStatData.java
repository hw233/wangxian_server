package com.fy.engineserver.battlefield.concrete;

/**
 * 战场统计数据
 * 
 */
public class BattleFieldStatData {
	//玩家的id
	protected long playerId;
	
	//玩家的名字
	protected String playerName;
	
	protected String career;

	protected int playerLevel;
	
	
	//玩家所在的一方
	protected int battleSide;
	
	//杀人次数
	protected int killingNum;
	
	//被杀次数
	protected int killedNum;
	
	//荣誉击杀
	protected int honorKillingNum;
	
	//荣誉点
	protected int honorPoints;

	//伤害量
	protected int totalDamage;
	
	//治疗量
	protected int totalEnhenceHp;
	
	protected String description = "";

	
	public transient long lastDeadTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
	
	public int getPlayerLevel() {
		return playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public int getBattleSide() {
		return battleSide;
	}

	public void setBattleSide(int battleSide) {
		this.battleSide = battleSide;
	}

	public int getKillingNum() {
		return killingNum;
	}

	public void setKillingNum(int killingNum) {
		this.killingNum = killingNum;
	}

	public int getKilledNum() {
		return killedNum;
	}

	public void setKilledNum(int killedNum) {
		this.killedNum = killedNum;
	}

	public int getHonorKillingNum() {
		return honorKillingNum;
	}

	public void setHonorKillingNum(int honorKillingNum) {
		this.honorKillingNum = honorKillingNum;
	}

	public int getHonorPoints() {
		return honorPoints;
	}

	public void setHonorPoints(int honorPoints) {
		this.honorPoints = honorPoints;
	}

	public int getTotalDamage() {
		return totalDamage;
	}

	public void setTotalDamage(int totalDamage) {
		this.totalDamage = totalDamage;
	}

	public int getTotalEnhenceHp() {
		return totalEnhenceHp;
	}

	public void setTotalEnhenceHp(int totalEnhenceHp) {
		this.totalEnhenceHp = totalEnhenceHp;
	}

	
	
}
