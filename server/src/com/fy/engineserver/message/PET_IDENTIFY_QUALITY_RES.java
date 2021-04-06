package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 宠物鉴定<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>petEntityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>quality</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>growthClass</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class PET_IDENTIFY_QUALITY_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	long petEntityId;
	String result;
	int quality;
	int growthClass;

	public PET_IDENTIFY_QUALITY_RES(){
	}

	public PET_IDENTIFY_QUALITY_RES(long seqNum,long petEntityId,String result,int quality,int growthClass){
		this.seqNum = seqNum;
		this.petEntityId = petEntityId;
		this.result = result;
		this.quality = quality;
		this.growthClass = growthClass;
	}

	public PET_IDENTIFY_QUALITY_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		petEntityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
		quality = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		growthClass = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x7000FF21;
	}

	public String getTypeDescription() {
		return "PET_IDENTIFY_QUALITY_RES";
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
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
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

			buffer.putLong(petEntityId);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = result.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(quality);
			buffer.putInt(growthClass);
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
	 *	所要鉴定的宠物物品id
	 */
	public long getPetEntityId(){
		return petEntityId;
	}

	/**
	 * 设置属性：
	 *	所要鉴定的宠物物品id
	 */
	public void setPetEntityId(long petEntityId){
		this.petEntityId = petEntityId;
	}

	/**
	 * 获取属性：
	 *	空为成功，否则为错误信息
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	空为成功，否则为错误信息
	 */
	public void setResult(String result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	鉴定后的品质
	 */
	public int getQuality(){
		return quality;
	}

	/**
	 * 设置属性：
	 *	鉴定后的品质
	 */
	public void setQuality(int quality){
		this.quality = quality;
	}

	/**
	 * 获取属性：
	 *	鉴定后的成长品质
	 */
	public int getGrowthClass(){
		return growthClass;
	}

	/**
	 * 设置属性：
	 *	鉴定后的成长品质
	 */
	public void setGrowthClass(int growthClass){
		this.growthClass = growthClass;
	}

}