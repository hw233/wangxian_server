package com.xuanzhi.tools.transport2;

import java.nio.ByteBuffer;
import com.xuanzhi.tools.transport.*;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHAT_MESSAGE_TEST_REQ.html'>CHAT_MESSAGE_TEST_REQ</a></td><td>0x00000002</td><td><a href='./CHAT_MESSAGE_TEST_RES.html'>CHAT_MESSAGE_TEST_RES</a></td><td>0x80000002</td><td>无说明</td></tr>
 * </table>
 */
public class GameMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static GameMessageFactory self;

	public static GameMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(GameMessageFactory.class){
			if(self != null) return self;
			self = new GameMessageFactory();
		}
		return self;
	}

	public Message newMessage(byte[] messageContent,int offset,int size)
		throws MessageFormatErrorException, ConnectionException,Exception {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;

			if(type == 0x00000002L){
					return new CHAT_MESSAGE_TEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80000002L){
					return new CHAT_MESSAGE_TEST_RES(sn,messageContent,offset,end - offset);
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
	}
}
