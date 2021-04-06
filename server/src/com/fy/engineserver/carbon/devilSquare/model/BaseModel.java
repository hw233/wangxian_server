package com.fy.engineserver.carbon.devilSquare.model;

import java.util.Arrays;
import java.util.List;

/**
 * 副本基础配置
 * @author Administrator
 *
 */
public class BaseModel {
	/** 等级限制 */
	private int levelLimit ;
	/** 地图显示名 */
	private String cMapName;
	/** 最大进入人数 */
	private int maxPlayer;
	/** 门票名 */
	private String ticket;
	/** 需要门票数量 */
	private int ticketNum;
	/** 副本地图名 */
	private String carBonMap;
	/** 进入地图后的出生点 */
	private List<Integer[]> bornPoints;
	/** 准备时间 */
	private int prepareTime;
	/** 副本持续时间 */
	private int carbonLastTime;
	/** 副本结束后多久传出副本 */
	private int intervarl4Kick;
	/** 怪物刷新规则--对应RefreashModel(可以对应多个) */
	private List<Integer> refreashRule;
	/** 刷出所有小怪后间隔多久清楚所有小怪 */
	private int intervarl4Clean;
	/** 清屏后N久后刷新boss */
	private int interverl4Boss2;
	/** bossId */
	private int bossId;
	/** boss刷新坐标点 */
	private Integer[] bossPoint;
	/** 副本小怪最大刷新波数 */
	private int maxRefreashTimes;
	/** 副本中存在怪物最大数量 */
	private int maxMonsterNum;
	/** 门票颜色 */
	private int ticketColor;
	/** 副本开启间隔时间刷宝箱 */
	private List<Integer> intever4RefreashBoxs;
	/** 宝箱坐标集合 */
	private List<Integer[]> boxPoints;
	/** 宝箱刷新个数 */
	private int boxNum;
	
	
	@Override
	public String toString() {
		return "BaseModel [levelLimit=" + levelLimit + ", cMapName=" + cMapName + ", maxPlayer=" + maxPlayer + ", ticket=" + ticket + ", ticketNum=" + ticketNum + ", carBonMap=" + carBonMap + ", bornPoints=" + bornPoints + ", prepareTime=" + prepareTime + ", carbonLastTime=" + carbonLastTime + ", intervarl4Kick=" + intervarl4Kick + ", refreashRule=" + refreashRule + ", intervarl4Clean=" + intervarl4Clean + ", interverl4Boss2=" + interverl4Boss2 + ", bossId=" + bossId + ", bossPoint=" + Arrays.toString(bossPoint) + ", maxRefreashTimes=" + maxRefreashTimes + ", maxMonsterNum=" + maxMonsterNum + ", ticketColor=" + ticketColor + ", intever4RefreashBoxs=" + intever4RefreashBoxs + ", boxPoints=" + boxPoints + ", boxNum=" + boxNum + "]";
	}
	public int getCarbonLastTime() {
		return carbonLastTime;
	}
	public void setCarbonLastTime(int carbonLastTime) {
		this.carbonLastTime = carbonLastTime;
	}
	public int getLevelLimit() {
		return levelLimit;
	}
	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}
	public int getMaxPlayer() {
		return maxPlayer;
	}
	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}
	public String getCarBonMap() {
		return carBonMap;
	}
	public void setCarBonMap(String carBonMap) {
		this.carBonMap = carBonMap;
	}
	public int getPrepareTime() {
		return prepareTime;
	}
	public void setPrepareTime(int prepareTime) {
		this.prepareTime = prepareTime;
	}
	public List<Integer[]> getBornPoints() {
		return bornPoints;
	}
	public void setBornPoints(List<Integer[]> bornPoints) {
		this.bornPoints = bornPoints;
	}
	public int getIntervarl4Clean() {
		return intervarl4Clean;
	}
	public void setIntervarl4Clean(int intervarl4Clean) {
		this.intervarl4Clean = intervarl4Clean;
	}
	public int getInterverl4Boss2() {
		return interverl4Boss2;
	}
	public void setInterverl4Boss2(int interverl4Boss2) {
		this.interverl4Boss2 = interverl4Boss2;
	}
	public int getBossId() {
		return bossId;
	}
	public void setBossId(int bossId) {
		this.bossId = bossId;
	}
	public Integer[] getBossPoint() {
		return bossPoint;
	}
	public void setBossPoint(Integer[] bossPoint) {
		this.bossPoint = bossPoint;
	}
	public int getIntervarl4Kick() {
		return intervarl4Kick;
	}
	public void setIntervarl4Kick(int intervarl4Kick) {
		this.intervarl4Kick = intervarl4Kick;
	}
	public int getMaxRefreashTimes() {
		return maxRefreashTimes;
	}
	public void setMaxRefreashTimes(int maxRefreashTimes) {
		this.maxRefreashTimes = maxRefreashTimes;
	}
	public List<Integer> getRefreashRule() {
		return refreashRule;
	}
	public List<Integer> getIntever4RefreashBoxs() {
		return intever4RefreashBoxs;
	}
	public void setIntever4RefreashBoxs(List<Integer> intever4RefreashBoxs) {
		this.intever4RefreashBoxs = intever4RefreashBoxs;
	}
	public List<Integer[]> getBoxPoints() {
		return boxPoints;
	}
	public void setBoxPoints(List<Integer[]> boxPoints) {
		this.boxPoints = boxPoints;
	}
	public int getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}
	public void setRefreashRule(List<Integer> refreashRule) {
		this.refreashRule = refreashRule;
	}
	public int getMaxMonsterNum() {
		return maxMonsterNum;
	}
	public void setMaxMonsterNum(int maxMonsterNum) {
		this.maxMonsterNum = maxMonsterNum;
	}
	public int getTicketColor() {
		return ticketColor;
	}
	public void setTicketColor(int ticketColor) {
		this.ticketColor = ticketColor;
	}
	public String getcMapName() {
		return cMapName;
	}
	public void setcMapName(String cMapName) {
		this.cMapName = cMapName;
	}
	
}
