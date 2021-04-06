package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 选择出售物品,追售<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>failreason.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>failreason</td><td>String</td><td>failreason.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>bagindex</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>index</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>entityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>num</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>perPrice</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class BOOTHSALE_SELECT_FOR_SALE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int result;
	String failreason;
	int bagindex;
	int index;
	long entityId;
	int num;
	long perPrice;

	public BOOTHSALE_SELECT_FOR_SALE_RES(){
	}

	public BOOTHSALE_SELECT_FOR_SALE_RES(long seqNum,int result,String failreason,int bagindex,int index,long entityId,int num,long perPrice){
		this.seqNum = seqNum;
		this.result = result;
		this.failreason = failreason;
		this.bagindex = bagindex;
		this.index = index;
		this.entityId = entityId;
		this.num = num;
		this.perPrice = perPrice;
	}

	public BOOTHSALE_SELECT_FOR_SALE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		result = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		failreason = new String(content,offset,len,"UTF-8");
		offset += len;
		bagindex = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		index = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		entityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		num = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		perPrice = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
	}

	public int getType() {
		return 0x70F00002;
	}

	public String getTypeDescription() {
		return "BOOTHSALE_SELECT_FOR_SALE_RES";
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
		len += 2;
		try{
			len +=failreason.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		len += 4;
		len += 8;
		len += 4;
		len += 8;
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

			buffer.putInt(result);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = failreason.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(bagindex);
			buffer.putInt(index);
			buffer.putLong(entityId);
			buffer.putInt(num);
			buffer.putLong(perPrice);
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
	 *	0-成功 1-失败
	 */
	public int getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	0-成功 1-失败
	 */
	public void setResult(int result){
		this.result = result;
	}

	/**
	 * 获取属性：
	 *	失败的结果描述
	 */
	public String getFailreason(){
		return failreason;
	}

	/**
	 * 设置属性：
	 *	失败的结果描述
	 */
	public void setFailreason(String failreason){
		this.failreason = failreason;
	}

	/**
	 * 获取属性：
	 *	在背包中的位置
	 */
	public int getBagindex(){
		return bagindex;
	}

	/**
	 * 设置属性：
	 *	在背包中的位置
	 */
	public void setBagindex(int bagindex){
		this.bagindex = bagindex;
	}

	/**
	 * 获取属性：
	 *	新卖东西的索引
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 设置属性：
	 *	新卖东西的索引
	 */
	public void setIndex(int index){
		this.index = index;
	}

	/**
	 * 获取属性：
	 *	新卖东西的物品ID
	 */
	public long getEntityId(){
		return entityId;
	}

	/**
	 * 设置属性：
	 *	新卖东西的物品ID
	 */
	public void setEntityId(long entityId){
		this.entityId = entityId;
	}

	/**
	 * 获取属性：
	 *	新卖东西的数量
	 */
	public int getNum(){
		return num;
	}

	/**
	 * 设置属性：
	 *	新卖东西的数量
	 */
	public void setNum(int num){
		this.num = num;
	}

	/**
	 * 获取属性：
	 *	新卖东西的单价
	 */
	public long getPerPrice(){
		return perPrice;
	}

	/**
	 * 设置属性：
	 *	新卖东西的单价
	 */
	public void setPerPrice(long perPrice){
		this.perPrice = perPrice;
	}

}