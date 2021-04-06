package com.fy.engineserver.activity.fairyRobbery.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FairyRobberyModel {
	/** 渡劫id */
	private int id;
	/** 渡劫名称 */
	private String name;
	/** 角色等级限制 */
	private long levelLimit;
	/** 需要完成的任务id */
	private long taskId;
	/** 境界限制 */
	private int classLvLimit;
	/** 渡劫地图名 */
	private String mapName;
	/** 玩家出生点坐标 */
	private int[] bornPoint;
	/** 玩家开始挑战提示内容 */
	private String startDes;
	/** 挑战胜利提示内容 */
	private String endDes;
	/** 失败提示内容 */
	private String failureDes;
	/** 刷新boss个数 */
	private int refreshBossNum;
	/** boss */
	private List<RobberyMonsterModel> bossList = new ArrayList<RobberyMonsterModel>();
	
	@Override
	public String toString() {
		return "FairyRobberyModel [id=" + id + ", name=" + name + ", levelLimit=" + levelLimit + ", taskId=" + taskId + ", classLvLimit=" + classLvLimit + ", mapName=" + mapName + ", bornPoint=" + Arrays.toString(bornPoint) + ", startDes=" + startDes + ", endDes=" + endDes + ", failureDes=" + failureDes + ", refreshBossNum=" + refreshBossNum + ", bossList=" + bossList + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClassLvLimit() {
		return classLvLimit;
	}
	public void setClassLvLimit(int classLvLimit) {
		this.classLvLimit = classLvLimit;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(long levelLimit) {
		this.levelLimit = levelLimit;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int[] getBornPoint() {
		return bornPoint;
	}
	public void setBornPoint(int[] bornPoint) {
		this.bornPoint = bornPoint;
	}
	public String getStartDes() {
		return startDes;
	}
	public void setStartDes(String startDes) {
		this.startDes = startDes;
	}
	public String getEndDes() {
		return endDes;
	}
	public void setEndDes(String endDes) {
		this.endDes = endDes;
	}
	public int getRefreshBossNum() {
		return refreshBossNum;
	}
	public void setRefreshBossNum(int refreshBossNum) {
		this.refreshBossNum = refreshBossNum;
	}
	public List<RobberyMonsterModel> getBossList() {
		return bossList;
	}
	public void setBossList(List<RobberyMonsterModel> bossList) {
		this.bossList = bossList;
	}
	public String getFailureDes() {
		return failureDes;
	}
	public void setFailureDes(String failureDes) {
		this.failureDes = failureDes;
	}
	
	
}
