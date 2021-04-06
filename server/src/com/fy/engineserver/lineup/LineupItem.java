package com.fy.engineserver.lineup;

import com.xuanzhi.tools.transport.Connection;

/**
 * 排队的实体
 * 
 * @version 创建时间：Mar 1, 2012 2:38:20 PM
 * 
 */
public class LineupItem {
	
	String userName;
	
	Connection conn;
	
	long enterTime;
	
	long notifyEnterTime = 0;
	
	int pos;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public long getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}

	public long getNotifyEnterTime() {
		return notifyEnterTime;
	}

	public void setNotifyEnterTime(long notifyEnterTime) {
		this.notifyEnterTime = notifyEnterTime;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getLogStr() {
		return "{username:"+userName+"}{pos:"+pos+"}{createTime:"+enterTime+"}{notifyTime:"+notifyEnterTime+"}";
	}
}
