package com.fy.engineserver.activity;

import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.config.ServerFit4Activity;

/**
 * 活动基础类，提供基础的活动服务器，时间是否满足判断-----为统一管理活动，所有活动都继承此类
 *
 */
public abstract class BaseActivityInstance {
	/** 活动开始时间 */
	private long startTime;
	/** 活动结束时间 */
	private long endTime;
	/** 服务器判断 */
	private ServerFit4Activity serverfit;
	
	public BaseActivityInstance(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		this.serverfit = new ServerFit4Activity(platForms, openServerNames, notOpenServers);
		if("".equals(startTime) || "".equals(endTime)) {
			throw new Exception("[活动开始或结束时间填写错误] [开始时间:" + startTime + "] [结束时间:" + endTime + "]");
		}
		this.startTime = TimeTool.formatter.varChar19.parse(startTime);
		this.endTime = TimeTool.formatter.varChar19.parse(endTime);
//		if (this.startTime > this.endTime) {
//			throw new Exception("[活动开启时间大于结束时间] [开始时间:" + startTime + "] [结束时间:" + endTime + "]");
//		}
	}
//	public BaseActivityInstance(Object ...args) {
//	}
	
	public String getPlatForm4Show() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < serverfit.getPlatForms().length; i++) {
			sb.append(serverfit.getPlatForms()[i]);
			if(i < (serverfit.getPlatForms().length - 1)) {
				sb.append("、");
			}
		}
		return sb.toString();
	}
	public String getOpenServer4Show() {
		if(serverfit.getOpenServerNames() == null || serverfit.getOpenServerNames().size() <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < serverfit.getOpenServerNames().size(); i++) {
			sb.append(serverfit.getOpenServerNames().get(i));
			if(i < (serverfit.getOpenServerNames().size() - 1)) {
				sb.append("、");
			}
		}
		return sb.toString();
	}
	public String getNotOpenServer4Show() {
		if(serverfit.getNotOpenServerNames() == null || serverfit.getNotOpenServerNames().size() <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < serverfit.getNotOpenServerNames().size(); i++) {
			sb.append(serverfit.getNotOpenServerNames().get(i));
			if(i < (serverfit.getNotOpenServerNames().size() - 1)) {
				sb.append("、");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 返回活动的具体描述（比如在XX商店购买N个XXX，赠送M个XXXX），用做页面展示
	 * @return
	 */
	public abstract String getInfoShow() ;
	
	/**
	 * 判断当前活动是否开放
	 * @return  null代表正常开放
	 */
	public String isThisServerFit() {
		long now = System.currentTimeMillis();
		if(!serverfit.thisServerOpen()) {
			return "此服务器不开放";
		}
		if(now < startTime || now > endTime) {
			return "未到达开放时间";
		}
		return null;
	}
	
	/**
	 * 判断某个时间活动是否开放
	 * @return  null代表正常开放
	 */
	public String isThisServerFit(long time) {
		if(!serverfit.thisServerOpen()) {
			return "此服务器不开放";
		}
		if(time < startTime || time > endTime) {
			return "未到达开放时间";
		}
		return null;
	}
	
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public ServerFit4Activity getServerfit() {
		return serverfit;
	}
	public void setServerfit(ServerFit4Activity serverfit) {
		this.serverfit = serverfit;
	}

	
}
