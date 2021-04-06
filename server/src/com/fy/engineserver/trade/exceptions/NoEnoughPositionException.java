package com.fy.engineserver.trade.exceptions;

/**
 * 位置不足(满包)
 * 
 *
 */
public class NoEnoughPositionException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public NoEnoughPositionException() {

	}

	public NoEnoughPositionException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
