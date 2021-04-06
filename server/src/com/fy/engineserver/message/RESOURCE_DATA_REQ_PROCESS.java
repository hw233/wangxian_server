package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送资源数据包<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>keyName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>keyName</td><td>String</td><td>keyName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>offsetIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>data.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>data</td><td>byte[]</td><td>data.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class RESOURCE_DATA_REQ_PROCESS implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte resType;
	String keyName;
	int offsetIndex;
	byte[] data;

	public RESOURCE_DATA_REQ_PROCESS(){
	}

	public RESOURCE_DATA_REQ_PROCESS(long seqNum,byte resType,String keyName,int offsetIndex,byte[] data){
		this.seqNum = seqNum;
		this.resType = resType;
		this.keyName = keyName;
		this.offsetIndex = offsetIndex;
		this.data = data;
	}

	public RESOURCE_DATA_REQ_PROCESS(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		resType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		keyName = new String(content,offset,len,"UTF-8");
		offset += len;
		offsetIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 2048) throw new Exception("array length ["+len+"] big than the max length [2048]");
		data = new byte[len];
		System.arraycopy(content,offset,data,0,len);
		offset += len;
	}

	public int getType() {
		return 0x00000018;
	}

	public String getTypeDescription() {
		return "RESOURCE_DATA_REQ_PROCESS";
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
			len +=keyName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
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

			buffer.put(resType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = keyName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(offsetIndex);
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
	 *	0-错误，1-切片素材，2-部件素材，3-形象素材,4-地图，5-纯图片
	 */
	public byte getResType(){
		return resType;
	}

	/**
	 * 设置属性：
	 *	0-错误，1-切片素材，2-部件素材，3-形象素材,4-地图，5-纯图片
	 */
	public void setResType(byte resType){
		this.resType = resType;
	}

	/**
	 * 获取属性：
	 *	素材名称
	 */
	public String getKeyName(){
		return keyName;
	}

	/**
	 * 设置属性：
	 *	素材名称
	 */
	public void setKeyName(String keyName){
		this.keyName = keyName;
	}

	/**
	 * 获取属性：
	 *	包中的数据在整个数据中的偏移量，从0开始
	 */
	public int getOffsetIndex(){
		return offsetIndex;
	}

	/**
	 * 设置属性：
	 *	包中的数据在整个数据中的偏移量，从0开始
	 */
	public void setOffsetIndex(int offsetIndex){
		this.offsetIndex = offsetIndex;
	}

	/**
	 * 获取属性：
	 *	数据
	 */
	public byte[] getData(){
		return data;
	}

	/**
	 * 设置属性：
	 *	数据
	 */
	public void setData(byte[] data){
		this.data = data;
	}

}