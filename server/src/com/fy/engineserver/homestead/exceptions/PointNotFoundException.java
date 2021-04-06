package com.fy.engineserver.homestead.exceptions;

/**
 * 点没找到
 * 
 *
 */
public class PointNotFoundException extends Throwable {

	private String msg;

	public PointNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public PointNotFoundException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
