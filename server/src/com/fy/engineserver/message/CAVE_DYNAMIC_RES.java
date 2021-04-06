package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.CaveDynamic;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取仙府动态<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readMsgs.length</td><td>int</td><td>4个字节</td><td>CaveDynamic数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readMsgs[0].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readMsgs[0].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readMsgs[0].message</td><td>String</td><td>readMsgs[0].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readMsgs[1].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readMsgs[1].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readMsgs[1].message</td><td>String</td><td>readMsgs[1].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readMsgs[2].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>readMsgs[2].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>readMsgs[2].message</td><td>String</td><td>readMsgs[2].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unreadMsgs.length</td><td>int</td><td>4个字节</td><td>CaveDynamic数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unreadMsgs[0].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unreadMsgs[0].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unreadMsgs[0].message</td><td>String</td><td>unreadMsgs[0].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unreadMsgs[1].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unreadMsgs[1].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unreadMsgs[1].message</td><td>String</td><td>unreadMsgs[1].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unreadMsgs[2].time</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>unreadMsgs[2].message.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>unreadMsgs[2].message</td><td>String</td><td>unreadMsgs[2].message.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAVE_DYNAMIC_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	CaveDynamic[] readMsgs;
	CaveDynamic[] unreadMsgs;

	public CAVE_DYNAMIC_RES(){
	}

	public CAVE_DYNAMIC_RES(long seqNum,CaveDynamic[] readMsgs,CaveDynamic[] unreadMsgs){
		this.seqNum = seqNum;
		this.readMsgs = readMsgs;
		this.unreadMsgs = unreadMsgs;
	}

	public CAVE_DYNAMIC_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		readMsgs = new CaveDynamic[len];
		for(int i = 0 ; i < readMsgs.length ; i++){
			readMsgs[i] = new CaveDynamic();
			readMsgs[i].setTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			readMsgs[i].setMessage(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		unreadMsgs = new CaveDynamic[len];
		for(int i = 0 ; i < unreadMsgs.length ; i++){
			unreadMsgs[i] = new CaveDynamic();
			unreadMsgs[i].setTime((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			unreadMsgs[i].setMessage(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x8F000050;
	}

	public String getTypeDescription() {
		return "CAVE_DYNAMIC_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 4;
		for(int i = 0 ; i < readMsgs.length ; i++){
			len += 8;
			len += 2;
			if(readMsgs[i].getMessage() != null){
				try{
				len += readMsgs[i].getMessage().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < unreadMsgs.length ; i++){
			len += 8;
			len += 2;
			if(unreadMsgs[i].getMessage() != null){
				try{
				len += unreadMsgs[i].getMessage().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(readMsgs.length);
			for(int i = 0 ; i < readMsgs.length ; i++){
				buffer.putLong(readMsgs[i].getTime());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = readMsgs[i].getMessage().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(unreadMsgs.length);
			for(int i = 0 ; i < unreadMsgs.length ; i++){
				buffer.putLong(unreadMsgs[i].getTime());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = unreadMsgs[i].getMessage().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	已读消息
	 */
	public CaveDynamic[] getReadMsgs(){
		return readMsgs;
	}

	/**
	 * 设置属性：
	 *	已读消息
	 */
	public void setReadMsgs(CaveDynamic[] readMsgs){
		this.readMsgs = readMsgs;
	}

	/**
	 * 获取属性：
	 *	未读消息
	 */
	public CaveDynamic[] getUnreadMsgs(){
		return unreadMsgs;
	}

	/**
	 * 设置属性：
	 *	未读消息
	 */
	public void setUnreadMsgs(CaveDynamic[] unreadMsgs){
		this.unreadMsgs = unreadMsgs;
	}

}