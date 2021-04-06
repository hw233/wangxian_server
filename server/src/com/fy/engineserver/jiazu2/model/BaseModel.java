package com.fy.engineserver.jiazu2.model;

import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.util.CompoundReturn;

public class BaseModel {
	/** 每次修炼增加经验值 */
	private int addExp;
	/** 每一次消耗修炼值数量  （X10需要乘以10） */
	private int costXiulian;
	/** 每一次消耗银子数量 */
	private int costSiliverNums;
	/** 刷新任务消耗金钱数量 */
	private int cost4RefreshTask;

	public int getAddExp() {
		return addExp;
	}

	public void setAddExp(int addExp) {
		this.addExp = addExp;
	}

	public int getCostXiulian() {
		return costXiulian;
	}

	public void setCostXiulian(int costXiulian) {
		this.costXiulian = costXiulian;
	}

	public int getCostSiliverNums() {
		return costSiliverNums;
	}

	public void setCostSiliverNums(int costSiliverNums) {
		this.costSiliverNums = costSiliverNums;
	}

	public int getCost4RefreshTask() {
		CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.refreshTaskActivity);
		if (cr != null && cr.getBooleanValue() && cr.getLongValue() > 0) {
			return (int) cr.getLongValue();
		}
		return cost4RefreshTask;
	}

	public void setCost4RefreshTask(int cost4RefreshTask) {
		this.cost4RefreshTask = cost4RefreshTask;
	}
}
