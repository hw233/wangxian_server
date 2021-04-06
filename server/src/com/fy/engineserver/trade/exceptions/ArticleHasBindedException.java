package com.fy.engineserver.trade.exceptions;

/**
 * 物品已绑定
 * 
 *
 */
public class ArticleHasBindedException extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg;

	public ArticleHasBindedException() {

	}

	public ArticleHasBindedException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
