package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class ClearCacheReq extends MessageAdapter {

	public String cacheName;
	
	public String getName(){
		return "CLEAR_CACHE_REQ";
	}

	
	public int getType() {
		return MessageFactory.CLEAR_CACHE_REQ;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeInt(getType());
			oos.writeObject(cacheName);

			oos.flush();
		}catch(Exception e){
			throw e;
		}
	}
	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		cacheName = (String)input.readObject();
	}
	
	public String toString(){
		return "[ClearCacheReq] ["+cacheName+"]";
	}
	

	public int getSendType() {
		return MessageFactory.SEND_TYPE_MULTICAST;
	}
	
	public int getPriority(){
		return 5;
	}
	

}
