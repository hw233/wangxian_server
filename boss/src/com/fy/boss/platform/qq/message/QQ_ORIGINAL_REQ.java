package com.fy.boss.platform.qq.message;

import java.nio.ByteBuffer;

import com.fy.boss.platform.qq.message.QQMessageFactory;
import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 腾讯发送过来的原始消息包
 */
public class QQ_ORIGINAL_REQ implements RequestMessage{

	static QQMessageFactory mf = QQMessageFactory.getInstance();
	
	private byte[] data;
	
	public QQ_ORIGINAL_REQ(byte[] content) throws Exception{
		this.data = content;
	}

	public int getType() {
		return 0;
	}

	public String getTypeDescription() {
		return "QQ_ORIGINAL_REQ";
	}

	public String getSequenceNumAsString() {
		return "0";
	}

	public long getSequnceNum(){
		return 0;
	}

	public int getLength() {
		return data.length;
	}
	
	public byte[] getData() {
		return data;
	}

	@Override
	public int writeTo(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.put(data);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

}