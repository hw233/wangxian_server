package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求服务器，宠物强化，服务端发送强化成功消息前必须再次发出QUERY_EQUIPMENT_STRONG_RES通知客户端材料变化<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petEntityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongMaterialID.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>strongMaterialID</td><td>long[]</td><td>strongMaterialID.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>strongType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PET_STRONG_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petEntityId;
	long[] strongMaterialID;
	byte strongType;

	public PET_STRONG_REQ(){
	}

	public PET_STRONG_REQ(long seqNum,long petEntityId,long[] strongMaterialID,byte strongType){
		this.seqNum = seqNum;
		this.petEntityId = petEntityId;
		this.strongMaterialID = strongMaterialID;
		this.strongType = strongType;
	}

	public PET_STRONG_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petEntityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		strongMaterialID = new long[len];
		for(int i = 0 ; i < strongMaterialID.length ; i++){
			strongMaterialID[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		strongType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
	}

	public int getType() {
		return 0x00F0EE12;
	}

	public String getTypeDescription() {
		return "PET_STRONG_REQ";
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
		len += strongMaterialID.length * 8;
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

			buffer.putLong(petEntityId);
			buffer.putInt(strongMaterialID.length);
			for(int i = 0 ; i < strongMaterialID.length; i++){
				buffer.putLong(strongMaterialID[i]);
			}
			buffer.put(strongType);
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
	 *	要强化的宠物道具id，玩家的背包中必须有此宠物道具id
	 */
	public long getPetEntityId(){
		return petEntityId;
	}

	/**
	 * 设置属性：
	 *	要强化的宠物道具id，玩家的背包中必须有此宠物道具id
	 */
	public void setPetEntityId(long petEntityId){
		this.petEntityId = petEntityId;
	}

	/**
	 * 获取属性：
	 *	强化符ID
	 */
	public long[] getStrongMaterialID(){
		return strongMaterialID;
	}

	/**
	 * 设置属性：
	 *	强化符ID
	 */
	public void setStrongMaterialID(long[] strongMaterialID){
		this.strongMaterialID = strongMaterialID;
	}

	/**
	 * 获取属性：
	 *	强化类型，0表示使用金币强化，1表示使用元宝强化
	 */
	public byte getStrongType(){
		return strongType;
	}

	/**
	 * 设置属性：
	 *	强化类型，0表示使用金币强化，1表示使用元宝强化
	 */
	public void setStrongType(byte strongType){
		this.strongType = strongType;
	}

}