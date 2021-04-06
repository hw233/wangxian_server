package com.fy.engineserver.trade.exceptions;

/**
 * 钱不足
 * 
 *
 */
public class NoEnoughMoneyException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public NoEnoughMoneyException() {

	}

	public NoEnoughMoneyException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
