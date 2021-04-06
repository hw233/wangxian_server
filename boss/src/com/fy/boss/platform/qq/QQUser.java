/**
 * 
 */
package com.fy.boss.platform.qq;

/**
 * 腾讯用户
 * @author Administrator
 *
 */
public class QQUser {
	
	
	/**
	 * 用户ID
	 */
	private String uid;
	
	/**
	 * sessionID
	 */
	private String sid;
	
	/**
	 * 登录时间
	 */
	private long loginTime;

	/**
	 * 
	 */
	public QQUser() {
		// TODO Auto-generated constructor stub
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

}
