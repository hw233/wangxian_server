package com.fy.engineserver.downcity.downcity2;

import java.util.List;

public class DownCityCacheModel {
	/** 角色id */
	private long playerId;
	/** 通关地图名 */
	private String mapName;
	/** 已经获得过的物品 */
	private List<PropModel> propList;
	/** 已经抽取的次数 */
	private int times;
	/** 随机出来显示的物品列表 */
	private List<PropModel> showPropList;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public List<PropModel> getPropList() {
		return propList;
	}
	public void setPropList(List<PropModel> propList) {
		this.propList = propList;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public List<PropModel> getShowPropList() {
		return showPropList;
	}
	public void setShowPropList(List<PropModel> showPropList) {
		this.showPropList = showPropList;
	}
	
}
