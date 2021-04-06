package com.fy.engineserver.homestead.exceptions;

/**
 * 建筑不能再升级了
 * 
 *
 */
public class BuildingCanNotLvUpException extends Throwable {

	private String msg;

	public BuildingCanNotLvUpException() {
		// TODO Auto-generated constructor stub
	}

	public BuildingCanNotLvUpException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
