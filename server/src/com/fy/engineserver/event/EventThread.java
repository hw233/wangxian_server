package com.fy.engineserver.event;


/**
 * 事件管理器的驱动线程。
 * 是否考虑使用BlockingQueue，以避免轮询？
 * 
 * create on 2013年8月16日
 */
public class EventThread implements Runnable{
	public static boolean work = false;
	public void start(){
		if(work){
			EventRouter.log.warn("alread started");
			return;
		}
		work = true;
		new Thread(this, "EventThread").start();
	}
	@Override
	public void run() {
		while(work){
			EventRouter.getInst().update();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				EventRouter.log.error(e.toString());
			}
		}
		work = false;
		EventRouter.log.info("Event thread is stop");
	}

}
