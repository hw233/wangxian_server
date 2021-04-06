package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看种植物成熟时间<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>NPCIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>NPCIds</td><td>long[]</td><td>NPCIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>harvestTime.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>harvestTime</td><td>long[]</td><td>harvestTime.length</td><td>*</td></tr>
 * </table>
 */
public class CAVE_CHECK_PLANT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] NPCIds;
	long[] harvestTime;

	public CAVE_CHECK_PLANT_RES(){
	}

	public CAVE_CHECK_PLANT_RES(long seqNum,long[] NPCIds,long[] harvestTime){
		this.seqNum = seqNum;
		this.NPCIds = NPCIds;
		this.harvestTime = harvestTime;
	}

	public CAVE_CHECK_PLANT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		NPCIds = new long[len];
		for(int i = 0 ; i < NPCIds.length ; i++){
			NPCIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		harvestTime = new long[len];
		for(int i = 0 ; i < harvestTime.length ; i++){
			harvestTime[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x8F000013;
	}

	public String getTypeDescription() {
		return "CAVE_CHECK_PLANT_RES";
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
		len += 4;
		len += NPCIds.length * 8;
		len += 4;
		len += harvestTime.length * 8;
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

			buffer.putInt(NPCIds.length);
			for(int i = 0 ; i < NPCIds.length; i++){
				buffer.putLong(NPCIds[i]);
			}
			buffer.putInt(harvestTime.length);
			for(int i = 0 ; i < harvestTime.length; i++){
				buffer.putLong(harvestTime[i]);
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
	 *	npcId
	 */
	public long[] getNPCIds(){
		return NPCIds;
	}

	/**
	 * 设置属性：
	 *	npcId
	 */
	public void setNPCIds(long[] NPCIds){
		this.NPCIds = NPCIds;
	}

	/**
	 * 获取属性：
	 *	成熟时间
	 */
	public long[] getHarvestTime(){
		return harvestTime;
	}

	/**
	 * 设置属性：
	 *	成熟时间
	 */
	public void setHarvestTime(long[] harvestTime){
		this.harvestTime = harvestTime;
	}

}