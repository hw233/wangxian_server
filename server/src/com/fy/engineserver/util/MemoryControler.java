package com.fy.engineserver.util;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

import com.fy.engineserver.gametime.SystemTime;
import com.xuanzhi.tools.text.StringUtil;

/**
 * 内存监控器
 * 
 * 
 */
public class MemoryControler {
	public static Logger logger = LoggerFactory.getLogger(MemoryControler.class);

	public void changeLog() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		FileAppender<ILoggingEvent> ca = new FileAppender<ILoggingEvent>();
		ca.setContext(lc);
		ca.setName(MemoryControler.class.getName());

		ca.setFile("d:/te.log");
		ca.setPrudent(false);

		ca.setAppend(false);

		PatternLayoutEncoder pl = new PatternLayoutEncoder();
		pl.setContext(lc);
		pl.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		pl.start();

		ca.setEncoder(pl);
		ca.start();

		ch.qos.logback.classic.Logger logger2 = lc.getLogger(MemoryControler.class);
		logger2.addAppender(ca);
		logger2.setAdditive(false);
		logger = logger2;
	}

	private static MemoryControler instance;
	static Object lock = new Object();
	private Timer timer;
	private TimerTask timerTask;
	private long timeDistance;

	static long base = 1024 * 1024L;

	private MemoryControler() {
		if (logger.isInfoEnabled()) logger.info("[系统初始化]" + SystemTime.getInstance().getTimer());
		this.timer = SystemTime.getInstance().getTimer();
	}

	public static MemoryControler getInstance() {
		return instance;
	}

	public void print() {
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (logger.isDebugEnabled()) logger.debug(memoryToString());
			}
		};
		timer.schedule(timerTask, 0, getTimeDistance());
	}

	public long getTimeDistance() {
		return timeDistance;
	}

	public void setTimeDistance(long timeDistance) {
		this.timeDistance = timeDistance;
	}

	static Random random = new Random();

	/**
	 * 测试log内存占用情况
	 * @param times
	 * @return
	 */
	public String testLogMemory(long times, String randomString, boolean isRandom) {
		long start = System.currentTimeMillis();
		StringBuffer sbf = new StringBuffer();
		sbf.append("[开始的时候]").append(memoryToString());
		sbf.append("\n");
		for (long i = 0; i < times; i++) {
			String prefix = StringUtil.randomString(20);// randomString;
			if (logger.isInfoEnabled()) logger.info(prefix + "[这是一些测试的东西][当前索引:{}][随机一个数:{}]", new Object[] { i }, random.nextInt(randomString.length()));
		}
		sbf.append("[结束的时候]").append(memoryToString());
		sbf.append("\n");
		sbf.append("[耗时:").append(System.currentTimeMillis() - start).append("MS]");
		return sbf.toString();
	}

	public static String memoryToString() {
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = totalMemory - freeMemory;
		return "[当前内存统计][total:" + totalMemory / base + "m] [max:" + maxMemory / base + "m] [free:" + freeMemory / base + "m] [used:" + usedMemory / base + "m]";
	}

	public void init() {
		
		instance = this;
		instance.print();
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {
		if (timerTask != null) {
			timerTask.cancel();
		}
	}

	public static void main(String[] args) {
		MemoryControler controler = new MemoryControler();
		controler.changeLog();
		try {
			for (int i = 0; i < 100; i++) {
				System.out.println(controler.testLogMemory(1000000L, "1", true));
				Thread.sleep(1000L);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
