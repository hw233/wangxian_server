package com.fy.boss.cmd.message;

import com.fy.boss.cmd.message.CMDMessageFactory;
import com.fy.boss.cmd.message.COMMON_CMD_REQ;
import com.fy.boss.cmd.message.COMMON_CMD_RES;
import com.fy.boss.cmd.message.FILE_PACKET_REQ;
import com.fy.boss.cmd.message.FILE_PACKET_RES;
import com.fy.boss.cmd.message.SERVER_LOG_REQ;
import com.fy.boss.cmd.message.SERVER_LOG_RES;
import com.fy.boss.cmd.message.SERVER_STATUS_REQ;
import com.fy.boss.cmd.message.SERVER_STATUS_RES;
import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COMMON_CMD_REQ.html'>COMMON_CMD_REQ</a></td><td>0x0000A000</td><td><a href='./COMMON_CMD_RES.html'>COMMON_CMD_RES</a></td><td>0x8000A000</td><td>普通Shell命令</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FILE_PACKET_REQ.html'>FILE_PACKET_REQ</a></td><td>0x0000A001</td><td><a href='./FILE_PACKET_RES.html'>FILE_PACKET_RES</a></td><td>0x8000A001</td><td>拷贝数据包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_STATUS_REQ.html'>SERVER_STATUS_REQ</a></td><td>0x0000A002</td><td><a href='./SERVER_STATUS_RES.html'>SERVER_STATUS_RES</a></td><td>0x8000A002</td><td>服务器状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_LOG_REQ.html'>SERVER_LOG_REQ</a></td><td>0x0000A003</td><td><a href='./SERVER_LOG_RES.html'>SERVER_LOG_RES</a></td><td>0x8000A003</td><td>服务器日志</td></tr>
 * </table>
 */
public class CMDMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static CMDMessageFactory self;

	public static CMDMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(CMDMessageFactory.class){
			if(self != null) return self;
			self = new CMDMessageFactory();
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
			if(type == 0x0000A000L){
				try {
					return new COMMON_CMD_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct COMMON_CMD_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x8000A000L){
				try {
					return new COMMON_CMD_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct COMMON_CMD_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x0000A001L){
				try {
					return new FILE_PACKET_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FILE_PACKET_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x8000A001L){
				try {
					return new FILE_PACKET_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct FILE_PACKET_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x0000A002L){
				try {
					return new SERVER_STATUS_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SERVER_STATUS_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x8000A002L){
				try {
					return new SERVER_STATUS_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SERVER_STATUS_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x0000A003L){
				try {
					return new SERVER_LOG_REQ(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SERVER_LOG_REQ error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else if(type == 0x8000A003L){
				try {
					return new SERVER_LOG_RES(sn,messageContent,offset,end - offset);
				} catch (Exception e) {
					throw new MessageFormatErrorException("construct SERVER_LOG_RES error:" + e.getClass() + ":" + e.getMessage(),e);
				}
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
		}catch(IndexOutOfBoundsException e){
			throw new ConnectionException("parse message error",e);
		}
	}
}
