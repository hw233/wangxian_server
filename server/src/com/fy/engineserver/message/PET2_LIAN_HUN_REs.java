package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物炼魂，吃道具<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hun</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hunNeed</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PET2_LIAN_HUN_REs implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petId;
	int hun;
	int hunNeed;

	public PET2_LIAN_HUN_REs(){
	}

	public PET2_LIAN_HUN_REs(long seqNum,long petId,int hun,int hunNeed){
		this.seqNum = seqNum;
		this.petId = petId;
		this.hun = hun;
		this.hunNeed = hunNeed;
	}

	public PET2_LIAN_HUN_REs(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		hun = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		hunNeed = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x8E0EAA66;
	}

	public String getTypeDescription() {
		return "PET2_LIAN_HUN_REs";
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

			buffer.putLong(petId);
			buffer.putInt(hun);
			buffer.putInt(hunNeed);
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
	 *	宠物id
	 */
	public long getPetId(){
		return petId;
	}

	/**
	 * 设置属性：
	 *	宠物id
	 */
	public void setPetId(long petId){
		this.petId = petId;
	}

	/**
	 * 获取属性：
	 *	新的炼魂值。
	 */
	public int getHun(){
		return hun;
	}

	/**
	 * 设置属性：
	 *	新的炼魂值。
	 */
	public void setHun(int hun){
		this.hun = hun;
	}

	/**
	 * 获取属性：
	 *	升级需要值
	 */
	public int getHunNeed(){
		return hunNeed;
	}

	/**
	 * 设置属性：
	 *	升级需要值
	 */
	public void setHunNeed(int hunNeed){
		this.hunNeed = hunNeed;
	}

}