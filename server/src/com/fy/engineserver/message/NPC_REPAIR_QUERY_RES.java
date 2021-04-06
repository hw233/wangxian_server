package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查询玩家身上各个装备的修理价格<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentIds</td><td>long[]</td><td>equipmentIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>prices.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>prices</td><td>int[]</td><td>prices.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>priceForAll</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>priceForCurrent</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class NPC_REPAIR_QUERY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] equipmentIds;
	int[] prices;
	int priceForAll;
	int priceForCurrent;

	public NPC_REPAIR_QUERY_RES(){
	}

	public NPC_REPAIR_QUERY_RES(long seqNum,long[] equipmentIds,int[] prices,int priceForAll,int priceForCurrent){
		this.seqNum = seqNum;
		this.equipmentIds = equipmentIds;
		this.prices = prices;
		this.priceForAll = priceForAll;
		this.priceForCurrent = priceForCurrent;
	}

	public NPC_REPAIR_QUERY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		equipmentIds = new long[len];
		for(int i = 0 ; i < equipmentIds.length ; i++){
			equipmentIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		prices = new int[len];
		for(int i = 0 ; i < prices.length ; i++){
			prices[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		priceForAll = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		priceForCurrent = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EEF2;
	}

	public String getTypeDescription() {
		return "NPC_REPAIR_QUERY_RES";
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
		len += equipmentIds.length * 8;
		len += 4;
		len += prices.length * 4;
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

			buffer.putInt(equipmentIds.length);
			for(int i = 0 ; i < equipmentIds.length; i++){
				buffer.putLong(equipmentIds[i]);
			}
			buffer.putInt(prices.length);
			for(int i = 0 ; i < prices.length; i++){
				buffer.putInt(prices[i]);
			}
			buffer.putInt(priceForAll);
			buffer.putInt(priceForCurrent);
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
	 *	各个装备的id
	 */
	public long[] getEquipmentIds(){
		return equipmentIds;
	}

	/**
	 * 设置属性：
	 *	各个装备的id
	 */
	public void setEquipmentIds(long[] equipmentIds){
		this.equipmentIds = equipmentIds;
	}

	/**
	 * 获取属性：
	 *	各个装备修理的价格
	 */
	public int[] getPrices(){
		return prices;
	}

	/**
	 * 设置属性：
	 *	各个装备修理的价格
	 */
	public void setPrices(int[] prices){
		this.prices = prices;
	}

	/**
	 * 获取属性：
	 *	所有装备的修理价格
	 */
	public int getPriceForAll(){
		return priceForAll;
	}

	/**
	 * 设置属性：
	 *	所有装备的修理价格
	 */
	public void setPriceForAll(int priceForAll){
		this.priceForAll = priceForAll;
	}

	/**
	 * 获取属性：
	 *	当前装备在身上的装备修理价格
	 */
	public int getPriceForCurrent(){
		return priceForCurrent;
	}

	/**
	 * 设置属性：
	 *	当前装备在身上的装备修理价格
	 */
	public void setPriceForCurrent(int priceForCurrent){
		this.priceForCurrent = priceForCurrent;
	}

}