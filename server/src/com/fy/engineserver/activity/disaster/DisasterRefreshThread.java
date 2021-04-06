package com.fy.engineserver.activity.disaster;

import com.fy.engineserver.activity.disaster.instance.Disaster;

/**
 * 金猴天灾Npc怪物等刷新线程
 */
public class DisasterRefreshThread extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DisasterManager.logger.warn("[DisasterRefreshThread] [启动成功] [" + this.getName() + "]");
		while (DisasterGameThread.isOpen) {
			long now = System.currentTimeMillis();
			for (int i=0; i<DisasterManager.getInst().gameThreads.length; i++) {
				Disaster[] ds = DisasterManager.getInst().gameThreads[i].getGameList().toArray(new Disaster[0]);
				if (ds != null && ds.length > 0) {
					for (Disaster d : ds) {
						try {
							d.checkStepChange(now);
						} catch (Exception e) {
							DisasterManager.logger.warn("[通知活动场景刷NPC] [异常] [" + d.getLogString() + "]", e);
						}
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				
			}
		}
		DisasterManager.logger.warn("[DisasterRefreshThread] [关闭成功] [" + this.getName() + "]");
	}

}
