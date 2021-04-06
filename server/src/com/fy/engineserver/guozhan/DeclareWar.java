package com.fy.engineserver.guozhan;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * 
 * @version 创建时间：May 3, 2012 3:56:36 PM
 * 
 */
public class DeclareWar implements Serializable {
	
	public static final long serialVersionUID = 45794839834549753L;
	
	private int id;
	
	/**
	 * 宣战国
	 */
	private byte declareCountryId;
	
	/**
	 * 被宣战国
	 */
	private byte enemyCountryId;
	
	/**
	 * 宣战时间
	 */
	private long declareTime;
	
	/**
	 * 宣战玩家
	 */
	private long declarePlayerId;
	
	/**
	 * 是否提前提示过玩家了
	 */
	private boolean notified;

	public byte getDeclareCountryId() {
		return declareCountryId;
	}

	public void setDeclareCountryId(byte declareCountryId) {
		this.declareCountryId = declareCountryId;
	}

	public byte getEnemyCountryId() {
		return enemyCountryId;
	}

	public void setEnemyCountryId(byte enemyCountryId) {
		this.enemyCountryId = enemyCountryId;
	}

	public long getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(long declareTime) {
		this.declareTime = declareTime;
	}

	public long getDeclarePlayerId() {
		return declarePlayerId;
	}

	public void setDeclarePlayerId(long declarePlayerId) {
		this.declarePlayerId = declarePlayerId;
	}
	
	public long getStartFightTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getDeclareTime());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		String openTime = GuozhanOrganizer.getInstance().getConstants().国战开启时间;
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(openTime.split(":")[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(openTime.split(":")[1]));
		cal.set(Calendar.SECOND, 0);
		return cal.getTimeInMillis();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isNotified() {
		return notified;
	}

	public void setNotified(boolean notified) {
		this.notified = notified;
	}
	
	
}
