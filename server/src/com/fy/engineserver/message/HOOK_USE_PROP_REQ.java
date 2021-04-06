package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 挂机使用物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagIndex</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityID</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>soulType</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class HOOK_USE_PROP_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short bagIndex;
	short index;
	long entityID;
	int soulType;

	public HOOK_USE_PROP_REQ(){
	}

	public HOOK_USE_PROP_REQ(long seqNum,short bagIndex,short index,long entityID,int soulType){
		this.seqNum = seqNum;
		this.bagIndex = bagIndex;
		this.index = index;
		this.entityID = entityID;
		this.soulType = soulType;
	}

	public HOOK_USE_PROP_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		bagIndex = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		index = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		entityID = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		soulType = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000A0F6;
	}

	public String getTypeDescription() {
		return "HOOK_USE_PROP_REQ";
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
		len += 2;
		len += 8;
		len += 4;
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

			buffer.putShort(bagIndex);
			buffer.putShort(index);
			buffer.putLong(entityID);
			buffer.putInt(soulType);
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
	 *	物品在哪个背包：包类型
	 */
	public short getBagIndex(){
		return bagIndex;
	}

	/**
	 * 设置属性：
	 *	物品在哪个背包：包类型
	 */
	public void setBagIndex(short bagIndex){
		this.bagIndex = bagIndex;
	}

	/**
	 * 获取属性：
	 *	物品在背包中所占格子的索引
	 */
	public short getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	物品在背包中所占格子的索引
	 */
	public void setIndex(short index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	物品ID
	 */
	public long getEntityID(){
		return entityID;
	}

	/**
	 * 设置属性：
	 *	物品ID
	 */
	public void setEntityID(long entityID){
		this.entityID = entityID;
	}

	/**
	 * 获取属性：
	 *	要操作的元神的类型 
	 */
	public int getSoulType(){
		return soulType;
	}

	/**
	 * 设置属性：
	 *	要操作的元神的类型 
	 */
	public void setSoulType(int soulType){
		this.soulType = soulType;
	}

}