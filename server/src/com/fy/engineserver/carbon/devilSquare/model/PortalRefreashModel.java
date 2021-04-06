package com.fy.engineserver.carbon.devilSquare.model;

import java.util.List;

/**
 * 传送门刷新规则
 * @author Administrator
 *
 */
public class PortalRefreashModel {
	@Override
	public String toString() {
		return "PortalRefreashModel [transNPCId=" + transNPCId + ", openTime=" + openTime + ", transLastTime=" + transLastTime + ", transMap=" + transMap + ", transPoints=" + transPoints + "]";
	}
	/** 传送门id */
	private int transNPCId ;
	/** 传送门开启时间---规则在讨论，先这么写 */
	private List<Integer> openTime;
	/** 传送门存在时间 */
	private int transLastTime;
	/** 传送门刷新地图 */
	private String transMap;
	/** 传送门刷新坐标点集合 */
	private List<Integer[]> transPoints;
	
	public int getTransLastTime() {
		return transLastTime;
	}
	public void setTransLastTime(int transLastTime) {
		this.transLastTime = transLastTime;
	}
	public String getTransMap() {
		return transMap;
	}
	public void setTransMap(String transMap) {
		this.transMap = transMap;
	}
	public List<Integer[]> getTransPoints() {
		return transPoints;
	}
	public void setTransPoints(List<Integer[]> transPoints) {
		this.transPoints = transPoints;
	}
	public List<Integer> getOpenTime() {
		return openTime;
	}
	public void setOpenTime(List<Integer> openTime) {
		this.openTime = openTime;
	}
	public int getTransNPCId() {
		return transNPCId;
	}
	public void setTransNPCId(int transNPCId) {
		this.transNPCId = transNPCId;
	}
}
