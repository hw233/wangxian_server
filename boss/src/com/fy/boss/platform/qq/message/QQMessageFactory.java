package com.fy.boss.platform.qq.message;

import com.fy.boss.platform.qq.message.QQMessageFactory;
import com.fy.boss.platform.qq.message.QQ_ORIGINAL_REQ;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFactory;
import com.xuanzhi.tools.transport.MessageFormatErrorException;

public class QQMessageFactory implements MessageFactory {

	protected static QQMessageFactory self;

	public static QQMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(QQMessageFactory.class){
			if(self != null) return self;
			self = new QQMessageFactory();
		}
		return self;
	}
	
	public int getNumOfByteForMessageLength() {
		return 4;
	}

	/**
	 * 将网络传输的字节数组转化为整数，遵循高位字节在前的规则。
	 * 比如：字节数组为
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}
	 * 那么对应的整数为为：
	 *     0x007c1234abcd0088     
	 */
	public long byteArrayToNumber(byte[] bytes, int offset,int numOfBytes) {
		long l = 0L;
		for(int i = 0 ; i < numOfBytes ; i++){
			l = l | ((long)(bytes[offset + i] & 0xFF) << (8 * (numOfBytes - 1 - i)));
		}
		return l;
	}

	/**
	 * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。
	 * 比如：	整数为
	 *     0x 007c 1234 abcd 0088
	 * 那么对应的字节数组为：
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}     
	 */
	public byte[] numberToByteArray(long n, int numOfBytes) {
		byte bytes[] = new byte[numOfBytes];
		for(int i = 0 ; i < numOfBytes ; i++)
			bytes[i] = (byte)((n >> (8 *(numOfBytes - 1 - i))) & 0xFF);
		return bytes;
	}

	/**
	 * 将整数转化为网络传输的字节数组，遵循高位字节在前的规则。
	 * 比如：	整数为
	 *     0x 007c 1234 abcd 0088
	 * 那么对应的字节数组为：
	 *     {0x00,0x7c,0x12,0x34,0xab,0xcd,0x00,0x88}     
	 */
	public byte[] numberToByteArray(int n, int numOfBytes) {
		byte bytes[] = new byte[numOfBytes];
		for(int i = 0 ; i < numOfBytes ; i++)
			bytes[i] = (byte)((n >> (8*(numOfBytes - 1 - i))) & 0xFF);
		return bytes;
	}

	@Override
	public Message newMessage(byte[] messageContent, int offset, int size) 
			throws MessageFormatErrorException, ConnectionException, Exception {
		// TODO Auto-generated method stub
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		offset += getNumOfByteForMessageLength();
		byte content[] = new byte[size];
		System.arraycopy(messageContent, offset, content, 0, size-offset);
		return new QQ_ORIGINAL_REQ(content);
	}
	
}
