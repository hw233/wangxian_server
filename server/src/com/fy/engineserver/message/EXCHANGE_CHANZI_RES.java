package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 兑换铲子<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>exchangeType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>leftNum</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class EXCHANGE_CHANZI_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte exchangeType;
	int leftNum;

	public EXCHANGE_CHANZI_RES(){
	}

	public EXCHANGE_CHANZI_RES(long seqNum,byte exchangeType,int leftNum){
		this.seqNum = seqNum;
		this.exchangeType = exchangeType;
		this.leftNum = leftNum;
	}

	public EXCHANGE_CHANZI_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		exchangeType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		leftNum = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x70F0EF82;
	}

	public String getTypeDescription() {
		return "EXCHANGE_CHANZI_RES";
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

			buffer.put(exchangeType);
			buffer.putInt(leftNum);
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
	 *	0:银铲子   1:金铲子  2:琉璃铲子
	 */
	public byte getExchangeType(){
		return exchangeType;
	}

	/**
	 * 设置属性：
	 *	0:银铲子   1:金铲子  2:琉璃铲子
	 */
	public void setExchangeType(byte exchangeType){
		this.exchangeType = exchangeType;
	}

	/**
	 * 获取属性：
	 *	对应铲子剩余数量
	 */
	public int getLeftNum(){
		return leftNum;
	}

	/**
	 * 设置属性：
	 *	对应铲子剩余数量
	 */
	public void setLeftNum(int leftNum){
		this.leftNum = leftNum;
	}

}