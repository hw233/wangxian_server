package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 通知玩家此频道是否能发言，是否有道具<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sort</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hasProps</td><td>boolean</td><td>1个字节</td><td>布尔型长度,0==false，其他为true</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>price.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>price</td><td>String</td><td>price.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class FEE_CHAT_ALLOW_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte sort;
	boolean hasProps;
	String price;

	public FEE_CHAT_ALLOW_REQ(){
	}

	public FEE_CHAT_ALLOW_REQ(long seqNum,byte sort,boolean hasProps,String price){
		this.seqNum = seqNum;
		this.sort = sort;
		this.hasProps = hasProps;
		this.price = price;
	}

	public FEE_CHAT_ALLOW_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		sort = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		hasProps = mf.byteArrayToNumber(content,offset,1) != 0;
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		price = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x00000E03;
	}

	public String getTypeDescription() {
		return "FEE_CHAT_ALLOW_REQ";
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
		len += 1;
		len += 2;
		try{
			len +=price.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.put(sort);
			buffer.put((byte)(hasProps==false?0:1));
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = price.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
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
	 *	属于哪个频道
	 */
	public byte getSort(){
		return sort;
	}

	/**
	 * 设置属性：
	 *	属于哪个频道
	 */
	public void setSort(byte sort){
		this.sort = sort;
	}

	/**
	 * 获取属性：
	 *	是否有喊话道具
	 */
	public boolean getHasProps(){
		return hasProps;
	}

	/**
	 * 设置属性：
	 *	是否有喊话道具
	 */
	public void setHasProps(boolean hasProps){
		this.hasProps = hasProps;
	}

	/**
	 * 获取属性：
	 *	喊话道具的价格,包含了量词，xx元宝
	 */
	public String getPrice(){
		return price;
	}

	/**
	 * 设置属性：
	 *	喊话道具的价格,包含了量词，xx元宝
	 */
	public void setPrice(String price){
		this.price = price;
	}

}