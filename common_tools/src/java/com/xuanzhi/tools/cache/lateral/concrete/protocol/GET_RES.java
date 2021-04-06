package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.nio.ByteBuffer;


import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 消息体为：
 *    cacheName		String
 *    handleModel   byte
 *    key           未知类型，根据实际情况判断
 *    value			未知类型，根据实际情况判断
 */
public class GET_RES implements ResponseMessage{

	static TFWMessageFactory mf = TFWMessageFactory.getInstance();
	
	long seqNum;
	int handleModel;
	String cacheName;
	Object key;
	Object value;
	byte keyBytes[];
	byte valueBytes[];
	/**
	 * 发起端的构造函数，用于准备发送消息
	 * @param seqNum
	 * @param timeout
	 */
	public GET_RES(long seqNum,String cacheName,int handleModel,Object key,Object value) throws Exception{
		this.seqNum = seqNum;
		this.handleModel = handleModel;
		this.cacheName = cacheName;
		this.key = key;
		this.value = value;
		keyBytes = mf.getByteArrayOfObject(key);
		valueBytes = mf.getByteArrayOfObject(value);
	}
	
	/**
	 * 接收端构造函数，用于解析接收到的消息
	 * @param seqNum 此消息对应的序列号
	 * @param content 整个消息的数据
	 * @param offset 消息体开始的位置
	 * @param size 
	 */
	public GET_RES(long seqNum,byte[] content,int offset,int size)throws Exception{
		this.seqNum = seqNum;
		int len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		this.cacheName = new String(content,offset,len);
		offset += len;
		this.handleModel = (int)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int t = (int)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = mf.getLengthByTypeValue(t);
		if(len == -1){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		key = mf.getObjectFromByteArray(t,content,offset,len);
		offset += len;
		
		t = (int)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		len = mf.getLengthByTypeValue(t);
		if(len == -1){
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		value = mf.getObjectFromByteArray(t,content,offset,len);
		offset += len;
		
	}
	
	public int writeTo(ByteBuffer buffer) {
		if(buffer.remaining() >= getLength()){
			buffer.put(mf.numberToByteArray(getLength(),mf.getNumOfByteForMessageLength()));
			buffer.put(mf.numberToByteArray(getType(),4));
			buffer.put(mf.numberToByteArray(seqNum,4));
			
			try {
				buffer.put(mf.getByteArrayOfObject(cacheName));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			buffer.put(mf.numberToByteArray(handleModel,1));
			
			buffer.put(mf.numberToByteArray(mf.getTypeValueOfObject(key),1));
			buffer.put(keyBytes);
			
			buffer.put(mf.numberToByteArray(mf.getTypeValueOfObject(value),1));
			buffer.put(valueBytes);
			
			return getLength();
		}else{
			return 0;
		}
	}

	public int getLength() {
		if(keyBytes == null){
			try {
				this.keyBytes = mf.getByteArrayOfObject(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(valueBytes == null){
			try {
				this.valueBytes = mf.getByteArrayOfObject(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mf.getNumOfByteForMessageLength() + 4 + 4 + 
		2 + cacheName.getBytes().length + 1 
			+ 1 + keyBytes.length 
			+ 1 + valueBytes.length;
	}

	public int getType() {
		return 0x80000003;
	}

	public String getTypeDescription() {
		return "GET_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}
	
	public long getSequnceNum(){
		return seqNum;
	}
	
	public String getCacheName(){
		return cacheName;
	}
	
	public int getHandleModel(){
		return this.handleModel;
	}
	
	public Object getKey(){
		return key;
	}
	
	public Object getValue(){
		return value;
	}
}
