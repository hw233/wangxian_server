package com.fy.engineserver.bourn;

/**
 * 境界配置
 * 
 */
public class BournCfg {
	/** 境界等级 */
	private int grade;
	/** 境界经验 */
	private int exp;
	/** 境界开始任务(自动加入可接任务列表) */
	private long startTask;
	/** 境界结束任务(点击升级时的必要条件) */
	private long endTask;
	/** 境界升级所需的角色最低等级 */
	private int playerMinLevel;
	/** 境界等级下打坐时一次增加的经验值 */
	private int zazenOnceExp;
	/** 各个境界等级刷新任务消耗经验值 */
	private long refreshTaskExpCost;

	public BournCfg(int grade, int exp, long startTask, long endTask, int playerMinLevel, int zazenOnceExp, long refreshTaskExpCost) {
		this.grade = grade;
		this.exp = exp;
		this.startTask = startTask;
		this.endTask = endTask;
		this.playerMinLevel = playerMinLevel;
		this.zazenOnceExp = zazenOnceExp;
		this.refreshTaskExpCost = refreshTaskExpCost;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public long getStartTask() {
		return startTask;
	}

	public void setStartTask(long startTask) {
		this.startTask = startTask;
	}

	public long getEndTask() {
		return endTask;
	}

	public void setEndTask(long endTask) {
		this.endTask = endTask;
	}

	public int getPlayerMinLevel() {
		return playerMinLevel;
	}

	public void setPlayerMinLevel(int playerMinLevel) {
		this.playerMinLevel = playerMinLevel;
	}

	public int getZazenOnceExp() {
		return zazenOnceExp;
	}

	public void setZazenOnceExp(int zazenOnceExp) {
		this.zazenOnceExp = zazenOnceExp;
	}

	public long getRefreshTaskExpCost() {
		return refreshTaskExpCost;
	}

	public void setRefreshTaskExpCost(long refreshTaskExpCost) {
		this.refreshTaskExpCost = refreshTaskExpCost;
	}

	@Override
	public String toString() {
		return "BournCfg [grade=" + grade + ", exp=" + exp + ", startTask=" + startTask + ", endTask=" + endTask + ", playerMinLevel=" + playerMinLevel + ", zazenOnceExp=" + zazenOnceExp + ", refreshTaskExpCost=" + refreshTaskExpCost + "]";
	}
}
