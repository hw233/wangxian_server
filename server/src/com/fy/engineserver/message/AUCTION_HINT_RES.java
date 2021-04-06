package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器通知客户端，拍卖行操作结果，mode: 0创建成功,1-撤销成功,2-竞拍成功,3-一口价购得 , 失败信息,hintContent不空表示失败<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>mode</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>hintContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>hintContent</td><td>String</td><td>hintContent.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class AUCTION_HINT_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte mode;
	String hintContent;

	public AUCTION_HINT_RES(long seqNum,byte mode,String hintContent){
		this.seqNum = seqNum;
		this.mode = mode;
		this.hintContent = hintContent;
	}

	public AUCTION_HINT_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		mode = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		hintContent = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x8000EF16;
	}

	public String getTypeDescription() {
		return "AUCTION_HINT_RES";
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
		len += 2;
		try{
			len +=hintContent.getBytes("UTF-8").length;
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
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.put(mode);
			byte[] tmpBytes1 = hintContent.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	拍卖行操作结果，mode: 0-创建成功,1-撤销成功,2-竞拍成功,3-一口价成功
	 */
	public byte getMode(){
		return mode;
	}

	/**
	 * 设置属性：
	 *	拍卖行操作结果，mode: 0-创建成功,1-撤销成功,2-竞拍成功,3-一口价成功
	 */
	public void setMode(byte mode){
		this.mode = mode;
	}

	/**
	 * 获取属性：
	 *	失败信息,hintContent不空表示失败
	 */
	public String getHintContent(){
		return hintContent;
	}

	/**
	 * 设置属性：
	 *	失败信息,hintContent不空表示失败
	 */
	public void setHintContent(String hintContent){
		this.hintContent = hintContent;
	}

}
