package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送请求给服务器，要求进入某个地图<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resultString.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resultString</td><td>String</td><td>resultString.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>x</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>y</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>worldMapX</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>worldMapY</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ENTER_GAME_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte result;
	String resultString;
	short x;
	short y;
	short worldMapX;
	short worldMapY;

	public ENTER_GAME_RES(){
	}

	public ENTER_GAME_RES(long seqNum,byte result,String resultString,short x,short y,short worldMapX,short worldMapY){
		this.seqNum = seqNum;
		this.result = result;
		this.resultString = resultString;
		this.x = x;
		this.y = y;
		this.worldMapX = worldMapX;
		this.worldMapY = worldMapY;
	}

	public ENTER_GAME_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		resultString = new String(content,offset,len,"UTF-8");
		offset += len;
		x = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		y = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		worldMapX = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		worldMapY = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
	}

	public int getType() {
		return 0x70000090;
	}

	public String getTypeDescription() {
		return "ENTER_GAME_RES";
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
			len +=resultString.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 2;
		len += 2;
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

			buffer.put(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = resultString.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putShort(x);
			buffer.putShort(y);
			buffer.putShort(worldMapX);
			buffer.putShort(worldMapY);
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
	 *	0表示成功，1表示地图人数已满，2表示地图不存在
	 */
	public byte getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	0表示成功，1表示地图人数已满，2表示地图不存在
	 */
	public void setResult(byte result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	错误信息
	 */
	public String getResultString(){
		return resultString;
	}

	/**
	 * 设置属性：
	 *	错误信息
	 */
	public void setResultString(String resultString){
		this.resultString = resultString;
	}

	/**
	 * 获取属性：
	 *	精灵x轴当前的位置
	 */
	public short getX(){
		return x;
	}

	/**
	 * 设置属性：
	 *	精灵x轴当前的位置
	 */
	public void setX(short x){
		this.x = x;
	}

	/**
	 * 获取属性：
	 *	精灵y轴当前的位置
	 */
	public short getY(){
		return y;
	}

	/**
	 * 设置属性：
	 *	精灵y轴当前的位置
	 */
	public void setY(short y){
		this.y = y;
	}

	/**
	 * 获取属性：
	 *	精灵在世界地图中的X位置
	 */
	public short getWorldMapX(){
		return worldMapX;
	}

	/**
	 * 设置属性：
	 *	精灵在世界地图中的X位置
	 */
	public void setWorldMapX(short worldMapX){
		this.worldMapX = worldMapX;
	}

	/**
	 * 获取属性：
	 *	精灵在世界地图中的Y位置
	 */
	public short getWorldMapY(){
		return worldMapY;
	}

	/**
	 * 设置属性：
	 *	精灵在世界地图中的Y位置
	 */
	public void setWorldMapY(short worldMapY){
		this.worldMapY = worldMapY;
	}

}