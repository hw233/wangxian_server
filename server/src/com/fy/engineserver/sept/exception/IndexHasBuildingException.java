package com.fy.engineserver.sept.exception;

public class IndexHasBuildingException extends Throwable {

	/** 无效的Index,所选的Index已经有建筑了
	 * 
	 * 下午02:46:50
	 */
	private static final long serialVersionUID = 1996880264516106034L;

	private String msg = null;

	public IndexHasBuildingException() {

	}

	public IndexHasBuildingException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
