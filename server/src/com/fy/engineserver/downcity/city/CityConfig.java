/**
 * 
 */
package com.fy.engineserver.downcity.city;

import java.util.Arrays;


/**
 * @author Administrator
 *
 */

public class CityConfig {
	private String cityName = "";
	private String mapName = "";
	//2=多人,1=单人
	private int joinType;
	//1:无敌，2:系统技能
	private int cityType;
	private int jiaZuLevelLimit;
	private int minLevelLimit;
	private int maxLevelLimit;
	private int bossId;
	private String bossName;
	private String bossAvata;
	private int [] bossXY;
	private int [] playerXY;
	private long[] ids;
	private int[] nums;
	private int [][] areaXYs;	
	private int [][] monsterIds;
	
	private int weekLimitNum;
	private int dayLimitNum;
	private int monthCardAddNum;
	
	
	public int getWeekLimitNum() {
		return weekLimitNum;
	}
	public void setWeekLimitNum(int weekLimitNum) {
		this.weekLimitNum = weekLimitNum;
	}
	public int getDayLimitNum() {
		return dayLimitNum;
	}
	public void setDayLimitNum(int dayLimitNum) {
		this.dayLimitNum = dayLimitNum;
	}
	public int getMonthCardAddNum() {
		return monthCardAddNum;
	}
	public void setMonthCardAddNum(int monthCardAddNum) {
		this.monthCardAddNum = monthCardAddNum;
	}
	public int getJoinType() {
		return joinType;
	}
	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}
	public int getJiaZuLevelLimit() {
		return jiaZuLevelLimit;
	}
	public void setJiaZuLevelLimit(int jiaZuLevelLimit) {
		this.jiaZuLevelLimit = jiaZuLevelLimit;
	}
	public String getBossName() {
		return bossName;
	}
	public void setBossName(String bossName) {
		this.bossName = bossName;
	}
	public String getBossAvata() {
		return bossAvata;
	}
	public void setBossAvata(String bossAvata) {
		this.bossAvata = bossAvata;
	}
	public long[] getIds() {
		return ids;
	}
	public void setIds(long[] ids) {
		this.ids = ids;
	}
	public int[] getNums() {
		return nums;
	}
	public void setNums(int[] nums) {
		this.nums = nums;
	}
	public int getCityType() {
		return cityType;
	}
	public void setCityType(int cityType) {
		this.cityType = cityType;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int getMinLevelLimit() {
		return minLevelLimit;
	}
	public void setMinLevelLimit(int minLevelLimit) {
		this.minLevelLimit = minLevelLimit;
	}
	public int getMaxLevelLimit() {
		return maxLevelLimit;
	}
	public void setMaxLevelLimit(int maxLevelLimit) {
		this.maxLevelLimit = maxLevelLimit;
	}
	public int[] getBossXY() {
		return bossXY;
	}
	public void setBossXY(int[] bossXY) {
		this.bossXY = bossXY;
	}
	public int[] getPlayerXY() {
		return playerXY;
	}
	public void setPlayerXY(int[] playerXY) {
		this.playerXY = playerXY;
	}
	public int[][] getAreaXYs() {
		return areaXYs;
	}
	public void setAreaXYs(int[][] areaXYs) {
		this.areaXYs = areaXYs;
	}
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public int[][] getMonsterIds() {
		return monsterIds;
	}
	public void setMonsterIds(int[][] monsterIds) {
		this.monsterIds = monsterIds;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0;i<areaXYs.length;i++){
			sBuffer.append(Arrays.toString(areaXYs[i])).append(";");
		}
		String areaXYstr = sBuffer.toString();
		sBuffer.delete(0,sBuffer.length());
		for(int i=0;i<monsterIds.length;i++){
			sBuffer.append(Arrays.toString(monsterIds[i])).append(";");
		}
		String monsterIdstr = sBuffer.toString();
		
		return "[areaXYs=" + areaXYstr + ", bossId="
				+ bossId + ", bossXY=" + Arrays.toString(bossXY)
				+ ", cityName=" + cityName + ", mapName=" + mapName
				+ ", maxLevelLimit=" + maxLevelLimit + ", minLevelLimit="
				+ minLevelLimit + ", monsterIds=" + monsterIdstr
				+ ", playerXY=" + Arrays.toString(playerXY) + "]";
	}
	
}
