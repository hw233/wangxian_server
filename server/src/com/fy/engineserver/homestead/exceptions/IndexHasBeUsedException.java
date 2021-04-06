package com.fy.engineserver.homestead.exceptions;

public class IndexHasBeUsedException extends Throwable {

	private String msg;

	public IndexHasBeUsedException() {
		// TODO Auto-generated constructor stub
	}

	public IndexHasBeUsedException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
