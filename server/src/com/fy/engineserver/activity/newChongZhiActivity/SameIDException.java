package com.fy.engineserver.activity.newChongZhiActivity;

public class SameIDException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6993936008545427334L;

	private String msg;

	public SameIDException() {

	}

	public SameIDException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
