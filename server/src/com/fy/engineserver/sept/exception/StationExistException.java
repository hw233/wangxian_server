package com.fy.engineserver.sept.exception;

public class StationExistException extends Throwable {

	/**
	 * 
	 * 下午02:46:50
	 */
	private static final long serialVersionUID = 1996880264516106034L;

	private String msg = null;

	public StationExistException() {

	}

	public StationExistException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



}
