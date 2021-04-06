package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 庄园的工作进度
 * 
 * 
 */
@SimpleEmbeddable
public class CaveSchedule implements FaeryConfig {

	/** 显示的名字 */
	private String name;
	/** 建筑类型(哪一个建筑) */
	private int buildingType;
	/** 操作类型 */
	private int optionType;
	/** 开始时间 */
	private long startTime;
	/** 持续时间 */
	private long lastTime;
	/** 剩余时间 */
	private transient long leftTime;
	/** 被加速次数 */
	private int accelerateCount;
	/** 被加速累计时间 */
	private long accelerateTime;

	public CaveSchedule() {

	}

	public CaveSchedule(String name, int buildingType, int optionType, long lastTime) {
		super();
		this.name = name;
		this.buildingType = buildingType;
		this.optionType = optionType;
		this.startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		this.lastTime = lastTime;
		this.leftTime = lastTime;
	}

	/**
	 * 加速
	 * @param accelerate
	 */
	public synchronized void accelerate(long accelerate) {
		accelerateTime += accelerate;
		accelerateCount++;
	}

	/**
	 * 进度是否完成了
	 * @return
	 */
	public boolean hasDone() {
		return getLeftTime() <= 0;
	}

	public int getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(int buildingType) {
		this.buildingType = buildingType;
	}

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getLeftTime() {
		long left = lastTime - accelerateTime - (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime);
		return left <= 0 ? 0 : left;
	}

	public void setLeftTime(long leftTime) {
		this.leftTime = leftTime;
	}

	public int getAccelerateCount() {
		return accelerateCount;
	}

	public void setAccelerateCount(int accelerateCount) {
		this.accelerateCount = accelerateCount;
	}

	public long getAccelerateTime() {
		return accelerateTime;
	}

	public void setAccelerateTime(long accelerateTime) {
		this.accelerateTime = accelerateTime;
	}

	@Override
	public String toString() {
		return "CaveSchedule [name=" + name + ", buildingType=" + buildingType + ", optionType=" + optionType + ", startTime=" + startTime + ", lastTime=" + lastTime + ", leftTime=" + getLeftTime() + "]";
	}
}
