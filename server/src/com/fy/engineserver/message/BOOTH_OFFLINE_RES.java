package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 离线摆摊<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>pid</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buyDays.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buyDays</td><td>int[]</td><td>buyDays.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>costMoney.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>costMoney</td><td>long[]</td><td>costMoney.length</td><td>*</td></tr>
 * </table>
 */
public class BOOTH_OFFLINE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long pid;
	int[] buyDays;
	long[] costMoney;

	public BOOTH_OFFLINE_RES(){
	}

	public BOOTH_OFFLINE_RES(long seqNum,long pid,int[] buyDays,long[] costMoney){
		this.seqNum = seqNum;
		this.pid = pid;
		this.buyDays = buyDays;
		this.costMoney = costMoney;
	}

	public BOOTH_OFFLINE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		pid = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		buyDays = new int[len];
		for(int i = 0 ; i < buyDays.length ; i++){
			buyDays[i] = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		costMoney = new long[len];
		for(int i = 0 ; i < costMoney.length ; i++){
			costMoney[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
	}

	public int getType() {
		return 0x70FFF111;
	}

	public String getTypeDescription() {
		return "BOOTH_OFFLINE_RES";
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
		len += buyDays.length * 4;
		len += 4;
		len += costMoney.length * 8;
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

			buffer.putLong(pid);
			buffer.putInt(buyDays.length);
			for(int i = 0 ; i < buyDays.length; i++){
				buffer.putInt(buyDays[i]);
			}
			buffer.putInt(costMoney.length);
			for(int i = 0 ; i < costMoney.length; i++){
				buffer.putLong(costMoney[i]);
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
	 *	角色id
	 */
	public long getPid(){
		return pid;
	}

	/**
	 * 设置属性：
	 *	角色id
	 */
	public void setPid(long pid){
		this.pid = pid;
	}

	/**
	 * 获取属性：
	 *	购买天数
	 */
	public int[] getBuyDays(){
		return buyDays;
	}

	/**
	 * 设置属性：
	 *	购买天数
	 */
	public void setBuyDays(int[] buyDays){
		this.buyDays = buyDays;
	}

	/**
	 * 获取属性：
	 *	消耗的钱,单位文
	 */
	public long[] getCostMoney(){
		return costMoney;
	}

	/**
	 * 设置属性：
	 *	消耗的钱,单位文
	 */
	public void setCostMoney(long[] costMoney){
		this.costMoney = costMoney;
	}

}