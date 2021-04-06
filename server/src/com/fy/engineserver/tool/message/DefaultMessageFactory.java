package com.fy.engineserver.tool.message;

import com.xuanzhi.tools.transport.AbstractMessageFactory;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.MessageFormatErrorException;



/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./VERIFY_USER_REQ.html'>VERIFY_USER_REQ</a></td><td>0x00000040</td><td><a href='./VERIFY_USER_RES.html'>VERIFY_USER_RES</a></td><td>0x80000040</td><td>工具客户端发送验证信息给服务器，等待验证结果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NPC_REQ.html'>NPC_REQ</a></td><td>0x00000001</td><td><a href='./NPC_RES.html'>NPC_RES</a></td><td>0x80000001</td><td>请求NPC数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MONSTER_REQ.html'>MONSTER_REQ</a></td><td>0x00000002</td><td><a href='./MONSTER_RES.html'>MONSTER_RES</a></td><td>0x80000002</td><td>请求MONSTER数据</td></tr>
 * </table>
 */
public class DefaultMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static DefaultMessageFactory self;

	public static DefaultMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(DefaultMessageFactory.class){
			if(self != null) return self;
			self = new DefaultMessageFactory();
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
			if(type == 0x00000040L){
				try {
					return new VERIFY_USER_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct VERIFY_USER_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000040L){
				try {
					return new VERIFY_USER_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct VERIFY_USER_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000001L){
				try {
					return new NPC_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct NPC_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000001L){
				try {
					return new NPC_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct NPC_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x00000002L){
				try {
					return new MONSTER_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct MONSTER_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x80000002L){
				try {
					return new MONSTER_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct MONSTER_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
}
