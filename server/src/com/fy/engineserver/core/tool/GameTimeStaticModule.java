package com.fy.engineserver.core.tool;


import org.slf4j.Logger;

import com.fy.engineserver.core.BeatHeartThread;
import com.fy.engineserver.core.Game;

public class GameTimeStaticModule {
	/** 时间  0，开始执行方法时间   最后一位为执行结束时间 */
	private long[] timeStatics;
	/** 时间点描述 */
	private String[] timeStaticDes;
	/** 执行时间阈值，超过此值将日志打印出来 */
	public static long DELAY_TIME = 5000;
	
	public GameTimeStaticModule(long[] timeStatics, String[] timeStaticDes) {
		super();
		this.timeStatics = timeStatics;
		this.timeStaticDes = timeStaticDes;
	}
	/**
	 * 
	 * @param logger
	 */
	public void analysis(Game game, Logger logger) {
		try {
			if (logger != null && logger.isInfoEnabled() && this.timeStatics != null && this.timeStatics.length > 1) {
				long tt = this.timeStatics[this.timeStatics.length-1] - this.timeStatics[0];
				if (tt > DELAY_TIME) {
					StringBuffer sb = new StringBuffer();
					sb.append(game.gi.name + " ");
					sb.append(" [总耗时:" + (tt) + "ms]");
					for (int i=1; i<timeStatics.length; i++) {
						sb.append(" [" + timeStaticDes[i]).append("->耗时:").append(this.timeStatics[i] - this.timeStatics[i-1]).append("ms] ");
					}
					logger.info(sb.toString());
				}
			}
		} catch (Exception e) {
			BeatHeartThread.logger.warn("[分析地图心跳耗时] [异常] [" + game.getGameInfo().name + "]", e);
		}
	}
	
	public String[] getTimeStaticDes() {
		return timeStaticDes;
	}



	public void setTimeStaticDes(String[] timeStaticDes) {
		this.timeStaticDes = timeStaticDes;
	}



	public long[] getTimeStatics() {
		return timeStatics;
	}

	public void setTimeStatics(long[] timeStatics) {
		this.timeStatics = timeStatics;
	}
	
}
