package com.xuanzhi.tools.transport;

public class ConnectionException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4393028652310856882L;

	public ConnectionException(String message){
		super(message);
	}
	
	public ConnectionException(String message,Throwable e){
		super(message,e);
	}
}
