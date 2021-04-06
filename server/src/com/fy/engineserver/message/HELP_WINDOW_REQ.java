package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 窗口的帮助请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>id.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>String</td><td>id.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>screenW</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>screenH</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class HELP_WINDOW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte helpType;
	String id;
	short screenW;
	short screenH;

	public HELP_WINDOW_REQ(){
	}

	public HELP_WINDOW_REQ(long seqNum,byte helpType,String id,short screenW,short screenH){
		this.seqNum = seqNum;
		this.helpType = helpType;
		this.id = id;
		this.screenW = screenW;
		this.screenH = screenH;
	}

	public HELP_WINDOW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		helpType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		id = new String(content,offset,len,"UTF-8");
		offset += len;
		screenW = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		screenH = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
	}

	public int getType() {
		return 0x00000F21;
	}

	public String getTypeDescription() {
		return "HELP_WINDOW_REQ";
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
		len += 1;
		len += 2;
		try{
			len +=id.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len += 2;
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

			buffer.put(helpType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = id.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putShort(screenW);
			buffer.putShort(screenH);
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
	 *	0:窗口的帮助,1-主界面的帮助
	 */
	public byte getHelpType(){
		return helpType;
	}

	/**
	 * 设置属性：
	 *	0:窗口的帮助,1-主界面的帮助
	 */
	public void setHelpType(byte helpType){
		this.helpType = helpType;
	}

	/**
	 * 获取属性：
	 *	关键字，对窗口而言是窗口的ID
	 */
	public String getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	关键字，对窗口而言是窗口的ID
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	手机屏幕大小
	 */
	public short getScreenW(){
		return screenW;
	}

	/**
	 * 设置属性：
	 *	手机屏幕大小
	 */
	public void setScreenW(short screenW){
		this.screenW = screenW;
	}

	/**
	 * 获取属性：
	 *	手机屏幕大小
	 */
	public short getScreenH(){
		return screenH;
	}

	/**
	 * 设置属性：
	 *	手机屏幕大小
	 */
	public void setScreenH(short screenH){
		this.screenH = screenH;
	}

}