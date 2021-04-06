package com.fy.engineserver.economic;

public class SavingFailedException extends Exception {
	private String msg;

	public SavingFailedException(String msg) {
		super();
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
