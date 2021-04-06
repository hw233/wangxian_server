package com.fy.engineserver.time;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


/**
 * 暂停型计时器
 * 剔除暂停时间
 * 
 *
 */
@SimpleEmbeddable
public class PauseTimer extends Timer{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offline() {
		// TODO Auto-generated method stub
		
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
