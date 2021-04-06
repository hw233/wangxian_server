package com.fy.engineserver.time;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


@SimpleEmbeddable
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class Timer {
	
	protected boolean dirty;
	
	/**
	 * 开启时间点
	 */
	protected long openTime;
	
	/**
	 * 结束时间点
	 */
	protected long endTime;
	
	/**
	 * 暂停时间点
	 */
	protected long pauseTime;
	
	/**
	 * 下线时间点
	 */
	protected long offlineTime;

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
		this.dirty = true;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
		this.dirty = true;
	}

	public long getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(long pauseTime) {
		this.pauseTime = pauseTime;
		this.dirty = true;
	}

	public long getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(long offlineTime) {
		this.offlineTime = offlineTime;
		this.dirty = true;
	}

	byte open = 0;
	byte offline = 1;
	byte paused = 2;
	
	protected byte nowState;
	
	/**
	 * 得到当前的状态，当前状态为
	 * @return
	 */
	public byte getNowState() {
		// TODO Auto-generated method stub
		return nowState;
	}

	/**
	 * 设置当前状态
	 * @param nowState
	 */
	public void setNowState(byte nowState) {
		// TODO Auto-generated method stub
		this.nowState = nowState;
		this.dirty = true;
	}

	/**
	 * 初始化定时器
	 * 在初始化时需要根据定时器的状态进行计算
	 */
	public abstract void init();
	
	/**
	 * 玩家下线，根据不同情况重写此方法
	 */
	public abstract void offline();
	
	/**
	 * 玩家上线调用
	 */
	public abstract void online();
	
	/**
	 * 开启计时器
	 */
	protected void open() {
		// TODO Auto-generated method stub
		setOpenTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		setNowState(open);
		
	}
	
	/**
	 * 暂停计时器
	 */
	public abstract void pause();
	
	/**
	 * 从暂停状态唤醒
	 */
	public abstract void resume();
	
	/**
	 * 时间结束
	 */
	public boolean isClosed(){
		if(endTime != 0 && com.fy.engineserver.gametime.SystemTime.currentTimeMillis() > endTime){
			return true;
		}
		return false;
	}
}
