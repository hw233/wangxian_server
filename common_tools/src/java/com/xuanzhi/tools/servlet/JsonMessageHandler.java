package com.xuanzhi.tools.servlet;

import java.util.LinkedList;
import java.util.List;

/**
 * 定义一个json处理类
 * 
 */
public abstract class JsonMessageHandler implements Runnable {
	
	protected static JsonMessageHandler self;
	
	protected List<JsonObject> queue = new LinkedList<JsonObject>();
	
	protected int queueMaxSize = 1000000;
	
	protected boolean running;
	
	public static JsonMessageHandler getInstance() {
		return self;
	}
	
	public List<JsonObject> getQueue() {
		return queue;
	}

	public void setQueue(List<JsonObject> queue) {
		this.queue = queue;
	}

	public int getQueueMaxSize() {
		return queueMaxSize;
	}

	public void setQueueMaxSize(int queueMaxSize) {
		this.queueMaxSize = queueMaxSize;
	}

	public synchronized void putMessage(JsonObject message) {
		if(queue.size() < queueMaxSize) {
			queue.add(message);
		}
	}
	
	public synchronized List<JsonObject> pop() {
		List<JsonObject> list = new LinkedList<JsonObject>();
		if(queue.size() > 0) {
			list.addAll(queue);
			queue.clear();
		}
		return list;
	}
	
	public void start() {
		this.running = true;
		Thread t = new Thread(this, "JsonMessageHandler");
		t.start();
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(100);
				List<JsonObject> list = pop();
				for(JsonObject obj : list) {
					handleMessage(obj);
				}
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理一个json数据，此方法必须立即返回
	 * @param json
	 */
	public abstract void handleMessage(JsonObject obj);
	
}
