package com.fy.engineserver.time;


import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 下线且暂停型计时器
 * 剔除下线时间和暂停时间
 * 
 *
 */
@SimpleEmbeddable
public class OfflineAndPauseTimer extends Timer{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offline() {
		// TODO Auto-generated method stub
		setNowState(offline);
		setOfflineTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		setNowState(paused);
		setPauseTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}

	@Override
	public void online() {
		// TODO Auto-generated method stub
		if(nowState == offline){
			open();
			long 需要减去的时间 = openTime - offlineTime;
			setEndTime(endTime + 需要减去的时间);
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		if(nowState == paused){
			open();
			long 需要减去的时间 = openTime - pauseTime;
			setEndTime(endTime + 需要减去的时间);
		}
	}

}
