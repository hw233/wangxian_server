package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.*;
import java.net.*;

public class MessageFactory {

	public static final int DISCOVER_REQ = 0;
	public static final int DISCOVER_RES = 1;
	public static final int FIND_OBJECT_REQ = 2;
	public static final int FIND_OBJECT_RES = 3;
	
	public static final int GET_OBJECT_REQ = 4;
	public static final int GET_OBJECT_RES = 5;
	
	public static final int UPDATE_OBJECT_REQ = 6;
	public static final int ADD_OBJECT_REQ = 7;
	public static final int REMOVE_OBJECT_REQ = 8;
	public static final int CLEAR_CACHE_REQ = 9;
	

	public static final int HEARTBIT_REQ = 10;
	
	public static final int SEND_TYPE_UNICAST = 0;
	public static final int SEND_TYPE_MULTICAST = 1;
	
	public static Message constructMessage(byte buffer[],int length) throws Exception{
		
		ObjectInputStream o = new ObjectInputStream(new ByteArrayInputStream(buffer,0,length));
		int type = o.readInt();
		Message message = null;
		switch(type){
		case DISCOVER_REQ:
			message = new DiscoverReq();
			message.readFrom(o);
			break;
		case DISCOVER_RES:
			message = new DiscoverRes();
			message.readFrom(o);
			break;
		case FIND_OBJECT_REQ:
			message = new FindObjectReq();
			message.readFrom(o);
			break;
		case FIND_OBJECT_RES:
			message = new FindObjectRes();
			message.readFrom(o);
			break;
		case GET_OBJECT_REQ:
			message = new GetObjectReq();
			message.readFrom(o);
			break;
		case GET_OBJECT_RES:
			message = new GetObjectRes();
			message.readFrom(o);
			break;
		case UPDATE_OBJECT_REQ:
			message = new UpdateObjectReq();
			message.readFrom(o);
			break;
		case ADD_OBJECT_REQ:
			message = new AddObjectReq();
			message.readFrom(o);
			break;
		case REMOVE_OBJECT_REQ:
			message = new RemoveObjectReq();
			message.readFrom(o);
			break;
		case CLEAR_CACHE_REQ:
			message = new ClearCacheReq();
			message.readFrom(o);
			break;
		case HEARTBIT_REQ:
			message = new HeartBitReq();
			message.readFrom(o);
			break;	
		default:
			throw new Exception("unknow type ["+type+"] of message");
		}
			
		return message;
	}
}
