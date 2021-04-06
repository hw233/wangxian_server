package com.fy.engineserver.activity.TransitRobbery.model;

public class EachLevelDetal {
	/** 持续时间 */
	private int duration;
	/** 胜利参数(怪物id，数目！怪物id，数目) */
	private String vCondition;
	/** 怪物刷出的初始坐标 */
	private String initPoint;
	/** 开启间隔时间 */
	private int interval;
	/** 提示内容 */
	private String tips;
	
	public String toString(){
		return "duration=" + duration + "  vcondition=" + vCondition + "  initpoint=" + initPoint + "  inteval=" + interval + "  tips:" + tips;
	}
	
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getvCondition() {
		return vCondition;
	}
	public void setvCondition(String vCondition) {
		this.vCondition = vCondition;
	}
	public String getInitPoint() {
		return initPoint;
	}
	public void setInitPoint(String initPoint) {
		this.initPoint = initPoint;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}
}
