package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 灭世游戏服务器给客户端发送资源包，一个完整的资源包由多个小资源包拼接而成<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fileName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fileName</td><td>String</td><td>fileName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>offsetNum</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>data.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>data</td><td>byte[]</td><td>data.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class MIESHI_RESOURCE_FILE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String fileName;
	long offsetNum;
	byte[] data;

	public MIESHI_RESOURCE_FILE_REQ(){
	}

	public MIESHI_RESOURCE_FILE_REQ(long seqNum,String fileName,long offsetNum,byte[] data){
		this.seqNum = seqNum;
		this.fileName = fileName;
		this.offsetNum = offsetNum;
		this.data = data;
	}

	public MIESHI_RESOURCE_FILE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		fileName = new String(content,offset,len,"UTF-8");
		offset += len;
		offsetNum = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		data = new byte[len];
		System.arraycopy(content,offset,data,0,len);
		offset += len;
	}

	public int getType() {
		return 0x66666666;
	}

	public String getTypeDescription() {
		return "MIESHI_RESOURCE_FILE_REQ";
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
			len +=fileName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 8;
		len += 4;
		len += data.length;
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
			 tmpBytes1 = fileName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putLong(offsetNum);
			buffer.putInt(data.length);
			buffer.put(data);
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
	 *	资源文件名
	 */
	public String getFileName(){
		return fileName;
	}

	/**
	 * 设置属性：
	 *	资源文件名
	 */
	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	/**
	 * 获取属性：
	 *	偏移量，由于是把一个大包分成多个小包发送，所以每个小包都需要偏移量来定位
	 */
	public long getOffsetNum(){
		return offsetNum;
	}

	/**
	 * 设置属性：
	 *	偏移量，由于是把一个大包分成多个小包发送，所以每个小包都需要偏移量来定位
	 */
	public void setOffsetNum(long offsetNum){
		this.offsetNum = offsetNum;
	}

	/**
	 * 获取属性：
	 *	小包字节数据
	 */
	public byte[] getData(){
		return data;
	}

	/**
	 * 设置属性：
	 *	小包字节数据
	 */
	public void setData(byte[] data){
		this.data = data;
	}

}