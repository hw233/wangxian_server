package com.fy.engineserver.time;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


/**
 * 绝对型计时器
 * 到期后就结束，不对时间进行调整
 * 
 *
 */
@SimpleEmbeddable
public class AbsoluteTimer extends Timer{

	@Override
	public byte getNowState() {
		// TODO Auto-generated method stub
		return open;
	}

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
		
	}

	@Override
	public void online() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
