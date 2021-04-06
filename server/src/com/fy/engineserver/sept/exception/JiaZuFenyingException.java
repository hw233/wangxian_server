package com.fy.engineserver.sept.exception;

public class JiaZuFenyingException extends Throwable{

	/**
	 * 2014年9月1日16:35:19
	 */
	private static final long serialVersionUID = 1996880264516106034L;

	private String msg = null;

	public JiaZuFenyingException() {

	}

	public JiaZuFenyingException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}



}
