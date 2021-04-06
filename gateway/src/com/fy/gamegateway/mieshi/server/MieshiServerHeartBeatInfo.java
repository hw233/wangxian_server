package com.fy.gamegateway.mieshi.server;

public class MieshiServerHeartBeatInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7997618544962678250L;
	
	public String serverName = "";
	
	/**
	 * 
	 * delayTimes[0] 记录1~5超时次数
	 * delayTimes[1] 记录5~10超时次数
	 * delayTimes[2] 记录10~30超时次数
	 * delayTimes[3] 记录30~超时次数
	 */
	public int delayTimes[] = new int[4];
	
	/**
	 * 
	 * totalDelayTime[0] 记录1~5超时时间总和
	 * totalDelayTime[1] 记录5~10超时时间总和
	 * totalDelayTime[2] 记录10~30超时时间总和
	 * totalDelayTime[3] 记录30~超时时间总和
	 */

	public long totalDelayTime[] = new long[4];

}
