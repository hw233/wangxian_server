package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class HeartBitReq extends MessageAdapter {

	
	public String getName(){
		return "HEARTBIT_REQ";
	}
	
	public int getType() {
		return MessageFactory.HEARTBIT_REQ;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeInt(getType());
		
			oos.flush();
		}catch(Exception e){
			throw e;
		}
	}
	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		
	}
	
	public String toString(){
		return "[HeartBitReq]";
	}
	
	
	public int getSendType() {
		return MessageFactory.SEND_TYPE_MULTICAST;
	}
	
	public int getPriority(){
		return 2;
	}

}
