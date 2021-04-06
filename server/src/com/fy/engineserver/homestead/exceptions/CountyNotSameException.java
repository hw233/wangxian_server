package com.fy.engineserver.homestead.exceptions;

/**
 * 角色和仙境的国家需要一致
 * 
 *
 */
public class CountyNotSameException extends Throwable {

	private String msg;

	public CountyNotSameException() {
		// TODO Auto-generated constructor stub
	}

	public CountyNotSameException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
