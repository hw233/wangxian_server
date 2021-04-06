package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看宠物存放仓库信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>action</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>NPCId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>grade</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>petIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petIds</td><td>long[]</td><td>petIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>storePetNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>storePetNums</td><td>int[]</td><td>storePetNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>ownerId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CAVE_QUERY_PETSTORE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte action;
	long NPCId;
	int grade;
	long[] petIds;
	int[] storePetNums;
	long ownerId;

	public CAVE_QUERY_PETSTORE_RES(){
	}

	public CAVE_QUERY_PETSTORE_RES(long seqNum,byte action,long NPCId,int grade,long[] petIds,int[] storePetNums,long ownerId){
		this.seqNum = seqNum;
		this.action = action;
		this.NPCId = NPCId;
		this.grade = grade;
		this.petIds = petIds;
		this.storePetNums = storePetNums;
		this.ownerId = ownerId;
	}

	public CAVE_QUERY_PETSTORE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		action = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		NPCId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		grade = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		petIds = new long[len];
		for(int i = 0 ; i < petIds.length ; i++){
			petIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		storePetNums = new int[len];
		for(int i = 0 ; i < storePetNums.length ; i++){
			storePetNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		ownerId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x8F000019;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_PETSTORE_RES";
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
		len += 4;
		len += 4;
		len += petIds.length * 8;
		len += 4;
		len += storePetNums.length * 4;
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

			buffer.put(action);
			buffer.putLong(NPCId);
			buffer.putInt(grade);
			buffer.putInt(petIds.length);
			for(int i = 0 ; i < petIds.length; i++){
				buffer.putLong(petIds[i]);
			}
			buffer.putInt(storePetNums.length);
			for(int i = 0 ; i < storePetNums.length; i++){
				buffer.putInt(storePetNums[i]);
			}
			buffer.putLong(ownerId);
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
	 *	宠物仓库等级
	 */
	public int getGrade(){
		return grade;
	}

	/**
	 * 设置属性：
	 *	宠物仓库等级
	 */
	public void setGrade(int grade){
		this.grade = grade;
	}

	/**
	 * 获取属性：
	 *	存放的宠物ID
	 */
	public long[] getPetIds(){
		return petIds;
	}

	/**
	 * 设置属性：
	 *	存放的宠物ID
	 */
	public void setPetIds(long[] petIds){
		this.petIds = petIds;
	}

	/**
	 * 获取属性：
	 *	每个级别可存放的数量
	 */
	public int[] getStorePetNums(){
		return storePetNums;
	}

	/**
	 * 设置属性：
	 *	每个级别可存放的数量
	 */
	public void setStorePetNums(int[] storePetNums){
		this.storePetNums = storePetNums;
	}

	/**
	 * 获取属性：
	 *	主人ID
	 */
	public long getOwnerId(){
		return ownerId;
	}

	/**
	 * 设置属性：
	 *	主人ID
	 */
	public void setOwnerId(long ownerId){
		this.ownerId = ownerId;
	}

}