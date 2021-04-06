package com.fy.engineserver.carbon.devilSquare.model;

import java.util.ArrayList;
import java.util.List;

public class RefreashRule {
	@Override
	public String toString() {
		return "RefreashRule [interval=" + interval + ", monsterIds=" + monsterIds + ", maxNumbers=" + maxNumbers + "]";
	}

	/** 刷新间隔时间 */
	private int interval;
	/** 刷新怪物id集合 */
	private List<Integer> monsterIds;
	/** 死亡后复活间隔时间 */
	private int intervalRevive;
	/** 刷怪数量 */
	private int maxNumbers;

	public RefreashRule(int interval, int monsterIds, int maxNumbers, int intervalRevive) {
		this.interval = interval;
		this.maxNumbers = maxNumbers;
		this.monsterIds = new ArrayList<Integer>();
		this.monsterIds.add(monsterIds);
		this.intervalRevive = intervalRevive;
	}
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<Integer> getMonsterIds() {
		return monsterIds;
	}

	public void setMonsterIds(List<Integer> monsterIds) {
		this.monsterIds = monsterIds;
	}

	public int getMaxNumbers() {
		return maxNumbers;
	}

	public void setMaxNumbers(int maxNumbers) {
		this.maxNumbers = maxNumbers;
	}

	public int getIntervalRevive() {
		return intervalRevive;
	}

	public void setIntervalRevive(int intervalRevive) {
		this.intervalRevive = intervalRevive;
	}
}