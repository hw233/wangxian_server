package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.ResourceCollection;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获得仙府资源<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currCollections.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>currCollections.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>currCollections.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxCollections.food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>maxCollections.wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>maxCollections.stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * </table>
 */
public class CAVE_QUERY_RESOURCECOLLECTION_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ResourceCollection currCollections;
	ResourceCollection maxCollections;

	public CAVE_QUERY_RESOURCECOLLECTION_RES(){
	}

	public CAVE_QUERY_RESOURCECOLLECTION_RES(long seqNum,ResourceCollection currCollections,ResourceCollection maxCollections){
		this.seqNum = seqNum;
		this.currCollections = currCollections;
		this.maxCollections = maxCollections;
	}

	public CAVE_QUERY_RESOURCECOLLECTION_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		currCollections = new ResourceCollection();
		currCollections.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		currCollections.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		currCollections.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maxCollections = new ResourceCollection();
		maxCollections.setFood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maxCollections.setWood((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
		maxCollections.setStone((int)mf.byteArrayToNumber(content,offset,4));
		offset += 4;
	}

	public int getType() {
		return 0x8F000053;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_RESOURCECOLLECTION_RES";
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
		len += 4;
		len += 4;
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

			buffer.putInt((int)currCollections.getFood());
			buffer.putInt((int)currCollections.getWood());
			buffer.putInt((int)currCollections.getStone());
			buffer.putInt((int)maxCollections.getFood());
			buffer.putInt((int)maxCollections.getWood());
			buffer.putInt((int)maxCollections.getStone());
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
	 *	当前资源
	 */
	public ResourceCollection getCurrCollections(){
		return currCollections;
	}

	/**
	 * 设置属性：
	 *	当前资源
	 */
	public void setCurrCollections(ResourceCollection currCollections){
		this.currCollections = currCollections;
	}

	/**
	 * 获取属性：
	 *	资源上限
	 */
	public ResourceCollection getMaxCollections(){
		return maxCollections;
	}

	/**
	 * 设置属性：
	 *	资源上限
	 */
	public void setMaxCollections(ResourceCollection maxCollections){
		this.maxCollections = maxCollections;
	}

}