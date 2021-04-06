package com.fy.engineserver.toolitems.message;

import com.xuanzhi.tools.transport.AbstractMessageFactory;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFormatErrorException;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>BUFF_REQ</td><td>0x00000001</td><td>BUFF_RES</td><td>0x80000001</td><td>请求BUFF数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>TASK_REQ</td><td>0x00000002</td><td>TASK_RES</td><td>0x80000002</td><td>请求TASK数据</td></tr>
 * </table>
 */
public class ItemEditorMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static ItemEditorMessageFactory self;

	public static ItemEditorMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(ItemEditorMessageFactory.class){
			if(self != null) return self;
			self = new ItemEditorMessageFactory();
		}
		return self;
	}

	public Message newMessage(byte[] messageContent,int offset,int size)
		throws MessageFormatErrorException, ConnectionException {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;

		try{
			if(type == 0x00000001L){
				try {
					return new BUFF_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BUFF_REQ error:" + e.getClass() + ":" + e.getMessage());
				}
			}else if(type == 0x80000001L){
				try {
					return new BUFF_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct BUFF_RES error:" + e.getClass() + ":" + e.getMessage());
				}
			}else if(type == 0x00000002L){
				try {
					return new TASK_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TASK_REQ error:" + e.getClass() + ":" + e.getMessage());
				}
			}else if(type == 0x80000002L){
				try {
					return new TASK_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct TASK_RES error:" + e.getClass() + ":" + e.getMessage());
				}
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
}
