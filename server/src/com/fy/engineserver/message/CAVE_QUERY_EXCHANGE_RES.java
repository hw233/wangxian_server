package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.homestead.cave.ResourceCollection;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 查看兑换资源数量<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>responseIds.length</td><td>int</td><td>4</td><td>数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>responseIds</td><td>long[]</td><td>responseIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resources.length</td><td>int</td><td>4个字节</td><td>ResourceCollection数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resources[0].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resources[0].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resources[0].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resources[1].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resources[1].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resources[1].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resources[2].food</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>resources[2].wood</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>resources[2].stone</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class CAVE_QUERY_EXCHANGE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long[] responseIds;
	ResourceCollection[] resources;

	public CAVE_QUERY_EXCHANGE_RES(){
	}

	public CAVE_QUERY_EXCHANGE_RES(long seqNum,long[] responseIds,ResourceCollection[] resources){
		this.seqNum = seqNum;
		this.responseIds = responseIds;
		this.resources = resources;
	}

	public CAVE_QUERY_EXCHANGE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
		responseIds = new long[len];
		for(int i = 0 ; i < responseIds.length ; i++){
			responseIds[i] = (long)mf.byteArrayToNumber(content,offset,8);
			offset += 8;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		resources = new ResourceCollection[len];
		for(int i = 0 ; i < resources.length ; i++){
			resources[i] = new ResourceCollection();
			resources[i].setFood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resources[i].setWood((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			resources[i].setStone((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x8F00002A;
	}

	public String getTypeDescription() {
		return "CAVE_QUERY_EXCHANGE_RES";
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
		len += responseIds.length * 8;
		len += 4;
		for(int i = 0 ; i < resources.length ; i++){
			len += 4;
			len += 4;
			len += 4;
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

			buffer.putInt(responseIds.length);
			for(int i = 0 ; i < responseIds.length; i++){
				buffer.putLong(responseIds[i]);
			}
			buffer.putInt(resources.length);
			for(int i = 0 ; i < resources.length ; i++){
				buffer.putInt((int)resources[i].getFood());
				buffer.putInt((int)resources[i].getWood());
				buffer.putInt((int)resources[i].getStone());
			}
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
	 *	物品ID
	 */
	public long[] getResponseIds(){
		return responseIds;
	}

	/**
	 * 设置属性：
	 *	物品ID
	 */
	public void setResponseIds(long[] responseIds){
		this.responseIds = responseIds;
	}

	/**
	 * 获取属性：
	 *	种植成本
	 */
	public ResourceCollection[] getResources(){
		return resources;
	}

	/**
	 * 设置属性：
	 *	种植成本
	 */
	public void setResources(ResourceCollection[] resources){
		this.resources = resources;
	}

}