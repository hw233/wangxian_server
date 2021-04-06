package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class GetObjectRes extends MessageAdapter {

	public boolean found = false;
	public String cacheName;
	public Serializable key;
	public Serializable value = null;
	
	public String getName(){
		return "GET_OBJECT_RES";
	}
	
	public int getType() {
		return MessageFactory.GET_OBJECT_RES;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeInt(getType());
			oos.writeObject(cacheName);
			oos.writeObject(key);
			oos.writeBoolean(found);
			if(found){
				oos.writeObject(value);
			}
			oos.flush();
		}catch(Exception e){
			throw e;
		}
	}
	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		cacheName = (String)input.readObject();
		key = (Serializable)input.readObject();
		found = input.readBoolean();
		if(found){
			value = (Serializable)input.readObject();
		}
	}
	
	public String toString(){
		return "[GetObjectRes] ["+cacheName+"] ["+key+"] ["+value+"]";
	}
	
	public int getSendType() {
		return MessageFactory.SEND_TYPE_UNICAST;
	}

	public int getPriority(){
		return 10;
	}
}

