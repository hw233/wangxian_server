package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 镶嵌货取出坐骑装备神匣<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>horseEquId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>inlayIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>inlayId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class INLAYTAKE_HORSEBAOSHI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long horseEquId;
	int inlayIndex;
	long inlayId;

	public INLAYTAKE_HORSEBAOSHI_RES(){
	}

	public INLAYTAKE_HORSEBAOSHI_RES(long seqNum,long horseEquId,int inlayIndex,long inlayId){
		this.seqNum = seqNum;
		this.horseEquId = horseEquId;
		this.inlayIndex = inlayIndex;
		this.inlayId = inlayId;
	}

	public INLAYTAKE_HORSEBAOSHI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		horseEquId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		inlayIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		inlayId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70FFF103;
	}

	public String getTypeDescription() {
		return "INLAYTAKE_HORSEBAOSHI_RES";
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
		len += 4;
		len += 8;
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

			buffer.putLong(horseEquId);
			buffer.putInt(inlayIndex);
			buffer.putLong(inlayId);
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
	 *	坐骑装备id
	 */
	public long getHorseEquId(){
		return horseEquId;
	}

	/**
	 * 设置属性：
	 *	坐骑装备id
	 */
	public void setHorseEquId(long horseEquId){
		this.horseEquId = horseEquId;
	}

	/**
	 * 获取属性：
	 *	镶嵌索引
	 */
	public int getInlayIndex(){
		return inlayIndex;
	}

	/**
	 * 设置属性：
	 *	镶嵌索引
	 */
	public void setInlayIndex(int inlayIndex){
		this.inlayIndex = inlayIndex;
	}

	/**
	 * 获取属性：
	 *	格子中镶嵌的物品id
	 */
	public long getInlayId(){
		return inlayId;
	}

	/**
	 * 设置属性：
	 *	格子中镶嵌的物品id
	 */
	public void setInlayId(long inlayId){
		this.inlayId = inlayId;
	}

}