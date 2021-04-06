package com.fy.engineserver.gametime;

import java.util.Timer;
import java.util.TimerTask;

public class UseSelfTime extends AbstractGameTime {

	private long currentTimeMillis;

	private static Boolean started = false;

	private Timer timer = null;
	private TimerTask timerTask = null;

	private static UseSelfTime instance;
	
	private UseSelfTime() {

	}

	public static UseSelfTime getInstance() {
		if (instance == null) {
			synchronized (started) {
				if (instance == null) {
					instance = new UseSelfTime();
				}
			}
		}
		return instance;
	}

	@Override
	public long currentTimeMillis() {
		count++;
		return currentTimeMillis;
	}

	@Override
	void doStartOperate() {
		synchronized (this) {
			if (!started) {
				synchronized (started) {
					if (!started) {
						timer = new Timer();
						timerTask = new TimerTask() {
							@Override
							public void run() {
								currentTimeMillis = System.currentTimeMillis();
							}
						};

						timer.schedule(timerTask, 0, period);
					}
				}
			}
		}
	}

	@Override
	void doCancelOperate() {
		synchronized (this) {
			if (timerTask != null) {
				timerTask.cancel();
			}
			if (timer != null) {
				timer.cancel();
			}
		}
	}
}
