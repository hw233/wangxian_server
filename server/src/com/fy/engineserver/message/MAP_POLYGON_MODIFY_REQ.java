package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器发送给客户端，通知客户端增加或者删除某个碰撞区域<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>modityType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>polygonX.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>polygonX</td><td>short[]</td><td>polygonX.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>polygonY.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>polygonY</td><td>short[]</td><td>polygonY.length</td><td>*</td></tr>
 * </table>
 */
public class MAP_POLYGON_MODIFY_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte modityType;
	short[] polygonX;
	short[] polygonY;

	public MAP_POLYGON_MODIFY_REQ(){
	}

	public MAP_POLYGON_MODIFY_REQ(long seqNum,byte modityType,short[] polygonX,short[] polygonY){
		this.seqNum = seqNum;
		this.modityType = modityType;
		this.polygonX = polygonX;
		this.polygonY = polygonY;
	}

	public MAP_POLYGON_MODIFY_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		modityType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		polygonX = new short[len];
		for(int i = 0 ; i < polygonX.length ; i++){
			polygonX[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		polygonY = new short[len];
		for(int i = 0 ; i < polygonY.length ; i++){
			polygonY[i] = (short)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
		}
	}

	public int getType() {
		return 0x0000EF08;
	}

	public String getTypeDescription() {
		return "MAP_POLYGON_MODIFY_REQ";
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
		len += 4;
		len += polygonX.length * 2;
		len += 4;
		len += polygonY.length * 2;
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

			buffer.put(modityType);
			buffer.putInt(polygonX.length);
			for(int i = 0 ; i < polygonX.length; i++){
				buffer.putShort(polygonX[i]);
			}
			buffer.putInt(polygonY.length);
			for(int i = 0 ; i < polygonY.length; i++){
				buffer.putShort(polygonY[i]);
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
	 *	修改类型，0标识增加，1标识删除
	 */
	public byte getModityType(){
		return modityType;
	}

	/**
	 * 设置属性：
	 *	修改类型，0标识增加，1标识删除
	 */
	public void setModityType(byte modityType){
		this.modityType = modityType;
	}

	/**
	 * 获取属性：
	 *	碰撞区域的X坐标
	 */
	public short[] getPolygonX(){
		return polygonX;
	}

	/**
	 * 设置属性：
	 *	碰撞区域的X坐标
	 */
	public void setPolygonX(short[] polygonX){
		this.polygonX = polygonX;
	}

	/**
	 * 获取属性：
	 *	碰撞区域的Y坐标
	 */
	public short[] getPolygonY(){
		return polygonY;
	}

	/**
	 * 设置属性：
	 *	碰撞区域的Y坐标
	 */
	public void setPolygonY(short[] polygonY){
		this.polygonY = polygonY;
	}

}