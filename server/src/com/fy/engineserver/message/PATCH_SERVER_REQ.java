package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 服务器向客户端发送资源数据<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>offsetIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>packetIndex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>data.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>data</td><td>byte[]</td><td>data.length</td><td>数组实际长度</td></tr>
 * </table>
 */
public class PATCH_SERVER_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int offsetIndex;
	int packetIndex;
	byte[] data;

	public PATCH_SERVER_REQ(long seqNum,int offsetIndex,int packetIndex,byte[] data){
		this.seqNum = seqNum;
		this.offsetIndex = offsetIndex;
		this.packetIndex = packetIndex;
		this.data = data;
	}

	public PATCH_SERVER_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		offsetIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		packetIndex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		data = new byte[len];
		System.arraycopy(content,offset,data,0,len);
		offset += len;
	}

	public int getType() {
		return 0x00000F24;
	}

	public String getTypeDescription() {
		return "PATCH_SERVER_REQ";
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
		len += 4;
		len += data.length;
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

			buffer.putInt(offsetIndex);
			buffer.putInt(packetIndex);
			buffer.putInt(data.length);
			buffer.put(data);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	包中的数据在整个补丁数据中的偏移量，从0开始
	 */
	public int getOffsetIndex(){
		return offsetIndex;
	}

	/**
	 * 设置属性：
	 *	包中的数据在整个补丁数据中的偏移量，从0开始
	 */
	public void setOffsetIndex(int offsetIndex){
		this.offsetIndex = offsetIndex;
	}

	/**
	 * 获取属性：
	 *	包的编号，从0开始到packetNum-1
	 */
	public int getPacketIndex(){
		return packetIndex;
	}

	/**
	 * 设置属性：
	 *	包的编号，从0开始到packetNum-1
	 */
	public void setPacketIndex(int packetIndex){
		this.packetIndex = packetIndex;
	}

	/**
	 * 获取属性：
	 *	数据
	 */
	public byte[] getData(){
		return data;
	}

	/**
	 * 设置属性：
	 *	数据
	 */
	public void setData(byte[] data){
		this.data = data;
	}

}
