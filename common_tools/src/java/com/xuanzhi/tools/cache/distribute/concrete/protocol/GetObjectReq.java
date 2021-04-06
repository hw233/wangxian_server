package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class GetObjectReq extends MessageAdapter {

	public String cacheName;
	public Serializable key;

	public transient int referenceNum = 0;
	
	public String getName(){
		return "GET_OBJECT_REQ";
	}
	
	public int getType() {
		return MessageFactory.GET_OBJECT_REQ;
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
		return "[GetObjectReq] ["+cacheName+"] ["+key+"]";
	}
	

	public int getSendType() {
		return MessageFactory.SEND_TYPE_UNICAST;
	}
	
	public int getPriority(){
		return 9;
	}

}
