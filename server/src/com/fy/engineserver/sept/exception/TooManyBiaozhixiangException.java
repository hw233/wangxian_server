package com.fy.engineserver.sept.exception;

public class TooManyBiaozhixiangException extends Throwable{

	/**
	 * 
	 * 下午02:46:50
	 */
	private static final long serialVersionUID = 1996880264516106034L;

	private String msg = null;

	public TooManyBiaozhixiangException() {

	}

	public TooManyBiaozhixiangException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



}
