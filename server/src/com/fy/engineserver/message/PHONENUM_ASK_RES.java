package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 手机号绑定答题<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>msg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>msg</td><td>String</td><td>msg.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>phoneNum.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>phoneNum</td><td>String</td><td>phoneNum.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>jiaoyanTime</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sendSpace</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sendMsg.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sendMsg</td><td>String</td><td>sendMsg.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class PHONENUM_ASK_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String msg;
	String phoneNum;
	long jiaoyanTime;
	int sendSpace;
	String sendMsg;

	public PHONENUM_ASK_RES(){
	}

	public PHONENUM_ASK_RES(long seqNum,String msg,String phoneNum,long jiaoyanTime,int sendSpace,String sendMsg){
		this.seqNum = seqNum;
		this.msg = msg;
		this.phoneNum = phoneNum;
		this.jiaoyanTime = jiaoyanTime;
		this.sendSpace = sendSpace;
		this.sendMsg = sendMsg;
	}

	public PHONENUM_ASK_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		msg = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		phoneNum = new String(content,offset,len,"UTF-8");
		offset += len;
		jiaoyanTime = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		sendSpace = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		sendMsg = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x700EAE11;
	}

	public String getTypeDescription() {
		return "PHONENUM_ASK_RES";
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
		len += 2;
		try{
			len +=msg.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		try{
			len +=phoneNum.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		len += 2;
		try{
			len +=sendMsg.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = msg.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
				try{
			tmpBytes1 = phoneNum.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(jiaoyanTime);
			buffer.putInt(sendSpace);
				try{
			tmpBytes1 = sendMsg.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	答题手机号相关信息
	 */
	public String getMsg(){
		return msg;
	}

	/**
	 * 设置属性：
	 *	答题手机号相关信息
	 */
	public void setMsg(String msg){
		this.msg = msg;
	}

	/**
	 * 获取属性：
	 *	手机号
	 */
	public String getPhoneNum(){
		return phoneNum;
	}

	/**
	 * 设置属性：
	 *	手机号
	 */
	public void setPhoneNum(String phoneNum){
		this.phoneNum = phoneNum;
	}

	/**
	 * 获取属性：
	 *	可以校验的剩余时间
	 */
	public long getJiaoyanTime(){
		return jiaoyanTime;
	}

	/**
	 * 设置属性：
	 *	可以校验的剩余时间
	 */
	public void setJiaoyanTime(long jiaoyanTime){
		this.jiaoyanTime = jiaoyanTime;
	}

	/**
	 * 获取属性：
	 *	发送间隔
	 */
	public int getSendSpace(){
		return sendSpace;
	}

	/**
	 * 设置属性：
	 *	发送间隔
	 */
	public void setSendSpace(int sendSpace){
		this.sendSpace = sendSpace;
	}

	/**
	 * 获取属性：
	 *	发送后显示的信息
	 */
	public String getSendMsg(){
		return sendMsg;
	}

	/**
	 * 设置属性：
	 *	发送后显示的信息
	 */
	public void setSendMsg(String sendMsg){
		this.sendMsg = sendMsg;
	}

}