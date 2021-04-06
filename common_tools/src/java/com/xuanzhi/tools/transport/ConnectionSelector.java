package com.xuanzhi.tools.transport;

/**
 * 
 *
 */
public interface ConnectionSelector {

	/**
	 * 将Connection加入到Selector中，并设置给定的关心的operation。
	 * 关于operation，可以是OP_READ或者OP_WRITE
	 * 
	 * @param conn
	 * @param operation
	 */
	public void returnConnectoin(Connection conn,int operation);
	
	/**
	 * 从Selector中取出一个Connection，这个Connection对应的operation将被清空。
	 * 即时这个Connection上有数据到达，或者有空间可以写数据，Selector都不会关心。
	 * 这样做，是为了保证同一个Connetion只有一个线程操作。
	 * 
	 * 此方法返回null表示，没有更多的链接可以用.
	 * 如果在创建新的链接出现错误，则抛出IOException,
	 * 如果所有的链接都在别使用，而且也不能再创建新的链接，则等待一段时间
	 * 
	 * @param policy 选择链接的策略，请查看SelectorPolicy类的文档。
	 * @param timeout 等待的时间，如果小于等于0，表示永远等待
	 * @see SelectorPolicy
	 */
	public Connection takeoutConnection(SelectorPolicy policy,long timeout) throws java.io.IOException;
}
