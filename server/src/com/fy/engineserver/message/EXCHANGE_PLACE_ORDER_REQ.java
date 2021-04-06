package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送指令下单,下单成功，会收到响应，下单不成功，服务器会通过HINT_REQ指令提示错误，不发送响应<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>price</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>amount</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EXCHANGE_PLACE_ORDER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte orderType;
	int price;
	int amount;

	public EXCHANGE_PLACE_ORDER_REQ(long seqNum,byte orderType,int price,int amount){
		this.seqNum = seqNum;
		this.orderType = orderType;
		this.price = price;
		this.amount = amount;
	}

	public EXCHANGE_PLACE_ORDER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		orderType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		price = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		amount = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F0EF11;
	}

	public String getTypeDescription() {
		return "EXCHANGE_PLACE_ORDER_REQ";
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
		len += 4;
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put(orderType);
			buffer.putInt(price);
			buffer.putInt(amount);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	0标识买单，1标识卖单
	 */
	public byte getOrderType(){
		return orderType;
	}

	/**
	 * 设置属性：
	 *	0标识买单，1标识卖单
	 */
	public void setOrderType(byte orderType){
		this.orderType = orderType;
	}

	/**
	 * 获取属性：
	 *	单价
	 */
	public int getPrice(){
		return price;
	}

	/**
	 * 设置属性：
	 *	单价
	 */
	public void setPrice(int price){
		this.price = price;
	}

	/**
	 * 获取属性：
	 *	数量
	 */
	public int getAmount(){
		return amount;
	}

	/**
	 * 设置属性：
	 *	数量
	 */
	public void setAmount(int amount){
		this.amount = amount;
	}

}
