package com.fy.engineserver.trade.exceptions;

/**
 * 位置越界
 * 
 *
 */
public class OutOfIndexException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public OutOfIndexException() {

	}

	public OutOfIndexException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
