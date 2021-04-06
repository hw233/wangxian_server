package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.ResponseMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 获取一个物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>buyPrice</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>sellPrice</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>result.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>result</td><td>String</td><td>result.length</td><td>字符串对应的byte数组</td></tr>
 * </table>
 */
public class CACHE_GOODS_GET_RES implements ResponseMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	long articleEntityId;
	int buyPrice;
	int sellPrice;
	String result;

	public CACHE_GOODS_GET_RES(long seqNum,long articleEntityId,int buyPrice,int sellPrice,String result){
		this.seqNum = seqNum;
		this.articleEntityId = articleEntityId;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.result = result;
	}

	public CACHE_GOODS_GET_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		articleEntityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buyPrice = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		sellPrice = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 1024000) throw new Exception("string length ["+len+"] big than the max length [1024000]");
		result = new String(content,offset,len,"UTF-8");
		offset += len;
	}

	public int getType() {
		return 0x80000025;
	}

	public String getTypeDescription() {
		return "CACHE_GOODS_GET_RES";
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
		len += 4;
		len += 4;
		len += 2;
		try{
			len +=result.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
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

			buffer.putLong(articleEntityId);
			buffer.putInt(buyPrice);
			buffer.putInt(sellPrice);
			byte[] tmpBytes1 = result.getBytes("UTF-8");
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	物品Id
	 */
	public long getArticleEntityId(){
		return articleEntityId;
	}

	/**
	 * 设置属性：
	 *	物品Id
	 */
	public void setArticleEntityId(long articleEntityId){
		this.articleEntityId = articleEntityId;
	}

	/**
	 * 获取属性：
	 *	收购价格
	 */
	public int getBuyPrice(){
		return buyPrice;
	}

	/**
	 * 设置属性：
	 *	收购价格
	 */
	public void setBuyPrice(int buyPrice){
		this.buyPrice = buyPrice;
	}

	/**
	 * 获取属性：
	 *	出售价格
	 */
	public int getSellPrice(){
		return sellPrice;
	}

	/**
	 * 设置属性：
	 *	出售价格
	 */
	public void setSellPrice(int sellPrice){
		this.sellPrice = sellPrice;
	}

	/**
	 * 获取属性：
	 *	结果说明
	 */
	public String getResult(){
		return result;
	}

	/**
	 * 设置属性：
	 *	结果说明
	 */
	public void setResult(String result){
		this.result = result;
	}

}
