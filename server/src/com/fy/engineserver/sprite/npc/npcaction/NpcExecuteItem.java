package com.fy.engineserver.sprite.npc.npcaction;

/**
 * Boss执行条目，一个Boss可以有多个执行条目
 * 
 * 
 *
 */
public class NpcExecuteItem implements Cloneable{

	/**
	 * boss开启后多长时间，才能执行此条目
	 */
	protected long exeTimeLimit = 0;
	
	/**
	 * 当前目标距离boss最小距离
	 */
	protected int minDistance = 0;
	
	/**
	 * 当前目标距离boss最大距离，
	 * 只有当前目标在最小和最大距离范围内，才执行
	 */
	protected int maxDistance = 10000;
	
	/**
	 * boss的血量小于多少百分比才执行，100
	 */
	protected int hpPercent = 100;
	
	/**
	 * 最多执行几次
	 */
	protected int maxExeTimes = 10000;
	
	/**
	 * 两次执行之间的最小间隔
	 */
	protected long exeTimeGap = 0;
	
	/**
	 * 是否无视BOSS移动
	 */
	protected boolean ignoreMoving;
	
	/**
	 * 执行的动作
	 */
	protected int actionId;
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	//
	// 运行时的数据
	//
	//已执行的次数
	protected int exeTimes = 0;
	//最后一次执行的时间
	protected long lastExeTime = 0; 
	
	public boolean turnOnFlag = false;
	public long turnOnTime;

	public NpcAction action;
	


	public int getExeTimes() {
		return exeTimes;
	}

	public void setExeTimes(int exeTimes) {
		this.exeTimes = exeTimes;
	}

	public long getLastExeTime() {
		return lastExeTime;
	}

	public void setLastExeTime(long lastExeTime) {
		this.lastExeTime = lastExeTime;
	}

	public void setAction(NpcAction action) {
		this.action = action;
	}

	public boolean isIgnoreMoving() {
		return ignoreMoving;
	}

	public void setIgnoreMoving(boolean ignoreMoving) {
		this.ignoreMoving = ignoreMoving;
	}

	public int getexeTimes(){
		return exeTimes;
	}
	
	public long getlastExeTime(){
		return lastExeTime;
	}
	
	public NpcAction getAction(){
		return action;
	}
	
	public long getExeTimeLimit() {
		return exeTimeLimit;
	}

	public void setExeTimeLimit(long exeTimeLimit) {
		this.exeTimeLimit = exeTimeLimit;
	}

	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	public int getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	public int getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int hpPercent) {
		this.hpPercent = hpPercent;
	}

	public int getMaxExeTimes() {
		return maxExeTimes;
	}

	public void setMaxExeTimes(int maxExeTimes) {
		this.maxExeTimes = maxExeTimes;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public long getExeTimeGap() {
		return exeTimeGap;
	}

	public void setExeTimeGap(long exeTimeGap) {
		this.exeTimeGap = exeTimeGap;
	}

	public Object clone() throws CloneNotSupportedException{
		NpcExecuteItem nei = (NpcExecuteItem)super.clone();
		if(nei != null && action != null){
			nei.action = (NpcAction)action.clone();
		}
		return nei;
	}
}
