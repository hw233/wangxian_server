package com.fy.engineserver.sprite.pet;

import java.util.Calendar;

import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 宠物繁殖，宠物繁殖时，双方交出公母宠物物品各一个，然后24小时候各生成一个子宠物，再48小时后过期，删除次繁殖。
 * 
 *
 */
public class PetMating {
	
	public static final int 繁殖中 = 0;
	public static final int 完成 = 1;
	public static final int 过期 = 2;
	public static final int 不正常 = 3;
	
	private long createTime;
	
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	private long id;
	
	private long petEntityAId;
	
	private long petEntityBId;
	
	private long playerAId;
	
	private long playerBId;
	
	private boolean playerAConfirmed;
	
	private boolean playerBConfirmed;
	
	private long startMating;
	
	private long finishTime;
	
	private long childForAId;
	
	private long childForBId;
	
	private long expireTime;
	
	private int state;
	
	private transient boolean dirty;
	
	private transient long lastUpdateTime;
	
	public PetMating() {}
	
	public PetMating(Player playerA, PetPropsEntity petEntityA, Player playerB, PetPropsEntity petEntityB) {
		this.playerAId = playerA != null?playerA.getId():-1;
		this.petEntityAId = petEntityA != null?petEntityA.getId():-1;
		this.playerBId = playerB != null?playerB.getId():-1;
		this.petEntityBId = petEntityB!= null?petEntityB.getId() : -1;
		Calendar cal = Calendar.getInstance();
		this.startMating = cal.getTimeInMillis();
		cal.setTimeInMillis(startMating + 24*60*60*1000);
		this.finishTime = cal.getTimeInMillis();
		cal.setTimeInMillis(finishTime + 48*60*60*1000);
		this.expireTime = cal.getTimeInMillis();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取父宠
	 * @return
	 */
	public long getPetEntityAId() {
		return petEntityAId;
	}

	/**
	 * 设置父宠
	 * @param petEntityAId
	 */
	public void setPetEntityAId(long petEntityAId) {
		this.petEntityAId = petEntityAId;
		setDirty(true);
	}

	/**
	 * 获取母宠
	 * @return
	 */
	public long getPetEntityBId() {
		return petEntityBId;
	}

	/**
	 * 设置母宠
	 * @param petEntityBId
	 */
	public void setPetEntityBId(long petEntityBId) {
		this.petEntityBId = petEntityBId;
		setDirty(true);
	}

	/**
	 * 设置父宠繁殖玩家
	 * @return
	 */
	public long getPlayerAId() {
		return playerAId;
	}

	/**
	 * 设置父宠玩家
	 * @param playerA
	 */
	public void setPlayerAId(long playerAId) {
		this.playerAId = playerAId;
		setDirty(true);
	}

	/**
	 * 得到母宠玩家
	 * @return
	 */
	public long getPlayerBId() {
		return playerBId;
	}

	/**
	 * 设置母宠玩家
	 * @param playerB
	 */
	public void setPlayerBId(long playerBId) {
		this.playerBId = playerBId;
		setDirty(true);
	}

	/**
	 * 得到开始交配的时间
	 * @return
	 */
	public long getStartMating() {
		return startMating;
	}

	/**
	 * 设置交配的时间
	 * @param startMating
	 */
	public void setStartMating(long startMating) {
		this.startMating = startMating;
		setDirty(true);
	}

	/**
	 * 得到父方子宠
	 * @return
	 */
	public long getChildForAId() {
		return childForAId;
	}

	/**
	 * 设置父方子宠
	 * @param childForA
	 */
	public void setChildForAId(long childForAId) {
		this.childForAId = childForAId;
		setDirty(true);
	}

	/**
	 * 获取母方子宠
	 * @return
	 */
	public long getChildForBId() {
		return childForBId;
	}

	/**
	 * 设置母方子宠
	 * @param childForB
	 */
	public void setChildForBId(long childForBId) {
		this.childForBId = childForBId;
		setDirty(true);
	}

	/**
	 * 得到过期时间
	 * @return
	 */
	public long getExpireTime() {
		return expireTime;
	}

	/**
	 * 设置过期时间
	 * @param expireTime
	 */
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
		setDirty(true);
	}

	/**
	 * 得到完成时间
	 * @return
	 */
	public long getFinishTime() {
		return finishTime;
	}

	/**
	 * 设置完成时间
	 * @param finishTime
	 */
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
		setDirty(true);
	}

	/**
	 * 当前的状态， 0-繁殖中，1-繁殖完成，2-繁殖过期
	 * @return
	 */
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		setDirty(true);
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public boolean isPlayerAConfirmed() {
		return playerAConfirmed;
	}

	public void setPlayerAConfirmed(boolean playerAConfirmed) {
		this.playerAConfirmed = playerAConfirmed;
	}

	public boolean isPlayerBConfirmed() {
		return playerBConfirmed;
	}

	public void setPlayerBConfirmed(boolean playerBConfirmed) {
		this.playerBConfirmed = playerBConfirmed;
	}
	
	public boolean isMatingConfirmed() {
		return this.playerAConfirmed && this.playerBConfirmed && this.petEntityAId != -1 && this.petEntityBId != -1;
	}
}
