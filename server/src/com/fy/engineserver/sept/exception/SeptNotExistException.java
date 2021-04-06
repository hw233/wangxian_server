package com.fy.engineserver.sept.exception;

public class SeptNotExistException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1996880264516106034L;

	private String msg = null;

	public SeptNotExistException() {

	}

	public SeptNotExistException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
