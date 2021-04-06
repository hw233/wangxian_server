package com.fy.engineserver.homestead.exceptions;

public class LevelToolowException extends Exception {

	private String msg;

	public LevelToolowException() {
		// TODO Auto-generated constructor stub
	}

	public LevelToolowException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
