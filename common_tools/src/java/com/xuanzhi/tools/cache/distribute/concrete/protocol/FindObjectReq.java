package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class FindObjectReq extends MessageAdapter {

	public String cacheName;
	public Serializable key;
	
	public long id = (long)(Math.random() * 999999999999L);
	
	public String getName(){
		return "FIND_OBJECT_REQ";
	}

	public int getType() {
		return MessageFactory.FIND_OBJECT_REQ;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeInt(getType());
			oos.writeObject(cacheName);
			oos.writeObject(key);
			oos.flush();
		}catch(Exception e){
			throw e;
		}
	}
	
	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		cacheName = (String)input.readObject();
		key = (Serializable)input.readObject();
	}
	
	public String toString(){
		return "[FindObjectReq] ["+cacheName+"] ["+key+"]";
	}
	

	public int getSendType() {
		return MessageFactory.SEND_TYPE_MULTICAST;
	}

	public int getPriority(){
		return 8;
	}
}
