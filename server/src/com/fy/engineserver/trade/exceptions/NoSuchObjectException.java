package com.fy.engineserver.trade.exceptions;

public class NoSuchObjectException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public NoSuchObjectException() {

	}

	public NoSuchObjectException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
