package com.fy.engineserver.seal;

import java.util.ArrayList;
import java.util.List;

/**
 * 封印副本线程
 * 
 * 
 */
public class SealThread implements Runnable {

	public List<SealDownCity> tasks = new ArrayList<SealDownCity>();
	public List<SealDownCity> removeTasks = new ArrayList<SealDownCity>();
	private static long sleeptime = 500;
	private boolean isstart = false;

	public void start(String name) {
		isstart = true;
		new Thread(this, name).start();
	}

	public void stop() {
		isstart = false;
	}

	public void addTask(SealDownCity city) {
		tasks.add(city);
	}

	@Override
	public void run() {
		while (isstart) {
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (tasks.size() > 0) {
				for (SealDownCity city : tasks) {
					try {
						if (city.stat == 8) {
							removeTasks.add(city);
						} else {
							city.heartbeat();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				tasks.removeAll(removeTasks);
			}
		}
	}

}
