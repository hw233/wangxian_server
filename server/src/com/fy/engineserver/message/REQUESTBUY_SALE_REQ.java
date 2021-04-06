package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 出售物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class REQUESTBUY_SALE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long id;
	long entityId;
	int index;
	int num;

	public REQUESTBUY_SALE_REQ(){
	}

	public REQUESTBUY_SALE_REQ(long seqNum,long id,long entityId,int index,int num){
		this.seqNum = seqNum;
		this.id = id;
		this.entityId = entityId;
		this.index = index;
		this.num = num;
	}

	public REQUESTBUY_SALE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		entityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F00106;
	}

	public String getTypeDescription() {
		return "REQUESTBUY_SALE_REQ";
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
		len += 8;
		len += 8;
		len += 4;
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

			buffer.putLong(id);
			buffer.putLong(entityId);
			buffer.putInt(index);
			buffer.putInt(num);
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
	 *	要出售给某个求购ID，未知发-1
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	要出售给某个求购ID，未知发-1
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	要出售的物品ID
	 */
	public long getEntityId(){
		return entityId;
	}

	/**
	 * 设置属性：
	 *	要出售的物品ID
	 */
	public void setEntityId(long entityId){
		this.entityId = entityId;
	}

	/**
	 * 获取属性：
	 *	物品所在的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	物品所在的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	出售个数
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	出售个数
	 */
	public void setNum(int num){
		this.num = num;
	}

}