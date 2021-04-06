package com.xuanzhi.tools.async;

public interface ClientUser {
	
	/**
	 * 通知引用者对象已被清除，此时客户用户如果对对象保留引用，那么可能导致内存泄露或多对象共存的问题，
	 * 此时通常客户使用者需要注销对对象的引用
	 * @param object
	 */
	public void notifyMasterCleared(Storable object);
	
}
