package com.fy.engineserver.sprite.monster;

public class SimpleMonster {
	/** 怪物id */
	private long spriteId;
	/** 怪物模板id */
	private int spriteCategoryId;
	/** 怪物名 */
	private String monsterName;
	/** 拥有者id */
	private long ownerId;
	/** 所在地图名 */
	private String mapName;
	/** 是否boss */
	private boolean isBoss;
	/** 怪物等级 */
	private int level;
	
	
	public SimpleMonster(long spriteId, int spriteCategoryId, String monsterName, long ownerId, String mapName, boolean isBoss, int level) {
		super();
		this.spriteId = spriteId;
		this.spriteCategoryId = spriteCategoryId;
		this.monsterName = monsterName;
		this.ownerId = ownerId;
		this.mapName = mapName;
		this.isBoss = isBoss;
		this.level = level;
	}
	@Override
	public String toString() {
		return "SimpleMonster [spriteId=" + spriteId + ", spriteCategoryId=" + spriteCategoryId + ", monsterName=" + monsterName + ", ownerId=" + ownerId + ", mapName=" + mapName + ", isBoss=" + isBoss + ", level=" + level + "]";
	}
	public long getSpriteId() {
		return spriteId;
	}
	public void setSpriteId(long spriteId) {
		this.spriteId = spriteId;
	}
	public int getSpriteCategoryId() {
		return spriteCategoryId;
	}
	public void setSpriteCategoryId(int spriteCategoryId) {
		this.spriteCategoryId = spriteCategoryId;
	}
	public String getMonsterName() {
		return monsterName;
	}
	public void setMonsterName(String monsterName) {
		this.monsterName = monsterName;
	}
	public long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public boolean isBoss() {
		return isBoss;
	}
	public void setBoss(boolean isBoss) {
		this.isBoss = isBoss;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
