package com.fy.engineserver.carbon.devilSquare;

import com.fy.engineserver.carbon.devilSquare.instance.DevilSquare;

/**
 * 副本心跳管理线程
 * @author Administrator
 *
 */
public class DevilSquareThread extends Thread{
	public int heartbeatTime = 5000;
	public boolean isStart = true;
	private DevilSquare ds;
	
	@Override
	public void run() {
		while(isStart) {
			long startTime = System.currentTimeMillis();
			if(ds != null) {
				try{
					ds.heartBeat();
					if(heartbeatTime == DevilSquareConstant.NOMAL) {
						if(ds.getStatus() == DevilSquareConstant.UNSTART || (ds.getStatus() == DevilSquareConstant.FINISH && ds.playerList.size() <= 0)) {
							heartbeatTime = DevilSquareConstant.DORMANT;
						}
					} else {
						if(ds.getStatus() == DevilSquareConstant.START || ds.getStatus() == DevilSquareConstant.PREPARE) {
							heartbeatTime = DevilSquareConstant.NOMAL;
						}
					}
				} catch(Exception e) {
					DevilSquareManager.logger.error("[恶魔广场线程心跳报错]["+ds.carbonLevel + "]", e);
				}
			}
			long endTime = System.currentTimeMillis();
			if(endTime > (startTime + 100)) {
				if(DevilSquareManager.logger.isWarnEnabled()) {
					DevilSquareManager.logger.warn("[恶魔广场] [副本心跳时间超时 : " + (endTime - startTime) + " ms] [carbonLevel:" + ds.carbonLevel + "]");
				}
			}
			try {
				Thread.sleep(heartbeatTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				DevilSquareManager.logger.error("[恶魔广场线程心跳报错] [" + ds.carbonLevel + "]", e);
			}
		}
	}

	public DevilSquare getDs() {
		return ds;
	}

	public void setDs(DevilSquare ds) {
		this.ds = ds;
	}
}
