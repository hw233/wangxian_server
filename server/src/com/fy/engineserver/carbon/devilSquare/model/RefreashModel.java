package com.fy.engineserver.carbon.devilSquare.model;

import java.util.List;
/**
 * 副本刷怪规则
 * @author Administrator
 *
 */
public class RefreashModel {
	@Override
	public String toString() {
		return "RefreashModel [id=" + id + ", refreashPoints=" + refreashPoints + ", refreashTimes=" + refreashTimes + ", refreashRules=" + refreashRules + "]";
	}

	/** 索引id */
	private int id;
	/** 怪物刷新坐标点集合 ---存储x,y坐标点集合*/
	private List<Integer[]> refreashPoints;
	/** 刷怪总波数 */
	private int refreashTimes;
	/** 刷怪规则(存储刷怪间隔时间，怪物id集合，刷怪数量) */
	private List<RefreashRule> refreashRules;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer[]> getRefreashPoints() {
		return refreashPoints;
	}

	public void setRefreashPoints(List<Integer[]> refreashPoints) {
		this.refreashPoints = refreashPoints;
	}

	public int getRefreashTimes() {
		return refreashTimes;
	}

	public void setRefreashTimes(int refreashTimes) {
		this.refreashTimes = refreashTimes;
	}

	public List<RefreashRule> getRefreashRules() {
		return refreashRules;
	}

	public void setRefreashRules(List<RefreashRule> refreashRules) {
		this.refreashRules = refreashRules;
	}

}

