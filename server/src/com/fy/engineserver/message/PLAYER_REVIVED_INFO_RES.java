package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 玩家复活信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>chengfaCD</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currentReviCostYinzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>reviCostBasicYinzi</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>reviCount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PLAYER_REVIVED_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long chengfaCD;
	int currentReviCostYinzi;
	int reviCostBasicYinzi;
	int reviCount;

	public PLAYER_REVIVED_INFO_RES(){
	}

	public PLAYER_REVIVED_INFO_RES(long seqNum,long chengfaCD,int currentReviCostYinzi,int reviCostBasicYinzi,int reviCount){
		this.seqNum = seqNum;
		this.chengfaCD = chengfaCD;
		this.currentReviCostYinzi = currentReviCostYinzi;
		this.reviCostBasicYinzi = reviCostBasicYinzi;
		this.reviCount = reviCount;
	}

	public PLAYER_REVIVED_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		chengfaCD = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		currentReviCostYinzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		reviCostBasicYinzi = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		reviCount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000EAF2;
	}

	public String getTypeDescription() {
		return "PLAYER_REVIVED_INFO_RES";
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

			buffer.putLong(chengfaCD);
			buffer.putInt(currentReviCostYinzi);
			buffer.putInt(reviCostBasicYinzi);
			buffer.putInt(reviCount);
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
	 *	惩罚CD
	 */
	public long getChengfaCD(){
		return chengfaCD;
	}

	/**
	 * 设置属性：
	 *	惩罚CD
	 */
	public void setChengfaCD(long chengfaCD){
		this.chengfaCD = chengfaCD;
	}

	/**
	 * 获取属性：
	 *	当前复活需要的绑银
	 */
	public int getCurrentReviCostYinzi(){
		return currentReviCostYinzi;
	}

	/**
	 * 设置属性：
	 *	当前复活需要的绑银
	 */
	public void setCurrentReviCostYinzi(int currentReviCostYinzi){
		this.currentReviCostYinzi = currentReviCostYinzi;
	}

	/**
	 * 获取属性：
	 *	复活所需要的基本银子
	 */
	public int getReviCostBasicYinzi(){
		return reviCostBasicYinzi;
	}

	/**
	 * 设置属性：
	 *	复活所需要的基本银子
	 */
	public void setReviCostBasicYinzi(int reviCostBasicYinzi){
		this.reviCostBasicYinzi = reviCostBasicYinzi;
	}

	/**
	 * 获取属性：
	 *	当前时间段复活次数
	 */
	public int getReviCount(){
		return reviCount;
	}

	/**
	 * 设置属性：
	 *	当前时间段复活次数
	 */
	public void setReviCount(int reviCount){
		this.reviCount = reviCount;
	}

}