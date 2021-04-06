package com.fy.engineserver.homestead.exceptions;

/**
 * 仙府不存在
 * 
 *
 */
public class FertyNotFountException extends Throwable {

	private String msg;

	public FertyNotFountException() {
		// TODO Auto-generated constructor stub
	}

	public FertyNotFountException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
