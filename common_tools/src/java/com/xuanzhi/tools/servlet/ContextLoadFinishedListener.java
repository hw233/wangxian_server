package com.xuanzhi.tools.servlet;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xuanzhi.tools.text.DateUtil;

/**
 * 
 * 
 */
public class ContextLoadFinishedListener implements ServletContextListener {

	private static ContextLoadFinishedListener self;

	private static boolean loadFinished;

	public ContextLoadFinishedListener getInstance() {
		return self;
	}

	public static boolean isLoadFinished() {
		return loadFinished;
	}

	public static void setLoadFinished(boolean loadFinished) {
		ContextLoadFinishedListener.loadFinished = loadFinished;
	}

	public void contextInitialized(ServletContextEvent event) {
		long start = System.currentTimeMillis();
		// System.out.println("[系统加载开始测试1]
		// ["+ContextLoadFinishedListener.class.getName()+"]");
		self = this;
		loadFinished = true;
		// try {
		// Thread.sleep(10*1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		String time = DateUtil.formatDate(new Date(), "yyyy年MM月dd日 HH:mm:ss");
		System.out.println("===================================================================================");
		System.out.println(" 系统于" + time + "加载完成! ");
		System.out.println("===================================================================================");
		System.out.println("[系统监听加载完成] [PID:"+getPid()+"] [" + ContextLoadFinishedListener.class.getName() + "] [" + (System.currentTimeMillis() - start) + "ms]");
	}

	public void contextDestroyed(ServletContextEvent event) {
		self = null;
		loadFinished = false;
		String time = DateUtil.formatDate(new Date(), "yyyy年MM月dd日 HH:mm:ss");
		System.out.println("===================================================================================");
		System.out.println(" 系统于" + time + "开始销毁! ");
		System.out.println("===================================================================================");
		System.out.println("[系统监听执行销毁方法] [PID:"+getPid()+"] [" + ContextLoadFinishedListener.class.getName() + "]");
	}

	public static void main(String[] args) throws Exception {
		int pid = getPid();
		System.out.println("pid: " + pid);
		System.in.read(); // block the program so that we can do some
		// probing on it
	}

	private static int getPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName(); // format: "pid@hostname"
		try {
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Exception e) {
			return -1;
		}
	}
}
