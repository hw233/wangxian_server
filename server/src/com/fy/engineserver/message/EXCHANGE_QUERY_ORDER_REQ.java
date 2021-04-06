package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端查询用户的订单信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>orderType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>start</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EXCHANGE_QUERY_ORDER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte orderType;
	int start;
	int num;

	public EXCHANGE_QUERY_ORDER_REQ(long seqNum,byte orderType,int start,int num){
		this.seqNum = seqNum;
		this.orderType = orderType;
		this.start = start;
		this.num = num;
	}

	public EXCHANGE_QUERY_ORDER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		orderType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		start = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00F0EF13;
	}

	public String getTypeDescription() {
		return "EXCHANGE_QUERY_ORDER_REQ";
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
			buffer.putInt(start);
			buffer.putInt(num);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	0标识所有订单，1标识处于激活状态的订单，2标识已经完成交易的订单,3-所有的在激活状态下的订单[全部完成交易,取消]的除外
	 */
	public byte getOrderType(){
		return orderType;
	}

	/**
	 * 设置属性：
	 *	0标识所有订单，1标识处于激活状态的订单，2标识已经完成交易的订单,3-所有的在激活状态下的订单[全部完成交易,取消]的除外
	 */
	public void setOrderType(byte orderType){
		this.orderType = orderType;
	}

	/**
	 * 获取属性：
	 *	开始的编号
	 */
	public int getStart(){
		return start;
	}

	/**
	 * 设置属性：
	 *	开始的编号
	 */
	public void setStart(int start){
		this.start = start;
	}

	/**
	 * 获取属性：
	 *	取出多少个，0标识取出所有的
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	取出多少个，0标识取出所有的
	 */
	public void setNum(int num){
		this.num = num;
	}

}
