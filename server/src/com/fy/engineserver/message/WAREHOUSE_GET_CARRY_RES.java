package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 随身获取一个仓库<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>open</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>entityIds</td><td>long[]</td><td>entityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>counts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>counts</td><td>int[]</td><td>counts.length</td><td>*</td></tr>
 * </table>
 */
public class WAREHOUSE_GET_CARRY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	boolean open;
	long[] entityIds;
	int[] counts;

	public WAREHOUSE_GET_CARRY_RES(){
	}

	public WAREHOUSE_GET_CARRY_RES(long seqNum,boolean open,long[] entityIds,int[] counts){
		this.seqNum = seqNum;
		this.open = open;
		this.entityIds = entityIds;
		this.counts = counts;
	}

	public WAREHOUSE_GET_CARRY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		open = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		entityIds = new long[len];
		for(int i = 0 ; i < entityIds.length ; i++){
			entityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		counts = new int[len];
		for(int i = 0 ; i < counts.length ; i++){
			counts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
	}

	public int getType() {
		return 0x7000B011;
	}

	public String getTypeDescription() {
		return "WAREHOUSE_GET_CARRY_RES";
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
		len += 4;
		len += entityIds.length * 8;
		len += 4;
		len += counts.length * 4;
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

			buffer.put((byte)(open==false?0:1));
			buffer.putInt(entityIds.length);
			for(int i = 0 ; i < entityIds.length; i++){
				buffer.putLong(entityIds[i]);
			}
			buffer.putInt(counts.length);
			for(int i = 0 ; i < counts.length; i++){
				buffer.putInt(counts[i]);
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
	 *	是否打开仓库，true开启仓库，false只更新仓库
	 */
	public boolean getOpen(){
		return open;
	}

	/**
	 * 设置属性：
	 *	是否打开仓库，true开启仓库，false只更新仓库
	 */
	public void setOpen(boolean open){
		this.open = open;
	}

	/**
	 * 获取属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public long[] getEntityIds(){
		return entityIds;
	}

	/**
	 * 设置属性：
	 *	格子物品数组，值为-1的元素表示格子为空
	 */
	public void setEntityIds(long[] entityIds){
		this.entityIds = entityIds;
	}

	/**
	 * 获取属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public int[] getCounts(){
		return counts;
	}

	/**
	 * 设置属性：
	 *	格子物品数量数组，值为0的元素表示格子为空
	 */
	public void setCounts(int[] counts){
		this.counts = counts;
	}

}