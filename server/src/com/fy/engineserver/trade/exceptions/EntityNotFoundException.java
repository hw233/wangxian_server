package com.fy.engineserver.trade.exceptions;

/**
 * 物品不存在
 * 
 *
 */
public class EntityNotFoundException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public EntityNotFoundException() {

	}

	public EntityNotFoundException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
