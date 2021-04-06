package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.net.InetAddress;

import com.xuanzhi.tools.cache.Cacheable;

public abstract class MessageAdapter implements Message,Cacheable {

	protected InetAddress toAddress;
	
	protected InetAddress fromAddress;
	
	public int getPriority() {
		return 0;
	}
	
	public InetAddress getToAddress() {
		return toAddress;
	}

	public InetAddress getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(InetAddress address) {
		this.fromAddress = address;
	}

	public void setToAddress(InetAddress address) {
		this.toAddress = address;
	}
	
	public int getSize(){
		return 1;
	}
}
