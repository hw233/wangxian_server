package com.xuanzhi.tools.cache.lateral.concrete.protocol;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;


/**
 * 无消息体。
 */
public class ACTIVE_TEST_REQ implements RequestMessage{

	static TFWMessageFactory mf = TFWMessageFactory.getInstance();
	
	long seqNum;
	
	public ACTIVE_TEST_REQ(long seqNum){
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
		return 0x00000002;
	}

	public String getTypeDescription() {
		return "ACTIVE_TEST_REQ";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}
	
	public long getSequnceNum(){
		return seqNum;
	}
}
