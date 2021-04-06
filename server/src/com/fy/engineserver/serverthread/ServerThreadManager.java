package com.fy.engineserver.serverthread;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
/**
 * 线程管理，需要对应manager实现ServerThreadInterface接口
 * 
 * @date on create 2016年8月24日 下午1:44:36
 */
public class ServerThreadManager implements Runnable{
	
	public static Logger logger = TransitRobberyManager.logger;

	protected static ServerThreadManager inst;
	/** 检查间隔 */
	public static long checkInteval = 60000;
	
	public static boolean work = true;
	
	protected List<ServerThreadInterface> serverThreads = new ArrayList<ServerThreadInterface>();
	
	private static Object lock = new Object();
	
	private ServerThreadManager(){
		inst = this;
	}
	
	/**
	 * 注册管理
	 * @param st
	 */
	public static void register(ServerThreadInterface st) {
		if (inst == null) {
			synchronized (lock) {
				if (inst == null) {
					new ServerThreadManager();
					Thread t = new Thread(inst);
					t.setName("服务器线程动态管理");
					t.start();
				}
			}
		}
		try {
			if (!inst.serverThreads.contains(st)) {
				inst.serverThreads.add(st);
			}
		} catch (Exception e) {
			logger.warn("[注册线程管理] [异常] [" + st.getClass() + "]", e);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (work) {
			try {
				Thread.sleep(checkInteval);
			} catch(Exception e){}
			ServerThreadInterface[] list = serverThreads.toArray(new ServerThreadInterface[serverThreads.size()]);
			int len = list.length;
			if (len > 0) {
				for (int i=0; i<len; i++) {
					try {
						if (list[i].needChangeThreadNums()) {
							list[i].changeThreadNums();
						}
					} catch (Exception e) {
						if (logger.isInfoEnabled()) {
							logger.info("[动态修改线程数量] [异常] [" + list[i].getClass() + "]", e);
						}
					}
				}
			}
		}
	}
}
