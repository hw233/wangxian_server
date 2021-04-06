package com.fy.engineserver.downcity.downcity2;

public class DownCityBaseModel {
	/** 地图名 */
	private String mapName;
	/** 通过后是否有转盘奖励 */
	private boolean showTun;
	/** 最大可抽取次数 */
	private int maxTimes;
	/** 对应花费银子，单位 */
	private long[] costMoney;
	
	@Override
	public String toString() {
		return "DownCityBaseModel [mapName=" + mapName + ", showTun=" + showTun + ", maxTimes=" + maxTimes + ", costMoney=" + getCostMoney() + "]";
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public boolean isShowTun() {
		return showTun;
	}
	public void setShowTun(boolean showTun) {
		this.showTun = showTun;
	}
	public int getMaxTimes() {
		return maxTimes;
	}
	public void setMaxTimes(int maxTimes) {
		this.maxTimes = maxTimes;
	}
	public long[] getCostMoney() {
		return costMoney;
	}
	public void setCostMoney(long[] costMoney) {
		this.costMoney = costMoney;
	}
	
	
}
