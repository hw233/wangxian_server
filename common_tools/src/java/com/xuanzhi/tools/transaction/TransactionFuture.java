package com.xuanzhi.tools.transaction;

/**
 * 一个事物处理的返回，此返回需要包含执行的结果,可以提交以及回滚等
 *
 */
public interface TransactionFuture {
	/**
	 * 是否执行成功完成
	 * @return
	 */
	public boolean isAccomplished();
	
	/**
	 * 返回的结果对象
	 * @return
	 */
	public Object getResultObject();
	
	/**
	 * 提交
	 */
	public void commit() throws Exception;
	
	/**
	 * 回滚
	 */
	public void rollback() throws Exception;
	
	/**
	 * 是否完成事物
	 * @return
	 */
	public boolean isTransactionFinished();
	
	/**
	 * 此事物的开始时间
	 * @return
	 */
	public java.util.Date getStartTime();
	
	/**
	 * 得到此事物的一些执行轨迹
	 * @return
	 */
	public String getTrace();
}
