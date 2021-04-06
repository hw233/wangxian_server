package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.nio.ByteBuffer;


import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 消息体为：
 * result            byte类型  表示结果，0标识正确，其他表示错误
 *                           当result为0时，返回的是String对象
 *                           当result不为0时，返回的是Exception对象
 *                           
 * message           未知类型  String对应的数据或者Exception对应的数据
 */
public class REMOVE_RES implements ResponseMessage{

	static TFWMessageFactory mf = TFWMessageFactory.getInstance();
	
	long seqNum;
	int result;
	String message;
	Throwable exception;
	byte messageBytes[];
	/**
	 * 发起端的构造函数，用于准备发送消息
	 * @param seqNum
	 * @param timeout
	 */
	public REMOVE_RES(long seqNum,String message) throws Exception{
		this.seqNum = seqNum;
		this.result = 0;
		this.message = message;
		this.exception = null;
		if(message != null){
			messageBytes = mf.getByteArrayOfObject(message);
		}
	}
	
	public REMOVE_RES(long seqNum,Throwable exception) throws Exception{
		this.seqNum = seqNum;
		this.result = 1;
		this.message = null;
		this.exception = exception;
		if(exception != null){
			messageBytes = mf.getByteArrayOfObject(exception);
		}
	}
	
	/**
	 * 接收端构造函数，用于解析接收到的消息
	 * @param seqNum 此消息对应的序列号
	 * @param content 整个消息的数据
	 * @param offset 消息体开始的位置
	 * @param size 
	 */
	public REMOVE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		this.result = (int)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int t = (int)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len > 0){
			if(result == 0){
				message = (String)mf.getObjectFromByteArray(t,content,offset,len);
			}else{
				exception = (Throwable)mf.getObjectFromByteArray(t,content,offset,len);
			}
		}
	}
	
	public int writeTo(ByteBuffer buffer) {
		if(buffer.remaining() >= getLength()){
			buffer.put(mf.numberToByteArray(getLength(),mf.getNumOfByteForMessageLength()));
			buffer.put(mf.numberToByteArray(getType(),4));
			buffer.put(mf.numberToByteArray(seqNum,4));
			
			buffer.put(mf.numberToByteArray(result,1));
			int type = 0;
			if(result == 0)
				type = mf.getTypeValueOfObject(message);
			else
				type = mf.getTypeValueOfObject(this.exception);
			
			buffer.put(mf.numberToByteArray(type,1));
			
			buffer.put(this.messageBytes);
			
			return getLength();
		}else{
			return 0;
		}
	}

	public int getLength() {
		int len = mf.getNumOfByteForMessageLength() + 4 + 4 + 1 + 1 + this.messageBytes.length;
		return len;
	}

	public int getType() {
		return 0x80000006;
	}

	public String getTypeDescription() {
		return "REMOVE_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}
	
	public long getSequnceNum(){
		return seqNum;
	}
	
	public boolean isSuccess(){
		return result == 0;
	}
	
	public String getSuccMessage(){
		return message;
	}
	
	public Throwable getException(){
		return this.exception;
	}
}
