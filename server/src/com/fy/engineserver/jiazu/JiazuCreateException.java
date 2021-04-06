package com.fy.engineserver.jiazu;

public class JiazuCreateException extends Exception {
	String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
