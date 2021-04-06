package com.fy.engineserver.trade.exceptions;

/**
 * 空位置
 * 
 *
 */
public class CellEmptyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public CellEmptyException() {

	}

	public CellEmptyException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
