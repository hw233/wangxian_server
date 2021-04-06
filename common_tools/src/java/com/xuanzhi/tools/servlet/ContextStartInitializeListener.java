package com.xuanzhi.tools.servlet;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xuanzhi.tools.text.DateUtil;

/**
 * 
 * 
 */
public class ContextStartInitializeListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {
		//ObjectTrackerService.config(System.getProperty("user.dir") + "/objectTrackerService.ddc", 30, "com.xuanzhi.sanguo.common.Game", null);
		String time = DateUtil.formatDate(new Date(), "yyyy年MM月dd日 HH:mm:ss");
		System.out.println("===================================================================================");
		System.out.println(" 系统于" + time + "开始启动! ");
		System.out.println("===================================================================================");
	}

	public void contextDestroyed(ServletContextEvent event) {
		String time = DateUtil.formatDate(new Date(), "yyyy年MM月dd日 HH:mm:ss");
		System.out.println("===================================================================================");
		System.out.println(" 系统于" + time + "安全关闭! ");
		System.out.println("===================================================================================");
	}
}
