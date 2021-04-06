package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class UpdateObjectReq extends MessageAdapter {

	public String cacheName;
	public Serializable key;

	
	public String getName(){
		return "UPDATE_OBJECT_REQ";
	}
	
	public int getType() {
		return MessageFactory.UPDATE_OBJECT_REQ;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{

		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
		oos.writeInt(getType());
		oos.writeObject(cacheName);
		oos.writeObject(key);
		oos.flush();
	
	}
	
	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		cacheName = (String)input.readObject();
		key = (Serializable)input.readObject();
	}
	
	public int getSendType() {
		return MessageFactory.SEND_TYPE_MULTICAST;
	}
	
	public int getPriority(){
		return 10;
	}
	
	public String toString(){
		return "[UpdateObjectReq] ["+cacheName+"] ["+key+"]";
	}
}

