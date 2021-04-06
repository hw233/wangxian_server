package com.xuanzhi.tools.monitor;

public class MonitorPoint {

	protected MonitorData data;
	
	protected long checkTime;
	
	protected long costTime;
	
	protected int level;
	
	protected boolean isAlerted = false;
	
	public MonitorPoint(MonitorData data,long checkTime,long costTime){
		this.data = data;
		this.checkTime = checkTime;
		this.costTime = costTime;
	}

	public long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(long checkTime) {
		this.checkTime = checkTime;
	}

	public long getCostTime() {
		return costTime;
	}

	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	public MonitorData getData() {
		return data;
	}

	public void setData(MonitorData data) {
		this.data = data;
	}

	public boolean isAlerted() {
		return isAlerted;
	}

	public void setAlerted(boolean isAlerted) {
		this.isAlerted = isAlerted;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public String getLevelAsString(){
		if(level == 0) return "正常";
		else
			return level+"级警报";
	}
	
	
}
