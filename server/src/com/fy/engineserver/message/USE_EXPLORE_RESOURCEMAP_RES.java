package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 使用寻宝藏宝图道具，得到是那个地图<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>x</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>y</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showMap.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>showMap</td><td>String</td><td>showMap.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>country</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class USE_EXPLORE_RESOURCEMAP_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int x;
	int y;
	String showMap;
	byte country;

	public USE_EXPLORE_RESOURCEMAP_RES(){
	}

	public USE_EXPLORE_RESOURCEMAP_RES(long seqNum,int x,int y,String showMap,byte country){
		this.seqNum = seqNum;
		this.x = x;
		this.y = y;
		this.showMap = showMap;
		this.country = country;
	}

	public USE_EXPLORE_RESOURCEMAP_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		x = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		y = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		showMap = new String(content,offset,len,"UTF-8");
		offset += len;
		country = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x700F0300;
	}

	public String getTypeDescription() {
		return "USE_EXPLORE_RESOURCEMAP_RES";
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
		len += 4;
		len += 2;
		try{
			len +=showMap.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			buffer.putInt(x);
			buffer.putInt(y);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = showMap.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put(country);
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
	 *	显示那个地图片段的x
	 */
	public int getX(){
		return x;
	}

	/**
	 * 设置属性：
	 *	显示那个地图片段的x
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * 获取属性：
	 *	显示那个地图片段的y
	 */
	public int getY(){
		return y;
	}

	/**
	 * 设置属性：
	 *	显示那个地图片段的y
	 */
	public void setY(int y){
		this.y = y;
	}

	/**
	 * 获取属性：
	 *	显示的中文地图名
	 */
	public String getShowMap(){
		return showMap;
	}

	/**
	 * 设置属性：
	 *	显示的中文地图名
	 */
	public void setShowMap(String showMap){
		this.showMap = showMap;
	}

	/**
	 * 获取属性：
	 *	国家
	 */
	public byte getCountry(){
		return country;
	}

	/**
	 * 设置属性：
	 *	国家
	 */
	public void setCountry(byte country){
		this.country = country;
	}

}