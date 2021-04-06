package com.fy.engineserver.gametime;

public class UseSystemTime extends AbstractGameTime {
	private static UseSystemTime instance;
	private static Object started = new Object();

	private UseSystemTime() {

	}

	public static UseSystemTime getInstance() {
		if (instance == null) {
			synchronized (started) {
				if (instance == null) {
					instance = new UseSystemTime();
				}
			}
		}
		return instance;
	}

	@Override
	public long currentTimeMillis() {
		count++;
		return System.currentTimeMillis();
	}

	@Override
	void doStartOperate() {

	}

	@Override
	void doCancelOperate() {

	}

}
