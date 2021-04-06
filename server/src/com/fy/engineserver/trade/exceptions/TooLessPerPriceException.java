package com.fy.engineserver.trade.exceptions;

public class TooLessPerPriceException extends Throwable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public TooLessPerPriceException() {

	}

	public TooLessPerPriceException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
