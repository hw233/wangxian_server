package com.fy.engineserver.activity.chestFight;

public class ChestFightThread extends Thread{
	public static boolean isopen = true;
	
	public static long sleepTime4activ = 100;
	
	public static long sleepTime4doom = 5000;		//活动未开启而且活动地图中没有玩家的时候休眠时间增长
	
	public long heartTime = sleepTime4doom;
	
	public void run() {
		ChestFightManager.logger.warn("[宝箱争夺] [线程启动] [成功]");
		long lastTime = System.currentTimeMillis();
		while (isopen) {
			long now = System.currentTimeMillis();
			try {
				ChestFightManager.inst.fight.heartBeat(now);
				if (now >= (lastTime + 15000)) {
					boolean b = ChestFightManager.inst.fight.isActiveAct();
					if (b) {
						heartTime = sleepTime4activ;
					} else {
						heartTime = sleepTime4doom;
					}
				}
			} catch (Exception e) {
				ChestFightManager.logger.warn("[宝箱争夺] [心跳异常]", e);
			}
			try {
				Thread.sleep(heartTime);
			} catch (Exception e) {
				
			}
		}
		ChestFightManager.logger.warn("[宝箱争夺] [线程关闭] [成功]");
	}
}
