package com.fy.engineserver.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameSubThread implements Runnable {

	private List<com.fy.engineserver.core.event.Event> events;
		
	private boolean running;
	
	private boolean pooled;

	public GameSubThread() {
		this.events =  Collections.synchronizedList(new LinkedList<com.fy.engineserver.core.event.Event>());
	}

	public void addEvent(com.fy.engineserver.core.event.Event event) {
		this.events.add(event);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isPooled() {
		return pooled;
	}

	public void setPooled(boolean pooled) {
		this.pooled = pooled;
	}

	public void run() {
		if(!pooled) {
			while (true) {
				try {
					Thread.sleep(100);
					int size = events.size();
					for (int i = 0; i < size; i++) {
						com.fy.engineserver.core.event.Event event = events
								.remove(0);
						event.handle();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		} else {
			if(running) {
				try {
					int size = events.size();
					for (int i = 0; i < size; i++) {
						com.fy.engineserver.core.event.Event event = events
								.remove(0);
						event.handle();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
					running = false;
				}
			}
		}
	}
}
