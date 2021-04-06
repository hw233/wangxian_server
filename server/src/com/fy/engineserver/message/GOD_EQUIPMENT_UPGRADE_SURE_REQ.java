package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 仙装合成确认<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>tuZhiId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialIds</td><td>long[]</td><td>materialIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>materialNums.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>materialNums</td><td>int[]</td><td>materialNums.length</td><td>*</td></tr>
 * </table>
 */
public class GOD_EQUIPMENT_UPGRADE_SURE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long tuZhiId;
	long[] materialIds;
	int[] materialNums;

	public GOD_EQUIPMENT_UPGRADE_SURE_REQ(){
	}

	public GOD_EQUIPMENT_UPGRADE_SURE_REQ(long seqNum,long tuZhiId,long[] materialIds,int[] materialNums){
		this.seqNum = seqNum;
		this.tuZhiId = tuZhiId;
		this.materialIds = materialIds;
		this.materialNums = materialNums;
	}

	public GOD_EQUIPMENT_UPGRADE_SURE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		tuZhiId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialIds = new long[len];
		for(int i = 0 ; i < materialIds.length ; i++){
			materialIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		materialNums = new int[len];
		for(int i = 0 ; i < materialNums.length ; i++){
			materialNums[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x00F0EF25;
	}

	public String getTypeDescription() {
		return "GOD_EQUIPMENT_UPGRADE_SURE_REQ";
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
		len += materialIds.length * 8;
		len += 4;
		len += materialNums.length * 4;
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

			buffer.putLong(tuZhiId);
			buffer.putInt(materialIds.length);
			for(int i = 0 ; i < materialIds.length; i++){
				buffer.putLong(materialIds[i]);
			}
			buffer.putInt(materialNums.length);
			for(int i = 0 ; i < materialNums.length; i++){
				buffer.putInt(materialNums[i]);
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
	 *	图纸id
	 */
	public long getTuZhiId(){
		return tuZhiId;
	}

	/**
	 * 设置属性：
	 *	图纸id
	 */
	public void setTuZhiId(long tuZhiId){
		this.tuZhiId = tuZhiId;
	}

	/**
	 * 获取属性：
	 *	材料id集合
	 */
	public long[] getMaterialIds(){
		return materialIds;
	}

	/**
	 * 设置属性：
	 *	材料id集合
	 */
	public void setMaterialIds(long[] materialIds){
		this.materialIds = materialIds;
	}

	/**
	 * 获取属性：
	 *	材料数量集合
	 */
	public int[] getMaterialNums(){
		return materialNums;
	}

	/**
	 * 设置属性：
	 *	材料数量集合
	 */
	public void setMaterialNums(int[] materialNums){
		this.materialNums = materialNums;
	}

}