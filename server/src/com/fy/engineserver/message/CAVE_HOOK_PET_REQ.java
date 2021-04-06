package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物存放/挂机<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>NPCId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CAVE_HOOK_PET_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte action;
	long NPCId;
	long petId;
	int index;

	public CAVE_HOOK_PET_REQ(){
	}

	public CAVE_HOOK_PET_REQ(long seqNum,byte action,long NPCId,long petId,int index){
		this.seqNum = seqNum;
		this.action = action;
		this.NPCId = NPCId;
		this.petId = petId;
		this.index = index;
	}

	public CAVE_HOOK_PET_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		action = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		NPCId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		petId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0F000024;
	}

	public String getTypeDescription() {
		return "CAVE_HOOK_PET_REQ";
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
		len += 8;
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

			buffer.put(action);
			buffer.putLong(NPCId);
			buffer.putLong(petId);
			buffer.putInt(index);
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
	 *	0-存放{自己仙洞} 1-挂机{其他人仙洞}
	 */
	public byte getAction(){
		return action;
	}

	/**
	 * 设置属性：
	 *	0-存放{自己仙洞} 1-挂机{其他人仙洞}
	 */
	public void setAction(byte action){
		this.action = action;
	}

	/**
	 * 获取属性：
	 *	NPCId
	 */
	public long getNPCId(){
		return NPCId;
	}

	/**
	 * 设置属性：
	 *	NPCId
	 */
	public void setNPCId(long NPCId){
		this.NPCId = NPCId;
	}

	/**
	 * 获取属性：
	 *	宠物ID
	 */
	public long getPetId(){
		return petId;
	}

	/**
	 * 设置属性：
	 *	宠物ID
	 */
	public void setPetId(long petId){
		this.petId = petId;
	}

	/**
	 * 获取属性：
	 *	要存放的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	要存放的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

}