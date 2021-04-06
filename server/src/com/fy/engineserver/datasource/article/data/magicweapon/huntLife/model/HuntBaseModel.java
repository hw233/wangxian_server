package com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model;

import java.util.Arrays;
/**
 * 猎命基础配置
 * @author Administrator
 *
 */
public class HuntBaseModel {
	/** 单抽消耗银子数 */
	private long onceCost;
	/** 十连抽消耗银子数 */
	private long tenCost;
	/** 获得免费次数间隔时间 */
	private long interverFreeTime;
	/** 开孔数量 */
	private int[] kongNums;
	/** 需要玩家等级 */
	private int[] needLevels;
	/** 十连抽必产物品列表 */
	private int[] mustGiveArticles ;
	/**  对应产出概率 */
	private int[] prob;
	
	
	@Override
	public String toString() {
		return "HuntBaseModel [onceCost=" + onceCost + ", tenCost=" + tenCost + ", interverFreeTime=" + interverFreeTime + ", kongNums=" + Arrays.toString(kongNums) + ", needLevels=" + Arrays.toString(needLevels) + ", mustGiveArticles=" + Arrays.toString(mustGiveArticles) + ", problems=" + Arrays.toString(prob) + "]";
	}


	public long getOnceCost() {
		return onceCost;
	}


	public void setOnceCost(long onceCost) {
		this.onceCost = onceCost;
	}


	public long getTenCost() {
		return tenCost;
	}


	public void setTenCost(long tenCost) {
		this.tenCost = tenCost;
	}


	public long getInterverFreeTime() {
		return interverFreeTime;
	}


	public void setInterverFreeTime(long interverFreeTime) {
		this.interverFreeTime = interverFreeTime;
	}


	public int[] getKongNums() {
		return kongNums;
	}


	public void setKongNums(int[] kongNums) {
		this.kongNums = kongNums;
	}


	public int[] getNeedLevels() {
		return needLevels;
	}


	public void setNeedLevels(int[] needLevels) {
		this.needLevels = needLevels;
	}


	public int[] getMustGiveArticles() {
		return mustGiveArticles;
	}


	public void setMustGiveArticles(int[] mustGiveArticles) {
		this.mustGiveArticles = mustGiveArticles;
	}


	public int[] getProb() {
		return prob;
	}


	public void setProb(int[] problems) {
		this.prob = problems;
	}
	
	
	
	
}
