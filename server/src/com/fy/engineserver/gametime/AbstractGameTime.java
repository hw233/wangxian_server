package com.fy.engineserver.gametime;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;

public abstract class AbstractGameTime implements CurrentTimeApi {

	protected long count;

	protected long startTime;
	protected long endTime;
	protected Boolean started = Boolean.FALSE;// 是否在开启状态
	protected Boolean runned = Boolean.FALSE;// 是否运行过

	protected long period = 1L;

	public long getCount() {
		return count;
	}

	public void start() {
		if (!started) {
			synchronized (started) {
				if (!started) {
					startTime = System.currentTimeMillis();
					started = Boolean.TRUE;
					runned = Boolean.TRUE;
					doStartOperate();
					return;
				}
			}
		}
		throw new IllegalStateException("GameTime already started!");
	}

	public void cancel() {
		if (started) {
			synchronized (started) {
				if (started) {
					endTime = System.currentTimeMillis();
					started = Boolean.FALSE;
					doCancelOperate();
					return;
				}
			}
		}
		throw new IllegalStateException("GameTime already canceled!");
	}

	abstract void doStartOperate();

	abstract void doCancelOperate();

	@Override
	public String getAverageVisitTimesInfo(TimeDistance distance) {
		if (!runned) {
			throw new IllegalStateException("任务还未开启");
		}
		long currentCount = count;
		long end = started ? System.currentTimeMillis() : endTime;
		long timeOff = (end - startTime) / distance.getTimeMillis();
		timeOff = timeOff == 0 ? 1 : timeOff;
		long preTimes = currentCount / timeOff;
		StringBuffer sbf = new StringBuffer();
		sbf.append("[开始时间:").append(TimeTool.formatter.varChar23.format(startTime)).append("][结束时间:").append(TimeTool.formatter.varChar23.format(end)).append("]");
		sbf.append("[时间间隔:").append(timeOff).append(distance.getName()).append("][总次数:").append(currentCount).append("][").append(preTimes).append("次/").append(distance.getName()).append("]");
		return sbf.toString();
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return "GameTime [count=" + count + ", startTime=" + startTime + ", endTime=" + endTime + ", period=" + period + ",averageVisitTimes=" + getAverageVisitTimesInfo(TimeDistance.SECOND) + "]";
	}

}
