package com.fy.engineserver.movedata.movedata4tencent;

import java.util.ArrayList;
import java.util.List;

public class Move4TencentThread implements Runnable{
	
	private List<Long> playerIds = new ArrayList<Long>();
	
	public boolean finish = false;
	
	public String threadName = "";
	
	public long startTime;
	
	public long endTime;
	
	public Move4TencentThread(List<Long> list) {
		this.setPlayerIds(list);
	}
	
	public static int 时间 = 200;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		startTime = System.currentTimeMillis();
		for (int i=0; i<getPlayerIds().size(); i++) {
			Move4TencentManager.inst.move(getPlayerIds().get(i));
			try {
				Thread.sleep(时间);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Move4TencentManager.logger.warn("["+threadName+"] [总player数:" + playerIds.size() + "] [已执行完:" + i + "]");
		}
		endTime = System.currentTimeMillis();
		Move4TencentManager.inst.notifyThreadFinish(this);
		finish = true;
	}

	public List<Long> getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(List<Long> playerIds) {
		this.playerIds = playerIds;
	}

}
