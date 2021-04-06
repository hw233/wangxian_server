package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.Serializable;

public class DiscoverReq extends MessageAdapter {


	public java.util.List cacheNameList = new java.util.ArrayList();
	
	public String getName(){
		return "DISCOVER_REQ";
	}

	
	public int getType() {
		return MessageFactory.DISCOVER_REQ;
	}

	public void writeTo(java.io.OutputStream out) throws Exception{
		try{
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeInt(getType());
			oos.writeInt(cacheNameList.size());
			for(int i = 0 ; i < cacheNameList.size() ; i++){
				oos.writeObject(cacheNameList.get(i));
			}
			oos.flush();
		}catch(Exception e){
			throw e;
		}
	}

	public void readFrom(java.io.ObjectInputStream input) throws Exception{
		int s = input.readInt();
		for(int i = 0 ; i < s ; i++){
			cacheNameList.add(input.readObject());
		}
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < cacheNameList.size() ; i++){
			sb.append(cacheNameList.get(i)+",");
		}
		return "[DiscoverReq] ["+sb.toString()+"]";
	}
	
	public int getSendType() {
		return MessageFactory.SEND_TYPE_MULTICAST;
	}

	public int getPriority(){
		return 4;
	}
}
