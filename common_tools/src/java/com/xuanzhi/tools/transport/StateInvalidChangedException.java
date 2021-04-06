package com.xuanzhi.tools.transport;

public class StateInvalidChangedException extends IllegalStateException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1730952955344105179L;

	public StateInvalidChangedException(String message){
		super(message);
	}
}
