package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 更改条件，通知玩家交易栏状态<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfEntityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfEntityIds</td><td>long[]</td><td>selfEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfCounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfCounts</td><td>int[]</td><td>selfCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfCoins</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfKnapType.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfKnapType</td><td>byte[]</td><td>selfKnapType.length</td><td>数组实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfKnapCellIndexes.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>selfKnapCellIndexes</td><td>int[]</td><td>selfKnapCellIndexes.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>selfAgree</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dstEntityIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dstEntityIds</td><td>long[]</td><td>dstEntityIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dstCounts.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dstCounts</td><td>int[]</td><td>dstCounts.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>dstCoins</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>dstAgree</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * </table>
 */
public class DEAL_UPDATE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] selfEntityIds;
	int[] selfCounts;
	int selfCoins;
	byte[] selfKnapType;
	int[] selfKnapCellIndexes;
	boolean selfAgree;
	long[] dstEntityIds;
	int[] dstCounts;
	int dstCoins;
	boolean dstAgree;

	public DEAL_UPDATE_REQ(){
	}

	public DEAL_UPDATE_REQ(long seqNum,long[] selfEntityIds,int[] selfCounts,int selfCoins,byte[] selfKnapType,int[] selfKnapCellIndexes,boolean selfAgree,long[] dstEntityIds,int[] dstCounts,int dstCoins,boolean dstAgree){
		this.seqNum = seqNum;
		this.selfEntityIds = selfEntityIds;
		this.selfCounts = selfCounts;
		this.selfCoins = selfCoins;
		this.selfKnapType = selfKnapType;
		this.selfKnapCellIndexes = selfKnapCellIndexes;
		this.selfAgree = selfAgree;
		this.dstEntityIds = dstEntityIds;
		this.dstCounts = dstCounts;
		this.dstCoins = dstCoins;
		this.dstAgree = dstAgree;
	}

	public DEAL_UPDATE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		selfEntityIds = new long[len];
		for(int i = 0 ; i < selfEntityIds.length ; i++){
			selfEntityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		selfCounts = new int[len];
		for(int i = 0 ; i < selfCounts.length ; i++){
			selfCounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		selfCoins = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		selfKnapType = new byte[len];
		System.arraycopy(content,offset,selfKnapType,0,len);
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		selfKnapCellIndexes = new int[len];
		for(int i = 0 ; i < selfKnapCellIndexes.length ; i++){
			selfKnapCellIndexes[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		selfAgree = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		dstEntityIds = new long[len];
		for(int i = 0 ; i < dstEntityIds.length ; i++){
			dstEntityIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		dstCounts = new int[len];
		for(int i = 0 ; i < dstCounts.length ; i++){
			dstCounts[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		dstCoins = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		dstAgree = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
	}

	public int getType() {
		return 0x0000A006;
	}

	public String getTypeDescription() {
		return "DEAL_UPDATE_REQ";
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
		len += selfEntityIds.length * 8;
		len += 4;
		len += selfCounts.length * 4;
		len += 4;
		len += 4;
		len += selfKnapType.length;
		len += 4;
		len += selfKnapCellIndexes.length * 4;
		len += 1;
		len += 4;
		len += dstEntityIds.length * 8;
		len += 4;
		len += dstCounts.length * 4;
		len += 4;
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

			buffer.putInt(selfEntityIds.length);
			for(int i = 0 ; i < selfEntityIds.length; i++){
				buffer.putLong(selfEntityIds[i]);
			}
			buffer.putInt(selfCounts.length);
			for(int i = 0 ; i < selfCounts.length; i++){
				buffer.putInt(selfCounts[i]);
			}
			buffer.putInt(selfCoins);
			buffer.putInt(selfKnapType.length);
			buffer.put(selfKnapType);
			buffer.putInt(selfKnapCellIndexes.length);
			for(int i = 0 ; i < selfKnapCellIndexes.length; i++){
				buffer.putInt(selfKnapCellIndexes[i]);
			}
			buffer.put((byte)(selfAgree==false?0:1));
			buffer.putInt(dstEntityIds.length);
			for(int i = 0 ; i < dstEntityIds.length; i++){
				buffer.putLong(dstEntityIds[i]);
			}
			buffer.putInt(dstCounts.length);
			for(int i = 0 ; i < dstCounts.length; i++){
				buffer.putInt(dstCounts[i]);
			}
			buffer.putInt(dstCoins);
			buffer.put((byte)(dstAgree==false?0:1));
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
	 *	自己交易栏的物品，数组长度为交易栏格子数量
	 */
	public long[] getSelfEntityIds(){
		return selfEntityIds;
	}

	/**
	 * 设置属性：
	 *	自己交易栏的物品，数组长度为交易栏格子数量
	 */
	public void setSelfEntityIds(long[] selfEntityIds){
		this.selfEntityIds = selfEntityIds;
	}

	/**
	 * 获取属性：
	 *	自己交易栏的物品对应数量
	 */
	public int[] getSelfCounts(){
		return selfCounts;
	}

	/**
	 * 设置属性：
	 *	自己交易栏的物品对应数量
	 */
	public void setSelfCounts(int[] selfCounts){
		this.selfCounts = selfCounts;
	}

	/**
	 * 获取属性：
	 *	自己金钱
	 */
	public int getSelfCoins(){
		return selfCoins;
	}

	/**
	 * 设置属性：
	 *	自己金钱
	 */
	public void setSelfCoins(int selfCoins){
		this.selfCoins = selfCoins;
	}

	/**
	 * 获取属性：
	 *	自己交易栏的物品对应的背包栏类型
	 */
	public byte[] getSelfKnapType(){
		return selfKnapType;
	}

	/**
	 * 设置属性：
	 *	自己交易栏的物品对应的背包栏类型
	 */
	public void setSelfKnapType(byte[] selfKnapType){
		this.selfKnapType = selfKnapType;
	}

	/**
	 * 获取属性：
	 *	自己交易栏的物品对应的背包栏下标，客户端据此锁定/解锁背包栏的格子
	 */
	public int[] getSelfKnapCellIndexes(){
		return selfKnapCellIndexes;
	}

	/**
	 * 设置属性：
	 *	自己交易栏的物品对应的背包栏下标，客户端据此锁定/解锁背包栏的格子
	 */
	public void setSelfKnapCellIndexes(int[] selfKnapCellIndexes){
		this.selfKnapCellIndexes = selfKnapCellIndexes;
	}

	/**
	 * 获取属性：
	 *	自己是否已锁定
	 */
	public boolean getSelfAgree(){
		return selfAgree;
	}

	/**
	 * 设置属性：
	 *	自己是否已锁定
	 */
	public void setSelfAgree(boolean selfAgree){
		this.selfAgree = selfAgree;
	}

	/**
	 * 获取属性：
	 *	对方交易栏的物品，数组长度为交易栏格子数量
	 */
	public long[] getDstEntityIds(){
		return dstEntityIds;
	}

	/**
	 * 设置属性：
	 *	对方交易栏的物品，数组长度为交易栏格子数量
	 */
	public void setDstEntityIds(long[] dstEntityIds){
		this.dstEntityIds = dstEntityIds;
	}

	/**
	 * 获取属性：
	 *	对方交易栏的物品对应数量
	 */
	public int[] getDstCounts(){
		return dstCounts;
	}

	/**
	 * 设置属性：
	 *	对方交易栏的物品对应数量
	 */
	public void setDstCounts(int[] dstCounts){
		this.dstCounts = dstCounts;
	}

	/**
	 * 获取属性：
	 *	对方金钱
	 */
	public int getDstCoins(){
		return dstCoins;
	}

	/**
	 * 设置属性：
	 *	对方金钱
	 */
	public void setDstCoins(int dstCoins){
		this.dstCoins = dstCoins;
	}

	/**
	 * 获取属性：
	 *	对方是否已锁定
	 */
	public boolean getDstAgree(){
		return dstAgree;
	}

	/**
	 * 设置属性：
	 *	对方是否已锁定
	 */
	public void setDstAgree(boolean dstAgree){
		this.dstAgree = dstAgree;
	}

}