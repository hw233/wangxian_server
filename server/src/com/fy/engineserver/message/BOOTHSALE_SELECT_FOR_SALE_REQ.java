package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 选择出售物品,追售<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>saleNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>perPrice</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOOTHSALE_SELECT_FOR_SALE_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int index;
	int saleNum;
	long perPrice;

	public BOOTHSALE_SELECT_FOR_SALE_REQ(){
	}

	public BOOTHSALE_SELECT_FOR_SALE_REQ(long seqNum,int index,int saleNum,long perPrice){
		this.seqNum = seqNum;
		this.index = index;
		this.saleNum = saleNum;
		this.perPrice = perPrice;
	}

	public BOOTHSALE_SELECT_FOR_SALE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		saleNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		perPrice = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x00F00002;
	}

	public String getTypeDescription() {
		return "BOOTHSALE_SELECT_FOR_SALE_REQ";
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
		len += 4;
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

			buffer.putInt(index);
			buffer.putInt(saleNum);
			buffer.putLong(perPrice);
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
	 *	在背包中的位置
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	在背包中的位置
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	出售个数
	 */
	public int getSaleNum(){
		return saleNum;
	}

	/**
	 * 设置属性：
	 *	出售个数
	 */
	public void setSaleNum(int saleNum){
		this.saleNum = saleNum;
	}

	/**
	 * 获取属性：
	 *	物品单价
	 */
	public long getPerPrice(){
		return perPrice;
	}

	/**
	 * 设置属性：
	 *	物品单价
	 */
	public void setPerPrice(long perPrice){
		this.perPrice = perPrice;
	}

}