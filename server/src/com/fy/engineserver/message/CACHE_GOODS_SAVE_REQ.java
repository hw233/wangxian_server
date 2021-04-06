package com.fy.engineserver.message;

import java.nio.ByteBuffer;

import com.xuanzhi.tools.transport.RequestMessage;



/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 保存一个商品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntityId</td><td>long</td><td>8个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>buyPrice</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>sellPrice</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * </table>
 */
public class CACHE_GOODS_SAVE_REQ implements RequestMessage{

	static CacheSystemMessageFactory mf = CacheSystemMessageFactory.getInstance();

	long seqNum;
	long id;
	long articleEntityId;
	int buyPrice;
	int sellPrice;

	public CACHE_GOODS_SAVE_REQ(long seqNum,long id,long articleEntityId,int buyPrice,int sellPrice){
		this.seqNum = seqNum;
		this.id = id;
		this.articleEntityId = articleEntityId;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
	}

	public CACHE_GOODS_SAVE_REQ(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		id = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		articleEntityId = (long)mf.byteArrayToNumber(content,offset,8);
		offset += 8;
		buyPrice = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		sellPrice = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
	}

	public int getType() {
		return 0x00000023;
	}

	public String getTypeDescription() {
		return "CACHE_GOODS_SAVE_REQ";
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
		len += 8;
		len += 4;
		len += 4;
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

			buffer.putLong(id);
			buffer.putLong(articleEntityId);
			buffer.putInt(buyPrice);
			buffer.putInt(sellPrice);
		}catch(Exception e){
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		return messageLength;
	}

	/**
	 * 获取属性：
	 *	商品Id
	 */
	public long getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	商品Id
	 */
	public void setId(long id){
		this.id = id;
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

}
