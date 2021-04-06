package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，背包某个格子发生了变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagIndex</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>count</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fangbao</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class NOTIFY_KNAPSACKCHANGE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short bagIndex;
	short index;
	short count;
	long id;
	boolean fangbao;

	public NOTIFY_KNAPSACKCHANGE_REQ(){
	}

	public NOTIFY_KNAPSACKCHANGE_REQ(long seqNum,short bagIndex,short index,short count,long id,boolean fangbao){
		this.seqNum = seqNum;
		this.bagIndex = bagIndex;
		this.index = index;
		this.count = count;
		this.id = id;
		this.fangbao = fangbao;
	}

	public NOTIFY_KNAPSACKCHANGE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		bagIndex = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		index = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		count = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		fangbao = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x000000F9;
	}

	public String getTypeDescription() {
		return "NOTIFY_KNAPSACKCHANGE_REQ";
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
		len += 2;
		len += 8;
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

			buffer.putShort(bagIndex);
			buffer.putShort(index);
			buffer.putShort(count);
			buffer.putLong(id);
			buffer.put((byte)(fangbao==false?0:1));
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
	 *	背包的编号
	 */
	public short getBagIndex(){
		return bagIndex;
	}

	/**
	 * 设置属性：
	 *	背包的编号
	 */
	public void setBagIndex(short bagIndex){
		this.bagIndex = bagIndex;
	}

	/**
	 * 获取属性：
	 *	格子的编号
	 */
	public short getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	格子的编号
	 */
	public void setIndex(short index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	物品的个数，为0时标识空，大于0标识有东西，下面3个数组，最多只有一个元素。
	 */
	public short getCount(){
		return count;
	}

	/**
	 * 设置属性：
	 *	物品的个数，为0时标识空，大于0标识有东西，下面3个数组，最多只有一个元素。
	 */
	public void setCount(short count){
		this.count = count;
	}

	/**
	 * 获取属性：
	 *	格子中具体的物品Id，如果没有物品，值为-1
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	格子中具体的物品Id，如果没有物品，值为-1
	 */
	public void setId(long id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	true:防爆包  
	 */
	public boolean getFangbao(){
		return fangbao;
	}

	/**
	 * 设置属性：
	 *	true:防爆包  
	 */
	public void setFangbao(boolean fangbao){
		this.fangbao = fangbao;
	}

}