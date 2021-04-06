package com.fy.engineserver.trade.exceptions;

/**
 * 物品数量不足
 * 
 *
 */
public class NoEnoughNumberException extends Throwable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public NoEnoughNumberException() {

	}

	public NoEnoughNumberException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
