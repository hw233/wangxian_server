package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 出售商品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>shopName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>shopName</td><td>String</td><td>shopName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>fangbaoFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>knapsackIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class SHOP_SELL_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	String shopName;
	boolean fangbaoFlag;
	int knapsackIndex;

	public SHOP_SELL_REQ(){
	}

	public SHOP_SELL_REQ(long seqNum,String shopName,boolean fangbaoFlag,int knapsackIndex){
		this.seqNum = seqNum;
		this.shopName = shopName;
		this.fangbaoFlag = fangbaoFlag;
		this.knapsackIndex = knapsackIndex;
	}

	public SHOP_SELL_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		shopName = new String(content,offset,len,"UTF-8");
		offset += len;
		fangbaoFlag = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		knapsackIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x0000EA03;
	}

	public String getTypeDescription() {
		return "SHOP_SELL_REQ";
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
		len += 2;
		try{
			len +=shopName.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 1;
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

			byte[] tmpBytes1;
				try{
			 tmpBytes1 = shopName.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.put((byte)(fangbaoFlag==false?0:1));
			buffer.putInt(knapsackIndex);
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
	 *	商店名称
	 */
	public String getShopName(){
		return shopName;
	}

	/**
	 * 设置属性：
	 *	商店名称
	 */
	public void setShopName(String shopName){
		this.shopName = shopName;
	}

	/**
	 * 获取属性：
	 *	是不是防爆包
	 */
	public boolean getFangbaoFlag(){
		return fangbaoFlag;
	}

	/**
	 * 设置属性：
	 *	是不是防爆包
	 */
	public void setFangbaoFlag(boolean fangbaoFlag){
		this.fangbaoFlag = fangbaoFlag;
	}

	/**
	 * 获取属性：
	 *	出售的物品对应玩家背包的下标
	 */
	public int getKnapsackIndex(){
		return knapsackIndex;
	}

	/**
	 * 设置属性：
	 *	出售的物品对应玩家背包的下标
	 */
	public void setKnapsackIndex(int knapsackIndex){
		this.knapsackIndex = knapsackIndex;
	}

}