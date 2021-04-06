package com.fy.engineserver.homestead.exceptions;

/**
 * 等级超出了上限·
 * 
 *
 */
public class OutOfMaxLevelException extends Throwable {

	private String msg;

	public OutOfMaxLevelException() {
		// TODO Auto-generated constructor stub
	}

	public OutOfMaxLevelException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
