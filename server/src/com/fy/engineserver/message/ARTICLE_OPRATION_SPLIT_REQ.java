package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发起的对背包中物品进行拆分<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>bagIndex</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>fangbaotype</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>splitOutCount</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>putToType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>putToIndex</td><td>short</td><td>2个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class ARTICLE_OPRATION_SPLIT_REQ implements RequestMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	short bagIndex;
	byte fangbaotype;
	short index;
	short splitOutCount;
	byte putToType;
	short putToIndex;

	public ARTICLE_OPRATION_SPLIT_REQ(long seqNum,short bagIndex,byte fangbaotype,short index,short splitOutCount,byte putToType,short putToIndex){
		this.seqNum = seqNum;
		this.bagIndex = bagIndex;
		this.fangbaotype = fangbaotype;
		this.index = index;
		this.splitOutCount = splitOutCount;
		this.putToType = putToType;
		this.putToIndex = putToIndex;
	}

	public ARTICLE_OPRATION_SPLIT_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		bagIndex = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		fangbaotype = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		index = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		splitOutCount = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		putToType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		putToIndex = (short)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
	}

	public int getType() {
		return 0x000002F6;
	}

	public String getTypeDescription() {
		return "ARTICLE_OPRATION_SPLIT_REQ";
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
		len += 1;
		len += 2;
		len += 2;
		len += 1;
		len += 2;
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

			buffer.putShort(bagIndex);
			buffer.put(fangbaotype);
			buffer.putShort(index);
			buffer.putShort(splitOutCount);
			buffer.put(putToType);
			buffer.putShort(putToIndex);
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	物品在哪个背包：包类型
	 */
	public short getBagIndex(){
		return bagIndex;
	}

	/**
	 * 设置属性：
	 *	物品在哪个背包：包类型
	 */
	public void setBagIndex(short bagIndex){
		this.bagIndex = bagIndex;
	}

	/**
	 * 获取属性：
	 *	0:一般包  1:相应类型的防爆包
	 */
	public byte getFangbaotype(){
		return fangbaotype;
	}

	/**
	 * 设置属性：
	 *	0:一般包  1:相应类型的防爆包
	 */
	public void setFangbaotype(byte fangbaotype){
		this.fangbaotype = fangbaotype;
	}

	/**
	 * 获取属性：
	 *	物品在背包中所占格子的索引
	 */
	public short getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	物品在背包中所占格子的索引
	 */
	public void setIndex(short index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	拆分几个出来
	 */
	public short getSplitOutCount(){
		return splitOutCount;
	}

	/**
	 * 设置属性：
	 *	拆分几个出来
	 */
	public void setSplitOutCount(short splitOutCount){
		this.splitOutCount = splitOutCount;
	}

	/**
	 * 获取属性：
	 *	0:一般包  1:相应类型的防爆包,-1服务器自己决定拆分到哪里
	 */
	public byte getPutToType(){
		return putToType;
	}

	/**
	 * 设置属性：
	 *	0:一般包  1:相应类型的防爆包,-1服务器自己决定拆分到哪里
	 */
	public void setPutToType(byte putToType){
		this.putToType = putToType;
	}

	/**
	 * 获取属性：
	 *	物品在相应背包中所占格子的索引
	 */
	public short getPutToIndex(){
		return putToIndex;
	}

	/**
	 * 设置属性：
	 *	物品在相应背包中所占格子的索引
	 */
	public void setPutToIndex(short putToIndex){
		this.putToIndex = putToIndex;
	}

}
