package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;


/**
 * 无消息体。
 */
public class ACTIVE_TEST_RES implements ResponseMessage{

	static TFWMessageFactory mf = TFWMessageFactory.getInstance();
	
	long seqNum;
	
	public ACTIVE_TEST_RES(long seqNum){
		this.seqNum = seqNum;
	}
	
	/**
	 * 接收端构造函数，用于解析接收到的消息
	 * @param seqNum 此消息对应的序列号
	 * @param content 整个消息的数据
	 * @param offset 消息体开始的位置
	 * @param size 
	 */
	public ACTIVE_TEST_RES(long seqNum,byte[] content,int offset,int size){
		this.seqNum = seqNum;
	}
	
	public int writeTo(ByteBuffer buffer) {
		if(buffer.remaining() >= getLength()){
			buffer.put(mf.numberToByteArray(getLength(),mf.getNumOfByteForMessageLength()));
			buffer.put(mf.numberToByteArray(getType(),4));
			buffer.put(mf.numberToByteArray(seqNum,4));
			return getLength();
		}else{
			return 0;
		}
	}

	public int getLength() {
		return mf.getNumOfByteForMessageLength() + 4 + 4;
	}

	public int getType() {
		return 0x80000002;
	}

	public String getTypeDescription() {
		return "ACTIVE_TEST_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}
	
	public long getSequnceNum(){
		return seqNum;
	}
}
