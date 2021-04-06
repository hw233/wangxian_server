package com.xuanzhi.tools.transport;

/**
 * 异常观察者，当网络环境中出现一些异常的时候，会通知此接口的实现者。
 * 
 */
public interface ExceptionObserver {

	/**
	 * 在某个链接上发生了异常
	 * @param conn 对应的链接
	 * @param e 对应的异常
	 * @param description 此错误的描述
	 * @param data 此异常发生时的数据
	 * @param offset
	 * @param size
	 */
	public void catchException(Connection conn,Exception e,String description,byte data[],int offset,int size);
}
