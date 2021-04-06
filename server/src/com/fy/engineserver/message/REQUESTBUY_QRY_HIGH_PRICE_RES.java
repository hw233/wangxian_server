package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.trade.requestbuy.RequestBuyInfo4Client;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 试图出售物品(查看最高价)<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client.requestBuyId</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>requestBuyInfo4Client.perPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>requestBuyInfo4Client.leftNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class REQUESTBUY_QRY_HIGH_PRICE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	RequestBuyInfo4Client requestBuyInfo4Client;

	public REQUESTBUY_QRY_HIGH_PRICE_RES(){
	}

	public REQUESTBUY_QRY_HIGH_PRICE_RES(long seqNum,RequestBuyInfo4Client requestBuyInfo4Client){
		this.seqNum = seqNum;
		this.requestBuyInfo4Client = requestBuyInfo4Client;
	}

	public REQUESTBUY_QRY_HIGH_PRICE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		requestBuyInfo4Client = new RequestBuyInfo4Client();
		requestBuyInfo4Client.setRequestBuyId((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		requestBuyInfo4Client.setPerPrice((long)mf.byteArrayToNumber(content,offset,8));
		offset += 8;
		requestBuyInfo4Client.setLeftNum((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x70F00105;
	}

	public String getTypeDescription() {
		return "REQUESTBUY_QRY_HIGH_PRICE_RES";
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
		len += 8;
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

			buffer.putLong(requestBuyInfo4Client.getRequestBuyId());
			buffer.putLong(requestBuyInfo4Client.getPerPrice());
			buffer.putInt((int)requestBuyInfo4Client.getLeftNum());
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
	 *	求购信息
	 */
	public RequestBuyInfo4Client getRequestBuyInfo4Client(){
		return requestBuyInfo4Client;
	}

	/**
	 * 设置属性：
	 *	求购信息
	 */
	public void setRequestBuyInfo4Client(RequestBuyInfo4Client requestBuyInfo4Client){
		this.requestBuyInfo4Client = requestBuyInfo4Client;
	}

}