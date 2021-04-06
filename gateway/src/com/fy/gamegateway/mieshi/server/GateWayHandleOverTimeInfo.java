package com.fy.gamegateway.mieshi.server;

public class GateWayHandleOverTimeInfo implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3253109966948060079L;

	public String serverName = "";
	
	/**
	 * 
	 * delayTimes[0] 记录1~5秒内协议处理次数
	 * delayTimes[1] 记录5~10秒内协议处理超时次数
	 * delayTimes[2] 记录10~30秒内协议处理超时次数
	 * delayTimes[3] 记录30~秒以上协议处理超时次数
	 */
	public int delayTimes[] = new int[4];
	

}
