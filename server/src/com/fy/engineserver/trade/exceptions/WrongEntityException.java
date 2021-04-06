package com.fy.engineserver.trade.exceptions;

public class WrongEntityException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public WrongEntityException() {

	}

	public WrongEntityException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
