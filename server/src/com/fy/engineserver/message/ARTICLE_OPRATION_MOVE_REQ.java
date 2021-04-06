package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发起的对背包中物品进行移动<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>moveType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>splitOutCount</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>indexFrom</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>indexTo</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ARTICLE_OPRATION_MOVE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte moveType;
	short splitOutCount;
	short indexFrom;
	short indexTo;

	public ARTICLE_OPRATION_MOVE_REQ(){
	}

	public ARTICLE_OPRATION_MOVE_REQ(long seqNum,byte moveType,short splitOutCount,short indexFrom,short indexTo){
		this.seqNum = seqNum;
		this.moveType = moveType;
		this.splitOutCount = splitOutCount;
		this.indexFrom = indexFrom;
		this.indexTo = indexTo;
	}

	public ARTICLE_OPRATION_MOVE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		moveType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		splitOutCount = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		indexFrom = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		indexTo = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
	}

	public int getType() {
		return 0x000001F6;
	}

	public String getTypeDescription() {
		return "ARTICLE_OPRATION_MOVE_REQ";
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

			buffer.put(moveType);
			buffer.putShort(splitOutCount);
			buffer.putShort(indexFrom);
			buffer.putShort(indexTo);
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
	 *	0:一般包移动到一般包 1:防爆包移动到防爆包 2:一般包移动到防爆包3：防爆包移动到一般包
	 */
	public byte getMoveType(){
		return moveType;
	}

	/**
	 * 设置属性：
	 *	0:一般包移动到一般包 1:防爆包移动到防爆包 2:一般包移动到防爆包3：防爆包移动到一般包
	 */
	public void setMoveType(byte moveType){
		this.moveType = moveType;
	}

	/**
	 * 获取属性：
	 *	拆分几个出来
	 */
	public short getSplitOutCount(){
		return splitOutCount;
	}

	/**
	 * 设置属性：
	 *	拆分几个出来
	 */
	public void setSplitOutCount(short splitOutCount){
		this.splitOutCount = splitOutCount;
	}

	/**
	 * 获取属性：
	 *	物品在背包中所占格子的索引
	 */
	public short getIndexFrom(){
		return indexFrom;
	}

	/**
	 * 设置属性：
	 *	物品在背包中所占格子的索引
	 */
	public void setIndexFrom(short indexFrom){
		this.indexFrom = indexFrom;
	}

	/**
	 * 获取属性：
	 *	物品在背包中所占格子的索引
	 */
	public short getIndexTo(){
		return indexTo;
	}

	/**
	 * 设置属性：
	 *	物品在背包中所占格子的索引
	 */
	public void setIndexTo(short indexTo){
		this.indexTo = indexTo;
	}

}