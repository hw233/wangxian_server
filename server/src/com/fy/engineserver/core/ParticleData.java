package com.fy.engineserver.core;

/**
 * 粒子效果数据
 * 用于通知客户端播放粒子效果
 * 包含如下几个数据
 * 1) 在那个实体上播,id(-1表示在屏幕上)
 * 2) 播放哪个粒子
 * 3) 播放多长时间(-1表示例子本身时间)
 * 4) 播放例子相对于目标的实体的位置,默认目标的身高作为1,分别有前后,左右,上下,三个参数确定,规定为,左,上,前,三个方向为正
 * 
 */
public class ParticleData {

	private long targetId;
	private String particleName;
	private long delayTime;
	private double upValue;
	private double leftValue;
	private double frontValue;

	public ParticleData() {

	}

	public ParticleData(long targetId, String particleName, long delayTime, double upValue, double leftValue, double frontValue) {
		this.targetId = targetId;
		this.particleName = particleName;
		this.delayTime = delayTime;
		this.upValue = upValue;
		this.leftValue = leftValue;
		this.frontValue = frontValue;
	}

	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public double getUpValue() {
		return upValue;
	}

	public void setUpValue(double upValue) {
		this.upValue = upValue;
	}

	public double getLeftValue() {
		return leftValue;
	}

	public void setLeftValue(double leftValue) {
		this.leftValue = leftValue;
	}

	public double getFrontValue() {
		return frontValue;
	}

	public void setFrontValue(double frontValue) {
		this.frontValue = frontValue;
	}

	@Override
	public String toString() {
		return "ParticleData [targetId=" + targetId + ", particleName=" + particleName + ", delayTime=" + delayTime + ", upValue=" + upValue + ", leftValue=" + leftValue + ", frontValue=" + frontValue + "]";
	}

}
