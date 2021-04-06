package com.xuanzhi.tools.text;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.access.joran.JoranConfigurator;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.spi.JoranException;


/**
 * 
 * 
 */
public class CopyOfLogbackConfigListener implements ServletContextListener {

	private static final String CONFIG_LOCATION = "logbackConfigLocation";

	
	public void contextInitialized(ServletContextEvent event) {
		// 从web.xml中加载指定文件名的日志配置文件
		String logbackConfigLocation = event.getServletContext().getInitParameter(CONFIG_LOCATION);
		String fn = event.getServletContext().getRealPath(logbackConfigLocation);
		try {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			joranConfigurator.doConfigure(fn);
			System.out.println("[加载logback成功] [" + fn + "]");
		} catch (JoranException e) {
			System.out.println("[加载logback失败] [" + fn + "] \n" + StringUtil.getStackTrace(e));
		}
//		File file = new File(configFilePath);
//		configure(file);
//		MyWatchDog dog = new MyWatchDog();
//		dog.setDaemon(true);
//		dog.setName("Logback-Watchdog");
//		dog.addFile(file);
//		dog.start();
	}

	public void configure(File file) {
		String fn = file.getPath();
		try {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			joranConfigurator.doConfigure(fn);
			System.out.println("[加载logback成功] [" + fn + "]");
		} catch (JoranException e) {
			System.out.println("[加载logback失败] [" + fn + "] \n" + StringUtil.getStackTrace(e));
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

	public class MyWatchDog extends com.xuanzhi.tools.watchdog.FileWatchdog {
		public void doOnChange(File file) {
			try {
				configure(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
