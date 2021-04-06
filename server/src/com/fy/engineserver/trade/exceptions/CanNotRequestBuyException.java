package com.fy.engineserver.trade.exceptions;

public class CanNotRequestBuyException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public CanNotRequestBuyException() {

	}

	public CanNotRequestBuyException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
