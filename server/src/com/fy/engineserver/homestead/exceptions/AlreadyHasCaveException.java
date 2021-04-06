package com.fy.engineserver.homestead.exceptions;

/**
 * 已经有洞府了
 * 
 *
 */
public class AlreadyHasCaveException extends Throwable {

	private String msg;

	public AlreadyHasCaveException() {
		// TODO Auto-generated constructor stub
	}

	public AlreadyHasCaveException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
