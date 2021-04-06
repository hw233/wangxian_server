package com.fy.engineserver.activity.fairyRobbery.model;

import java.util.Arrays;

public class RobberyMonsterModel {
	/** boss模板id */
	private int bossId;
	/** 出生点坐标 */
	private int[] bornPoint;
	/** 限制时间（-1代表不限时） */
	private long limitTime;
	/** 胜利条件（0击杀boss  1限时生存） */
	private int succType;
	
	@Override
	public String toString() {
		return "RobberyMonsterModel [bossId=" + bossId + ", bornPoint=" + Arrays.toString(bornPoint) + ", limitTime=" + limitTime + ", succType=" + succType + "]";
	}
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public int[] getBornPoint() {
		return bornPoint;
	}
	public void setBornPoint(int[] bornPoint) {
		this.bornPoint = bornPoint;
	}
	public long getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}
	public int getSuccType() {
		return succType;
	}
	public void setSuccType(int succType) {
		this.succType = succType;
	}
	
	
}
